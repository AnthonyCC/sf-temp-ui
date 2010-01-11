/*
 * Created on Dec 13, 2004
 *
 */
package com.freshdirect.fdstore.attributes;

import com.freshdirect.cms.AttributeDefI;

/**
 * @author mrose
 *
 */
public interface FDAttributeBuilderI {
    
    public com.freshdirect.fdstore.attributes.EnumAttributeType getFDAttributeType();
    
    public Object constructValue(AttributeDefI cmsAttrDef, Object value);
    
}
