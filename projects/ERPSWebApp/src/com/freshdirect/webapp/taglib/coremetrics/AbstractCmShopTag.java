package com.freshdirect.webapp.taglib.coremetrics;

import java.util.List;

import org.apache.log4j.Logger;

import com.freshdirect.fdstore.coremetrics.builder.AbstractShopTagModelBuilder;
import com.freshdirect.fdstore.coremetrics.builder.SkipTagException;
import com.freshdirect.fdstore.coremetrics.tagmodel.ShopTagModel;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.framework.util.log.LoggerFactory;

public abstract class AbstractCmShopTag <X extends AbstractShopTagModelBuilder> extends AbstractCmTag {
	private static final Logger LOGGER = LoggerFactory.getInstance(AbstractCmShopTag.class);
	public static final String DISPLAY_SHOPS = "cmDisplayShops";

	protected X tagModelBuilder;
	
	@Override
	public String getTagJs() throws SkipTagException {
		initTag();
		StringBuilder shopScriptSb = new StringBuilder();
		List<ShopTagModel> tagModels = tagModelBuilder.buildTagModels();

		for (ShopTagModel tagModel : tagModels) {
			appendTag(shopScriptSb, tagModel);
			shopScriptSb.append(getTagDelimiter());
		}

		if (tagModels.size()>0){
				shopScriptSb.append(getFormattedTag(DISPLAY_SHOPS, new String[0]));
		}
		
		String shopScript = shopScriptSb.toString();
		//LOGGER.debug(shopScript);
		return shopScript;
	}
	
	public void setCart(FDCartModel cart) {
		tagModelBuilder.setCart(cart);
	}

	protected abstract void initTag();
	
	protected abstract void appendTag(StringBuilder shopScriptSb, ShopTagModel tagModel);
}
