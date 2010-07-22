package com.freshdirect.selenium.ccl;

import java.util.List;

import com.freshdirect.selenium.FDSeleniumTestSupport;

public class CreateDeleteListTest extends FDSeleniumTestSupport {
	
	public void setUp() throws Exception {
		super.setUp();
		doLogIn();
	}

	public void testCreateList() throws Exception {
		List lists = getCCLOnAllListsPage();
		
		//String unusedName = findUnusedListName("createListTest", lists);
		String unusedName = findUnusedListName("Test", lists);
		createListOnQuickshopPage(unusedName);
		
		List shownOnQSPage = getCCLOnQuickShopPage();
		lists = getCCLOnAllListsPage();
		//assertEquals(unusedName, ((CCList)shownOnQSPage.get(0)).name);
		assertEquals(unusedName, "Test"+BAD_CHARACTERS);
		assertEquals(unusedName, ((CCList)lists.get(0)).name);
		
		// Now try to create a list with an already existing name
		selenium.open("/quickshop/index.jsp");
		assertTrue(createListOnCurrentPage(unusedName, true, false)); // This is true if an error occured
		assertEquals(CCL_LIST_EXISTS, selenium.getText(getPanelXP(XP_CCL_ERROR_ELEMENT_TEMPLATE, "CCL_createPanel")));
		selenium.click(getPanelXP(XP_CCL_CLOSE_TEMPLATE, "CCL_createPanel"));
		pause(5000);
		
		// Now try to create a list with an empty name
		selenium.open("/quickshop/index.jsp");
		assertTrue(createListOnCurrentPage("            ", true, false)); // This is true if an error occured
		assertEquals(CCL_LIST_EMPTY_NAME, selenium.getText(getPanelXP(XP_CCL_ERROR_ELEMENT_TEMPLATE, "CCL_createPanel")));
		selenium.click(getPanelXP(XP_CCL_CLOSE_TEMPLATE, "CCL_createPanel"));
		pause(5000);
		
		// Now try to create a list with a zero length name
		selenium.open("/quickshop/index.jsp");
		assertTrue(createListOnCurrentPage("", true, false)); // This is true if an error occured
		assertEquals(CCL_LIST_EMPTY_NAME, selenium.getText(getPanelXP(XP_CCL_ERROR_ELEMENT_TEMPLATE, "CCL_createPanel")));
		selenium.click(getPanelXP(XP_CCL_CLOSE_TEMPLATE, "CCL_createPanel"));
		pause(5000);
		
		// Now create a list on the all lists page
		selenium.open("/quickshop/all_lists.jsp");
		assertFalse(createListOnCurrentPage(findUnusedListName("allListsList", lists), false, false)); 
		pause(5000);
	}	
	
	public void testCreateListWithEnter() throws Exception {
		List lists = getCCLOnAllListsPage();
		
		//String unusedName = findUnusedListName("createListTest", lists);
		String unusedName = findUnusedListName("enterTest", lists);

		
		createListOnQuickshopPage(unusedName);
		
		List shownOnQSPage = getCCLOnQuickShopPage();
		lists = getCCLOnAllListsPage();
		//assertEquals(unusedName, ((CCList)shownOnQSPage.get(0)).name);
		assertEquals(unusedName, "enterTest"+BAD_CHARACTERS);
		assertEquals(unusedName, ((CCList)lists.get(0)).name);
		
		// Now try to create a list with an already existing name
		selenium.open("/quickshop/index.jsp");
		assertTrue(createListOnCurrentPage(unusedName, true, true)); // This is true if an error occured
		assertEquals(CCL_LIST_EXISTS, selenium.getText(getPanelXP(XP_CCL_ERROR_ELEMENT_TEMPLATE, "CCL_createPanel")));
		selenium.click(getPanelXP(XP_CCL_CLOSE_TEMPLATE, "CCL_createPanel"));
		
		// Now try to create a list with an empty name
		selenium.open("/quickshop/index.jsp");
		assertTrue(createListOnCurrentPage("            ", true, true)); // This is true if an error occured
		assertEquals(CCL_LIST_EMPTY_NAME, selenium.getText(getPanelXP(XP_CCL_ERROR_ELEMENT_TEMPLATE, "CCL_createPanel")));
		selenium.click(getPanelXP(XP_CCL_CLOSE_TEMPLATE, "CCL_createPanel"));

		// Now try to create a list with a zero length name
		selenium.open("/quickshop/index.jsp");
		assertTrue(createListOnCurrentPage("", true, true)); // This is true if an error occured
		assertEquals(CCL_LIST_EMPTY_NAME, selenium.getText(getPanelXP(XP_CCL_ERROR_ELEMENT_TEMPLATE, "CCL_createPanel")));
		selenium.click(getPanelXP(XP_CCL_CLOSE_TEMPLATE, "CCL_createPanel"));

		// Now create a list on the all lists page
		selenium.open("/quickshop/all_lists.jsp");
		assertFalse(createListOnCurrentPage(findUnusedListName("allListsList", lists), false, true)); 
	}	

	
	public void testDeleteList() throws Exception {
		// Delete all lists, except the last one
		List lists = getCCLOnAllListsPage();
		for (int i = 0; i < lists.size() - 1; i++) {
			CCList l = (CCList) lists.get(i);
			l.deleteList();
		}
		
		CCList lastList = (CCList) lists.get(lists.size()-1);
		lastList.openList();
		assertFalse(selenium.isElementPresent(XP_DELETE_LIST));
	}
}
