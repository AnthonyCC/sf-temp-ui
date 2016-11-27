package com.freshdirect.selenium;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.thoughtworks.selenium.*;


import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;

import com.freshdirect.framework.util.CSVUtils;
import com.freshdirect.framework.util.NVL;
import com.freshdirect.framework.util.QueryStringBuilder;


public class FDSeleniumTestSupport extends SeleniumTestSupport {

	public static final String XP_CCL_ERROR_ELEMENT_TEMPLATE = "//div[@id='$PANELNAME$']//div[contains(@class,'bd')]//div[contains(@class,'text11rbold')]";
/*	changed line commented out, new below it  */
//	public static final String XP_CCL_CLOSE_TEMPLATE = "//div[@id='$PANELNAME$']//span[@class='container-close']";
	public static final String XP_CCL_CLOSE_TEMPLATE = "link=Close";
	public static final String XP_CCL_HELP_TEMPLATE = "//div[@id='$PANELNAME$']//span[@class='ccl-panel-help']";
	public static final String XP_DELETE_LIST = "link=DELETE LIST";
	public static final String XP_RENAME_LIST = "link=RENAME LIST";
	public static final String XP_LIST_DETAIL_NAME = "//table[@class='bodyCopy']//td[@class='title18']/b";
	public static final String CCL_LIST_EXISTS = "Oops! That name is taken.";
	public static final String CCL_LIST_EMPTY_NAME = "Your new list needs a name:";
	public static final String CCL_RENAME_EMPTY = "Your list needs a name:";
	public static final String ENTER_NAME = "Please enter a name:";

	public static final String XP_SALES_UNIT = "//select[@name='salesUnit']";
	public static final String XP_MT_BF_PAK = "//select[@name='C_MT_BF_PAK']";
	public static final String XP_MT_BF_MAR = "//select[@name='C_MT_BF_MAR']";
	public static final String XP_MT_BF_TW1 = "//select[@name='C_MT_BF_TW1']";
	public static final String XP_QUANTITY = "//input[@type='text' and @name='quantity']";
	
	public static final String XP_ADD_QUANTITY = "//img[@alt='Increase quantity']";
	public static final String XP_REMOVE_QUANTITY = "//img[@alt='Decrease quantity']";
	
	public static final String XP_SORT = "//select[@name='sort']";
	public static final String XP_BRAND = "//select[@name='brand']";
	
	public static final String XP_SAVE_TO_SHOPPING_LIST = "//a[@id='ccl-add-action']";
	public static final String XP_ITEMS_ADDED = "//div[@id='CCL_savePanel']/div/div[@class='toleft']";
	public static final String XP_LIST_SELECT = "//div[@id='CCL_savePanel']//select[@id='dropdown']";
	public static final String XP_ITEMS_ADDED_LISTNAME = "//span[@id=('CCL_listName')]";
	public static final String XP_KEEP_SHOPPING = "//a[@id='keepShopping']";
	public static final String XP_VISIT_THIS_LIST = "//a[@id='visitThisList']";
	public static final String XP_CREATE_ANOTHER_LIST = "//a[@id='createAnotherList']";
	public static final String XP_CONTINUE = "//a[@id='contLink']";
	public static final String XP_CONTINUE_SHOPPING = "//div[@id='CCL_renamePanel']//a[@id='contShopping']";
	public static final String ITEMS_ADDED_PREFIX = "Items saved to ";
	public static final String ITEMS_ADDED_SUFFIX = ".";
	
	public static final String XP_REMOVE_LINK = "//table[@id='vieworder_table']//a[@name='remove_link']"; // returns the first remove link

