package com.freshdirect.storeapi.attributes.cms;

import com.freshdirect.cms.core.domain.Attribute;
import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.storeapi.CmsLegacy;
import com.freshdirect.storeapi.content.ContentFactory;

@CmsLegacy
public class GenericContentNodeBuilder extends AbstractAttributeBuilder {

    public GenericContentNodeBuilder() {
    }

    @Override
    public Object buildValue(Attribute aDef, Object value) {
        return ContentFactory.getInstance().getContentNodeByKey((ContentKey) value);
    }

}
