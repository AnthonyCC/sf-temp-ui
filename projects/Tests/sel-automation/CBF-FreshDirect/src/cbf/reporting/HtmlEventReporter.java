/******************************************************************************
$Id : HtmlEventReporter.java 9/8/2014 1:21:56 PM
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

package cbf.reporting;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import cbf.engine.ResultReporter;
import cbf.engine.TestCase;
import cbf.engine.TestIteration;
import cbf.engine.TestResult;
import cbf.engine.TestStep;
import cbf.engine.TestResult.ResultType;
import cbf.harness.ResourcePaths;
import cbf.utils.FileUtils;
import cbf.utils.LogUtils;

/**
 * 
 * Implements ResultReporter and generates HTML reports
 * 
 */
public class HtmlEventReporter implements ResultReporter {

	// private static final Map = null;

	/**
	 * Constructor to initialize parameters
	 * 
	 * @param params
	 *            map containing parameters
	 */

	public HtmlEventReporter(Map params) {
		dynamicName = (String) params.get("dynamicName");
		summaryPath = (String) params.get("summaryPath");
		if (summaryPath.equals("")) {
			summaryPath = ResourcePaths.singleton.getRunResource("",
					"");
		}
		if (!(FileUtils.makeFolder(summaryPath))) {
			logger
					.handleError("Cant create/access html reports folder; these will not be generated: "
							+ summaryPath);
		}

		detailsPath = (String) params.get("detailsPath");
		if (detailsPath.equals("")) {
			detailsPath = ResourcePaths.singleton.getRunResource("HtmlEvents",
					"");
		}
		if (!(FileUtils.makeFolder(detailsPath))) {
			logger
					.handleError("Cant create/access html reports folder; these will not be generated: "
							+ detailsPath);
		}

	}

	/**
	 * Reporter open method
	 * 
	 * @param headers
	 *            contains header info, like run name, config details etc
	 */
	public void open(Map headers) {
	}

	/**
	 * Reporter close method
	 */
	public void close() {
	}

	/**
	 * Reports entity execution start details
	 * 
	 * @param result
	 *            entity object
	 */
	public void start(TestResult result) {
		report("START", result, result.entityDetails);
	}

	/**
	 * Logs execution details in report
	 * 
	 * @param result
	 *            entity details
	 * @param rsType
	 *            result type of the current executed entity
	 * @param details
	 *            execution details of the current executed entity
	 */
	public void log(TestResult result, ResultType rsType, Map details) {
		report("DETAILS", result, details);
	}

	/**
	 * Reports execution details along with result counts
	 * 
	 * @param result
	 *            execution details
	 * @param rsType
	 *            result type of the current executed entity
	 * @param details
	 *            execution details of the current executed entity
	 */
	public void finish(TestResult result, ResultType rsType, Object details) {
		report("FINISH", result, result.entityDetails);
	}

	/**
	 * Returns HtmlEventReporter along with html report folder path format
	 * string
	 */
	public String toString() {
		return "HtmlEventReporter(" + summaryPath + ")";
	}

	private void writeTestSet() {

		startSummaryFile(dynamicName + ".html", "TestSet");
		String str = "";
		str = "<td>" + testCases + "</td><td>" + failed + "</td><td>"
				+ testStart + "</td><td>" + testFinish + "</td><td>"
				+ calculateDuration(testFinish, testStart) + "</td><td>"
				+ eReportLink("ExecutionReport");
		testSetArray.add(str);
		logSummary(testSetArray);
		testCaseSummaryTemplate();
		logSummary(testCasesArray);
		finishSummaryFile();
	}

