package com.freshdirect.fdstore.attributes.cms;

import com.freshdirect.cms.AttributeDefI;
import com.freshdirect.cms.ContentKey;
import com.freshdirect.fdstore.attributes.EnumAttributeType;
import com.freshdirect.fdstore.content.ContentFactory;

public class GenericContentNodeBuilder extends AbstractAttributeBuilder {

    final EnumAttributeType type;
    
    public GenericContentNodeBuilder(EnumAttributeType type) {
        this.type = type;
    }

    @Override
    public Object buildValue(AttributeDefI aDef, Object value) {
        return ContentFactory.getInstance().getContentNodeByKey((ContentKey) value);
    }

    @Override
    public EnumAttributeType getFDAttributeType() {
        return type;
    }

}