	public static final String XP_PRODUCT_ROW_TEMPLATE = "//table[@id='vieworder_table']//tr[$ROWNUMBER$]";
	public static final String XP_DEPARTMENT_LINK_TEMPLATE = XP_PRODUCT_ROW_TEMPLATE + "//a[contains(@href,'/department.jsp')]";
	public static final String XP_PRODUCT_QUANTITY_TEMPLATE = XP_PRODUCT_ROW_TEMPLATE + "//input[contains(@name,'quantity_')]";
	public static final String XP_PRODUCT_LINK_TEMPLATE = XP_PRODUCT_ROW_TEMPLATE + "//a[contains(@href,'ccl_item_modify.jsp')]";
	public static final String XP_CCL_PANEL_TEXT_INPUT = "//input[@type = 'text' and contains(@class, 'ccl-panel-text-input')]";
	
	public static final String XP_CART_CLICK = "//a[@id='your_cart']";
	public static final String XP_ADD_TO_CART_CLICK = "//input[@name='add_to_cart']";
	public static final String XP_REMOVE_ALL_FROM_CART_CLICK = "//a[@name='remove_all']";
	public static final String XP_ADD_SELECTED_TO_CART_CLICK = "//input[@name='addMultipleToCart']";
	public static final String XP_CLEAR_CART_CLICK = "//a[@name='remove_all']";
	
	public static final String SC_REMOVE_ALL = "link=Remove All Items";
		
	public static final String BAD_CHARACTERS = "<>&;:\"'$\\~^+-.";
	
	public static final String SELENIUM_INDEX = "/index.jsp";

	/* Menu Bar Navigation */
	public static final String VIEW_CART = "NAV_CART_IMG";
	public static final String CHECK_OUT = "NAV_CHECKOUT_IMG";
	public static final String DELIVERY_INFO = "DELIVERY_IMG";
	public static final String QUICK_SHOP = "DELIVERY_IMG";
	public static final String YOUR_ACCOUNT = "NAV_QUICKSHOP_IMG";
	public static final String GET_HELP = "NAV_YOURACCOUNT_IMG";
	
	public static final String FD_HOME = "//map[@id='globalNav']/area[1]";
	public static final String FD_FRUIT = "//map[@id='globalNav']/area[2]";
	public static final String FD_VEGETABLES = "//map[@id='globalNav']/area[3]";
	public static final String FD_MEAT = "//map[@id='globalNav']/area[4]";
	public static final String FD_SEAFOOD = "//map[@id='globalNav']/area[5]";
	public static final String FD_DELI = "//map[@id='globalNav']/area[6]";
	public static final String FD_CHEESE = "//map[@id='globalNav']/area[7]";
	public static final String FD_DAIRY = "//map[@id='globalNav']/area[8]";
	
	public static final String FD_4_MINUTE_MEALS = "//map[@id='globalNav']/area[9]";
	public static final String FD_READY_TO_COOK = "//map[@id='globalNav']/area[10]";
	public static final String FD_HEAT_AND_EAT = "//map[@id='globalNav']/area[11]";
	public static final String FD_BAKERY = "//map[@id='globalNav']/area[12]";
	public static final String FD_CATERING = "//map[@id='globalNav']/area[13]";

	public static final String FD_WHATS_GOOD = "//map[@id='globalNav']/area[14]";
	public static final String FD_ORGANIC = "//map[@id='globalNav']/area[15]";
	public static final String FD_LOCAL = "//map[@id='globalNav']/area[16]";
	public static final String FD_KOSHER = "//map[@id='globalNav']/area[17]";
	public static final String FD_RECIPES = "//map[@id='globalNav']/area[18]";
	
	public static final String FD_PASTA = "//map[@id='globalNav']/area[19]";
	public static final String FD_COFFEE = "//map[@id='globalNav']/area[20]";
	public static final String FD_GROCERY = "//map[@id='globalNav']/area[21]";
	public static final String FD_HEALTH_BEAUTY = "//map[@id='globalNav']/area[22]";
	public static final String FD_BUY_BIG = "//map[@id='globalNav']/area[23]";
	public static final String FD_FROZEN = "//map[@id='globalNav']/area[24]";
	public static final String FD_WINE = "//map[@id='globalNav']/area[25]";
	
