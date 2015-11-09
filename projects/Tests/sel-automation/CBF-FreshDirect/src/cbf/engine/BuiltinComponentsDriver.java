/******************************************************************************
$Id : BuiltinComponentsDriver.java 9/8/2014 1:21:37 PM
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

import cbf.harness.ParameterAccess;
import cbf.utils.DataRow;
import cbf.utils.FrameworkException;
import cbf.utils.LogUtils;
import cbf.utils.SleepUtils;
import cbf.utils.SleepUtils.TimeSlab;

/**
 * 
 * Implements built-in components
 * 
 */
class BuiltinComponentsDriver {
	private ParameterAccess paramsAccess;
	LogUtils logger = new LogUtils(this);

	/**
	 * Constructor to initialize ParameterAccess object
	 * 
	 * @param paramsAccess
	 *            object of ParameterAccess
	 */
	public BuiltinComponentsDriver(ParameterAccess paramsAccess) {
		this.paramsAccess = paramsAccess;
	}

	/**
	 * Performs mapping with DataRow
	 * 
	 * @param moduleCode
	 *            module code for the input component, in this case it will be _FW
	 * @param componentCode
	 *            component code for the input component, can be 1. save, which saves
	 *            the previous component's input parameters or output parameters
	 *            required in further execution lifecycle 2. sleep, a generic
	 *            component for wait with specific timeslabs(YIELD, LOW, MEDIUM,
	 *            HIGH)
	 * 
	 * @param input
	 *            input parameters
	 * @param output
	 *            Empty hashmap
	 */
	public void perform(String componentCode, DataRow input,
			Map<String, String> output) {
		eval(componentCode, input, output);
	}

	private void save(DataRow input, DataRow output) {
		paramsAccess.copyRecentParameters(input.get("inputName"), input
				.get("outputName"));

	}

	private void sleep(DataRow input, DataRow output) {
		String level = input.get("level");
		if (level.equals(""))
			logger.handleError("Parameter level must be specified", level);
		TimeSlab sleepLevel = null;
		switch (level.charAt(0)) {
		case 'Y':
			sleepLevel = TimeSlab.YIELD;
			break;
		case 'L':
			sleepLevel = TimeSlab.LOW;
			break;
		case 'M':
			sleepLevel = TimeSlab.MEDIUM;
			break;
		case 'H':
			sleepLevel = TimeSlab.HIGH;
			break;
		default:
			logger.handleError("Invalid level : ", sleepLevel);

		}
		SleepUtils.getInstance().sleep(sleepLevel);
	}

	private void fail(DataRow input, DataRow output) {
		// failed("","Failed");
	}

	/**
	 * Invokes specific function related to provided component code for execution
	 * 
	 * @param componentCode
	 *            code for the component under execution
	 * @param input
	 *            DataRow of input parameters
	 * @param output
	 *            empty DataRow passed to capture any runtime output during
	 *            execution of component
	 */
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
						logger.trace("Executing............." + componentCode);
						m.invoke(this, input, output);
					} catch (IllegalArgumentException e) {
						logger
								.handleError("Invalid component code", e,
										componentCode);
					} catch (IllegalAccessException e) {
						logger
								.handleError("Invalid component code", e,
										componentCode);
					} catch (InvocationTargetException e) {
						logger
								.handleError("Invalid component code", e,
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

	/**
	 * Returns BuiltinComponentsDriver format string
	 */
	public String toString() {
		return "BuiltinComponentsDriver()";
	}
}
