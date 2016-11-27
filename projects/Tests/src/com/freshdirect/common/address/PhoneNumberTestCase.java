package com.freshdirect.common.address;

import junit.framework.TestCase;

public class PhoneNumberTestCase extends TestCase {

	public PhoneNumberTestCase(String arg0) {
		super(arg0);
	}

	public void testFormat() {
		assertEquals("(123) 456-7890", new PhoneNumber("1234567890").getPhone());
		assertEquals("(123) 456-7890", new PhoneNumber("(123) 456-7890").getPhone());
		assertEquals("(123) 456-7890", new PhoneNumber("123.456.7890").getPhone());
		assertEquals("12837", new PhoneNumber("12837").getPhone());
		assertEquals("12837", new PhoneNumber("12837crap").getPhone());
	}

}
