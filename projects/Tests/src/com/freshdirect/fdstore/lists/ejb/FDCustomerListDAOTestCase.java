package com.freshdirect.fdstore.lists.ejb;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.dbunit.DatabaseUnitException;

import com.freshdirect.DbTestCaseSupport;
import com.freshdirect.fdstore.FDConfiguration;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.ejb.EnumCustomerListType;
import com.freshdirect.fdstore.lists.FDCustomerCreatedList;
import com.freshdirect.fdstore.lists.FDCustomerCreatedListInfo;
import com.freshdirect.fdstore.lists.FDCustomerProductListLineItem;
import com.freshdirect.fdstore.lists.FDCustomerRecipeList;
import com.freshdirect.fdstore.lists.FDCustomerRecipeListLineItem;
import com.freshdirect.fdstore.lists.FDCustomerShoppingList;
import com.freshdirect.framework.core.PrimaryKey;


public class FDCustomerListDAOTestCase extends DbTestCaseSupport{
	
	protected void setUp() throws Exception {
		super.setUp();
		currentDate = null;
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		currentDate = null;
	}

	public FDCustomerListDAOTestCase(String name) {
		super(name);
	}
	
	private final List idGenerator = new ArrayList();
	
	private Date currentDate;

	private final FDCustomerListDAO dao = new FDCustomerListDAO() {
		
		protected String getNextId(Connection conn) throws SQLException {
			return (String) idGenerator.remove(0);
		}
		
		protected Date getCurrentDate() {
			if (currentDate != null) {
				return currentDate; 
			}
			return new Date();
		}
	};
	
	protected String getSchema() {
		return "CUST";
	}

	protected String[] getAffectedTables() {
		return new String[] {
			"CUST.CUSTOMERLIST",
			"CUST.CUSTOMERLIST_DETAILS",
			"CUST.CUSTOMERLIST_RECIPES",
		};
	}
	
	public void testLoadShoppingListByName() throws Exception {
		this.setUpDataSet("SampleList.xml");

		String customerPk = "C1";
		FDIdentity identity = new FDIdentity(customerPk);

		String name = "Test list 1";
		FDCustomerShoppingList list = (FDCustomerShoppingList) dao.load(conn, identity, EnumCustomerListType.SHOPPING_LIST, name);
		assertEquals(list.getName(), name);
		assertEquals(list.getCustomerPk().getId(), customerPk);
		assertEquals(2, list.getLineItems().size());
	}

	public void testGetCustomerCreatedList() throws Exception {
		this.setUpDataSet("CCLList.xml");
		
		String customerPk = "C1";
		FDIdentity identity = new FDIdentity(customerPk);

		FDCustomerCreatedList list = (FDCustomerCreatedList) dao.getCustomerCreatedList(conn, identity, "L3");
		assertEquals(list.getId(), "L3");
		assertEquals(list.getCount(), 0);
		assertEquals(list.getLineItems().size(), 0);
		
		list = (FDCustomerCreatedList) dao.getCustomerCreatedList(conn, identity, "L4");
		assertEquals(list.getId(), "L4");
		assertEquals(list.getCount(), 0);
		assertEquals(list.getLineItems().size(), 0);

		list = (FDCustomerCreatedList) dao.getCustomerCreatedList(conn, identity, "L5");
		assertEquals(list.getId(), "L5");
		assertEquals(list.getCount(), 2);
		assertEquals(list.getLineItems().size(), 2);
	}
	
	public void testLoadRecipeListByName() throws Exception {
		this.setUpDataSet("SampleList.xml");

		String customerPk = "C1";
		FDIdentity identity = new FDIdentity(customerPk);

		String name = "Test recipe list 1";
		FDCustomerRecipeList list = (FDCustomerRecipeList) dao.load(conn, identity, EnumCustomerListType.RECIPE_LIST, name);
		assertEquals(list.getName(), name);
		assertEquals(list.getCustomerPk().getId(), customerPk);
		assertEquals(2, list.getLineItems().size());
	}
	
