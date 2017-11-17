package com.freshdirect.cms.core.domain;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Date;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.freshdirect.cms.category.UnitTest;

@Category(UnitTest.class)
public class ScalarAttributeTest {

    @Test
    public void testScalarWithNullName() {

        try {
            @SuppressWarnings("unused")
            Attribute scalar = new Scalar(null, AttributeFlags.NONE, true, null);

            fail("Creating scalars with null name should blow up the test");
        } catch (IllegalArgumentException exc) {
            assertTrue(exc.getMessage().contains("Missing name"));
        }
    }

    @Test
    public void testScalarWithNullFlags() {
        Scalar scalar = new Scalar("attr1", null, true, String.class);

        assertTrue("Scalar type mismatch", String.class.equals(scalar.getType()));
        assertTrue("Scalar should not be enumerated", !scalar.isEnumerated());
        assertTrue("Flags not specified in construction should fall back to NONE", AttributeFlags.NONE == scalar.getFlags());
    }

    @Test
    public void testScalarWithNullType() {

        try {
            @SuppressWarnings("unused")
            Attribute scalar = new Scalar("test", AttributeFlags.NONE, true, null);

            fail("Creating scalars with null type should blow up the test");
        } catch (IllegalArgumentException exc) {
            assertTrue(exc.getMessage().contains("is null"));
        }
    }

    @Test
    public void testScalarWithInvalidType() {
        try {
            @SuppressWarnings("unused")
            Attribute scalar = new Scalar("test", AttributeFlags.NONE, true, Long.class);

            fail("Creating scalars with java.lang.Long type should blow up the test");
        } catch (IllegalArgumentException exc) {
            assertTrue(exc.getMessage().contains("Unsupported scalar type"));
        }

    }

    @Test
    public void testScalarWithValidTypes() {
        try {
            @SuppressWarnings("unused")
            Scalar scalar;

            scalar = new Scalar("stringAttr", AttributeFlags.NONE, true, String.class);
            assertTrue("Scalar type mismatch", String.class.equals(scalar.getType()));
            assertTrue("Scalar should not be enumerated", !scalar.isEnumerated());

            scalar = new Scalar("boolAttr", AttributeFlags.NONE, true, Boolean.class);
            assertTrue("Scalar type mismatch", Boolean.class.equals(scalar.getType()));
            assertTrue("Scalar should not be enumerated", !scalar.isEnumerated());

            scalar = new Scalar("intAttr", AttributeFlags.NONE, true, Integer.class);
            assertTrue("Scalar type mismatch", Integer.class.equals(scalar.getType()));
            assertTrue("Scalar should not be enumerated", !scalar.isEnumerated());

            scalar = new Scalar("doubleAttr", AttributeFlags.NONE, true, Double.class);
            assertTrue("Scalar type mismatch", Double.class.equals(scalar.getType()));
            assertTrue("Scalar should not be enumerated", !scalar.isEnumerated());

            scalar = new Scalar("dateAttr", AttributeFlags.NONE, true, Date.class);
            assertTrue("Scalar type mismatch", Date.class.equals(scalar.getType()));
            assertTrue("Scalar should not be enumerated", !scalar.isEnumerated());
        } catch (IllegalArgumentException exc) {
            fail(exc.getMessage());
        }

    }

    @Test
    public void testEnumWithNullEnums() {

        try {
            @SuppressWarnings("unused")
            Attribute scalar = new Scalar("test", AttributeFlags.NONE, true, null, null);

            fail("Creating enums with null enum list should blow up the test");
        } catch (IllegalArgumentException exc) {
            assertTrue(exc.getMessage().contains("is null"));
        }
    }

    @Test
    public void testEnumWithEmptyEnums() {

        try {
            @SuppressWarnings("unused")
            Attribute scalar = new Scalar("test", AttributeFlags.NONE, true, null, new Class<?>[] {});

            fail("Creating enums with empty enum list should blow up the test");
        } catch (IllegalArgumentException exc) {
            assertTrue(exc.getMessage().contains("is null"));
        }
    }

    @Test
    public void testEnumWithNotMatchingTypes() {
        try {
            @SuppressWarnings("unused")
            Attribute scalar = new Scalar("test", AttributeFlags.NONE, true, Integer.class, new String[] {"a", "b"});

            fail("Creating scalars with java.lang.Long type should blow up the test");
        } catch (IllegalArgumentException exc) {
            assertTrue(exc.getMessage().contains("must match scalar type"));
        }

    }

    @Test
    public void testEnumWithSomeNullEnums() {
        try {
            @SuppressWarnings("unused")
            Attribute scalar = new Scalar("test", AttributeFlags.NONE, true, String.class, new String[] {"a", null, "b"});

            fail("Creating scalars having null enum should blow up the test");
        } catch (IllegalArgumentException exc) {
            assertTrue(exc.getMessage().contains("null enumerated"));
        }

    }

    @Test
    public void testEnumWithValidTypes() {
        try {
            Scalar scalar;
            scalar = new Scalar("test", AttributeFlags.NONE, true, String.class, new String[] {"a", "b"});
            assertTrue("Scalar type mismatch", String.class.equals(scalar.getType()));
            assertTrue("Scalar should be enumerated", scalar.isEnumerated());

            scalar = new Scalar("test", AttributeFlags.NONE, true, Integer.class, new Integer[] {1, 2, 3});
            assertTrue("Scalar type mismatch", Integer.class.equals(scalar.getType()));
            assertTrue("Scalar should be enumerated", scalar.isEnumerated());
        } catch (IllegalArgumentException exc) {
            fail(exc.getMessage());
        }
    }
}
