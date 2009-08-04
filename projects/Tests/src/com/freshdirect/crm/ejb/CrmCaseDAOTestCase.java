package com.freshdirect.crm.ejb;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.freshdirect.DbTestCaseSupport;
import com.freshdirect.crm.CrmAgentInfo;
import com.freshdirect.crm.CrmCaseAction;
import com.freshdirect.crm.CrmCaseActionType;
import com.freshdirect.crm.CrmCaseModel;
import com.freshdirect.crm.CrmCaseOrigin;
import com.freshdirect.crm.CrmCasePriority;
import com.freshdirect.crm.CrmCaseQueue;
import com.freshdirect.crm.CrmCaseState;
import com.freshdirect.crm.CrmCaseSubject;
import com.freshdirect.crm.CrmCaseTemplate;
import com.freshdirect.crm.CrmDepartment;
import com.freshdirect.crm.CrmQueueInfo;
import com.freshdirect.enums.EnumManager;
import com.freshdirect.enums.TestableEnumManager;
import com.freshdirect.framework.core.PrimaryKey;

public class CrmCaseDAOTestCase extends DbTestCaseSupport {

	private final List idGenerator = new ArrayList();

	private final CrmCaseDAO dao = new CrmCaseDAO() {
		protected String getNextId(Connection conn) throws SQLException {
			return (String) idGenerator.remove(0);
		}
	};

	public CrmCaseDAOTestCase(String name) {
		super(name);
	}

	protected String getSchema() {
		return "CUST";
	}

	protected String[] getAffectedTables() {
		return new String[] {
			"CUST.CASE_ORIGIN",
			"CUST.ROLE",
			"CUST.AGENT",
			"CUST.DEPARTMENT",
			"CUST.CASEACTION_TYPE",
			"CUST.CASE_STATE",
			"CUST.CASE_PRIORITY",
			"CUST.CASE_QUEUE",
			"CUST.CASE_SUBJECT",
			"CUST.CASE",
			"CUST.CASE_DEPARTMENT",
			"CUST.CASEACTION" };
	}

	public void testFindByPrimaryKey() throws Exception {
		this.setUpDataSet("CrmCaseDAO-two.xml");

		PrimaryKey pk = new PrimaryKey("c1");
		PrimaryKey dbPk = dao.findByPrimaryKey(conn, pk);
		assertEquals(pk.getId(), dbPk.getId());
	}

	public void testLoadCaseInfoByTemplate() throws Exception {
		this.setUpDataSet("CrmCaseDAO-two.xml");

		CrmCaseTemplate t = new CrmCaseTemplate();
		Set states = new HashSet();
		states.add(CrmCaseState.getEnum("OPEN"));
		t.setStates(states);

		List l = dao.loadCaseInfoByTemplate(conn, t);
		assertEquals(2, l.size());
		
		CrmCaseModel c0 = findCase(l, "c0");
		CrmCaseModel c1 = findCase(l, "c1");
		assertEquals(DATEFORMAT.parse("2003-06-26 00:00:00.0"), c0.getCreateDate() );
		assertEquals(DATEFORMAT.parse("2003-06-26 00:00:00.0"), c0.getLastModDate() );
		assertEquals(DATEFORMAT.parse("2003-06-27 00:00:00.0"), c1.getCreateDate() );
		assertEquals(DATEFORMAT.parse("2003-06-28 00:00:00.0"), c1.getLastModDate() );

		t.setPriority(CrmCasePriority.getEnum("HI"));
		l = dao.loadCaseInfoByTemplate(conn, t);
		assertEquals(1, l.size());

		t = new CrmCaseTemplate();
		t.setQueue(CrmCaseQueue.getEnum("QA"));
		l = dao.loadCaseInfoByTemplate(conn, t);
		assertEquals(1, l.size());
	}

	private final static CrmCaseModel findCase(List cases, String id) {
		for (Iterator i = cases.iterator(); i.hasNext(); ) {
			CrmCaseModel c = (CrmCaseModel) i.next();
			if (id.equals(c.getPK().getId())) {
				return c;
			}
		}
		return null;
	}
	
	private final static DateFormat DATEFORMAT = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.S");

