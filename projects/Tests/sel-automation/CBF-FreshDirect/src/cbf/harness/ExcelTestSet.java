/******************************************************************************
$Id : ExcelTestSet.java 9/8/2014 1:21:47 PM
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

package cbf.harness;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cbf.engine.TestCase;
import cbf.engine.TestInstance;
import cbf.engine.TestIteration;
import cbf.engine.TestSet;
import cbf.utils.Configuration;
import cbf.utils.DTAccess;
import cbf.utils.Utils;

/**
 * 
 * Implements TestSet interface and makes TestInstance
 * 
 */
public class ExcelTestSet implements TestSet {
	String testSetSheet = "TestSet";
	private static Map<Integer, ArrayList<String>> testInstanceInfo = new HashMap<Integer, ArrayList<String>>();

	/**
	 * Constructor which reads the TestSet sheet and makes a list map for
	 * instances as per the user selection
	 * 
	 */
	public ExcelTestSet(String testSet) {
		String testCaseName;
		String folderPath;
		List<Map> testInstances;

		if(testSet==null || testSet.equals(""))
			testSetSheet=(String) Harness.GCONFIG.get("TestSetSheet");
		else
			testSetSheet=testSet;
		
		DTAccess dtAccess = new DTAccess((String) ResourcePaths.singleton
				.getSuiteResource("Lab", "TestSet.xls"));
		testInstances = dtAccess.readSheet(testSetSheet);

		int index = 0;

		for (int ix = 0; ix < testInstances.size(); ix++) {
			if (!Utils.string2Bool(((String) testInstances.get(ix).get(
					"SelectYN")))) {
				continue;
			}
			testCaseName = (String) testInstances.get(ix).get("TestCase Name");
			folderPath = (String) testInstances.get(ix).get("Folder Path");
			ArrayList<String> ab = new ArrayList<String>();
			ab.add(testCaseName);
			ab.add(folderPath);
			testInstanceInfo.put(index, ab);
			index++;

		}
	}

	/**
	 * Returns test instance object specified at the given index in instance
	 * array
	 * 
	 * @param ix
	 *            index of TestInstance
	 * @return testInstance
	 */
	public TestInstance testInstance(final int ix) {
		testInstanceInfo.get(ix);
		final ArrayList<String> params = testInstanceInfo.get(ix);
		return new TestInstance() {

			public TestCase testCase() {
				return null;
			}

			public String description() {
				return null;
			}

			public String instanceName() {
				return params.get(0);
			}

			public TestIteration[] iterations() {
				return null;
			}

			public String folderPath() {
				return params.get(1);
			}
		};
	}

	/**
	 * Returns number of TestInstances
	 * 
	 * @return TestInsances count
	 */
	public int testInstanceCount() {
		return testInstanceInfo.size();
	}
}