	/* Front page navigation
	/* Use these if you want 
	 * to access the links 
	 * from the front page.
	 * Row 1                 */
	public static final String FP_FRUIT = "//td[3]/map[1]/area[1]";
	public static final String FP_VEGETABLES = "//td[3]/map[1]/area[2]";
	public static final String FP_MEAT = "//td[3]/map[1]/area[3]";
	public static final String FP_SEAFOOD = "//td[3]/map[1]/area[4]";
	public static final String FP_DELI = "//td[3]/map[1]/area[5]";
	public static final String FP_CHEESE = "//td[3]/map[1]/area[6]";
	public static final String FP_DAIRY = "//td[3]/map[1]/area[7]";
	
	/* Row 2 */
	public static final String FP_ORGANIC_ALL_NATURAL = "//map[2]/area[1]";
	public static final String FP_LOCAL_FOODS = "//map[2]/area[2]";
	public static final String FP_KOSHER = "//map[2]/area[3]";
	public static final String FP_PASTA = "//map[2]/area[4]";
	
	/* Row 3 */
	public static final String FP_BAKERY = "//map[3]/area[1]";
	public static final String FP_READY_TO_COOK = "//map[3]/area[2]";
	public static final String FP_HEAT_EAT = "//map[3]/area[3]";
	public static final String FP_CATERING = "//map[3]/area[4]";
	public static final String FP_4_MINUTE_MEALS = "//map[3]/area[5]";
	
	/* Row 4 */
	public static final String FP_COFFEE = "//map[4]/area[1]";
	public static final String FP_GROCERY = "//map[4]/area[2]";
	public static final String FP_FROZEN = "//map[4]/area[3]";
	public static final String FP_HEALTH_BEAUTY = "//map[4]/area[4]";
	public static final String FP_BUY_BIG = "//map[4]/area[5]";
	public static final String FP_WINE = "//map[4]/area[6]";
	
	
	
	public Pattern linkPatternWithItems = Pattern.compile("\\A(.*)\\(([0-9]+)\\sItems?\\)\\z");
	public Pattern nameLinkPatternWithoutItems = Pattern.compile("\\A(.*)\\z");

	
	public Pattern listNamePattern = Pattern.compile("\\A(.*)\\(([0-9]+)\\sitems?\\)\\z");
	public Pattern hrefPattern = Pattern.compile("\\A.*/shop_from_list.jsp\\?ccListId=(.*)\\z");
	public Pattern spanPattern = Pattern.compile("\\Alist:(.*),([0-9]+)\\z");
	
	public void doLogIn() {
		selenium.open("/");
		selenium.click("link=Click here to log in");
		selenium.waitForPageToLoad(NORMAL_LOAD_TIMEOUT);
		selenium.type("userid", SeleniumTestProperties.getTestUsername());
		selenium.type("password", SeleniumTestProperties.getTestPassword());
		selenium.click("check_access");
		selenium.waitForPageToLoad(INITIAL_LOAD_TIMEOUT);
	}
	
	public void doLogout() {
		selenium.click("//b");
		selenium.waitForPageToLoad(NORMAL_LOAD_TIMEOUT);
	}

	public void addToCart() {
		selenium.click("add_to_cart");
		selenium.waitForPageToLoad(NORMAL_LOAD_TIMEOUT);
	}
	
	public void addMultipleToCart() {
		selenium.click("addMultipleToCart");
		selenium.waitForPageToLoad(NORMAL_LOAD_TIMEOUT);
	}
	
	public String findUnusedListName(String string, List lists) {
		String ns = string + BAD_CHARACTERS;
		int i = 2;
		for (Iterator it = lists.iterator(); it.hasNext();) {
			CCList cc = (CCList) it.next();
			if (cc.name.equals(ns)) {
				ns = string + BAD_CHARACTERS + "_" + i++;
				it = lists.iterator();
			}
		}		
		return ns;
	}

	public void createListOnQuickshopPage(String listName) {
		selenium.open("/quickshop/index.jsp"); //was all_lists.jsp
		selenium.waitForPageToLoad(NORMAL_LOAD_TIMEOUT);		
		createListOnCurrentPage(listName, false, false);
	}

