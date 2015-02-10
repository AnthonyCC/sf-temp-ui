package com.freshdirect.dataloader.invoice;

import org.apache.log4j.Logger;

import com.freshdirect.dataloader.payment.InvoiceBatchListenerI;
import com.freshdirect.dataloader.payment.InvoiceLoadListener;
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
 * This class will be used for populating the invoice for orders via Jco-server (3.0) registered to the ERP system
 *
 * @author kkanuganti
 */
public class FDInvoiceBatchJcoServer extends FdSapServer
{
	private static final Logger LOG = Logger.getLogger(FDInvoiceBatchJcoServer.class.getName());

	private String serverName;

	private String functionName;
	
	/**
	 * @param serverName
	 * @param functionName
	 * @param programId
	 */
	public FDInvoiceBatchJcoServer(String serverName, String functionName, String programId) {
		super();
		this.serverName = serverName;
		this.functionName = functionName;
		this.setProgramId(programId);
	}

	@Override
	protected JCoRepository createRepository()
	{
		final JCoCustomRepository repository = JCo.createCustomRepository("FDInvoiceBatchRepository");
		
		final JCoListMetaData fmetaImport = JCo.createListMetaData("INVOICE_IMPORTS");
		fmetaImport.add("FOLDER", JCoMetaData.TYPE_CHAR, 128, 0, 0, null, null, JCoListMetaData.IMPORT_PARAMETER, null, null);
		fmetaImport.add("FILENAME", JCoMetaData.TYPE_CHAR, 128, 0, 0, null, null, JCoListMetaData.IMPORT_PARAMETER, null, null);
		fmetaImport.lock();

		final JCoListMetaData fmetaExport = JCo.createListMetaData("INVOICE_EXPORTS");
		fmetaExport.add("RETURN", JCoMetaData.TYPE_CHAR, 5000, 0, 0, null, null, JCoListMetaData.EXPORT_PARAMETER, null, null);
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
			try
			{
				String folder = importParamList.getString("FOLDER");
				String fileName = importParamList.getString("FILENAME");
				
				if(LOG.isInfoEnabled())
				{
					LOG.info(String.format("Importing invoice files, fileName [%s] from folder [%s]", folder, fileName));
				}
				
				InvoiceBatchListenerI listener = new InvoiceLoadListener();
				listener.processInvoiceBatch(folder, fileName);				

				exportParamList.setValue("RETURN", "S");
				/*exportParamList.setValue("MESSAGE",
						String.format("%s Invoice details imported successfully! [ %s ]", new Date()));*/								
			}
			catch (final Exception e)
			{
				LOG.error("Error importing invoice(s): Exception is ", e);
				exportParamList.setValue("RETURN", "E");
				/*exportParamList.setValue("MESSAGE",
						"Error importing invoice(s) " + e.toString().substring(0, Math.min(230, e.toString().length())));*/
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