	private void report(String eventType, TestResult result, Object eventData) {
		String str1 = "";
		try {
			switch (result.entityType) {
			case ITERATION:
				if (eventType.equals("START")) {
					if (testStart == null)
						testStart = result.startTime;
					TestIteration iteration = (TestIteration) eventData;
					String iterName = "";
					Map iterParams = iteration.parameters();
					if (iterParams != null) {
						if (iterParams.containsKey("_rowId"))
							iterName = iterParams.get("_rowId").toString();
						if (iterName == null || iterName.equals("")) {
							iterName = "("
									+ result.parent.childCount
									+ " of "
									+ ((TestCase) result.parent.entityDetails)
											.iterationCount() + ")";
						}
					}
					tcName = result.parent.entityName;
					if (iterName != null) {
						tcName = tcName + " " + iterName;
					}

					detailsArray.add("<td colspan = 8 align = center>" + tcName
							+ "</td>");

				} else if (eventType.equals("FINISH")) {
					testCases++;
					testFinish = result.finishTime;
					String str = "";
					String status = "";
					if (result.msRsType.isPassed())
						status = "Passed";
					else {
						status = "Failed";
						failed++;
					}
					str = "<td>"
							+ ((TestIteration) eventData).stepCount()
							+ "</td><td>"
							+ result.childCount
							+ "</td><td>"
							+ result.startTime
							+ "</td><td>"
							+ result.finishTime
							+ "</td><td>"
							+ calculateDuration(result.finishTime,
									result.startTime);

					str1 = "<td>" + tcName + "</td><td>" + status + "</td>"
							+ str;
					summaryArray.add(str1);
					str1 = "<td>" + tcName + "</td><td>"
							+ testCaseFileLink(testCaseFileName, status)
							+ "</td>" + str;
					testCasesArray.add(str1);
				}
				break;
			case TESTCASE:
				if (eventType.equals("START")) {
					entityName = result.entityName;
					//testSetPath = summaryPath + "/" + dynamicName + ".html";
					testSetPath = "../" + dynamicName + ".html";
					String timeStamp=dynamicName;
					for(int i=0;i<3;i++){
						timeStamp=timeStamp.substring(timeStamp.indexOf("_")+1);
					}
					testCaseFileName = dynamicName.substring(0, dynamicName
							.indexOf("_"))
							+ "_"
							+ entityName+"_"
							+ timeStamp;
					startDetailsFile(testCaseFileName + ".html", entityName);
				} else if (eventType.equals("FINISH")) {
					logDetails(summaryArray);
					componentsTemplate();
					logDetails(detailsArray);

					finishDetailsFile();
					writeTestSet();
				}
				break;
			case TESTSTEP:
				if (eventType.equals("START")) {
					String str = "<td colspan = 8 align = center >"
							+ result.entityName + "</td>";
					startTime = result.startTime;
					//detailsArray.add(str);
				} else if (eventType.equals("FINISH")
						&& !(result.finalRsType.equals(ResultType.DONE))) {
					String str = "<td + rightSpan + >" + result.entityName.toString()
							+ "</td><td/><td/>" + "<td>";

					if (result.msRsType.isPassed()) {
						str = str + "PASSED";
					} else {
						str = str + "FAILED";
					}
					str = str + "</td><td>" + result.startTime + "</td>";
					str = str + "<td>" + result.finishTime + "</td>";
					str = str
							+ "<td>"
							+ calculateDuration(result.finishTime,
									result.startTime) + "</td>";
					//detailsArray.add(str);
				}
				break;
			case COMPONENT:
				if (eventType.equals("DETAILS")) {

					Map detailsMap = (Map) eventData;

					String str = "<td + rightSpan + >"
							+ detailsMap.get("name").toString() + "</td>";

					str = str + "<td>" + result.finalRsType + "</td>";
					str = str + "<td>" + detailsMap.get("expected") + "</td>";
					str = str + "<td>" + screenDumpLink((String) detailsMap.get("actual"),result);
					str = str + "<td>" + startTime + "</td>";
					str = str + "<td>" + new Date() + "</td>";
					str = str + "<td>"
							+ calculateDuration(new Date(), startTime)
							+ "</td>";
					startTime = new Date();
					/*str = str + "<td>" + screenDumpLink("Screen Dump", result)
							+ "</td>";*/
					detailsArray.add(str);
				}

			}
		} catch (Exception e) {
			logger.handleError("Error in Excel reporting", e);
		}

	}

	private void startDetailsFile(String fileName, String title) {
		openDetailsFile(detailsPath + "/" + fileName);

		String logoPath = ResourcePaths.singleton.getFrameworkResource(
				"Misc_ResultsViewer", "igate.jpg");
		writeDetails("<html><head>");
		writeDetails("<title>" + title + "</title>");
		writeDetails("</head><body bgcolor=#FFFFCC>");
		writeDetails("<img src=" + logoPath + " align=right>");
		writeDetails("<h1 align=center>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp<u><font color=#003399>HTML REPORT</u></font></h1>");
		testCaseDetailsTemplate();
	}

