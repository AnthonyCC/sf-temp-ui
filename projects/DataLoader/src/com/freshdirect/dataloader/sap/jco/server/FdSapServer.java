package com.freshdirect.dataloader.sap.jco.server;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.mail.MessagingException;

import org.apache.log4j.Category;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.dataloader.response.FDJcoServerNotification;
import com.freshdirect.dataloader.response.FDJcoServerResult;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.mail.ErpMailSender;
import com.sap.conn.jco.JCoCustomRepository;
import com.sap.conn.jco.JCoDestinationManager;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoMetaData;
import com.sap.conn.jco.JCoRecordField;
import com.sap.conn.jco.JCoRecordFieldIterator;
import com.sap.conn.jco.JCoRecordMetaData;
import com.sap.conn.jco.JCoRepository;
import com.sap.conn.jco.JCoTable;
import com.sap.conn.jco.ext.ServerDataProvider;
import com.sap.conn.jco.server.DefaultServerHandlerFactory;
import com.sap.conn.jco.server.JCoApplicationAuthorizationException;
import com.sap.conn.jco.server.JCoServer;
import com.sap.conn.jco.server.JCoServerAuthorizationData;
import com.sap.conn.jco.server.JCoServerContext;
import com.sap.conn.jco.server.JCoServerContextInfo;
import com.sap.conn.jco.server.JCoServerErrorListener;
import com.sap.conn.jco.server.JCoServerExceptionListener;
import com.sap.conn.jco.server.JCoServerFactory;
import com.sap.conn.jco.server.JCoServerFunctionHandler;
import com.sap.conn.jco.server.JCoServerSecurityHandler;
import com.sap.conn.jco.server.JCoServerState;
import com.sap.conn.jco.server.JCoServerStateChangedListener;
import com.sap.conn.jco.server.JCoServerTIDHandler;


/**
 * Represents the JCo server registered with ERP system
 * 
 * Created by kkanuganti on 01/19/15.
 */
public abstract class FdSapServer
{
	private static final Category LOG = LoggerFactory.getInstance(FdSapServer.class.getName());

	protected List<TableMetaData> tableMetaDataList = new ArrayList<TableMetaData>();

	protected JCoServer server;

	static MyTIDHandler myTIDHandler = null;
		
	private String programId;
	
	protected JCoTable materialErrorTable;
	
	/**
	 * Convenience method to output the attributes of a function
	 *
	 * @param function
	 * @param serverCtx
	 */
	protected static void logAttributes(final JCoFunction function, final JCoServerContext serverCtx)
	{
		if (LOG.isInfoEnabled())
		{
			LOG.debug("----------------------------------------------------------------");
			LOG.debug("call              : {}"+ function.getName());
			LOG.debug("ConnectionId      : {}"+ serverCtx.getConnectionID());
			LOG.debug("SessionId         : {}"+ serverCtx.getSessionID());
			LOG.debug("TID               : {}"+ serverCtx.getTID());
			LOG.debug("repository name   : {}"+ serverCtx.getRepository().getName());
			LOG.debug("is in transaction : {}"+ serverCtx.isInTransaction());
			LOG.debug("is stateful       : {}"+ serverCtx.isStatefulSession());
			LOG.debug("----------------------------------------------------------------");
			LOG.debug("gwhost: {}"+ serverCtx.getServer().getGatewayHost());
			LOG.debug("gwserv: {}"+ serverCtx.getServer().getGatewayService());
			LOG.debug("progid: {}"+ serverCtx.getServer().getProgramID());
			LOG.debug("----------------------------------------------------------------");
			LOG.debug("attributes  : {}"+ serverCtx.getConnectionAttributes().toString());
			LOG.debug("----------------------------------------------------------------");
			LOG.debug("CPIC conversation ID: {}"+ serverCtx.getConnectionAttributes().getCPICConversationID());
			LOG.debug("----------------------------------------------------------------");
			LOG.debug("req text: {}"+ function.getImportParameterList().getString("REQUTEXT"));
			function.getExportParameterList().setValue("ECHOTEXT", function.getImportParameterList().getString("REQUTEXT"));
			function.getExportParameterList().setValue("RESPTEXT", "Hello World");

			// In (tRFC Server) we also set the status to executed:
			if (myTIDHandler != null)
			{
				myTIDHandler.execute(serverCtx);
			}
		}
	}

