/*
 * $Workfile: QuickDateFormatTestCase.java$
 *
 * $Date: 9/10/2001 6:06:23 PM$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.framework.util;

import java.util.*;
import java.text.*;
import junit.framework.*;

/**
 * Test case for QuickDateFormat.
 *
 * @version $Revision: 5$
 * @author $Author: Viktor Szathmary$
 */
public class QuickDateFormatTestCase extends TestCase {

    public static void main(String[] args) {
		junit.textui.TestRunner.run(new QuickDateFormatTestCase("testFormatTime"));
	}
    
	public QuickDateFormatTestCase(String testName) {
		super(testName);
	}
	

	public void testFormatDate() throws ParseException {
		Date d = new Date();
		QuickDateFormat qdf = new QuickDateFormat( QuickDateFormat.FORMAT_DATE );
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		assertEquals( sdf.format(d), qdf.format(d) );
	}

	public void testFormatShortDate() throws ParseException {
		Date d = new Date();
		QuickDateFormat qdf = new QuickDateFormat( QuickDateFormat.FORMAT_SHORT_DATE );
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		assertEquals( sdf.format(d), qdf.format(d) );
	}

	public void testFormatTime() throws ParseException {
		Date d = new Date();
		QuickDateFormat qdf = new QuickDateFormat( QuickDateFormat.FORMAT_TIME );
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		assertEquals( sdf.format(d), qdf.format(d) );
	}

	public void testFormatShortTime() throws ParseException {
		Date d = new Date();
		QuickDateFormat qdf = new QuickDateFormat( QuickDateFormat.FORMAT_SHORT_TIME );
		SimpleDateFormat sdf = new SimpleDateFormat("HHmmss");
		assertEquals( sdf.format(d), qdf.format(d) );
	}

	public void testFormatIso() throws ParseException {
		Date d = new Date();
		QuickDateFormat qdf = new QuickDateFormat( QuickDateFormat.FORMAT_ISO );
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS");
		assertEquals( sdf.format(d), qdf.format(d) );
	}

}
