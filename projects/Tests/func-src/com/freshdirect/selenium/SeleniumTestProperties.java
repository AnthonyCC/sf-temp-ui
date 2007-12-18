package com.freshdirect.selenium;

import java.io.File;

public class SeleniumTestProperties {

	private final static String SELENIUM_TEST_BASE = "selenium.test_base"; 
	private final static String SELENIUM_SERVER_HOST = "selenium.server_host"; 
	private static final String SELENIUM_SERVER_PORT = "selenium.server_port";
	private static final String SELENIUM_BROWSER_STRING = "selenium.browser_string";
	private static final String SELENIUM_SPEED = "selenium.speed";
	private static final String SELENIUM_USERNAME = "selenium.username";
	private static final String SELENIUM_PASSWORD = "selenium.password";
	
	private static final String SELENIUM_TEST_DEFAULT_USERNAME = "selenium@test.com";
	private static final String SELENIUM_TEST_DEFAULT_PASSWORD = "selenium";
	
	private static final boolean THIS_IS_WINDOWS = File.pathSeparator.equals(";");
	
	public static String getSeleniumTestBaseURL() {
		return System.getProperty(SELENIUM_TEST_BASE);
	}

	public static String getSeleniumServerHost() {
		return System.getProperty(SELENIUM_SERVER_HOST, "localhost");
	}

	public static String getSeleniumBrowserString() {
		return System.getProperty(SELENIUM_BROWSER_STRING, getDefaultBrowserString());
	}

	private static String getDefaultBrowserString() {
		return THIS_IS_WINDOWS ? "*iexplore" : "*firefox";
	}

	public static int getSeleniumServerPort() {
		String propVal = System.getProperty(SELENIUM_SERVER_PORT, "4444");
		try {
			return Integer.valueOf(propVal).intValue();
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	public static String getTestUsername() {		
		return System.getProperty(SELENIUM_USERNAME, SELENIUM_TEST_DEFAULT_USERNAME);
	}

	public static String getTestPassword() {
		return System.getProperty(SELENIUM_PASSWORD, SELENIUM_TEST_DEFAULT_PASSWORD);
	}

	public static String getSeleniumSpeed() {
		return System.getProperty(SELENIUM_SPEED, "0");
	}


}
