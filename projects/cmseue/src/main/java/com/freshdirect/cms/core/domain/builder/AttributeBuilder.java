package com.freshdirect.cms.core.domain.builder;

import java.util.NoSuchElementException;

import com.freshdirect.cms.core.domain.Attribute;
import com.freshdirect.cms.core.domain.AttributeFlags;
import com.freshdirect.cms.core.domain.ContentType;
import com.freshdirect.cms.core.domain.Relationship;
import com.freshdirect.cms.core.domain.RelationshipCardinality;
import com.freshdirect.cms.core.domain.Scalar;
import com.google.common.base.Optional;

public final class AttributeBuilder {

    private enum AttributeKind {
        SCALAR, RELATIONSHIP, ENUM
    }

    private final AttributeKind kind;

    // -- basic properties (common) --

    private Optional<String> name = Optional.absent();

    // -- flags (optional, common) --

    private boolean required = false;
    private boolean inheritable = false;

    private boolean readOnly = false;

    // -- attribute fields --
    private Optional<Class<?>> attrType = Optional.absent();

    // -- relationship fields --

    private RelationshipCardinality cardinality = RelationshipCardinality.ONE;
    private boolean navigable = false;
    private Optional<ContentType[]> destinationTypes = Optional.absent();

    // -- enum values --
    private Optional<Object[]> enumValues = Optional.absent();

    private AttributeBuilder(AttributeKind kind) {
        this.kind = kind;
    }

    public static AttributeBuilder attribute() {
        return new AttributeBuilder(AttributeKind.SCALAR);
    }

    public static AttributeBuilder enumeratedAttribute() {
        return new AttributeBuilder(AttributeKind.ENUM);
    }

    public static AttributeBuilder relationship() {
        return new AttributeBuilder(AttributeKind.RELATIONSHIP);
    }

    public AttributeBuilder name(String name) {
        this.name = Optional.of(name);
        return this;
    }

    public AttributeBuilder toName(String name) {
        this.name = Optional.of(name);
        return this;
    }

    public AttributeBuilder required() {
        this.required = true;
        return this;
    }

    public AttributeBuilder inheritable() {
        this.inheritable = true;
        return this;
    }

    public AttributeBuilder readOnly() {
        this.readOnly = true;
        return this;
    }

    public AttributeBuilder type(Class<?> attrType) {
        this.attrType = Optional.<Class<?>>of(attrType);
        return this;
    }

    public AttributeBuilder withValues(Object... enums) {
        this.enumValues = Optional.of(enums);
        return this;
    }

    public AttributeBuilder many() {
        this.cardinality  = RelationshipCardinality.MANY;
        return this;
    }

    public AttributeBuilder one() {
        this.cardinality = RelationshipCardinality.ONE;
        return this;
    }

    public AttributeBuilder navigable() {
        this.navigable = true;
        return this;
    }

    public AttributeBuilder anyDestinationType() {
        this.destinationTypes = Optional.of(new ContentType[] {});
        return this;
    }

    public AttributeBuilder destinationTypes(ContentType... destinationTypes) {
        this.destinationTypes = Optional.of(destinationTypes);
        return this;
    }

    public AttributeBuilder enumValues(Object[] values) {
        this.enumValues = Optional.of(values);
        return this;
    }

    private Scalar buildAttribute() {

        // mandatory fields
        Class<?> type = null;
        String name = null;

        // optional fields
        final AttributeFlags flags = AttributeFlags.valueOf(required, inheritable);

        try {
            type = attrType.get();
        } catch (NoSuchElementException exc) {
            throw new RuntimeException("Error! Attribute type is not specified!");
        }

        try {
            name = this.name.get();
        } catch (NoSuchElementException exc) {
            throw new RuntimeException("Attribute 'name' is a required parameter! Don't forget to invoke name() method.");
        }

        return new Scalar(name, flags, readOnly, type);
    }

    private Scalar buildEnum() {

        // mandatory fields
        Class<?> type = null;
        Object[] enums = null;
        String name = null;

        // optional fields
        final AttributeFlags flags = AttributeFlags.valueOf(required, inheritable);

        try {
            type = attrType.get();
        } catch (NoSuchElementException exc) {
            throw new RuntimeException("Error! Attribute type is not specified!");
        }

        try {
            name = this.name.get();
        } catch (NoSuchElementException exc) {
            throw new RuntimeException("Attribute 'name' is a required parameter! Don't forget to invoke name() method.");
        }

        try {
            enums = this.enumValues.get();
        } catch (NoSuchElementException exc) {
            throw new RuntimeException("Missing enumerated values!");
        }

        return new Scalar(name, flags, readOnly, type, enums);
    }

    private Relationship buildRelationship() {
        // mandatory fields
        String name = null;
        ContentType[] destTypes = null;

        // optional fields
        AttributeFlags flags = AttributeFlags.valueOf(required, inheritable);

        try {
            name = this.name.get();
        } catch (NoSuchElementException exc) {
            // Incomplete Configuration
            throw new RuntimeException("Relationship 'name' is a required parameter! Don't forget to specify it using name() method.");
        }

        try {
            destTypes = this.destinationTypes.get();
        } catch (NoSuchElementException exc) {
            // Incomplete Configuration
            throw new RuntimeException("Relationship 'destinationTypes' is required parameter! Don't forget to specify it using destinationTypes() method.");
        }

        if (navigable && RelationshipCardinality.ONE == cardinality) {
            throw new RuntimeException("Only relationships with MANY cardinality support navigable property.");
        }

        return new Relationship(name, flags, readOnly, cardinality, navigable, destTypes);
    }

    public Attribute build() {
        switch (kind) {
            case SCALAR:
                return buildAttribute();
            case RELATIONSHIP:
                return buildRelationship();
            case ENUM:
                return buildEnum();
            default:
                return null;
        }
    }
}
