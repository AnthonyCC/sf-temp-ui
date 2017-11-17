/*
 * Created on Dec 13, 2004
 *
 */
package com.freshdirect.storeapi.attributes.cms;

import com.freshdirect.cms.core.domain.Attribute;
import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.storeapi.CmsLegacy;
import com.freshdirect.storeapi.content.ContentFactory;

/**
 * @author mrose
 *
 */
@CmsLegacy
public class DomainValueBuilder extends AbstractAttributeBuilder {

    @Override
    public Object buildValue(Attribute aDef, Object value) {
        ContentKey key = (ContentKey) value;
        return ContentFactory.getInstance().getDomainValueById(key.getId());
    }
}
