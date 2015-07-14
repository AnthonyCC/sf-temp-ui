package com.freshdirect.webapp.taglib.coremetrics;

import org.apache.log4j.Logger;

import com.freshdirect.fdlogistics.model.FDTimeslot;
import com.freshdirect.fdstore.content.util.QueryParameterCollection;
import com.freshdirect.fdstore.coremetrics.builder.ElementTagModelBuilder;
import com.freshdirect.fdstore.coremetrics.builder.SkipTagException;
import com.freshdirect.fdstore.coremetrics.tagmodel.ElementTagModel;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.util.FilteringNavigator;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.smartstore.TabRecommendation;
import com.freshdirect.webapp.taglib.fdstore.SessionName;

public class CmElementTag extends AbstractCmTag {

	private static final Logger LOGGER = LoggerFactory.getInstance(CmElementTag.class);
	private ElementTagModelBuilder tagModelBuilder = new ElementTagModelBuilder();

	@Override
	protected String getFunctionName() {
		return "cmCreateElementTag";
	}
	
	@Override
	public String getTagJs() throws SkipTagException {

		tagModelBuilder.setUser((FDUserI) getSession().getAttribute(SessionName.USER));
		
		ElementTagModel tagModel = tagModelBuilder.buildTagModel();
		
		String tagJs = null;

		if(ElementTagModelBuilder.CAT_VIDEO.equals(tagModel.getElementCategory())){
			
			String attr = "\"-_--_--_--_--_--_--_--_--_--_--_-\"+video.title+\"-_-\"+state+\"-_-\"+Math.round(player.getCurrentTime())+\"-_-\"+Math.round(player.getDuration())";
			
			tagJs = getFormattedTag(
					toJsObject(tagModel.getElementId()),
					toJsVar(tagModel.getElementCategory()),
					attr);
			
		} else {
			
			tagJs = getFormattedTag( 
					toJsVar(tagModel.getElementId()), 
					toJsVar(tagModel.getElementCategory()), 
					toJsVar(mapToAttrString(tagModel.getAttributesMaps())));
			
		}

		//LOGGER.debug(tagJs);
		return tagJs;
	}
	
	public void setElementId(String elementId) {
		tagModelBuilder.setElementId(elementId);
	}

	public void setElementCategory(String elementCategory) {
		tagModelBuilder.setElementCategory(elementCategory);
	}

	public void setCarouselId(String carouselId) {
		tagModelBuilder.setCarouselId(carouselId);
	}

	public void setSiteFeature(String siteFeature) {
		tagModelBuilder.setSiteFeature(siteFeature);
	}

	public void setVariant(String variant) {
		tagModelBuilder.setVariant(variant);
	}

	public void setTimeslot(FDTimeslot slot) {
		tagModelBuilder.setTimeSlot(slot);
	}
	
	public void setSoType(boolean soType){
		tagModelBuilder.setSoType(soType);
	}
	
	public void setTabRecommendation(TabRecommendation tabRecommendation) {
		tagModelBuilder.setTabRecommendation(tabRecommendation);
	}

	public void setTabNumber(int tabNumber) {
		tagModelBuilder.setTabNumber(tabNumber);
	}

	public void setSearchNavigator(FilteringNavigator searchNavigator){
		tagModelBuilder.setSearchNavigator(searchNavigator);
	}
	
	public void setQueryParamCollection(QueryParameterCollection queryParamCollection) {
		tagModelBuilder.setQueryParamCollection(queryParamCollection);
	}
	
	public void setProductId(String productId) {
		tagModelBuilder.setProductId(productId);
	}
	
	public void setSkuCode(String skuCode) {
		tagModelBuilder.setSkuCode(skuCode);
	}
	
	public void setCouponOfferType(String couponOfferType){
		tagModelBuilder.setCouponOfferType(couponOfferType);
	}
}
