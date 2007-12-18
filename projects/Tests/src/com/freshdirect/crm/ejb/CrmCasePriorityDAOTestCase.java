package com.freshdirect.crm.ejb;

import java.sql.SQLException;
import java.util.List;

import org.dbunit.DatabaseUnitException;

import com.freshdirect.DbTestCaseSupport;
import com.freshdirect.crm.CrmCasePriority;

public class CrmCasePriorityDAOTestCase extends DbTestCaseSupport {

	public CrmCasePriorityDAOTestCase(String name) {
		super(name);
	}

	protected String getSchema() {
		return "CUST";
	}

	protected String[] getAffectedTables() {
		return new String[] { "CUST.CASE_PRIORITY" };
	}

	public void testLoadAll() throws SQLException, DatabaseUnitException {
		// setup
		this.setUpDataSet("CrmCasePriorityDAO-init.xml");

		// execute
		CrmCasePriorityDAO dao = new CrmCasePriorityDAO();
		List l = dao.loadAll(conn);

		// verify
		assertEquals(2, l.size());

		CrmCasePriority p1 = (CrmCasePriority) l.get(0);
		assertEquals("LO", p1.getCode());
		assertEquals("Low Pri", p1.getName());
		assertEquals("Low Priority", p1.getDescription());
		assertEquals(0, p1.getPriority());

		CrmCasePriority p2 = (CrmCasePriority) l.get(1);
		assertEquals("HI", p2.getCode());
		assertEquals("High Pri", p2.getName());
		assertEquals("High Priority", p2.getDescription());
		assertEquals(1, p2.getPriority());

	}

}
