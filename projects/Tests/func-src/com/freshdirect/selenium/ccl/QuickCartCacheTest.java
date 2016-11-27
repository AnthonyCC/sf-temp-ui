package com.freshdirect.selenium.ccl;

import com.freshdirect.selenium.FDSeleniumTestSupport;


public class QuickCartCacheTest extends FDSeleniumTestSupport {

	private final static int MAXTIMES = 5;  // number of times to go through a full item-by-item delete cycle 
	private final static int MAXITEMS = 17; // how many items to put on the list
	
	public void setUp() throws Exception {
		super.setUp();
		doLogIn();
	}
	
	public void testNavigateAway()  {
		CCList zombi = ensureSingleListWithName("zombi");
		String[] salesUnits;
		String[] packaging;
		String title;
	
		openFiletMignon(); // Open Filet Mignon
		
		salesUnits = selenium.getSelectOptions(XP_SALES_UNIT);
		selenium.select(XP_SALES_UNIT, salesUnits[1]);
		packaging = selenium.getSelectOptions(XP_MT_BF_PAK); // Packaging
		selenium.select(XP_MT_BF_PAK, packaging[1]);
		selenium.click(XP_SAVE_TO_SHOPPING_LIST);
		waitForElementPresent(XP_ITEMS_ADDED, NORMAL_LOAD_TIMEOUT);
		
		/*
		String itemsAdded = selenium.getText(XP_ITEMS_ADDED);
		String assertedItemsAdded = ITEMS_ADDED_PREFIX + zombi.name + ITEMS_ADDED_SUFFIX; 
		assertEquals(assertedItemsAdded, itemsAdded);	
		String listName = selenium.getText(XP_ITEMS_ADDED_LISTNAME); // Checks that the listname is inside a bold tag
		assertEquals(zombi.name, listName);
		*/
		String listName = selenium.getText("//span[@id='CCL_listName']");
		assertEquals(zombi.name, listName);
		
		selenium.click(XP_KEEP_SHOPPING);
		
		// navigate away so the quickcart cache needs reloading
		waitForElementPresent(XP_CART_CLICK,NORMAL_LOAD_TIMEOUT);
		selenium.click(XP_CART_CLICK);
		
		openFiletMignon(); // again
		
		salesUnits = selenium.getSelectOptions(XP_SALES_UNIT);
		selenium.select(XP_SALES_UNIT, salesUnits[salesUnits.length - 2]);
		packaging = selenium.getSelectOptions(XP_MT_BF_PAK);
		selenium.select(XP_MT_BF_PAK, packaging[packaging.length - 2]);
		selenium.click(XP_SAVE_TO_SHOPPING_LIST);
		waitForElementPresent(XP_ITEMS_ADDED, NORMAL_LOAD_TIMEOUT);
		listName = selenium.getText(XP_ITEMS_ADDED_LISTNAME); // Checks that the listname is inside a bold tag
		assertEquals(zombi.name, listName);
		selenium.click(XP_VISIT_THIS_LIST);
		
		selenium.waitForPageToLoad(NORMAL_LOAD_TIMEOUT);
		waitForElementPresent(XP_LIST_DETAIL_NAME,NORMAL_LOAD_TIMEOUT);
		
		title = selenium.getText(XP_LIST_DETAIL_NAME);
		assertTrue(title.indexOf("(2 items)") != -1);


		selenium.click(XP_REMOVE_LINK);
		selenium.waitForPageToLoad(NORMAL_LOAD_TIMEOUT);
		waitForElementPresent(XP_LIST_DETAIL_NAME,NORMAL_LOAD_TIMEOUT);
		title = selenium.getText(XP_LIST_DETAIL_NAME);
		
		//System.out.println("TITLE = " + title);
		assertTrue(title.indexOf("(1 item)") != -1);

		selenium.click(XP_REMOVE_LINK);
		
		selenium.waitForPageToLoad(NORMAL_LOAD_TIMEOUT);
		waitForElementPresent(XP_LIST_DETAIL_NAME,NORMAL_LOAD_TIMEOUT);
		title = selenium.getText(XP_LIST_DETAIL_NAME);

		//System.out.println("TITLE = " + title);
		assertTrue(title.indexOf("(0 items") != -1);
		
	}
	
	public void testRepeatedRemoves() {
		ensureSingleListWithName("zombi");
		
		String[] salesUnits;
		String[] packaging;
		String title;
		
		for(int i=0; i< MAXTIMES; ++i) {
			openFiletMignon();
			
			for(int j = 0; j < MAXITEMS; ++j) {
				salesUnits = selenium.getSelectOptions(XP_SALES_UNIT);
				selenium.select(XP_SALES_UNIT, salesUnits[1]);
				packaging = selenium.getSelectOptions(XP_MT_BF_PAK); // Packaging
				selenium.select(XP_MT_BF_PAK, packaging[1]);
				selenium.click(XP_SAVE_TO_SHOPPING_LIST);
				waitForElementPresent(XP_ITEMS_ADDED, NORMAL_LOAD_TIMEOUT);
				selenium.click(j == MAXITEMS -1 ? XP_VISIT_THIS_LIST : XP_KEEP_SHOPPING);
				if (j == MAXITEMS - 1) selenium.waitForPageToLoad(NORMAL_LOAD_TIMEOUT);
			}
			
			for(int j = MAXITEMS -1; j>=0; --j) {
				waitForElementPresent(XP_REMOVE_LINK, NORMAL_LOAD_TIMEOUT);
				selenium.click(XP_REMOVE_LINK);
				selenium.waitForPageToLoad(NORMAL_LOAD_TIMEOUT);
				waitForElementPresent(XP_LIST_DETAIL_NAME,NORMAL_LOAD_TIMEOUT);
				title = selenium.getText(XP_LIST_DETAIL_NAME);
				assertTrue(title.indexOf("(" + j + " item" + (j == 1 ? "" : "s") + ")") != -1);
			}
			
		}
	}
	
}
