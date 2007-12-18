/*
 * Created on Oct 28, 2004
 *
 */
package com.freshdirect.cms.node;

import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.RelationshipDefI;
import com.freshdirect.cms.RelationshipI;

/**
 * Trivial implementation of {@link com.freshdirect.cms.RelationshipI}.
 */
class Relationship extends Attribute implements RelationshipI {

	public Relationship(ContentNodeI node, RelationshipDefI relDef) {
		super(node, relDef);
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;

		if (obj == null)
			return false;

		if (obj instanceof Relationship) {
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
