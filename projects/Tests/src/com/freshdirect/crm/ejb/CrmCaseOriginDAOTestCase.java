package com.freshdirect.crm.ejb;

import java.sql.SQLException;
import java.util.List;

import org.dbunit.DatabaseUnitException;

import com.freshdirect.DbTestCaseSupport;
import com.freshdirect.crm.CrmCaseOrigin;

public class CrmCaseOriginDAOTestCase extends DbTestCaseSupport {

	public CrmCaseOriginDAOTestCase(String name) {
		super(name);
	}

	protected String getSchema() {
		return "CUST";
	}

	protected String[] getAffectedTables() {
		return new String[] { "CUST.CASE_ORIGIN" };
	}

	public void testLoadAll() throws SQLException, DatabaseUnitException {
		// setup
		this.setUpDataSet("CrmCaseOriginDAO-init.xml");

		// execute
		CrmCaseOriginDAO dao = new CrmCaseOriginDAO();
		List l = dao.loadAll(conn);

		// verify
		assertEquals(2, l.size());

		CrmCaseOrigin o1 = (CrmCaseOrigin) l.get(0);
		assertEquals("MAIL", o1.getCode());
		assertEquals("Email", o1.getName());
		assertEquals("Email Origin", o1.getDescription());
		assertEquals(false, o1.isObsolete());

		CrmCaseOrigin o2 = (CrmCaseOrigin) l.get(1);
		assertEquals("SYS", o2.getCode());
		assertEquals("System", o2.getName());
		assertEquals("System Origin", o2.getDescription());
		assertEquals(true, o2.isObsolete());
	}

}
