package com.freshdirect.sap.jco;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Category;

import com.freshdirect.erp.EnumATPRule;
import com.freshdirect.erp.EnumProductPromotionType;
import com.freshdirect.erp.ErpProductPromotion;
import com.freshdirect.erp.model.ErpProductInfoModel;
import com.freshdirect.erp.model.ErpProductInfoModel.ErpMaterialPrice;
import com.freshdirect.fdstore.EnumAvailabilityStatus;
import com.freshdirect.fdstore.FDProductPromotionInfo;
import com.freshdirect.framework.util.DateUtil;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.sap.SapProductPromotionConstants;
import com.freshdirect.sap.bapi.BapiException;
import com.freshdirect.sap.bapi.BapiProductPromotionPreviewI;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoTable;

public class JcoBapiProductPromotionPreview extends JcoBapiFunction implements BapiProductPromotionPreviewI, SapProductPromotionConstants {

	private static Category LOGGER = LoggerFactory.getInstance( JcoBapiProductPromotionPreview.class );
	
	private List<ErpProductInfoModel> erpProductInfoList = new ArrayList<ErpProductInfoModel>();
	private Map<String, ErpProductInfoModel> erpProductInfoMap = new HashMap<String,ErpProductInfoModel>();
	private Map<String,List<FDProductPromotionInfo>> productPromotionInfoMap;
	
	private String functionName = null;
	
	public List<ErpProductInfoModel> getErpProductInfoList() {
		return erpProductInfoList;
	}

	public void setErpProductInfoList(List<ErpProductInfoModel> erpProductInfoList) {
		this.erpProductInfoList = erpProductInfoList;
	}
	
	public Map<String, List<FDProductPromotionInfo>> getProductPromotionInfoMap() {
		return productPromotionInfoMap;
	}

	public void setProductPromotionInfoMap(
			Map<String, List<FDProductPromotionInfo>> productPromotionInfoMap) {
		this.productPromotionInfoMap = productPromotionInfoMap;
	}

	public Map<String, ErpProductInfoModel> getErpProductInfoMap() {
		return erpProductInfoMap;
	}

	public void setErpProductInfoMap(
			Map<String, ErpProductInfoModel> erpProductInfoMap) {
		this.erpProductInfoMap = erpProductInfoMap;
	}

	public JcoBapiProductPromotionPreview(String functionName) throws JCoException {
		super(functionName);
		this.functionName = functionName;
	}
	
	public void getPromotionProductsForPreview(String id){
		
	}	

	protected void processResponse() throws BapiException
	{
		super.processResponse();

//		JCO.Table ppHeaderpromoHeader = function.getTableParameterList().getTable(TABLE_ZDDPP_PROMO_HDR);	
		if("ZDDPA_PREVIEW".equals(functionName)){
			JCoTable ppDetailTbl = function.getTableParameterList().getTable(TABLE_ZDDPP_PROMO_DTL);
			productPromotionInfoMap = getProductPromotionInfo(ppDetailTbl);			
		}else{
			JCoTable ppDetailTbl = function.getTableParameterList().getTable(TABLE_ZDDPP_PROMO_DTL);
			productPromotionInfoMap = getProductPromotionInfo(ppDetailTbl);
			JCoTable ppPriceTbl = function.getTableParameterList().getTable(TABLE_ZDDPP_PROMO_PRICE);
			Map<String,Map<String,List<ErpMaterialPrice>>> ppPricesMap = getProductPromotionPrices(ppPriceTbl);
			populatePrices(ppPricesMap);
		}
	}

	private void populatePrices(
			Map<String, Map<String, List<ErpMaterialPrice>>> ppPricesMap)
	{
		if(null != erpProductInfoMap && null !=ppPricesMap){
			for (Iterator iterator = erpProductInfoMap.keySet().iterator(); iterator.hasNext();) {
				ErpProductInfoModel erpProductInfoModel = (ErpProductInfoModel)erpProductInfoMap.get(iterator.next());
				Map<String,List<ErpMaterialPrice>> zonePrices =ppPricesMap.get(erpProductInfoModel.getSkuCode());
				if(null !=zonePrices){
					List<ErpMaterialPrice> matPriceList = new ArrayList<ErpMaterialPrice>();
					for (Iterator iterator2 = zonePrices.keySet().iterator(); iterator2.hasNext();) {
						List<ErpMaterialPrice> matPrices = (List<ErpMaterialPrice>) zonePrices.get(iterator2.next());
						matPriceList.addAll(matPrices);
					}				
					erpProductInfoModel.setMaterialPrices(matPriceList.toArray(new ErpProductInfoModel.ErpMaterialPrice[0]));
				}
			}
		}
	}
	
