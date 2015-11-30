/******************************************************************************
 $Id : WebUIDriver.java 9/8/2014 1:21:28 PM
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

package ui;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.HasInputDevices;
import org.openqa.selenium.interactions.Mouse;
import org.openqa.selenium.internal.Locatable;
import org.openqa.selenium.server.SeleniumCommandTimedOutException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.sikuli.script.FindFailed;

import ModuleDrivers.CompositeAppDriver;
import ModuleDrivers.OrderStorefrontDriver;
import cbf.engine.BaseAppDriver;
import cbf.engine.TestResult.ResultType;
import cbf.utils.LogUtils;
import cbf.utils.SleepUtils;
import cbf.utils.SleepUtils.TimeSlab;

import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.google.common.base.Function;
//import com.opera.core.systems.scope.protos.SelftestProtos.SelftestResult.Result;
import com.thoughtworks.selenium.SeleniumException;
import com.thoughtworks.selenium.webdriven.WebDriverBackedSelenium;

/**
 * 
 * Extends BaseAppDriver and handles all the common functionalities for
 * webControls like setting the TextBox,Selecting an option in Dropdown ,etc..
 * 
 */

public class WebUIDriver extends BaseAppDriver {

	// variables for add to cart
	public String productFullName = null;
	// reorder flag for add to cart
	boolean reorder_page = false;	
	// locator for the default product to be selected
	String first_item = null;
	// alcohol product add to cart
	boolean item_alcohol = false;
	// flag to check if alcohol alert is accepted
	public static boolean item_alcohol_accepted = false;
	// customize product add to cart
	boolean item_customizable = false;
	// item quantity check variables
	double item_initialexistingqty = 0.0;
	double item_finalexistingqty = 0.0;
	double item_newquantity = 0.0;
	double clickerror_added_quantity = 0.0;	
	double hovererror_added_quantity = 0.0;	
	// item quantity for the dropdown textbox
	String item_quantity = "0";
	public static String orderNum=null; 
	public static String fistname=null;
	public String add_method;
	//	public static String fistnameEdit=null;
	public String match=null;
	public String address=null;
	public static TableHandler tableObj;
	public static WebDriverBackedSelenium selenium;
	Actions actions = new Actions(webDriver);
	public boolean reserve_flag = false;
	public String verify_day, verify_time, prdct_category, prdct_department,
	verify_subtotal;
	public static String fd_username;
	public String verify_products[];
	public String verify_products_unit[];
	public String verify_products_quantity[];
	public String Zipcode;
	public String UserID = null;
	public String Password = null;
	public WebDriverWait wait = new WebDriverWait(webDriver, 180);
	public static String CRMWindow;
	// public Configuration GCONFIG = Harness.GCONFIG;
	// custom mouseover
	public RobotPowered robot;
	// This will wait for the page to load for 300 seconds (i.e. 300000 Millie
	// seconds)
	public String PageLoadTime = "300000";
	// defined flag for AlcoholRestriction ;
	public boolean alcoholRestriction = false;

	/**
	 * Overloaded Constructor to initialize webdriver and selenium
	 * 
	 * @param webDriver
	 *            object of WebDriver
	 * @param selenium
	 *            object of WebDriverBackedSelenium
	 */
	public WebUIDriver(Map parameters) {
		BaseAppDriver.webDriver = (WebDriver) parameters.get("webDriver");
		selenium = new WebDriverBackedSelenium(webDriver, (String) parameters
				.get("sBaseUrl"));
		objMap = new ObjectMap();
		uiMap = objMap.uiMap;
		tableObj = new TableHandler();
		try {
			robot = new RobotPowered(webDriver);
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Default constructor
	 */
	public WebUIDriver() {
	}

	/**
	 * Launches the Application in the Browser
	 * 
	 * @param sURL
	 *            URL of the page to be opened.
	 */
	public void waitForPageLoad() {

		try{
			wait.until(new Function<WebDriver, Boolean>() {

				public Boolean apply(WebDriver driver) {
					if(CompositeAppDriver.startUp.equalsIgnoreCase("IE"))
						SleepUtils.getInstance().sleep(TimeSlab.LOW);
					else
						SleepUtils.getInstance().sleep(TimeSlab.YIELD);

					System.out.println("Current Window State       : "
							+ String.valueOf(((JavascriptExecutor) driver).executeScript("return document.readyState")));
					return String
					.valueOf(((JavascriptExecutor) driver).executeScript("return document.readyState"))
					.equals("complete");
				}
			});
		}
		catch(TimeoutException e)
		{
			RESULT.failed("Wait for page to load exception ", "Page should be loaded and status should be COMPLETE within 180 seconds",
			"Page is not loaded in 180 seconds and status is not COMPLETE");
		}

	}

	public void launchApplication(String sURL) {
		try{
			if (CompositeAppDriver.startUp.equalsIgnoreCase("SAFARI")) {
				// Bring Safari browser page to front
				// TODO does not work if there is already a Safari window opened
				String cmd = "osascript -e \"tell app \\\"Safari\\\" to activate\"";
				List<String> command = new ArrayList<String>();
				command.add("/bin/sh");
				command.add("-c");
				command.add(cmd);
				ProcessBuilder pb = new ProcessBuilder(command);
				try {
					pb.start();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					RESULT.failed("Safari browser launch","Safari browser should be launched successfully",
					"Safari browser does not launch successfully");

				}
			}
			webDriver.get(sURL);
		}catch(Exception e){
			RESULT.failed("Launch Application Function","Launch Application Function should be successfully",
			"Launch Application Function failed");
		}
	}

	/**
	 * Checks page title matches or not
	 * 
	 * @param sPageTitle
	 *            title of page opened
	 * @return page match or not value
	 */
	public boolean pageMatch(String sPageTitle) {
		if (sPageTitle.equals(webDriver.getTitle())) {
			return true;
		}
		return false;
	}

	/**
	 * Identifies the Locator as needed by web-webDriver object
	 * 
	 * @param actualLocator
	 *            to be identified
	 * @return Identified locator as Web Element
	 */

	public WebElement getwebDriverLocator(String actualLocator) {
		WebElement element = null;
		try {
			if (actualLocator.startsWith("//")) {
				element = webDriver.findElement(By.xpath(actualLocator));
			} else {
				try {
					element = webDriver.findElement(By.id(actualLocator));
				} catch (Exception e) {
					try {
						element = webDriver.findElement(By.name(actualLocator));
					} catch (Exception e1) {
						try {
							element = webDriver.findElement(By
									.className(actualLocator));
						} catch (Exception e2) {
							try {
								element = webDriver.findElement(By
										.cssSelector(actualLocator));
							} catch (Exception e3) {
								try {
									element = webDriver.findElement(By
											.tagName(actualLocator));
								} catch (Exception e5) {
									try {
										element = webDriver.findElement(By
												.linkText(actualLocator));
									} catch (Exception e4) {
										try {
											element = webDriver
											.findElement(By
													.partialLinkText(actualLocator));
										} catch (Exception e6) {
											logger.error("Element not found ");
										}
									}
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {

			logger.error("Element not found ");
		}
		return element;

	}

	/**
	 * Sets the value to the specified UI Element
	 * 
	 * @param sElementName
	 *            name of element
	 * @param sValue
	 *            value of element
	 */
	public void setValue(String sElementName, String sValue) {
		Map result = null;
		try {
			result = objMap.getElementMap(sElementName);
			if (result != null) {
				// String sLocator = (String) result.get("locator");
				String sLocator = objMap.getLocator(sElementName);
				String sType = (String) result.get("type");
				String sOtherInfo = "";
				try {
					sOtherInfo = (String) result.get("others");
				} catch (NullPointerException e) {
				}
				if (sType.equalsIgnoreCase("textBox")
						|| sType.equalsIgnoreCase("textArea")) {
					if (sOtherInfo != null && (sOtherInfo.matches("ajax")))
						setAjaxElement(sLocator, sValue, (String) result
								.get("ajaxInfo"));
					else {
						try {
							if (checkElement(sLocator, 10)) {
								// getwebDriverLocator(sLocator).click();
								// if(!(getwebDriverLocator(sLocator).getAttribute("value").equals("")
								// ||
								// getwebDriverLocator(sLocator).getAttribute("value")
								// == null)){
								/*
								 * try{ getwebDriverLocator(sLocator).clear();
								 * //Thread.sleep(1000); }
								 * catch(WebDriverException e) {
								 * 
								 * }
								 */
								if (!(sValue == null || sValue.equals(""))) {
									getwebDriverLocator(sLocator).clear();
									getwebDriverLocator(sLocator).sendKeys(
											sValue);
									logger.trace("Typed text '" + sValue
											+ "' in the input element '"
											+ sElementName + "'");
								}
							}
						} catch (Exception e) {
							logger.trace("Error caused By \"" + sElementName
									+ "\" ", e);
						}
					}
				} else if (sType.equalsIgnoreCase("dropDown")
						&& (sValue != null) && (!(sValue.equals("")))) {
					if (checkElement(sLocator, 10)) {
						if (sElementName.matches("productSelection.format")) {
							Thread.sleep(2500);// To Check Format DropDown
						}
						try {
							// if(!(getwebDriverLocator(sLocator).getText().equals("")
							// || getwebDriverLocator(sLocator).getText() ==
							// null)){
							// getwebDriverLocator(sLocator).clear();

							Select selectOption = new Select(
									getwebDriverLocator(sLocator));
							selectOption.selectByVisibleText(sValue);
							logger.trace("Selected label '" + sValue
									+ "' in the input element '" + sElementName
									+ "'");
						} catch (Exception e) {
							logger.trace("Error caused By \"" + sElementName
									+ "\" ", e);
						}
					}
				} else if (sType.equalsIgnoreCase("radioButton")) {
					if (checkElement(sLocator, 10)) {
						if (sValue.equals("Yes")) {
							getwebDriverLocator(sLocator).click();
							logger.trace("Selected the input element '"
									+ sElementName + "'");
						} else if (sValue.equals("No")) {
							getwebDriverLocator(sLocator).click();
							logger.trace("Deselected the input element '"
									+ sElementName + "'");
						}

					}
				} else if (sType.equalsIgnoreCase("checkBox")) {
					if (checkElement(sLocator, 10)) {
						if (sValue.equals("Yes")) {
							getwebDriverLocator(sLocator).click();
							logger.trace("Selected the input element '"
									+ sElementName + "'");
						}
					}
				} else if (sType.equalsIgnoreCase("multiSelectBox")) {
					setMultiSelectBox(sElementName, sValue);
				}

			}
		} catch (NullPointerException e) {
			logger.trace("Error caused By \"" + sElementName + "\" ", e);
		} catch (Exception e) {
			logger.trace("Error caused By \"" + sElementName + "\" ", e);
		}
	}

	/**
	 * Set the value to Incremental box
	 * 
	 * @param sLocator
	 *            name of locator
	 * @param sValue
	 *            value of the locator
	 * @param sAjaxInfo
	 *            ajax info of locator
	 */
	public void setAjaxElement(String sLocator, String sValue, String sAjaxInfo) {
		try {
			if (sAjaxInfo.equals("Calendar"))
				setCalendar(sLocator, sValue);
			else if (sAjaxInfo.equals("ajax")) {
				typeWithAjax(sLocator, sValue);
			} else {
				setIncrementalBox(sLocator, sValue, sAjaxInfo);
			}
		} catch (Exception e) {
			logger.trace("Error caused while setting value to \"" + sLocator
					+ "\"", e);
		}
	}

	/**
	 * Sets the value to the Calendar control
	 * 
	 * @param sLocator
	 *            name of locator
	 * @param sValue
	 *            value of the locator
	 */
	public void setCalendar(String sLocator, String sValue) {
		try {
			if (checkElement(sLocator, 5)) {
				getwebDriverLocator(sLocator).click();
				webDriver.findElement(
						By.linkText(sValue.substring(0, sValue.indexOf("/"))
								.toString())).click();
			}
		} catch (Exception e) {
			logger.trace("Error caused while setting value to \"" + sLocator
					+ "\"", e);
		}
	}

	/**
	 * Sets the value of the Incremental Box
	 * 
	 * @param sLocator
	 *            name of locator
	 * @param sValue
	 *            value of the locator
	 * @param sAjaxInfo
	 *            ajax info of locator
	 */
	public void setIncrementalBox(String sLocator, String sValue,
			String sAjaxInfo) {
		try {
			String sTemp = sValue;
			if (!(sValue.equals(""))) {
				if (sValue.contains(","))
					sValue = sValue.substring(0, sValue.indexOf(","));
				WebElement textBox = getwebDriverLocator(sLocator);
				textBox.sendKeys("");
				textBox.sendKeys(sValue);
				logger.trace("Typed text '" + sValue
						+ "' in the input element '" + sLocator + "'");
				if (checkElementForIncrementalBox("//div[@id='" + sAjaxInfo
						+ "']/ul/li[1]", 30)) {
					if (webDriver
							.findElement(
									By
									.xpath(("//div[@id='" + sAjaxInfo + "']/ul/li[1]")))
									.isDisplayed()) {
						webDriver.findElement(
								By.xpath("//div[@id='" + sAjaxInfo
										+ "']/ul/li[1]")).click();
						logger.trace("Set the Value : '" + sTemp
								+ "' to the input element '" + sLocator + "'");
					}
				}
			}
		} catch (Exception e) {
			logger
			.trace("Error caught Setting Value to the Incremental Box : \" "
					+ sLocator + "\"");
		}
	}

	/**
	 * Setting a value to ajax elements
	 * 
	 * @param sLocator
	 *            name of locator
	 * @param sValue
	 *            value of locator
	 */

	public void typeWithAjax(String sLocator, String sValue) {
		try {
			if (!(sValue.equals(""))) {
				if (sValue.contains(","))
					sValue = sValue.substring(0, sValue.indexOf(","));
				WebElement textBox = getwebDriverLocator(sLocator);
				textBox.sendKeys("");
				textBox.sendKeys(sValue);
				getwebDriverLocator(sLocator).sendKeys(Keys.TAB);
				logger.trace("Typed text '" + sValue
						+ "' in the input element '" + sLocator + "'");
			}
		} catch (Exception e) {
			logger.trace("Error caught Setting Value " + "\" " + sValue
					+ " to the Element: \" " + sLocator + "\"");
		}
	}

	/**
	 * Selects the options from MultiSelectBox
	 * 
	 * @param sMultiSelectBox
	 *            name of multi select box
	 * @param sOptionsToBeSelected
	 *            name of object to be selected
	 */

	public void setMultiSelectBox(String sMultiSelectBox,
			String sOptionsToBeSelected) {
		try {
			getwebDriverLocator(sMultiSelectBox).click();
			try {
				WebElement selectOption = getwebDriverLocator(objMap
						.getLocator("Select All"));
				if (selectOption.isDisplayed()) {
					selectOption.click();
					selectOption.click();
				}
			} catch (SeleniumException e) {
				logger.trace("Error : Unable to find Select All checkbox for "
						+ sMultiSelectBox + " MultiselectBox");
			}

			String sOptions[] = sOptionsToBeSelected.split(";");
			for (String sOption : sOptions) {
				if (checkElement(objMap.getLocator(sOption), 3)) {
					getwebDriverLocator(objMap.getLocator(sOption)).click();
					logger.trace("Selected the Option :" + sOption);
				}
			}
			getwebDriverLocator(objMap.getLocator(sMultiSelectBox)).click();
		} catch (Exception e) {
			logger.trace("Error : caused by " + sMultiSelectBox + " Element");
		}
	}

	/**
	 * Sets the value to all the locators on the specified page.
	 * 
	 * @param sPageName
	 *            name of the Page
	 * @param lData
	 *            to be used to set the values
	 */
	public void setPage(String sPageName, Map lData) {
		List<Map> resultMap = new ArrayList();
		Map result;
		String sElement = "";
		resultMap = objMap.getPageLocators(sPageName);
		setActualStaticPage(sPageName, lData, resultMap);
	}

	/**
	 * Sets the value to the list of selected locators in the specified page
	 * 
	 * @param sPageName
	 *            name of page
	 * @param lData
	 *            to be used to set the values
	 * @param selectedElements
	 *            list of selected elements
	 */
	public void setCustomPage(String sPageName, Map lData,
			List<String> selectedElements) {
		List<Map> resultMap = new ArrayList();
		Map result;
		String sElement = "";
		resultMap = objMap.getPageLocators(sPageName);
		setActualStaticPage(sPageName, lData, objMap
				.getSelectedMappingElements(resultMap, selectedElements));
	}

	/**
	 * Sets the value to the dynamic elements in the specified page
	 * 
	 * @param sPageName
	 *            name of page
	 * @param lData
	 *            to be used to set the values
	 */
	public void setDynamicValues(String sPageName, Map lData) {
		List<Map> resultMap = new ArrayList();
		String sElement = "";
		resultMap = objMap.getDynamicLocators(sPageName);
		setActualDynamicValue(sPageName, lData, resultMap);
	}

	/**
	 * Sets the value to the list of selected dynamic elements in the specified
	 * page
	 * 
	 * @param sPageName
	 *            name of page
	 * @param lData
	 *            to be used to set the values
	 * @param selectedLocators
	 *            list of dynamic locators
	 */
	public void setCustomDynamicValues(String sPageName, Map lData,
			List<String> selectedLocators) {
		List<Map> resultMap = new ArrayList();
		Map result;
		String sElement = "";
		resultMap = objMap.getDynamicLocators(sPageName);
		setActualDynamicValue(sPageName, lData, objMap
				.getSelectedMappingElements(resultMap, selectedLocators));
	}

	/**
	 * Sets the given value to the list of UI elements in the specified page
	 * 
	 * @param sPageName
	 *            name of page
	 * @param lData
	 *            to be used to set the values
	 * @param uiElements
	 *            list of dynamic locators
	 */
	public void setActualDynamicValue(String sPageName, Map lData,
			List<Map> uiElements) {
		String sElement = "";
		int iCount = 0;
		String sDataValues[] = {};
		for (Map uiElement : uiElements) {
			try {
				sElement = ((String) uiElement.get("elementName"));
				if (lData.containsKey(sElement)) {
					String sData = (String) lData.get(sElement);
					String sType = (String) uiElement.get("type");
					if (sType.equals(""))
						sType = getElementType(uiElement, 0);

					if (sType.equals("button")) {
						if (sData.equals("No")) {
							iCount = 0;
							continue;
						} else if (sData.contains("-")
								&& sData.substring(0, sData.indexOf("-"))
								.equals("Yes"))
							iCount = Integer.parseInt(sData.substring(sData
									.indexOf("-") + 1)); // This is to convert
						// the -x value to the
						// no of repetitions.
						else if (sData.equals("Yes"))
							iCount = 1;
					} else if (sType.equals("checkBox")) {
						if (sData.equals("No")) {
							iCount = 0;
							continue;
						} else if (sData.contains("Yes")) {
							iCount = 1;
							sDataValues = sData.split(";");
						}
					} else if (sType.equals("dropDown")
							|| sType.equals("textBox")
							|| sType.equals("textArea")) {
						sDataValues = sData.split(";");
						if (sDataValues.length == 1
								&& !(sDataValues[0].equals("")))
							iCount = 1;
						else if (sDataValues.length > 1)
							iCount = sDataValues.length;
					}
					for (int i = 1; i <= iCount; i++) {
						if (!((((String) uiElement.get("type"))
								.equals("button")) || ((String) uiElement
										.get("type")).equals("radioButton"))) {
							if (sPageName.contains("queue"))
								setDynamicElement(sElement, sDataValues[i],
										i + 1);
							else
								setDynamicElement(sElement, sDataValues[i - 1],
										i);
						} else {
							try {
								if (sData.contains("Yes")) {
									String sTemp = objMap.getLocator(sElement);
									if (sTemp.contains(sDynamicValue1))
										sTemp = sTemp.replaceAll(
												sDynamicValue1, "");
									if (checkElement(sTemp, 60)) {
										try {
											getwebDriverLocator(sTemp).click();
										} catch (Exception e) {
											e.printStackTrace();
											System.out
											.println("Exception in click .. set Dynamic ..");
										}

										Thread.sleep(3000);
										waitForBrowserStability("300");
									}
								} else
									continue;
							} catch (Exception e) {
								logger.trace("Exception Caused by \""
										+ sElement + "\"");
							}
							try {
								Thread.sleep(1000);
							} catch (InterruptedException e) {
							}

						}
					}
				}
			} catch (Exception e) {
				logger.trace("Error : caused by " + sElement);
			}
		}
	}

	/**
	 * Reset the value of the existing dynamic elements with given data
	 * 
	 * @param lData
	 *            to be used
	 * @param sPageName
	 *            name of the page
	 * @param startIndex
	 *            starting index of dynamic element
	 */

	public void UpdateDynamicValues(Map lData, String sPageName, int startIndex) {
		String sElement = "";
		int iCount = 0;
		String sDataValues[] = {};
		List<Map> resultMap = objMap.getDynamicLocators(sPageName);
		for (Map uiElement : resultMap) {
			sElement = ((String) uiElement.get("elementName"));
			if (lData.containsKey(sElement)) {
				String sData = (String) lData.get(sElement);
				String sType = (String) uiElement.get("type");
				if (sType.equals(""))
					sType = getElementType(uiElement, startIndex);

				if (sType.equals("button")) {
					if (sData.equals("No")) {
						iCount = 0;
						continue;
					} else if (sData.contains("-")
							&& sData.substring(0, sData.indexOf("-")).equals(
							"Yes"))
						iCount = Integer.parseInt(sData.substring(sData
								.indexOf("-") + 1)); // This is to convert
					// the -x value to the
					// no of repetitions.
					else if (sData.equals("Yes"))
						iCount = 1;
				} else if (sType.equals("checkBox")) {
					if (sData.equals("No")) {
						iCount = 0;
						continue;
					} else {
						iCount = 1;
						sDataValues = sData.split(";");
					}
				} else if (sType.equals("dropDown") || sType.equals("textBox")
						|| sType.equals("textArea")) {
					sDataValues = sData.split(";");
					if (sDataValues.length == 1 && !(sDataValues[0].equals("")))
						iCount = 1;
					else if (sDataValues.length > 1)
						iCount = sDataValues.length;
				}
				for (int i = startIndex, j = 0; i < (startIndex + iCount); i++, j++) {
					if (!((((String) uiElement.get("type")).equals("button")) || ((String) uiElement
							.get("type")).equals("radioButton"))) {
						setDynamicElement(sElement, sDataValues[j], i);
					} else {
						try {
							if (sData.contains("Yes")) {
								String sTemp = objMap.getLocator(sElement);
								if (sTemp.contains(sDynamicValue1))
									sTemp = sTemp
									.replaceAll(sDynamicValue1, "");
								if (checkElement(sTemp, 60)) {
									getwebDriverLocator(sTemp).click();
									Thread.sleep(3000);
									waitForBrowserStability("300");
								}
							} else
								continue;
						} catch (Exception e) {
							logger.trace("Exception Caused by \"" + sElement
									+ "\"");
						}
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
						}

					}
				}
			}
		}
	}

	/**
	 * Sets the value to the specified dynamic Element
	 * 
	 * @param sElementName
	 *            whose value has to be set
	 * @param sValue
	 *            to be set
	 * @param ix
	 *            index of dynamic element
	 */
	public void setDynamicElement(String sElementName, String sValue, int ix) {
		Map result = null;
		try {
			result = objMap.getElementMap(sElementName);
			if (result != null) {
				// String sLocator = (String) result.get("locator");
				String sLocator = objMap.getLocator(sElementName);
				sLocator = sLocator.replace(sDynamicValue1, "" + ix);
				String sType = (String) result.get("type");
				if (sType.equals(""))
					sType = getElementType(result, ix);
				String sOtherInfo = "";
				try {
					sOtherInfo = (String) result.get("others");
				} catch (NullPointerException e) {
				}
				if ((sType.equalsIgnoreCase("textBox") || sType
						.equalsIgnoreCase("textArea"))
						&& sValue != null) {
					if (sOtherInfo != null && (sOtherInfo.matches("ajax"))) {
						String sAjaxInfo = (String) result.get("ajaxInfo");
						if (sAjaxInfo.contains(sDynamicValue1))
							sAjaxInfo = sAjaxInfo.replace(sDynamicValue1, ""
									+ ix);
						setAjaxElement(sLocator, sValue, sAjaxInfo);
					} else {
						try {
							if (checkElement(sLocator, 3)) {
								getwebDriverLocator(sLocator).sendKeys(sValue);
								logger.trace("Typed text '" + sValue
										+ "' in the input element '"
										+ sElementName + "'");

							}
						} catch (Exception e) {

						}
					}
				} else if (sType.equalsIgnoreCase("dropDown")
						&& (sValue != null)) {
					if (checkElement(sLocator, 10)) {
						try {

							Select selectOption = new Select(
									getwebDriverLocator(sLocator));
							selectOption.selectByVisibleText(sValue);

							logger.trace("Selected label '" + sValue
									+ "' in the input element '" + sElementName
									+ "'");
						} catch (Exception e) {

						}
					}
				} else if (sType.equalsIgnoreCase("checkBox")) {
					if (sValue.contains("-")
							&& sValue.substring(0, sValue.indexOf("-")).equals(
							"Yes") && sOtherInfo != null
							&& (sOtherInfo.matches("series"))) {
						int iCount;
						if (checkElement(sLocator.substring(0, sLocator
								.indexOf("[_" + ix)), 10)) {
							for (int i = 1; i <= (Integer) selenium
							.getXpathCount(sLocator.substring(0,
									sLocator.indexOf("[_" + ix))); i++) {
								if (checkElement(sLocator.replaceFirst(
										"_" + ix, "" + i), 10)) {
									getwebDriverLocator(
											sLocator.replaceFirst("_" + ix, ""
													+ i)).click();
									logger.trace("Unchecked "
											+ sLocator.replaceFirst("_" + ix,
													"" + i));
								}
							}
						}
						if (sValue.substring(sValue.indexOf("-") + 1,
								sValue.length()).equals("All")) {
							iCount = (Integer) selenium.getXpathCount(sLocator
									.substring(0, sLocator.indexOf("[_" + ix)));
						} else {
							iCount = Integer.parseInt(sValue.substring(sValue
									.indexOf("-") + 1, sValue.length()));
						}
						for (int i = 1; i <= iCount; i++) {
							String sTemp = sLocator.replaceFirst("_" + ix, ""
									+ i);
							if (checkElement(sTemp, 10)) {
								getwebDriverLocator(sTemp).click();
								logger.trace("Selected the input element '"
										+ sTemp + "'");
							}
						}
					} else {
						if (checkElement(sLocator, 10)) {
							getwebDriverLocator(sLocator).click();
							selenium.fireEvent(sLocator, "click");
							logger.trace("Selected the input element '"
									+ sElementName + "'");
						}
					}
				} else if (sType.equalsIgnoreCase("multiSelectBox")) {
					setMultiSelectBox(sElementName, sValue);
				}
			} else {
				logger
				.trace("Error : Unknown Element \"" + sElementName
						+ "\"");
			}
		} catch (Exception e) {
			logger.trace("Error caused by Element \"" + sElementName + "\""
					+ e.getMessage());
		}
	}

	/**
	 * Navigates to the Menu
	 * 
	 * @param sMenuList
	 *            list of menu items
	 */
	public void menuNavigation(String sMenuList) {
		String[] aMenu;
		aMenu = sMenuList.split(",");
		try {
			for (int i = 0; i < aMenu.length; i++) {
				if (!(aMenu[i].matches(""))) {
					getwebDriverLocator(objMap.getLocator(aMenu[i])).click();
					logger.trace("Clicked on link: "
							+ objMap.getLocator(aMenu[i]) + ".");
					waitForBrowserStability("1000");
				}
			}
		} catch (Exception e) {
			if (sMenuList.contains(","))
				sMenuList = sMenuList.replaceAll(",", "-->");
			logger.trace("Exception caught while Navigating to  " + sMenuList);
		}
	}

	/**
	 * Retrieves the type of the specified UI element
	 * 
	 * @param uiElement
	 *            map of UI Elements
	 * @param index
	 *            of the Element
	 */
	public String getElementType(Map uiElement, int index) {
		String sElementType = "";
		String sLocator = (String) uiElement.get("locator");
		sLocator = sLocator.replace(sDynamicValue1, "" + index);
		try {
			if (webDriver.findElement(
					By.xpath("//input[@id='" + sLocator + "']")).getAttribute(
					"type").equals("text")) {
				sElementType = "textBox";
			}
		} catch (SeleniumException e) {
			if (webDriver.findElement(
					By.xpath("//select[@id='" + sLocator + "']")).isDisplayed())
				sElementType = "dropDown";
			try {
				if (selenium.getAttribute(
						"//div[@id='" + sLocator + "']/@class").contains(
						"multiselect"))
					sElementType = "multiSelectBox";
			} catch (SeleniumException e1) {
				e1.printStackTrace();
			}
		}
		return sElementType;
	}

	/**
	 * Checks the presence of element up-to specified Time
	 * 
	 * @param sActualLocator
	 *            of the element
	 * @param timeInSec
	 *            time limit
	 * @return Boolean Result
	 */
	public boolean checkElement(String sActualLocator, int timeInSec) {
		WebDriverWait waitForElement = new WebDriverWait(webDriver,
				TimeUnit.MILLISECONDS.toSeconds(500));
		WebElement webElement = getwebDriverLocator(sActualLocator);
		boolean result = false;
		try {
			for (int second = 0;; second++) {
				if (second >= timeInSec * 2) {
					logger.trace("TimeOut waiting for " + sActualLocator + " "
							+ (second / 2) + " Seconds");
					break;
				}
				try {
					if (webElement.isDisplayed()) {
						result = true;
						break;
					}
				} catch (Exception e) {
				}
				waitForElement.until(ExpectedConditions
						.visibilityOf(webElement));
			}
		} catch (Exception e) {
			logger
			.trace("Error: Caused while Verifying the Presence of Element \" "
					+ sActualLocator + " \"");
		}

		return result;
	}

	/**
	 * Checks the presence of element for incremental box
	 * 
	 * @param sActualLocatore
	 *            of the element
	 * @param time
	 *            time limit
	 * @return Boolean Result
	 */
	public boolean checkElementForIncrementalBox(String sActualLocatore,
			int time) {
		boolean result = false;
		WebElement webElement = getwebDriverLocator(sActualLocatore);
		WebDriverWait waitForElement = new WebDriverWait(webDriver,
				TimeUnit.MILLISECONDS.toSeconds(500));
		try {
			for (int second = 0;; second++) {
				if (second >= time * 2) {
					logger.trace("TimeOut waiting for " + sActualLocatore + " "
							+ (second / 2) + " Seconds");
					break;
				}
				try {
					if (webElement.isDisplayed()) {
						result = true;
						break;
					}
				} catch (Exception e) {
				}
				waitForElement.until(ExpectedConditions
						.visibilityOf(webElement));
			}
		} catch (Exception e) {
			logger
			.trace("Error: Caused while Verifying the Presence of Element \" "
					+ sActualLocatore + " \"");
		}

		return result;
	}

	/**
	 * Checks the presence of the specified text,up-to specified Time
	 * 
	 * @param text
	 *            to be verified
	 * @param timeInSec
	 *            time-limit(In Seconds)
	 * @return boolean Result
	 */
	public boolean checkText(String text, int timeInSec) {
		boolean result = false;
		WebDriverWait waitForElement = new WebDriverWait(webDriver,
				TimeUnit.MILLISECONDS.toSeconds(500));

		try {
			for (int second = 0;; second++) {
				if (second >= timeInSec * 10) {
					logger.trace("TimeOut for " + second);
					break;
				}
				try {
					if (webDriver.findElement(By.tagName("body")).getText()
							.contains(text)) {
						result = true;
						break;
					}
				} catch (Exception e) {
				}
				waitForElement.until(ExpectedConditions.visibilityOf(webDriver
						.findElement(By.tagName("body"))));
			}
		} catch (Exception e) {
			logger
			.trace("Error: Caused while Verifying the Presence of Text \" "
					+ text + " \"");
		}
		return result;
	}

	/**
	 * Gets the value of the specified locator
	 * 
	 * @param sElementName
	 *            whose value has to be obtained
	 */
	public String getValue(String sElementName) {
		Map result = null;
		String sValue = "";
		try {
			result = objMap.getElementMap(sElementName);
			if (result != null) {
				// String sLocator = (String) result.get("locator");
				String sLocator = objMap.getLocator(sElementName);
				String sType = (String) result.get("type");
				if (getwebDriverLocator(sLocator).isDisplayed()) {
					if (sType.equalsIgnoreCase("textBox")
							|| sType.equalsIgnoreCase("textArea")) {
						sValue = getwebDriverLocator(sLocator).getText();
					} else if (sType.equalsIgnoreCase("label")) {
						sValue = getwebDriverLocator(sLocator).getText();

					} else if (sType.equalsIgnoreCase("dropDown")) {

						Select selectOption = new Select(
								getwebDriverLocator(sLocator));
						sValue = selectOption.getFirstSelectedOption()
						.getText();

					} else if (sType.equalsIgnoreCase("checkBox")) {
						if (getwebDriverLocator(sLocator).isSelected()) {
							sValue = "Yes";
						} else {
							sValue = "No";
						}
					} else if (sType.equalsIgnoreCase("radioButton")) {
						if (getwebDriverLocator(sLocator).isSelected()) {
							sValue = "Yes";
						} else {
							sValue = "No";
						}
					}
				}
			}
		} catch (NullPointerException e) {
			logger.trace("Error: Element Not Present \" " + sElementName
					+ " \"");
		} catch (Exception e) {
			String eMsg = e.getMessage();
			if (eMsg == "Specified element is not a Select (has no options)")
				System.out.println(" ");
			else if (eMsg == "" + sElementName + " not found")
				logger.trace("" + sElementName + " not found");

		}
		return sValue;
	}

	/**
	 * Gets the value of the specified page
	 * 
	 * @param sPageName
	 *            name of Page
	 * @return List of page values
	 */
	public List<HashMap<String, String>> getPage(String sPageName) {
		List<HashMap<String, String>> elementList = new ArrayList<HashMap<String, String>>();
		String sElement = "";
		String sLocator = "";
		List<Map> result = objMap.getPageLocators(sPageName);
		try {
			for (Map map : result) {
				sElement = ((String) map.get("elementName"));
				sLocator = (String) map.get("locator");
				if (getwebDriverLocator(sLocator).isDisplayed()) {
					if ((!((String) map.get("type")).equals("button"))) {
						HashMap<String, String> pageResult = new HashMap<String, String>();
						pageResult.put(sElement, getValue(sElement));
						elementList.add(pageResult);
					}
				}
			}
		} catch (Exception e) {
			logger
			.trace("Error : Caused while verifying the Values of Page \" "
					+ sPageName + "\"");
		}
		return elementList;
	}

	/**
	 * Checks whether the page is loaded or not
	 * 
	 * @param maxTimeInSec
	 *            time to wait(In seconds)
	 * @return boolean result
	 */
	public boolean waitForBrowserStability(String maxTimeInSec) {
		boolean bResult = false;
		int maxWait = Integer.parseInt(maxTimeInSec);
		int secsWaited = 0;
		try {
			do {
				Thread.sleep(100);
				secsWaited++;
				if (isBrowserLoaded()) {
					bResult = true;
					break;
				}
			} while (secsWaited < (maxWait * 10));
			Thread.sleep(100);
		} catch (Exception e) {
			logger
			.trace("Exception caught while waiting for the page to load ");
			bResult = false;
		}
		return bResult;
	}

	/**
	 * Checks if body of the page is loaded or not
	 * 
	 * @return Boolean Result
	 */
	public boolean isBrowserLoaded() {
		try {
			if ((Boolean
					.parseBoolean(selenium
							.getEval("(\"complete\" == selenium.browserbot.getCurrentWindow().document.readyState)")))) {
				return true;
			} else {
				logger.trace("Waiting for page to load.");
				return false;
			}
		} catch (SeleniumCommandTimedOutException e) {
			e.getMessage();
			return false;
		}
	}

	/**
	 * Retrieves the value of the specified locator
	 * 
	 * @param locatorMap
	 *            of the UI element
	 * @param sActualLocator
	 *            whose value has to be retrieved
	 * @return Value of the specified locator
	 */
	public String getDynamicValue(Map locatorMap, String sActualLocator) {
		String sValue = "";
		String sElementName = "";
		WebElement boxType = getwebDriverLocator(sActualLocator);
		try {
			if (locatorMap != null) {
				sElementName = (String) locatorMap.get("elementName");
				String sType = (String) locatorMap.get("type");
				if (boxType.isDisplayed()) {
					if (sType.equalsIgnoreCase("textBox")
							|| sType.equalsIgnoreCase("textArea")) {
						sValue = boxType.getText();
					} else if (sType.equalsIgnoreCase("dropDown")) {
						sValue = selenium.getSelectedLabel(sActualLocator);
					} else if (sType.equalsIgnoreCase("checkBox")) {
						if (boxType.isSelected()) {
							sValue = "Yes";
						} else {
							sValue = "No";
						}
					} else if (sType.equalsIgnoreCase("radioButton")) {
						if (boxType.isSelected()) {
							sValue = "Yes";
						} else {
							sValue = "No";
						}
					} else if (sType.equalsIgnoreCase("label")) {
						sValue = boxType.getText();
					}
				}
			} else {
				logger
				.trace("Error : Unknown Element \"" + sElementName
						+ "\"");
			}
		} catch (NullPointerException e) {
			logger.trace("Error: Element Not Present \" " + sElementName
					+ " \"");
		} catch (Exception e) {

		}
		return sValue;
	}

	/**
	 * Clicks the specified element,using web-webDriver object
	 * 
	 * @param sElement
	 *            to be clicked
	 */

	public void click(String sElement) {
		try {
			getwebDriverLocator(objMap.getLocator(sElement)).click();
		} catch (Exception e) {
			//				try
			//				{
			getwebDriverLocator(objMap.getLocator(sElement)).click();
			//				}
			//				catch(Exception e1){
			//				}
		}
	}

	/**
	 * Sets the given value to the list of UI elements in the specified page
	 * 
	 * @param sPageName
	 *            name of page
	 * @param lData
	 *            to be used to set the values
	 * @param uiElements
	 *            list of selected UI elements
	 */
	public void setActualStaticPage(String sPageName, Map lData,
			List<Map> uiElements) {
		String sElement = "";
		for (Map uiElement : uiElements) {
			sElement = ((String) uiElement.get("elementName"));
			if (lData.containsKey(sElement)) {
				String sData = (String) lData.get(sElement);
				if (!((((String) uiElement.get("type")).equals("button")) || ((String) uiElement
						.get("type")).equals("radioButton")))
					setValue(sElement, sData);
				else {
					try {
						if (sData.equals("Yes")) {
							String sTemp = objMap.getLocator(sElement);
							if (((String) uiElement.get("type"))
									.equals("button")) {
								if (checkElement(sTemp, 60)) {
									Thread.sleep(4000);
									getwebDriverLocator(sTemp).click();
									waitForBrowserStability("300");

								} else {
									logger.trace("\"" + sElement
											+ " \" Element Not Found");
								}
							} else {
								if (checkElement(sTemp, 60)) {
									getwebDriverLocator(sTemp).click();
									waitForBrowserStability("300");
								} else {
									logger.trace("\"" + sElement
											+ " \" Element Not Found");
								}
							}
						} else
							continue;

					} catch (NullPointerException e) {
						logger.trace("Null pointer Exception Caused by \""
								+ sElement + "\"");
					} catch (Exception e) {
						logger
						.trace("Exception Caused by \"" + sElement
								+ "\"");
					}
				}
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
				}
			}
		}
	}

	/**
	 * Mouse overs on the specified menu,and clicks the sub menu.
	 * 
	 * @param menu
	 *            of the elements
	 * @param subMenu
	 *            menu to be clicked
	 */

	public void mouseOverAndClick(String menu, String subMenu) {
		try {
			WebElement link = webDriver.findElement(By.xpath(objMap
					.getLocator(menu)));
			Locatable hoverItem = (Locatable) link;
			Mouse mouse = ((HasInputDevices) webDriver).getMouse();
			mouse.mouseMove(hoverItem.getCoordinates());
			Locatable hoverSubItem = (Locatable) webDriver.findElement(By
					.linkText(objMap.getLocator(subMenu)));
			mouse = ((HasInputDevices) webDriver).getMouse();
			mouse.click(hoverSubItem.getCoordinates());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Performs the drag and drop operation
	 * 
	 * @param fromLocator
	 *            Contains the locator of source element
	 * @param ToLocator
	 *            Contains the locator of destination element
	 */
	public void dragAndDrop(String fromLocator, String ToLocator) {
		try {
			WebElement From = webDriver.findElement(By.xpath(objMap
					.getLocator(fromLocator)));
			WebElement To = webDriver.findElement(By.xpath(objMap
					.getLocator(ToLocator)));
			Actions builder = new Actions(webDriver);
			Action dragAndDrop = builder.clickAndHold(From).moveToElement(To)
			.release(To).build();
			dragAndDrop.perform();
		} catch (Exception e) {
			logger.warning("Please provide XPath");
		}
	}

	/**
	 * Switch to another window
	 * 
	 * @param title
	 *            Contains title of the new window
	 * @return boolean data i.e True or False
	 */
	public boolean switchToWindow(String title) {
		Set<String> availableWindows = webDriver.getWindowHandles();
		if (availableWindows.size() > 1) {
			for (String windowId : availableWindows) {
				if (webDriver.switchTo().window(windowId).getTitle().equals(
						title))
					return true;
			}
		}
		logger.warning("No child window is available to switch");
		return false;
	}

	/**
	 * Uploads a file
	 * 
	 * @param FilePath
	 *            Contains path of the file which is to be uploaded
	 * @param sBrowse
	 *            Contains locator of the Browse button
	 * @param sUpload
	 *            locator of the Upload button
	 */
	public void FileUpload(String FilePath, String sBrowse, String sUpload) {
		try {
			WebElement webElement = getwebDriverLocator(sBrowse);
			webElement.sendKeys(FilePath);
			if (sUpload != null) {
				uiDriver.click("sUpload");
			}
		} catch (Exception e) {
			logger.error("Invalid File Path: ", e);
		}

	}

	/**
	 * Invokes Enter Key-press Event
	 * 
	 * @param afterTimeInSec
	 *            Time to wait(In Seconds)
	 */
	public void pressEnter(int afterTimeInSec) {
		try {
			Robot robot = new Robot();
			Thread.sleep(afterTimeInSec * 1000);
			robot.keyPress(KeyEvent.VK_ENTER);
			robot.keyRelease(KeyEvent.VK_ENTER);
			waitForBrowserStability("1000");
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Compares actual and expected messages from the application and returns
	 * results accordingly.
	 * 
	 * @param sElement
	 *            for which message to be checked
	 * @param expectedMsg
	 *            expected message
	 * @return boolean result
	 */
	public boolean verifyMsg(String sElement, String expectedMsg) {
		boolean isVerified = false;
		String sLocator = objMap.getLocator(sElement);
		String actualMsg = webDriver.findElement(By.xpath(sLocator)).getText();
		if (actualMsg.equalsIgnoreCase(expectedMsg)) {
			isVerified = true;
		}
		return isVerified;
	}

	/**
	 * Implements keyboard shortcuts
	 * 
	 * @param str
	 *            Give the shortcut you want to use. Example:
	 *            CONTROL_T,CONTROL_V etc.
	 */
	public void pressKeyEvent(String str) throws Exception {
		Robot key = new Robot();
		char c = 0;
		/*
		 * final int VK_CONTROL = 0x11; final int VK_SHIFT = 0x10; final int
		 * VK_ALT = 0x12;
		 */
		try {
			String temp[] = str.split("_");
			for (int i = 0; i < temp.length; i++) {
				if (temp[i].equalsIgnoreCase("CONTROL")) {
					key.keyPress(KeyEvent.VK_CONTROL);
				} else if (temp[i].equalsIgnoreCase("ALT")) {
					key.keyPress(KeyEvent.VK_ALT);
				} else if (temp[i].equalsIgnoreCase("SHIFT")) {
					key.keyPress(KeyEvent.VK_SHIFT);
				} else {
					c = temp[i].charAt(0);
					key.keyPress(Character.toUpperCase(c));
				}
			}
			key.keyRelease(Character.toUpperCase(c));
			key.keyRelease(KeyEvent.VK_SHIFT);
			key.keyRelease(KeyEvent.VK_ALT);
			key.keyRelease(KeyEvent.VK_CONTROL);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Verify Tooltip's text
	 * 
	 * @param ele
	 *            Give element for which Tooltip is visible
	 * @param Expected
	 *            Expected text - Should display
	 */

	public void isTooltipVisible(WebElement ele, String Expected)
	throws Exception {
		try {
			if (!ele.getAttribute("title").isEmpty()) {
				// String str = ((JComponent) ele).getToolTipText();
				String str = ele.getAttribute("title");
				System.out.println("Tooltip text:" + str);
				if (str.contains(Expected)) {
					RESULT.passed("Pass", "Expected",
					"Tooltip's text matches with actual value");
				} else {
					RESULT.failed("Fail", "Unexpected",
					"Tooltip's text does not match with actual value");
				}
			} else {
				RESULT.failed("Fail", "Not Visible",
						"Tooltip is not visible for an element " + ele);
			}
		} catch (Exception e) {
			throw e;
		}

	}

	/**
	 * Verify particular element is present or not on the webpage
	 * 
	 * @param by
	 *            locator
	 * 
	 * @return boolean
	 */

	public boolean isElementPresent(By by) {

		try {
			webDriver.findElement(by);

			return true;
		} catch (NoSuchElementException e) {

			return false;
		}
	}

	/**
	 * Verify particular element is present or not on the webpage within given
	 * time
	 * 
	 * @param by
	 *            locator
	 * 
	 * @param TimeOutinSeconds
	 *            Max time in second
	 * @return boolean
	 */

	public boolean isElementPresent(By by, int TimeOutinSeconds)
	throws InterruptedException {
		webDriver.manage().timeouts().implicitlyWait(TimeOutinSeconds,
				TimeUnit.SECONDS);
		try {
			webDriver.findElement(by);
			webDriver.manage().timeouts().wait();
			return true;
		} catch (NoSuchElementException e) {
			webDriver.manage().timeouts().wait();
			return false;
		}
	}

	/**
	 * Verify particular element is present or not on the webPage
	 * 
	 * @param element
	 *            locator
	 * 
	 * @return boolean
	 */
	public boolean isElementpresentweb(WebElement element) {

		try {
			WebElement ele = element;
			return true;
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	/**
	 * Verifies the text in the javaScript alert pop up box and clicks on either
	 * OK or cancel.
	 * 
	 * @param value
	 *            Expected text
	 * @param ButtonToClick
	 *            It can be "OK" or "Cancel"
	 */

	public void popup_VerifyTextinAlert(String value, String ButtonToClick) {

		Alert alt = webDriver.switchTo().alert();
		String alerttext = alt.getText();
		if (alerttext.matches(value)) {
			RESULT.passed("PASS", "Expected Message present",
					"The expected message is present in the pop up and it is: "
					+ alerttext);
		} else {
			RESULT.failed("FAIL", "Expected Message is not present",
					"The expected message is: '" + value
					+ "' but the actual value is: '" + alerttext + "'");
		}
		if (ButtonToClick.equalsIgnoreCase("yes")
				|| ButtonToClick.equalsIgnoreCase("ok")) {
			alt.accept();
		} else {
			alt.dismiss();
		}

	}

	/**
	 * Clicks OK button on javaScript alert box
	 * 
	 */
	public void popup_ClickOkOnAlert() {
		try
		{
			Alert alt = webDriver.switchTo().alert();
			alt.accept();
		}
		catch(Exception e)
		{
			RESULT.warning("Handle alert ", "Alert should be handled", "Alert might not be handled");
		}

	}

	/**
	 * Clicks Cancel button on javaScript alert box
	 * 
	 */

	public void popup_ClickCancelOnAlert() {

		Alert alt = webDriver.switchTo().alert();
		alt.dismiss();
	}

	/**
	 * Sets the text in the javaScript alert pop up box
	 * 
	 * @param value
	 *            Text you want to set in pop up
	 */

	public void popup_SetTextinAlert(String value) {

		Alert alt = webDriver.switchTo().alert();
		alt.sendKeys(value);
		alt.accept();
	}

	/**
	 * Gets the text from the javaScript alert pop up box
	 * 
	 * @return String
	 */

	public String popup_getAlertText() {

		Alert alt = webDriver.switchTo().alert();
		return alt.getText();
	}

	/**
	 * Verify Alert
	 * 
	 * @param TimeOutinSeconds
	 *            Give max time limit
	 * @return boolean
	 */

	public boolean popup_isAlertPresent(int TimeOutinSeconds) {

		for (int i = 0; i < TimeOutinSeconds; i++) {
			try {
				Thread.sleep(500);
				Alert alt = webDriver.switchTo().alert();
				return true;
			} catch (Exception e) {
			}
		}
		return false;
	}

	/**
	 * For Highlighting an element
	 * 
	 * @param ele
	 *            Locator
	 * @param driver
	 *            Current driver
	 */

	public void DrawHighlight(String ele, WebDriver driver)
	throws InterruptedException {

		WebElement element = uiDriver.getwebDriverLocator(objMap
				.getLocator(ele));
		// Configuration GCONFIG = Harness.GCONFIG;
		if (CompositeAppDriver.startUp.equalsIgnoreCase("IE")) {
			JavascriptExecutor js = ((JavascriptExecutor) driver);
			String bgcolor = element.getCssValue("backgroundColor");
			for (int i = 0; i < 2; i++) {
				js.executeScript("arguments[0].style.border='4px solid red'",
						element);
				// js.executeScript("arguments[0].style.backgroundColor = '"+"rgb(0,200,0)"+"'",
				// element);
				// js.executeScript(bgcolor,element,js);
				Thread.sleep(500);
				js.executeScript("arguments[0].style.border='none'", element);
			}
		}
		else
		{
			// draw a border around the found element
			for (int i = 0; i < 2; i++) {
				JavascriptExecutor js = (JavascriptExecutor) webDriver;
				js.executeScript(
						"arguments[0].setAttribute('style', arguments[1]);",
						element, "color: red; border: 5px solid red;");
				Thread.sleep(500);
				js.executeScript(
						"arguments[0].setAttribute('style', arguments[1]);",
						element, "");
			}
		}


	}

	/**
	 * For Double clicking on any element
	 * 
	 * @param element
	 *            Locator
	 */

	public void doubleclick(WebElement element) {
		Actions dblclick = new Actions(webDriver);
		dblclick.doubleClick(element).build().perform();
	}

	/**
	 * Check the running process and kill it
	 * 
	 * @param serviceName
	 *            Give name of the process that you want to kill
	 * @return Boolean
	 */

	public static boolean isProcessRunningAndKill(String serviceName)
	throws IOException {
		boolean boolRunning = false;
		String line;
		Process p = Runtime.getRuntime().exec(
				System.getenv("windir") + "\\system32\\" + "tasklist.exe");
		BufferedReader input = new BufferedReader(new InputStreamReader(p
				.getInputStream()));
		while ((line = input.readLine()) != null) {
			System.out.println("Task : " + line);
			if (line.contains(serviceName)) {
				boolRunning = true;
				Runtime.getRuntime().exec("taskkill /f /IM " + serviceName);
			}
		}
		input.close();
		return boolRunning;
	}

	/**
	 * Check that text is present on the page
	 * 
	 * @param text
	 *            Give name of the text you want to search
	 * @return Boolean
	 */

	public static boolean isTextPresent(String text) {
		List<WebElement> foundElements = webDriver.findElements(By
				.xpath("//*[contains(text(), '" + text + "')]"));
		return foundElements.size() > 0;
	}

	/**
	 * Click/mouseHover the specified element fetching it from the data
	 * file,using web-webDriver object
	 * 
	 * @param sValue
	 *            to be clicked or mouseHover
	 */
	public void clickFromData(String sValue) {

		Map result = null;
		String[] parts = null;
		String valueForMouseHOver = null, valueForClick = null;
		// Checks for the underscore in the sValue String.
		if (sValue.contains("_")) {
			parts = sValue.split("_");
			valueForClick = parts[parts.length - 1];
			for (int i = 1; i < parts.length; i++) {
				valueForMouseHOver = parts[i];

				for (Map map : uiMap) {
					String temp = (String) map.get("elementName");
					if (valueForMouseHOver.matches(temp)
							|| valueForMouseHOver.equals(temp)) {
						result = map;
						break;
					}
				}
				if (result != null) {
					try {
						String loc = (String) result.get("locator");
						if (valueForMouseHOver.matches(valueForClick)
								|| valueForMouseHOver.equals(valueForClick)) {
							getwebDriverLocator(loc).click();
						} else {
							Actions action = new Actions(webDriver);
							action.moveToElement(
									uiDriver.getwebDriverLocator(loc)).build()
									.perform();
							SleepUtils.getInstance().sleep(TimeSlab.YIELD);
						}
					} catch (Exception e) {
						logger.trace(e.getMessage());
					}

				}
			}
		}

		else if (sValue.contains("->")) {
			parts = sValue.split("->");
			for (int i = 0; i < parts.length; i++) {
				valueForMouseHOver = parts[i];

				for (Map map : uiMap) {
					String temp = (String) map.get("elementName");
					if (valueForMouseHOver.matches(temp)
							|| valueForMouseHOver.equals(temp)) {
						result = map;
						break;
					}
				}
				if (result != null) {
					String loc = (String) result.get("locator");
					getwebDriverLocator(loc).click();

				}
			}
		} else {

			try {
				for (Map map : uiMap) {
					String temp = (String) map.get("elementName");
					if (sValue.matches(temp) || sValue.equals(temp)) {
						result = map;
						break;
					}
				}
				if (result != null) {
					String loc = (String) result.get("locator");
					getwebDriverLocator(loc).click();
				}
			} catch (NullPointerException e) {
				RESULT.failed("Click on link ", "Should not click on link "+sValue, "Not able to click on link "+sValue+"Error "+e);
                  return;
				// e.printStackTrace();
			}
		}
	}

	/**
	 * 
	 * Inner class of WebUIDriver and handles all the common functionalities for
	 * table
	 */

	public class TableHandler {
		/**
		 * For getting total number of columns in table
		 * 
		 * @param sElementName
		 *            Element name for table locator - uiMap
		 * @return int - Number of columns
		 */

		public int getColumnCount(String sElementName) {
			int col_count = 0;
			Map result = null;
			result = objMap.getElementMap(sElementName);
			if (result != null) {
				// String sLocator = (String) result.get("locator");
				String sLocator = objMap.getLocator(sElementName);
				String sType = (String) result.get("type");
				if (sType.equalsIgnoreCase("table")) {
					col_count = getwebDriverLocator(sLocator).findElements(
							By.tagName("td")).size();
				}
			}
			return col_count;
		}

		/**
		 * For getting total number of rows in table
		 * 
		 * @param sElementName
		 *            Element name for table locator - uiMap
		 * @return int - Number of rows
		 */
		public int getRowCount(String sElementName) {
			int row_count = 0;
			Map result = null;
			result = objMap.getElementMap(sElementName);
			if (result != null) {
				// String sLocator = (String) result.get("locator");
				String sLocator = objMap.getLocator(sElementName);
				String sType = (String) result.get("type");
				if (sType.equalsIgnoreCase("table")) {
					row_count = getwebDriverLocator(sLocator).findElements(
							By.tagName("tr")).size();
				}
			}
			return row_count;
		}

		/**
		 * For getting total number of columns for particular row in table
		 * 
		 * @param tableElement
		 *            Element of table
		 * @return int - Number of columns
		 */

		public int getColumnCountOfRow(WebElement tableElement) {
			// return tableElement.findElements(By.tagName("td")).size();
			List<WebElement> td_collection = tableElement.findElement(
					By.tagName("tr")).findElements(By.tagName("td"));

			if (td_collection.size() == 0) {
				td_collection = tableElement.findElement(By.tagName("tr"))
				.findElements(By.tagName("th"));
			}
			return td_collection.size();
		}

		/**
		 * For getting an object of the table - name matches with given text
		 * 
		 * @param textTosearch
		 *            Name of the table you want to search
		 * @return WebElement - Object of table
		 */
		public WebElement getTableObjectByText(String textTosearch) {
			List<WebElement> tablecollection = webDriver.findElements(By
					.tagName("table"));
			int FinalIndex = 0;
			for (int i = 0; i < tablecollection.size(); i++) {
				if (tablecollection.get(i).getText().contains(textTosearch)) {
					FinalIndex = i;
					break;
				}
				if (i == tablecollection.size() - 1) {
					RESULT.failed("FAIL", "The table does not exist",
							"The table with the given text,'" + textTosearch
							+ "' is not present");
				}
			}
			return tablecollection.get(FinalIndex);
		}

		/**
		 * For getting data of particular cell
		 * 
		 * @param tableElement
		 *            Element of the table
		 * @param row
		 *            Row number
		 * @param col
		 *            Column number
		 * @return String - Data of that particular cell
		 */

		public String getCellData(WebElement tableElement, int row, int col) {
			// int RowCount=GetRowCount(tableElement);
			List<WebElement> tr_Collection = tableElement.findElements(By
					.tagName("tr"));
			int row_num = 0;
			String Data = null;
			for (WebElement trElement : tr_Collection) {

				if (row_num == row) {
					List<WebElement> td_collection = trElement.findElements(By
							.tagName("td"));

					if (td_collection.size() == 0) {
						td_collection = trElement
						.findElements(By.tagName("th"));
					}
					Data = td_collection.get(col).getText();
					break;
				}

				row_num++;
			}
			return Data;
		}

		/**
		 * For getting data of particular cell
		 * 
		 * @param tableElement
		 *            Element of the table
		 * @param row
		 *            Row number
		 * @param colName
		 *            Column name
		 * @return String - Data of that particular cell
		 */

		public String getCellData(WebElement tableElement, int row,
				String colName) {
			// int RowCount=GetRowCount(tableElement);
			List<WebElement> tr_Collection = tableElement.findElements(By
					.tagName("tr"));
			int row_num = 0;
			int col_num = 0;
			String Data = null;
			for (WebElement trElement : tr_Collection) {
				if (row_num == 0) {
					List<WebElement> td_col = trElement.findElements(By
							.tagName("td"));
					if (td_col.size() == 0) {
						td_col = trElement.findElements(By.tagName("th"));
					}
					for (int i = 0; i < td_col.size(); i++) {
						if (td_col.get(i).getText().contains(colName)) {
							col_num = i;
							break;
						}
					}
				}

				if (row_num == row) {
					List<WebElement> td_collection = trElement.findElements(By
							.tagName("td"));
					Data = td_collection.get(col_num).getText();
					break;
				}

				row_num++;
			}
			return Data;

		}

		/**
		 * For getting data of particular cell
		 * 
		 * @param tableElement
		 *            Element of the table
		 * @param rowName
		 *            Row name
		 * @param colName
		 *            Column name
		 * @return String - Data of that particular cell
		 */
		public String getCellData(WebElement tableElement, String rowName,
				String colName) {

			// int RowCount=GetRowCount(tableElement);
			List<WebElement> tr_Collection = tableElement.findElements(By
					.tagName("tr"));
			int row_num = 0;
			int col_num = 0;

			String Data = null;
			for (WebElement trElement : tr_Collection) {
				if (row_num == 0) {
					List<WebElement> td_col = trElement.findElements(By
							.tagName("td"));

					for (int i = 0; i < td_col.size(); i++) {
						if (td_col.get(i).getText().contains(colName)) {
							col_num = i;
							break;
						}
					}
				}
				List<WebElement> td_col = trElement.findElements(By
						.tagName("td"));
				if (td_col.get(0).getText().contains(rowName)) {
					Data = td_col.get(col_num).getText();
					break;
				}
				row_num++;
			}
			return Data;

		}

		/**
		 * Overriding toString() method to return TableHandler format string
		 */
		public String toString() {
			return "TableHandler()";
		}

	}

	/**
	 * Function Name: FD_login Purpose: For Login in to FreshDirect application
	 * Created By: Avani Thakkar Created Date: 15th May,2015 Modified By:
	 * Modified Date:
	 * 
	 * @param UID
	 *            txtuseriD for log in
	 * @param txtpass
	 *            password for particular user
	 * @return void
	 */

	public void FD_login(String UID, String PASS) throws InterruptedException {
		if (webDriver.findElements(By.xpath(objMap.getLocator("btngoAnonymous"))).size() > 0) 
		{
			try
			{
				uiDriver.setValue("txtuserNameAnonymous", UID);
				uiDriver.setValue("txtuserPwdAnonymous", PASS);
				SleepUtils.getInstance().sleep(TimeSlab.YIELD);
				uiDriver.click("btngoAnonymous");
				SleepUtils.getInstance().sleep(TimeSlab.YIELD);
			}
			catch(Exception e)
			{
				RESULT.failed("Anonymous user Login ", "User should be able to login", "User is unable to login");
				return;
			}
		}
		else 
		{
			try
			{
				uiDriver.click("btnlogin");
				uiDriver.setValue("txtuserID", UID);
				uiDriver.setValue("txtpass", PASS);
				SleepUtils.getInstance().sleep(TimeSlab.YIELD);
				if (CompositeAppDriver.startUp.equalsIgnoreCase("IE"))
				{
					uiDriver.getwebDriverLocator(objMap.getLocator("btnsignin")).sendKeys("\n");
				}
				else
				{
				uiDriver.click("btnsignin");
				}
				SleepUtils.getInstance().sleep(TimeSlab.YIELD);
			}
			catch(Exception e)
			{
				RESULT.failed("User Login ", "User should be able to login", "User is unable to login");
			}
		}
		fd_username = UID;
		waitForPageLoad();
	}

	@SuppressWarnings("deprecation")
	public boolean isDisplayed(String sElementName) {
		try {
			waitForPageLoad();

			if(uiDriver.getwebDriverLocator(
					objMap.getLocator(sElementName)).isDisplayed())
				return true;
			else
				return false;

		} catch (NoSuchElementException e) {
			RESULT.failed("Visibility of an element", sElementName+" element should be available", sElementName+" element is not available");
			return false;// System.out.println(".........");
		} catch (NullPointerException e) {
			RESULT.failed("Visibility of an element", sElementName+" element should be available", sElementName+" element refers to null");
			return false;//
		}
		catch(ElementNotVisibleException e)
		{
			RESULT.failed("Visibility of an element", sElementName+" element should be available", sElementName+" element refers to null");
			return false;
		}

	}

	/* Precondition : User should be logged in first */
	private enum filterType {
		Filter, department, special_pref, search, list, no_action
	};

	public void FD_reorderYourList(String YourList, String Department,
			String SpecialPref, String FlexibilityFlag) throws FindFailed,
			InterruptedException, TimeoutException {
		int i = 0, flag = 0;

		int count,flag_dept=0,flag_pref=0;
		String listName, Dept_text, spl_text;
		// String reorder = webDriver.getTitle();
		WebElement listheader;
		WebElement breadcumbs_1;

		// * For multiple data - the component is invoked(different data) For
		// the first invocation it will click Reorder tab
		try {
			try
			{
				if (!(webDriver.findElements(By.xpath(objMap.getLocator("tabyourList"))).size() > 0)) 
				{
					// click reorder tab
					uiDriver.getwebDriverLocator(objMap.getLocator("lnkreorder"))
					.click();
					wait.until(ExpectedConditions.visibilityOfElementLocated(By
							.xpath(objMap.getLocator("tabyourList"))));
					uiDriver.getwebDriverLocator(objMap.getLocator("tabyourList"))
					.click();
				}
			}
			catch(Exception e)
			{
				RESULT.failed("Reorder Your List : Reorder tab on Homepage", 
						"Application should navigate to reorder page and Reorder Your List should be clicked successfully",
				"Application could not click on Reorder Your List tab successfully");
			}


			try {
				wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(objMap.getLocator("lstreorderShoppingLists"))));
			} 
			catch (TimeoutException e) 
			{
				RESULT.failed("Reorder Your List", "Shopping list section should be available", "Shopping list section not available");
				return;
			}
			count=0;
			if (webDriver.findElements(By.xpath(objMap.getLocator("lstreorderShoppingLists"))).size() > 0) {
				List<WebElement> shoppinglist = webDriver.findElements(By.xpath(objMap.getLocator("lstreorderShoppingLists")));
				if (!shoppinglist.isEmpty()) {
					for (i = 0; i < shoppinglist.size(); i++)
					{
						listName = shoppinglist.get(i).getText();
						listName=listName.split(" \\(")[0];

						if (listName.equals(YourList)) 
						{
							shoppinglist.get(i).findElement(By.xpath(objMap.getLocator("radlistName"))).click();
							try {
								do {
									SleepUtils.getInstance().sleep(TimeSlab.YIELD);
									listheader = uiDriver.getwebDriverLocator(objMap.getLocator("strreorderYourListHeader"));
									count++;
								} while (!listheader.getText().contains(YourList)&& count < 5);

								if (listheader.getText().contains(YourList)) {
									flag = 1;
									RESULT.passed("Your List",
											YourList + " should be selectd",
											YourList + " selectd");
								} else
									RESULT.failed("Your List",
											YourList + " should be selectd",
											YourList + " is not selectd");

							} catch (Exception e) {
								RESULT.warning("Reorder Your List", "User given shopping list should be selected successfully", "Unsuccessful to select shopping list "+YourList);
							}
							break;
						}
					}
				}
				else
				{
					RESULT.warning("Reorder Your List", "Shopping List should be available to select", "No list available");
				}
				if (flag == 0) {
					RESULT.warning("List match", YourList
							+ " List should be Available", YourList
							+ " List is not Available");

					if (FlexibilityFlag.equalsIgnoreCase("Yes")|| FlexibilityFlag.isEmpty()) 
					{
						/* Split the string with ' \\('
						 * e.g. product_text=list1 (1). Breadcrum is compared with list only
						 */
						String defaultList = shoppinglist.get(0).getText();
						defaultList=defaultList.split(" \\(")[0];
						shoppinglist.get(0).findElement(By.xpath(objMap.getLocator("radlistName"))).click();

						count=0;
						try {
							do {
								SleepUtils.getInstance().sleep(TimeSlab.YIELD);
								listheader = uiDriver.getwebDriverLocator(objMap.getLocator("strreorderYourListHeader"));
								count++;
							} while (!listheader.getText().contains(defaultList)&& count < 5);

							if (listheader.getText().contains(defaultList)) {
								flag = 1;
								RESULT.passed("Reorder Your List",
										defaultList + " should be selected",
										defaultList + " selectd");
							} else
								RESULT.failed("Reorder Your List",
										defaultList + " should be selected",
										defaultList + " is not selected");

						} catch (Exception e) {
							RESULT.warning("Reorder Your List", "Default shopping list should be selected successfully", "Unsuccessful to select shopping list "+defaultList);
						}
					}
				}
				if ((flag == 0) && FlexibilityFlag.equalsIgnoreCase("No")) {
					RESULT.passed("Select List",
							"User should not select any List by default",
					"List is not selected!!!!");
					return;
				}

				List<WebElement> departmentList = webDriver.findElements(By.xpath(objMap.getLocator("lstreorderDept")));
				if (!Department.isEmpty()) 
				{
					for (i = 0; i < departmentList.size(); i++) 
					{
						Dept_text = departmentList.get(i).getText();
						if (Dept_text.contains(Department))
						{
							departmentList.get(i).findElement(By.xpath(objMap.getLocator("lstdepartmentName"))).click();
							count=0;
							try {
								do {
									SleepUtils.getInstance().sleep(TimeSlab.YIELD);
									breadcumbs_1 = uiDriver.getwebDriverLocator(objMap.getLocator("lstreorderBreadcumbs"));
									count++;
								} while (!breadcumbs_1.getText().contains(Department)
										&& count < 5);

								if (breadcumbs_1.getText().contains(Department)) {
									flag_dept=1;
									RESULT.passed("Deaprtment selection",
											Department + " should be selected",
											Department + " selected");
									break;
								} else
									RESULT.failed("Deaprtment selection",
											Department + " should be selected",
											Department + " is not selected");

							} catch (Exception e) {
								RESULT.warning("Reorder Your List", "User given department should be selected successfully", "Unsuccessful to select department "+Department);
							}

						}
					}
					if (flag_dept == 0) {
						RESULT.warning("Department Selection",
								"Given Department should be avavilablae",
						"Given Department is not available");

						String default_dept=departmentList.get(1).getText();
						/* Split the string with ' \\('
						 * e.g. product_text=Vegetables (1). Breadcrum is compared with Vegetables only
						 */
						default_dept=default_dept.split(" \\(")[0];
						departmentList.get(1).findElement(By.xpath(objMap.getLocator("lstdepartmentName"))).click();
						count=0;
						try {
							do {
								SleepUtils.getInstance().sleep(TimeSlab.YIELD);
								breadcumbs_1 = uiDriver.getwebDriverLocator(objMap.getLocator("lstreorderBreadcumbs"));
								count++;
							} while (!breadcumbs_1.getText().contains(default_dept) && count < 5);

							if (breadcumbs_1.getText().contains(default_dept)){

								RESULT.passed("Deaprtment selection",
										default_dept + " should be selectd",
										default_dept + " selectd");

							} else
								RESULT.failed("Deaprtment selection",
										default_dept + " should be selectd",
										default_dept + " is not selectd");

						} catch (Exception e) {
							RESULT.warning("Reorder Your List "," Default selected department breadcrum should be validated", default_dept+" default department breadcrum validation failed");
						}
					}
				}
				else
				{
					RESULT.warning("Reorder Your List", "Department should be available to select", "No department available");
				}
				if ((webDriver.findElements(By.xpath(objMap.getLocator("lstreorderSpecPref"))).size() > 0)
						&& !SpecialPref.isEmpty()) 
				{
					String Pref[] = SpecialPref.split(",");
					List<WebElement> special_prefList = webDriver.findElements(By.xpath(objMap.getLocator("lstreorderSpecPref")));
					for (int x = 0; x < Pref.length; x++) {
						if ((webDriver.findElements(By.xpath(objMap.getLocator("lstreorderSpecPref"))).size() > 0)) {

							for (i = 0; i < special_prefList.size(); i++) 
							{
								spl_text = special_prefList.get(i).getText();
								if (spl_text.contains(Pref[x])) {
									flag_pref++;

									if (CompositeAppDriver.startUp.equalsIgnoreCase("SAFARI"))
										special_prefList.get(i).findElement(By.xpath(objMap.getLocator("radlistName"))).click();
									else
										special_prefList.get(i).findElement(By.xpath(objMap.getLocator("radlistName"))).sendKeys(Keys.SPACE);
									try {
										count=0;
										do {
											SleepUtils.getInstance().sleep(TimeSlab.YIELD);
											breadcumbs_1 = uiDriver.getwebDriverLocator(objMap.getLocator("lstreorderBreadcumbs"));
											count++;
										} while (!breadcumbs_1.getText().contains(Pref[x])&& count < 10);
										if (breadcumbs_1.getText().contains(Pref[x]))
										{
											RESULT.passed(
													"Your List",
													Pref[x]+ " should be checked",
													Pref[x]+ " checked");
											break;
										} else
											RESULT.failed(
													"Your List",
													Pref[x]+ " should be checked",
													Pref[x]+ " is unchecked");

									} catch (Exception e) {
										RESULT.warning("Reorder Your List "," Only show : special preferences breadcrum should be validated", Pref[x]+" special preferences breadcrum validation failed");
									}
								}/*
								 * else{ RESULT.warning("Special preference",
								 * "Special preference should be available ",
								 * "Given Special preference is not available for selected department "
								 * + Pref[x] ); }
								 */
							}
						}
					}
					if(flag_pref==0)
					{
						RESULT.warning(
								"Special Preference Selection",
								"Given Special Preference should be avavilablae",
						"Special Preference is not available");

						WebElement selected_preference = special_prefList.get(0).findElement(By.xpath(objMap.getLocator("radlistName")));
						String product_text = special_prefList.get(0).getText();
						/* Split the string with ' \\('
						 * e.g. product_text=Organic (1). Breadcrum is compared with Organic only
						 */
						product_text = product_text.split(" \\(")[0];

						if (CompositeAppDriver.startUp.equalsIgnoreCase("SAFARI"))
							selected_preference.click();
						else
							selected_preference.sendKeys(Keys.SPACE);
						SleepUtils.getInstance().sleep(TimeSlab.YIELD);
						count=0;
						try {
							do {
								SleepUtils.getInstance().sleep(TimeSlab.YIELD);
								breadcumbs_1 = uiDriver.getwebDriverLocator(objMap.getLocator("lstreorderBreadcumbs"));
								count++;
							} while (!breadcumbs_1.getText().contains(product_text)&& count < 10);
							if (breadcumbs_1.getText().contains(product_text))
							{
								RESULT.passed(
										"Reorder Your List",
										product_text+ " should be checked",
										product_text+ " checked");

							} else
								RESULT.failed(
										"Reorder Your List",
										product_text+ " should be checked",
										product_text+ " is unchecked");

						} catch (Exception e) 
						{
							RESULT.warning("Reorder Your List "," Default special preferences breadcrum should be validated", product_text+" special preferences breadcrum validation failed");
						}


					}
				} else {
					RESULT.log(
							"Splpref not found or No splpref data",
							ResultType.PASSED,
							"Splpref should be visible and splpref data should be provided",
							"Splpref not found because may be there is no Splpref for selected Department "
							+ Department
							+ " or No splpref data given.",
							true);
				}
			} else {
				RESULT.warning("Reorder Your List", "Your List section should be available",
				"Your list section not available");
			}
		} catch (Exception e) {
			RESULT.failed("Reorder Your List Exception", "Your List selection should be successful",
			"Your list selection is not successful");
		}

	}

	public void FD_displayValidation(String Element) {
		if (uiDriver.isDisplayed(Element)) {
			RESULT.passed(Element + " Validation of ", Element
					+ " should be displayed", Element
					+ " is displayed successfuly");
		} else {
			RESULT.failed(Element + " Validation of ", Element
					+ " should be displayed", Element + " is not displayed");
			return;
		}
	}

	private enum topItemsType {
		DEPARTMENT, SEARCH
	};

	public void FD_reorderYourTopItems(String Filter, String DepartmentOrItem,
			String SpecialPref, String FlexibilityFlag) throws FindFailed,
			InterruptedException, TimeoutException {
		int i = 0;
		int flag = 0;
		int count ;
		int flag_pref=0;
		String product_text;
		WebElement breadcumbs_1;
		Filter = Filter.toUpperCase();
		try {
			/*
			 * For multiple data - the component is invoked(different data) For the
			 * first invocation it will click Reorder tab
			 */
			if (!(webDriver.findElements(By.xpath(objMap.getLocator("tabpastOrder"))).size() > 0)) {
				try{
					uiDriver.getwebDriverLocator(objMap.getLocator("lnkreorder"))
					.click();
					wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(objMap.getLocator("tabyourTopItems"))));
					FD_displayValidation("tabyourTopItems");
					uiDriver.getwebDriverLocator(objMap.getLocator("tabyourTopItems")).click();
					wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(objMap.getLocator("lstreorderDept"))));
					//FD_displayValidation("lstreorderDept");
				}
				catch (Exception e) 
				{
					RESULT.warning("Your Top Items", "Application should navigate to reorder page and Your Top Items should be clicked successfully",
					"Application could not click on Your Top Items tab successfully");
				}
			}
			else 
			{
				try {
					wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(objMap.getLocator("tabyourTopItems"))));
					uiDriver.getwebDriverLocator(objMap.getLocator("tabyourTopItems")).click();
					try
					{
						wait.until(ExpectedConditions.stalenessOf(uiDriver.getwebDriverLocator(objMap.getLocator("lstreorderDept"))));
					}
					catch(Exception e)
					{
						try
						{
							wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(objMap.getLocator("lstreorderDept"))));
						}
						catch(Exception e1)
						{
							RESULT.failed("Your Top Items", "Your Top Items tab should be successfully clicked and department list should be available",
							"Your Top Items tab is either not clicked or department list is not available");
							return;
						}

					}
					wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(objMap.getLocator("lstreorderDept"))));
					//FD_displayValidation("lstreorderDept");
				} catch (Exception e) {

					RESULT.failed("Your Top Items", "Your Top Items tab should be successfully clicked and department list should be available",
					"Your Top Items tab is either not clicked or department list is not available");
					return;
				}
			}

			switch (topItemsType.valueOf(Filter)) {

			case DEPARTMENT:

				/* Check the user input department from past order */
				//wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(objMap.getLocator("lstreorderDept"))));
				List<WebElement> departmentList = webDriver.findElements(By.xpath(objMap.getLocator("lstreorderDept")));
				if (!DepartmentOrItem.isEmpty()) {
					for (i = 0; i < departmentList.size(); i++) {
						product_text = departmentList.get(i).getText();

						if (product_text.contains(DepartmentOrItem)) {

							departmentList.get(i).findElement(By.xpath(objMap.getLocator("lstdepartmentName"))).click();
							count=0;
							try {
								//wait for breadcrum to load
								do {
									SleepUtils.getInstance().sleep(TimeSlab.YIELD);
									breadcumbs_1 = uiDriver.getwebDriverLocator(objMap.getLocator("lstreorderBreadcumbs"));
									count++;
								} while (!breadcumbs_1.getText().contains(DepartmentOrItem)
										&& count < 5);

								if (breadcumbs_1.getText().contains(DepartmentOrItem)) {
									flag = 1;
									RESULT.passed("Your Top Items",
											DepartmentOrItem+ " should be selected",
											DepartmentOrItem + " selected");
								} else
									RESULT.failed("Your Top Items",
											DepartmentOrItem+ " should be selected",
											DepartmentOrItem+ " is not selected");

							} catch (Exception e) {
								RESULT.warning("Reorder Your Top Items", DepartmentOrItem+" should be selected", DepartmentOrItem+" selection caught exception");
							}
							break;
						}
					}
					if (flag == 0) {
						RESULT.warning("Reorder Your Top Items", DepartmentOrItem
								+ " Departmant should be Available",
								DepartmentOrItem + " Departmant is not available");

						if (FlexibilityFlag.equalsIgnoreCase("Yes")|| FlexibilityFlag.isEmpty())
						{
							/* Split the string with ' \\('
							 * e.g. product_text=Vegetables (1). Breadcrum is compared with Vegetables only
							 */
							String defaultDept = departmentList.get(1).getText().split(" \\(")[0];
							departmentList.get(1).findElement(By.xpath(objMap.getLocator("lstdepartmentName")))
							.click();

							count=0;
							try {
								do {
									SleepUtils.getInstance().sleep(TimeSlab.YIELD);
									breadcumbs_1 = uiDriver.getwebDriverLocator(objMap.getLocator("lstreorderBreadcumbs"));
									count++;
								} while (!breadcumbs_1.getText().contains(defaultDept)
										&& count < 5);

								if (breadcumbs_1.getText().contains(defaultDept)) {

									RESULT.passed("Department selection",
											defaultDept + " should be selected",
											defaultDept + " selected");

								} else
									RESULT.failed("Department selection",
											defaultDept + " should be selected",
											defaultDept + " is not selected");

							} catch (Exception e) 
							{
								RESULT.warning("Your Top Items", "Default department breadcrum validation should be select successful", "Unsuccessful validation of department "+defaultDept);
							}
							RESULT.passed(
									"Select Department",
									"User should be able to select department",
									defaultDept+ " default first department selected Sucessfully!!!!");
						}
					}
					if ((flag == 0) && FlexibilityFlag.equalsIgnoreCase("No")) {
						RESULT.passed("Your Top Items",
								"User should not select any department by default",
						"Department is not selected!!!!");	

					}
				}
				else
				{
					RESULT.warning("Reorder Your Top Items","Department section should have some departments","No departments available in departments");
				}


				if ((webDriver.findElements(By.xpath(objMap.getLocator("lstreorderSpecPref"))).size() > 0)
						&& !SpecialPref.isEmpty()) 
				{
					String Pref[] = SpecialPref.split(",");
					List<WebElement> special_prefList = webDriver.findElements(By.xpath(objMap.getLocator("lstreorderSpecPref")));
					for (int x = 0; x < Pref.length; x++) {
						if ((webDriver.findElements(By.xpath(objMap.getLocator("lstreorderSpecPref"))).size() > 0)) {

							for (i = 0; i < special_prefList.size(); i++) 
							{
								special_prefList = webDriver.findElements(By.xpath(objMap.getLocator("lstreorderSpecPref")));
								product_text = special_prefList.get(i).getText();
								if (product_text.contains(Pref[x])) {

									flag_pref=1;
									if (CompositeAppDriver.startUp.equalsIgnoreCase("SAFARI"))special_prefList.get(i)
									.findElement(By.xpath(objMap.getLocator("radlistName"))).click();
									else
										special_prefList.get(i).findElement(By.xpath(objMap.getLocator("radlistName")))
										.sendKeys(Keys.SPACE);
									count=0;
									try {
										do {
											SleepUtils.getInstance().sleep(TimeSlab.YIELD);
											breadcumbs_1 = uiDriver.getwebDriverLocator(objMap.getLocator("lstreorderBreadcumbs"));
											count++;
										} while (!breadcumbs_1.getText().contains(Pref[x])&& count < 10);
										if (breadcumbs_1.getText().contains(Pref[x])) {
											flag_pref=1;
											RESULT.passed(
													"Reorder Your Top Items",
													Pref[x]+ " should be checked",
													Pref[x]+ " checked");
											break;
										} else
											RESULT.failed(
													"Reorder Your Top Items",
													Pref[x]+ " should be checked",
													Pref[x]+ " is unchecked");

									} catch (Exception e) {
										RESULT.warning("Reorder Your List", "Special Preference breadcrum validation should be successful", "Unsuccessful validation of sepcial preferences "+Pref);
									}
								}
							}
						}
					}
					if(flag_pref==0)
					{
						RESULT.warning(
								"Special Preference Selection",
								"Given Special Preference should be avavilablae",
						"Special Preference is not available");
						WebElement selected_preference = special_prefList.get(0).findElement(By.xpath(objMap.getLocator("radlistName")));
						product_text = special_prefList.get(0).getText();
						/* Split the string with ' \\('
						 * e.g. product_text=Organic (1). Breadcrum is compared with Organic only
						 */
						product_text = product_text.split(" \\(")[0];
						selected_preference.click();

						if (CompositeAppDriver.startUp.equalsIgnoreCase("SAFARI"))
							selected_preference.click();
						else
						{
							selected_preference.sendKeys(Keys.SPACE);
						}

						SleepUtils.getInstance().sleep(TimeSlab.YIELD);
						count=0;
						try {
							do {
								SleepUtils.getInstance().sleep(TimeSlab.YIELD);
								breadcumbs_1 = uiDriver.getwebDriverLocator(objMap.getLocator("lstreorderBreadcumbs"));
								count++;
							} while (!breadcumbs_1.getText().contains(product_text)&& count < 10);
							if (breadcumbs_1.getText().contains(product_text))
							{
								RESULT.passed(
										"Reorder Your Top Items",
										product_text+ " should be checked",
										product_text+ " checked");

							} else
								RESULT.failed(
										"Reorder Your Top Items",
										product_text+ " should be checked",
										product_text+ " is unchecked");

						} catch (Exception e) 
						{
							RESULT.warning("Your Top Items "," Default special preferences breadcrum should be validated", product_text+" special preferences breadcrum validation failed");
						}
					}
				} else {
					RESULT.log(
							"Splpref not found or No splpref data",
							ResultType.PASSED,
							"Splpref should be visible and splpref data should be provided",
							"Splpref not found because may be there is no Splpref for selected Department "
							+ DepartmentOrItem
							+ " or No splpref data given.",
							true);
				}
				break;

			case SEARCH:
				/* Check the user input search item from past order */
				uiDriver.FD_searchFunction("QUICKSEARCH", "", "",
						DepartmentOrItem);
				break;

			}
		} catch (Exception e) {
			RESULT.failed("Reorder Your Top Items Exception","Reorder Your Top Items should be successful","Reorder Your Top Items is not successful");
		}
	}

	public void FD_reorderPastOrder(String filter, String Timeframe,
			String Department, String SpecialPreference,
			String FlexibilityFlag, String Product) throws FindFailed,
			InterruptedException, TimeoutException {
		int i = 0;
		int flag = 0;
		int flag_dept = 0;
		int flag_pref = 0;
		int bread_j = 0;
		int count = 0;
		String product_text;
		WebElement breadcumbs_1;

		/*
		 * For multiple data - the component is invoked(different data) For the
		 * first invocation it will click Reorder tab
		 */
		try {

			try{
				if (!(webDriver.findElements(By.xpath(objMap.getLocator("tabpastOrder"))).size() > 0)) 
				{
					// click reorder tab
					uiDriver.getwebDriverLocator(objMap.getLocator("lnkreorder")).click();
					wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("tabpastOrder")));
					uiDriver.getwebDriverLocator(objMap.getLocator("tabpastOrder")).click();

				}
			}
			catch(Exception e)
			{
				RESULT.failed("Reorder Past order : Reorder tab on Homepage", 
						"Application should navigate to reorder page and Reorder Past Order should be clicked successfully",
				"Application could not click on reorder past order tab successfully");
			}

			try 
			{
				try
				{
					wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(objMap.getLocator("lstreorderTimeFrame"))));
				}
				catch(TimeoutException e)
				{
					RESULT.failed("Reorder Past Order", "Time frame section should be available", 
					"Time frame section is not available");
					return;
				}

				// Put verification of banner display
				WebElement selected_timeframe = webDriver.findElements(By.xpath(objMap.getLocator("lstreorderTimeFrame"))).get(0);

				if (selected_timeframe.getText().toLowerCase().contains("Your Last Order".toLowerCase())) 
				{
					if (selected_timeframe.findElement(By.xpath(objMap.getLocator("chktimeFrame"))).getAttribute("checked").contains("true"))
						RESULT
						.passed(
								"Shop from This order with date automatically selected.",
								"Your Last Order checkbox should be checked",
						"Your Last Order is checked");
					else 
						RESULT
						.failed(
								"Shop from This order with date automatically selected.",
								"Your Last Order checkbox should be checked",
						"Your Last Order is not checked");				}
			} catch (Exception e) {
				RESULT.failed("Reorder Past Order", "Your Last Orders should be automatically checked", "Your Last orders is not checked");

			}

			switch (filterType.valueOf(filter)) {
			case Filter:

				/*
				 * This code snippet takes the value of time frame to be
				 * selected from user and check the checkbox The List retreives
				 * all the available time frames and comparision is done with
				 * the data text
				 */

				/* To retrieve size of list */
				List<WebElement> timeframeList = webDriver.findElements(By
						.xpath(objMap.getLocator("lstreorderTimeFrame")));
				wait.until(ExpectedConditions.presenceOfElementLocated(By
						.xpath(objMap.getLocator("lstreorderTimeFrame"))));
				/*
				 * This for loop checks if the data matches with the latest time
				 * frames displayed. If found then flag is set to 1
				 */
				int y = 1;
				boolean limit = false;
				ArrayList list = new ArrayList();
				//If multiple date is to be selected provide dates seperated by : in data set
				String Timeframes[] = Timeframe.split(":");
				System.out.println("Before for:" + Timeframes.length);
				if(timeframeList.size()>0)
				{
					for (int p = 0; p < Timeframes.length; p++) {
						System.out.println("Before for:" + timeframeList.size());
						for (i = 1; i < timeframeList.size(); i++) {
							System.out.println("Product " + i + ":");
							// SleepUtils.getInstance().sleep(TimeSlab.MEDIUM);
							product_text = timeframeList.get(i).getText();

							if (product_text.contains(Timeframes[p])) {
								timeframeList.get(i).findElement(By.xpath(objMap.getLocator("chktimeFrame"))).click();
								list.add(Timeframes[p]);
								flag = 1;
								y++;
							}
						}
					}
					try
					{	/*User specified date is not available on the Timeframe section 
					 *Check for See more section 
					 */
						uiDriver.getwebDriverLocator(objMap.getLocator("poptimeFrameSeeMore")).click();
						try
						{
							List<WebElement> ul_list = webDriver.findElements(By.xpath(objMap.getLocator("lstrmoreTimeFrame")));
							if(ul_list.size()>0)
							{
								for (int p = 0; p < Timeframes.length; p++)
								{
									for (WebElement ul_ite : ul_list) {
										i = 1;
										List<WebElement> li_list = ul_ite.findElements(By.tagName("li"));
										for (WebElement li_ite : li_list)
										{
											/* First li has no span/label/span */
											if (i == 1) {
												i = 2;
												continue;
											}
											product_text = li_ite.getText();
											if (product_text.contains(Timeframes[p]))
											{
												if (li_ite.findElement(By.xpath("span/label/input")).isEnabled()) {
													li_ite.findElement(By.xpath("span/label/input")).click();
													if (li_ite.findElement(By.xpath("span/label/input")).isSelected()) 
													{
														flag = 1;
														y++;
														list.add(Timeframes[p]);
													}
													else 
													{ 
														/* Check for whether user has already selected 14 timeframes
														 * If yes : display a warning to indicate maximum timeframe check
														 * If no : It is a defect
														 */
														if (y > 14) 
														{
															RESULT.warning(
																	"Maximum Limit of Timeframe Selection",
																	"Maximum 14 Timeframes  Should be selected ",
																	(y - 1)+ "  Timeframes are Selected ");
															limit = true;
															break;
														} else {
															RESULT.failed(
																	"Checkbox checked",
																	"Checkbox Should be checked",
															"Checkbox is not checked");
															return;
														}

													}
												} else {
													RESULT.warning("Checkbox Enable",
															"Checkbox Should be Enable",
													"Checkbox is not Enable");
												}
											}
										}
										if (limit == true) {
											break;
										}
									}
									if (limit == true) {
										break;
									}
								}
							}
							else
							{
								RESULT.warning("Reorder Past Order ", "See more should have some dates", "See more has 0 dates");
							}

							try
							{
								uiDriver.getwebDriverLocator(objMap.getLocator("popseeMoreClose")).click();
							}
							catch(Exception e)
							{
								RESULT.warning("Reorder Past Order : See more frame", "See more frame should be successfully closed", "Unable to close See More section");
								return;
							}
						}
						catch(Exception e)
						{
							RESULT.warning("Reorder Past Order : See more frame", "Date(s) should be selected from See More section", "Unable to select date(s) from See More section");
							return;
						}
					}
					catch(NoSuchElementException e)
					{
						RESULT.warning("Reorder Past Order", "See More link should be available and clickable", "See more link might not be available or clickable ");
						return;
					}
					catch(Exception e)
					{
						RESULT.warning("Reorder Past Order", "See More link should be available and clickable", "See more link might not be available or clickable ");
						return;
					}

					// Validates whether the timeframe has been selected successfully using breadcrum check
					if (flag == 1) {
						try {
							SleepUtils.getInstance().sleep(TimeSlab.YIELD);
							breadcumbs_1 = uiDriver.getwebDriverLocator(objMap.getLocator("lstreorderBreadcumbs"));
							wait.until(ExpectedConditions.textToBePresentInElement(breadcumbs_1, list.get(0).toString()));
							for (int m = 0; m < list.size(); m++) {
								if (breadcumbs_1.getText().contains(
										list.get(m).toString())) 
								{
									RESULT.passed("Successfully checked", 
											list.get(m).toString()+ " Timeframe should be checked", 
											list.get(m).toString()+ " Timeframe is checked");
								} else {
									RESULT.failed("Successfully checked", 
											list.get(m).toString()+ " Timeframe should be checked", 
											list.get(m).toString()+ " TImeframe is  unchecked");
									return;
								}
							}
						} catch (Exception e) {
							RESULT.warning("Reorder Past Order ", "Breadcrum should be validated", "Breadcrum validation failed");

						}
					}

					if (flag == 0 && FlexibilityFlag.equalsIgnoreCase("YES")) {
						RESULT.warning("Timeframe selection",
								"Given Timeframe Should be available",
						"Given Timeframe is not available ");

						WebElement selected_timeframe = timeframeList.get(0);
						if (selected_timeframe.findElement(By.xpath(objMap.getLocator("chktimeFrame")))
								.getAttribute("checked").contains("true")) {
							RESULT.passed("First Timeframe Selection",
									"First Time frame Should be selected",
							"First Timeframe selected");
						} else {
							product_text = timeframeList.get(0).getText();
							timeframeList.get(0).findElement(By.xpath(objMap.getLocator("chktimeFrame"))).click();
							SleepUtils.getInstance().sleep(TimeSlab.YIELD);
							count=0;
							if (uiDriver.isDisplayed("lstreorderBreadcumbs")) {
								try {
									do {
										SleepUtils.getInstance().sleep(
												TimeSlab.YIELD);
										breadcumbs_1 = uiDriver.getwebDriverLocator(objMap.getLocator("lstreorderBreadcumbs"));
										count++;
									} while (!breadcumbs_1.getText().contains(product_text)&& count < 5);
									if (breadcumbs_1.getText().contains(
											product_text.replaceAll("\\s+$", "")
											.replaceAll("^\\s+", ""))) {
										RESULT.passed(
												"First Timeframe Selection",
												"First Time frame Should be selected",
										"First Timerame selected");
									} else {
										RESULT.failed(
												"First Timeframe Selection",
												"First Time frame Should be selected",
										"First Timerame not selected");
										return;
									}
								} catch (Exception e) {
									RESULT.warning("Reorder Past Order ", "Timeframe breadcrum should be validated", "Timeframe breadcrum validation failed");
								}
							}
						}
					} else if (flag == 0 && FlexibilityFlag.equalsIgnoreCase("NO")) {
						RESULT.failed("Timeframe selection",
								"Given Timeframe Should be available",
								"Given Timeframe is not available " + Timeframe);
						return;
					}
				}
				else
				{
					RESULT.warning("Reorder Past Order", "Past orders should be available to select", "No past orders available");
				}



				/*
				 * This case is invoked when user input is to select data from
				 * the department category
				 */
				/* Check the user input department from past order */
				List<WebElement> departmentList = webDriver.findElements(By.xpath(objMap.getLocator("lstreorderDept")));
				// System.out.println(departmentList.getClass());
				if(departmentList.size()>0)
				{
					for (i = 1; i < departmentList.size(); i++) {
						product_text = departmentList.get(i).getText();
						System.out.println(product_text);
						if (product_text.contains(Department)) {
							System.out.println(departmentList.get(i).findElement(By.xpath(objMap.getLocator("lstdepartmentName"))).getText());
							departmentList.get(i).findElement(By.xpath(objMap.getLocator("lstdepartmentName"))).click();

							try {
								// do {
								SleepUtils.getInstance().sleep(TimeSlab.YIELD);
								wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(objMap.getLocator("lstreorderBreadcumbs"))));
								FD_displayValidation("lstreorderBreadcumbs");
								breadcumbs_1 = uiDriver.getwebDriverLocator(objMap.getLocator("lstreorderBreadcumbs"));

								if (breadcumbs_1.getText().contains(Department))
								{
									flag_dept = 1;
									RESULT.passed("Successfully selected",
											Department + " should be selected",
											Department + "selected");
								}

								else {
									RESULT.failed("Successfully selected",
											Department + " should be selected",
											Department + "not selected");
									return;
								}
							} catch (Exception e) {
								RESULT.warning("Reorder Past Order ", "Department breadcrum should be validated", "Department breadcrum validation failed");
							}
							break;
						}
					}
					if (flag_dept == 0) {
						RESULT.warning("Department Selection",
								"Given Department should be avavilablae",
						"Given Department is not available");
						product_text = departmentList.get(1).getText();
						System.out.println(product_text);
						/* Split the string with ' \\('
						 * e.g. product_text=Vegetables (1). Breadcrum is compared with Vegetables
						 */
						product_text = product_text.split(" \\(")[0];
						System.out.println(product_text);
						departmentList.get(1).findElement(By.xpath(objMap.getLocator("lstdepartmentName"))).click();
						try {
							// do {
							SleepUtils.getInstance().sleep(TimeSlab.YIELD);
							wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(objMap.getLocator("lstreorderBreadcumbs"))));
							FD_displayValidation("lstreorderBreadcumbs");
							breadcumbs_1 = uiDriver.getwebDriverLocator(objMap.getLocator("lstreorderBreadcumbs"));
							// count++;
							// } while (!breadcumbs_1.getText().contains(Department)
							// && count<5);
							if (breadcumbs_1.getText().contains(
									product_text.replaceAll("\\s+$", "").replaceAll("^\\s+", "")))
								RESULT.passed("First Department Selection",
										" First Department Should be selected",
								"First Department selected");
							else {
								RESULT.failed("First Department Selection",
										" First Department Should be selected",
								"First Department not selected");
								return;
							}
						} catch (Exception e) {
							RESULT.warning("Reorder Past Order ", product_text+" default selected department breadcrum should be validated", product_text+" default department breadcrum validation failed");
						}
					}
				}
				else
				{
					RESULT.warning("Reorder Past Order", "Department should be available to select", "No department available");
				}

				/*
				 * This case is invoked when data is from other category like
				 * Sale, Shop By, etc. Here tester needs to assure that the
				 * portion is displayed with first time page loading. If in time
				 * frame any order from 'See more orders' is checked then in GUI
				 * the div expands and special preferences section will not be
				 * visible on page.
				 */
				/* Check the user input special preference from past order */
				if (webDriver.findElements(By.xpath(objMap.getLocator("lstreorderSpecPref"))).size() > 0) {
					count = 0;
					List<WebElement> special_prefList = webDriver.findElements(By.xpath(objMap.getLocator("lstreorderSpecPref")));
					if(special_prefList.size()>0)
					{
						for (i = 0; i < special_prefList.size(); i++) {
							product_text = special_prefList.get(i).getText();
							System.out.println(product_text);
							if (product_text.contains(SpecialPreference)) {

								flag_pref = 1;
								if (CompositeAppDriver.startUp.equalsIgnoreCase("SAFARI"))
									special_prefList.get(i).findElement(By.xpath(objMap.getLocator("radlistName"))).click();
								else
									special_prefList.get(i).findElement(By.xpath(objMap.getLocator("radlistName"))).sendKeys(Keys.SPACE);
								try {
									do {
										SleepUtils.getInstance().sleep(TimeSlab.YIELD);
										breadcumbs_1 = uiDriver.getwebDriverLocator(objMap.getLocator("lstreorderBreadcumbs"));
										count++;
									} while (!breadcumbs_1.getText().contains(SpecialPreference)&& count < 5);
									if (breadcumbs_1.getText().contains(SpecialPreference))
										RESULT.passed("Successfully checked",
												SpecialPreference+ " should be checked",
												SpecialPreference + "checked");
									else {
										RESULT.failed("Successfully checked",
												SpecialPreference+ " should be checked",
												SpecialPreference + "is unchecked");
										return;
									}
								} catch (Exception e) {
									RESULT.warning("Reorder Past Order ",SpecialPreference+" only show breadcrum should be validated", SpecialPreference+" only show breadcrum validation failed");
								}

								break;
							}
						}
						if (flag_pref == 0) {
							RESULT.warning(
									"Special Preference Selection",
									"Given Special Preference should be avavilablae",
							"Special Preference is not available");
							product_text = special_prefList.get(0).getText();
							WebElement selected_preference = webDriver.findElements(By.xpath(objMap.getLocator("lstreorderSpecPref"))).get(0);
							if (selected_preference.getAttribute("checked") != null && selected_preference.getAttribute("checked").contains("checked")) {
								RESULT.passed(
										"First Special Preference Selection",
										"First Special Preference Should be selected",
								"First Special Preference selected");
							} else {
								System.out.println(product_text);
								/* Split the string with ' \\('
								 * e.g. product_text=Organic (1). Breadcrum is compared with Organic only
								 */
								product_text = product_text.split(" \\(")[0];
								if (CompositeAppDriver.startUp.equalsIgnoreCase("SAFARI"))
									special_prefList.get(0).findElement(By.xpath(objMap.getLocator("radlistName"))).click();
								else
									special_prefList.get(0).findElement(By.xpath(objMap.getLocator("radlistName"))).sendKeys(Keys.SPACE);
								SleepUtils.getInstance().sleep(TimeSlab.YIELD);
								count=0;
								if (uiDriver.isDisplayed("lstreorderBreadcumbs")) {
									try {
										do {
											SleepUtils.getInstance().sleep(TimeSlab.YIELD);
											breadcumbs_1 = uiDriver.getwebDriverLocator(objMap.getLocator("lstreorderBreadcumbs"));
											count++;
										} while (!breadcumbs_1.getText().contains(product_text) && count < 5);
										if (breadcumbs_1.getText().contains(product_text
												.replaceAll("\\s+$", "")
												.replaceAll("^\\s+", ""))) {
											RESULT.passed(
													"First Special Preference Selection",
													"First Special Preference Should be selected",
											"First Special Preference selected");
										} else {
											RESULT.failed(
													"First Special Preference Selection",
													"First Special Preference Should be selected",
											"First Special Preference not selected");
											return;
										}
									} catch (Exception e) {

									}RESULT.warning("Reorder Past Order "," Default only show breadcrum should be validated", "Default only show breadcrum validation failed");
								}
							}
						}
					}
					else
					{
						RESULT.warning("Reorder Past Order", "Only show section should have some value to select", "Only show section has not value to select");
					}



				} else {
					RESULT.warning("Special Preference Selection",
							"Special Preference section should be available",
					"Special Preference Section is not available");
				}

				break;

				/*
				 * This case uses the Search textbox to search for a product in
				 * Reorder tab
				 */
			case search:
				/* Check the user input search item from past order */
				uiDriver.FD_searchFunction("QUICKSEARCH", "", "", Product);
				break;
			}
		} catch (Exception e) {

			RESULT.failed("Reorder Past Order Exception", "Past Order selection should be successful", "Past Order selection failed");
		}

	}

	@SuppressWarnings("null")
	public void FD_Search(String search_text) throws InterruptedException,
	TimeoutException {
		WebDriverWait wait = null;
		int count_next_clicks = 1;
		String search_items[][] = null;

		try {
			Actions ac_key = new Actions(webDriver);
			// uiDriver.getwebDriverLocator(objMap.getLocator("")).sendKeys(search_text.toLowerCase());
			uiDriver.setValue("txtreorderSearch", search_text.toLowerCase());
			ac_key.sendKeys(Keys.ENTER).build().perform();
			webDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

			/*
			 * Information on pages: Number of pages of search, Current active
			 * page
			 */

			WebElement pagination_tab = uiDriver.getwebDriverLocator(objMap
					.getLocator("lnksearchPagination"));

			int prod_perpage = Integer.parseInt(pagination_tab
					.getAttribute("data-pagesize"));
			int toatl_products = Integer.parseInt(pagination_tab
					.getAttribute("data-itemcount"));

			int total_pages = (toatl_products / prod_perpage)
			+ (toatl_products % prod_perpage > 0 ? 1 : 0);
			System.out.println(total_pages);

			webDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			WebElement current_page = uiDriver.getwebDriverLocator(objMap
					.getLocator("btnSearchCurrentPage"));
			webDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			int page_number = Integer.parseInt(current_page
					.getAttribute("data-page"));
			System.out.println(page_number);

			while (page_number <= total_pages
					&& count_next_clicks <= total_pages) {
				/* Row selection */

				List<WebElement> row_list = uiDriver.getwebDriverLocator(
						objMap.getLocator("lstrsearchRowList")).findElements(
								By.tagName("ol"));
				wait
				.until(ExpectedConditions
						.visibilityOfAllElements(row_list));

				search_items = new String[(row_list.size() + 1) * page_number][prod_perpage];
				System.out.println("Number of rows " + row_list.size());
				int i = 0;
				for (WebElement row_ite : row_list) {
					/* Column Selection */

					List<WebElement> col_list = row_ite.findElements(By
							.tagName("li"));
					System.out.println("Number of columns in row is "
							+ col_list.size());
					int j = 0;
					for (WebElement col_ite : col_list) {

						// WebElement
						// item_text=col_ite.findElement(By.tagName("a"));
						search_items[i][j] = col_ite.getText();
						j++;
					}
					i++;
				}
				uiDriver.getwebDriverLocator(
						objMap.getLocator("btnreorder_searchnextpageclick"))
						.click();
				count_next_clicks++;
				Thread.sleep(30000);
				current_page = uiDriver.getwebDriverLocator(objMap
						.getLocator("reorder_searchcurrentpage"));
				page_number = Integer.parseInt(current_page
						.getAttribute("data-page"));
			}

		} catch (NoSuchElementException e) {
			System.out.println(e.getMessage());
			if (search_items != null) {
				System.out.println("Passed");
				RESULT.passed("Searching product using search field",
						"Product search should be done successfully",
				"Product search is successful");
			} else {
				RESULT
				.log(
						"Searching product using search field",
						ResultType.FAILED,
						"Product search should be done successfully",
						"Product search failed because may be there exist no product with the search text "
						+ search_text, true);
			}

		} catch (StaleElementReferenceException e) {
			System.out.println(e.getMessage());
		}
	}

	// function to handle the alcohol restriction pop-up on the express checkout
	public boolean FD_alcoholPopUp(String restrinction_handle) {

		// flag to return to decide weather chnage should be clicked or not
		boolean change_address = true;

		if (webDriver.findElements(
				By.xpath(objMap.getLocator("btnchangeAddress"))).size() > 0
				&& webDriver.findElement(
						By.xpath(objMap.getLocator("btnchangeAddress")))
						.isDisplayed()) {
			// Remove the item 
			if (restrinction_handle.equalsIgnoreCase("Remove") || restrinction_handle.isEmpty()) {
				uiDriver.click("btnremoveProceed");
				uiDriver.wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(objMap.getLocator("btnremoveProceed"))));
				RESULT
				.passed("Alcohol Restriction",
						"Alcohol Restriciton popup should be displayed and Remove should be clicked",
				"Alcohol Restriciton popup is displayed and Alcohol item removed and proceeded");
			} 
			// change the address
			else if (restrinction_handle.equalsIgnoreCase("Change")) {
				uiDriver.click("btnchangeAddress");
				uiDriver.wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(objMap.getLocator("btnchangeAddress"))));
				RESULT
				.passed("Alcohol Restriction",
						"Alcohol Restriciton popup should be displayed and Change Address should be clicked",
				"Alcohol Restriciton popup is displayed and Address changed");
				change_address = false;
			} 
			// if Chnage/Remove variable is not set then By default remove
			else {
				uiDriver.click("btnremoveProceed");
				uiDriver.wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(objMap.getLocator("btnremoveProceed"))));
				RESULT
				.passed("Alcohol Restriction",
						"Alcohol Restriciton popup should be displayed and Remove should be clicked by default",
						"Alcohol Restriciton popup is displayed, handle vriable contain: " + restrinction_handle +" taking Remove and alcohol item Removed and proceeded");
			}
		}
		return change_address;
	}

	/**
	 * Function Name: FD_chooseDeliveryAddress Purpose: Allows FreshDirect user
	 * to Add, Edit, Select or Delete Delivery Address Created By: Shraddha Shah
	 * Created Date: Modified By: Modified Date:
	 * 
	 * Enum method is used in switch case statement in order to select one of
	 * the option of Delivery Address
	 * 
	 * @param AddSelectDeleteEdit
	 *            Option to be selected to Add, Select, Edit or Delete delivery
	 *            address
	 * @param ServiceType
	 *            Residential or Commercial service to be selected by user
	 * @param AddressNickName
	 *            Nickname of the Delivery Address to be added
	 * @param FirstName
	 *            First Name of user
	 * @param LastName
	 *            Last Name of user
	 * @param StreetAddress
	 *            Street Address to be provided by user
	 * @param Apartment
	 *            Apartment name to be provided by user
	 * @param AddLine2
	 *            Address Line2 to be given by user
	 * @param City
	 *            City name of user
	 * @param State
	 *            State to be added by user
	 * @param ZipCode
	 *            Zip code to be given by user
	 * @param Contact1
	 *            Contact number to be provided by user
	 * @param Ext1
	 *            Extension number to be provided by user
	 * @param ContactType
	 *            Type of contact i.e Work, Home, Mobile to be mentioned by user
	 * @param AddAltContact
	 *            Whether user wants to add an alternate contact or not
	 * @param Contact2
	 *            Alternate Contact number to be provided by user
	 * @param Ext2
	 *            Alternate Extension number to be provided by user
	 * @param AltContactType
	 *            Type of alternate contact i.e Work, Home, Mobile to be
	 *            mentioned by user
	 * @param SpclDelInstructions
	 *            Special delivery instructions to be mentioned by user
	 * @param DoormanDelivery
	 *            Whether user allows delivery to doorman or not
	 * @param NeighbourDelivery
	 *            Whether user allows delivery to neighbor or not
	 * @param AltFirstName
	 *            First Name of Neighbor
	 * @param AltLastName
	 *            Last Name of Neighbor
	 * @param AltApt
	 *            Apartment Name of Neighbor
	 * @param AltPhone
	 *            Contact number of neighbor
	 * @param AltExtn
	 *            Extension number of neighbor
	 * @param UnattendedDelivery
	 *            Whether user allows unattended delivery or not
	 * @param UnattendedDelInstructions
	 *            Instructions to be provided by user in case of unattended
	 *            delivery
	 * @param CompanyName
	 *            Company Name to be provided by user if Service Type is
	 *            selected as Commercial
	 * @param SaveCancelBtn
	 *            Either to Save the delivery address or cancel it
	 * @param SelectAddress
	 *            Address given by user to select delivery address while placing
	 *            an order
	 * @param FlexibilityFlag
	 *            Flexibility provided to user to select first delivery address
	 *            if address given by user does not exist in the list of
	 *            addresses
	 **/

	private enum ChooseOpt {
		Add, Select, SelectPickup, Delete, Edit, AddCheckingAcc
	};

	public void FD_chooseDeliveryAddress(String AddSelectDeleteEdit,
			String ServiceType, String AddressNickName, String FirstName,
			String LastName, String StreetAddress, String Apartment,
			String AddLine2, String City, String State, String ZipCode,
			String Contact1, String Ext1, String ContactType,
			String AddAltContact, String Contact2, String Ext2,
			String AltContactType, String SpclDelInstructions,
			String DoormanDelivery, String NeighbourDelivery,
			String AltFirstName, String AltLastName, String AltApt,
			String AltPhone, String AltExtn, String UnattendedDelivery,
			String UnattendedDelInstructions, String CompanyName,
			String SaveCancelBtn, String SelectAddress, String FlexibilityFlag)
	throws InterruptedException, FindFailed {
		try{
			// variables for address operations
			String ChoosedOpt = AddSelectDeleteEdit;
			String AddressCountBefore = uiDriver.getwebDriverLocator(
					objMap.getLocator("straddressCount")).getAttribute(
					"data-item-count");

			switch (ChooseOpt.valueOf(ChoosedOpt)) {
			// Add a delivery address
			case Add:
				SleepUtils.getInstance().sleep(TimeSlab.YIELD);
				// if add button on address pop-up is available then add it
				if (webDriver.findElements(
						By.xpath(objMap.getLocator("btnaddNewAddress"))).size() > 0 && webDriver.findElement(
								By.xpath(objMap.getLocator("btnaddNewAddress"))).isDisplayed()) {
					uiDriver.click("btnaddNewAddress");
					String Servicetype = ServiceType;
					//Adding address details for Residential
					if (ServiceType.equalsIgnoreCase("Residential")) {					
						uiDriver.click("radresidential");
						uiDriver.setValue("txtaddressNickname", AddressNickName);
						uiDriver.setValue("txtfirstNameDelAddress", FirstName);
						uiDriver.setValue("txtlastNameDelAddress", LastName);
						uiDriver.setValue("txtstreetAddress", StreetAddress);
						uiDriver.setValue("txtapartment", Apartment);
						uiDriver.setValue("txtaddressLine2", AddLine2);
						uiDriver.setValue("txtcity", City);
						uiDriver.setValue("drpstate", State);
						uiDriver.setValue("txtzipCode", ZipCode);
						uiDriver.setValue("txtphone", Contact1);
						uiDriver.setValue("txtphoneExtn", Ext1);
						String Contacttype = ContactType;
						if (ContactType.equalsIgnoreCase("Mobile")) {
							uiDriver.click("radmobileNumber");
						}
						else if (ContactType.equalsIgnoreCase("Home")) {
							uiDriver.click("radhomeNumber");
						}
						else if (ContactType.equalsIgnoreCase("Work")) {
							uiDriver.click("radworkNumber");
						}
						else
						{
							RESULT.warning("Add delivery Address","Contact type argument should be 'Home'or 'Mobile' or 'Work'",
							"Contact type argument is other than 'Home'or 'Mobile' or 'Work'");
						}
						if (AddAltContact.equalsIgnoreCase("Yes")) {
							uiDriver.click("btnaddAltContact");
							uiDriver.setValue("txtaltPhone", Contact2);
							uiDriver.setValue("txtaltPhoneExtn", Ext2);
							String AltContacttype = AltContactType;
							if (AltContacttype.equalsIgnoreCase("Mobile")) {
								uiDriver.click("radaltMobileNumber");
							}
							if (AltContacttype.equalsIgnoreCase("Home")) {
								uiDriver.click("radaltHomeNumber");
							}
							if (AltContacttype.equalsIgnoreCase("Work")) {
								uiDriver.click("radaltWorkNumber");
							}
						}
						uiDriver.setValue("txtdeliveryInstructions",
								SpclDelInstructions);
						if (DoormanDelivery.equalsIgnoreCase("yes")) {
							uiDriver.click("raddoormanDelivery");
						}
						if (NeighbourDelivery.equalsIgnoreCase("yes")) {
							uiDriver.click("radneighbourDelivery");
							uiDriver.setValue("txtneighborFirstName",
									AltFirstName);
							uiDriver.setValue("txtneighborLastName",
									AltLastName);
							uiDriver.setValue("txtneighborAddress", AltApt);
							uiDriver.setValue("txtneighborPhone", AltPhone);
							uiDriver.setValue("txtneighborPhoneExtn", AltExtn);
						}
						//if unattended delivery option is available to user then it can be selected
						if (UnattendedDelivery.equalsIgnoreCase("yes")) {
							if (webDriver.findElements(
									By.xpath(objMap.getLocator("radunattendedDelivery")))
									.size() > 0) {
								uiDriver.click("radunattendedDelivery");
								uiDriver.setValue("txtunattendedDelInstructions",UnattendedDelInstructions);
							} else {
								RESULT.warning(
										"Add Delivery Address",
										"User should be able to select Unattended Delivery Option",
								"Sorry!!! Unattended Delivery option is not available for your zone address");
							}
						}
						RESULT.done(
								"Address fields",
								"All the given data shoud be entered in relevant Address fields",
						"All the data entered in relevant Address fields");
						//saving or cancelling address details added by user
						if (SaveCancelBtn.equalsIgnoreCase("Cancel")) {
							uiDriver.click("btncancelHome");
						} else {
							uiDriver.click("btnsaveHome");
							//capturing error in entering address details after clicking on save button
							try {
								int k = 0;
								List<WebElement> Errorlist = webDriver
								.findElements(By
										.xpath(objMap
												.getLocator("popnewAddressHome")));
								System.out.println("size" + Errorlist.size());
								for (int i = 0; i < Errorlist.size(); i++) {
									WebElement Error = Errorlist.get(i);
									k++;
									String Error1 = Error
									.getAttribute("fdform-error");
									System.out.println("Error Attribute"
											+ Error1);
									if (!Error.getAttribute("fdform-error")
											.equalsIgnoreCase(null)) {
										System.out.println("error present");
										RESULT
										.warning(
												"Add Delivery Address",
												"User should be able to add new delivery address",
										"Please fill the required details");
										return;
									}
								}
							} catch (Exception e) 
							{
								RESULT.failed("Add Delivery Address", "Error might have been generated and warning should be displayed", 
								"Error might not have been generated");
							}
							try {
								uiDriver.wait
								.until(ExpectedConditions
										.invisibilityOfElementLocated(By
												.xpath(objMap
														.getLocator("btnsaveHome"))));
							} catch (Exception e) {
								RESULT.warning(
										"Add Delivery Address",
										"User should be able to add new delivery address",
										"User is not able to add delivery address and the exception caught is " + e.getMessage());
								return;
							}
							//Taking count of address to verify if new address gets added or not
							String AddressCountAfter = uiDriver.getwebDriverLocator(
									objMap.getLocator("straddressCount"))
									.getAttribute("data-item-count");
							if (Integer.parseInt(AddressCountAfter) == Integer
									.parseInt(AddressCountBefore) + 1) {
								RESULT.passed(
										"Add Delivery Address",
										"User should be able add new delivery address",
								"Address added Sucessfully!!!!");
							} else {
								RESULT.failed(
										"Add Delivery Address",
										"User should be able to add new delivery address",
								"Unable to add address!!");
								return;
							}

						}
					}
					//adding address details for Office
					else if (ServiceType.equalsIgnoreCase("Office")) {
						uiDriver.click("radofficeDelAddress");
						uiDriver.setValue("txtcompanyNameOffice", CompanyName);
						uiDriver.setValue("txtfirstNameOffice", FirstName);
						uiDriver.setValue("txtlastNameOffice", LastName);
						uiDriver.setValue("txtstreetAddressOffice",
								StreetAddress);
						uiDriver.setValue("txtapartmentOffice", Apartment);
						uiDriver.setValue("txtaddressLine2Office", AddLine2);
						uiDriver.setValue("txtcityOffice", City);
						uiDriver.setValue("drpstateOffice", State);
						uiDriver.setValue("txtzipCodeOffice", ZipCode);
						uiDriver.setValue("txtphoneOffice", Contact1);
						uiDriver.setValue("txtphoneExtnOffice", Ext1);
						if (AddAltContact.equalsIgnoreCase("Yes")) {
							uiDriver.click("btnaddAltContactOffice");
							uiDriver.setValue("txtaltPhoneOffice", Contact2);
							uiDriver.setValue("txtaltPhoneExtnOffice", Ext2);
						}
						RESULT.done(
								"Address fields",
								"All the given data shoud be entered in relevant Address fields",
						"All the data entered in relevant Address fields");
						//saving or cancelling address details entered
						if (SaveCancelBtn.equalsIgnoreCase("Cancel")) {
							uiDriver.click("btncancelOffice");
						} else {
							uiDriver.click("btnsaveOffice");
							//checking if error encountered in details that are entered
							int k = 0;
							List<WebElement> Errorlist = webDriver
							.findElements(By.xpath(objMap
									.getLocator("popnewAddressOffice")));
							System.out.println("size" + Errorlist.size());
							for (int p = 0; p < Errorlist.size(); p++) {
								try {
									WebElement Error = Errorlist.get(p);
									k++;
									System.out.println("Element present" + k);
									String Error1 = Error
									.getAttribute("fdform-error");
									System.out.println("Error Attribute"
											+ Error1);
									if (!Error.getAttribute("fdform-error")
											.equalsIgnoreCase(null)) {
										System.out.println("error present");
										RESULT
										.warning(
												"Add Delivery Address",
												"User should be able to add new delivery address",
										"Please fill the required details");
										return;
									}
									// break;
								} catch (Exception e) {
									RESULT.failed("Add Delivery Address", "Error might have been generated and warning should be displayed", 
									"Error might not have been generated");
								}
							}
							try {
								uiDriver.wait
								.until(ExpectedConditions
										.invisibilityOfElementLocated(By
												.xpath(objMap
														.getLocator("btnsaveOffice"))));
							} catch (Exception e) {
								RESULT
								.failed(
										"Add Delivery Address",
										"User should be able to add new delivery address",
										"User is unable to add new address and the exception caught is " + e.getMessage());
								return;
							}
							//Taking address count to verify if new address gets added or not
							String AddressCountAfter = uiDriver
							.getwebDriverLocator(objMap
									.getLocator("straddressCount"))
									.getAttribute("data-item-count");
							if (Integer.parseInt(AddressCountAfter) == Integer
									.parseInt(AddressCountBefore) + 1) {
								RESULT
								.passed(
										"Add Delivery Address",
										"User should be able add new delivery address",
								"Address added Sucessfully!!!!");
							} else {

								RESULT
								.failed(
										"Add Delivery Address",
										"User should be able to add new delivery address",
								"Unable to add address");
								return;
							}
						}
					}
					else
					{
						RESULT.warning("Add Delivery Address", "Service Type must be either 'Office' or 'Home'",
						"Service Type is other than Office or Home");
						return;
					}
				} 
				// else display warning showing add button not available
				else {
					RESULT.failed("Add Delivery Address",
							"User should be able to add new delivery address",
					"Add New Address button is not displayed");
				}
				break;

				// Select a delivery address for placing an order	
			case Select:
				SleepUtils.getInstance().sleep(TimeSlab.YIELD);
				try
				{
					//Taking list of addresses and its corresponding radio buttons
					List<WebElement> addLst = webDriver.findElements(By
							.xpath(objMap.getLocator("lstaddressPath")));
					int iSize = addLst.size();
					List<WebElement> selectLst = webDriver.findElements(By
							.xpath(objMap.getLocator("lstradioPath")));
					int jSize = selectLst.size();
					String myadd = SelectAddress;
					String myaddnew = myadd.replaceAll("\n\n", "\n");

					int a = 0;
					for (int i = 0, j = 0; i < iSize; i++, j++) {
						String gotadd = addLst.get(i).getText();
						//selecting an address if it maps the address provided by the user
						if (gotadd.contains(myaddnew)) {
							a++;
							System.out.println(gotadd);
							try {
									selectLst.get(j).click();
									System.out.println("Delivery Address selected");
									List<WebElement> addLst1 = webDriver.findElements(By.xpath(objMap.getLocator("lstselectingAddress")));
									address = addLst1.get(j).getText();
								
							} catch (Exception e) {
								try
								{
									selectLst.get(j).click();
								}
								catch(Exception e1)
								{
									RESULT.failed("Select Delivery address", "Delivery address should be selected",
											"Delivery address is not selected successfully and the exception caught is "+e1.getMessage());
								}

								System.out.println("Delivery Address selected in catch");
							}
							SleepUtils.getInstance().sleep(TimeSlab.MEDIUM);						
							break;
						}
					}
					//selecting first address by default if Flexibility Flag = Yes
					if (a == 0) {
						if (FlexibilityFlag.equalsIgnoreCase("Yes")) {
							uiDriver.click("raddefaultAddress");
							List<WebElement> addLst1 = webDriver.findElements(By.xpath(objMap.getLocator("lstselectingAddress")));
							address = addLst1.get(0).getText();
							System.out.println(address);
							SleepUtils.getInstance().sleep(TimeSlab.MEDIUM);							
						} else {
							String Error_Msg = "Address did not match";
							System.out.println(Error_Msg);
							RESULT
							.failed(
									"Select Delivery Address",
									"User should be able to select Delivery Address",
									myaddnew + " Address did not match and the FF=No");
							return;
						}
					}
				}
				catch(Exception e)
				{
					RESULT.failed("Select Delivery Address", "Delivery address should be successfully selected",
							"Delivery address is not selected successfully and the exception caught is "+e.getMessage());
				}
				break;

				// delete a delivery address	
			case Delete:
				SleepUtils.getInstance().sleep(TimeSlab.YIELD);
				try
				{
					//Taking list of addresses and its corresponding delete buttons
					List<WebElement> addLst1 = webDriver.findElements(By
							.xpath(objMap.getLocator("lstaddressPath")));
					int dSize = addLst1.size();
					List<WebElement> delLst = webDriver.findElements(By
							.xpath(objMap.getLocator("lstdeletePath")));
					int eSize = delLst.size();
					String myadd1 = SelectAddress;
					String myadd1new = myadd1.replaceAll("\n\n", "\n");
					// System.out.println("given:"+myadd1);
					int b = 0;
					for (int i = 0, j = 0; i < dSize; i++, j++) {
						String gotadd1 = addLst1.get(i).getText();
						// System.out.println("Address:"+gotadd1);
						//deleting an address if it maps the address provided by the user
						if (gotadd1.startsWith(myadd1new)) {
							b++;
							delLst.get(j).click();
							if (uiDriver.getwebDriverLocator(
									objMap.getLocator("btndelete")).isDisplayed()) {
								RESULT
								.done("Delete Address",
										"Delete OR Cancel popup should be displayed",
								"Delete OR Cancel popup is displayed");
								uiDriver.click("btndelete");
								try{
									wait.until(ExpectedConditions.stalenessOf(delLst.get(j)));
								}catch (Exception e) {
									RESULT.failed("Delete Address buttton","Delete Address buttton should get vanished",
									"DDelete Address buttton is available");
								}
							}
							else
							{
								RESULT.warning("Delete Address","Delete OR Cancel popup should be displayed",
								"Delete OR Cancel popup is not displayed");
							}
							//SleepUtils.getInstance().sleep(TimeSlab.HIGH);
							String AddressCountAfter =  uiDriver.getwebDriverLocator(
									objMap.getLocator("straddressCount")).getAttribute(
									"data-item-count");
							//checking if given address has deleted successfully or not by taking address count
							if (Integer.parseInt(AddressCountAfter) == Integer
									.parseInt(AddressCountBefore) - 1) {
								System.out
								.println("Address deleted successfully!!!!");
								RESULT
								.passed(
										"Delete Delivery Address",
										"User should be able to delete delivery address",
										myadd1
										+ " Address deleted Sucessfully!!!!");
							} else {
								RESULT
								.failed(
										"Delete Delivery Address",
										"User should be able to delete delivery address",
										myadd1 + " Address is not deleted");
								return;
							}

							break;
						}
					}
					if (b == 0) {
						RESULT.failed("Delete Delivery Address",
								"User should be able to delete Delivery Address",
								myadd1 + " Address did not match");
						return;
					}
				}
				catch(Exception e)
				{
					RESULT.failed("Delete Delivery Address", "User should be able to delete Delivery Address",
							"User is not able to Delivery Address and the exception caught is "+e.getMessage());
				}
				break;

				// Edit a delivery address	
			case Edit:
				try
				{
					//Taking list of addresses and their corresponding edit buttons
					SleepUtils.getInstance().sleep(TimeSlab.YIELD);
					List<WebElement> addLst2 = webDriver.findElements(By
							.xpath(objMap.getLocator("lstaddressPath")));
					int fSize = addLst2.size();
					List<WebElement> editLst = webDriver.findElements(By
							.xpath(objMap.getLocator("lsteditPath")));
					int gSize = editLst.size();
					String myadd2 = SelectAddress;
					String myadd2new = myadd2.replaceAll("\n\n", "\n");
					// System.out.println("given:"+myadd1);
					System.out.println(fSize);
					int c = 0;

					for (int i = 0, j = 0; i < fSize; i++, j++) {
						String gotadd2 = addLst2.get(i).getText();
						// System.out.println("Address:"+gotadd2);
						if (gotadd2.startsWith(myadd2new)) {
							c++;
							System.out.println(gotadd2);
							editLst.get(j).click();
							SleepUtils.getInstance().sleep(TimeSlab.YIELD);
							String ServiceType1 = ServiceType;
							//Editing Residential address
							if (ServiceType1.equalsIgnoreCase("Residential")) {
								uiDriver.click("radresidential");
								uiDriver.setValue("txtaddressNickname",
										AddressNickName);
								uiDriver.setValue("txtfirstNameDelAddress",
										FirstName);
								uiDriver
								.setValue("txtlastNameDelAddress", LastName);
								uiDriver
								.setValue("txtstreetAddress", StreetAddress);
								uiDriver.setValue("txtapartment", Apartment);
								uiDriver.setValue("txtaddressLine2", AddLine2);
								uiDriver.setValue("txtcity", City);
								uiDriver.setValue("drpstate", State);
								uiDriver.setValue("txtzipCode", ZipCode);
								uiDriver.setValue("txtphone", Contact1);
								uiDriver.setValue("txtphoneExtn", Ext1);
								String Contacttype = ContactType;
								if (ContactType.equalsIgnoreCase("Mobile")) {
									uiDriver.click("radmobileNumber");
								}
								else if (ContactType.equalsIgnoreCase("Home")) {
									uiDriver.click("radhomeNumber");
								}
								else if (ContactType.equalsIgnoreCase("Work")) {
									uiDriver.click("radworkNumber");
								}
								if (AddAltContact.equalsIgnoreCase("Yes")) {
									uiDriver.click("btnaddAltContact");
									uiDriver.setValue("txtaltPhone", Contact2);
									uiDriver.setValue("txtaltPhoneExtn", Ext2);
									String AltContacttype = AltContactType;
									if (AltContacttype.equalsIgnoreCase("Mobile")) {
										uiDriver.click("radaltMobileNumber");
									}
									if (AltContacttype.equalsIgnoreCase("Home")) {
										uiDriver.click("radaltHomeNumber");
									}
									if (AltContacttype.equalsIgnoreCase("Work")) {
										uiDriver.click("radaltWorkNumber");
									}
								}
								uiDriver.setValue("txtdeliveryInstructions",
										SpclDelInstructions);
								if (DoormanDelivery.equalsIgnoreCase("yes")) {
									uiDriver.click("raddoormanDelivery");
								}
								if (NeighbourDelivery.equalsIgnoreCase("yes")) {
									uiDriver.click("radneighbourDelivery");
									uiDriver.setValue("txtneighborFirstName",
											AltFirstName);
									uiDriver.setValue("txtneighborLastName",
											AltLastName);
									uiDriver.setValue("txtneighborAddress", AltApt);
									uiDriver.setValue("txtneighborPhone", AltPhone);
									uiDriver.setValue("txtneighborPhoneExtn",
											AltExtn);
								}
								if (UnattendedDelivery.equalsIgnoreCase("yes")) {
									uiDriver.click("radunattendedDelivery");
									uiDriver.setValue(
											"txtunattendedDelInstructions",
											UnattendedDelInstructions);
								}
								RESULT.done(
										"Address fields",
										"All the given data shoud be entered in relevant Address fields",
								"All the data entered in relevant Address fields");
								if (SaveCancelBtn.equalsIgnoreCase("Cancel")) {
									uiDriver.click("btncancelHome");
									RESULT.passed(
											"Edit Delivery Address",
											"User should be able to edit delivery address",
											myadd2
											+ " Address cancelled Sucessfully!!!!");
								} else {
									uiDriver.click("btnsaveHome");
									try {
										//checking for error in details entered
										int k = 0;

										List<WebElement> Errorlist = webDriver
										.findElements(By
												.xpath(objMap
														.getLocator("popnewAddressHome")));
										System.out.println("size"
												+ Errorlist.size());
										for (int p = 0; p < Errorlist.size(); p++) {
											WebElement Error = Errorlist
											.get(p);
											k++;
											String Error1 = Error
											.getAttribute("fdform-error");
											System.out.println("Error Attribute"
													+ Error1);
											if (!Error.getAttribute("fdform-error")
													.equalsIgnoreCase(null)) {
												System.out.println("error present");
												RESULT
												.warning(
														"Edit Delivery Address",
														"User should be able to edit delivery address",
												"Please fill the required details");
												return;
											}
										}
									} catch (Exception e) {
										RESULT.warning("Edit Delivery Address", "Error might have been generated and warning should be displayed",
												"Error is not generated and the execution is inside to check for error. Exception caught is "+e.getMessage());
									}
									try {
										uiDriver.wait
										.until(ExpectedConditions
												.invisibilityOfElementLocated(By
														.xpath(objMap
																.getLocator("btnsaveHome"))));
									} catch (Exception e) {
										RESULT
										.failed(
												"Edit Delivery Address",
												"User should be able to edit delivery address",
												"Exception occured  " + e);
										return;
									}
									RESULT.passed(
											"Edit Delivery Address : Home",
											"User should be able edit delivery address",
											myadd2
											+ " Address edited Sucessfully!!!!");

								}
							}
							//Editing Office address
							else if (ServiceType1.equalsIgnoreCase("Office")) {
								uiDriver.click("radofficeDelAddress");
								uiDriver.setValue("txtcompanyNameOffice",
										CompanyName);
								uiDriver.setValue("txtfirstNameOffice", FirstName);
								uiDriver.setValue("txtlastNameOffice", LastName);
								uiDriver.setValue("txtstreetAddressOffice",
										StreetAddress);
								uiDriver.setValue("txtapartmentOffice", Apartment);
								uiDriver
								.setValue("txtaddressLine2Office", AddLine2);
								uiDriver.setValue("txtcityOffice", City);
								uiDriver.setValue("drpstateOffice", State);
								uiDriver.setValue("txtzipCodeOffice", ZipCode);
								uiDriver.setValue("txtphoneOffice", Contact1);
								uiDriver.setValue("txtphoneExtnOffice", Ext1);
								if (AddAltContact.equalsIgnoreCase("Yes")) {
									uiDriver.click("btnaddAltContactOffice");
									uiDriver
									.setValue("txtaltPhoneOffice", Contact2);
									uiDriver
									.setValue("txtaltPhoneExtnOffice", Ext2);
								}
								RESULT
								.done(
										"Address fields",
										"All the given data shoud be entered in relevant Address fields",
								"All the data entered in relevant Address fields");
								if (SaveCancelBtn.equalsIgnoreCase("Cancel")) {
									uiDriver.click("btncancelOffice");
									RESULT.passed(
											"Edit Delivery Address",
											"User should be able to edit delivery address",
											myadd2
											+ " Address cancelled Sucessfully!!!!");
								} else {
									uiDriver.click("btnsaveOffice");
									//checking for error in details entered
									int k = 0;
									List<WebElement> Errorlist = webDriver
									.findElements(By
											.xpath(objMap
													.getLocator("popnewAddressOffice")));
									System.out.println("size" + Errorlist.size());
									for (int p = 0; p < Errorlist.size(); p++) {
										try {
											WebElement Error = Errorlist
											.get(p);
											k++;
											String Error1 = Error
											.getAttribute("fdform-error");
											System.out.println("Error Attribute"
													+ Error1);
											if (!Error.getAttribute("fdform-error")
													.equalsIgnoreCase(null)) {
												System.out.println("error present");
												RESULT
												.warning(
														"Edit Delivery Address",
														"User should be able to edit delivery address",
												"Please fill the required details");
												return;
											}
											// break;
										} catch (Exception e) {
											RESULT.warning("Edit Delivery Address", "Error might have been generated and warning should be displayed",
											"Error is not generated");
										}
									}

									try {
										uiDriver.wait
										.until(ExpectedConditions
												.invisibilityOfElementLocated(By
														.xpath(objMap
																.getLocator("btnsaveOffice"))));
									} catch (Exception e) {
										RESULT
										.failed(
												"Edit Delivery Address",
												"User should be able to edit delivery address",
												"Exception occured " + e);
										return;
									}

									RESULT
									.passed(
											"Edit Delivery Address : Office",
											"User should be able edit delivery address",
											myadd2
											+ " Address edited Sucessfully!!!!");

								}
							}
							else
							{
								RESULT.failed("Edit Delivery address","Service Type should be 'Office' or 'Home'",
								"Service Type is anything other than 'Office' or 'Home'");
							}

							break;
						}
					}
					if (c == 0) {
						RESULT.failed("Edit Delivery Address",
								"User should be able to edit address", myadd2
								+ " Address did not match");
						return;
					}
				}
				catch(Exception e)
				{
					RESULT.failed("Edit Delivery Address", "Delivery address should be edited successfully",
					"Delivery address is not edited ");
				}
				break;
			}
		} catch (Exception e) {
			RESULT.failed("Delivery address function", "Delivery Address function should be successful for address " + AddSelectDeleteEdit,
					"Delivery Address function is not successful for address " + AddSelectDeleteEdit);

		}
	}


	private enum AccDetailsDelAdd_CRM {
		Add, Select, SelectPickup, Delete, Edit
	};

	public void FD_AccDetailsDelAdd_CRM(
			String CRMAddSelectSelectPickupDeleteEdit,
			String CRMUseCustDefault, String CRMServiceType,
			String CRMCompName, String CRMFirstNameTb, String CRMLastNameTb,
			String CRMStreetAdd, String CRMAddLine2, String CRMAptName,
			String CRMCity, String CRMState, String CRMZipCode,
			String CRMContact, String CRMAltContact, String CRMSpecialDelivery,
			String CRMDoormanRadioBtn, String CRMNeighborRadioBtn,
			String CRMAltFirstName, String CRMAltLastName, String CRMAltApt,
			String CRMAltPhn, String CRMSaveCancelBtn, String CRMSelectAddress)
	throws InterruptedException, FindFailed

	{
		try {

			String ChoosedOpt = CRMAddSelectSelectPickupDeleteEdit;
			switch (AccDetailsDelAdd_CRM.valueOf(ChoosedOpt)) {
			/* Add a new Delivery Address to the list */
			case Add:

				List<WebElement> Listbefore = webDriver.findElements(By
						.name(objMap.getLocator("CRM_selectAddressList")));
				int bSize = Listbefore.size();
				try
				{
					uiDriver.click("CRM_AddNewAddressBtn");
				}
				catch(Exception e)
				{
					RESULT.failed("Add Address in CRM","Add button should be available",
					"Add button is not available");
					return;
				}
				if (CRMUseCustDefault.equalsIgnoreCase("yes")) {
					uiDriver.click("CRM_UseCustDefault");
				}

				String Servicetype = CRMServiceType;
				if (Servicetype.equalsIgnoreCase("CORPORATE")) {
					uiDriver.click("CRM_Commercial");
					uiDriver.setValue("CRM_CompName", CRMCompName);
				} else if (Servicetype.equalsIgnoreCase("HOME")) {
					uiDriver.click("CRM_Residential");
				}
				uiDriver.setValue("txtCRM_FirstNameTb", CRMFirstNameTb);
				uiDriver.setValue("txtCRM_LastNameTb", CRMLastNameTb);
				uiDriver.setValue("txtCRM_StreetAdd", CRMStreetAdd);
				uiDriver.setValue("txtCRM_AddLine2", CRMAddLine2);
				uiDriver.setValue("txtCRM_AptName", CRMAptName);
				uiDriver.setValue("txtCRM_City", CRMCity);
				uiDriver.setValue("txtCRM_State", CRMState);
				uiDriver.setValue("txtCRM_ZipCode", CRMZipCode);
				uiDriver.setValue("txtCRM_Contact", CRMContact);
				uiDriver.setValue("txtCRM_AltContact", CRMAltContact);
				uiDriver.setValue("txtCRM_SpecialDelivery", CRMSpecialDelivery);

				if (CRMDoormanRadioBtn.equalsIgnoreCase("yes")) {
					uiDriver.click("CRM_DoormanRadioBtn");
				}
				if (CRMNeighborRadioBtn.equalsIgnoreCase("yes")) {
					uiDriver.click("CRM_NeighborRadioBtn");
					uiDriver.setValue("txtCRM_AltFirstName", CRMAltFirstName);
					uiDriver.setValue("txtCRM_AltLastName", CRMAltLastName);
					uiDriver.setValue("txtCRM_AltApt", CRMAltApt);
					uiDriver.setValue("txtCRM_AltPhn", CRMAltPhn);
				}
				String SaveCancelButton = CRMSaveCancelBtn;
				if (SaveCancelButton.equalsIgnoreCase("save")) {
					uiDriver.click("CRM_SaveBtn");
					uiDriver.wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className(objMap.getLocator("CRM_SaveBtn"))));
					List<WebElement> Listafter = webDriver.findElements(By
							.name(objMap.getLocator("CRM_selectAddressList")));
					int aSize = Listafter.size();
					if (aSize > bSize) {
						RESULT.passed("ChooseAction",
								"Delivery Address should be added to the list",
						"Delivery Address added successfully!!");
					}
					else
					{
						RESULT.failed("ChooseAction",
								"Delivery Address should be added to the list",
						"Valid details might not have entered");
					}
				}

				else if (SaveCancelButton.equalsIgnoreCase("cancel")) {
					uiDriver.click("CRM_CancelBtn");
					System.out.println("Address canceled!!!");
					SleepUtils.getInstance().sleep(TimeSlab.YIELD);
					RESULT.passed("ChooseAction",
							"No change in the list of address",
					"Delivery address canceled");
				}

				else {
					RESULT.failed("ChooseAction",
							"Paramters should be either Save or cancel ",
					"Paramter is anything else than Save or cancel");
				}
				break;
				/*
				 * if(webDriver.getTitle().equals(
				 * "/ FreshDirect CRM : Checkout > Select Delivery Address /")) {
				 * SleepUtils.getInstance().sleep(TimeSlab.YIELD);
				 * System.out.println("Address added Sucessfully!!!!");
				 * RESULT.passed("ChooseAction",
				 * "User should be able add new delivery address",
				 * "Address added Sucessfully!!!!"); } else { String
				 * Error_Msg1=webDriver
				 * .findElement(By.xpath(objMap.getLocator("CRM_ErrorMsgAdd"
				 * ))).getText(); System.out.println(Error_Msg1);
				 * RESULT.failed("ChooseAction",
				 * "User should be able add new delivery address",Error_Msg1); }
				 */

				/* Select Delivery Address from the existing list */
			case Select:

				int flag=0;
				List<WebElement> addLst = webDriver.findElements(By
						.xpath(objMap.getLocator("CRM_AddressPath")));
				int iSize = addLst.size();

				List<WebElement> precedingSiblings = webDriver.findElements(By
						.name(objMap.getLocator("CRM_RadioPath")));
				int jSize = precedingSiblings.size();

				String myadd = CRMSelectAddress;
				System.out.println("given:" + myadd);
				System.out.println(iSize);
				System.out.println(jSize);

				//Trying to figure out why <= used? Ideally it will give ArrayIndexOutOfBoundException
				for (int i = 0, j = 0; i < iSize; i++, j++) {
					//					for (int i = 0, j = 0; i <= iSize; i++, j++) {
					String gotadd = addLst.get(i).getText();
					System.out.println(gotadd);
					if (gotadd.startsWith(myadd)) {
						flag++;
						System.out.println(gotadd);
						// precedingSiblings.get(j).getAttribute("value");
						precedingSiblings.get(j).click();
						System.out.println(precedingSiblings.get(j)
								.getAttribute("value"));
						System.out.println("Delivery Address selected");
						RESULT.passed("ChooseAction",
								"User should be able select new delivery address",
						"Address selected Sucessfully!!!!");
						break;
					} 
				}
				if(flag==0)
				{
					String Error_Msg2 = "Address did not match";
					System.out.println(Error_Msg2);
					RESULT.failed(
							"ChooseAction",
							"User should be able to select Delivery Address",
							"User is not able to select delivery address and the error is "+Error_Msg2);
				}
				break;

				/* Select Pickup Location as a Delivery Address */
			case SelectPickup:
				flag=0;
				List<WebElement> addLstP = webDriver.findElements(By
						.xpath(objMap.getLocator("CRM_PickupPath")));
				int pSize = addLstP.size();

				List<WebElement> Pickup = webDriver.findElements(By
						.xpath(objMap.getLocator("CRM_PickupRadioPath")));
				int p1Size = Pickup.size();

				String myaddP = CRMSelectAddress;
				System.out.println("given:" + myaddP);
				System.out.println(pSize);
				System.out.println(p1Size);

				for (int i = 0, j = 0; i <= pSize; i++, j++) {
					String gotaddP = addLstP.get(i).getText();
					System.out.println(gotaddP);
					if (gotaddP.startsWith(myaddP)) {
						flag++;
						System.out.println(gotaddP);
						Pickup.get(j).click();
						System.out.println("Delivery Address selected");
						RESULT.passed("ChooseAction",
								"User should be able select delivery address",
						"Address selected Sucessfully!!!!");
						break;
					} 

				}
				if(flag==0)
				{
					String Error_Msg2 = "Address did not match";
					System.out.println(Error_Msg2);
					RESULT.failed(
							"ChooseAction",
							"User should be able to select Delivery Address",
							Error_Msg2);
					break;
				}


				/* Delete Delivery Address from the existing list */
			case Delete:
				flag=0;
				List<WebElement> addLst1 = webDriver.findElements(By
						.xpath(objMap.getLocator("CRM_AddressPath")));
				int dSize = addLst1.size();
				List<WebElement> DelLst = webDriver.findElements(By
						.xpath(objMap.getLocator("CRM_DeleteAddressPath")));
				int d1Size = DelLst.size();

				String myadd1 = CRMSelectAddress;
				System.out.println("given:" + myadd1);
				System.out.println(dSize);

				for (int i = 0, j = 0; i < dSize && j < d1Size; i++, j++) {
					String gotadd1 = addLst1.get(i).getText();
					SleepUtils.getInstance().sleep(TimeSlab.YIELD);
					System.out.println("Address:" + gotadd1);
					if (gotadd1.startsWith(myadd1)) {
						flag++;
						System.out.println(gotadd1);
						SleepUtils.getInstance().sleep(TimeSlab.YIELD);
						DelLst.get(j).click();
						uiDriver.popup_ClickOkOnAlert();
						System.out.println("Address deleted successfully!!!!");
						RESULT.passed("ChooseAction",
								"User should be able add new delivery address",
						"Address deleted Sucessfully!!!!");
						break;
					} 
				}
				if(flag==0)
				{
					RESULT.failed(
							"ChooseAction",
							"User should be able to delete Delivery Address",
					"Address did not match");
				}
				break;

				/* Edit existing Delivery Address from the list */
			case Edit:
				flag=0;
				List<WebElement> addLst2 = webDriver.findElements(By
						.xpath(objMap.getLocator("CRM_AddressPath")));
				int eSize = addLst2.size();
				// WebElement common=null;
				List<WebElement> EditLst = webDriver.findElements(By
						.xpath(objMap.getLocator("CRM_EditAddressPath")));
				int e1Size = EditLst.size();

				String myadd2 = Normalizer.normalize(CRMSelectAddress, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
				System.out.println("given:" + myadd2);
				System.out.println(eSize);
				System.out.println(e1Size);

				for (int i = 0; i < eSize; i++) {

					String gotadd2 = Normalizer.normalize(addLst2.get(i).getText(), Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
					System.out.println("Address:" + gotadd2);
					if (gotadd2.startsWith(myadd2)) {
						System.out.println(gotadd2);
						flag++;
						SleepUtils.getInstance().sleep(TimeSlab.YIELD);

						for (int k = 0; k < e1Size; k++) {
							String editbtn = EditLst.get(k)
							.getAttribute("href");
							if (editbtn.contains("edit_delivery_address")) {
								// common=EditLst.get(k);
								EditLst.get(k + i).click();
								wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(objMap.getLocator("radCRM_Commercial"))));
								break;
							}
						}
						// EditLst.get(j).click();
						if (CRMUseCustDefault.equalsIgnoreCase("yes")) {
							uiDriver.click("CRM_UseCustDefault");
						}

						String Servicetype1 = CRMServiceType;
						if (Servicetype1.equalsIgnoreCase("CORPORATE")) {
							uiDriver.click("radCRM_Commercial");
							uiDriver.setValue("txtCRM_CompName", CRMCompName);
						} else if (Servicetype1.equalsIgnoreCase("HOME")) {
							uiDriver.click("radCRM_Residential");
						}

						uiDriver.setValue("txtCRM_FirstNameTb", CRMFirstNameTb);
						uiDriver.setValue("txtCRM_LastNameTb", CRMLastNameTb);
						uiDriver.setValue("txtCRM_StreetAdd", CRMStreetAdd);
						uiDriver.setValue("txtCRM_AddLine2", CRMAddLine2);
						uiDriver.setValue("txtCRM_AptName", CRMAptName);
						uiDriver.setValue("txtCRM_City", CRMCity);

						// Select Dropdown1=new
						// Select(webDriver.findElement(By.name(objMap.getLocator("CRM_State"))));
						// Dropdown1.selectByValue(input.get("CRM_State"));

						uiDriver.setValue("drpCRM_State", CRMState);
						uiDriver.setValue("txtCRM_ZipCode", CRMZipCode);
						uiDriver.setValue("txtCRM_Contact", CRMContact);
						uiDriver.setValue("txtCRM_AltContact", CRMAltContact);
						uiDriver.setValue("txtCRM_SpecialDelivery",
								CRMSpecialDelivery);

						if (CRMDoormanRadioBtn.equalsIgnoreCase("yes")) {
							uiDriver.click("CRM_DoormanRadioBtn");
						}

						if (CRMNeighborRadioBtn.equalsIgnoreCase("yes")) {
							uiDriver.click("CRM_NeighborRadioBtn");
							uiDriver.setValue("txtCRM_AltFirstName",
									CRMAltFirstName);
							uiDriver.setValue("txtCRM_AltLastName",
									CRMAltLastName);
							uiDriver.setValue("txtCRM_AltApt", CRMAltApt);
							uiDriver.setValue("txtCRM_AltPhn", CRMAltPhn);
						}

						String SaveCancelButton1 = CRMSaveCancelBtn;
						if (SaveCancelButton1.equalsIgnoreCase("save")) {
							uiDriver.click("CRM_SaveBtn");
							try{
							uiDriver.wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className(objMap.getLocator("CRM_SaveBtn"))));
							if (webDriver.getTitle().equals("/ FreshDirect CRM : Account Details /")) {
								RESULT.passed("Edit Address",
										"Address should be edited",
								"Address edited successfully!!");
							}	
							else
							{
								RESULT.failed("Edit Address",
										"Address should be edited",
								"ADdress is not edited, Please Enter valid details");
							}
						}
						catch (Exception e) {
							RESULT.failed("Edit Address",
									"Address should be edited",
							"ADdress is not edited, Please Enter valid details");
						}
						}

						else if (SaveCancelButton1.equalsIgnoreCase("cancel")) {
							uiDriver.click("CRM_CancelBtn");
							System.out.println("Address canceled!!!");
							SleepUtils.getInstance().sleep(TimeSlab.YIELD);
							RESULT.passed("Edit Address",
									"Delivery address should remain unchanged",
							"Delivery address not edited");
							break;
						} else {
							RESULT.failed("Edit Address",
									"Parameters should be either Save or Cancel",
							"Parameters are other than Save or Cancel");
							return;
						}

					}

				}
				if(flag==0)
				{
					RESULT.failed("Edit Address","Desired Address should be available to be edited",
					"Desired Address is not available to edit");
				}
				break;

			}
		} catch (Exception e) {


			RESULT.failed("FD_AccDetailsDelAdd_CRM Exception ", "Delivery Address option : Select/Edit/Delete should be performed successfully",
					"Delivery Address option : Select/Edit/Delete is not performed successfully and the exception caught is " + e.getMessage());
			return;
		}

	}

	/**
	 * Function to search the product in CRM and adds it to CRM cart for
	 * customer
	 * 
	 * Pre-condition: 1. Login must be called before this function. 2. Search
	 * for the customer for which we need to place New Order
	 * 
	 * @param method
	 *            : defines the way we start to search for a product i.e. search
	 *            or browse
	 * 
	 * @param path
	 *            : Path of the hierarchy for the link text we need to select
	 *            from the available categories on page
	 * 
	 * @param product
	 *            : Product link text we need to select from the available
	 *            product on page.
	 * 
	 * @param product_method
	 *            : Defines the way we want to add the product to cart
	 *            i.e.directly from product page or clicking on to product
	 * 
	 * @param count
	 *            : Count that we need to enter into input for product
	 */

	public void FD_selectProdBrowse_CRM(String method, String path,
			String product, String product_method, String count) {

		WebDriverWait wait = new WebDriverWait(webDriver, 20);

		// click on New order
		webDriver.findElement(
				By.linkText(objMap.getLocator("btnsearch_product_new_order")))
				.click();
		try {
			// for browse method if else search method
			if (method.equalsIgnoreCase("browse")) {
				// click on browse
				webDriver.findElement(
						By.linkText(objMap
								.getLocator("btnsearch_product_browse")))
								.click();
				// wait and click on the hierarchy based on the path from data
				String split_path[] = path.split("->");
				for (int i = 0; i < split_path.length; i++) {
					wait.until(ExpectedConditions.visibilityOfElementLocated(By
							.linkText(split_path[i])));
					webDriver.findElement(By.linkText(split_path[i])).click();
				}

			} else {
				// click on browse
				webDriver.findElement(
						By.linkText(objMap.getLocator("btnsearchProduct")))
						.click();
				uiDriver.selenium.waitForPageToLoad("10000");
				// enter the product in textbox
				uiDriver.setValue("btnsearchProductBox", product);
				// click one the search button
				uiDriver.click("btnsearchButton");
				// webDriver.findElement(By.xpath(objMap.getLocator("btnsearchButton"))).click();
			}
			// Boolean to know if product is found
			boolean product_found = false;
			// Store the String of the product added to cart for the verifiation
			String product_added_text = null;
			// get the <tr> list of product
			List<WebElement> CRM_product_list = webDriver.findElements(By
					.xpath(objMap.getLocator("lstproductCRM")));
			// go through the list to find the product
			for (int i = 2; i < CRM_product_list.size(); i++) {
				String compare_product = CRM_product_list.get(i).getText();
				if (compare_product.contains(product)
						&& !compare_product.contains("0 matches")) {
					// store the result that product found
					product_found = true;
					// store the text
					product_added_text = compare_product.split("\\n")[1].trim();
					// add directly from the list of product page
					if (product_method.equalsIgnoreCase("hover")) {
						// enter the the quantity
						WebElement product_count = CRM_product_list
						.get(i)
						.findElement(
								By
								.xpath(objMap
										.getLocator("txtproductCRMHover")));
						product_count.clear();
						product_count.sendKeys(count);
						// click add button
						WebElement product_add = CRM_product_list
						.get(i)
						.findElement(
								By
								.xpath(objMap
										.getLocator("btnproductCRMHoverAdd")));
						product_add.click();
						uiDriver.selenium.waitForPageToLoad("10000");

					} else {
						// click on the product
						CRM_product_list.get(i).findElement(
								By.xpath(objMap
										.getLocator("lnkproductCRMClick")))
										.click();
						uiDriver.selenium.waitForPageToLoad("10000");
						// enter the the quantity
						// wait.until(ExpectedConditions
						// .presenceOfElementLocated(By.xpath(objMap
						// .getLocator("txtproductCRMClick"))));
						WebElement product_count = webDriver.findElement(By
								.className(objMap
										.getLocator("txtproductCRMClick")));
						for (int c = 0; c < Integer.parseInt(count) - 1; c++)
							product_count.click();
						// click add button
						WebElement product_add = webDriver
						.findElement(By.id(objMap
								.getLocator("btnproductCRMClickAdd")));
						product_add.click();
						uiDriver.selenium.waitForPageToLoad("10000");
					}
					break;
				}
			}

			// if entered product is not available select the first one
			if (!product_found && method.equalsIgnoreCase("browse")) {
				product_added_text = CRM_product_list.get(2).getText().split(
				"\\n")[1].trim();
				if (product_method.equalsIgnoreCase("hover")) {
					// enter the the quantity
					WebElement product_count = CRM_product_list.get(2)
					.findElement(
							By.xpath(objMap
									.getLocator("txtProductCRMHover")));
					product_count.clear();
					product_count.sendKeys(count);
					// click add button
					WebElement product_add = CRM_product_list
					.get(2)
					.findElement(
							By
							.xpath(objMap
									.getLocator("btnProductCRMHoverAdd")));
					product_add.click();
					uiDriver.selenium.waitForPageToLoad("10000");
				} else {
					// click on the product
					CRM_product_list.get(2).findElement(
							By.xpath(objMap.getLocator("lnkProductCRMClick")))
							.click();
					uiDriver.selenium.waitForPageToLoad("10000");
					// enter the the quantity
					// wait.until(ExpectedConditions
					// .presenceOfElementLocated(By.id(objMap
					// .getLocator("txtProductCRMClick"))));
					WebElement product_count = webDriver.findElement(By
							.xpath(objMap.getLocator("txtProductCRMClick")));
					product_count.clear();
					product_count.sendKeys(count);
					// click add button
					WebElement product_add = webDriver.findElement(By.id(objMap
							.getLocator("btnProductCRMClickAdd")));
					product_add.click();
					uiDriver.selenium.waitForPageToLoad("10000");
				}
			}
			// flag for the result
			boolean result = false;
			// check for the product in cart only if we have the product text
			if (product_added_text != null) {
				// get the order detail <tr> list
				List<WebElement> CRM_cart_product_list = webDriver
				.findElements(By.xpath(objMap
						.getLocator("lstproductCRMCart")));

				// go through the list to find the product added for result
				for (int i = 0; i < CRM_cart_product_list.size(); i++) {
					String compare_product = CRM_cart_product_list.get(i)
					.getText();
					if (compare_product.contains(product_added_text)) {
						result = true;
						break;
					}
				}
			}
			// result
			if (!result) {
				RESULT.failed("Search product CRM",
						"Product search should be Successfully: "
						+ product_added_text,
						"Failed to search and add product: " + product);
			} else {
				RESULT.passed("Search product CRM",
						"Product search should be Successfully: " + product,
						"Product searched and added: " + product_added_text);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 *Function Name: FD_chooseTimeslot Purpose: Function to Select Time slot
	 * for order Delivery Created By: Tejas Patel Created Date: Modified By:
	 * Modified Date:
	 * 
	 * @param Delivery_Day
	 *            :Define day for order Delivery
	 * @param Time_Slot
	 *            :Select Time slot for given Delivery_Day
	 * @param FlexibilityFlag
	 *            :It will give Flexibility to user for selecting Time slot
	 **/

	public void FD_chooseTimeslot(String Delivery_Day, String Time_Slot,
			String FlexibilityFlag) throws InterruptedException {
		// WebDriverWait wait = new WebDriverWait(webDriver, 10);
		// convert given Delivery_day into UpperCase
		String DeliveryDay = Delivery_Day.toUpperCase();
		// Store the given Slot into local variable
		String TimeSlot = Time_Slot;
		FlexibilityFlag = FlexibilityFlag.toUpperCase();
		// find the list of days from table
		try {
			wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By
					.xpath(objMap.getLocator("lstdays"))));
		} catch (TimeoutException e) {
			RESULT.warning("Time Slot ", "List of days should be available", "List of days is not available");
			return;

		}
		List<WebElement> Days = webDriver.findElements(By.xpath(objMap.getLocator("lstdays")));
		// defined flag for check given slot is sold out or not
		boolean soldout = false;
		// defined flag for outer for loop break;
		boolean flag1 = false;
		// defined flag to check no slot available for given day
		boolean NoSlotonday = false;
		// defined Flag To check slot available or not
		boolean Check_Slot = false;
		// defined flag for select available slot for given day
		boolean No_Slot = false;
		// redefination of flag for reservation
		reserve_flag = false;
		alcoholRestriction = false;
		// iterating individual day from list
		int p = 0;
		try
		{
			for(int c=0;c<2;c++){
				for (WebElement element : Days) {
					// for chrome need wait
					SleepUtils.getInstance().sleep(TimeSlab.YIELD);

					// store the name of the day
					String day = element.findElement(
							By.xpath(objMap.getLocator("strfullday"))).getText();
					if (day.equals("") || day == null) {
						// store the name of the day
						day = element.findElement(By.xpath(objMap.getLocator("strhalfday"))).getText();
					}
					// comparing day with DeliveryDay

					if (DeliveryDay.contains(day)) {
						p++;
						if (p == 2) {
							RESULT.failed("Choose Time Slot ",
									"User should able to select Time slot for whole week ",
							"User is unable to select Time slot for whole week");
							return;
						}
						// find the list of Time Slot for given Delivery_day
						List<WebElement> Slots ;
						try
						{
							Slots= element.findElements(By.xpath(objMap.getLocator("lstslotList")));
						}
						catch(Exception e)
						{
							RESULT.failed("Time Slot", "Time slot list should be available", "Time slot list is not available");
							return;
						}

						// count the slot for given day
						int i = 0;
						// count the iteration of for loop
						int j = 0;
						do {
							j++;
							// iterating individual Slot for given Delivery_day
							for (WebElement element1 : Slots) {
								// store the text of the slot
								String Slot = element1.getText();
								// System.out.println("slot is  "+ Slot );
								if (Slot.contains(TimeSlot)) {
									if (reserve_flag == true) {
										if (Slot.contains("Reserved")
												&& Slot.contains(verify_time
														.split("\n")[0])
														&& day.contains(verify_day.substring(0,
																3).toUpperCase())) {
											System.out.println("Reservation passed");
											RESULT
											.passed(
													"Time slot reservation",
													"User should be able to see reserved time slot",
											"User is able to see reserved time slot");
										} else {
											System.out.println("Reservation failed");
											RESULT.failed(
													"Time slot reservation error ",
													"User could not see reservation ",
											"User is unable to see time slot");
											return;
										}
									} else {
										// select given Time_slot
										try
										{
											element1.findElement(By.xpath(objMap.getLocator("radslotSelect")))
											.click();
										}
										catch(Exception e)
										{
											RESULT.failed("Time Slot",TimeSlot+"Time slot should be clicked",TimeSlot+"Time slot is not clicked");
										}

										SleepUtils.getInstance().sleep(TimeSlab.YIELD);
										Slot = element1.getText();
										if (Slot.contains("SOLD OUT")) {
											soldout = true;
											RESULT.warning(
													"choose Time Slot",
													"Given Time Slot should be available ",
													"Given Slot is SOLD OUT"
													+ TimeSlot);
										} else {
											if (element1.findElements(By.xpath(objMap.getLocator("imgalcoholRestriction")))
													.size() > 0) {
												alcoholRestriction = true;

											}
											flag1 = true;
											verify_day = Delivery_Day;
											verify_time = TimeSlot;
											System.out
											.println("Your order will delivered at   "
													+ TimeSlot + day);
											Check_Slot = true;
											robot.scrollToElement(element1,webDriver);
											RESULT.passed(
													"choose Time Slot",
													"User should be able to Select Given Time Slot",
													"User has select Time Slot successfully"
													+ TimeSlot+ day);
											break;
										}
									}

								}
								// given Slot not found then it will take first slot of
								// given day
								else if ((No_Slot == true)
										&& (FlexibilityFlag.equals("YES"))) {
									if ((Slot.contains("am") || Slot.contains("pm"))
											&& Slot.contains("-")) {
										try
										{
											element1.findElement(By.xpath(objMap.getLocator("radslotSelect")))
											.click();
										}
										catch(Exception e)
										{
											RESULT.failed("Time Slot : No slot as per data and FF=Yes",
													element1.findElement(By.xpath(objMap.getLocator("radslotSelect"))).getText()+
													"Time slot should be clicked",
											"Time slot is not clicked");
										}

										SleepUtils.getInstance().sleep(TimeSlab.YIELD);
										Slot = element1.getText();
										if (Slot.contains("SOLD OUT")) {
											RESULT.warning("choose Time Slot",
													"  Time Slot should be available ",
													" Time Slot is SOLD OUT" + Slot);
										} else {
											i++;
											if (i == 1) {
												if (element1.findElements(By.xpath(objMap.getLocator("imgalcoholRestriction")))
														.size() > 0) {
													alcoholRestriction = true;
													System.out
													.println(" AlcoholRestriction Flag   "
															+ alcoholRestriction);
												}
												flag1 = true;
												verify_day = day;
												verify_time = Slot;
												System.out
												.println(" Fresh direct will deliver your order at"
														+ Slot + DeliveryDay);
												Check_Slot = true;
												robot.scrollToElement(element1,webDriver);
												RESULT.passed(
														"choose Time slot",
														"User should be able to Select Time Slot for given day",
														"User has select Time Slot successfully for given day  "
														+ day + "  "
														+ Slot);
												break;
											}
										}
									}
								}
							}

							if ((j == 1) && (Check_Slot == false) && (soldout == false))
								// for given day if given slot not found than print the
								// message
							{
								RESULT.warning("choose Time Slot",
										" Given Time Slot should be available ",
										"No Delivery available for given slot"
										+ TimeSlot);
								System.out
								.println(" No Delivery available for given  slot  "
										+ TimeSlot);

							}
							if ((i == 0) && (j == 2) && (FlexibilityFlag.equals("YES")))
								// for given day if slot not found than print the message
							{
								System.out
								.println(" No Delivery available for given day   "
										+ DeliveryDay);
								Check_Slot = true;
								NoSlotonday = true;
								RESULT.warning("choose Time Slot",
										"Time Slot should be available for given day",
										"No Delivery available for given day"
										+ DeliveryDay);
							}
							if ((j == 1) && (FlexibilityFlag.equals("NO"))) {
								flag1 = true;
								Check_Slot = true;
								RESULT.failed("choose Time Slot",
										" Given Time Slot should be available ",
										"No Delivery available for given slot"
										+ TimeSlot);
								return;
							}
							No_Slot = true;
						} while ((Check_Slot == false) && (No_Slot == true));

					} else if ((NoSlotonday == true) && (FlexibilityFlag.equals("YES"))) {

						// find the list of Time Slot for given Delivery_day
						List<WebElement> Slots;
						try{
							Slots= element.findElements(By.xpath(objMap
									.getLocator("lstslotList")));
						}
						catch(Exception e)
						{
							RESULT.failed("Time Slot : No slot on day and FF=Yes", "Time slot list should be available ",
							"Time slot list is not available");
							return;
						}

						for (WebElement element1 : Slots) {
							String Slot = element1.getText();
							if ((Slot.contains("am") || Slot.contains("pm"))
									&& Slot.contains("-")) {
								try
								{
									element1.findElement(
											By.xpath(objMap.getLocator("radslotSelect")))
											.click();
								}
								catch(Exception e)
								{
									RESULT.failed("Time Slot : No slot as per data and FF=Yes",
											element1.findElement(
													By.xpath(objMap.getLocator("radslotSelect"))).getText()+
													" Time slot should be clicked",
									"Time slot is not clicked");
								}

								SleepUtils.getInstance().sleep(TimeSlab.YIELD);
								Slot = element1.getText();
								if (Slot.contains("SOLD OUT")) {
									RESULT.warning("choose Time Slot",
											" Time Slot should be available ",
											"Time Slot is SOLD OUT" + Slot);
								} else {
									if (element1.findElements(By.xpath(objMap.getLocator("imgalcoholRestriction")))
											.size() > 0) {
										alcoholRestriction = true;
										System.out
										.println(" AlcoholRestriction Flag   "
												+ alcoholRestriction);
									}
									flag1 = true;
									verify_day = day;
									verify_time = Slot;
									System.out
									.println(" Fresh direct will deliver your order at  "
											+ Slot + day);
									robot.scrollToElement(element1,webDriver);
									RESULT.passed(
											"choose Time Slot",
											"User should be able to Select Time Slot for next day",
											"User has select Time Slot successfully   "
											+ Slot + "  " + day);
									break;
								}
							}
						}

					}
					if (flag1 == true) {
						break;
					}
				}
				if (flag1 == true) {
					break;
				}
			}
		}
		catch(Exception e)
		{
			RESULT.failed("Time slot exception", "Time slot should be successfully selected", "Time slot is not selected successfully");
		}
		System.out.println(" AlcoholRestriction Flag   " + alcoholRestriction);
	}


	public void FD_unavailablePopUp(){

		try{

			uiDriver.waitForPageLoad();
			SleepUtils.getInstance().sleep(TimeSlab.YIELD);
			// check if the unavailable popup is available then perform the operatiuon
			if (webDriver.findElements(By.xpath(objMap.getLocator("strverifyWarning"))).size() > 0  
					&& webDriver.findElement(By.xpath(objMap.getLocator("strverifyWarning"))).isDisplayed()) {

				// get the warning message
				RESULT.warning(
						"Unvailability popup",
						"Few items should be lesser than the available cart items.",
						"Message: "+ webDriver.findElement(By.xpath(objMap.getLocator("strverifyWarning"))).getText());
				try {
					// verify Continue button is available
					if (webDriver.findElements(By.xpath(objMap.getLocator("btncontinuePopUp"))).size() > 0 
							&& webDriver.findElement(By.xpath(objMap.getLocator("btncontinuePopUp"))).isDisplayed()) 
					{
						uiDriver.getwebDriverLocator(
								objMap.getLocator("btncontinuePopUp")).click();
						wait.until(ExpectedConditions
								.invisibilityOfElementLocated(By.xpath(objMap
										.getLocator("btncontinuePopUp"))));
						SleepUtils.getInstance().sleep(TimeSlab.YIELD);
						// to verify order total for checkout
						if (webDriver.findElements(By.xpath(objMap.getLocator("strverifyWarningOnPlaced"))).size() > 0 
								&& webDriver.findElement(By.xpath(objMap.getLocator("strverifyWarningOnPlaced"))).isDisplayed() )
						{
//							RESULT.warning(
//									"Unvailability popup",
//									"Order should be placed and review page should be displayed.",
//									"ATTENTION: "
//									+ webDriver.findElement(By.xpath(objMap.getLocator("strverifyWarningOnPlaced"))).getText());
//						return;
							
							try{
						    String cartSubtotal = uiDriver.getwebDriverLocator(objMap.getLocator("txtSubTotal")).getText();
							cartSubtotal = cartSubtotal.replaceAll(",", "");
							
							// for $50 check
							List<WebElement> addLst = webDriver.findElement(By.xpath(objMap.getLocator("strofficeDelivery"))).findElements(By.tagName("option"));
							
							int iSize = addLst.size();
							int a = 0;
							for (int i = 0; i < iSize; i++) {
								try {
									if (addLst.get(i).getAttribute("selected").equalsIgnoreCase("true")) {
										a++;
										break;
									}
								} catch (NullPointerException e) {
									continue;
								}
							}
							
							if(Double.parseDouble(cartSubtotal.replace("$", "")) < Double.parseDouble("30"))
							{
								if (webDriver.findElements(By.xpath(objMap.getLocator("strverifyWarningOnPlaced"))).size() >0)
								{
									RESULT.warning("Checkout verification",
											"User should get warning message for minimum cart requirement",
											"ATTENTION: "
											+ webDriver.findElement(By.xpath(objMap.getLocator("strverifyWarningOnPlaced"))).getText());
									return;
								}
								else
								{
									RESULT.failed(
											"Checkout verification",
											"User should get warning message for minimum cart requirement",
									"No warning message is displayed for  minimum cart requirement");
									return;
								}
							}
							
							else if (Double.parseDouble(cartSubtotal.replace("$", "")) < Double.parseDouble("50")
									&& Double.parseDouble(cartSubtotal.replace("$", "")) > Double.parseDouble("30")&& a>0)
							{
								if(webDriver.findElement(By.xpath(objMap.getLocator("strverifyWarningOnPlaced"))).getText().contains("$50.00") && a>0) 
								{
									RESULT.warning("Checkout verification",
											"User should get warning message for minimum cart requirement",
											"ATTENTION: "
											+ webDriver.findElement(By.xpath(objMap.getLocator("strverifyWarningOnPlaced"))).getText());
									return;
								}		
								else
								{
									RESULT.failed(
											"Checkout verification",
											"User should get warning message for minimum cart requirement",
									"No warning message is displayed for  minimum cart requirement");
									return;
								}
							}							
							}catch(Exception e){
								RESULT.failed("Order subtotal", "Order subtotal should be get", "Order subtotal is not get due to" + e);
							}	
						}						
					}else{
						RESULT.failed("Items unavailability pop up", "Continue button should be available", "Continue button is not available");
					}
				} catch (Exception e) {
					RESULT.failed("Items unavailability pop up",
							"Some items in the product shows unavailability and Continue button should be clicked",
					"Some items in the product shows unavailability and Continue button is not clicked");
					return;
				}
			}
		}catch(Exception e){
			RESULT.failed("Items unavailability pop up",
					"Unavailability pop up should get handled",
			"Unavailability pop up is not handled");
		}
	}

	/**
	 *Function Name: FD_returnProducts Purpose: Function to store the products
	 * and their respective quantity to be verified in FD_placeOrder Created By:
	 * Bhavik Tikudiya Created Date: Modified By: Modified Date:
	 * 
	 * return void
	 * 
	 **/

	public void FD_returnProducts() {
		try {
			uiDriver.waitForPageLoad();
			// check if the warning is available for the products
			/*if (webDriver.findElements(By.xpath(objMap.getLocator("strverifyWarning"))).size() > 0  
					&& webDriver.findElement(By.xpath(objMap.getLocator("strverifyWarning"))).isDisplayed()) {
				RESULT.warning(
						"Place Order",
						"Few items should be lesser than the available cart items.",
						"Message: "+ webDriver.findElement(By.xpath(objMap.getLocator("strverifyWarning"))).getText());

				try {
					// verify save changes button is enabled
					if (webDriver.findElements(By.xpath(objMap.getLocator("btnsaveChanges"))).size() > 0 
							&& webDriver.findElement(By.xpath(objMap.getLocator("btnsaveChanges"))).isDisplayed()) 
					{
						uiDriver.getwebDriverLocator(
								objMap.getLocator("btnsaveChanges")).click();
						wait.until(ExpectedConditions
								.invisibilityOfElementLocated(By.xpath(objMap
										.getLocator("btnsaveChanges"))));

						// ******** After click on save changes button calculate total and predict the error message instead of if>>and handle********
						if (webDriver.findElements(By.xpath(objMap.getLocator("strverifyWarningOnPlaced"))).size() > 0 
								&& webDriver.findElement(By.xpath(objMap.getLocator("strverifyWarningOnPlaced"))).isDisplayed() ) {
							RESULT.warning(
									"Place Order",
									"Order should be placed and review page should be displayed.",
									"ATTENTION: "
									+ webDriver.findElement(By.xpath(objMap.getLocator("strverifyWarningOnPlaced"))).getText());
						}
					}
				} catch (Exception e) {
					RESULT.failed("Item unavailability exception",
							"Some items in the product shows unavailability and save changes button should be clicked",
							"Some items in the product shows unavailability and save changes button is not clicked");
					return;
				}
			}*/

			// Extract Zip code
			try
			{
				String Address = webDriver.findElement(By.xpath(objMap.getLocator("straddress"))).getText();
				Zipcode = Address.substring(Math.max(0, Address.length() - 5));
			}
			catch(NoSuchElementException e)
			{
				RESULT.warning("Checkout page : zipcode extraction", "zipcode should be extracted successfully", "Zipcode is not extracted from checkout page ");
			}

			// store the subtotal in global variable
			try
			{
				String temp_subtotal = uiDriver.getwebDriverLocator(objMap.getLocator("strverifySubTotal")).getText();
				verify_subtotal = temp_subtotal.replace("$", "").replace(",", "");

			}catch(NoSuchElementException e)
			{
				RESULT.failed("Checkout page : subtotal extraction", "Subtotal should be extracted successfully", "Subtotal is not extracted from checkout page ");
			}

			// take the list of items available in cart
			List<WebElement> availableProducts = webDriver.findElements(By.xpath(objMap.getLocator("lstverifyProducts")));
			// intitialize the global arrays
			verify_products = new String[availableProducts.size()];
			verify_products_quantity = new String[availableProducts.size()];
			verify_products_unit = new String[availableProducts.size()];

			// go through the list to get product name its respective quantity
			// int count = 0;
			if(availableProducts.size()>0)
			{
				for (int i = 0; i < availableProducts.size(); i++) {
					String product_text = availableProducts.get(i).findElement(
							By.xpath(objMap.getLocator("strverifyProductName")))
							.getText();
					String product_quantity_text = availableProducts.get(i)
					.findElement(
							By.xpath(objMap
									.getLocator("strverifyProductQty")))
									.getText();
					String product_unit_text = availableProducts.get(i)
					.findElement(
							By.xpath(objMap
									.getLocator("strverifyProductUnit")))
									.getText();
					verify_products[i] = product_text.replace("(new)", "");
					verify_products_quantity[i] = product_quantity_text;
					verify_products_unit[i] = product_unit_text;
				}
			}
			else
			{
				RESULT.warning("Item unavailbility for delivery on selected slot", "Some items should be available ", "No items available for delivery");
			}


		} catch (Exception e) {
			RESULT.warning(
					"Place Order",
					"Order should be placed and review page should be displayed.",
			"Enable to retrieve products for verification");
		}
	}

	/**
	 *Function Name: FD_placeOrder Purpose: Function to review the details on
	 * the success page of order submission Created By: Bhavik Tikudiya Created
	 * Date: Modified By: Modified Date:
	 * 
	 * @param day
	 *            : Day selected for the order delivery
	 *@param time
	 *            : Time selected for the order delivery return void
	 * 
	 **/
	public void FD_placeOrder(String day, String time) {
		try {
			String t_verify_subtotal = null;
			SleepUtils.getInstance().sleep(TimeSlab.YIELD);
			// To check if success page will avialble at the end or not
			boolean success_page = true;
			try {
				// click on the place order button
				if (webDriver.findElements(By.xpath(objMap.getLocator("btnplaceOrder"))).size() > 0) {
					uiDriver.getwebDriverLocator(objMap.getLocator("btnplaceOrder")).click();
					uiDriver.waitForPageLoad();
					///****************************Place a logic to track defect whether any 30$ or 50$ or 750$ check should come or not******************
					try {
						webDriver.findElement(By.className(objMap
								.getLocator("strinternalError")));
						{
							RESULT.failed("Internal Error",
									"Internal Error",
									"Order is not submitted,due to Error "+ webDriver.findElement(By.className(objMap.getLocator("strinternalError")))
									.getText());
							return;
						}
					} 
					catch (Exception e1) 
					{
						try 
						{
							uiDriver.waitForPageLoad();
							(new WebDriverWait(webDriver, 40)).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(objMap.getLocator("strverifyWarning"))));
						
							//wait.until(ExpectedConditions.visibilityOf(webDriver.findElement(By.xpath(objMap.getLocator("btnsaveChanges")))));
							// handle the save chnages button after place order
							// click
							//if (webDriver.findElements(By.xpath(objMap.getLocator("btnsaveChanges"))).size() > 0) 
							//{
							// handle unavailablity pop-up 
							uiDriver.FD_unavailablePopUp();
							//return if we are not on checkout page
							if (!webDriver.getTitle().equalsIgnoreCase("Checkout")) {								
								return;
							}
							FD_returnProducts();							
							uiDriver.getwebDriverLocator(objMap.getLocator("btnplaceOrder")).click();
							uiDriver.waitForPageLoad();
							//**********************Calculate order total to track defect whether any 30$ or 50$ or 750$ check should come or not***********
							if(webDriver.findElements(By.className(objMap.getLocator("strinternalError"))).size()>0)
							{
								RESULT.warning(
										"Internal Error",
										"Internal Error",
										"Order is not submitted,due to Error "
										+ webDriver.findElement(By.className(objMap.getLocator("strinternalError"))).getText());
								return;
							}

							//}
						} catch (Exception e)
						{

						}
					}

				}
			} catch (NullPointerException e) {
				RESULT.failed("Add items to cart",
						"Place order button should be displayed",
				"Place order button is not displayed");
				return;
			}
			// wait for the invisibility of the place order
			try {
				wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(objMap.getLocator("btnChangeTimeSlot"))));
			}
			catch (Exception e)
			{
				// show message if order total exceed message is displayed
				//*************Calculate order total to track defect whether any 750$ check should come or not**********************
				if (webDriver.findElements(By.xpath(objMap.getLocator("strsubtotalExceeds"))).size() > 0) {
					RESULT.warning(
							"Place Order",
							"Message showing the order limit should be diaplyed.",
							"Message: "+ webDriver.findElement(By.xpath(objMap.getLocator("strsubtotalExceeds"))).getText());
					return;
				}
			}
			SleepUtils.getInstance().sleep(TimeSlab.YIELD);
			//*************Remove this if : Calculate order total to track defect whether any 30$ or 50 check should come or not**********************
			if (!(webDriver.findElements(By.xpath(objMap.getLocator("strverifyWarningOnPlaced"))).size() > 0)) 
			{
				if (webDriver.findElements(By.xpath(objMap.getLocator("strorderNumber"))).size() > 0) {
					//save order number as required at the time of modification of same order
					orderNum=webDriver.findElement(By.xpath(objMap.getLocator("strorderNumber"))).getText().split("#")[1];
					System.out.println("ordernum"+orderNum);
					RESULT.passed(
							"Order Placed",
							"Order should be submitted",
							"Order is submitted: "
							+ webDriver.findElement(By.xpath(objMap.getLocator("strorderNumber"))).getText());
				}
				else 
				{
					RESULT.failed("Place order", "Order number should be submitted and order number should be generated",
					"Order number is not generated");
				}
				// verify the time and day
				// get the time text to be verified
				try
				{
					String time_text = uiDriver.getwebDriverLocator(objMap.getLocator("strverifyTime")).getText();
					// covert it into the specified format
					time_text = time_text.replaceAll(" ", "");
					String TimeSlot1 = time_text.split("-")[0];
					String TimeSlot2 = time_text.split("-")[1];
					TimeSlot1 = TimeSlot1.replaceAll(("AM"), "");
					TimeSlot1 = TimeSlot1.replaceAll(("PM"), "");
					time_text = TimeSlot1 + "-" + TimeSlot2;
					// get the day text to be verified
					String day_text = uiDriver.getwebDriverLocator(objMap.getLocator("strverifyDay")).getText().toUpperCase();
					// result based on the verification
					if (day_text.contains(day.toUpperCase()) && time_text.contains(time.toUpperCase())) {
						RESULT.passed("Place Order", "Time Slot should be: " + day
								+ " " + time, "Time Slot is: " + time_text);
					} else {
						RESULT.failed("Place Order", "Time Slot should be: " + day
								+ " " + time, "Time Slot is: " + time_text);
					}
				}
				catch(NoSuchElementException e)
				{
					RESULT.failed("Day and Time verification", "Day and time should be available on success page",
					"Day or time is not available on success page");
					return;
				}catch(NullPointerException e)
				{
					RESULT.failed("Day and Time verification", "Day and time should be available on success page",
					"Day or time is null on success page");
					return;
				}
				try
				{
					t_verify_subtotal = uiDriver.getwebDriverLocator(objMap.getLocator("strverifySubTotalSubmit")).getText().replace(",", "").replace("*", "");
					// verify subtotal
					if (t_verify_subtotal.contains(verify_subtotal)) {
						RESULT.passed("Place Order", "Subtotal should be: "
								+ verify_subtotal, "Subtotal is: "
								+ t_verify_subtotal);
					} else {
						RESULT.failed("Place Order", "Subtotal should be: "
								+ verify_subtotal, "Subtotal is: "
								+ t_verify_subtotal);
					}
				}
				catch(NoSuchElementException e)
				{
					RESULT.failed("Sub total verification", "Sub total should be available on Order summary page",
					"Sub total is not available on Order summary page");
					return;
				}catch(NullPointerException e)
				{
					RESULT.failed("Sub total verification", "Sub total should be available on Order summary page",
					"Sub total is not available on Order summary page");
					return;
				}

				// verify the products and quantity
				try
				{
					wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(objMap.getLocator("lstverifyProducts"))));
					List<WebElement> products_to_verify = webDriver.findElements(By.xpath(objMap.getLocator("lstverifyProducts")));

					String product_to_verify_name[] = new String[products_to_verify.size()];
					String product_to_verify_qty[] = new String[products_to_verify.size()];
					String product_to_verify_unit[] = new String[products_to_verify.size()];
					
					// go through the list to get product name its respective
					// quantity
					if(products_to_verify.size()>0)
					{
						for (int i = 0; i < products_to_verify.size(); i++) 
						{
							try
							{
								String product_text = products_to_verify.get(i).findElement(By.xpath(objMap.getLocator("strverifyProductName"))).getText();
								String product_quantity_text = products_to_verify.get(i).findElement(By.xpath(objMap.getLocator("strverifyProductQty"))).getText();
								String product_unit_text = products_to_verify.get(i).findElement(By.xpath(objMap.getLocator("strverifyProductUnit"))).getText();
								product_to_verify_name[i] = product_text;
								product_to_verify_qty[i] = product_quantity_text;
								product_to_verify_unit[i]= product_unit_text;
							}
							catch(Exception e)
							{
								RESULT.failed("Product Details", "Product name and quantity should be available", "Product name and quantity is not available");
							}

						}
						for (int i = 0; i < products_to_verify.size(); i++) {
							if ((verify_products[i].equals(product_to_verify_name[i]))
									&& verify_products_quantity[i].equals(product_to_verify_qty[i])) 
							{
								RESULT.passed("Place Order", "Expected product: "
										+ verify_products[i] + " and Quantity: "
										+ verify_products_quantity[i],
										"Actual product: " + product_to_verify_name[i]
										                                            + " and Quantity: "
										                                            + product_to_verify_qty[i]);
							} else {
								RESULT.failed("Place Order", "Expected product: "
										+ verify_products[i] + " and Quantity: "
										+ verify_products_quantity[i],
										"Actual product: " + product_to_verify_name[i]
										                                            + " and Quantity: "
										                                            + product_to_verify_qty[i]);
							}
							//Check for Product unit which is mentioned below product name on Xpress checkout page
							if (verify_products_unit[i].equals(product_to_verify_unit[i]))
							{
								RESULT.passed("Place Order", "Expected product unit: "
										+ verify_products_unit[i],
										"Actual product unit: " + product_to_verify_unit[i]);
										                                           									                                            
							} else {
								RESULT.warning("Place Order", "Expected product unit: "
										+ verify_products_unit[i],
										"Actual product unit: " + product_to_verify_unit[i]);
							}
						}
					}
					else
					{
						RESULT.failed("Product details","Product details should be available","No product details available");
					}
				}
				catch(NoSuchElementException e)
				{
					RESULT.failed("Product details on summary page", "Product details should be available on Order summary page",
					"Product details is not available on Order summary page");

				}
				catch(NullPointerException e)
				{
					RESULT.failed("Product details on summary page", "Product details should be available on Order summary page",
					"Product details refers to null on Order summary page");
				}
				if (Double.parseDouble(t_verify_subtotal.replace("$", "")) > Double.parseDouble("750")) 
				{
					RESULT.failed(
							"Order Placed",
							"Order should not be submitted as Sub-total exceeds $750",
							"Order is submitted: "
							+ webDriver.findElement(By.xpath(objMap.getLocator("strorderNumber"))).getText()
							+ " with Sub-Total: "
							+ t_verify_subtotal);
				} else if (Double.parseDouble(t_verify_subtotal.replace("$", "")) < Double.parseDouble("30")) {
					RESULT.failed(
							"Order Placed",
							"Order should not be submitted as Sub-total is not fulfilling minimum order requirement of $30",
							"Order is submitted: "
							+ webDriver.findElement(By.xpath(objMap.getLocator("strorderNumber"))).getText()
							+ " with Sub-Total: "
							+ t_verify_subtotal);
				} else if (Double.parseDouble(t_verify_subtotal.replace("$", "")) < Double.parseDouble("50")
						&& Double.parseDouble(t_verify_subtotal.replace("$", "")) > Double.parseDouble("30")) {
					List<WebElement> addLst = webDriver.findElement(By.xpath(objMap.getLocator("strofficeDelivery"))).findElements(By.tagName("option"));
					int iSize = addLst.size();
					int a = 0;
					for (int i = 0; i < iSize; i++) {
						try {
							if (addLst.get(i).getAttribute("selected").equalsIgnoreCase("true")) {
								a++;
								break;
							}
						} catch (NullPointerException e) {
							continue;
						}
					}
					if (a > 0) {
						RESULT.failed(
								"Order Placed",
								"Order should not be submitted as Sub-total is not fulfilling minimum order requirement for Corporate user of $50",
								"Order is submitted: "
								+ webDriver.findElement(By.xpath(objMap.getLocator("strorderNumber"))).getText()
								+ " with Sub-Total: "
								+ t_verify_subtotal);
					}
				}
			} 
			else 
			{
				// Verify 30$ Minimum cart requirement for Home Address
				//*********************Calculate logic to check which 30$ or 50$ check should come********************************
				if(Double.parseDouble(verify_subtotal.replace("$", "")) < Double.parseDouble("30"))
				{
					if (webDriver.findElement(By.xpath(objMap.getLocator("strverifyWarningOnPlaced"))).getText().contains("$30.00"))
					{
						RESULT.warning("Place Order",
								"Order should be placed and review page should be displayed.",
								"ATTENTION: "
								+ webDriver.findElement(By.xpath(objMap.getLocator("strverifyWarningOnPlaced"))).getText());
					}
					else
					{
						RESULT.failed(
								"Place Order",
								"Order should not be placed and Minimum Order Requirement warning should be displayed",
						"Order should not be placed and Minimum Order Requirement warning is not displayed");
					}
				}
				// Verify 50$ Minimum cart requirement for Office Address
				else if (Double.parseDouble(verify_subtotal.replace("$", "")) < Double.parseDouble("50")
						&& Double.parseDouble(verify_subtotal.replace("$", "")) > Double.parseDouble("30"))
				{
					if(webDriver.findElement(By.xpath(objMap.getLocator("strverifyWarningOnPlaced"))).getText().contains("$50.00")) 
					{
						FD_minimumCartVerification("strofficeDelivery", "50","Office");
					}
					else
					{
						RESULT.failed(
								"Place Order",
								"Order should not be placed and Minimum Order Requirement warning should be displayed",
						"Order should not be placed and Minimum Order Requirement warning is not displayed");
					}
				}				
				// Verify 99$ Minimum cart requirement for Pickup Address
				//				else if (webDriver.findElement(By.xpath(objMap.getLocator("strverifyWarningOnPlaced"))).getText().contains("$99.00")) 
				//				{
				//					FD_minimumCartVerification("strpickup", "99", "Pickup");
				//				}
				else if (webDriver.findElements(By.xpath(objMap.getLocator("strverifyWarningOnPlaced"))).size() > 0) 
				{
					RESULT.warning("Place Order",
							"Order should be placed and review page should be displayed.",
							"ATTENTION: "
							+ webDriver.findElement(By.xpath(objMap.getLocator("strverifyWarningOnPlaced"))).getText());
				} else {
					RESULT.failed(
							"Place Order",
							"Order should be placed and review page should be displayed.",
					"Order is not submitted, also warning message for order total not displayed.");
				}

			}
			// check for the order placement if the success page is displayed

		} catch (NoSuchWindowException e1) {
			RESULT.warning("DOM", "DOM should be loaded",
			"Problem in loading of DOM oe window does not exist");
			return;
		} catch (Exception e) {
			RESULT.warning("Place order", "Place order aborted",
					"Place order aborted due to " + e.getMessage()
					+ "at line number "
					+ e.getStackTrace()[0].getLineNumber());
			return;
		}
	}

	public void FD_minimumCartVerification(String locator, String minPrice,
			String addressType) {
		List<WebElement> addLst = webDriver.findElement(
				By.xpath(objMap.getLocator(locator))).findElements(
						By.tagName("option"));
		int iSize = addLst.size();
		int a = 0;
		for (int i = 0; i < iSize; i++) {
			try {
				if (addLst.get(i).getAttribute("selected").equalsIgnoreCase(
				"true")) {
					a++;
					break;
				}
			} catch (NullPointerException e) {
				continue;
			}
		}
		if (a == 0) {
			RESULT.failed("Place Order",
					"Minimum Cart requirement to place an order for "
					+ addressType + " Address should be $" + minPrice,
					"Minimum Cart requirement to place an order for "
					+ addressType + " Address is not $" + minPrice);
		} else {
			RESULT.passed("Place Order",
					"Minimum Cart requirement to place an order for "
					+ addressType + " Address should be $" + minPrice,
					"Minimum Cart requirement to place an order for "
					+ addressType + " Address is $" + minPrice);
		}
	}

	/**
	 *Function Name: FD_reviewOrder Purpose: Function to store the products and
	 * their respective quantity to be verified in FD_submitOrder Created By:
	 * Tejas Patel Created Date: Modified By: Modified Date:
	 * 
	 * return void
	 * 
	 **/
	public void FD_reviewOrder() {
		try {
			// store the subtotal in global variable
			String temp_subtotal = uiDriver.getwebDriverLocator(
					objMap.getLocator("strverifySubTotalOld")).getText();
			verify_subtotal = temp_subtotal.replace("$", "");
			// take the list of items unavailable if warning is diaplayed

			// double temp = Double.parseDouble(verify_subtotal);
			// verify_subtotal = String.valueOf((double) Math.round(temp * 100)
			// / 100);
			System.out.println(temp_subtotal);
			// take the list of items available in cart
			List<WebElement> availableProducts = webDriver.findElements(By
					.xpath(objMap.getLocator("lstverifyProductsOld")));
			// intitialize the global arrays
			System.out.println(verify_subtotal + availableProducts.size());
			verify_products = new String[availableProducts.size()];
			verify_products_quantity = new String[availableProducts.size()];

			// go through the list to get product name its respective quantity
			int count = 0;
			for (int i = 0; i < availableProducts.size(); i++) {
				String product_text = availableProducts.get(i).findElement(
						By.xpath(objMap.getLocator("strverifyProductNameOld")))
						.getText();
				String product_quantity_text = availableProducts.get(i)
				.findElement(
						By.xpath(objMap
								.getLocator("strverifyProductQtyOld")))
								.getText();
				verify_products[i] = product_text;
				verify_products_quantity[i] = product_quantity_text;
				System.out.println(product_text + product_quantity_text);
			}

		} catch (Exception e) {
			RESULT.failed("Review Order","Order review should be successful",
					"Order review is not successful");
		}
	}

	/**
	 *Function Name: FD_submitOrder Purpose: Function to review the details on
	 * the success page of order submission Created By: Tejas Patel Created
	 * Date: Modified By: Modified Date:
	 * 
	 * @param day
	 *            : Day selected for the order delivery
	 *@param time
	 *            : Time selected for the order delivery return void
	 * 
	 **/
	public void FD_submitOrder(String day, String time) {
		try {
			if (webDriver.findElements(
					By.xpath(objMap.getLocator("strorderNumberOld"))).size() > 0) {
				// verify the time and day
				// get the time text to be verified
				String time_text = uiDriver.getwebDriverLocator(
						objMap.getLocator("strverifyTimeOld")).getText();
				// covert it into the specified format
				time_text = time_text.toUpperCase();
				System.out.println(time_text);

				if (time_text.contains(day.toUpperCase())
						&& time_text.contains(time.toUpperCase())) {
					RESULT.passed("Submit Order", "Time Slot should be: " + day
							+ " " + time, "Time Slot is: " + time_text);
				} else {
					RESULT.failed("Submit Order", "Time Slot should be: " + day
							+ " " + time, "Time Slot is: " + time_text);
				}

				// verify subtotal
				String t_verify_subtotal = uiDriver.getwebDriverLocator(
						objMap.getLocator("strverifySubTotalSubmitOld"))
						.getText().replace("$", "");
				System.out.println(t_verify_subtotal);
				if (t_verify_subtotal.contains(verify_subtotal)) {
					RESULT.passed("Submit Order", "Subtotal should be: "
							+ verify_subtotal, "Subtotal is: "
							+ t_verify_subtotal);
				} else {
					RESULT.failed("Submit Order", "Subtotal should be: "
							+ verify_subtotal, "Subtotal is: "
							+ t_verify_subtotal);
				}

				// verify the products and quantity
				wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By
						.xpath(objMap.getLocator("lstsubmitedProduct"))));
				List<WebElement> products_to_verify = webDriver.findElements(By
						.xpath(objMap.getLocator("lstsubmitedProduct")));

				String product_to_verify_name[] = new String[products_to_verify
				                                             .size()];
				String product_to_verify_qty[] = new String[products_to_verify
				                                            .size()];

				// go through the list to get product name its respective
				// quantity
				for (int i = 0; i < products_to_verify.size(); i++) {
					String product_text = products_to_verify
					.get(i).findElement(
							By.xpath(objMap.getLocator("strverifyProductNameOld")))
							.getText();
					String product_quantity_text = products_to_verify
					.get(i)
					.findElement(
							By.xpath(objMap.getLocator("strverifyProductQtyOld")))
							.getText();
					product_to_verify_name[i] = product_text;
					product_to_verify_qty[i] = product_quantity_text;
					System.out.println(product_text + product_quantity_text);
				}
				

				
				for (int i = 0; i < products_to_verify.size(); i++) {
					if ((verify_products[i].contains(product_to_verify_name[i]) || product_to_verify_name[i].contains(verify_products[i]))
							&& verify_products_quantity[i].equals(product_to_verify_qty[i])) 
					{
						RESULT.passed("Place Order", "Expected product: "
								+ verify_products[i] + " and Quantity: "
								+ verify_products_quantity[i],
								"Actual product: " + product_to_verify_name[i]
								                                            + " and Quantity: "
								                                            + product_to_verify_qty[i]);
					} else {
						RESULT.failed("Place Order", "Expected product: "
								+ verify_products[i] + " and Quantity: "
								+ verify_products_quantity[i],
								"Actual product: " + product_to_verify_name[i]
								                                            + " and Quantity: "
								                                            + product_to_verify_qty[i]);
					}
				}	
				
//				for (int i = 0; i < products_to_verify.size(); i++) {
//					for (int j = 0; j < verify_products.length; j++) {
//						if (product_to_verify_name[i].contains(verify_products[j])){
//							if(verify_products_quantity[j].equals(product_to_verify_qty[i])) {
//								RESULT.passed("Submit Order", "Expected product: "
//										+ verify_products[j] + " and Quantity: "
//										+ verify_products_quantity[j],
//										"Actual product: " + product_to_verify_name[i]
//										                                            + " and Quantity: "
//										                                            + product_to_verify_qty[i]);
//								break;
//							}else{
//								RESULT.failed("Submit Order", "Expected product: "
//										+ verify_products[j] + " and Quantity: "
//										+ verify_products_quantity[j],
//										"Actual product: " + product_to_verify_name[i]
//										                                            + " and Quantity: "
//										                                            + product_to_verify_qty[i]);
//							}	
//						}
//						if(!Arrays.asList(product_to_verify_name).contains(verify_products[j])){
//							RESULT.failed("Submit Order", "Expected product: "
//									+ verify_products[j] + " and Quantity: "
//									+ verify_products_quantity[j],
//									"Actual product: " + product_to_verify_name[i]
//									                                            + " and Quantity: "
//									                                            + product_to_verify_qty[i]);
//						}
//					}
//				}

			} else {
				RESULT
				.failed(
						"Submit Order",
						"Order should be submitted and review page should be displayed",
				"Order is not submitted");
			}

		} catch (Exception e) {
			//			e.printStackTrace();
			RESULT.failed("Submit Order ","Submit order should be successful","Submit order is not successful");
			return;
		}
	}

	private enum event {
		addAddress, updateAddress, changeEmail
	};

	/*// public void FD_emailNotification(String clickevent,
	// String AddSelectSelectPickupDeleteEdit, String FirstNameTb,
	// String LastNameTb, String ServiceType, String CompName,
	// String StreetAdd, String AptName, String AddLine2, String City,
	// String State, String ZipCode, String Contact, String Ext1,
	// String AltContact, String Ext2, String SpclDelInstructions,
	// String DoormanDelivery, String NeighbourDelivery,
	// String AltFirstName, String AltLastName, String AltApt,
	// String AltPhone, String AltExtn, String None, String SelectAddress,
	// String newemail) {
	//
	// switch (event.valueOf(clickevent)) {
	// case addAddress:
	//
	// if (webDriver.getTitle().equals(
	// "FreshDirect - Checkout - Choose Delivery Address")) {
	// try {
	// uiDriver.FD_chooseDeliveryAddress(
	// AddSelectSelectPickupDeleteEdit, FirstNameTb,
	// LastNameTb, ServiceType, CompName, StreetAdd,
	// AptName, AddLine2, City, State, ZipCode, Contact,
	// Ext1, AltContact, Ext2, SpclDelInstructions,
	// DoormanDelivery, NeighbourDelivery, AltFirstName,
	// AltLastName, AltApt, AltPhone, AltExtn, None,
	// SelectAddress);
	// } catch (FindFailed e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// } catch (InterruptedException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// } else {
	// RESULT
	// .failed(
	// "DeliveryAddress",
	// "User should be able to navigate on Delivery Address page",
	// "Error occured somewhere");
	// }
	//
	// break;
	//
	// case updateAddress:
	// if (webDriver.getTitle().equals(
	// "FreshDirect - Checkout - Choose Delivery Address")) {
	// try {
	// uiDriver.FD_chooseDeliveryAddress(
	// AddSelectSelectPickupDeleteEdit, FirstNameTb,
	// LastNameTb, ServiceType, CompName, StreetAdd,
	// AptName, AddLine2, City, State, ZipCode, Contact,
	// Ext1, AltContact, Ext2, SpclDelInstructions,
	// DoormanDelivery, NeighbourDelivery, AltFirstName,
	// AltLastName, AltApt, AltPhone, AltExtn, None,
	// SelectAddress);
	// } catch (FindFailed e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// } catch (InterruptedException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// } else {
	// RESULT
	// .failed(
	// "DeliveryAddress",
	// "User should be able to navigate on Delivery Address page",
	// "Error occured somewhere");
	// }
	//
	// break;
	//
	// case changeEmail:
	// uiDriver.setValue("txtrepeatemail", newemail);
	// // uiDriver.FD_emailValidation;
	// uiDriver.getwebDriverLocator(objMap.getLocator("btnsavechanges"))
	// .click();
	//
	// // Get the required wait for the email url to open or message to
	// // arrive in mailbox
	//
	// break;
	//
	// }
	// }
	 */
	// enum method for required for switch case to select one of the option for
	// payment
	public void FD_chooseReserveTimeSlot(String Address, String ReserveType,
			String Day, String Time, String FlexibilityFlag) {
		try{
			
			try{
			wait.until(ExpectedConditions.presenceOfElementLocated(By.name(objMap
					.getLocator("drpreserveAddress"))));
			}catch(Exception e){
				RESULT.failed("Choose reserve delivery time", "Reserve delivery addresses dropdown should be available"
						, "Reserve delivery addresses dropdown is not available");
			}
			
			Select add_ddl = new Select(uiDriver.getwebDriverLocator(objMap
					.getLocator("drpreserveAddress")));
			
			webDriver.findElement(By.name(objMap.getLocator("drpreserveAddress")))
			.click();
			add_ddl.selectByVisibleText(Address);
			waitForPageLoad();
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.name(objMap
					.getLocator("drpreserveAddress"))));

			//page refresh to handle stale element reference error
			if (CompositeAppDriver.startUp.equalsIgnoreCase("IE")) {
				webDriver.navigate().refresh();
				if(uiDriver.popup_isAlertPresent(10))
					uiDriver.popup_ClickOkOnAlert();
			}
			uiDriver.FD_chooseTimeslot(Day, Time, FlexibilityFlag);


			if (ReserveType.equalsIgnoreCase("This week")) {				
				uiDriver.click("radreserveThisWeek");
			} else if (ReserveType.equalsIgnoreCase("Every week")) {				
				uiDriver.click("radreserveEveryWeek");
			}else{
				RESULT.warning("Choose Reserved delivery time Reserve Type", "Reserve type should have value from allowed values ['This week','Every week']"
						, "Reserve type has value  "+ ReserveType);
			}
			
			SleepUtils.getInstance().sleep(TimeSlab.YIELD);
			uiDriver.click("btnreserveTime");
			waitForPageLoad();
			wait.until(ExpectedConditions.visibilityOfElementLocated(By
					.xpath(objMap.getLocator("btnreserveCancel"))));

			if (uiDriver.getwebDriverLocator(
					objMap.getLocator("strreserveSuccessMsg")).getText().contains(
					"has been reserved")) {
				RESULT.passed("Reservation of Time slot",
						"Time slot reservation should be successful",
						"Time slot reserved at " + uiDriver.verify_day + " "
						+ uiDriver.verify_time);
				reserve_flag = true;
			} else if(webDriver.findElements(By.xpath(objMap.getLocator("strerrorReservation"))).size()>0)
			{
				RESULT.failed("Reservation Time slot updation unsuccessful",
						"Reservation time slot should be modified",
						webDriver.findElement(By.xpath(objMap.getLocator("strerrorReservation"))).getText());
			}
			else {
				RESULT.failed("Reservation of Time slot",
						"Time slot reservation should be successful",
						"Reservation time slot unsuccessful at " + Day
						+ " " + Time);
				return;
			}

			wait.until(ExpectedConditions.visibilityOfElementLocated(By.name(objMap
					.getLocator("drpreserveAddress"))));
			
		}catch(Exception e){
			RESULT.failed("Choose reserve delivery time function", "Reserve delivery time should be selected successful"
					, "Reserve delivery is not selected due to "+e.getMessage());
		}
	}

	/**
	 * Function Name: FD_emailValidation Purpose: To validate Entered email
	 * Created By: Avani Thakkar Created Date: 1st July,2015 Modified By:
	 * Modified Date:
	 * 
	 * @param email
	 *            email id to be validated
	 * @param PASS
	 *            password for particular user
	 * @return void
	 */
	public void FD_emailValidation(String email) {
		// String email="test@test.com";
		String text = "^[a-zA-Z0-9_]+([\\.]?[a-zA-Z0-9_]+)*@[a-zA-Z0-9_]+(\\.[a-zA-Z0-9_]{2,3}){1,2}$";
		Pattern p = Pattern.compile(text);
		Matcher m = p.matcher(email);
		if (m.find()) {
			RESULT.passed("Email id validation",
					"Email id should match to the pattern",
			"Email id matched according to pattern");
			System.out.println("Passed");
		} else {
			String spltemail[] = email.split("@");
			if (email.contains("`") || email.contains("~")
					|| email.contains("!") || email.contains("#")
					|| email.contains("$") || email.contains("%")
					|| email.contains("^") || email.contains("&")
					|| email.contains("*") || email.contains("(")
					|| email.contains(")") || email.contains("-")
					|| email.contains("+") || email.contains("=")
					|| email.contains("{") || email.contains("[")
					|| email.contains("}") || email.contains("]")
					|| email.contains(":") || email.contains(";")
					|| email.contains("'") || email.contains("|")
					|| email.contains("1") || email.contains(",")
					|| email.contains(">") || email.contains("?")
					|| email.contains("/")) {
				RESULT.failed("Email id validation",
						"Email id should match to the pattern",
				"Email id contain special characters");
			} else if (email.split(" ").length > 1) {
				RESULT.failed("Email id validation",
						"Email id should match to the pattern",
				"Email id contain blank or white space");
			} else if (spltemail.length > 2) {
				RESULT.failed("Email id validation",
						"Email id should match to the pattern",
				"Email id contain more than one '@' character");
			} else if (spltemail.length < 2) {
				RESULT.failed("Email id validation",
						"Email id should match to the pattern",
				"Email id contains less than one '@' symbol");
			} else {
				Matcher match1 = Pattern.compile(
				"^[a-zA-Z0-9_]+([\\.]?[a-zA-Z0-9_]+)$").matcher(
						spltemail[0]);
				Matcher match2 = Pattern.compile(
				"^[a-zA-Z0-9_]+(\\.[a-zA-Z0-9_]{2,3}){1,2}$").matcher(
						spltemail[1]);
				if (!(match1.find())) {
					RESULT
					.failed(
							"Email id validation",
							"Email id should match to the pattern",
							spltemail[0]
							          + ":part of Email id is incorerct. it can contain only alphanumeric characters and '.' and '_' only");
				} else if (!(match2.find())) {
					RESULT
					.failed(
							"Email id validation",
							"Email id should match to the pattern",
							spltemail[1]
							          + ":part of Email id is incorerct. "
							          + "It can contain only alphanumeric characters and maximum 2 '.' characters followed by 2 to 3 alphabetic characters");
				}
			}
		}
	}

	// Hash map of Hash map method for FD_checkcart function
	public HashMap<String, HashMap<String, String>> NewHash(String qnt,
			String nm, String prc, int itm) {
		HashMap<String, String> innerMap;
		HashMap<String, HashMap<String, String>> outerMap;
		innerMap = new HashMap<String, String>();
		outerMap = new HashMap<String, HashMap<String, String>>();

		innerMap.put("Quantity", qnt);
		innerMap.put("Name", nm);
		innerMap.put("Price", prc);

		outerMap.put("prod" + itm, innerMap);

		return outerMap;

	}

	/**
	 * Function Name: FD_viewCart() Purpose: For verifying added products are
	 * same in your cart page and "your cart" button mouse hover Created By:
	 * Avani Thakkar Created Date: 15th May,2015 Modified By: Avani Thakkar
	 * Modified Date: 3rd September,2015
	 * 
	 * @return void
	 */

	public void FD_viewCart() {
		String quantity;
		String name;
		String price;
		String subtotal = null;
		int emptyCart = 0;
		HashMap<String, HashMap<String, String>> outerMap = new HashMap<String, HashMap<String, String>>();
		List<HashMap<String, HashMap<String, String>>> lstouterMap = new ArrayList<HashMap<String, HashMap<String, String>>>();
		try {
			// Hover the mouse on "your cart" button to verify item detail
			WebElement btnYourcart = uiDriver.getwebDriverLocator(objMap
					.getLocator("btnyourCart"));
			robot.moveToElement(btnYourcart);
			try
			{
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(objMap.getLocator("popyourCart"))));
			}
			catch(Exception e)
			{
				RESULT.failed("View Cart ", "Your cart frame should be available", "Your cart frame is not available");
				return;
			}

			if (webDriver.findElement(By.id(objMap.getLocator("popyourCart"))).isDisplayed()) {
				RESULT.passed("YourCart Flyout",
						"YourCart Flyout should be displayed",
				"YourCart Flyout is displayed successfully");
			} else {
				RESULT.failed("YourCart Flyout",
						"YourCart Flyout should be displayed",
				"YourCart Flyout is not displayed");
				return;
			}

			// verify "checkout" and "view cart" button is present or not
			try
			{
				WebElement btnview = uiDriver.getwebDriverLocator(objMap.getLocator("btnviewCart"));
				WebElement btnchkout = uiDriver.getwebDriverLocator(objMap.getLocator("btncheckOut1"));

				robot.moveToElement(btnYourcart);
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(objMap.getLocator("popyourCart"))));
				if (btnview.isEnabled() && btnchkout.isEnabled()) {
					RESULT.passed(
							"View cart and checkout button",
							"View cart and checkout button should exist and should be enabled",
					"View cart and checkout button exists and enabled");
				} else
					RESULT.failed(
							"View cart and checkout button",
							"View cart and checkout button should exist and should be enabled",
					"View cart and checkout button does not exist or not enabled");
			}
			catch(Exception e)
			{
				RESULT.failed("View Cart ", "View cart and checkout button should be available", "View cart and checkout button is not available");
			}

			// retrieve all items details like "Quantity","Name" and "Price"
			List<WebElement> Lstitems_row;
			try
			{
				Lstitems_row = uiDriver.getwebDriverLocator(objMap.getLocator("lstpopupCart")).findElements(
						By.className(objMap.getLocator("lstcartLine")));
			}
			catch(Exception e)
			{
				RESULT.failed("View cart ", "Your cart frame should be available", "Your cart frame is not available");
				return;
			}


			int index_tbquant = 0;
			if (Lstitems_row.size() == 0) {
				RESULT.passed("view cart",
						"No items should be available in cart",
				"No items available in your cart");
				emptyCart = 1;
			}
			else
			{
				robot.moveToElement(btnYourcart);
				int j = 0,count;
				for (WebElement item : Lstitems_row) {
					// quantity of product
					//robot.moveToElement(btnYourcart);
					wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(objMap.getLocator("popyourCart"))));
					List<WebElement> qaunt = item.findElements(By.xpath(objMap.getLocator("lstqnty")));
					List<WebElement> quant_drp = item.findElements(By.xpath(objMap.getLocator("lstqntyDropdown")));
					if (quant_drp.size() > 0) {
						quantity = "1";
					} else {
						quantity = qaunt.get(index_tbquant).getAttribute(
						"value");
						index_tbquant++;
					}

					// name of product
					count=0;
					do {
						// actions.moveToElement(btnYourcart).build().perform();
						//robot.moveToElement(btnYourcart);
						wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(objMap.getLocator("popyourCart"))));
						count++;						
						List<WebElement> nam = item.findElements(By.xpath(objMap.getLocator("lstname")));
						name = nam.get(j).getText();
					} while (name == null || name.equals("") && count<5);

					// price of product
					count=0;
					do {
						// actions.moveToElement(btnYourcart).build().perform();
						//robot.moveToElement(btnYourcart);
						wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(objMap.getLocator("popyourCart"))));
						count++;
						List<WebElement> pric = item.findElements(By
								.xpath(objMap.getLocator("lstprice")));
						price = pric.get(j).getText();
					} while (price == null || price.equals("") && count<5);

					// add all details in list of hash map of hash map
					outerMap = this.NewHash(quantity, name, price, j);
					lstouterMap.add(j, outerMap);
					j++;
				}
			}

			// verify cart by clicking on your cart button
			btnYourcart.click();
			waitForPageLoad();
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(objMap.getLocator("txtSubTotal"))));
//			SleepUtils.getInstance().sleep(TimeSlab.YIELD);
			if (!(webDriver.findElements(By.xpath(objMap.getLocator("lstverifyProducts"))).size() > 0)) {
				if (emptyCart == 1) {
					RESULT.passed(
							"View Cart",
							"Cart page as well as cart popup should contain same products",
					"Cart popup as well as cart page is empty");
				} else {
					RESULT.failed(
							"View Cart",
							"Cart page as well as cart popup should contain same products",
					"Cart popup is not empty but cart page is empty");
				}
			} else {
				try
				{
					List<WebElement> LstCartClick = webDriver.findElements(By
							.xpath(objMap.getLocator("lstverifyProducts")));
					int i = 0;
					int k = 0;
					for (WebElement prod : LstCartClick) {
						if (LstCartClick.size() == k)
							break;

						// Quantity of product
						if (prod.findElements(
								By.className(objMap.getLocator("lstqntyNew")))
								.size() > 0) {
							quantity = prod.findElement(
									By.className(objMap.getLocator("lstqntyNew")))
									.getAttribute("value");
						} else if (prod.findElements(
								By.xpath(objMap.getLocator("lstqntyDropdownPage")))
								.size() > 0) {
							quantity = "1";
						} else {
							quantity = prod.findElement(
									By.className(objMap.getLocator("lstqntyNew")))
									.getAttribute("value");
							RESULT
							.warning(
									"Quantity type mismatch",
									"Quantity type should be either text box or drop down of sales unit",
							"Quantity type is other than text box and dropdown of sales unit");
						}

						// Name of product
						List<WebElement> nam = webDriver.findElements(By
								.xpath(objMap.getLocator("lnkitemName1")));
						name = nam.get(k).getText();

						// price of product
						price = prod.findElement(
								By.className(objMap.getLocator("lstpriceNew")))
								.getText();

						// match details of each product
						try
						{
							if ((lstouterMap.get(k).get("prod" + k).get("Quantity")
									.toString().equalsIgnoreCase(quantity))
									&& (lstouterMap.get(k).get("prod" + k).get("Name")
											.toString().startsWith(name))
											&& (lstouterMap.get(k).get("prod" + k).get("Price")
													.toString().equalsIgnoreCase(price))) {
								RESULT.passed("Product details matched",
										"Cart page should contain details of item:"
										+ lstouterMap.get(k).get("prod" + k)
										.get("Name")
										+ " Quantity:"
										+ lstouterMap.get(k).get("prod" + k)
										.get("Quantity")
										+ "Price:"
										+ lstouterMap.get(k).get("prod" + k)
										.get("Price"),
										"All details of a Product:" + name
										+ " Quantity:" + quantity + "Price:"
										+ price + "matched");
							} else {
								RESULT.failed("Product details matched",
										"Cart page should contain details of item:"
										+ lstouterMap.get(k).get("prod" + k)
										.get("Name")
										+ " Quantity:"
										+ lstouterMap.get(k).get("prod" + k)
										.get("Quantity")
										+ "Price:"
										+ lstouterMap.get(k).get("prod" + k)
										.get("Price"),
										"All details of a Product:" + name
										+ " Quantity:" + quantity + "Price:"
										+ price + " did not matched");
								i++;
								return;
							}
							k++;
						}
						catch(Exception e)
						{
							RESULT.failed("View cart : Comparision", "Product comparision: Quantity, Name, Price  on Your cart and View cart page should be successful",
							"Product comparision : Quantity, Name, Price on Your cart and View cart page is not successful ");
						}

					}				
				}
				catch(Exception e)
				{
					RESULT.failed("View cart page products details ", "Product details on view cart page should be available",
					"Product details on view cart page migth not be available");
				}

			}
		} catch (Exception e) {
			RESULT.failed("View cart Exception ", "Product comparision on view cart and your cart should be successful",
			"Product comparisionon on view cart and your cart is not successful");		
		}

	}

	private enum choosedOpt {
		Select, SelectPickup, Add, AddCheckingAcc, Delete, Edit
	};

	/**
	 * Function Name: FD_paymentOption() Purpose: Select/ add / delete / edit
	 * credit card, Bank Account details and EBT card details Created By: Avani
	 * Thakkar Created Date: 18th May,2015 Modified By: Avani Thakkar Modified
	 * Date: 7th September,2015
	 * 
	 * @param Chooseoptn
	 *            Choose action you want to perform: Select,Edit,Delete,Add
	 *@param cardDetail
	 *            Provide exact card details to select,edit,delete
	 *@param NameOnCard
	 *            Name displayed on the credit/EBT card
	 *@param Cardtype
	 *            Type of card:Visa,MasterCard,Amex,Discover
	 *@param type
	 *            type of bank account for adding checking
	 *            account:checking,savings
	 *@param routing
	 *            routing number of bank account for adding checking account
	 *@param bank
	 *            Name of Bank for checking account
	 *@param CardNum
	 *            Number of your credit/EBT card
	 *@param ExpiryMnth
	 *            Expire month of your card
	 *@param ExpiryYr
	 *            Expire year of your card
	 *@param CardStreetAdd
	 *            Your Address:street name
	 *@param CardAppNum
	 *            Your Address:Appartment number
	 *@param CardAddLine2
	 *            Your Address:bench mark point or address line 2
	 *@param CardCountry
	 *            Your country of provided address
	 *@param CardCity
	 *            City of of provided address
	 *@param CardState
	 *            State of provided address
	 *@param CardZip
	 *            Zip code of provided address
	 * 
	 *@Return void
	 */

	public void FD_paymentOption(String AddEditDeleteSelect, String cardDetail,
			String NameOnCard, String routing, String Bank,
			String CardOrAcctype, String CardNum, String ExpiryMnth,
			String ExpiryYr, String CardStreetAdd, String CardAppNum,
			String CardAddLine2, String CardCountry, String CardCity,
			String CardState, String CardZip, String PhoneNum,
			String FlexibilityFlg) {
		try {
			String cards = uiDriver.getwebDriverLocator(
					objMap.getLocator("lstpayment")).getAttribute(
					"data-item-count");
			// String invalArg = null;
			String mycard = cardDetail.replaceAll("\\n", "");
			/*String spltSel[] = mycard1.split("\\n");
			String mycard = "";
			for (int i = 0; i < spltSel.length; i++) {
				mycard = mycard + spltSel[i] + "\n";
			}*/
			
			int a = 0;
			// replace two new line character with single to match in the
			// applecation
//			mycard = mycard.replaceAll("\n\n", "\n");

			// choose option for operation on details of card/account
			String choosedOpt = AddEditDeleteSelect;
			switch (ChooseOpt.valueOf(choosedOpt)) {
			// select card having details same as given in excel
			case Select:
				
				if (cards.equals("0")) {
					RESULT.failed(
							"Selection of Credit/EBT card/checking account ",
							mycard+ ":Credit/EBT card/checking account should get selected",
					"No credit/EBT card/checking account available in list to select");
					return;
				} else {
					try{
						
						List<WebElement> cardList = uiDriver.getwebDriverLocator(objMap.getLocator("lstpayment")).findElements(
								By.className(objMap.getLocator("lstcards1")));
						for (WebElement card : cardList) {
							// Retrieve one by one all card details to compare with
							// given card detail
							String details = card.getText().replaceAll("\n", "");
							if (details.contains(mycard)) {
								a++;
								card.findElement(By.tagName(objMap.getLocator("radcard"))).click();
//								SleepUtils.getInstance().sleep(TimeSlab.MEDIUM);
								/*RESULT.passed(
										"Selection of Credit/EBT card/checking account",
										mycard+ ":Credit/EBT card/checking account should get selected",
										"Selected card/account is:"+ details);*/
								break;
							}
						}
						if (a == 0 && (FlexibilityFlg.equalsIgnoreCase("Yes")||
								!FlexibilityFlg.equalsIgnoreCase("No"))) {
							
							RESULT.warning(
									"Selection of Credit/EBT card/checking account",
									mycard+ ":Credit/EBT card/checking account should get selected",
									"No credit/EBT card/checking account matched with given details:"
									+ mycard);
							cardList.get(0).findElement(By.tagName(objMap.getLocator("radcard")))
							.click();
//							SleepUtils.getInstance().sleep(TimeSlab.MEDIUM);
							// String spltdetails[] =
							// cardList.get(0).getText().split("\n");
							/*RESULT.passed(
									"Selection of Credit/EBT card/checking account",
									mycard+ ":Credit/EBT card/checking account should get selected",
									mycard+ ":card is not available in system so first available and selected card/account is:"
									+ cardList.get(0).getText());*/
							match= cardList.get(0).getText().split("Billing address")[0].replaceAll("\n", "");

						} else if (a == 0 && FlexibilityFlg.equalsIgnoreCase("No")) {
							RESULT.failed(
									"Selection of Credit/EBT card/checking account",
									mycard+ ":Credit/EBT card/checking account should get selected",
									"No credit/EBT card/checking account matched with given details:"+ mycard);
							return;
						}
					}
//					}
					catch(Exception e)
					{
						RESULT.failed("Payment Options : Card Select","Card should be selected successfully","Card is not selected and exception is "+e.getMessage());
					}
				}
				break;

			case Delete:
				if (cards.equals("0")) {
					RESULT.failed(
							"Deletion of Credit/EBT card/checking account ",
							mycard+ ":Credit/EBT card/checking account should get deleted",
					"No credit/EBT card/checking account available in list to Delete");
					return;
				} else 
				{
					// delete particular card from list
					try
					{
						List<WebElement> cardListdel = uiDriver.getwebDriverLocator(objMap.getLocator("lstpayment")).
						findElements(By.className(objMap.getLocator("lstcards1")));
						System.out.println("cards:" + cardListdel.size());
						for (WebElement card : cardListdel) {
							// Retrieve one by one all card details to compare with
							// given card detail
							String details = card.getText();
							System.out.println("\n retrieved card details:"
									+ details);
							if (details.startsWith(mycard)) {
								WebElement delete = card.findElement(By.xpath(objMap.getLocator("btndelCard")));
								if (delete.isEnabled()) {
									delete.click();
									a++;
									SleepUtils.getInstance().sleep(TimeSlab.YIELD);
									RESULT.done(
											"Delete Payment option",
											"Delete OR Cancel popup should be displayed",
									"Delete OR Cancel popup is displayed");
									uiDriver.click("btndelConfm");
									String cardsUpdate = uiDriver
									.getwebDriverLocator(
											objMap.getLocator("lstpayment"))
											.getAttribute("data-item-count");
									System.out.println("before:" + cards + "after:"
											+ cardsUpdate);
									//Total number of cards will be reduced by 1
									if (Integer.parseInt(cards) == Integer
											.parseInt(cardsUpdate) - 1) {
										// String spltdetails[] =
										// details.split("\n");
										RESULT.passed(
												"Deletion of Credit/EBT card/checking account",
												mycard
												+ ":Credit/EBT card/checking account should get deleted",
												"Deleted card/account is:"
												+ details);
									} else {
										RESULT
										.failed(
												"Deletion of Credit/EBT card/Checking account",
												mycard
												+ ":Credit/EBT card/Checking account should get deleted",
										"Credit/EBT card/Checking account could not get deleted and count could not get update");
										return;
									}
								} else if ((!delete.isEnabled())
										&& cards.equals("1")) {
									RESULT
									.failed(
											"Deletion of Credit/EBT card/Checking account",
											mycard
											+ ":Credit/EBT card/Checking account should get deleted",
									"Delete button not available as there is only one card/account available in the system.");
									return;
								}
								break;
							}
						}
						if (a == 0) {
							RESULT
							.failed(
									"Deletion of Credit/EBT card/Checking account",
									mycard
									+ ":Credit/EBT card/Checking account should get deleted",
									"Credit/EBT card/Checking account with given details:"
									+ mycard + " not found");
							return;
						}
					}
					catch(Exception e)
					{
						RESULT.failed("Payment Options : Card Delete", "Card should be successfully deleted", "Card is not deleted and the exception caught is "+e.getMessage());
						return;
					}
				}
				break;

			case Edit:
				if (cards.equals("0")) {
					RESULT.failed(
							"Editing of Credit/EBT card/checking account ",
							mycard+ ":Credit/EBT card/checking account should get Edited",
					"No credit/EBT card/checking account available in list to edit");
					return;
				} else {
					// edit particular card with list to edit some fields
					try{

						List<WebElement> cardListedt = uiDriver.getwebDriverLocator(objMap.getLocator("lstpayment")).findElements(By
								.className(objMap.getLocator("lstcards1")));
						System.out.println("cards:" + cardListedt.size());
						for (WebElement card : cardListedt) {
							// Retrieve one by one all card details to compare with
							// given card detail
							String details = card.getText();
							if (details.startsWith(mycard)) {
								a++;
								WebElement edit = card.findElement(By.xpath(objMap.getLocator("btneditCard")));
								wait.until(ExpectedConditions.elementToBeClickable(edit));
								edit.click();
								// check if it is credit card
								if (uiDriver.getwebDriverLocator(objMap.getLocator("btnsaveCreditCard")).isDisplayed()) {
									// provide name and address details
									uiDriver.setValue("txtnameOnCard", NameOnCard);
									uiDriver.setValue("txtcardStreetAdd",
											CardStreetAdd);
									uiDriver.setValue("txtcardAppNum", CardAppNum);
									uiDriver.setValue("txtcardAddLine2",
											CardAddLine2);
									uiDriver.setValue("drpcardCountry", CardCountry);
									uiDriver.setValue("txtcardCity", CardCity);
									uiDriver.setValue("drpcardState", CardState);
									uiDriver.setValue("txtcardZip", CardZip);
									uiDriver.getwebDriverLocator(objMap.getLocator("lstcreditCard")).findElement(By
											.name(objMap.getLocator("txtphoneNum")))
											.sendKeys(PhoneNum);
									// provide credit card details like card type
									// and expiration month and year of card
									uiDriver.setValue("drpcardtype", CardOrAcctype);
									uiDriver.setValue("drpexpiryMnth", ExpiryMnth);
									uiDriver.setValue("drpexpiryYr", ExpiryYr);
									RESULT.done(
											"Payment option fields",
											"All the given data shoud be entered in relevant Payment option fields",
									"All the data entered in relevant Payment option fields");
									uiDriver.click("btnsaveCreditCard");
									wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(objMap
											.getLocator("btndonePaymnt"))));
								}
								// check if it is bank account
								else if (uiDriver.getwebDriverLocator(
										objMap.getLocator("btnsaveBank"))
										.isDisplayed()) {
									// select type of account
									if (CardOrAcctype.equalsIgnoreCase("Checking"))
										uiDriver.click("radchecking");
									else if (CardOrAcctype.equalsIgnoreCase("savings"))
										uiDriver.click("radsavings");
									else {
										RESULT.failed(
												"Editing of checking account",
												mycard+ ":Bank account should get edited with given details",
												"You have entered invalid account type other than checking and savings:"+ CardOrAcctype);
										return;
									}
									// provide name and address details
									// provide account holder name and other bank
									// details
									uiDriver.getwebDriverLocator(objMap.getLocator("lstbank")).findElement(By
											.name(objMap.getLocator("txtnameOnCard"))).clear();
									uiDriver.getwebDriverLocator(objMap.getLocator("lstbank")).findElement(
											By.name(objMap.getLocator("txtnameOnCard")))
											.sendKeys(NameOnCard);
									uiDriver.setValue("txtroutenum", routing);// provide
									// routing
									// #
									uiDriver.setValue("txtbank", Bank);// provide
									// bank name
									// provide address details
									uiDriver.getwebDriverLocator(objMap.getLocator("lstbank")).findElement(By
											.name(objMap.getLocator("drpcardCountry")))
											.sendKeys(CardCountry);
									uiDriver.getwebDriverLocator(objMap.getLocator("lstbank"))
									.findElement(By.name(objMap.getLocator("txtcardStreetAdd")))
									.sendKeys(CardStreetAdd);
									uiDriver.getwebDriverLocator(objMap.getLocator("lstbank"))
									.findElement(By.name(objMap.getLocator("txtcardAppNum")))
									.sendKeys(CardAppNum);
									uiDriver.getwebDriverLocator(
											objMap.getLocator("lstbank")).findElement(
													By.name(objMap.getLocator("txtcardAddLine2")))
													.sendKeys(CardAddLine2);
									uiDriver.getwebDriverLocator(
											objMap.getLocator("lstbank")).findElement(
													By.name(objMap.getLocator("txtcardCity")))
													.sendKeys(CardCity);
									uiDriver.getwebDriverLocator(
											objMap.getLocator("lstbank")).findElement(
													By.name(objMap.getLocator("drpcardState")))
													.sendKeys(CardState);
									uiDriver.getwebDriverLocator(
											objMap.getLocator("lstbank"))
											.findElement(By.name(objMap.getLocator("txtcardZip")))
											.sendKeys(CardZip);
									// provide Best# for billing inquiry
									uiDriver.getwebDriverLocator(
											objMap.getLocator("lstbank"))
											.findElement(By.name(objMap.getLocator("txtphoneNum")))
											.sendKeys(PhoneNum);
									// accept the terms and conditions of checking
									// account
									uiDriver.click("chkacceptTerms");
									RESULT.done(
											"Payment option fields",
											"All the given data shoud be entered in relevant Payment option fields",
									"All the data entered in relevant Payment option fields");
									uiDriver.click("btnsaveBank");// click on save
									wait.until(ExpectedConditions
											.presenceOfElementLocated(By.xpath(objMap.getLocator("btndonePaymnt"))));
								}
								// check if it is EBT card
								else if (uiDriver.getwebDriverLocator(
										objMap.getLocator("btnsaveEBT"))
										.isDisplayed()) {
									// provide name and address details
									uiDriver.getwebDriverLocator(
											objMap.getLocator("lstebt"))
											.findElement(By.name(objMap.getLocator("txtnameOnCard")))
											.sendKeys(NameOnCard);
									uiDriver.getwebDriverLocator(
											objMap.getLocator("lstebt"))
											.findElement(By.name(objMap.getLocator("drpcardCountry")))
											.sendKeys(CardCountry);
									uiDriver.getwebDriverLocator(
											objMap.getLocator("lstebt"))
											.findElement(By.name(objMap.getLocator("txtcardStreetAdd")))
											.sendKeys(CardStreetAdd);
									uiDriver.getwebDriverLocator(
											objMap.getLocator("lstebt"))
											.findElement(By.name(objMap.getLocator("txtcardAppNum")))
											.sendKeys(CardAppNum);
									uiDriver.getwebDriverLocator(
											objMap.getLocator("lstebt"))
											.findElement(By.name(objMap.getLocator("txtcardAddLine2")))
											.sendKeys(CardAddLine2);
									uiDriver
									.getwebDriverLocator(
											objMap.getLocator("lstebt"))
											.findElement(By.name(objMap.getLocator("txtcardCity")))
											.sendKeys(CardCity);
									uiDriver.getwebDriverLocator(
											objMap.getLocator("lstebt"))
											.findElement(By.name(objMap.getLocator("drpcardState")))
											.sendKeys(CardState);
									uiDriver.getwebDriverLocator(
											objMap.getLocator("lstebt"))
											.findElement(By.name(objMap.getLocator("txtcardZip")))
											.sendKeys(CardZip);
									// provide Best# for billing inquiry
									uiDriver.getwebDriverLocator(objMap.getLocator("lstebt"))
									.findElement(
											By.name(objMap.getLocator("txtphoneNum")))
											.sendKeys(PhoneNum);
									RESULT.done(
											"Payment option fields",
											"All the given data shoud be entered in relevant Payment option fields",
									"All the data entered in relevant Payment option fields");
									uiDriver.click("btnsaveEBT");
									wait.until(ExpectedConditions
											.presenceOfElementLocated(By
													.xpath(objMap
															.getLocator("btndonePaymnt"))));
								}
								if (uiDriver.getwebDriverLocator(
										objMap.getLocator("btnsaveEBT"))
										.isEnabled()
										|| uiDriver.getwebDriverLocator(
												objMap.getLocator("btnsaveCreditCard"))
												.isEnabled()
												|| uiDriver.getwebDriverLocator(
														objMap.getLocator("btnsaveBank"))
														.isEnabled()) {
									RESULT.failed(
											"Edit existing Credit/EBT card/Checking account",
											mycard
											+ ":Credit/EBT card/checking account should get edited with given details",
									"one or more given detail is incorrect so could not save edited details");
									return;
								} else {
									RESULT.passed(
											"Edit existing Credit/EBT card/Checking account",
											mycard
											+ ":Credit/EBT card/checking account should get edited with given details",
									"Credit/EBT card updated with given details");
								}
								break;
							}
						}
						if (a == 0) {
							RESULT.failed(
									"Edit existing Credit or EBT card",
									mycard
									+ ":Credit or EBT card should get updated with given details",
							"No credit or EBT card matched with given details to edit");
							return;
						}

					}catch(Exception e)
					{
						RESULT.failed("Payment Options : Card Edit", "Card should be edited successfully", "Card is not edited and the exception caught is "+e.getMessage());
					}

				}
				break;

			case Add:
				// click on add new card details
				try
				{
					uiDriver.getwebDriverLocator(objMap.getLocator("lstpayment"))
					.findElement(By.xpath(objMap.getLocator("btnaddCard")))
					.click();
					// check if bank account is to be added or credit card
					if (cardDetail.equalsIgnoreCase("credit card")
							&& uiDriver.isElementPresent(By.xpath(objMap
									.getLocator("radcreditAdd")))) {
						uiDriver.click("radcreditAdd");
						// provide all field values for credit card
						uiDriver.setValue("txtnameOnCard", NameOnCard);
						uiDriver.setValue("drpcardtype", CardOrAcctype);
						uiDriver.setValue("txtcardNum", CardNum);
						uiDriver.setValue("drpexpiryMnth", ExpiryMnth);
						uiDriver.setValue("drpexpiryYr", ExpiryYr);
						// add address details
						uiDriver.setValue("drpcardCountry", CardCountry);
						uiDriver.setValue("txtcardStreetAdd", CardStreetAdd);
						uiDriver.setValue("txtcardAppNum", CardAppNum);
						uiDriver.setValue("txtcardAddLine2", CardAddLine2);
						uiDriver.setValue("txtcardCity", CardCity);
						uiDriver.setValue("drpcardState", CardState);
						uiDriver.setValue("txtcardZip", CardZip);
						uiDriver.getwebDriverLocator(
								objMap.getLocator("lstcreditCard")).findElement(
										By.name(objMap.getLocator("txtphoneNum")))
										.sendKeys(PhoneNum);
						// provide
						// Best# for
						// billing
						// inquiry
						// click on save changes
						RESULT.done(
								"Payment option fields",
								"All the given data shoud be entered in relevant Payment option fields",
						"All the data entered in relevant Payment option fields");
						uiDriver.click("btnsaveCreditCard");
						wait.until(ExpectedConditions.presenceOfElementLocated(By
								.xpath(objMap.getLocator("btndonePaymnt"))));
					} else if (cardDetail.equalsIgnoreCase("Bank")
							&& uiDriver.isElementPresent(By.xpath(objMap
									.getLocator("radbankAdd")))) {
						uiDriver.click("radbankAdd");
						// select account type
						if (CardOrAcctype.equalsIgnoreCase("Checking")) {
							uiDriver.click("radchecking");
						} else if (CardOrAcctype.equalsIgnoreCase("savings")) {
							uiDriver.click("radsavings");
						} else {
							RESULT
							.failed(
									"Addition of bank account",
									"Bank account should get added with given details",
									"You have entered invalid account type other than checking and savings:"
									+ CardOrAcctype);
							return;
						}
						// provide account holder name and other bank details
						uiDriver
						.getwebDriverLocator(objMap.getLocator("lstbank"))
						.findElement(
								By.name(objMap.getLocator("txtnameOnCard")))
								.sendKeys(NameOnCard);
						uiDriver.getwebDriverLocator(objMap.getLocator("lstbank"))
						.findElement(
								By.name(objMap.getLocator("txtcardNum")))
								.sendKeys(CardNum);
						uiDriver.setValue("txtcardverif", CardNum);// verify
						// provided
						// account #
						uiDriver.setValue("txtroutenum", routing);// provide
						// routing #
						// provide address details
						uiDriver
						.getwebDriverLocator(objMap.getLocator("lstbank"))
						.findElement(By.name(objMap.getLocator("drpcardCountry")))
						.sendKeys(CardCountry);
						uiDriver.getwebDriverLocator(objMap.getLocator("lstbank"))
						.findElement(By.name(objMap.getLocator("txtcardStreetAdd")))
						.sendKeys(CardStreetAdd);
						uiDriver
						.getwebDriverLocator(objMap.getLocator("lstbank"))
						.findElement(By.name(objMap.getLocator("txtcardAppNum")))
						.sendKeys(CardAppNum);
						uiDriver.getwebDriverLocator(objMap.getLocator("lstbank"))
						.findElement(By.name(objMap.getLocator("txtcardAddLine2")))
						.sendKeys(CardAddLine2);
						uiDriver.getwebDriverLocator(objMap.getLocator("lstbank"))
						.findElement(By.name(objMap.getLocator("txtcardCity")))
						.sendKeys(CardCity);
						uiDriver.getwebDriverLocator(objMap.getLocator("lstbank"))
						.findElement(By.name(objMap.getLocator("drpcardState")))
						.sendKeys(CardState);
						uiDriver.getwebDriverLocator(objMap.getLocator("lstbank"))
						.findElement(By.name(objMap.getLocator("txtcardZip")))
						.sendKeys(CardZip);
						// provide Best# for billing inquiry
						uiDriver.getwebDriverLocator(objMap.getLocator("lstbank"))
						.findElement(By.name(objMap.getLocator("txtphoneNum")))
						.sendKeys(PhoneNum);
						// accept the terms and conditions of checking account
						try
						{
							if(getwebDriverLocator(objMap.getLocator("chkacceptTerms")).isDisplayed())
								uiDriver.click("chkacceptTerms");
						}
						catch(NoSuchElementException e)
						{
							RESULT.warning("Payment : ADD", "Accept Terms checkbox should be visible", 
							"Accept Terms checkbox is not visible");
						}

						RESULT.done(
								"Payment option fields",
								"All the given data shoud be entered in relevant Payment option fields",
						"All the data entered in relevant Payment option fields");
						uiDriver.click("btnsaveBank");// click on save
						wait.until(ExpectedConditions.presenceOfElementLocated(By
								.xpath(objMap.getLocator("btndonePaymnt"))));
					} else if (cardDetail.equalsIgnoreCase("EBT")
							&& uiDriver.isElementPresent(By.xpath(objMap
									.getLocator("radebtAdd")))) {
						uiDriver.click("radebtAdd");
						uiDriver
						.getwebDriverLocator(objMap.getLocator("lstebt"))
						.findElement(
								By.name(objMap.getLocator("txtnameOnCard")))
								.sendKeys(NameOnCard);
						uiDriver.getwebDriverLocator(objMap.getLocator("lstebt"))
						.findElement(
								By.name(objMap.getLocator("txtcardNum")))
								.sendKeys(CardNum);
						// provide address details
						uiDriver
						.getwebDriverLocator(objMap.getLocator("lstebt"))
						.findElement(By.name(objMap.getLocator("drpcardCountry")))
						.sendKeys(CardCountry);
						uiDriver.getwebDriverLocator(objMap.getLocator("lstebt"))
						.findElement(By.name(objMap.getLocator("txtcardStreetAdd")))
						.sendKeys(CardStreetAdd);
						uiDriver
						.getwebDriverLocator(objMap.getLocator("lstebt"))
						.findElement(By.name(objMap.getLocator("txtcardAppNum")))
						.sendKeys(CardAppNum);
						uiDriver.getwebDriverLocator(objMap.getLocator("lstebt"))
						.findElement(By.name(objMap
								.getLocator("txtcardAddLine2")))
								.sendKeys(CardAddLine2);
						uiDriver.getwebDriverLocator(objMap.getLocator("lstebt"))
						.findElement(By.name(objMap.getLocator("txtcardCity")))
						.sendKeys(CardCity);
						uiDriver.getwebDriverLocator(objMap.getLocator("lstebt"))
						.findElement(By.name(objMap.getLocator("drpcardState")))
						.sendKeys(CardState);
						uiDriver.getwebDriverLocator(objMap.getLocator("lstebt"))
						.findElement(By.name(objMap.getLocator("txtcardZip")))
						.sendKeys(CardZip);
						// provide Best# for billing inquiry
						uiDriver.getwebDriverLocator(objMap.getLocator("lstebt"))
						.findElement(By.name(objMap.getLocator("txtphoneNum")))
						.sendKeys(PhoneNum);
						RESULT.done(
								"Payment option fields",
								"All the given data shoud be entered in relevant Payment option fields",
						"All the data entered in relevant Payment option fields");
						uiDriver.click("btnsaveEBT");
						wait.until(ExpectedConditions.presenceOfElementLocated(By
								.xpath(objMap.getLocator("btndonePaymnt"))));
					} else {
						RESULT.failed(
								"Adition of Credit/EBT card/Bank account",
								cardDetail
								+ " :should get added with given details",
								"given argument is incorrect or radiobutton for given selection is not available:"
								+ cardDetail);
						return;
					}
					String cardsUpdate = uiDriver.getwebDriverLocator(
							objMap.getLocator("lstpayment")).getAttribute(
							"data-item-count");
					if (Integer.parseInt(cards) + 1 == Integer
							.parseInt(cardsUpdate))
						RESULT.passed("Adition of Credit/EBT card/Bank account",
								cardDetail
								+ " :should get added with given details",
								cardDetail + " : with given name:" + NameOnCard
								+ " added");
					else {
						RESULT.failed(
								"Adition of Credit/EBT card/Bank account",
								cardDetail
								+ " :should get added with given details",
								cardDetail
								+ " :could not get added as one or more details is incorrect");
						return;
					}
				}
				catch(Exception e)
				{
					RESULT.failed("Payment Oprions : Add", "Payment option should be added", "Payment option is not added and exception caught is "+e.getMessage());
				}

				break;
			}
		} catch (NoSuchElementException e) {
			RESULT.failed("Payment option Exception","Payment should be successful","Payment method caught exception "+e.getMessage());
			return;
		} catch (Exception e) {
			RESULT.failed("Payment option Exception","Payment should be successful","Payment method caught exception "+e.getMessage());
		}
	}

	/*
	 * add_ToCart function clicks on the product to be addded and clicks on Add
	 * to car tbutton. Exceptions like : clicking on add to cart a pop up
	 * whether to create a new order, modify existing order or close popup is
	 * handled. :clicking on add to cart if alcohol alert comes then it
	 * "Accepts" the T&C and moves ahead :clicking on add to cart if product is
	 * unavailable then warning is logged into report
	 */
	public double yourCart(String productName) {

		//wait for the page to be ready
		waitForPageLoad();

		double myprod_quantity = 0.0;
		robot.moveToElement(uiDriver.getwebDriverLocator(objMap
				.getLocator("btnyourCart")));
		try{
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(objMap
					.getLocator("popyourCart"))));
			if (webDriver.findElement(By.id(objMap.getLocator("popyourCart")))
					.isDisplayed()) {
				RESULT.passed("YourCart Flyout",
						"YourCart Flyout should be displayed",
				"YourCart Flyout is displayed successfully");
			} else {
				RESULT.failed("YourCart Flyout",
						"YourCart Flyout should be displayed",
				"YourCart Flyout is not displayed");
			}
			List<WebElement> Lstitems_row_1 = uiDriver.getwebDriverLocator(
					objMap.getLocator("lstpopupCart")).findElements(
							By.className(objMap.getLocator("lstcartline")));
			List<WebElement> nam;
			boolean item_check_flag = false;

			if (Lstitems_row_1.size() == 0) {				
				robot.moveToElement(uiDriver.getwebDriverLocator(objMap
						.getLocator("btnyourCart")));
				wait.until(ExpectedConditions.visibilityOfElementLocated(By
						.id(objMap.getLocator("popyourCart"))));
				if (webDriver.findElement(
						By.id(objMap.getLocator("popyourCart"))).isDisplayed()) {
					RESULT.passed("YourCart Flyout",
							"YourCart Flyout should be displayed",
					"YourCart Flyout is displayed successfully");
				} else {
					RESULT.failed("YourCart Flyout",
							"YourCart Flyout should be displayed",
					"YourCart Flyout is not displayed");
				}

				return 0;
			} else {

				int j = 0;

				for (WebElement item : Lstitems_row_1) {

					// name of product
					//do {
					//robot.moveToElement(uiDriver.getwebDriverLocator(objMap
					//		.getLocator("btnyourCart")));
					//wait.until(ExpectedConditions
					//		.visibilityOfElementLocated(By.id(objMap
					//				.getLocator("popyourCart"))));

					nam = item.findElements(By.xpath(objMap
							.getLocator("lstNAME")));
					// name = nam.get(j).getText();
					//} while (nam.get(j).getText() == null
					//		|| nam.get(j).getText().equals(""));

					// quantity of product
					//robot.moveToElement(uiDriver.getwebDriverLocator(objMap
					//		.getLocator("btnyourCart")));
					//wait.until(ExpectedConditions.visibilityOfElementLocated(By
					//		.id(objMap.getLocator("popyourCart"))));

					List<WebElement> qaunt = item.findElements(By
							.className(objMap.getLocator("txtqty")));
					List<WebElement> quant_drp = item.findElements(By
							.xpath(objMap.getLocator("lstqntyDropdown")));
					if (nam.get(j).getText().contains(productName.replaceAll("\n", " "))) {
						if (quant_drp.size() > 0) {
							myprod_quantity += 1;						
						} else {
							if (qaunt.size() > 1)
								myprod_quantity += Double.parseDouble(qaunt
										.get(j).getAttribute("value"));
							else
								myprod_quantity += Double.parseDouble(qaunt
										.get(0).getAttribute("value"));							
						}
					}
					j++;
				}
			}

			uiDriver.robot.moveToElement(webDriver.findElement(By.name(objMap
					.getLocator("imgfd_Logo"))));

		}catch(Exception e){
			uiDriver.click("btnyourCart");
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(objMap.getLocator("txtSubTotal"))));
			List<WebElement> LstCartClick =webDriver.findElements(By.xpath(objMap.getLocator("lstverifyProducts")));
			System.out.println("\n Number of items in cart page is "+ LstCartClick.size());
			if(LstCartClick.size()== 0){
				RESULT.warning("Your Cart is Empty",
						"0 item should be available in cart",
				"0 item available in your cart");

			}else{
				int j = 0;
				//				int index_tbquant=0;
				for (WebElement item : LstCartClick) {
					//nam = item.findElements(By.xpath(objMap.getLocator("lstNAME")));
					List<WebElement> nam=webDriver.findElements(By.xpath(objMap.getLocator("lnkitemName1")));
					String name = nam.get(j).getText();
					List<WebElement> qaunt = item.findElements(By.className(objMap.getLocator("txtqty")));
					List<WebElement> quant_drp=item.findElements(By.className(objMap.getLocator("drpqty")));
					if (nam.get(j).getText().contains(productName.replaceAll("\n", " "))) 
					{
						if(quant_drp.size()>0)
						{
							myprod_quantity+=1;
						}
						else
						{
							if(qaunt.size()>1)
								myprod_quantity += Double.parseDouble(qaunt.get(j).getAttribute("value"));
							else
								myprod_quantity += Double.parseDouble(qaunt.get(0).getAttribute("value"));
						}
					} 
					j++;
				}
			}
			webDriver.navigate().back();
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(objMap.getLocator("btnyourCart"))));
		}
		return myprod_quantity;
	}

	public void orderCreationPopup(String orderflag_new_modify) {
		//int count = 0;
		//if (uiDriver.popup_isAlertPresent(5));
		SleepUtils.getInstance().sleep(TimeSlab.YIELD);
		//do {
		if (webDriver.findElements(
				By.xpath(objMap.getLocator("popaddToCart11"))).size() > 0
				&& webDriver.findElement(
						By.xpath(objMap.getLocator("popaddToCart11")))
						.isDisplayed()) {
			// If user wants to add to a new order
			if (orderflag_new_modify.equalsIgnoreCase("New")) {
				uiDriver.getwebDriverLocator(
						objMap.getLocator("popaddToCart11")).click();
				//break;
			}
			/*
			 * If user wants to modify an existing order Pre-condtion :
			 * Default 0 index provided to select dropdownlist
			 */
			else if (orderflag_new_modify.equalsIgnoreCase("Modify")) {
				uiDriver.getwebDriverLocator(
						objMap.getLocator("popaddToCart12")).click();
				Select select = new Select(uiDriver
						.getwebDriverLocator(objMap
								.getLocator("popaddToCart21")));
				select.selectByIndex(0);
				uiDriver.getwebDriverLocator(
						objMap.getLocator("popaddToCart22")).click();
				//break;
			} else {
				uiDriver.getwebDriverLocator(objMap.getLocator("btnClose"))
				.click();
				//break;
			}
			// 
		}
		//count++;
		//} while (count < 3);
	}

	/**
	 * Function Name: FD_addToCart
	 * Purpose: For adding a product to cart from reorder and normal page with hover and click method
	 * Created By: Bhavik Tikudiya Created Date: 27th Oct,2015
	 * Modified By: Modified Date:
	 * 
	 * @param method
	 *            method to be user for adding a product to cart. i.e. hover or click
	 * @param item
	 *            product/item to be added to cart
	 * @param quantity
	 *            quantity of the item/product to be added in cart
	 * @param new_modify_flag
	 *            flag to decide weather create a new order or modify
	 * @param reorder_grid_or_list
	 *  		  for reorder slect the view from which item/product to be added. i.e. grid/list
	 * @param flexibility
	 *  		  Flexibily flag if user value not available
	 *  
	 * @return void
	 */
	public void FD_addToCart(String method, String item,
			String quantity, String new_modify_flag,
			String reorder_grid_or_list, String flexibility) {
		try {

			// product to be added
			WebElement product;			
			// method for add to cart
			add_method = method;

			//wait for the page statibility
			waitForPageLoad();

			// store the page reorder/normal and first_item
			// get the default first item for
			if (webDriver.findElements(
					By.xpath(objMap.getLocator("tabyourTopItems"))).size() > 0) {
				
				//refresh for IE
				//if (CompositeAppDriver.startUp.equalsIgnoreCase("IE")) {
					webDriver.navigate().refresh();
					waitForPageLoad();
					wait.until(ExpectedConditions.visibilityOfElementLocated(By
							.xpath(objMap.getLocator("tabyourTopItems"))));
				//}
				
				// update the reorder_page flag
				reorder_page = true;
				// for grid
				try
				{
					if (reorder_grid_or_list.equalsIgnoreCase("grid")) {
						uiDriver.click("btngridReorder");
						wait.until(ExpectedConditions.visibilityOfElementLocated(By
								.xpath(objMap.getLocator("lnkfirstItem"))));
						//						SleepUtils.getInstance().sleep(TimeSlab.LOW);
						first_item = objMap.getLocator("lnkfirstItem");
					}
					// for list
					else if (reorder_grid_or_list.equalsIgnoreCase("list")) {
						uiDriver.click("btnlistReorder");
						wait.until(ExpectedConditions.visibilityOfElementLocated(By
								.xpath(objMap.getLocator("lnkfirstItemReorder"))));
						first_item = objMap.getLocator("lnkfirstItemReorder");
					}
					// default move ahead with the grid
					else {
						uiDriver.click("btngridReorder");
						//						SleepUtils.getInstance().sleep(TimeSlab.LOW);
						wait.until(ExpectedConditions.visibilityOfElementLocated(By
								.xpath(objMap.getLocator("lnkfirstItem"))));
						first_item = objMap.getLocator("lnkfirstItem");
						RESULT
						.warning(
								"Grid or List Selection",
								"Grid or List prefrence for product selction should be provided",
						"Grid or List prefrence for product selction not provided, moving with Grid");
					}
				}
				catch(Exception e)
				{
					RESULT.warning("Reorder: Grid or List Selection", "Grid or List button should be clicked", 
					"Unable to click Grid or List");
				}

			} else {
				first_item = objMap.getLocator("lnkfirstItem");
			}

			// selection of the product
			try {
				product = productSelection(item, flexibility);
				// return from the function if product is null
				if (product == null)
					return;
			} catch (Exception e) {
				RESULT.failed("Product selection",
						"Product should be selected successfully",
						"Product selection failed due to: " + e.getMessage());
				return;
			}

			// product out of stock check
			try {
				product = handleOutOfStock(reorder_page,
						reorder_grid_or_list, flexibility);
				if (product == null)
					return;
			} catch (Exception e) {
				RESULT.failed("Product out of stock check",
						"Product out of stock check should be successfully",
						"Product  out of stock check failed due to: "
						+ e.getMessage());
				return;
			}

			// product customizable identification
			try {
				item_customizable = itemCustomizationCheck(
						reorder_page, reorder_grid_or_list);
			} catch (Exception e) {
				RESULT
				.failed(
						"Product customizable check",
						"Product customizable check should be successfully",
						"Product customizable failed due to: "
						+ e.getMessage());
				return;
			}

			// retrieve initial Quantity of the product to be added before
			// adding it to cart
			item_initialexistingqty = yourCart(productFullName);

			// add item to cart based on the click or hover prefrence provided
			if (method.equalsIgnoreCase("click")
					|| method.replace(" ", "").length() == 0 || method == null || (!method.equalsIgnoreCase("click") && !method.equalsIgnoreCase("hover"))) {
				//show warning if method contains value apart from hover or click and go ahead with click
				if (method.replace(" ", "").length() == 0 || method == null || (!method.equalsIgnoreCase("click") && !method.equalsIgnoreCase("hover"))) {
					RESULT.warning("Method to add product to cart",
							"Method to add product to cart should be provided.",
					"Method to add product to cart is not as per the allowed selection, instead going ahead with Click method");
				}

				// store the method into variable
				add_method = "click";

				// add item to cart using click method
				try{
					//for item having dropdown quantity we need to assign 1 after adding product
					item_quantity = quantity;
					itemAddUsingClick(quantity, new_modify_flag, reorder_grid_or_list);
					quantity = item_quantity;
				}catch(Exception e){
					RESULT.failed("Add item to cart using click method",
							"Add item to cart using click method should be successful",
							"Add item to cart using click method failed due to: "
							+ e.getMessage());
					return;
				}

			} else {

				// store the method into variable
				add_method = "hover";

				// add item to cart using hover method
				try{
					//for item having dropdown quantity we need to assign 1 after adding product
					item_quantity = quantity;
					itemAddUsingHover(quantity, new_modify_flag, reorder_grid_or_list);
					quantity = item_quantity;
				}catch(Exception e){
					RESULT.failed("Add item to cart using hover method",
							"Add item to cart using hover method should be successful",
							"Add item to cart using hover method failed due to: "
							+ e.getMessage());
					e.printStackTrace();
					return;
				}
			}

			// retrieve final Quantity of the product to be added after
			// adding it to cart
			item_finalexistingqty = yourCart(productFullName);

			// dispaly result based on the method used for add to cart
			try{
				itemAddValidations(add_method, quantity);
			}catch(Exception e){
				RESULT.failed("Add item to cart validations",
						"Add item to cart validations should be successful",
						"Add item to cart validations failed due to: "
						+ e.getMessage());
				return;
			}

		} catch (Exception e) {
			RESULT.failed("Add item to cart function Exception",
					"Add item to cart function should be successful",
					"Add item to cart function failed due to: "
					+ e.getMessage());
		}// end of main try/catch block
	}

	//function to validate the added item in /FD_addToCart
	public void itemAddValidations(String addMethod, String quantity) {		

		//validation for the click method having final added quantity greater then the initial 
		if (addMethod.equalsIgnoreCase("click")
				&& item_finalexistingqty > item_initialexistingqty) {
			if (item_finalexistingqty == item_initialexistingqty
					+ Double.parseDouble(quantity)) {
				RESULT.passed("Item added to cart using click method",
						"Item: "+ productFullName
						+ " addition should be successful to cart with quantity: "
						+ quantity,
						"Item: "+ productFullName
						+ " addition successful to cart with quantity: "
						+ quantity);
			} else if (item_finalexistingqty == item_initialexistingqty
					+ item_newquantity) {
				RESULT.passed("Item added to cart using click method",
						"Item: "+ productFullName
						+ " addition should be successful to cart with quantity: "
						+ quantity,
						"Item: "+ productFullName
						+ " addition successful to cart with quantity: "
						+ item_newquantity);
			}
			else {
				RESULT.failed("Item added to cart using click method",
						"Item: "+ productFullName
						+ " addition should be successful to cart with quantity: "
						+ quantity,
						"Item addition unsuccessful to cart. Quantity is: "
						+ item_finalexistingqty);
			}
		}

		//validation for the hover method having final added quantity greater then the initial 
		else if (addMethod.equalsIgnoreCase("hover")
				&& item_finalexistingqty > item_initialexistingqty) {

			if (item_finalexistingqty == item_initialexistingqty
					+ Double.parseDouble(quantity)) {
				RESULT.passed("Item added to cart using hover method",
						"Item: "+ productFullName
						+ " addition should be successful to cart with quantity: "
						+ quantity,
						"Item: "+ productFullName
						+ " addition successful to cart with quantity: "
						+ quantity);
			}
			else if (item_finalexistingqty == item_newquantity) {
				RESULT.passed("Item added to cart using hover method",
						"Item: "+ productFullName
						+ " addition should be successful to cart with quantity: "
						+ quantity,
						"Item:"+ productFullName
						+ " addition successful to cart with partial quantity: "
						+ (item_finalexistingqty -item_initialexistingqty));
			} else {
				RESULT.failed("Item added to cart using hover method",
						"Item"+ productFullName
						+ " addition should be unsuccessful to cart with quantity: "
						+ quantity,
						"Item: "+ productFullName
						+ " addition unsuccessful to cart. Quantity is: "
						+ (item_finalexistingqty - item_newquantity));
			}

		}

		// if threshold value is entered for click method
		else if (clickerror_added_quantity == item_finalexistingqty && clickerror_added_quantity == item_initialexistingqty) {
			RESULT.warning("Item added to cart using click method",
					"Item: "+ productFullName
					+ " threshold value should reach with quantity: "
					+ (item_initialexistingqty),
					"Item: "+ productFullName
					+ " threshold value reach with quantity: "
					+ item_finalexistingqty);
		}

		// if threshold value is entered for click method
		else if (hovererror_added_quantity == item_finalexistingqty && hovererror_added_quantity == item_initialexistingqty) {
			RESULT.warning("Item added to cart using hover method",
					"Item: "+ productFullName
					+ " threshold value should reach with quantity: "
					+ (item_initialexistingqty),
					"Item: "+ productFullName
					+ " threshold value reach with quantity: "
					+ item_finalexistingqty);
		}

		//if any validation does not get hit
		else {
			RESULT.failed("Item add to cart", "Item should be added ",
			"Item adding unsuccessful");
		}

	}

	// function to add item into cart using hover method in /FD_addToCart
	public void itemAddUsingHover(String quantity, String new_or_modify, String gridOrList) {

		SleepUtils.getInstance().sleep(TimeSlab.YIELD);

		// create product object to avoid attchement to the DOM
		WebElement product;
		//for list
		if(reorder_page ==  true && gridOrList.equalsIgnoreCase("list"))
			product = webDriver.findElement(By.partialLinkText(productFullName.trim().replaceAll("\n", " ")));
		else
			product = webDriver.findElement(By.partialLinkText(productFullName.trim()));

		//scroll to the item to be added using hover method
		JavascriptExecutor jse = (JavascriptExecutor) webDriver;
		jse.executeScript("arguments[0].scrollIntoView()", product);

		//hover on to the item
		robot.moveToElement(product);

		SleepUtils.getInstance().sleep(TimeSlab.YIELD);

		//for customizable item
		if (item_customizable == true) {

			//for reorder using list add to cart click
			if(reorder_page ==  true && gridOrList.equalsIgnoreCase("list")){

				//find the product from list and perform the operation
				List<WebElement> RowProdList = webDriver.findElements(By
						.xpath("//*[@id='productlist']/ol/li"));
				boolean loop_break = false;
				for (WebElement prod1 : RowProdList) {
					if (prod1.findElement(
							By.xpath("//div[@class='itemlist-item-header']/a"))
							.getText().contains(productFullName.replaceAll("\n", " "))) {
						//set flag to break the loop
						loop_break = true;
						if (prod1.findElements(By.xpath("//div[@class='itemlist-item-addtocart']/button"))
								.size() > 0) {
							prod1.findElement(By.xpath("//div[@class='itemlist-item-addtocart']/button")).click();
						}
					}
					// break the loop based on flag
					if(loop_break){
						break;
					}
				}

			}
			//for normal and reorder using grid add to cart click
			else{
				if (webDriver.findElements(By.xpath(objMap.getLocator("btnsearchAddToCartCustomize")))
						.size() > 0) {
					// hover over add to cart
					jse.executeScript("arguments[0].scrollIntoView()", webDriver.findElement(By.xpath(objMap.getLocator("btnsearchAddToCartCustomize"))));
					robot.moveToElement(webDriver.findElement(By.xpath(objMap.getLocator("btnsearchAddToCartCustomize"))));
					//click on add to cart
					uiDriver.getwebDriverLocator(objMap.getLocator("btnsearchAddToCartCustomize")).click();
				}
			}

			// wait for the pop for customization using its add to cart
			try{			
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(objMap
						.getLocator("btnaddToCartCustHover"))));
			}
			catch(Exception e){
				RESULT.failed("Customization pop up add to cart button in hover method",
						"Customization pop up add to cart button in hover method should be available",
				"Customization pop up add to cart button in hover method not available ");
				return;
			}

			// get drop downs for selection
			List<WebElement> drp_cust = uiDriver
			.getwebDriverLocator(
					objMap.getLocator("lstdrpCustomizeProductHover"))
					.findElements(By.tagName("Select"));
			// select the drop down values
			for (WebElement ele_drp_cust : drp_cust) {
				jse.executeScript("arguments[0].scrollIntoView()", ele_drp_cust);
				Select s = new Select(ele_drp_cust);
				s.selectByIndex(1);
			}

			// enter the quantity in text box if avaialble
			if (webDriver.findElements(By.xpath(objMap.getLocator("txtqauntityCustHover")))
					.size() > 0) {
				if (!(quantity.equals(null) || quantity == "" || quantity == " " || quantity.matches("^[a-zA-z]$"))) {
					if (CompositeAppDriver.startUp.equalsIgnoreCase("FIREFOX")) {
						uiDriver.getwebDriverLocator(objMap.getLocator("txtqauntityCustHover"))
						.click();
						actions.keyDown(Keys.CONTROL).sendKeys(
						"a").keyUp(Keys.CONTROL)
						.sendKeys(Keys.DELETE)
						.perform();
					} else {
						uiDriver.getwebDriverLocator(objMap.getLocator("txtqauntityCustHover"))
						.click();
						uiDriver.getwebDriverLocator(objMap.getLocator("txtqauntityCustHover"))
						.sendKeys(Keys.DELETE);
						uiDriver.getwebDriverLocator(objMap.getLocator("txtqauntityCustHover"))
						.sendKeys(Keys.DELETE);
						uiDriver.getwebDriverLocator(objMap.getLocator("txtqauntityCustHover"))
						.sendKeys(Keys.DELETE);
						uiDriver.getwebDriverLocator(objMap.getLocator("txtqauntityCustHover"))
						.sendKeys(Keys.BACK_SPACE);
						uiDriver.getwebDriverLocator(objMap.getLocator("txtqauntityCustHover"))
						.sendKeys(Keys.BACK_SPACE);
						uiDriver.getwebDriverLocator(objMap.getLocator("txtqauntityCustHover"))
						.sendKeys(Keys.BACK_SPACE);
					}
					uiDriver.getwebDriverLocator(objMap.getLocator("txtqauntityCustHover"))
					.sendKeys(quantity);
				}else{
					RESULT.warning("Product quantity box value",
							"Product quantity box value should be integer",
							"Product quantity box is not integer : " + quantity);
				}
			} 
			// if quantity with drop down box is avaialble
			else {
				// store 1 is global item quantity variable for future use
				item_quantity = "1";
				if (webDriver.findElement(By.className(objMap.getLocator("divproductClassHover"))).findElements(
						By.className(objMap.getLocator("drpqty"))).size() > 0)
				{
					Select salesunit_item = new Select(webDriver.findElement(By
							.xpath(objMap.getLocator("drpqty"))));
					salesunit_item.selectByIndex(1);

				}
			}

			// click on add to cart in customization box
			webDriver.findElement(By.xpath(objMap.getLocator("btnaddToCartCustHover")))
			.click();
			waitForPageLoad();

		}
		// for normal item
		else {				

			//for reorder using list 
			if(reorder_page ==  true && gridOrList.equalsIgnoreCase("list")){

				//find the product from list and perform the operation
				List<WebElement> RowProdList = webDriver.findElements(By
						.xpath("//*[@id='productlist']/ol/li"));
				boolean loop_break = false;
				for (WebElement prod1 : RowProdList) {
					//once product is found perform the operation
					if (prod1.findElement(
							By.xpath("//div[@class='itemlist-item-header']/a"))
							.getText().contains(productFullName.replaceAll("\n", " "))) {
						//set flag to break the loop
						loop_break = true;
						// quantity box having drop down
						if (prod1.findElements(By.xpath(objMap.getLocator("drpsalesUnitCustProduct")))
								.size() > 0) {
							Select salesunit_item = new Select(prod1.findElement(By.xpath(objMap.getLocator("drpsalesUnitCustProduct"))));
							salesunit_item.selectByIndex(1);
							// store 1 is global item quantity variable for future use
							item_quantity = "1";
						}
						// quantity box having text box
						else{
							if (!(quantity.equals(null) || quantity == "" || quantity == " " || quantity.matches("^[a-zA-z]$"))) {
								if (CompositeAppDriver.startUp.equalsIgnoreCase("FIREFOX")) {
									prod1.findElement(By.className(objMap.getLocator("txtqty")))
									.click();

									actions.keyDown(Keys.CONTROL).sendKeys("a")
									.keyUp(Keys.CONTROL).sendKeys(
											Keys.DELETE).perform();
								} else {
									prod1.findElement(By.className(objMap.getLocator("txtqty")))
									.click();
									prod1.findElement(By.className(objMap.getLocator("txtqty")))
									.sendKeys(Keys.DELETE);
									prod1.findElement(By.className(objMap.getLocator("txtqty")))
									.sendKeys(Keys.DELETE);
									prod1.findElement(By.className(objMap.getLocator("txtqty")))
									.sendKeys(Keys.DELETE);
									prod1.findElement(By.className(objMap.getLocator("txtqty")))
									.sendKeys(Keys.BACK_SPACE);
									prod1.findElement(By.className(objMap.getLocator("txtqty")))
									.sendKeys(Keys.BACK_SPACE);
									prod1.findElement(By.className(objMap.getLocator("txtqty")))
									.sendKeys(Keys.BACK_SPACE);
								}

								prod1.findElement(By.className(objMap.getLocator("txtqty")))
								.sendKeys(quantity);

							}else{
								RESULT.warning("Product quantity box value",
										"Product quantity box value should be integer",
										"Product quantity box is not integer : " + quantity);
							}
						}

						// click on add to cart					
						prod1.findElement(By.xpath(objMap.getLocator("lstaddToCartList")))
						.click();
						waitForPageLoad();
					}
					// break the loop based on flag
					if(loop_break){
						break;
					}
				}

			}
			//for normal and reorder using grid 
			else{
				// quantity box having drop down
				if (webDriver.findElement(By.className(objMap.getLocator("divproductClassHover")))
						.findElements(By.className(objMap.getLocator("drpqty"))).size() > 0)
				{
					Select salesunit_item = new Select(webDriver.findElement(
							By.className(objMap.getLocator("divproductClassHover")))
							.findElement(By.className(objMap.getLocator("drpqty"))));
					salesunit_item.selectByIndex(1);
					// store 1 is global item quantity variable for future use
					item_quantity = "1";
				}
				// quantity box having text box
				else 
				{
					if (!(quantity.equals(null) || quantity == "" || quantity == " " || quantity.matches("^[a-zA-z]$"))) {
						if (CompositeAppDriver.startUp.equalsIgnoreCase("FIREFOX")) {
							webDriver.findElement(By.className(objMap.getLocator("divproductClassHover")))
							.findElement(By.className(objMap.getLocator("txtqty")))
							.click();
							actions.keyDown(Keys.CONTROL).sendKeys("a")
							.keyUp(Keys.CONTROL).sendKeys(
									Keys.DELETE).perform();
						} else {
							webDriver.findElement(By.className(objMap.getLocator("divproductClassHover")))
							.findElement(By.className(objMap.getLocator("txtqty")))
							.click();
							webDriver.findElement(By.className(objMap.getLocator("divproductClassHover")))
							.findElement(By.className(objMap.getLocator("txtqty")))
							.sendKeys(Keys.DELETE);
							webDriver.findElement(By.className(objMap.getLocator("divproductClassHover")))
							.findElement(By.className(objMap.getLocator("txtqty")))
							.sendKeys(Keys.DELETE);
							webDriver.findElement(By.className(objMap.getLocator("divproductClassHover")))
							.findElement(By.className(objMap.getLocator("txtqty")))
							.sendKeys(Keys.DELETE);
							webDriver.findElement(By.className(objMap.getLocator("divproductClassHover")))
							.findElement(By.className(objMap.getLocator("txtqty")))
							.sendKeys(Keys.BACK_SPACE);
							webDriver.findElement(By.className(objMap.getLocator("divproductClassHover")))
							.findElement(By.className(objMap.getLocator("txtqty")))
							.sendKeys(Keys.BACK_SPACE);
							webDriver.findElement(By.className(objMap.getLocator("divproductClassHover")))
							.findElement(By.className(objMap.getLocator("txtqty")))
							.sendKeys(Keys.BACK_SPACE);
						}

						webDriver.findElement(By.className(objMap.getLocator("divproductClassHover")))
						.findElement(By.className(objMap.getLocator("txtqty")))
						.sendKeys(quantity);
					}else{
						RESULT.warning("Product quantity box value",
								"Product quantity box value should be integer",
								"Product quantity box is not integer : " + quantity);
					}
				}
				// click on add to cart
				webDriver.findElement(By.className(objMap.getLocator("divproductClassHover")))
				.findElement(By.className(objMap.getLocator("btnaddToCartClass")))
				.click();
				waitForPageLoad();
			}
		}

		// This will accept the alcohol terms and conditions while
		// adding it to cart for alcohol item
		if(item_alcohol && item_alcohol_accepted == false){
			handleAlcoholAlert("hover");
		}


		// handle the new/modify order
		SleepUtils.getInstance().sleep(TimeSlab.YIELD);
		uiDriver.orderCreationPopup(new_or_modify);
		SleepUtils.getInstance().sleep(TimeSlab.YIELD);

		// for the successful adding of quantity for reorder using list 
		if(reorder_page ==  true && gridOrList.equalsIgnoreCase("list")){
			//find the product from list and perform the operation
			List<WebElement> RowProdList = webDriver.findElements(By
					.xpath("//*[@id='productlist']/ol/li"));
			boolean loop_break = false;
			for (WebElement prod1 : RowProdList) {
				//once product is found perform the operation
				if (prod1.findElement(
						By.xpath("//div[@class='itemlist-item-header']/a"))
						.getText().contains(productFullName.replaceAll("\n", " "))) {
					//set flag to break the loop
					loop_break = true;
					if (prod1.findElements(By.cssSelector(objMap.getLocator("stritemInCartSuccess"))).size() > 0) {
						item_newquantity = Double.parseDouble(prod1.findElement(By.cssSelector(objMap.getLocator("stritemInCartSuccess")))
								.getText().split(" ")[0]);
						//check with the update quantity handling drop down one
						if(Double.parseDouble(item_quantity) > item_newquantity){
							RESULT.warning("Add to cart using hover method", 
									"Add to cart should be successful with quantity: " + quantity, 
									"Add to cart should is successful with allowed quantity: " + item_newquantity);
						}
					} else {			
						if (prod1.findElements(By.cssSelector(objMap.getLocator("stritemInCartError"))).size() > 0) {
							hovererror_added_quantity = Double.parseDouble(prod1.findElement(By.cssSelector(objMap.getLocator("stritemInCartError")))
									.getText().split(" ")[0]);			
						}else{
							RESULT.failed("Add to cart using hover method",
									"Quantity added message should be available in hover method",
									"Quantity added message is not available in hover method for user quantity: " + quantity);
							return;
						}		
					}
				}
				//break the loop based on the flag
				if(loop_break){
					break;
				}
			}
		}
		//for the successful adding of quantity for normal reorder using grid
		else{
			//hover to item for verification
			robot.moveToElement(product);
			//for the successful adding of quantity
			if (webDriver.findElements(By.cssSelector(objMap.getLocator("stritemInCartSuccess"))).size() > 0) {
				item_newquantity = Double.parseDouble(uiDriver.getwebDriverLocator(objMap.getLocator("stritemInCartSuccess"))
						.getText().split(" ")[0]);
				//check with the update quantity handling drop down one
				if(Double.parseDouble(item_quantity) > item_newquantity){
					RESULT.warning("Add to cart using hover method", 
							"Add to cart should be successful with quantity: " + quantity, 
							"Add to cart should is successful with allowed quantity: " + item_newquantity);
				}
			} else {			
				if (webDriver.findElements(By.cssSelector(objMap.getLocator("stritemInCartError"))).size() > 0) {
					hovererror_added_quantity = Double.parseDouble(uiDriver.getwebDriverLocator(objMap.getLocator("stritemInCartError"))
							.getText().split(" ")[0]);			
				}else{
					RESULT.failed("Add to cart using hover method",
							"Add to cart with error quantity should be available using hover method",
							"Add to cart with error quantity is not available using hover method for user quantity: " + quantity);
					return;
				}		
			}	
		}
	}

	// function to item into cart using click method in /FD_addToCart
	public void itemAddUsingClick(String quantity, String new_or_modify, String gridOrList) {


		// create product object to avoid attchement to the DOM
		WebElement product;
		//for list
		if(reorder_page ==  true && gridOrList.equalsIgnoreCase("list"))
			product = webDriver.findElement(By.partialLinkText(productFullName.trim().replaceAll("\n", " ")));
		else
			product = webDriver.findElement(By.partialLinkText(productFullName.trim()));
		
		
		//scroll to the item to be added using hover method
		JavascriptExecutor jse = (JavascriptExecutor) webDriver;
		jse.executeScript("arguments[0].scrollIntoView()", product);

		//move to the item
		robot.moveToElement(product);
		//click on product based on the browser
		if (CompositeAppDriver.startUp.equalsIgnoreCase("FIREFOX")
				|| CompositeAppDriver.startUp
				.equalsIgnoreCase("CHROME")
				|| CompositeAppDriver.startUp
				.equalsIgnoreCase("IE")) {
			jse.executeScript("arguments[0].click()", product);
		} else if (CompositeAppDriver.startUp
				.equalsIgnoreCase("SAFARI")) {
			product.click();
		}else{
			product.click();
		}
		//wait for page to navigate
		waitForPageLoad();
		// if product is alcohol alert should be dispalyed
		if (item_alcohol && item_alcohol_accepted == false) {
			handleAlcoholAlert("click");
		}
		//wait for the PDP page to be available
		try{			
			wait.until(ExpectedConditions
					.invisibilityOfElementLocated(By
							.partialLinkText(productFullName)));
			waitForPageLoad();
			wait.until(ExpectedConditions.visibilityOfElementLocated(By
					.xpath(objMap.getLocator("btnback"))));

		}catch(Exception e){
			RESULT.failed("Navigation to PDP page in click method",
					"Navigation to PDP page in click method should be successful",
					"Navigation to PDP page in click method failed due to: "
					+ e.getMessage());
			return;
		}

		//if product is customizable select the dropdown with first value
		if (item_customizable == true) {
			try{
				wait.until(ExpectedConditions
						.presenceOfElementLocated(By
								.xpath(objMap
										.getLocator("lstdrpCustomizeProduct"))));
				List<WebElement> drp_cust;
				do {				
					drp_cust = uiDriver
					.getwebDriverLocator(objMap.getLocator("lstdrpCustomizeProduct"))
					.findElements(By.tagName("select"));				
				} while (drp_cust.size() < 0);

				if (drp_cust.size() > 0) {
					for (WebElement ele_drp_cust : drp_cust) {					
						Select s = new Select(ele_drp_cust);
						s.selectByIndex(1);
					}
				}
			}catch(Exception e){
				RESULT.failed("Customizable product drop downs selction",
						"Customizable product drop downs selction should be successful",
						"Customizable product drop downs selction failed due to: "
						+ e.getMessage());
				return;
			}
		}

		//if quantity is in dropdown form
		if (webDriver.findElement(By.className(objMap
				.getLocator("divproductDrpQty"))).findElements(
						By.className(objMap.getLocator("drpqty")))
						.size() > 0){
			Select salesunit_item = new Select(webDriver.findElement(By.className(objMap
					.getLocator("divproductDrpQty")))
					.findElement(By.tagName("Select")));
			salesunit_item.selectByIndex(1);
			// store 1 is global item quantity variable for future use
			item_quantity = "1";
		}
		//else textbox should be displayed
		else{
			if (webDriver.findElement(
					By.className(objMap
							.getLocator("divproductClassClick")))
							.findElements(
									By.className(objMap.getLocator("txtqty")))
									.size() > 0){
				//check for the quantity provide by user is proper or not
				if (!(quantity.equals(null) || quantity.replace(" ", "").length() == 0 || quantity.matches("^[a-zA-z]$"))) {
					if (CompositeAppDriver.startUp.equalsIgnoreCase("FIREFOX")
							|| CompositeAppDriver.startUp.equalsIgnoreCase("CHROME")) {
						webDriver.findElement(By.className(objMap.getLocator("divproductClassClick")))
						.findElement(By.className(objMap.getLocator("txtqty"))).click();
						actions.keyDown(Keys.CONTROL).sendKeys("a")
						.keyUp(Keys.CONTROL).sendKeys(Keys.DELETE).perform();
					} else {
						webDriver.findElement(By.className(objMap.getLocator("divproductClassClick")))
						.findElement(By.className(objMap.getLocator("txtqty")))
						.click();
						webDriver.findElement(By.className(objMap.getLocator("divproductClassClick")))
						.findElement(By.className(objMap.getLocator("txtqty")))
						.sendKeys(Keys.BACK_SPACE);
						webDriver.findElement(By.className(objMap.getLocator("divproductClassClick")))
						.findElement(By.className(objMap.getLocator("txtqty")))
						.sendKeys(Keys.BACK_SPACE);
						webDriver.findElement(By.className(objMap.getLocator("divproductClassClick")))
						.findElement(By.className(objMap.getLocator("txtqty")))
						.sendKeys(Keys.BACK_SPACE);
						webDriver.findElement(By.className(objMap.getLocator("divproductClassClick")))
						.findElement(By.className(objMap.getLocator("txtqty")))
						.sendKeys(Keys.DELETE);
						webDriver.findElement(By.className(objMap.getLocator("divproductClassClick")))
						.findElement(By.className(objMap.getLocator("txtqty")))
						.sendKeys(Keys.DELETE);
						webDriver.findElement(By.className(objMap.getLocator("divproductClassClick")))
						.findElement(By.className(objMap.getLocator("txtqty")))
						.sendKeys(Keys.DELETE);
					}
					webDriver.findElement(By.className(objMap.getLocator("divproductClassClick")))
					.findElement(By.className(objMap.getLocator("txtqty")))
					.sendKeys(quantity);
				}else{
					RESULT.warning("Product quantity box value",
							"Product quantity box value should be integer",
							"Product quantity box is not integer : " + quantity);
				}
			}else{
				RESULT.failed("Product quantity box",
						"Product quantity box should be avaialble",
				"Product quantity box is not avaialble");
				return;
			}
		}

		// click on add to cart
		webDriver.findElement(By.xpath(objMap.getLocator("btnaddToCart")))
		.click();
		waitForPageLoad();
		SleepUtils.getInstance().sleep(TimeSlab.YIELD);

		//handle the  craete new/modify order pop-up
		orderCreationPopup(new_or_modify);
		SleepUtils.getInstance().sleep(TimeSlab.YIELD);

		//get the added quantity based on page navigation
		//check if we are still on same PDP page
		//if we are on pdp page
		if(webDriver.findElements(By.xpath(objMap.getLocator("btnaddToCart"))).size() > 0){
			if (webDriver.findElements(
					By.cssSelector(objMap
							.getLocator("stritemInCartError"))).size() > 0) {
				clickerror_added_quantity = Double
				.parseDouble(uiDriver.getwebDriverLocator(
								objMap.getLocator("stritemInCartError"))
								.getText().split(" ")[0]);				
			}else{
				RESULT.failed("Product add to cart quantity exceeded",
						"Product add to cart quantity exceeded message should be avaialble",
				"Product add to cart quantity exceeded message is not avaialble");
				return;
			}
		}
		//if we are on continue shopping page
		else{

			// wait for the countinue shopping page after add to cart
			wait.until(ExpectedConditions
					.visibilityOfElementLocated(By.linkText((objMap
							.getLocator("btncontShopping")))));

			// if successfully added get the quantity
			String qty[];
			qty = webDriver
			.findElement(
					By
					.xpath(objMap
							.getLocator("straddedQuantity")))
							.getText().split(":");
			item_newquantity = Double.parseDouble(qty[1].trim());
		}
	}

	// function to check item customizable in /FD_addToCart
	public boolean itemCustomizationCheck(boolean reorderPage, String gridOrList) {

		// create product object to avoid attchement to the DOM
		WebElement product;
		//for list
		if(reorder_page ==  true && gridOrList.equalsIgnoreCase("list"))
			product = webDriver.findElement(By.partialLinkText(productFullName.trim().replaceAll("\n", " ")));
		else
			product = webDriver.findElement(By.partialLinkText(productFullName.trim()));	
		
		// hover over the product to check for customization
		robot.moveToElement(product);

		// for reorder page
		if (reorderPage) {
			// for list
			if (gridOrList.equalsIgnoreCase("list")) {
				List<WebElement> RowProdList = webDriver.findElements(By
						.xpath("//*[@id='productlist']/ol/li"));
				for (WebElement prod1 : RowProdList) {
					if (prod1.findElement(
							By.xpath("//div[@class='itemlist-item-header']/a"))
							.getText().contains(productFullName)) {
						if (prod1
								.findElements(
										By
										.xpath("//div[@class='itemlist-item-addtocart']/button"))
										.size() > 0) {
							return true;
						}
					}
				}
			}
			// for grid
			else {
				if (webDriver.findElements(
						By.xpath(objMap
								.getLocator("btnreorderGridCustomizable")))
								.size() > 0) {
					return true;
				}
			}
		}
		// for normal page
		else {
			if (webDriver.findElements(
					By.xpath(objMap.getLocator("btnsearchAddToCartCustomize")))
					.size() > 0) {
				return true;
			}
		}
		return false;
	}

	// function to handle out of stock in /FD_addToCart
	public WebElement handleOutOfStock(boolean reorderPage,
			String gridOrList, String flexibility) {

		// out of stock flag
		boolean outOfStock_flag = false;
		// alcohol flag
		boolean alcohol_product = false;
		
		// create product object to avoid attchement to the DOM
		WebElement product;
		//for list
		if(reorder_page ==  true && gridOrList.equalsIgnoreCase("list"))
			product = webDriver.findElement(By.partialLinkText(productFullName.trim().replaceAll("\n", " ")));
		else
			product = webDriver.findElement(By.partialLinkText(productFullName.trim()));	
		
		//scroll to the item to be added using hover method
		JavascriptExecutor jse = (JavascriptExecutor) webDriver;
		jse.executeScript("arguments[0].scrollIntoView()", product);
		// move to product for out of stock check	
		robot.moveToElement(product);

		// our of stock check returning flags
		boolean temp_flags[] = outOfStockCheck(reorderPage, gridOrList);

		outOfStock_flag = temp_flags[0];
		alcohol_product = temp_flags[1];

		// store alcohol flag for future use in global variable
		item_alcohol = alcohol_product;

		// handle out of stock product
		if (outOfStock_flag == true) {

			// click on the product to get the unavailability message
			robot.moveToElement(product);
			product.click();
			// SleepUtils.getInstance().sleep(TimeSlab.LOW);
			waitForPageLoad();

			// if product is alcohol alert should be dispalyed
			if (alcohol_product) {
				handleAlcoholAlert("click");
			}

			// unavailable message should be dispalyed
			if (webDriver.findElements(
					By.xpath(objMap.getLocator("strprodUnavailability")))
					.size() > 0) {
				// warning for out of stock product
				RESULT.warning("Out of stock product",
						"Product is Out of stock",
						"Message: "
						+ uiDriver
						.getwebDriverLocator(objMap.getLocator("strprodUnavailability"))
								.getText());

				// go back to the product list page
				uiDriver.click("btnback");
				SleepUtils.getInstance().sleep(TimeSlab.LOW);

				// if alcohol go back one more time
				if (alcohol_product) {
					webDriver.navigate().back();
					SleepUtils.getInstance().sleep(TimeSlab.LOW);
				}

				// if flexibility yes then move ahead with first item
				if (flexibility.equalsIgnoreCase("yes")) {
					product = uiDriver.getwebDriverLocator(first_item);
					productFullName = product.getText();

					// move to product for out of stock check
					robot.moveToElement(product);

					// out of stock check returning flags
					temp_flags = outOfStockCheck(reorderPage, gridOrList);

					// store alcohol flag for future use in global variable
					item_alcohol = temp_flags[1];

					// if out of stock flag is true then return as first product
					// is out of stock too
					if (temp_flags[0] == true) {
						RESULT.warning("Out of stock product",
								"First product on the page should be out of stock",
						"First product on the page is out of stock");
						return null;
					}

				} else {
					RESULT.warning("Out of stock product", productFullName
							+ ": Desired product should be out of stock",
							productFullName
							+ ": Desired product is out of stock");
					return null;
				}
			} else {
				RESULT.failed("Out of stock unavailable message on PDP page",
						"Unavailable message should be dispalyed on PDP page for out of stock product",
				"Unavailable message is not dispalyed on PDP page for out of stock product");
				return null;
			}// unavailable message if/else
		}
		return product;
	}

	// function for out of stock check in / handleOutOfStock
	public boolean[] outOfStockCheck(boolean reorderPage, String gridOrList) {

		// flags for out of stock and alcohol return
		// o for out of stock and 1 for alcohol check
		boolean flags[] = new boolean[2];
		flags[0] = false;
		flags[1] = false;

		// check for out of stock based on page
		// for reorder page
		if (reorderPage == true) {

			// variable to be based on reorder page grid or list
			List<WebElement> RowProdList;
			String header_text_xpath;
			String alcohol_check_attribute;

			// for list
			if (gridOrList.equalsIgnoreCase("list")) {
				RowProdList = webDriver.findElements(By
						.xpath("//*[@id='productlist']/ol/li"));
				header_text_xpath = "//div[@class='itemlist-item-header']/a";
				alcohol_check_attribute = "data-productid";
			}
			// for grid
			else {
				RowProdList = webDriver.findElements(By
						.xpath("//*[@id='productlist']/ul/li"));
				header_text_xpath = "div[@class='portrait-item-header']/a";
				alcohol_check_attribute = "data-product-id";
			}

			for (WebElement prod1 : RowProdList) {
				if (prod1.findElement(By.xpath(header_text_xpath)).getText()
						.contains(productFullName)) {
					if (prod1.getAttribute("class").contains("unavailable")) {
						flags[0] = true;
					}
					// for alcohol product check the attributr if it
					// starts
					// with win_...
					if (prod1.getAttribute(alcohol_check_attribute).startsWith(
					"win")) {
						flags[1] = true;
					}
					break;
				}
			}
		}
		// for normal page
		else {
			List<WebElement> RowProdList = webDriver.findElements(By
					.xpath("//*[@class='sectionContent']/ul/li"));
			for (WebElement prod1 : RowProdList) {
				if (prod1.findElement(
						By.xpath("div[@class='portrait-item-header']/a"))
						.getText().contains(productFullName)) {
					if (prod1.getAttribute("class").contains("unavailable")) {
						flags[0] = true;
					}
					// for alcohol product check the attributr if it
					// starts
					// with win_...
					if (prod1.getAttribute("data-product-id").startsWith("win")) {
						flags[1] = true;
					}
					break;
				}
			}

		}
		return flags;
	}

	// function to handle alcohol alert in / handleOutOfStock
	public void handleAlcoholAlert(String method) {

		try{
			uiDriver.wait.until(ExpectedConditions.visibilityOfElementLocated(By.className(objMap.getLocator("alertAlcoholClick"))));
		}catch(Exception e){
			RESULT.failed("Health warning alert ",
					"Health warning : Alcohol alert should get displayed",
			"Health warning : Alcohol alert is not displayed");
		}

		if (webDriver.findElements(
				By.className(objMap.getLocator("alertAlcoholClick"))).size() > 0) {

			// store the value so that we know user has accepted the alcohol alert once

			item_alcohol_accepted = true;

			RESULT.passed("Health warning alert ",
					"Health warning : Alcohol alert should be accepted",
			"Health warning : Alcohol alert accepted");

			do {
				uiDriver.getwebDriverLocator(
						objMap.getLocator("btnalcoholAlertAccept")).click();
				waitForPageLoad();
			}while (webDriver.findElements(
					By.className(objMap.getLocator("alertAlcoholClick"))).size() > 0 && webDriver.findElement(
							By.className(objMap.getLocator("alertAlcoholClick")))
							.isDisplayed());

			// wait for the page to be dispalyed based on the add to caret method.
			try {
				// if click next page will be PDP
				if(method.equalsIgnoreCase("click")){
					wait.until(ExpectedConditions.visibilityOfElementLocated(By
							.xpath(objMap.getLocator("btnback"))));
				}
				// if hover it will be on same page
				else{
					SleepUtils.getInstance().sleep(TimeSlab.YIELD);
				}		
			} catch (Exception e) {
				RESULT.failed("Out of stock PDP page navigation",
						"PDP page should be displayed successfully",
				"PDP page is not displayed successfully");
			}

		} else {
			RESULT.failed("Health warning alert ",
					"Health warning : Alcohol alert should be accepted",
			"Health warning : Alcohol alert did not appear");
		}
	}

	// function for the product selection in /FD_addToCart
	public WebElement productSelection(String item, String flexibility) {
		// product to be returned
		WebElement product;

		item = item.replaceAll("\\\\n", "\n");
		try {
			// check for the product on the first page
			product = webDriver.findElement(By.partialLinkText(item.trim()));

			productFullName = product.getText();
		} catch (NoSuchElementException e) {
			// if not available on first page check click Show all and check
			// again
			try {
				if (webDriver.findElements(
						By.xpath(objMap.getLocator("btnshowAll"))).size() > 0) {
					// click on the show all button
					uiDriver.getwebDriverLocator(
							objMap.getLocator("btnshowAll")).click();
					// wait for the products to load
					wait.until(ExpectedConditions
							.invisibilityOfElementLocated(By.xpath(objMap
									.getLocator("btnpagePrev"))));
				}
				// check for the product after showall
				product = webDriver
				.findElement(By.partialLinkText(item.trim()));
				productFullName = product.getText();
			} catch (NoSuchElementException e1) {
				// if product not available after show all check for flexibility

				// if flexibility is yes then add the first product
				if (flexibility.equalsIgnoreCase("yes")) {
					product = uiDriver.getwebDriverLocator(first_item);
					productFullName = product.getText();
					RESULT.warning("Item unavailability",
							"User specified Item: " + item
							+ " should be available", "Item: " + item
							+ " is not available so " + productFullName
							+ " will get added to cart ");

				}
				// if flexibility is no then return null and abort the script
				else if (flexibility.equalsIgnoreCase("No")) {
					RESULT.failed("Item unavalibality", "User specified Item: "
							+ item + " should be avaialble", "Item: " + item
							+ " is not available ");
					return null;
				}
				// if value not provide then go with the Yes
				else {
					RESULT
					.warning("Item unavailability",
							"Flexibility should be provided.",
					"Flexibility not provided. Considering flexibility Yes and moving ahead.");
					product = uiDriver.getwebDriverLocator(first_item);
					productFullName = product.getText();
					RESULT.warning("Item unavailability",
							"User specified Item: " + item
							+ " should be available", "Item: " + item
							+ " instead " + productFullName
							+ " is added to cart ");
				}
			}// end of show all catch
		}// end of first catch
		return product;
	}

	/*
	 * 
	 * @SuppressWarnings("deprecation")
	 * 
	 * /** FD_modifyCart Created By:Avani Thakkar Created Date:25th May,2015
	 * Modified By: Modified Date: Modify the cart
	 * 
	 * @param ItemName Give names of item you want to modify or delete separated
	 * by comma:Strawbarries,Apples,.... (Give name correctly with case
	 * sensitivity)
	 * 
	 * @param QuantityDelete Provide either quantity for items or "delete"
	 * particular item respectively provided item names: Delete,10,4,..
	 * otherwise if you want to delete all items give "Delete all"
	 */
	private enum ModifyCart {
		EMPTYCART, INCREASE, DECREASE, DELETE, UPDATE
	};

	//This function returns the quantity of specific item in cart(if it is displayed in textbbox) else it returns -1
		public double getCartItemQuantity(String lineItem) {
			double itemQty = 0.0;
			int initialLineItems = 0, finalLineItems = 0;
			List<WebElement> cartItemRows;
			String itemText;		
			try
			{
				waitForPageLoad();
				cartItemRows = webDriver.findElements(By.className(objMap
						.getLocator("lblcartItemRows")));
				for (int j = 0; j < cartItemRows.size(); j++) {
					itemText = cartItemRows.get(j).findElement(
							By.xpath(objMap.getLocator("lnkcartItemName"))).getText();
					if (itemText.equals(lineItem) && cartItemRows.get(j).findElements(By.className(objMap.getLocator("txtqty"))).size()>0) {
						itemQty += Double.parseDouble(cartItemRows.get(j).findElement(
								By.className(objMap.getLocator("txtqty")))
								.getAttribute("value"));
					}
					else if(itemText.equals(lineItem) && cartItemRows.get(j).findElements(By.className(objMap.getLocator("drpqty"))).size()>0){
						return -1;
					}
				}
			}
			catch(Exception e)
			{
				RESULT.failed("Get Item Quantity Exception", "Quantity should be successfully retrieved", "Unable to retrieve quantity");

			}
			return itemQty;
		}

		public boolean updateCartItemQuantity(String lineItem) {
			List<WebElement> cartItemRows;
			String itemText;

			try
			{
				cartItemRows = webDriver.findElements(By.className(objMap
						.getLocator("lblcartItemRows")));
				for (int j = 0; j < cartItemRows.size(); j++) {
					itemText = cartItemRows.get(j).findElement(
							By.xpath(objMap.getLocator("lnkcartItemName"))).getText();
					if (itemText.equals(lineItem) && cartItemRows.get(j).findElements(By.className(objMap.getLocator("drpqty"))).size()>0) {

						//Verify '+' or '-' buttons are not displayed for dropdown item qty
						if(cartItemRows.get(j).findElements(By.className(objMap.getLocator("btnqtyPlus"))).size()>0 && cartItemRows.get(j).findElement(By.className(objMap.getLocator("btnqtyPlus"))).isDisplayed()){
							RESULT.failed("'+' button presence check for item with qty dropdown", "'+' button should not be displayed","'+' button is displayed");
						}
						if(cartItemRows.get(j).findElements(By.className(objMap.getLocator("btnqtyMinus"))).size()>0 && cartItemRows.get(j).findElement(By.className(objMap.getLocator("btnqtyMinus"))).isDisplayed()){
							RESULT.failed("'-' button presence check for item with qty dropdown", "'-' button should not be displayed","'-' button is displayed");
						}

						//Select any other value in the dropdown					
						Select dropdown = new Select(cartItemRows.get(j).findElement(By.className(objMap.getLocator("drpqty"))));
						WebElement selectedOption=dropdown.getFirstSelectedOption();
						List<WebElement> options=dropdown.getOptions();
						for (WebElement option : options) {
							if(selectedOption.getText().equalsIgnoreCase(option.getText())){
								continue;
							}
							else{
								dropdown.selectByVisibleText(option.getText());
								return true;
							}
						}					

					}

				}
			}
			catch(Exception e)
			{
				RESULT.failed("Get Item Quantity Exception", "Quantity should be successfully retrieved", "Unable to retrieve quantity");

			}
			return false;
		}

		public boolean cartOperation(String lineItem, String quantity,
				String operation, String emptyCartAcceptDecline)
		{
			String ops = operation.toUpperCase();
			double initialQty = 0.0, finalQty = 0.0, qty = 0.0, qtyBefore, qtyAfter;
			int initialLineItems = 0, finalLineItems = 0;
			boolean found = false, search=false;
			List<WebElement> cartItems, cartItemRows;
			String itemText;

			try
			{
				switch (ModifyCart.valueOf(ops)) {
				case INCREASE: {
					// Getting initial quantities of that item
					initialQty = getCartItemQuantity(lineItem);

					//Check if the item qty is displayed in dropdown
					if(initialQty==-1){
						RESULT.warning("Product with qty in dropdown","Quantity increase operation should not be perform with qty in dropdown.","Hence performing update operation on item : "+lineItem);
						if(updateCartItemQuantity(lineItem)){
							RESULT.passed("Update operation on dropdown", "Different value should be selected", "Dropdown updation successful");
							return true;
						}
					}

					// Increasing the item quantities
					for (int i = 0; i < Integer.parseInt(quantity); i++) {
						// Reading all cart item rows
						try
						{
							cartItemRows = webDriver.findElements(By.className(objMap
									.getLocator("lblcartItemRows")));

							for (int j = 0; j < cartItemRows.size(); j++) {
								// Reading item name for one row
								try{
									itemText = cartItemRows.get(j).findElement(
											By.xpath(objMap.getLocator("lnkcartItemName")))
											.getText();


									if (itemText.equals(lineItem)) {
										if (cartItemRows.get(j).findElements(
												By.className(objMap.getLocator("btnqtyPlus")))
												.size() > 0) {
											// qtyBefore=cartItemRows.get(j).findElement(By.className("qty")).getAttribute("value");
											qtyBefore = getCartItemQuantity(lineItem);
											if (CompositeAppDriver.startUp.equalsIgnoreCase("IE"))
											{
												cartItemRows.get(j).findElement(
														By.className(objMap
																.getLocator("btnqtyPlus"))).sendKeys("\n");
											}
											else
											{
											cartItemRows.get(j).findElement(
													By.className(objMap
															.getLocator("btnqtyPlus"))).click();	
											}
											try {
												SleepUtils.getInstance().sleep(TimeSlab.YIELD);
												qtyAfter = getCartItemQuantity(lineItem);
												if (Double.compare(qtyBefore, qtyAfter) == 0) {
													RESULT.warning(
															"Modify cart - Increase Qty",
															"Quantity should be increased",
															lineItem
															+ " : quantity is no more increasing.");
													search=true;
													break;
												}
											} catch (Exception e) {
												RESULT.failed("Modify cart - Increase Qty",
														"Exception block caught",
														"Exception caught for product:"
														+ lineItem);
											}
											break;
										} else {
											RESULT.warning(
													"Modify cart - Increase Qty",
													lineItem
													+ ": Item name matched. '+' button should be available for : "
													+ itemText,
													"Quantity failed to incremented as '+' button is not found for item : "
													+ itemText);
											search=true;
											break;
										}
									}
								}
								catch(Exception e)
								{
									RESULT.warning("Cart Operation", "Item name should be available", "Item name is not available");
								}


							}//inner for loop
							if(search){
								break;
							}
						}
						catch(Exception e)
						{
							RESULT.warning("Cart Operation : Increase","Item rows should be available","Item rows is not available");
						}
					}//Outer for loop
					
					// Getting final quantities of that item
					finalQty = getCartItemQuantity(lineItem);
					// Compare results based on quantities
					// If given quantity increased in the application then pass
					if (Double.compare((finalQty - initialQty), Double
							.parseDouble(quantity)) == 0) {
						RESULT.passed("Modify cart - Increase Qty", lineItem
								+ " : Quantity should be incremented by " + quantity,
								"Quantity increased by " + (finalQty - initialQty));
						return true;
					}
					// if quantity is increased but only till certain limit because of
					// cart constraint then warn user
					else if (finalQty > initialQty) {
						RESULT.warning("Modify cart - Increase Qty", lineItem
								+ " : Quantity should be incremented by" + quantity,
								"Quantity increased by " + (finalQty - initialQty));
						return true;
					}
					// If quantity is not increased at all
					else {
						RESULT.failed("Modify cart - Increase Qty", lineItem
								+ " : Quantity should be incremented",
								"Failed to increase quantity. \nInitial qty:"
								+ initialQty + "\nFinal qty:" + finalQty);
						return false;
					}
				}

				case DECREASE: {

					// Getting initial quantities of that item
					initialQty = getCartItemQuantity(lineItem);

					//Check if the item qty is displayed in dropdown
					if(initialQty==-1){
						RESULT.warning("Product with qty in dropdown","Quantity decrease operation should not be perform with qty in dropdown.","Hence performing update operation on item : "+lineItem);
						if(updateCartItemQuantity(lineItem)){
							RESULT.passed("Update operation on dropdown", "Different value should be selected", "Dropdown updation successful");
							return true;
						}
					}

					// Decreasing the item quantities
					for (int i = 0; i < Integer.parseInt(quantity); i++) {
						// Reading all cart item rows
						try
						{
							cartItemRows = webDriver.findElements(By.className(objMap
									.getLocator("lblcartItemRows")));
							for (int j = cartItemRows.size() - 1; j >= 0; j--) {
								// Reading item name for one row
								try
								{
									itemText = cartItemRows.get(j).findElement(
											By.xpath(objMap.getLocator("lnkcartItemName")))
											.getText();
									if (itemText.equals(lineItem)) {
										if (cartItemRows.get(j).findElements(
												By.className(objMap.getLocator("btnqtyMinus")))
												.size() > 0) {
											// qtyBefore=cartItemRows.get(j).findElement(By.className("qty")).getAttribute("value");
											qtyBefore = getCartItemQuantity(lineItem);
											if (CompositeAppDriver.startUp.equalsIgnoreCase("IE"))
											{
												cartItemRows.get(j).findElement(
														By.className(objMap
																.getLocator("btnqtyMinus")))
																.sendKeys("\n");
											}
											else
											{
											cartItemRows.get(j).findElement(
													By.className(objMap
															.getLocator("btnqtyMinus")))
															.click();
											}
											try {
												SleepUtils.getInstance().sleep(TimeSlab.YIELD);
												qtyAfter = getCartItemQuantity(lineItem);
												if (Double.compare(qtyBefore, qtyAfter) == 0) {
													RESULT
													.warning(
															"Modify cart - Decrease Qty",
															"Quantity should be decreased",
															lineItem
															+ " : quantity is no more decreasing.");
													search=true;
													break;
												}
											} catch (Exception e) {
												RESULT.failed("Modify cart - Decrease Qty",
														"Exception block caught",
														"Exception caught : " + lineItem);
											}
											break;
										} else {
											RESULT
											.warning(
													"Modify cart - Decrease Qty",
													lineItem
													+ ": Item name matched. '-' button should be available for : "
													+ itemText,
													"Quantity failed to incremented as '-' button is not found for item : "
													+ itemText);
											search=true;
											break;
										}
									}
								}
								catch(Exception e)
								{
									RESULT.warning("Cart Operation", "Item name should be available", "Item name is not available");
								}

							}//inner for loop
							if(search){
								break;
							}
						}
						catch(Exception e)
						{
							RESULT.warning("Cart Operation : Decrease", "Item row should be available", "Item row is not available");
						}

					}
					// Getting final quantities of that item
					finalQty = getCartItemQuantity(lineItem);
					// Compare results based on quantities
					// If expected quantity decreased in the application then pass
					if (Double.compare((initialQty - finalQty), Double
							.parseDouble(quantity)) == 0) {
						RESULT.passed("Modify cart - Decrease Qty", lineItem
								+ " : Quantity should be decreased by " + quantity,
								"Quantity decreased by " + (initialQty - finalQty));
						return true;
					}
					// if quantity is decreased but only till certain limit because of
					// cart constraint then warn user
					else if (finalQty < initialQty) {
						RESULT.warning("Modify cart - Decrease Qty", lineItem
								+ " : Quantity should be decreased by" + quantity,
								"Quantity decreased by " + (initialQty - finalQty));
						return true;
					}
					// If quantity is not increased at all
					else {
						RESULT.failed("Modify cart - Decrease Qty", lineItem
								+ " : Quantity should be decreased",
								"Failed to decrease quantity. \nInitial qty:"
								+ initialQty + "\nFinal qty:" + finalQty);
						return false;
					}
				}
				case DELETE: {
					// Getting initial number of line items
					try
					{

						cartItemRows = webDriver.findElements(By.className(objMap
								.getLocator("lblcartItemRows")));
						for (int j = 0; j < cartItemRows.size(); j++) {
							// Reading item name for one row
							try
							{
								itemText = cartItemRows.get(j).findElement(
										By.xpath(objMap.getLocator("lnkcartItemName")))
										.getText();
								if (itemText.equals(lineItem)) {
									initialLineItems += 1;
								}
							}
							catch(Exception e)
							{
								RESULT.failed("Cart Operation :DELETE", "Item Text should be available",
										"Item text is available and the exception caught is "+e.getMessage());
							}

						}


						// Deleting a line item from cart
						for (int j = 0; j < cartItemRows.size(); j++) {
							// Reading item name for one row
							try
							{
								itemText = cartItemRows.get(j).findElement(
										By.xpath(objMap.getLocator("lnkcartItemName")))
										.getText();
								if (itemText.equals(lineItem)) {
									if (cartItemRows.get(j).findElements(
											By.partialLinkText("Delete")).size() > 0) {
										cartItemRows.get(j).findElement(
												By.partialLinkText("Delete")).click();
										try {
											SleepUtils.getInstance().sleep(TimeSlab.LOW);

										} catch (Exception e) {
											RESULT.failed("Modify cart - Delete item",
													"Exception block caught",
													"Exception caught for product:" + lineItem);
										}
										break;
									} else {
										RESULT
										.warning(
												"Modify cart - Delete item",
												lineItem
												+ ": Item name matched. 'Delete' link should be available for : "
												+ itemText,
												"Quantity failed to delete as 'Delete' link is not found for item : "
												+ itemText);
										break;
									}
								}
							}
							catch(Exception e)
							{

								RESULT.failed("Cart Operation :DELETE", "Item name should be available",
										"Item name is available and the exception caught is "+e.getMessage());
							}
						}
						// Getting number of line items after delete of a line item
						cartItemRows = webDriver.findElements(By.className(objMap
								.getLocator("lblcartItemRows")));
						for (int j = 0; j < cartItemRows.size(); j++) {
							// Reading item name for one row
							itemText = cartItemRows.get(j).findElement(
									By.xpath(objMap.getLocator("lnkcartItemName")))
									.getText();
							if (itemText.equals(lineItem)) {
								finalLineItems += 1;
							}
						}
					}
					catch(Exception e)
					{
						RESULT.failed("Cart Operation : DELETE", "Item row should be available",
								"Item row is successful and the exception caught is  "+e.getMessage());
					}
					// Check if one line item is deleted from cart
					if (initialLineItems == finalLineItems + 1) {
						RESULT.passed("Modify cart - Delete line item", lineItem
								+ " : should be deleted ", "Line item : " + lineItem
								+ " is deleted.");
						return true;
					} else {
						RESULT.failed("Modify cart - Delete line item", lineItem
								+ " : should be deleted ", "Line item : " + lineItem
								+ " is NOT deleted.");
						return false;
					}
				}

				case UPDATE: {
					// Getting initial quantities of that item
					initialQty = getCartItemQuantity(lineItem);

					//Check if the item qty is displayed in dropdown
					if(initialQty==-1){
						RESULT.warning("Product with qty in dropdown","Quantity update operation should be perform with qty in dropdown.","Hence performing update operation on item : "+lineItem);
						if(updateCartItemQuantity(lineItem)){
							RESULT.passed("Update operation on dropdown", "Different value should be selected", "Dropdown updation successful");
							return true;
						}
					}

					// Updating line item quantity
					try
					{
						cartItemRows = webDriver.findElements(By.className(objMap
								.getLocator("lblcartItemRows")));
						for (int j = 0; j < cartItemRows.size(); j++) {
							// Reading item name for one row
							try
							{
								itemText = cartItemRows.get(j).findElement(
										By.xpath(objMap.getLocator("lnkcartItemName")))
										.getText();
								if (itemText.equals(lineItem)) {
									// Checking if textbox is displayed for item quantity
									try {
										if (cartItemRows.get(j).findElements(
												By.className(objMap.getLocator("txtqty")))
												.size() > 0) {
											// Get quantity before updating
											qty = Double.parseDouble(cartItemRows.get(j)
													.findElement(
															By.className(objMap
																	.getLocator("txtqty")))
																	.getAttribute("value"));
											cartItemRows.get(j).findElement(
													By.className(objMap.getLocator("txtqty")))
													.click();
											// Pressing control + A keys to select all the
											// digits in quantity field
											String selectAll = Keys.chord(Keys.CONTROL, "a");
											cartItemRows.get(j).findElement(
													By.className(objMap.getLocator("txtqty")))
													.sendKeys(selectAll);
											cartItemRows.get(j).findElement(
													By.className(objMap.getLocator("txtqty")))
													.sendKeys(Keys.DELETE);
											cartItemRows.get(j).findElement(
													By.className(objMap.getLocator("txtqty")))
													.sendKeys(quantity);
											SleepUtils.getInstance().sleep(TimeSlab.LOW);
											break;
										}
										else {
											RESULT
											.failed("Modify cart - Update qty",
													"Item quantity should be updated",
											"Item qty is not updated as quantity textbox is not found!!");
											return false;
										}
									} catch (Exception e) {
										RESULT.failed("Modify cart - Update Qty",
												"Exception block caught",
												"Exception caught for product:" + lineItem);
										return false;
									}
								}
							}
							catch(Exception e)
							{
								RESULT.failed("Cart Operation : UPDATE","Item name should be available","Item name is unavailable");
							}

						}
					}
					catch(Exception e)
					{
						RESULT.failed("Cart Operation : UPDATE","Item row should be available..","Item row is unavailable");
					}

					// Getting final quantities of that item
					finalQty = getCartItemQuantity(lineItem);
					// Compare results based on quantities
					// If given quantity updated in the application then pass
					if (finalQty == initialQty - qty + Double.parseDouble(quantity)) {
						RESULT.passed("Modify cart - update Qty", lineItem
								+ " : Quantity should be updated to value :  "
								+ quantity, " Quantity updated for item :" + lineItem);
						return true;
					}
					// if quantity is updated but at certain limit because of cart
					// constraint then warn user
					else if (finalQty != initialQty) {
						RESULT.warning(
								"Modify cart - Update Qty",
								lineItem
								+ " : Quantity should be updated to value: "
								+ quantity,
								"Quantity updated and adjusted automatically. Total item quantity in cart for item : "
								+ lineItem + " is: " + finalQty);
						return true;
					} else {
						RESULT.failed("Modify cart - Update Qty",
								"Item quantity should be updated",
						"Item quantity is not updated");
						return false;
					}
				}

				case EMPTYCART: {
					// check if user wants to delete all items in cart
					WebElement EmptyAll;
					try
					{	
						try
						{
							EmptyAll = uiDriver.getwebDriverLocator(objMap
									.getLocator("lnkemptyAll"));
							// click on delete all
							EmptyAll.click();
						}
						catch(Exception e)
						{
							RESULT.failed("Cart Operation : EMPTY ALL","Empty All link should be available","Empty All link is not available");
							return false;
						}
						// popup for confirmation to delete all
						try
						{
							if (emptyCartAcceptDecline.equalsIgnoreCase("true")) {
								if(uiDriver.popup_isAlertPresent(10)){
									uiDriver.popup_ClickOkOnAlert();
								}

								/*  uiDriver .getwebDriverLocator(objMap.getLocator("btnupdate"
								  )) .click(); // update cart with changes made*/							 
								System.out.println(uiDriver.getwebDriverLocator(
										objMap.getLocator("txtSubTotal")).getText());
								if (uiDriver.getwebDriverLocator(
										objMap.getLocator("txtSubTotal")).getText().contains(
										"$0.00")) {
									RESULT.passed("Empty Cart",
											"All items available in order should get deleted",
									"All items in current order successfylly deleted");
									return true;
								} else {
									RESULT.failed("Empty Cart",
											"All items available in order should get deleted",
									"All items in cart could not get deleted");
									return false;
								}
							} else {
								uiDriver.popup_ClickcancelOnAlert();
								if (uiDriver.isElementpresentweb(EmptyAll)) {
									RESULT
									.passed(
											"Empty Cart",
											"All items available in order should not get deleted",
									"All items in current order has not been deleted");
									return true;
								} else {
									RESULT
									.failed(
											"Empty Cart",
											"All items available in order should not get deleted",
									"All items got deleted");
									return false;
								}
							}
						}
						catch(Exception e)
						{
							RESULT.failed("Cart Operation : EMPTY ALL", "Pop up confirmation for Delete should be available",
							"Pop up confirmation for Delete is not available");
						}

					}
					catch(Exception e)
					{
						RESULT.failed("Cart Operation : EMPTY ALL","Cart should be emptied successfully","Cart is not emptied successfully");
					}


				}
				default: {
					RESULT
					.failed(
							"Operation check",
							"Operation should one of these (INCREASE,DECREASE,DELETE,UPDATE,EMPTYCART)",
					"Correct operation not found");
					return false;
				}

				}
			}
			catch(Exception e)
			{
				RESULT.failed("Cart Operation Exception", "Cart operation should be successfully performed",
				"Cart operation is not performed");
				return false;
			}

		}

		public void FD_modifyCart(String itemName, String operation,
				String quantity, String emptyCartAcceptDecline,
				String flexibilityFlag) throws InterruptedException, FindFailed {

			try {
				boolean found = false;
				String lineItem, itemText;
				List<WebElement> cartItemRows, lineItems;
				// Getting the line items complete row
				// Getting only product names list from cart
				try
				{
					wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(objMap.getLocator("lnkcartItemNames"))));
					lineItems = webDriver.findElements(By.xpath(objMap
							.getLocator("lnkcartItemNames")));
				}
				catch(Exception e)
				{
					try{
						wait.until(ExpectedConditions.stalenessOf(webDriver.findElement(By.xpath(objMap.getLocator("lnkcartItemNames")))));
						lineItems = webDriver.findElements(By.xpath(objMap
								.getLocator("lnkcartItemNames")));
					}catch(Exception e1){
					RESULT.failed("Modify Cart", "Items should be available","No item is available");
					return;
					}
				}

				// Iterating through all the list elements
				for (int i = 0; i < lineItems.size(); i++) {
					if (lineItems.get(i).getText().contains(itemName)) {
						lineItem = lineItems.get(i).getText();
						found = cartOperation(lineItem, quantity, operation,
								emptyCartAcceptDecline);
						if (found) {
							break;
						}
					}
				}
				if (found == false
						&& (flexibilityFlag.equalsIgnoreCase("yes") || flexibilityFlag
								.isEmpty())) {
					RESULT.warning("Product availability check",
							"Product text should be availble in cart",
					"Product text not found. Hence performing operaion on any other product");
					// Getting all the product rows from cart
					cartItemRows = webDriver.findElements(By.className(objMap
							.getLocator("lblcartItemRows")));
					// Iterating through list for any other product based on
					// flexibility flag settings
					for (int i = 0; i < cartItemRows.size(); i++) {
						itemText = cartItemRows.get(i).findElement(
								By.xpath(objMap.getLocator("lnkcartItemName")))
								.getText();
						found = cartOperation(itemText, quantity, operation,
								emptyCartAcceptDecline);
						if (found) {
							RESULT.passed("Modify cart",
									"Modify cart performed on another product: "
									+ itemText,
									"Cart operation performed for: " + itemText);
							break;
						}
					}
				}
				if (found == false && flexibilityFlag.equalsIgnoreCase("no")) {
					RESULT
					.warning(
							"Modify cart",
							"Product match required and user should be flexible to perform operation on some other product",
					"Product match not found and flexibility flag is set to 'no'");
				}
			} catch (Exception e) {
				RESULT.failed("Modify Cart Exception", "Cart should be modified successfully",
						"Cart is not modified and exception caught is " + e.getMessage());
			}
		}

	public double getCartItemQuantityCRM(String LineItem) {
		double ItemQty = 0.0;
		List<WebElement> CartItemRows;
		String ItemText;
		try{
			CartItemRows = webDriver.findElements(By.xpath(objMap.getLocator("lblcartItemRowsCRM")));
			for (int j = 0; j < CartItemRows.size(); j++) {
				ItemText = CartItemRows.get(j).findElement(By.xpath(objMap.getLocator("lnkcartItemNameCRM"))).getText();
				if (ItemText.equals(LineItem)&& CartItemRows.get(j).findElements(By.className(objMap.getLocator("btnqtyPlus"))).size() > 0) {
					ItemQty += Double.parseDouble(CartItemRows.get(j).findElement(
							By.xpath(objMap.getLocator("txtqtyCRM")))
							.getAttribute("value"));
				}					
			}
		}
		catch(Exception e)
		{
			RESULT.failed("Get Item Quantity Exception", "Quantity should be successfully retrieved",
			"Unable to retrieve quantity");			
		}	
		return ItemQty;
	}

	public boolean cartOperationCRM(String LineItem, String Quantity,
			String Operation, String DeleteAllAcceptDecline)
	{
		String Ops = Operation.toUpperCase();
		double InitialQty = 0.0, FinalQty = 0.0, Qty = 0.0, QtyBefore, QtyAfter;
		int InitialLineItems = 0, FinalLineItems = 0;
		List<WebElement>  CartItemRows;
		String ItemText;

		try
		{
			switch (ModifyCart.valueOf(Ops)) {
			case INCREASE: {
				try {
					//Checking for customized product
					CartItemRows = webDriver.findElements(By.xpath(objMap.getLocator("lblcartItemRowsCRM")));
					for (int k = 0; k < CartItemRows.size(); k++) {
						// Reading item name for one row
						ItemText = CartItemRows.get(k).findElement(
								By.xpath(objMap.getLocator("lnkcartItemNameCRM")))
								.getText();
						if (ItemText.equals(LineItem)) {
							if((CartItemRows.get(k).findElements(By.className(objMap.getLocator("drpqtyCRM"))).size() > 0)){
								RESULT.warning("Customized product", "No Increase option should be available for Customized product :"+LineItem, 
										"No Increase option is available for Customized product :"+LineItem);
								return true;
							}
						}
					}
					// Getting initial quantities of that item
					InitialQty = getCartItemQuantityCRM(LineItem);

					// Increasing the item quantities
					outer: for (int i = 0; i < Integer.parseInt(Quantity); i++) {
						// Reading all cart item rows
						try
						{
							//CartItemRows = webDriver.findElements(By.xpath(objMap.getLocator("lblcartItemRowsCRM")));
							for (int j = 0; j < CartItemRows.size(); j++) {
								// Reading item name for one row
								try{
									ItemText = CartItemRows.get(j).findElement(
											By.xpath(objMap.getLocator("lnkcartItemNameCRM")))
											.getText();
									if (ItemText.equals(LineItem)) {
										if (CartItemRows.get(j).findElements(By.className(objMap.getLocator("btnqtyPlus"))).size() > 0) {
											QtyBefore = getCartItemQuantityCRM(LineItem);
											CartItemRows.get(j).findElement(By.className(objMap
													.getLocator("btnqtyPlus"))).click();
											try {
												Thread.sleep(2000);
												QtyAfter = getCartItemQuantityCRM(LineItem);
												if (Double.compare(QtyBefore, QtyAfter) == 0) {
													RESULT.warning("Modify cart - Increase Qty",
															"Quantity should be increased",
															LineItem+ " : quantity is not more increasing.");			
													break outer;
												}
											} catch (Exception e) {
												RESULT.failed("Modify cart - Increase Qty", "Exception block caught",
														"Exception caught for product:"+ LineItem);
											}
											break;
										} else {
											RESULT.warning(
													"Modify cart - Increase Qty",
													LineItem+ ": Item name matched. '+' button should be available for : "+ ItemText,
													"Quantity failed to incremented as '+' button is not found for item : "+ ItemText);
										}
									}
								}
								catch(Exception e){
									RESULT.warning("Cart Operation", "Item name should be available", "Item name is not available");
								}
							}
						}
						catch(Exception e){
							RESULT.warning("Cart Operation : Increase","Item rows should be available","Item rows is not available");
						}	
					}
					// Getting final quantities of that item
					FinalQty = getCartItemQuantityCRM(LineItem);
					// Compare results based on quantities
					// If given quantity increased in the application then pass
					if (Double.compare((FinalQty - InitialQty), Double
							.parseDouble(Quantity)) == 0) {
						uiDriver.click("btnupdate1CRM");
						uiDriver.waitForPageLoad();
						RESULT.passed("Modify cart - Increase Qty", LineItem
								+ " : Quantity should be incremented by " + Quantity,
								"Quantity increased by " + (FinalQty - InitialQty));
						return true;
					}
					// if quantity is increased but only till certain limit because of
					// cart constraint then warn user
					else if (FinalQty > InitialQty) {
						uiDriver.click("btnupdate1CRM");
						uiDriver.waitForPageLoad();
						RESULT.warning("Modify cart - Increase Qty", LineItem
								+ " : Quantity should be incremented by" + Quantity,
								"Quantity increased by " + (FinalQty - InitialQty));
						return true;
					}
					// If quantity is not increased at all
					else {
						RESULT.failed("Modify cart - Increase Qty", 
								LineItem+ " : Quantity should be incremented",
								"Failed to increase quantity. \nInitial qty:"+ InitialQty + "\nFinal qty:" + FinalQty);
						return false;
					}
				} catch (Exception e) {
					e.printStackTrace();
					RESULT.failed("Error occurred", "Increase Cart Operation should be successful", "Increase Cart Operation is failed due to :"+e);
					return false;
				}
			}

			case DECREASE: {
				try {
					//Checking for customized product
					CartItemRows = webDriver.findElements(By.xpath(objMap.getLocator("lblcartItemRowsCRM")));
					for (int k = 0; k < CartItemRows.size(); k++) {
						// Reading item name for one row
						ItemText = CartItemRows.get(k).findElement(
								By.xpath(objMap.getLocator("lnkcartItemNameCRM")))
								.getText();
						if (ItemText.equals(LineItem)) {
							if((CartItemRows.get(k).findElements(By.className(objMap.getLocator("drpqtyCRM"))).size() > 0)){
								RESULT.warning("Customized product", "No Decrease option should be available for Customized product :"+LineItem, 
										"No Decrease option is available for Customized product :"+LineItem);
								return true;
							}
						}
					}
					// Getting initial quantities of that item
					InitialQty = getCartItemQuantityCRM(LineItem);

					// Decreasing the item quantities
					outer: for (int i = 0; i < Integer.parseInt(Quantity); i++) {
						// Reading all cart item rows
						try{
							//CartItemRows = webDriver.findElements(By.xpath(objMap.getLocator("lblcartItemRowsCRM")));
							for (int j = CartItemRows.size() - 1; j >= 0; j--) {
								// Reading item name for one row
								try{
									ItemText = CartItemRows.get(j).findElement(By.xpath(objMap.getLocator("lnkcartItemNameCRM"))).getText();
									if (ItemText.equals(LineItem)) {
										if (CartItemRows.get(j).findElements(By.className(objMap.getLocator("btnqtyMinus"))).size() > 0) {
											// qtyBefore=cartItemRows.get(j).findElement(By.className("qty")).getAttribute("value");
											QtyBefore = getCartItemQuantityCRM(LineItem);
											CartItemRows.get(j).findElement(By.className(objMap.getLocator("btnqtyMinus"))).click();
											try {
												Thread.sleep(2000);
												QtyAfter = getCartItemQuantityCRM(LineItem);
												if (Double.compare(QtyBefore, QtyAfter) == 0) {
													RESULT.warning("Modify cart - Decrease Qty",
															"Quantity should be decreased",
															LineItem+ " : quantity is not more decreasing.");
													break outer;
												}
											} catch (Exception e) {
												RESULT.failed("Modify cart - Decrease Qty",
														"Exception block caught",
														"Exception caught : " + LineItem);
											}
											break;
										} else {
											RESULT.warning("Modify cart - Decrease Qty",
													LineItem+ ": Item name matched. '-' button should be available for : "+ ItemText,
													"Quantity failed to incremented as '-' button is not found for item : "+ ItemText);
										}
									}
								}
								catch(Exception e){
									RESULT.warning("Cart Operation", "Item name should be available", "Item name is not available");
								}
							}
						}
						catch(Exception e){
							RESULT.warning("Cart Operation : Decrease", "Item row should be available", "Item rpw is not available");
						}	
					}
					// Getting final quantities of that item
					FinalQty = getCartItemQuantityCRM(LineItem);
					// Compare results based on quantities
					// If expected quantity decreased in the application then pass
					if (Double.compare((InitialQty - FinalQty), Double
							.parseDouble(Quantity)) == 0) {
						uiDriver.click("btnupdate1CRM");
						uiDriver.waitForPageLoad();
						RESULT.passed("Modify cart - Decrease Qty", LineItem
								+ " : Quantity should be decreased by " + Quantity,
								"Quantity decreased by " + (InitialQty - FinalQty));
						return true;
					}
					// if quantity is decreased but only till certain limit because of
					// cart constraint then warn user
					else if (FinalQty < InitialQty) {
						uiDriver.click("btnupdate1CRM");
						uiDriver.waitForPageLoad();
						RESULT.warning("Modify cart - Decrease Qty", LineItem
								+ " : Quantity should be decreased by" + Quantity,
								"Quantity decreased by " + (InitialQty - FinalQty));
						return true;
					}
					// If quantity is not increased at all
					else {
						RESULT.failed("Modify cart - Decrease Qty", LineItem
								+ " : Quantity should be decreased",
								"Failed to decrease quantity. \nInitial qty:"
								+ InitialQty + "\nFinal qty:" + FinalQty);
						return false;
					}
				} catch (Exception e) {
					e.printStackTrace();
					RESULT.failed("Error occurred", "Decrease Cart Operation should be successful", "Decrease Cart Operation is failed due to :"+e);
					return false;
				}
			}
			case DELETE: {
				// Getting initial number of line items
				try{
					try{				
					CartItemRows = webDriver.findElements(By.xpath(objMap.getLocator("lblcartItemRowsCRM")));
					for (int k = 0; k < CartItemRows.size(); k++) {
						// Reading item name for one row
						try
						{
							ItemText = CartItemRows.get(k).findElement(
									By.xpath(objMap.getLocator("lnkcartItemNameCRM")))
									.getText();
							if (ItemText.equals(LineItem)) {
								InitialLineItems += 1;
							}
						}
						catch(Exception e){
							RESULT.failed("Cart Operation :DELETE", "Item Text should be available",
									"Item text is available and the exception caught is "+e.getMessage());
						}					
					}
					// Deleting a line item from cart
					for (int j = 0; j < CartItemRows.size(); j++) {
						// Reading item name for one row
						try{
							ItemText = CartItemRows.get(j).findElement(By.xpath(objMap.getLocator("lnkcartItemNameCRM"))).getText();
							if (ItemText.equals(LineItem)) {
								if (CartItemRows.get(j).findElements(By.partialLinkText("Delete")).size() > 0) {
									CartItemRows.get(j).findElement(By.partialLinkText("Delete")).click();
									uiDriver.waitForPageLoad();
									break;
								} else {
									RESULT.warning("Modify cart - Delete item",
											LineItem+ ": Item name matched. 'Delete' link should be available for : "+ ItemText,
											"Quantity failed to incremented as 'Delete' link is not found for item : "+ ItemText);
								}
							}
						}
						catch(Exception e){
							RESULT.failed("Cart Operation :DELETE", "Item name should be available",
									"Item name is available and the exception caught is "+e.getMessage());
						}
					}
					// Getting number of line items after delete of a line item
					CartItemRows = webDriver.findElements(By.xpath(objMap.getLocator("lblcartItemRowsCRM")));
					for (int j = 0; j < CartItemRows.size(); j++) {
						// Reading item name for one row
						ItemText = CartItemRows.get(j).findElement(By.xpath(objMap.getLocator("lnkcartItemNameCRM"))).getText();
						if (ItemText.equals(LineItem)) {
							FinalLineItems += 1;
						}
					}
				}
				catch(Exception e){
					RESULT.failed("Cart Operation : DELETE", "Item row should be available",
							"Item row is successful and the exception caught is  "+e.getMessage());
				}
				// Check if one line item is deleted from cart
				if (InitialLineItems == FinalLineItems + 1) {
					RESULT.passed("Modify cart - Delete line item", 
							LineItem+ " : should be deleted ", 
							"Line item : " + LineItem + " is deleted.");
					return true;
				} else {
					RESULT.failed("Modify cart - Delete line item", 
							LineItem + " : should be deleted ", 
							"Line item : " + LineItem+ " is NOT deleted.");
					return false;
				}
				} catch (Exception e) {
					e.printStackTrace();
					RESULT.failed("Error occurred", "Delete Cart Operation should be successful", "Delete Cart Operation is failed due to :"+e);
					return false;
				}
			}

			case UPDATE: {
				try{
					try{
						CartItemRows = webDriver.findElements(By.xpath(objMap.getLocator("lblcartItemRowsCRM")));
						for (int j = 0; j < CartItemRows.size(); j++) {
							// Reading item name for one row
							try{
								ItemText = CartItemRows.get(j).findElement(By.xpath(objMap.getLocator("lnkcartItemNameCRM"))).getText();
								if (ItemText.equals(LineItem)) {
									// Checking if textbox is displayed for item quantity
									try {
										if (CartItemRows.get(j).findElements(By.className(objMap.getLocator("txtqty"))).size() > 0) {
											// Getting initial quantities of that item
											InitialQty = getCartItemQuantityCRM(LineItem);
											// Get quantity before updating
											Qty = Double.parseDouble(CartItemRows.get(j).findElement(By.className
													(objMap.getLocator("txtqty"))).getAttribute("value"));
											CartItemRows.get(j).findElement(By.className(objMap.getLocator("txtqty"))).click();
											// Pressing control + A keys to select all the
											// digits in quantity field
											String SelectAll = Keys.chord(Keys.CONTROL, "a");
											CartItemRows.get(j).findElement(By.className(objMap.getLocator("txtqty"))).sendKeys(SelectAll);
											if (CompositeAppDriver.startUp.equalsIgnoreCase("IE")){
												CartItemRows.get(j).findElement(By.className(objMap.getLocator("txtqty"))).sendKeys(Keys.ARROW_LEFT);
												CartItemRows.get(j).findElement(By.className(objMap.getLocator("txtqty"))).sendKeys(Keys.ARROW_LEFT);
											}			
											CartItemRows.get(j).findElement(By.className(objMap.getLocator("txtqty"))).sendKeys(Keys.DELETE);
											CartItemRows.get(j).findElement(By.className(objMap.getLocator("txtqty"))).sendKeys(Quantity);
											Thread.sleep(2000);
											break;
										}
										// Checking if dropdown is displayed for item quantity
										else if ((CartItemRows.get(j).findElements(By.className(objMap.getLocator("drpqtyCRM"))).size() > 0)) {
											//Select any other value in the dropdown					
											Select dropdown = new Select(CartItemRows.get(j).findElement(By.className(objMap.getLocator("drpqtyCRM"))));
											WebElement selectedOption=dropdown.getFirstSelectedOption();
											System.out.println("Current selected option is :"+selectedOption.getText());
											List<WebElement> options=dropdown.getOptions();
											for (WebElement option : options) {
												if(selectedOption.getText().equalsIgnoreCase(option.getText())){
													continue;
												}
												else{dropdown.selectByIndex(2);
													WebElement updatedOption=dropdown.getFirstSelectedOption();
													System.out.println("Current selected option is :"+updatedOption.getText());
													if(selectedOption.getText().equalsIgnoreCase(updatedOption.getText())){
														dropdown.selectByIndex(3);
														WebElement updatedOption1=dropdown.getFirstSelectedOption();
														System.out.println("Current selected option is :"+updatedOption1.getText());
													}
													uiDriver.click("btnupdate1CRM");
													uiDriver.waitForPageLoad();
													RESULT.passed("Modify cart - Update qty","Item quantity should be updated","Item qty updated in qty dropdown");
													return true;
												}
											}
										} else {
											RESULT.failed("Modify cart - Update qty",
													"Item quantity should be updated",
											"Item qty is not updated as quantity textbox/dropdown is not found!!");
											return false;
										}
									} catch (Exception e) {
										RESULT.failed("Modify cart - Update Qty",
												"Exception block caught",
												"Exception caught for product:" + LineItem);
									}
								}
							}
							catch(Exception e){
								RESULT.failed("Cart Operation : UPDATE","Item name should be available","Item name is unavailable");
							}					
						}
					}
					catch(Exception e){
						RESULT.failed("Cart Operation : UPDATE","Item row should be available","Item row is unavailable");
					}			
					// Getting final quantities of that item
					FinalQty = getCartItemQuantityCRM(LineItem);
					// Compare results based on quantities
					// If given quantity updated in the application then pass
					if (FinalQty == InitialQty - Qty + Double.parseDouble(Quantity)) {
						uiDriver.click("btnupdate1CRM");
						uiDriver.waitForPageLoad();
						RESULT.passed("Modify cart - update Qty", 
								LineItem+ " : Quantity should be updated to value :  "+ Quantity, 
								" Quantity updated for item :" + LineItem);
						return true;
					}
					// if quantity is updated but at certain limit because of cart
					// constraint then warn user
					else if (FinalQty != InitialQty) {
						uiDriver.click("btnupdate1CRM");
						uiDriver.waitForPageLoad();
						RESULT.warning("Modify cart - Update Qty",
								LineItem+ " : Quantity should be updated to value: "+ Quantity,
								"Quantity updated and adjusted automatically. Total item quantity in cart for item : "+ LineItem + " is: " + FinalQty);
						return true;
					} else {
						RESULT.failed("Modify cart - Update Qty",
								"Item quantity should be updated",
						"Item quantity is not updated");
						return false;
					}
				} catch (Exception e) {
					e.printStackTrace();
					RESULT.failed("Error occurred", "Update Cart Operation should be successful", "Update Cart Operation is failed due to :"+e);
					return false;
				}
			}

			case EMPTYCART: {
				// check if user wants to delete all items in cart
				WebElement DeleteAll;
				try{	
					try{
						DeleteAll = uiDriver.getwebDriverLocator(objMap.getLocator("lnkdeleteAllCRM"));
						// click on delete all
						DeleteAll.click();
					}
					catch(Exception e){
						RESULT.failed("Cart Operation : EMPTY ALL","Delete All link should be available","Delete All link is not available");
						return false;
					}
					// popup for confirmation to delete all
					if (DeleteAllAcceptDecline.equalsIgnoreCase("true")) {
						if(uiDriver.popup_isAlertPresent(10)){
							uiDriver.popup_ClickOkOnAlert();
						}
						uiDriver.waitForPageLoad();
						System.out.println(uiDriver.getwebDriverLocator(objMap.getLocator("txtSubTotalCRM")).getText());
						if (uiDriver.getwebDriverLocator(objMap.getLocator("txtSubTotalCRM")).getText().contains("$0.00")) {
							RESULT.passed("Empty Cart",
									"All items available in order should get deleted",
							"All items in current order successfylly deleted");
							return true;
						} else {
							RESULT.failed("Empty Cart",
									"All items available in order should get deleted",
							"All items in cart could not get deleted");
							return false;
						}
					} else {
						if (DeleteAllAcceptDecline.equalsIgnoreCase("true")) {
							uiDriver.popup_ClickcancelOnAlert();
						}
						uiDriver.waitForPageLoad();
						if (uiDriver.isElementpresentweb(DeleteAll)) {
							RESULT.passed("Empty Cart",
									"All items available in order should not get deleted",
							"All items in current order has not been deleted");
							return true;
						} else {
							RESULT.failed("Empty Cart",
									"All items available in order should not get deleted",
							"All items got deleted");
							return false;
						}
					}
				}
				catch(Exception e){
					RESULT.failed("Cart Operation : EMPTY ALL","Cart should be emptied successfully","Cart is not emptied successfully");
					return false;
				}			
			}
			default: {
				RESULT.failed("Operation check",
						"Operation should one of these (INCREASE,DECREASE,DELETE,UPDATE,EMPTYCART)",
				"Correct operation not found");
				return false;
			}
			}
		}
		catch(Exception e){
			RESULT.failed("Cart Operation Exception", "Cart operation should be successfully performed",
			"Cart operation is not performed");
			return false;
		}		
	}

	public void FD_modifyCartCRM(String ItemName, String Operation,
			String Quantity, String DeleteAllAcceptDecline,
			String FlexibilityFlag) throws InterruptedException, FindFailed {

		try {
			boolean found = false;
			String LineItem, ItemText;
			List<WebElement> CartItemRows, LineItems;
			// Getting the line items complete row
			// Getting only product names list from cart
			try{
				wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(objMap.getLocator("lnkcartItemNamesCRM"))));
				LineItems = webDriver.findElements(By.xpath(objMap
						.getLocator("lnkcartItemNamesCRM")));
			}
			catch(Exception e){
				RESULT.failed("Modify Cart", "Items should be available","No item is available or user is on incorrect page");
				return;
			}
			// Iterating through all the list elements
			for (int i = 0; i < LineItems.size(); i++) {
				if (LineItems.get(i).getText().contains(ItemName)) {
					LineItem = LineItems.get(i).getText();
					found = cartOperationCRM(LineItem, Quantity, Operation,
							DeleteAllAcceptDecline);
					if (found) {
						break;
					}
				}
			}
			if (found == false && (FlexibilityFlag.equalsIgnoreCase("yes") || FlexibilityFlag.isEmpty())) {
				RESULT.warning("Product availability check",
						"Product text should be availble in cart",
				"Product text not found. Hence performing operaion on any other product");
				// Getting all the product rows from cart
				CartItemRows = webDriver.findElements(By.xpath(objMap.getLocator("lblcartItemRowsCRM")));
				// Iterating through list for any other product based on
				// flexibility flag settings
				for (int i = 0; i < CartItemRows.size(); i++) {
					ItemText = CartItemRows.get(i).findElement(By.xpath(objMap.getLocator("lnkcartItemNameCRM"))).getText();
					found = cartOperationCRM(ItemText, Quantity, Operation,
							DeleteAllAcceptDecline);
					if (found) {
						RESULT.passed("Modify cart",
								"Modify cart performed on another product: "+ ItemText,
								"Cart operation performed for: " + ItemText);
						break;
					}
				}
			}
			if (found == false && FlexibilityFlag.equalsIgnoreCase("no")) {
				RESULT.passed("Modify cart",
						"Product match required and user should be flexible to perform operation on some other product",
				"Product match not found and flexibility flag is set to 'no'");
			}
		} catch (Exception e) {
			RESULT.failed("Modify Cart CRM", "Cart should be modified successfully",
					"Cart is not modified and exception caught is " + e.getMessage());
		}
	}
	
	/**
	 * Function Name: FD_cancelOrder Purpose: To cancel the placed order Created
	 * By: Avani Thakkar Created Date: 27th May,2015 Modified By: Modified Date:
	 * 
	 * @param OrderNo
	 *            Provide order number need to cancel from list of placed orders
	 * @return void
	 */

	public void FD_cancelOrder(String OrderNo) throws InterruptedException,
	FindFailed {
		try {
			// checking particular order nuumber is present
			if (webDriver.findElements(By.linkText(OrderNo)).size()>0 && 
					uiDriver.isElementPresent(By.linkText(OrderNo))) {
				// click on order number
				webDriver.findElement(By.linkText(OrderNo)).click();
				uiDriver.waitForPageLoad();
				//	SleepUtils.getInstance().sleep(TimeSlab.YIELD);
				String status;
				try
				{
					status = uiDriver.getwebDriverLocator(
							objMap.getLocator("strsubmitted")).getText();
					System.out.println("status:" + status);
				}
				catch(Exception e)
				{
					RESULT.failed("Cancel order", " Order status should be available", "Order status is not available and exception caught is "+e.getMessage());
					return;
				}

				// check if status is submitted or other than submitted
				if (status.contains("Submitted")) {

					try
					{	// click on cancel button and confirm cancellation
						uiDriver.getwebDriverLocator(
								objMap.getLocator("btncancelOrd")).click();
						wait.until(ExpectedConditions.visibilityOfElementLocated(By
								.xpath(objMap.getLocator("btncancelConfrm"))));
						uiDriver.click("btncancelConfrm");
					}
					catch(Exception e)
					{
						RESULT.warning("Cancel order", "Order cnacellation should be confirmed", 
								"Order cancellation is not confirmed and exception caught is "+e.getMessage());
					}
					uiDriver.waitForPageLoad();
					//					SleepUtils.getInstance().sleep(TimeSlab.MEDIUM);
					if (webDriver.findElements(By.xpath(objMap.getLocator("strcancelOrder")))
							.size() > 0) {
						String orderStatus = uiDriver.getwebDriverLocator(
								objMap.getLocator("strcancelOrder")).getText();
						// check order status after cancellation
						if ((orderStatus.contains("Cancelled"))
								&& (orderStatus.contains(OrderNo))) {
							RESULT.passed("Cancel order",
									"Order should get cancelled successfully",
									"Desired order cancelled successfully: Order #"
									+ OrderNo);
						} else {
							RESULT.failed("Cancel order",
									"Order should get cancelled successfully",
									"Desired order could not get cancelled: Order #"
									+ OrderNo);
							return;
						}

					} else {
						RESULT.warning(
								"Cancel order",
								"Order should get cancelled",
								"You have crossed thresold time to cancel the order so you can not cancel this Order #"
								+ OrderNo);
						return;
					}
				} else if (status.contains("Cancelled")) {
					RESULT.passed("Cancel order",
							"Order should get cancelled successfully",
							"Desired order already  cancelled: Order #"
							+ OrderNo);
					if(webDriver.findElements(By.linkText(objMap.getLocator("btncancelOrd"))).size() > 0 
							&& webDriver.findElements(By.linkText(objMap.getLocator("btnmodifyOrd"))).size() > 0)
					{
						RESULT.failed(
								"Modify order button and cancel order button",
								"Modify order button and cancel order button should not be present for Order status"+ status,
								"Modify order button and cancel order button are  present for Order status"+ status);
						return;

					}else{
						RESULT.passed(
								"Modify order button and cancel order button",
								"Modify order button and cancel order button should not be present for Order status"+ status,
								"Modify order button and cancel order button are not present for Order status"+ status);

					}
				} else {
					RESULT.warning(
							"Cancel order",
							"Order should get cancelled successfully",
							"Desired order is in other than submitted Status,so it could not be cancelled: Order #"
							+ OrderNo);
					if(webDriver.findElements(By.linkText(objMap.getLocator("btncancelOrd"))).size() > 0 
							&& webDriver.findElements(By.linkText(objMap.getLocator("btnmodifyOrd"))).size() > 0)
					{
						RESULT.failed(
								"Modify order button and cancel order button",
								"Modify order button and cancel order button should not be present for Order status"+ status,
								"Modify order button and cancel order button are  present for Order status"+ status);
						return;

					}else{
						RESULT.passed(
								"Modify order button and cancel order button",
								"Modify order button and cancel order button should not be present for Order status"+ status,
								"Modify order button and cancel order button are not present for Order status"+ status);

					}

				}
			} else {
				RESULT.failed("Cancel order",
						"Order should get cancelled successfully",
						"Desired order is not present in your order list: Order #"
						+ OrderNo);
				return;
			}

		} catch (Exception e) {
			RESULT.failed("Cancel Order Exception", "Order should be cancelled successfully", 
					"Order is not cancelled and exception caught is "+e.getMessage());
		}
	}

	/**
	 * Function Name: FD_modifyOrder Purpose: To Modify the placed order Created
	 * By: Avani Thakkar Created Date: 27th May,2015 Modified By: Modified Date:
	 * 
	 *@param OrderNo
	 *            Provide order number need to modify or cancel from list of
	 *            placed orders
	 *@param ItemName
	 *            Give names of item you want to modify or delete separated by
	 *            comma:Strawbarries,Apples,.... (Give name correctly with case
	 *            sensitivity)
	 *@param QuantityDelete
	 *            Provide either quantity for items or "delete" particular item
	 *            respectively provided item names: Delete,10,4,.. otherwise if
	 *            you want to delete all items give "Delete all"
	 *@param UpdateCancel
	 *            Either Update the cart with given modification or cancel the
	 *            Updation:Cancel,Update
	 * @return void
	 */

	public void FD_modifyOrder(String OrderNo, String ItemName,
			String Quantity, String Operation, String EmptyCartAcceptDecline,
			String FlexibilityFlag) throws InterruptedException, FindFailed {
		try {
			if (webDriver.findElements(By.linkText(OrderNo)).size()>0 && 
					uiDriver.isElementPresent(By.linkText(OrderNo))) {
				// click on order number
				webDriver.findElement(By.linkText(OrderNo)).click();
				// check if modify order button is visible
				// uiDriver.isDisplayed("btnmodOrd");
				waitForPageLoad();
				//				SleepUtils.getInstance().sleep(TimeSlab.YIELD);
				String orderStatus;
				try
				{
					orderStatus = uiDriver.getwebDriverLocator(
							objMap.getLocator("strsubmitted")).getText();
				}
				catch(Exception e)
				{
					RESULT.failed("Modify Order ", "Order status should be available", 
							"Order status is not available and excepion caught is "+e.getMessage());
					return;
				}

				System.out.println("status:" + orderStatus);

				// check if status is submitted or other than submitted
				if (orderStatus.contains("Submitted")) {

					// modify the placed order
					String Operate = Operation.toUpperCase();
					try
					{
						uiDriver.getwebDriverLocator(
								objMap.getLocator("btnmodifyOrd")).click();
						wait.until(ExpectedConditions.visibilityOfElementLocated(By
								.className(objMap.getLocator("txtyourCart"))));
					}
					catch(Exception e)
					{
						RESULT.failed("Modify Order", "Order should be available to modify",
						"Order is not available to modify");
						return;
					}

					// click on modify order button wait for page to load
					// while (!uiDriver.isDisplayed("txtyourCart")) {
					// while
					// (!webDriver.findElement(By.className("cartheader__title")).getText().contains("Your Cart"))
					// {
					// selenium.waitForPageToLoad(PageLoadTime);
					// }

					// check if user want to update cart with modification or
					// cancel updates
					// Item names and values for Operation would be comma
					// separated so split those values and call modify_cart
					// function
					String Items[] = ItemName.split(":");
					String Qty[] = Quantity.split(":");
					String oper[] = Operate.split(":");
					for (int i = 0; i < oper.length; i++) {
						if (oper[i].contains("INCREASE")|| oper[i].contains("DECREASE")
								|| oper[i].contains("DELETE")|| oper[i].contains("UPDATE")
								|| oper[i].contains("EMPTYCART")) {
							uiDriver.FD_modifyCart(Items[i], oper[i], Qty[i],
									EmptyCartAcceptDecline, FlexibilityFlag);
						}
						else if (oper[i].contains("CANCEL")) {
							try
							{
								if (uiDriver.isDisplayed("btncancelChanges")) {
									uiDriver.getwebDriverLocator(
											objMap.getLocator("btncancelChanges"))
											.click();
									RESULT.passed(
											"Cancel modification",
											"Cart updation should get cancelled successfully.No modifications should be applied",
									"Cart updation cancelled successfully. No modifications applied");
								} else {
									RESULT.failed(
											"Cancel modification",
											"Cancel changes button should be displayed",
									"Cancel changes button is not displayed");
								}
							}
							catch(Exception e)
							{
								RESULT.failed("Modify Order ","Cancel changes button should be visible and clicked",
								"Cancel changes button is not visible or clicked");
							}

						} else if (!oper[i].contains("INCREASE")
								&& !oper[i].contains("DECREASE")
								&& !oper[i].contains("DELETE")
								&& !oper[i].contains("UPDATE")
								&& !oper[i].contains("EMPTYCART")
								&& !oper[i].contains("CANCEL")) {
							RESULT.failed(
									"Invalid argument for 'Operation'",
									"Given argument should be INCREASE, DECREASE, DELETE, UPDATE, CANCEL or EMPTYCART",
							"Given argument is other than Operation Arguments. Give correct argument");
						}
					}
				} else {
					RESULT.failed("Modify Order",
							"Order should be modified as per specified by user",
							"Order can not be modified in status: "
							+ orderStatus
							+ " which is other than submitted");
					//					if(webDriver.findElements(By.linkText(objMap.getLocator("btncancelOrd"))).size() > 0 
					//							&& webDriver.findElements(By.linkText(objMap.getLocator("btnmodifyOrd"))).size() > 0)
					//					{
					//						RESULT.failed(
					//							"Modify order button and cancel order button",
					//							"Modify order button and cancel order button should not be present for Order status"+ orderStatus,
					//							"Modify order button and cancel order button are  present for Order status"+ orderStatus);
					//			               return;
					//						
					//					}else{
					//						RESULT.passed(
					//								"Modify order button and cancel order button",
					//								"Modify order button and cancel order button should not be present for Order status"+ orderStatus,
					//								"Modify order button and cancel order button are not present for Order status"+ orderStatus);
					//				              
					//					}
					return;
				}

			} else {
				RESULT.warning("Modify Order : " + OrderNo,
						"Order number should be available",
				"Order number is not available");
			}
		} catch (Exception e) {
			RESULT.failed("Modify Order Exception", "Order should be modified",
					"Order is not modified and exception caught is " + e.getMessage());
		}
	}

	public void FD_cancelOrderCRM(String OrderNo, String EmailNotification,
			String Reason, String Notes) throws InterruptedException,
			FindFailed {
		try {
			uiDriver.click("lnkorders");
			uiDriver.waitForPageLoad();
			//SleepUtils.getInstance().sleep(TimeSlab.YIELD);
			if (uiDriver.isElementPresent(By.linkText(OrderNo))) {
				// click on order number
				webDriver.findElement(By.linkText(OrderNo)).click();
				try{
					wait.until(ExpectedConditions.visibilityOfElementLocated(By
						.xpath(objMap.getLocator("txtorderStatusCRM"))));
				}catch(Exception e){
					RESULT.failed("Cancel Order CRM", "Navigation to Order page should be successfully",
							"Navigation to Order page failed due to " + e.getMessage());
					return;
				}
				String OrderStatus = webDriver.findElement(
						By.xpath(objMap.getLocator("txtorderStatusCRM")))
						.getText();
				System.out.println("status:" + OrderStatus);
				if (OrderStatus.equalsIgnoreCase("submitted")) {
					uiDriver.click("lnkcancelOrderCRM");
					waitForPageLoad();
					if (EmailNotification.equalsIgnoreCase("yes")) {
						uiDriver.click("chkemailNotification");
//						waitForPageLoad();
					}
					uiDriver.setValue("drpreason", Reason);
					uiDriver.setValue("txtnotes", Notes);
					try{
					uiDriver.click("btncancelOrderCRM");
//					waitForPageLoad();
					}catch (Exception e) {
						RESULT.failed("Cancel order", "Cancel order button should be visible", "Cancel order button not found");
					}
					if(uiDriver.popup_isAlertPresent(10)){
						uiDriver.popup_ClickOkOnAlert();
					}
					uiDriver.waitForPageLoad();
					//SleepUtils.getInstance().sleep(TimeSlab.MEDIUM);
					OrderStatus = webDriver.findElement(
							By.xpath(objMap.getLocator("txtorderStatusCRM")))
							.getText();
					
					if (OrderStatus.contains("Cancel")) {
						RESULT.passed("Cancel Order CRM",
								"Order should get cancelled successfully",
								"Desired order cancelled successfully: Order #"
								+ OrderNo);
					} else {
						RESULT.failed("Cancel Order CRM",
								"Order should get cancelled successfully",
								"Desired order could not get cancelled: Order #"
								+ OrderNo+" in status: "+OrderStatus);
						return;
					}

				} else if (OrderStatus.equalsIgnoreCase("cancelled")) {
					RESULT.passed("Cancel Order CRM",
							"Order should get cancelled",
							"Desired order already  cancelled: Order #"
							+ OrderNo);

				} else {
					RESULT
					.failed(
							"Cancel Order CRM",
							"Order should get cancelled successfully",
							"Desired order is in other than submitted Status,so it could not cancelled: Order #"
							+ OrderNo);
					return;
				}
			} else {
				RESULT.warning("Cancel Order CRM",
						"Order should get cancelled successfully",
						"Desired order is not present in your order list: Order #"
						+ OrderNo);
				return;
			}

		} catch (Exception e) {
			RESULT.failed("Cancel Order CRM function", "Order should get canceled successfully",
					"Order is not canceled due to " + e.getMessage());
		}
	}

	public void FD_modifyOrderCRM(String OrderNo, String ItemName,String Quantity, String Operation, String DeleteAllAcceptDecline, 
			String FlexibilityFlag) throws InterruptedException,
			FindFailed {
		try{
			uiDriver.click("lnkorders");
			uiDriver.waitForPageLoad();
			if (uiDriver.isElementPresent(By.linkText(OrderNo))){
				// click on order number
				webDriver.findElement(By.linkText(OrderNo)).click();
				try{
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(objMap.getLocator("txtorderStatusCRM"))));
				}catch(Exception e){
					RESULT.failed("Modify Order CRM", "Navigation to Order page should be successfully",
							"Navigation to Order page failed due to " + e.getMessage());
					return;
				}
				CRMWindow = webDriver.getWindowHandle();
				String OrderStatus=webDriver.findElement(By.xpath(objMap.getLocator("txtorderStatusCRM"))).getText();
				System.out.println(OrderStatus+": Status");
				if(OrderStatus.equalsIgnoreCase("submitted")){
//					uiDriver.click("lnkmodifyOrder");
					String winHandleBefore=webDriver.getWindowHandle();
					webDriver.findElement(By.linkText(objMap.getLocator("lnkmodifyOrder"))).click();
					//close CRM tab after navigating to storefront
					try{
						webDriver.switchTo().window(winHandleBefore).close();
//						webDriver.switchTo().window(winHandleafter);
					}catch (Exception e) {
						RESULT.failed("CRM tab close","CRM tab should be closed ","CRM tab is not closed");
					}
					//get window handlers as list
					SleepUtils.getInstance().sleep(TimeSlab.YIELD);
					List<String> browserTabs = new ArrayList<String> (webDriver.getWindowHandles());
					System.out.println(browserTabs.size()+": tab size after close");
					//switch to new tab
					webDriver.switchTo().window(browserTabs.get(0));
					System.out.println(webDriver.getCurrentUrl());
					uiDriver.waitForPageLoad();
					if (CompositeAppDriver.startUp.equalsIgnoreCase("IE")){
						webDriver.manage().window().maximize();
					}
					wait.until(ExpectedConditions.visibilityOfElementLocated(By.name(objMap.getLocator("imgfd_Logo"))));
					if(webDriver.findElements(By.name(objMap.getLocator("imgfd_Logo"))).size()>0)
					{
						RESULT.passed("Modify Order in CRM", "User should be navigated to storefront application", 
								"Nevigation to storefront is successfull");
					}
					else
					{
						RESULT.failed("Modify Order in CRM", "User should be navigated to storefront application", 
						"Nevigation to storefront is unsuccessfull");
						return;
					}
					String Operate = Operation.toUpperCase();
					// Item names and values for Operation would be colon
					// separated so split those values and call modify_cart
					// function
					String Items[] = ItemName.split(":");
					String Qty[] = Quantity.split(":");
					String oper[] = Operate.split(":");
					for (int i = 0; i < oper.length; i++) {
						if (oper[i].contains("INCREASE")|| oper[i].contains("DECREASE")
								|| oper[i].contains("DELETE")|| oper[i].contains("UPDATE")|| oper[i].contains("EMPTYCART")) {
							uiDriver.FD_modifyCartCRM(Items[i], oper[i], Qty[i],
									DeleteAllAcceptDecline, FlexibilityFlag);
						}
						else if (oper[i].contains("CANCEL")) {
							try{
								if (uiDriver.isDisplayed("btncancelUpdates1CRM")) {
									uiDriver.getwebDriverLocator(objMap.getLocator("btncancelUpdates1CRM")).click();
									RESULT.passed("Cancel modification",
											"Cart updation should get cancelled successfully.No modifications should be applied",
									"Cart updation cancelled successfully. No modifications applied");
								} else {
									RESULT.failed("Cancel modification",
											"Cancel changes button should be displayed",
									"Cancel changes button is not displayed");
								}
							}catch(Exception e){
								RESULT.failed("Modify Order ","Cancel changes button should be visible and clicked",
								"Cancel changes button is not visible or clicked");
							}
						} else if (!oper[i].contains("INCREASE")
								&& !oper[i].contains("DECREASE")
								&& !oper[i].contains("DELETE")
								&& !oper[i].contains("UPDATE")
								&& !oper[i].contains("EMPTYCART")
								&& !oper[i].contains("CANCEL")) {
							RESULT.failed("Invalid argument for 'Operation'",
									"Given argument should be INCREASE, DECREASE, DELETE, UPDATE, CANCEL or EMPTYCART",
							"Given argument is other than Operation Arguments. Give correct argument");
						}
					}
					/*webDriver.close();
					webDriver.switchTo().window(browserTabs.get(0));*/
				}else {
					RESULT.failed("Modify Order",
							"Order should modified as per specified by user",
							"Order can not be modified in status: "
							+ OrderStatus
							+ " which is other than submitted");
				}return;
			}else
			{	RESULT.warning("Modify Order CRM",
					"Order should get modify successfully",
					"Desired order is not present in your order list: Order #"+ OrderNo);
			return;
			}
		}catch(Exception e){
			RESULT.failed("Modify Order CRM function", "Order should get modified successfully",
					"Order is not modified due to " + e.getMessage());
		}
	}	
	
	/**
	 * FD_Deliverypass_Add_Verify Function perform Adding and Verifying of
	 * Delivery pass. Before Adding DeliveryPass to cart it will check if
	 * customer has DeliveryPass or not. If customer has DeliveryPass then it
	 * will not add and Print Message. For Verification of DeliveryPass Function
	 * Check six Month Membership for customer.
	 */

	private enum AddVerify {
		ADD, VERIFY, RENEW
	};

	/**
	 *Function Name: FD_Deliverypass_Add_Verify Purpose: Function to Add,Verify
	 * and Renew Deliverypass for customer Created By: Tejas Patel Created Date:
	 * Modified By: Modified Date:
	 * 
	 * @param Process
	 *            :Define process(i.e Add/Verify/Renew) for Deliverypass
	 **/
	public void FD_Deliverypass_Add_Verify(String Process)
	throws InterruptedException {
		String process1 = Process.toUpperCase();
		switch (AddVerify.valueOf(process1)) {
		case ADD: 
			try{
				int count;
				
				String DeliverypassDetail = uiDriver.getwebDriverLocator(
						objMap.getLocator("strdeliverypassDetail")).getText();
				if (DeliverypassDetail.contains("Six-Month DeliveryPass")) {
					System.out.println("Sorry! you have already Delivery Pass");
					RESULT.warning("Add Deliverypass",
							"User should already have a Deliverypass",
							"User is having Deliverypass");
				}else {
					//signUP for DeliveryPass
					uiDriver.click("lnkdeliverypass_signup");
					waitForPageLoad();
					
					try{
						wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(objMap.getLocator("btnadd_cart"))));
					}catch(Exception e){
						RESULT.failed("Delivery Pass Options Add", "Delivery Pass PDP should get displayed",
						"Delivery Pass PDP is not available after waiting for 180 seconds");
					}
					if (webDriver.findElements(
							By.className(objMap.getLocator("strcart_info"))).size() > 0) {
						String cart_info = uiDriver.getwebDriverLocator(
								objMap.getLocator("strcart_info")).getText();
						if (cart_info.contains("1 in cart")) {
							RESULT.passed("Add Deliverypass",
									"User should be able to add Deliverypass",
							" Deliverypass already added");

						}else {
							// adding DeliveryPass To cart
							uiDriver.click("btnadd_cart");
							RESULT.passed("Add Deliverypass",
									"User should be able to add Deliverypass",
							" Deliverypass added Sucessfully ");
							waitForPageLoad();
							orderCreationPopup("new");
						}
					}else{
						RESULT.failed("Delivery Pass Options Add", "Add to cart button message should be available",
								"Add to cart button message is not available");
					}

				}

			}catch(Exception e){
				RESULT.failed("Delivery Pass Options Add", "Delivery pass should get added",
						"Delivery pass is not added due to " + e.getMessage());
			}
			break;
		case VERIFY: 

			try{

				uiDriver.getwebDriverLocator(objMap
						.getLocator("strdeliverypassDetail"));
				String Verification = uiDriver.getwebDriverLocator(
						objMap.getLocator("strdeliverypassDetail")).getText();
				if (Verification.contains("Six-Month DeliveryPass")) {
					System.out.println("Verification Done");
					RESULT
					.passed(
							"Verify Deliverypass",
							"User should be able to verify membership of Deliverypass",
					"Verification Done");
				} else {
					RESULT
					.failed(
							"Verify Deliverypass",
							"User should be able to verify membership of Deliverypass",
					"User unable to verify membership of Deliverypass");
				}

			}catch(Exception e){
				RESULT.failed("Delivery Pass Options Verify", "Delivery pass should get verified",
						"Delivery pass is not verified due to " + e.getMessage());
			}
			break;
		case RENEW: 

			try{
				try {
					webDriver.findElement(By.linkText(objMap
							.getLocator("lnkrenewaloff")));

					uiDriver.click("lnkrenewaloff");
					waitForPageLoad();
					if (uiDriver.isDisplayed("btnrenewalon")) {
						uiDriver.click("btnrenewalon");
						waitForPageLoad();
						if (webDriver.findElements(
								By.linkText(objMap.getLocator("lnkrenewaloff")))
								.size() > 0) {
							RESULT
							.passed(
									"Verify renewal off/on link of Deliverypass",
									"User should be able to verify renewal off/on link of deliverypass",
							"Verification Done");
						} else {
							RESULT
							.failed("Verify renewal off/on link of Deliverypass",
									"User should be able to verify renewal off/on link of deliverypass",
							"Verification Fail");
						}
					} else {
						RESULT.failed("Delivery Pass Options Renew",
								"Renew On button should be displayed",
							"Renew On button is not displayed");
					}
				} catch (NoSuchElementException e1) {
					uiDriver.click("btnrenewalon");
					waitForPageLoad();
					if (uiDriver.isDisplayed("lnkrenewaloff")) {
						uiDriver.click("lnkrenewaloff");
						selenium.waitForPageToLoad(PageLoadTime);
						if (uiDriver.isDisplayed("btnrenewalon")) {
							RESULT
							.passed(
									"Verify renewal off/on link of Deliverypass",
									"User should be able to verify renewal off/on link of deliverypass",
							"Verification Done");
						} else {
							RESULT
							.failed(
									"Verify renewal off/on link of Deliverypass",
									"User should be able to verify renewal off/on link of deliverypass",
							"Verification Fail");
						}
					} else {
						RESULT.failed("Delivery Pass Options Renew",
								"Renew Off button should be displayed",
								"Renew Off button is not displayed");
					}
				}
			}catch(Exception e){
				RESULT.failed("Delivery Pass Options Renew", "Delivery pass should get renewed",
						"Delivery pass is not renewed due to " + e.getMessage());
			}
			break;

		}// end of switch case

	}

	// Enum options for the searchOptions

	private enum searchOptions {
		CLICK, HOVER, QUICKSEARCH
	};

	/**
	 *Function Name: FD_searchFunction Purpose: Function to search a product in
	 * Storefront Created By: Bhavik Tikudiya Created Date: Modified By:
	 * Modified Date:
	 * 
	 * @param method
	 *            : method refers the way user wants to search a product i.e.
	 *            Quick, Click or Hover
	 *@param dept
	 *            : dept refers the main department from the navigation header
	 *            for the product i.e. Fruit
	 *@param sub_dept
	 *            : sub_dept refers the sub-main which is displayed in some of
	 *            the department i.e. Kitchen
	 *@param category
	 *            : category refers to the search item i.e Apples return void
	 * 
	 **/
	public void FD_searchFunction(String method, String dept, String sub_dept,
			String category)  {

		// Global variable which can be used in Fd_addToCart if specified item
		// is not available
		prdct_category = category;
		prdct_department = dept;
		int count=0;
		String breadcrumbs_text = null;
		switch (searchOptions.valueOf(method.toUpperCase())) {
		case QUICKSEARCH:// Quick Search
			if (webDriver.findElements(
					By.xpath(objMap.getLocator("tabyourTopItems"))).size() > 0
					&& !(webDriver.findElement(By.xpath(objMap
							.getLocator("tabyourList").replace("/strong", ""))).getAttribute("class").equals("selected") )) {

				// enter product to search

				uiDriver.setValue("txtreorderSearch", category);
				uiDriver.getwebDriverLocator(objMap.getLocator("btnreorderSearch")).click();
				waitForPageLoad();
				try
				{
					wait.until(ExpectedConditions.visibilityOfElementLocated(By
							.xpath(objMap.getLocator("btnreorderAtoZ"))));
				}
				catch(Exception e)
				{
					RESULT.log("Reorder search wait", 
							ResultType.FAILED, 
							"A to Z sort should be displayed", 
							"A to Z sort is not displayed", true);
				}

				// result based on the list appeared or not
				if (webDriver.findElements(
						By.xpath(objMap
								.getLocator("stremptyReorderSearchResult")))
								.size() > 0) {

					RESULT.warning("Searching product using search field",
							"Product search should be successfully",
							"Product search : Showing 0 product found for "
							+ category);
				} else {
					// for list
					if (webDriver.findElements(
							By.xpath(objMap.getLocator("lnkfirstItemReorder")))
							.size() > 0 && webDriver.findElement(
									By.xpath(objMap.getLocator("lnkfirstItemReorder"))).isDisplayed()) {

						RESULT.passed("Searching product using search field",
								"Product search should be done successfully",
						"Product search is successful");
					}
					// for grid
					else if(webDriver.findElements(
							By.xpath(objMap.getLocator("lnkfirstItem")))
							.size() > 0 && webDriver.findElement(
									By.xpath(objMap.getLocator("lnkfirstItem"))).isDisplayed()){
						RESULT.passed("Searching product using search field",
								"Product search should be done successfully",
						"Product search is successful");
					}
					// fail
					else {
						RESULT.log("Searching product using search field",
								ResultType.FAILED,
								"Product search should be done successfully",
								"Product search failed", true);

					}
				}
			} else {
				// enter product to search0
				try {
					uiDriver.setValue("txtsearchField", category.replaceAll(
							"'", ""));
					// click on the go
					if (CompositeAppDriver.startUp.equalsIgnoreCase("IE")
							|| CompositeAppDriver.startUp
							.equalsIgnoreCase("CHROME")) {
						uiDriver.getwebDriverLocator(
								objMap.getLocator("txtsearchField")).sendKeys(
										Keys.ENTER);
					} else {
						uiDriver.click("btnsearchGo");
						waitForPageLoad();
					}

				} catch (Exception e) {
					RESULT.failed("Search product", 
							"User should be able to input search data", 
					"User is not able to input search data");
				}

				try
				{	
					// search result for partial search i.e if searched wine123 will give result for wine
					try{
						wait.until(ExpectedConditions.visibilityOfElementLocated(By
								.xpath(objMap.getLocator("strverifyDifferentResult"))));
						String str_products_diaplyed = uiDriver.getwebDriverLocator(
								objMap.getLocator("strverifyDifferentResult")).getText();					
						if (str_products_diaplyed.startsWith("0")) {
							RESULT.warning("Searching product using search field",
									"Partial product search should be successful",
									str_products_diaplyed.split("Did you mean")[0]);
						}
					}catch(Exception e){
						RESULT.failed("Searching product using search field",
								"Product search result count string should be displayed",
								"Product search result count string is not displayed");
						return;
					}
					
					// result based on the title of the page and search result
					wait.until(ExpectedConditions.visibilityOfElementLocated(By
							.xpath(objMap.getLocator("strverifyNoSearchResult"))));
					if (uiDriver.getwebDriverLocator(
							objMap.getLocator("strverifyNoSearchResult")).getText()
							.contains("(0)")) {
						RESULT.warning("Searching product using search field",
								"Product search should be successful",
								"Product search failed : Showing 0 product found for "
								+ category);
					} else {
						if (webDriver.getTitle().toLowerCase().contains(
								category.replaceAll("'", "").toLowerCase())) {
							RESULT.passed("Searching product using search field",
									"Product search should be successful",
							"Product search is successfully");
						} else {
							RESULT.failed("Searching product using search field",
									"Product search should be successful",
							"Product search failed");
						}
					}
				}
				catch(Exception e)
				{
					RESULT.failed("Search product", "Valid search string should be displayed",
					"Valid search string not displayed");
					return;
				}
			}
			break;

		case CLICK:// click method
			try {
				String depth = "2";
				// click on the Fruit tab directly
				try {
					webDriver.findElement(By.xpath(objMap.getLocator(dept)))
					.click();					
				} catch (NoSuchElementException e) {
					RESULT
					.failed(
							"Searching product using click",
							"Product search should be successfully: "
							+ dept
							+ " should be  available in Global navigation bar",
							"Product search failed: "
							+ dept
							+ " is not available in Global navigation bar");
					return;
				}				
				waitForPageLoad();				
				if (CompositeAppDriver.startUp.equalsIgnoreCase("IE"))
				{
//					wait.until(ExpectedConditions.visibilityOfElementLocated(By
//							.xpath(objMap.getLocator("strShopBy"))));
//					robot.moveMouseToCoordinates(0, 0);
					((JavascriptExecutor) webDriver).executeScript("scroll(0, -250);");					
				}
				else
				{
				robot.moveToElement(webDriver.findElement(By.name(objMap
						.getLocator("imgfd_Logo"))));
				}
				// click on sub-departement if sub-departement is available
				if (!sub_dept.endsWith(dept)
						&& sub_dept.replace(" ", "").length() > 0) {
					// For the sub-departement correct the sub-departement if we
					// have item with space which is without space in excel data
					if (sub_dept.equals("CheeseShop"))
						sub_dept = "Cheese Shop";
					else if (sub_dept.equals("FourMinuteMeals"))
						sub_dept = "4-Minute Meals";
					else if (sub_dept.equals("PreppedtoCook"))
						sub_dept = "Prepped to Cook";
					else if (sub_dept.equals("PastaShop"))
						sub_dept = "Pasta Shop";
					else if (sub_dept.equals("HeatEat"))
						sub_dept = "Heat & Eat";
					else if (sub_dept.equals("HealthBeauty"))
						sub_dept = "Health & Beauty";
					String sub_dept_xpath = "//span[text() = '" + sub_dept
					+ "']";
					if (CompositeAppDriver.startUp.equalsIgnoreCase("SAFARI"))
						webDriver.findElement(By.xpath(sub_dept_xpath)).click();
					else
						actions
						.moveToElement(
								webDriver.findElement(By
										.xpath(sub_dept_xpath)))
										.click().build().perform();
					// builder.moveToElement(webDriver.findElement(By.xpath(sub_dept_xpath))).click().build().perform();
					depth = "3";
					waitForPageLoad();

				}
				if (category.replace(" ", "").length() > 0) {
					// click on category from side
					// wait for the page to load with element visible condition
					String category_xapth;
					if (category.equals("Cheese")) {
						category_xapth = "//li[@data-component='menuitem']/label/span/span[text()='"
							+ category.split("&")[0] + "']";
					} else {
						category_xapth = "//span[contains(text(),'"
							+ category.split("&")[0] + "')]";
					}
					try
					
					{
						wait.until(ExpectedConditions.visibilityOfElementLocated(By
								.xpath(category_xapth)));
						robot.moveToElement(webDriver.findElement(By
								.xpath(category_xapth)));
						// click on category
						webDriver.findElement(By.xpath(category_xapth)).click();
					}
					catch(Exception e)
					{
						RESULT.failed("Search product : Click", category+" Category should be available", category+" Category is not available");
					}

					String verify_locator = objMap.getLocator(
					"strverifyCategoryClick").replace("3", depth);
					try
					{
						wait.until(ExpectedConditions.visibilityOfElementLocated(By
								.xpath(verify_locator)));
					}
					catch(TimeoutException e)
					{
						RESULT.failed("Search Product : Click", "Category breadcrumb should be displayed", 
						"Category breadcrumb is not displayed within 3 minutes");
					}


				}
				breadcrumbs_text = null;
				SleepUtils.getInstance().sleep(TimeSlab.YIELD);

				try
				{
					count=0;
					do {
						breadcrumbs_text = webDriver.findElement(
								By.xpath(objMap
										.getLocator("strverifySearchResults")))
										.getText();
						SleepUtils.getInstance().sleep(TimeSlab.YIELD);
						count++;
					} while (breadcrumbs_text == null
							|| breadcrumbs_text.replace(" ", "").equals("") && count <90);

				}
				catch(Exception e)
				{
					RESULT.failed("Search Product : Click",
							"Breadcrumb should be displayed ",
					"Breadcrumb is not displayed");
					return;
				}


				// if (webDriver.getTitle().toLowerCase().contains(
				// category.toLowerCase().split("&")[0])
				// || webDriver.getTitle().toLowerCase().contains(
				// dept.toLowerCase().split("&")[0])) {
				if (breadcrumbs_text.toLowerCase().contains(
						category.toLowerCase())) {
					RESULT.passed("Searching product using click",
							"Product search should be successfully",
					"Product search is successfully");
				} else {
					RESULT.failed("Searching product using click",
							"Product search should be successful : "
							+ category + " departement -" + dept,
							"Product search failed : " + webDriver.getTitle());
					return;
				}
				break;
			} catch (Exception e) {
				RESULT.failed("Search Product Exception",
						"Search product should be successful",
				"Search product is not succcessful");
				return;

			}

		case HOVER:// hover method
			try {
				String category_hover_xapth;
				// Action to mouse over the header tab to make menu appear
				// exception
				// for flowers
				try {
					//					if (dept.equals("Flowers")) {
					//						webDriver
					//								.findElement(By.xpath(objMap.getLocator(dept)))
					//								.click();
					//						waitForPageLoad();
					//					} else {
					// builder.moveToElement(webDriver.findElement(By.xpath(objMap.getLocator(dept)))).build().perform();
					robot.moveToElement(webDriver.findElement(By
							.xpath(objMap.getLocator(dept))));
					//}
				} catch (NoSuchElementException e) {
					RESULT
					.failed(
							"Searching product using hover",
							"Product search should be successfully: "
							+ dept
							+ " should be  available in Global navigation bar",
							"Product search failed: "
							+ dept
							+ " is not available in Global navigation bar");
					return;
				}
				category_hover_xapth = objMap.getLocator(dept).split("span")[0];
				// hover on sub-departement if sub-departement is available
				if (!sub_dept.endsWith(dept)
						&& sub_dept.replace(" ", "").length() > 0) {
					String temp_sub_dept = sub_dept;
					// For the sub-departement correct the sub-departement if we
					// have item with space whichis without space in excel data
					if (sub_dept.equals("CheeseShop"))
						sub_dept = "Cheese Shop";
					else if (sub_dept.equals("FourMinuteMeals"))
						sub_dept = "4-Minute Meals";
					else if (sub_dept.equals("PreppedtoCook"))
						sub_dept = "Prepped To Cook";
					else if (sub_dept.equals("PastaShop"))
						sub_dept = "Pasta Shop";
					else if (sub_dept.equals("HeatEat"))
						sub_dept = "Heat & Eat";
					else if (sub_dept.equals("HealthBeauty"))
						sub_dept = "Health & Beauty";
					String sub_dept_xpath;
					if (dept.contains("Deli") || dept.contains("Kitchen")
							|| dept.contains("Grocery")) {
						for (int i = 1; i < 6; i++) {
							if (dept.contains("Deli")) {
								sub_dept_xpath = "//li[@data-id='deliandcheese']/li/div/ul/li["
									+ i + "]/span/a";
							} else if (dept.equals("Kitchen")) {
								sub_dept_xpath = "//li[@data-id='kitchen']/li/div/ul/li["
									+ i + "]/span/a";
							} else if (dept.equals("Grocery")) {
								sub_dept_xpath = "//li[@data-id='supergro']/li/div/ul/li["
									+ i + "]/span/a";
							} else
								break;
							if (webDriver.findElement(By.xpath(sub_dept_xpath))
									.getText().equalsIgnoreCase(sub_dept)) {
								try
								{
									robot.moveToElement(webDriver.findElement(By
											.xpath(sub_dept_xpath)));
								}
								catch(NoSuchElementException e)
								{
									RESULT.failed("Search Product : Hover",sub_dept+" Sub department should be available",sub_dept+" Sub department is not available");
								}

								break;
							} else
								System.out.println("going for next category");

						}
					} else {
						sub_dept_xpath = "//*[text() = '" + sub_dept + "']";
						try
						{
							wait.until(ExpectedConditions
									.visibilityOfElementLocated(By
											.xpath(sub_dept_xpath)));
							robot.moveToElement(webDriver.findElement(By
									.xpath(sub_dept_xpath)));
							if (sub_dept.equals("Flowers")) {
								webDriver.findElement(By
										.xpath(sub_dept_xpath)).click();
								category = sub_dept;
							}
						}
						catch(Exception e)
						{
							RESULT.failed("Search Product : Hover", "Sub department should be available",
							"Sub department is not available");
						}

					}					
					category_hover_xapth += "li/div/ul/li/"
						+ objMap.getLocator(temp_sub_dept) + "div/div[2]/";
				} else {
					category_hover_xapth += "li/div/div[2]/";
				}
				// For flower added the eual check
				if (category.replace(" ", "").length() > 0 && !(category.equalsIgnoreCase(sub_dept))) {
					// wait for the page to load with element visible condition

					if (category.equals("Cheese")) {
						category_hover_xapth += "li/a[text()='" + category
						+ "']";
					} else if (category.equals("Local")) {
						category_hover_xapth += "li/a[text()='" + category
						+ "']";
					} else {
						category_hover_xapth += "div[@class='dropdown-column']/ul/li/a[contains(text(),'"
							+ category + "')]";
					}
					WebElement category_click;
					System.out.println(webDriver.findElements(
							By.xpath(category_hover_xapth)).size());
					try
					{
						if (category.equals("Cheese")) {
							category_click = webDriver.findElements(
									By.xpath(category_hover_xapth)).get(0);
						} else {
							category_click = webDriver.findElement(By
									.xpath(category_hover_xapth));
						}
						wait.until(ExpectedConditions.visibilityOf(category_click));
						category_click.click();
					}
					catch(Exception e)
					{
						RESULT.failed("Search Product : Hover", "Category should be available",
						"Category is not available");
					}

				}
				try
				{
					waitForPageLoad();
					wait.until(ExpectedConditions.visibilityOfElementLocated(By
							.xpath(objMap.getLocator("strverifySearchResults"))));
				}
				catch(TimeoutException e)
				{
					RESULT.failed("Search Product : Hover", "Category breadcrumb should be displayed", 
					"Category breadcrumb is not displayed within 3 minutes");
				}

				breadcrumbs_text = null;
				SleepUtils.getInstance().sleep(TimeSlab.YIELD);
				count=0;
				do {
					breadcrumbs_text = webDriver.findElement(
							By.xpath(objMap
									.getLocator("strverifySearchResults")))
									.getText();
					SleepUtils.getInstance().sleep(TimeSlab.YIELD);
					count++;
				} while (breadcrumbs_text == null
						|| breadcrumbs_text.replace(" ", "").equals("") && count<90);

				// result based on the title of the page
				// if (webDriver.getTitle().toLowerCase().contains(
				// category.toLowerCase().split("&")[0])
				// || webDriver.getTitle().toLowerCase().contains(
				// dept.toLowerCase().split("&")[0])) {
				if (breadcrumbs_text.toLowerCase().contains(
						category.toLowerCase())) {
					RESULT.passed("Searching product using hover",
							"Product search should be successfully",
					"Product search is successfully");
				} else {
					RESULT.failed("Searching product using hover",
							"Product search should be successfully : "
							+ category + " departement -" + dept,
							"Product search failed : " + webDriver.getTitle());
				}
				break;
			} catch (Exception e) {
				RESULT.failed("Search product : Hover", "Search product should be successful", "Search product is not successful");
				e.printStackTrace();
			}
		default:// function does not received any method
			RESULT.failed("Searching product",
					"Product search should be successful : " + category
					+ " departement -" + dept, "Product search failed");
			break;
		}
	}

	/**
	 * Function Name: FD_addList 
	 * Purpose: For add a product/item into a list
	 * Created By: Nirali Kotak Created Date: 15th July,2015
	 * Modified By: Modified Date:
	 * 
	 * @param product
	 *            product/item to be added in list
	 * @param listflag_new_exist
	 *            flag to add to new or existing list
	 * @param listname
	 *            list name to be added in
	 * @param FlexibilityFlag
	 *            Flexibily flag if user value not available
	 *  
	 * @return void
	 */
	public void FD_addList(WebElement product, String listflag_new_exist,
			String listname, String FlexibilityFlag) throws FindFailed,
			InterruptedException {


		String ddl_listname[];
		String success_msg;
		String error_msg;
		int i;

		try {
			Actions ac_key = new Actions(webDriver);
			if(!(product.getText().equalsIgnoreCase("add all to list")))
			{
				try {
					JavascriptExecutor jse = (JavascriptExecutor) webDriver;
					jse.executeScript("arguments[0].scrollIntoView()", product);
					//click product
					if (CompositeAppDriver.startUp.equalsIgnoreCase("FIREFOX")
							|| CompositeAppDriver.startUp
							.equalsIgnoreCase("CHROME")
							|| CompositeAppDriver.startUp.equalsIgnoreCase("IE")) {
						jse.executeScript("arguments[0].click()", product);
					} else {
						product.click();
					}
				} catch (Exception e) {
					if (FlexibilityFlag.equalsIgnoreCase("Yes")) {
						try {
							product = uiDriver.getwebDriverLocator(objMap
									.getLocator("lnkfirstItemReorder"));
							JavascriptExecutor jse = (JavascriptExecutor) webDriver;
							jse.executeScript("arguments[0].scrollIntoView()",
									product);

							if (CompositeAppDriver.startUp
									.equalsIgnoreCase("FIREFOX")
									|| CompositeAppDriver.startUp
									.equalsIgnoreCase("CHROME")
									|| CompositeAppDriver.startUp
									.equalsIgnoreCase("IE")) {
								jse.executeScript("arguments[0].click()", product);
							} else if (CompositeAppDriver.startUp
									.equalsIgnoreCase("SAFARI")) {
								product.click();
							}
						} catch (Exception e1) {
							// e.printStackTrace();
							//First product locator
							product = uiDriver.getwebDriverLocator(objMap
									.getLocator("lnkfirstItem"));
							JavascriptExecutor jse = (JavascriptExecutor) webDriver;
							jse.executeScript("arguments[0].scrollIntoView()",
									product);

							if (CompositeAppDriver.startUp
									.equalsIgnoreCase("FIREFOX")
									|| CompositeAppDriver.startUp
									.equalsIgnoreCase("CHROME")
									|| CompositeAppDriver.startUp
									.equalsIgnoreCase("IE")) {
								jse.executeScript("arguments[0].click()", product);
							} else if (CompositeAppDriver.startUp
									.equalsIgnoreCase("SAFARI")) {
								product.click();
							}
						}

					} else {
						RESULT
						.failed("Save to list operation failed",
								"Item should be available",
						"Item is not available");
						return;
					}

				}
				//Out of stock product
				if (webDriver.findElements(By.xpath("diaavailability")).size() > 0) {
					RESULT.warning("Unavailable product message",
							"Product should be unavailable and availability note should be displayed",
							"Product is unavailable :"
							+ uiDriver.getwebDriverLocator(
									objMap.getLocator("strprodUnavailability")).getText());

					return;

				} else {
					SleepUtils.getInstance().sleep(TimeSlab.YIELD);
					try{
						wait.until(ExpectedConditions.visibilityOfElementLocated(By
								.id(objMap.getLocator("btnsaveList"))));
					}catch(TimeoutException e){
						RESULT.failed("Add item to list function Product click",
								"Add to List button should be available",
						"Add to List button is not available");
					}
					uiDriver.getwebDriverLocator(objMap.getLocator("btnsaveList"))
					.click();
				}
			}
			else
			{
				product.click();
			}
			/* Create new list */
			if (listflag_new_exist.equals("New")) {
				SleepUtils.getInstance().sleep(TimeSlab.YIELD);
				// System.out.println("Test Data well");
				List<WebElement> ddl = uiDriver.getwebDriverLocator(
						objMap.getLocator("drplist")).findElements(
								By.tagName("option"));

				/*
				 * Basic Verification Point - 3 Loop checks if the new list name
				 * matches any of the existing list name. It will return
				 * FreshDirect error message if matches otherwise continues with
				 * new list creation.
				 */
				ddl_listname = new String[ddl.size()];
				for (i = 0; i < ddl.size(); i++) {
					ddl_listname[i] = ddl.get(i).getText();
					System.out.println(ddl.get(i).getText());
					if (ddl_listname[i].equalsIgnoreCase(listname)) {
						System.out.println("List already exist");
						uiDriver.setValue("txtnewList", listname);
						if (CompositeAppDriver.startUp
								.equalsIgnoreCase("SAFARI")) {
							webDriver.findElement(
									By.xpath(objMap.getLocator("btnnewList")))
									.click();
						} else {
							ac_key.sendKeys(Keys.TAB).build().perform();
							ac_key.sendKeys(Keys.ENTER).build().perform();
						}

						wait.until(ExpectedConditions.visibilityOf(uiDriver
								.getwebDriverLocator(objMap.getLocator("strlistNameDuplicateError"))));

						error_msg = uiDriver.getwebDriverLocator(
								objMap.getLocator("strlistNameDuplicateError"))
								.getText();
						if (error_msg.contains("Oops! That name is taken!")) {
							uiDriver.click("btncloseList");
							RESULT.passed("Duplicate List name",
									"Error should be displayed", error_msg);
							return;
						} else {
							RESULT.failed("Duplicate List name",
									"Error mesage should be displayed",
							"Error message not displayed properly");
							return;
						}
					}
				}

				uiDriver.getwebDriverLocator(objMap.getLocator("txtnewList"))
				.sendKeys(listname);

				if (CompositeAppDriver.startUp.equalsIgnoreCase("SAFARI")) {
					webDriver.findElement(
							By.xpath(objMap.getLocator("btnnewList"))).click();
				} else {
					ac_key.sendKeys(Keys.TAB).build().perform();
					ac_key.sendKeys(Keys.ENTER).build().perform();
				}

				SleepUtils.getInstance().sleep(TimeSlab.YIELD);
				if (uiDriver.getwebDriverLocator(
						objMap.getLocator("btnlistAddOk")).isEnabled()) {
					RESULT.passed("New list creation ",
							"New list should be created successfully",
							"New list " + listname + " created successfully");
				} else {
					RESULT.failed("New list creation ",
							"New list should be created successfully",
							"New list " + listname + " creation  unsuccessful");
				}

				uiDriver.getwebDriverLocator(objMap.getLocator("btnlistAddOk"))
				.click();

			}
			/*
			 * Add to existing list It checks if user wants to add to an
			 * existing list It will take the list name from user and will
			 * select the list and add items to it
			 */
			else if (listflag_new_exist.equals("Exist")) {

				try {
					SleepUtils.getInstance().sleep(TimeSlab.YIELD);
					wait.until(ExpectedConditions.visibilityOf(uiDriver
							.getwebDriverLocator(objMap
									.getLocator("txtnewList"))));
					uiDriver.DrawHighlight("txtnewList", webDriver);
				} catch (Exception e) {
					System.out.println("Text box not found");
					return;
				}

				Select list_ddl = new Select(uiDriver
						.getwebDriverLocator(objMap.getLocator("drplist")));
				try {
					list_ddl.selectByVisibleText(listname);

				} catch (Exception e) {
					if (FlexibilityFlag.equalsIgnoreCase("Yes")) {
						list_ddl.selectByIndex(0);
					} else {
						RESULT.warning("Item addition to existing list",
								"Item should be added to the list " + listname,
								"Item could not be added to list as list :"
								+ listname + " was unavailable");
						return;
					}
				}

				uiDriver.click("btnaddAddToList");
				SleepUtils.getInstance().sleep(TimeSlab.YIELD);

				if (uiDriver.isDisplayed("btnlistAddOk")) {
					RESULT.passed("Item addition to existing list",
							"Item should be added to the list :" + listname,
							"Item is added to the list :" + listname);
				} else {
					RESULT.failed("Item addition to existing list",
							"Item should be added to the list :" + listname,
							"Item is not added to the list :" + listname);
					return;
				}
				uiDriver.getwebDriverLocator(objMap.getLocator("btnlistAddOk"))
				.click();

			} else {
				return;
			}

		} catch (Exception e) {
			RESULT.failed("Add item to list function Exception",
					"Add item to list function should be successful",
					"Add item to list function failed due to: "
					+ e.getMessage());
		}
	}

	/**
	 * Function Name: FD_loginCRM Purpose: For Login in to CRM of FreshDirect
	 * application Created By: Avani Thakkar Created Date: 15th May,2015
	 * Modified By: Modified Date:
	 * 
	 * @param UID
	 *            userId for log in
	 * @param PASS
	 *            password for particular user
	 * @return void
	 */

	public void FD_loginCRM(String UserID, String Password)
	throws InterruptedException, FindFailed {
		uiDriver.setValue("txtCRMUID", UserID);
		uiDriver.setValue("txtCRMPassword", Password);
		//SleepUtils.getInstance().sleep(TimeSlab.YIELD);
		if(webDriver.findElements(By.className(objMap.getLocator("btnCRMLogin"))).size()>0
				&& uiDriver.isDisplayed("btnCRMLogin"))
		{
			RESULT.passed("Log in to CRM application", "Log in button should be available and displayed", 
			"Log in button is available and displayed");
		}else
		{
			RESULT.failed("Log in to CRM application", "Log in button for CRM application should be available and displayed", 
					"Log in button for CRM application is not available or not displayed");
			return;
		}
		uiDriver.click("btnCRMLogin");
		try{
			uiDriver.wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(objMap.getLocator("lnkhome"))));
		}catch(Exception e)
		{
			RESULT.failed("CRM Homepage","Homepage Should not  displayed","Homepage is not displayed");
			return;
		}

		waitForPageLoad();
	}

	public void FD_loginEmailNotification(String UserID, String Password)
	throws InterruptedException, FindFailed {
		try{
			uiDriver.setValue("txtuserNameEmail", UserID);
			uiDriver.setValue("txtpasswordEmail", Password);
			//		SleepUtils.getInstance().sleep(TimeSlab.YIELD);
			uiDriver.click("btnloginEmail");
			//		selenium.waitForPageToLoad(PageLoadTime);
			waitForPageLoad();
		}catch(Exception e){
			RESULT.failed("Login to SquirrelMail function","Login should not be successful",
			"Login unsuccessful with userid");
		}
	}

	/**
	 *Function Name: FD_newCustomerCRM Purpose: Function to Add new customer in
	 * CRM Created By: Tejas Patel Created Date: Modified By: Modified Date:
	 * 
	 * @param Customer_type
	 *            : Customer type like home, corporate
	 * @param Zip
	 *            : Zipcode for customer
	 * @param Title
	 *            : Title of customer like(Mr./Ms./Mrs)
	 * @param First_name
	 *            : First name of customer
	 * @param Middle_name
	 *            : Middle name of customer
	 * @param Last_name
	 *            : Last name of customer
	 * @param Home_phone
	 *            : Home phone of customer
	 * @param Home_ext
	 *            : Home extension of customer
	 * @param Work_phone
	 *            : Work phone of customer
	 * @param Work_ext
	 *            : Work extension of customer
	 * @param Cell_phone
	 *            : Cell phone of customer
	 * @param Cell_ext
	 *            : Cell extension of customer
	 * @param Alter_email
	 *            : Alternate Email of customer
	 * @param Email_CRM
	 *            : Email of customer
	 * @param Pass_CRM
	 *            : Password of customer
	 * @param TOB
	 *            : Town Of birth of customer
	 * @param Address_type
	 *            : Address_type of customer
	 * @param Delivery_Fname
	 *            : First name of customer for delivery
	 * @param Delivery_Lname
	 *            : Last name of customer for delivery
	 * @param Delivery_Companyname
	 *            : Companyname of customer for delivery
	 * @param Add1
	 *            : Address of customer
	 * @param Add2
	 *            : Address of customer
	 * @param AptName
	 *            : ApartmentName of customer
	 * @param City
	 *            : city of customer
	 * @param State
	 *            : State of customer
	 * @param Zip_CRM
	 *            : Zipcode of customer for delivery
	 * @param Delivery_phone
	 *            : Phone number of customer for Delivery
	 * @param Instruction
	 *            : Instruction of Delivery
	 * @param Alternate_Delivery
	 *            : Alternate Delivery of customer
	 * @param Card_name
	 *            : card holder name for payment
	 * @param Card_type
	 *            : Card type for payment
	 * @param Card_number
	 *            : Card number for payment
	 * @param Card_month
	 *            : Card expire month
	 * @param Card_year
	 *            : Card expire year
	 * @param Use_Delivery
	 *            : Define use payment address as delivery address
	 * @param Bil_Add1
	 *            : Address for payment
	 * @param Bil_Add2
	 *            : Address for payment
	 * @param Bil_AptName
	 *            : Apartment for payment
	 * @param Bil_City
	 *            : City for payment
	 * @param Bil_State
	 *            : State for payment
	 * @param Bil_Zipcode
	 *            : zipcode for payment
	 * @param Bil_Country
	 *            : country for payment
	 **/

	public void FD_newCustomerCRM(String CustomerType, String Zip,
			String Title, String FirstName, String MiddleName, String LastName,
			String HomePhone, String HomeExt, String WorkPhone, String WorkExt,
			String CellPhone, String CellExt, String AlterEmail,
			String EmailCRM, String PassCRM, String TOB, String AddressType,
			String DeliveryFirstName, String DeliveryLastName,
			String DeliveryCompanyname, String Add1, String Add2,
			String AptName, String City, String State, String ZipCRM,
			String DeliveryPhone, String Instruction, String AlternateDelivery,
			String CardName, String CardType, String CardNumber,
			String CardMonth, String CardYear, String UseDelivery,
			String BilAdd1, String BilAdd2, String BilAptName, String BilCity,
			String BilState, String BilZipcode, String BilCountry) {
		try {

			uiDriver.click("lnknewCustomer");

			try{
				wait.until(ExpectedConditions.visibilityOfElementLocated(By
						.xpath(objMap.getLocator("txtzipcodeCRM"))));
			}catch(Exception e){
				RESULT.failed("Navigation to create new customer page", "Navigation to create new customer page should be successful", "Navigation to create new customer page failed");
				return;
			}

			if (uiDriver.isDisplayed("txtzipcodeCRM")) {
				RESULT.passed("Create New Customer",
						"Page Should be Navigated to Check Zone",
				"Page is Navigated to Check Zone");
			} else {
				RESULT.failed("Create New Customer",
						"Page Should be Navigated to Check Zone",
				"Page is not Navigated to Check Zone");
				return;
			}

			String Customer_type1 = CustomerType.toUpperCase();
			System.out.println("Customer Type: " + Customer_type1);
			if (Customer_type1.equals("HOME DELIVERY")) {
				uiDriver.setValue("txtzipcodeCRM", Zip);
				uiDriver.click("btnzipSubmit");
			} else if (Customer_type1.equals("WEB ORDERS")) {
				uiDriver.setValue("txtwebZip", Zip);
				uiDriver.click("btnwebSubmit");
			} else if (Customer_type1.equals("CORP ORDER")) {
				uiDriver.setValue("txtcorpZip", Zip);
				uiDriver.click("btncorpSubmit");
				SleepUtils.getInstance().sleep(TimeSlab.MEDIUM);
				if (webDriver.getTitle().contains("More Information Required")) {
					uiDriver.setValue("txtCRM_StreetAdd", Add1);
					uiDriver.setValue("txtCRM_AptName", AptName);
					uiDriver.setValue("txtCRM_City", City);
					uiDriver.setValue("txtCRM_State1", State);
					uiDriver.setValue("txtCRM_ZipCode", ZipCRM);
					uiDriver.click("btnCRM_ADD_Check");
				}
			}else{
				RESULT.warning("Customer type for create new customer page", "Customer type for create new customer page should be provided as per the allowed values [HOME DELIVERY,WEB ORDERS,CORP ORDER]", 
						"Customer type for create new customer page is not proper: " + Customer_type1);				
			}

			try{
				wait.until(ExpectedConditions.visibilityOfElementLocated(By
						.name(objMap.getLocator("drptitle"))));
			}catch(Exception e){
				RESULT.failed("Navigation to new customer details page", "Navigation to new customer details page should be successful", "Navigation to new customer details page failed");
				return;
			}

			uiDriver.setValue("drptitle", Title);
			uiDriver.setValue("txtfirst_name", FirstName);
			uiDriver.setValue("txtmiddle_name", MiddleName);
			uiDriver.setValue("txtlast_name", LastName);
			uiDriver.setValue("txthome_phone", HomePhone);
			uiDriver.setValue("txthome_ext", HomeExt);
			uiDriver.setValue("txtwork_phone", WorkPhone);
			uiDriver.setValue("txtwork_ext", WorkExt);
			uiDriver.setValue("txtcell_phone", CellPhone);
			uiDriver.setValue("txtcell_ext", CellExt);
			uiDriver.setValue("txtalter_email", AlterEmail);
			uiDriver.getwebDriverLocator(objMap.getLocator("txtemail_CRM"))
			.clear();
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Date date = new Date();
			String Date1 = date.toString();
			Date1 = Date1.replaceAll(" ", "");
			Date1 = Date1.replaceAll(":", "");
			Date1 = Date1.split("IST")[0];
			System.out.println(Date1);
			String EmailCRM1 = "Test" + Date1 + "@gmail.com";
			uiDriver.setValue("txtemail_CRM", EmailCRM1);
			
			// for safari add the hidden repeat email using javascript to add attribute
			if (CompositeAppDriver.startUp.equalsIgnoreCase("SAFARI")){
				JavascriptExecutor jse = (JavascriptExecutor)webDriver;
				jse.executeScript("document.getElementsByName('repeat_email')[0].setAttribute('type', 'text');");
				webDriver.findElement(By.xpath(objMap.getLocator("txtrepeatEmail_CRM"))).clear();
				webDriver.findElement(By.xpath(objMap.getLocator("txtrepeatEmail_CRM"))).sendKeys(EmailCRM1);
			}
			
			uiDriver.getwebDriverLocator(objMap.getLocator("txtpass_CRM"))
			.clear();
			uiDriver.setValue("txtpass_CRM", PassCRM);
			uiDriver.setValue("txtpass_CRM2", PassCRM);
			uiDriver.setValue("txttob", TOB);
			// Delivery info
			String ADD_Type = AddressType.toUpperCase();
			if (Customer_type1.equals("HOME DELIVERY")
					|| CustomerType.equals("CORP ORDER")) {
				if (ADD_Type.equals("RESEDENTIAL")) {
					uiDriver.click("radhome_CRM");
				} else {
					uiDriver.click("radcorp_CRM");
				}
			}
			if (CompositeAppDriver.startUp.equalsIgnoreCase("SAFARI")){
				uiDriver.getwebDriverLocator(objMap.getLocator("txtCRM_FirstNameTb")).clear();
				uiDriver.setValue("txtCRM_FirstNameTb",DeliveryFirstName);
				uiDriver.getwebDriverLocator(objMap.getLocator("txtCRM_LastNameTb")).clear();
				uiDriver.setValue("txtCRM_LastNameTb", DeliveryLastName);
			}
			uiDriver.setValue("txtCRM_CompName", DeliveryCompanyname);
			uiDriver.getwebDriverLocator(objMap.getLocator("txtCRM_StreetAdd"))
			.clear();
			uiDriver.setValue("txtCRM_StreetAdd", Add1);
			uiDriver.setValue("txtCRM_AddLine2", Add2);
			uiDriver.getwebDriverLocator(objMap.getLocator("txtCRM_AptName"))
			.clear();
			uiDriver.setValue("txtCRM_AptName", AptName);
			uiDriver.getwebDriverLocator(objMap.getLocator("txtCRM_City"))
			.clear();
			uiDriver.setValue("txtCRM_City", City);
			if (Customer_type1.equals("WEB ORDERS")) {
				System.out.println("Customer Type 2nd: " + Customer_type1);
				uiDriver
				.getwebDriverLocator(objMap.getLocator("txtCRM_State1"))
				.clear();
				uiDriver.setValue("txtCRM_State1", State);
			} else {
				/*
				 * Select selectOption = new Select(
				 * getwebDriverLocator("CRM_State"));
				 * selectOption.selectByVisibleText(State);
				 */
				uiDriver.setValue("drpCRM_State", State);
			}
			uiDriver.getwebDriverLocator(objMap.getLocator("txtCRM_ZipCode"))
			.clear();
			uiDriver.setValue("txtCRM_ZipCode", ZipCRM);
			uiDriver.getwebDriverLocator(objMap.getLocator("txtCRM_Contact"))
			.clear();
			uiDriver.setValue("txtCRM_Contact", DeliveryPhone);
			uiDriver.setValue("txtCRM_SpecialDelivery", Instruction);
			String Alt_Delviery = AlternateDelivery.toUpperCase();
			if (Alt_Delviery.equals("DOORMAN")) {
				uiDriver.click("raddoorman");
			} else {
				uiDriver.click("radnone_CRM");
			}
			// payment detail
			uiDriver.getwebDriverLocator(objMap.getLocator("txtcard_name"))
			.clear();
			uiDriver.setValue("txtcard_name", CardName);
			uiDriver.setValue("radcard_type", CardType);
			uiDriver.setValue("txtcard_number", CardNumber);
			uiDriver.setValue("drpcard_month", CardMonth);
			uiDriver.setValue("drpcard_year", CardYear);
			String Use_Delivery1 = UseDelivery.toUpperCase();
			if (Use_Delivery1.equals("YES")) {
				// uiDriver.click("chkbillingAddress");
				if (CompositeAppDriver.startUp.equalsIgnoreCase("SAFARI")){
					uiDriver.getwebDriverLocator(
							objMap.getLocator("chkbillingAddress")).click();
				}else
					uiDriver.getwebDriverLocator(
							objMap.getLocator("chkbillingAddress")).sendKeys(" ");

			} else {
				uiDriver.setValue("txtbil_Add1", BilAdd1);
				uiDriver.setValue("txtbil_Add2", BilAdd2);
				uiDriver.setValue("txtbil_AptName", BilAptName);
				uiDriver.setValue("txtbil_City", BilCity);
				uiDriver.setValue("txtbil_State", BilState);
				uiDriver.setValue("txtbil_Zipcode", BilZipcode);
				uiDriver.setValue("txtbil_Country", BilCountry);
			}
			uiDriver.click("btncreateAccount");
			SleepUtils.getInstance().sleep(TimeSlab.MEDIUM);
			if (webDriver.findElements(
					By.className(objMap.getLocator("strerror_Detail"))).size() > 0) {
				System.out.println(" Please Enter valid data  ");
				uiDriver.click("lnkerror_List");
				List<WebElement> Errors = webDriver.findElements(By
						.xpath(objMap.getLocator("lsterror_Lists")));
				for (WebElement error1 : Errors) {
					String error_1 = error1.findElement(
							By.xpath(objMap.getLocator("strerror_Text1")))
							.getText();
					// System.out.println(error_1);
					String error_2 = error1.findElement(
							By.xpath(objMap.getLocator("strerror_Text2")))
							.getText();
					// System.out.println(error_2);
					String error = error_1 + " " + error_2;
					System.out.println(error);
					RESULT.warning("Create New Customer",
							"User should be able to create new customer",
							"Please Enter Valid Data,Error Found" + error);
				}
				return;
			} else {
				RESULT.passed("Create New Customer",
						"User should be able to create new customer",
				"new customer created sucessfully");
				UserID = EmailCRM1;
				Password = PassCRM;
			}
		} catch (Exception e) {
			RESULT.failed("Create new customer CRM function", "Create new customer CRM function should be successful", "Create new customer CRM is failed");
		}
	}

	public void popup_ClickcancelOnAlert() {
		if (uiDriver.popup_isAlertPresent(5)) {
			Alert alt = webDriver.switchTo().alert();
			alt.dismiss();
		}
	}

	/**
	 * Function Name: FD_payment_CRM Purpose: Select or add or delete or edit
	 * credit card or EBT or Checking Account details Created By: Avani Thakkar
	 * Created Date: 17th June,2015 Modified By: Modified Date:
	 * 
	 * @param Chooseoptn
	 *            Choose action you want to perform: Select,Edit,Delete,Add
	 *@param cardDetail
	 *            Provide exact card details to select,edit,delete and if you
	 *            want to add new card provide type:EBT,Credit,Checking
	 *@param NameOnCard
	 *            Name displayed on the credit/EBT card
	 *@param CardOrAcctype
	 *            Type of card:Visa,MasterCard,Amex,Discover or type of account
	 *            for checkinh account:savings,checking
	 *@param routing
	 *            routing number of bank account for adding checking account
	 *@param bank
	 *            Name of Bank for checking account
	 *@param CardNum
	 *            Number of your credit/EBT card
	 *@param ExpiryMnth
	 *            Expire month of your card
	 *@param ExpiryYr
	 *            Expire year of your card
	 *@param CardStreetAdd
	 *            Your Address:street name
	 *@param CardAppNum
	 *            Your Address:Appartment number
	 *@param CardAddLine2
	 *            Your Address:bench mark point or address line 2
	 *@param CardCountry
	 *            Your country of provided address
	 *@param CardCity
	 *            City of of provided address
	 *@param CardState
	 *            State of provided address
	 *@param CardZip
	 *            Zip code of provided address
	 * @return void
	 */

	public void FD_payment_CRM(String Chooseoptn, String cardDetail,
			String NameOnCard, String routing, String Bank,
			String CardOrAcctype, String CardNum, String ExpiryMnth,
			String ExpiryYr, String CardStreetAdd, String CardAppNum,
			String CardAddLine2, String CardCountry, String CardCity,
			String CardState, String CardZip) throws InterruptedException {
		try {
			String choosedOpt = Chooseoptn;
			switch (ChooseOpt.valueOf(choosedOpt)) {
			// select card having details same as given in excel
			case Select:
				// list of all cards and its radio buttons in payment option
				// page
				List<WebElement> CardList = webDriver.findElements(By
						.xpath(objMap.getLocator("lstcardDetails")));
				List<WebElement> CardRadio = webDriver.findElements(By
						.name(objMap.getLocator("lstbtnRadio")));
				// check if no cards added in system
				int a = 0;
				cardDetail = cardDetail.replaceAll("    ", "   ");
				cardDetail = cardDetail.replaceAll("Billing Address   ",
				"Billing Address  ");
				cardDetail = cardDetail.replaceAll("AVS Status:  ",
				"AVS Status:");
				if (CardList.size() == 0) {
					RESULT.failed(
							"Selection of Credit/EBT card/checking account ",
							"Required Credit/EBT card/checking account should get selected",
					"No credit/EBT card/checking account available in list to select");
				} else {
					for (int i = 0, j = 0; i < CardList.size(); i++, j++) {
						// Retrieve one by one all cards details to compare with
						// given card detail
						String details = CardList.get(i).getText();
						// compare with data given by user
						if (details.contains(cardDetail)) {
							a++;
							CardRadio.get(j).click();
							String spltdetails[] = details.split(" ");
							RESULT
							.passed(
									"Selection of Credit/EBT card/checking account",
									"Required Credit/EBT card/checking account should get selected",
									"Selected card/account is:"
									+ spltdetails[0] + "\n"
									+ spltdetails[1] + "\n"
									+ spltdetails[2]);
							break;
						}
					}
					if (a == 0) {
						RESULT
						.failed(
								"Selection of Credit/EBT card/checking account",
								"Required Credit/EBT card/checking account should get selected",
						"No credit/EBT card/checking account matched with given details");
					}
				}
				break;
			case Add:
				// check which type of card details to be added
				WebElement common = null;
				cardDetail = cardDetail.toLowerCase();
				List<WebElement> add = webDriver.findElements(By
						.className(objMap.getLocator("btnaddcarddet")));
				for (WebElement addall : add) {
					String checkcard = addall.getAttribute("href");
					if (checkcard.contains(cardDetail)) {
						common = addall;
						break;
					}
				}
				if (cardDetail.contains("ebt")) {
					// click on add EBT card and provide all details
					common.click();
					// Enter name of account holder
					uiDriver.setValue("txtNameOnCard", NameOnCard);
					// provide account#
					uiDriver.setValue("txtCardNum", CardNum);
					// provide address details
					uiDriver.setValue("txtCardStreetAdd", CardStreetAdd);
					uiDriver.setValue("txtCardAppNum", CardAppNum);
					uiDriver.setValue("txtCardAddLine2", CardAddLine2);
					uiDriver.setValue("drpCardCountry", CardCountry);
					uiDriver.setValue("txtCardCity", CardCity);
					uiDriver.setValue("drpCardState", CardState);
					uiDriver.setValue("txtCardZip", CardZip);

				} else if (cardDetail.contains("credit")) {
					// click on add credit card and provide all field values
					common.click();
					wait.until(ExpectedConditions.visibilityOfElementLocated(By.name(objMap.getLocator("txtNameOnCard"))));
					// Enter name of account holder
					uiDriver.setValue("txtNameOnCard", NameOnCard);
					// provide type of card
					uiDriver.setValue("drpCardtype", CardOrAcctype);

					// provide account #
					uiDriver.setValue("txtCardNum", CardNum);
					uiDriver.setValue("drpExpiryMnth", ExpiryMnth); // enter
					// expiry
					// month of
					// card
					uiDriver.setValue("drpExpiryYr", ExpiryYr); // Enter expiry
					// year of card
					// provide address details
					uiDriver.setValue("txtCardStreetAdd", CardStreetAdd);
					uiDriver.setValue("txtCardAppNum", CardAppNum);
					uiDriver.setValue("txtCardAddLine2", CardAddLine2);
					uiDriver.setValue("drpCardCountry", CardCountry);
					uiDriver.setValue("txtCardCity", CardCity);
					uiDriver.setValue("drpCardState", CardState);
					uiDriver.setValue("txtCardZip", CardZip);
					//Could not put size() check as locator might be by id or by name or classname
					if (uiDriver.getwebDriverLocator(
							objMap.getLocator("chkCrmAvsStatus")).isDisplayed()) {
						uiDriver.click("chkCrmAvsStatus"); // check checkbox of
						// AVS status
					}
				} else if (cardDetail.contains("checking")) {
					common.click();
					// provide all field values
					uiDriver.setValue("txtNameOnCard", NameOnCard); // Enter
					// name of
					// account
					// holder
					// select account type
					if (CardOrAcctype.equalsIgnoreCase("Checking")) {
						uiDriver.click("radchecking");
					} else if (CardOrAcctype.equalsIgnoreCase("savings")) {
						uiDriver.click("radsavings");
					} else {
						RESULT.failed(
								"Addition of Credit/EBT card/checking account",
								"Credit/EBT card/checking account should get added with given details",
						"You have entered invalid account type or card type");
						return;
					}
					uiDriver.setValue("txtCardNum", CardNum);// provide account
					// #
					uiDriver.setValue("txtcardverif", CardNum);// verify
					// provided
					// account #
					uiDriver.setValue("txtroutenum", routing);// provide routing
					// #
					uiDriver.setValue("txtbank", Bank);// provide bank name
					// provide address details
					uiDriver.setValue("txtCardStreetAdd", CardStreetAdd);
					uiDriver.setValue("txtCardAppNum", CardAppNum);
					uiDriver.setValue("txtCardAddLine2", CardAddLine2);
					uiDriver.setValue("txtCardCity", CardCity);
					uiDriver.setValue("txtCardStateE", CardState);
					uiDriver.setValue("txtCardZip", CardZip);
					uiDriver.click("chkacceptTerms"); // accept the terms and
					// conditions of
					// checking account
				} else {
					RESULT.failed(
							"Addition of Credit/EBT card/checking account",
							"Type of card can be only'EBT','Credit','Checking'",
					"Type of card specified is othern than:'EBT','Credit crad','Checking account'. Please select one of this only");
					return;
				}
				// click on save changes
				uiDriver.click("btnCRM_SaveBtn");
				try{
					wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className(objMap.getLocator("btnCRM_SaveBtn"))));
				}catch(Exception e){
					RESULT.failed(
							"Addition of Credit/EBT card/checking account",
							"Credit/EBT card/checking account should get added with given details",
					"one or more given detail is wrong");
					return;
				}
				if (webDriver.getTitle().contains(
				"Select Payment Method")
				|| webDriver.getTitle().contains("Account Details")) {
					RESULT.passed(
							"Adition of Credit/EBT card or Checking account",
							"Credit/EBT card or checking account should get added with given details",
					"Credit/EBT card or checking account with given details added");
				}else{
					RESULT.failed(
							"Addition of Credit/EBT card/checking account",
							"Credit/EBT card/checking account should get added with given details",
					"one or more given detail is wrong");
					return;
				}
				break;
				// delete particular card from list
			case Delete:
				// list of all cards and delete buttons in payment option page
				List<WebElement> CardList2 = webDriver.findElements(By
						.xpath(objMap.getLocator("lstcardDetails")));
				List<WebElement> CardDelete = webDriver.findElements(By
						.xpath(objMap.getLocator("btnCardDelete")));
				int c = 0;
				cardDetail = cardDetail.replaceAll("    ", "   ");
				cardDetail = cardDetail.replaceAll("Billing Address   ",
				"Billing Address  ");
				cardDetail = cardDetail.replaceAll("AVS Status:  ",
				"AVS Status:");
				// compare card of excel with cards fetched from current list
				for (int i = 0, j = 0; i < CardList2.size()
				&& j < CardDelete.size(); i++, j++) {
					String details = CardList2.get(i).getText();
					if (details.contains(cardDetail)) {
						c++;
						CardDelete.get(j).click();
						uiDriver.popup_ClickOkOnAlert();
						String spltdetails[] = details.split(" ");
						RESULT
						.passed(
								"Deletion of Credit/EBT card/Checking account",
								"Desired Credit/EBT card/Checking account should get deleted",
								"Deleted card/account is:"
								+ spltdetails[0] + "\n"
								+ spltdetails[1] + "\n"
								+ spltdetails[2]);
						break;
					}
				}
				if (c == 0) {
					RESULT
					.failed(
							"Deletion of Credit/EBT card/Checking account",
							"Desired Credit/EBT card/Checking account should get deleted",
					"No Credit/EBT card/Checking account matched with given details");
				}
				break;
				// edit particular card with list to edit some fields
			case Edit:
				List<WebElement> CardList1;
				List<WebElement> CardEdit;
				int b = 0;

				// user redirects to payment options from account details tab
				if (uiDriver.isElementPresent(By.className(objMap
						.getLocator("lstpaymentCRM")))) {
					CardList1 = webDriver.findElements(By.className(objMap
							.getLocator("lstpaymentCRM")));
					int eSize = CardList1.size();
					System.out.println(eSize);
					cardDetail = cardDetail.replaceAll("    ", "   ");
					cardDetail = cardDetail.replaceAll("Billing Address   ",
					"Billing Address  ");
					cardDetail = cardDetail.replaceAll("AVS Status:  ",
					"AVS Status:");
					cardDetail = cardDetail.replaceAll("AVS Check:  ",
					"AVS Check: ");
					cardDetail = cardDetail.replaceAll("\\n", "");
					System.out.println("Details:" + cardDetail);
					for (WebElement addall : CardList1) {
						String card_check = addall.getText();
						card_check = card_check.replaceFirst("[0-9]", "");
						card_check = card_check.replaceAll("\\n", "");
						System.out.println("Details:" + card_check);
						if (card_check.startsWith(cardDetail)) {
							b++;
							// click on edit button for respective address
							addall.findElement(
									By.className(objMap
											.getLocator("lsteditCard")))
											.click();
							wait.until(ExpectedConditions.visibilityOfElementLocated(By.name(objMap.getLocator("txtNameOnCard"))));
							break;// break loop if card details found in list
						}
					}
				} else if (uiDriver.isElementPresent(By.xpath(objMap
						.getLocator("lstcardDetails")))
						&& uiDriver.isElementPresent(By.xpath(objMap
								.getLocator("btnCardEdit")))) {
					// list of all cards in payment option page along with edit
					// button
					CardList1 = webDriver.findElements(By.xpath(objMap
							.getLocator("lstcardDetails")));
					CardEdit = webDriver.findElements(By.xpath(objMap
							.getLocator("btnCardEdit")));
					for (int i = 0, j = 0; i < CardList1.size()
					&& j < CardEdit.size(); i++, j++) {
						String details = CardList1.get(i).getText();
						if (details.startsWith(cardDetail)) {
							b++;
							// click on edit button for respective address
							CardEdit.get(j).click();
							break;// break loop if card details found in list
						}
					}
				}
				if (b > 0) {
					uiDriver.setValue("txtNameOnCard", NameOnCard);
					// for credit card
					if (uiDriver.isElementPresent(By.name(objMap
							.getLocator("drpCardtype")))) {
						uiDriver.setValue("drpCardtype", CardOrAcctype);
						uiDriver.setValue("txtCardNum", CardNum);
						uiDriver.setValue("drpExpiryMnth", ExpiryMnth);
						uiDriver.setValue("drpExpiryYr", ExpiryYr);
						uiDriver.setValue("txtCardStreetAdd", CardStreetAdd);
						uiDriver.setValue("txtCardAppNum", CardAppNum);
						uiDriver.setValue("txtCardAddLine2", CardAddLine2);
						uiDriver.setValue("drpCardCountry", CardCountry);
						uiDriver.setValue("drpCardState", CardState);
						if (uiDriver.getwebDriverLocator(
								objMap.getLocator("chkCrmAvsStatus"))
								.isDisplayed()) {
							uiDriver.click("chkCrmAvsStatus");
						}
					}
					// for Checking Account
					else if (uiDriver.isElementPresent(By.xpath(objMap
							.getLocator("radchecking")))) {
						if (CardOrAcctype.equalsIgnoreCase("Checking")) {
							uiDriver.click("radchecking");
						} else if (CardOrAcctype.equalsIgnoreCase("savings")) {
							uiDriver.click("radsavings");
						} else {
							RESULT
							.failed(
									"Edit Checking account",
									"Account type should be either checking or savings",
							"You have entered invalid account type other than checking and savings");
						}
						uiDriver.setValue("txtCardNum", CardNum);// provide
						// account #
						uiDriver.setValue("txtcardverif", CardNum);// verify
						// provided
						// account #
						uiDriver.setValue("txtroutenum", routing);// provide
						// routing #
						uiDriver.setValue("txtbank", Bank);// provide bank name
						// provide address details
						uiDriver.setValue("txtCardStreetAdd", CardStreetAdd);
						uiDriver.setValue("txtCardAppNum", CardAppNum);
						uiDriver.setValue("txtCardAddLine2", CardAddLine2);
						uiDriver.setValue("txtCardStateE", CardState);
					}
					// for EBT card
					else if (uiDriver.isElementPresent(By.name(objMap
							.getLocator("txtCardNum")))) {
						uiDriver.setValue("txtCardNum", CardNum);
						uiDriver.setValue("txtCardStreetAdd", CardStreetAdd);
						uiDriver.setValue("txtCardAppNum", CardAppNum);
						uiDriver.setValue("txtCardAddLine2", CardAddLine2);
						uiDriver.setValue("drpCardCountry", CardCountry);
						uiDriver.setValue("drpCardState", CardState);
					}
					uiDriver.setValue("txtCardCity", CardCity);
					uiDriver.setValue("txtCardZip", CardZip);
					uiDriver.click("btnCRM_SaveBtn");
					//SleepUtils.getInstance().sleep(TimeSlab.MEDIUM);
					uiDriver.wait.until(ExpectedConditions.invisibilityOfElementLocated(By
							.className(objMap.getLocator("btnCRM_SaveBtn"))));
					if (webDriver.getTitle().contains("Edit")) {
						RESULT
						.failed(
								"Edit existing Credit/EBT card/Checking account",
								"Credit/EBT card/checking account should get edited with given details",
						"one or more given detail is incorrect");
						return;
					} else {
						RESULT
						.passed(
								"Edit existing Credit/EBT card/checking",
								"Credit/EBT card or checking account should get updated with given details",
						"Credit/EBT card or checking account updated with given details");
					}
				} else {
					RESULT
					.failed(
							"No card details matched for Edit Credit/EBT card/Checking account",
							"Credit/EBT card/checking account should be found with given details",
					"No credit/EBT card or checking account matched with given details");
					return;
				}
				break;// break case
			}

		} catch (Exception e) {
			RESULT
			.failed(
					"Payment CRM Exception",
					"Desired Credit or EBT card should get deleted/edited/selected/added",
					"Desired Credit or EBT card should get deleted/edited/selected/added and exception caught is " + e.getMessage());
		}

	}

	/**
	 * Function Name: FD_chooseDeliveryAddress_CRM Purpose: Allows Fresh Direct
	 * CRM user to Add, Select, Delete or Edit Delivery Address Created By:
	 * Shraddha Shah Created Date: Modified By: Modified Date:
	 * 
	 * @param CRM_AddSelectSelectPickupDeleteEdit
	 *            Option to be selected for delivery address
	 * @param CRM_UseCustDefault
	 *            Checkbox to use default customer details
	 * @param CRM_ServiceType
	 *            Radio Button to select either Home or Corporate service
	 * @param CRM_CompName
	 *            to be entered if corporate service is chosen
	 * @param CRM_FirstNameTb
	 *            First Name to be entered by user
	 * @param CRM_LastNameTb
	 *            Last Name to be entered by user
	 * @param CRM_StreetAdd
	 *            Street address to be entered by user
	 * @param CRM_AddLine2
	 *            Address to be entered by user
	 * @param CRM_AptName
	 *            Appartment Name to be entered by user
	 * @param CRM_City
	 *            City Name to be entered by user
	 * @param CRM_State
	 *            State to be selected by user from Dropdown list
	 * @param CRM_ZipCode
	 *            Zip Code to be entered by user
	 * @param CRM_Contact
	 *            Contact number to be entered by user
	 * @param CRM_AltContact
	 *            Alternate Contact to be entered by user
	 * @param CRM_SpecialDelivery
	 *            For Special Delivery
	 * @param CRM_DoormanRadioBtn
	 *            Selected by user if product is picked up by Doorman
	 * @param CRM_NeighborRadioBtn
	 *            Selected by user if product is picked up by Neighbour
	 * @param CRM_AltFirstName
	 *            Neighbour's First Name
	 * @param CRM_AltLastName
	 *            Neighbour's First Name
	 * @param CRM_AltApt
	 *            Neighbour's Appartment
	 * @param CRM_AltPhn
	 *            Neighbour's Contact number
	 * @param CRM_SaveCancelBtn
	 *            Save or cancel button to be clicked by user after adding
	 *            Delivery Address Details
	 * @param CRM_SelectAddress
	 *            Address string to map with list of delivery Address
	 **/
	public void FD_chooseDeliveryAddress_CRM(
			String CRM_AddSelectSelectPickupDeleteEdit,
			String CRM_UseCustDefault, String CRM_ServiceType,
			String CRM_CompName, String CRM_FirstNameTb, String CRM_LastNameTb,
			String CRM_StreetAdd, String CRM_AddLine2, String CRM_AptName,
			String CRM_City, String CRM_State, String CRM_ZipCode,
			String CRM_Contact, String CRM_AltContact,
			String CRM_SpecialDelivery, String CRM_DoormanRadioBtn,
			String CRM_NeighborRadioBtn, String CRM_AltFirstName,
			String CRM_AltLastName, String CRM_AltApt, String CRM_AltPhn,
			String CRM_SaveCancelBtn, String CRM_SelectAddress)
	throws InterruptedException, FindFailed

	{
		try {

			String ChoosedOpt = CRM_AddSelectSelectPickupDeleteEdit;
			switch (ChooseOpt.valueOf(ChoosedOpt)) {
			/* Add a new Delivery Address to the list */
			case Add:

				List<WebElement> Listbefore = webDriver.findElements(By
						.name(objMap.getLocator("lstCRM_selectAddressList")));
				int bSize = Listbefore.size();
				uiDriver.click("btnCRM_AddNewAddressBtn");
				if (CRM_UseCustDefault.equalsIgnoreCase("yes")) {
					uiDriver.click("CRM_UseCustDefault");
				}
				String Servicetype = CRM_ServiceType;
				if (Servicetype.equalsIgnoreCase("CORPORATE")) {
					uiDriver.click("radCRM_Commercial");
					uiDriver.setValue("txtCRM_CompName", CRM_CompName);
				} else if (Servicetype.equalsIgnoreCase("HOME")) {
					uiDriver.click("radCRM_Residential");
				}
				uiDriver.setValue("txtCRM_FirstNameTb", CRM_FirstNameTb);
				uiDriver.setValue("txtCRM_LastNameTb", CRM_LastNameTb);
				uiDriver.setValue("txtCRM_StreetAdd", CRM_StreetAdd);
				uiDriver.setValue("txtCRM_AddLine2", CRM_AddLine2);
				uiDriver.setValue("txtCRM_AptName", CRM_AptName);
				uiDriver.setValue("txtCRM_City", CRM_City);
				uiDriver.setValue("drpCRM_State", CRM_State);
				uiDriver.setValue("txtCRM_ZipCode", CRM_ZipCode);
				uiDriver.setValue("txtCRM_Contact", CRM_Contact);
				uiDriver.setValue("txtCRM_AltContact", CRM_AltContact);
				uiDriver
				.setValue("txtCRM_SpecialDelivery", CRM_SpecialDelivery);
				if (CRM_DoormanRadioBtn.equalsIgnoreCase("yes")) {
					uiDriver.click("radCRM_DoormanRadioBtn");
				}
				if (CRM_NeighborRadioBtn.equalsIgnoreCase("yes")) {
					uiDriver.click("radCRM_NeighborRadioBtn");
					uiDriver.setValue("txtCRM_AltFirstName", CRM_AltFirstName);
					uiDriver.setValue("txtCRM_AltLastName", CRM_AltLastName);
					uiDriver.setValue("txtCRM_AltApt", CRM_AltApt);
					uiDriver.setValue("txtCRM_AltPhn", CRM_AltPhn);
				}
				String SaveCancelButton = CRM_SaveCancelBtn;
				if (SaveCancelButton.equalsIgnoreCase("save")) {
					uiDriver.click("btnCRM_SaveBtn");
					System.out.println("Address added Sucessfully!!!!");
					// List<WebElement> Listafter =
					// webDriver.findElements(By.name(objMap.getLocator("lstCRM_selectAddressList")));
					// int aSize = Listafter.size();
					// if (aSize > bSize) {
					RESULT.passed("ChooseAction",
							"Delivery Address should be added to the list",
					"Delivery Address added successfully!!");
					// }
				} else if (SaveCancelButton.equalsIgnoreCase("cancel")) {
					uiDriver.click("btnCRM_CancelBtn");
					System.out.println("Address canceled!!!");
					SleepUtils.getInstance().sleep(TimeSlab.YIELD);
					RESULT.passed("ChooseAction",
							"No change in the list of address",
					"Delivery address canceled");
				} else {
					RESULT.failed("ChooseAction",
							"Error encountered somewhere", "Missing some data");
					return;
				}
				break;

				/* Select Delivery Address from the existing list */
			case Select:

				List<WebElement> addLst = webDriver.findElements(By
						.xpath(objMap.getLocator("strCRM_AddressPath")));
				int iSize = addLst.size();
				List<WebElement> precedingSiblings = webDriver.findElements(By
						.name(objMap.getLocator("radCRM_RadioPath")));
				int jSize = precedingSiblings.size();
				String myadd = CRM_SelectAddress;
				myadd = myadd.replaceAll(":  ", ": ");
				myadd = myadd.replaceAll("#   ", "#  ");
				System.out.println("given:" + myadd);
				System.out.println("Address List Size" + iSize);
				System.out.println("Radio List Size" + jSize);
				int n = 0;
				for (int i = 0, j = 0; i < iSize; i++, j++) {
					String gotadd = addLst.get(i).getText();
					System.out.println(gotadd);
					if (gotadd.startsWith(myadd)) {
						n++;
						System.out.println(gotadd);
						// precedingSiblings.get(j).getAttribute("value");
						precedingSiblings.get(j).click();
						System.out.println(precedingSiblings.get(j)
								.getAttribute("value"));
						System.out.println("Delivery Address selected");
						RESULT.passed("ChooseAction",
								"User should be able add new delivery address",
						"Address selected Sucessfully!!!!");
						break;
					}
				}
				if (n == 0) {
					String Msg = "Address did not match";
					System.out.println(Msg);
					RESULT.failed("ChooseAction",
							"User should be able to select Delivery Address",
							Msg);
					return;
				}

				break;

				/* Select Pickup Location as a Delivery Address */
			case SelectPickup:

				List<WebElement> addLstP = webDriver.findElements(By
						.xpath(objMap.getLocator("strCRM_PickupPath")));
				int pSize = addLstP.size();
				// List<WebElement> Pickup = webDriver.findElements(By
				// .xpath(objMap.getLocator("CRM_PickupRadioPath")));
				List<WebElement> Pickup = webDriver
				.findElements(By.className(objMap
						.getLocator("radCRM_PickupRadioPath")));
				int p1Size = Pickup.size();
				String myaddP = CRM_SelectAddress;
				System.out.println("given:" + myaddP);
				System.out.println(pSize);
				System.out.println(p1Size);

				int p = 0;
				for (int i = 0, j = 0; i < pSize; i++, j++) {
					String gotaddP = addLstP.get(i).getText();
					System.out.println(gotaddP);
					if (gotaddP.startsWith(myaddP)) {
						p++;
						System.out.println(gotaddP);
						Pickup.get(j).click();
						System.out.println("Delivery Address selected");
						RESULT.passed("ChooseAction",
								"User should be able select delivery address",
						"Address selected Sucessfully!!!!");
						break;
					}
				}
				if (p == 0) {
					String Error_Msg2 = "Address did not match";
					System.out.println(Error_Msg2);
					RESULT.failed("ChooseAction",
							"User should be able to select Delivery Address",
							Error_Msg2);
					return;
				}
				break;

				/* Delete Delivery Address from the existing list */
			case Delete:

				List<WebElement> addLst1 = webDriver.findElements(By
						.xpath(objMap.getLocator("strCRM_AddressPath")));
				int dSize = addLst1.size();
				List<WebElement> DelLst = webDriver.findElements(By
						.xpath(objMap.getLocator("lnkCRM_DeleteAddressPath")));
				int d1Size = DelLst.size();
				String myadd1 = CRM_SelectAddress;
				myadd1 = myadd1.replaceAll(":  ", ": ");
				myadd1 = myadd1.replaceAll("#   ", "#  ");
				System.out.println("given:" + myadd1);
				System.out.println(dSize);
				int q = 0;
				for (int i = 0, j = 0; i < dSize && j < d1Size; i++, j++) {
					String gotadd1 = addLst1.get(i).getText();
					// System.out.println("Address:" + gotadd1);
					if (gotadd1.startsWith(myadd1)) {
						q++;
						System.out.println(gotadd1);
						SleepUtils.getInstance().sleep(TimeSlab.YIELD);
						DelLst.get(j).click();
						uiDriver.popup_ClickOkOnAlert();
						System.out.println("Address deleted successfully!!!!");
						RESULT.passed("ChooseAction",
								"User should be able add new delivery address",
						"Address deleted Sucessfully!!!!");
						break;
					}
				}
				if (q == 0) {
					RESULT.failed("ChooseAction",
							"User should be able to delete Delivery Address",
					"Address did not match");
					return;
				}

				break;

				/* Edit existing Delivery Address from the list */
			case Edit:
				List<WebElement> addLst2 = webDriver.findElements(By
						.xpath(objMap.getLocator("strCRM_AddressPath")));
				int eSize = addLst2.size();
				List<WebElement> EditLst = webDriver.findElements(By
						.xpath(objMap.getLocator("lnkCRM_EditAddressPath")));
				int e1Size = EditLst.size();
				String myadd2 = CRM_SelectAddress;
				myadd2 = myadd2.replaceAll(":  ", ": ");
				myadd2 = myadd2.replaceAll("#   ", "#  ");
				// System.out.println("given:"+myadd2);
				System.out.println(eSize);
				System.out.println(e1Size);
				int r = 0;
				for (int i = 0, j = 0; i < eSize && j < e1Size; i++, j++) {
					String gotadd2 = addLst2.get(i).getText();
					// System.out.println("Address:"+gotadd2);
					if (gotadd2.startsWith(myadd2)) {
						r++;
						System.out.println(gotadd2);
						SleepUtils.getInstance().sleep(TimeSlab.YIELD);
						EditLst.get(j).click();
						if (CRM_UseCustDefault.equalsIgnoreCase("yes")) {
							uiDriver.click("CRM_UseCustDefault");
						}
						String Servicetype1 = CRM_ServiceType;
						if (Servicetype1.equalsIgnoreCase("CORPORATE")) {
							uiDriver.click("radCRM_Commercial");
							uiDriver.setValue("txtCRM_CompName", CRM_CompName);
						} else if (Servicetype1.equalsIgnoreCase("HOME")) {
							uiDriver.click("radCRM_Residential");
						}
						uiDriver
						.setValue("txtCRM_FirstNameTb", CRM_FirstNameTb);
						uiDriver.setValue("txtCRM_LastNameTb", CRM_LastNameTb);
						uiDriver.setValue("txtCRM_StreetAdd", CRM_StreetAdd);
						uiDriver.setValue("txtCRM_AddLine2", CRM_AddLine2);
						uiDriver.setValue("txtCRM_AptName", CRM_AptName);
						uiDriver.setValue("txtCRM_City", CRM_City);
						uiDriver.setValue("drpCRM_State", CRM_State);
						uiDriver.setValue("txtCRM_ZipCode", CRM_ZipCode);
						uiDriver.setValue("txtCRM_Contact", CRM_Contact);
						uiDriver.setValue("txtCRM_AltContact", CRM_AltContact);
						uiDriver.setValue("txtCRM_SpecialDelivery",
								CRM_SpecialDelivery);
						if (CRM_DoormanRadioBtn.equalsIgnoreCase("yes")) {
							uiDriver.click("radCRM_DoormanRadioBtn");
						}
						if (CRM_NeighborRadioBtn.equalsIgnoreCase("yes")) {
							uiDriver.click("radCRM_NeighborRadioBtn");
							uiDriver.setValue("txtCRM_AltFirstName",
									CRM_AltFirstName);
							uiDriver.setValue("txtCRM_AltLastName",
									CRM_AltLastName);
							uiDriver.setValue("txtCRM_AltApt", CRM_AltApt);
							uiDriver.setValue("txtCRM_AltPhn", CRM_AltPhn);
						}
						String SaveCancelButton1 = CRM_SaveCancelBtn;
						if (SaveCancelButton1.equalsIgnoreCase("save")) {
							uiDriver.click("btnCRM_SaveBtn");
							SleepUtils.getInstance().sleep(TimeSlab.YIELD);
							if (webDriver
									.getTitle()
									.equals(
									"/ FreshDirect CRM : Checkout > Select Delivery Address /")) {
								System.out
								.println("Address edited Sucessfully!!!!");
								SleepUtils.getInstance().sleep(TimeSlab.YIELD);
								RESULT.passed("ChooseAction",
										"Address should be edited",
								"Address edited successfully!!");
								break;
							}
						} else if (SaveCancelButton1.equalsIgnoreCase("cancel")) {
							uiDriver.click("btnCRM_CancelBtn");
							System.out.println("Address canceled!!!");
							SleepUtils.getInstance().sleep(TimeSlab.YIELD);
							RESULT.passed("ChooseAction",
									"Delivery address should remain unchanged",
							"Delivery address not edited");
							break;
						} else {
							RESULT.failed("ChooseAction",
									"Error encountered somewhere",
							"Missing some data");
							break;
						}
					}
				}
				if (r == 0) {
					RESULT.failed("ChooseAction", "Address should be edited",
					"Address did not match");
					return;
				}
			}
		} catch (Exception e) {
			System.out.println("Please enter valid option");
			RESULT.failed("Error Found", "Delivery Address should be selected",
					"Error occured" + e);
			return;
		}
	}

	/**
	 *Function Name: FD_case_CRM Purpose: Function to create New case in CRM
	 * Created By: Tejas Patel Created Date: Modified By: Modified Date:
	 * 
	 * @param Assigned
	 *            : Name of assigned person for case
	 * @param Queue
	 *            : Queue of case
	 * @param Priority
	 *            : Priority of case
	 * @param Subject
	 *            : Subject of case
	 * @param Reported
	 *            : Reported quantity of case
	 * @param Actual
	 *            : Actual quantity of case
	 * @param Summary
	 *            : Summary of case
	 * @param Notes
	 *            : Notes of case
	 **/
	public void FD_case_CRM(String Assigned, String Queue, String Priority,
			String Subject, String Reported, String Actual, String Summary,
			String Notes) throws InterruptedException {
		try
		{
			try
			{
				//wait for new case link
				uiDriver.wait.until(ExpectedConditions.visibilityOfElementLocated(By
						.linkText(objMap.getLocator("lnknewcase"))));
				//click on New Case link
				if (CompositeAppDriver.startUp.equalsIgnoreCase("IE"))
				{
					uiDriver.getwebDriverLocator(objMap.getLocator("lnknewcase")).sendKeys("\n");
				}
				else
				{
				uiDriver.click("lnknewcase");
				}
			}
			catch(Exception e)
			{
				RESULT.failed("Case creation","Case creation tab should be available","Case creation tab is not available");
				return;
			}
			uiDriver.wait.until(ExpectedConditions.visibilityOfElementLocated(By
					.name(objMap.getLocator("drpqueue"))));
			//verify uer is redirected to case creation page
			if (webDriver.getTitle().contains("/ FreshDirect CRM : New Case /")) {
				RESULT
				.passed("New Case", "Should able to create New Case",
				"Screen for creating New case has been opened successfully");
			} else {

				RESULT.failed("New Case", "Should able to create New Case",
				"Failed to open screen for creating New case");
			}
			// insert data to create a case
			// uiDriver.setValue("drpassigned", Assigned);
			uiDriver.setValue("drpqueue", Queue);
			uiDriver.setValue("drppriority", Priority);
			uiDriver.setValue("drpsubject", Subject);
			uiDriver.getwebDriverLocator(objMap.getLocator("txtreported")).clear();
			uiDriver.setValue("txtreported", Reported);
			uiDriver.getwebDriverLocator(objMap.getLocator("txtactual")).clear();
			uiDriver.setValue("txtactual", Actual);
			uiDriver.setValue("txtsummary", Summary);
			uiDriver.setValue("txtnote", Notes);
			uiDriver.click("chkprivatecase");
			WebElement element = uiDriver.getwebDriverLocator(objMap
					.getLocator("btncreatecase"));
			((JavascriptExecutor) webDriver).executeScript(
					"arguments[0].scrollIntoView(true);", element);
			uiDriver.click("btncreatecase");
			waitForPageLoad();
			//			SleepUtils.getInstance().sleep(TimeSlab.MEDIUM);
			if (webDriver.getTitle().contains(" Case Details")) {
				RESULT.passed("New case Created on current order",
						"Should able to submit New Case",
				"New case created successfully");
			} else {
				RESULT.failed("New case Created on current order",
						"Should able to submit New Case",
				"Failed to create New case");
			}
		}
		catch(Exception e)
		{
			RESULT.failed("Case creation","Case creation should be successful","Case creation is unsuccessful and the exception caught is "+e.getMessage());
		}


	}

	/**
	 *Function Name: FD_SignUP Purpose: Function to create New user in
	 * Freshdirect Application Created By: Tejas Patel Created Date: Modified
	 * By: Modified Date:
	 * 
	 * @param Firstname
	 *            : First name of user
	 * @param Lastname
	 *            : Last name of user
	 * @param ZipCode
	 *            : Zipcode of user
	 * @param ServiceType
	 *            : ServiceType of user
	 * @param Email
	 *            : Email of user
	 * @param ConfirmEmail
	 *            : Email of user
	 * @param PassWord1
	 *            : Password of user
	 * @param ConfirmPassWord1
	 *            : Password of user
	 * @param SecurityQuestion
	 *            : Sequrity question for user
	 * @param CompanyName
	 *            : companyname of user
	 * @param CompanyContactNum
	 *            : phone number of user
	 * @param CompanyFloor
	 *            : Apartment name of user
	 * @param CompanyAddress
	 *            : Address of company
	 * @param CompanyCity
	 *            : City of company
	 * @param CompanyState
	 *            : State of company
	 * @param CompanyZipcode
	 *            : Zipcode of company
	 **/
	public void FD_SignUP(String Firstname, String Lastname, String ZipCode,
			String ServiceType, String Email, String ConfirmEmail,
			String PassWord1, String ConfirmPassWord1, String SecurityQuestion,
			String CompanyName, String CompanyContactNum, String CompanyFloor,
			String CompanyAddress, String CompanyCity, String CompanyState,
			String CompanyZipcode) throws InterruptedException

			{
		try {
			uiDriver.click("btnsignup");
			try{
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(objMap.getLocator("frameiframeSignup"))));
				webDriver.switchTo().frame(uiDriver.getwebDriverLocator(objMap.getLocator("frameiframeSignup")));
			}catch(Exception e){
				uiDriver.launchApplication("https://dev1.freshdirect.com/registration/signup_lite.jsp");
			}
			SleepUtils.getInstance().sleep(TimeSlab.YIELD);
			uiDriver.setValue("txtfirstName", Firstname);
			uiDriver.setValue("txtlastName", Lastname);
			uiDriver.setValue("txtzipCodeSignup", ZipCode);
			String Servicetype = ServiceType.toUpperCase();
			String string1 = "HOME";
			String string2 = "OFFICE";
			if (Servicetype.equals(string1)) {
				uiDriver.click("radhome");
			} else {
				uiDriver.click("radoffice");
			}
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Date date = new Date();
			String Date1 = date.toString();
			Date1 = Date1.replaceAll(" ", "");
			Date1 = Date1.replaceAll(":", "");
			Date1 = Date1.split("IST")[0];
			System.out.println(Date1);
			String Email1 = "Test" + Date1 + "@gmail.com";
			uiDriver.setValue("txtemail", Email1);
			uiDriver.setValue("txtconfirmEmail", Email1);
			uiDriver.setValue("txtpassWord1", PassWord1);
			uiDriver.setValue("txtconfirmPassWord1", ConfirmPassWord1);
			uiDriver.setValue("txtsecurityQuestion", SecurityQuestion);
			RESULT.done("Signup in Freshdirect", "User should able to fill reuired details", "Details have been entered");
			uiDriver.click("btnSIGNup");		
			SleepUtils.getInstance().sleep(TimeSlab.MEDIUM);
			if (webDriver.findElements(
					By.className(objMap.getLocator("strerrorMsgSignup"))).size() > 0) {
				System.out.println(" Please Enter valid data ");
				List<WebElement> Errors = webDriver.findElements(By
						.className(objMap.getLocator("strerrorMsgSignup")));
				for (WebElement error1 : Errors) {
					String error = error1.getText();
					System.out.println(error);
					RESULT
					.warning(
							"SignUP in to FreshDirect",
							"User should be able to SignUP successfully",
							error);
				}
			} else {
				if (webDriver.findElements(
						By.xpath(objMap.getLocator("btnchkAddress"))).size() > 0) {
					uiDriver.setValue("txtcompanyName", CompanyName);
					uiDriver.setValue("txtcompanyPhone", CompanyContactNum);
					uiDriver.setValue("txtcompanyFloor", CompanyFloor);
					uiDriver.setValue("txtcompanyStreetAddress", CompanyAddress);
					uiDriver.setValue("txtcompanyCity", CompanyCity);
					uiDriver.setValue("txtcompanyState", CompanyState);
					uiDriver.setValue("txtcompanyZipCode", CompanyZipcode);
					RESULT.done("Signup in Freshdirect", "User should able to fill reuired details", "Details have been entered");
					uiDriver.click("btnchkAddress");					
					if (webDriver.findElements(By.className(objMap.getLocator("strcompanyErrorMsg"))).size() > 0) 
					{
						System.out.println(" Please Enter valid data ");
						List<WebElement> Errors = webDriver.findElements(By.className(objMap.getLocator("strcompanyErrorMsg")));
						for (WebElement error2 : Errors) 
						{
							String error = error2.getText();
							System.out.println(error);
							RESULT.warning(
									"SignUP in to FreshDirect",
									"User should be able to SignUP successfully",
									error);
							return;
						}
					} else if (webDriver.findElements(By.id(objMap.getLocator("strnoOfficeDelivery"))).size() > 0) {
						RESULT.warning("SignUP in to FreshDirect",
								"User should be able to SignUP successfully",
						"Please enter valid Corporate Address");
						return;
					}
					else {
						RESULT.done("SignUP in to FreshDirect",
								"User should be able to click on CheckAddress button successfully",
						"User has clicked on CheckAddress button successfully with user "+Email1);
					}
				} else {
					RESULT.done("SignUP in to FreshDirect",
							"User should be able to click on SignUP button successfully",
					"User has clicked on signUP button successfully with user "+Email1);
				}
			}
		}

		catch (Exception e) {
			e.printStackTrace();
			RESULT.failed("SignUP in to FreshDirect",
					"User should be able to SignUP successfully",
			"Unable to SignUp successfully");
			return;
		}
			}

	/**
	 * FD_filters Function verify the items coming under different filter are
	 * according to filter type or not. It will take filtername as input
	 * parameter. Password field requires minimum 6 character After pressing
	 * SignUP Button if there is any invalid data than it will print the
	 * Messages.
	 * 
	 */
	private enum Sort_type {
		ATOZ, ZTOA, PRICEUP, PRICEDOWN, DISCOUNT_DOLLARUP, DISCOUNT_DOLLARDOWN
	};

	/**
	 *Function Name: FD_filters Purpose: Function to Filter products with
	 * different filter value Created By: Tejas Patel Created Date: Modified By:
	 * Modified Date:
	 * 
	 * @param filtername
	 *            : Define filte name for filter the products
	 **/
	public void FD_filters(String filtername) throws InterruptedException,
	FindFailed {
		try {
			//Actions builder = new Actions(webDriver);
			String item_name1 = null;
			String item_name2 = null;
			String last_item = null;
			String Direction = null;
			double exactPrice1 = 0;
			double exactPrice2 = 0;
			double exactPrice3 = 0;
			double exactdiscount1 = 0;
			double exactdiscount2 = 0;
			double exactdiscount3 = 0;
			String result1 = null;
			String result2 = null;
			String result3 = null;
			boolean check_flag = false;
			boolean unavailable_flag = false;
			int c;
			String atoz;
			String rowlist = null;

			try
			{
				if ((webDriver.findElements(
						By.xpath(objMap.getLocator("tabyourTopItems"))).size() > 0)
						&& !(webDriver.findElement(
								By.xpath(objMap.getLocator("btnlistReorder")))
								.getAttribute("data-active") == null)) {
					atoz = objMap.getLocator("btnreorderAtoZ");
					rowlist = objMap.getLocator("lstreorderRowlist");

				} else if ((webDriver.findElements(
						By.xpath(objMap.getLocator("tabyourTopItems"))).size() > 0)
						&& webDriver.findElement(
								By.xpath(objMap.getLocator("btngridReorder")))
								.getAttribute("data-active").equalsIgnoreCase(
								"true")) {
					atoz = objMap.getLocator("btnreorderAtoZ");
					rowlist = objMap.getLocator("lstproductReorder");
				} else {
					atoz = objMap.getLocator("btnAtoZ");
					rowlist = objMap.getLocator("lstcoupon_rowlist");
				}
			}
			catch(Exception e)
			{
				RESULT.failed("Filter Function","Grid or List must be one option available for Reorder page",
				"Grid or list option is not available for reorder page");
				return;
			}

			String filter_type = filtername.toUpperCase();
			switch (Sort_type.valueOf(filter_type)) {
			case ATOZ: {

				try
				{
					Direction = webDriver.findElement(By.xpath(atoz)).getAttribute(
					"data-direction");
				}
				catch(Exception e)
				{
					RESULT.warning("Product filters function", "Direction attribute should be available", 
					"Direction attribute is not available");
				}
				// For normal page
				if (!(webDriver.findElements(
						By.xpath(objMap.getLocator("tabyourTopItems"))).size() > 0)) {
					System.out.println("Direction " + Direction);
					if (Direction.equals("true")) {
						try
						{
							uiDriver.getwebDriverLocator(atoz).sendKeys("\n");
							SleepUtils.getInstance().sleep(TimeSlab.MEDIUM);
							wait.until(ExpectedConditions.stalenessOf(webDriver
									.findElement(By.xpath(atoz))));
						}
						catch(Exception e)
						{
							RESULT.warning("Product filters function","A to Z filter should be clicked on normal page",
							"A to z filter is not clicked");
						}
						Direction = webDriver.findElement(By.xpath(atoz))
						.getAttribute("data-direction");
						System.out.println("Direction " + Direction);
					}
				} else {
					try
					{
						if (CompositeAppDriver.startUp.equalsIgnoreCase("SAFARI"))
							uiDriver.getwebDriverLocator(atoz).click();
						else
							uiDriver.getwebDriverLocator(atoz).sendKeys("\n");
						SleepUtils.getInstance().sleep(TimeSlab.MEDIUM);
					}
					catch(Exception e)
					{
						RESULT.failed("Product filters function", "A to Z filters should be clicked on reorder page",
						"A to Z filters is not clicked");
					}
				}
				if (Direction.equals("false")
						||webDriver.findElements(
								By.xpath(objMap.getLocator("tabyourTopItems")))
								.size() > 0) {
					if (webDriver.findElements(
							By.xpath(objMap.getLocator("btnshowAll"))).size() > 0) {
						// click on the show all button
						uiDriver.getwebDriverLocator(
								objMap.getLocator("btnshowAll")).click();
						// wait for the products to load
						try
						{
							wait.until(ExpectedConditions.invisibilityOfElementLocated(By
									.xpath("//button[@data-component='pager-prev']")));
						}
						catch(TimeoutException e)
						{
							RESULT.warning("Product Filters", "Pagination frame should be available", "Pagination frame is not available");
						}

					}
					try
					{
						List<WebElement> row_list = webDriver.findElements(By
								.xpath(rowlist));
						wait.until(ExpectedConditions.visibilityOfAllElements(row_list));

						System.out.println("Number of rows " + row_list.size());

						for (int i = 0; i < row_list.size(); i++) {
							List<WebElement> col_list = row_list.get(i)
							.findElements(By.tagName("li"));
							System.out.println("Number of columns in row is "
									+ col_list.size());

							for (int j = 0; j < col_list.size(); j++) {
								if (i > 0 && j == 0) {
									if(col_list.get(j).getAttribute("class").contains("unavailable")){
										unavailable_flag = true;
										break;
									}
									item_name1 = col_list.get(j).findElement(
											By.tagName("a")).getText();
									c = (last_item).compareTo(item_name1);
									if (c > 0) {
										System.out
										.println("items are not in A-Z order ");
										RESULT
										.failed(
												"Filter products in A-Z order",
												"Products  should display in A-Z order ",
										"Products are not display in A-Z order");
										check_flag = true;
										break;
									}
								}
								if (j == col_list.size() - 1) {
									if(col_list.get(j).getAttribute("class").contains("unavailable")){
										unavailable_flag = true;
										break;
									}
									last_item = col_list.get(j).findElement(
											By.tagName("a")).getText();
									System.out.println("last item " + last_item);
								} else {
									if(col_list.get(j).getAttribute("class").contains("unavailable")&& 
											col_list.get(j+1).getAttribute("class").contains("unavailable")){
										unavailable_flag = true;
										break;
									}

									item_name1 = col_list.get(j).findElement(
											By.tagName("a")).getText();
									item_name2 = col_list.get(j + 1).findElement(
											By.tagName("a")).getText();

									System.out.println("Item name1  " + item_name1);
									System.out.println("Item name2  " + item_name2);
									c = (item_name1).compareTo(item_name2);
									if (c > 0) {
										System.out
										.println("items are not in A-Z order ");
										RESULT
										.failed(
												"Filter products in A-Z order",
												"Products  should display in A-Z order ",
										"Products are not display in A-Z order");
										check_flag = true;
										break;

									}
								}

							}
							if (check_flag == true) {
								break;
							}
							if (unavailable_flag == true) {
								break;
							}

						}
					}catch(Exception e)
					{
						RESULT.failed("Product filters ", "A to Z filter should be applied and product list should be validated ",
						"A to Z filter is not validated");
					}
					if (check_flag == false) {
						System.out.println("Products over ");
						RESULT.passed("Filter products in A-Z order",
								"Products  should display in A-Z order ",
						"Products are  display in A-Z order");
					}
				}
			}
			break;

			case ZTOA: {
				try
				{
					uiDriver.click("btnAtoZ");
					wait.until(ExpectedConditions.stalenessOf(webDriver
							.findElement(By.xpath(objMap.getLocator("btnAtoZ")))));
					Direction = webDriver.findElement(
							By.xpath(objMap.getLocator("btnAtoZ"))).getAttribute(
							"data-direction");
				}
				catch(Exception e)
				{
					RESULT.failed("Filters : Z to A", "A -Z/Z-A should be clicked successfully",
							"A -Z/Z-A is not clicked successfully and the exception caught is "+e.getMessage());
				}

				System.out.println("Direction " + Direction);
				if (Direction.equals("false")) {
					try
					{
						uiDriver.click("btnAtoZ");
						wait.until(ExpectedConditions
								.stalenessOf(webDriver.findElement(By.xpath(objMap
										.getLocator("btnAtoZ")))));
						Direction = webDriver.findElement(
								By.xpath(objMap.getLocator("btnAtoZ")))
								.getAttribute("data-direction");
					}
					catch(Exception e){
						RESULT.failed("Filters : Z to A", "A -Z/Z-A should be clicked successfully",
								"A -Z/Z-A is not clicked successfully and the exception caught is "+e.getMessage());
					}

					System.out.println("Direction " + Direction);
				}

				if (Direction.equals("true")) {
					if (webDriver.findElements(
							By.xpath(objMap.getLocator("btnshowAll"))).size() > 0) {
						// click on the show all button
						uiDriver.getwebDriverLocator(
								objMap.getLocator("btnshowAll")).click();
						// wait for the products to load
						try
						{
							wait.until(ExpectedConditions
									.invisibilityOfElementLocated(By
											.xpath("//button[@data-component='pager-prev']")));
						}
						catch(TimeoutException e)
						{
							RESULT.warning("Product filters","Pagination frame should be available on application ",
							"Pagination frame is not available");
						}

					}
					try
					{
						List<WebElement> row_list = webDriver.findElements(By
								.xpath(objMap.getLocator("lstcoupon_rowlist")));
						wait.until(ExpectedConditions
								.visibilityOfAllElements(row_list));

						System.out.println("Number of rows " + row_list.size());

						for (int i = 0; i < row_list.size(); i++) {
							List<WebElement> col_list = row_list.get(i)
							.findElements(By.tagName("li"));
							System.out.println("Number of columns in row is "
									+ col_list.size());

							for (int j = 0; j < col_list.size(); j++) {
								if (i > 0 && j == 0) {
									if(col_list.get(j).getAttribute("class").contains("unavailable")){
										unavailable_flag = true;
										break;
									}
									item_name1 = col_list.get(j).findElement(
											By.tagName("a")).getText();
									c = (last_item).compareTo(item_name1);
									if (c < 0) {
										System.out
										.println("items are not in Z-A order ");
										RESULT
										.failed(
												"Filter products in Z-A order",
												"Products  should display in Z-A order ",
										"Products are not display in Z-A order");
										check_flag = true;
										break;
									}
								}
								if (j == col_list.size() - 1) {
									if(col_list.get(j).getAttribute("class").contains("unavailable")){
										unavailable_flag = true;
										break;
									}
									last_item = col_list.get(j).findElement(
											By.tagName("a")).getText();
									System.out.println("last item " + last_item);
								} else {
									if(col_list.get(j).getAttribute("class").contains("unavailable")&& 
											col_list.get(j+1).getAttribute("class").contains("unavailable")){
										unavailable_flag = true;
										break;
									}
									item_name1 = col_list.get(j).findElement(
											By.tagName("a")).getText();
									item_name2 = col_list.get(j + 1).findElement(
											By.tagName("a")).getText();

									System.out.println("Item name1  " + item_name1);
									System.out.println("Item name2  " + item_name2);
									c = (item_name1).compareTo(item_name2);
									if (c < 0) {
										System.out
										.println("items are not in Z-A order");
										RESULT
										.failed(
												"Filter products in Z-A order",
												"Products  should display in Z-A order ",
										"Products are not display in Z-A order");
										check_flag = true;
										break;
									}
								}
							}
							if (check_flag == true) {
								break;
							}
							if (unavailable_flag == true) {
								break;
							}
						}
					}
					catch(Exception e)
					{
						RESULT.failed("Product filters ", "Z to A filter should be applied and product list should be validated ",
						"Z to A  filter is not validated");
					}

					if (check_flag == false) {
						System.out.println("Products over ");
						RESULT.passed("Filter products in Z-A order",
								"Products  should display in Z-A order ",
						"Products are  display in Z-A order");
					}
				}
			}
			break;
			case PRICEUP: {
				try
				{
					uiDriver.click("btnprice");
					wait.until(ExpectedConditions.stalenessOf(webDriver
							.findElement(By.xpath(objMap.getLocator("btnprice")))));
					Direction = webDriver.findElement(
							By.xpath(objMap.getLocator("btnprice"))).getAttribute(
							"data-direction");
				}
				catch(Exception e)
				{
					RESULT.failed("Product Filters", "Price up should be clicked successfully",
							"Price up is not clicked successfully and the exception caught is "+e.getMessage());
				}
				System.out.println("Direction " + Direction);
				if (Direction.equals("true")) {
					try
					{
						uiDriver.click("btnprice");
						wait.until(ExpectedConditions.stalenessOf(webDriver
								.findElement(By
										.xpath(objMap.getLocator("btnprice")))));
						Direction = webDriver.findElement(
								By.xpath(objMap.getLocator("btnprice")))
								.getAttribute("data-direction");
					}
					catch(Exception e)
					{
						RESULT.failed("Product Filters", "Price up should be clicked successfully",
								"Price up is not clicked successfully and the exception caught is "+e.getMessage());
					}
					System.out.println("Direction " + Direction);
				}
				if (Direction.equals("false")) {
					if (webDriver.findElements(
							By.xpath(objMap.getLocator("btnshowAll"))).size() > 0) {
						// click on the show all button
						uiDriver.getwebDriverLocator(
								objMap.getLocator("btnshowAll")).click();
						// wait for the products to load
						try
						{
							wait.until(ExpectedConditions
									.invisibilityOfElementLocated(By
											.xpath("//button[@data-component='pager-prev']")));
						}
						catch(TimeoutException e)
						{
							RESULT.warning("Product filters : PRICEUP", "Pagination frame should be available",
							"Pagination frame is not available");
						}
					}

					try
					{
						List<WebElement> row_list = webDriver.findElements(By.xpath(objMap.getLocator("lstcoupon_rowlist")));
						wait.until(ExpectedConditions.visibilityOfAllElements(row_list));
						System.out.println("Number of rows " + row_list.size());
						for (int i = 0; i < row_list.size(); i++) {
							List<WebElement> col_list = row_list.get(i)
							.findElements(By.tagName("li"));
							System.out.println("Number of columns in row is "
									+ col_list.size());

							for (int j = 0; j < col_list.size(); j++) {
								if (i > 0 && j == 0) {
									if(col_list.get(j).getAttribute("class").contains("unavailable")){
										unavailable_flag = true;
										break;
									}
									item_name1 = col_list
									.get(j)
									.findElement(
											By
											.xpath(objMap
													.getLocator("strprice")))
													.getText();
									item_name1 = item_name1.split("\\/")[0];
									result1 = item_name1.replaceAll("[$]", "");
									exactPrice1 = Double.parseDouble(result1);
									System.out.println("price1 " + exactPrice1);
									if (exactPrice3 > exactPrice1) {
										System.out
										.println("items are not in PriceUP order");
										RESULT
										.failed(
												"Filter products in PriceUP order",
												"Products  should display in PriceUP order ",
										"Products are not display in PriceUP order");
										check_flag = true;
										break;
									}
								}
								if (j == col_list.size() - 1) {
									if(col_list.get(j).getAttribute("class").contains("unavailable")){
										unavailable_flag = true;
										break;
									}
									last_item = col_list.get(j).findElement(By.xpath(objMap.getLocator("strprice"))).getText();
									last_item = last_item.split("\\/")[0];
									result3 = last_item.replaceAll("[$]", "");
									exactPrice3 = Double.parseDouble(result3);
									System.out.println("price3 " + exactPrice3);
								} else {
									if(col_list.get(j).getAttribute("class").contains("unavailable")&& 
											col_list.get(j+1).getAttribute("class").contains("unavailable")){
										unavailable_flag = true;
										break;
									}
									item_name1 = col_list.get(j).findElement(By.xpath(objMap.getLocator("strprice"))).getText();
									item_name1 = item_name1.split("\\/")[0];
									result1 = item_name1.replaceAll("[$]", "");
									exactPrice1 = Double.parseDouble(result1);item_name2 = col_list.get(j + 1).findElement(By.xpath(objMap.getLocator("strprice"))).getText();
									item_name2 = item_name2.split("\\/")[0];
									result2 = item_name2.replaceAll("[$]", "");
									exactPrice2 = Double.parseDouble(result2);
									System.out.println("price1 " + exactPrice1);
									System.out.println("price2  " + exactPrice2);
									if (exactPrice2 < exactPrice1) {
										System.out
										.println("items are not in PriceUP order");
										RESULT
										.failed(
												"Filter products in PriceUP order",
												"Products  should display in PriceUP order ",
										"Products are not display in PriceUP order");
										check_flag = true;
										break;
									}
								}

							}
							if (check_flag == true) {
								break;
							}
							if (unavailable_flag == true) {
								break;
							}

						}
					}
					catch(Exception e)
					{
						RESULT.failed("Product Filters : PRICEUP ", "PRICE UP filter validation should be successful",
						"PRICE UP filter validation is not successful");
					}

					if (check_flag == false) {
						System.out.println("Products over ");
						RESULT.passed("Filter products in PriceUP order",
								"Products  should display in PriceUP order ",
						"Products are  display in PriceUP order");
					}
				}
			}
			break;
			case PRICEDOWN: {

				try
				{
					uiDriver.click("btnprice");
					wait.until(ExpectedConditions.stalenessOf(webDriver
							.findElement(By.xpath(objMap.getLocator("btnprice")))));
					Direction = webDriver.findElement(
							By.xpath(objMap.getLocator("btnprice"))).getAttribute(
							"data-direction");
				}
				catch(Exception e)
				{
					RESULT.failed("Product Filters : PRICEDOWN", "Price down should be clicked successfully",
							"Price down is not clicked successfully and the exception caught is "+e.getMessage());
				}
				System.out.println("Direction " + Direction);
				if (Direction.equals("false")) {
					try
					{
						uiDriver.click("btnprice");
						wait.until(ExpectedConditions.stalenessOf(webDriver
								.findElement(By
										.xpath(objMap.getLocator("btnprice")))));
						Direction = webDriver.findElement(
								By.xpath(objMap.getLocator("btnprice")))
								.getAttribute("data-direction");
					}
					catch(Exception e)
					{
						RESULT.failed("Product Filters : PRICEDOWN", "Price down should be clicked successfully",
								"Price down is not clicked successfully and the exception caught is "+e.getMessage());
					}

					System.out.println("Direction " + Direction);
				}

				if (Direction.equals("true")) {
					if (webDriver.findElements(
							By.xpath(objMap.getLocator("btnshowAll"))).size() > 0) {
						// click on the show all button
						uiDriver.getwebDriverLocator(
								objMap.getLocator("btnshowAll")).click();
						// wait for the products to load
						try
						{
							wait.until(ExpectedConditions
									.invisibilityOfElementLocated(By
											.xpath("//button[@data-component='pager-prev']")));
						}
						catch(TimeoutException e)
						{
							RESULT.warning("Product filters : PRICEDOWN", "Pagination frame should be available",
							"Pagination frame is not available");
						}

					}

					try
					{
						List<WebElement> row_list = webDriver.findElements(By
								.xpath(objMap.getLocator("lstcoupon_rowlist")));
						wait.until(ExpectedConditions
								.visibilityOfAllElements(row_list));
						System.out.println("Number of rows " + row_list.size());
						for (int i = 0; i < row_list.size(); i++) {
							List<WebElement> col_list = row_list.get(i)
							.findElements(By.tagName("li"));
							System.out.println("Number of columns in row is "
									+ col_list.size());

							for (int j = 0; j < col_list.size(); j++) {
								if (i > 0 && j == 0) {
									if(col_list.get(j).getAttribute("class").contains("unavailable")){
										unavailable_flag = true;
										break;
									}
									item_name1 = col_list
									.get(j)
									.findElement(
											By
											.xpath(objMap
													.getLocator("strprice")))
													.getText();
									item_name1 = item_name1.split("\\/")[0];
									result1 = item_name1.replaceAll("[$]", "");
									exactPrice1 = Double.parseDouble(result1);
									System.out.println("price1 " + exactPrice1);
									if (exactPrice3 < exactPrice1) {
										System.out
										.println("items are not in PRICEDOWN order");
										RESULT
										.failed(
												"Filter products in PRICEDOWN order",
												"Products  should display in PRICEDOWN order ",
										"Products are not display in PRICEDOWN order");
										check_flag = true;
										break;
									}
								}
								if (j == col_list.size() - 1) {
									if(col_list.get(j).getAttribute("class").contains("unavailable")){
										unavailable_flag = true;
										break;
									}
									last_item = col_list
									.get(j)
									.findElement(
											By
											.xpath(objMap
													.getLocator("strprice")))
													.getText();
									last_item = last_item.split("\\/")[0];
									result3 = last_item.replaceAll("[$]", "");
									exactPrice3 = Double.parseDouble(result3);
									System.out.println("price3 " + exactPrice3);
								} else {
									if(col_list.get(j).getAttribute("class").contains("unavailable")&& 
											col_list.get(j+1).getAttribute("class").contains("unavailable")){
										unavailable_flag = true;
										break;
									}
									item_name1 = col_list
									.get(j)
									.findElement(
											By
											.xpath(objMap
													.getLocator("strprice")))
													.getText();
									item_name1 = item_name1.split("\\/")[0];
									result1 = item_name1.replaceAll("[$]", "");
									exactPrice1 = Double.parseDouble(result1);
									item_name2 = col_list
									.get(j + 1)
									.findElement(
											By
											.xpath(objMap
													.getLocator("strprice")))
													.getText();
									item_name2 = item_name2.split("\\/")[0];
									result2 = item_name2.replaceAll("[$]", "");
									exactPrice2 = Double.parseDouble(result2);
									System.out.println("price1 " + exactPrice1);
									System.out.println("price2  " + exactPrice2);
									if (exactPrice2 > exactPrice1) {
										System.out
										.println("items are not in PRICEDOWN order");
										RESULT
										.failed(
												"Filter products in PRICEDOWN order",
												"Products  should display in PRICEDOWN order ",
										"Products are not display in PRICEDOWN order");
										check_flag = true;
										break;

									}
								}

							}
							if (check_flag == true) {
								break;
							}
							if (unavailable_flag == true) {
								break;
							}

						}
					}
					catch(Exception e)
					{
						RESULT.failed("Product Filters : PRICEDOWN ", "PRICEDOWN filter validation should be successful",
						"PRICEDOWN filter validation is not successful");
					}

					if (check_flag == false) {
						System.out.println("Products over ");
						RESULT.passed("Filter products in PRICEDOWN  order",
								"Products  should display in PRICEDOWNorder ",
						"Products are  display in PRICEDOWN order");
					}
				}

			}
			break;

			case DISCOUNT_DOLLARUP: {
				try
				{
					uiDriver.click("btndiscount_dolar");
					wait.until(ExpectedConditions.stalenessOf(webDriver
							.findElement(By.xpath(objMap
									.getLocator("btndiscount_dolar")))));
					Direction = webDriver.findElement(
							By.xpath(objMap.getLocator("btndiscount_dolar")))
							.getAttribute("data-direction");
				}
				catch(Exception e)
				{
					RESULT.failed("Product Filters : DISCOUNT_DOLLARUP", "DISCOUNT_DOLLARUP should be clicked successfully",
							"DISCOUNT_DOLLARUP is not clicked successfully and the exception caught is "+e.getMessage());
				}
				System.out.println("Direction " + Direction);

				if (Direction.equals("false")) {
					try
					{
						uiDriver.click("btndiscount_dolar");
						wait.until(ExpectedConditions.stalenessOf(webDriver
								.findElement(By.xpath(objMap
										.getLocator("btndiscount_dolar")))));
						Direction = webDriver.findElement(
								By.xpath(objMap.getLocator("btndiscount_dolar")))
								.getAttribute("data-direction");
					}
					catch(Exception e)
					{
						RESULT.failed("Product Filters : DISCOUNT_DOLLARUP", "DISCOUNT_DOLLARUP should be clicked successfully",
								"DISCOUNT_DOLLARUP is not clicked successfully and the exception caught is "+e.getMessage());
					}
					System.out.println("Direction " + Direction);
				}
				if (Direction.equals("true")) {
					if (webDriver.findElements(
							By.xpath(objMap.getLocator("btnshowAll"))).size() > 0) {
						// click on the show all button
						uiDriver.getwebDriverLocator(
								objMap.getLocator("btnshowAll")).click();
						// wait for the products to load
						try
						{
							wait
							.until(ExpectedConditions
									.invisibilityOfElementLocated(By
											.xpath("//button[@data-component='pager-prev']")));
						}
						catch(Exception e)
						{
							RESULT.warning("Product filters : DISCOUNT_DOLLARUP", "Pagination frame should be available",
							"Pagination frame is not available");
						}

					}

					try
					{
						List<WebElement> row_list = webDriver.findElements(By
								.xpath(objMap.getLocator("lstcoupon_rowlist")));
						wait.until(ExpectedConditions
								.visibilityOfAllElements(row_list));

						System.out.println("Number of rows " + row_list.size());

						for (int i = 0; i < row_list.size(); i++) {
							List<WebElement> col_list = row_list.get(i)
							.findElements(By.tagName("li"));
							System.out.println("Number of columns in row is "
									+ col_list.size());

							for (int j = 0; j < col_list.size(); j++) {
								if (i > 0 && j == 0) {
									if(col_list.get(j).getAttribute("class").contains("unavailable")){
										unavailable_flag = true;
										break;
									}
									item_name1 = col_list
									.get(j)
									.findElement(
											By
											.xpath(objMap
													.getLocator("strdolar_discount")))
													.getText();
									item_name1 = item_name1.split(" ")[1];
									result1 = item_name1.replaceAll("[$]", "");
									exactdiscount1 = Double.parseDouble(result1);
									System.out.println("exactdiscount1 "
											+ exactdiscount1);
									if (exactdiscount3 > exactdiscount1) {
										System.out
										.println("items are not in DISCOUNT_DOLARUP order");
										RESULT
										.failed(
												"Filter products in DISCOUNT_DOLARUP order",
												"Products  should display in DISCOUNT_DOLARUP order ",
										"Products are not display in DISCOUNT_DOLARUP order");
										check_flag = true;
										break;
									}
								}
								if (j == col_list.size() - 1) {
									if(col_list.get(j).getAttribute("class").contains("unavailable")){
										unavailable_flag = true;
										break;
									}
									last_item = col_list
									.get(j)
									.findElement(
											By
											.xpath(objMap
													.getLocator("strdolar_discount")))
													.getText();
									last_item = last_item.split(" ")[1];
									result3 = last_item.replaceAll("[$]", "");
									exactdiscount3 = Double.parseDouble(result3);
									System.out.println("exactdiscount3 "
											+ exactdiscount3);
								} else {
									if(col_list.get(j).getAttribute("class").contains("unavailable")&& 
											col_list.get(j+1).getAttribute("class").contains("unavailable")){
										unavailable_flag = true;
										break;
									}
									item_name1 = col_list
									.get(j)
									.findElement(
											By
											.xpath(objMap
													.getLocator("strdolar_discount")))
													.getText();
									item_name1 = item_name1.split(" ")[1];
									result1 = item_name1.replaceAll("[$]", "");
									exactdiscount1 = Double.parseDouble(result1);
									item_name2 = col_list
									.get(j + 1)
									.findElement(
											By
											.xpath(objMap
													.getLocator("strdolar_discount")))
													.getText();

									item_name2 = item_name2.split(" ")[1];
									result2 = item_name2.replaceAll("[$]", "");
									exactdiscount2 = Double.parseDouble(result2);
									System.out.println("exactdiscount1 "
											+ exactdiscount1);
									System.out.println("exactdiscount2  "
											+ exactdiscount2);

									if (exactdiscount1 > exactdiscount2) {
										System.out
										.println("items are not in DISCOUNT_DOLARUP order");
										RESULT
										.failed(
												"Filter products in DISCOUNT_DOLARUP order",
												"Products  should display in DISCOUNT_DOLARUP order ",
										"Products are not display in DISCOUNT_DOLARUP order");
										check_flag = true;
										break;

									}
								}

							}
							if (check_flag == true) {
								break;
							}
							if (unavailable_flag == true) {
								break;
							}

						}
					}
					catch(Exception e)
					{
						RESULT.failed("Product Filters : DISCOUNT_DOLARUP ", "DISCOUNT_DOLARUP filter validation should be successful",
						"DISCOUNT_DOLARUP filter validation is not successful");
					}
					if (check_flag == false) {
						System.out.println("Products over ");
						RESULT
						.passed(
								"Filter products in DISCOUNT_DOLARUP order",
								"Products  should display in DISCOUNT_DOLARUP order ",
						"Products are  display in DISCOUNT_DOLARUP order");
					}
				}
			}
			break;

			case DISCOUNT_DOLLARDOWN: {
				try
				{
					uiDriver.click("btndiscount_dolar");

					wait.until(ExpectedConditions.stalenessOf(webDriver
							.findElement(By.xpath(objMap
									.getLocator("btndiscount_dolar")))));
					Direction = webDriver.findElement(
							By.xpath(objMap.getLocator("btndiscount_dolar")))
							.getAttribute("data-direction");
				}
				catch(Exception e)
				{
					RESULT.failed("Product Filters : DISCOUNT_DOLLARDOWN", "DISCOUNT_DOLLARDOWN should be clicked successfully",
							"DISCOUNT_DOLLARDOWN is not clicked successfully and the exception caught is "+e.getMessage());
				}

				System.out.println("Direction " + Direction);
				if (Direction.equals("true")) {
					try
					{
						uiDriver.click("btndiscount_dolar");
						wait.until(ExpectedConditions.stalenessOf(webDriver
								.findElement(By.xpath(objMap
										.getLocator("btndiscount_dolar")))));
						Direction = webDriver.findElement(
								By.xpath(objMap.getLocator("btndiscount_dolar")))
								.getAttribute("data-direction");
					}
					catch(Exception e)
					{
						RESULT.failed("Product Filters : DISCOUNT_DOLLARDOWN", "DISCOUNT_DOLLARDOWN should be clicked successfully",
								"DISCOUNT_DOLLARDOWN is not clicked successfully and the exception caught is "+e.getMessage());
					}

					System.out.println("Direction " + Direction);
				}
				if (Direction.equals("false")) {
					if (webDriver.findElements(
							By.xpath(objMap.getLocator("btnshowAll"))).size() > 0) {
						// click on the show all button
						uiDriver.getwebDriverLocator(
								objMap.getLocator("btnshowAll")).click();
						// wait for the products to load
						try
						{
							wait.until(ExpectedConditions
									.invisibilityOfElementLocated(By
											.xpath("//button[@data-component='pager-prev']")));
						}
						catch(TimeoutException e)
						{
							RESULT.warning("Product filters : DISCOUNT_DOLLARDOWN", "Pagination frame should be available",
							"Pagination frame is not available");
						}

					}

					try
					{
						List<WebElement> row_list = webDriver.findElements(By
								.xpath(objMap.getLocator("lstcoupon_rowlist")));
						wait.until(ExpectedConditions
								.visibilityOfAllElements(row_list));
						System.out.println("Number of rows " + row_list.size());
						for (int i = 0; i < row_list.size(); i++) {
							List<WebElement> col_list = row_list.get(i)
							.findElements(By.tagName("li"));
							System.out.println("Number of columns in row is "
									+ col_list.size());
							for (int j = 0; j < col_list.size(); j++) {
								if (i > 0 && j == 0) {
									if(col_list.get(j).getAttribute("class").contains("unavailable")){
										unavailable_flag = true;
										break;
									}
									item_name1 = col_list
									.get(j)
									.findElement(
											By
											.xpath(objMap
													.getLocator("strdolar_discount")))
													.getText();
									item_name1 = item_name1.split(" ")[1];
									result1 = item_name1.replaceAll("[$]", "");
									exactdiscount1 = Double.parseDouble(result1);
									System.out.println("exactdiscount1 "
											+ exactdiscount1);
									if (exactdiscount3 < exactdiscount1) {
										System.out
										.println("items are not in DISCOUNT_DOLARDOWN order");
										RESULT
										.failed(
												"Filter products in DISCOUNT_DOLARDOWN order",
												"Products  should display in DISCOUNT_DOLARDOWN order ",
										"Products are not display in DISCOUNT_DOLARDOWN order");
										check_flag = true;
										break;
									}
								}
								if (j == col_list.size() - 1) {
									if(col_list.get(j).getAttribute("class").contains("unavailable")){
										unavailable_flag = true;
										break;
									}
									last_item = col_list
									.get(j)
									.findElement(
											By
											.xpath(objMap
													.getLocator("strdolar_discount")))
													.getText();
									last_item = last_item.split(" ")[1];
									result3 = last_item.replaceAll("[$]", "");
									exactdiscount3 = Double.parseDouble(result3);
									System.out.println("exactdiscount3 "
											+ exactdiscount3);
								} else {
									if(col_list.get(j).getAttribute("class").contains("unavailable")&& 
											col_list.get(j+1).getAttribute("class").contains("unavailable")){
										unavailable_flag = true;
										break;
									}
									item_name1 = col_list
									.get(j)
									.findElement(
											By
											.xpath(objMap
													.getLocator("strdolar_discount")))
													.getText();
									item_name1 = item_name1.split(" ")[1];
									result1 = item_name1.replaceAll("[$]", "");
									exactdiscount1 = Double.parseDouble(result1);
									item_name2 = col_list
									.get(j + 1)
									.findElement(
											By
											.xpath(objMap
													.getLocator("strdolar_discount")))
													.getText();
									item_name2 = item_name2.split(" ")[1];
									result2 = item_name2.replaceAll("[$]", "");
									exactdiscount2 = Double.parseDouble(result2);
									System.out.println("exactdiscount1 "
											+ exactdiscount1);
									System.out.println("exactdiscount2  "
											+ exactdiscount2);

									if (exactdiscount1 < exactdiscount2) {
										System.out
										.println("items are not in DISCOUNT_DOLARDOWN order");
										RESULT
										.failed(
												"Filter products in DISCOUNT_DOLARDOWN order",
												"Products  should display in DISCOUNT_DOLARDOWN order ",
										"Products are not display in DISCOUNT_DOLARDOWN order");
										check_flag = true;
										break;

									}
								}

							}
							if (check_flag == true) {
								break;
							}
							if (unavailable_flag == true) {
								break;
							}

						}
					}
					catch(Exception e)
					{
						RESULT.failed("Product Filters : DISCOUNT_DOLARDOWN ", "DISCOUNT_DOLARDOWN filter validation should be successful",
						"DISCOUNT_DOLARDOWN filter validation is not successful");
					}
					if (check_flag == false) {
						System.out.println("Products over ");
						RESULT
						.passed(
								"Filter products in DISCOUNT_DOLARDOWN order",
								"Products  should display in DISCOUNT_DOLARDOWN order ",
						"Products are  display in DISCOUNT_DOLARDOWN order");
					}
				}

			}
			break;
			}

		} catch (Exception e) {
			RESULT.failed("Product filters Exception","Filters check should be validated successfully",
					"Filters check is not validated and the exception caught is "+e.getMessage());
		}

	}

	/**
	 * Function Name: FD_viewSourceLang Purpose: FD_ viewSourceLang function
	 * verifies the presence of lang attribute in Source Page Created By:
	 * Shraddha Shah Created Date: Modified By: Modified Date:
	 **/

	public void FD_viewSourceLang() {
		String PageSource = webDriver.getPageSource();
		SleepUtils.getInstance().sleep(TimeSlab.LOW);
		try
		{
			WebElement Meta = webDriver.findElement(By
					.xpath(objMap.getLocator("tagmetaLangDetect")));
			String lang = Meta.getAttribute("lang");

			// Verifying lang="es-ES" for spanish language
			if (webDriver.findElements(
					By.cssSelector(objMap.getLocator("btnENGLISH"))).size() > 0) {
				if (lang.equals("es-ES")) {
					RESULT.passed("Verify Source Language",
							"The lang='es-ES'should be available in source HTML",
					"The lang='es-ES' is available in source HTML");
				} else {
					RESULT.failed("Verify Source Language",
							"The lang='es-ES' should be available in source HTML",
					"The lang='es-ES' is not available in source HTML");
				}
			}
			// Verifying lang="en-US" for english language
			else {
				if (lang.equals("en-US")) {
					RESULT
					.passed(
							"Verify Source Language",
							"The lang='en-US' should be available in source HTML",
					"The lang='en-US' in the tag is available in source HTML");
				} else {
					RESULT.failed("Verify Source Language",
							"The lang='en-US' should be available in source HTML",
					"The lang='en-US' is not available in source HTML");
				}
			}
		}
		catch(NoSuchElementException e)
		{
			RESULT.failed("Meta tag in application", "Meta tag should be available", "Meta tag is not available");
		}
		catch(Exception e)
		{
			RESULT.failed("Meta tag in application", "Meta tag should be available", "Meta tag is not available");
		}

	}

	public void FD_passwordValidation(String password)
	throws InterruptedException {
		try{
			String password_Pattern = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*\\S+$).{0,}";
			String password_Symbol = "~`!@#$%^&*() _+=-][}{';:/><";
			final int MIN_PASSWORD_LENGTH = 6;
			final int MAX_PASSWORD_LENGTH = 20;
			int score = 0, length = 0, Symbol=0;
			boolean upperCase = false, lowerCase = false, digits = false, nonAlpha = false;
			boolean ActUpper = false, ActLower = false, ActDigit = false, ActSymbol = false, ActLength = false;
			boolean ExpUpper = false, ExpLower = false, ExpDigit = false, ExpSymbol = false, ExpLength = false;
			length = password.toString().length();
			length = password.trim().length();
			
			// Hint box validation.
			// Check PWD for only Whitespace or Null value.
			password = password.trim();
			if ((password != null) && (password.length() > 0)) {

				char[] aC = password.toCharArray();
				for (char i : aC) {
					// 1 or more Capital letters hint line scratch Out
					// validation.
					if (Character.isUpperCase(i)) {
						ActUpper=true ;
						if (uiDriver.isElementpresentweb(getwebDriverLocator(objMap.getLocator("txtCapitalValidation")))) {
							ExpUpper=true;
							/*RESULT.passed("Validating Password hint ScratchOut",
									"1 or more capital letters hint line should Scratchout",
							"1 or more Capital letters is scratchedOut successfully");
						} else if (!uiDriver.isElementpresentweb(getwebDriverLocator(objMap.getLocator("txtCapitalValidation")))&& Upper == 1) {
							RESULT.failed("Validating Password hint ScratchOut",
									"1 or more capital letters hint line should Scratchout",
							"Password hint validation is Failed: 1 or more capital letters line is not Scratchedout");*/
						}
						// 1 or more letters hint line scratch Out validation.
					} else if (Character.isLowerCase(i)) {
						 ActLower=true;
						if (uiDriver.isElementpresentweb(getwebDriverLocator(objMap.getLocator("txtLetterValidation")))) {
							ExpLower=true;
							/*RESULT.passed("Validating Password hint ScratchOut",
									"1 or more letters hint line should Scratchout",
							"1 or more letters is scratchedOut successfully");
						} else if (!uiDriver.isElementpresentweb(getwebDriverLocator(objMap.getLocator("txtLetterValidation")))&& Lower == 1) {
							RESULT.failed("Validating Password hint ScratchOut",
									"1 or more letters hint line should Scratchout",
							"Password hint validation is Failed: 1 or more letters line is not Scratchedout");*/
						}
						// 1 or more numbers hint line scratch Out validation.
					} else if (Character.isDigit(i)) {
						ActDigit=true;
						if (uiDriver.isElementpresentweb(getwebDriverLocator(objMap.getLocator("txtNumValidation")))) {
							ExpDigit=true;
							/*RESULT.passed("Validating Password hint ScratchOut",
									"1 or more numbers hint line should Scratchout",
							"1 or more numbers is scratchedOut successfully");
						} else if (!uiDriver.isElementpresentweb(getwebDriverLocator(objMap.getLocator("txtNumValidation")))&& Digit == 1) {
							RESULT.failed("Validating Password hint ScratchOut",
									"1 or more numbers hint line should Scratchout",
							"Password hint validation is Failed: 1 or more numbers line is not Scratchedout");*/
						}
						// 1 or more special characters hint line scratch Out
						// validation.
					} else if (password_Symbol.indexOf(String.valueOf(i)) >= 0) {
						Symbol++; ActSymbol=true;
						if (uiDriver.isElementpresentweb(getwebDriverLocator(objMap.getLocator("txtSplCharValidation")))) {
							ExpSymbol=true;
							/*RESULT.passed("Validating Password hint ScratchOut",
									"1  or more special characters hint line should Scratchout",
							"1 or more special characters is scratchedOut successfully");
						} else if (!uiDriver.isElementpresentweb(getwebDriverLocator(objMap.getLocator("txtSplCharValidation"))) && Symbol == 1) {
							RESULT.failed("Validating Password hint ScratchOut",
									"1 or more special characters hint line should Scratchout",
							"Password hint validation is Failed: 1 or more special characters line is not Scratchedout");*/
						}
					}
				}
				// 6 or more characters hint line scratch Out validation.
				if ((length >= MIN_PASSWORD_LENGTH)
						&& (length <= MAX_PASSWORD_LENGTH)) {
					ActLength=true;
					if (uiDriver.isElementpresentweb(getwebDriverLocator(objMap.getLocator("txtLengthValidation")))) {
						ExpLength=true;
						/*RESULT.passed("Validating Password hint ScratchOut",
								"6  or more characters hint line should Scratchout",
						"6 or more characters is scratchedOut successfully");
					} else if (!uiDriver.isElementpresentweb(getwebDriverLocator(objMap.getLocator("txtLengthValidation"))) && Len == 1) {
						RESULT.failed("Validating Password hint ScratchOut",
								"6 or more characters hint line should Scratchout",
						"Password hint validation is Failed: 6 or more characters line is not Scratchedout");*/
					}
				}
				if (ExpUpper==ActUpper && ActLower==ExpLower && ActDigit==ExpDigit && ActSymbol==ExpSymbol && ActLength==ExpLength) {
					RESULT.passed("Password hint Validation",
							"Password hint Validation should be successful for :"+password,
					"Password hint Validation is successful for :"+password);
				} else {
					RESULT.failed("Password hint Validation",
							"Password hint Validation should be successful for :"+password,
							"Password hint Validation is failed for :"+password);
				}
			} else {
				RESULT.failed(
						"Validating Password",
						"password validation should be done successfully",
				"Password validation failed: field is blank or only Whitespace");
			}
			
			// Calculate Score for PWD strength
			// Add +0 to score if PWD length < 6.
			if (length < 6) {
				score = 0;
			}
			// Add +10 to score if PWD length is >6.
			else if (length > 6) {
				score += 10;
			}
			// Add +10 to score if PWD length is <12.
			else if (length < 12) {
				score += 10;
			}else 
				score += 15;
			
			// Add +5 to score if PWD Contains letter (a-z).
			lowerCase = password.matches(".*[a-z]+.*");
			if (lowerCase) {
				score += 5;
			}
			// Add +5 to score if PWD Contains letter (A-Z).
			upperCase = password.matches(".*[A-Z]+.*");
			if (upperCase) {
				score += 5;
			}
			// Add +5 to score if PWD Contains letter (a-z) & letter (A-Z).
			if (upperCase && lowerCase) {
				score += 5;
			}
			// Add +5 to score if PWD Contains digits (0-9).
			digits = password.matches("(.)*(\\d)(.)*");
			if (digits) {
				score += 5;
			}
			// if PWD Contains nonword characters & no whiteSpace Add +15 to
			// score or
			// if PWD Contains no nonword characters & no whiteSpace Add +10 to
			// score.
			boolean whiteSpace = password.matches("(.)*(\\s)(.)*");
			if (!whiteSpace) {
				nonAlpha = password.matches("(.)*(\\W)(.)*");
				if (nonAlpha) {
					score += (Symbol >1) ? 15 : 10;
				}
			}
			// Add +35 to score if PWD Contains all 4. ie digits, nonword
			// characters, upperCase && lowerCase characters.
			if (upperCase && lowerCase && digits && nonAlpha) {
				score += 35;
			}

			// Set score to 0 if PWD contains only whiteSpaces.
			int whiteSpacelength = password.trim().length();
			if (whiteSpacelength == 0) {
				score = 0;
			}
			// Validate for weak strength if Score is <15.
			if (score < 15) {
				if (uiDriver.isDisplayed(("txtPwdWeak"))) {
					RESULT.passed("Validating Strength of a Password",
							"Password Strength should be WEAK",
					"Password Strength WEAK validation is successful");
				} else if (!uiDriver.isDisplayed("txtPwdWeak")) {
					RESULT.failed("Validating Strength of a Password",
							"Password Strength should be WEAK",
					"Password Strength WEAK validation is Failed");
				}
				// Validate for Average strength if Score is >=15 && <=20.
			} else if (score < 20) {
				if (uiDriver.isDisplayed("txtPwdAvg")) {
					RESULT.passed("Validating Strength of a Password",
							"Password Strength should be Average",
					"Password Strength Average validation is successful");
				} else if (!uiDriver.isDisplayed("txtPwdAvg")) {
					RESULT.failed("Validating Strength of a Password",
							"Password Strength should be Average",
					"Password Strength Average validation is Failed");
				}
			}
			// Validate for Strong strength if Score is >20 && <=35.
			else if (score < 35) {
				if (uiDriver.isDisplayed("txtPwdStrong")) {
					RESULT.passed("Validating Strength of a Password",
							"Password Strength should be Strong",
					"Password Strength Strong validation is successful");
				} else if (!uiDriver.isDisplayed("txtPwdStrong")) {
					RESULT.failed("Validating Strength of a Password",
							"Password Strength should be Strong",
					"Password Strength Strong validation is Failed");
				}
				// Validate for Very strong strength if Score is >35.
			} else if (score > 34) {
				if (uiDriver.isDisplayed("txtPwdVryStrong")) {
					RESULT.passed("Validating Strength of a Password",
							"Password Strength should be Very Strong",
					"Password Strength Very Strong validation is successful");
				} else if (uiDriver
						.isDisplayed("txtPwdVryStrong")) {
					RESULT.failed("Validating Strength of a Password",
							"Password Strength should be Very Strong",
					"Password Strength Very Strong validation is Failed");
				}
			}
		}catch(Exception e){
			RESULT.failed(
					"Password Validation Function",
					"password validation should be done successfully",
			"Password validation failed");
		}
	}

	private enum CRMPasswordValidation {
		HOME, CORP, WEB, MODIFY
	};

	public void FD_passwordValidationCRM(String password, String Type)
	throws InterruptedException {
		String type = Type.toUpperCase();
		switch (CRMPasswordValidation.valueOf(type)) {
		case HOME: {
			try {
				uiDriver.click("lnkhome");
				waitForPageLoad();
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText(objMap.getLocator("lnknewCustomer"))));
				uiDriver.click("lnknewCustomer");
				waitForPageLoad();
				wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(objMap.getLocator("txthomeZipcodeCRM"))));
				if (webDriver.findElements(By.xpath(objMap.getLocator("txthomeZipcodeCRM"))).size() > 0) {
					uiDriver.setValue("txthomeZipcodeCRM", "11001");
					uiDriver.click("btnhomeSubmitCRM");
					wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(objMap.getLocator("strdetailsHomeCRM"))));
					if (webDriver.findElements(By.xpath(objMap.getLocator("strdetailsHomeCRM"))).size() > 0) {
						String PWD[] = password.split("\n");
						for (int i = 0; i < PWD.length; i++) {
							uiDriver.setValue("txtnewCustPwdCRM",PWD[i]);
							String Pwd=PWD[i];
							uiDriver.FD_passwordValidation(Pwd);
						}
					}
				}
			} catch (Exception e) {
				RESULT.warning("PWD validation at Create new cust(Home) page",
									"Error in PWD validation at Create new cust(Home) page",
										"Error occurred in PWD validation at Create new cust(Home) page");
			}
		}
		break;
		case CORP: {
			try {
				uiDriver.click("lnkhome");
				waitForPageLoad();
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText(objMap.getLocator("lnknewCustomer"))));
				uiDriver.click("lnknewCustomer");
				waitForPageLoad();
				wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(objMap.getLocator("txtcorpZipcodeCRM"))));
				if (webDriver.findElements(By.xpath(objMap.getLocator("txtcorpZipcodeCRM"))).size() > 0) {
					uiDriver.setValue("txtcorpZipcodeCRM", "11001");
					uiDriver.click("btncorpSubmitCRM");
					wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(objMap.getLocator("strdetailsCorpCRM"))));
					if (webDriver.findElements(By.xpath(objMap.getLocator("strdetailsCorpCRM"))).size() > 0) {
						String PWD[] = password.split("\n");
						for (int i = 0; i < PWD.length; i++) {
							uiDriver.setValue("txtnewCustPwdCRM",PWD[i]);
							String Pwd=PWD[i];
							uiDriver.FD_passwordValidation(Pwd);
						}
					}
				}
			} catch (Exception e) {
				RESULT.warning("PWD validation at Create new cust(Corp) page",
									"Error in PWD validation at Create new cust(Corp) page",
										"Error occurred in PWD validation at Create new cust(Corp) page");
			}
		}
		break;
		case WEB: {
			try {
				uiDriver.click("lnkhome");
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText(objMap.getLocator("lnknewCustomer"))));
				uiDriver.click("lnknewCustomer");
				wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(objMap.getLocator("txtwebZipcodeCRM"))));
				if (webDriver.findElements(By.xpath(objMap.getLocator("txtwebZipcodeCRM"))).size() > 0) {
					uiDriver.setValue("txtwebZipcodeCRM", "11001");
					uiDriver.click("btnwebSubmitCRM");
					wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(objMap.getLocator("strdetailsWebCRM"))));
					if (webDriver.findElements(By.xpath(objMap.getLocator("strdetailsWebCRM"))).size() > 0) {
						String PWD[] = password.split("\n");
						for (int i = 0; i < PWD.length; i++) {
							uiDriver.setValue("txtnewCustPwdCRM",PWD[i]);
							String Pwd=PWD[i];
							uiDriver.FD_passwordValidation(Pwd);
						}
					}
				}
			} catch (Exception e) {
				RESULT.warning("PWD validation at Create new cust(Web) page",
									"Error in PWD validation at Create new cust(Web) page",
										"Error occurred in PWD validation at Create new cust(Web) page");
			}
		}
		break;
		case MODIFY: {
			try {
				uiDriver.click("lnkAccountDetails");
				wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(objMap.getLocator("btnun&PwdEdit"))));
				uiDriver.click("btnun&PwdEdit");
				wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(objMap.getLocator("streditUN&PWD"))));
				if (webDriver.findElements(By.xpath(objMap.getLocator("streditUN&PWD"))).size() > 0) {
					String PWD[] = password.split("\n");
					for (int i = 0; i < PWD.length; i++) {
						uiDriver.setValue("txteditCustPwdCRM",PWD[i]);
						String Pwd=PWD[i];
						uiDriver.FD_passwordValidation(Pwd);
					}
				}
			} catch (Exception e) {
				RESULT.warning("PWD validation at Modify customer page",
									"Error in PWD validation at Modify customer page",
										"Error occurred in PWD validation at Modify customer page");
			}
		}
		}
	}

	public void FD_chooseDeliveryAddress_old(
			String AddSelectSelectPickupDeleteEdit, String FirstNameTb,
			String LastNameTb, String ServiceType, String CompName,
			String StreetAdd, String AptName, String AddLine2, String City,
			String State, String ZipCode, String Contact, String Ext1,
			String AltContact, String Ext2, String SpclDelInstructions,
			String DoormanDelivery, String NeighbourDelivery,
			String AltFirstName, String AltLastName, String AltApt,
			String AltPhone, String AltExtn, String None, String SelectAddress,
			String FlexibilityFlag) throws InterruptedException, FindFailed

			{
		try {

			String ChoosedOpt = AddSelectSelectPickupDeleteEdit;
			switch (ChooseOpt.valueOf(ChoosedOpt)) {
			/* Add a new Delivery Address to the list */
			case Add:
				webDriver.findElement(
						By.xpath(objMap.getLocator("btnaddAddress"))).click();
				uiDriver.waitForPageLoad();
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(objMap.getLocator("txtaddNewAddress"))));
				// uiDriver.click("btnaddAddress");
				uiDriver.setValue("txtfirstName", FirstNameTb);
				uiDriver.setValue("txtlastNameYourAccount", LastNameTb);
				String Servicetype = ServiceType;
				if (Servicetype.equalsIgnoreCase("CORPORATE")) {
					uiDriver.click("radcommercialYourAccountAdd");
					uiDriver.setValue("txtcompName", CompName);
				} else if (Servicetype.equals("HOME")) {
					uiDriver.click("radresidentialYourAccountAdd");
				} else {
					RESULT.warning("Select service type", "Service type should be provided from the allowed values [HOME,CORPORATE]"
							, "Service type is: " + Servicetype);
				}
				uiDriver.setValue("txtstreetAdd", StreetAdd);
				uiDriver.setValue("txtaptName", AptName);
				uiDriver.setValue("txtaddLine2", AddLine2);
				uiDriver.setValue("txtcity", City);
				uiDriver.setValue("txtstate", State);
				webDriver.findElement(By.name("zipcode")).sendKeys(ZipCode);
				//uiDriver.setValue("txtzipCode", ZipCode);
				uiDriver.setValue("txtcontact", Contact);
				uiDriver.setValue("txtext1", Ext1);
				uiDriver.setValue("txtaltContact", AltContact);
				uiDriver.setValue("txtext2", Ext2);
				uiDriver.setValue("txtdelInstructions", SpclDelInstructions);
				//Give atleast one options as Yes
				if(None.equalsIgnoreCase("yes")){
					uiDriver.click("None");
				}else if (DoormanDelivery.equalsIgnoreCase("yes")) {
					uiDriver.click("DoormanDelivery");
				}else if (NeighbourDelivery.equalsIgnoreCase("yes")) {
					uiDriver.click("radneighbourDeliveryOld");
					uiDriver.setValue("txtalternateFName", AltFirstName);
					uiDriver.setValue("txtalternateLName", AltLastName);
					uiDriver.setValue("txtalternateAptName", AltApt);
					uiDriver.setValue("txtalternatePhone", AltPhone);
					uiDriver.setValue("txtalternaltPhone", AltExtn);
				}
				RESULT.done("Address fields",
								"All the given data shoud be entered in relevant Address fields",
									"All the data entered in relevant Address fields");
				// click on save changes button
				uiDriver.click("btnsaveAddress");
				uiDriver.waitForPageLoad();
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(objMap.getLocator("btnaddAddress"))));
				try{
					//check if add new delivery address button is available (if available it means address addition successfull
					if (webDriver.findElements(By.xpath(objMap.getLocator("btnaddAddress"))).size() > 0) {
						RESULT.passed("Add Delivery address",
								"Delivery address addition should be successful through Your Account link",
						"Delivery address addition is successful through Your Account link");
						return;
					}
					else {
						//invalid data error (added by AT)
						String Error_Msg1 = webDriver.findElement(By.xpath(objMap.getLocator("strerrorAddress"))).getText();
						System.out.println(Error_Msg1);
						RESULT.failed("Add Delivery address",
								"User should be able add new delivery address",
								"User is not able to add new delivery address and the error generated is "+Error_Msg1);
					}
				}catch (Exception e) {
					RESULT.failed("Add Delivery address",
							"User should be able add new delivery address",
							"User is not able to add new delivery address and the exception generated is : "+e.getMessage());
				}

				break;

				/* Select Delivery Address from the existing list */
			case Select:

				List<WebElement> addLst = webDriver.findElements(By
						.xpath(objMap.getLocator("lstaddressPathOld")));
				int iSize = addLst.size();
				List<WebElement> precedingSiblings = webDriver.findElements(By
						.xpath(objMap.getLocator("lstradioPathOld")));
				int jSize = precedingSiblings.size();

				String myadd = SelectAddress;
				System.out.println("given:" + myadd);
				System.out.println(iSize);

				String spltAddress[] = myadd.split("\\n");
				String myaddedt = "";
				for (int i = 0; i < spltAddress.length; i++) {
					myaddedt = myaddedt + spltAddress[i] + "\n";
				}
				System.out.println(myaddedt);
				int a = 0;
				for (int i = 0, j = 0; i < iSize; i++, j++) {
					String gotadd = addLst.get(i).getText();
					System.out.println(gotadd);
					if (gotadd.startsWith(myaddedt)) {
						a++;
						precedingSiblings.get(j).getAttribute("value");
						precedingSiblings.get(j).click();
						RESULT.passed("Select Delivery Address",
								"User should be able select Delivery Address",
								gotadd + " Address selected Sucessfully!!!!");
						break;
					}
				}
				if ((a == 0) && (FlexibilityFlag.equalsIgnoreCase("YES"))) {
					precedingSiblings.get(0).click();
					RESULT
					.passed(
							"Select Delivery Address",
							"User should be able to select delivery address",
							myadd+ " Address do not exist in the list.Default First Address selected Sucessfully!!!!");

				} else if ((a == 0) && (FlexibilityFlag.equalsIgnoreCase("NO"))) {
					System.out.println("Address did not match");
					RESULT.failed("Select Delivery Address",
							"User should be able to select Delivery Address",
							myadd + " Address did not match");
					return;
				}
				break;
				/* Select Pickup address as a delivery address */

			case SelectPickup:

				List<WebElement> addLst3 = webDriver.findElements(By
						.xpath(objMap.getLocator("lstselectPickup")));
				int iSize1 = addLst3.size();
				List<WebElement> precedingSiblings1 = webDriver.findElements(By
						.xpath(objMap.getLocator("lstselectPickupRadio")));
				int jSize1 = precedingSiblings1.size();

				String myadd3 = SelectAddress;
				System.out.println("given:" + myadd3);
				System.out.println(iSize1);
				int b = 0;
				for (int i = 0, j = 0; i < iSize1; i++, j++) {
					String gotadd3 = addLst3.get(i).getText();
					System.out.println(gotadd3);
					if (gotadd3.startsWith(myadd3)) {
						b++;
						System.out.println(gotadd3);
						precedingSiblings1.get(j).getAttribute("value");
						precedingSiblings1.get(j).click();
						System.out.println(precedingSiblings1.get(j)
								.getAttribute("value"));
						System.out.println("Delivery Address selected!!!");
						RESULT.passed("Select Pickup address",
								"User should be able select pickup address",
								gotadd3 + " Address selected Sucessfully!!!!");
						break;
					}
				}
				if (b == 0) {
					String Error_Msg2 = "Address did not match";
					System.out.println(Error_Msg2);
					RESULT.failed("Select Pickup address",
							"User should be able to select Pickup Address",
					"Given address did not matched with any of address available in list");
				}
				break;

				/* Delete Delivery Address from the existing list */
			case Delete:

				List<WebElement> addLst1 = webDriver.findElements(By
						.xpath(objMap.getLocator("lstaddress")));
				int dSize = addLst1.size();

				String myadd1 = SelectAddress;

				String spltAddress1[] = myadd1.split("\\n");
				String myaddedt1 = "";
				for (int i = 0; i < spltAddress1.length; i++) {
					myaddedt1 = myaddedt1 + spltAddress1[i] + "\n";
				}

				int c = 0;
				for (int i = 0; i < dSize; i++) {

					String gotadd1 = addLst1.get(i).getText();
					// System.out.println("Address:"+gotadd1);
					if (gotadd1.startsWith(myaddedt1)) {
						c++;
						addLst1.get(i).findElement(By.tagName("input")).click();
						RESULT.passed("Delete Delivery address",
								"User should be able delete new delivery address",
								gotadd1+ " Address deleted Sucessfully!!!!");
						break;
					}
				}
				if (c == 0) {
					RESULT.failed("Delete Delivery address",
							"User should be able to delete Delivery Address",
					"Given address did not matched with any of address available in list");

				}

				break;

				/* Edit existing Delivery Address from the list */
			case Edit:
				List<WebElement> addLst2 = webDriver.findElements(By
						.xpath(objMap.getLocator("lstaddress")));

				int eSize = addLst2.size();

				List<WebElement> editLst2 = webDriver.findElements(By
						.xpath(objMap.getLocator("lsteditButtons")));
				int editbtnSize = editLst2.size();

				String myadd2 = SelectAddress;
				// System.out.println("given:"+myadd1);
				System.out.println(eSize);

				String spltAddress2[] = myadd2.split("\\n");
				String myaddedt2 = "";
				for (int i = 0; i < spltAddress2.length; i++) {
					myaddedt2 = myaddedt2 + spltAddress2[i] + "\n";
				}
				System.out.println(myaddedt2);
				int d = 0;
				for (int i = 0; i < eSize; i++) {

					String gotadd2 = addLst2.get(i).getText();
					// System.out.println("Address:"+gotadd2);
					if (gotadd2.contains(myaddedt2)) {
						d++;
						System.out.println(gotadd2);
						//						addLst2.get(i).findElement(By.xpath("//img[@alt='EDIT']")).click();
						editLst2.get(i).click();
						uiDriver.waitForPageLoad();
						wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(objMap.getLocator("txteditAddress"))));
						uiDriver.setValue("txtfirstName", FirstNameTb);
						uiDriver.setValue("txtlastNameYourAccount", LastNameTb);
						String Servicetype1 = ServiceType;
						if (Servicetype1.equals("CORPORATE")) {
							uiDriver.click("radcommercialYourAccountAdd");
							uiDriver.setValue("txtcompName", CompName);
						}
						else if (Servicetype1.equals("HOME")) {
							uiDriver.click("radresidentialYourAccountAdd");
						}else
							RESULT.warning("Select service type", "Service type should be provided from the allowed values [HOME,CORPORATE]"
									, "Service type is: " + Servicetype1);
						uiDriver.setValue("txtstreetAdd", StreetAdd);
						uiDriver.setValue("txtaptName", AptName);
						uiDriver.setValue("txtaddLine2", AddLine2);
						uiDriver.setValue("txtcity", City);
						uiDriver.setValue("txtstate", State);
						webDriver.findElement(By.name("zipcode")).sendKeys(ZipCode);
						//uiDriver.setValue("txtzipCode", ZipCode);
						uiDriver.setValue("txtcontact", Contact);
						uiDriver.setValue("txtext1", Ext1);
						uiDriver.setValue("txtaltContact", AltContact);
						//						uiDriver.setValue("txtext2", Ext2);
						uiDriver.setValue("txtdelInstructions",
								SpclDelInstructions);
						if (None.equalsIgnoreCase("yes")) {
							uiDriver.click("None");
						}else if (DoormanDelivery.equalsIgnoreCase("yes")) {
							uiDriver.click("DoormanDelivery");
						}else if (NeighbourDelivery.equalsIgnoreCase("yes")) {
							uiDriver.click("NeighbourDelivery");
							uiDriver.setValue("AltFirstName", AltFirstName);
							uiDriver.setValue("AltLastName", AltLastName);
							uiDriver.setValue("AltApt", AltApt);
							uiDriver.setValue("AltPhone", AltPhone);
							uiDriver.setValue("AltExtn", AltExtn);
						}
						RESULT.done("Address fields",
								"All the given data shoud be entered in relevant Address fields",
									"All the data entered in relevant Address fields");
						uiDriver.click("btnsaveAddress");
						uiDriver.waitForPageLoad();
						wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(objMap.getLocator("btnaddAddress"))));
						System.out.println("Address edited successfully!!!");
						if (webDriver.findElements(By.xpath(objMap.getLocator("btnaddAddress"))).size() > 0) {
							SleepUtils.getInstance().sleep(TimeSlab.YIELD);
							RESULT.passed("Edit Delivery address",
									"User should be able edit delivery address",
									gotadd2+" Address edited Sucessfully!!!!");
						} else {
							String Error_Msg3 = webDriver.findElement(By.xpath(objMap.getLocator("strerrorAddress")))
							.getText();
							System.out.println(Error_Msg3);
							RESULT.failed("Edit Delivery address",
									"User should be able edit delivery address",
									Error_Msg3);
						}
						break;
					}
				}
				if (d == 0) {
					RESULT.failed("Edit Delivery address",
							"User should be able edit delivery address",
					"Given address did not matched with any of address available in list");
					return;
				}
				break;
			}
		} catch (Exception e) {
			RESULT.failed(AddSelectSelectPickupDeleteEdit+" delivery address", 
					"Delivery Address should get "+AddSelectSelectPickupDeleteEdit+"ed successfully",
					AddSelectSelectPickupDeleteEdit+" Address failed due to " + e.getMessage());
		}
			}

	public void FD_paymentOption_old(String Chooseoptn, String cardDetail,
			String NameOnCard, String routing, String Bank,
			String CardOrAcctype, String CardNum, String ExpiryMnth,
			String ExpiryYr, String CardStreetAdd, String CardAppNum,
			String CardAddLine2, String CardCountry, String CardCity,
			String CardState, String CardZip, String FlexibilityFlag) {

		try {
			// handle exception if an order is already placed
			if (webDriver.getTitle().endsWith("An order is already placed")) {
				uiDriver.getwebDriverLocator(
						objMap.getLocator("btnalreadyPlaced")).click();
			}
			// select chosen option for operation on details of card/account via
			// switch case
			String choosedOpt = Chooseoptn;
			switch (ChooseOpt.valueOf(choosedOpt)) {
			case Select:
				SleepUtils.getInstance().sleep(TimeSlab.YIELD);
				// select card having details same as given in excel
				List<WebElement> CardList = webDriver.findElements(By
						.xpath(objMap.getLocator("lstcardsOld")));
				// list of all cards in payment option page
				List<WebElement> CardRadio = webDriver.findElements(By
						.xpath(objMap.getLocator("lstradiobtnOld")));
				// To tackle problem due to \n while dealing with excel handle
				// the problem caused due to excel appending extra "/n"
				String mycard = cardDetail;
				// String spltSel[] = mycard1.split("\\n");
				mycard = mycard.replaceAll("\\n", "");
				mycard = mycard.replaceAll(" ", "");
				// String mycard = "";
				// for (int i = 0; i < spltSel.length-1; i++) {
				// mycard = mycard + spltSel[i] + "\n";
				// }
				// mycard=mycard+spltSel[spltSel.length];
				// check if no cards added in system
				int a = 0;
				if (CardList.size() == 0) {
					// System.out.println("No card in list");
					RESULT.failed("Selection of Payment option", mycard
							+ ":Payment option should get selected",
					"No Payment option available in list to select");
				} else {
					// Retrieve one by one all card details to compare with
					// given card detail
					for (int i = 0, j = 0; i < CardList.size(); i++, j++) {
						String details1 = CardList.get(i).findElement(
								By.tagName(objMap
										.getLocator("lstgetCardDetails")))
										.getText();
						System.out.println("Details:" + details1);
						String details = details1.replaceAll("\\n", "");
						details = details.replaceAll(" ", "");
						if (details.startsWith(mycard)) {
							a++;
							CardRadio.get(j).click();
							RESULT.passed("Selection of Payment option", mycard
									+ ":Payment option should get selected",
									details1 + ":Payment option selected");
							break;
						}
					}
					if (a == 0 && FlexibilityFlag.equalsIgnoreCase("Yes")) {
						RESULT
						.warning(
								"Selection of Payment option",
								cardDetail
								+ ":Payment option should get selected",
								"No Payment option matched with given details:"
								+ cardDetail);
						CardRadio.get(0).click();
						String card = CardList.get(0).findElement(
								By.tagName(objMap
										.getLocator("lstgetCardDetails")))
										.getText();
						RESULT.passed(
								"Selection of Payment option",
								cardDetail
								+ ":Payment option should get selected",
								cardDetail
								+ ":card is not available in system so first available card:"
								+ card + " is selected");

					} else if (a == 0 && FlexibilityFlag.equalsIgnoreCase("No")) {
						RESULT.failed("Selection of Payment option", cardDetail
								+ ":Payment option should get selected",
								"No Payment option from list matched with given details:"
								+ mycard);
						return;
					}
				}
				break;
			case Add:
				// click on add new card details
				uiDriver.click("AddCreditCard");
				// provide all field values
				uiDriver.setValue("NameOnCard", NameOnCard);
				uiDriver.setValue("Cardtype", CardOrAcctype);
				uiDriver.setValue("CardNum", CardNum);
				uiDriver.setValue("ExpiryMnth", ExpiryMnth);
				uiDriver.setValue("ExpiryYr", ExpiryYr);
				uiDriver.setValue("CardStreetAdd", CardStreetAdd);
				uiDriver.setValue("CardAppNum", CardAppNum);
				uiDriver.setValue("CardAddLine2", CardAddLine2);
				uiDriver.setValue("CardCountry", CardCountry);
				uiDriver.setValue("CardCity", CardCity);
				uiDriver.setValue("CardState", CardState);
				uiDriver.setValue("CardZip", CardZip);
				// click on save changes
				uiDriver.click("SaveChanges");
				if (webDriver.getTitle().contains("Add Credit Card")) {
					// System.out.println("Card not added");
					RESULT
					.failed(
							"Adition of Credit or EBT card",
							"Credit or EBT card should get added with given details",
					"one or more given detail is wrong");
				} else {
					// System.out.println("card added");
					RESULT
					.passed(
							"Adition of Credit or EBT card",
							"Credit or EBT card should get added with given details",
					"Credit or EBT card with given details added");
				}
				break;
			case AddCheckingAcc:
				// click on add new checking account details
				uiDriver.click("AddcheckingAcc");
				// provide all field values
				uiDriver.setValue("NameOnCard", NameOnCard); // Enter name of
				// account
				// holder
				if (CardOrAcctype.equalsIgnoreCase("Checking")) // select
					// account type
				{
					uiDriver.click("checking");
				} else if (CardOrAcctype.equalsIgnoreCase("savings")) {
					uiDriver.click("savings");
				} else {
					RESULT
					.failed(
							"Invalid account type",
							"Account type should be either checking or savings",
					"You have entered invalid account type other than checking and savings");
				}
				uiDriver.setValue("routenum", routing);// provide routing #
				uiDriver.setValue("CardNum", CardNum);// provide account #
				uiDriver.setValue("cardverif", CardNum);// verify provided
				// account #
				uiDriver.setValue("bank", Bank);// provide bank name provide
				// address details
				uiDriver.setValue("CardStreetAdd", CardStreetAdd);
				uiDriver.setValue("CardAppNum", CardAppNum);
				uiDriver.setValue("CardAddLine2", CardAddLine2);
				uiDriver.setValue("CardCity", CardCity);
				uiDriver.setValue("CardState", CardState);
				uiDriver.setValue("CardZip", CardZip);
				uiDriver.click("acceptTerms"); // accept the terms and
				// conditions of checking
				// account
				// click on save changes
				uiDriver.click("SaveChanges");
				if (webDriver.getTitle().contains("Add Checking Account")) {
					// System.out.println("Card not added");
					RESULT
					.failed(
							"Adition of Checking account",
							"Checking account should get added with given details",
					"one or more given detail is wrong");
				} else {
					// System.out.println("card added");
					RESULT
					.passed(
							"Adition of Checking account",
							"Checking account should get added with given details",
					"Checking account with given details added");
				}
				break;
			case Delete:
				// delete particular card from list
				List<WebElement> CardList2 = webDriver.findElements(By
						.xpath(objMap.getLocator("cards")));
				mycard = cardDetail;
				String spltdel[] = mycard.split("\\n");
				String mycardDel = "";
				for (int i = 0; i < spltdel.length; i++) {
					mycardDel = mycardDel + spltdel[i] + "\n";
				}
				int c = 0;
				// compare card of excel with cards fetched from current list
				for (WebElement cardDel : CardList2) {
					String details = cardDel.findElement(
							By.tagName(objMap.getLocator("getCardDetails")))
							.getText();
					System.out.println("Details:" + details);
					if (details.startsWith(mycardDel)) {
						c++;
						// System.out.println("card details found");
						cardDel.findElement(
								By.tagName(objMap.getLocator("DelBtn")))
								.click();
						RESULT
						.passed(
								"Deletion of Credit or EBT card",
								"Desired Credit or EBT card should get deleted",
						"Desired Credit or EBT card deleted");
						break;
					}
				}
				if (c == 0) {
					RESULT.failed("Deletion of Credit or EBT card",
							"Desired Credit or EBT card should get deleted",
					"No credit or EBT card matched with given details");
				}
				break;

			case Edit:
				// edit particular card with list to edit some fields
				List<WebElement> CardList1 = webDriver.findElements(By
						.xpath(objMap.getLocator("cards")));
				System.out.println("\n Number of items in cart is "
						+ CardList1.size());
				mycard = cardDetail;
				String spltEd[] = mycard.split("\\n");
				String mycardEdt = "";
				int b = 0;
				for (int i = 0; i < spltEd.length; i++) {
					mycardEdt = mycardEdt + spltEd[i] + "\n";
				}

				for (WebElement cardEdit : CardList1) {
					String details = cardEdit.findElement(
							By.tagName(objMap.getLocator("getCardDetails")))
							.getText();
					System.out.println("Details:" + details);

					if (details.startsWith(mycardEdt)) {
						b++;
						System.out.println("card details found");
						cardEdit.findElement(
								By.tagName(objMap.getLocator("editbtn")))
								.click();
						uiDriver.setValue("NameOnCard", NameOnCard);
						uiDriver.setValue("CardOrAcctype", CardOrAcctype);
						uiDriver.setValue("CardNum", CardNum);
						uiDriver.setValue("routenum", routing);// provide
						// routing #
						uiDriver.setValue("CardNum", CardNum);// provide account
						// #
						uiDriver.setValue("cardverif", CardNum);// verify
						// provided
						// account #
						uiDriver.setValue("bank", Bank);// provide bank name
						// provide address
						// details
						uiDriver.setValue("ExpiryMnth", ExpiryMnth);
						uiDriver.setValue("ExpiryYr", ExpiryYr);
						uiDriver.setValue("CardStreetAdd", CardStreetAdd);
						uiDriver.setValue("CardAppNum", CardAppNum);
						uiDriver.setValue("CardAddLine2", CardAddLine2);
						uiDriver.setValue("CardCountry", CardCountry);
						uiDriver.setValue("CardCity", CardCity);
						uiDriver.setValue("CardState", CardState);
						uiDriver.setValue("CardZip", CardZip);
						uiDriver.click("SaveChanges");
						try
						{
							uiDriver.wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className(objMap.getLocator("SaveChanges"))));							
						}
						catch(Exception e)
						{
							RESULT
							.failed(
									"Edit existing Credit or EBT card",
									"Credit or EBT card should get added with given details",
							"one or more given detail is incorrect or error in updating payment detail");
						}						
						if (webDriver.getTitle().contains("Edit Credit Card")) {
							RESULT
							.failed(
									"Edit existing Credit or EBT card",
									"Credit or EBT card should get added with given details",
							"one or more given detail is incorrect");
						}
						 else {
							 						 
							RESULT
							.passed(
									"Edit existing Credit or EBT card",
									"Credit or EBT card should get updated with given details",
							"Credit or EBT card updated with given details");
						}
						}
					//check if no details matched with given one
					if (b == 0) {
						RESULT
						.failed(
								"Edit existing Credit or EBT card",
								"Credit or EBT card should get updated with given details",
						"No credit or EBt card matched with given details");
					}
					break;
				}
				break;
			}

		} catch (Exception e) {
			// System.out.println("\n Exception"+e);
			RESULT.failed(
					"Choose Payment Option",
					"Desired Credit or EBT card should get "+Chooseoptn+"ed successfully",
					"Desired Credit or EBT card is not "+Chooseoptn+"ed and the exception caught is " + e.getMessage());
//			e.printStackTrace();
		}
	}

	public int FD_couponItemQty(String Item) throws InterruptedException {
		//Creating String array of numbers.
		String[] prodQty = new String[]{"ANY","ONE","TWO","THREE","FOUR","FIVE","SIX","SEVEN","EIGHT","NINE","TEN"};
		try {
			//List of all coupon items in the page.
			List<WebElement> CpnItemlst = webDriver.findElements(By.xpath(objMap.getLocator("lstcouponItems")));
			for (int i = 0; i < CpnItemlst.size(); i++) {
				String prodName=CpnItemlst.get(i).getText();
				//Iterating coupon items & comparing with the given data.
				if (prodName.contains(Item)) {
					// Hover on Details & take screen shot
					if (CompositeAppDriver.startUp.equalsIgnoreCase("IE")){
						actions.moveToElement(CpnItemlst.get(i).findElement(By.xpath(objMap.getLocator("lnkdetails")))).build().perform();
					}
					robot.moveToElement(CpnItemlst.get(i).findElement(By.xpath(objMap.getLocator("lnkdetails"))));
					SleepUtils.getInstance().sleep(TimeSlab.YIELD);
					RESULT.done("Hover on coupon details", "tool tip should be displayed", "Tool tip is displayed");
					//Fetching coupon details & changing to upper case.
					String details = CpnItemlst.get(i).findElement(By.xpath(objMap.getLocator("strcouponDetails"))).getAttribute("innerHTML");
					String Detail = details.toUpperCase();
					//Converting string details into list.
					List<String> product= Arrays.asList(Detail.split(" "));
					//Comparing list(product) with array(prodQty) to find a match.
					for (int j = 0; j < product.size(); j++) {
						for (int k = 0; k < prodQty.length; k++) {
							if (product.get(j).equals(prodQty[k])) {
								if (k==0)return 1;
								return k;
							}}
					}}
			}
			//	isDisplayed(sElementName)
		} catch (Exception e) {
			e.printStackTrace();
			RESULT.failed("Error occurred", "Error occurred while getting min qty for coupon item", "Error occurred while getting min qty for coupon item");
		}return 0;
	}

	private enum AddYMALOrCarousal {
		YMAL, CAROUSAL
	};

	public void FD_addYMALOrCarousal(String Item, String ItemType,
			String OrderFlagNewModify, String Quantity, String ClickOrHover,
			String FlexibilityFlag, String GridOrList)
	throws InterruptedException {
		String type = ItemType.toUpperCase();
		switch (AddYMALOrCarousal.valueOf(type)) {
		case YMAL: {
			if (uiDriver.isDisplayed("strymalOrCarosProd")) {
				try {
					uiDriver.FD_addToCart(ClickOrHover, Item, Quantity, OrderFlagNewModify, 
							GridOrList, FlexibilityFlag);
					/*uiDriver.FD_addToCart(Item, OrderFlagNewModify, Quantity,
							ClickOrHover, FlexibilityFlag, "", SalesUnit,
							Packaging);*/
				} catch (Exception e) {
					RESULT.failed("Error encountered",
							"YMAL product should be Added to cart",
							"Error occured" + e);
				}
			} else {
				RESULT.failed("YMAL product",
						"YMAL product should be Displayed",
				"YMAL product is not found");
			}
		}
		break;
		case CAROUSAL: {
			// webDriver.findElement(By.name("FD_LOGO")).click();
			// WebUIDriver.selenium.waitForPageToLoad(PageLoadTime);
			SleepUtils.getInstance().sleep(TimeSlab.YIELD);
			if (uiDriver.isDisplayed("strymalOrCarosProd")) {
				try {uiDriver.FD_addToCart(ClickOrHover, Item, Quantity, OrderFlagNewModify, 
						GridOrList, FlexibilityFlag);
				/*uiDriver.FD_addToCart(Item, OrderFlagNewModify, Quantity,
							ClickOrHover, FlexibilityFlag, "", SalesUnit,
							Packaging);*/
				} catch (Exception e) {
					RESULT.failed("Error encountered",
							"Carousels product should be Added to cart",
							"Error occured" + e);
				}
			} else {
				RESULT.failed("Carousels product",
						"Carousels product should be Displayed",
				"Carousels product is not found");
			}
		}
		break;
		default: {
			RESULT.warning("Operation check",
					"Operation should be either YMAL or CAROUSAL",
			"Correct operation not found");
		}
		break;
		}
	}

	public void FD_giftCardPaymentOption(String ChooseOptn, String CardDetail,
			String NameOnCard, String Routing, String Bank,
			String CardOrAccType, String CardNum, String ExpiryMnth,
			String ExpiryYr, String CardStreetAdd, String CardAppNum,
			String CardAddLine2, String CardCountry, String CardCity,
			String CardState, String CardZip) {
		try {

			// select chosen option for operation on details of card/account via
			// switch case
			String choosedOpt = ChooseOptn;
			switch (ChooseOpt.valueOf(choosedOpt)) {
			// select card having details same as given in excel
			case Select:
				// list of all cards and its radio buttons on payment option
				// page
				List<WebElement> CardList = webDriver.findElements(By
						.xpath(objMap.getLocator("lstcards")));
				List<WebElement> CardRadio = webDriver.findElements(By
						.xpath(objMap.getLocator("lstcardRadBtn")));
				// To handle the problem caused due to excel appending extra
				// "/n"
				String mycard1 = CardDetail;
				String spltSel[] = mycard1.split("\\n");
				String mycard = "";
				for (int i = 0; i < spltSel.length; i++) {
					mycard = mycard + spltSel[i] + "\n";
				}
				// check if no cards added in system
				int a = 0;
				if (CardList.size() == 0) {
					RESULT
					.failed(
							"Selection of Credit/EBT card/checking account ",
							"Required Credit/EBT card/checking account should get selected",
					"No credit/EBT card/checking account available in list to select");
				} else {
					// Retrieve one by one all card details to compare with
					// given card detail
					for (int i = 0, j = 0; i < CardList.size(); i++, j++) {
						String details = CardList.get(i)
						.findElement(
								By.tagName(objMap
										.getLocator("txtgetCardName")))
										.getText();
						if (details.startsWith(mycard)) {
							a++;
							CardRadio.get(j).click();
							String spltdetails[] = details.split(" ");
							RESULT
							.passed(
									"Selection of Credit/EBT card/checking account",
									"Required Credit/EBT card/checking account should get selected",
									"Selected card/account is:"
									+ spltdetails[0] + "\n"
									+ spltdetails[1] + "\n"
									+ spltdetails[2]);
							break;
						}
					}
					if (a == 0) {
						RESULT
						.failed(
								"Selection of Credit/EBT card/checking account",
								"Required Credit/EBT card/checking account should get selected",
						"No credit/EBT card/checking account matched with given details");
					}
				}
				break;
			case Add:
				// click on add new card details
				uiDriver.click("imgAddCreditCard");
				// provide all field values
				uiDriver.setValue("txtnameOnCard", NameOnCard);
				uiDriver.setValue("drpcardtype", CardOrAccType);
				uiDriver.setValue("txtcardNum", CardNum);
				uiDriver.setValue("drpexpiryMnth", ExpiryMnth);
				uiDriver.setValue("drpexpiryYr", ExpiryYr);
				uiDriver.setValue("txtcardStreetAdd", CardStreetAdd);
				uiDriver.setValue("txtcardAppNum", CardAppNum);
				uiDriver.setValue("txtcardAddLine2", CardAddLine2);
				uiDriver.setValue("drpcardCountry", CardCountry);
				uiDriver.setValue("txtcardCity", CardCity);
				uiDriver.setValue("drpcardState", CardState);
				uiDriver.setValue("txtcardZip", CardZip);
				// click on save changes
				uiDriver.click("btnsaveChanges");
				if (webDriver.getTitle().contains("Add Credit Card")) {
					RESULT.failed("Adition of Credit card",
							"Credit card should get added with given details",
					"one or more given detail is wrong");
				} else {
					RESULT.passed("Adition of Credit card",
							"Credit card should get added with given details",
					"Credit card with given details added");
				}
				break;
			case AddCheckingAcc:
				// click on add new checking account details
				uiDriver.click("imgaddcheckingAcc");
				// provide all field values
				uiDriver.setValue("txtnameOnCard", NameOnCard); // Enter name of
				// account
				// holder
				if (CardOrAccType.equalsIgnoreCase("Checking")) // select
					// account type
				{
					uiDriver.click("radchecking");
				} else if (CardOrAccType.equalsIgnoreCase("savings")) {
					uiDriver.click("radsavings");
				} else {
					RESULT
					.failed(
							"Addition of checking account",
							"Checking account should get added with given details",
					"You have entered invalid account type other than checking and savings");
				}
				uiDriver.setValue("txtroutenum", Routing);// provide routing #
				uiDriver.setValue("txtcardNum", CardNum);// provide account #
				uiDriver.setValue("txtcardverif", CardNum);// verify provided
				// account #
				uiDriver.setValue("txtbank", Bank);// provide bank name
				// provide address details
				uiDriver.setValue("txtcardStreetAdd", CardStreetAdd);
				uiDriver.setValue("txtcardAppNum", CardAppNum);
				uiDriver.setValue("txtcardAddLine2", CardAddLine2);
				uiDriver.setValue("txtcardCity", CardCity);
				uiDriver.setValue("drpcardState", CardState);
				uiDriver.setValue("txtcardZip", CardZip);
				// accept the terms and conditions of checking account
				uiDriver.click("chkacceptTerms");
				// click on save changes
				uiDriver.click("btnsaveChanges");
				if (webDriver.getTitle().contains("Add Checking Account")) {
					RESULT
					.failed(
							"Adition of Checking account",
							"Checking account should get added with given details",
					"one or more given detail is wrong. so Checking account has not been added");
				} else {
					RESULT
					.passed(
							"Adition of Checking account",
							"Checking account should get added with given details",
					"Checking account with given details added");
				}
				break;
			case Delete:
				// delete particular card from list
				List<WebElement> CardList2 = webDriver.findElements(By
						.xpath(objMap.getLocator("lstcards")));
				mycard = CardDetail;
				String spltdel[] = mycard.split("\\n");
				String mycardDel = "";
				for (int i = 0; i < spltdel.length; i++) {
					mycardDel = mycardDel + spltdel[i] + "\n";
				}
				int c = 0;
				// compare card of excel with cards fetched from current list
				for (WebElement cardDel : CardList2) {
					String details = cardDel.findElement(
							By.tagName(objMap.getLocator("txtgetCardDetails")))
							.getText();
					if (details.startsWith(mycardDel)) {
						c++;
						cardDel.findElement(
								By.tagName(objMap.getLocator("btndel")))
								.click();
						String spltdetails[] = details.split(" ");
						RESULT
						.passed(
								"Deletion of Credit/EBT card/Checking account",
								"Desired Credit/EBT card/Checking account should get deleted",
								"Deleted card/account is:"
								+ spltdetails[0] + "\n"
								+ spltdetails[1] + "\n"
								+ spltdetails[2]);
						break;
					}
				}
				if (c == 0) {
					RESULT
					.failed(
							"Deletion of Credit/EBT card/Checking account",
							"Desired Credit/EBT card/Checking account should get deleted",
					"Credit/EBT card/Checking account with given details not found");
				}
				break;
			case Edit:
				// edit particular card with list to edit some fields
				List<WebElement> CardList1 = webDriver.findElements(By
						.xpath(objMap.getLocator("lstcards")));
				System.out.println("\n Number of items in cart is "
						+ CardList1.size());
				mycard = CardDetail;
				String spltEd[] = mycard.split("\\n");
				String mycardEdt = "";
				int b = 0;
				for (int i = 0; i < spltEd.length; i++) {
					mycardEdt = mycardEdt + spltEd[i] + "\n";
				}

				for (WebElement cardEdit : CardList1) {
					String details = cardEdit.findElement(
							By.tagName(objMap.getLocator("txtgetCardDetails")))
							.getText();
					System.out.println("Details:" + details);
					if (details.startsWith(mycardEdt)) {
						b++;
						System.out.println("card details found");
						cardEdit.findElement(
								By.tagName(objMap.getLocator("btnedit")))
								.click();
						uiDriver.setValue("txtnameOnCard", NameOnCard);
						uiDriver.setValue("drpcardtype", CardOrAccType);
						uiDriver.setValue("txtcardNum", CardNum);
						uiDriver.setValue("txtroutenum", Routing);// provide
						// routing #
						uiDriver.setValue("txtcardNum", CardNum);// provide
						// account #
						uiDriver.setValue("txtcardverif", CardNum);// verify
						// provided
						// account #
						uiDriver.setValue("txtbank", Bank);// provide bank name
						// provide address details
						uiDriver.setValue("drpexpiryMnth", ExpiryMnth);
						uiDriver.setValue("drpexpiryYr", ExpiryYr);
						uiDriver.setValue("txtcardStreetAdd", CardStreetAdd);
						uiDriver.setValue("txtcardAppNum", CardAppNum);
						uiDriver.setValue("txtcardAddLine2", CardAddLine2);
						uiDriver.setValue("drpcardCountry", CardCountry);
						uiDriver.setValue("txtcardCity", CardCity);
						uiDriver.setValue("drpcardState", CardState);
						uiDriver.setValue("txtcardZip", CardZip);
						uiDriver.click("btnsaveChanges");
						if (webDriver.getTitle().contains("Edit Credit Card")) {
							RESULT
							.failed(
									"Edit existing Credit/EBT card/Checking account",
									"Credit/EBT card/checking account should get edited with given details",
							"one or more given detail is incorrect");
						} else {
							RESULT
							.passed(
									"Edit existing Credit/EBT card/Checking account",
									"Credit/EBT card/checking account should get edited with given details",
							"Credit/EBT card updated with given details");
						}
						if (b == 0) {
							RESULT
							.failed(
									"Edit existing Credit or EBT card",
									"Credit or EBT card should get updated with given details",
							"No credit or EBt card matched with given details");
						}
						break;
					} else {
						RESULT
						.done(
								"Edit existing Credit or EBT card",
								"Credit or EBT card should get updated with given details",
						"Given details did not matched with any existing card details");
					}
				}
				break;
			}

		} catch (Exception e) {
			RESULT
			.failed(
					"Error occured some where",
					"Desired Credit or EBT card should get deleted or edited or selected or added",
					"\n Exception catched:" + e);
		}
	}

	public void FD_tdsValidation() throws InterruptedException {
		try {

			String[] taxable = new String[] {
					"Assorted Breakfast Sandwiches Platter, Large",
					"Breakfast Planner Package (Serves 8-10)",
					"75 Wine Company Cabernet Sauvignon",
			"Chalone Vineyard Pinot Noir Monterey" };
			String[] nonTaxable = new String[] {
					"Baby Mum-Mum Rice Rusks, Vegetable",
			"FreshDirect 'Sides-in-a-Snap' Saag Paneer" };
			String[] stateBottle = new String[] {
					"FreshDirect Frozen Chicken, Feta & Tabouleh",
					"FreshDirect Frozen Veggie 'Fried' Rice",
					"Smart & Simple Frozen Artichoke & Ricotta Ravioli",
			"Smart & Simple Frozen Shrimp & Grits (Frozen)" };
			String[] special = new String[] { "Organic Hass Avocado" };

			List<WebElement> nameList = webDriver.findElements(By.xpath(objMap
					.getLocator("lstproducts")));

			for (int i = 0; i < nameList.size(); i++) {

				String name = nameList.get(i).findElement(
						By.xpath(objMap.getLocator("lnkitemNam"))).getText();


				if (Arrays.asList(taxable).contains(name)) {
					if (nameList.get(i).findElements(
							By.xpath(objMap.getLocator("strtaxableT"))).size() > 0) {
						RESULT.passed("Taxable(T) Item Validation",
								"T should be displayed", "T is displayed");
					} else {
						RESULT.failed("Taxable(T) Item Validation",
								"T should be displayed", "T is not displayed");
					}
				} else if (Arrays.asList(nonTaxable).contains(name)) {
					if (!(nameList.get(i).findElements(
							By.xpath(objMap.getLocator("strtaxableT"))).size() > 0)
							|| !(nameList.get(i).findElements(
									By.xpath(objMap
											.getLocator("strstateBottleD")))
											.size() > 0)
											|| !(nameList.get(i).findElements(
													By.xpath(objMap.getLocator("strspecialS")))
													.size() > 0)) {
						RESULT.passed("NonTaxable Item Validation",
								"No Letter should be displayed",
						"No Letter is displayed");
					} else {
						RESULT.failed("NonTaxable Item Validation",
								"No Letter should be displayed",
						"NonTaxable Item Validation unsuccessful");
					}
				} else if (Arrays.asList(stateBottle).contains(name)) {
					if (nameList.get(i).findElements(
							By.xpath(objMap.getLocator("strstateBottleD")))
							.size() > 0) {
						RESULT.passed("State bottle(D) Item Validation",
								"D should be displayed", "D is displayed");
					} else {
						RESULT.failed("State bottle(D) Item Validation",
								"D should be displayed", "D is not displayed");
					}
				} else if (Arrays.asList(special).contains(name)) {
					if (nameList.get(i).findElements(
							By.xpath(objMap.getLocator("strspecialS"))).size() > 0) {
						RESULT.passed("Special(S) Item Validation",
								"S should be displayed", "S is displayed");
					} else {
						RESULT.failed("Special(S) Item Validation",
								"S should be displayed", "S is not displayed");
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			RESULT.failed("T,D & S Validation",
					"T,D & S Validation should be successful",
					"T,D & S Validation is failed due to " + e.getMessage());
		}
	}

	public File takescreenshot() {
		System.out.println("ENTERED WEUIDRIVER TakeSS method******");
		File scrFile = ((TakesScreenshot) webDriver)
		.getScreenshotAs(OutputType.FILE);
		return scrFile;
	}

	/**
	 * Shut downs the webdriver
	 */

	public void recover() {
		webDriver.quit(); // Shutting down the webdriver.
	}

	/**
	 * Overriding toString() method to return WebUIDriver format string
	 */
	public String toString() {
		return "WebUIDriver()";
	}

	private List<Map> uiMap;

	private String sDynamicValue1 = "_x";
	// private String sDynamicValue2 = "_y";

	private LogUtils logger = new LogUtils(this);
	// private ObjectMap objMap = new ObjectMap();
}
