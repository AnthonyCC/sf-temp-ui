package com.freshdirect.fdstore.promotion;

import junit.framework.TestCase;

public class ExclusionSetTestCase extends TestCase {

	public ExclusionSetTestCase(String arg0) {
		super(arg0);
	}

	public void testExclusions() {
		ExclusionSet set = new ExclusionSet(false);
		set.exclude("1");
		set.exclude("2");

		assertFalse(set.isAllowed("0"));
		assertTrue(set.isAllowed("1"));
		assertTrue(set.isAllowed("2"));
		assertFalse(set.isAllowed("3"));

		set.setAllowAll(true);

		assertTrue(set.isAllowed("0"));
		assertFalse(set.isAllowed("1"));
		assertFalse(set.isAllowed("2"));
		assertTrue(set.isAllowed("3"));
	}

}
