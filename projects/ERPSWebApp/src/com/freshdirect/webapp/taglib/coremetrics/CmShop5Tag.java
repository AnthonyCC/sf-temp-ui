package com.freshdirect.webapp.taglib.coremetrics;

import org.apache.log4j.Logger;

import com.freshdirect.fdstore.coremetrics.builder.SkipTagException;
import com.freshdirect.fdstore.coremetrics.builder.Shop5TagModelBuilder;
import com.freshdirect.fdstore.coremetrics.tagmodel.Shop5TagModel;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.framework.util.log.LoggerFactory;

public class CmShop5Tag extends AbstractCmTag {
	private static final Logger LOGGER = LoggerFactory.getInstance(CmShop5Tag.class);
	private static final String SHOP_5_TAG_FS = "cmCreateShopAction5Tag(%s,%s,%s,%s,%s,%s);";
	private static final String DISPLAY_SHOPS = "cmDisplayShops();";

	private Shop5TagModelBuilder tagModelBuilder = new Shop5TagModelBuilder();
	
	
	protected void checkContext() throws CmContextException {
		throw new CmContextException("Shop5 tag is disabled in first release");
	}
	
	@Override
	protected String getTagJs() throws SkipTagException {

		StringBuilder shop5ScriptSb = new StringBuilder();

		for (Shop5TagModel tagModel : tagModelBuilder.buildTagModels()) {
			
			shop5ScriptSb.append("\n").append(
					String.format(SHOP_5_TAG_FS, 
							toJsVar(tagModel.getProductId()), 
							toJsVar(tagModel.getProductName()), 
							toJsVar(tagModel.getQuantity()), 
							toJsVar(tagModel.getUnitPrice()), 
							toJsVar(tagModel.getCategoryId()),
							toJsVar(mapToAttrString(tagModel.getAttributesMaps()))));
		}

		shop5ScriptSb.append("\n").append(DISPLAY_SHOPS).append("\n");
		
		String shop5Script = shop5ScriptSb.toString();
		LOGGER.debug(shop5Script);
		return shop5Script;
	}

	public void setCart(FDCartModel cart) {
		tagModelBuilder.setCart(cart);
	}

	public void setSource(String source) {
		tagModelBuilder.setSource(source);
	}
}
