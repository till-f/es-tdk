package fzi.mottem.runtime;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.swt.widgets.Display;

import fzi.mottem.runtime.rtgraph.GraphShell;
import fzi.mottem.runtime.tracedb.util.NearestMatchLong;
import fzi.util.eclipse.IntegrationUtils;


/**
 accesses the profiler/trace data sqlite database and caches the sqlquery results in memory

 O
 |
 + Construction
 |
 + TraceControllers do their adds
 |     | 
 |  +->|
 |  |  | insertValueMS(...)
 |  +--+
 |
 + synchronizeToMemory
 |
 + analyze blocks are executed 
 |     | 
 |  +->|
 |  |  | injectValueMS(...)
 |  +--+
 |
 + synchronizeToHardStorage
 |
 + only read access is allowed
 |
 O
 
 
 The datastructure for the in memory cached results has the following (example) layout

 O
 |
 + Timestamp 1
      |
      + Function 1
 |
 + Timestamp 2
      |
      + Variable 1
      |
      + Injected Variable 1
      |
      + Injected Variable 2
 |
 + Timestamp 3
      |
      + Variable 2
 |
 O

 */
public class TraceDB 
{
	private class DBEntry
	{
		public TraceDBEvent Event;
		public String UID;
		public String Value;

		public DBEntry(TraceDBEvent event, String uid, String value)
		{
			Event = event;
			UID = uid;
			Value = value;
		}
	}
	
	public final static String TABLE_VALUES = "profileTable";
	public final static String COLUMN_VALUES_UID = "uid";
	public final static String COLUMN_VALUES_TIME = "time";
	public final static String COLUMN_VALUES_EVENT = "event";
	public final static String COLUMN_VALUES_VALUE = "value";
	
	public final static String TABLE_METAINFO = "metaInfo";
	public final static String COLUMN_METAINFO_UID = "uid";
	public final static String COLUMN_METAINFO_KEY = "key";
	public final static String COLUMN_METAINFO_VALUE = "value";
	
	public enum TraceDBEvent { Read, Write, Call, Return, Receive, Send, UNSPECIFIED };

	public static final Map<TraceDBEvent, Character> EventToCharMap;
	public static final Map<Character, TraceDBEvent> CharToEventMap;
	
	static
	{
		Map<TraceDBEvent, Character> e2c = new HashMap<TraceDBEvent, Character>();
		e2c.put(TraceDBEvent.Read, 'R');
		e2c.put(TraceDBEvent.Write, 'W');
		e2c.put(TraceDBEvent.Call, 'C');
		e2c.put(TraceDBEvent.Return, 'X');
		e2c.put(TraceDBEvent.Receive, 'I');
		e2c.put(TraceDBEvent.Send, 'O');
		e2c.put(TraceDBEvent.UNSPECIFIED, '-');
		EventToCharMap = Collections.unmodifiableMap(e2c);
		
		Map<Character, TraceDBEvent> c2e = new HashMap<Character, TraceDBEvent>();
		for (TraceDBEvent event : EventToCharMap.keySet())
		{
			c2e.put(EventToCharMap.get(event), event);
		}
		CharToEventMap = Collections.unmodifiableMap(c2e);
	}
	
	private long _startTime = 0;

	private final TreeMap<Double, List<DBEntry>> _inMemoryDB = new TreeMap<Double,List<DBEntry>>();
	private final Connection _connection;
	private final PreparedStatement _preparedInsertValueQuery;
	private final PreparedStatement _preparedInsertMetaDatsaQuery;
	private final IFile _dbFile;
	private final IFile _plotImageFile;
	private final String _plotName;

	private boolean _isSynchronizedToMemory = false;
	private boolean _isSynchronizedToHardStorage = false;

