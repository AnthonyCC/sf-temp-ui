
/*
 * RegisterCustomerLoadTest.java
 * JUnit based test
 *
 * Created on May 15, 2002, 7:58 PM
 */                

package com.freshdirect.pci;

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
        
            Date start = new Date();
            junit.textui.TestRunner.run(suite());
            
            Date end = new Date();
            System.out.println("Ran from " + start + " to " + end);

    }
    
    
    public static Test suite() {
		LoadUtil.doStoreWarmup();
		
        TestSuite suite = new TestSuite();
        //Set 5 seconds delay between start of each user.
        Timer timer = new ConstantTimer(3000);
        int maxUsers = 20;
        int iterations = 500;
        
        Test testCase = new CreateOrderTestCase("testCreateOrder");
        Test loadTest = new LoadTest(testCase, maxUsers, iterations, timer);
        
        suite.addTest(loadTest);
        
        return suite;
    
    }
    

}
