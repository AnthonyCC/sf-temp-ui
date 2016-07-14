package com.freshdirect.cmsadmin;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

public class MockitoTestExecutionListener extends RunListener {

    private enum TestResult {
        SUCCESS, FAILED
    }

    private static final String UTC = "UTC";
    private static final String START_CLASS = 
    "\n----- Start Class -------------------------------------\n" + 
    "  Test class : %s\n" + 
    "  Started    : %s";

    private static final String START_CASE = 
    "\n----- Start Case --------------------------------------\n" + 
    "  Test case  : %s\n" + 
    "  Started    : %s";

    private static final String LOG = 
    "\n----- Log ---------------------------------------------\n";

    private static final String RESULT_CLASS = 
    "\n----- Class Result ------------------------------------\n" + 
    "  Test class : %s\n" + 
    "  Finished   : %s\n"  + 
    "  Duration   : %s ms";

    private static final String RESULT_CASE = 
    "\n----- Case Result -------------------------------------\n" + 
    "  Finished   : %s\n"    + 
    "  Duration   : %s ms"; 

    private static final String EXCEPTION = 
    "\n----- Exception ---------------------------------------\n" + 
    "%s";

    private static final String IGNORE = 
    "\n----- Case Ignore -------------------------------------\n" + 
    "  Test case  : %s";

    private static final String END = 
    "\n----- End ---------------------------------------------\n\n";

    private static final String FAILED_CASES = 
    "\n----- Failed case(s) ----------------------------------";

    private static final String FAILED_CASE = "\n  %s  %s";
    
    private final DateFormat dateFormat;
    private Date startTimeOfTestMethod;
    private String testClassName;
    
    public MockitoTestExecutionListener() {
        dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        dateFormat.setTimeZone(TimeZone.getTimeZone(UTC));
    }

    public void testRunStarted(Description description) throws Exception {
        System.out.print(String.format(START_CLASS, description.getClassName(), dateFormat.format(new Date())));
        System.out.print(END);
    }
    
    public void testRunFinished(Result result) throws Exception {
        if (!result.wasSuccessful()){
            System.out.print(FAILED_CASES);
            for (Failure failure : result.getFailures()) {
                System.out.print(String.format(FAILED_CASE, failure.getTestHeader(), TestResult.FAILED));
            }
            System.out.print(END);
        }
        System.out.print(String.format(RESULT_CLASS, testClassName, dateFormat.format(new Date()), result.getRunTime()));
        System.out.print(END);
    }
    
    public void testStarted(Description description) throws Exception {
        testClassName = description.getClassName();
        startTimeOfTestMethod = new Date();
        System.out.print(String.format(START_CASE, description.getMethodName(), dateFormat.format(startTimeOfTestMethod)));
        System.out.print(LOG);
    }

    public void testFinished(Description description) throws Exception {
        Date endTimeOfTestClass = new Date();
        long duration = endTimeOfTestClass.getTime() - startTimeOfTestMethod.getTime();
        System.out.print(String.format(RESULT_CASE, dateFormat.format(endTimeOfTestClass), duration));
        System.out.print(END);
    }

    public void testFailure(Failure failure) throws Exception {
        System.out.print(String.format(EXCEPTION, ExceptionUtils.getStackTrace(failure.getException())));
        System.out.print(END);
    }

    public void testAssumptionFailure(Failure failure) {
        System.out.print(String.format(EXCEPTION, ExceptionUtils.getStackTrace(failure.getException())));
        System.out.print(END);
    }

    public void testIgnored(Description description) throws Exception {
        System.out.print(String.format(IGNORE, description.getMethodName()));
        System.out.print(END);
    }
}
