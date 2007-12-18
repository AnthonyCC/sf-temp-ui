package com.freshdirect.crm.ejb;

import java.sql.SQLException;
import java.util.List;

import org.dbunit.DatabaseUnitException;

import com.freshdirect.DbTestCaseSupport;
import com.freshdirect.crm.CrmDepartment;

public class CrmDepartmentDAOTestCase extends DbTestCaseSupport {

	public CrmDepartmentDAOTestCase(String name) {
		super(name);
	}

	protected String getSchema() {
		return "CUST";
	}

	protected String[] getAffectedTables() {
		return new String[] { "CUST.DEPARTMENT" };
	}

	public void testLoadAll() throws SQLException, DatabaseUnitException {
		// setup
		this.setUpDataSet("CrmDepartmentDAO-init.xml");

		// execute
		CrmDepartmentDAO dao = new CrmDepartmentDAO();
		List l = dao.loadAll(conn);

		// verify
		assertEquals(2, l.size());

		CrmDepartment da = (CrmDepartment) l.get(0);
		assertEquals("DA", da.getCode());
		assertEquals("Dept A", da.getName());
		assertEquals("Department A", da.getDescription());
		assertTrue(!da.isObsolete());

		CrmDepartment db = (CrmDepartment) l.get(1);
		assertEquals("DB", db.getCode());
		assertEquals("Dept B", db.getName());
		assertEquals("Department B", db.getDescription());
		assertTrue(db.isObsolete());

	}

}