	private void startSummaryFile(String fileName, String title) {
		openSummaryFile(summaryPath + "/" + fileName);

		String logoPath = ResourcePaths.singleton.getFrameworkResource(
				"Misc_ResultsViewer", "igate.jpg");
		writeSummary("<html><head>");
		writeSummary("<title>" + title + "</title>");
		writeSummary("</head><body bgcolor=#FFFFCC>");
		writeSummary("<img src=" + logoPath + " align=right>");
		writeSummary("<h1 align=center>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp<u><font color=#003399>HTML REPORT</u></font></h1>");

		testSetTemplate();
	}

	private void finishDetailsFile() {
		writeDetails("</table><h4 align=right><a href='" + testSetPath
				+ "'>TestSetFile</a></h4></body></html>");

		closeDetailsFile();
	}

	private void finishSummaryFile() {
		writeSummary("</table></body></html>");

		closeSummaryFile();
	}

	private void testSetTemplate() {
		writeSummary("&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp<table bgcolor=#DCDCDC align=center border=3 cellspacing=2 cellpadding=2 >");
		writeSummary("<th colspan=7 bgcolor=#003399 align=center><font color=#FFFFFF>TEST SET SUMMARY</font></th>");
		writeSummary("<tr bgcolor=#EFF5F9><td><b><font color=#1F1F7A>Tests Executed</font></b></td><td><b><font color=#1F1F7A>Tests Failed</font></b></td><td><b><font color=#1F1F7A>Start time</font></b></td><td><b><font color=#1F1F7A>End time</font></b></td><td><b><font color=#1F1F7A>Duration</font></b></td><td><b><font color=#1F1F7A>Execution Report</font></b></td></tr><br><br>");
	}

	private void testCaseDetailsTemplate() {
		writeDetails("&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp<table bgcolor=#DCDCDC align=center border=3 cellspacing=2 cellpadding=2 >");
		writeDetails("<th colspan=7 bgcolor=#003399 align=center><font color=#FFFFFF>TEST CASE SUMMARY</font></th>");
		writeDetails("<tr bgcolor=#EFF5F9><td><b><font color=#1F1F7A>TestCase Name</font></b></td><td><b><font color=#1F1F7A>Status</font></b></td><td><b><font color=#1F1F7A>No. of components</font></b></td><td><b><font color=#1F1F7A>Components Run</font></b></td><td><b><font color=#1F1F7A>Start time</font></b></td><td><b><font color=#1F1F7A>End time</font></b></td><td><b><font color=#1F1F7A>Duration</font></b></td></tr>");
	}

	private void testCaseSummaryTemplate() {
		writeSummary("&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp<table bgcolor=#DCDCDC align=center border=3 cellspacing=2 cellpadding=2 >");
		writeSummary("<br><br><br>");
		writeSummary("<th colspan=7 bgcolor=#003399 align=center><font color=#FFFFFF>TEST CASE SUMMARY</font></th>");
		writeSummary("<tr bgcolor=#EFF5F9><td><b><font color=#1F1F7A>TestCase Name</font></b></td><td><b><font color=#1F1F7A>Status</font></b></td><td><b><font color=#1F1F7A>No. of components</font></b></td><td><b><font color=#1F1F7A>Components Run</font></b></td><td><b><font color=#1F1F7A>Start time</font></b></td><td><b><font color=#1F1F7A>End time</font></b></td><td><b><font color=#1F1F7A>Duration</font></b></td></tr>");
	}

	private void componentsTemplate() {
		writeDetails("<table bgcolor=#DCDCDC align=center border=3 cellspacing=2 cellpadding=2 >");
		writeDetails("<br><br><br>");
		writeDetails("<th colspan=10 bgcolor=#003399 align=center><font color=#FFFFFF>TEST CASE DETAILS</font></th>");
		writeDetails("<tr bgcolor=#EFF5F9>"
				+ "<td><b><font color=#1F1F7A>Step</font></b></td><td><b><font color=#1F1F7A>Result</font></b></td><td><b><font color=#1F1F7A>Expected Result</font></b></td><td><b><font color=#1F1F7A>Actual Result</font></b></td><td><b><font color=#1F1F7A>Start Time</font></b></td><td><b><font color=#1F1F7A>End Time</font></b></td><td><b><font color=#1F1F7A>Duration</font></b></td></tr>");
	}

	private void logDetails(ArrayList<String> strArray2) {
		for (String str : strArray2)

			writeDetails("<tr>" + str + "</tr>");
	}

	private void logSummary(ArrayList<String> strArray2) {
		for (String str : strArray2)

			writeSummary("<tr>" + str + "</tr>");
	}

