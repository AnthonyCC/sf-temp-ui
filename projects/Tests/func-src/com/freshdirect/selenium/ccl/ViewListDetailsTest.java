package com.freshdirect.selenium.ccl;

import java.util.List;

import com.freshdirect.selenium.FDSeleniumTestSupport;
import com.freshdirect.selenium.FDSeleniumTestSupport.ProductCategory;
import com.freshdirect.selenium.FDSeleniumTestSupport.ProductInfo;

public class ViewListDetailsTest extends FDSeleniumTestSupport {

	public void setUp() throws Exception {
		super.setUp();
		doLogIn();
	}
	
	/**
	 * TEST-CCL-M1-7.1
	 * Attempting to View the List Items
	 * 
	 * ref: https://dev.euedge.com/projects/freshdirect/wiki/Projects/CCL/Testing/TestPlan#test-ccl-m1-7.1
	 */
	public void testListDetailsParser() {
		// create a single empty list
		CCList l = ensureSingleListWithName("ListDetailsTest");
		
		// Save a Filet Mignon
		openFiletMignon(); // Open Filet Mignon
		String[] salesUnits = selenium.getSelectOptions(XP_SALES_UNIT);
		selenium.select(XP_SALES_UNIT, salesUnits[1]);
		String[] packaging = selenium.getSelectOptions(XP_MT_BF_PAK); // Packaging
		selenium.select(XP_MT_BF_PAK, packaging[1]);
		selenium.click(XP_SAVE_TO_SHOPPING_LIST);
		/*
		waitForElementPresent(XP_ITEMS_ADDED, NORMAL_LOAD_TIMEOUT);
		String itemsAdded = selenium.getText(XP_ITEMS_ADDED);
		String assertedItemsAdded = ITEMS_ADDED_PREFIX + l.name + ITEMS_ADDED_SUFFIX; 
		assertEquals(assertedItemsAdded, itemsAdded);	
		String listName = selenium.getText(XP_ITEMS_ADDED_LISTNAME); // Checks that the listname is inside a bold tag
		assertEquals(l.name, listName);
		selenium.click(XP_KEEP_SHOPPING);
		*/
		assertShoppingList(l.name);
		
		l.openList();
		List listContents = parseListContents();
		assertEquals(1, listContents.size());
		ProductCategory cat = (ProductCategory) listContents.get(0);
		assertEquals("Meat", cat.name);
		assertEquals(1, cat.size());
		ProductInfo mignon = (ProductInfo) cat.get(0);
		assertEquals("Filet Mignon", mignon.name);
		assertEquals("1", mignon.quantity);
		
		
		// Save a Gouda
		
		// Parse list details and check contents
	}
	
}
