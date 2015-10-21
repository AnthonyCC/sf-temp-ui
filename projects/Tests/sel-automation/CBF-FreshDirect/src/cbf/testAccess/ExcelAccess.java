/******************************************************************************
$Id : ExcelAccess.java 9/8/2014 1:21:48 PM
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

import cbf.engine.DataAccess;
import cbf.engine.TestCase;
import cbf.engine.TestCaseAccess;
import cbf.harness.Harness;
import cbf.harness.ResourcePaths;
import cbf.plugin.PluginManager;
import cbf.utils.LogUtils;

/**
 * 
 * Implements TestCaseAccess interface and makes TestCase from test file
 * 
 */
public class ExcelAccess implements TestCaseAccess {

	/**
	 * Constructor for initializing resource paths and data table access
	 * 
	 * @param params
	 *            map containing parameters
	 */
	public ExcelAccess(Map params) {
		this.params = params;
		//resourcePaths = Harness.resourcePaths;
		
		try {
			Map<String,Object> dataAccessMap=(Map<String, Object>) Harness.GCONFIG.get("DataAccess");
			dataTableAccess=(DataAccess) PluginManager.getPlugin((String) dataAccessMap.get("plugin"),(Map<String, Object>) dataAccessMap.get("parameters"));
		} catch (ClassCastException e) {
			logger.handleError(
					"Value for 'DataAccess' is not proper in user config file ",
					e);
		}
	}

	/**
	 * Deserializes test file, resolves references and returns TestCase object
	 * 
	 * @param info
	 *            Map having info related to TestCase like test name
	 * @return object of TestCase
	 */
	public TestCase getTestCase(Map info) {
		logger.trace("getTestCase()");
		/*String testName = resourcePaths.getTestName();
		String testFile = resourcePaths.getTestFile();*/
		testName= (String) info.get("instanceName");
		String testFile=(String) params.get("folderpath");
		if(testFile.equals("")){
			testFile=resourcePaths.getSuiteResource("Plan/TestCases", testName+".xls");
		}
		if (testFile == null) {
			logger.handleError("Deserializing test step file:" + testFile
					+ ": can not find it");
		}
		TestCase oTestCase = null;
		try {
			oTestCase = deserializeTest(testName, testFile);
		} catch (Exception e) {
			logger.handleError("Deserializing test step file:" , testFile, e);
		}

		return oTestCase;
	}

	/**
	 * Returns ExcelAccess with test name format string
	 */
	public String toString() {
		return "ExcelAccess(" + testName + ")";
	}

	private TestCase deserializeTest(String testName, String serializedFileName) {
		logger.trace("DeserializeTest(" + testName + ";" + serializedFileName
				+ ")");
		return ExcelDeserializer.deserialize(dataTableAccess, testName,
				serializedFileName);

	}

	private LogUtils logger = new LogUtils(this);
	private ResourcePaths resourcePaths=ResourcePaths.singleton;
	private DataAccess dataTableAccess;
	private Map params;
	private String testName;
}