	public TraceDB(IFile dbFile, IFile plotImageFile, String plotName)
	{
		_dbFile = dbFile;
		_plotImageFile = plotImageFile;
		_plotName = plotName;
		
		try
		{
			IPath osPath = IntegrationUtils.getSystemPathForWorkspaceRelativePath(_dbFile.getFullPath());
			String jdbcURL = "jdbc:sqlite:" + osPath.toOSString();
			
			_connection = DriverManager.getConnection(jdbcURL);
			_connection.setAutoCommit(false);
			Statement statement = _connection.createStatement();
			statement.setQueryTimeout(30);
			statement.executeUpdate("drop table if exists " + TABLE_VALUES);
			statement.executeUpdate("drop table if exists " + TABLE_METAINFO);
			statement.executeUpdate("create table " + TABLE_VALUES + " (" + COLUMN_VALUES_TIME + " DOUBLE, " + COLUMN_VALUES_EVENT + " STRING, " + COLUMN_VALUES_UID + " STRING, " + COLUMN_VALUES_VALUE + " BLOB)");
			statement.executeUpdate("create table " + TABLE_METAINFO + " (" + COLUMN_METAINFO_UID + " INTEGER, " + COLUMN_METAINFO_KEY + " STRING, " + COLUMN_METAINFO_VALUE + " STRING)");
			_connection.commit();
			
			_preparedInsertValueQuery     = _connection.prepareStatement("INSERT INTO " + TABLE_VALUES + " VALUES (?,?,?,?)");
			_preparedInsertMetaDatsaQuery = _connection.prepareStatement("INSERT INTO " + TABLE_METAINFO + " VALUES (?,?,?)");
		}
		catch (SQLException e)
		{
			throw new RuntimeException(e);
		}
	}
	
	public IFile getPlotImageFile()
	{
		return _plotImageFile;
	}
	
	public String getPlotName()
	{
		return _plotName;
	}
	
	public void setStartTime(long startTime)
	{
		_startTime = startTime;
	}
	
