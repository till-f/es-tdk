package fzi.mottem.runtime.rti.isystem;

import fzi.mottem.model.testrigmodel.ProcessorCore;
import si.isystem.connect.CDebugFacade;
import si.isystem.connect.CExecutionController;
import si.isystem.connect.CIDEController;
import si.isystem.connect.CLoaderController;
import si.isystem.connect.ConnectionMgr;

public class ISYSTEMCtrlSet
{
	private final ProcessorCore _processorCore;
	private final ConnectionMgr _connectionManager;

	private CLoaderController _loaderController;
	private CExecutionController _executionController;
	private CDebugFacade _debugFacade;
	private CIDEController _ideController;
	
	private boolean _initialized = false;
	
	public ConnectionMgr getConnectionManager()
	{
		if (!_initialized)
			throw new RuntimeException("Access to ISYSTEMCtrlSet before initialization not allowed");

		return _connectionManager;
	}

	public CLoaderController getLoaderController()
	{
		if (!_initialized)
			throw new RuntimeException("Access to ISYSTEMCtrlSet before initialization not allowed");

		return _loaderController;
	}

	public CExecutionController getExecutionController()
	{
		if (!_initialized)
			throw new RuntimeException("Access to ISYSTEMCtrlSet before initialization not allowed");

		return _executionController;
	}

	public CDebugFacade getDebugFacade()
	{
		if (!_initialized)
			throw new RuntimeException("Access to ISYSTEMCtrlSet before initialization not allowed");

		return _debugFacade;
	}

	public ProcessorCore getProcessorCore()
	{
		if (!_initialized)
			throw new RuntimeException("Access to ISYSTEMCtrlSet before initialization not allowed");

		return _processorCore;
	}

	public ISYSTEMCtrlSet(ProcessorCore processorCore, 
						  ConnectionMgr connectionManager)
	{
		_processorCore = processorCore;
		_connectionManager = connectionManager;
	}

	public void init(String workspacePath)
	{
		if (_initialized)
			throw new RuntimeException("Re-initialization of ISYSTEMCtrlSet not allowed");

		_connectionManager.connectMRU(workspacePath);
		_loaderController = new CLoaderController(_connectionManager);
		_executionController = new CExecutionController(_connectionManager);
		_debugFacade = new CDebugFacade(_connectionManager);
		_ideController = new CIDEController(_connectionManager);
		
		_ideController.minimize();
		
		_initialized = true;
	}

	public void cleanup()
	{
		if (_initialized)
		{
			if (_connectionManager.isConnected())
			{
				_connectionManager.disconnect();
			}
		}
	}

}
