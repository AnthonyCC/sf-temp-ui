package com.freshdirect.dataloader.productpromotion;

import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.naming.Context;
import javax.naming.NamingException;

import org.apache.log4j.Category;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.dataloader.LoaderException;
import com.freshdirect.dataloader.bapi.BapiFunctionI;
import com.freshdirect.dataloader.sap.ejb.SAPProductPromotionLoaderHome;
import com.freshdirect.dataloader.sap.ejb.SAPProductPromotionLoaderSB;
import com.freshdirect.erp.EnumProductPromotionType;
import com.freshdirect.erp.ErpProductPromotion;
import com.freshdirect.erp.ErpProductPromotionInfo;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.framework.util.DateUtil;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.sap.SapProductPromotionConstants;
import com.sap.mw.jco.JCO;
import com.sap.mw.jco.JCO.MetaData;
import com.sap.mw.jco.JCO.ParameterList;

public class SAPProductPromotionContentLoader implements BapiFunctionI, SapProductPromotionConstants{

	public static final String SAP_FUNCTION_ID = "ZDDPP_PUBLISH";
	public static final String SAP_PROGRAM_ID = "ZDDPP_PUB";
	public static final String MASTER_DEFAULT_ZONE="100000";
	
	
	private int batchNumber;
	

	private static Category LOGGER = LoggerFactory.getInstance( SAPProductPromotionContentLoader.class );
	
	static JCO.MetaData smeta[] = new JCO.MetaData[2];
	static int totalSize[] = new int[2];
	static {			
		DataStructure[] sapData1 = new DataStructure[] {
			    //NAME      JCO_TYPE     LEN DEC DESCRIPTION
				//Table: ZDDPP_PROMO_HDR 
				new DataStructure(FIELD_PROMO, JCO.TYPE_CHAR, 10, 0, "Product Promotion"),
				new DataStructure(FIELD_PRTYP, JCO.TYPE_NUM, 3, 0, "Product Promotion Type"),
				new DataStructure(FIELD_PRNAM, JCO.TYPE_CHAR, 30, 0, "Promotion Name"),
				new DataStructure(FIELD_STRDATE, JCO.TYPE_CHAR, 10, 0, "Promotion Start Date"),
				new DataStructure(FIELD_STRTIME, JCO.TYPE_CHAR, 6, 0, "Promotion Start Time"),
				new DataStructure(FIELD_ENDDATE, JCO.TYPE_CHAR, 10, 0, "Promotion End Date"),
				new DataStructure(FIELD_ENDTIME, JCO.TYPE_CHAR, 6, 0, "Promotion End Time"),
				new DataStructure(FIELD_PRSTAT, JCO.TYPE_CHAR, 1, 0, "Status"),
				new DataStructure(FIELD_PRTX, JCO.TYPE_NUM, 50, 0, "Promotion Text")
		};
		
		DataStructure[] sapData2 = new DataStructure[] {
				
				//Table: ZDDPP_PROMO_DTL 
				new DataStructure(FIELD_PROMO, JCO.TYPE_CHAR, 10, 0, "Product Promotion"),
				new DataStructure(FIELD_PRTYP, JCO.TYPE_NUM, 3, 0, "Product Promotion Type"),
				new DataStructure(FIELD_BISMT, JCO.TYPE_CHAR, 18, 0, "Web SKU ID"),
				new DataStructure(FIELD_MATNR, JCO.TYPE_CHAR, 18, 0, "SAP Material Number"),
				new DataStructure(FIELD_ZZDEPT, JCO.TYPE_CHAR, 3, 0, "SAP Department"),
				new DataStructure(FIELD_ZONEID, JCO.TYPE_CHAR, 6, 0, "Pricing Zone ID"),
				new DataStructure(FIELD_PRIORY, JCO.TYPE_NUM, 2, 0, "Priority"),
				new DataStructure(FIELD_FEATR, JCO.TYPE_CHAR, 1, 0, "Featured"),
				new DataStructure(FIELD_FEATRH, JCO.TYPE_NUM, 3, 0, "Featured Header"),		
				new DataStructure(FIELD_CATEGORY, JCO.TYPE_NUM, 50, 0, "Category"),
				new DataStructure(FIELD_CATEGORY_POSITION, JCO.TYPE_NUM, 3, 0, "Category Position"),
								
		};
		smeta[0] = new JCO.MetaData("PARAM1");
		for (DataStructure element : sapData1) {
			smeta[0].addInfo(element.fieldName, element.type, element.length, totalSize[0], element.decimal);
			totalSize[0] += element.length;
		}
		
		smeta[1] = new JCO.MetaData("PARAM2");
		for (DataStructure element : sapData2) {
			smeta[1].addInfo(element.fieldName, element.type, element.length, totalSize[1], element.decimal);
			totalSize[1] += element.length;
		}
		
	}	
	@Override
	public void execute(ParameterList input, ParameterList output,
			ParameterList tables) {

		SAPProductPromotionLoaderSB sb =getSAPProductPromotionLoader();
		String status="L";
		try {
			
			batchNumber = sb.getNextBatchNumber();
			Timestamp timestamp =new Timestamp(new java.util.Date().getTime());
			sb.createHistoryData(timestamp, batchNumber);
			List<ErpProductPromotion> ppList = populateProductPromotion(tables);
			List<ErpProductPromotionInfo> ppInfoList = populateProductPromotionInfo(tables);
//			loadProductPromotions(ppList, ppInfoList);
			if(null != ppInfoList && !ppInfoList.isEmpty()){
				LOGGER.info("No.of valid promotion products in batch:"+batchNumber+", are:"+ppInfoList.size());
				sb.loadProductPromotions(ppList, ppInfoList, batchNumber);
				output.setValue("S", "RETURN");
				output.setValue("Export successful.", "MESSAGE");
				status ="S";
			}else{
				LOGGER.info("No valid promotion products exported in batch:"+batchNumber);
				output.setValue("E", "RETURN");
				output.setValue("Export Failed:"+"No valid promotion products found from the export.", "MESSAGE");				
				status ="F";
			}
			
		} catch (JCO.ConversionException e) {
			output.setValue("E", "RETURN");
			output.setValue("Export Failed:"+e.getMessage(), "MESSAGE");
			LOGGER.error("Exception occured:"+e);
			status ="F";
		} catch (Exception e) {
			output.setValue("E", "RETURN");
			output.setValue("Export Failed:"+e.getMessage(), "MESSAGE");
			LOGGER.error("Exception occured:"+e);
			status ="F";
			
		}finally{
			try {
				sb.updateHistoryData(batchNumber, status);
			} catch (Exception e) {
				LOGGER.error("Exception occured:"+e);
			}
		}
		
	}

