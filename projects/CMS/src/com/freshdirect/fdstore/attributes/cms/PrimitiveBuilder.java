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
public class PrimitiveBuilder extends AbstractAttributeBuilder {
    
    
    public PrimitiveBuilder() {
    }
    
    public Object buildValue(AttributeDefI aDef, Object value) {
        return value;
    }
    

}
