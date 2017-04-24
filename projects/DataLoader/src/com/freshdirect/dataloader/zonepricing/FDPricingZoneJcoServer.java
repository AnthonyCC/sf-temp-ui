package com.freshdirect.dataloader.zonepricing;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJBException;
import javax.naming.Context;
import javax.naming.NamingException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.customer.EnumZoneServiceType;
import com.freshdirect.customer.ErpZoneMasterInfo;
import com.freshdirect.customer.ErpZoneRegionInfo;
import com.freshdirect.customer.ErpZoneRegionZipInfo;
import com.freshdirect.dataloader.LoaderException;
import com.freshdirect.dataloader.sap.ejb.SAPZoneInfoLoaderHome;
import com.freshdirect.dataloader.sap.ejb.SAPZoneInfoLoaderSB;
import com.freshdirect.dataloader.sap.jco.server.FDSapFunctionHandler;
import com.freshdirect.dataloader.sap.jco.server.FdSapServer;
import com.freshdirect.dataloader.util.FDSapHelperUtils;
import com.freshdirect.fdlogistics.services.impl.LogisticsServiceLocator;
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
 * This class represents the processing of pricing zone details sent by SAP to Store front
 *
 * @author kkanuganti
 *
 */
public class FDPricingZoneJcoServer extends FdSapServer
{
	private static final Logger LOG = Logger.getLogger(FDPricingZoneJcoServer.class.getName());

	private String serverName;
	
	private String functionName;
	
	/**
	 * @param serverName
	 * @param functionName
	 * @param programId
	 */
	public FDPricingZoneJcoServer(String serverName, String functionName, String programId)  {
		super();
		this.serverName = serverName;
		this.functionName = functionName;
		this.setProgramId(programId);
	}
	
