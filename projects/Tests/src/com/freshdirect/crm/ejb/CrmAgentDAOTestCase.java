package com.freshdirect.crm.ejb;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.ejb.FinderException;

import com.freshdirect.DbTestCaseSupport;
import com.freshdirect.crm.CrmAgentModel;
import com.freshdirect.crm.CrmAgentRole;
import com.freshdirect.crm.CrmCaseQueue;
import com.freshdirect.enum.EnumManager;
import com.freshdirect.enum.TestableEnumManager;
import com.freshdirect.framework.core.PrimaryKey;

/**
 * @author knadeem
 */
public class CrmAgentDAOTestCase extends DbTestCaseSupport {

	private final CrmAgentEntityDAO dao = new CrmAgentEntityDAO();

	public CrmAgentDAOTestCase(String name) {
		super(name);
	}

	protected String getSchema() {
		return "CUST";
	}

	protected String[] getAffectedTables() {
		return new String[] {
			"CUST.ROLE",
			"CUST.AGENT",
			"CUST.CASE_PRIORITY",
			"CUST.CASE_QUEUE",
			"CUST.CASE_SUBJECT",
			"CUST.AGENT_QUEUE" };
	}

	protected void setUp() throws Exception {
		super.setUp();
		EnumManager.setInstance(new TestableEnumManager(conn));
		this.setUpDataSet("CrmAgentDAO-init.xml");
	}

	public void testFindByPrimaryKey() throws Exception {
		PrimaryKey pk = new PrimaryKey("a1");
		PrimaryKey dbPk = dao.findByPrimaryKey(conn, pk);
		assertEquals(pk.getId(), dbPk.getId());
	}

	public void testFindByIdAndPassword() throws Exception {
		try {
			dao.findUserByIdAndPassword(conn, "blah", "blah");
			fail();
		} catch (FinderException e) {
		}

		PrimaryKey pk = dao.findUserByIdAndPassword(conn, "knadeem", "martini");
		assertEquals("a1", pk.getId());

		pk = dao.findUserByIdAndPassword(conn, "rgayle", "blue");
		assertEquals("a2", pk.getId());
	}

	public void testFindAll() throws Exception {
		Collection col = dao.findAll(conn);
		assertEquals(2, col.size());
	}

	public void testCreate() throws Exception {
		CrmAgentModel agent = new CrmAgentModel("mrose", "martini1", "Mike", "Rose", true, CrmAgentRole.getEnum("SUP"));
		List agentQueues = new ArrayList();
		agentQueues.add(CrmCaseQueue.getEnum("QA"));
		agentQueues.add(CrmCaseQueue.getEnum("QB"));
		PrimaryKey pk = new PrimaryKey("a3");
		dao.create(conn, pk, agent);

		PrimaryKey dbPk = dao.findUserByIdAndPassword(conn, "mrose", "martini1");
		assertEquals(pk.getId(), dbPk.getId());
	}

	public void testLoad() throws Exception {
		PrimaryKey pk = new PrimaryKey("a1");
		CrmAgentModel agent = (CrmAgentModel) dao.load(conn, pk);

		assertEquals(pk.getId(), agent.getPK().getId());
		assertEquals("knadeem", agent.getUserId());
		assertEquals("martini", agent.getPassword());
		assertEquals("Kashif", agent.getFirstName());
		assertEquals("Nadeem", agent.getLastName());
		assertEquals(true, agent.isActive());
		assertEquals("SUP", agent.getRole().getCode());
	}

	public void testStore() throws Exception {
		PrimaryKey pk = new PrimaryKey("a1");
		CrmAgentModel agent = new CrmAgentModel(pk);
		agent.setUserId("knadeem");
		agent.setPassword("martini");
		agent.setFirstName("Kashif");
		agent.setLastName("Nadeem");
		agent.setActive(true);
		agent.setRole(CrmAgentRole.getEnum("CSR"));
		List agentQueues = new ArrayList();
		agentQueues.add(CrmCaseQueue.getEnum("QA"));
		agentQueues.add(CrmCaseQueue.getEnum("QB"));
		agent.setAgentQueues(agentQueues);
		dao.store(conn, agent);

		this.assertDataSet("CrmAgentDAO-expected.xml");
	}

	public void testRemove() throws Exception {
		PrimaryKey pk = new PrimaryKey("a1");
		dao.remove(conn, pk);

		this.assertDataSet("CrmAgentDAO-expectedDelete.xml");
	}

}
