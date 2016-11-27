/******************************************************************************
$Id : BaseModuleDriver.java 9/8/2014 1:21:28 PM
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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import org.openqa.selenium.WebDriver;
//import org.sikuli.script.Screen;

import ui.ObjectMap;
//import ui.SikuliUIDriver;
import ui.WebUIDriver;
import cbf.utils.DataRow;
import cbf.utils.FrameworkException;
import cbf.utils.LogUtils;

/**
 * 
 * Implements ModuleDriver and initializes logger
 *
 */
public class BaseModuleDriver implements ModuleDriver {


	private LogUtils logger = new LogUtils(this);
	public static TestResultLogger RESULT;
	public static WebUIDriver uiDriver;
	public static ObjectMap objMap;
	public static WebDriver webDriver;
	//public static Screen sikuli;
	//public static SikuliUIDriver sikuliUIDriver;
//	public static String Environment="dev1";

	/**
	 * Constructor to initialize TestResultLogger
	 * @param resultLogger 
	 *            TestResultLogger object with methods like passed, failed, error etc available for reporting runtime results
	 */
	public BaseModuleDriver(TestResultLogger resultLogger) {
		RESULT = resultLogger;
		objMap = BaseAppDriver.objMap;
		uiDriver=BaseAppDriver.uiDriver;
		webDriver=BaseAppDriver.webDriver;
		/*sikuli= BaseAppDriver.sikuli;
		sikuliUIDriver = BaseAppDriver.sikuliUIDriver;*/
	}

	/**
	 * Performs mapping with DataRow
	 * 
	 * @param componentCode
	 *            takes component code value
	 * @param input
	 *            DataRow of input parameters
	 * @param output
	 *            empty DataRow passed to capture any runtime output during execution of component
	 */
	public void perform(String componentCode, DataRow input,
			DataRow output) {
		eval(componentCode, input, output);
	}	
	
	private void eval(String componentCode, DataRow input,
			Map<String, String> output) {
		try {
			Class<?> clazz = this.getClass();
			// Trace debugging, see output
			boolean found = false;
			for (Method m : clazz.getDeclaredMethods())
				if (m.getName().equalsIgnoreCase(componentCode)) {
					found = true;
					try {
						logger.trace("Executing............." + componentCode+" with parameters "+input);
//						if(componentCode.equals("LaunchApp"))
//						{Environment=input.get("_rowId");
//						System.out.println(Environment);}
						m.invoke(this, input, output);
					} catch (IllegalArgumentException e) {
						logger
								.handleError("Invalid component code ", e,
										componentCode);
					} catch (IllegalAccessException e) {
						logger
								.handleError("Invalid component code ", e,
										componentCode);
					} catch (InvocationTargetException e) {
						logger
								.handleError("Invalid component code ", e,
										componentCode);
					}
					break;
				}
			if (found == false) {
				logger.handleError("Invalid component code",
						new FrameworkException(
								"Method with the component code doesn't exists",
								componentCode));
			}
		} catch (SecurityException e) {
			logger.trace(e.getMessage());
		}
	}

}
