package com.freshdirect.dataloader.sap.jco;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.freshdirect.dataloader.BadDataException;
import com.freshdirect.dataloader.LoaderException;
import com.freshdirect.dataloader.sap.SAPLoadListener;
import com.freshdirect.dataloader.sap.jco.server.FDSapFunctionHandler;
import com.freshdirect.dataloader.sap.jco.server.FdSapServer;
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
 * This class is used for populating the base material, sales unit, product options and material pricing
 * via Jco-server (3.0) registered to the ERP system
 *
 * @author kkanuganti
 */
public class FDMaterialBatchJcoServer extends FdSapServer
{
	private static final Logger LOG = Logger.getLogger(FDMaterialBatchJcoServer.class.getName());

	private String serverName;

	private String functionName;
	
	/**
	 * @param serverName
	 * @param functionName
	 * @param programId
	 */
	public FDMaterialBatchJcoServer(String serverName, String functionName, String programId) {
		super();
		this.serverName = serverName;
		this.functionName = functionName;
		this.setProgramId(programId);
	}

	@Override
	protected JCoRepository createRepository()
	{
		final JCoCustomRepository repository = JCo.createCustomRepository("FDMaterialLoaderRepository");
		
		final JCoListMetaData fmetaImport = JCo.createListMetaData("MATERIAL_IMPORTS");
		fmetaImport.add("DESTINATION", JCoMetaData.TYPE_CHAR, 128, 0, 0, null, null, JCoListMetaData.IMPORT_PARAMETER, null, null);
		fmetaImport.add("PREFIX", JCoMetaData.TYPE_CHAR, 128, 0, 0, null, null, JCoListMetaData.IMPORT_PARAMETER, null, null);
		
		fmetaImport.lock();

		final JCoListMetaData fmetaExport = JCo.createListMetaData("MATERIAL_EXPORTS");
		fmetaExport.add("RETURN", JCoMetaData.TYPE_CHAR, 255, 0, 0, null, null, JCoListMetaData.EXPORT_PARAMETER, null, null);
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
			String firstLine = StringUtils.EMPTY;
			try
			{
				String destination = importParamList.getString("DESTINATION");
				String prefix = importParamList.getString("PREFIX");
				
				if(LOG.isInfoEnabled())
				{
					LOG.info(String.format("Importing material files, destination [%s], prefix [%s]", destination, prefix));
				}
				
				SapBatchListenerI listener = new SAPLoadListener();
				listener.processErpsBatch(destination, prefix);				

				exportParamList.setValue("RETURN", "S");
				
				/*exportParamList.setValue("MESSAGE",
						String.format("%s Material details imported successfully! [ %s ]", new Date()));*/								
			}
			catch (final LoaderException le)
			{
				LOG.warn("Error occured processing batch", le);

				BadDataException[] bdes = le.getBadDataExceptions();
				if (bdes!=null)
				{
					for (int i=0; i<bdes.length; i++)
					{
						LOG.warn("Nested bad data exception", bdes[i]);
					
						if(i == 0)
						{
							if(bdes[0] != null) {
								firstLine = bdes[0].getMessage();
							}
						}
					}
				}
				
				// Make BDEs part of the SAP error message
				String errorMsg = ( le.getNestedException()==null ? le : le.getMessage() ).toString();
				String returnErr = firstLine + ": " + errorMsg;
				returnErr = returnErr.substring(0, Math.min(255, returnErr.length()));
				LOG.info("Error message to SAP: '"+returnErr+"'");
				exportParamList.setValue("RETURN", returnErr);
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
	public String getFunctionName() {
		return functionName;
	}

	/**
	 * @param functionName the functionName to set
	 */
	public void setFunctionName(String functionName) {
		this.functionName = functionName;
	}
	
	

}
