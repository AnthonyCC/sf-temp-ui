/******************************************************************************
$Id : BaseAppDriver.java 9/8/2014 1:21:28 PM
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

import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.sikuli.script.Screen;

import ui.ObjectMap;
import ui.SikuliUIDriver;
import ui.WebUIDriver;
import ModuleDrivers.CompositeAppDriver;
import cbf.utils.DataRow;
import cbf.utils.LogUtils;

/**
 * 
 * Implements AppDriver interface and initializes logger
 *
 */
public class BaseAppDriver implements AppDriver {
	public static WebUIDriver uiDriver;
	public static ObjectMap objMap;
	public static WebDriver webDriver;
	public static Screen sikuli;
	public static SikuliUIDriver sikuliUIDriver;

	protected Map<String, ModuleDriver> moduleDrivers;

	public static TestResultLogger RESULT;

	private LogUtils logger = new LogUtils(this);

	/**
	 * Initializes TestResultLogger
	 * 
	 * @param resultLogger
	 *            TestResultLogger object with methods like passed, failed, error etc available for reporting runtime results
	 */
	public void initialize(TestResultLogger resultLogger) {
		moduleDrivers = new CompositeAppDriver()
				.initializeModules(resultLogger);
		RESULT = resultLogger;
	}

	/**
	 * Invokes specific function related to provided component code for execution
	 * 
	 * @param moduleCode
	 *            module code for the input component
	 * @param componentCode
	 *            code for the component under execution
	 * @param input
	 *            DataRow of input parameters
	 * @param output
	 *            empty DataRow passed to capture any runtime output during execution of component
	 */
	public void perform(String moduleCode, String componentCode, DataRow input,
			DataRow output) {

    ModuleDriver driver = moduleDrivers.get(moduleCode);
		if (driver == null) {
			logger.handleError("Cannot find driver for module", moduleCode);
      return;
    }
		//objMap.readLocators(moduleCode);
		driver.perform(componentCode, input, output);
		
	}

	/**
	 * Recovers from abrupt execution interruption
	 */
	public void recover() {
	}
}
