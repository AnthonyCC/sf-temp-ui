package com.freshdirect.cms.query;

import org.apache.commons.collections.Transformer;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.EnumCardinality;
import com.freshdirect.cms.RelationshipDefI;

/**
 * {@link org.apache.commons.collections.Transformer} that returns the
 * {@link com.freshdirect.cms.ContentNodeI} referenced in a relationship.
 */
public class RelationshipLookupTransformer implements Transformer {

	private final RelationshipDefI relDef;

	public RelationshipLookupTransformer(RelationshipDefI relDef) {
		this.relDef = relDef;
		if (!EnumCardinality.ONE.equals(relDef.getCardinality())) {
			throw new IllegalArgumentException(
					"Only cardinality ONE is supported");
		}
	}

	public Object transform(Object o) {
		ContentNodeI node = (ContentNodeI) o;
		ContentKey k = (ContentKey) node.getAttribute(relDef.getName())
				.getValue();
		return k == null ? null : k.lookupContentNode();
	}

}
