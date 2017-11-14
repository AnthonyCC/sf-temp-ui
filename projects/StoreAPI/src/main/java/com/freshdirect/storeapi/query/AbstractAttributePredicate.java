package com.freshdirect.storeapi.query;

import java.io.Serializable;
import java.util.Map;

import org.apache.commons.collections.Predicate;

import com.freshdirect.cms.core.domain.Attribute;
import com.freshdirect.storeapi.CmsLegacy;
import com.freshdirect.storeapi.ContentNode;
import com.freshdirect.storeapi.ContentNodeI;

/**
 * Template class for {@link org.apache.commons.collections.Predicate}s that
 * evaluate on a specific attribute of {@link com.freshdirect.storeapi.ContentNodeI}s.
 */
@CmsLegacy
public abstract class AbstractAttributePredicate implements Predicate,
		Serializable {

    private static final long serialVersionUID = -3349581482692004848L;

    private final Attribute attributeDef;

	/**
	 * @param attributeDef
	 *            definition of the attribute to evaluate on
	 */
	public AbstractAttributePredicate(Attribute attributeDef) {
		this.attributeDef = attributeDef;
	}

	public Attribute getAttributeDef() {
		return attributeDef;
	}

	@Override
    public final boolean evaluate(Object obj) {
        ContentNodeI node = (ContentNodeI) obj;
        if (node == null) {
            return false;
        }

        Map<Attribute, Object> payload = ((ContentNode) node).getPayload();
        if (!payload.keySet().contains(attributeDef)) {
            return false;
        }

        return this.evaluateValue(payload.get(attributeDef));
    }

	protected abstract boolean evaluateValue(Object value);

}