	/**
	 * inserts data into DB.
	 * This function (or the corresponding version without time argument below)
	 * must be used by all ITraceControllers.
	 * 
	 * This function is not meant to be called during tracing! Trace Controllers
	 * should cache trace data using a private storage. (this is currently not done...)
	 * 
	 * There is another interface for direct value injection that is used
	 * when the PTSpec code adds data in analyze blocks (see below)
	 */
	public void insertValueMS(Double time, TraceDBEvent event, String uid, String value)
	{
		if (_isSynchronizedToHardStorage)
			throw new RuntimeException("Unexpected insert to Trace DB after synchronized to hard storage");
		
		try
		{
			_preparedInsertValueQuery.setDouble(1, time);
			_preparedInsertValueQuery.setString(2, EventToCharMap.get(event).toString());
			_preparedInsertValueQuery.setString(3, uid);
			_preparedInsertValueQuery.setString(4, value);
			_preparedInsertValueQuery.addBatch();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	/**
	 * inserts data into DB using internal time base.
	 * This function (or the corresponding version with time argument above)
	 * must be used by all ITraceControllers.
	 * 
	 * Internal time might be less accurate than timestamps created by hardware and
	 * should only be used if the hardware does not support timestamps. 
	 * 
	 * !TODO: this function should cache data as adding prepared statements might be less
	 * performant...
	 */
	public void insertValue(TraceDBEvent event, String uid, String value)
	{
		if (_isSynchronizedToHardStorage)
			throw new RuntimeException("Unexpected insert to Trace DB after synchronized to hard storage");
		
		try
		{
			_preparedInsertValueQuery.setDouble(1, System.currentTimeMillis() - _startTime);
			_preparedInsertValueQuery.setString(2, EventToCharMap.get(event).toString());
			_preparedInsertValueQuery.setString(3, uid);
			_preparedInsertValueQuery.setString(4, value);
			_preparedInsertValueQuery.addBatch();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	
	/*
	 * Used by generated code during plot creation to add rendering information.
	 */
	public void insertMetaInfo(String uid, String key, String value)
	{
		if (_isSynchronizedToHardStorage)
			throw new RuntimeException("Unexpected insert to Trace DB after synchronized to hard storage");
		
		try
		{
			_preparedInsertMetaDatsaQuery.setString(1, uid);
			_preparedInsertMetaDatsaQuery.setString(2, key);
			_preparedInsertMetaDatsaQuery.setString(3, value);
			_preparedInsertMetaDatsaQuery.addBatch();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	
	/**
	 * This injects data into intermediate database AND into in-memory structure.
	 * Internal time reppresentation is nanoseconds
	 */
	@SuppressWarnings("serial")
	public void injectValueMS(Double time, String uid, String value)
	{
		if (_isSynchronizedToHardStorage)
			throw new RuntimeException("Unexpected inject to Trace DB after synchronized to hard storage");
		
		insertValueMS(time, TraceDBEvent.Write, uid, value);
		
		DBEntry dbEntry = new DBEntry(TraceDBEvent.Write, uid, value);

		if(!_inMemoryDB.containsKey(time))
		{
			_inMemoryDB.put(time, new LinkedList<DBEntry>()
				{{
					add(dbEntry);
				}});
		}
		else
		{
			List<DBEntry> list = _inMemoryDB.get(time);
			
			// try to find an existing injected value with the same name/UID
			for (DBEntry entry : list)
			{
				if(uid.equals(entry.UID))
				{
					entry.Value = value;
					return;
				}
			}
			
			// name/UID not found, insert a new HashMap instance at the given time
			list.add(dbEntry);
		}
	}

	/**
	 * Synchronizes database to in-memory structure.
	 * Must be called once after TraceControllers have completed their work.
	 * Must not be called twice as this would duplicate data!
	 */
	public void synchronizeToMemory()
	{
		if (_isSynchronizedToMemory)
			throw new RuntimeException("synchronizeToMemory() called multiple times");
		
		_isSynchronizedToMemory = true;
		
		// first execute all statements enqueued in batch as
		// TraceControllers have added data, then load internal
		// memory structure
		try
		{
			_preparedInsertValueQuery.executeBatch();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e);
		}

		loadToMemory();
	}
	
	/**
	 * Synchronizes database to in-memory structure.
	 * Must be called after all operations on trace have completed to preserve data.
	 * DB may not be write-accessed afterwards!
	 */
	public void synchronizeToHardStorage()
	{
		// first execute all statements enqueued in batch as there
		// might be injected values, then commit.
		try
		{
			_preparedInsertValueQuery.executeBatch();
			_preparedInsertMetaDatsaQuery.executeBatch();
			_connection.commit();
			_connection.close();
			_dbFile.refreshLocal(0, null);
			_isSynchronizedToHardStorage = true;
		}
		catch (SQLException | CoreException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	/**
	 *  searches backwards from the given time to the lowest time, returns the last written value of the given Variable
	 *  if no value has been found, it searches in the reverse direction (forward) to get the first value ever put inside the map
	 *  
	 * @param time 
	 * @param uid
	 * @return the value for the key VALUE is still hexadecimal (use Double.valueOf) or another structure for other datatypes
	 */
	public String getLastValue(Double time, String uid)
	{
		SortedMap<Double, List<DBEntry>> backwardsMap = _inMemoryDB.descendingMap().tailMap(time);
		String value = getValueOfFirstEntryInSet(backwardsMap.entrySet(), uid);
		
		if (value != null)
			return value;

		SortedMap<Double, List<DBEntry>> forwardsMap = _inMemoryDB.tailMap(time);
		value = getValueOfFirstEntryInSet(forwardsMap.entrySet(), uid);
		
		return value;
	}
	
	/**
	 * @return true if the current event matches the given variable or function name
	 *  		false otherwise or if the given time does not exist (is no key) in the internal TreeMap
	 */
	public boolean checkEvent(Double time, String uid, EEvent ptsEvent)
	{ 
		if(!_inMemoryDB.containsKey(time))
			return false;
		
		for (DBEntry entry :_inMemoryDB.get(time))
		{
			if(uid.equals(entry.UID) && 
			   matchEvent(ptsEvent, entry.Event))
			{
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * @return the last time key value that is contained the internal data structure
	 */
	public Double getLastTime()
	{
		return _inMemoryDB.lastKey();
	}

	/**
	 * @return the nearest exactly matching time
	 */
	public Double getTime(Double time)
	{ 
		if(_inMemoryDB.isEmpty() && time == 0L)
			return 0.0;
		
		if(_inMemoryDB.containsKey(time))
			return time;

		if(_inMemoryDB.firstKey() > time) // TODo check for emptiness
			return _inMemoryDB.firstKey();

		if(_inMemoryDB.lastKey() < time)
			return _inMemoryDB.lastKey();

		return NearestMatchLong.closest2(time,_inMemoryDB.keySet()); // find the nearest matching time;		
	}

	/**
	 * @return the next time value greater > than time or the last Key if no value can be found
	 */
	public Double getNextDelta(Double time)
	{
		Double closest = _inMemoryDB.containsKey(time) ? time :  NearestMatchLong.closest2(time,_inMemoryDB.keySet()); // find the nearest matching time;
		{
			Iterator<Double> it =  _inMemoryDB.tailMap(closest).keySet().iterator();
			if(it.hasNext())
			{
				it.next();
				if(it.hasNext())
				{
					return it.next();
				}
			}
			return _inMemoryDB.lastKey();
		}	
	}

	/**
	 * Debugging helper
	 */
	public void printMemoryDB()
	{
		for(Entry<Double, List<DBEntry>> entry : _inMemoryDB.entrySet()) 
		{
			Iterator<DBEntry> it = entry.getValue().iterator();
			while(it.hasNext())
			{
				DBEntry dbEntry = it.next();
				System.out.println(entry.getKey().toString() + " => (" + dbEntry.Event + ") " + dbEntry.UID + " := " + dbEntry.Value);
			}
		}
	}
	
	/**
	 * Debugging helper
	 */
	public void printSQLiteDB()
	{
		try
		{
			Statement statement = _connection.createStatement();
			ResultSet rs = statement.executeQuery("select * from " + TABLE_VALUES);

			while(rs.next())
			{
				// read the result set
				System.out.println("time = " + rs.getString(TraceDB.COLUMN_VALUES_TIME));
				System.out.println("event = " + rs.getString(TraceDB.COLUMN_VALUES_EVENT));
				System.out.println("name = " + rs.getString(TraceDB.COLUMN_VALUES_UID));
				String value = rs.getString(TraceDB.COLUMN_VALUES_VALUE);
				if(value != null)
				{
					value = Long.decode(rs.getString(TraceDB.COLUMN_VALUES_VALUE)).toString();
				}
				System.out.println("value = " + value);
			}
		} 
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}

	private void loadToMemory()
	{
		_inMemoryDB.clear();
		
		// load the sqlite-JDBC driver using the current class loader
		// this is normally already called by the ctor of XmlToDB()
		try
		{
			Statement statement = _connection.createStatement();
			ResultSet rs = statement.executeQuery("select * from " + TABLE_VALUES);

			// copy Database to memory
			while(rs.next())
			{
				// read the result set

				double time = rs.getDouble(COLUMN_VALUES_TIME);
				TraceDBEvent event = CharToEventMap.get(rs.getString(COLUMN_VALUES_EVENT).charAt(0));
				String uid = rs.getString(COLUMN_VALUES_UID);
				String value = rs.getString(COLUMN_VALUES_VALUE);

				if(_inMemoryDB.containsKey(time))
				{
					// time value exists, update the existing event field (multiple events for the same time stamp possible)
					List<DBEntry> list = _inMemoryDB.get(time);
					
					list.add(new DBEntry(event, uid, value));
				}
				else
				{
					List<DBEntry> list = new LinkedList<DBEntry>();
					
					list.add(new DBEntry(event, uid, value));
					
					_inMemoryDB.put(time, list);
				}
			}
			System.out.println();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}

	}
	
	private boolean matchEvent(EEvent ptsEvent, TraceDBEvent dbEvent)
	{
		switch(ptsEvent)
		{
			case Read:
				return dbEvent == TraceDBEvent.Read;
			case Written:
				return dbEvent == TraceDBEvent.Write;
			case Called:
				return dbEvent == TraceDBEvent.Call;
			case Returned:
				return dbEvent == TraceDBEvent.Return;
			case Received:
				return dbEvent == TraceDBEvent.Receive;
			case Sent:
				return dbEvent == TraceDBEvent.Send;
		default:
			break;
		}
		
		throw new RuntimeException("Unexpected EEvent: " + ptsEvent);
	}

	private String getValueOfFirstEntryInSet(Set<Entry<Double, List<DBEntry>>> entrySet, String uid)
	{
		for(Entry<Double, List<DBEntry>> entry : entrySet)
		{
			// iterate over the maps that are associated to this time value
			Iterator<DBEntry> it = entry.getValue().iterator();
			while(it.hasNext()) 
			{
				DBEntry innerDBEntry = it.next();
				if(uid.equals(innerDBEntry.UID))
					return innerDBEntry.Value;
			}
		}
		
		return null;
	}
	
	public void generateImage(String path)
	{
		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				GraphShell gShell = new GraphShell(_dbFile.getRawLocation().toOSString());
				gShell.generatePNG(_plotImageFile.getRawLocation().toOSString());
			}
		});
	}
	
}
