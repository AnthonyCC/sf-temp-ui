package com.freshdirect.framework.util;

import junit.framework.TestCase;

public class MathUtilTest extends TestCase {

	public MathUtilTest(String arg0) {
		super(arg0);
	}

	public void testRoundDecimal() {
		assertRoundDecimal(0, 0);
		assertRoundDecimal(0, 0.001);
		assertRoundDecimal(0.01, 0.01);
		assertRoundDecimal(0.02, 0.015);
		assertRoundDecimal(-0.01, -0.015);
	}

	private void assertRoundDecimal(double expected, double value) {
		assertEquals(expected, MathUtil.roundDecimal(value), 0.000000001);
	}

}
