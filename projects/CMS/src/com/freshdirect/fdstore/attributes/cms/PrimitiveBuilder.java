/*
 * Created on Dec 13, 2004
 *
 */
package com.freshdirect.fdstore.attributes.cms;

import com.freshdirect.cms.AttributeDefI;
import com.freshdirect.fdstore.attributes.EnumAttributeType;

/**
 * @author mrose
 *
 */
public class PrimitiveBuilder extends AbstractAttributeBuilder {
    
    final EnumAttributeType type;
    
    public PrimitiveBuilder(EnumAttributeType type) {
        this.type = type;
    }
    
    public Object buildValue(AttributeDefI aDef, Object value) {
        return value;
    }
    
    @Override
    public EnumAttributeType getFDAttributeType() {
        return type;
    }

}
