package com.freshdirect.selenium.ccl;


import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import com.freshdirect.selenium.FDSeleniumTestSupport;


public class SaveItemToListTest extends FDSeleniumTestSupport {
	public void setUp() throws Exception {
		super.setUp();
		doLogIn();
	}



	/**
	 * TEST-CCL-M1-6.1
	 * Saving a properly configured item to an existing list
	 * 
	 * ref: https://dev.euedge.com/projects/freshdirect/wiki/Projects/CCL/Testing/TestPlan#test-ccl-m1-6.3
	 */
	public void testSavePerishableItemToList() {
		{
			CCList l = ensureSingleListWithName("saveItemTest"); // Make sure that we have a single list
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

			String listName = selenium.getText("//span[@id='CCL_listName']");
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
		}

		// create another list 
		{
			List lists = getCCLOnAllListsPage();
			createListOnAllListsPage(findUnusedListName("alfaSaveItemTest", lists));
			lists = getCCLOnAllListsPage();
			
			openFiletMignon();
			String[] salesUnits = selenium.getSelectOptions(XP_SALES_UNIT);
			selenium.select(XP_SALES_UNIT, salesUnits[1]);
			String[] packaging = selenium.getSelectOptions(XP_MT_BF_PAK); // Packaging
			selenium.select(XP_MT_BF_PAK, packaging[1]);
			selenium.click(XP_SAVE_TO_SHOPPING_LIST);
			waitForElementPresent(XP_LIST_SELECT, NORMAL_LOAD_TIMEOUT);
			String[] options = selenium.getSelectOptions(XP_LIST_SELECT);
			String[] listNames = new String[options.length - 1];
			for (int i = 0; i < options.length - 1; i++) {
				listNames[i] = getListNameFromPattern(options[i]);
			}
			TreeSet sorted = new TreeSet(new CompareByName());
			sorted.addAll(lists);
			
			String[] assertedListNames = new String[sorted.size()];
			int i = 0;
			for (Iterator it = sorted.iterator(); it.hasNext();) {
				CCList l = (CCList) it.next();
				assertedListNames[i++] = l.name;
			}
			assertEquals(assertedListNames, listNames);
			
			// Select the last list:
			selenium.select(XP_LIST_SELECT, options[listNames.length-1]); // Need to select using the actual option string
			
			// create list
			selenium.click(XP_CONTINUE);
			
			/*
			waitForElementPresent(XP_ITEMS_ADDED, NORMAL_LOAD_TIMEOUT);

			String itemsAdded = selenium.getText(XP_ITEMS_ADDED);
			String assertedItemsAdded = ITEMS_ADDED_PREFIX + listNames[listNames.length-1] + ITEMS_ADDED_SUFFIX; 
			assertEquals(assertedItemsAdded, itemsAdded);	
			String listName = selenium.getText(XP_ITEMS_ADDED_LISTNAME); // Checks that the listname is inside a bold tag
			assertEquals(listNames[listNames.length-1], listName);

			String listName = selenium.getText("//span[@id='CCL_listName']");
			assertEquals(listNames[listNames.length-1], listName);

			selenium.click(XP_KEEP_SHOPPING);
			*/
			assertShoppingList(listNames[listNames.length-1]);
			
		}		
	}



