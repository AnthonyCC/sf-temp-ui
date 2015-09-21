/******************************************************************************
$Id : TestResultLogger.java 9/8/2014 1:21:38 PM
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

import cbf.engine.TestResult.ResultType;
import cbf.utils.Utils;

/**
 * 
 * Logs the execution results in reports
 * 
 */
public class TestResultLogger {
	private TestResultTracker tracker;

	/**
	 * Constructor to initialize TestResultTracker object
	 * 
	 * @param tracker
	 *            object of TestResultTracker
	 */
	public TestResultLogger(TestResultTracker tracker) {
		this.tracker = tracker;
	}

	/**
	 * Logs TestStep result as passed in report
	 * 
	 * @param name
	 *            name of TestStep
	 * @param expected
	 *            expected result of TestStep
	 * @param actual
	 *            actual result of TestStep
	 */
	public void passed(String name, String expected, String actual) {
		log(name, ResultType.PASSED, expected, actual, true);
	}

	/**
	 * Logs TestStep result as failed in report
	 * 
	 * @param name
	 *            name of TestStep
	 * @param expected
	 *            expected result of TestStep
	 * @param actual
	 *            actual result of TestStep
	 */
	public void failed(String name, String expected, String actual) {
		log(name, ResultType.FAILED, expected, actual, true);
	}

	/**
	 * Logs TestStep result as error in report
	 * 
	 * @param name
	 *            name of TestStep
	 * @param expected
	 *            expected result of TestStep
	 * @param actual
	 *            actual result of TestStep
	 */
	public void error(String name, String expected, String actual) {
		log(name, ResultType.ERROR, expected, actual, true);
	}

	/**
	 * Logs TestStep result as done in report
	 * 
	 * @param name
	 *            name of TestStep
	 * @param expected
	 *            expected result of TestStep
	 * @param actual
	 *            actual result of TestStep
	 */
	public void done(String name, String expected, String actual) {
		log(name, ResultType.DONE, expected, actual, true);
	}

	/**
	 * Logs TestStep result as warning in report
	 * 
	 * @param name
	 *            name of TestStep
	 * @param expected
	 *            expected result of TestStep
	 * @param actual
	 *            actual result of TestStep
	 */
	public void warning(String name, String expected, String actual) {
		log(name, ResultType.WARNING, expected, actual, true);
	}

	/**
	 * Logs the execution results in report as per the inputs
	 * 
	 * @param name
	 *            name of TestStep
	 * @param rsType
	 *            ResultType of TestStep
	 * @param expected
	 *            expected result of TestStep
	 * @param actual
	 *            actual result of TestStep
	 */
	public void log(String name, ResultType rsType, String expected,
			String actual, boolean screenDump) {
		tracker.log(rsType, Utils.toMap(new Object[] { "name", name,
				"expected", expected,"actual", actual,"screenDump",
				new Boolean(screenDump) }));
	}
}