	public void createListOnAllListsPage(String listName) {
		selenium.open("/quickshop/all_lists.jsp");
		selenium.waitForPageToLoad(NORMAL_LOAD_TIMEOUT);
		createListOnCurrentPage(listName, false, false);
	}
	
	public boolean createListOnCurrentPage(String listName, boolean assertError, boolean useEnter) {
		selenium.click("link=click here.");
		selenium.type("//div[2]/input", listName);
		if (useEnter) {
			selenium.keyDown("//div[2]/input", "\\13");
		} else {
			selenium.click("contLink");
		}
		if (assertError) {
			waitForElementPresent(getPanelXP(XP_CCL_ERROR_ELEMENT_TEMPLATE, "CCL_createPanel"), SMALL_LOAD_TIMEOUT);
			if (selenium.isElementPresent(getPanelXP(XP_CCL_ERROR_ELEMENT_TEMPLATE, "CCL_createPanel"))) {
				return true;
			} 
		} else {
			selenium.waitForPageToLoad(NORMAL_LOAD_TIMEOUT);		
		}
		return false;
	}
	
	public List getCCLOnAllListsPage() {
		selenium.open("/quickshop/all_lists.jsp");
		selenium.waitForPageToLoad(NORMAL_LOAD_TIMEOUT);
		return getCCLOnCurrentPage(nameLinkPatternWithoutItems,2);
	}

	public List getCCLOnQuickShopPage() {
		selenium.open("/quickshop/index.jsp");
		selenium.waitForPageToLoad(NORMAL_LOAD_TIMEOUT);
		return getCCLOnCurrentPage(linkPatternWithItems,1);
	}

	public List getCCLOnCurrentPage(Pattern nameLinkPattern, int start) {
		List l = new ArrayList();
	

		for(int i=start; selenium.isElementPresent("//table[@id='CCL_lists']//tr["+i+"]//span[contains(@id,'list')]");++i) {

			String linkText = selenium.getText("//table[@id='CCL_lists']//tr["+i+"]//a[contains(@href, 'shop_from_list')]");
			String spanAttr = selenium.getAttribute("//table[@id='CCL_lists']//tr["+i+"]//span[contains(@id, 'list')]@id");
			
			Matcher idMatcher = spanPattern.matcher(spanAttr);
			Matcher linkMatcher = nameLinkPattern.matcher(linkText);
			
			assertTrue(idMatcher.matches());
			assertTrue(linkMatcher.matches());
			
			String name = linkMatcher.group(1).trim();
		
			String id = idMatcher.group(1);
			int items = Integer.valueOf(idMatcher.group(2)).intValue();
			
			l.add(new CCList(name,items,id));

		}
		
		return l;
	}
	
	public void waitForElementPresent(String locator, String timeoutStr) {
		int timeout = Integer.valueOf(timeoutStr).intValue();
		for (int second = 0;;second+=1000) {
			if (second >= timeout) fail("waitForElementPresent timeout:"+locator);
			try { if (selenium.isElementPresent(locator)) break; } catch (Exception e) {}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
		}		
	}
	
	public void waitForElementNotPresent(String locator, String timeoutStr) {
		int timeout = Integer.valueOf(timeoutStr).intValue();
		for (int second = 0;;second+=1000) {
			if (second >= timeout) fail("waitForElementPresent timeout:"+locator);
			try { if (!selenium.isElementPresent(locator)) break; } catch (Exception e) {}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
		}		
	}
	
	/** Retrieve test data.
	 * 
	 * @param queryParams
	 * @return
	 * @throws IOException
	 */
	
	public List retrieveTestData(String queryParams) throws IOException {
		//StringBuffer URL = new StringBuffer(System.getProperty("selenium.test_base"));
		StringBuffer URL = new StringBuffer("http://localhost:7001");
		URL.append("/test/data/data.jsp?").append(queryParams);

		HttpClient client = new HttpClient();
		GetMethod get = new GetMethod(URL.toString());
		client.executeMethod(get);
		List result = CSVUtils.parse(get.getResponseBodyAsStream(), true, false); 
		get.releaseConnection();
		client.endSession();
		return result;
	}

	
	public String getPanelXP(String template, String panel) {
		return getXP(template, "PANELNAME", panel);
	}
	
