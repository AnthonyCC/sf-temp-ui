/*
 * Created on Dec 13, 2004
 *
 */
package com.freshdirect.fdstore.attributes.cms;

import com.freshdirect.cms.AttributeDefI;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.RelationshipDefI;
import com.freshdirect.fdstore.attributes.EnumAttributeType;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.content.ContentNodeModelUtil;

public class GenericNodeBuilder extends AbstractAttributeBuilder {

	public EnumAttributeType getFDAttributeType() {
		return EnumAttributeType.GENERIC_NODE;
	}

	public Object buildValue(AttributeDefI aDef, Object value) {
		RelationshipDefI relDef = (RelationshipDefI) aDef;
		ContentNodeI cNode = (ContentNodeI) value;
		ContentNodeModel model = ContentNodeModelUtil.constructModel(
				cNode.getKey(), relDef.isNavigable());
		return model;
	}

}
