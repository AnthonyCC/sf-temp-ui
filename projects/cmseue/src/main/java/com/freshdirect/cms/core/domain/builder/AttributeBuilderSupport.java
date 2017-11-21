package com.freshdirect.cms.core.domain.builder;

import java.util.Date;

import com.freshdirect.cms.core.domain.ContentType;
import com.freshdirect.cms.core.domain.Relationship;

public final class AttributeBuilderSupport {

    public static AttributeBuilder attribute() {
        return AttributeBuilder.attribute();
    }

    public static AttributeBuilder stringAttribute(String name) {
        return AttributeBuilder.attribute().type(String.class).name(name);
    }

    public static AttributeBuilder booleanAttribute(String name) {
        return AttributeBuilder.attribute().type(Boolean.class).name(name);
    }

    public static AttributeBuilder integerAttribute(String name) {
        return AttributeBuilder.attribute().type(Integer.class).name(name);
    }

    public static AttributeBuilder doubleAttribute(String name) {
        return AttributeBuilder.attribute().type(Double.class).name(name);
    }

    public static AttributeBuilder dateAttribute(String name) {
        return AttributeBuilder.attribute().type(Date.class).name(name);
    }

    public static AttributeBuilder stringEnum(String name) {
        return AttributeBuilder.enumeratedAttribute().type(String.class).name(name);
    }

    public static AttributeBuilder integerEnum(String name) {
        return AttributeBuilder.enumeratedAttribute().type(Integer.class).name(name);
    }

    public static AttributeBuilder singleRelationship(String name, ContentType... destinationTypes) {
        return AttributeBuilder.relationship().name(name).one().destinationTypes(destinationTypes);
    }

    public static AttributeBuilder multiRelationship(String name, ContentType... destinationTypes) {
        return AttributeBuilder.relationship().name(name).many().destinationTypes(destinationTypes);
    }

    public static AttributeBuilder linkOneOf(ContentType... destinationTypes) {
        return AttributeBuilder.relationship().one().destinationTypes(destinationTypes);
    }

    public static AttributeBuilder linkManyOf(ContentType... destinationTypes) {
        return AttributeBuilder.relationship().many().destinationTypes(destinationTypes);
    }

    public static AttributeBuilder linkMany() {
        return AttributeBuilder.relationship().many().destinationTypes(Relationship.ANY_DESTINATION);
    }
}
