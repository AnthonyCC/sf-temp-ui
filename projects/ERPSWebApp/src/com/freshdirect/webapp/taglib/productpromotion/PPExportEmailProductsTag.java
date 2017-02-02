package com.freshdirect.webapp.taglib.productpromotion;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.fdstore.FDContentTypes;
import com.freshdirect.common.pricing.ZoneInfo;
import com.freshdirect.customer.ErpZoneMasterInfo;
import com.freshdirect.erp.EnumFeaturedHeaderType;
import com.freshdirect.erp.ejb.ProductPromotionInfoManager;
import com.freshdirect.fdstore.FDProductPromotionInfo;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.ProductModelPromotionAdapter;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.SkuModel;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.webapp.taglib.AbstractGetterTag;
import com.freshdirect.webapp.template.TemplateContext;

/**
 * 
 * @author ksriram
 *
 */
public class PPExportEmailProductsTag extends AbstractGetterTag<String>{

	private static final long serialVersionUID = -779157820243159440L;
	private static final Logger LOGGER = LoggerFactory.getInstance(PPExportEmailProductsTag.class);
	
	private static final String D_P = "d_p";
	private static final String TAB ="\t";
	private static final String NEW_LINE ="\r\n";
	private static final String ATTR_FEAT = "feat";
	private static final String ATTR_IMG = "img";
	private static final String ATTR_NAME = "name";
	private static final String ATTR_PRICE = "price";
	private static final String ATTR_WAS_PRICE = "waspr";
	private static final String ATTR_RATING = "rate";	
	private static final String ATTR_SAVE = "save";
	private static final String ATTR_SS_RATING = "ssrate";
	@Override
	protected String getResult() throws Exception {
		String ppId= request.getParameter("pp_id");
		boolean isExport = request.getRequestURI().indexOf("ppicks_export_email_products.jsp")>-1;//Parameter("exportEmail");
		LOGGER.info("Generating promotion products for export.");
		String ppType="PRESIDENTS_PICKS";
		Map<ZoneInfo,List<FDProductPromotionInfo>> productPromoInfoMap = ProductPromotionInfoManager.getAllProductsByType(ppType);
		request.setAttribute("promoInfo",productPromoInfoMap);
		StringBuffer buffer = new StringBuffer();
		if(isExport){
			HttpServletResponse response =(HttpServletResponse)pageContext.getResponse();
			//response.setContentType("application/vnd.ms-excel");
			response.setContentType("text/plain");
		    response.setHeader("Content-Disposition", "attachment; filename=PresPicks_Products_Export_"+new Date().getTime()+".txt");
		    response.setCharacterEncoding("utf-8");
		    appendData(isExport,buffer,"TAG");appendData(isExport,buffer,"Key(PRICING_ZONE)");appendData(isExport,buffer,"H");appendNewLine(isExport,buffer);
		}else{
			buffer.append("<table>");
		}
			
		if(null !=productPromoInfoMap){			
			List<ErpZoneMasterInfo> zoneInfoList=ErpProductPromotionUtil.getAvailablePricingZones();			
			for (Iterator iterator = zoneInfoList.iterator(); iterator.hasNext();) {
				ErpZoneMasterInfo erpZoneInfo = (ErpZoneMasterInfo) iterator.next();
				String zoneId = erpZoneInfo.getSapId();
				ZoneInfo zoneInfo = new ZoneInfo(zoneId,"0001","01");
				
				List<FDProductPromotionInfo> skusList = productPromoInfoMap.get(new ZoneInfo(zoneId,"0001","01"));//Default SalesOrg info for FD.
				if(null == skusList){
					skusList = productPromoInfoMap.get(ErpProductPromotionUtil.DEFAULT_ZONE_INFO);
				}
				int i=1;
				zoneId = zoneId.replaceFirst("0000", "");//[APPDEV-2981]-Removing the '0's from the zone id, to be consistent with what is stored in the data warehouse.
				if(null != skusList){				
					populateBuffer(buffer, i, zoneId, skusList,isExport, zoneInfo);					
				}
			}
		}
		if(!isExport){
			buffer.append("</table>");
		}
		LOGGER.info("Completed generating promotion products, for export.");	
		
		return buffer.toString();
	}


