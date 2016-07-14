/*
 * Created on Aug 10, 2005
 */
package com.freshdirect.cms.fdstore;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.freshdirect.cms.AttributeDefI;
import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.EnumAttributeType;
import com.freshdirect.cms.application.ContentServiceI;
import com.freshdirect.cms.application.DraftContext;
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

    @Override
	public ContentNodeI decorateNode(ContentNodeI node, ContentServiceI contentService, DraftContext draftContext) {
		if (!FDContentTypes.CONFIGURED_PRODUCT.equals(node.getKey().getType())) {
			return null;
		}

		Map<String, AttributeDefI> mapDefinition = new HashMap<String, AttributeDefI>();

		ContentKey skuKey = (ContentKey) node.getAttribute("SKU").getValue();
		if (skuKey != null) {
			Set<ContentKey> charKeys = new HashSet<ContentKey>();
			collectReachable(charKeys, FDContentTypes.ERP_CHARACTERISTIC, contentService.getContentNode(skuKey, draftContext), contentService, draftContext );
			for (final ContentKey cKey : charKeys) {
				ContentNodeI charNode = contentService.getContentNode(cKey, draftContext);
				String charName = (String) charNode.getAttribute("name").getValue();
				Map<Object, String> values = new HashMap<Object, String>();
				Set<ContentKey> cvKeys = charNode.getChildKeys();
				for (Iterator<ContentKey> cvi = cvKeys.iterator(); cvi.hasNext();) {
					ContentNodeI cvNode = contentService.getContentNode((ContentKey) cvi.next(), draftContext);
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

	private static void collectReachable(Set<ContentKey> collectedKeys, ContentType targetType, ContentNodeI root, ContentServiceI contentService, DraftContext draftContext) {
		Set<ContentKey> children = root.getChildKeys();
		for (final ContentKey k : children) {
			if (targetType.equals(k.getType())) {
				collectedKeys.add(k);
				continue;
			}
			collectReachable(collectedKeys, targetType, contentService.getContentNode(k, draftContext), contentService, draftContext);
		}
	}

}
