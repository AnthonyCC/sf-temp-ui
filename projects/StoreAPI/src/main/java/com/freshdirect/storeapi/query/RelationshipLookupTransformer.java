package com.freshdirect.storeapi.query;

import org.apache.commons.collections.Transformer;

import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.domain.Relationship;
import com.freshdirect.cms.core.domain.RelationshipCardinality;
import com.freshdirect.storeapi.CmsLegacy;
import com.freshdirect.storeapi.ContentNodeI;
import com.freshdirect.storeapi.application.CmsManager;

/**
 * {@link org.apache.commons.collections.Transformer} that returns the
 * {@link com.freshdirect.storeapi.ContentNodeI} referenced in a relationship.
 */
@CmsLegacy
public class RelationshipLookupTransformer implements Transformer {

	private final Relationship relDef;

	public RelationshipLookupTransformer(Relationship relDef) {
		this.relDef = relDef;
		if (RelationshipCardinality.ONE != relDef.getCardinality()) {
			throw new IllegalArgumentException(
					"Only cardinality ONE is supported");
		}
	}

	@Override
	public Object transform(Object o) {
	    ContentNodeI node = (ContentNodeI) o;
		ContentKey k = (ContentKey) node.getAttributeValue(relDef.getName());
		return k == null ? null : CmsManager.getInstance().getContentNode(k);
	}

}
