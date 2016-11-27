package com.freshdirect.crm.ejb;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.freshdirect.DbTestCaseSupport;
import com.freshdirect.crm.CrmCaseSubject;

public class CrmCaseSubjectDAOTestCase extends DbTestCaseSupport {

	public CrmCaseSubjectDAOTestCase(String name) {
		super(name);
	}

	protected String getSchema() {
		return "CUST";
	}

	protected String[] getAffectedTables() {
		return new String[] {"CUST.CASE_PRIORITY", "CUST.CASE_QUEUE", "CUST.CASE_SUBJECT"};
	}

	public void testLoadAll() throws Exception {
		// setup
		this.setUpDataSet("CrmCaseQueueSubjectDAO-init.xml");

		// execute
		CrmCaseSubjectDAO dao = new CrmCaseSubjectDAO();
		List l = dao.loadAll(conn);

		Collections.sort(l, new Comparator() {
			public int compare(Object o1, Object o2) {
				return ((CrmCaseSubject) o1).getCode().compareTo(((CrmCaseSubject) o2).getCode());
			}
		});

		// verify
		assertEquals(3, l.size());

		CrmCaseSubject sa1 = (CrmCaseSubject) l.get(0);
		assertSubject(sa1, "SA1", "Subject A1", "Subject A1 Description", true, "LO");

		CrmCaseSubject sb1 = (CrmCaseSubject) l.get(1);
		assertSubject(sb1, "SB1", "Subject B1", "Subject B1 Description", false, "LO");

		CrmCaseSubject sb2 = (CrmCaseSubject) l.get(2);
		assertSubject(sb2, "SB2", "Subject B2", "Subject B2 Description", false, "HI");
	}

	private void assertSubject(CrmCaseSubject s, String code, String name, String desc, boolean obs, String pri) {
		assertEquals(code, s.getCode());
		assertEquals(name, s.getName());
		assertEquals(desc, s.getDescription());
		assertEquals(obs, s.isObsolete());
		assertEquals(pri, s.getPriorityCode());
	}

}