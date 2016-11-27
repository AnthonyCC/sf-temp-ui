package com.freshdirect.crm;

import java.util.List;
import java.util.ArrayList;

import junit.framework.TestCase;

public class CrmOperationCollectionTestCase extends TestCase {

	public CrmOperationCollectionTestCase(String name) {
		super(name);
	}

	private CrmOperationCollection operations = new CrmOperationCollection();

	private final static int ROLES = 3;
	private final static int SUBJECTS = 10;
	private final static int STATES = 4;

	public void setUp() {
        ArrayList ops = new ArrayList();
		for (int r = 0; r < ROLES; r++) {
			for (int s = 0; s < SUBJECTS; s++) {
				for (int ss = 0; ss < STATES; ss++) {
					for (int es = 0; es < STATES; es++) {
						String role = "role" + r;
						String subject = "subject" + s;
						String startState = "startState" + ss;
						String endState = "endState" + es;
						String actionType = "actionType";
						ops.add(new CrmCaseOperation(role, subject, startState, endState, actionType));
					}
				}
			}
		}
        operations.setOperations(ops);
	}

	public void testMatching() {
		List matches;

		matches = operations.getOperations(new CrmCaseOperation((String) null, null, null, null, null));
		assertEquals(ROLES * SUBJECTS * STATES * STATES, matches.size());

		matches = operations.getOperations(new CrmCaseOperation("role0", null, null, null, null));
		assertEquals(SUBJECTS * STATES * STATES, matches.size());

		matches = operations.getOperations(new CrmCaseOperation(null, "subject0", null, null, null));
		assertEquals(ROLES * STATES * STATES, matches.size());

		matches = operations.getOperations(new CrmCaseOperation(null, null, "startState0", null, null));
		assertEquals(ROLES * SUBJECTS * STATES, matches.size());

		matches = operations.getOperations(new CrmCaseOperation(null, null, null, "endState0", null));
		assertEquals(ROLES * SUBJECTS * STATES, matches.size());

		matches = operations.getOperations(new CrmCaseOperation("role0", "subject0", "startState0", null, null));
		assertEquals(STATES, matches.size());

	}

}