	/**
	 * TEST-CCL-M1-6.2
	 * Saving a properly configured item to a new list
	 * 
	 * ref: https://dev.euedge.com/projects/freshdirect/wiki/Projects/CCL/Testing/TestPlan#test-ccl-m1-6.2
	 */
	public void testSavePerishableItemToListByCreatingNewList() {
		// create another list 
		{
			List lists = getCCLOnAllListsPage();
			if (lists.size() == 1) {
				// Make sure to have at least 2 lists, so the dropdown will be shown
				createListOnAllListsPage(findUnusedListName("placeholderSaveItemTest", lists));
				lists = getCCLOnAllListsPage();
			}
			
			openFiletMignon();
			String[] salesUnits = selenium.getSelectOptions(XP_SALES_UNIT);
			selenium.select(XP_SALES_UNIT, salesUnits[1]);
			String[] packaging = selenium.getSelectOptions(XP_MT_BF_PAK); // Packaging
			selenium.select(XP_MT_BF_PAK, packaging[1]);
			selenium.click(XP_SAVE_TO_SHOPPING_LIST);
			waitForElementPresent(XP_LIST_SELECT, NORMAL_LOAD_TIMEOUT);
			String[] options = selenium.getSelectOptions(XP_LIST_SELECT);
			
			// Select the CREATE NEW LIST:
			assertEquals("CREATE NEW LIST", options[options.length-1]);
			selenium.select(XP_LIST_SELECT, options[options.length-1]); 
			waitForElementPresent(XP_CCL_PANEL_TEXT_INPUT, SMALL_LOAD_TIMEOUT);
			
			// Select the first list, check that the CREATE NEW LIST disappears
			selenium.select(XP_LIST_SELECT, options[0]); 
			waitForElementNotPresent(XP_CCL_PANEL_TEXT_INPUT, NORMAL_LOAD_TIMEOUT);
			
			// Select the CREATE NEW LIST again:
			assertEquals("CREATE NEW LIST", options[options.length-1]);
			selenium.select(XP_LIST_SELECT, options[options.length-1]); 
			waitForElementPresent(XP_CCL_PANEL_TEXT_INPUT, SMALL_LOAD_TIMEOUT);
			
			// Empty string test
			selenium.type(XP_CCL_PANEL_TEXT_INPUT, "         ");
			selenium.click(XP_CONTINUE);
			waitForElementPresent(getPanelXP(XP_CCL_ERROR_ELEMENT_TEMPLATE, "CCL_savePanel"), SMALL_LOAD_TIMEOUT);
			assertEquals(ENTER_NAME, selenium.getText(getPanelXP(XP_CCL_ERROR_ELEMENT_TEMPLATE, "CCL_savePanel")));

			// Zero length string test
			selenium.type(XP_CCL_PANEL_TEXT_INPUT, "");
			selenium.click(XP_CONTINUE);
			waitForElementPresent(getPanelXP(XP_CCL_ERROR_ELEMENT_TEMPLATE, "CCL_savePanel"), SMALL_LOAD_TIMEOUT);
			assertEquals(ENTER_NAME, selenium.getText(getPanelXP(XP_CCL_ERROR_ELEMENT_TEMPLATE, "CCL_savePanel")));

			// Existing list test
			CCList existing = (CCList) lists.get(0);
			selenium.type(XP_CCL_PANEL_TEXT_INPUT, existing.name);
			selenium.click(XP_CONTINUE);
			waitForElementPresent(getPanelXP(XP_CCL_ERROR_ELEMENT_TEMPLATE, "CCL_savePanel"), SMALL_LOAD_TIMEOUT);
			assertEquals(CCL_LIST_EXISTS, selenium.getText(getPanelXP(XP_CCL_ERROR_ELEMENT_TEMPLATE, "CCL_savePanel")));
			
			// Create new list
			String reqListName = findUnusedListName("newSaveList", lists);
			selenium.type(XP_CCL_PANEL_TEXT_INPUT, reqListName);
			selenium.click(XP_CONTINUE);
			/*
			waitForElementPresent(XP_ITEMS_ADDED, NORMAL_LOAD_TIMEOUT);

			String itemsAdded = selenium.getText(XP_ITEMS_ADDED);
			String assertedItemsAdded = ITEMS_ADDED_PREFIX + reqListName + ITEMS_ADDED_SUFFIX; 
			assertEquals(assertedItemsAdded, itemsAdded);	
			String listName = selenium.getText(XP_ITEMS_ADDED_LISTNAME); // Checks that the listname is inside a bold tag
			assertEquals(reqListName, listName);

			String listName = selenium.getText("//span[@id='CCL_listName']");
			assertEquals(reqListName, listName);

			selenium.click(XP_KEEP_SHOPPING);			
			*/
			assertShoppingList(reqListName);
		}
	}



	/**
	 * TEST-CCL-M1-6.3
	 * Trying the save an item which is not well configured
	 * 
	 * ref: https://dev.euedge.com/projects/freshdirect/wiki/Projects/CCL/Testing/TestPlan#test-ccl-m1-6.3
	 */
	public void testSaveUnconfiguredPerishableProduct() {
		openFiletMignon();
		selenium.click(XP_SAVE_TO_SHOPPING_LIST);
		waitForElementPresent("//div[@id='CCL_savePanel']", SMALL_LOAD_TIMEOUT); // The popup should show itself first
		selenium.waitForPageToLoad(NORMAL_LOAD_TIMEOUT);
		assertTrue(selenium.isElementPresent("//span[@class='text11rbold' and text()='Please select Thickness']"));
		assertTrue(selenium.isElementPresent("//span[@class='text11rbold' and text()='Please select Packaging']"));
	
		String[] salesUnits = selenium.getSelectOptions(XP_SALES_UNIT);
		selenium.select(XP_SALES_UNIT, salesUnits[1]);
		selenium.click(XP_SAVE_TO_SHOPPING_LIST);
		selenium.waitForPageToLoad(NORMAL_LOAD_TIMEOUT);
		assertFalse(selenium.isElementPresent("//span[@class='text11rbold' and text()='Please select Thickness']"));
		assertTrue(selenium.isElementPresent("//span[@class='text11rbold' and text()='Please select Packaging']"));
	
		String[] packaging = selenium.getSelectOptions(XP_MT_BF_PAK); // Packaging
		selenium.select(XP_SALES_UNIT, salesUnits[0]);
		selenium.select(XP_MT_BF_PAK, packaging[1]);
		selenium.click(XP_SAVE_TO_SHOPPING_LIST);
		selenium.waitForPageToLoad(NORMAL_LOAD_TIMEOUT);
		assertTrue(selenium.isElementPresent("//span[@class='text11rbold' and text()='Please select Thickness']"));
		assertFalse(selenium.isElementPresent("//span[@class='text11rbold' and text()='Please select Packaging']"));	
	}
}
