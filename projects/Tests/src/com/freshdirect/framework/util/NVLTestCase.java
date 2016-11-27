/*
 * $Workfile: NVLTestCase.java$
 *
 * $Date: 11/8/2001 12:55:31 PM$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.framework.util;

import junit.framework.*;

/**
 * Test case for NVL.
 *
 * @version $Revision: 2$
 * @author $Author: Viktor Szathmary$
 */
public class NVLTestCase extends TestCase {

    public static void main(String[] args) {
		junit.textui.TestRunner.run(new NVLTestCase("testApply"));
	}
    
	public NVLTestCase(String testName) {
		super(testName);
	}
	
	/**
	 * Test the apply methods on NVL.
	 */
	public void testApply() {
		
		String s;
		s = NVL.apply("foo", "bar");
		assertEquals( s, "foo" );
		s = NVL.apply(null, "bar");
		assertEquals( s, "bar" );

		Double d;		
		d = NVL.apply(new Double(4), new Double(5));
		assertEquals( d, new Double(4) );
		d = NVL.apply(null, new Double(9));
		assertEquals( d, new Double(9) );

	}

}
