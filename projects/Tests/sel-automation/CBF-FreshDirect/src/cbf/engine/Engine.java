/******************************************************************************
$Id : Engine.java 9/8/2014 1:21:37 PM
Copyright 2014-2016 IGATE GROUP OF COMPANIES. All rights reserved
(Subject to Limited Distribution and Restricted Disclosure Only.)
THIS SOURCE FILE MAY CONTAIN INFORMATION WHICH IS THE PROPRIETARY
INFORMATION OF IGATE GROUP OF COMPANIES AND IS INTENDED FOR USE
ONLY BY THE ENTITY WHO IS ENTITLED TO AND MAY CONTAIN
INFORMATION THAT IS PRIVILEGED, CONFIDENTIAL, OR EXEMPT FROM
DISCLOSURE UNDER APPLICABLE LAW.
YOUR ACCESS TO THIS SOURCE FILE IS GOVERNED BY THE TERMS AND
CONDITIONS OF AN AGREEMENT BETWEEN YOU AND IGATE GROUP OF COMPANIES.
The USE, DISCLOSURE REPRODUCTION OR TRANSFER OF THIS PROGRAM IS
RESTRICTED AS SET FORTH THEREIN.
 ******************************************************************************/


package cbf.engine;


import java.util.List;

import cbf.engine.TestCaseRunner.TCMaker;
import cbf.engine.TestResultTracker.Reporter;
import cbf.utils.LogUtils;

public class Engine {
	AppDriver appDriver;
	TestResultTracker resultTracker;
	TestResultLogger RPT;
	LogUtils logger;

	// Need clarification on Result Reporter object
	public Engine(String runName, AppDriver appDriver,
			List<Reporter> resultReporter) {
		this.appDriver = appDriver;
		this.resultTracker = new TestResultTracker(resultReporter);
		this.RPT = new TestResultLogger(this.resultTracker);
		appDriver.initialize(this.RPT);
	}

	public TestResult runTestCase(TCMaker tcMaker, String TCName) {
		TestCaseRunner oTestCaseRunner = new TestCaseRunner(appDriver,
				resultTracker, RPT);
		return oTestCaseRunner.runTestCase(tcMaker, TCName);

	}

}
