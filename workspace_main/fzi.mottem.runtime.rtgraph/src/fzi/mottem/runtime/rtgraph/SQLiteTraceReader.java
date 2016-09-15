package fzi.mottem.runtime.rtgraph;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import org.csstudio.swt.xygraph.figures.Trace.PointStyle;
import org.csstudio.swt.xygraph.figures.Trace.TraceType;

import fzi.mottem.runtime.dataexchanger.DEMessage;
import fzi.mottem.runtime.dataexchanger.DataExchanger;
import fzi.mottem.runtime.dataexchanger.Signal;
import fzi.mottem.runtime.rtgraph.XML.GraphViewRepresentation;
import fzi.mottem.runtime.rtgraph.XML.TraceRepresentation;

public class SQLiteTraceReader {

	HashMap<String, TraceType> axisValueMapping = new HashMap<String, TraceType>();
	HashMap<String, PointStyle> pointValueMapping = new HashMap<String, PointStyle>();

	public SQLiteTraceReader() {
		// TODO find better implementation for the mapping

		axisValueMapping.put("hold", TraceType.STEP_HORIZONTALLY);
		axisValueMapping.put("dots", TraceType.POINT);
		axisValueMapping.put("area", TraceType.AREA);
		axisValueMapping.put("solid", TraceType.SOLID_LINE);
		axisValueMapping.put("dash", TraceType.DASH_LINE);
		axisValueMapping.put("bar", TraceType.BAR);

		pointValueMapping.put("circle", PointStyle.CIRCLE);
		pointValueMapping.put("cross", PointStyle.CROSS);
		pointValueMapping.put("diamond", PointStyle.DIAMOND);
		pointValueMapping.put("point", PointStyle.POINT);
		pointValueMapping.put("square", PointStyle.SQUARE);
		pointValueMapping.put("triangle", PointStyle.TRIANGLE);
	}

	/**
	 * Prints the table contained in the .db file denoted by the path parameter
	 * 
	 * @param path
	 *            the path to the .db file
	 */
	public void printTraceTable(String path) {
		Connection c = null;
		Statement stmt = null;
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:" + path);
			c.setAutoCommit(false);
			System.out.println("SQLTraceReader: Opened database " + path + " successfully");

			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM profileTable;");
			while (rs.next()) {
				long time = rs.getInt("time");
				String uid = rs.getString("uid");
				String event = rs.getString("event");
				double value = rs.getDouble("value");
				System.out.println("Time = " + time);
				System.out.println("Event = " + event);
				System.out.println("UID = " + uid);
				System.out.println("Value = " + value);
				System.out.println();
			}
			rs.close();
			stmt.close();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
	}

	/**
	 * Reads the .db file denoted by its file path and returns an ArrayList with
	 * the contained DEMessages
	 * 
	 * @param filePath
	 * @return
	 */
	public HashMap<String, ArrayList<DEMessage>> getDEMessages(String filePath) {

		Connection c = null;
		Statement stmt = null;

		Signal s = new Signal("unnamed", "unnamed");
		String uid = "";

		HashMap<String, ArrayList<DEMessage>> msgMap = new HashMap<String, ArrayList<DEMessage>>();

		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:" + filePath);
			c.setAutoCommit(false);

			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM profileTable;");

			while (rs.next()) {

				long time = rs.getInt("time");
				uid = rs.getString("uid");
				// String event = rs.getString("event");
				double value = rs.getDouble("value");

				s = DataExchanger.getSignal(uid);
				msgMap.putIfAbsent(uid, new ArrayList<DEMessage>());
				msgMap.get(uid).add(new DEMessage(s, value, time));

			}
			rs.close();
			stmt.close();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			// System.exit(0);
		}
		return msgMap;
	}

	public void applyDBMetaData(GraphViewRepresentation gvr, String filePath) {

		Connection c = null;
		Statement stmt = null;

		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:" + filePath);
			c.setAutoCommit(false);

			stmt = c.createStatement();

			for (TraceRepresentation tr : gvr.traceRepresentations) {

				ResultSet rs = stmt.executeQuery("SELECT * FROM metaInfo WHERE uid = '" + tr.getSignalUID() + "';");
				while (rs.next()) {
					// so far so good
					// now apply the key-value pairs to the trace representation
					// and finally apply the graph view representation
					String value = rs.getString("value");
					String key = rs.getString("key");

					updateTraceRepresentation(tr, key, value);

				}
				rs.close();
			}

			ResultSet rsGlobal = stmt.executeQuery("SELECT * FROM metaInfo WHERE uid = 'GLOBAL';");
			while (rsGlobal.next()) {
				// so far so good
				// now apply the key-value pairs to the trace representation
				// and finally apply the graph view representation
				String value = rsGlobal.getString("value");
				String key = rsGlobal.getString("key");
				updateGraphViewRepresentation(gvr, key, value);
			}

			stmt.close();
			c.close();

		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			// System.exit(0);
		}

	}

	private void updateGraphViewRepresentation(GraphViewRepresentation gvr, String key, String value) {
		switch (key) {
		case "max":
			gvr.setMaxPrimaryXrange(Double.valueOf(value));
			break;
		case "min":
			gvr.setMinPrimaryXrange(Double.valueOf(value));
			break;
		case "axis":
			if (value.equals("lg"))
				gvr.setPrimaryXLogarithmic(true);
			break;
		default:
			break;
		}
	}

	private void updateTraceRepresentation(TraceRepresentation tr, String key, String value) {

		switch (key) {
		case "max":
			tr.setMax_range(Double.valueOf(value));
			break;
		case "min":
			tr.setMin_range(Double.valueOf(value));
			break;
		case "line":
			TraceType ttype = axisValueMapping.get(value);
			if (ttype != null)
				tr.setType(ttype);
			break;
		case "axis":
			if (value.equals("lg"))
				tr.setLogarithmic(true);
			break;
		default:
			break;
		}
	}

}
