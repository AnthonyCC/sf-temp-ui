/*
 * Created on Dec 13, 2004
 *
 */
package com.freshdirect.storeapi.attributes.cms;

import com.freshdirect.cms.core.domain.Attribute;
import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.domain.Relationship;
import com.freshdirect.storeapi.CmsLegacy;
import com.freshdirect.storeapi.content.ContentNodeModel;
import com.freshdirect.storeapi.content.ContentNodeModelUtil;

@CmsLegacy
public class GenericNodeBuilder extends AbstractAttributeBuilder {

    @Override
    public Object buildValue(Attribute aDef, Object value) {
        Relationship relDef = (Relationship) aDef;
        ContentKey key = (ContentKey) value;
        ContentNodeModel model = ContentNodeModelUtil.constructModel(key, relDef.isNavigable());
        return model;
    }

}
