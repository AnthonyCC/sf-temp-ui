package com.freshdirect.framework.state;

import java.util.HashSet;
import java.util.Set;

import junit.framework.TestCase;

public class StateGraphTest extends TestCase {
	public void testSimple() {
		Set<String> states = new HashSet<String>();
		Set<String> t;
		states.add("INIT");
		states.add("T");

		t = new HashSet<String>();
		t.add("T");
		
		StateGraph<String> sg = createGraphAndPerformBasicTest(states, t, new String[]{"INIT", "T"}, "INIT", true);
		
		Set<String> subsq = new HashSet<String>(1);
		subsq.add("T");
		
		assertEquals(subsq, sg.getSubsequentStates());
	}



	public void testBadGraphs() {
		Set<String> states;
		Set<String> t;


		states = new HashSet<String>();
		states.add("INIT");
		states.add("T");
		t = new HashSet<String>();
		t.add("INIT");
		t.add("T");

		StateGraph<String> sg = null;

		// no edges
		try {
			sg = createGraphAndPerformBasicTest(states, t , new String[]{}, "INIT", false);
		} catch (IllegalArgumentException exc) {
			exc.printStackTrace();
		}
		assertNull(sg);
		
		// INIT is member of terminal labels
		// FAILURE: no label can direct from terminal label
		try {
			sg = createGraphAndPerformBasicTest(states, t , new String[]{"INIT", "T"}, "INIT", false);
		} catch (IllegalArgumentException exc) {
			exc.printStackTrace();
		}
		assertNull(sg);

		
		// {INIT}->{A}, {B}->{T}
		// FAILURE: A is not connected to B
		states = new HashSet<String>();
		states.add("INIT");
		states.add("A");
		states.add("B");
		t = new HashSet<String>();
		t.add("T");
		states.addAll(t);
		
		try {
			sg = createGraphAndPerformBasicTest(states, t , new String[]{"INIT", "A", null, "B", "T"}, "INIT", false);
		} catch (IllegalArgumentException exc) {
			exc.printStackTrace();
		}
		assertNull(sg);

		// FAILURE: {T}->{T} edge is invalid!
		try {
			sg = createGraphAndPerformBasicTest(states, t , new String[]{"INIT", "A", null, "A", "B", null, "B", "T", null, "T", "T"}, "INIT", false);
		} catch (IllegalArgumentException exc) {
			exc.printStackTrace();
		}
		assertNull(sg);
	}


	public void testStateTraversal() {
	}
	
	

	// States: {INIT}->{A}->{T}
	public void testTraversal() {
		Set<String> states = new HashSet<String>();
		Set<String> t = new HashSet<String>();
		states.add("INIT");
		states.add("A");

		t.add("T"); // terminal set
		states.addAll(t);
		
		StateGraph<String> sg = createGraphAndPerformBasicTest(states, t, new String[]{"INIT", "A", null, "A", "T"}, "INIT", true);
		
		Set<String> subsq = new HashSet<String>(1);
		subsq.add("A");
		
		assertEquals(subsq, sg.getSubsequentStates());
		
		// step ahead
		sg.setNextState("A");

		assertEquals("A", sg.getState());
		assertFalse(sg.isTerminal());

		try {
			sg.setNextState("B");
		} catch (IllegalArgumentException exc) {
			// must fail since "B" is not member of state labels
		}
		assertEquals("A", sg.getState());

		// step ahead
		sg.setNextState("T");
		assertEquals("T", sg.getState());
		assertTrue(sg.isTerminal());

	}


	/**
	 * @param states
	 * @param t
	 * @param e
	 * @param failIfNull Check if graph is created otherwise it may return null in case it fails
	 * @return
	 */
	protected StateGraph<String> createGraphAndPerformBasicTest(
			Set<String> states, Set<String> t, String[] e, String start, boolean failIfNull) {
		
		StateGraph<String> sg  = null;
		
		try {
			sg = new StateGraph<String>(states, StateGraph.toEdgesMap(e), start, t);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		if (failIfNull)
			assertNotNull(sg);
		if (sg == null) {
			return null;
		}
		
		
		assertEquals(start, sg.getStart()); // check initial state
		assertEquals(t, sg.getTerminalStates());
		assertFalse(sg.isTerminal());

		return sg;
	}
}
