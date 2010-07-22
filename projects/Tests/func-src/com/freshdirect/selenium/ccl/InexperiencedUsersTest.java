package com.freshdirect.selenium.ccl;

import java.util.List;

import com.freshdirect.selenium.FDSeleniumTestSupport;

public class InexperiencedUsersTest extends FDSeleniumTestSupport {

	public void setUp() throws Exception {
		super.setUp();
		doLogIn();
	}
	
	private static final String XP_NEW_ICON = "//img[@src='/media_stat/ccl/lists_new.gif']";
	private static final String XP_MORE_ABOUT_LISTS = "//a[@href='/unsupported.jsp' and text()='Learn more']";
	private static final String XP_KEEP_SHOPPING = "//a[@id=\"keepShopping\"]";
	
	public void testNotNewSigns() throws Exception {
		// first count the lists shown on the quickshop landing page
		List lists = getCCLOnAllListsPage();
		String unusedList = findUnusedListName("unusedList", lists); // create a list to have at least 2
		createListOnAllListsPage(unusedList);
		lists = getCCLOnAllListsPage();
		assertTrue(2 <= lists.size());

		// Check no new icon on the QuickShop Landing Page
		selenium.open("/quickshop/index.jsp");
		selenium.waitForPageToLoad(NORMAL_LOAD_TIMEOUT);		
		assertFalse(selenium.isElementPresent(XP_NEW_ICON));
		
		// Check new icon + More about lists on Perishable product page
		openFiletMignon();
		checkNotNewAndMoreAboutLists();

		// Check new icon + More about lists on Grocery product page
		openFirstCannedGoods();
		checkNotNewAndMoreAboutLists();
		
		// Recipe test
		openFirstBeefRecipe();
		// no longer needed - checkNoCCLOnRecipePage();
		checkNotNewAndMoreAboutLists();

		/*
		// Party platter test
		openFirstPartyPackage();
		checkNotNewAndMoreAboutLists();
		*/	
	}

	public void testNotNewSignsAfterSavingFiletMignon() throws Exception {
		// first count the lists shown on the quickshop landing page
		CCList unused = ensureSingleListWithName("unusedList");
		List lists = getCCLOnAllListsPage();
		assertEquals(1, lists.size());
		assertEquals(unused.name,((CCList)lists.get(0)).name);
				
		// Save a filet mignon
		openFiletMignon();
		String[] salesUnits = selenium.getSelectOptions(XP_SALES_UNIT);
		selenium.select(XP_SALES_UNIT, salesUnits[1]);
		String[] packaging = selenium.getSelectOptions(XP_MT_BF_PAK); // Packaging
		selenium.select(XP_MT_BF_PAK, packaging[1]);
		selenium.click(XP_SAVE_TO_SHOPPING_LIST);

		/*
		waitForElementPresent(XP_ITEMS_ADDED, NORMAL_LOAD_TIMEOUT);

		String itemsAdded = selenium.getText(XP_ITEMS_ADDED);
		String assertedItemsAdded = ITEMS_ADDED_PREFIX + unused.name + ITEMS_ADDED_SUFFIX; 
		//assertEquals(assertedItemsAdded, itemsAdded);	
		assertEquals(unused.name, itemsAdded);
		String listName = selenium.getText(XP_ITEMS_ADDED_LISTNAME); // Checks that the listname is inside a bold tag
		assertEquals(unused.name, listName);
		// match list names
		assertEquals(unused.name, selenium.getText("//span[@id='CCL_listName']"));

		selenium.click(XP_KEEP_SHOPPING);
		*/
		assertShoppingList(unused.name);
		
		// Check no new icon on the QuickShop Landing Page
		selenium.open("/quickshop/index.jsp");
		selenium.waitForPageToLoad(NORMAL_LOAD_TIMEOUT);		
		assertFalse(selenium.isElementPresent(XP_NEW_ICON));

		// Check no new icon + More about lists on Perishable product page
		openFiletMignon();
		checkNotNewAndMoreAboutLists();

		// Check no new icon + More about lists on Grocery product page
		openFirstCannedGoods();
		checkNotNewAndMoreAboutLists();
		
		// Recipe test
		openFirstBeefRecipe();
		// no longer needed - checkNoCCLOnRecipePage();
		checkNotNewAndMoreAboutLists();
		
		/*
		// Party platter test
		openFirstPartyPackage();
		checkNotNewAndMoreAboutLists();
		*/	
	}
	
	
	public void testNewSigns() throws Exception {
		// first count the lists shown on the quickshop landing page
		CCList unused = ensureSingleListWithName("unusedList");
		List lists = getCCLOnAllListsPage();
		assertEquals(1, lists.size());
		assertEquals(unused.name,((CCList)lists.get(0)).name);
		
		// Check new icon on the QuickShop Landing Page
		selenium.open("/quickshop/index.jsp");
		selenium.waitForPageToLoad(NORMAL_LOAD_TIMEOUT);		
		assertTrue(selenium.isElementPresent(XP_NEW_ICON));
		
		// Check new icon + More about lists on Perishable product page
		openFiletMignon();
	//	checkNewAndMoreAboutLists();

		// Check new icon + More about lists on Grocery product page
		openFirstCannedGoods();
		checkNewAndMoreAboutLists();
		
		// Recipe test
		openFirstBeefRecipe();
		checkNewAndMoreAboutLists();

		/*
		// Party platter test
		openFirstPartyPackage();
		checkNewAndMoreAboutLists();
		*/
	}
	
	private void checkNewAndMoreAboutLists() {
		assertTrue(selenium.isElementPresent(XP_SAVE_TO_SHOPPING_LIST));
		assertTrue(selenium.isElementPresent(XP_NEW_ICON));
		assertTrue(selenium.isElementPresent(XP_MORE_ABOUT_LISTS));
		selenium.click(XP_MORE_ABOUT_LISTS);
		waitForElementPresent(XP_KEEP_SHOPPING, NORMAL_LOAD_TIMEOUT);
		selenium.click(XP_KEEP_SHOPPING);				
	}

	private void checkNotNewAndMoreAboutLists() {
		assertTrue(selenium.isElementPresent(XP_SAVE_TO_SHOPPING_LIST));
		assertFalse(selenium.isElementPresent(XP_NEW_ICON));
		assertFalse(selenium.isElementPresent(XP_MORE_ABOUT_LISTS));
	}
	
	// DON'T USE IT! 
	private void checkNoCCLOnRecipePage() {
		assertFalse(selenium.isElementPresent(XP_SAVE_TO_SHOPPING_LIST));
		assertFalse(selenium.isElementPresent(XP_NEW_ICON));
		assertFalse(selenium.isElementPresent(XP_MORE_ABOUT_LISTS));
	}
}
