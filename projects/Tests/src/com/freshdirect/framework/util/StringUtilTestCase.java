package com.freshdirect.framework.util;

import junit.framework.TestCase;

public class StringUtilTestCase extends TestCase {

	public StringUtilTestCase(String name) {
		super(name);
	}

	public void testLeftPad() {
		
		String strToPad = null;
		String paddedString = null;
		
		// test 1: string to pad has a length less than length to pad to
		strToPad = "1";
		paddedString = StringUtil.leftPad(strToPad, 4, '0');
		assertEquals(paddedString, "0001");

		// test 2: string to pad has a length equal to length to pad to
		strToPad = "1234";
		paddedString = StringUtil.leftPad(strToPad, 4, '0');
		assertEquals(paddedString, "1234");

		// test 3: string to pad has a length greater than length to pad to
		strToPad = "123456";
		paddedString = StringUtil.leftPad(strToPad, 4, '0');
		assertEquals(paddedString, "123456");
		
		// test 4: string to pad is null
		strToPad = null;
		paddedString = StringUtil.leftPad(strToPad, 4, '0');
		assertNull(paddedString);
		
		// test 5: string to pad is an empty string
		strToPad = "";
		paddedString = StringUtil.leftPad(strToPad, 4, '0');
		assertEquals(paddedString, "0000");

	}

	public void testRightPad() {

		String strToPad = null;
		String paddedString = null;
		
		// test 1: string to pad has a length less than length to pad to
		strToPad = "1";
		paddedString = StringUtil.rightPad(strToPad, 4, '0');
		assertEquals(paddedString, "1000");

		// test 2: string to pad has a length equal to length to pad to
		strToPad = "1234";
		paddedString = StringUtil.rightPad(strToPad, 4, '0');
		assertEquals(paddedString, "1234");

		// test 3: string to pad has a length greater than length to pad to
		strToPad = "123456";
		paddedString = StringUtil.rightPad(strToPad, 4, '0');
		assertEquals(paddedString, "123456");

		// test 4: string to pad is null
		strToPad = null;
		paddedString = StringUtil.rightPad(strToPad, 4, '0');
		assertNull(paddedString);
		
		// test 5: string to pad is an empty string
		strToPad = "";
		paddedString = StringUtil.rightPad(strToPad, 4, '0');
		assertEquals(paddedString, "0000");
	}

	public void testSmartCapitalize() {
		assertEquals("", StringUtil.smartCapitalize(null));
		assertEquals("", StringUtil.smartCapitalize(""));
		assertEquals("A", StringUtil.smartCapitalize("a"));
		assertEquals("Hello", StringUtil.smartCapitalize("hello"));
		assertEquals("Hello World", StringUtil.smartCapitalize("helloWorld"));
		assertEquals("Hello World", StringUtil.smartCapitalize("HelloWorld"));
		assertEquals("Hello World", StringUtil.smartCapitalize("Hello_World"));
		assertEquals("Hello World", StringUtil.smartCapitalize("HELLO_WORLD"));
	}
	
	public void testUriEscape() {
		String escaped1 = "http%3A%2F%2Fwww.google.com%2Fsearch%3Fsource%3Dig%26hl%3Den%26q%3Dkakao%26btnG%3DGoogle%2BSearch";
		assertEquals(StringUtil.escapeUri("http://www.google.com/search?source=ig&hl=en&q=kakao&btnG=Google+Search"),escaped1);
		
		String escaped2 = "%5B%09%5D%5B%20%5D";
		assertEquals(StringUtil.escapeUri("[\t][ ]"),escaped2);
		
		StringBuffer buff = new StringBuffer();

		StringUtil.appendUriEscaped(" ",StringUtil.appendUriEscaped("//", buff));
		
		assertEquals("%2F%2F%20",buff.toString());
	}
}
