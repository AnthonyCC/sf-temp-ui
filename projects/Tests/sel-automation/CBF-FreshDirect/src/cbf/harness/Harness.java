/******************************************************************************
$Id : Harness.java 9/8/2014 1:21:47 PM
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

import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

import com.sun.corba.se.spi.copyobject.ReflectiveCopyException;

import ModuleDrivers.CompositeAppDriver;
import cbf.engine.AppDriver;
import cbf.engine.Engine;
import cbf.engine.ResultReporter;
import cbf.engine.TestCase;
import cbf.engine.TestCaseAccess;
import cbf.engine.TestSet;
import cbf.engine.TestCaseRunner.TCMaker;
import cbf.engine.TestResultTracker.Reporter;
import cbf.plugin.PluginManager;
import cbf.plugin.ReportingManager;
import cbf.utils.Configuration;
import cbf.utils.FileUtils;
import cbf.utils.LogUtils;

/**
 * 
 * Main class for Running Engine from Harness. Defines the runTestCase()
 * function.
 * 
 */
public class Harness {
	public static ResourcePaths resourcePaths;
	public static Configuration GCONFIG;

	/**
	 * Triggers execution for the current instance(runName)
	 * 
	 * @param instanceMap
	 *            name of instance
	 * @param dynamicName 
	 * @throws ReflectiveCopyException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	public void runHarness(Map<String, String> instanceMap, String dynamicName) {//throws InstantiationException, IllegalAccessException, InvocationTargetException, ReflectiveCopyException {
		
		new CompositeAppDriver().initializeDrivers(); // TODO : Where to initialize driver
		
		instanceName = instanceMap.get("instanceName");
		initialize(instanceName,dynamicName);
		int interTcDelay;
		interTcDelay = Integer.parseInt((String) GCONFIG
				.get("InterTestCaseDelay"));
		if (interTcDelay != 0) {
			try {
				Thread.sleep(interTcDelay * 1000);
				logger.trace("Test case delay:" + interTcDelay);
			} catch (InterruptedException e) {
				logger.handleError("Exception caught : " , e,
						interTcDelay);
			}
		}
		String message = run(instanceMap);
		finalize();
		if (!message.equals("")) {
			logger.handleError("Harness Error: " , message);
		}

	}

	/**
	 * Returns execution result
	 * 
	 * @param instanceMap
	 *            name of Instance
	 * @return run result
	 */
	public String run(Map<String, String> instanceMap) {
		((ResultReporter) reporter).open(GCONFIG.getAllProperties());
		String result = runTest(instanceMap, reporter);
		((ResultReporter) reporter).close();
		return result;
	}

	/**
	 * Executes test and returns result
	 * 
	 * @param instanceMap
	 *            name of Instance
	 * @param reporter
	 *            Object of Reporter
	 * @return Result message
	 */
	public String runTest(final Map<String, String> instanceMap,
			Reporter reporter) {
		AppDriver appDriver = null;
		String message = "";
		Engine engine = null;
		try {			
			appDriver = new AppLoader().loadApp();
		} catch (Exception e) {
			logger.handleError("Failed to LoadAppDriver " , e);
			message = "Failed to LoadAppDriver" + e;
		}

		// creating list of reporters
		List<Reporter> reporters = new ArrayList<Reporter>();
		reporters.add(reporter);
		try {
			engine = new Engine(instanceName, (AppDriver) appDriver, reporters);
		} catch (Exception e) {
			logger.handleError("FactoryEngine: " , e);
			message = "FactoryEngine:" + e;
		}

		TCMaker tcMaker = new TCMaker() {
			public TestCase make() throws Exception {
				return getTestCase(instanceMap);
			}

			public String toString() {
				return instanceName;
			}
		};
		try {
			engine.runTestCase(tcMaker, instanceName);
		} catch (Exception e) {
			logger.handleError("RunTestCase: " , e);
			message = "RunTestCase:" + e;
		}
		return message;
	}

	/**
	 * Executes each instance from the test set
	 * @param nodeLabel 
	 * @param startUp 
	 * @param testSet 
	 * @throws ReflectiveCopyException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	public void runTestSet(String configFileName, String testSet, String browser, String nodeLabel) {//throws InstantiationException, IllegalAccessException, InvocationTargetException, ReflectiveCopyException {
		initializeConfig(configFileName);
		String folderPath = nodeLabel+"_"+browser+"_"+testSet;
		initializeResourcePaths(folderPath);
		setErrorAndTracePath(); // TODO : set run path for Erace.csv and Trace.csv files at the starting
		TestSet ts = TestSetAccess.instantiate(testSet);
		int tsCount = ts.testInstanceCount();		

		String dynamicName=testSet+"_"+nodeLabel+"_"+browser+"_"+uniqueDate();
		Map<String, String> instanceMap = new HashMap<String, String>();
		for (int ix = 0; ix < tsCount; ix++) {
			instanceMap.put("folderPath", ts.testInstance(ix).folderPath());
			instanceMap.put("instanceName", ts.testInstance(ix).instanceName());
			runTestCase(instanceMap,dynamicName);
		}
	}
	
	private void setErrorAndTracePath(){
		FileAppender fa = new FileAppender();
		fa.setFile(runHome+"/Error.csv");
		fa
				.setLayout(new PatternLayout(
						"%C{1} %L [%t] %d{dd MMM,yyyy HH:mm:ss.SSS} %-5p - %m%n"));
		fa.setThreshold(Level.ERROR);
		fa.setAppend(true);
		fa.activateOptions();
		Logger.getRootLogger().addAppender(fa);

		fa = new FileAppender();
		fa.setFile(runHome+"/Trace.csv");
		fa
				.setLayout(new PatternLayout(
						"%C{1} %L [%t] %d{dd MMM,yyyy HH:mm:ss.SSS} %-5p - %m%n"));
		fa.setThreshold(Level.TRACE);
		fa.setAppend(true);
		fa.activateOptions();
		Logger.getRootLogger().addAppender(fa);
	}

	/**
	 * Executes test instance
	 * 
	 * @param instanceMap
	 *            name of instance
	 * @param dynamicName 
	 * @throws ReflectiveCopyException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	public void runTestCase(Map<String, String> instanceMap, String dynamicName) {//throws InstantiationException, IllegalAccessException, InvocationTargetException, ReflectiveCopyException {
		runHarness(instanceMap,dynamicName);
	}

	/**
	 * Initializes logs, reporters in the results folder
	 * 
	 * @param testInstanceName
	 *            name of TestInstance
	 * @param dynamicName 
	 * @throws ReflectiveCopyException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	public void initialize(String testInstanceName, String dynamicName) {//throws InstantiationException, IllegalAccessException, InvocationTargetException, ReflectiveCopyException {
		logger.trace("Initialize()");				
		reporter = makeReporter(runHome,dynamicName);
	}

	/**
	 * Finalize method to call garbage collector
	 */
	public void finalize() {

	}

