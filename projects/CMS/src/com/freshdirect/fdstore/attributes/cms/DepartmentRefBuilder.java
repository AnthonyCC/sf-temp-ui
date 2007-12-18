/*
 * Created on Dec 13, 2004
 *
 */
package com.freshdirect.fdstore.attributes.cms;

import com.freshdirect.cms.AttributeDefI;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.fdstore.attributes.EnumAttributeType;
import com.freshdirect.fdstore.content.DepartmentRef;

/**
 * @author mrose
 *
 */
public class DepartmentRefBuilder extends AbstractAttributeBuilder {
    
    public EnumAttributeType getFDAttributeType() {
        return EnumAttributeType.DEPARTMENTREF;
    }
    
    public Object buildValue(AttributeDefI aDef, Object value) {
        ContentNodeI cNode = (ContentNodeI) value;
        return new DepartmentRef(cNode.getKey().getId());
    }

}
