/*
 * Created on Aug 10, 2005
 */
package com.freshdirect.cms.fdstore;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.EnumAttributeType;
import com.freshdirect.cms.application.service.ContentDecoratorI;
import com.freshdirect.cms.meta.EnumDef;
import com.freshdirect.cms.node.AttributeMappedNode;

/**
 * {@link com.freshdirect.cms.application.service.ContentDecoratorI} that exposes
 * additional attributes for <code>ConfiguredProduct</code> nodes based on the
 * value of the <code>SKU</code> attribute and the characteristic values of the
 * corresponding material.
 * 
 * @see com.freshdirect.cms.node.AttributeMappedNode
 */
public class ConfiguredProductDecorator implements ContentDecoratorI {

	public ContentNodeI decorateNode(ContentNodeI node) {
		if (!FDContentTypes.CONFIGURED_PRODUCT.equals(node.getKey().getType())) {
			return null;
		}

		Map mapDefinition = new HashMap();

		ContentKey skuKey = (ContentKey) node.getAttribute("SKU").getValue();
		if (skuKey != null) {
			Set charKeys = new HashSet();
			collectReachable(charKeys, FDContentTypes.ERP_CHARACTERISTIC, skuKey.getContentNode());
			for (Iterator chi = charKeys.iterator(); chi.hasNext();) {
				ContentNodeI charNode = ((ContentKey) chi.next()).getContentNode();
				String charName = (String) charNode.getAttribute("name").getValue();
				Map values = new HashMap();
				Set cvKeys = charNode.getChildKeys();
				for (Iterator cvi = cvKeys.iterator(); cvi.hasNext();) {
					ContentNodeI cvNode = ((ContentKey) cvi.next()).getContentNode();
					String cvName = (String) cvNode.getAttribute("name").getValue();
					String label = cvNode.getLabel();
					values.put(cvName, label);
				}
				EnumDef enumDef = new EnumDef(charName, charName, true, false, false, EnumAttributeType.STRING, values);
				mapDefinition.put(charName, enumDef);
			}
		}

		ContentNodeI n = new AttributeMappedNode(node, "OPTIONS", mapDefinition, ",");
		return n;
	}

	private static void collectReachable(Set collectedKeys, ContentType targetType, ContentNodeI root) {
		Set children = root.getChildKeys();
		for (Iterator i = children.iterator(); i.hasNext();) {
			ContentKey k = (ContentKey) i.next();
			if (targetType.equals(k.getType())) {
				collectedKeys.add(k);
				continue;
			}
			collectReachable(collectedKeys, targetType, k.getContentNode());
		}
	}

}
