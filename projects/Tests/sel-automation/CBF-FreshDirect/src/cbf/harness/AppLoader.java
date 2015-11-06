/******************************************************************************
$Id : AppLoader.java 9/8/2014 1:21:47 PM
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

import cbf.engine.AppDriver;
import cbf.utils.Configuration;
import cbf.utils.LogUtils;

/**
 * 
 * Loads the application driver after bootstrap
 * 
 */
public class AppLoader {

	LogUtils logger = new LogUtils(this);
	Configuration GCONFIG = Harness.GCONFIG;

	/**
	 * Loads application driver and returns it's instance
	 * 
	 * @return object of AppDriver
	 */
	public AppDriver loadApp() {
		AppDriver driver = null;

		logger.trace("LoadApp()");
		String startUp = (String) GCONFIG.get("AppDriverBootStrap");
		logger.debug("AppDriverBootStrap: " + startUp);
		try {
			ClassLoader appLoader = ClassLoader.getSystemClassLoader();
			Class appDriver = appLoader.loadClass(startUp);
			driver = (AppDriver) appDriver.newInstance();
		} catch (Exception e) {
			logger.handleError("Error in loading AppDriver:", startUp, e);
		}
		return driver;
	}

	/**
	 * Returns AppLoader format string
	 * 
	 */
	public String toString() {
		return "AppLoader()";
	}

}
