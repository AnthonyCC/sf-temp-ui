package com.freshdirect.pci;

import junit.extensions.RepeatedTest;
import junit.framework.Test;
import junit.framework.TestResult;
import junit.framework.TestSuite;

import com.clarkware.junitperf.ConstantTimer;
import com.clarkware.junitperf.LoadTest;
import com.clarkware.junitperf.Timer;

/**
 * The <code>FDEventLoadTest</code> demonstrates how to 
 * decorate a <code>Test</code> as a <code>LoadTest</code>.
 *
 * @author <b>Sairam Krishnasamy</b>
 *
 * @see com.clarkware.junitperf.LoadTest
 * @see com.clarkware.junitperf.TimedTest
 */

public class FDPaymentMethodLoadTest {

	public static final long toleranceInMillis = 500;

	public static Test suite() throws Exception{
		TestSuite suite = new TestSuite();
		//suite.addTest(makeFDAddPaymentLoadTest());
		//suite.addTest(makeFDEditPaymentLoadTest());
		//suite.addTest(makeFDSelectPaymentLoadTest());		
		suite.addTest(makeFDDeletePaymentLoadTest());

		return suite;
	}

	/**
	 * Decorates a stateful test as a 10 user load test,
	 * providing each user with a different test instance
	 * to ensure thread safety.
	 * 	
	 * @return Test.
	 */ 
	protected static Test makeFDAddPaymentLoadTest() throws Exception{
        //Set 2 seconds delay between start of each user.
        Timer timer = new ConstantTimer(toleranceInMillis);
		int users = 20;
		int iterations = 500;
		
		Test testCase = new FDAddPaymentTestCase("testAddPaymentMethod");
		Test repeatedTest = new RepeatedTest(testCase, iterations);
		Test loadTest = new LoadTest(repeatedTest, users, timer);
		return loadTest;	
	}

	protected static Test makeFDEditPaymentLoadTest() throws Exception{
        //Set 2 seconds delay between start of each user.
        Timer timer = new ConstantTimer(toleranceInMillis);		
		int users = 20;
		int iterations = 500;
		Test testCase = new FDEditPaymentTestCase("testEditPaymentMethod");
		Test repeatedTest = new RepeatedTest(testCase, iterations);
		Test loadTest = new LoadTest(repeatedTest, users, timer);
		return loadTest;
	}
	
	protected static Test makeFDDeletePaymentLoadTest() throws Exception{
        //Set 2 seconds delay between start of each user.
        Timer timer = new ConstantTimer(toleranceInMillis);				
		int users = 20;
		int iterations = 500;
		Test testCase = new FDDeletePaymentTestCase("testDeletePaymentMethod");
		Test repeatedTest = new RepeatedTest(testCase, iterations);
		Test loadTest = new LoadTest(repeatedTest, users, timer);
		return loadTest;
	}

	protected static Test makeFDSelectPaymentLoadTest() throws Exception{
        //Set 2 seconds delay between start of each user.
        Timer timer = new ConstantTimer(toleranceInMillis);		
        
		int users = 20;
		int iterations = 500;
		Test testCase = new FDSelectPaymentTestCase("testSelectPaymentMethod");
		Test repeatedTest = new RepeatedTest(testCase, iterations);
		Test loadTest = new LoadTest(repeatedTest, users, timer);
		return loadTest;
	}
	
	public static void main(String args[]) throws Exception{
			TestResult results = junit.textui.TestRunner.run(suite());
			System.out.println();
			System.out.println("Test Results WasSucessful "+results.wasSuccessful());
			System.out.println();
		
	}
}
