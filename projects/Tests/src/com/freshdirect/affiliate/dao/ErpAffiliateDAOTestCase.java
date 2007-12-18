package com.freshdirect.affiliate.dao;

import java.sql.SQLException;
import java.util.List;

import org.dbunit.DatabaseUnitException;

import com.freshdirect.DbTestCaseSupport;
import com.freshdirect.affiliate.ErpAffiliate;

public class ErpAffiliateDAOTestCase extends DbTestCaseSupport {

	public ErpAffiliateDAOTestCase(String name) {
		super(name);
	}

	protected String getSchema() {
		return "CUST";
	}

	protected String[] getAffectedTables() {
		return new String[] { "CUST.AFFILIATE" };
	}

	public void testLoadAll() throws SQLException, DatabaseUnitException {
		// setup
		this.setUpDataSet("ErpAffiliateDAO-init.xml");

		// execute
		ErpAffiliateDAO dao = new ErpAffiliateDAO();
		List l = dao.loadAll(conn);

		// verify
		assertEquals(2, l.size());

		ErpAffiliate p1 = (ErpAffiliate) l.get(0);
		assertEquals("FD", p1.getCode());
		assertEquals("FreshDirect", p1.getName());
		assertEquals("FreshDirect Inc", p1.getDescription());
		assertEquals("ZT01", p1.getTaxConditionType());
		assertEquals("ZBD1", p1.getDepositConditionType());

		ErpAffiliate p2 = (ErpAffiliate) l.get(1);
		assertEquals("WBL", p2.getCode());
		assertEquals("WBL", p2.getName());
		assertEquals("WBL Inc", p2.getDescription());
		assertEquals("ZT02", p2.getTaxConditionType());
		assertEquals("ZBD2", p2.getDepositConditionType());

	}

}