	public void addRequest(String previewId){
		this.function.getImportParameterList().setValue(FIELD_REQUESTID, previewId);			
	}
	
	
	private List<ErpProductPromotion> populateProductPromotion(JCoTable ppTable)
	{		
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
					LOGGER.warn("Not a valid value for 'PRTYP' of 'ZDDPP_PROMO_HDR' with promotion id:"+ppId);									
				}
			} catch (Exception e) {
				LOGGER.error("Exception while parsing a row from 'ZDDPP_PROMO_HDR':"+e.getMessage());				
			} finally{
				ppTable.nextRow();
			}
			
		}
		return ppList;
	}
	
	
	private Map<String,List<FDProductPromotionInfo>> getProductPromotionInfo(
			JCoTable ppInfoTable) {
		
//		List<FDProductPromotionInfo> ppInfoList = new ArrayList<FDProductPromotionInfo>();
		Map<String,List<FDProductPromotionInfo>> zonePromoProdMap= new HashMap<String,List<FDProductPromotionInfo>>();
		Map<String,List<FDProductPromotionInfo>> promotionProductsMap = null; 
		List<FDProductPromotionInfo> promotionProducts =null;
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
					if (zoneId==null || "".equals(zoneId.trim())){
						zoneId=MASTER_DEFAULT_ZONE;
					}
					zoneId ="0000"+zoneId;
					Integer priority = ppInfoTable.getInt(FIELD_PRIORY);
					String featured = ppInfoTable.getString(FIELD_FEATR);
					boolean isFeatured = "X".equalsIgnoreCase(featured)?true:false;
					String featuredHeader = ppInfoTable.getString(FIELD_FEATRH);
					promotionProducts = zonePromoProdMap.get(zoneId);
	            	/*if(null == promotionProductsMap){
	            		promotionProductsMap = new HashMap<String,List<FDProductPromotionInfo>>();
	            		zonePromoProdMap.put(zoneId, promotionProductsMap);
	            	}
	            	if(isFeatured){
	            		promotionProducts = promotionProductsMap.get(FEATURED);
	            	}else{
	            		promotionProducts = promotionProductsMap.get(NON_FEATURED);
	            	}*/
	            	if(null == promotionProducts){
	            		promotionProducts = new ArrayList<FDProductPromotionInfo>();
//	            		promotionProductsMap.put(isFeatured?FEATURED:NON_FEATURED, promotionProducts);
	            		zonePromoProdMap.put(zoneId, promotionProducts);
	            	}
	            	String erpCategory = ppInfoTable.getString(FIELD_CATEGORY);
					Integer erpCatPosition = ppInfoTable.getInt(FIELD_CATEGORY_POSITION);
					
	            	FDProductPromotionInfo ppInfo= new FDProductPromotionInfo();				
					ppInfo.setType(enumType.getName());		
					ppInfo.setSkuCode(skuCode);
					ppInfo.setMatNumber(matNum);
					ppInfo.setErpDeptId(dept);
					ppInfo.setZoneId(zoneId);
					ppInfo.setPriority(priority);
					ppInfo.setFeaturedHeader(featuredHeader);
					ppInfo.setFeatured(isFeatured);
					ppInfo.setErpCategory(erpCategory);
					ppInfo.setErpCatPosition(erpCatPosition);
					promotionProducts.add(ppInfo);
					Collections.sort(promotionProducts, new FDProductPromotionInfoComparator());
					if(null==erpProductInfoMap.get(skuCode)){
					ErpProductInfoModel erpProductInfoModel = null;/*new ErpProductInfoModel(skuCode,new String[]{matNum},EnumATPRule.JIT,
							EnumAvailabilityStatus.AVAILABLE.getStatusCode(),new java.util.GregorianCalendar(3000, java.util.Calendar.JANUARY, 1).getTime(),
							"","","","","","");*/
//					erpProductInfoList.add(erpProductInfoModel);
					erpProductInfoMap.put(skuCode, erpProductInfoModel);
					}
					
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
		return zonePromoProdMap;
	}
	
	private Map<String,Map<String,List<ErpMaterialPrice>>> getProductPromotionPrices(
			JCoTable ppPriceTable) {
		
		/*List<ErpProductPromotionPrice> ppPriceList = new ArrayList<ErpProductPromotionPrice>();*/
//		List<ErpMaterialPrice> matPrices = new ArrayList<ErpMaterialPrice>(5);
		Map<String,Map<String,List<ErpMaterialPrice>>> skuZoneMatPrices = new HashMap<String,Map<String,List<ErpMaterialPrice>>>();
		ppPriceTable.firstRow();
		for(int i=0;i<ppPriceTable.getNumRows();i++){
			try {
				String skuCode = ppPriceTable.getString(FIELD_BISMT).trim();
				String matNum = ppPriceTable.getString(FIELD_MATNR).trim();
				String zoneId = ppPriceTable.getString(FIELD_ZONEID).trim();
				zoneId ="0000"+zoneId;
				String priceType = ppPriceTable.getString(FIELD_ZPRICE_TYPE).trim();
				String strQty = ppPriceTable.getString(FIELD_ZPRICE_QTY).trim();
				double qty=0.0;
				if(strQty!= null && !"".equals(strQty)){
					qty = Double.parseDouble(strQty);
				}
				String strPrice = ppPriceTable.getString(FIELD_ZPRICE_RATE).trim();
				double price =0.0;
				if(strPrice!= null &&!"".equals(strPrice)){
					price = Double.parseDouble(strPrice);
				}
				String priceUOM = ppPriceTable.getString(FIELD_ZPRICE_UOM).trim();
				if(null != zoneId){
					Map<String,List<ErpMaterialPrice>> zoneMatPrices = skuZoneMatPrices.get(skuCode);
					if(null == zoneMatPrices){
						zoneMatPrices = new HashMap<String,List<ErpMaterialPrice>>();
						skuZoneMatPrices.put(skuCode, zoneMatPrices);
					}
					List<ErpMaterialPrice> matPrices =zoneMatPrices.get(zoneId);
					if(null == matPrices){
						matPrices = new ArrayList<ErpMaterialPrice>(5);
						zoneMatPrices.put(zoneId, matPrices);
					}
					ErpMaterialPrice materialPrice = null;
					if("R".equalsIgnoreCase(priceType)){
						materialPrice = new ErpMaterialPrice(price,priceUOM,0.0,"",0.0,zoneId, null, null);//::FDX::
						for (Iterator iterator = matPrices.iterator(); iterator.hasNext();) {
							ErpMaterialPrice erpMaterialPrice = (ErpMaterialPrice) iterator.next();
							if(erpMaterialPrice.getPromoPrice()>0 && erpMaterialPrice.getSapZoneId().equals(zoneId)){
								materialPrice.setPromoPrice(erpMaterialPrice.getPromoPrice());
								iterator.remove();
							}							
						}
					}else if("S".equalsIgnoreCase(priceType)){
						materialPrice = new ErpMaterialPrice(price,priceUOM,0.0,priceUOM,qty,zoneId, null, null);//::FDX::
					}else if("P".equalsIgnoreCase(priceType)){
						for (Iterator iterator = matPrices.iterator(); iterator.hasNext();) {
							ErpMaterialPrice erpMaterialPrice = (ErpMaterialPrice) iterator.next();
							if(erpMaterialPrice.getScaleQuantity()<=0 && erpMaterialPrice.getSapZoneId().equals(zoneId)){
								erpMaterialPrice.setPromoPrice(price);
							}							
						}
						if(null==materialPrice){
							materialPrice = new ErpMaterialPrice(0,priceUOM,price,"",0.0,zoneId,null,null);//::FDX::
						}
					}else{
						LOGGER.warn("ZPRICE_TYPE:"+priceType+", is invalid or empty for Sku:"+skuCode+", Material:"+matNum+" and Zone:"+zoneId);
					}
					if(null !=materialPrice)
						matPrices.add(materialPrice);
					
				}			
			} catch (Exception e) {
				LOGGER.error("Exception while parsing a row from 'ZDDPP_PROMO_DTL':"+e.getMessage());
			} finally{
				ppPriceTable.nextRow();
			}
		}
		return skuZoneMatPrices;
	}
	
	public class FDProductPromotionInfoComparator implements Comparator<FDProductPromotionInfo>{

		@Override
		public int compare(FDProductPromotionInfo arg0,
				FDProductPromotionInfo arg1) {
			if(((Integer)arg0.getErpCatPosition()).equals(arg1.getErpCatPosition())){
				return ((Integer)arg0.getPriority()).compareTo(arg1.getPriority());
			}else{
				return  ((Integer)arg0.getErpCatPosition()).compareTo(arg1.getErpCatPosition());
			}
		}
		
	}
}
