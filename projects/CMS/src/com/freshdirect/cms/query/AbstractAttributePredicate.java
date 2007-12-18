package com.freshdirect.cms.query;

import java.io.Serializable;

import org.apache.commons.collections.Predicate;

import com.freshdirect.cms.AttributeDefI;
import com.freshdirect.cms.AttributeI;
import com.freshdirect.cms.ContentNodeI;

/**
 * Template class for {@link org.apache.commons.collections.Predicate}s that
 * evaluate on a specific attribute of {@link com.freshdirect.cms.ContentNodeI}s.
 */
public abstract class AbstractAttributePredicate implements Predicate,
		Serializable {

	private final AttributeDefI attributeDef;

	/**
	 * @param attributeDef
	 *            definition of the attribute to evaluate on
	 */
	public AbstractAttributePredicate(AttributeDefI attributeDef) {
		this.attributeDef = attributeDef;
	}

	public AttributeDefI getAttributeDef() {
		return attributeDef;
	}

	public final boolean evaluate(Object obj) {
		ContentNodeI node = (ContentNodeI) obj;
		if (node == null) {
			return false;
		}
		String attrName = attributeDef.getName();
		AttributeI attr = node.getAttribute(attrName);
		if (attr == null) {
			return false;
		}
		if (!attr.getDefinition().getAttributeType().equals(
				attributeDef.getAttributeType())) {
			throw new IllegalArgumentException("Attribute " + attrName
					+ " is not of type " + attributeDef.getAttributeType()
					+ " on " + node);
		}
		return this.evaluate(attr);
	}

	protected abstract boolean evaluate(AttributeI attribute);

}
