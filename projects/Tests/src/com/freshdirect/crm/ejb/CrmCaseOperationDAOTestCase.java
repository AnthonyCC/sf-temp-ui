package com.freshdirect.crm.ejb;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import org.dbunit.DatabaseUnitException;

import com.freshdirect.DbTestCaseSupport;
import com.freshdirect.crm.CrmCaseOperation;

public class CrmCaseOperationDAOTestCase extends DbTestCaseSupport {

	public CrmCaseOperationDAOTestCase(String name) {
		super(name);
	}

	protected String getSchema() {
		return "CUST";
	}

	protected String[] getAffectedTables() {
		return new String[] {
			"CUST.ROLE",
			"CUST.CASE_STATE",
			"CUST.CASEACTION_TYPE",
			"CUST.CASE_PRIORITY",
			"CUST.CASE_QUEUE",
			"CUST.CASE_SUBJECT",
			"CUST.CASE_OPERATION" };
	}

	public void testLoadAll() throws SQLException, DatabaseUnitException {
		// setup
		this.setUpDataSet("CrmCaseOperationDAO-init.xml");

		// execute
		CrmCaseOperationDAO dao = new CrmCaseOperationDAO();
		List l = dao.loadAll(conn);

		// verify
		assertEquals(5, l.size());

		assertOperation(l, new CrmCaseOperation("SUP", "SA1", "OPEN", "CLSD", "CLOSE"));
		assertOperation(l, new CrmCaseOperation("SUP", "SA1", "OPEN", "REVW", "ESCRVW"));
		assertOperation(l, new CrmCaseOperation("SUP", "SA1", "OPEN", "OPEN", "NOTE"));
		assertOperation(l, new CrmCaseOperation("SUP", "SA1", "REVW", "REVW", "NOTE"));
		assertOperation(l, new CrmCaseOperation("SUP", "SA1", "REVW", "CLSD", "CLOSE"));
	}

	private void assertOperation(List l, CrmCaseOperation template) {
		for (Iterator i = l.iterator(); i.hasNext();) {
			CrmCaseOperation op = (CrmCaseOperation) i.next();
			if (op.isMatching(template)) {
				return;
			}
		}
		fail();
	}

}
