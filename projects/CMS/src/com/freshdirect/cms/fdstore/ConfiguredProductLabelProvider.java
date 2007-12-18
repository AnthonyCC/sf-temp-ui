package com.freshdirect.cms.fdstore;

import java.util.Set;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.application.CmsManager;
import com.freshdirect.cms.labels.ILabelProvider;
import com.freshdirect.cms.node.ContentNodeUtil;

public class ConfiguredProductLabelProvider implements ILabelProvider {

	public String getLabel(ContentNodeI node) {
		if (!FDContentTypes.CONFIGURED_PRODUCT.equals(node.getKey().getType())) {
			return null;
		}
		ContentKey skuKey = (ContentKey) node.getAttribute("SKU").getValue();
		if (skuKey == null) {
			// no corresponding sku
			return null;
		}
		Set parentKeys = CmsManager.getInstance().getParentKeys(skuKey);
		if (parentKeys.isEmpty()) {
			// no product for sku
			return null;
		}

		ContentKey prodKey = (ContentKey) parentKeys.iterator().next();
		if (!FDContentTypes.PRODUCT.equals(prodKey.getType())) {
			return null;
		}

		return ContentNodeUtil.getLabel(prodKey.getContentNode());
	}

}
