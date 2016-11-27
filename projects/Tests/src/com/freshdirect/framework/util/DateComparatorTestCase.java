/*
 * $Workfile: DateComparatorTestCase.java$
 *
 * $Date: 9/11/2001 2:57:45 PM$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.framework.util;

import java.util.*;
import java.text.*;
import junit.framework.*;

/**
 * Test case for DateComparator.
 *
 * @version $Revision: 11$
 * @author $Author: Viktor Szathmary$
 */
public class DateComparatorTestCase extends TestCase {

    public static void main(String[] args) {
		junit.textui.TestRunner.run(new DateComparatorTestCase("testSeconds"));
	}
    
	public DateComparatorTestCase(String testName) {
		super(testName);
	}
	
	/**
	 * Test compare() with PRECISION_SECOND.
	 */
	public void testSeconds() {
		DateComparator dc;
		Date d1, d2;
		long currTime = System.currentTimeMillis();

		dc = new DateComparator( DateComparator.PRECISION_SECOND );
		d1 = new Date(currTime);
		d2 = new Date(currTime+800);
		assertTrue( dc.compare(d1,d2)==0 );
		assertTrue( dc.compare(d2,d1)==0 );
		
		d2 = new Date(currTime-800);
		assertTrue( dc.compare(d1,d2)==0 );
		assertTrue( dc.compare(d2,d1)==0 );
		
		d2 = new Date(currTime+1200);
		assertTrue( d1.before(d2) );	// test the test case :)
		assertTrue( dc.compare(d1,d2)<0 );
		assertTrue( dc.compare(d2,d1)>0 );
		
		d2 = new Date(currTime-1200);
		assertTrue( d1.after(d2) );		// test the test case :)
		assertTrue( dc.compare(d1,d2)>0 );
		assertTrue( dc.compare(d2,d1)<0 );
	}

	public void testDays() {
		Calendar epoch = Calendar.getInstance();
		epoch.setTime( new Date(0) );
		Calendar roller = Calendar.getInstance();
		roller.setTime( new Date(0) );

		assertEquals( epoch, roller );		// self-test
		
		DateComparator dc = new DateComparator( DateComparator.PRECISION_DAY );
		
		roller.add(12, Calendar.HOUR_OF_DAY);
		assertTrue( dc.compare( epoch.getTime(), roller.getTime() ) == 0 );

		roller.add(11, Calendar.HOUR_OF_DAY);
		assertTrue( dc.compare( epoch.getTime(), roller.getTime() ) == 0 );

		roller.add(4, Calendar.HOUR_OF_DAY);		
		assertTrue( dc.compare( epoch.getTime(), roller.getTime() ) < 0 );
	}

	public void testExtremes() throws ParseException {
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd HHmmss");
		Date d1 = df.parse("20010101 000000");
		Date d2 = df.parse("20010101 235959");
		Date d3 = df.parse("20010102 000000");

		DateComparator dc = new DateComparator( DateComparator.PRECISION_DAY );

		assertTrue( dc.compare( d1, d2 ) == 0 );
		assertTrue( dc.compare( d1, d3 ) < 0 );
		assertTrue( dc.compare( d2, d3 ) == 0 );
		assertTrue( dc.compare( d3, d2 ) == 0 );
	}

}
