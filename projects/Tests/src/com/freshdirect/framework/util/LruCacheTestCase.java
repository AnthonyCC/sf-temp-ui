/*
 * $Workfile: LruCacheTestCase.java$
 *
 * $Date: 2/11/2002 6:30:11 PM$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.framework.util;

import junit.framework.*;

/**
 * Test case for LruCache.
 *
 * @version $Revision: 12$
 * @author $Author: Viktor Szathmary$
 */
public class LruCacheTestCase extends TestCase {

    public static void main(String[] args) {
		junit.textui.TestRunner.run(new LruCacheTestCase("testAll"));
	}
    
	public LruCacheTestCase(String testName) {
		super(testName);
	}
	
	public void testAll() {
		LruCache cache = new LruCache(5);
		
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
		
		// test LRU behaviour
		cache.put( new Integer(6), "six" );
		assertNull( cache.get(new Integer(1)) );
		assertEquals( "two", cache.get(new Integer(2)) );
		assertEquals( "three", cache.get(new Integer(3)) );
		assertEquals( "four", cache.get(new Integer(4)) );
		assertEquals( "five", cache.get(new Integer(5)) );
		assertEquals( "six", cache.get(new Integer(6)) );

		cache.put( new Integer(7), "seven" );
		assertEquals( "seven", cache.get(new Integer(7)) );
		assertNull( cache.get(new Integer(2)) );

		// test overwriting an existing item
		cache.put( new Integer(7), "SIEBEN" );
		assertEquals( "SIEBEN", cache.get(new Integer(7)) );

		// test reusing an old key
		cache.put( new Integer(2), "ZWO" );
		assertEquals( "ZWO", cache.get(new Integer(2)) );

		// check if all the old stuff is still there
		assertNull( cache.get(new Integer(3)) );
		assertEquals( "four", cache.get(new Integer(4)) );
		assertEquals( "five", cache.get(new Integer(5)) );
		assertEquals( "six", cache.get(new Integer(6)) );
		assertEquals( "SIEBEN", cache.get(new Integer(7)) );
		
		// test LRU head-access bug
		cache.put(new Integer(99), "bug");
		assertEquals( "bug", cache.get(new Integer(99)) );
		cache.put(new Integer(100), "more");
		assertEquals( "more", cache.get(new Integer(100)) );
		
	}
	
}