	public String getXP(String template, String varname, String value) {
		return template.replaceAll("\\$"+varname+"\\$", value);
	}
	
	public CCList ensureSingleListWithName(String name) {
		List lists = getCCLOnAllListsPage();
		String unusedList = findUnusedListName(name, lists);
		createListOnAllListsPage(unusedList);
		for (Iterator it = lists.iterator(); it.hasNext();) {
			CCList cc = (CCList) it.next();
			cc.deleteList();
		}		
		lists = getCCLOnAllListsPage();
		return (CCList) lists.get(0);
	}
	
	public void openCotswold() {
		selenium.open("/");
		selenium.click(FD_CHEESE);
		selenium.waitForPageToLoad(NORMAL_LOAD_TIMEOUT);
		verifyTrue(selenium.isElementPresent("//img[@alt='Cheddars & Jacks']"));
		verifyTrue(selenium.isElementPresent("//tr[2]/td[2]/a/img"));
		selenium.click("//tr[2]/td[2]/a/img");
		selenium.waitForPageToLoad(NORMAL_LOAD_TIMEOUT);
		verifyTrue(selenium.isTextPresent("Cotswold"));
		selenium.click("//a[contains(@href, '/product.jsp?catId=jackoth&trk=cpage&productId=eng_cotswold')]");
		selenium.waitForPageToLoad(NORMAL_LOAD_TIMEOUT);
	
	}

	
	public void openFiletMignon() {
		selenium.open("/");
		selenium.click(FD_MEAT);
		//selenium.click("//area[@alt='Meat']");
		selenium.waitForPageToLoad(NORMAL_LOAD_TIMEOUT);
		selenium.click("link=Beef");
		//selenium.click("//a/font[text()='Beef']");
		selenium.waitForPageToLoad(NORMAL_LOAD_TIMEOUT);
		selenium.click("//table[2]/tbody/tr[2]/td[1]/a");
		//selenium.click("//a/font[text()='Steaks']");
		selenium.waitForPageToLoad(NORMAL_LOAD_TIMEOUT);
		selenium.click("//a[contains(@href, '/product.jsp?catId=bgril&trk=cpage&productId=bstk_flet_dfat')]");
		//selenium.click("link=Filet Mignon");
		selenium.waitForPageToLoad(NORMAL_LOAD_TIMEOUT);		
	}
	
	public void openSeviroliSpinachMozarellaRavioli() {
		selenium.open("/product.jsp?productId=dpsta_rff_spchrav&catId=frshpas_ravshell");
		selenium.waitForPageToLoad(NORMAL_LOAD_TIMEOUT);
	}
	
	public void openProduct(String prodId, String catId, String skuCode, String quantity, String salesUnit, String optionsQueryParams) {
		QueryStringBuilder queryParams = new QueryStringBuilder();
		queryParams.
			addParam("productId",prodId).
			addParam("catId",catId).
			addParam("skuCode",skuCode).
			addParam("salesUnit",salesUnit);
		StringBuffer URL = new StringBuffer("/product.jsp?").append(queryParams.toString());
		if (NVL.apply(optionsQueryParams,"").length() > 0) URL.append('&').append(optionsQueryParams);
		
		selenium.open(URL.toString());
		selenium.waitForPageToLoad(NORMAL_LOAD_TIMEOUT);
	}
	
	public void openFirstCannedGoods() {
		selenium.open("/");
		selenium.click("//area[@alt='Grocery']");
		selenium.waitForPageToLoad(NORMAL_LOAD_TIMEOUT);
		selenium.click("link=Canned Goods");
		selenium.waitForPageToLoad(NORMAL_LOAD_TIMEOUT);
		selenium.click("//a[2]");
		selenium.waitForPageToLoad(NORMAL_LOAD_TIMEOUT);		
	}
	
