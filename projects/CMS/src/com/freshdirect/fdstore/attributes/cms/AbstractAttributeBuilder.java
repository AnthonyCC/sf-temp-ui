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
import com.freshdirect.cms.RelationshipDefI;
import com.freshdirect.cms.application.CmsManager;
import com.freshdirect.cms.fdstore.FDContentTypes;
import com.freshdirect.fdstore.attributes.Attribute;
import com.freshdirect.fdstore.attributes.FDAttributeBuilderI;
import com.freshdirect.fdstore.attributes.MultiAttribute;

/**
 * @author mrose
 *
 */
public abstract class AbstractAttributeBuilder implements FDAttributeBuilderI {
    
    
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
			if (cmsAttrDef instanceof RelationshipDefI) {
			    Object newValue = ((ContentKey) value).lookupContentNode();
			    attr.setValue(buildValue(cmsAttrDef, newValue));
			} else {
			    attr.setValue(buildValue(cmsAttrDef, value));
			}
		} else {
			attr = new MultiAttribute(getFDAttributeType(), cmsAttrDef.getName(), cmsAttrDef.isInheritable());
			if (cmsAttrDef instanceof RelationshipDefI) {
			    attr.setValue(buildValue(cmsAttrDef, dereferenceKeys((List) value)));
			} else {
			    attr.setValue(buildValue(cmsAttrDef, (List) value));
			}
		}
        
        return attr;
        
    }
    
    public List buildValue(AttributeDefI aDef, List valueList) {
        List vals = new ArrayList();
		for (Iterator i = valueList.iterator(); i.hasNext();) {
			Object value = (ContentNodeI) i.next();
			Object atrValue = buildValue(aDef, value);
			if (atrValue != null) {
			    vals.add(atrValue);
			}
		}
		return vals;
    }
    
    protected List dereferenceKeys(List contentKeys) {
	
		List cNodes = new ArrayList();
		for (Iterator i = contentKeys.iterator(); i.hasNext();) {
			ContentKey d = (ContentKey) i.next();
			cNodes.add(CmsManager.getInstance().getContentNode(d));
		}
		
		return cNodes;
    }
    
    protected static ContentNodeI findBestParent(ContentNodeI childNode) {

		if (FDContentTypes.PRODUCT.equals(childNode.getKey().getType())) {
			Object n = childNode.getAttribute("PRIMARY_HOME").getValue();
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
