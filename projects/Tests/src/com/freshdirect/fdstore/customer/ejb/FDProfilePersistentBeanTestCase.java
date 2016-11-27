package com.freshdirect.fdstore.customer.ejb;

import java.util.Map;

import org.dbunit.Assertion;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.SortedTable;

import com.freshdirect.DbTestCaseSupport;
import com.freshdirect.fdstore.customer.ProfileModel;
import com.freshdirect.framework.core.PrimaryKey;

public class FDProfilePersistentBeanTestCase extends DbTestCaseSupport {

	public FDProfilePersistentBeanTestCase(String name) {
		super(name);
	}

	protected String getSchema() {
		return "CUST";
	}

	protected String[] getAffectedTables() {
		return new String[] { "CUST.CUSTOMER", "CUST.FDCUSTOMER", "CUST.PROFILE" };
	}

	public void testAll() throws Exception {
		// setup 
		this.setUpDataSet("InitProfile.xml");
		PrimaryKey pk1 = new PrimaryKey("1");
		FDProfilePersistentBean pb = new FDProfilePersistentBean();
		pb.setParentPK(pk1);

		// execute & verify
		pb.load(conn);
		ProfileModel profile = (ProfileModel) pb.getModel();

		Map map = profile.getAttributes();
		assertEquals(2, map.size());
		assertEquals("fooValue", map.get("foo"));
		assertEquals("barValue", map.get("bar"));
		
		pb.setAttribute("foo", "changedFoo");	
		pb.setAttribute("baz", "bazValue");	
		assertTrue( pb.isModified() );

		pb.store(conn);
	
		IDataSet actualData = this.getActualDataSet();
		IDataSet expectedData = this.loadDataSet("ExpectedProfile.xml");
		
		Assertion.assertEquals(new SortedTable(expectedData.getTable("CUST.PROFILE")), new SortedTable(actualData.getTable("CUST.PROFILE")));

		conn.close();
	}

}
