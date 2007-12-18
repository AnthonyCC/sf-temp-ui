package com.freshdirect.cms.validation;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.freshdirect.cms.AttributeDefI;
import com.freshdirect.cms.AttributeI;
import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.ContentTypeDefI;
import com.freshdirect.cms.EnumCardinality;
import com.freshdirect.cms.RelationshipDefI;
import com.freshdirect.cms.application.CmsRequestI;
import com.freshdirect.cms.application.ContentServiceI;

/**
 * {@link com.freshdirect.cms.validation.ContentValidatorI} to check basic structural issues.
 * Ensures the following:
 * <ul>
 * <li>node has a non-null key</li>
 * <li>required attributes have values</li>
 * <li>relationships values have no nulls and are of the correct types</li>
 * </ul>
 */
public class BasicValidator implements ContentValidatorI {

	public void validate(ContentValidationDelegate delegate, ContentServiceI service, ContentNodeI node, CmsRequestI request) {
		if (node.getKey() == null) {
			delegate.record("Node with null content key");
			return;
		}

		ContentTypeDefI typeDef = node.getDefinition();

		// TODO check that all required attrs have value
		for (Iterator i = node.getAttributes().entrySet().iterator(); i.hasNext();) {
			Map.Entry e = (Map.Entry) i.next();
			String name = (String) e.getKey();
			AttributeDefI def = typeDef.getAttributeDef(name);
			if (def == null) {
				// FIXME attribute defs should be consistent, but CompositeContentNode may not always be (as long as it works based on typeservice) 
				continue;
				//throw new ContentValidationException("No definition for attribute " + name, node.getKey());
			}
			AttributeI value = (AttributeI) e.getValue();

			if (def instanceof RelationshipDefI) {
				if (EnumCardinality.MANY.equals(def.getCardinality()))
					validateRelationship(delegate, service, node, (RelationshipDefI) def, (List) value.getValue());
				// TODO: validate relationships of cardinality = ONE
			} else {
				validateAttribute(delegate, node, def, value.getValue());
			}
		}
	}

	private void validateAttribute(ContentValidationDelegate delegate, ContentNodeI node, AttributeDefI def, Object value)
		throws ContentValidationException {
		if (value == null) {
			if (def.isRequired()) {
				delegate.record(node.getKey(), def.getName(), "Required value is null");
			}
			return;
		}
		// TODO validate attribute value
		/*
		 Class vc = value.getClass();
		 Class exp = def.getAttributeType().getValueClass();
		 if (!exp.isAssignableFrom(vc)) {
		 throw new ContentValidationException(def.getName()
		 + ": Invalid value type "
		 + vc.getName()
		 + ", expected "
		 + exp.getName());
		 }
		 */
	}

	private void validateRelationship(
		ContentValidationDelegate delegate,
		ContentServiceI service,
		ContentNodeI node,
		RelationshipDefI def,
		List values) throws ContentValidationException {
		if (values == null)
			return;

		for (Iterator i = values.iterator(); i.hasNext();) {
			Object o = i.next();

			if (o == null) {
				delegate.record(node.getKey(), def.getName(), "null value in List");
				return;
			}

			if (!(o instanceof ContentKey)) {
				delegate.record(node.getKey(), def.getName(), "expected ContentKey, found " + o.getClass().getName());
				return;
			}
			ContentKey destKey = (ContentKey) o;

			if (!def.getContentTypes().contains(destKey.getType())) {
				delegate.record(node.getKey(), def.getName(), "reference to incorrect node "
					+ destKey
					+ ", expected type "
					+ def.getContentTypes());
				return;
			}

			ContentNodeI destNode = service.getContentNode(destKey);
			if (destNode == null) {
				delegate.record(node.getKey(), def.getName(), "reference to nonexsistent node " + destKey);
			}
		}

	}

}
