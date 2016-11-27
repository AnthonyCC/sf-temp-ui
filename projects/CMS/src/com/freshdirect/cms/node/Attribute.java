/*
 * Created on Nov 3, 2004
 *
 */
package com.freshdirect.cms.node;

import com.freshdirect.cms.AttributeDefI;
import com.freshdirect.cms.AttributeI;
import com.freshdirect.cms.ContentNodeI;

/**
 * Trivial implementation of {@link com.freshdirect.cms.AttributeI}.
 */
class Attribute implements AttributeI {

	private final ContentNodeI contentNode;
	private final AttributeDefI attrDef;

	protected Object value;

	public Attribute(ContentNodeI contentNode, AttributeDefI attrDef) {
		this.contentNode = contentNode;
		this.attrDef = attrDef;
	}

	public String getName() {
		return this.attrDef.getName();
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public AttributeDefI getDefinition() {
		return this.attrDef;
	}

	public ContentNodeI getContentNode() {
		return this.contentNode;
	}

	public String toString() {
		return "[" + attrDef.getName() + ":" + value + "]";
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;

		if (obj == null)
			return false;

		if (obj instanceof Attribute) {
			Object val = ((Attribute) obj).getValue();
			Object mine = this.getValue();
			if (val == null) {
				return mine == null;
			}
			return val.equals(mine);
		}
		return false;
	}
}