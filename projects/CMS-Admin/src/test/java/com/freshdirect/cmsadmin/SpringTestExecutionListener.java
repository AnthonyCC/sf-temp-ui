package com.freshdirect.cmsadmin;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestExecutionListener;

public class SpringTestExecutionListener implements TestExecutionListener{

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
    "  Duration   : %s ms\n" + 
    "  Result     : %s";

    private static final String EXCEPTION = 
    "\n----- Exception ---------------------------------------\n" + 
    "%s";
    
    private static final String END = 
    "\n----- End ---------------------------------------------\n\n";
    
    private final DateFormat dateFormat;
    private Date startTimeOfTestClass;
    private Date startTimeOfTestMethod;

    public SpringTestExecutionListener() {
        dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        dateFormat.setTimeZone(TimeZone.getTimeZone(UTC));
    }

    @Override
    public void beforeTestClass(TestContext testContext) throws Exception {
        startTimeOfTestClass = new Date();
        System.out.print(String.format(START_CLASS, testContext.getTestClass().getName(), dateFormat.format(startTimeOfTestClass)));
        System.out.print(END);
    }

    @Override
    public void prepareTestInstance(TestContext testContext) throws Exception {
    }

    @Override
    public void beforeTestMethod(TestContext testContext) throws Exception {
        startTimeOfTestMethod = new Date();
        System.out.print(String.format(START_CASE, testContext.getTestMethod().getName(), dateFormat.format(startTimeOfTestMethod)));
        System.out.print(LOG);
    }

    @Override
    public void afterTestMethod(TestContext testContext) throws Exception {
        Date endTimeOfTestMethod = new Date();
        long duration = endTimeOfTestMethod.getTime() - startTimeOfTestMethod.getTime();
        TestResult testResult = checkResult(testContext);
        System.out.print(String.format(RESULT_CASE, dateFormat.format(endTimeOfTestMethod), duration, testResult));
        if (TestResult.FAILED == testResult){
            System.out.print(String.format(EXCEPTION, ExceptionUtils.getStackTrace(testContext.getTestException())));
        }
        System.out.print(END);
    }

    @Override
    public void afterTestClass(TestContext testContext) throws Exception {
        Date endTimeOfTestClass = new Date();
        long duration = endTimeOfTestClass.getTime() - startTimeOfTestClass.getTime();
        System.out.print(String.format(RESULT_CLASS, testContext.getTestClass().getName(), dateFormat.format(endTimeOfTestClass), duration));
        System.out.print(END);
    }

    private TestResult checkResult(TestContext testContext){
        return testContext.getTestException()==null ? TestResult.SUCCESS : TestResult.FAILED;
    }
}
