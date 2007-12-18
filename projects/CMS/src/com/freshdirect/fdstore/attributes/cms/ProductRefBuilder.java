/*
 * Created on Dec 13, 2004
 *
 */
package com.freshdirect.fdstore.attributes.cms;

import com.freshdirect.cms.AttributeDefI;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.fdstore.attributes.EnumAttributeType;
import com.freshdirect.fdstore.content.ProductRef;

/**
 * @author mrose
 *
 */
public class ProductRefBuilder extends AbstractAttributeBuilder {
    
    public EnumAttributeType getFDAttributeType() {
        return EnumAttributeType.PRODUCTREF;
    }
    
    public Object buildValue(AttributeDefI aDef, Object value) {
        ContentNodeI cNode = (ContentNodeI) value;
        ContentNodeI parentCat = findBestParent(cNode);
		if (parentCat == null)
			return null;
		return new ProductRef(parentCat.getKey().getId(), cNode.getKey().getId());
    }

}
