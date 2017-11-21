/*
 * Created on Dec 13, 2004
 *
 */
package com.freshdirect.storeapi.attributes.cms;

import java.util.ArrayList;
import java.util.List;

import com.freshdirect.cms.core.domain.Attribute;
import com.freshdirect.cms.core.domain.Relationship;
import com.freshdirect.cms.core.domain.RelationshipCardinality;
import com.freshdirect.storeapi.CmsLegacy;
import com.freshdirect.storeapi.attributes.FDAttributeBuilderI;

/**
 * @author mrose
 *
 */
@CmsLegacy
public abstract class AbstractAttributeBuilder implements FDAttributeBuilderI {

    public abstract Object buildValue(Attribute aDef, Object value);

    @Override
    public Object constructValue(Attribute cmsAttrDef, Object value) {
        boolean isManyCardinality = cmsAttrDef instanceof Relationship && RelationshipCardinality.MANY == ((Relationship) cmsAttrDef).getCardinality();

        if (!isManyCardinality) {
            if (value instanceof List) {
                value = ((List<Object>) value).get(0);
            }
            return buildValue(cmsAttrDef, value);
        }
        return buildListValue(cmsAttrDef, (List<Object>) value);
    }

    List<Object> buildListValue(Attribute aDef, List<Object> valueList) {
        List<Object> vals = new ArrayList<Object>();
        for (Object value : valueList) {
            Object atrValue = buildValue(aDef, value);
            if (atrValue != null) {
                vals.add(atrValue);
            }
        }
        return vals;
    }
}
