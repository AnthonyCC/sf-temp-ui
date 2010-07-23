package com.freshdirect.security.ticket.test;

import javax.naming.Context;

import com.freshdirect.DbTestCaseSupport;
import com.freshdirect.TestUtils;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.security.ticket.IllegalTicketUseException;
import com.freshdirect.security.ticket.NoSuchTicketException;
import com.freshdirect.security.ticket.Ticket;
import com.freshdirect.security.ticket.TicketExpiredException;
import com.freshdirect.security.ticket.TicketService;

public class TicketServiceTest extends DbTestCaseSupport {
	public TicketServiceTest(String name) {
		super(name);
	}

	@Override
	protected String[] getAffectedTables() {
		return new String[] { "CUST.SECURITYTICKET" };
	}

	@Override
	protected String getSchema() {
		return "CUST";
	}

    public void setUp() throws Exception {

        Context context = TestUtils.createContext();
        dbUnitSetUp(context);
        dbUnitRegisterPool(context);

        TestUtils.createMockContainer(context);

        TestUtils.createTransaction(context);
    }
	
	public void testTicketLifecycle() throws FDResourceException, IllegalArgumentException {
		Ticket ticket1 = TicketService.getInstance().create("agent1", "user1@email.com", 0);
		assertEquals("agent1", ticket1.getOwner());
		assertEquals("user1@email.com", ticket1.getPurpose());
		assertEquals(true, ticket1.isExpired()); // expired immediately
		assertEquals(false, ticket1.isUsed());
		try {
			TicketService.getInstance().use("4873920174389FDAIOFASY7023JK4", ticket1.getOwner(), ticket1.getPurpose());
			fail("should throw NoSuchTicketException");
		} catch (IllegalTicketUseException e) {
			e.printStackTrace();
			fail("unexpected exception");
		} catch (TicketExpiredException e) {
			e.printStackTrace();
			fail("unexpected exception");
		} catch (NoSuchTicketException e) {
		}
		try {
			TicketService.getInstance().use(ticket1.getKey(), ticket1.getOwner(), ticket1.getPurpose());
			fail("should throw TicketExpiredException");
		} catch (IllegalTicketUseException e) {
			e.printStackTrace();
			fail("unexpected exception");
		} catch (TicketExpiredException e) {
		} catch (NoSuchTicketException e) {
			e.printStackTrace();
			fail("unexpected exception");
		}
		Ticket ticket2 = TicketService.getInstance().create("agent1", "user1@email.com", 5);
		assertFalse(ticket2.getKey().equals(ticket1.getKey()));
		try {
			TicketService.getInstance().use(ticket2.getKey(), "agent2", ticket2.getPurpose());
			fail("should throw IllegalTicketUseException");
		} catch (IllegalTicketUseException e) {
			assertEquals("user does not match with owner", e.getMessage());
		} catch (TicketExpiredException e) {
			e.printStackTrace();
			fail("unexpected exception");
		} catch (NoSuchTicketException e) {
			e.printStackTrace();
			fail("unexpected exception");
		}
		try {
			TicketService.getInstance().use(ticket2.getKey(), ticket2.getOwner(), "user2@email.com");
			fail("should throw IllegalTicketUseException");
		} catch (IllegalTicketUseException e) {
			assertEquals("purpose does not match", e.getMessage());
		} catch (TicketExpiredException e) {
			e.printStackTrace();
			fail("unexpected exception");
		} catch (NoSuchTicketException e) {
			e.printStackTrace();
			fail("unexpected exception");
		}
		try {
			ticket2 = TicketService.getInstance().use(ticket2.getKey(), ticket2.getOwner(), ticket2.getPurpose());
		} catch (IllegalTicketUseException e) {
			e.printStackTrace();
			fail("unexpected exception");
		} catch (TicketExpiredException e) {
			e.printStackTrace();
			fail("unexpected exception");
		} catch (NoSuchTicketException e) {
			e.printStackTrace();
			fail("unexpected exception");
		}
		try {
			ticket2 = TicketService.getInstance().use(ticket2.getKey(), ticket2.getOwner(), ticket2.getPurpose());
			fail("should throw IllegalTicketUseException");
		} catch (IllegalTicketUseException e) {
			assertEquals("already used", e.getMessage());
		} catch (TicketExpiredException e) {
			e.printStackTrace();
			fail("unexpected exception");
		} catch (NoSuchTicketException e) {
			e.printStackTrace();
			fail("unexpected exception");
		}
		try {
			Ticket ticket3 = TicketService.getInstance().reload(ticket2);
			assertEquals(ticket3, ticket2);
		} catch (NoSuchTicketException e) {
			e.printStackTrace();
			fail("unexpected exception");
		}
	}
}