	public void openFirstBeefRecipe() {
		selenium.open("/");
		selenium.click("//area[@alt='Recipes']");
		selenium.waitForPageToLoad(NORMAL_LOAD_TIMEOUT);
		selenium.click("link=Beef/Veal");
		selenium.waitForPageToLoad(NORMAL_LOAD_TIMEOUT);
		selenium.click("//tr[3]/td/a[1]/b");
		selenium.waitForPageToLoad(NORMAL_LOAD_TIMEOUT);		
	}
	
	public void openFirstPartyPackage() {
		selenium.click("//area[@alt='Easy Meals']"); //where?
		selenium.waitForPageToLoad(NORMAL_LOAD_TIMEOUT);
		selenium.click("link=Party Packages");
		selenium.waitForPageToLoad(NORMAL_LOAD_TIMEOUT);
		selenium.click("//td/table/tbody/tr[1]/td[2]/a/b");
		selenium.waitForPageToLoad(NORMAL_LOAD_TIMEOUT);
	}
	
	public void clickRenameLinkForListOnAllListsPage(String listId) {
		List lists = getCCLOnAllListsPage();
		int i = 2;
		for (Iterator it = lists.iterator(); it.hasNext();) {
			CCList l = (CCList) it.next();
			if (listId.equals(l.id)) {
				selenium.click("//table[@id='CCL_lists']//tr["+i+"]//a[text()='RENAME']");
				return;
			}
			i++;
		}
		fail();
	}
	

	
	public CCList deleteListsExceptLast() {
		// Delete all lists, except the last one
		List lists = getCCLOnAllListsPage();
		for (int i = 0; i < lists.size() - 1; i++) {
			CCList l = (CCList) lists.get(i);
			l.deleteList();
		}
		return (CCList) lists.get(lists.size()-1);
	}

	public String getListNameFromPattern(String string) {
		Matcher m = listNamePattern.matcher(string);
		assertTrue(m.matches());
		return m.group(1).trim();
	}	
	
	public String getListNameOnDetailPage() {
		String s = selenium.getText(XP_LIST_DETAIL_NAME);
		return getListNameFromPattern(s);
	}
	
	public void clickOnOpenListLinkOnAllListsPage(int row) {
		selenium.click("//table[@id='CCL_lists']//tr["+row+"]//a[contains(@href, 'shop_from_list')]");
		selenium.waitForPageToLoad(NORMAL_LOAD_TIMEOUT);
	}
	
	public class CCList {
		public String name;
		public int items;
		public String id;

		public CCList(String name, int items, String id) {
			this.name = name;
			this.items = items;
			this.id = id;			
		}

		public void openList() {
			selenium.open("/quickshop/shop_from_list.jsp?ccListId="+id);
			selenium.waitForPageToLoad(NORMAL_LOAD_TIMEOUT);			
		}
		
		public void deleteList() {
			openList();
			assertTrue(selenium.isElementPresent(XP_DELETE_LIST));
			selenium.click(XP_DELETE_LIST);
			assertEquals("Are you sure you want to delete "+name+"?",selenium.getText("//div[@id='CCL_deletePanel']/div[2]/div[1]"));
			selenium.click("deleteList");
			selenium.waitForPageToLoad(NORMAL_LOAD_TIMEOUT);			
		}

		public boolean renameList(String newName, boolean assertError, boolean useEnter) {
			openList();
			assertTrue(selenium.isElementPresent(XP_RENAME_LIST));
			selenium.click(XP_RENAME_LIST);
			selenium.type("//div[2]/input", newName);
			if (useEnter) {
				selenium.keyDown("//div[2]/input", "\\13");
			} else {
				selenium.click("contLink");
			}
			if (assertError) {
				waitForElementPresent(getPanelXP(XP_CCL_ERROR_ELEMENT_TEMPLATE, "CCL_renamePanel"), SMALL_LOAD_TIMEOUT);
				if (selenium.isElementPresent(getPanelXP(XP_CCL_ERROR_ELEMENT_TEMPLATE, "CCL_renamePanel"))) {
					return true;
				} 
			} else {
				waitForElementPresent("//div[@id='CCL_renamePanel']", SMALL_LOAD_TIMEOUT);
				selenium.click(XP_CONTINUE_SHOPPING);
				selenium.waitForPageToLoad(NORMAL_LOAD_TIMEOUT);		
			}
			this.name = newName.trim();
			return false;
		}
		
