package com.freshdirect.cms.changecontrol.service;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import com.freshdirect.cms.category.UnitTest;
import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.domain.ContentKeyFactory;

@Category(UnitTest.class)
@RunWith(MockitoJUnitRunner.class)
public class ContentKeyChangeCalculatorTest {

    @InjectMocks
    private ContentKeyChangeCalculator contentKeyChangeCalculator;

    @Test
    public void testEmptyOldValuesNotEmptyNewValues() {
        List<ContentKey> oldValues = new ArrayList<ContentKey>();
        List<ContentKey> newValues = new ArrayList<ContentKey>();
        newValues.add(ContentKeyFactory.get("Product:test"));
        newValues.add(ContentKeyFactory.get("Product:test2"));

        String[] describedValues = contentKeyChangeCalculator.describeContentKeyListChange(oldValues, newValues);

        Assert.assertNotNull("Result should not be null", describedValues);
        Assert.assertTrue("Result array length should be 2", describedValues.length == 2);
        Assert.assertNull("There should be no description to the first element", describedValues[0]);
        Assert.assertNotNull("There should be description for the second element", describedValues[1]);
        Assert.assertTrue("The description should be about adding values", describedValues[1].trim().equals("Added: Product:test2, Product:test"));
    }

    @Test
    public void testNotEmptyOldValuesEmptyNewValues() {
        List<ContentKey> oldValues = new ArrayList<ContentKey>();
        List<ContentKey> newValues = new ArrayList<ContentKey>();
        oldValues.add(ContentKeyFactory.get("Product:test"));
        oldValues.add(ContentKeyFactory.get("Product:test2"));

        String[] describedValues = contentKeyChangeCalculator.describeContentKeyListChange(oldValues, newValues);

        Assert.assertNotNull("Result should not be null", describedValues);
        Assert.assertTrue("Result array length should be 2", describedValues.length == 2);
        Assert.assertNotNull("There should be description to the first element", describedValues[0]);
        Assert.assertNull("There should be no description to the second element", describedValues[1]);
        Assert.assertTrue("The description should be about removing values", describedValues[0].trim().equals("Deleted: Product:test2, Product:test"));
    }

    @Test
    public void testNotEmptyOldValueNotEmptyNewValue() {
        List<ContentKey> oldValues = new ArrayList<ContentKey>();
        List<ContentKey> newValues = new ArrayList<ContentKey>();
        oldValues.add(ContentKeyFactory.get("Product:test"));
        newValues.add(ContentKeyFactory.get("Product:test2"));

        String[] describedValues = contentKeyChangeCalculator.describeContentKeyListChange(oldValues, newValues);

        Assert.assertNotNull("Result should not be null", describedValues);
        Assert.assertTrue("Result array length should be 2", describedValues.length == 2);
        Assert.assertNotNull("There should be description to the first element", describedValues[0]);
        Assert.assertNotNull("There should be description to the second element", describedValues[1]);
        Assert.assertTrue("The description should be about removing values", describedValues[0].trim().equals("Deleted: Product:test"));
        Assert.assertTrue("The description should be about adding values", describedValues[1].trim().equals("Added: Product:test2"));
    }

    @Test
    public void testKeysExchanged() {
        List<ContentKey> oldValues = new ArrayList<ContentKey>();
        List<ContentKey> newValues = new ArrayList<ContentKey>();
        oldValues.add(ContentKeyFactory.get("Product:test"));
        oldValues.add(ContentKeyFactory.get("Product:test2"));

        newValues.add(ContentKeyFactory.get("Product:test2"));
        newValues.add(ContentKeyFactory.get("Product:test"));

        String[] describedValues = contentKeyChangeCalculator.describeContentKeyListChange(oldValues, newValues);

        Assert.assertNotNull("Result should not be null", describedValues);
        Assert.assertTrue("Result array length should be 2", describedValues.length == 2);
        Assert.assertNull("There should be no description to the first element", describedValues[0]);
        Assert.assertNotNull("There should be description to the second element", describedValues[1]);
        Assert.assertTrue("The description should be about exchanging values", describedValues[1].trim().equals("Exchanged: Product:test2<->Product:test;"));
    }

