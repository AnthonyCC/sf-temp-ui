/*
 * Created on Dec 13, 2004
 *
 */
package com.freshdirect.fdstore.attributes.cms;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.freshdirect.cms.AttributeDefI;
import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.EnumCardinality;
import com.freshdirect.cms.application.CmsManager;
import com.freshdirect.cms.fdstore.FDContentTypes;
import com.freshdirect.fdstore.attributes.FDAttributeBuilderI;

/**
 * @author mrose
 *
 */
public abstract class AbstractAttributeBuilder implements FDAttributeBuilderI {
    
    public abstract Object buildValue(AttributeDefI aDef, Object value);
    /*
    public Attribute build(AttributeDefI cmsAttrDef, Object value) {

        Attribute attr = null;

        if (!EnumCardinality.MANY.equals(cmsAttrDef.getCardinality())) {
            attr = new Attribute(getFDAttributeType(), cmsAttrDef.getName(), cmsAttrDef.isInheritable());
            //
            // need to unwrap improperly created relationships
            //
            if (value instanceof List) {
                value = ((List) value).get(0);
            }
            attr.setValue(buildValue(cmsAttrDef, value));
        } else {
            throw new RuntimeException("MultiAttribute support is removed, this is an illegal usage of the API " +
            		"(calling getAttribute('"+cmsAttrDef.getName()+"')");
        }

        return attr;
    }*/
    
    public Object constructValue(AttributeDefI cmsAttrDef, Object value) {
        if (!EnumCardinality.MANY.equals(cmsAttrDef.getCardinality())) {
            if (value instanceof List) {
                value = ((List) value).get(0);
            }
            return buildValue(cmsAttrDef, value);
        } else {
            return buildListValue(cmsAttrDef, (List) value);
        }
    }
    
    
    @SuppressWarnings("unchecked")
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
    
    protected ContentNodeI findBestParent(ContentNodeI childNode) {

		if (FDContentTypes.PRODUCT.equals(childNode.getKey().getType())) {
			Object n = childNode.getAttributeValue("PRIMARY_HOME");
			if(n != null) {		
				ContentNodeI d = ((ContentKey) n).lookupContentNode();
				return d;
			}
		}

		Collection parents = CmsManager.getInstance().getParentKeys(childNode.getKey());
		if (!parents.isEmpty()) {
			List parentList = new ArrayList(parents);
			return ((ContentKey)parentList.get(0)).lookupContentNode();
		} else {
			System.out.println("no parent found for " + childNode.getKey());
			return null;
		}

	}
    

}
