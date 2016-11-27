package com.freshdirect.selenium.ccl;

import java.util.List;

import com.freshdirect.selenium.FDSeleniumTestSupport;

public class RenameListTest extends FDSeleniumTestSupport {

	public void setUp() throws Exception {
		super.setUp();
		doLogIn();
		deleteListsExceptLast();
	}

	public void testRenameList() {
		List lists = getCCLOnAllListsPage();
		
		// Use this list for NameExists tests
		String existingList = findUnusedListName("existingList", lists);
		createListOnAllListsPage(existingList);

		// This list will be renamed
		String unusedName = findUnusedListName("renameTest", lists);
		createListOnAllListsPage(unusedName);
		
		lists = getCCLOnAllListsPage();
		CCList l = (CCList) lists.get(0);
		assertEquals(unusedName, l.name);
		String newName = findUnusedListName("newRenameTest", lists);
		l.renameList(newName, false, false);
		lists = getCCLOnAllListsPage();
		CCList l2 = (CCList) lists.get(0);
		assertEquals(newName, l2.name);
		assertEquals(l.id, l2.id);
		assertEquals(l.name, l2.name);
		
		assertTrue(l2.renameList(existingList, true, false));
		assertEquals(CCL_LIST_EXISTS, selenium.getText(getPanelXP(XP_CCL_ERROR_ELEMENT_TEMPLATE, "CCL_renamePanel")));
		
		assertTrue(l2.renameList("", true, false));
		assertEquals(CCL_RENAME_EMPTY, selenium.getText(getPanelXP(XP_CCL_ERROR_ELEMENT_TEMPLATE, "CCL_renamePanel")));

		assertTrue(l2.renameList("         	", true, false));
		assertEquals(CCL_RENAME_EMPTY, selenium.getText(getPanelXP(XP_CCL_ERROR_ELEMENT_TEMPLATE, "CCL_renamePanel")));
	}

	public void testRenameListWithEnter() {
		List lists = getCCLOnAllListsPage();
		
		// Use this list for NameExists tests
		String existingList = findUnusedListName("existingList", lists);
		createListOnAllListsPage(existingList);

		// This list will be renamed
		String unusedName = findUnusedListName("renameTest", lists);
		createListOnAllListsPage(unusedName);
		
		lists = getCCLOnAllListsPage();
		CCList l = (CCList) lists.get(0);
		assertEquals(unusedName, l.name);
		String newName = findUnusedListName("newRenameTest", lists);
		l.renameList(newName, false, true); // Use enter instead of clicking on Continue
		lists = getCCLOnAllListsPage();
		CCList l2 = (CCList) lists.get(0);
		assertEquals(newName, l2.name);
		assertEquals(l.id, l2.id);
		assertEquals(l.name, l2.name);
		
		assertTrue(l2.renameList(existingList, true, true));
		assertEquals(CCL_LIST_EXISTS, selenium.getText(getPanelXP(XP_CCL_ERROR_ELEMENT_TEMPLATE, "CCL_renamePanel")));
		
		assertTrue(l2.renameList("", true, true));
		assertEquals(CCL_RENAME_EMPTY, selenium.getText(getPanelXP(XP_CCL_ERROR_ELEMENT_TEMPLATE, "CCL_renamePanel")));

		assertTrue(l2.renameList("         	", true, true));
		assertEquals(CCL_RENAME_EMPTY, selenium.getText(getPanelXP(XP_CCL_ERROR_ELEMENT_TEMPLATE, "CCL_renamePanel")));
	}

	public void testRenameListOnAllListsPage() {
		List lists = getCCLOnAllListsPage();
		
		// Use this list for NameExists tests
		String existingList = findUnusedListName("existingList", lists);
		createListOnAllListsPage(existingList);

		// This list will be renamed
		String unusedName = findUnusedListName("renameTest", lists);
		createListOnAllListsPage(unusedName);
		
		lists = getCCLOnAllListsPage();
		CCList l = (CCList) lists.get(0);
		assertEquals(unusedName, l.name);
		String newName = findUnusedListName("newRenameTest", lists);
		l.renameListOnAllListsPage(newName, false, true); // Use enter instead of clicking on Continue
		lists = getCCLOnAllListsPage();
		CCList l2 = (CCList) lists.get(0);
		assertEquals(newName, l2.name);
		assertEquals(l.id, l2.id);
		assertEquals(l.name, l2.name);
		
		assertTrue(l2.renameListOnAllListsPage(existingList, true, false));
		assertEquals(CCL_LIST_EXISTS, selenium.getText(getPanelXP(XP_CCL_ERROR_ELEMENT_TEMPLATE, "CCL_renamePanel")));
		
		assertTrue(l2.renameListOnAllListsPage("", true, false));
		assertEquals(CCL_RENAME_EMPTY, selenium.getText(getPanelXP(XP_CCL_ERROR_ELEMENT_TEMPLATE, "CCL_renamePanel")));

		assertTrue(l2.renameListOnAllListsPage("         	", true, false));
		assertEquals(CCL_RENAME_EMPTY, selenium.getText(getPanelXP(XP_CCL_ERROR_ELEMENT_TEMPLATE, "CCL_renamePanel")));
	}

	
}
