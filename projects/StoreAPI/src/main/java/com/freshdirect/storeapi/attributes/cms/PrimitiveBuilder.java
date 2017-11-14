/*
 * Created on Dec 13, 2004
 *
 */
package com.freshdirect.storeapi.attributes.cms;

import com.freshdirect.cms.core.domain.Attribute;
import com.freshdirect.storeapi.CmsLegacy;

/**
 * @author mrose
 *
 */
@CmsLegacy
public class PrimitiveBuilder extends AbstractAttributeBuilder {

    public PrimitiveBuilder() {
    }

    @Override
    public Object buildValue(Attribute aDef, Object value) {
        return value;
    }

}
