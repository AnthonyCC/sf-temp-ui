package com.freshdirect.selenium.ccl;

import com.freshdirect.selenium.FDSeleniumTestSupport;


import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.util.QueryStringBuilder;

import java.util.Random;
import java.util.Iterator;
import java.util.List;

import java.io.IOException;

import org.apache.log4j.Category;

public class ShoppingLoadTest extends FDSeleniumTestSupport {

	private static Category LOGGER = LoggerFactory.getInstance(ShoppingLoadTest.class);
	private final static int LIST_COUNT = 10; // number of lists to be used
	private final static int MAX_ITEMS = 100; // max items in a list
	private final static int MAX_PRODUCTS = 2500; // max products to retrieve
	private final static int MAX_VARIATION_OF_SINGLE_PRODUCT = 5; 

	public void setUp() throws Exception {
		super.setUp();
		doLogIn();
		deleteListsExceptLast();
	}

	private static final String PERISHABLE_DATA_QUERY;

	static {
		QueryStringBuilder qs = new QueryStringBuilder();
		qs.
			addParam("max_prods", MAX_PRODUCTS). // MAX PRODUCTS TO RETRIEVE
			addParam("notunav", "true"). // ONLY NOT UNAVAILABLE
			addParam("nothidden","true"). // NOT HIDDEN
			addParam("notgroc", "true"). // ONLY NON GROCERY CATEGORY LAYOUTS
			addParam("layout", "PERISHABLE"). // ONLY PERISHABLES
			addParam("varmax", MAX_VARIATION_OF_SINGLE_PRODUCT). // ONLY THIS MANY OPTIONS FROM THE PERMS
			addParam("output", "csv"). // AS CSV
			addParam("produce", "true"). // NO UI FORMS, JUST DATA
			addParam("column_order", // COLUMNS IN THIS ORDER
				"product_id.parent_id.perm_skus.min_quantity.perm_salesunits.perm_options");
		PERISHABLE_DATA_QUERY = qs.toString();
	}

	public void testPerishables() throws IOException {

		clearCart();
	
		List bundle = retrieveTestData(PERISHABLE_DATA_QUERY);

		LOGGER.info("RETRIEVED " + bundle.size() + " PRODUCTS.");
		// create a default list, it will not be used
		ensureSingleListWithName("DEFAULT"); // default


		CCList[] testLists = new CCList[LIST_COUNT];

		for (int i = 0; i < LIST_COUNT; ++i)
			createListOnAllListsPage("LISTA " + i);
		for (Iterator i = getCCLOnAllListsPage().iterator(); i.hasNext();) {
			CCList lista = (CCList) i.next();
			if (!lista.name.startsWith("LISTA"))
				continue;
			testLists[Integer.parseInt(lista.name.substring("LISTA ".length()))] = lista;
		}

		CCList currentList = testLists[0];

		Random R = new Random(System.currentTimeMillis());

		int productCount = 0;
		for (Iterator i = bundle.iterator(); i.hasNext(); ++productCount) {
			List row = (List) i.next();
			
			if (productCount % 7 == 0) this.goToAllListsPage();

			openProduct(
				(String) row.get(0), /* productId */
				(String) row.get(1), /* catId */
				(String) row.get(2), /* skuCode */
				row.get(3).toString(), /* quantity */
				(String) row.get(4), /* salesUnit */
				(String) row.get(5) /* [options] */
			);

			try {
				selenium.click(XP_SAVE_TO_SHOPPING_LIST);
				waitForElementPresent(XP_LIST_SELECT, NORMAL_LOAD_TIMEOUT);

				String[] listNames = selenium.getSelectOptions(XP_LIST_SELECT);
				String listName = null;

				for (int ln = 0; ln < listNames.length; ++ln) {
					if (listNames[ln].startsWith(currentList.name)) {
						listName = listNames[ln];
						break;
					}
				}
				selenium.select(XP_LIST_SELECT, listName);

				selenium.click(XP_CONTINUE);
				waitForElementPresent(XP_ITEMS_ADDED, NORMAL_LOAD_TIMEOUT);
				

				if (getItemCount(listName) >= MAX_ITEMS) {
					removeAllElements(currentList.id);
				} 
				
				if (getItemCount(listName) >= 10 && productCount % 7 == 0) {
					goToList(currentList.id);
					selenium.click(XP_ADD_SELECTED_TO_CART_CLICK);
					selenium.waitForPageToLoad(NORMAL_LOAD_TIMEOUT);
				}
				
				if (productCount % 200 == 0) {
					goToCart();
					clearCart();		
				}
				
				currentList = testLists[R.nextInt(LIST_COUNT)];
				
			} catch (Exception e) {
				LOGGER.warn(e);
			}
		}
	}
}
