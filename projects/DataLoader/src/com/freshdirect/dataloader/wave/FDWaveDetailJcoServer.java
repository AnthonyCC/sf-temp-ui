package com.freshdirect.dataloader.wave;

import java.rmi.RemoteException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.naming.Context;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.customer.ErpShippingInfo;
import com.freshdirect.customer.ejb.ErpCustomerManagerHome;
import com.freshdirect.customer.ejb.ErpCustomerManagerSB;
import com.freshdirect.dataloader.response.FDJcoServerResult;
import com.freshdirect.dataloader.sap.jco.server.FDSapFunctionHandler;
import com.freshdirect.dataloader.sap.jco.server.FdSapServer;
import com.freshdirect.ecomm.gateway.OrderResourceApiClient;
import com.freshdirect.ecomm.gateway.OrderResourceApiClientI;
import com.freshdirect.fdstore.FDStoreProperties;
import com.sap.conn.jco.JCo;
import com.sap.conn.jco.JCoCustomRepository;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoFunctionTemplate;
import com.sap.conn.jco.JCoListMetaData;
import com.sap.conn.jco.JCoMetaData;
import com.sap.conn.jco.JCoParameterList;
import com.sap.conn.jco.JCoRecordMetaData;
import com.sap.conn.jco.JCoRepository;
import com.sap.conn.jco.JCoTable;
import com.sap.conn.jco.server.JCoServerContext;
import com.sap.conn.jco.server.JCoServerFunctionHandler;


/**
 * This class represents the processing of wave details sent by SAP to Store front after all waves have been dropped
 *
 * @author kkanuganti
 *
 */
public class FDWaveDetailJcoServer extends FdSapServer
{

	private static final Logger LOG = Logger.getLogger(FDWaveDetailJcoServer.class.getName());

	private String serverName;
	
	private String functionName;
	
	/**
	 * @param serverName
	 * @param functionName
	 * @param programId
	 */
	public FDWaveDetailJcoServer(String serverName, String functionName, String programId) {
		super();
		this.serverName = serverName;
		this.functionName = functionName;
		this.setProgramId(programId);
	}

