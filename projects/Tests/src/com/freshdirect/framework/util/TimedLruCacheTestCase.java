/*
 * $Workfile: TimedLruCacheTestCase.java$
 *
 * $Date: 2/4/2002 5:03:21 PM$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.framework.util;

import junit.framework.*;

/**
 * Test case for LruCache.
 *
 * @version $Revision: 9$
 * @author $Author: Viktor Szathmary$
 */
public class TimedLruCacheTestCase extends TestCase {

    public static void main(String[] args) {
		junit.textui.TestRunner.run(new TimedLruCacheTestCase("testAll"));
	}
    
	public TimedLruCacheTestCase(String testName) {
		super(testName);
	}
	
	public void testAll() {
		try {
			TimedLruCache cache = new TimedLruCache(5, 1000);
			
			cache.put( new Integer(1), "one" );
			cache.put( new Integer(2), "two" );
			cache.put( new Integer(3), "three" );
			cache.put( new Integer(4), "four" );
			cache.put( new Integer(5), "five" );
			
			assertEquals( "one", cache.get(new Integer(1)) );
			assertEquals( "two", cache.get(new Integer(2)) );
			assertEquals( "three", cache.get(new Integer(3)) );
			assertEquals( "four", cache.get(new Integer(4)) );
			assertEquals( "five", cache.get(new Integer(5)) );
			
			cache.put( new Integer(6), "six" );
			assertEquals( "six", cache.get(new Integer(6)) );
			assertNull( cache.get(new Integer(1)) );
			assertEquals( "two", cache.get(new Integer(2)) );
			assertEquals( "three", cache.get(new Integer(3)) );
			assertEquals( "four", cache.get(new Integer(4)) );
			assertEquals( "five", cache.get(new Integer(5)) );
		
			Thread.sleep(1200);

			assertNull( cache.get(new Integer(1)) );
			assertNull( cache.get(new Integer(2)) );
			assertNull( cache.get(new Integer(3)) );
			assertNull( cache.get(new Integer(4)) );
			assertNull( cache.get(new Integer(5)) );
			assertNull( cache.get(new Integer(6)) );

			
			cache.put(new Integer(10), "ten");
			assertEquals( "ten", cache.get(new Integer(10)) );

			Thread.sleep(600);
			cache.put(new Integer(20), "twenty");
			assertEquals( "ten", cache.get(new Integer(10)) );
			assertEquals( "twenty", cache.get(new Integer(20)) );

			Thread.sleep(600);
			assertNull( cache.get(new Integer(10)) );
			assertEquals( "twenty", cache.get(new Integer(20)) );
			
			// test overwrite/renewal
			cache.put( new Integer(20), "ZWANZIG");
			assertEquals( "ZWANZIG", cache.get(new Integer(20)) );
			Thread.sleep(600);
			assertEquals( "ZWANZIG", cache.get(new Integer(20)) );
			Thread.sleep(600);
			assertNull( cache.get(new Integer(20)) );


		} catch (InterruptedException ex) { fail("InterruptedException"); }

	}
	
}