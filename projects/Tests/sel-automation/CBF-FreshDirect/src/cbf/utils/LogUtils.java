/******************************************************************************
$Id : LogUtils.java 9/8/2014 1:22:41 PM
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

package cbf.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.PropertyConfigurator;

/**
 * 
 * Utility provides functionalities related to logging and error handling Levels
 * of logging: debug|trace|details|warning|error
 * 
 * Logging utilities accept varargs of objects. StringUtils is used to
 * stringify.
 * 
 * handleError if first vararg (after text msg) is an Exception, it is treated
 * as root cause
 */
public class LogUtils {

	private Logger logger = Logger.getLogger(this.getClass());
	// private String log4JPropertyFile =
	// ResourcePaths.singleton.geFrameworkResource("Resources",
	// "log4j.properties");
	private String log4JPropertyFile = "Resources/log4j.properties"; //TODO : Pass dynamic path
	private Properties p = new Properties();

	private static boolean logInitialized = false;

	private static Logger traceLog = Logger.getLogger("traceLogger");
	private static Logger errorLog = Logger.getLogger("errorLogger");
	private Object owner;

	/**
	 * Overloaded constructor to initialize owner
	 * 
	 * @param owner
	 *            value of owner
	 */
	public LogUtils(Object owner) {
		this.owner = owner;
		initializeLog();

	}

	private void initializeLog() {
		if (logInitialized == false) {
			try {				
				p.load(new FileInputStream(log4JPropertyFile));
				PropertyConfigurator.configure(p);
			} catch (IOException e) {
			}
			logInitialized = true;
		}
	}

	/**
	 * Default constructor
	 */
	public LogUtils() {
		this("<Null>");
	}

	/**
	 * Logs arguments as debug
	 * 
	 * @param varargs
	 *            object of arguments
	 */
	public void debug(Object... varargs) {
		traceLog.debug(StringUtils.toString(owner) + DELIM + toString(varargs));
	}

	/**
	 * Logs arguments as trace
	 * 
	 * @param varargs
	 *            object of arguments
	 */
	public void trace(Object... varargs) {
		traceLog.trace(StringUtils.toString(owner) + DELIM + toString(varargs));
	}

	/**
	 * Logs arguments as detail
	 * 
	 * @param varargs
	 *            object of arguments
	 */
	public void detail(Object... varargs) {
		traceLog.info(StringUtils.toString(owner) + DELIM + toString(varargs));
	}

	/**
	 * Logs arguments as warning
	 * 
	 * @param varargs
	 *            object of arguments
	 */
	public void warning(Object... varargs) {
		errorLog.warn(StringUtils.toString(owner) + DELIM + toString(varargs));
	}

	/**
	 * Logs arguments as error
	 * 
	 * @param varargs
	 *            object of arguments
	 */
	public void error(Object... varargs) {
		errorLog.error(StringUtils.toString(owner) + DELIM + toString(varargs));

	}

	/**
	 * Handles error and logs arguments
	 * 
	 * @param msg
	 *            text message for exception
	 * @param varargs
	 *            object of arguments
	 */
	public void handleError(String msg, Object... varargs) {
		Exception rootCause = null;
		if (varargs.length != 0) {
			if (varargs[0] instanceof Exception) {
				rootCause = (Exception) varargs[0];
			}
		}
		String text = "Error: " + msg + DELIM + toString(varargs);
		error(text);

		// Do **not** repeatedly log framework exception's
		if (rootCause != null && !(rootCause instanceof FrameworkException)) {
			error(msg, rootCause.getStackTrace());
		}

		throw new FrameworkException(text, rootCause);
	}

	/**
	 * TODO: public void handleError(String msg, Exception exception, Object...
	 * varargs) Useful in highlighting root cause
	 */

	/**
	 * Rethrow an exception after due logging
	 * 
	 * @param msg
	 *            text message for exception
	 * @param exception
	 *            object of FrameworkException
	 */
	public void handleError(String msg, FrameworkException exception) {
		error("Error: " + msg + DELIM + "Re-throwing exception");
		throw exception;
	}

	private static final String DELIM = "|";

	private static String toString(Object... varargs) {
		String s = "", delim = "";
		for (Object obj : varargs) {
			s += delim + StringUtils.toString(obj);
			delim = DELIM;
		}
		return s;
	}
}