	public void testCreate() throws Exception {
		this.setUpDataSet("CrmCaseDAO-one.xml");

		CrmCaseModel c1 = new CrmCaseModel();
		c1.setAssignedAgentPK(new PrimaryKey("a1"));
		c1.setOrigin(CrmCaseOrigin.getEnum("MAIL"));
		c1.setState(CrmCaseState.getEnum("OPEN"));
		c1.setPriority(CrmCasePriority.getEnum("LO"));
		c1.setSubject(CrmCaseSubject.getEnum("SA1"));
		c1.setSummary("Case 1 Summary");

		Set depts = new HashSet();
		depts.add(CrmDepartment.getEnum("DA"));
		depts.add(CrmDepartment.getEnum("DB"));
		c1.setDepartments(depts);

		List actions = new ArrayList();
		CrmCaseAction a1 = new CrmCaseAction();
		a1.setType(CrmCaseActionType.getEnum("CREA"));
		a1.setAgentPK(new PrimaryKey("a1"));
		a1.setNote("Created c1");
		a1.setTimestamp(DATEFORMAT.parse("2003-06-27 00:00:00.0"));
		actions.add(a1);
		CrmCaseAction a2 = new CrmCaseAction();
		a2.setType(CrmCaseActionType.getEnum("NOTE"));
		a2.setAgentPK(new PrimaryKey("a1"));
		a2.setNote("Note for c1");
		a2.setTimestamp(DATEFORMAT.parse("2003-06-28 00:00:00.0"));
		actions.add(a2);
		c1.setActions(actions);

		idGenerator.add("c1a0");
		idGenerator.add("c1a1");

		dao.create(conn, new PrimaryKey("c1"), c1);

		this.assertDataSet("CrmCaseDAO-two.xml");
	}

	public void testLoad() throws Exception {
		this.setUpDataSet("CrmCaseDAO-two.xml");

		PrimaryKey pk = new PrimaryKey("c1");
		CrmCaseModel c1 = (CrmCaseModel) dao.load(conn, pk);

		assertEquals(pk.getId(), c1.getPK().getId());

		assertEquals(new PrimaryKey("a1"), c1.getAssignedAgentPK());
		assertEquals(CrmCaseOrigin.getEnum("MAIL"), c1.getOrigin());
		assertEquals(CrmCaseState.getEnum("OPEN"), c1.getState());
		assertEquals(CrmCasePriority.getEnum("LO"), c1.getPriority());
		assertEquals(CrmCaseSubject.getEnum("SA1"), c1.getSubject());

		assertEquals("Case 1 Summary", c1.getSummary());

		assertNull(c1.getCustomerPK());
		assertNull(c1.getSalePK());
		assertNull(c1.getLockedAgentPK());

		assertEquals(2, c1.getDepartments().size());
		assertEquals(2, c1.getActions().size());
	}

	public void testStore() throws Exception {
		this.setUpDataSet("CrmCaseDAO-two.xml");

		PrimaryKey pk = new PrimaryKey("c1");
		CrmCaseModel c1 = (CrmCaseModel) dao.load(conn, pk);

		dao.store(conn, c1);

		this.assertDataSet("CrmCaseDAO-two.xml");
	}

	public void testRemove() throws Exception {
		this.setUpDataSet("CrmCaseDAO-two.xml");

		PrimaryKey pk = new PrimaryKey("c1");
		dao.remove(conn, pk);

		this.assertDataSet("CrmCaseDAO-one.xml");
	}

	public void testGetQueueOverview() throws Exception {
		this.setUpDataSet("CrmCaseDAO-two.xml");

		List l = dao.loadQueueOverview(conn);
		assertEquals(2, l.size());

		CrmQueueInfo i1 = (CrmQueueInfo) l.get(0);
		assertEquals(CrmCaseQueue.getEnum("QA"), i1.getQueue());
		assertEquals(1, i1.getOpen());
		assertEquals(0, i1.getUnassigned());
		assertEquals(DATEFORMAT.parse("2003-06-27 00:00:00.0"), i1.getOldest());

		CrmQueueInfo i2 = (CrmQueueInfo) l.get(1);
		assertEquals(CrmCaseQueue.getEnum("QB"), i2.getQueue());
		assertEquals(1, i2.getOpen());
		assertEquals(0, i2.getUnassigned());
		assertEquals(DATEFORMAT.parse("2003-06-26 00:00:00.0"), i2.getOldest());
	}

	public void testGetCSROverview() throws Exception {
		this.setUpDataSet("CrmCaseDAO-two.xml");

		List l = dao.loadCSROverview(conn);
		assertEquals(1, l.size());
		CrmAgentInfo ai1 = (CrmAgentInfo) l.get(0);
		assertEquals(new PrimaryKey("a1"), ai1.getAgentPK());
		assertEquals(2, ai1.getAssigned());
		assertEquals(2, ai1.getOpen());
		assertEquals(0, ai1.getClosed());
		assertEquals(DATEFORMAT.parse("2003-06-26 00:00:00.0"), ai1.getOldest());
	}

	protected void setUp() throws Exception {
		super.setUp();
		EnumManager.setInstance(new TestableEnumManager(conn));
	}

}