	public void testShoppingListStore() throws Exception {
		this.setUpDataSet("SampleList.xml");

		String customerPk = "C1";
		FDIdentity identity = new FDIdentity(customerPk);

 		FDCustomerShoppingList list = (FDCustomerShoppingList) dao.load(conn, identity, EnumCustomerListType.SHOPPING_LIST, "Test list 1");

		idGenerator.add("D1");
		idGenerator.add("D2");

		dao.store(conn, list);

		this.assertDataSet("SampleList.xml");
	}

	public void testRecipeListStore() throws Exception {
		this.setUpDataSet("SampleList.xml");

		String customerPk = "C1";
		FDIdentity identity = new FDIdentity(customerPk);

		FDCustomerRecipeList list = (FDCustomerRecipeList) dao.load(conn, identity, EnumCustomerListType.RECIPE_LIST, "Test recipe list 1");

		idGenerator.add("R1");
		idGenerator.add("R2");

		dao.store(conn, list);

		
		this.assertDataSet("SampleList.xml", getIgnoreModificationDateColumnSpec());
	}

	private static Map getIgnoreModificationDateColumnSpec() {
		Set s = new HashSet();
		s.add("MODIFICATION_DATE");
		HashMap m = new HashMap();		
		m.put("CUST.CUSTOMERLIST", s);
		return m;
	}

	private final static Date REC_DATE = new GregorianCalendar(2004, 06, 20, 0, 0, 0).getTime();
	
	public void testStore() throws Exception {
		PrimaryKey customerPk = new PrimaryKey("C1");
		
		List								lineItems;
		Map								configMap;
		FDCustomerProductListLineItem	slLineItem;
		FDCustomerRecipeListLineItem		rlLineItem;
		FDCustomerShoppingList 			shoppingList;
		FDCustomerRecipeList				recipeList;

		// create the shopping list sample data
		lineItems = new ArrayList();
		
        slLineItem = new FDCustomerProductListLineItem("VEG0010950",new FDConfiguration(50, "EA"));
        slLineItem.setRecipeSourceId(null);
        slLineItem.setFrequency(5);
        slLineItem.setFirstPurchase(REC_DATE);
        slLineItem.setLastPurchase(REC_DATE);
        slLineItem.setDeleted(null);
        
        lineItems.add(slLineItem);
        configMap = new HashMap();
        configMap.put("C_SF_MAR", "LHR");
        slLineItem = new FDCustomerProductListLineItem("SEA0064611",new FDConfiguration(1, "H05", configMap));
        slLineItem.setFrequency(2);
        slLineItem.setFirstPurchase(REC_DATE);
        slLineItem.setLastPurchase(REC_DATE);
        slLineItem.setDeleted(null);
        
        lineItems.add(slLineItem);
        
                
		shoppingList = new FDCustomerShoppingList();
		shoppingList.setCustomerPk(customerPk);
		shoppingList.setName("Test list 1");
        shoppingList.setCreateDate(REC_DATE);
		shoppingList.setLineItems(lineItems);
        shoppingList.setModificationDate(REC_DATE);

		idGenerator.add("L1");
		idGenerator.add("D1");
		idGenerator.add("D2");
		
		dao.store(conn, shoppingList);
		assertEquals(shoppingList.getId(), "L1");
		{
			Set s = new HashSet();
			for (Iterator it = shoppingList.getLineItems().iterator(); it.hasNext();) {
				FDCustomerProductListLineItem item = (FDCustomerProductListLineItem)it.next(); 
				s.add(item.getId().intern());
			}
			assertEquals(s.size(), 2);
			assertTrue(s.contains("D1".intern()));
			assertTrue(s.contains("D2".intern()));
		}


		// now create the recipe list sample data
		lineItems = new ArrayList();
		
        rlLineItem = new FDCustomerRecipeListLineItem();
        rlLineItem.setRecipeId("REC_FOO");
        rlLineItem.setFrequency(5);
        rlLineItem.setFirstPurchase(REC_DATE);
        rlLineItem.setLastPurchase(REC_DATE);
        rlLineItem.setDeleted(null);
        
        lineItems.add(rlLineItem);
        
        rlLineItem = new FDCustomerRecipeListLineItem();
        rlLineItem.setRecipeId("REC_BAR");
        rlLineItem.setFrequency(2);
        rlLineItem.setFirstPurchase(REC_DATE);
        rlLineItem.setLastPurchase(REC_DATE);
        rlLineItem.setDeleted(null);
        
        lineItems.add(rlLineItem);
        
        
		recipeList = new FDCustomerRecipeList(customerPk,
										      "Test recipe list 1");
        recipeList.setCreateDate(REC_DATE);
		recipeList.setLineItems(lineItems);
        recipeList.setModificationDate(REC_DATE);

		idGenerator.add("L2");
		idGenerator.add("R1");
		idGenerator.add("R2");
		
		dao.store(conn, recipeList);
		assertEquals(recipeList.getId(), "L2");
		{
			Set s = new HashSet();
			for (Iterator it = recipeList.getLineItems().iterator(); it.hasNext();) {
				FDCustomerRecipeListLineItem item = (FDCustomerRecipeListLineItem)it.next(); 
				s.add(item.getId().intern());
			}
			assertEquals(s.size(), 2);
			assertTrue(s.contains("R1".intern()));
			assertTrue(s.contains("R2".intern()));
		}
		
		
		this.assertDataSet("SampleList.xml");
	}

