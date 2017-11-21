package com.freshdirect.cms.core.domain;

import java.util.Arrays;

/**
 * This type defines relationship between content types
 *
 * @author segabor
 *
 */
public final class Relationship extends Attribute {

    public static final ContentType[] ANY_DESTINATION = new ContentType[] {};

    /**
     * Relationship cardinality
     */
    private final RelationshipCardinality cardinality;

    /**
     * Allowed child content types
     */
    private final ContentType[] destinationTypes;

    /**
     * Navigable qualifier.
     */
    private final boolean navigable;


    public Relationship(final String name, final AttributeFlags flags, boolean readOnly,
            final RelationshipCardinality cardinality, boolean navigable, final ContentType... destinationTypes) {
        super(name, flags, readOnly);

        if (cardinality == null) {
            throw new IllegalArgumentException("Missing cardinality parameter!");
        } else if (destinationTypes == null) {
            throw new IllegalArgumentException("Missing destination types parameter!");
        }

        this.cardinality = cardinality;
        this.destinationTypes = destinationTypes;
        this.navigable = navigable;
    }


    /**
     * Returns relationship cardinality.
     * @return
     */
    public RelationshipCardinality getCardinality() {
        return cardinality;
    }

    /**
     * Returns child content types.
     *
     * @return
     */
    public ContentType[] getDestinationTypes() {
        return destinationTypes;
    }

    /**
     * Navigable qualifier.
     * Navigable relationships span a tree-like
     * topology in CMS.
     *
     * @return
     */
    public boolean isNavigable() {
        return navigable;
    }


    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Relationship [cardinality=" + cardinality
                + ", destinationTypes=" + Arrays.toString(destinationTypes)
                + ", navigable=" + navigable
                + ", name=" + name
                + ", flags=" + flags
                + ", readOnly=" + readOnly + "]";
    }
}
