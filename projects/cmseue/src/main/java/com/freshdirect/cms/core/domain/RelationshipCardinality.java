package com.freshdirect.cms.core.domain;

/**
 * Relationship cardinality
 *
 * @author segabor
 *
 */
public enum RelationshipCardinality {
    /**
     * ONE keyword denotes a single relationship
     * Value should contains only a content key
     */
    ONE,

    /**
     * MANY keyword marks relationship may contain
     * an ordered list of content keys
     */
    MANY;
}