	Date DEL_DATE = new GregorianCalendar(2004, 06, 29, 0, 0, 0).getTime();
	Date ADD_DATE = new GregorianCalendar(2004, 06, 28, 0, 0, 0).getTime();
	Date MOD_DATE = new GregorianCalendar(2004, 06, 27, 0, 0, 0).getTime();

	
	public void testStoreDateHandling() throws Exception {
		PrimaryKey customerPk = new PrimaryKey("C1"); 
		{
			FDCustomerShoppingList l = new FDCustomerShoppingList();
			l.setCustomerPk(customerPk);
			l.setName("Test Shopping 1");
			l.setCreateDate(null);
			l.setModificationDate(null);
			currentDate = REC_DATE;
			idGenerator.add("L1");
			dao.store(conn, l);
			assertEquals(l.getId(),"L1");
			assertEquals(l.getCreateDate(), REC_DATE);
			assertEquals(l.getModificationDate(), REC_DATE);
		}
		{
			FDCustomerShoppingList l = new FDCustomerShoppingList();
			l.setCustomerPk(customerPk);
			l.setName("Test Shopping 2");
			l.setCreateDate(REC_DATE);
			l.setModificationDate(null);
			currentDate = ADD_DATE;
			idGenerator.add("L2");
			dao.store(conn, l);
			assertEquals(l.getId(),"L2");
			assertEquals(l.getCreateDate(), REC_DATE);
			assertEquals(l.getModificationDate(), REC_DATE);
		}
		{
			FDCustomerShoppingList l = new FDCustomerShoppingList();
			l.setCustomerPk(customerPk);
			l.setName("Test Shopping 3");
			l.setCreateDate(null);
			l.setModificationDate(REC_DATE);
			currentDate = ADD_DATE;
			idGenerator.add("L3");
			dao.store(conn, l);
			assertEquals(l.getId(),"L3");
			assertEquals(l.getCreateDate(), ADD_DATE);
			assertEquals(l.getModificationDate(), ADD_DATE);
		}
	}
	
	
	public void testStoreCustomerCreatedList() throws Exception {
		//PrimaryKey customerPk = new PrimaryKey("C1");
		
		{
			idGenerator.add("L3");
			FDCustomerCreatedList l = new FDCustomerCreatedList();
			l.setCustomerPk(new PrimaryKey("C1"));
			l.setName("Test CCL 1");
			l.setCreateDate(REC_DATE);
			l.setModificationDate(REC_DATE);
			
			dao.store(conn, l);
			assertEquals(l.getId(), "L3");
		}
		
		{
			FDCustomerCreatedList l = new FDCustomerCreatedList();
			l.setCustomerPk(new PrimaryKey("C1"));
			l.setName("Test CCL 2");
			l.setCreateDate(REC_DATE);

			FDCustomerProductListLineItem li = new FDCustomerProductListLineItem("VEG0010950",new FDConfiguration(50, "EA"));
	        li.setFrequency(5);
	        li.setRecipeSourceId(null);
	        li.setFirstPurchase(REC_DATE);
	        li.setLastPurchase(REC_DATE);
	        li.setDeleted(MOD_DATE);
	        
	        List lineItems = new ArrayList();
	        lineItems.add(li);
	        l.setLineItems(lineItems);
			l.setModificationDate(MOD_DATE);

			idGenerator.add("L4");
			idGenerator.add("D3");

			dao.store(conn, l);
			assertEquals(l.getId(), "L4");
			{
				Set s = new HashSet();
				for (Iterator it = l.getLineItems().iterator(); it.hasNext();) {
					FDCustomerProductListLineItem item = (FDCustomerProductListLineItem)it.next(); 
					s.add(item.getId().intern());
				}
				assertEquals(s.size(), 1);
				assertTrue(s.contains("D3".intern()));
			}
		}
		
		{
	        List lineItems = new ArrayList();

			FDCustomerCreatedList l = new FDCustomerCreatedList();
			l.setCustomerPk(new PrimaryKey("C1"));
			l.setName("Test CCL 3");
			l.setCreateDate(REC_DATE);

			FDCustomerProductListLineItem li = new FDCustomerProductListLineItem("VEG0010950",new FDConfiguration(50, "EA"));
	        li.setFrequency(5);
	        li.setFirstPurchase(ADD_DATE);
	        li.setLastPurchase(ADD_DATE);
	        li.setDeleted(null);
	        lineItems.add(li);
	        
	        Map configMap = new HashMap();
	        configMap.put("C_SF_MAR", "LHR");
	        li = new FDCustomerProductListLineItem("SEA0064611",new FDConfiguration(1, "H05", configMap));
	        li.setFrequency(2);
	        li.setFirstPurchase(ADD_DATE);
	        li.setLastPurchase(ADD_DATE);
	        li.setDeleted(null);
	        lineItems.add(li);

	        li = new FDCustomerProductListLineItem("VEG0010951",new FDConfiguration(33, "EA"));
	        li.setFrequency(5);
	        li.setFirstPurchase(ADD_DATE);
	        li.setLastPurchase(ADD_DATE);
	        li.setDeleted(DEL_DATE);
	        lineItems.add(li);

	        l.setLineItems(lineItems);
			l.setModificationDate(DEL_DATE);

			idGenerator.add("L5");
			idGenerator.add("D4");
			idGenerator.add("D5");
			idGenerator.add("D6");
	        dao.store(conn, l);
			assertEquals(l.getId(), "L5");
			{
				Set s = new HashSet();
				for (Iterator it = l.getLineItems().iterator(); it.hasNext();) {
					FDCustomerProductListLineItem item = (FDCustomerProductListLineItem)it.next(); 
					s.add(item.getId().intern());
				}
				assertEquals(s.size(), 3);
				assertTrue(s.contains("D4".intern()));
				assertTrue(s.contains("D5".intern()));
				assertTrue(s.contains("D6".intern()));
			}
		}
		
		this.assertDataSet("CCLList.xml");		
		
		// Check the propagation of the modification date
		{
			Date MOD_DATE = new GregorianCalendar(2004, 07, 28, 0, 0, 0).getTime();
			FDCustomerCreatedList l = dao.getCustomerCreatedList(conn, new FDIdentity("C1"), "L3");
			l.setModificationDate(MOD_DATE);
			dao.store(conn, l); 
			FDCustomerCreatedList l2 = dao.getCustomerCreatedList(conn, new FDIdentity("C1"), "L3");
			assertEquals(MOD_DATE, l2.getModificationDate());
		}
	}
	
	
	public void testGetCustomerCreatedListInfos() throws DatabaseUnitException, SQLException {
		this.setUpDataSet("CCLList.xml");
		
		String customerPk = "C1";
		FDIdentity identity = new FDIdentity(customerPk);
		List  lists = dao.getCustomerCreatedListInfos(conn, identity);
		
		assertEquals(lists.size(), 3);
		
		for (Iterator it = lists.iterator(); it.hasNext();) {
			FDCustomerCreatedListInfo li = (FDCustomerCreatedListInfo) it.next();
			try {
				li.getLineItems();
				fail();
			} catch (UnsupportedOperationException e) {
				// Catch valid exception
			}
			
			if (li.getId().equals("L3")) {
				assertEquals(li.getCreateDate(), li.getModificationDate());
				assertEquals(li.getCount(), 0);
			}

			if (li.getId().equals("L4")) {
				assertFalse(li.getCreateDate().equals(li.getModificationDate()));
				assertEquals(li.getCount(), 0);
			}
						
			if (li.getId().equals("L5")) {
				assertFalse(li.getCreateDate().equals(li.getModificationDate()));
				assertEquals(li.getCount(), 2);
			}
		}
				
		assertEquals(dao.getCustomerCreatedListInfos(conn, new FDIdentity("C2")).size(), 0);

	}
		
