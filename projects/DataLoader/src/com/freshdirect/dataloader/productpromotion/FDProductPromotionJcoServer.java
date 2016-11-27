package com.freshdirect.dataloader.productpromotion;

import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.naming.Context;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.dataloader.response.FDJcoServerResult;
import com.freshdirect.dataloader.sap.ejb.SAPProductPromotionLoaderHome;
import com.freshdirect.dataloader.sap.ejb.SAPProductPromotionLoaderSB;
import com.freshdirect.dataloader.sap.jco.server.FDSapFunctionHandler;
import com.freshdirect.dataloader.sap.jco.server.FdSapServer;
import com.freshdirect.dataloader.util.FDSapHelperUtils;
import com.freshdirect.erp.EnumProductPromotionType;
import com.freshdirect.erp.ErpProductPromotion;
import com.freshdirect.erp.ErpProductPromotionInfo;
import com.freshdirect.framework.util.DateUtil;
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
 * This class will be used for populating the DDPP product promotions via Jco-server registered to ERP system
 *
 * @author kkanuganti
 *
 */
public class FDProductPromotionJcoServer extends FdSapServer
{
	private static final Logger LOG = Logger.getLogger(FDProductPromotionJcoServer.class.getName());
	
	private String serverName;
	
	private String functionName;
	
	int batchNumber;
	
	/**
	 * @param serverName
	 * @param functionName
	 * @param programId
	 */
	public FDProductPromotionJcoServer(String serverName, String functionName, String programId) {
		super();
		this.serverName = serverName;
		this.functionName = functionName;
		this.setProgramId(programId);
	}