	/**
	 * Overloaded toString() method of Object class to return Harness format
	 * string
	 */
	public String toString() {
		return "Harness()";

	}

	private String getAutoHome() {
		if (!G_AUTO_HOME.equals("")) {
			return G_AUTO_HOME;
		}
		G_AUTO_HOME = (String) GCONFIG.get("AutoHome");
		return G_AUTO_HOME;
	}

	private void initializeLogs(String resultsFolder) {
		logger.trace("InitializeLogs(" + resultsFolder + ")");
	}

	private Reporter makeReporter(String resultFolder, String dynamicName){// throws InstantiationException, IllegalAccessException, InvocationTargetException, ReflectiveCopyException {
		// Add desired Reporters using ManageReporters
		String reporterSelection = "";
		Object obj = GCONFIG.get("ResultReporter");
		if (!obj.toString().equals("{}")) {
			ArrayList<Map<String, Object>> list = (ArrayList<Map<String, Object>>) obj;
			int i = 0;
			for (Map<String, Object> map : list) {
				if (i == 0) {
					reporterSelection = (String) map.get("plugin");
					i++;
				} else
					reporterSelection = reporterSelection + ","
							+ (String) map.get("plugin");
			}
		}
		String[] reportsSelected = null;
		if (reporterSelection != null && !reporterSelection.equals("")) {
			reporterSelection = reporterSelection.trim();
			reportsSelected = reporterSelection.split(",");
		}
		return new ReportingManager(resultFolder, reportsSelected,
				resourcePaths,dynamicName);
	}

	private TestCase getTestCase(Map<String, String> instanceMap) throws InstantiationException, IllegalAccessException, InvocationTargetException, ReflectiveCopyException {
		logger.trace("GetTestCase()");
		try {
			Map<String,Object> testCaseAccessMap=(Map<String, Object>) GCONFIG.get("TestCaseAccess");
			return ((TestCaseAccess) PluginManager.getPlugin((String) testCaseAccessMap.get("plugin"),(Map<String, Object>) testCaseAccessMap.get("parameters")))
					.getTestCase(instanceMap);
		} catch (ClassCastException e) {
			logger.handleError(
					"Value for 'TestCaseAccess' is not proper in user config file ",
					e);
		}
		return null;
	}

	private void initializeConfig(String configFileName) {
		try {
			GCONFIG = new Configuration(configFileName);
		} catch (FileNotFoundException e) {
			logger.handleError("File not exist ", configFileName, e);
		}
	}	
	
	
	private void initializeResourcePaths(String folderPath){
		getAutoHome();
		String workHome = (String) GCONFIG.get("WorkHome");
		runHome=(String) GCONFIG.get("RunHome");
		logger.trace("AutoHome:" , G_AUTO_HOME);
		if (G_AUTO_HOME.equals("")) {
			logger.handleError("AUTO_HOME is invalid:" , G_AUTO_HOME);
			return;
		}
		
		initializeLogs(runHome);
		if (runHome == null || !(FileUtils.makeFolder(runHome))) {
			logger.handleError("Can't create/access work folder: " , runHome);

		}		
		
		/*String uniqueName="ExecutedOn_"+uniqueDate();
		runHome=runHome+"/"+uniqueName;
*/		runHome = runHome + "/" + folderPath;
		resourcePaths = ResourcePaths.getInstance(G_AUTO_HOME, workHome,runHome);
		
		if (runHome == null || !(FileUtils.makeFolder(runHome))) {
			logger.handleError("Can't create/access work folder: " , runHome);
		}
	}
	
	
	private String uniqueDate(){
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		Date date = new Date();
		String uniqueDate = sdf.format(date);
		SimpleDateFormat sdf1 = new SimpleDateFormat("HH-mm-ss");
		Date time = new Date();
		String uniqueTime = sdf1.format(time);
		String uniqueName = uniqueDate + "_Time_" + uniqueTime;
		
		return uniqueName;
	}

	
	private LogUtils logger = new LogUtils(this);
	private String G_AUTO_HOME = "";
	private Reporter reporter;
	private String instanceName,runHome;

}