	public void testGetCustomerCreatedLists() throws DatabaseUnitException, SQLException {
		this.setUpDataSet("CCLList.xml");
		
		String customerPk = "C1";
		FDIdentity identity = new FDIdentity(customerPk);
		List  lists = dao.getCustomerCreatedLists(conn, identity);
		
		assertEquals(lists.size(), 3);
		
		for (Iterator it = lists.iterator(); it.hasNext();) {
			FDCustomerCreatedList li = (FDCustomerCreatedList) it.next();
					
			
			if (li.getId().equals("L3")) {
				assertEquals(li.getCreateDate(), li.getModificationDate());
				assertEquals(li.getCount(), 0);
				List items = li.getLineItems();
				assertEquals(items.size(), 0);
			}

			if (li.getId().equals("L4")) {
				assertFalse(li.getCreateDate().equals(li.getModificationDate()));
				assertEquals(li.getCount(), 0);
				List items = li.getLineItems();
				assertEquals(items.size(), 0);
			}
						
			if (li.getId().equals("L5")) {
				assertFalse(li.getCreateDate().equals(li.getModificationDate()));
				assertEquals(li.getCount(), 2);
				List items = li.getLineItems();
				assertEquals(items.size(), 2);

				for (Iterator iter = items.iterator(); iter.hasNext();) {
					FDCustomerProductListLineItem item = (FDCustomerProductListLineItem) iter.next();
					if (item.getSkuCode().equals("VEG0010950")) {
						assertEquals(item.getConfiguration(), new FDConfiguration(50, "EA"));
						assertEquals(item.getFrequency(), 5);
						assertEquals(item.getFirstPurchase(), ADD_DATE);
						assertEquals(item.getLastPurchase(), ADD_DATE);
						assertNull(item.getDeleted());						
					} else if (item.getSkuCode().equals("SEA0064611")) {
				        Map configMap = new HashMap();
				        configMap.put("C_SF_MAR", "LHR");
				        assertEquals(item.getConfiguration(), new FDConfiguration(1, "H05", configMap));
				        assertEquals(item.getFrequency(), 2);
				        assertEquals(item.getFirstPurchase(), ADD_DATE);
				        assertEquals(item.getLastPurchase(), ADD_DATE);
				        assertNull(item.getDeleted());						
					}
					else {
						fail();
					}
				}

			}
		}
				
		assertEquals(dao.getCustomerCreatedLists(conn, new FDIdentity("C2")).size(), 0);

	}

