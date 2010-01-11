/*
 * Created on Dec 13, 2004
 *
 */
package com.freshdirect.fdstore.attributes.cms;

import com.freshdirect.cms.AttributeDefI;
import com.freshdirect.cms.ContentKey;
import com.freshdirect.fdstore.attributes.EnumAttributeType;
import com.freshdirect.fdstore.content.ContentFactory;

/**
 * @author mrose
 * 
 */
public class DomainValueBuilder extends AbstractAttributeBuilder {

    public EnumAttributeType getFDAttributeType() {
        return EnumAttributeType.DOMAINVALUEREF;
    }

    public Object buildValue(AttributeDefI aDef, Object value) {
        ContentKey key = (ContentKey) value;
        return ContentFactory.getInstance().getDomainValueById(key.getId());

    }
}
