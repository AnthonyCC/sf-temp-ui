package com.freshdirect.crm.ejb;

import java.sql.SQLException;
import java.util.List;

import org.dbunit.DatabaseUnitException;

import com.freshdirect.DbTestCaseSupport;
import com.freshdirect.crm.CrmCaseActionType;

public class CrmCaseActionTypeDAOTestCase extends DbTestCaseSupport {

	public CrmCaseActionTypeDAOTestCase(String name) {
		super(name);
	}

	protected String getSchema() {
		return "CUST";
	}

	protected String[] getAffectedTables() {
		return new String[] { "CUST.CASEACTION_TYPE" };
	}

	public void testLoadAll() throws SQLException, DatabaseUnitException {
		// setup
		this.setUpDataSet("CrmCaseActionTypeDAO-init.xml");

		// execute
		CrmCaseActionTypeDAO dao = new CrmCaseActionTypeDAO();
		List l = dao.loadAll(conn);

		// verify
		assertEquals(2, l.size());

		CrmCaseActionType cat1 = (CrmCaseActionType) l.get(0);
		assertEquals("CREA", cat1.getCode());
		assertEquals("Create", cat1.getName());
		assertEquals("Create Description", cat1.getDescription());

		CrmCaseActionType cat2 = (CrmCaseActionType) l.get(1);
		assertEquals("NOTE", cat2.getCode());
		assertEquals("Note", cat2.getName());
		assertEquals("Note Description", cat2.getDescription());

	}

}