	public void testCreateCustomerCreatedList() throws SQLException {
		FDIdentity identity = new FDIdentity("C1");
		currentDate = REC_DATE;
		idGenerator.add("L1");
		dao.createCustomerCreatedList(conn, identity, "Test CCL 1");
		
		FDCustomerCreatedList ccl = dao.getCustomerCreatedList(conn, identity, "L1");
		assertEquals(ccl.getName(), "Test CCL 1");
		assertEquals(ccl.getCount(), 0);
		assertEquals(ccl.getCreateDate(), REC_DATE);
		assertEquals(ccl.getModificationDate(), REC_DATE);
		assertEquals(ccl.getId(), "L1");
		assertEquals(ccl.getCustomerPk().getId(), identity.getErpCustomerPK());
	}

	public void testDeleteCreateCustomerCreatedList() throws SQLException, DatabaseUnitException {
		FDIdentity identity = new FDIdentity("C1");
		setUpDataSet("CCLList.xml");
		assertNotNull(dao.getCustomerCreatedList(conn, identity, "L3"));
		dao.deleteCustomerCreatedList(conn, identity, "Test CCL 1");
		assertNull(dao.getCustomerCreatedList(conn, identity, "L3"));
		
		assertEquals(dao.getCustomerCreatedListInfos(conn, identity).size(), 2);
		dao.deleteCustomerCreatedList(conn, identity, "NonExistent list");		
		assertEquals(dao.getCustomerCreatedListInfos(conn, identity).size(), 2);
	}

