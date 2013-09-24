package com.freshdirect.webapp.taglib.coremetrics;

import org.apache.log4j.Logger;

import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.util.QueryParameterCollection;
import com.freshdirect.fdstore.coremetrics.builder.ProductViewTagModelBuilder;
import com.freshdirect.fdstore.coremetrics.builder.SkipTagException;
import com.freshdirect.fdstore.coremetrics.tagmodel.AbstractTagModel;
import com.freshdirect.fdstore.coremetrics.tagmodel.ProductViewTagModel;
import com.freshdirect.framework.util.log.LoggerFactory;

public class CmProductViewTag extends AbstractCmTag {

	private static final Logger LOGGER = LoggerFactory.getInstance(CmProductViewTag.class);
	private ProductViewTagModelBuilder builder = new ProductViewTagModelBuilder();

	@Override
	protected String getFunctionName() {
		return "cmCreateProductviewTag";
	}
	
	@Override
	protected String getTagJs() throws SkipTagException {

		builder.setVirtualCategoryId(extractVirtualCategoryId());
		ProductViewTagModel model = builder.buildTagModel();
		
		String tagJs = getFormattedTag(
				toJsVar(model.getProductId()),
				toJsVar(model.getProductName()),
				toJsVar(model.getCategoryId()),
				toJsVar(mapToAttrString(model.getAttributesMaps())) + decorateFromCoremetricsTrackingObject(),
				toJsVar(model.getVirtualCategoryId()));
		
		LOGGER.debug(tagJs);
		return tagJs;
	}

	private String decorateFromCoremetricsTrackingObject() {
		
		StringBuilder sb = new StringBuilder();
		sb.append(" + \"" + AbstractTagModel.ATTR_DELIMITER + "\" + ");
		sb.append(CmFieldDecoratorTag.CM_PAGE_CONTENT_HIERARCHY);
		sb.append(" + \"" + AbstractTagModel.ATTR_DELIMITER + "\" + ");
		sb.append(CmFieldDecoratorTag.CM_PAGE_ID);
		return sb.toString();
		
	}
	
	private String extractVirtualCategoryId(){
		QueryParameterCollection qv = QueryParameterCollection.decode(getRequest().getHeader("referer")); 
		
		return qv.getParameterValue("cm_vc");
	}
	
	public void setProductModel(ProductModel productModel) {
		builder.setProductModel(productModel);
	}

	public void setQuickbuy(boolean quickbuy) {
		builder.setQuickbuy(quickbuy);
	}

}