	/**
	 * loads the configuration properties from database to connect to SAP Gateway
	 */
	private void createDestination()
	{
		final Properties jcoServerProperties = new Properties();
		
		final String gwHost = ErpServicesProperties.getJcoClientListenHost();
		if (gwHost == null)
			throw new IllegalArgumentException("gwHost not specified");

		final String gwServ = ErpServicesProperties.getJcoClientListenServer();
		if (gwServ == null)
			throw new IllegalArgumentException("gwServ not specified");

		jcoServerProperties.setProperty(ServerDataProvider.JCO_GWHOST, gwHost);
		jcoServerProperties.setProperty(ServerDataProvider.JCO_GWSERV, gwServ);
		jcoServerProperties.setProperty(ServerDataProvider.JCO_CONNECTION_COUNT, "1");
		
		jcoServerProperties.setProperty(ServerDataProvider.JCO_PROGID, getProgramId());
		jcoServerProperties.setProperty(ServerDataProvider.JCO_TRACE, "0");

		LOG.info("Loaded configuration: " + jcoServerProperties);
		
		File destCfg = new File(getServerName() + ".jcoServer");

		try 
		{
			FileOutputStream fos = new FileOutputStream(destCfg, false);
			jcoServerProperties.store(fos, "for tests only !");
			fos.close();
		}
		catch (Exception e) 
		{
			LOG.error("",e);
			throw new RuntimeException(
					"Unable to create the destination files", e);
		}
	}

	/**
	 * method to start the server
	 */
	public void startServer()
	{
		initializeServer();
		
		LOG.info("STARTING server: "+ getServerName());
		
		server.start();
		
		LOG.info("Server -> "+ getServerName() +", State -> " +server.getState());
	}

	/**
	 * Will add a new repository and handler to the JCoServer
	 */
	protected void initializeServer()
	{
		//creates the connection file to the FD SAP server
		createDestination();

		JCoRepository repository = null;
		try
		{
			repository = createRepository();
			server = JCoServerFactory.getServer(getServerName());
		}
		catch (final JCoException ex)
		{
			ex.printStackTrace();
			throw new RuntimeException("Unable to create the server " + getServerName() + ", because of " + ex.getMessage(), ex);
			
		}
		if (repository instanceof JCoCustomRepository)
		{
			final String repDest = server.getRepositoryDestination();
			if (repDest != null)
			{				
				try
				{
					LOG.debug("Repository destination for Server: "+ getServerName() +" , destination: "+ repDest);
					
					((JCoCustomRepository) repository).setDestination(JCoDestinationManager.getDestination(repDest));

				}
				catch (final JCoException e)
				{
					e.printStackTrace();
					LOG.error(">>> repository contains static function definition only");
				}
			}
		}

		server.setRepository(repository);

		addHandler(getHandler());

		// Enable if used transaction based in future

		// myTIDHandler = new MyTIDHandler();
		// server.setTIDHandler(myTIDHandler);

	}

	/**
	 * method to stop the server
	 */
	public void stopServer()
	{
		server.stop();
		LOG.info("STOPPING server "+ getServerName());
		LOG.info("Server: "+ getServerName() + ", State -> " + server.getState());

	}

	protected void addHandler(final FDSapFunctionHandler fdSapFunctionHandler)
	{
		final DefaultServerHandlerFactory.FunctionHandlerFactory factory = new DefaultServerHandlerFactory.FunctionHandlerFactory();
		factory.registerHandler(fdSapFunctionHandler.getFunctionName(), (JCoServerFunctionHandler) fdSapFunctionHandler);
		server.setCallHandlerFactory(factory);
	}

	protected void addListeners(final JCoServer server)
	{
		final MyThrowableListener eListener = new MyThrowableListener();
		server.addServerErrorListener(eListener);
		server.addServerExceptionListener(eListener);

		final MyStateChangedListener slistener = new MyStateChangedListener();
		server.addServerStateChangedListener(slistener);

		server.setSecurityHandler(new SecurityHandler());

	}

