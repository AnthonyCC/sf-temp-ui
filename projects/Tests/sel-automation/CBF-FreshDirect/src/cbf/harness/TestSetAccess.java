/******************************************************************************
$Id : TestSetAccess.java 9/8/2014 1:21:28 PM
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

import java.util.Map;

import cbf.engine.TestSet;
import cbf.plugin.PluginManager;
import cbf.utils.LogUtils;

/**
 * 
 * Provides accesss to testset
 * 
 */
public class TestSetAccess {

	/**
	 * Instantiates TestSet
	 * @param testSet 
	 * 
	 * @return TestSet object
	 */
	public static TestSet instantiate(String testSet) {

	return new ExcelTestSet(testSet);
	}

	private LogUtils logger = new LogUtils(this);
}