	@Override
	protected JCoRepository createRepository()
	{

		final JCoCustomRepository repository = JCo.createCustomRepository("ProductPromotionRepository");
		final JCoRecordMetaData metaDDPPHeaderList = JCo.createRecordMetaData("DDPPHEADERLIST");

		tableMetaDataList.add(new TableMetaData("VKORG", JCoMetaData.TYPE_CHAR, 4, 0, "Sales Organization"));
		tableMetaDataList.add(new TableMetaData("VTWEG", JCoMetaData.TYPE_CHAR, 2, 0, "Distribution Channel"));
		tableMetaDataList.add(new TableMetaData("PROMO", JCoMetaData.TYPE_CHAR, 10, 0, "Promotion"));
		tableMetaDataList.add(new TableMetaData("PRTYP", JCoMetaData.TYPE_NUM, 3, 0, "Promotion Type"));
		tableMetaDataList.add(new TableMetaData("PRNAM", JCoMetaData.TYPE_CHAR, 30, 0, "Promotion Name"));
		tableMetaDataList.add(new TableMetaData("STRDATE", JCoMetaData.TYPE_CHAR, 10, 0, "Start Date"));
		tableMetaDataList.add(new TableMetaData("STRTIME", JCoMetaData.TYPE_CHAR, 6, 0, "Start Time"));
		tableMetaDataList.add(new TableMetaData("ENDDATE", JCoMetaData.TYPE_CHAR, 10, 0, "End Date"));
		tableMetaDataList.add(new TableMetaData("ENDTIME", JCoMetaData.TYPE_CHAR, 6, 0, "End Time"));
		tableMetaDataList.add(new TableMetaData("PRSTAT", JCoMetaData.TYPE_CHAR, 1, 0, "Status"));
		tableMetaDataList.add(new TableMetaData("PRTX", JCoMetaData.TYPE_NUM, 50, 0, "Promotion Text"));

		createTableRecord(metaDDPPHeaderList, tableMetaDataList);
		metaDDPPHeaderList.lock();
		repository.addRecordMetaDataToCache(metaDDPPHeaderList);

		final JCoRecordMetaData metaDDPPDtlList = JCo.createRecordMetaData("DDPPITEMLIST");
		tableMetaDataList = new ArrayList<TableMetaData>();
	
		tableMetaDataList.add(new TableMetaData("VKORG", JCoMetaData.TYPE_CHAR, 4, 0, "Sales Organization"));
		tableMetaDataList.add(new TableMetaData("VTWEG", JCoMetaData.TYPE_CHAR, 2, 0, "Distribution Channel"));
		tableMetaDataList.add(new TableMetaData("PROMO", JCoMetaData.TYPE_CHAR, 10, 0, "Promotion"));
		tableMetaDataList.add(new TableMetaData("PRTYP", JCoMetaData.TYPE_NUM, 3, 0, "Promotion Type"));
		tableMetaDataList.add(new TableMetaData("BISMT", JCoMetaData.TYPE_CHAR, 18, 0, "Web SKU ID"));
		tableMetaDataList.add(new TableMetaData("MATNR", JCoMetaData.TYPE_CHAR, 18, 0, "SAP Material Number"));
		tableMetaDataList.add(new TableMetaData("ZZDEPT", JCoMetaData.TYPE_CHAR, 3, 0, "Department"));
		tableMetaDataList.add(new TableMetaData("ZONEID", JCoMetaData.TYPE_CHAR, 6, 0, "Zone ID (Pricing Zone)"));
		tableMetaDataList.add(new TableMetaData("PRIORY", JCoMetaData.TYPE_NUM, 3, 0, "Priority � Order Priority"));
		tableMetaDataList.add(new TableMetaData("FEATR", JCoMetaData.TYPE_CHAR, 1, 0, "Featured"));
		tableMetaDataList.add(new TableMetaData("FEATRN", JCoMetaData.TYPE_NUM, 3, 0, "Featured Header"));
		tableMetaDataList.add(new TableMetaData("ZCATEGORY", JCoMetaData.TYPE_NUM, 50, 0, "Category"));
		tableMetaDataList.add(new TableMetaData("CATPOS", JCoMetaData.TYPE_NUM, 3, 0, "Category Position"));

		createTableRecord(metaDDPPDtlList, tableMetaDataList);
		metaDDPPDtlList.lock();
		repository.addRecordMetaDataToCache(metaDDPPDtlList);

		final JCoListMetaData fmetaImport = JCo.createListMetaData("DDPP_PROMO_IMPORTS");
		fmetaImport.add("ZDDPP_PROMO_HDR", JCoMetaData.TYPE_TABLE, metaDDPPHeaderList, JCoListMetaData.IMPORT_PARAMETER);
		fmetaImport.add("ZDDPP_PROMO_DTL", JCoMetaData.TYPE_TABLE, metaDDPPDtlList, JCoListMetaData.IMPORT_PARAMETER);
		fmetaImport.lock();

		final JCoListMetaData fmetaExport = JCo.createListMetaData("DDPP_PROMO_EXPORTS");
		fmetaExport.add("RETURN", JCoMetaData.TYPE_CHAR, 1, 0, 0, null, null, JCoListMetaData.EXPORT_PARAMETER, null, null);
		fmetaExport.add("MESSAGE", JCoMetaData.TYPE_CHAR, 220, 0, 0, null, null, JCoListMetaData.EXPORT_PARAMETER, null, null);
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
			final FDJcoServerResult result = new FDJcoServerResult();
			final JCoParameterList exportParamList = function.getExportParameterList();
			
			SAPProductPromotionLoaderSB sb = getSAPProductPromotionLoader();
			String ppStatus = "L";
						
			try
			{
				final JCoTable ddppPromoHeaderTable = function.getTableParameterList().getTable("ZDDPP_PROMO_HDR");
				final JCoTable ddppPromoDtlTable = function.getTableParameterList().getTable("ZDDPP_PROMO_DTL");

				final Map<String, ErpProductPromotion> productPromotionMap = new HashMap<String, ErpProductPromotion>();
				List<ErpProductPromotionInfo> ppInfoList = new ArrayList<ErpProductPromotionInfo>();

				//promotion header table
				for (int i = 0; i < ddppPromoHeaderTable.getNumRows(); i++)
				{
					ddppPromoHeaderTable.setRow(i);
					
					final String promoId = FDSapHelperUtils.getString(ddppPromoHeaderTable.getString("PROMO"));
					final Integer promoType = ddppPromoHeaderTable.getInt("PRTYP");
					EnumProductPromotionType promoEnumType = EnumProductPromotionType.getEnum(promoType);
					
					final String promoName = FDSapHelperUtils.getString(ddppPromoHeaderTable.getString("PRNAM"));
					final String promoDesc = FDSapHelperUtils.getString(ddppPromoHeaderTable.getString("PRTX"));
					final String startDate = FDSapHelperUtils.getString(ddppPromoHeaderTable.getString("STRDATE"));
					final String endDate = FDSapHelperUtils.getString(ddppPromoHeaderTable.getString("ENDDATE"));
					final String status = FDSapHelperUtils.getString(ddppPromoHeaderTable.getString("PRSTAT"));
					//final String promoText = FDSapHelperUtils.getString(ddppPromoHeaderTable.getString("PRTX"));
					
					if (LOG.isDebugEnabled())
					{
						LOG.debug("Promo Header Record For Promo Id:" + promoId + "\t Promo Type:" + promoType + "\t Start Date:" + startDate
								+ "\t Promo Name:" + promoName + "\t status:" + status);
					}

					if (!productPromotionMap.containsKey(promoId) && promoEnumType != null)
					{
						final ErpProductPromotion productPromotion = new ErpProductPromotion();
						
						productPromotion.setErpPromtoionId(promoId);
						productPromotion.setPpType(promoEnumType.getName());
						
						productPromotion.setPpName(promoName);
						productPromotion.setPpDescription(promoDesc);
						productPromotion.setPpStatus(status);
						productPromotion.setStartDate(DateUtil.parseMDY2(startDate));
						productPromotion.setEndDate(DateUtil.parseMDY2(endDate));
						
						productPromotionMap.put(promoId, productPromotion);
					} 
					else
					{
						LOG.warn("[PRODUCT_PROMOTION: INVALID_PROMOTION_TYPE]: "+ promoType + " is not a valid value for 'PRTYP' of 'ZDDPP_PROMO_HDR' with promotion id:" + promoId);
					}
					
					ddppPromoHeaderTable.nextRow();
				}

				//promotion detail table
				for (int i = 0; i < ddppPromoDtlTable.getNumRows(); i++)
				{
					ddppPromoDtlTable.setRow(i);
					
					final String promoId = FDSapHelperUtils.getString(ddppPromoDtlTable.getString("PROMO"));
					final Integer ppType = ddppPromoDtlTable.getInt("PRTYP");
					EnumProductPromotionType promoType = EnumProductPromotionType.getEnum(ppType);
					
					String materialID = FDSapHelperUtils.getString(ddppPromoDtlTable.getString("MATNR"));
					String dept = FDSapHelperUtils.getString(ddppPromoDtlTable.getString("ZZDEPT"));
					String pricingZoneId = FDSapHelperUtils.getString(ddppPromoDtlTable.getString("ZONEID"));
					String skuCode = FDSapHelperUtils.getString(ddppPromoDtlTable.getString("BISMT"));
					final Integer priority = ddppPromoDtlTable.getInt("PRIORY");
					String featured = FDSapHelperUtils.getString(ddppPromoDtlTable.getString("FEATR"));
					String featuredHeader = FDSapHelperUtils.getString(ddppPromoDtlTable.getString("FEATRN"));
					String category = FDSapHelperUtils.getString(ddppPromoDtlTable.getString("ZCATEGORY"));
					String categoryPosition = FDSapHelperUtils.getString(ddppPromoDtlTable.getString("CATPOS"));
					String salesOrg = FDSapHelperUtils.getString(ddppPromoDtlTable.getString("VKORG"));
					String distChannel = FDSapHelperUtils.getString(ddppPromoDtlTable.getString("VTWEG"));

					if (LOG.isDebugEnabled())
					{
						LOG.debug("Promo Dtl record For Promo Id:" + promoId + "\t Promo Type:" + ppType + "\t Material No:" + materialID
								+ "\t Sku Code:" + skuCode + "\t Priority:" + priority + "\t Featured:" + featured + "\t Featured Header:" + featuredHeader + "\t PricingZoneId:" + pricingZoneId);
					}
					
					if(null != promoType)
					{
						ErpProductPromotionInfo ppInfo = new ErpProductPromotionInfo();
						ppInfoList.add(ppInfo);
						
						ppInfo.setType(promoType.getName());		
						ppInfo.setSkuCode(skuCode);
						ppInfo.setMatNumber(materialID);
						ppInfo.setErpDeptId(dept);
						
						if(EnumProductPromotionType.PRODUCTS_ASSORTMENTS.equals(promoType))
						{
							ppInfo.setErpCategory(category);
							ppInfo.setErpCatPosition(Integer.valueOf(categoryPosition));
							if (pricingZoneId == null || "".equals(pricingZoneId.trim())){
								pricingZoneId = FDSapHelperUtils.MASTER_DEFAULT_ZONE;
							}
						}
						ppInfo.setZoneId("0000" + pricingZoneId);
						ppInfo.setPriority(priority);
						ppInfo.setFeaturedHeader(featuredHeader);
						ppInfo.setFeatured(featured);
						ppInfo.setErpPromtoionId(promoId);
						ppInfo.setSalesOrg(salesOrg);
						ppInfo.setDistChannel(distChannel);
					}
					else
					{
						//Not a valid product promotion type.
						LOG.error("[PRODUCT_PROMOTION: INVALID_PROMOTION_TYPE]:Cannot add product promotion(s) for material " + materialID + ", because of invlid promotion type "
								+ ppType);			
					}

					ddppPromoDtlTable.nextRow();
				}

				if (FDJcoServerResult.OK_STATUS.equals(result.getStatus()))
				{	
					batchNumber = sb.getNextBatchNumber();
					Timestamp timestamp = new Timestamp(new java.util.Date().getTime());
					sb.createHistoryData(timestamp, batchNumber);
					
					List<ErpProductPromotion> ppList = new ArrayList<ErpProductPromotion>();
					for(Map.Entry<String, ErpProductPromotion> entry: productPromotionMap.entrySet())
					{
				        ppList.add(entry.getValue());
				    }
					
					if(null != ppInfoList && !ppInfoList.isEmpty())
					{
						LOG.info("No. of valid promotion products in batch: "+ batchNumber + ", size: "+ ppInfoList.size());
						sb.loadProductPromotions(ppList, ppInfoList, batchNumber);
						exportParamList.setValue("RETURN", "S");
						exportParamList.setValue("MESSAGE", 
								String.format("Product Promotion details imported for %s products successfully! [ %s ]", ppInfoList.size(), new Date()));
						
						ppStatus = "S";
					} 
					else 
					{
						exportParamList.setValue("RETURN", "E");
						exportParamList.setValue("MESSAGE", "Export Failed:" + "No valid promotion products found in the export.");
						
						ppStatus = "F";
					}					
				}
				else
				{
					exportParamList.setValue("RETURN", "E");
					exportParamList.setValue("MESSAGE",
							result.getError().toString().substring(0, Math.min(255, result.getError().toString().length())));
					
					ppStatus = "F";
				}
			}
			catch (final Exception e)
			{
				LOG.error("Error importing product promotion: ", e);
				exportParamList.setValue("RETURN", "E");
				exportParamList.setValue("MESSAGE",
						"Error importing product promotion: " + e.toString().substring(0, Math.min(220, e.toString().length())));
				ppStatus = "F";
			}
			finally
			{
				try
				{
					sb.updateHistoryData(batchNumber, ppStatus);
				} 
				catch (Exception e)
				{
					LOG.error("[PRODUCT_PROMOTION: HISTORY_UPDATE_ERROR]:Exception occured: ", e);
				}
			}
		}
	}

	private SAPProductPromotionLoaderSB getSAPProductPromotionLoader() 
	{
		Context ctx = null;
		SAPProductPromotionLoaderSB sb = null;
		try
		{
			ctx = ErpServicesProperties.getInitialContext();
			SAPProductPromotionLoaderHome mgr = (SAPProductPromotionLoaderHome) ctx.lookup("freshdirect.dataloader.SAPProductPromotionLoader");
			sb = mgr.create();	        		 
			
		} catch(NamingException ex) {
			LOG.error("Exception occured:"+ex);
			throw new EJBException("Failed to load the initial context: " + ex.toString());
		} catch(RemoteException ex) {
			LOG.error("Exception occured:"+ex);
			throw new EJBException("Failed to get the SAPProductPromotionLoaderSB: " + ex.toString());
		} catch(CreateException ex) {
			LOG.error("Exception occured:"+ex);
			throw new EJBException("Failed to get the SAPProductPromotionLoaderSB: " + ex.toString());
		} finally {
			if (ctx != null) {
				try {
					ctx.close();
				} catch (NamingException e) {
					LOG.error("Exception occured:"+e);
				}
			}
		}
		return sb;
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