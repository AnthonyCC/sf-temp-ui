package com.freshdirect.webapp.util;

import junit.framework.TestCase;




public class RobotRecognizerTest extends TestCase {
	
	

	
	private static String moz5ua1ok = "Mozilla/5.0 (compatible; googlebot/2.1; +http://www.google.com/bot.html)";
	private static String moz5ua2ok = "Mozilla/5.0 ( compatible;  googlebot/2.1;  +http://www.google.com/bot.html ) extra";
	private static String moz5ua3no = "Mozilla (compatible; googlebot; +http://www.google.com/bot.html)";



	public void testMoz5GoogRobots() {
		assertTrue(RobotRecognizer.isMoz5Googlebot(moz5ua1ok.toLowerCase()));
		assertTrue(RobotRecognizer.isMoz5Googlebot(moz5ua2ok.toLowerCase()));
		assertFalse(RobotRecognizer.isMoz5Googlebot(moz5ua3no.toLowerCase()));
	}
}