		public boolean renameListOnAllListsPage(String newName, boolean assertError, boolean useEnter) {
			clickRenameLinkForListOnAllListsPage(id);
			selenium.type("//div[2]/input", newName);
			if (useEnter) {
				selenium.keyDown("//div[2]/input", "\\13");
			} else {
				selenium.click("contLink");
			}
			if (assertError) {
				waitForElementPresent(getPanelXP(XP_CCL_ERROR_ELEMENT_TEMPLATE, "CCL_renamePanel"), SMALL_LOAD_TIMEOUT);
				if (selenium.isElementPresent(getPanelXP(XP_CCL_ERROR_ELEMENT_TEMPLATE, "CCL_renamePanel"))) {
					return true;
				} 
			} else {
				waitForElementPresent("//div[@id='CCL_renamePanel']", SMALL_LOAD_TIMEOUT);
				selenium.click(XP_CONTINUE_SHOPPING);
				selenium.waitForPageToLoad(NORMAL_LOAD_TIMEOUT);		
			}
			this.name = newName.trim();
			return false;			
		}
	}	

	public List parseListContents() {
		List result = new ArrayList();
		List category = null;
		for (int i=1; selenium.isElementPresent(getRowXP(XP_PRODUCT_ROW_TEMPLATE,i));i++) {
			if (selenium.isElementPresent(getRowXP(XP_DEPARTMENT_LINK_TEMPLATE,i))) {
				// Setting department
				String name = selenium.getText(getRowXP(XP_DEPARTMENT_LINK_TEMPLATE,i));
				String link = selenium.getAttribute(getRowXP(XP_DEPARTMENT_LINK_TEMPLATE+"@href",i));
				category = new ProductCategory(i, name, link);
				result.add(category);
			} else if (selenium.isElementPresent(getRowXP(XP_PRODUCT_LINK_TEMPLATE,i))) {
				// Found a product
				if (category == null) {
					throw new IllegalStateException("Category == null during list parse");
				}
				String prodNumStr = selenium.getAttribute(getRowXP(XP_PRODUCT_QUANTITY_TEMPLATE+"@name",i));
				int prodNum = new Integer(prodNumStr.substring(prodNumStr.indexOf('_')+1)).intValue();
				String quantity = selenium.getAttribute(getRowXP(XP_PRODUCT_QUANTITY_TEMPLATE+"@value",i));
				String name = selenium.getText(getRowXP(XP_PRODUCT_LINK_TEMPLATE,i));
				String link = selenium.getAttribute(getRowXP(XP_PRODUCT_LINK_TEMPLATE+"@href",i));
				String confDesc = selenium.getText(getRowXP(XP_PRODUCT_ROW_TEMPLATE,i)+"//font[@class='text9nb']");
				category.add(new ProductInfo(i, prodNum, name, quantity, link, confDesc));
			}
			
		}
		return result;
	}


	public String getRowXP(String template, int i) {
		return getXP(template, "ROWNUMBER", ""+i);
	}


	public class ProductInfo {
		public String name;
		public String quantity;
		public int row;
		public int productNum;
		public String link;
		public String confDesc;
		public ProductInfo(int row, int productIndex, String name, String quantity, String link, String confDesc) {
			this.row = row;
			this.productNum = productIndex;
			this.name = name;
			this.quantity = quantity;
			this.link = link;
			this.confDesc = confDesc;
		}
		
		public String getRemoveLinkXP() {
			return getRowXP(XP_PRODUCT_ROW_TEMPLATE, row)+"//a[text() = 'REMOVE']";
		}
	}
	
