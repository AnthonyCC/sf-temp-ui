/*
 * Created on Dec 13, 2004
 *
 */
package com.freshdirect.fdstore.attributes.cms;

import com.freshdirect.cms.AttributeDefI;

/**
 * @author mrose
 *
 */
public abstract class PrimitiveBuilder extends AbstractAttributeBuilder {
    
    public Object buildValue(AttributeDefI aDef, Object value) {
        return value;
    }

}
