/******************************************************************************
$Id : DesignerAccess.java 9/8/2014 1:21:28 PM
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

package cbf.testAccess;

import java.util.Map;

import cbf.testAccess.DesignerDeserializer;
import cbf.engine.TestCase;
import cbf.engine.TestCaseAccess;
import cbf.utils.FrameworkException;
import cbf.utils.LogUtils;

/**
 * 
 * Implements TestCaseAccess interface and makes TestCase from designer
 *
 */
public class DesignerAccess implements TestCaseAccess {
	
	/**
	 * Constructor to initialize CONFIG parameter
	 * 
	 * @param params
	 *            map containing parameters
	 */
	public DesignerAccess(Map params) {
		this.params=params;
	}

	/**
	 * Deserializes test file, resolves references and returns TestCase object
	 * 
	 * @param info
	 *            Map having info related to TestCase like test name
	 * @return object of TestCase
	 * 
	 */
	public TestCase getTestCase(Map info) {		
		logger.trace("getTestCase()");
		testName = (String) info.get("instanceName");
		folderPath = (String) info.get("folderPath");
		if (folderPath == null) {
			logger.handleError("Deserializing test step file:" + folderPath
					+ ": can not find it");
		}
		
		TestCase oTestCase = null;
		try {
			oTestCase = deserializeTest(testName, folderPath);
		} catch (FrameworkException e) {
			logger.handleError("Deserializing test step file:" + folderPath);
		}

		return oTestCase;
	}
	
	private LogUtils logger = new LogUtils(this);
	private String testName;
	private String folderPath;
	private Map params;

	private TestCase deserializeTest(String testName, String folderPath) {
		logger.trace("DeserializeTest(" + testName + ";" + folderPath + ")");
		return DesignerDeserializer.deserialize(params, testName, folderPath);
	}

	/**
	 * Returns DesignerAccess with test name format string
	 */
	public String toString() {
		return "DesignerAccess(" + testName + ")";
	}
}
