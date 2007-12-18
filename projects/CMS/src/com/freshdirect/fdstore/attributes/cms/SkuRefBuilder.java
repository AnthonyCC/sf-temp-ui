/*
 * Created on Dec 13, 2004
 *
 */
package com.freshdirect.fdstore.attributes.cms;

import com.freshdirect.cms.AttributeDefI;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.fdstore.attributes.EnumAttributeType;
import com.freshdirect.fdstore.content.SkuRef;

/**
 * @author mrose
 *
 */
public class SkuRefBuilder extends AbstractAttributeBuilder {
    
    public EnumAttributeType getFDAttributeType() {
        return EnumAttributeType.SKUREF;
    }
    
    public Object buildValue(AttributeDefI aDef, Object value) {
        ContentNodeI cNode = (ContentNodeI) value;
        ContentNodeI parentProd = findBestParent(cNode);
		if (parentProd == null)
			return null;
		ContentNodeI parentCat = findBestParent(parentProd);
		if (parentCat == null)
			return null;
		return new SkuRef(parentCat.getKey().getId(), parentProd.getKey().getId(), cNode.getKey().getId());
    }

}