	public class ProductCategory extends ArrayList {
		public int row;
		public String name;
		public String link;
		
		public ProductCategory(int row, String name, String link) {
			this.row = row;
			this.name = name;
			this.link = link;
		}
	}

	public static class CompareByName implements Comparator {
		 public int compare(Object o1, Object o2) {
			 if (!(o1 instanceof CCList) || !(o2 instanceof CCList)) return 0;
			 
			 CCList l1 = (CCList)o1;
			 CCList l2 = (CCList)o2;
			 
			 // should not return 0, since YoYo and yoyo than would be treated equal
			 return l1.name.compareToIgnoreCase(l2.name) < 0 ? -1 : 1;
		 }
		 
		 public boolean equals(Object o) {
			 // there is only one static instance
			 return o == this;
		 }
	}
	
	
	/**
	 * From the "Listname (x Items)" pattern, get the item count.
	 * @return item count or -1
	 */
	public int getItemCount(String listName) {
		try {
			if (listName.endsWith(" items)")) {
				return Integer.parseInt(listName.substring(listName.lastIndexOf('(')+1,listName.length() - 7));
			} else if (listName.endsWith(" item)")) return 1;
		} catch(NumberFormatException e) {
			return -1;	
		} 
		
		return -1;
	}
	
	
	public void goToList(String listId) {
		selenium.open("/quickshop/shop_from_list.jsp?ccListId=" + listId);
		selenium.waitForPageToLoad(NORMAL_LOAD_TIMEOUT);
	}
	
	public void goToCart() {
		selenium.open("/view_cart.jsp");
		selenium.waitForPageToLoad(NORMAL_LOAD_TIMEOUT);
	}
	
	public void clearCart() {
		selenium.open("view_cart.jsp?remove=1");
		selenium.waitForPageToLoad(NORMAL_LOAD_TIMEOUT);
	}
	
	public void goToAllListsPage() {
		selenium.open("/quickshop/all_lists.jsp");
		selenium.waitForPageToLoad(NORMAL_LOAD_TIMEOUT);
	}
	
	
	public void removeAllElements(String listId) {
		goToList(listId);
		
	    for(;;) {
	    	waitForElementPresent(XP_LIST_DETAIL_NAME,NORMAL_LOAD_TIMEOUT);
			String title = selenium.getText(XP_LIST_DETAIL_NAME);
	    	int count = getItemCount(title);
	    	if (count == 0) break;
	    	else if (count == -1) throw new RuntimeException("Could not deduce item count");
	    	selenium.click(XP_REMOVE_LINK);
			selenium.waitForPageToLoad(NORMAL_LOAD_TIMEOUT);
	    }
	}
	
	
	public void assertShoppingList(String myListName){
		// create list
		// selenium.click(XP_CONTINUE);
		waitForElementPresent(XP_ITEMS_ADDED, NORMAL_LOAD_TIMEOUT);
		
		/*
		String itemsAdded = selenium.getText(XP_ITEMS_ADDED);
		String assertedItemsAdded = ITEMS_ADDED_PREFIX + listNames[listNames.length-1] + ITEMS_ADDED_SUFFIX; 
		assertEquals(assertedItemsAdded, itemsAdded);	
		String listName = selenium.getText(XP_ITEMS_ADDED_LISTNAME); // Checks that the listname is inside a bold tag
		assertEquals(listNames[listNames.length-1], listName);
		*/
		String listName = selenium.getText("//span[@id='CCL_listName']");
		assertEquals(myListName, listName);

		selenium.click(XP_KEEP_SHOPPING);
	}
	
	
	
	
	
	
	
	/*-------------------------------------------------------------------------*/
	
	public void open4MM(){
		
		selenium.click(FD_4_MINUTE_MEALS);
		selenium.waitForPageToLoad(NORMAL_LOAD_TIMEOUT);
		assertEquals("FreshDirect - 4-Minute Meals", selenium.getTitle());
	}
	
	
}
