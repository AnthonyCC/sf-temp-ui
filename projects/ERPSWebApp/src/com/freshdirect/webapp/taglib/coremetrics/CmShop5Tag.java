package com.freshdirect.webapp.taglib.coremetrics;

import java.util.List;

import com.freshdirect.fdstore.coremetrics.builder.Shop5TagModelBuilder;
import com.freshdirect.fdstore.coremetrics.tagmodel.ShopTagModel;
import com.freshdirect.fdstore.customer.FDCartLineI;

public class CmShop5Tag extends AbstractCmShopTag<Shop5TagModelBuilder> {

	/**
	 * Optional list of cart lines. If set, overrides recent order lines.
	 */
	@SuppressWarnings( "unused" )
	private List<FDCartLineI> explicitList = null;

	/**
	 * Explicit list of cart line items. Optional.
	 * 
	 * @param explicitList
	 */
	public void setExplicitList(List<FDCartLineI> explicitList) {
		this.explicitList = explicitList;

		tagModelBuilder.setExplicitList(explicitList);
	}
	
	public CmShop5Tag() {
		tagModelBuilder = new Shop5TagModelBuilder();
	}

	@Override
	protected String getFunctionName() {
		return "cmCreateShopAction5Tag";
	}
	
	protected void appendTag(StringBuilder shopScriptSb, ShopTagModel tagModel){
		shopScriptSb.append(
				getFormattedTag( 
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
