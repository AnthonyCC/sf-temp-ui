/*
 * Created on Dec 13, 2004
 *
 */
package com.freshdirect.fdstore.attributes.cms;

import com.freshdirect.fdstore.attributes.EnumAttributeType;

/**
 * @author mrose
 *
 */
public class IntegerBuilder extends PrimitiveBuilder {
    
    public EnumAttributeType getFDAttributeType() {
        return EnumAttributeType.INTEGER;
    }

}