	private int populateBuffer(StringBuffer buffer, int i, String zoneId,
			List<FDProductPromotionInfo> skusList,boolean isExport, ZoneInfo zoneInfo) throws FDResourceException {
		int featuredCount=0;
		if(null != skusList){
			for (Iterator iterator3 = skusList.iterator(); iterator3.hasNext();) {
				FDProductPromotionInfo promotionSkuModel = (FDProductPromotionInfo) iterator3.next();
				/*if(promotionSkuModel.isFeatured()){
					if(featuredCount >= 3){
						continue;
					}else{
						featuredCount++;
					}
				}*/
				ProductModel productModel = null;
				try {
					ProductModel prodModel = ((SkuModel) ContentFactory.getInstance().getContentNodeByKey(ContentKey.getContentKey(FDContentTypes.SKU, promotionSkuModel.getSkuCode()))).getProductModel();
					productModel = new ProductModelPromotionAdapter(prodModel,promotionSkuModel.isFeatured(),promotionSkuModel.getFeaturedHeader(),promotionSkuModel.getSkuCode());
				} catch (Exception e){
					LOGGER.info("sku "+promotionSkuModel.getSkuCode() +" missing in CMS "+e.getMessage());	
					
				}
				if(productModel!=null && !productModel.isUnavailable()){
					if(promotionSkuModel.isFeatured()){				
						featuredCount++;
					}
					EnumFeaturedHeaderType fhType = null;
					if(featuredCount <= 3){
						String featHeader = promotionSkuModel.getFeaturedHeader();
						
						if(null !=featHeader && !"".equals(featHeader.trim())){
							fhType =EnumFeaturedHeaderType.getEnum(Integer.valueOf(promotionSkuModel.getFeaturedHeader()));		
						}
					}
					
	//				FDProductPromotionSku promotionSkuModel = promotionSkuModel.getPromotionSku();
					
					appendData(isExport,buffer,D_P+i+ATTR_FEAT);appendData(isExport,buffer,zoneId);appendData(isExport,buffer,null !=fhType?fhType.getDescription():"");appendNewLine(isExport,buffer);
					if(i<10 && isExport){
						appendData(isExport,buffer,D_P+i+ATTR_IMG);/*appendTab(isExport,buffer);*/appendData(isExport,buffer,zoneId);appendData(isExport,buffer,productModel.getDetailImage().getPath()/*getHtml()*/);appendNewLine(isExport,buffer);
					}else{
						appendData(isExport,buffer,D_P+i+ATTR_IMG);appendData(isExport,buffer,zoneId);appendData(isExport,buffer,productModel.getDetailImage().getPath()/*getHtml()*/);appendNewLine(isExport,buffer);
					}
					
					appendData(isExport,buffer,D_P+i+ATTR_NAME);appendData(isExport,buffer,zoneId);appendData(isExport,buffer,productModel.getFullName());appendNewLine(isExport,buffer);
					appendData(isExport,buffer,D_P+i+ATTR_PRICE);appendData(isExport,buffer,zoneId);appendData(isExport,buffer,productModel.getPriceCalculator(promotionSkuModel.getSkuCode()).getDefaultPrice());appendNewLine(isExport,buffer);
					appendData(isExport,buffer,D_P+i+ATTR_WAS_PRICE);appendData(isExport,buffer,zoneId);appendData(isExport,buffer,productModel.getPriceCalculator(promotionSkuModel.getSkuCode()).isOnSale()?"$"+productModel.getPriceCalculator(promotionSkuModel.getSkuCode()).getWasPrice():"");appendNewLine(isExport,buffer);
					String rating = "";
					String ratingUrl="";
					rating =(null !=productModel.getProductRatingEnum()?productModel.getProductRatingEnum().getStatusCodeInDisplayFormat():"");
					if(null !=rating && !"".equals(rating)&& !"X".equalsIgnoreCase(rating)){
						ratingUrl ="<img src=\"http://www.freshdirect.com/media_stat/images/ratings/"+rating+".gif\" width=\"59\" height=\"11\" border=\"0\">";
					}
					appendData(isExport,buffer,D_P+i+ATTR_RATING);appendData(isExport,buffer,zoneId);appendData(isExport,buffer,ratingUrl);appendNewLine(isExport,buffer);
					String scaleDisplay=TemplateContext.getScaleDisplay(productModel,zoneInfo);
					appendData(isExport,buffer,D_P+i+ATTR_SAVE);appendData(isExport,buffer,zoneId);appendData(isExport,buffer,(null!=scaleDisplay&&!"".equals(scaleDisplay))?"SAVE! "+scaleDisplay:((productModel.getPriceCalculator().getHighestDealPercentage() > 0)?"SAVE "+(productModel.getPriceCalculator(promotionSkuModel.getSkuCode()).getHighestDealPercentage()+ "%"):""));appendNewLine(isExport,buffer);
					StringBuilder bufSS = new StringBuilder();
					String ratingSS = productModel.getSustainabilityRating(promotionSkuModel.getSkuCode());
					if (ratingSS != null && ratingSS.trim().length() > 0) {
						bufSS.append("<img src=\"http://www.freshdirect.com/media_stat/images/ratings/"
								+ "fish_" + ratingSS + ".gif\"");	
						bufSS.append(" name=\"ss_rating_" + ratingSS + "\"");	
						bufSS.append(" width=\"35\"");	
						bufSS.append(" height=\"15\"");	
						bufSS.append(" border=\"0\"");					
						bufSS.append(" alt=\"Very Ocean-Friendly" + ratingSS + "\"");	
						bufSS.append(" />");
					}
					appendData(isExport,buffer,D_P+i+ATTR_SS_RATING);appendData(isExport,buffer,zoneId);appendData(isExport,buffer,bufSS.toString());appendNewLine(isExport,buffer);						
					i++;
				}
			}
		}
		return i;
	}

	private static void appendNewLine(boolean isExport, StringBuffer buff){
		if(isExport){
			buff.append(NEW_LINE);
		}else{
			buff.append("</tr>");
		}
	}
	private static void appendTab(boolean isExport, StringBuffer buffer){
		if(isExport){
			buffer.append(TAB);
		}
	}
	private static StringBuffer appendData(boolean isExport, StringBuffer buffer,String data){
		if(!isExport){
			if(data.contains(D_P)){
				buffer.append("<tr>");
			}
			buffer.append("<td>").append(data).append("</td>");			
		}else{
			buffer.append(data).append(TAB);
		}
		
		return buffer;
	}

	public static class TagEI extends AbstractGetterTag.TagEI {
		protected String getResultType() {
//			return "java.util.Map<java.lang.String,java.util.Map<java.lang.String,java.util.List<com.freshdirect.cms.util.ProductPromotionSkuModel>>>";
			return "java.lang.String";
		}
	}
}
