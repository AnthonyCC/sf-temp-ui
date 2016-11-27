package com.freshdirect.crm.ejb;

import java.util.List;

import com.freshdirect.DbTestCaseSupport;
import com.freshdirect.crm.CrmCaseQueue;

public class CrmCaseQueueDAOTestCase extends DbTestCaseSupport {

	public CrmCaseQueueDAOTestCase(String name) {
		super(name);
	}

	protected String getSchema() {
		return "CUST";
	}

	protected String[] getAffectedTables() {
		return new String[] { "CUST.CASE_PRIORITY", "CUST.CASE_QUEUE", "CUST.CASE_SUBJECT" };
	}

	public void testLoadAll() throws Exception {
		// setup
		this.setUpDataSet("CrmCaseQueueSubjectDAO-init.xml");

		// execute
		CrmCaseQueueDAO dao = new CrmCaseQueueDAO();
		List l = dao.loadAll(conn);

		// verify
		assertEquals(2, l.size());

		CrmCaseQueue qa = (CrmCaseQueue) l.get(0);
		assertQueue(qa, "QA", "Queue A", "Queue A Description", true);

		CrmCaseQueue qb = (CrmCaseQueue) l.get(1);
		assertQueue(qb, "QB", "Queue B", "Queue B Description", false);

	}

	private void assertQueue(CrmCaseQueue q, String code, String name, String desc, boolean obs) {
		assertEquals(code, q.getCode());
		assertEquals(name, q.getName());
		assertEquals(desc, q.getDescription());
		assertEquals(obs, q.isObsolete());
	}

}
