package com.freshdirect.dataloader.disablemod;

import org.apache.log4j.Logger;

import com.freshdirect.dataloader.sap.jco.server.FDSapFunctionHandler;
import com.freshdirect.dataloader.sap.jco.server.FdSapServer;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.sap.conn.jco.JCo;
import com.sap.conn.jco.JCoCustomRepository;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoFunctionTemplate;
import com.sap.conn.jco.JCoListMetaData;
import com.sap.conn.jco.JCoMetaData;
import com.sap.conn.jco.JCoParameterList;
import com.sap.conn.jco.JCoRepository;
import com.sap.conn.jco.server.JCoServerContext;
import com.sap.conn.jco.server.JCoServerFunctionHandler;


/**
 * This jco listener checks if the order can start picking. 
 *
 * @author tbalumuri
 *
 */
public class FDDisableOrderModificationlJcoServer extends FdSapServer
{

	private static final Logger LOG = Logger.getLogger(FDDisableOrderModificationlJcoServer.class.getName());

	private String serverName;
	
	private String functionName;
	
	/**
	 * @param serverName
	 * @param functionName
	 * @param programId
	 */
	public FDDisableOrderModificationlJcoServer(String serverName, String functionName, String programId) {
		super();
		this.serverName = serverName;
		this.functionName = functionName;
		this.setProgramId(programId);
	}

	@Override
	protected JCoRepository createRepository()
	{
		final JCoCustomRepository repository = JCo.createCustomRepository("DisableOrderRepository");
				
		final JCoListMetaData fmetaImport = JCo.createListMetaData("DISABLEORDER_IMPORT");
		fmetaImport.add("BSTNK", JCoMetaData.TYPE_CHAR, 20, 0, 0, null, null, JCoListMetaData.IMPORT_PARAMETER, null, null);

		fmetaImport.lock();

		final JCoListMetaData fmetaExport = JCo.createListMetaData("DISABLEORDER_EXPORT");
		fmetaExport.add("RETURN", JCoMetaData.TYPE_CHAR, 1, 0, 0, null, null, JCoListMetaData.EXPORT_PARAMETER, null, null);
		fmetaExport.add("MESSAGE", JCoMetaData.TYPE_CHAR, 255, 0, 0, null, null, JCoListMetaData.EXPORT_PARAMETER, null, null);
		fmetaExport.lock();

		final JCoFunctionTemplate fT = JCo.createFunctionTemplate(functionName, fmetaImport, fmetaExport, null, null, null);
		repository.addFunctionTemplateToCache(fT);

		return repository;
	
	}

	@Override
	protected FDSapFunctionHandler getHandler()
	{

		return new FDConnectionHandler();
	}

	private boolean orderReadyForPick(String orderNum) throws FDResourceException {
		return FDCustomerManager.isReadyForPick(orderNum);
	}
	
	private void updateOrderPickStarted(String orderNum) throws FDResourceException {
		FDCustomerManager.updateOrderInProcess(orderNum);
	}
	
	protected class FDConnectionHandler extends FDSapFunctionHandler implements JCoServerFunctionHandler
	{
		@Override
		public String getFunctionName()
		{
			return functionName;
		}

		public void handleRequest(final JCoServerContext serverCtx, final JCoFunction function)
		{

			final JCoParameterList exportParamList = function.getExportParameterList();
			final JCoParameterList importParamList = function.getImportParameterList();
			try
			{
				String orderNum = importParamList.getString("BSTNK");
				LOG.info("order to be pick started? "+orderNum);
				
				if(orderReadyForPick(orderNum)){
					
				exportParamList.setValue("RETURN", "X");
				exportParamList.setValue("MESSAGE",
							"Success importing order pick status");
				updateOrderPickStarted(orderNum);
				}
				else{
					exportParamList.setValue("RETURN", "E");
					exportParamList.setValue("MESSAGE",
							"Error importing order pick status");
				}
					
			}catch (Exception e) {
				LOG.error("Error importing order status: " + e);
				exportParamList.setValue("RETURN", "E");
				exportParamList.setValue("MESSAGE",
						"Error importing order pick status");
			}

		}
	}
	
	/**
	 * @param serverName
	 *           the serverName to set
	 */
	public void setServerName(final String serverName)
	{
		this.serverName = serverName;
	}

	/**
	 * @return the serverName
	 */
	@Override
	public String getServerName()
	{
		return serverName;
	}

	/**
	 * @return the functionName
	 */
	public String getFunctionName()
	{
		return functionName;
	}

	/**
	 * @param functionName the functionName to set
	 */
	public void setFunctionName(String functionName)
	{
		this.functionName = functionName;
	}
	
	
}