	private List<ErpProductPromotionInfo> populateProductPromotionInfo(
			ParameterList tables) {
		JCO.Table ppInfoTable = tables.getTable(TABLE_ZDDPP_PROMO_DTL);
		List<ErpProductPromotionInfo> ppInfoList = new ArrayList<ErpProductPromotionInfo>();
		ppInfoTable.firstRow();
		for(int i=0;i<ppInfoTable.getNumRows();i++){
			try {
				String ppId = ppInfoTable.getString(FIELD_PROMO).trim();
				Integer ppType = ppInfoTable.getInt(FIELD_PRTYP);
				EnumProductPromotionType enumType = EnumProductPromotionType.getEnum(ppType);
				if(null != enumType){
					String skuCode = ppInfoTable.getString(FIELD_BISMT).trim();
					String matNum = ppInfoTable.getString(FIELD_MATNR).trim();
					String dept = ppInfoTable.getString(FIELD_ZZDEPT).trim();
					String zoneId = ppInfoTable.getString(FIELD_ZONEID).trim();
					Integer priority = ppInfoTable.getInt(FIELD_PRIORY);
					String featured = ppInfoTable.getString(FIELD_FEATR);
					String featuredHeader = ppInfoTable.getString(FIELD_FEATRH);
					
					ErpProductPromotionInfo ppInfo= new ErpProductPromotionInfo();				
					ppInfo.setType(enumType.getName());		
					ppInfo.setSkuCode(skuCode);
					ppInfo.setMatNumber(matNum);
					ppInfo.setErpDeptId(dept);
					if(EnumProductPromotionType.PRODUCTS_ASSORTMENTS.equals(enumType)){
						String erpCategory = ppInfoTable.getString(FIELD_CATEGORY);
						Integer erpCatPosition = ppInfoTable.getInt(FIELD_CATEGORY_POSITION);
						ppInfo.setErpCategory(erpCategory);
						ppInfo.setErpCatPosition(erpCatPosition);
						if (zoneId==null || "".equals(zoneId.trim())){
							zoneId=MASTER_DEFAULT_ZONE;
						}
					}
					ppInfo.setZoneId("0000"+zoneId);
					ppInfo.setPriority(priority);
					ppInfo.setFeaturedHeader(featuredHeader);
					ppInfo.setFeatured(featured);
					ppInfo.setErpPromtoionId(ppId);
					ppInfoList.add(ppInfo);
				}else{
					//Not a valid product promotion type.
					LOGGER.warn("Not a valid value for 'PRTYP' of the promotion details with promotion id:"+ppId);				
				}
			} catch (Exception e) {
				LOGGER.error("Exception while parsing a row from 'ZDDPP_PROMO_DTL':"+e.getMessage());
			} finally{
				ppInfoTable.nextRow();
			}
		}
		return ppInfoList;
	}

