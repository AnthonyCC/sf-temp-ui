
/*
 * RegisterCustomerLoadTest.java
 * JUnit based test
 *
 * Created on May 15, 2002, 7:58 PM
 */

package com.freshdirect.load;

import java.util.Date;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.clarkware.junitperf.ConstantTimer;
import com.clarkware.junitperf.LoadTest;
import com.clarkware.junitperf.Timer;

/**
 *
 * @author mrose
 */
public class ResubmitCustomerLoadTest extends TestCase {
    
    public ResubmitCustomerLoadTest(java.lang.String testName) {
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
        
        Timer timer = new ConstantTimer(1000);
        int maxUsers = 5;
        int iterations = 5;
        
        Test testCase = new ResubmitCustomerTestCase("testResubmitCustomer");
        Test loadTest = new LoadTest(testCase, maxUsers, iterations, timer);
        
        suite.addTest(loadTest);
        
        return suite;
        
    }
    
    
}
