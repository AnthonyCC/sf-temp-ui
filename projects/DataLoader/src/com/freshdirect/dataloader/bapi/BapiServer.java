package com.freshdirect.dataloader.bapi;

import org.apache.log4j.Category;
import com.freshdirect.framework.util.log.LoggerFactory;

import com.sap.mw.jco.JCO;

public abstract class BapiServer implements JCO.ServerExceptionListener, JCO.ServerStateChangedListener {

	private static Category LOGGER = LoggerFactory.getInstance( BapiServer.class );

	private String gwHost;
	private String gwServ;
	private String progId;

	private JCO.Server server;

    /**
     * Create an instance of the server.
     *
     * @param gwhost the gateway host
     * @param gwserv the gateway service number
     * @param progid the program id
	 */
	public BapiServer(String gwHost, String gwServ, String progId) {
		JCO.addServerExceptionListener(this);
		JCO.addServerStateChangedListener(this);
		this.gwHost = gwHost;
		this.gwServ = gwServ;
		this.progId = progId;
	}

	protected abstract BapiRepository getRepository();

	public void start() {

		final BapiRepository repository = this.getRepository();

		this.server = new JCO.Server(this.gwHost, this.gwServ, this.progId, repository) {
			protected void handleRequest(JCO.Function function) {
		
				String funcName = function.getName();
				LOGGER.debug("handleRequest " + BapiServer.this.progId + " " + funcName + ")");
		
				BapiFunctionI func = repository.getFunction(funcName);
				if (function!=null) {
					JCO.ParameterList input  = function.getImportParameterList();
					JCO.ParameterList output = function.getExportParameterList();
					JCO.ParameterList tables = function.getTableParameterList();
					func.execute(input, output, tables);
		
				} else {
					// !!! unknown function, anything we can do?
					LOGGER.warn("Unknown function "+funcName+" invoked in "+ BapiServer.this.progId);
				}
			}
			
		};

		//JCO.setTraceLevel(0);
		//this.server.setTrace(false);

		this.server.start();
	}

	public void serverExceptionOccurred(JCO.Server server, Exception ex) {
		if (this.progId.equals( server.getProgID() )) {
			LOGGER.warn("Exception in server " + this.progId, ex);
		}
	}

	public void serverStateChangeOccurred(JCO.Server server, int oldState, int newState) {
		if (this.progId.equals( server.getProgID() )) {
			LOGGER.info(
				"Server " + this.progId + " changed state from [" +
				this.stateToString(oldState) + "] to [" +
				this.stateToString(newState) + "]" );
		}
	}
	
	private String stateToString(int state) {
		StringBuffer buf = new StringBuffer();
		if ((state & JCO.STATE_STOPPED    ) != 0) buf.append(" STOPPED ");
		if ((state & JCO.STATE_STARTED    ) != 0) buf.append(" STARTED ");
		if ((state & JCO.STATE_LISTENING  ) != 0) buf.append(" LISTENING ");
		if ((state & JCO.STATE_TRANSACTION) != 0) buf.append(" TRANSACTION ");
		if ((state & JCO.STATE_BUSY       ) != 0) buf.append(" BUSY ");
		return buf.toString();
	}

}
