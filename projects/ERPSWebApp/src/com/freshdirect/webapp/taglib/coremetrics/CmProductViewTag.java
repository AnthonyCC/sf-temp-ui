package com.freshdirect.webapp.taglib.coremetrics;

import org.apache.log4j.Logger;

import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.coremetrics.builder.SkipTagException;
import com.freshdirect.fdstore.coremetrics.builder.ProductViewTagModelBuilder;
import com.freshdirect.fdstore.coremetrics.tagmodel.ProductViewTagModel;
import com.freshdirect.framework.util.log.LoggerFactory;

public class CmProductViewTag extends AbstractCmTag {

	private static final Logger LOGGER = LoggerFactory.getInstance(CmProductViewTag.class);
	private static final String PRODUCT_VIEW_TAG_FS = "cmCreateProductviewTag(%s,%s,%s,%s);";

	private ProductViewTagModelBuilder builder = new ProductViewTagModelBuilder();

	@Override
	protected String getTagJs() throws SkipTagException {

		ProductViewTagModel model = builder.buildTagModel();
		
		String setProductScript = String.format(PRODUCT_VIEW_TAG_FS,
				toJsVar(model.getProductId()),
				toJsVar(model.getProductName()),
				toJsVar(model.getCategoryId()),
				toJsVar(mapToAttrString(model.getAttributesMaps())));
		
		LOGGER.debug(setProductScript);
		return setProductScript;
	}

	public void setProductModel(ProductModel productModel) {
		builder.setProductModel(productModel);
	}

	public void setQuickbuy(boolean quickbuy) {
		builder.setQuickbuy(quickbuy);
	}

}
