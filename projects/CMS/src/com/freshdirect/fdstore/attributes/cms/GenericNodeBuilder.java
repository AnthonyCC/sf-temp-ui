/*
 * Created on Dec 13, 2004
 *
 */
package com.freshdirect.fdstore.attributes.cms;

import com.freshdirect.cms.AttributeDefI;
import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.RelationshipDefI;
import com.freshdirect.storeapi.content.ContentNodeModel;
import com.freshdirect.storeapi.content.ContentNodeModelUtil;

public class GenericNodeBuilder extends AbstractAttributeBuilder {

	public Object buildValue(AttributeDefI aDef, Object value) {
		RelationshipDefI relDef = (RelationshipDefI) aDef;
		ContentKey key = (ContentKey) value;
		ContentNodeModel model = ContentNodeModelUtil.constructModel(
				key, relDef.isNavigable());
		return model;
	}

}