	protected class SecurityHandler implements JCoServerSecurityHandler
	{
		@Override
		public void checkAuthorization(final JCoServerContext jCoServerContext, final String s,
				final JCoServerAuthorizationData jCoServerAuthorizationData) throws JCoApplicationAuthorizationException
		{
			LOG.info("checking authorization");
		}
	}


	protected class MyThrowableListener implements JCoServerErrorListener, JCoServerExceptionListener
	{

		public void serverErrorOccurred(final JCoServer jcoServer, final String connectionId, final Error error)
		{
			LOG.error(">>> Error occured on {"+ jcoServer.getProgramID() +"} connection {"+ connectionId +"}");
			error.printStackTrace();
		}

		public void serverExceptionOccurred(final JCoServer jcoServer, final String connectionId, final Exception error)
		{
			LOG.error(">>> Error occured on {"+ jcoServer.getProgramID() +"} connection {"+ connectionId +"}");
			error.printStackTrace();
		}

		@Override
		public void serverErrorOccurred(final JCoServer jCoServer, final String s, final JCoServerContextInfo jCoServerContextInfo,
				final Error error)
		{
			LOG.error("error {}", error);
		}

		@Override
		public void serverExceptionOccurred(final JCoServer jCoServer, final String s,
				final JCoServerContextInfo jCoServerContextInfo, final Exception e)
		{
			LOG.error("exception {}", e);

		}
	}

	protected class MyStateChangedListener implements JCoServerStateChangedListener
	{
		public void serverStateChangeOccurred(final JCoServer server, final JCoServerState oldState, final JCoServerState newState)
		{

			// Defined states are: STARTED, DEAD, ALIVE, STOPPED;
			// see JCoServerState class for details.
			// Details for connections managed by a server instance
			// are available via JCoServerMonitor
			LOG.info("Server state changed from {" + oldState.toString()
					+ "} to {" + newState.toString()
					+ "} on server with program id {" + getProgramId() + "}");
		}

	}


	//Leaving the Transaction code in case we need to use it later
	private enum TIDState
	{
		CREATED, EXECUTED, COMMITTED, ROLLED_BACK, CONFIRMED;
	}

	protected class MyTIDHandler implements JCoServerTIDHandler
	{

		Map<String, TIDState> availableTIDs = new Hashtable<String, TIDState>();

		public boolean checkTID(final JCoServerContext serverCtx, final String tid)
		{
			LOG.info("TID Handler: checkTID for { "+ tid +" }");
			final TIDState state = availableTIDs.get(tid);
			if (state == null)
			{
				availableTIDs.put(tid, TIDState.CREATED);
				return true;
			}

			if (state == TIDState.CREATED || state == TIDState.ROLLED_BACK)
			{
				return true;
			}

			return false;
			// "true" - JCo will now execute the transaction,
			// "false" - already executed this transaction previously, so JCo will skip the handleRequest() step and will immediately return an OK code to R/3.
		}

		public void commit(final JCoServerContext serverCtx, final String tid)
		{
			LOG.info("TID Handler: commit for { "+ tid +" }");
			//react on commit e.g. commit on the database
			//if necessary throw a RuntimeException, if the commit was not possible
			availableTIDs.put(tid, TIDState.COMMITTED);
		}

		public void rollback(final JCoServerContext serverCtx, final String tid)
		{
			LOG.info("TID Handler: rollback for { "+ tid +" }");
			//react on roll back e.g. roll back on the database
			availableTIDs.put(tid, TIDState.ROLLED_BACK);
		}

		public void confirmTID(final JCoServerContext serverCtx, final String tid)
		{
			LOG.info("TID Handler: confirmTID for { "+ tid +" }");
			try
			{
				//clean up the resources
			}
			//catch(Throwable t) {} //partner wont react on an exception at this point
			finally
			{
				availableTIDs.remove(tid);
			}
		}

		public void execute(final JCoServerContext serverCtx)
		{
			final String tid = serverCtx.getTID();
			if (tid != null)
			{
				LOG.info("TID Handler: execute for { "+ tid +" }");
				availableTIDs.put(tid, TIDState.EXECUTED);
			}
		}

	}

