package com.freshdirect.webapp.taglib.coremetrics;

import com.freshdirect.fdstore.coremetrics.builder.Shop5TagModelBuilder;
import com.freshdirect.fdstore.coremetrics.tagmodel.ShopTagModel;

public class CmShop5Tag extends AbstractCmShopTag<Shop5TagModelBuilder> {
	private static final String SHOP_5_TAG_FS = "cmCreateShopAction5Tag(%s,%s,%s,%s,%s,%s);";

	public CmShop5Tag() {
		tagModelBuilder = new Shop5TagModelBuilder();
	}
		
	protected void appendTag(StringBuilder shopScriptSb, ShopTagModel tagModel){
		shopScriptSb.append("\n").append(
				String.format(SHOP_5_TAG_FS, 
						toJsVar(tagModel.getProductId()), 
						toJsVar(tagModel.getProductName()), 
						toJsVar(tagModel.getQuantity()), 
						toJsVar(tagModel.getUnitPrice()), 
						toJsVar(tagModel.getCategoryId()),
						toJsVar(mapToAttrString(tagModel.getAttributesMaps()))));
	}

	protected void initTag() {
	}
}
