package com.freshdirect.cms.core.domain;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.freshdirect.cms.category.UnitTest;

@Category(UnitTest.class)
public class RelationshipAttributeTest {

    @Test
    public void testRelationshipWithNullName() {

        try {
            @SuppressWarnings("unused")
            Attribute rship = new Relationship(null, AttributeFlags.NONE, true, RelationshipCardinality.ONE, false, new ContentType[] {});

            fail("Creating relationship with null name should blow up the test");
        } catch (IllegalArgumentException exc) {
            assertTrue(exc.getMessage().contains("Missing name"));
        }
    }

    @Test
    public void testRelationshipWithNullFlags() {
        Attribute scalar = new Relationship("nullFlags", null, true, RelationshipCardinality.ONE, false, new ContentType[] { ContentType.Category });

        assertTrue("Flags not specified in construction should fall back to NONE", AttributeFlags.NONE == scalar.getFlags());
    }

    @Test
    public void testRelationshipWithoutCardinality() {

        try {
            @SuppressWarnings("unused")
            Attribute rship = new Relationship("noCardinality", AttributeFlags.NONE, true, null, false, new ContentType[] {});

            fail("Creating relationship without cardinality should blow up the test");
        } catch (IllegalArgumentException exc) {
            assertTrue(exc.getMessage().contains("Missing cardinality"));
        }
    }

    @Test
    public void testRelationshipWithoutDestinationTypes() {

        try {
            @SuppressWarnings("unused")
            Attribute rship = new Relationship("noDestTypes", AttributeFlags.NONE, true, RelationshipCardinality.ONE, false, null);

            fail("Creating relationship with null destination types should blow up the test");
        } catch (IllegalArgumentException exc) {
            assertTrue(exc.getMessage().contains("Missing destination types"));
        }
    }

    @Test
    public void testRelationshipWithOneDestinationType() {
        Relationship relationship = new Relationship("oneDest", AttributeFlags.NONE, true, RelationshipCardinality.ONE, false, new ContentType[] { ContentType.Category });

        assertTrue(relationship != null);
        assertTrue(relationship.getFlags() == AttributeFlags.NONE);
        assertTrue(relationship.getCardinality() != null);
        assertTrue(relationship.getCardinality() == RelationshipCardinality.ONE);
        assertTrue(!relationship.isNavigable());
        assertTrue(relationship.getDestinationTypes() != null);
        assertTrue(relationship.getDestinationTypes().length == 1);
    }

}
