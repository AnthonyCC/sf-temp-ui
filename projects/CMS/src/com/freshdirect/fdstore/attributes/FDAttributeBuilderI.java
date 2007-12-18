/*
 * Created on Dec 13, 2004
 *
 */
package com.freshdirect.fdstore.attributes;

import java.util.List;

import com.freshdirect.cms.AttributeDefI;

/**
 * @author mrose
 *
 */
public interface FDAttributeBuilderI {
    
    public Attribute build(AttributeDefI aDef, Object value);
    
    public com.freshdirect.fdstore.attributes.EnumAttributeType getFDAttributeType();
    
    public Object buildValue(AttributeDefI aDef, Object value);
    
    public List buildValue(AttributeDefI aDef, List valueList);

}