	private String screenDumpLink(String name, TestResult eventData) {
		String sDumpFile;
		try {
			sDumpFile = (String) ((Map) ((Map) eventData.miscInfo)
					.get("screenDump")).get("filePath");
			sDumpFile=sDumpFile.replaceAll(ResourcePaths.singleton.getRunResource("", ""), "..");
		} catch (Exception e) {
			return name;
		}

		if (sDumpFile == null) {
			// return null;
			return name;
		}

		return "<a href='" + sDumpFile + "'>" + name + "</a>";
	}

	private String eReportLink(String name) {
		String filepath;
		try {
			/*filepath = ResourcePaths.singleton.getRunResource( name + "_"
					+ dynamicName + ".xls","");*/
			filepath="./"+name + "_"+ dynamicName + ".xls";

		} catch (Exception e) {
			return "";
		}

		if (filepath == null) {
			return "";
		}

		return "<a href='" + filepath + "'>" + name + "</a>";
	}

	private String testCaseFileLink(String name, String status) {
		String filepath;
		try {
			//filepath = detailsPath + "/" + name + ".html";
			filepath="./HtmlEvents/"+ name + ".html";
			

		} catch (Exception e) {
			return "";
		}

		if (filepath == null) {
			return "";
		}

		return "<a href='" + filepath + "'>" + status + "</a>";
	}

	private String calculateDuration(Date d2, Date d1) {
		long diff = d2.getTime() - d1.getTime();

		long diffSeconds = diff / 1000 % 60;
		long diffMinutes = diff / (60 * 1000) % 60;
		long diffHours = diff / (60 * 60 * 1000) % 24;

		String diffTime = cal(String.valueOf(diffHours)) + ":"
				+ cal(String.valueOf(diffMinutes)) + ":"
				+ cal(String.valueOf(diffSeconds));
		return diffTime;

	}

	private String cal(String time) {
		while (time.length() != 2)
			time = "0" + time;
		return time;
	}

	// IO Methods
	/*
	 * 'Function Name: openFile 'Description: This Function creates the file in
	 * txt format ' FilePath - Path of the result file
	 */
	private void openDetailsFile(String filePath) {
		try {
			fileWriter = new FileWriter(filePath);
			bufWriter1 = new BufferedWriter(fileWriter);
		} catch (IOException e) {
			logger.handleError(
					"Exception caught : When trying to open a file ", filePath,
					e);
		}
	}

	private void openSummaryFile(String filePath) {
		try {
			fileWriter = new FileWriter(filePath);
			bufWriter2 = new BufferedWriter(fileWriter);
		} catch (IOException e) {
			logger.handleError(
					"Exception caught : When trying to open a file ", filePath,
					e);
		}
	}

	private void closeDetailsFile() {
		try {
			bufWriter1.close();
		} catch (IOException e) {
			logger.handleError("Exception caught : ", e);
		} finally {
			try {
				fileWriter.close();
			} catch (IOException e) {
				logger.handleError("Exception caught : ", e);
			} finally {
				fileWriter = bufWriter1 = null;
			}
		}
	}

	private void closeSummaryFile() {
		try {
			bufWriter2.close();
		} catch (IOException e) {
			logger.handleError("Exception caught : ", e);
		} finally {
			try {
				fileWriter.close();
			} catch (IOException e) {
				logger.handleError("Exception caught : ", e);
			} finally {
				fileWriter = bufWriter2 = null;
			}
		}
	}

	private void writeDetails(String lines) {
		try {
			bufWriter1.write(lines);
		} catch (IOException e) {
			logger.handleError("Exception caught : ", e);
		}
	}

	private void writeSummary(String lines) {
		try {
			bufWriter2.write(lines);
		} catch (IOException e) {
			logger.handleError("Exception caught : ", e);
		}
	}

	private Writer bufWriter1, bufWriter2, fileWriter;
	private LogUtils logger = new LogUtils(this);
	private String summaryPath, detailsPath, dynamicName,entityName;
	private String tcName = "", testSetPath, testCaseFileName = "";
	private static int failed = 0, testCases = 0;
	private static Date testStart, testFinish,startTime = null;
	private ArrayList<String> detailsArray = new ArrayList<String>();
	private ArrayList<String> summaryArray = new ArrayList<String>();
	private ArrayList<String> testSetArray = new ArrayList<String>();
	private static ArrayList<String> testCasesArray = new ArrayList<String>();
}
