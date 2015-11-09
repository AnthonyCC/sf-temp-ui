/******************************************************************************
$Id : ScreenDumpManager.java 9/8/2014 1:21:57 PM
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

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.io.FileUtils;

import ui.WebUIDriver;

import javax.imageio.ImageIO;

import ModuleDrivers.CompositeAppDriver;

import cbf.engine.ResultReporter;
import cbf.engine.TestResult;
import cbf.engine.TestResult.EntityType;
import cbf.engine.TestResult.ResultType;
import cbf.harness.ResourcePaths;
import cbf.utils.LogUtils;
import cbf.utils.UniqueUtils;
import cbf.utils.Utils;

/**
 * 
 * Implements ResultReporter and takes screenshots
 * 
 */
public class ScreenDumpManager implements ResultReporter {
	public LogUtils logger = new LogUtils(this);

	/**
	 * Constructor to initialize folder path
	 * 
	 * @param params map containing parameters
	 */
	public ScreenDumpManager(Map params) {
		dumpFolder = (String) params.get("folderpath");
		if(dumpFolder.equals("")){
			dumpFolder=ResourcePaths.singleton.getRunResource("ScreenShots","");
		}
		if(!cbf.utils.FileUtils.makeFolder(dumpFolder)){
			logger.handleError("Cant create/access ScreenShots folder; these will not be generated: " , dumpFolder);
		}
	}

	/**
	 * Closes the dump manager
	 */
	public void close() {
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
	 * Reporter finish method
	 * 
	 * @param result
	 *            execution details
	 * @param rsType
	 * 			result type of the current executed entity
	 * @param details
	 * 			execution details of the current executed entity             
	 */
	public void finish(TestResult result, ResultType rsType, Object details) {
	}

	/**
	 * Logs Screenshot
	 * 
	 * @param result
	 *          entity details
	 * @param rsType
	 * 			result type of the current executed entity
	 * @param details
	 * 			execution details of the current executed entity           
	 */
	public void log(TestResult result, ResultType rsType, Map details) {
		logger.trace("Report log");
		if (!isScreenDump(rsType, result, details)) {
			return;
		}
		//
		// logger.trace(details.containsKey("screenDump"));
		// logger.trace(details.get("screenDump"));
		//
		if (details.get("screenDump").toString().equalsIgnoreCase("true")) {

			String dumpName = makeDumpName(result, details);
			String fileName = dumpName + ".png";
			filePath = dumpFolder + "/" + fileName;
			dumpScreen(filePath);
			result.miscInfo.put("screenDump", Utils.toMap(new String[] {
					"name", dumpName, "fileName", fileName, "filePath",
					filePath }));
		}
	}

	/**
	 * Starts the screenshot process
	 * 
	 * @param result
	 *            object of TestResult
	 */
	public void start(TestResult result) {
	}

	/**
	 * Returns ScreenDumpManager along with screendump file path format string
	 */
	public String toString() {
		return "ScreenDumpManager(" + filePath + ")";
	}
	
	private void dumpScreen(String fileName) {
		logger.trace("DumpScreen : " , fileName);
		Rectangle screenRectangle = new Rectangle(Toolkit.getDefaultToolkit()
				.getScreenSize());
		Robot robot = null;
		try {
			robot = new Robot();
		} catch (AWTException e) {
			logger.handleError("Exception in ScreenDumpManager "
					, e);
		}
		System.out.println("***********TAKE SCREENSHOT**********");
		if(CompositeAppDriver.startUp.equals("IE"))
		{
		BufferedImage image = robot.createScreenCapture(screenRectangle);
		try {
			ImageIO.write(image, "png", new File(fileName));
		} catch (IOException e) {
			logger.handleError("Exception caught while creating screen dump",
					fileName, e.getMessage());
		}
		}
		else
		{
			WebUIDriver driver = new WebUIDriver();
			File ss = driver.takescreenshot();
			try {
				FileUtils.copyFile(ss, new File(fileName));
				System.out.println("***DONE WITH SS*******");
			} catch (IOException e) {
				logger.handleError("Exception caught while creating screen dump",
						fileName, e.getMessage());
			}
		}
			
	}

	private String makeDumpName(TestResult result, Object eventData) {
		String name;
		name = result.parent.parent.parent.entityName;
		name = name + "_" + result.parent.entityName;// step
		name = name + "_" + result.childCount; // check#. Makes sure that sName
		// is unique, even when
		// checkName isnt
		uniqueSuffix = UniqueUtils.getInstance().uniqueString("3");
		name = name + "_" + uniqueSuffix;

		// ' blank out suspicious characters
		Pattern pattern = Pattern.compile("[^\\w-_]+");
		Matcher m = pattern.matcher(name);
		while (m.find()) {
			m.replaceAll(" ");
		}
		return name;
	}

	// ' Determine if screen dump is needed
	private boolean isScreenDump(ResultType eventType, TestResult result,
			Map eventData) {
		if (result.entityType != EntityType.COMPONENT)
			return false;

		boolean isPassed = result.finalRsType.isPassed();
		if ((!isPassed) || result.finalRsType==ResultType.PASSED) { // passed or failed
			return true; // ' Enable screen dump for all passed and failed logs
		}

		return (Boolean) eventData.get("screenDump");
	}

	private String dumpFolder;
	private String filePath;
	String uniqueSuffix ;
}