	@Override
	protected JCoRepository createRepository()
	{
		final JCoCustomRepository repository = JCo.createCustomRepository("MasterPricingZoneRepository");
		final JCoRecordMetaData metaMasterPricingZoneList = JCo.createRecordMetaData("ZONEPRICELIST");

		tableMetaDataList.add(new TableMetaData("Z_ZONEID_ID", JCoMetaData.TYPE_CHAR, 10, 0, "Zone Id"));
		tableMetaDataList.add(new TableMetaData("Z_REGION_ID", JCoMetaData.TYPE_CHAR, 10, 0, "Region Id"));
		tableMetaDataList.add(new TableMetaData("Z_SERV_TYPE", JCoMetaData.TYPE_NUM, 2, 0, "Service Type"));
		tableMetaDataList.add(new TableMetaData("Z_ZONE_DESC", JCoMetaData.TYPE_CHAR, 40, 0, "Zone Description"));
		tableMetaDataList.add(new TableMetaData("Z_ZONE_ZIPCODE", JCoMetaData.TYPE_CHAR, 5, 0, "Material Number"));

		createTableRecord(metaMasterPricingZoneList, tableMetaDataList);
		metaMasterPricingZoneList.lock();
		repository.addRecordMetaDataToCache(metaMasterPricingZoneList);

		final JCoListMetaData fmetaImport = JCo.createListMetaData("ZONE_PRICE_IMPORTS");
		fmetaImport.add("ZSD_ZONE_INFO", JCoMetaData.TYPE_TABLE, metaMasterPricingZoneList, JCoListMetaData.IMPORT_PARAMETER);
		fmetaImport.lock();

		final JCoListMetaData fmetaExport = JCo.createListMetaData("ZONE_PRICE_EXPORTS");
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
				final JCoTable zonePricingTable = function.getTableParameterList().getTable("ZSD_ZONE_INFO");
				
				List<ErpZoneMasterInfo> zoneInfos = new ArrayList<ErpZoneMasterInfo>();
				
				if (zonePricingTable != null)
				{
					for (int i = 0; i < zonePricingTable.getNumRows(); i++)
					{
						zonePricingTable.setRow(i);

						String zoneId = FDSapHelperUtils.getString(zonePricingTable.getString("Z_ZONEID_ID"));
						String regionId = FDSapHelperUtils.getString(zonePricingTable.getString("Z_REGION_ID"));
						final String serviceType = FDSapHelperUtils.getString(zonePricingTable.getString("Z_SERV_TYPE"));
						final String zoneDesc = FDSapHelperUtils.getString(zonePricingTable.getString("Z_ZONE_DESC"));
						final String zipCode = FDSapHelperUtils.getString(zonePricingTable.getString("Z_ZONE_ZIPCODE"));

						if (LOG.isDebugEnabled())
						{
							LOG.debug("Got Zone Pricing Record -> Zone ID :" + zoneId + "\t Region ID:" + regionId
									+ "\t Service Type:" + serviceType + "\t Zone Desc:" + zoneDesc + "\t Zip Code:" + zipCode);
						}

						if ((zoneId == null || StringUtils.isEmpty(zoneId))
								|| (regionId == null || StringUtils.isEmpty(regionId)))
						{
							throw new LoaderException("Zone or Region can't be NULL. Zone pricing record -> Zone ID :" + zoneId
									+ "\t Region ID:" + regionId + "\t Service Type:" + serviceType + "\t Zone Desc:" + zoneDesc
									+ "\t Zip Code:" + zipCode);
						}
						
						constructZoneInfoModel(zoneId, regionId, serviceType, zoneDesc, zipCode, zoneInfos);
						
						zonePricingTable.nextRow();					
					}					

					if(zoneInfos.size() > 0) 
					{
							storeZoneInfo(zoneInfos);

					}
									
					exportParamList.setValue("RETURN", "S");
					exportParamList
								.setValue("MESSAGE", String.format("Pricing zone details imported successfully [ %s ]", new Date()));
				} 
				else
				{
					exportParamList.setValue("RETURN", "W");
					exportParamList.setValue("MESSAGE", String.format("No Pricing zone details to process. Please check the export data [ %s ]", new Date()));
				}
			}
			catch (final Exception e)
			{
				LOG.error("Error importing pricing zone(s): ", e);
				exportParamList.setValue("RETURN", "E");
				exportParamList.setValue("MESSAGE",
						"Error importing pricing zone(s) " + e.toString().substring(0, Math.min(230, e.toString().length())));
			}
		}
	}


	/**
	 * @param zoneId
	 * @param regionId
	 * @param serviceType
	 * @param desc
	 * @param zipCode
	 * @param zoneInfos
	 * @throws LoaderException
	 */
	@SuppressWarnings("unchecked")
	public void constructZoneInfoModel(String zoneId, String regionId,
			String serviceType, String desc, String zipCode,
			List<ErpZoneMasterInfo> zoneInfos) throws LoaderException
	{
		ErpZoneMasterInfo zone = null;
		ErpZoneRegionInfo region = null;
		ErpZoneRegionZipInfo zipInfo = null;
		List<ErpZoneRegionZipInfo> erpZoneRegionZipList = null;
	
		try
		{
			for (int i = 0; i < zoneInfos.size(); i++)
			{
				ErpZoneMasterInfo masterInfo = zoneInfos.get(i);
				if (masterInfo.getSapId().equalsIgnoreCase(zoneId))
				{
					zone = masterInfo;
					region = masterInfo.getRegion();
					erpZoneRegionZipList = region.getZoneRegionZipList();
					break;
				}
			}
			if (region == null)
			{
				region = new ErpZoneRegionInfo(regionId, null);
			}
			if (erpZoneRegionZipList == null
					&& (zipCode != null && zipCode.trim().length() > 0))
			{
				erpZoneRegionZipList = new ArrayList<ErpZoneRegionZipInfo>();
				region.setZoneRegionZipList(erpZoneRegionZipList);
			}

			if (zipCode != null && zipCode.trim().length() > 0) {
				zipInfo = new ErpZoneRegionZipInfo(region, zipCode);
				region.addZoneRegionZipModel(zipInfo);
			}
			EnumZoneServiceType enumServType = EnumZoneServiceType
					.getEnumByCode(serviceType);
			if (zone == null) {
				zone = new ErpZoneMasterInfo(zoneId, region, enumServType, desc);
				zoneInfos.add(zone);
			}

		} catch (Exception e) {
			LOG.error(e);
			throw new LoaderException(e,
					"Error in constructing ZoneInfoModel: "
							+ e.getMessage());
		}

	}
	
	/**
	 * Method to persist / store pricing zone details
	 * 
	 * @param zoneInfos
	 */
	public void storeZoneInfo(List<ErpZoneMasterInfo> zoneInfos)
	{
		Context ctx = null;
		try 
		{
			if(FDStoreProperties.isStorefront2_0Enabled()){
				LogisticsServiceLocator.getInstance().getCommerceService().loadData(zoneInfos);
			}else{			
			ctx = ErpServicesProperties.getInitialContext();
			SAPZoneInfoLoaderHome mgr = (SAPZoneInfoLoaderHome) ctx.lookup("freshdirect.dataloader.SAPZoneInfoLoader");
			SAPZoneInfoLoaderSB sb = mgr.create();
			sb.loadData(zoneInfos);	
			}
			
		} catch(Exception ex) {
			throw new EJBException("Storing pricing zone info failed. Exception:" + ex.toString());
		} finally {
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
}