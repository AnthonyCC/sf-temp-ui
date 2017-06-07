package com.freshdirect.dataloader.inventory;


import java.rmi.RemoteException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.naming.Context;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.customer.ErpRestrictedAvailabilityModel;
import com.freshdirect.dataloader.sap.jco.server.FDSapFunctionHandler;
import com.freshdirect.dataloader.sap.jco.server.FdSapServer;
import com.freshdirect.dataloader.util.FDSapHelperUtils;
import com.freshdirect.erp.ejb.ErpInventoryManagerHome;
import com.freshdirect.erp.ejb.ErpInventoryManagerSB;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.payment.service.FDECommerceService;
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
 * This class represents the processing of product availability details sent by SAP to Store front
 * 
 * @author kkanuganti
 *
 */
public class FDRestrictedAvailabilityJcoServer extends FdSapServer
{
	private static final Logger LOG = Logger.getLogger(FDRestrictedAvailabilityJcoServer.class.getName());

	private String serverName;
	
	private String functionName;
	
	/**
	 * @param serverName
	 * @param functionName
	 * @param programId
	 */
	public FDRestrictedAvailabilityJcoServer(String serverName, String functionName, String programId) {
		super();
		this.serverName = serverName;
		this.functionName = functionName;
		this.setProgramId(programId);
	}
	
	@Override
	protected JCoRepository createRepository()
	{
		final JCoCustomRepository repository = JCo.createCustomRepository("RestrictedAvailabilityRepository");
		final JCoRecordMetaData metaRestrictedAvailabilityList = JCo.createRecordMetaData("RESTRICTAVAILABILITYLIST");
		
		tableMetaDataList.add(new TableMetaData("MATNR", JCoMetaData.TYPE_CHAR, 18, 0, "Material No."));
		tableMetaDataList.add(new TableMetaData("DATE", JCoMetaData.TYPE_DATE, 8, 0, "Delivery Date"));
		tableMetaDataList.add(new TableMetaData("INDIC", JCoMetaData.TYPE_CHAR, 1, 0, ""));
		
		createTableRecord(metaRestrictedAvailabilityList, tableMetaDataList);
		metaRestrictedAvailabilityList.lock();
		repository.addRecordMetaDataToCache(metaRestrictedAvailabilityList);

		final JCoListMetaData fmetaImport = JCo.createListMetaData("RESTRICT_AVAILABILITY_IMPORTS");
		fmetaImport.add("Z_RESTRICT_ATP", JCoMetaData.TYPE_TABLE, metaRestrictedAvailabilityList, JCoListMetaData.IMPORT_PARAMETER);
		fmetaImport.lock();

		final JCoListMetaData fmetaExport = JCo.createListMetaData("RESTRICT_AVAILABILITY_EXPORTS");
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
			
			Set<ErpRestrictedAvailabilityModel> restrictedInfos = new HashSet<ErpRestrictedAvailabilityModel>();
			Set<String> materialsDeleted = new HashSet<String>();

			try
			{
				final JCoTable materialRestrictionTable = function.getTableParameterList().getTable("Z_RESTRICT_ATP");
				for (int i = 0; i < materialRestrictionTable.getNumRows(); i++)
				{
					materialRestrictionTable.setRow(i);

					final String matNo = FDSapHelperUtils.getString(materialRestrictionTable.getString("MATNR"));
					final Date resDate = materialRestrictionTable.getDate("DATE");
					final String delIndicator = FDSapHelperUtils.getString(materialRestrictionTable.getString("INDIC"));

					if (LOG.isDebugEnabled())
					{
						LOG.debug("Got Restricted availability record For Material No:" + matNo + "\t Restricted Date:" + resDate
								+ "\t Delete Indicator:" + delIndicator);
					}
					
					if("X".equals(delIndicator))
					{
						materialsDeleted.add(matNo);
					} 
					else
					{
						ErpRestrictedAvailabilityModel model = new ErpRestrictedAvailabilityModel(matNo, resDate, false);
						restrictedInfos.add(model);
						materialsDeleted.add(matNo);
					}

					materialRestrictionTable.nextRow();
				}
				
				if(restrictedInfos.size() > 0)
				{
					updateRestrictedInfos(restrictedInfos, materialsDeleted);
			
					exportParamList.setValue("RETURN", "S");
					exportParamList.setValue("MESSAGE", String.format("Product restriction availability updated successfully! [ %s ]", new Date()));
				} 
				else 
				{
					exportParamList.setValue("RETURN", "W");
					exportParamList.setValue("MESSAGE", String.format("No Product restriction availability to process. Please check the export data [ %s ]", new Date()));
				}
			}
			catch (final Exception e)
			{
				LOG.error("Error importing product restriction availability. Exception is " + e);
				exportParamList.setValue("RETURN", "E");
				exportParamList.setValue("MESSAGE",
						"Error importing product restriction availability " + e.toString().substring(0, Math.min(225, e.toString().length())));
			}

		}
	}

	private void updateRestrictedInfos(Set<ErpRestrictedAvailabilityModel> restrictedInfos, Set<String> deletedMaterials)
			throws NamingException, EJBException, CreateException, FDResourceException, RemoteException
	{
		Context ctx = null;
		try
		{
			ctx = ErpServicesProperties.getInitialContext();
			ErpInventoryManagerHome mgr = (ErpInventoryManagerHome) ctx.lookup("freshdirect.erp.InventoryManager");
			ErpInventoryManagerSB sb = mgr.create();
			if(FDStoreProperties.isSF2_0_AndServiceEnabled("erp.ejb.ErpInventoryManagerSB")){
				FDECommerceService.getInstance().updateRestrictedInfos(restrictedInfos, deletedMaterials);
			}else{
				sb.updateRestrictedInfos(restrictedInfos, deletedMaterials);
			}
		} 
		catch(Exception ex)
		{
			throw new EJBException(ex.toString());
		} 
		finally 
		{
			if (ctx != null)
			{
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
	 * @return String
	 */
	public String getFunctionName() {
		return functionName;
	}

	/**
	 * @param functionName
	 */
	public void setFunctionName(String functionName) {
		this.functionName = functionName;
	}

}