    @Test
    public void testKeysMoved() {
        List<ContentKey> oldValues = new ArrayList<ContentKey>();
        List<ContentKey> newValues = new ArrayList<ContentKey>();
        oldValues.add(ContentKeyFactory.get("Product:test1"));
        oldValues.add(ContentKeyFactory.get("Product:test2"));
        oldValues.add(ContentKeyFactory.get("Product:test3"));
        oldValues.add(ContentKeyFactory.get("Product:test4"));

        newValues.add(ContentKeyFactory.get("Product:test4"));
        newValues.add(ContentKeyFactory.get("Product:test1"));
        newValues.add(ContentKeyFactory.get("Product:test2"));
        newValues.add(ContentKeyFactory.get("Product:test3"));


        String[] describedValues = contentKeyChangeCalculator.describeContentKeyListChange(oldValues, newValues);

        Assert.assertNotNull("Result should not be null", describedValues);
        Assert.assertTrue("Result array length should be 2", describedValues.length == 2);
        Assert.assertNull("There should be no description to the first element", describedValues[0]);
        Assert.assertNotNull("There should be description to the second element", describedValues[1]);
        Assert.assertTrue("The description should be about moving values", describedValues[1].trim().equals("Moved: Product:test1 0 => 1; Product:test2 1 => 2; Product:test3 2 => 3; Product:test4 3 => 0;"));
    }

    @Test
    public void testKeysMovedAndDeleted() {
        List<ContentKey> oldValues = new ArrayList<ContentKey>();
        List<ContentKey> newValues = new ArrayList<ContentKey>();
        oldValues.add(ContentKeyFactory.get("Product:test1"));
        oldValues.add(ContentKeyFactory.get("Product:test2"));
        oldValues.add(ContentKeyFactory.get("Product:test3"));
        oldValues.add(ContentKeyFactory.get("Product:test4"));

        newValues.add(ContentKeyFactory.get("Product:test4"));
        newValues.add(ContentKeyFactory.get("Product:test1"));
        newValues.add(ContentKeyFactory.get("Product:test2"));


        String[] describedValues = contentKeyChangeCalculator.describeContentKeyListChange(oldValues, newValues);

        Assert.assertNotNull("Result should not be null", describedValues);
        Assert.assertTrue("Result array length should be 2", describedValues.length == 2);
        Assert.assertNotNull("There should be description to the first element", describedValues[0]);
        Assert.assertNotNull("There should be description to the second element", describedValues[1]);
        Assert.assertTrue("The description should be about removing values", describedValues[0].trim().equals("Deleted: Product:test3"));
        Assert.assertTrue("The description should be about moving values", describedValues[1].trim().equals("Moved: Product:test1 0 => 1; Product:test2 1 => 2; Product:test4 3 => 0;"));
    }

    @Test
    public void testKeysMovedAndAdded() {
        List<ContentKey> oldValues = new ArrayList<ContentKey>();
        List<ContentKey> newValues = new ArrayList<ContentKey>();

        oldValues.add(ContentKeyFactory.get("Product:test2"));
        oldValues.add(ContentKeyFactory.get("Product:test3"));
        oldValues.add(ContentKeyFactory.get("Product:test4"));

        newValues.add(ContentKeyFactory.get("Product:test4"));
        newValues.add(ContentKeyFactory.get("Product:test1"));
        newValues.add(ContentKeyFactory.get("Product:test2"));
        newValues.add(ContentKeyFactory.get("Product:test3"));

        String[] describedValues = contentKeyChangeCalculator.describeContentKeyListChange(oldValues, newValues);

        Assert.assertNotNull("Result should not be null", describedValues);
        Assert.assertTrue("Result array length should be 2", describedValues.length == 2);
        Assert.assertNull("There should be no description to the first element", describedValues[0]);
        Assert.assertNotNull("There should be description to the second element", describedValues[1]);
        Assert.assertTrue("The description should be about moving values", describedValues[1].contains("Moved: Product:test2 0 => 2; Product:test3 1 => 3; Product:test4 2 => 0;"));
        Assert.assertTrue("The description should be about moving values", describedValues[1].contains("Added: Product:test1"));
    }

}
