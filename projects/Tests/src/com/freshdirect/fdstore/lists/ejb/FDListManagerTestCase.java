package com.freshdirect.fdstore.lists.ejb;

import java.util.Iterator;
import java.util.List;

import com.freshdirect.customer.ErpCustomerInfoModel;
import com.freshdirect.fdstore.customer.FDCustomerManagerTestSupport;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDUser;
import com.freshdirect.fdstore.lists.FDCustomerCreatedList;
import com.freshdirect.fdstore.lists.FDCustomerListExistsException;
import com.freshdirect.fdstore.lists.FDListManager;
import com.freshdirect.framework.core.PrimaryKey;


public class FDListManagerTestCase extends FDCustomerManagerTestSupport {
	
	public FDListManagerTestCase(String name) {
		super(name);
	}
	
	private final FDCustomerListDAO dao = new FDCustomerListDAO();
	
	protected String getSchema() {
		return "CUST";
	}

	protected String[] getAffectedTables() {
		return new String[] {
			"CUST.CUSTOMERLIST",
			"CUST.CUSTOMERLIST_DETAILS",
			// The ordering of the following entries _does_matter_ because of foreign key constraints.
			"CUST.CUSTOMER",
			"CUST.FDCUSTOMER",
			"CUST.PROFILE",
			"CUST.FDUSER",
			"CUST.CUSTOMERINFO",
			"CUST.FDCARTLINE",
			"CUST.CUSTOMERALERT"
		};
	}		

	public void testCreateCustomerCreatedList() throws Exception {
		FDUser user = new FDUser(new PrimaryKey("U1")); 
		FDIdentity identity = new FDIdentity("C1"); 
		user.setIdentity(identity);
		try {
			FDListManager.createCustomerCreatedList(user, "Test1");
		} catch (FDCustomerListExistsException e) {
			fail(e.getMessage());
		}
		assertNotNull(getCCLByName(identity, "Test1"));
		
		try {
			FDListManager.createCustomerCreatedList(user, "Test1");
			fail("Should have thrown FDCustomerListExistsException");
		} catch (FDCustomerListExistsException e) {
			// Great, everything seems to work
		}
	}

	public void testDeleteCustomerCreatedList() throws Exception {
		// Setup user
		setUpDataSet("CCLUser.xml");
		FDUser user = new FDUser(new PrimaryKey("FDC3")); 
		FDIdentity identity = new FDIdentity("C3"); 
		user.setIdentity(identity);
		ErpCustomerInfoModel testInfo = new ErpCustomerInfoModel();
		testInfo.setFirstName("TestFirstName");
		setCustomerInfo(testInfo);
		
		assertEquals(getCCLs(identity).size(), 0);
		// No lists, still succeed, it even creates the default list		
		FDListManager.deleteCustomerCreatedList(user, "Test1");	
		assertNotNull(getCCLByName(identity, "TestFirstName's List"));
		assertEquals(getCCLs(identity).size(), 1);
		
		// We now have the default list, try to delete it.
		FDListManager.deleteCustomerCreatedList(user, "TestFirstName's List");	
		assertNotNull(getCCLByName(identity, "TestFirstName's List"));
		assertEquals(getCCLs(identity).size(), 1);
		
		// Create a list (with DAO) and delete it
		createCCLByName(identity, "Test List");
		assertNotNull(getCCLByName(identity, "Test List"));
		assertEquals(getCCLs(identity).size(), 2);
		FDListManager.deleteCustomerCreatedList(user, "Test List");
		assertNull(getCCLByName(identity, "Test List"));
		assertEquals(getCCLs(identity).size(), 1);
	}

	
	private FDCustomerCreatedList getCCLByName(FDIdentity identity, String name) throws Exception {
		List l = dao.getCustomerCreatedLists(conn, identity);
		for (Iterator it = l.iterator(); it.hasNext();) {
			FDCustomerCreatedList list = (FDCustomerCreatedList) it.next();
			if (list.getName().equals(name)) {
				return list;
			}
		}
		return null;
	}

	private List getCCLs(FDIdentity identity) throws Exception {
		return dao.getCustomerCreatedLists(conn, identity);
	}

	
	private void createCCLByName(FDIdentity identity, String listName) throws Exception {
		dao.createCustomerCreatedList(conn, identity, listName);
	}
}
