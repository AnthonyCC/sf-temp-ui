/******************************************************************************
$Id : ReportingManager.java 9/8/2014 1:21:56 PM
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

package cbf.plugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cbf.engine.ResultReporter;
import cbf.engine.TestResult;
import cbf.engine.TestResult.ResultType;
import cbf.engine.TestResultTracker.Reporter;
import cbf.harness.Harness;
import cbf.harness.ResourcePaths;
import cbf.utils.LogUtils;

/**
 * A reporter, which manages other reports Understands the dependency across
 * reports
 **/
public class ReportingManager implements ResultReporter {
	public ReportingManager(String resultsFolder, String[] reporterSelection,
			ResourcePaths oResourcePaths,String dynamicName) {
		this.resourcePaths = oResourcePaths;
		this.reporters = selectReporters(resultsFolder, reporterSelection,dynamicName);
	}

	/**
	 * Opens the report file and updates the headers as required
	 * 
	 * @param headers
	 *            run details to be updated in report
	 * 
	 */
	public void open(Map headers) {
		// 'Start all reporters
		for (Reporter reporter : reporters) {

			try {
				((ResultReporter) reporter).open(headers);
				logger.trace("opened reporter: " , reporter);
			} catch (Exception e) {
				logger.handleError("Error opening reporter", e, reporter);
			}
		}
	}

	/**
	 * Closes the report file
	 */
	public void close() {
		// 'Stop all reporters
		for (Reporter reporter : reporters) {

			try {
				((ResultReporter) reporter).close();
			} catch (Exception e) {
				logger.warning("Error closing reporter - " , reporter , e);
			}
		}
	}

	/**
	 * Starts the specific reporter
	 * 
	 * @param result
	 *            object of TestResult
	 */
	public void start(TestResult result) {
		for (Reporter reporter : reporters) {
			try {
				reporter.start(result);
			} catch (Exception e) {
				logger.warning("Error in start - ", reporter, result, e);
			}
		}
	}

	/**
	 * Logs specified reporter
	 * 
	 * @param result
	 *            entity details
	 * @param rsType
	 *            result type of the current executed entity
	 * @param details
	 *            execution details of the current executed entity
	 */
	public void log(TestResult result, ResultType rsType, Map details) {
		for (Reporter reporter : reporters) {
			try {
				reporter.log(result, rsType, details);
			} catch (Exception e) {
				logger.warning("Error in reporting - " , reporter
						+ " - " + result + " - " + rsType + " - " + details, e);
			}
		}
	}

	/**
	 * Reporter finish method
	 * 
	 * @param result
	 *            execution details
	 * @param rsType
	 *            result type of the current executed entity
	 * @param details
	 *            execution details of the current executed entity
	 */
	public void finish(TestResult result, ResultType rsType, Object details) {
		for (Reporter reporter : reporters) {
			try {
				reporter.finish(result, rsType, details);
			} catch (Exception e) {
				logger.warning("Error in finish - " , reporter
						+ " - " + result + " - " + rsType + " - " + details, e);
			}
		}
	}

	/**
	 * Overrides toString() method of Object class to return ReportingManager
	 * format string
	 */
	public String toString() {
		return "ReportingManager()";
	}
	
	private List<Reporter> selectReporters(String resultsFolder,
			String[] reporterSelection, String dynamicName) {

		/*
		 * ' Adds reporter in the correct order of dependency ' Caveat:Any
		 * invalid selection is silently skipped without warning
		 */

		List<Map<String, Object>> reporterList = (ArrayList<Map<String, Object>>) Harness.GCONFIG
				.get("ResultReporter");
		for (String reporterName : supportedReports) {
			boolean isSelected = false;
			if (reporterSelection == null) {
				isSelected = true; // ' Default: all
			} else {
				for (String selReport : reporterSelection) {
					if (selReport.equalsIgnoreCase(reporterName)) {
						isSelected = true;
						break;
					}
				}
			}
			if (isSelected) {
				Map<String, Object> reporterMap = traverseArray(reporterList,
						reporterName);
				Reporter reporter = null;
				try {
					Map<String, Object> paramsMap = (Map<String, Object>) reporterMap.get("parameters");
					if(paramsMap==null)
						paramsMap=new HashMap<String, Object>();
					paramsMap.put("dynamicName", dynamicName);
					reporter = (Reporter) PluginManager.getPlugin((String) reporterMap.get("plugin"),paramsMap);
				
				} catch (ClassCastException c) {
					
					logger.handleError(reporterName+ " plugin value is not proper in user config file ", c);

				}
				reporters.add(reporter);
				logger.trace("Reporter selected: " , reporter);
			}
		}
		return reporters;
	}

	private Map<String, Object> traverseArray(
			List<Map<String, Object>> reporterList, String key) {
		try {
			for (Map<String, Object> innerMap : reporterList) {
				for (String str : innerMap.keySet()) {

					if (str.equalsIgnoreCase("plugin")) {

						if (innerMap.get(str).equals(key)) {
							return innerMap;
						}
					}
				}
			}
		} catch (ClassCastException c) {
			logger.handleError(
					"ResultReporter config is not proper in user config file ",
					c);

		}
		return null;
	}

	
	// new reports are added
	final String[] supportedReports = { "ScreenDump", "HtmlEvent",
			"ExcelReport", "ResultEventLogger", "EmailAlert" }; /* "ExcelSummary" old style is disabled as a default*/


	private List<Reporter> reporters = new ArrayList<Reporter>(); /* Holds the managed reporters collection*/
	private ResourcePaths resourcePaths;
	private LogUtils logger = new LogUtils(this);
}