	@Override
	protected JCoRepository createRepository()
	{
		final JCoCustomRepository repository = JCo.createCustomRepository("WaveDetailRepository");
		final JCoRecordMetaData metaWaveList = JCo.createRecordMetaData("WAVELIST");

		tableMetaDataList.add(new TableMetaData("BSTNK", JCoMetaData.TYPE_CHAR, 20, 0, "WebOrder No"));
		tableMetaDataList.add(new TableMetaData("VBELN", JCoMetaData.TYPE_CHAR, 10, 0, "Sap Order No"));
		tableMetaDataList.add(new TableMetaData("ZZSWAVENO", JCoMetaData.TYPE_NUM, 6, 0, "Wave No"));
		// Start APPDEV-5319  As part of HRy SAP is no more going to pass below attributes 
		
		//tableMetaDataList.add(new TableMetaData("ZZTRKNO", JCoMetaData.TYPE_NUM, 6, 0, "Truck No"));
		//tableMetaDataList.add(new TableMetaData("ZZSTOPSEQ", JCoMetaData.TYPE_CHAR, 5, 0, "Stop No"));

		//END APPDEV-5319
		createTableRecord(metaWaveList, tableMetaDataList);
		metaWaveList.lock();

		repository.addRecordMetaDataToCache(metaWaveList);

		final JCoListMetaData fmetaImport = JCo.createListMetaData("WAVE_IMPORTS");
		fmetaImport.add("WAVE", JCoMetaData.TYPE_TABLE, metaWaveList, JCoListMetaData.IMPORT_PARAMETER);
		fmetaImport.lock();

		final JCoListMetaData fmetaExport = JCo.createListMetaData("WAVE_EXPORTS");
		fmetaExport.add("RETURN", JCoMetaData.TYPE_CHAR, 1, 0, 0, null, null, JCoListMetaData.EXPORT_PARAMETER, null, null);
		fmetaExport.add("MESSAGE", JCoMetaData.TYPE_CHAR, 255, 0, 0, null, null, JCoListMetaData.EXPORT_PARAMETER, null, null);
		fmetaExport.lock();

		final JCoFunctionTemplate fT = JCo.createFunctionTemplate(functionName, fmetaImport, fmetaExport, null, fmetaImport, null);
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
			try
			{
				final JCoTable waveTable = function.getTableParameterList().getTable("WAVE");
				final FDJcoServerResult result = new FDJcoServerResult();

				Map<String, ErpShippingInfo> waveEntries = new HashMap<String, ErpShippingInfo>(waveTable.getNumRows());
				if (waveTable != null && waveTable.getNumRows() > 0)
				{
					for (int i = 0; i < waveTable.getNumRows(); i++)
					{
						waveTable.setRow(i);

						final String webOrderNo = waveTable.getString("BSTNK");
						final String sapOrderNo = waveTable.getString("VBELN");
						final String waveNo = leftPad(String.valueOf(waveTable.getInt("ZZSWAVENO")), 6, '0');
						// Start APPDEV-5319  As part of HRy SAP is no more going to pass below attributes 
						
						/*final String truckNumber = leftPad(String.valueOf(waveTable.getInt("ZZTRKNO")), 6, '0');
						final String stopNo = leftPad(waveTable.getString("ZZSTOPSEQ"), 5, '0'); */
                       
						//END APPDEV-5319
						
						if (LOG.isDebugEnabled())
						{
							LOG.debug("Got Wave Record For WebOrderNo:" + webOrderNo + "\t SapOrderNo:" + sapOrderNo + "\t Wave Number:"
									+ waveNo + "\t Route Number:");
						}

						waveEntries.put(webOrderNo, new ErpShippingInfo(waveNo, null, null, 0, 0, 0));
						waveTable.nextRow();
					}

					LOG.info(String.format("Got %s orders for wave details [ %s ]", waveEntries.size(), new Date()));
					
					if (waveEntries.size() > 0 && FDJcoServerResult.OK_STATUS.equals(result.getStatus()))
					{
						updateWaveInfo(waveEntries);
						
						exportParamList.setValue("RETURN", "S");
						exportParamList.setValue("MESSAGE", String.format("Wave details imported for %s orders successfully! [ %s ]",
								waveEntries.size(), new Date()));
					}
					else
					{
						exportParamList.setValue("RETURN", "W");
						exportParamList.setValue("MESSAGE", String.format("No Wave details to process. Please check the export data [ %s ]", new Date()));
					}
				}
			}
			catch (final Exception e)
			{
				LOG.error("Error importing wave details: ", e);
				exportParamList.setValue("RETURN", "E");
				exportParamList.setValue("MESSAGE",
						"Error importing wave details " + e.toString().substring(0, Math.min(226, e.toString().length())));
			}
		}
	}
	
	/**
	 * @param waveEntries
	 * 
	 * @throws EJBException
	 * @throws NamingException 
	 * @throws CreateException 
	 * @throws RemoteException 
	 * @throws FinderException 
	 */
	private void updateWaveInfo(Map<String, ErpShippingInfo> waveEntries) throws EJBException, NamingException, RemoteException, CreateException, FinderException
	{
		Context ctx = null;
		try
		{
			ctx = ErpServicesProperties.getInitialContext();
			ErpCustomerManagerHome mgr = (ErpCustomerManagerHome) ctx.lookup("freshdirect.erp.CustomerManager");
			ErpCustomerManagerSB sb = mgr.create();

			for (String saleId : waveEntries.keySet())
			{ 
				if(FDStoreProperties.isSF2_0_AndServiceEnabled("updateWaveInfo_Api")){
		    		OrderResourceApiClientI service = OrderResourceApiClient.getInstance();
		    		service.updateWaveInfo(saleId, waveEntries.get(saleId));
		    	}else{
				sb.updateWaveInfo(saleId, waveEntries.get(saleId));
		    	}
			}
		}
		finally {
			if (ctx != null) {
				try {
					ctx.close();
				} catch (NamingException e) {
				}
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
