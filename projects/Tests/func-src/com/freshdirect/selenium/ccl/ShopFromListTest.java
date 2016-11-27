package com.freshdirect.selenium.ccl;

import java.util.List;

import com.freshdirect.selenium.FDSeleniumTestSupport;

public class ShopFromListTest extends FDSeleniumTestSupport {
	public String targetListName;
	
	public void setUp() throws Exception {
		super.setUp();
		doLogIn();
		
		// establish preconditions
		targetListName = "SFLTest " + Math.round(Math.random()*255);

		// create list
		createListOnAllListsPage(targetListName);
		
	}

	


	private CCList getTargetList() {
		List lists = getCCLOnAllListsPage();
		for (int i=0; i<lists.size(); i++) {
			CCList l = (CCList) lists.get(i);
			if (targetListName.compareToIgnoreCase(l.name) == 0) {
				return l;
			}
		}
		return null;
	}
	

	
	/**
	 * Helper method to configure Filet Mignon product and to add to shopping list
	 * @param shoppingListName target list to add
	 */
	private void configureFiletMignon(String shoppingListName) {
		openFiletMignon(); // Open Filet Mignon
		
		// configure mignon
		String[] salesUnits = selenium.getSelectOptions(XP_SALES_UNIT);
		selenium.select(XP_SALES_UNIT, salesUnits[1]);
		String[] packaging = selenium.getSelectOptions(XP_MT_BF_PAK); // Packaging
		selenium.select(XP_MT_BF_PAK, packaging[1]);
		
		// == save to shopping list
		
		// click to 'save' on mignon page
		selenium.click(XP_SAVE_TO_SHOPPING_LIST);
		waitForElementPresent("//select[@id='dropdown']", SMALL_LOAD_TIMEOUT);

		// select my list and save
		selenium.select("//select[@id='dropdown']", "value=" + shoppingListName);
		selenium.click("id=contLink");

		assertShoppingList(shoppingListName);
	}
	

	
	/**
	 * Helper method to configure Seviroli Spinach Mozarella Ravioli product and to add to shopping list
	 * @param shoppingListName target list to add
	 */
	private void configureSeviroliSpinachMozarellaRavioli(String shoppingListName) {
		openSeviroliSpinachMozarellaRavioli(); // Open ravioli

		// == save to shopping list

		// click to 'save' on mignon page
		selenium.click(XP_SAVE_TO_SHOPPING_LIST);
		waitForElementPresent("//select[@id='dropdown']", SMALL_LOAD_TIMEOUT);

		// select my list and save
		selenium.select("//select[@id='dropdown']", "value=" + shoppingListName);
		selenium.click("id=contLink");

		assertShoppingList(shoppingListName);
	}
	
	
	
	
	public void tearDown() throws Exception {
		// getTargetList().deleteList();
		
		super.tearDown();
	}





	/**
	 * TEST-CCL-M1-8.1
	 * Adding multiple items to cart from list
	 * 
	 * https://dev.euedge.com/projects/freshdirect/wiki/Projects/CCL/Testing/TestPlan#test-ccl-m1-8.1
	 */
	public void testAddMultipleItemsToCart() {
		// 1. ADD ITEMS TO LIST
		//

		// [1] add mignon to list
		configureFiletMignon(targetListName);

		// [2] add ravioli to list
		configureSeviroliSpinachMozarellaRavioli(targetListName);
		
		// go to QuickShop page and collect shopping lists
		List lists = getCCLOnAllListsPage();
		
		assertTrue(lists.size() > 0);



		
		// 2. ADD ITEMS IN LIST TO CART
		//

		// get my list
		CCList l = getTargetList(); assertNotNull(l);

		// go to shopping list page
		l.openList();

		// click to 'add items'
		selenium.click("xpath=(//input[@name='addMultipleToCart'])[1]");
		selenium.waitForPageToLoad(NORMAL_LOAD_TIMEOUT);
		

		// 3. ENSURE ITEMS ADDED TO CART
		selenium.open("/view_cart.jsp");
		selenium.waitForPageToLoad(NORMAL_LOAD_TIMEOUT);		

		int n = 0; // number of found items
		int k = 1;
		while(n < 2 && selenium.isElementPresent("xpath=(//a[contains(@href, '/product_modify.jsp')])[" + k + "]") ) {
			String txt = selenium.getText("xpath=(//a[contains(@href, '/product_modify.jsp')])[" + k + "]");
			if ("Seviroli Spinach & Mozzarella Ravioli".compareToIgnoreCase(txt) == 0) {
				n++;
			}
			if ("Filet Mignon".compareToIgnoreCase(txt) == 0) {
				n++;
			}
			k++;
		}
		
		assertTrue(n >= 2);
	}
}
