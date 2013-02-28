package com.freshdirect.webapp.taglib.coremetrics;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.PageContext;

import org.apache.log4j.Logger;

import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.util.QueryParameterCollection;
import com.freshdirect.fdstore.coremetrics.builder.SkipTagException;
import com.freshdirect.fdstore.coremetrics.builder.ProductViewTagModelBuilder;
import com.freshdirect.fdstore.coremetrics.tagmodel.ProductViewTagModel;
import com.freshdirect.framework.util.log.LoggerFactory;

public class CmProductViewTag extends AbstractCmTag {

	private static final Logger LOGGER = LoggerFactory.getInstance(CmProductViewTag.class);
	private static final String PRODUCT_VIEW_TAG_FS = "cmCreateProductviewTag(%s,%s,%s,%s,%s);";

	private ProductViewTagModelBuilder builder = new ProductViewTagModelBuilder();

	@Override
	protected String getTagJs() throws SkipTagException {

		builder.setVirtualCategoryId(extractVirtualCategoryId());
		ProductViewTagModel model = builder.buildTagModel();
		
		String tagJs = String.format(PRODUCT_VIEW_TAG_FS,
				toJsVar(model.getProductId()),
				toJsVar(model.getProductName()),
				toJsVar(model.getCategoryId()),
				toJsVar(mapToAttrString(model.getAttributesMaps())),
				toJsVar(model.getVirtualCategoryId()));
		
		LOGGER.debug(tagJs);
		return tagJs;
	}

	private String extractVirtualCategoryId(){
		PageContext ctx = (PageContext) getJspContext();
		HttpServletRequest request = (HttpServletRequest) ctx.getRequest();
		QueryParameterCollection qv = QueryParameterCollection.decode(request.getHeader("referer")); 
		
		return qv.getParameterValue("cm_vc");
	}
	
	public void setProductModel(ProductModel productModel) {
		builder.setProductModel(productModel);
	}

	public void setQuickbuy(boolean quickbuy) {
		builder.setQuickbuy(quickbuy);
	}

}
