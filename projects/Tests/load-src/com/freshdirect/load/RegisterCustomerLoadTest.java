
/*
 * RegisterCustomerLoadTest.java
 * JUnit based test
 *
 * Created on May 15, 2002, 7:58 PM
 */                

package com.freshdirect.load;

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
public class RegisterCustomerLoadTest extends TestCase {
    
    public RegisterCustomerLoadTest(java.lang.String testName) {
        super(testName);
    }        
    
    public static void main(java.lang.String[] args) {
        
        //Calendar endTest = Calendar.getInstance();
        //endTest.set(2003, 04, 13, 8, 0); // May 13, 2003, 8:00 AM
        //int i = 0;
        //while (endTest.getTime().after(new Date())) { // keep repeating test until end time
        //    System.out.println("Starting iteration " + ++i + " at " + new Date());
            junit.textui.TestRunner.run(suite());
        //    System.out.println("Done with iteration " + i + " at " + new Date());
        //}

    }
    
    public static Test suite() {
        
        TestSuite suite = new TestSuite();
        
        Timer timer = new ConstantTimer(1000);
        int maxUsers = 20;
        int iterations = 200;
        
        Test testCase = new RegisterCustomerTestCase("testCustomerRegistration");
        Test loadTest = new LoadTest(testCase, maxUsers, iterations, timer);
        suite.addTest(loadTest);
        
        return suite;
    
    }


}