	public void testIsCustomerCreatedList() throws SQLException {
		PrimaryKey customerPk = new PrimaryKey("C1");
		FDIdentity identity = new FDIdentity("C1");
		
		FDCustomerRecipeList recipeList = new FDCustomerRecipeList(customerPk,
			      "Test recipe list 1");
		recipeList.setCreateDate(REC_DATE);
		recipeList.setModificationDate(REC_DATE);
		
		idGenerator.add("L1");
		
		dao.store(conn, recipeList);

		idGenerator.add("L2");

		FDCustomerCreatedList ccl = new FDCustomerCreatedList();
		ccl.setCustomerPk(customerPk);
		ccl.setName("Test CCL 1");
		ccl.setCreateDate(REC_DATE);
		ccl.setModificationDate(REC_DATE);
		
		dao.store(conn, ccl);
		
		assertTrue(dao.isCustomerCreatedList(conn, identity, "Test CCL 1"));
		assertFalse(dao.isCustomerCreatedList(conn, identity, "Test recipe list 1"));
		assertFalse(dao.isCustomerCreatedList(conn, identity, "N/A list"));
		
	}

	public void testRenameCustomerCreatedList() throws Exception {
		FDIdentity identity = new FDIdentity("C1");
		setUpDataSet("CCLList.xml");
		assertEquals("Test CCL 1", dao.getCustomerCreatedList(conn, identity, "L3").getName());
		dao.renameCustomerCreatedList(conn, identity, "Test CCL 1", "newlist");
		assertEquals("newlist", dao.getCustomerCreatedList(conn, identity, "L3").getName());		

		try {
			dao.renameCustomerCreatedList(conn, identity, "Test CCL 2", "Test CCL 3");
			fail(); // Should have thrown an exception
		} catch (SQLException e) {
			// Catch expected exception
		}
	}
	

	/**
	 * Try to access CCL list created by other user which is prohibited
	 * 
	 * @throws Exception
	 */
	public void testLoadOtherUsersList() throws Exception {
		this.setUpDataSet("CCLList.xml");
		
		String customerPk = "C2"; // other user
		FDIdentity identity = new FDIdentity(customerPk);

		FDCustomerCreatedList list = (FDCustomerCreatedList) dao.getCustomerCreatedList(conn, identity, "L1");
		assertNull(list);
	}


	/**
	 * Try to remove a list item from a list which is not mine
	 * 
	 * @throws Exception
	 */
	public void testRemoveItemFromOthersList() throws Exception {
		this.setUpDataSet("CCLList.xml");

		String customerPk = "C2"; // other user
		FDIdentity identity = new FDIdentity(customerPk);

		PrimaryKey itemPK = new PrimaryKey("D3");
		
		boolean result = dao.removeItem(conn, identity, itemPK);
		assertFalse(result);
	}


	/**
	 * Try to enquire the name of a list which is not mine
	 * 
	 * @throws Exception
	 */
	public void testGetOtherUsersListName() throws Exception {
		this.setUpDataSet("CCLList.xml");

		FDIdentity identityOrig = new FDIdentity("C1");
		FDIdentity identity = new FDIdentity("C2");

		assertNotNull(dao.getListName(conn, identityOrig, EnumCustomerListType.CC_LIST, "L3"));

		assertNull(dao.getListName(conn, identity, EnumCustomerListType.CC_LIST, "L3"));
		assertNull(dao.getListName(conn, identity, EnumCustomerListType.CC_LIST, "L4"));
		assertNull(dao.getListName(conn, identity, EnumCustomerListType.CC_LIST, "L5"));
	}
}