	private List<ErpProductPromotion> populateProductPromotion(
			ParameterList tables) {
		JCO.Table ppTable = tables.getTable(TABLE_ZDDPP_PROMO_HDR);
		List<ErpProductPromotion> ppList = new ArrayList<ErpProductPromotion>();
		ppTable.firstRow();
		for (int i = 0; i < ppTable.getNumRows(); i++) {
			try {
				String ppId = ppTable.getString(FIELD_PROMO).trim();
				Integer ppType = ppTable.getInt(FIELD_PRTYP);
				EnumProductPromotionType enumType = EnumProductPromotionType.getEnum(ppType);
				if(null != enumType){
					ErpProductPromotion productPromotion = new ErpProductPromotion();
					productPromotion.setPpType(EnumProductPromotionType.getEnum(ppType).getName());
					String ppName = ppTable.getString(FIELD_PRNAM).trim();
					String startDate = ppTable.getString(FIELD_STRDATE).trim();
				//  String startTime = ppTable.getString(FIELD_STRTIME).trim();
					String endDate = ppTable.getString(FIELD_ENDDATE).trim();
				//	String endTime = ppTable.getString(FIELD_ENDTIME).trim();
					String status = ppTable.getString(FIELD_PRSTAT).trim();
					String desc = ppTable.getString(FIELD_PRTX).trim();
					
					productPromotion.setErpPromtoionId(ppId);
					
					productPromotion.setPpName(ppName);
					productPromotion.setPpDescription(desc);
					productPromotion.setPpStatus(status);
					productPromotion.setStartDate(DateUtil.parseMDY2(startDate));
					productPromotion.setEndDate(DateUtil.parseMDY2(endDate));				
					ppList.add(productPromotion);
				} else{
					LOGGER.warn(ppType+" is not a valid value for 'PRTYP' of 'ZDDPP_PROMO_HDR' with promotion id:"+ppId);									
				}
			} catch (Exception e) {
				LOGGER.error("Exception while parsing a row from 'ZDDPP_PROMO_HDR':"+e.getMessage());				
			} finally{
				ppTable.nextRow();
			}
			
		}
		return ppList;
	}

	private void loadProductPromotions(List<ErpProductPromotion> ppList,
			List<ErpProductPromotionInfo> ppInfoList)
			throws LoaderException, RemoteException {
		SAPProductPromotionLoaderSB sb =getSAPProductPromotionLoader();		
		sb.loadProductPromotions(ppList, ppInfoList, batchNumber);
		
	}

	private SAPProductPromotionLoaderSB getSAPProductPromotionLoader() {
		Context ctx = null;
		SAPProductPromotionLoaderSB sb = null;
		try {
			ctx = ErpServicesProperties.getInitialContext();
			SAPProductPromotionLoaderHome mgr = (SAPProductPromotionLoaderHome) ctx.lookup("freshdirect.dataloader.SAPProductPromotionLoader");
			sb = mgr.create();	        		 
			
		} catch(NamingException ex) {
			LOGGER.error("Exception occured:"+ex);
			throw new EJBException("Failed to load the initial context: " + ex.toString());
		} catch(RemoteException ex) {
			LOGGER.error("Exception occured:"+ex);
			throw new EJBException("Failed to get the SAPProductPromotionLoaderSB: " + ex.toString());
		} catch(CreateException ex) {
			LOGGER.error("Exception occured:"+ex);
			throw new EJBException("Failed to get the SAPProductPromotionLoaderSB: " + ex.toString());
		} finally {
			if (ctx != null) {
				try {
					ctx.close();
				} catch (NamingException e) {
					LOGGER.error("Exception occured:"+e);
				}
			}
		}
		return sb;
	}

	@Override
	public MetaData getFunctionMetaData() {
		JCO.MetaData fmeta = new JCO.MetaData(SAP_FUNCTION_ID);
		fmeta.addInfo(TABLE_ZDDPP_PROMO_HDR,	JCO.TYPE_TABLE,	totalSize[0], 0, 0, 0,	"PARAM1");
		fmeta.addInfo(TABLE_ZDDPP_PROMO_DTL,	JCO.TYPE_TABLE,	totalSize[1], 0, 0, 0,	"PARAM2");
		fmeta.addInfo("RETURN", JCO.TYPE_CHAR, 1, 0, 0, JCO.EXPORT_PARAMETER, null);
		fmeta.addInfo("MESSAGE", JCO.TYPE_CHAR, 255, 0, 0, JCO.EXPORT_PARAMETER, null);
		return fmeta;
	}

	@Override
	public MetaData[] getStructureMetaData() {
		return smeta;
	}
	
	public static void main(String[] args){
		SAPProductPromotionContentLoader loader = new SAPProductPromotionContentLoader();
		loader.execute(null, null, null);
	}

}

class DataStructure {
	String fieldName;
	int type;
	int length;
	int decimal;
	String description;
	DataStructure (String fieldName,
				   int type,
				   int length,
				   int decimal,
				   String description) {
		this.fieldName = fieldName;
		this.type = type;
		this.length = length;
		this.decimal = decimal;
		this.description = description;
	}
}
