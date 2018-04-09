package com.freshdirect.cms.core.domain;

import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.freshdirect.cms.category.UnitTest;

@RunWith(MockitoJUnitRunner.class)
@Category(UnitTest.class)
public class ContentKeyFactoryTest {

    @Test
    public void testValidKey() {
        ContentKey contentKey = ContentKeyFactory.get(ContentType.Product, "test");

        Assert.assertEquals(ContentType.Product, contentKey.getType());
        Assert.assertEquals("test", contentKey.getId());
        Assert.assertEquals("Product:test", contentKey.getEncoded());
    }

    @Test
    public void testValidKeyFromString() {
        ContentKey contentKey = ContentKeyFactory.get("Product:test");

        Assert.assertEquals(ContentType.Product, contentKey.getType());
        Assert.assertEquals("test", contentKey.getId());
        Assert.assertEquals("Product:test", contentKey.getEncoded());
    }

    @Test
    public void testValidKeyFromTwoStrings() {
        ContentKey contentKey = ContentKeyFactory.get("Product", "test");

        Assert.assertEquals(ContentType.Product, contentKey.getType());
        Assert.assertEquals("test", contentKey.getId());
        Assert.assertEquals("Product:test", contentKey.getEncoded());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyId() {
        ContentKeyFactory.get(ContentType.Product, "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullId() {
        ContentKeyFactory.get(ContentType.Product, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullType() {
        ContentKeyFactory.get((ContentType) null, "test");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullStringType() {
        ContentKeyFactory.get((String) null, "test");
    }
}
