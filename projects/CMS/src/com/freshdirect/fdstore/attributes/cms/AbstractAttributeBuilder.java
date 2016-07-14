/*
 * Created on Dec 13, 2004
 *
 */
package com.freshdirect.fdstore.attributes.cms;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.freshdirect.cms.AttributeDefI;
import com.freshdirect.cms.EnumCardinality;
import com.freshdirect.fdstore.attributes.FDAttributeBuilderI;

/**
 * @author mrose
 *
 */
public abstract class AbstractAttributeBuilder implements FDAttributeBuilderI {
    
    public abstract Object buildValue(AttributeDefI aDef, Object value);
    
    @Override
    public Object constructValue(AttributeDefI cmsAttrDef, Object value) {
        if (!EnumCardinality.MANY.equals(cmsAttrDef.getCardinality())) {
            if (value instanceof List) {
                value = ((List) value).get(0);
            }
            return buildValue(cmsAttrDef, value);
        }
		return buildListValue(cmsAttrDef, (List) value);
    }
    
    
    List buildListValue(AttributeDefI aDef, List valueList) {
        List vals = new ArrayList();
        for (Iterator i = valueList.iterator(); i.hasNext();) {
            Object value = i.next();
            Object atrValue = buildValue(aDef, value);
            if (atrValue != null) {
                vals.add(atrValue);
            }
        }
        return vals;
    }
}
