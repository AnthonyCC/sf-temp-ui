/*
 * $Workfile$
 *
 * $Date$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.punishment;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.clarkware.junitperf.ConstantTimer;
import com.clarkware.junitperf.LoadTest;
import com.clarkware.junitperf.Timer;

/**
 *
 *
 * @version $Revision$
 * @author $Author$
 */                 
public class FDFactoryLoadTest extends TestCase {
    
    public FDFactoryLoadTest(java.lang.String testName) {
        super(testName);
    }        
    
    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }
    
    public static Test suite() {
        
        TestSuite suite = new TestSuite();
        
        Timer timer = new ConstantTimer(1000);
        int maxUsers = 5;
        int iterations = 5;
        
        Test testCase = new FDFactoryProductInfosTest("testGetProductInfos");
        Test loadTest = new LoadTest(testCase, maxUsers, iterations, timer);
        suite.addTest(loadTest);
        
        return suite;
    
    }


}
