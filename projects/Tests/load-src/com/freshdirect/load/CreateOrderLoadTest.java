
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
public class CreateOrderLoadTest extends TestCase {
    
    public CreateOrderLoadTest(java.lang.String testName) {
        super(testName);
    }        
    
    public static void main(java.lang.String[] args) {
        
        System.out.println("beginning store warmup...");
        //LoadUtil.doStoreWarmup();
        
        //Calendar endTest = Calendar.getInstance();
        //endTest.set(2003, 05, 11, 8, 0); // June 11, 2003, 8:00 AM
        //int i = 0;
        //while (endTest.getTime().after(new Date())) { // keep repeating test until end time
        //  System.out.println("Starting iteration " + ++i + " at " + new Date());
            Date start = new Date();
            junit.textui.TestRunner.run(suite());
            
            Date end = new Date();
            System.out.println("Ran from " + start + " to " + end);
        //  System.out.println("Done with iteration " + i + " at " + new Date());
        //}
    }
    
    
    public static Test suite() {
		LoadUtil.doStoreWarmup();
		
        TestSuite suite = new TestSuite();
        
        Timer timer = new ConstantTimer(20000);
        int maxUsers = 5;
        int iterations = 100;
        
        Test testCase = new CreateOrderTestCase("testCreateOrder");
        Test loadTest = new LoadTest(testCase, maxUsers, iterations, timer);
        
        suite.addTest(loadTest);
        
        return suite;
    
    }
    

}