	/**
	 * Will return a String that represents the columns & values in a JCoTable
	 *
	 * @param table
	 * @return string
	 */
	public static String convertToString(final JCoTable table)
	{
		final StringBuffer retVal = new StringBuffer();
		if (table != null)
		{
			final JCoRecordFieldIterator rIt = table.getRecordFieldIterator();
			while (rIt.hasNextField())
			{
				final JCoRecordField recordField = rIt.nextRecordField();
				try
				{
					retVal.append(recordField.getName() + "=" + recordField.getValue() + "\t");
				}
				catch (final Exception e)
				{
					e.printStackTrace();
				}
				//retVal.append(recordField.getName() +"="  + table.getString(recordField.getName())+"\t");
			}
		}
		return retVal.toString();
	}

	/**
	 * Will populate the MetaDataList with the data from the JCoTable
	 *
	 * @param metaDataList
	 * @param tableMetaDataList
	 */
	protected static void createTableRecord(final JCoRecordMetaData metaDataList, final List<TableMetaData> tableMetaDataList)
	{
		int offset = 0;
		for (final TableMetaData tableMetaData : tableMetaDataList)
		{
			final int size = tableMetaData.getColumnSize();
			addColumnToMetaData(metaDataList, tableMetaData.getColumnName(), tableMetaData.getType(), size, offset,
					tableMetaData.getDecimals());
			offset += size;
		}
	}

	/**
	 * Adds a metadata for a table to the metaDataList for non BCD type columns
	 *
	 * @param metaDataList
	 * @param columnName
	 * @param type
	 * @param columnSize
	 * @param offset
	 */
	protected static void addColumnToMetaData(final JCoRecordMetaData metaDataList, final String columnName, final int type,
			final int columnSize, final int offset)
	{
		addColumnToMetaData(metaDataList, columnName, type, columnSize, -1);
	}

	/**
	 * Adds a metadata for a table to the metaDataList for BCD type columns
	 *
	 * @param metaDataList
	 * @param columnName
	 * @param type
	 * @param columnSize
	 * @param offset
	 * @param decimals
	 */
	protected static void addColumnToMetaData(final JCoRecordMetaData metaDataList, final String columnName, final int type,
			final int columnSize, final int offset, final int decimals)
	{
		if (type == JCoMetaData.TYPE_BCD && decimals >= 0)
		{
			metaDataList.add(columnName, type, columnSize, offset, 2 * columnSize, 2 * offset, decimals, null, null, null);
		}
		else
		{
			metaDataList.add(columnName, type, columnSize, offset, 2 * columnSize, 2 * offset);
		}

	}

	/**
	 * This method sets the catalog version in the session if non exists for catalog
	 *
	 * @param catalogId
	 */

	protected abstract JCoRepository createRepository();

	protected abstract FDSapFunctionHandler getHandler();

	/**
	 * @return String
	 */
	public abstract String getServerName();

	/**
	 * inner class to hold the basic info for table columns
	 */
	public class TableMetaData
	{

		String columnName;
		int columnSize;
		int decimals;
		int type;
		String columnDescription;

		/**
		 * @param columnName
		 * @param type
		 * @param columnSize
		 */
		public TableMetaData(final String columnName, final int type, final int columnSize)
		{
			this.columnName = columnName;
			this.type = type;
			this.columnSize = columnSize;

		}

		/**
		 * @param columnName
		 * @param type
		 * @param columnSize
		 * @param decimals
		 * @param columnDescription
		 */
		public TableMetaData(final String columnName, final int type, final int columnSize, final int decimals,
				final String columnDescription)
		{
			this.columnName = columnName;
			this.type = type;
			this.columnSize = columnSize;
			this.decimals = decimals;
			this.columnDescription = columnDescription;
		}

		/**
		 * @param columnName
		 * @param type
		 * @param columnSize
		 * @param columnDescription
		 */
		public TableMetaData(final String columnName, final int type, final int columnSize, final String columnDescription)
		{
			this.columnName = columnName;
			this.type = type;
			this.columnSize = columnSize;
			this.columnDescription = columnDescription;
		}

		/**
		 * @return the columnName
		 */
		public String getColumnName()
		{
			return columnName;
		}

		/**
		 * @param columnName the columnName to set
		 */
		public void setColumnName(String columnName)
		{
			this.columnName = columnName;
		}

		/**
		 * @return the columnSize
		 */
		public int getColumnSize()
		{
			return columnSize;
		}

