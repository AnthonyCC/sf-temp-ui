package com.freshdirect.selenium.ccl;

import java.util.List;

import com.freshdirect.selenium.FDSeleniumTestSupport;

public class AllListsPageTest extends FDSeleniumTestSupport {

	public void setUp() throws Exception {
		super.setUp();
		doLogIn();
		deleteListsExceptLast();
	}


	
	/**
	 * TEST-CCL-M1-5.1 Display All Lists
	 * Rendering (this is a new page)
	 * 
	 * ref: https://dev.euedge.com/projects/freshdirect/wiki/Projects/CCL/Testing/TestPlan#test-ccl-m1-5.1
	 * 
	 * @throws Exception
	 */
	public void testAllListsPage() throws Exception {
		List lists = getCCLOnAllListsPage();
		createListOnAllListsPage(findUnusedListName("alfa", lists));
		createListOnAllListsPage(findUnusedListName("beta", lists));
		createListOnAllListsPage(findUnusedListName("gamma", lists));
		createListOnAllListsPage(findUnusedListName("delta", lists));
		
		lists = getCCLOnAllListsPage();
		
		for (int i = 1; i <= lists.size(); i++) {
			CCList l = (CCList) lists.get(i-1);
			selenium.open("/quickshop/all_lists.jsp");
			clickOnOpenListLinkOnAllListsPage(i+1);
			String openedListName = getListNameOnDetailPage();
			assertEquals(l.name, openedListName);			
		}
	}

}
