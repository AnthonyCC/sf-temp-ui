package com.freshdirect.delivery.ejb;

import java.util.Date;

import com.clarkware.junitperf.ConstantTimer;
import com.clarkware.junitperf.LoadTest;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class DlvZipCheckLoadTestCase extends TestCase {
	
	public DlvZipCheckLoadTestCase(java.lang.String testName) {
		super(testName);
	}

	public static void main(java.lang.String[] args) {
		Date start = new Date();
		junit.textui.TestRunner.run(suite());
		Date end = new Date();
		System.out.println("Ran from " + start + " to " + end);
	}

	public static Test suite() {
    
		TestSuite suite = new TestSuite();
    
		com.clarkware.junitperf.Timer timer = new ConstantTimer(1000);
		int maxUsers = 10;
		int iterations = 20;
    
		Test testCase = new DlvZipCheckTestCase("testZipCheck");
		Test loadTest = new LoadTest(testCase, maxUsers, iterations, timer);
		suite.addTest(loadTest);
    
		return suite;
    
	}

}