		/**
		 * @param columnSize the columnSize to set
		 */
		public void setColumnSize(int columnSize)
		{
			this.columnSize = columnSize;
		}

		/**
		 * @return the decimals
		 */
		public int getDecimals()
		{
			return decimals;
		}

		/**
		 * @param decimals the decimals to set
		 */
		public void setDecimals(int decimals)
		{
			this.decimals = decimals;
		}

		/**
		 * @return the type
		 */
		public int getType()
		{
			return type;
		}

		/**
		 * @param type the type to set
		 */
		public void setType(int type)
		{
			this.type = type;
		}

		/**
		 * @return the columnDescription
		 */
		public String getColumnDescription()
		{
			return columnDescription;
		}

		/**
		 * @param columnDescription the columnDescription to set
		 */
		public void setColumnDescription(String columnDescription)
		{
			this.columnDescription = columnDescription;
		}
	}

	/**
	 * @param strToPad
	 * @param maxLength
	 * @param padChar
	 * @return String
	 */
	public static String leftPad(final String strToPad, final int maxLength, final char padChar)
	{
		String paddedStr = strToPad;
		if (strToPad != null && maxLength > 0)
		{
			final int numCharsToPad = maxLength - strToPad.length();
			for (int i = 0; i < numCharsToPad; i++)
			{
				paddedStr = padChar + paddedStr;
			}
		}
		return paddedStr;
	}

	/**
	 * @param strToPad
	 * @param maxLength
	 * @param padChar
	 * @return String
	 */
	public static String rightPad(final String strToPad, final int maxLength, final char padChar)
	{
		String paddedStr = strToPad;
		if (strToPad != null && maxLength > 0)
		{
			final int numCharsToPad = maxLength - strToPad.length();
			for (int i = 0; i < numCharsToPad; i++)
			{
				paddedStr += padChar;
			}
		}
		return paddedStr;
	}
	
	/**
	 * @param matNo
	 * @param result
	 * @param errorMessage
	 */
	public void populateResponseRecord(final FDJcoServerResult result, final String matNo, final String errorMessage)
	{
		if (matNo != null && result != null)
		{
			materialErrorTable.appendRow();
			materialErrorTable.setValue("MATNR", matNo);
			materialErrorTable.setValue("ERROR", errorMessage);
		}
		
		if(result != null)
		{
			result.addError(matNo, errorMessage);
		}
	}
	
	/**
	 * @param result
	 * @param exportName 
	 * @param batchNumber 
	 */
	public void emailFailureReport(FDJcoServerResult result, String exportName, String batchNumber)
	{
		try
		{
			if (result.getError() != null && result.getError().getMessages() != null && result.getError().getMessages().size() > 0)
			{
				Date currentDate = new Date();
				SimpleDateFormat dateFormatter = new SimpleDateFormat("EEE, MMM d, yyyy 'at' hh:mm:ss a zzz");
				String subject = exportName + " export for the delivery date " + dateFormatter.format(currentDate);

				StringBuffer buf = new StringBuffer();
				String br = "\n";
				
				buf.append(exportName + " export report for ").append(dateFormatter.format(currentDate));
				if(batchNumber != null)
				{
					buf.append(", batch "+ batchNumber);
				}
				buf.append(br);
				buf.append(br);
				buf.append("Material No.").append("\t\t\t").append("Error Message").append(br);
				buf.append("----------------------------------------------------------------------------------").append(br);
				
				FDJcoServerNotification error = result.getError();
				for (Map.Entry<String, String> errorEntry : error.getMessages().entrySet())
				{
					buf.append(errorEntry.getKey()).append("\t\t\t").append(errorEntry.getValue()).append(br);
				}

				ErpMailSender mailer = new ErpMailSender();
				mailer.sendMail(ErpServicesProperties.getCronFailureMailFrom(), ErpServicesProperties.getCronFailureMailTo(), "",
						subject, buf.toString(), true, "");
			}
		}
		catch (MessagingException e)
		{
			LOG.warn("Error sending report email: ", e);
		}
	}

	/**
	 * @return the programId
	 */
	public String getProgramId()
	{
		return programId;
	}

	/**
	 * @param programId
	 *           the programId to set
	 */
	public void setProgramId(final String programId)
	{
		this.programId = programId;
	}
}
