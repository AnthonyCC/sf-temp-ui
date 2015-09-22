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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
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
import org.openqa.selenium.support.ui.WebDriverWait;
import org.sikuli.script.FindFailed;

import cbf.engine.BaseAppDriver;
import cbf.engine.TestResult.ResultType;
import cbf.harness.Harness;
import cbf.utils.Configuration;
import cbf.utils.LogUtils;
import cbf.utils.SleepUtils;
import cbf.utils.SleepUtils.TimeSlab;

import com.thoughtworks.selenium.SeleniumException;
import com.thoughtworks.selenium.webdriven.WebDriverBackedSelenium;

/**
 * 
 * Extends BaseAppDriver and handles all the common functionalities for
 * webControls like setting the TextBox,Selecting an option in Dropdown ,etc..
 * 
 */

public class WebUIDriver extends BaseAppDriver {

	public String productFullName=null;
	public static TableHandler tableObj;
	public static WebDriverBackedSelenium selenium;
	Actions actions = new Actions(webDriver);
	public static boolean reserve_flag = false;
	public String verify_day, verify_time, prdct_category,prdct_department, verify_subtotal;
	public String verify_products[];
	public String verify_products_quantity[];
	public String Zipcode;
	public WebDriverWait wait = new WebDriverWait(webDriver, 180);
	public Configuration GCONFIG = Harness.GCONFIG;
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
	public void launchApplication(String sURL) {
		if (((String) GCONFIG.get("Browser")).equalsIgnoreCase("SAFARI")) {
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
				e.printStackTrace();
			}
		}
		webDriver.get(sURL);
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
			try {
				getwebDriverLocator(objMap.getLocator(sElement)).click();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
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

		Alert alt = webDriver.switchTo().alert();
		alt.accept();
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
		//Configuration GCONFIG = Harness.GCONFIG;
		if (((String) GCONFIG.get("Browser")).equalsIgnoreCase("FIREFOX")) {
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

		if (((String) GCONFIG.get("Browser")).equalsIgnoreCase("IE")) {
			JavascriptExecutor js = ((JavascriptExecutor) driver);
			String bgcolor = element.getCssValue("backgroundColor");
			for (int i = 0; i < 2; i++) {
				js.executeScript("arguments[0].style.border='4px solid red'",
						element);
			
				Thread.sleep(500);
				js.executeScript("arguments[0].style.border='none'", element);
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
	 * Click/mouseHover the specified element fetching it from the data file,using web-webDriver object
	 * 
	 * @param sValue
	 *            to be clicked or mouseHover
	 */
	public void clickFromData(String sValue) {

		Map result = null;
		String[] parts = null;
		String valueForMouseHOver= null,valueForClick = null;
		//Checks for the underscore in the sValue String.
		if(sValue.contains("_"))
		{
			parts = sValue.split("_");
			valueForClick = parts[parts.length - 1];
			for(int i=1;i<parts.length;i++)
			{
				valueForMouseHOver = parts[i];

				for (Map map : uiMap) 
				{
					String temp = (String) map.get("elementName");
					if (valueForMouseHOver.matches(temp) || valueForMouseHOver.equals(temp)) 
					{
						result = map;
						break;
					}
				}
				if (result != null)
				{
					try
					{
						String loc = (String) result.get("locator");
						if(valueForMouseHOver.matches(valueForClick) || valueForMouseHOver.equals(valueForClick))
						{
							getwebDriverLocator(loc).click();
						}
						else
						{
							Actions action = new Actions(webDriver);
							action.moveToElement(uiDriver.getwebDriverLocator(loc)).build().perform();
							SleepUtils.getInstance().sleep(TimeSlab.YIELD);
						}
					}
					catch(Exception e)
					{
						logger.trace(e.getMessage());
					}
					
				}
			}
		}
		
		else if(sValue.contains("->"))
		{
			parts = sValue.split("->");
			for(int i=0;i<parts.length;i++)
			{
				valueForMouseHOver = parts[i];

				for (Map map : uiMap) 
				{
					String temp = (String) map.get("elementName");
					if (valueForMouseHOver.matches(temp) || valueForMouseHOver.equals(temp)) 
					{
						result = map;
						break;
					}
				}
				if (result != null)
				{
						String loc = (String) result.get("locator");
						getwebDriverLocator(loc).click();
			
				}
			}
		}
		else
		{

			try {
				for (Map map : uiMap) 
				{
					String temp = (String) map.get("elementName");
					if (sValue.matches(temp) || sValue.equals(temp)) 
					{
						result = map;
						break;
					}
				}
				if (result != null)
				{
					String loc = (String) result.get("locator");
					getwebDriverLocator(loc).click();
				}
			}
			catch (NullPointerException e) {

				//e.printStackTrace();
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
     * Function Name: FD_login
     * Purpose: For Login in to FreshDirect application
     * Created By: Avani Thakkar 
     * Created Date: 15th May,2015 
     * Modified By:
     * Modified Date: 
     * 
     * @param UID
     *            txtuseriD for log in
     * @param txtpass
     *            password for particular user
     * @return void 
       */

	public void FD_login(String UID, String PASS) throws InterruptedException {
		// uiDriver.DrawHighlight("userID", webDriver);
		if (webDriver.findElements(By.xpath(objMap.getLocator("btngoAnonymous"))).size() > 0) {
			uiDriver.setValue("txtuserNameAnonymous", UID);
			uiDriver.setValue("txtuserPwdAnonymous", PASS);
			SleepUtils.getInstance().sleep(TimeSlab.YIELD);
			uiDriver.click("btngoAnonymous");
			SleepUtils.getInstance().sleep(TimeSlab.YIELD);
		} 
		else{

			uiDriver.click("btnlogin");		
			uiDriver.setValue("txtuserID", UID);
			uiDriver.setValue("txtpass", PASS);
			SleepUtils.getInstance().sleep(TimeSlab.YIELD);
			uiDriver.click("btnsignin");
			SleepUtils.getInstance().sleep(TimeSlab.YIELD);
		}
		selenium.waitForPageToLoad(PageLoadTime);
	}

	@SuppressWarnings("deprecation")
	public boolean isDisplayed(String sElementName) {
		try {
			selenium.waitForPageToLoad(PageLoadTime);
			while (!uiDriver.getwebDriverLocator(objMap.getLocator(sElementName)).isDisplayed()) {
				SleepUtils.getInstance().sleep(TimeSlab.YIELD);
			}
			return true;
		} catch (NoSuchElementException e) {
			return false;// System.out.println(".........");
		} catch (NullPointerException e) {
			return false;//
		}

	}

	/* Precondition : User should be logged in first */
	private enum filterType {
		Filter, department, special_pref, search, list, no_action
	};

	public void FD_reorderYourList(String YourList, String Department, String SpecialPref, String FlexibilityFlag)
	throws FindFailed, InterruptedException, TimeoutException {
		int i = 0, flag = 0;;
		int count=0;
		String listName,Dept_text, spl_text;
//		String reorder = webDriver.getTitle();
		WebElement listheader;
		WebElement breadcumbs_1;

		// * For multiple data - the component is invoked(different data) For the
		// * first invocation it will click Reorder tab

		try {
			if (!(webDriver.findElements(By.xpath(objMap.getLocator("tabyourList"))).size() > 0)) {
				// click reorder tab
				uiDriver.getwebDriverLocator(objMap.getLocator("lnkreorder")).click();
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(objMap.getLocator("tabyourList"))));
				uiDriver.getwebDriverLocator(objMap.getLocator("tabyourList")).click();
				// Need to check this wait				
			}
			try {
				wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(objMap.getLocator("lstreorderShoppingLists"))));
			} catch (Exception e) {
				System.out.println("Unable to find element");
				e.printStackTrace();
			}
			if(webDriver.findElements(By.xpath(objMap.getLocator("lstreorderShoppingLists"))).size()>0){
				List<WebElement> shoppinglist = webDriver.findElements(By.xpath(objMap.getLocator("lstreorderShoppingLists")));
				if(!shoppinglist.isEmpty()){
					for (i = 0; i < shoppinglist.size(); i++) {
						listName = shoppinglist.get(i).getText();
						System.out.println(listName);
						if (listName.contains(YourList)) {
							System.out.println(shoppinglist.get(i).findElement(By.xpath(objMap.getLocator("lstreorderShoppingLists"))).getText());
							shoppinglist.get(i).findElement(By.xpath(objMap.getLocator("radlistName"))).click();
							try {
								do {
									SleepUtils.getInstance().sleep(TimeSlab.YIELD);
									listheader = uiDriver.getwebDriverLocator(objMap.getLocator("strreorderYourListHeader"));
									count++;
								} while (!listheader.getText().contains(YourList) && count<5);

								if(listheader.getText().contains(YourList)){
									flag=1;
									RESULT.passed("Successfully selectd", YourList+ " should be selectd", YourList+ " selectd");
								}
								else
									RESULT.failed("Successfully selectd", YourList+ " should be selectd", YourList+ " is not selectd");

							} catch (Exception e) {
								System.out.println("yourList Header error;");
								e.printStackTrace();
							}
							break;
						}
					} }
				if (flag == 0) {
					RESULT.warning("List match", YourList+" List should be Available", YourList+" List is not Available");

					if (FlexibilityFlag.equalsIgnoreCase("Yes")||FlexibilityFlag.isEmpty()) {
						shoppinglist.get(1).findElement(By.xpath(objMap.getLocator("radlistName"))).click();
						String defaultList = shoppinglist.get(1).findElement(By.xpath(objMap.getLocator("radlistName"))).getText();
						RESULT.passed("Select List",
								"User should be able to select list",
								defaultList +" Default First List selected Sucessfully!!!!");
					} 
				} 
				if ((flag == 0)&& FlexibilityFlag.equalsIgnoreCase("No")) {
					RESULT.passed("Select List",
							"User should not select any List by default",
					"List is not selected!!!!");
				}

				List<WebElement> departmentList = webDriver.findElements(By.xpath(objMap.getLocator("lstreorderDept")));
				if(!Department.isEmpty()){
					for (i = 0; i < departmentList.size(); i++) {
						Dept_text = departmentList.get(i).getText();
						System.out.println(Dept_text);
						if (Dept_text.contains(Department)) {
							System.out.println(departmentList.get(i).findElement(By.xpath(objMap.getLocator("lstdepartmentName"))).getText());
							departmentList.get(i).findElement(By.xpath(objMap.getLocator("lstdepartmentName"))).click();
							try {
								do {
									SleepUtils.getInstance().sleep(TimeSlab.YIELD);
									breadcumbs_1 = uiDriver.getwebDriverLocator(objMap.getLocator("lstreorderBreadcumbs"));
									count++;
								} while (!breadcumbs_1.getText().contains(Department) && count<5);

								if(breadcumbs_1.getText().contains(Department)){
									RESULT.passed("Successfully selectd", Department+ " should be selectd", Department+ " selectd");
								}
								else
									RESULT.failed("Successfully selectd", Department+ " should be selectd", Department+ " is not selectd");

							} catch (Exception e) {
								System.out.println("Department Breadcumbs error;");
								e.printStackTrace();
							}
							break;
						}
					} 
				}
				if ((webDriver.findElements(By.xpath(objMap.getLocator("lstreorderSpecPref"))).size() > 0) && !SpecialPref.isEmpty()) {
					String Pref[] = SpecialPref.split(",");
					for (int x = 0; x < Pref.length; x++) {
						if ((webDriver.findElements(By.xpath(objMap.getLocator("lstreorderSpecPref"))).size() > 0)) {
							List<WebElement> special_prefList = webDriver.findElements(By.xpath(objMap.getLocator("lstreorderSpecPref")));
							for (i = 0; i < special_prefList.size(); i++) {
								spl_text = special_prefList.get(i).getText();
								if (spl_text.contains(Pref[x])) {
									// Check one example for this and change xpath value
									if (((String) GCONFIG.get("Browser")).equalsIgnoreCase("SAFARI"))
										special_prefList.get(i).findElement(By.xpath(objMap.getLocator("radlistName"))).click();
									else
										special_prefList.get(i).findElement(By.xpath(objMap.getLocator("radlistName"))).sendKeys(Keys.SPACE);
									try {
										do {
											SleepUtils.getInstance().sleep(TimeSlab.YIELD);
											breadcumbs_1 = uiDriver.getwebDriverLocator(objMap.getLocator("lstreorderBreadcumbs"));
											count++;
										} while (!breadcumbs_1.getText().contains(Pref[x])&& count<10);
										if(breadcumbs_1.getText().contains(Pref[x])){
											RESULT.passed("Successfully checked", Pref[x]+ " should be checked", Pref[x]+ " checked");
											break;
										}
										else
											RESULT.failed("Successfully checked", Pref[x]+ " should be checked", Pref[x]+ " is unchecked");

									} catch (Exception e) {
										System.out.println("Special Preference Breadcumbs error;");
										e.printStackTrace();
									}
								}
							}
						}
					}}else{
						RESULT.log("Splpref not found or No splpref data",ResultType.PASSED,
								"Splpref should be visible and splpref data should be provided",
								"Splpref not found because may be there is no Splpref for selected Department "+Department 
								+" or No splpref data given.", true);
					}
			}else{
				RESULT.passed("Find List","User should find YourList","YourList Not found...!!!!");
			}
		} catch (Exception e) {
			e.printStackTrace();
			RESULT.failed("Error", "Element not found", "Element not found");
		}

	}


	private enum topItemsType {
		DEPARTMENT, SEARCH
	};

	public void FD_reorderYourTopItems(String Filter, String DepartmentOrItem , String SpecialPref, String FlexibilityFlag)
	throws FindFailed, InterruptedException, TimeoutException {
		int i = 0;
		int flag = 0;
		int count=0;
		String product_text;
		WebElement breadcumbs_1;
		Filter = Filter.toUpperCase();
		try {
			if (!(webDriver.findElements(By.xpath(objMap.getLocator("tabyourTopItems"))).size() > 0)) {
				// click reorder tab
				uiDriver.getwebDriverLocator(objMap.getLocator("lnkreorder")).click();
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(objMap.getLocator("tabyourTopItems"))));
				uiDriver.getwebDriverLocator(objMap.getLocator("tabyourTopItems")).click();

				try {
					wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(objMap.getLocator("lstreorderDept"))));

				} catch (Exception e) {
					System.out.println("Unable to find element");
					e.printStackTrace();
				}
			}
			else
			{
				try
				{
					wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(objMap.getLocator("tabyourTopItems"))));
					uiDriver.getwebDriverLocator(objMap.getLocator("tabyourTopItems")).click();
					SleepUtils.getInstance().sleep(TimeSlab.YIELD);
					wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(objMap.getLocator("lstreorderDept"))));
				}
				catch(Exception e){
					System.out.println("Error in Your Top Items tab");
				}
			}

			switch (topItemsType.valueOf(Filter)) {
		
			case DEPARTMENT:

				/* Check the user input department from past order */
				List<WebElement> departmentList = webDriver.findElements(By.xpath(objMap.getLocator("lstreorderDept")));
				if(!DepartmentOrItem.isEmpty()){
					for (i = 0; i < departmentList.size(); i++) {
						product_text = departmentList.get(i).getText();
						System.out.println(product_text);
						if (product_text.contains(DepartmentOrItem)) {
							System.out.println(departmentList.get(i).findElement(By.xpath(objMap.getLocator("lstdepartmentName"))).getText());
							departmentList.get(i).findElement(By.xpath(objMap.getLocator("lstdepartmentName"))).click();
							try {
								do {
									SleepUtils.getInstance().sleep(TimeSlab.YIELD);
									breadcumbs_1 = uiDriver.getwebDriverLocator(objMap.getLocator("lstreorderBreadcumbs"));
									count++;
								} while (!breadcumbs_1.getText().contains(DepartmentOrItem) && count<5);

								if(breadcumbs_1.getText().contains(DepartmentOrItem)){
									flag=1;
									RESULT.passed("Successfully selectd", DepartmentOrItem+ " should be selectd", DepartmentOrItem+ " selectd");
								}
								else
									RESULT.failed("Successfully selectd", DepartmentOrItem+ " should be selectd", DepartmentOrItem+ " is not selectd");

							} catch (Exception e) {
								System.out.println("Department Breadcumbs error;");
								e.printStackTrace();
							}
							break;
						}
					} }
				if (flag == 0) {
					RESULT.warning("Department match", DepartmentOrItem+" Departmant should be Available", DepartmentOrItem+" Departmant is not Available");
					
					if (FlexibilityFlag.equalsIgnoreCase("Yes")||FlexibilityFlag.isEmpty()) {
						departmentList.get(1).findElement(By.xpath(objMap.getLocator("lstdepartmentName"))).click();
						String defaultDept = departmentList.get(1).findElement(By.xpath(objMap.getLocator("lstdepartmentName"))).getText();
						RESULT.passed("Select Department",
								"User should be able to select department",
								defaultDept +" Default First department selected Sucessfully!!!!");
					} 
				} 
				if ((flag == 0)&& FlexibilityFlag.equalsIgnoreCase("No")) {
					RESULT.passed("Select Department",
							"User should not select any department by default",
					"Department is not selected!!!!");

				}
				
				if ((webDriver.findElements(By.xpath(objMap.getLocator("lstreorderSpecPref"))).size() > 0) && !SpecialPref.isEmpty()) {
					String Pref[] = SpecialPref.split(",");
					for (int x = 0; x < Pref.length; x++) {
						if ((webDriver.findElements(By.xpath(objMap.getLocator("lstreorderSpecPref"))).size() > 0)) {
							List<WebElement> special_prefList = webDriver.findElements(By.xpath(objMap.getLocator("lstreorderSpecPref")));
							for (i = 0; i < special_prefList.size(); i++) {
								product_text = special_prefList.get(i).getText();
								if (product_text.contains(Pref[x])) {
									// Check one example for this and change xpath value
									//special_prefList.get(i).findElement(By.xpath(objMap.getLocator("radlistName"))).click();
									if (((String) GCONFIG.get("Browser")).equalsIgnoreCase("SAFARI"))
										special_prefList.get(i).findElement(By.xpath(objMap.getLocator("radlistName"))).click();
									else
										special_prefList.get(i).findElement(By.xpath(objMap.getLocator("radlistName"))).sendKeys(Keys.SPACE);
									try {
										do {
											SleepUtils.getInstance().sleep(TimeSlab.YIELD);
											breadcumbs_1 = uiDriver.getwebDriverLocator(objMap.getLocator("lstreorderBreadcumbs"));
											count++;
										} while (!breadcumbs_1.getText().contains(Pref[x])&& count<10);
										if(breadcumbs_1.getText().contains(Pref[x])){
											RESULT.passed("Successfully checked", Pref[x]+ " should be checked", Pref[x]+ " checked");
										break;
										}
										else
											RESULT.failed("Successfully checked", Pref[x]+ " should be checked", Pref[x]+ " is unchecked");

									} catch (Exception e) {
										System.out.println("Special Preference Breadcumbs error;");
										e.printStackTrace();
									}
								}
							}
						}
					}}else{
						RESULT.log("Splpref not found or No splpref data",ResultType.PASSED,
								"Splpref should be visible and splpref data should be provided",
								"Splpref not found because may be there is no Splpref for selected Department "+DepartmentOrItem 
								+" or No splpref data given.", true);
					}

				break;

			case SEARCH:
				/* Check the user input search item from past order */
				uiDriver.FD_searchFunction("QUICKSEARCH", "", "", DepartmentOrItem);
				break;

			}
		} catch (Exception e) {
			e.printStackTrace();
			RESULT.failed("Error", "Unknown errror", "Unknown error");
		}
	}

	public void FD_reorderPastOrder(String filter, String Timeframe,String Department,String SpecialPreference,String FlexibilityFlag,String Product)
	throws FindFailed, InterruptedException, TimeoutException {
		int i = 0;
		int flag = 0;
		int flag_dept=0;
		int flag_pref=0;
		int bread_j = 0;
		int count=0;
		String product_text;
		WebElement breadcumbs_1;

		/*
		 * For multiple data - the component is invoked(different data) For the
		 * first invocation it will click Reorder tab
		 */
		try {
			if (!(webDriver.findElements(By.xpath(objMap.getLocator("tabpastOrder"))).size() > 0)) {
				// click reorder tab
				uiDriver.getwebDriverLocator(objMap.getLocator("lnkreorder")).click();
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("tabpastOrder")));
				uiDriver.getwebDriverLocator(objMap.getLocator("tabpastOrder")).click();
				// Need to check this wait
			}

			try {
				wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(objMap.getLocator("lstreorderTimeFrame"))));
				// Put verification of banner display
				WebElement selected_timeframe = webDriver.findElements(By.xpath(objMap.getLocator("lstreorderTimeFrame"))).get(0);
				if (selected_timeframe.getText().toLowerCase().contains("Your Last Order".toLowerCase())) {
					if (selected_timeframe.findElement(By.xpath(objMap.getLocator("chktimeFrame"))).getAttribute("checked").contains("true"))
						RESULT.passed("Shop from This order with date automatically selected.",
								"Your Last Order checkbox should be checked",
						"Your Last Order is checked");
					else{
						RESULT.failed("Shop from This order with date automatically selected.",
								"Your Last Order checkbox should be checked",
						"Your Last Order is not checked");
					return;
					}
				}
			} catch (Exception e) {
				System.out.println("Unable to find element");
				e.printStackTrace();
			}

			switch (filterType.valueOf(filter)) {
			case Filter:

				/*
				 * This code snippet takes the value of time frame to be
				 * selected from user and check the checkbox The List retreives
				 * all the available time frams and comparision is done with the
				 * data text
				 */

				/* To retrieve size of list */
				// String
				// total_size=uiDriver.getwebDriverLocator(objMap.getLocator("strtimeFrameListSize")).getText();
				// int list_size=
				// Integer.parseInt(total_size.substring(total_size.indexOf("(")+1,
				// total_size.indexOf(")")));
				// System.out.println(list_size);

				List<WebElement> timeframeList = webDriver.findElements(By.xpath(objMap.getLocator("lstreorderTimeFrame")));
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(objMap.getLocator("lstreorderTimeFrame"))));
				/*
				 * This for loop checks if the data matches with the latest time
				 * frames displayed. If found then flag is set to 1
				 */
				System.out.println("Before for:" + timeframeList.size());
				for (i = 1; i < timeframeList.size(); i++) {
					System.out.println("Product " + i + ":");
					product_text = timeframeList.get(i).getText();
					System.out.println(product_text);
					if (product_text.contains(Timeframe)) {
						timeframeList.get(i).findElement(By.xpath(objMap.getLocator("chktimeFrame"))).click();
						selenium.waitForPageToLoad(PageLoadTime);
						if (uiDriver.isDisplayed("lstreorderBreadcumbs")) {
							try {
								do {
									SleepUtils.getInstance().sleep(TimeSlab.YIELD);
									breadcumbs_1 = uiDriver.getwebDriverLocator(objMap.getLocator("lstreorderBreadcumbs"));
									count++;
								} while (!breadcumbs_1.getText().contains(Timeframe) && count<5);

								if(breadcumbs_1.getText().contains(Timeframe))
									RESULT.passed("Successfully checked", Timeframe+ " should be checked", Timeframe+ "checked");
								else{
									RESULT.failed("Successfully checked", Timeframe+ " should be checked", Timeframe+ "is unchecked");
								return;
								}
							} catch (Exception e) {
								System.out.println("Breadcumbs error;");
								e.printStackTrace();
							}
							flag = 1;
							break;
						} else {
							RESULT.failed("Wait for pageToLoad",
									"Page should be Loaded",
							"Maximum time out reached for pageToload");
							return;
						}

					}
				}
				/*
				 * This code is invoked only when the latest displayed time
				 * frame does not match with the data. This will target 'See
				 * more Order' options
				 */
				if (flag == 0) {
					uiDriver.getwebDriverLocator(objMap.getLocator("poptimeFrameSeeMore")).click();
					List<WebElement> ul_list = webDriver.findElements(By.xpath(objMap.getLocator("lstrmoreTimeFrame")));
					for (WebElement ul_ite : ul_list) {
						i = 1;
						List<WebElement> li_list = ul_ite.findElements(By.tagName("li"));
						for (WebElement li_ite : li_list) {
							/* First li has no span/label/span */
							if (i == 1) {
								System.out.println(li_ite.getAttribute("class"));
								i = 2;
								continue;
							}
							product_text = li_ite.getText();
							
							if (product_text.contains(Timeframe)) {
								// click
								li_ite.findElement(By.xpath("span/label/input")).click();
								flag = 2;
								
								uiDriver.getwebDriverLocator(objMap.getLocator("popseeMoreClose")).click();
								try {
									do {
										SleepUtils.getInstance().sleep(TimeSlab.YIELD);
										breadcumbs_1 = uiDriver.getwebDriverLocator(objMap.getLocator("lstreorderBreadcumbs"));
										count++;
									} while (!breadcumbs_1.getText().contains(Timeframe) &&count<5);
									if(breadcumbs_1.getText().contains(Timeframe))
										RESULT.passed("Successfully checked", Timeframe+ " should be checked", Timeframe+ "checked");
									else{
										RESULT.failed("Successfully checked", Timeframe+ " should be checked", Timeframe+ "is unchecked");
									return;
									}
								} catch (Exception e) {
									System.out.println("Breadcumbs error;");
									e.printStackTrace();
								}
								// Breaks inner for loop
								break;
							}
						}
						if (flag == 2) {
							break;
						}
						/*
						 * This will break the outer for loop. Also the user
						 * needs to click 'See more orders' link again to
						 * close the popup
						 */
					}
				}
				
				if (flag ==0 && FlexibilityFlag.equalsIgnoreCase("YES")) {
					uiDriver.getwebDriverLocator(objMap.getLocator("popseeMoreClose")).click();
					RESULT.warning("Timeframe selection", "Given Timeframe Should be available","Given Timeframe is not available "+Timeframe);
					WebElement selected_timeframe = timeframeList.get(0);
					if (selected_timeframe.findElement(By.xpath(objMap.getLocator("chktimeFrame"))).getAttribute("checked").contains("true"))
					{
						RESULT.passed("First Timeframe Selection",
								"First Time frame Should be selected",
						"First Timerame selected");
					}else{
						product_text = timeframeList.get(0).getText();
						System.out.println(product_text);
						timeframeList.get(0).findElement(By.xpath(objMap.getLocator("chktimeFrame"))).click();
						selenium.waitForPageToLoad(PageLoadTime);
						if (uiDriver.isDisplayed("lstreorderBreadcumbs")) {
							try {
								do {
									SleepUtils.getInstance().sleep(TimeSlab.YIELD);
									breadcumbs_1 = uiDriver.getwebDriverLocator(objMap.getLocator("lstreorderBreadcumbs"));
									count++;
								} while (!breadcumbs_1.getText().contains(product_text) && count<5);
								if(breadcumbs_1.getText().contains(product_text.replaceAll("\\s+$", "").replaceAll("^\\s+", ""))){
									RESULT.passed("First Timeframe Selection",
											"First Time frame Should be selected",
									"First Timerame selected");
								}else{
									RESULT.failed("First Timeframe Selection",
											"First Time frame Should be selected",
									"First Timerame not selected");
									return;
								}
							} catch (Exception e) {
								System.out.println("Breadcumbs error;");
								e.printStackTrace();
							}
						}
					}
				}else if(flag ==0 && FlexibilityFlag.equalsIgnoreCase("NO")){
					RESULT.failed("Timeframe selection", "Given Timeframe Should be available","Given Timeframe is not available "+Timeframe);
					return;
				}
				//break;

				/*
				 * This case is invoked when user input is to select data from the
				 * department category
				 */
			//case department:

				/* Check the user input department from past order */
				List<WebElement> departmentList = webDriver.findElements(By.xpath(objMap.getLocator("lstreorderDept")));
				
				for (i = 1; i < departmentList.size(); i++) {
					product_text = departmentList.get(i).getText();
					System.out.println(product_text);
					if (product_text.contains(Department)) {
						System.out.println(departmentList.get(i).findElement(By.xpath(objMap.getLocator("lstdepartmentName"))).getText());
						departmentList.get(i).findElement(By.xpath(objMap.getLocator("lstdepartmentName"))).click();
						flag_dept=1;
						try {
							do {
								SleepUtils.getInstance().sleep(TimeSlab.YIELD);
								breadcumbs_1 = uiDriver.getwebDriverLocator(objMap.getLocator("lstreorderBreadcumbs"));
								count++;
							} while (!breadcumbs_1.getText().contains(Department) && count<5);
							if(breadcumbs_1.getText().contains(Department))
								RESULT.passed("Successfully selected", Department+ " should be selected", Department+ "selected");
							else{
								RESULT.failed("Successfully selected", Department+ " should be selected", Department+ "not selected");
						       return;
							}
							} catch (Exception e) {
							System.out.println("Breadcumbs error;");
							e.printStackTrace();
						}
						break;
					}
				}
				if(flag_dept==0){
					RESULT.warning("Department Selection", "Given Department should be avavilablae","Given Department is not available");
					product_text = departmentList.get(1).getText();
					System.out.println(product_text);
					product_text=product_text.split("\\(")[0];
					System.out.println(product_text);
					departmentList.get(1).findElement(By.xpath(objMap.getLocator("lstdepartmentName"))).click();
					selenium.waitForPageToLoad(PageLoadTime);
					try {
						do {
							SleepUtils.getInstance().sleep(TimeSlab.YIELD);
							breadcumbs_1 = uiDriver.getwebDriverLocator(objMap.getLocator("lstreorderBreadcumbs"));
							count++;
						} while (!breadcumbs_1.getText().contains(Department) && count<5);
						if(breadcumbs_1.getText().contains(product_text.replaceAll("\\s+$", "").replaceAll("^\\s+", "")))
							RESULT.passed("First Department Selection", " First Department Should be selected","First Department selected");
						else{
							RESULT.failed("First Department Selection", " First Department Should be selected","First Department not selected");
							return;
						}
					} catch (Exception e) {
						System.out.println("Breadcumbs error;");
						e.printStackTrace();
					}
				}
				/*
				 * This case is invoked when data is from other category like Sale,
				 * Shop By, etc. Here tester needs to assure that the portion is
				 * displayed with first time page loading. If in time frame any
				 * order from 'See more orders' is checked then in GUI the div
				 * expands and special preferences section will not be visible on
				 * page.
				 */
				/* Check the user input special preference from past order */
				if (webDriver.findElements(By.xpath(objMap.getLocator("lstreorderSpecPref"))).size() > 0) {
				count=0;
				List<WebElement> special_prefList = webDriver.findElements(By.xpath(objMap.getLocator("lstreorderSpecPref")));
				for (i = 0; i < special_prefList.size(); i++) {
					product_text = special_prefList.get(i).getText();
					System.out.println(product_text);
					if (product_text.contains(SpecialPreference)) {
						// Check one example for this and change xpah value	
						if (((String) GCONFIG.get("Browser")).equalsIgnoreCase("SAFARI"))
							special_prefList.get(i).findElement(By.xpath(objMap.getLocator("radlistName"))).click();	
						else
							special_prefList.get(i).findElement(By.xpath(objMap.getLocator("radlistName"))).sendKeys(Keys.SPACE);
						try {
							do {
								SleepUtils.getInstance().sleep(TimeSlab.YIELD);
								breadcumbs_1 = uiDriver.getwebDriverLocator(objMap.getLocator("lstreorderBreadcumbs"));
								count++;
							} while (!breadcumbs_1.getText().contains(SpecialPreference) && count<5);
							if(breadcumbs_1.getText().contains(SpecialPreference))
								RESULT.passed("Successfully checked",SpecialPreference+ " should be checked", SpecialPreference+ "checked");
							else{
								RESULT.failed("Successfully checked", SpecialPreference+ " should be checked", SpecialPreference+ "is unchecked");
								return;
							}
						} catch (Exception e) {
							System.out.println("Breadcumbs error;");
							e.printStackTrace();
						}
						flag_pref=1;
						break;
					}
				}
					if(flag_pref==0){
						RESULT.warning("Special Preference Selection", "Given Special Preference should be avavilablae","Special Preference is not available");
						product_text = special_prefList.get(0).getText();
						WebElement selected_preference = webDriver.findElements(By.xpath(objMap.getLocator("lstreorderSpecPref"))).get(0);
						if (selected_preference.getAttribute("checked") != null && selected_preference.getAttribute("checked").contains("checked"))
						{
							RESULT.passed("First Special Preference Selection",
									"First Special Preference Should be selected",
							"First Special Preference selected");
						}else{
							System.out.println(product_text);
							product_text=product_text.split("\\(")[0];
							special_prefList.get(0).findElement(By.xpath(objMap.getLocator("radlistName"))).sendKeys(Keys.SPACE);
							selenium.waitForPageToLoad(PageLoadTime);
							if (uiDriver.isDisplayed("lstreorderBreadcumbs")) {
								try {
									do {
										SleepUtils.getInstance().sleep(TimeSlab.YIELD);
										breadcumbs_1 = uiDriver.getwebDriverLocator(objMap.getLocator("lstreorderBreadcumbs"));
										count++;
									} while (!breadcumbs_1.getText().contains(product_text) && count<5);
									if(breadcumbs_1.getText().contains(product_text.replaceAll("\\s+$", "").replaceAll("^\\s+", ""))){
										RESULT.passed("First Special Preference Selection",
												"First Special Preference Should be selected",
										"First Special Preference selected");
									}else{
										RESULT.failed("First Special Preference Selection",
												"First Special Preference Should be selected",
										"First Special Preference not selected");
										return;
									}
								} catch (Exception e) {
									System.out.println("Breadcumbs error;");
									e.printStackTrace();
								}
							}
						}
					}
				
				}else{
					RESULT.warning("Special Preference Selection", "Special Preference section should be available","Special Preference Section is not available");
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
			e.printStackTrace();
			RESULT.failed("Error", "Element not found", "Element not found");
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

			WebElement pagination_tab = uiDriver.getwebDriverLocator(objMap.getLocator("lnksearchPagination"));

			int prod_perpage = Integer.parseInt(pagination_tab.getAttribute("data-pagesize"));
			int toatl_products = Integer.parseInt(pagination_tab.getAttribute("data-itemcount"));

			int total_pages = (toatl_products / prod_perpage)+ (toatl_products % prod_perpage > 0 ? 1 : 0);
			System.out.println(total_pages);

			webDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			WebElement current_page = uiDriver.getwebDriverLocator(objMap.getLocator("btnSearchCurrentPage"));
			webDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			int page_number = Integer.parseInt(current_page.getAttribute("data-page"));
			System.out.println(page_number);

			while (page_number <= total_pages&& count_next_clicks <= total_pages) {
				/* Row selection */

				List<WebElement> row_list = uiDriver.getwebDriverLocator(objMap.getLocator("lstrsearchRowList")).findElements(By.tagName("ol"));
				wait.until(ExpectedConditions.visibilityOfAllElements(row_list));

				search_items = new String[(row_list.size() + 1) * page_number][prod_perpage];
				System.out.println("Number of rows " + row_list.size());
				int i = 0;
				for (WebElement row_ite : row_list) {
					/* Column Selection */

					List<WebElement> col_list = row_ite.findElements(By.tagName("li"));
					System.out.println("Number of columns in row is "+ col_list.size());
					int j = 0;
					for (WebElement col_ite : col_list) {

						// WebElement
						// item_text=col_ite.findElement(By.tagName("a"));
						search_items[i][j] = col_ite.getText();
						j++;
					}
					i++;
				}
				uiDriver.getwebDriverLocator(objMap.getLocator("btnreorder_searchnextpageclick")).click();
				count_next_clicks++;
				Thread.sleep(30000);
				current_page = uiDriver.getwebDriverLocator(objMap.getLocator("reorder_searchcurrentpage"));
				page_number = Integer.parseInt(current_page.getAttribute("data-page"));
			}

		} catch (NoSuchElementException e) {
			System.out.println(e.getMessage());
			if (search_items != null) {
				System.out.println("Passed");
				RESULT.passed("Searching product using search field",
						"Product search should be done successfully",
				"Product search is successful");
			} else {
				RESULT.log("Searching product using search field",ResultType.FAILED,
						"Product search should be done successfully",
						"Product search failed because may be there exist no product with the search text "+ search_text, true);
			}

		} catch (StaleElementReferenceException e) {
			System.out.println(e.getMessage());
		}
	}

	// function to handle the alcohol restriction pop-up on the express checkout
	public boolean FD_alcoholPopUp(String restrinction_handle) {
		boolean change_address = true;
		if (webDriver.findElements(By.xpath(objMap.getLocator("btnchangeAddress"))).size() > 0
				&& webDriver.findElement(By.xpath(objMap.getLocator("btnchangeAddress"))).isDisplayed()) {
			if (restrinction_handle.equalsIgnoreCase("Remove")) {
				uiDriver.click("btnremoveProceed");
				RESULT.passed("Alcohol Restriction",
						"Alcohol Restriciton popup should be displayed",
				"Alcohol Restriciton popup is displayed and Alcohol item removed and proceeded");
			} else if (restrinction_handle.equalsIgnoreCase("Change")) {
				uiDriver.click("btnchangeAddress");
				RESULT.passed("Alcohol Restriction",
						"Alcohol Restriciton popup should be displayed",
				"Alcohol Restriciton popup is displayed and Address changed");
				change_address = false;
			} else {
				RESULT.passed("Alcohol Restriction",
						"Alcohol Restriciton popup should be displayed",
				"Alcohol Restriciton popup is displayed and Alcohol item removed and proceeded");
			}

		}
		return change_address;
	}

	/**
     * Function Name: FD_chooseDeliveryAddress
     * Purpose: Allows FreshDirect user to Add, Edit, Select or Delete Delivery Address
     * Created By: Shraddha Shah 
     * Created Date: 
     * Modified By:
     * Modified Date: 
     * 
     * Enum method is used in switch case statement in order to select one of the option of Delivery Address
     * 
     * @param AddSelectDeleteEdit
     				Option to be selected to Add, Select, Edit or Delete delivery address
	 * @param ServiceType
	 				Residential or Commercial service to be selected by user
	 * @param AddressNickName
	 				Nickname of the Delivery Address to be added
	 * @param FirstName
	 				First Name of user
	 * @param LastName
	 				Last Name of user
	 * @param StreetAddress
	 				Street Address to be provided by user
	 * @param Apartment
	 				Apartment name to be provided by user
	 * @param AddLine2
	 				Address Line2 to be given by user
	 * @param City
	 				City name of user
	 * @param State
	 				State to be added by user
	 * @param ZipCode
	 				Zip code to be given by user
	 * @param Contact1
	 				Contact number to be provided by user
	 * @param Ext1
	 				Extension number to be provided by user
	 * @param ContactType
	 				Type of contact i.e Work, Home, Mobile to be mentioned by user
	 * @param AddAltContact
	 				Whether user wants to add an alternate contact or not
	 * @param Contact2
	 				Alternate Contact number to be provided by user
	 * @param Ext2
	 				Alternate Extension number to be provided by user
	 * @param AltContactType
	 * 				Type of alternate contact i.e Work, Home, Mobile to be mentioned by user
	 * @param SpclDelInstructions
	 				Special delivery instructions to be mentioned by user
	 * @param DoormanDelivery
	 				Whether user allows delivery to doorman or not
	 * @param NeighbourDelivery
	 				Whether user allows delivery to neighbor or not
	 * @param AltFirstName
	 				First Name of Neighbor
	 * @param AltLastName
	 				Last Name of Neighbor
	 * @param AltApt
	 				Apartment Name of Neighbor
	 * @param AltPhone
	 				Contact number of neighbor
	 * @param AltExtn
	 				Extension number of neighbor
	 * @param UnattendedDelivery
	 				Whether user allows unattended delivery or not
	 * @param UnattendedDelInstructions
	 				Instructions to be provided by user in case of unattended delivery
	 * @param CompanyName
	 				Company Name to be provided by user if Service Type is selected as Commercial
	 * @param SaveCancelBtn
	 				Either to Save the delivery address or cancel it
	 * @param SelectAddress
	 				Address given by user to select delivery address while placing an order
	 * @param FlexibilityFlag
	 				Flexibility provided to user to select first delivery address if address given by user does not exist in the list of addresses
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
	throws InterruptedException, FindFailed
	{
		try {
			String ChoosedOpt = AddSelectDeleteEdit;

			switch (ChooseOpt.valueOf(ChoosedOpt)) {
			case Add:
				SleepUtils.getInstance().sleep(TimeSlab.YIELD);
				String AddressCountBefore = uiDriver.getwebDriverLocator(objMap.getLocator("straddressCount")).getAttribute("data-item-count");
				uiDriver.click("btnaddNewAddress");
				String Servicetype = ServiceType;
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
					if (ContactType.equalsIgnoreCase("Home")) {
						uiDriver.click("radhomeNumber");
					}
					if (ContactType.equalsIgnoreCase("Work")) {
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
						uiDriver.setValue("txtneighborFirstName", AltFirstName);
						uiDriver.setValue("txtneighborLastName", AltLastName);
						uiDriver.setValue("txtneighborAddress", AltApt);
						uiDriver.setValue("txtneighborPhone", AltPhone);
						uiDriver.setValue("txtneighborPhoneExtn", AltExtn);
					}
					if (UnattendedDelivery.equalsIgnoreCase("yes")) {
						uiDriver.click("radunattendedDelivery");
						uiDriver.setValue("txtunattendedDelInstructions",
								UnattendedDelInstructions);
					}
					if (SaveCancelBtn.equalsIgnoreCase("Cancel")) {
						uiDriver.click("btncancelHome");
					} else {
						uiDriver.click("btnsaveHome");
						SleepUtils.getInstance().sleep(TimeSlab.HIGH);
						SleepUtils.getInstance().sleep(TimeSlab.YIELD);
						if (uiDriver.getwebDriverLocator(objMap.getLocator("btnsaveHome")).isDisplayed()) {
							RESULT.failed("Add Delivery Address",
									"User should be able to add new delivery address",
							"Please fill the required details");
							return;
						} else {
							String AddressCountAfter = uiDriver.getwebDriverLocator(objMap.getLocator("straddressCount")).getAttribute("data-item-count");
							if (Integer.parseInt(AddressCountAfter)==Integer.parseInt(AddressCountBefore)+1) {
								RESULT.passed("Add Delivery Address",
										"User should be able add new delivery address",
								"Address added Sucessfully!!!!");							
							} else {

								RESULT.failed("Add Delivery Address",
										"User should be able to add new delivery address",
								"Unable to add address");
								return;
							}	
						}
					} 
				}
				if (ServiceType.equalsIgnoreCase("Office")) {
					uiDriver.click("radofficeDelAddress");
					uiDriver.setValue("txtcompanyNameOffice", CompanyName);
					uiDriver.setValue("txtfirstNameOffice", FirstName);
					uiDriver.setValue("txtlastNameOffice", LastName);
					uiDriver.setValue("txtstreetAddressOffice", StreetAddress);
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
					if (SaveCancelBtn.equalsIgnoreCase("Cancel")) {
						uiDriver.click("btncancelOffice");
					} else {
						uiDriver.click("btnsaveOffice");
						SleepUtils.getInstance().sleep(TimeSlab.HIGH);
						SleepUtils.getInstance().sleep(TimeSlab.YIELD);
						if (uiDriver.getwebDriverLocator(objMap.getLocator("btnsaveOffice")).isDisplayed()) {
							RESULT.failed("Add Delivery Address",
									"User should be able to add new delivery address",
							"Please fill the required details");
							return;
						} else {
							String AddressCountAfter = uiDriver.getwebDriverLocator(objMap.getLocator("straddressCount")).getAttribute("data-item-count");
							if (Integer.parseInt(AddressCountAfter)==Integer.parseInt(AddressCountBefore)+1) {
								RESULT.passed("Add Delivery Address",
										"User should be able add new delivery address",
								"Address added Sucessfully!!!!");							
							} else {

								RESULT.failed("Add Delivery Address",
										"User should be able to add new delivery address",
								"Unable to add address");
								return;
							}	
						}
					}
				}	

				break;

			case Select:
				SleepUtils.getInstance().sleep(TimeSlab.YIELD);
				List<WebElement> addLst = webDriver.findElements(By.xpath(objMap.getLocator("lstaddressPath")));
				int iSize = addLst.size();
				List<WebElement> selectLst = webDriver.findElements(By.xpath(objMap.getLocator("lstradioPath")));
				int jSize = selectLst.size();
				//System.out.println(iSize);
				//System.out.println(jSize);
				String myadd = SelectAddress;
				//System.out.println("Given: " + myadd);
				String myaddnew = myadd.replaceAll("\n\n", "\n");
				//System.out.println("Squeeze: " + myaddnew);
				int a = 0;
				for (int i = 0, j = 0; i < iSize; i++, j++) {
					String gotadd = addLst.get(i).getText();
					//	System.out.println(gotadd);
					if (gotadd.contains(myaddnew)) {
						a++;
						System.out.println(gotadd);
						try{
							if(selectLst.get(j).getAttribute("checked").equalsIgnoreCase("true"))
							{
								System.out.println("Delivery Address already selected");
							}
							else
							{
								selectLst.get(j).click();
								System.out.println("Delivery Address selected");
							}
							}
							catch(Exception e)
							{
								selectLst.get(j).click();
								System.out.println("Delivery Address selected in catch");
							}
						RESULT.passed("Select Delivery Address",
								"User should be able to select delivery address",
								myaddnew +" Address selected Sucessfully!!!!");
						break;
					}
				}
				if (a == 0) {
					if (FlexibilityFlag.equalsIgnoreCase("Yes")) {
						uiDriver.click("raddefaultAddress");
						RESULT.passed("Select Delivery Address",
								"User should be able to select delivery address",
								myaddnew +" Address do not exist in the list.Default First Address selected Sucessfully!!!!");

					} else {
						String Error_Msg = "Address did not match";
						System.out.println(Error_Msg);
						RESULT.failed("Select Delivery Address",
								"User should be able to select Delivery Address",
								myaddnew + " Address did not match");
						return;
					}					
				}
				break;

			case Delete:
				SleepUtils.getInstance().sleep(TimeSlab.YIELD);
				List<WebElement> addLst1 = webDriver.findElements(By.xpath(objMap.getLocator("lstaddressPath")));
				int dSize = addLst1.size();
				List<WebElement> delLst = webDriver.findElements(By.xpath(objMap.getLocator("lstdeletePath")));
				int eSize = delLst.size();
				String myadd1 = SelectAddress;
				String myadd1new = myadd1.replaceAll("\n\n", "\n");
				// System.out.println("given:"+myadd1);
				System.out.println(dSize);
				System.out.println(eSize);
				int b = 0;
				for (int i = 0, j = 0; i < dSize; i++, j++) {
					String gotadd1 = addLst1.get(i).getText();
					// System.out.println("Address:"+gotadd1);
					if (gotadd1.startsWith(myadd1new)) {
						b++;
						System.out.println(gotadd1);
						delLst.get(j).click();
						if (uiDriver.getwebDriverLocator(objMap.getLocator("btndelete")).isDisplayed()) {
							uiDriver.click("btndelete");
						}
						System.out.println("Address deleted successfully!!!!");
						RESULT.passed("Delete Delivery Address",
								"User should be able to delete delivery address",
								myadd1 +" Address deleted Sucessfully!!!!");
						break;
					}
				}
				if (b == 0) {
					RESULT.failed("Delete Delivery Address",
							"User should be able to delete Delivery Address",
							myadd1 +" Address did not match");
					return;
				}
				break;

			case Edit:
				SleepUtils.getInstance().sleep(TimeSlab.YIELD);
				List<WebElement> addLst2 = webDriver.findElements(By.xpath(objMap.getLocator("lstaddressPath")));
				int fSize = addLst2.size();
				List<WebElement> editLst = webDriver.findElements(By.xpath(objMap.getLocator("lsteditPath")));
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
						if (ServiceType1.equalsIgnoreCase("Residential")) {
							uiDriver.click("radresidential");
							uiDriver.setValue("txtaddressNickname",AddressNickName);
							uiDriver.setValue("txtfirstNameDelAddress",FirstName);
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
							if (ContactType.equalsIgnoreCase("Home")) {
								uiDriver.click("radhomeNumber");
							}
							if (ContactType.equalsIgnoreCase("Work")) {
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
							if (SaveCancelBtn.equalsIgnoreCase("Cancel")) {
								uiDriver.click("btncancelHome");
								RESULT.passed("Edit Delivery Address",
										"User should be able to edit delivery address",
										myadd2 +" Address cancelled Sucessfully!!!!");
							} else {
								uiDriver.click("btnsaveHome");
								SleepUtils.getInstance().sleep(TimeSlab.HIGH);
								SleepUtils.getInstance().sleep(TimeSlab.YIELD);
								if (uiDriver.getwebDriverLocator(
										objMap.getLocator("btnsaveHome")).isDisplayed()) {
									RESULT.failed("Add Delivery Address",
											"User should be able to edit delivery address",
									"Please fill the required details");
									return;
								} else {
									RESULT.passed("Add Delivery Address",
											"User should be able edit delivery address",
											myadd2 +" Address edited Sucessfully!!!!");
								}
							}
						}
						if (ServiceType1.equalsIgnoreCase("Office")) {
							uiDriver.click("radofficeDelAddress");
							uiDriver.setValue("txtcompanyNameOffice", CompanyName);
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
							if (SaveCancelBtn.equalsIgnoreCase("Cancel")) {
								uiDriver.click("btncancelOffice");
								RESULT.passed("Edit Delivery Address",
										"User should be able to edit delivery address",
										myadd2 +" Address cancelled Sucessfully!!!!");
							} else {
								uiDriver.click("btnsaveOffice");
								SleepUtils.getInstance().sleep(TimeSlab.HIGH);
								SleepUtils.getInstance().sleep(TimeSlab.YIELD);
								if (uiDriver.getwebDriverLocator(
										objMap.getLocator("btnsaveOffice")).isDisplayed()) {
									RESULT.failed("Add Delivery Address",
											"User should be able to edit delivery address",
									"Please fill the required details");
									return;
								} else {
									RESULT.passed("Add Delivery Address",
											"User should be able edit delivery address",
											myadd2 +" Address edited Sucessfully!!!!");
								}
							}
						}

						break;
					}
				}
				if (c == 0) {
					RESULT.failed("Edit Delivery Address",
							"User should be able to edit address",
							myadd2 +" Address did not match");
					return;
				}
				break;
			}
		} catch (Exception e) {
			System.out.println("Please enter valid option");
			RESULT.failed("Error Found", "Delivery Address should be selected",
			"Error occured. Please enter valid option." );
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
		webDriver.findElement(By.linkText(objMap.getLocator("btnsearch_product_new_order"))).click();
		try {
			// for browse method if else search method
			if (method.equalsIgnoreCase("browse")) {
				// click on browse
				webDriver.findElement(By.linkText(objMap.getLocator("btnsearch_product_browse"))).click();
				// wait and click on the hierarchy based on the path from data
				String split_path[] = path.split("->");
				for (int i = 0; i < split_path.length; i++) {
					wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText(split_path[i])));
					webDriver.findElement(By.linkText(split_path[i])).click();
				}

			} else {
				// click on browse
				webDriver.findElement(By.linkText(objMap.getLocator("btnsearchProduct"))).click();
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
			List<WebElement> CRM_product_list = webDriver.findElements(By.xpath(objMap.getLocator("lstproductCRM")));
			// go through the list to find the product
			for (int i = 2; i < CRM_product_list.size(); i++) {
				String compare_product = CRM_product_list.get(i).getText();
				if (compare_product.contains(product)&& !compare_product.contains("0 matches")) {
					// store the result that product found
					product_found = true;
					// store the text
					product_added_text = compare_product.split("\\n")[1].trim();
					// add directly from the list of product page
					if (product_method.equalsIgnoreCase("hover")) {
						// enter the the quantity
						WebElement product_count = CRM_product_list.get(i).findElement(By.xpath(objMap.getLocator("txtproductCRMHover")));
						product_count.clear();
						product_count.sendKeys(count);
						// click add button
						WebElement product_add = CRM_product_list.get(i).findElement(By.xpath(objMap.getLocator("btnproductCRMHoverAdd")));
						product_add.click();
						uiDriver.selenium.waitForPageToLoad("10000");

					} else {
						// click on the product
						CRM_product_list.get(i).findElement(By.xpath(objMap.getLocator("lnkproductCRMClick"))).click();
						uiDriver.selenium.waitForPageToLoad("10000");
						// enter the the quantity
						
						WebElement product_count = webDriver.findElement(By.className(objMap.getLocator("txtproductCRMClick")));
						for (int c = 0; c < Integer.parseInt(count) - 1; c++)
							product_count.click();
						// click add button
						WebElement product_add = webDriver.findElement(By.id(objMap.getLocator("btnproductCRMClickAdd")));
						product_add.click();
						uiDriver.selenium.waitForPageToLoad("10000");
					}
					break;
				}
			}

			// if entered product is not available select the first one
			if (!product_found && method.equalsIgnoreCase("browse")) {
				product_added_text = CRM_product_list.get(2).getText().split("\\n")[1].trim();
				if (product_method.equalsIgnoreCase("hover")) {
					// enter the the quantity
					WebElement product_count = CRM_product_list.get(2).findElement(By.xpath(objMap.getLocator("txtProductCRMHover")));
					product_count.clear();
					product_count.sendKeys(count);
					// click add button
					WebElement product_add = CRM_product_list.get(2).findElement(By.xpath(objMap.getLocator("btnProductCRMHoverAdd")));
					product_add.click();
					uiDriver.selenium.waitForPageToLoad("10000");
				} else {
					// click on the product
					CRM_product_list.get(2).findElement(By.xpath(objMap.getLocator("lnkProductCRMClick"))).click();
					uiDriver.selenium.waitForPageToLoad("10000");
					// enter the the quantity
					// wait.until(ExpectedConditions
					// .presenceOfElementLocated(By.id(objMap
					// .getLocator("txtProductCRMClick"))));
					WebElement product_count = webDriver.findElement(By.xpath(objMap.getLocator("txtProductCRMClick")));
					product_count.clear();
					product_count.sendKeys(count);
					// click add button
					WebElement product_add = webDriver.findElement(By.id(objMap.getLocator("btnProductCRMClickAdd")));
					product_add.click();
					uiDriver.selenium.waitForPageToLoad("10000");
				}
			}
			// flag for the result
			boolean result = false;
			// check for the product in cart only if we have the product text
			if (product_added_text != null) {
				// get the order detail <tr> list
				List<WebElement> CRM_cart_product_list = webDriver.findElements(By.xpath(objMap.getLocator("lstproductCRMCart")));

				// go through the list to find the product added for result
				for (int i = 0; i < CRM_cart_product_list.size(); i++) {
					String compare_product = CRM_cart_product_list.get(i).getText();
					if (compare_product.contains(product_added_text)) {
						result = true;
						break;
					}
				}
			}
			// result
			if (!result) {
				RESULT.failed("Search product CRM",
						"Product search should be Successfully: "+ product_added_text,
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
	*Function Name: FD_chooseTimeslot
     * Purpose: Function to Select Time slot for order Delivery
     * Created By: Tejas Patel 
      * Created Date: 
      * Modified By:
     * Modified Date:
     * @param Delivery_Day
	 *            :Define day for order Delivery
	 * @param Time_Slot
	 *            :Select Time slot for given Delivery_Day
	 *  @param FlexibilityFlag
	 *            :It will give Flexibility to user for selecting Time slot         
     **/

	public void FD_chooseTimeslot(String Delivery_Day, String Time_Slot,
			String FlexibilityFlag) throws InterruptedException {
//		WebDriverWait wait = new WebDriverWait(webDriver, 10);
		// convert given Delivery_day into UpperCase
		String DeliveryDay = Delivery_Day.toUpperCase();
		// Store the given Slot into local variable
		String TimeSlot = Time_Slot;
		FlexibilityFlag = FlexibilityFlag.toUpperCase();
		// find the list of days from table
		try {
			wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(objMap.getLocator("lstdays"))));
		} catch (Exception e) {
			System.out.println("Days element error");
			e.printStackTrace();
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
		reserve_flag=false;
		// iterating individual day from list
		int p = 0;
		for (WebElement element : Days) {
			//for chrome need wait
			SleepUtils.getInstance().sleep(TimeSlab.YIELD);
			p++;
			// store the name of the day
			String day = element.findElement(By.xpath(objMap.getLocator("strfullday"))).getText();
			if (day.equals("") || day == null) {
				// store the name of the day
				day = element.findElement(By.xpath(objMap.getLocator("strhalfday"))).getText();
			}
			// comparing day with DeliveryDay
			if (DeliveryDay.contains(day)) {
				// find the list of Time Slot for given Delivery_day
				List<WebElement> Slots = element.findElements(By.xpath(objMap.getLocator("lstslotList")));
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
								if (Slot.contains("Reserved")&& Slot.contains(verify_time.split("\n")[0])&& day.contains(verify_day.substring(0,3).toUpperCase())) {
									System.out.println("Reservation passed");
									RESULT.passed("Time slot reservation",
											"User should be able to see reserved time slot",
									"User is able to see reserved time slot");
								} else {
									System.out.println("Reservation failed");
									RESULT.failed("Time slot reservation error ",
											"User could not see reservation ",
									"User is unable to see time slot");
									return;
								}
							} else {
								// select given Time_slot
								element1.findElement(By.xpath(objMap.getLocator("radslotSelect"))).click();
								SleepUtils.getInstance().sleep(TimeSlab.YIELD);
								Slot = element1.getText();
								if (Slot.contains("SOLD OUT")) {
									soldout = true;
									RESULT.warning("choose Time Slot",
											"Given Time Slot should be available ",
											"Given Slot is SOLD OUT"+ TimeSlot);
								} else {
									if (element1.findElements(By.xpath(objMap.getLocator("imgalcoholRestriction"))).size() > 0) {
										alcoholRestriction = true;
										System.out.println(" AlcoholRestriction Flag   "+ alcoholRestriction);
									}
									flag1 = true;
									verify_day = Delivery_Day;
									verify_time = TimeSlot;
									System.out.println("Your order will delivered at   "+TimeSlot + DeliveryDay);
									Check_Slot = true;
									RESULT.passed("choose Time Slot",
											"User should be able to Select Given Time Slot",
											"User has select Time Slot successfully"+ TimeSlot);
									break;
								}
							}

						}
						// given Slot not found than it will take first slot of
						// given day
						else if ((No_Slot == true)&& (FlexibilityFlag.equals("YES")))
						{
							if ((Slot.contains("am") || Slot.contains("pm"))&& Slot.contains("-")) {
								element1.findElement(By.xpath(objMap.getLocator("radslotSelect"))).click();
								SleepUtils.getInstance().sleep(TimeSlab.YIELD);
								Slot = element1.getText();
								if (Slot.contains("SOLD OUT")) {
									RESULT.warning("choose Time Slot",
											"  Time Slot should be available ",
											" Time Slot is SOLD OUT" + Slot);
								} else {
									i++;
									if (i == 1) {
										if (element1.findElements(By.xpath(objMap.getLocator("imgalcoholRestriction"))).size() > 0) {
											alcoholRestriction = true;
											System.out.println(" AlcoholRestriction Flag   "+ alcoholRestriction);
										}
										flag1 = true;
										verify_day = day;
										verify_time = Slot;
										System.out.println(" Fresh direct will deliver your order at"+ Slot + DeliveryDay);
										Check_Slot = true;
										RESULT.passed("choose Time slot",
												"User should be able to Select Time Slot for given day",
												"User has select Time Slot successfully for given day  "+ day + "  "+ Slot);
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
								"No Delivery available for given slot"+ TimeSlot);
						System.out.println(" No Delivery available for given  slot  "+ TimeSlot);

					}
					if ((i == 0) && (j == 2) && (FlexibilityFlag.equals("YES")))
						// for given day if slot not found than print the message
					{
						System.out.println(" No Delivery available for given day   "+ DeliveryDay);
						Check_Slot = true;
						NoSlotonday = true;
						RESULT.warning("choose Time Slot",
								"Time Slot should be available for given day",
								"No Delivery available for given day"+ DeliveryDay);
					}
					if ((j == 1) && (FlexibilityFlag.equals("NO"))) {
						flag1 = true;
						Check_Slot = true;
						RESULT.failed("choose Time Slot",
								" Given Time Slot should be available ",
								"No Delivery available for given slot"+ TimeSlot);
						return;
					}
					No_Slot = true;
				} while ((Check_Slot == false) && (No_Slot == true));

			} else if ((NoSlotonday == true) && (FlexibilityFlag.equals("YES"))) {

				// find the list of Time Slot for given Delivery_day
				List<WebElement> Slots = element.findElements(By.xpath(objMap.getLocator("lstslotList")));
				for (WebElement element1 : Slots) {
					String Slot = element1.getText();
					if ((Slot.contains("am") || Slot.contains("pm"))&& Slot.contains("-")) {
						element1.findElement(By.xpath(objMap.getLocator("radslotSelect"))).click();
						SleepUtils.getInstance().sleep(TimeSlab.YIELD);
						Slot = element1.getText();
						if (Slot.contains("SOLD OUT")) {
							RESULT.warning("choose Time Slot",
									" Time Slot should be available ",
									"Time Slot is SOLD OUT" + Slot);
						} else {
							if (element1.findElements(By.xpath(objMap.getLocator("imgalcoholRestriction"))).size() > 0) {
								alcoholRestriction = true;
								System.out.println(" AlcoholRestriction Flag   "+ alcoholRestriction);
							}
							flag1 = true;
							verify_day = day;
							verify_time = Slot;
							System.out.println(" Fresh direct will deliver your order at  "+ Slot + day);
							RESULT.passed("choose Time Slot",
									"User should be able to Select Time Slot for next day",
									"User has select Time Slot successfully   "+ Slot + "  " + day);
							break;
						}
					}
				}

			}
			if (flag1 == true) {
				break;
			}
			if ((p == 7) && (flag1 == false)) {
				RESULT.failed("Choose Time Slot ",
						"User should able to select Time slot for next days ",
				"User is unable to select Time slot for next days");
				return;

			}

		}
		System.out.println(" AlcoholRestriction Flag   " + alcoholRestriction);
	}
/**
	*Function Name: FD_returnProducts
     * Purpose: Function to store the products and their respective quantity to be verified in FD_placeOrder
     * Created By: Bhavik Tikudiya 
      * Created Date: 
      * Modified By:
     * Modified Date: 
	 * 	
	 * return void
	 *
	 **/

	public void FD_returnProducts() {
		try{
			String unavailableProductName[] = null;
			String unavailableProductQty[] = null;
			//Extract Zip code
			String Address = webDriver.findElement(By.xpath(objMap.getLocator("straddress"))).getText();
			Zipcode = Address.substring(Math.max(0, Address.length() - 5));
			// store the subtotal in global variable
			String temp_subtotal = uiDriver.getwebDriverLocator(objMap.getLocator("strverifySubTotal")).getText();
			verify_subtotal = temp_subtotal.replace("$", "").replace(",", "");
			// take the list of items unavailable if warning is diaplayed
			if (webDriver.findElements(By.xpath(objMap.getLocator("strverifyWarning"))).size() > 0 && webDriver.findElement(By.xpath(objMap.getLocator("strverifyWarning"))).isDisplayed()) {
				List<WebElement> unavailableProducts = webDriver.findElements(By.xpath(objMap.getLocator("lstunavailableProducts")));
				// intitialize the arrays
				unavailableProductName = new String[unavailableProducts.size()];
				unavailableProductQty = new String[unavailableProducts.size()];
				// go through the list to get product name its respective quantity
				for (int i = 0; i < unavailableProducts.size(); i++) {
					String product_text = unavailableProducts.get(i).findElement(By.xpath(objMap.getLocator("lstunavailableProductName"))).getText();
					String product_quantity_text = unavailableProducts.get(i).findElement(By.xpath(objMap.getLocator("lstunavailableProductQty"))).getText();
					String product_total = unavailableProducts.get(i).findElement(By.xpath(objMap.getLocator("lstunavailableProductTotal"))).getText().replace("$", "").split(" ")[2];
					double t_subtotal = Double.parseDouble(verify_subtotal);
					verify_subtotal = String.valueOf(t_subtotal- Double.parseDouble(product_total));
					unavailableProductName[i] = product_text;
					unavailableProductQty[i] = product_quantity_text;
				}
			}
			double temp = Double.parseDouble(verify_subtotal);
			verify_subtotal = String.valueOf((double) Math.round(temp * 100) / 100);
			// take the list of items available in cart
			List<WebElement> availableProducts = webDriver.findElements(By.xpath(objMap.getLocator("lstverifyProducts")));
			// intitialize the global arrays
			if (unavailableProductName != null) {
				verify_products = new String[availableProducts.size()  - unavailableProductName.length];
				verify_products_quantity = new String[availableProducts.size() - unavailableProductName.length];
			} else {
				verify_products = new String[availableProducts.size()];
				verify_products_quantity = new String[availableProducts.size()];
			}
			// go through the list to get product name its respective quantity
			int count = 0;
			for (int i = 0; i < availableProducts.size(); i++) {
				String product_text = availableProducts.get(i).findElement(By.xpath(objMap.getLocator("strverifyProductName"))).getText();
				String product_quantity_text = availableProducts.get(i).findElement(By.xpath(objMap.getLocator("strverifyProductQty"))).getText();
				boolean duplicate = false;
				if (unavailableProductName != null) {
					for (int j = 0; j < unavailableProductName.length; j++) {
						if (product_text.contains(unavailableProductName[j])&& product_quantity_text.contains(unavailableProductQty[j] .split(":")[1].replaceAll(" ", ""))) {
							duplicate = true;
						}
					}
					if (!duplicate) {
						verify_products[count] = product_text;
						verify_products_quantity[count] = product_quantity_text;
						count++;
					}
				} else {
					verify_products[i] = product_text;
					verify_products_quantity[i] = product_quantity_text;
				}
			}

		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	*Function Name: FD_placeOrder
     * Purpose: Function to review the details on the success page of order submission
     * Created By: Bhavik Tikudiya 
      * Created Date: 
      * Modified By:
     * Modified Date: 
	 * 
	 * @param day
	 *            : Day selected for the order delivery
	 *@param time
	 *            : Time selected for the order delivery
	 * return void
	 *
	 **/
	public void FD_placeOrder(String day, String time) {
		try {
			SleepUtils.getInstance().sleep(TimeSlab.YIELD);
			// check if the warning is available for the products
			if (webDriver.findElements(By.xpath(objMap.getLocator("strverifyWarning"))).size() > 0) {
				RESULT.warning("Place Order",
						"Few items should be lesser than the available cart items.",
						"Message: "+ webDriver.findElement(By.xpath(objMap.getLocator("strverifyWarning"))).getText());
				try{
				// verify save changes button is enabled
				if(webDriver.findElements(By.xpath(objMap.getLocator("btnsaveChanges"))).size()>0)
				{
					uiDriver.getwebDriverLocator(objMap.getLocator("btnsaveChanges")).click();
					wait.until(ExpectedConditions.stalenessOf(uiDriver.getwebDriverLocator(objMap.getLocator("btnsaveChanges"))));
				}
				}catch (Exception e) {
				System.out.println("save changes button not visible");
				e.printStackTrace();
				}
			}

			// click on the place order button
			if(webDriver.findElements(By.xpath(objMap.getLocator("btnplaceOrder"))).size()>0)
			{
				uiDriver.getwebDriverLocator(objMap.getLocator("btnplaceOrder")).click();
				System.out.println("olace order clicked");
			}
			// wait for the invisibility of the place order
			try {
				wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(objMap.getLocator("btnChangeTimeSlot"))));
			} catch (Exception e) {
				// show message if order total exceed message is displayed
				if (webDriver.findElements(By.xpath(objMap.getLocator("strsubtotalExceeds"))).size() > 0) {
					RESULT.warning("Place Order",
							"Message showing the order limit should be diaplyed.",
							"Message: "+ webDriver.findElement(By.xpath(objMap.getLocator("strsubtotalExceeds"))).getText());
					return;
				}
			}
			SleepUtils.getInstance().sleep(TimeSlab.YIELD);
			
			System.out.println("Zipcode " +Zipcode);
			if (!(webDriver.findElements(By.xpath(objMap.getLocator("strverifyWarningOnPlaced"))).size() > 0)) {
				// verify the time and day
				// get the time text to be verified
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
				if (day_text.contains(day.toUpperCase())&& time_text.contains(time.toUpperCase())) {
					RESULT.passed("Place Order",
							"Time Slot should be: " + day+ " " + time,
							"Time Slot is: " + time_text);
				} else {
					RESULT.failed("Place Order",
							"Time Slot should be: " + day+ " " + time,
							"Time Slot is: "+ time_text);
				}

				// verify subtotal
				String t_verify_subtotal = uiDriver.getwebDriverLocator(objMap.getLocator("strverifySubTotalSubmit")).getText().replace(",", "");

				if (t_verify_subtotal.contains(verify_subtotal)) {
					RESULT.passed("Place Order",
							"Subtotal should be: "+ verify_subtotal,
							"Subtotal is: " + t_verify_subtotal);
				} else {
					RESULT.failed("Place Order",
							"Subtotal should be: "+ verify_subtotal,
							"Subtotal is: "+ t_verify_subtotal);
				}

				// verify the products and quantity
				wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(objMap.getLocator("lstverifyProducts"))));
				List<WebElement> products_to_verify = webDriver.findElements(By.xpath(objMap.getLocator("lstverifyProducts")));

				String product_to_verify_name[] = new String[products_to_verify.size()];
				String product_to_verify_qty[] = new String[products_to_verify.size()];

				// go through the list to get product name its respective
				// quantity
				for (int i = 0; i < products_to_verify.size(); i++) {
					String product_text = products_to_verify.get(i).findElement(By.xpath(objMap.getLocator("strverifyProductName"))).getText();
					String product_quantity_text = products_to_verify.get(i).findElement(By.xpath(objMap.getLocator("strverifyProductQty"))).getText();
					product_to_verify_name[i] = product_text;
					product_to_verify_qty[i] = product_quantity_text;
				}

				for (int i = 0; i < products_to_verify.size(); i++) {
					if (verify_products[i].contains(product_to_verify_name[i])
							&& verify_products_quantity[i] .equals(product_to_verify_qty[i])) {
						RESULT.passed("Place Order",
								"Expected product: "+ verify_products[i] + " and Quantity: "+ verify_products_quantity[i],
								"Actual product: " + product_to_verify_name[i] + " and Quantity: " + product_to_verify_qty[i]);
					} else {
						RESULT.failed("Place Order",
								"Expected product: "+ verify_products[i] + " and Quantity: "+ verify_products_quantity[i],
								"Actual product: " + product_to_verify_name[i] + " and Quantity: "+ product_to_verify_qty[i]);
					}
				}				

			} else {							
				//Verify 30$ Minimum cart requirement for Home Address 
				if(webDriver.findElement(By.xpath(objMap.getLocator("strverifyWarningOnPlaced"))).getText().contains("$30.00"))
				{				
					FD_minimumCartVerification("strhomeDelivery", "30" , "Residential");
				}
				//Verify 50$ Minimum cart requirement for Office Address 
				else if(webDriver.findElement(By.xpath(objMap.getLocator("strverifyWarningOnPlaced"))).getText().contains("$50.00"))
				{
					FD_minimumCartVerification("strofficeDelivery", "50", "Office");
				}
				//Verify 99$ Minimum cart requirement for Pickup Address 
				else if(webDriver.findElement(By.xpath(objMap.getLocator("strverifyWarningOnPlaced"))).getText().contains("$99.00"))
				{
					FD_minimumCartVerification("strpickup", "99", "Pickup");
				}
				else
				{
					RESULT.warning("Place Order",
							"Order should be placed and review page should be displayed.",
							"ATTENTION: "+ webDriver.findElement(By.xpath(objMap.getLocator("strverifyWarningOnPlaced"))).getText());
				}

			} 
		}
		catch (NoSuchWindowException e1) {
			RESULT.warning("DOM", "DOM should be loaded", "Problem in loading of DOM oe window does not exist");
			return;
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	public void FD_minimumCartVerification(String locator , String minPrice, String addressType) {
		List<WebElement> addLst = webDriver.findElement(By.xpath(objMap.getLocator(locator))).findElements(By.tagName("option"));
		int iSize = addLst.size();
		int a=0;
		for (int i = 0; i < iSize; i++)
		{
			try{
			if(addLst.get(i).getAttribute("selected").equalsIgnoreCase("true"))
			{
				a++;						
				break;
			}
			}catch (NullPointerException e) {
				continue;
			}
		}
		if(a==0)
		{
			RESULT.failed("Place Order",
					"Minimum Cart requirement to place an order for "+addressType+" Address should be $" +minPrice,
					"Minimum Cart requirement to place an order for "+addressType+" Address is not $" +minPrice);
		}
		else
		{
			RESULT.passed("Place Order",
					"Minimum Cart requirement to place an order for "+addressType+" Address should be $" + minPrice,
					"Minimum Cart requirement to place an order for "+addressType+" Address is $" +minPrice);
		}
	}
	
	
	/**
	*Function Name: FD_reviewOrder
     * Purpose: Function to store the products and their respective quantity to be verified in FD_submitOrder
     * Created By: Tejas Patel 
      * Created Date: 
      * Modified By:
     * Modified Date: 
	 * 	
	 * return void
	 *
	 **/
	public void FD_reviewOrder() {
		try{
			
			// store the subtotal in global variable
			String temp_subtotal = uiDriver.getwebDriverLocator(objMap.getLocator("strverifySubTotalOld")).getText();
			verify_subtotal = temp_subtotal.replace("$", "");
			// take the list of items unavailable if warning is diaplayed
			
			//double temp = Double.parseDouble(verify_subtotal);
			//verify_subtotal = String.valueOf((double) Math.round(temp * 100) / 100);
			System.out.println(temp_subtotal);
			// take the list of items available in cart
			List<WebElement> availableProducts = webDriver.findElements(By.xpath(objMap.getLocator("lstverifyProductsOld")));
			// intitialize the global arrays
			System.out.println(verify_subtotal + availableProducts.size() );
				verify_products = new String[availableProducts.size()];
				verify_products_quantity = new String[availableProducts.size()];
			
			// go through the list to get product name its respective quantity
			int count = 0;
			for (int i = 0; i < availableProducts.size(); i++) {
				String product_text = availableProducts.get(i).findElement(By.xpath(objMap.getLocator("strverifyProductNameOld"))).getText();
				String product_quantity_text = availableProducts.get(i).findElement(By.xpath(objMap.getLocator("strverifyProductQtyOld"))).getText();
					verify_products[i] = product_text;
					verify_products_quantity[i] = product_quantity_text;
					System.out.println(product_text + product_quantity_text);
			}

		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	*Function Name: FD_submitOrder
     * Purpose: Function to review the details on the success page of order submission
     * Created By: Tejas Patel
      * Created Date: 
      * Modified By:
     * Modified Date: 
	 * 
	 * @param day
	 *            : Day selected for the order delivery
	 *@param time
	 *            : Time selected for the order delivery
	 * return void
	 *
	 **/	
	public void FD_submitOrder(String day, String time) {
		try {
			if (webDriver.findElements(By.xpath(objMap.getLocator("strorderNumberOld"))).size() > 0) {
				// verify the time and day
				// get the time text to be verified
				String time_text = uiDriver.getwebDriverLocator(objMap.getLocator("strverifyTimeOld")).getText();
				// covert it into the specified format
				time_text=time_text.toUpperCase();
				System.out.println(time_text);
				
				if (time_text.contains(day.toUpperCase())&& time_text.contains(time.toUpperCase())) {
					RESULT.passed("Submit Order",
							"Time Slot should be: " + day+ " " + time,
							"Time Slot is: " + time_text);
				} else {
					RESULT.failed("Submit Order",
							"Time Slot should be: " + day+ " " + time,
							"Time Slot is: "+ time_text);
				}

				// verify subtotal
				String t_verify_subtotal = uiDriver.getwebDriverLocator(objMap.getLocator("strverifySubTotalSubmitOld")).getText().replace("$", "");
				System.out.println(t_verify_subtotal);
				if (t_verify_subtotal.contains(verify_subtotal)) {
					RESULT.passed("Submit Order",
							"Subtotal should be: "+ verify_subtotal,
							"Subtotal is: " + t_verify_subtotal);
				} else {
					RESULT.failed("Submit Order",
							"Subtotal should be: "+ verify_subtotal,
							"Subtotal is: "+ t_verify_subtotal);
				}

				// verify the products and quantity
				wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(objMap.getLocator("lstsubmitedProduct"))));
				List<WebElement> products_to_verify = webDriver.findElements(By.xpath(objMap.getLocator("lstsubmitedProduct")));

				String product_to_verify_name[] = new String[products_to_verify.size()];
				String product_to_verify_qty[] = new String[products_to_verify.size()];

				// go through the list to get product name its respective
				// quantity
				for (int i = 0; i < products_to_verify.size(); i++) {
					String product_text = products_to_verify.get(i).findElement(By.xpath(objMap.getLocator("strverifyProductNameOld"))).getText();
					String product_quantity_text = products_to_verify.get(i).findElement(By.xpath(objMap.getLocator("strverifyProductQtyOld"))).getText();
					product_to_verify_name[i] = product_text;
					product_to_verify_qty[i] = product_quantity_text;
					System.out.println(product_text + product_quantity_text);
				}

				for (int i = 0; i < products_to_verify.size(); i++) {
					if (verify_products[i].contains(product_to_verify_name[i])
							&& verify_products_quantity[i] .equals(product_to_verify_qty[i])) {
						RESULT.passed("Submit Order",
								"Expected product: "+ verify_products[i] + " and Quantity: "+ verify_products_quantity[i],
								"Actual product: " + product_to_verify_name[i] + " and Quantity: " + product_to_verify_qty[i]);
					} else {
						RESULT.failed("Submit Order",
								"Expected product: "+ verify_products[i] + " and Quantity: "+ verify_products_quantity[i],
								"Actual product: " + product_to_verify_name[i] + " and Quantity: "+ product_to_verify_qty[i]);
					}
				}				

			} else {
				RESULT.failed("Submit Order",
						"Order should be submitted and review page should be displayed",
						"Order is not submitted");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private enum event {
		addAddress, updateAddress, changeEmail
	};

	public void FD_chooseReserveTimeSlot(String Address, String ReserveType,
			String Day, String Time, String FlexibilityFlag) {

//		WebDriverWait wait = new WebDriverWait(webDriver, 20);
		wait.until(ExpectedConditions.visibilityOf(webDriver.findElement(By.name(objMap.getLocator("drpreserveAddress")))));
		Select add_ddl = new Select(uiDriver.getwebDriverLocator(objMap.getLocator("drpreserveAddress")));
		try {
			uiDriver.DrawHighlight("drpreserveAddress", webDriver);
		} catch (InterruptedException e1) {
			System.out.println("Address Drop down list interruption");
			e1.printStackTrace();
		}
		webDriver.findElement(By.name(objMap.getLocator("drpreserveAddress"))).click();
		add_ddl.selectByVisibleText(Address);
		SleepUtils.getInstance().sleep(TimeSlab.YIELD);
		//wait.until(ExpectedConditions.visibilityOfElementLocated(By.name(objMap.getLocator("drpreserveAddress"))));

		try {
			uiDriver.FD_chooseTimeslot(Day, Time, FlexibilityFlag);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		if (ReserveType.equalsIgnoreCase("This week")) {
			try {
				uiDriver.DrawHighlight("radreserveThisWeek", webDriver);
			} catch (InterruptedException e) {
				System.out.println("This week radio button interruption");
				e.printStackTrace();
			}
			uiDriver.click("radreserveThisWeek");
		} else if (ReserveType.equalsIgnoreCase("Every week")) {
			try {
				uiDriver.DrawHighlight("radreserveEveryWeek", webDriver);
			} catch (InterruptedException e) {
				System.out.println("Every week radio button interruption");
				e.printStackTrace();
			}
			uiDriver.click("radreserveEveryWeek");
		}
		uiDriver.click("btnreserveTime");
		SleepUtils.getInstance().sleep(TimeSlab.YIELD);
//		uiDriver.selenium.waitForPageToLoad(PageLoadTime);
		//wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(objMap.getLocator("drpreserveAddress"))));

		if (uiDriver.getwebDriverLocator(objMap.getLocator("strreserveSuccessMsg")).getText().contains("has been reserved")) {
			RESULT.passed("Reservation Time slot updation successful",
					"Reservation time slot should be updated",
					"Reservation time slot updated to " + uiDriver.verify_day+ " " + uiDriver.verify_time);
			reserve_flag = true;
		} else {
			RESULT.failed("Reservation Time slot updation unsuccessful",
					"Reservation time slot updation error",
					"Reservation time slot cannot be updated to "+ Day + " " + Time);
		}

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.name(objMap.getLocator("drpreserveAddress"))));
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
     * Function Name: FD_viewCart()
     * Purpose: For verifying added products are same in 
     * 			your cart page and "your cart" button mouse hover
     * Created By: Avani Thakkar 
     * Created Date: 15th May,2015 
     * Modified By:	Avani Thakkar 
     * Modified Date: 3rd September,2015
     * 
     * @return void 
       */
	
	public void FD_viewCart() {
		String quantity;
		String name;
		String price;
		String subtotal=null;
		int emptyCart=0;
		HashMap<String, HashMap<String, String>> outerMap = new HashMap<String, HashMap<String, String>>();
		List<HashMap<String, HashMap<String, String>>> lstouterMap = new ArrayList<HashMap<String, HashMap<String, String>>>();
		try {
			// Hover the mouse on "your cart" button to verify item detail
			WebElement btnYourcart = uiDriver.getwebDriverLocator(objMap
					.getLocator("btnyourCart"));
			//actions.moveToElement(btnYourcart).build().perform();
			robot.moveToElement(btnYourcart);
			// verify "checkout" and "view cart" button is present or not
			WebElement btnview = uiDriver.getwebDriverLocator(objMap
					.getLocator("btnviewCart"));
			WebElement btnchkout = uiDriver.getwebDriverLocator(objMap
					.getLocator("btncheckOut1"));
			//actions.moveToElement(btnYourcart).build().perform();
			robot.moveToElement(btnYourcart);
			if (btnview.isEnabled() && btnchkout.isEnabled()) 
			{
				RESULT.passed("View cart and checkout button","View cart and checkout button should exist and should be enabled",
				"View cart and checkout button exists and enabled");
			}
			else
				RESULT.failed("View cart and checkout button","View cart and checkout button should exist and should be enabled",
				"View cart and checkout button does not exist or not enabled");

			// retrieve all items details like "Quantity","Name" and "Price"
			List<WebElement> Lstitems_row = uiDriver.getwebDriverLocator(
					objMap.getLocator("lstpopupCart")).findElements(
							By.className(objMap.getLocator("lstcartLine")));
			System.out.println("\n Number of items in cart is "
					+ Lstitems_row.size());
			int index_tbquant=0;
			if (Lstitems_row.size() == 0) {
				RESULT.passed("view cart",
						"No items should be available in cart",
				"No items available in your cart");
				emptyCart=1;
			}
			else
			{
				int j = 0;
				for (WebElement item : Lstitems_row) {
					// quantity of product
					//actions.moveToElement(btnYourcart).build().perform();
					robot.moveToElement(btnYourcart);
					List<WebElement> qaunt = item.findElements(By.xpath(objMap
							.getLocator("lstqnty")));
					List<WebElement> quant_drp=item.findElements(By.xpath(objMap.getLocator("lstqntyDropdown")));
					if(quant_drp.size()>0)
					{
						quantity ="1";
					}
					else
					{
						quantity = qaunt.get(index_tbquant).getAttribute("value");
						index_tbquant++;
					}

					// name of product
					do {
						//actions.moveToElement(btnYourcart).build().perform();
						robot.moveToElement(btnYourcart);
						List<WebElement> nam = item.findElements(By.xpath(objMap
								.getLocator("lstname")));
						name = nam.get(j).getText();
					} while (name == null || name.equals(""));

					// price of product
					do {
						//actions.moveToElement(btnYourcart).build().perform();
						robot.moveToElement(btnYourcart);
						List<WebElement> pric = item.findElements(By.xpath(objMap
								.getLocator("lstprice")));
						price = pric.get(j).getText();
					} while (price == null || price.equals(""));

					// add all details in list of hash map of hash map
					outerMap = this.NewHash(quantity, name, price, j);
					lstouterMap.add(j, outerMap);
					j++;
				}
				// sub total of all items
				subtotal = uiDriver.getwebDriverLocator(
						objMap.getLocator("strsubttl1")).getText();
			}
			// verify cart by clicking on your cart button
			btnYourcart.click();
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(objMap.getLocator("btncheckOut"))));
			SleepUtils.getInstance().sleep(TimeSlab.YIELD);
			if(!(webDriver.findElements(By.xpath(objMap.getLocator("lstverifyProducts"))).size()>0))
			{
				if(emptyCart==1)
				{
					RESULT.passed("View Cart","Cart page as well as cart popup should contain same products",
					"Cart popup as well as cart page is empty");
				}
				else
				{
					RESULT.failed("View Cart","Cart page as well as cart popup should contain same products",
					"Cart popup is not empty but cart page is empty");
				}
			}else{
				List<WebElement> LstCartClick =webDriver.findElements(By.xpath(objMap.getLocator("lstverifyProducts")));
				System.out.println("\n Number of items in cart page is "+ Lstitems_row.size());
				int i = 0;
				int k = 0;
				for (WebElement prod : LstCartClick)
				{
					if (LstCartClick.size() == k )
						break;

					// Quantity of product
					if(prod.findElements(By.className(objMap.getLocator("lstqntyNew"))).size()>0)
					{
						quantity=prod.findElement(By.className(objMap.getLocator("lstqntyNew"))).getAttribute("value");
					}
					else if(prod.findElements(By.xpath(objMap.getLocator("lstqntyDropdownPage"))).size()>0)
					{
						quantity ="1";					
					}
					else
					{
						quantity=prod.findElement(By.className(objMap.getLocator("lstqntyNew"))).getAttribute("value");
						RESULT.warning("Quantity type mismatch", "Quantity type should be either text box or drop down of sales unit", 
						"Quantity type is other than text box and dropdown of sales unit");
					}

					// Name of product
					List<WebElement> nam=webDriver.findElements(By.xpath(objMap.getLocator("lnkitemName1")));
					name = nam.get(k).getText();

					// price of product
					price = prod.findElement(By.className(objMap.getLocator("lstpriceNew"))).getText();

				/*	//calculate price from quantity and unit price and match with displayed one
					String priceEach=prod.findElement(By.className(objMap.getLocator("strunitPrice"))).getText();
					if(priceEach.contains("ea"))
					{
						if(prod.findElements(By.className(objMap.getLocator("strcouponState"))).size()>0)
						{
							String PriceEach[]=priceEach.substring(1).split("/");
							double pricematch=Double.parseDouble(PriceEach[0]) * Double.parseDouble(quantity);
							if(Double.parseDouble(String.format("%.2f", pricematch))>Double.parseDouble(price.substring(1)))
							{
								RESULT.passed("Price Match for coupon product", "For coupon product,Price: "+price.substring(1)+" should be less than:"+String.format("%.2f", pricematch), 
								"calculated price from quantity and unit price is less than displayed price");
							}
							else {
								RESULT.failed("Price Match for coupon product", "For coupon product,Price: "+price.substring(1)+" should be less than:"+String.format("%.2f", pricematch), 
								"calculated price from quantity and unit price is not less than displayed price");
							}
						}
						else
						{
							String PriceEach[]=priceEach.substring(1).split("/");
							double pricematch=Double.parseDouble(PriceEach[0]) * Double.parseDouble(quantity);
							if(Double.parseDouble(String.format("%.2f", pricematch))==Double.parseDouble(price.substring(1)))
							{
								RESULT.passed("Price Match", "Price: "+price.substring(1)+" should match with:"+String.format("%.2f", pricematch), 
								"calculated price from quantity and unit price matched with displayed price");
							}
							else {
								RESULT.failed("Price Match", "Price: "+price.substring(1)+" should match with:"+String.format("%.2f", pricematch), 
								"calculated price from quantity and unit price did not match with displayed price");
							}
						}
					}
					else
					{
						RESULT.warning("Price Match", "Unit price should be in each unit per item", "Unit price is in diff unit: "+priceEach.split("/")[1]);
					}
*/
					// match details of each product
					if ((lstouterMap.get(k).get("prod" + k).get("Quantity")
							.toString().equalsIgnoreCase(quantity))
							&& (lstouterMap.get(k).get("prod" + k).get("Name")
									.toString().startsWith(name))
									&& (lstouterMap.get(k).get("prod" + k).get("Price")
											.toString().equalsIgnoreCase(price))) {
						RESULT.passed("Product details matched","Cart page should contain details of item:"
								+ lstouterMap.get(k).get("prod" + k).get("Name")+" Quantity:"+lstouterMap.get(k).get("prod" + k).get("Quantity")+
								"Price:"+lstouterMap.get(k).get("prod" + k).get("Price"),"All details of a Product:"+ name+" Quantity:"+quantity+
								"Price:"+price+ "matched");
					}
					else
					{
						RESULT.failed("Product details matched","Cart page should contain details of item:"
								+ lstouterMap.get(k).get("prod" + k).get("Name")+" Quantity:"+lstouterMap.get(k).get("prod" + k).get("Quantity")+
								"Price:"+lstouterMap.get(k).get("prod" + k).get("Price"),"All details of a Product:"+ name+" Quantity:"+quantity+
								"Price:"+price+ " did not matched");
						i++;
						return;
					}
					k++;
				}
			/*	// Sub total of all products
				String sbtotal1 = uiDriver.getwebDriverLocator(objMap
						.getLocator("strsubTotalPage")).getText();
				String sbtotal[]=sbtotal1.split(" ");
				// verify details with previously retrieved from pop up menu
				if (sbtotal[1].equals(subtotal) && i == 0) {
					RESULT.passed("All product's details and Subtotal",
							"All Product details and subtotal:"+sbtotal[1]+" should match ",
							"All Product and subtotal:"+subtotal+" matched");
				} else {
					RESULT.failed("All product's details and Subtotal",
							"All Product details and subtotal:"+sbtotal[1]+" should match ",
							"one or more product details or subtotal:"+subtotal+" could not match");
				}*/
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}

	}

	private enum choosedOpt {
		Select, SelectPickup, Add, AddCheckingAcc, Delete, Edit
	};

	/**
	 * Function Name: 	FD_paymentOption()
	 * Purpose: 		Select/ add / delete / edit credit card, Bank Account details and EBT card details
	 * Created By:		Avani Thakkar 
	 * Created Date:	18th May,2015
	 * Modified By: 	Avani Thakkar
	 * Modified Date: 	7th September,2015
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
			String cards = uiDriver.getwebDriverLocator(objMap.getLocator("lstpayment")).getAttribute("data-item-count");
			// String invalArg = null;
			String mycard1 = cardDetail;
			String spltSel[] = mycard1.split("\\n");
			String mycard = "";
			for (int i = 0; i < spltSel.length; i++) {
				mycard = mycard + spltSel[i] + "\n";
			}
			int a = 0;
			// replace two new line character with single to match in the
			// applecation
			mycard = mycard.replaceAll("\n\n", "\n");

			// choose option for operation on details of card/account
			String choosedOpt = AddEditDeleteSelect;
			switch (ChooseOpt.valueOf(choosedOpt)) {
			// select card having details same as given in excel
			case Select:
				if (cards.equals("0")) {
					RESULT.failed("Selection of Credit/EBT card/checking account ",
							mycard+":Credit/EBT card/checking account should get selected",
					"No credit/EBT card/checking account available in list to select");
					return;
				}
				else{
					List<WebElement> cardList = uiDriver.getwebDriverLocator(objMap.getLocator("lstpayment")).findElements(By.className(objMap.getLocator("lstcards1")));
					for (WebElement card : cardList) {
						// Retrieve one by one all card details to compare with
						// given card detail
						String details = card.getText();
						if (details.startsWith(mycard)) {
							a++;
							card.findElement(By.tagName(objMap.getLocator("radcard"))).click();
							RESULT.passed("Selection of Credit/EBT card/checking account",
									mycard+":Credit/EBT card/checking account should get selected",
									"Selected card/account is:"+ details);
							break;
						}
					}
					if (a == 0 && FlexibilityFlg.equalsIgnoreCase("Yes")) {
						RESULT.warning("Selection of Credit/EBT card/checking account",
								mycard+":Credit/EBT card/checking account should get selected",
								"No credit/EBT card/checking account matched with given details:"+mycard);
						cardList.get(0).findElement(By.tagName(objMap.getLocator("radcard"))).click();
						//						String spltdetails[] = cardList.get(0).getText().split("\n");
						RESULT.passed("Selection of Credit/EBT card/checking account",
								mycard+":Credit/EBT card/checking account should get selected",
								mycard+":card is not available in system so first available and selected card/account is:"+cardList.get(0).getText());

					} else if (a == 0 && FlexibilityFlg.equalsIgnoreCase("No"))
					{
						RESULT.failed("Selection of Credit/EBT card/checking account",
								mycard+":Credit/EBT card/checking account should get selected",
								"No credit/EBT card/checking account matched with given details:"+mycard);
						return;
					}
				}
				break;
			case Delete:
				if (cards.equals("0")) {
					RESULT.failed("Selection of Credit/EBT card/checking account ",
							mycard+":Credit/EBT card/checking account should get selected",
					"No credit/EBT card/checking account available in list to Delete");
					return;
				}
				else{
					// delete particular card from list
					List<WebElement> cardListdel = uiDriver.getwebDriverLocator(objMap.getLocator("lstpayment")).findElements(By.className(objMap.getLocator("lstcards1")));
					System.out.println("cards:" + cardListdel.size());
					for (WebElement card : cardListdel) {
						// Retrieve one by one all card details to compare with
						// given card detail
						String details = card.getText();
						System.out.println("\n retrieved card details:"+ details);
						if (details.startsWith(mycard)) {
							WebElement delete = card.findElement(By.xpath(objMap.getLocator("btndelCard")));
							if (delete.isEnabled()) {
								delete.click();
								a++;
								uiDriver.click("btndelConfm");
								String cardsUpdate = uiDriver.getwebDriverLocator(objMap.getLocator("lstpayment")).getAttribute("data-item-count");
								System.out.println("before:" + cards + "after:"+ cardsUpdate);
								if (Integer.parseInt(cards)== Integer.parseInt(cardsUpdate)-1) {
									String spltdetails[] = details.split("\n");
									RESULT.passed("Deletion of Credit/EBT card/checking account",
											mycard+":Credit/EBT card/checking account should get deleted",
											"Deleted card/account is:"+ details);
								} else{
									RESULT.failed("Deletion of Credit/EBT card/Checking account",
											mycard+":Credit/EBT card/Checking account should get deleted",
									"Credit/EBT card/Checking account could not get deleted and count could not get update");
									return;
								}
							} else if ((!delete.isEnabled())
									&& cards.equals("1")) {
								RESULT.failed("Deletion of Credit/EBT card/Checking account",
										mycard+":Credit/EBT card/Checking account should get deleted",
								"Delete button not available as there is only one card/account available in the system.");
								return;
							}
							break;
						}
					}
					if (a == 0) {
						RESULT.failed("Deletion of Credit/EBT card/Checking account",
								mycard+":Credit/EBT card/Checking account should get deleted",
								"Credit/EBT card/Checking account with given details:"+	mycard+" not found");
						return;
					}
				}
				break;
			case Edit:
				if (cards.equals("0")) {
					RESULT.failed("Editing of Credit/EBT card/checking account ",
							mycard+":Credit/EBT card/checking account should get Edited",
					"No credit/EBT card/checking account available in list to edit");
					return;
				}
				else{
					// edit particular card with list to edit some fields
					List<WebElement> cardListedt = uiDriver.getwebDriverLocator(objMap.getLocator("lstpayment")).findElements(By.className(objMap.getLocator("lstcards1")));
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
								uiDriver.setValue("txtcardStreetAdd",CardStreetAdd);
								uiDriver.setValue("txtcardAppNum", CardAppNum);
								uiDriver.setValue("txtcardAddLine2",CardAddLine2);
								uiDriver.setValue("drpcardCountry", CardCountry);
								uiDriver.setValue("txtcardCity", CardCity);
								uiDriver.setValue("drpcardState", CardState);
								uiDriver.setValue("txtcardZip", CardZip);
								uiDriver.getwebDriverLocator(objMap.getLocator("lstcreditCard")).findElement(By.name(objMap.getLocator("txtphoneNum"))).sendKeys(PhoneNum);
								// provide credit card details like card type
								// and expiration month and year of card
								uiDriver.setValue("drpcardtype", CardOrAcctype);
								uiDriver.setValue("drpexpiryMnth", ExpiryMnth);
								uiDriver.setValue("drpexpiryYr", ExpiryYr);
								uiDriver.click("btnsaveCreditCard");
								wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(objMap.getLocator("btndonePaymnt"))));
							}
							// check if it is bank account
							else if (uiDriver.getwebDriverLocator(objMap.getLocator("btnsaveBank")).isDisplayed()) {
								// select type of account
								if (CardOrAcctype.equalsIgnoreCase("Checking"))
									uiDriver.click("radchecking");
								else if (CardOrAcctype.equalsIgnoreCase("savings"))
									uiDriver.click("radsavings");
								else
								{	RESULT.failed("Editing of checking account",
										mycard+":Bank account should get edited with given details",
										"You have entered invalid account type other than checking and savings:"+CardOrAcctype);
								return;
								}
								// provide name and address details
								// provide account holder name and other bank
								// details
								uiDriver.getwebDriverLocator(objMap.getLocator("lstbank")).findElement(By.name(objMap.getLocator("txtnameOnCard"))).clear();
								uiDriver.getwebDriverLocator(objMap.getLocator("lstbank")).findElement(By.name(objMap.getLocator("txtnameOnCard"))).sendKeys(NameOnCard);
								uiDriver.setValue("txtroutenum", routing);// provide
								// routing
								// #
								uiDriver.setValue("txtbank", Bank);// provide
								// bank name
								// provide address details
								uiDriver.getwebDriverLocator(objMap.getLocator("lstbank")).findElement(By.name(objMap.getLocator("drpcardCountry"))).sendKeys(CardCountry);
								uiDriver.getwebDriverLocator(objMap.getLocator("lstbank")).findElement(By.name(objMap.getLocator("txtcardStreetAdd"))).sendKeys(CardStreetAdd);
								uiDriver.getwebDriverLocator(objMap.getLocator("lstbank")).findElement(By.name(objMap.getLocator("txtcardAppNum"))).sendKeys(CardAppNum);
								uiDriver.getwebDriverLocator(objMap.getLocator("lstbank")).findElement(By.name(objMap.getLocator("txtcardAddLine2"))).sendKeys(CardAddLine2);
								uiDriver.getwebDriverLocator(objMap.getLocator("lstbank")).findElement(By.name(objMap.getLocator("txtcardCity"))).sendKeys(CardCity);
								uiDriver.getwebDriverLocator(objMap.getLocator("lstbank")).findElement(By.name(objMap.getLocator("drpcardState"))).sendKeys(CardState);
								uiDriver.getwebDriverLocator(objMap.getLocator("lstbank")).findElement(By.name(objMap.getLocator("txtcardZip"))).sendKeys(CardZip);
								// provide Best# for billing inquiry
								uiDriver.getwebDriverLocator(objMap.getLocator("lstbank")).findElement(By.name(objMap.getLocator("txtphoneNum"))).sendKeys(PhoneNum);
								// accept the terms and conditions of checking
								// account
								uiDriver.click("chkacceptTerms");
								uiDriver.click("btnsaveBank");// click on save
								wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(objMap.getLocator("btndonePaymnt"))));
							}
							// check if it is EBT card
							else if (uiDriver.getwebDriverLocator(objMap.getLocator("btnsaveEBT")).isDisplayed()) {
								// provide name and address details
								uiDriver.getwebDriverLocator(objMap.getLocator("lstebt")).findElement(By.name(objMap.getLocator("txtnameOnCard"))).sendKeys(NameOnCard);
								uiDriver.getwebDriverLocator(objMap.getLocator("lstebt")).findElement(By.name(objMap.getLocator("drpcardCountry"))).sendKeys(CardCountry);
								uiDriver.getwebDriverLocator(objMap.getLocator("lstebt")).findElement(By.name(objMap.getLocator("txtcardStreetAdd"))).sendKeys(CardStreetAdd);
								uiDriver.getwebDriverLocator(objMap.getLocator("lstebt")).findElement(By.name(objMap.getLocator("txtcardAppNum"))).sendKeys(CardAppNum);
								uiDriver.getwebDriverLocator(objMap.getLocator("lstebt")).findElement(By.name(objMap.getLocator("txtcardAddLine2"))).sendKeys(CardAddLine2);
								uiDriver.getwebDriverLocator(objMap.getLocator("lstebt")).findElement(By.name(objMap.getLocator("txtcardCity"))).sendKeys(CardCity);
								uiDriver.getwebDriverLocator(objMap.getLocator("lstebt")).findElement(By.name(objMap.getLocator("drpcardState"))).sendKeys(CardState);
								uiDriver.getwebDriverLocator(objMap.getLocator("lstebt")).findElement(By.name(objMap.getLocator("txtcardZip"))).sendKeys(CardZip);
								// provide Best# for billing inquiry
								uiDriver.getwebDriverLocator(objMap.getLocator("lstebt")).findElement(By.name(objMap.getLocator("txtphoneNum"))).sendKeys(PhoneNum);
								uiDriver.click("btnsaveEBT");
								wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(objMap.getLocator("btndonePaymnt"))));
							}
							if (uiDriver.getwebDriverLocator(objMap.getLocator("btnsaveEBT")).isEnabled()
									|| uiDriver.getwebDriverLocator(objMap.getLocator("btnsaveCreditCard")).isEnabled()
									|| uiDriver.getwebDriverLocator(objMap.getLocator("btnsaveBank")).isEnabled()) {
								RESULT.failed("Edit existing Credit/EBT card/Checking account",
										mycard+":Credit/EBT card/checking account should get edited with given details",
								"one or more given detail is incorrect so could not save edited details");
								return;
							} else {
								RESULT.passed("Edit existing Credit/EBT card/Checking account",
										mycard+":Credit/EBT card/checking account should get edited with given details",
								"Credit/EBT card updated with given details");
							}
							break;
						}
					}
					if (a == 0) {
						RESULT.failed("Edit existing Credit or EBT card",
								mycard+":Credit or EBT card should get updated with given details",
						"No credit or EBT card matched with given details to edit");
						return;
					}
				}
				break;
			case Add:
				// click on add new card details
				uiDriver.getwebDriverLocator(objMap.getLocator("lstpayment")).findElement(By.xpath(objMap.getLocator("btnaddCard"))).click();
				// check if bank account is to be added or credit card
				if (cardDetail.equalsIgnoreCase("credit card")&& uiDriver.isElementPresent(By.xpath(objMap.getLocator("radcreditAdd")))) {
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
					uiDriver.getwebDriverLocator(objMap.getLocator("lstcreditCard")).findElement(By.name(objMap.getLocator("txtphoneNum"))).sendKeys(PhoneNum);
					// provide
					// Best# for
					// billing
					// inquiry
					// click on save changes
					uiDriver.click("btnsaveCreditCard");
					wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(objMap.getLocator("btndonePaymnt"))));
				} else if (cardDetail.equalsIgnoreCase("Bank")&& uiDriver.isElementPresent(By.xpath(objMap.getLocator("radcreditAdd")))) {
					uiDriver.click("radbankAdd");
					// select account type
					if (CardOrAcctype.equalsIgnoreCase("Checking")) {
						uiDriver.click("radchecking");
					} else if (CardOrAcctype.equalsIgnoreCase("savings")) {
						uiDriver.click("radsavings");
					} else {
						RESULT.failed("Addition of bank account",
								"Bank account should get added with given details",
								"You have entered invalid account type other than checking and savings:"+CardOrAcctype);
						return;
					}
					// provide account holder name and other bank details
					uiDriver.getwebDriverLocator(objMap.getLocator("lstbank")).findElement(By.name(objMap.getLocator("txtnameOnCard"))).sendKeys(NameOnCard);
					uiDriver.getwebDriverLocator(objMap.getLocator("lstbank")).findElement(By.name(objMap.getLocator("txtcardNum"))).sendKeys(CardNum);
					uiDriver.setValue("txtcardverif", CardNum);// verify
					// provided
					// account #
					uiDriver.setValue("txtroutenum", routing);// provide
					// routing #
					// provide address details
					uiDriver.getwebDriverLocator(objMap.getLocator("lstbank")).findElement(By.name(objMap.getLocator("drpcardCountry"))).sendKeys(CardCountry);
					uiDriver.getwebDriverLocator(objMap.getLocator("lstbank")).findElement(By.name(objMap.getLocator("txtcardStreetAdd"))).sendKeys(CardStreetAdd);
					uiDriver.getwebDriverLocator(objMap.getLocator("lstbank")).findElement(By.name(objMap.getLocator("txtcardAppNum"))).sendKeys(CardAppNum);
					uiDriver.getwebDriverLocator(objMap.getLocator("lstbank")).findElement(By.name(objMap.getLocator("txtcardAddLine2"))).sendKeys(CardAddLine2);
					uiDriver.getwebDriverLocator(objMap.getLocator("lstbank")).findElement(By.name(objMap.getLocator("txtcardCity"))).sendKeys(CardCity);
					uiDriver.getwebDriverLocator(objMap.getLocator("lstbank")).findElement(By.name(objMap.getLocator("drpcardState"))).sendKeys(CardState);
					uiDriver.getwebDriverLocator(objMap.getLocator("lstbank")).findElement(By.name(objMap.getLocator("txtcardZip"))).sendKeys(CardZip);
					// provide Best# for billing inquiry
					uiDriver.getwebDriverLocator(objMap.getLocator("lstbank")).findElement(By.name(objMap.getLocator("txtphoneNum"))).sendKeys(PhoneNum);
					// accept the terms and conditions of checking account
					uiDriver.click("chkacceptTerms");
					uiDriver.click("btnsaveBank");// click on save
					wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(objMap.getLocator("btndonePaymnt"))));
				} else if (cardDetail.equalsIgnoreCase("EBT")&& uiDriver.isElementPresent(By.xpath(objMap.getLocator("radebtAdd")))) {
					uiDriver.click("radebtAdd");
					uiDriver.getwebDriverLocator(objMap.getLocator("lstebt")).findElement(By.name(objMap.getLocator("txtnameOnCard"))).sendKeys(NameOnCard);
					uiDriver.getwebDriverLocator(objMap.getLocator("lstebt")).findElement(By.name(objMap.getLocator("txtcardNum"))).sendKeys(CardNum);
					// provide address details
					uiDriver.getwebDriverLocator(objMap.getLocator("lstebt")).findElement(By.name(objMap.getLocator("drpcardCountry"))).sendKeys(CardCountry);
					uiDriver.getwebDriverLocator(objMap.getLocator("lstebt")).findElement(By.name(objMap.getLocator("txtcardStreetAdd"))).sendKeys(CardStreetAdd);
					uiDriver.getwebDriverLocator(objMap.getLocator("lstebt")).findElement(By.name(objMap.getLocator("txtcardAppNum"))).sendKeys(CardAppNum);
					uiDriver.getwebDriverLocator(objMap.getLocator("lstebt")).findElement(By.name(objMap.getLocator("txtcardAddLine2"))).sendKeys(CardAddLine2);
					uiDriver.getwebDriverLocator(objMap.getLocator("lstebt")).findElement(By.name(objMap.getLocator("txtcardCity"))).sendKeys(CardCity);
					uiDriver.getwebDriverLocator(objMap.getLocator("lstebt")).findElement(By.name(objMap.getLocator("drpcardState"))).sendKeys(CardState);
					uiDriver.getwebDriverLocator(objMap.getLocator("lstebt")).findElement(By.name(objMap.getLocator("txtcardZip"))).sendKeys(CardZip);
					// provide Best# for billing inquiry
					uiDriver.getwebDriverLocator(objMap.getLocator("lstebt")).findElement(By.name(objMap.getLocator("txtphoneNum"))).sendKeys(PhoneNum);
					uiDriver.click("btnsaveEBT");
					wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(objMap.getLocator("btndonePaymnt"))));
				} else{
					RESULT.failed("Adition of Credit/EBT card/Bank account",
							cardDetail+" :should get added with given details",
							"given argument is incorrect or radiobutton for given selection is not available:"+cardDetail);
					return;
				}
				String cardsUpdate = uiDriver.getwebDriverLocator(objMap.getLocator("lstpayment")).getAttribute("data-item-count");
				if (Integer.parseInt(cards)+1== Integer.parseInt(cardsUpdate))
					RESULT.passed("Adition of Credit/EBT card/Bank account",
							cardDetail+" :should get added with given details",
							cardDetail+" : with given name:"+ NameOnCard + " added");
				else{
					RESULT.failed("Adition of Credit/EBT card/Bank account",
							cardDetail+" :should get added with given details",
							cardDetail+" :could not get added as one or more details is incorrect");
					return;
				}
				// else{
				// String invald=
				// uiDriver.getwebDriverLocator(objMap.getLocator("txtphoneNum")).getAttribute("invalid");
				// if(invald.equals("true"))
				// RESULT.failed("Adition of Bank account",
				// "Bank account should get added with given details",
				// "Bank account with given details could not get added due to invalid value of:"+PhoneNum);
				// if(uiDriver.isElementPresent(By.xpath("//span[@fdform-error='phone'")))
				// {
				// // invalArg=PhoneNum;
				// RESULT.failed("Adition of Bank account",
				// "Bank account should get added with given details",
				// "Bank account with given details could not get added due to invalid value of:"+PhoneNum);
				// }
				// else
				// if(uiDriver.isElementPresent(By.xpath("//span[@fdform-error='cardNum'")))
				// {
				// // invalArg=CardNum;
				// RESULT.failed("Adition of Bank account",
				// "Bank account should get added with given details",
				// "Bank account with given details could not get added due to invalid value of:"+CardNum);}
				// else
				// RESULT.failed("Adition of Bank account",
				// "Bank account should get added with given details",
				// "Bank account with given details could not get added due to invalid value of some field");
				// }
				break;
			}
		} catch (NoSuchElementException e) {
			RESULT.failed("Element not found",
					"Desired : "+cardDetail+" :should get: "+AddEditDeleteSelect+"ed",
					"\n Exception catched:" + e);
			return;
		} catch (Exception e) {
			e.printStackTrace();
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
	public double yourCart(String productName)
	{
		double myprod_quantity=0.0;
		robot.moveToElement(uiDriver.getwebDriverLocator(objMap.getLocator("btnyourCart")));
		List<WebElement> Lstitems_row_1 = uiDriver.getwebDriverLocator(objMap.getLocator("lstpopupCart")).findElements(By.className(objMap.getLocator("lstcartline")));
		List<WebElement> nam;
		boolean item_check_flag=false;
		
		System.out.println("\n Number of items in cart before adding same item to the cart is "+ Lstitems_row_1.size());
		try
		{
			if (Lstitems_row_1.size() == 0) 
			{
				//actions.moveToElement(uiDriver.getwebDriverLocator(objMap.getLocator("btnyourCart"))).build().perform();
				robot.moveToElement(uiDriver.getwebDriverLocator(objMap.getLocator("btnyourCart")));				
				RESULT.warning("Your Cart is Empty",
						"0 item should be available in cart",
						"0 item available in your cart");
				return 0;
			} 
			else 
		{
			
				int j = 0;
//				int index_tbquant=0;
				for (WebElement item : Lstitems_row_1) {
					
					// name of product
					do {
						//actions.moveToElement(uiDriver.getwebDriverLocator(objMap.getLocator("btnyourCart"))).build().perform();
						robot.moveToElement(uiDriver.getwebDriverLocator(objMap.getLocator("btnyourCart")));
						nam = item.findElements(By.xpath(objMap.getLocator("lstNAME")));
						// name = nam.get(j).getText();
					} while (nam.get(j).getText() == null
							|| nam.get(j).getText().equals(""));
					// quantity of product
					//actions.moveToElement(uiDriver.getwebDriverLocator(objMap.getLocator("btnyourCart"))).build().perform();
					robot.moveToElement(uiDriver.getwebDriverLocator(objMap.getLocator("btnyourCart")));
					List<WebElement> qaunt = item.findElements(By.xpath(objMap.getLocator("lstqnty")));
					List<WebElement> quant_drp=item.findElements(By.xpath(objMap.getLocator("lstqntyDropdown")));
					if (nam.get(j).getText().contains(productName)) 
					{
						if(quant_drp.size()>0)
						{
							myprod_quantity+=1;
							//item_check_flag=true;
//							return myprod_quantity;
						
						}
						else
						{
							myprod_quantity += Double.parseDouble(qaunt.get(0).getAttribute("value"));
//							index_tbquant++;
							//item_check_flag=true;
							
						}
					} 
//					index_tbquant++;
					j++;
				}
//				if(item_check_flag==true)
//				{
//					RESULT.done("product: " +productName,"" ,productName+ " in cart is " +myprod_quantity);
					return myprod_quantity;
//					}
				
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return myprod_quantity;
			
	}

	public void orderCreationPopup(String orderflag_new_modify)
	{
		int count=0;
		if (uiDriver.popup_isAlertPresent(5));
		

		do {
			if (webDriver.findElements(By.xpath(objMap.getLocator("popaddToCart11"))).size() > 0
					&& webDriver.findElement(By.xpath(objMap.getLocator("popaddToCart11"))).isDisplayed() ) {
				// If user wants to add to a new order
				if (orderflag_new_modify.equalsIgnoreCase("New")) {
					uiDriver.getwebDriverLocator(objMap.getLocator("popaddToCart11")).click();
					break;
				}
				/*
				 * If user wants to modify an existing order
				 * Pre-condtion : Default 0 index provided to select
				 * dropdownlist
				 */
				else if (orderflag_new_modify.equalsIgnoreCase("Modify")) {
					uiDriver.getwebDriverLocator(objMap.getLocator("popaddToCart12")).click();
					Select select = new Select(uiDriver.getwebDriverLocator(objMap.getLocator("popaddToCart21")));
					select.selectByIndex(0);
					uiDriver.getwebDriverLocator(objMap.getLocator("popaddToCart22")).click();
					break;
				} else {
					uiDriver.getwebDriverLocator(objMap.getLocator("btnClose")).click();
					break;
				}
				// 
			}
			count++;
		} while (count < 5);
	}


	public void FD_addToCart(String prodct, String orderflag_new_modify,
			String quantity, String clickOrHover, String flexiFlag,
			String sales_unit, String packaging) throws InterruptedException {
		//Configuration GCONFIG = Harness.GCONFIG;

		double myprod_initialexistingqty = 0.0;
		double myprod_finalexistingqty=0.0;
		double myprod_newquantity=0.0;
		double clickerror_added_quantity=0.0;
		boolean cust_flag=false;
		boolean out_of_stock_flag = false;
		boolean reorderpage_flag=false;
		String errormsg=null;
		//String productFullName = null;
		String first_item;
		clickOrHover = clickOrHover.toLowerCase();
		WebElement product = null;
		List<WebElement> drp_cust;

		//Taking first item locator for Regular flow and reorder flow in case of flexibility="Yes"
try{
		if (webDriver.findElements(By.xpath(objMap.getLocator("tabyourTopItems"))).size() > 0)
		{
			//Reorder page
			uiDriver.click("btngridReorder");
			SleepUtils.getInstance().sleep(TimeSlab.LOW);

			reorderpage_flag=true;
			first_item = objMap.getLocator("lnkfirstItemReorder");
			clickOrHover="click";
		} 
		else 
		{
			//Normal page default first item
			first_item = objMap.getLocator("lnkfirstItem");
		}

		try 
		{
			product = webDriver.findElement(By.partialLinkText(prodct.trim()));
			productFullName = product.getText();

		}
		catch (Exception e)
		{
			// to handle the pagination
			try {
				if (webDriver.findElements(By.xpath(objMap.getLocator("btnshowAll"))).size() > 0)
				{      
					// click on the show all button
					uiDriver.getwebDriverLocator(objMap.getLocator("btnshowAll")).click();
					// wait for the products to load
					wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(objMap.getLocator("btnpagePrev"))));
				}
				product = webDriver.findElement(By.partialLinkText(prodct));
				productFullName = product.getText();
			} 
			catch (NoSuchElementException e1) 
			{
				if (flexiFlag.equalsIgnoreCase("Yes")) 
				{
					product = uiDriver.getwebDriverLocator(first_item);
					productFullName = product.getText();
					RESULT.warning("Item unavailability",
							"User specified Item: " + prodct+ " shows unavailablility",
							"Item: " + prodct+ " is not available. Item: "+ productFullName + " is added to cart ");
				}
				else if (flexiFlag.equalsIgnoreCase("No")) 
				{
					RESULT.failed("Item unavalibality",
							"User specified Item: " + prodct+ " shows unavailablility",
							"Item: " + prodct+ " is not available ");
					return;
				}
			}
		}
		//
		// For the customizable items check if the textbox for quantity is available or not
		robot.moveToElement(product);
	
		//To check whether product is Out of Stock
		String product_parts[];
		product_parts = productFullName.split("\n");
		if(product_parts.length > 1)
		{
			productFullName = product_parts[1];
		}
		System.out.println("1st: "+productFullName);
//		String reorderpage_item_unavailable="//a[contains(text(),'"+productFullName+"')]/ancestor::div[@class='itemlist-item-header']/following-sibling::div[@class='itemlist-item-unavailable']";
		//String normalpage_item_unavailable="//a[contains(text(),'"+productFullName+"')]/ancestor::div[@class='portrait-item-header']/following-sibling::div[@class='portrait-item-price']";
		try{
			if(reorderpage_flag==true)
			{
//				if(webDriver.findElements(By.xpath(reorderpage_item_unavailable)).size()>0) 
//				{
//					out_of_stock_flag=true;
//				}
				List<WebElement> RowProdList=webDriver.findElements(By.xpath("//*[@id='productlist']/ol/li"));
				for(WebElement prod1:RowProdList)
				{
					if(prod1.findElement(By.xpath("//div[@class='itemlist-item-header']/a")).getText().contains(productFullName))
					{
						if(prod1.getAttribute("class").contains("unavailable"))
						{
							out_of_stock_flag=true;
						}
					}
					else
					{
						if(webDriver.findElement(By.className(objMap.getLocator("divproductClassHover"))).findElement(By.tagName("li")).getAttribute("class").contains("unavailable"))
						{
							out_of_stock_flag=true;
						}
					}
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		if(out_of_stock_flag==true)                  
		{
			
			robot.moveToElement(product);
			product.click();
			SleepUtils.getInstance().sleep(TimeSlab.YIELD);

			if((!(prdct_department==null)) && prdct_department.equalsIgnoreCase("WinesSpirits"))

			{
				if (webDriver.findElements(By.className(objMap.getLocator("alertAlcoholClick"))).size() > 0)
				{
					do{
						uiDriver.getwebDriverLocator(objMap.getLocator("btnalcoholAlertAccept")).click();
					}while(uiDriver.getwebDriverLocator(objMap.getLocator("btnalcoholAlertAccept")).isDisplayed());
					
					RESULT.passed("Health warning alert ",
							"Health warning : Alcohol alert should be accepted",
							"Health warning : Alcohol alert accepted");
				}
				else
				{
					RESULT.failed("Health warning alert ",
							"Health warning : Alcohol alert should be accepted",
							"Health warning : Alcohol alert did not appear");
				}
			}
			uiDriver.selenium.waitForPageToLoad(PageLoadTime);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(objMap.getLocator("btnback"))));
			if (webDriver.findElements(By.xpath(objMap.getLocator("diaavailability"))).size() > 0) 
			{
				// for out of stock start
				RESULT.warning("Out of stock product",
						"Product is Out of stock",
						"Message: "+ uiDriver.getwebDriverLocator(objMap.getLocator("strprodUnavailability")).getText());
				if((!(prdct_department==null)) && prdct_department.equalsIgnoreCase("WinesSpirits"))
				{
					RESULT.failed("Alcohol alert ",
							"For wines & spirits navigation by Back button should result in to list of products page",
							"For wines & spirits navigation by Back button results in to Health warning");
					return;
				}
					
				uiDriver.click("btnback");
				uiDriver.selenium.waitForPageToLoad(PageLoadTime);
				wait.until(ExpectedConditions.presenceOfElementLocated(By.className(objMap.getLocator("strsortWaitElement"))));

				System.out.println(productFullName+" Product is out of stock");
				if (flexiFlag.equalsIgnoreCase("Yes")) 
				{
					product = uiDriver.getwebDriverLocator(first_item);
					productFullName = product.getText();
					System.out.println(productFullName);
					robot.moveToElement(product);
					//check if the default first item is also unavailable then test will terminate giving a warning of out of stock
//					reorderpage_item_unavailable="//a[contains(text(),'"+productFullName+"')]/ancestor::div[@class='itemlist-item-header']/following-sibling::div[@class='itemlist-item-unavailable']";
//					if(webDriver.findElements(By.xpath(reorderpage_item_unavailable)).size()>0)
//					{
//						RESULT.warning("Out of stock product", "First product on the page should be out of stock", "First product on the page is out of stock");
//						return;
//					}
//					else
//					{
//						if(webDriver.findElement(By.className(objMap.getLocator("divproductClassHover"))).findElement(By.tagName("li")).getAttribute("class").contains("unavailable"))
//						{
//							RESULT.warning("Out of stock product", "First product on the page should be out of stock", "First product on the page is out of stock");
//							return;
//						}
//					}
					List<WebElement> RowProdList=webDriver.findElements(By.xpath(objMap.getLocator("strreorderAddToCart")));
					for(WebElement prod1:RowProdList)
					{
						if(prod1.findElement(By.xpath(objMap.getLocator("lstoneColmnProdName"))).getText().contains(productFullName))
						{
							if(prod1.getAttribute("class").contains("unavailable"))
							{
								RESULT.warning("Out of stock product", "First product on the page should be out of stock", "First product on the page is out of stock");
								return;
							}
						}
						else
						{
							if(webDriver.findElement(By.className(objMap.getLocator("divproductClassHover"))).findElement(By.tagName("li")).getAttribute("class").contains("unavailable"))
							{
								RESULT.warning("Out of stock product", "First product on the page should be out of stock", "First product on the page is out of stock");
								return;
							}
						}						
					}	

				}
				else
				{
					RESULT.failed("Out of stock product", productFullName+": Desired product should be out of stock", productFullName+": Desired product is out of stock");
					return;
				}
			}
		}

		// retrieve initial Quantity of the product to be added before adding it to cart

		myprod_initialexistingqty=uiDriver.yourCart(productFullName);

		// wait for the addtocart button for normal flow
		// Normal flow
		// check if user want to add item by mouse hover or by clicking on that item
		if (clickOrHover.equals("click")) {

			try {
				JavascriptExecutor jse = (JavascriptExecutor) webDriver;
				jse.executeScript("arguments[0].scrollIntoView()", product);
		
				if(webDriver.findElements(By.xpath(objMap.getLocator("btnsearchAddToCartCustomize"))).size()>0)
				{
					cust_flag=true;
					System.out.println("Product is customizable");
				}
				//jse.executeScript("arguments[0].click()", product);
				//robot.moveToElement(product);
				if (((String) GCONFIG.get("Browser")).equalsIgnoreCase("FIREFOX") || ((String) GCONFIG.get("Browser")).equalsIgnoreCase("CHROME")
						||((String) GCONFIG.get("Browser")).equalsIgnoreCase("IE")) 
				{
					jse.executeScript("arguments[0].click()", product);
				}
				else if(((String) GCONFIG.get("Browser")).equalsIgnoreCase("SAFARI"))
				{
					product.click();
				}
			
				if((!(prdct_department==null)) && prdct_department.equalsIgnoreCase("WinesSpirits"))
				{
					wait.until(ExpectedConditions.presenceOfElementLocated(By.className(objMap.getLocator("alertAlcoholClick"))));
					if (webDriver.findElements(By.className(objMap.getLocator("alertAlcoholClick"))).size() > 0)
					{
						do{
							uiDriver.getwebDriverLocator(objMap.getLocator("btnalcoholAlertAccept")).click();
						}while(uiDriver.getwebDriverLocator(objMap.getLocator("btnalcoholAlertAccept")).isDisplayed());
						RESULT.passed("Alcohol alert ",
								"Alcohol alert should be accepted",
								"Alcohol alert passed");
					}
					else
					{
						RESULT.failed("Alcohol alert ",
								"Alcohol alert should be accepted",
								"Alcohol alert did not show up");
					}
				}
					wait.until(ExpectedConditions.invisibilityOfElementLocated(By.partialLinkText(productFullName)));
					wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(objMap.getLocator("btnback"))));
				
				if(cust_flag==true)
				{
					wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(objMap.getLocator("lstdrpCustomizeProduct"))));
					drp_cust=uiDriver.getwebDriverLocator(objMap.getLocator("lstdrpCustomizeProduct")).findElements(By.tagName("select"));
					if(drp_cust.size()>0)
					{
						for(WebElement ele_drp_cust:drp_cust)
						{
	//    								if(i==1)
	//    								{
	//    									i++;
	//    									continue;
	//    								}
							Select s=new Select(ele_drp_cust);
							s.selectByIndex(1);
						}
					}
				}
				// set the quantity of particular product

				if(webDriver.findElement(By.className(objMap.getLocator("divproductDrpQty"))).findElements(By.className(objMap.getLocator("drpqty"))).size()>0)
				//if(webDriver.findElements(By.xpath(objMap.getLocator("drpquantityClick"))).size()>0)
				{
					Select salesunit_item=new Select(webDriver.findElement(By.tagName("Select")));
					salesunit_item.selectByIndex(1);
				}
				else if(webDriver.findElement(By.className(objMap.getLocator("divproductClassClick"))).findElements(By.className(objMap.getLocator("txtqty"))).size()>0)
//				else if (webDriver.findElements(By.xpath(objMap.getLocator("txtdealQauntClick"))).size() > 0)
				{
					if (!(quantity.equals(null) || quantity == "" || quantity == " ")) {
						System.out.println("reached");
						if (((String) GCONFIG.get("Browser")).equalsIgnoreCase("FIREFOX") || ((String) GCONFIG.get("Browser")).equalsIgnoreCase("CHROME")) {
							webDriver.findElement(By.className(objMap.getLocator("divproductClassClick"))).findElement(By.className(objMap.getLocator("txtqty"))).click();
							actions.keyDown(Keys.CONTROL).sendKeys("a").keyUp(Keys.CONTROL).sendKeys(Keys.DELETE).perform();
						}
						else
						{
							webDriver.findElement(By.className(objMap.getLocator("divproductClassClick"))).findElement(By.className(objMap.getLocator("txtqty"))).click();
							webDriver.findElement(By.className(objMap.getLocator("divproductClassClick"))).findElement(By.className(objMap.getLocator("txtqty"))).sendKeys(Keys.BACK_SPACE);
							webDriver.findElement(By.className(objMap.getLocator("divproductClassClick"))).findElement(By.className(objMap.getLocator("txtqty"))).sendKeys(Keys.BACK_SPACE);
							webDriver.findElement(By.className(objMap.getLocator("divproductClassClick"))).findElement(By.className(objMap.getLocator("txtqty"))).sendKeys(Keys.BACK_SPACE);
							webDriver.findElement(By.className(objMap.getLocator("divproductClassClick"))).findElement(By.className(objMap.getLocator("txtqty"))).sendKeys(Keys.DELETE);
							webDriver.findElement(By.className(objMap.getLocator("divproductClassClick"))).findElement(By.className(objMap.getLocator("txtqty"))).sendKeys(Keys.DELETE);
							webDriver.findElement(By.className(objMap.getLocator("divproductClassClick"))).findElement(By.className(objMap.getLocator("txtqty"))).sendKeys(Keys.DELETE);
						}
						webDriver.findElement(By.className(objMap.getLocator("divproductClassClick"))).findElement(By.className(objMap.getLocator("txtqty"))).sendKeys(quantity);
					}
				}
				// click on add to cart
				webDriver.findElement(By.xpath(objMap.getLocator("btnaddToCart"))).click();
				selenium.waitForPageToLoad(PageLoadTime);
				SleepUtils.getInstance().sleep(TimeSlab.YIELD);

				// This will accept the alcohol terms and conditions while adding it to cart

				if((!(prdct_department==null)) && prdct_department.equalsIgnoreCase("WinesSpirits"))
				{
					if (webDriver.findElements(By.className(objMap.getLocator("alertAlcoholClick"))).size() > 0)
					{
						do{
							uiDriver.getwebDriverLocator(objMap.getLocator("btnalcoholAlertAccept")).click();
						}while(uiDriver.getwebDriverLocator(objMap.getLocator("btnalcoholAlertAccept")).isDisplayed());
						RESULT.passed("Alcohol alert ",
								"Alcohol alert should be accepted",
								"Alcohol alert passed");
					}
					else
					{
						RESULT.failed("Alcohol alert ",
								"Alcohol alert should be accepted",
								"Alcohol alert did not show up");
					}
				}
				uiDriver.orderCreationPopup(orderflag_new_modify);
			//	selenium.waitForPageToLoad(PageLoadTime);
				SleepUtils.getInstance().sleep(TimeSlab.YIELD);

				if (webDriver.findElements(By.cssSelector(objMap.getLocator("stritemInCartError"))).size() > 0)
				{
					clickerror_added_quantity = Double.parseDouble(uiDriver.getwebDriverLocator(objMap.getLocator("stritemInCartError")).getText().split(" ")[0]);
					errormsg=uiDriver.getwebDriverLocator(objMap.getLocator("stritemError")).getText();                   
				}
				else
				{
					wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText((objMap.getLocator("btncontShopping")))));
					System.out.println(webDriver.findElement(By.xpath(objMap.getLocator("straddedQuantity"))).getText());
					String qty[];
					qty=webDriver.findElement(By.xpath(objMap.getLocator("straddedQuantity"))).getText().split(":");
					myprod_newquantity=Double.parseDouble(qty[1]);
					System.out.println(myprod_newquantity);
				}
			} catch (Exception e1) 
			{
				e1.printStackTrace();
				System.out.println("Error in click method quantity try catch");
			}
		} 
		else if (clickOrHover.equals("hover")) {

			try {
				JavascriptExecutor jse = (JavascriptExecutor) webDriver;
				jse.executeScript("arguments[0].scrollIntoView()", product);
				
				robot.moveToElement(product);
				SleepUtils.getInstance().sleep(TimeSlab.YIELD);
				if(cust_flag==true)
				{
					if(webDriver.findElements(By.xpath(objMap.getLocator("btnsearchAddToCartCustomize"))).size() > 0){
						
						robot.moveToElement(uiDriver.getwebDriverLocator(objMap.getLocator("btnsearchAddToCartCustomize")));
						uiDriver.getwebDriverLocator(objMap.getLocator("btnsearchAddToCartCustomize")).click();
						SleepUtils.getInstance().sleep(TimeSlab.YIELD);
						//uiDriver.click("btnsearchAddToCartCustomize");
						wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(objMap.getLocator("btnaddToCartCustHover"))));
						System.out.println("Click2");
				
						if (webDriver.findElements(By.xpath(objMap.getLocator("txtqauntityCustHover"))).size() > 0) {
							drp_cust=uiDriver.getwebDriverLocator(objMap.getLocator("lstdrpCustomizeProductHover")).findElements(By.tagName("Select"));
							System.out.println("Click1");
							int i=1;
							for(WebElement ele_drp_cust:drp_cust)
							{
								//    uiDriver.DrawHighlight("ele_drp_cust", webDriver);
								//                                              if(i==1)
								//                                              {
								//                                                    i++;
								//                                                    continue;
								//                                              }
								System.out.println("drp");
								Select s=new Select(ele_drp_cust);
								s.selectByIndex(1);
							}
							System.out.println(objMap.getLocator("txtqauntityCustHover"));
							if (!(quantity.equals(null) || quantity == "" || quantity == " ")) {
								System.out.println("reached");
								if (((String) GCONFIG.get("Browser")).equalsIgnoreCase("FIREFOX") ) {
									uiDriver.getwebDriverLocator(objMap.getLocator("txtqauntityCustHover")).click();
									actions.keyDown(Keys.CONTROL).sendKeys("a").keyUp(Keys.CONTROL).sendKeys(Keys.DELETE).perform();
								}
								else  {
									uiDriver.getwebDriverLocator(objMap.getLocator("txtqauntityCustHover")).click();
									//actions.sendKeys(Keys.BACK_SPACE).perform();
									uiDriver.getwebDriverLocator(objMap.getLocator("txtqauntityCustHover")).sendKeys(Keys.DELETE);
									uiDriver.getwebDriverLocator(objMap.getLocator("txtqauntityCustHover")).sendKeys(Keys.DELETE);
									uiDriver.getwebDriverLocator(objMap.getLocator("txtqauntityCustHover")).sendKeys(Keys.DELETE);
									uiDriver.getwebDriverLocator(objMap.getLocator("txtqauntityCustHover")).sendKeys(Keys.BACK_SPACE);
									uiDriver.getwebDriverLocator(objMap.getLocator("txtqauntityCustHover")).sendKeys(Keys.BACK_SPACE);
									uiDriver.getwebDriverLocator(objMap.getLocator("txtqauntityCustHover")).sendKeys(Keys.BACK_SPACE);
								}
								
								uiDriver.getwebDriverLocator(objMap.getLocator("txtqauntityCustHover")).sendKeys(quantity);
							}

							// click on add to cart
							uiDriver.DrawHighlight("btnaddToCartCustHover", webDriver);
							webDriver.findElement(By.xpath(objMap.getLocator("btnaddToCartCustHover"))).click();
							selenium.waitForPageToLoad(PageLoadTime);
						}     
					}
				}
				
				else
				{
					if(webDriver.findElement(By.className(objMap.getLocator("divproductClassHover"))).findElements(By.className(objMap.getLocator("drpqty"))).size()>0)
					//if(webDriver.findElements(By.xpath(objMap.getLocator("drpqty"))).size()>0)
					{
						Select salesunit_item=new Select(webDriver.findElement(By.xpath(objMap.getLocator("drpqty"))));
						salesunit_item.selectByIndex(1);	
					}
					else if(webDriver.findElement(By.className(objMap.getLocator("divproductClassHover"))).findElements(By.className(objMap.getLocator("txtqty"))).size()>0)
					//else if (webDriver.findElements(By.xpath(objMap.getLocator("txtdealQuanty"))).size() > 0)
					{
						if (!(quantity.equals(null) || quantity == "" || quantity == " ")) {
							System.out.println("reached");
							if (((String) GCONFIG.get("Browser")).equalsIgnoreCase("FIREFOX") ) 
							{
								webDriver.findElement(By.className(objMap.getLocator("divproductClassHover"))).findElement(By.className(objMap.getLocator("txtqty"))).click();
								//uiDriver.getwebDriverLocator(objMap.getLocator("txtdealQuanty")).click();
								actions.keyDown(Keys.CONTROL).sendKeys("a").keyUp(Keys.CONTROL).sendKeys(Keys.DELETE).perform();
							}
							else 
							{
								webDriver.findElement(By.className(objMap.getLocator("divproductClassHover"))).findElement(By.className(objMap.getLocator("txtqty"))).click();
								webDriver.findElement(By.className(objMap.getLocator("divproductClassHover"))).findElement(By.className(objMap.getLocator("txtqty"))).sendKeys(Keys.DELETE);
								webDriver.findElement(By.className(objMap.getLocator("divproductClassHover"))).findElement(By.className(objMap.getLocator("txtqty"))).sendKeys(Keys.DELETE);
								webDriver.findElement(By.className(objMap.getLocator("divproductClassHover"))).findElement(By.className(objMap.getLocator("txtqty"))).sendKeys(Keys.DELETE);
								webDriver.findElement(By.className(objMap.getLocator("divproductClassHover"))).findElement(By.className(objMap.getLocator("txtqty"))).sendKeys(Keys.BACK_SPACE);
								webDriver.findElement(By.className(objMap.getLocator("divproductClassHover"))).findElement(By.className(objMap.getLocator("txtqty"))).sendKeys(Keys.BACK_SPACE);
								webDriver.findElement(By.className(objMap.getLocator("divproductClassHover"))).findElement(By.className(objMap.getLocator("txtqty"))).sendKeys(Keys.BACK_SPACE);
								
							}
						
							webDriver.findElement(By.className(objMap.getLocator("divproductClassHover"))).findElement(By.className(objMap.getLocator("txtqty"))).sendKeys(quantity);
						}
						// click on add to cart
						//actions.moveToElement(uiDriver.getwebDriverLocator(objMap.getLocator("btnsearchAddToCart"))).build().perform();
					
					}
					webDriver.findElement(By.className(objMap.getLocator("divproductClassHover"))).findElement(By.className(objMap.getLocator("btnaddToCartClass"))).click();
					//webDriver.findElement(By.xpath(objMap.getLocator("btnsearchAddToCart"))).click();
					selenium.waitForPageToLoad(PageLoadTime);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				// This will accept the alcohol terms and conditions while adding it to cart
				SleepUtils.getInstance().sleep(TimeSlab.MEDIUM);
				if (webDriver.findElements(By.className(objMap.getLocator("alertAlcohol"))).size() > 0) {
					do
					{
						uiDriver.getwebDriverLocator(objMap.getLocator("btnalcoholAlertAccept")).click();
					}while(uiDriver.getwebDriverLocator(objMap.getLocator("btnalcoholAlertAccept")).isDisplayed());
					
					RESULT.passed("Alcohol alert ",
							"Alcohol alert accepted",
					"Alcohol alert passed");
				}
				uiDriver.orderCreationPopup(orderflag_new_modify);
				selenium.waitForPageToLoad(PageLoadTime);
				//actions.moveToElement(product).build().perform();
				robot.moveToElement(product);
				if (webDriver.findElements(By.cssSelector(objMap.getLocator("stritemInCartError"))).size() > 0) {
					myprod_newquantity = Double.parseDouble(uiDriver.getwebDriverLocator(objMap.getLocator("stritemInCartError")).getText().split(" ")[0]);
					errormsg="0 added";
				}
				else if (webDriver.findElements(By.cssSelector(objMap.getLocator("stritemInCartSuccess"))).size() > 0)
				{
					myprod_newquantity = Double.parseDouble(uiDriver.getwebDriverLocator(objMap.getLocator("stritemInCartSuccess")).getText().split(" ")[0]);
				}
				// }

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		else
		{
			RESULT.failed("Parameter for Method :click/hover are not passed appropriately", 
					"Parameter for Method :click/hover should be passed appropriately", 
				"Parameter for Method :click/hover are not passed appropriately");
		}
		
		myprod_finalexistingqty=uiDriver.yourCart(productFullName);

		// Verify whether the product is added to the cart or not
		if(clickOrHover.equalsIgnoreCase("hover"))
		{
			if(myprod_finalexistingqty==myprod_newquantity)
			{
				RESULT.passed("Item added to cart", "Item addition should be successful to cart", "Item addition successful to cart");

				if(!(errormsg==null|| errormsg.equals("")))
				{
					RESULT.warning("Item addition unsuccessful", "Item addition should have some error generated", "Item addition has generated an error "+errormsg);
				}
			}
		}
		else if(clickOrHover.equalsIgnoreCase("click"))
		{
			if(myprod_finalexistingqty==myprod_initialexistingqty+Double.parseDouble(quantity))
			{
				RESULT.passed("Item added to cart", "Item addition should be successful to cart", "Item addition successful to cart with quantity :"+quantity);
			}
			else if(clickerror_added_quantity==myprod_finalexistingqty)
			{
				RESULT.warning("Item added to cart",
						"Item addition with variance in quantity is expected. Expected quantity is :"+myprod_initialexistingqty+myprod_newquantity,
						"Item addition with variance in quantity is expected.Actual quantity is :"+myprod_finalexistingqty);
			}
		}

		else {
			RESULT.failed("Item added to cart", "Item should be added ","Item adding unsuccessful ");
		}

	}	catch(Exception e)
	{
		e.printStackTrace();
	}
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

	public double getCartItemQuantity(String lineItem){
		double itemQty=0.0;
		int initialLineItems=0, finalLineItems=0;
		List<WebElement> cartItemRows;
		String itemText;
		cartItemRows=webDriver.findElements(By.className(objMap.getLocator("lblcartItemRows")));
		for(int j=0;j<cartItemRows.size();j++){				
			itemText=cartItemRows.get(j).findElement(By.xpath(objMap.getLocator("lnkcartItemName"))).getText();				
			if(itemText.equals(lineItem)){				
				itemQty+=Double.parseDouble(cartItemRows.get(j).findElement(By.className(objMap.getLocator("txtqty"))).getAttribute("value"));					
			}
		}
		return itemQty;
	}

	public boolean cartOperation(String lineItem,String quantity, String operation,String emptyCartAcceptDecline){
		String ops = operation.toUpperCase();
		double initialQty=0.0,finalQty=0.0,qty=0.0,qtyBefore,qtyAfter;
		int initialLineItems=0, finalLineItems=0;
		boolean found=false;
		List<WebElement> cartItems,cartItemRows;
		String itemText;
		switch (ModifyCart.valueOf(ops)) {
		case INCREASE: {
			//Getting initial quantities of that item
			initialQty=getCartItemQuantity(lineItem);

			//Increasing the item quantities			
			outer:		for(int i=0;i<Integer.parseInt(quantity);i++){
				//Reading all cart item rows
				cartItemRows=webDriver.findElements(By.className(objMap.getLocator("lblcartItemRows")));
				for(int j=0;j<cartItemRows.size();j++){
					//Reading item name for one row
					itemText=cartItemRows.get(j).findElement(By.xpath(objMap.getLocator("lnkcartItemName"))).getText();
					if(itemText.equals(lineItem)){						
						if(cartItemRows.get(j).findElements(By.className(objMap.getLocator("btnqtyPlus"))).size() > 0){
							//qtyBefore=cartItemRows.get(j).findElement(By.className("qty")).getAttribute("value");
							qtyBefore=getCartItemQuantity(lineItem);
							cartItemRows.get(j).findElement(By.className(objMap.getLocator("btnqtyPlus"))).click();									
							try{
								Thread.sleep(2000);
								qtyAfter=getCartItemQuantity(lineItem);
								if(Double.compare(qtyBefore, qtyAfter)==0){
									RESULT.warning("Modify cart - Increase Qty", "Quantity should be increased",lineItem+" : quantity is not more increasing.");
									break outer;
								}
							}
							catch(Exception e){
								RESULT.failed("Modify cart - Increase Qty","Exception block caught","Exception caught for product:"+lineItem);
							}
							break;
						}
						else{
							RESULT.warning("Modify cart - Increase Qty",lineItem+": Item name matched. '+' button should be available for : "+itemText,"Quantity failed to incremented as '+' button is not found for item : "+itemText);							
						}
					}
				}
			}
			//Getting final quantities of that item
			finalQty=getCartItemQuantity(lineItem);
			//Compare results based on quantities
			//If given quantity increased in the application then pass
			if(Double.compare((finalQty-initialQty),Double.parseDouble(quantity))==0){
				RESULT.passed("Modify cart - Increase Qty",lineItem+" : Quantity should be incremented by "+quantity,"Quantity increased by "+ (finalQty-initialQty));
				return true;
			}
			//if quantity is increased but only till certain limit because of cart constraint then warn user
			else if(finalQty>initialQty){
				RESULT.warning("Modify cart - Increase Qty",lineItem+" : Quantity should be incremented by"+quantity,"Quantity increased by "+ (finalQty-initialQty));
				return true;
			}
			//If quantity is not increased at all
			else{
				RESULT.failed("Modify cart - Increase Qty",lineItem+" : Quantity should be incremented","Failed to increase quantity. \nInitial qty:"+initialQty+"\nFinal qty:"+finalQty);
				return false;
			}			
		}

		case DECREASE: {

			//Getting initial quantities of that item			
			initialQty=getCartItemQuantity(lineItem);

			//Decreasing the item quantities			
			outer:		for(int i=0;i<Integer.parseInt(quantity);i++){
				//Reading all cart item rows
				cartItemRows=webDriver.findElements(By.className(objMap.getLocator("lblcartItemRows")));
				for(int j=cartItemRows.size()-1;j>=0;j--){
					//Reading item name for one row
					itemText=cartItemRows.get(j).findElement(By.xpath(objMap.getLocator("lnkcartItemName"))).getText();
					if(itemText.equals(lineItem)){						
						if(cartItemRows.get(j).findElements(By.className(objMap.getLocator("btnqtyMinus"))).size() > 0){
							//qtyBefore=cartItemRows.get(j).findElement(By.className("qty")).getAttribute("value");
							qtyBefore=getCartItemQuantity(lineItem);
							cartItemRows.get(j).findElement(By.className(objMap.getLocator("btnqtyMinus"))).click();															
							try{
								Thread.sleep(2000);
								qtyAfter=getCartItemQuantity(lineItem);
								if(Double.compare(qtyBefore, qtyAfter)==0){
									RESULT.warning("Modify cart - Decrease Qty", "Quantity should be decreased",lineItem+" : quantity is not more decreasing.");
									break outer;
								}
							}
							catch(Exception e){
								RESULT.failed("Modify cart - Decrease Qty","Exception block caught","Exception caught : "+lineItem);
							}
							break;
						}
						else{
							RESULT.warning("Modify cart - Decrease Qty",lineItem+": Item name matched. '-' button should be available for : "+itemText,"Quantity failed to incremented as '-' button is not found for item : "+itemText);							
						}
					}
				}				
			}			
			//Getting final quantities of that item
			finalQty=getCartItemQuantity(lineItem);
			//Compare results based on quantities
			//If expected quantity decreased in the application then pass
			if(Double.compare((initialQty-finalQty),Double.parseDouble(quantity))==0){
				RESULT.passed("Modify cart - Decrease Qty",lineItem+" : Quantity should be decreased by "+quantity,"Quantity decreased by "+ (initialQty-finalQty));
				return true;
			}
			//if quantity is decreased but only till certain limit because of cart constraint then warn user
			else if(finalQty<initialQty){
				RESULT.warning("Modify cart - Decrease Qty",lineItem+" : Quantity should be decreased by"+quantity,"Quantity decreased by "+ (initialQty-finalQty));
				return true;
			}
			//If quantity is not increased at all
			else{
				RESULT.failed("Modify cart - Decrease Qty",lineItem+" : Quantity should be decreased","Failed to decrease quantity. \nInitial qty:"+initialQty+"\nFinal qty:"+finalQty);
				return false;
			}			
		}	
		case DELETE: {
			//Getting initial number of line items			
			cartItemRows=webDriver.findElements(By.className(objMap.getLocator("lblcartItemRows")));
			for(int j=0;j<cartItemRows.size();j++){
				//Reading item name for one row
				itemText=cartItemRows.get(j).findElement(By.xpath(objMap.getLocator("lnkcartItemName"))).getText();
				if(itemText.equals(lineItem)){						
					initialLineItems+=1;
				}
			}

			//Deleting a line item from cart
			for(int j=0;j<cartItemRows.size();j++){
				//Reading item name for one row
				itemText=cartItemRows.get(j).findElement(By.xpath(objMap.getLocator("lnkcartItemName"))).getText();
				if(itemText.equals(lineItem)){						
					if(cartItemRows.get(j).findElements(By.partialLinkText("Delete")).size() > 0){
						cartItemRows.get(j).findElement(By.partialLinkText("Delete")).click();
						try{
							Thread.sleep(4000);
						}
						catch(Exception e){
							RESULT.failed("Modify cart - Delete item","Exception block caught","Exception caught for product:"+lineItem);
						}
						break;
					}
					else{
						RESULT.warning("Modify cart - Delete item",lineItem+": Item name matched. 'Delete' link should be available for : "+itemText,"Quantity failed to incremented as 'Delete' link is not found for item : "+itemText);							
					}
				}
			}
			//Getting number of line items after delete of a line item
			cartItemRows=webDriver.findElements(By.className(objMap.getLocator("lblcartItemRows")));
			for(int j=0;j<cartItemRows.size();j++){
				//Reading item name for one row
				itemText=cartItemRows.get(j).findElement(By.xpath(objMap.getLocator("lnkcartItemName"))).getText();
				if(itemText.equals(lineItem)){						
					finalLineItems+=1;
				}
			}
			//Check if one line item is deleted from cart
			if(initialLineItems==finalLineItems+1){
				RESULT.passed("Modify cart - Delete line item",lineItem+" : should be deleted ","Line item : "+lineItem+" is deleted.");
				return true;
			}			
			else{
				RESULT.failed("Modify cart - Delete line item",lineItem+" : should be deleted ","Line item : "+lineItem+" is NOT deleted.");
				return false;
			}

		}

		case UPDATE: {
			//Getting initial quantities of that item
			initialQty=getCartItemQuantity(lineItem);

			//Updating line item quantity			
			cartItemRows=webDriver.findElements(By.className(objMap.getLocator("lblcartItemRows")));
			for(int j=0;j<cartItemRows.size();j++){
				//Reading item name for one row
				itemText=cartItemRows.get(j).findElement(By.xpath(objMap.getLocator("lnkcartItemName"))).getText();
				if(itemText.equals(lineItem)){
					//Checking if textbox is displayed for item quantity
					try{
						if(cartItemRows.get(j).findElements(By.className(objMap.getLocator("txtqty"))).size() > 0){
							//Get quantity before updating
							qty=Double.parseDouble(cartItemRows.get(j).findElement(By.className(objMap.getLocator("txtqty"))).getAttribute("value"));							
							cartItemRows.get(j).findElement(By.className(objMap.getLocator("txtqty"))).click();
							//Pressing control + A keys to select all the digits in quantity field
							String selectAll = Keys.chord(Keys.CONTROL, "a");
							cartItemRows.get(j).findElement(By.className(objMap.getLocator("txtqty"))).sendKeys(selectAll);
							cartItemRows.get(j).findElement(By.className(objMap.getLocator("txtqty"))).sendKeys(Keys.DELETE);
							cartItemRows.get(j).findElement(By.className(objMap.getLocator("txtqty"))).sendKeys(quantity);						
							Thread.sleep(3000);						
							break;
						}
						// Checking if dropdown is displayed for item quantity
						else if ((cartItemRows.get(j).findElements(By.className(objMap.getLocator("drpqty"))).size() > 0)) {
							qty=Double.parseDouble(cartItemRows.get(j).findElement(By.className(objMap.getLocator("drpqty"))).getAttribute("value"));
							Select dropdown = new Select(cartItemRows.get(j).findElement(By.className(objMap.getLocator("drpqty"))));
							dropdown.selectByVisibleText(quantity);
							Thread.sleep(3000);
							//RESULT.passed("Modify cart - Update qty","Item quantity should be updated","Item qty updated in qty dropdown");
							break;
						} else {
							RESULT.failed("Modify cart - Update qty","Item quantity should be updated","Item qty is not updated as quantity textbox/dropdown is not found!!");
							return false;
						}
					}
					catch(Exception e){
						RESULT.failed("Modify cart - Update Qty","Exception block caught","Exception caught for product:"+lineItem);
					}
				}								
			}			
			//Getting final quantities of that item
			finalQty=getCartItemQuantity(lineItem);
			//Compare results based on quantities
			//If given quantity updated in the application then pass
			if(finalQty==initialQty-qty+Double.parseDouble(quantity)){
				RESULT.passed("Modify cart - update Qty",lineItem+" : Quantity should be updated to value :  "+quantity," Quantity updated for item :"+lineItem);
				return true;
			}
			//if quantity is updated but at certain limit because of cart constraint then warn user
			else if(finalQty!=initialQty){
				RESULT.warning("Modify cart - Update Qty",lineItem+" : Quantity should be updated to value: "+quantity,"Quantity updated and adjusted automatically. Total item quantity in cart for item : "+lineItem+" is: "+finalQty);
				return true;
			}
			else {
				RESULT.failed("Modify cart - Update Qty","Item quantity should be updated","Item quantity is not updated");
				return false;
			}			
		}

		case EMPTYCART: {
			// check if user wants to delete all items in cart
			WebElement EmptyAll = uiDriver.getwebDriverLocator(objMap.getLocator("lnkemptyAll"));
			// click on delete all
			EmptyAll.click();

			// popup for confirmation to delete all
			if (emptyCartAcceptDecline.equalsIgnoreCase("true")) {
				uiDriver.popup_ClickOkOnAlert();
				/*
				 * uiDriver
				 * .getwebDriverLocator(objMap.getLocator("btnupdate"
				 * )) .click(); // update cart with changes made
				 */
				System.out.println(uiDriver.getwebDriverLocator(objMap.getLocator("txtSubTotal")).getText());
				if (uiDriver.getwebDriverLocator(objMap.getLocator("txtSubTotal")).getText().contains("$0.00")) {
					RESULT.passed("Empty Cart","All items available in order should get deleted","All items in current order successfylly deleted");
					return true;
				} else {
					RESULT.failed("Empty Cart",	"All items available in order should get deleted","All items in cart could not get deleted");
					return false;
				}
			} 
			else {
				uiDriver.popup_ClickcancelOnAlert();
				if (uiDriver.isElementpresentweb(EmptyAll)) {
					RESULT.passed("Empty Cart",	"All items available in order should not get deleted","All items in current order has not been deleted");
					return true;
				} 
				else {
					RESULT.failed("Empty Cart","All items available in order should not get deleted","All items got deleted");
					return false;
				}
			}
		}		
		default: {
			RESULT.failed("Operation check", "Operation should one of these (INCREASE,DECREASE,DELETE,UPDATE,EMPTYCART)", "Correct operation not found");
			return false;
		}

		}
	}
	public void FD_modifyCart(String itemName, String operation,
			String quantity, String emptyCartAcceptDecline, String flexibilityFlag)
	throws InterruptedException, FindFailed {

		try {
			boolean found = false;
			String lineItem,itemText;
			List<WebElement> cartItemRows,lineItems;
			// Getting the line items complete row
			//Getting only product names list from cart
			lineItems = webDriver.findElements(By.xpath(objMap.getLocator("lnkcartItemNames")));			
			// Iterating through all the list elements
			for (int i=0;i<lineItems.size();i++) {
				if (lineItems.get(i).getText().contains(itemName)) {
					lineItem=lineItems.get(i).getText();
					found=cartOperation(lineItem,quantity,operation,emptyCartAcceptDecline);
					if(found){
						break;
					}
				}
			}
			if (found == false && (flexibilityFlag.equalsIgnoreCase("yes")||flexibilityFlag.isEmpty())) {
				RESULT.warning("Product availability check", "Product text should be availble in cart", "Product text not found. Hence performing operaion on any other product");
				//Getting all the product rows from cart
				cartItemRows=webDriver.findElements(By.className(objMap.getLocator("lblcartItemRows")));
				//Iterating through list for any other product based on flexibility flag settings
				for (int i=0;i<cartItemRows.size();i++) {
					itemText=cartItemRows.get(i).findElement(By.xpath(objMap.getLocator("lnkcartItemName"))).getText();
					found=cartOperation(itemText,quantity,operation,emptyCartAcceptDecline);					
					if(found){
						RESULT.passed("Modify cart","Modify cart performed on another product: "+itemText,"Cart operation performed for: "+itemText);
						break;
					}
				}				
			}
			if(found==false && flexibilityFlag.equalsIgnoreCase("no")){
				RESULT.passed("Modify cart", "Product match required and user should be flexible to perform operation on some other product","Product match not found and flexibility flag is set to 'no'");
			}
		} catch (Exception e) {
			RESULT.failed("Exception encountered", "Exception caught...","Exception details:" + e);
		}
	}

	/**
     * Function Name: 	FD_cancelOrder
     * Purpose: 		To cancel the placed order
     * Created By: 		Avani Thakkar 
     * Created Date: 	27th May,2015 
     * Modified By:
     * Modified Date: 
     * 
     * @param OrderNo
	 *            Provide order number need to cancel from list of placed orders
     * @return void 
       */

	public void FD_cancelOrder(String OrderNo) throws InterruptedException,
	FindFailed {
		try {
			// checking particular order nuumber is present
			if (uiDriver.isElementPresent(By.linkText(OrderNo))) {
				// click on order number
				webDriver.findElement(By.linkText(OrderNo)).click();
				SleepUtils.getInstance().sleep(TimeSlab.YIELD);
				String status = uiDriver.getwebDriverLocator(objMap.getLocator("strsubmitted")).getText();
				System.out.println("status:" + status);
				// check if status is submitted or other than submitted
				if (status.contains("Submitted")) {
					// click on cancel button and confirm cancellation
					uiDriver.getwebDriverLocator(objMap.getLocator("btncancelOrd")).click();
					wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(objMap.getLocator("btncancelConfrm"))));
					uiDriver.click("btncancelConfrm");
					SleepUtils.getInstance().sleep(TimeSlab.MEDIUM);
					if (webDriver.findElements(By.xpath(objMap.getLocator("strcancelOrder"))).size() > 0) {
						String orderStatus = uiDriver.getwebDriverLocator(objMap.getLocator("strcancelOrder")).getText();
                        //check order status after cancellation
						if ((orderStatus.contains("Cancelled"))&& (orderStatus.contains(OrderNo))) {
							RESULT.passed("Cancel order",
									"Order should get cancelled successfully",
									"Desired order cancelled successfully: Order #"+ OrderNo);
						} else {
							RESULT.failed("Cancel order",
									"Order should get cancelled successfully",
									"Desired order could not get cancelled: Order #"+ OrderNo);
							return;
						}

					} else {
						RESULT.failed("Cancel order",
								"Order should get cancelled",
								"You have crossed thresold time to cancel the order so you can not cancel this Order #"+ OrderNo);
						return;
					}
				} else if (status.contains("Cancelled")) {
					RESULT.passed("Cancel order",
							"Order should get cancelled successfully",
							"Desired order already  cancelled: Order #"+ OrderNo);
				} else {
					RESULT.failed("Cancel order",
							"Order should get cancelled successfully",
							"Desired order is in other than submitted Status,so it could not cancelled: Order #"+ OrderNo);
					return;
				}
			} else {
				RESULT.failed("Cancel order",
						"Order should get cancelled successfully",
						"Desired order is not present in your order list: Order #"+ OrderNo);
				return;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
     * Function Name: 	FD_modifyOrder
     * Purpose: 		To Modify the placed order
     * Created By: 		Avani Thakkar 
     * Created Date: 	27th May,2015 
     * Modified By:
     * Modified Date: 
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
			String Quantity, String Operation, String EmptyCartAcceptDecline, String FlexibilityFlag)
	throws InterruptedException, FindFailed {
		try {
			if (uiDriver.isElementPresent(By.linkText(OrderNo))) {
					// click on order number
					webDriver.findElement(By.linkText(OrderNo)).click();
					// check if modify order button is visible
					//uiDriver.isDisplayed("btnmodOrd");
					SleepUtils.getInstance().sleep(TimeSlab.YIELD);
					String orderStatus = uiDriver.getwebDriverLocator(objMap.getLocator("strsubmitted")).getText();
					System.out.println("status:" + orderStatus);

				// check if status is submitted or other than submitted
				if (orderStatus.contains("Submitted")) {

					// modify the placed order
					String Operate = Operation.toUpperCase();
					uiDriver.getwebDriverLocator(objMap.getLocator("btnmodifyOrd")).click();
					wait.until(ExpectedConditions.visibilityOfElementLocated(By.className(objMap.getLocator("txtyourCart"))));
					// click on modify order button wait for page to load
				//	while (!uiDriver.isDisplayed("txtyourCart")) {
						// while
						// (!webDriver.findElement(By.className("cartheader__title")).getText().contains("Your Cart"))
						// {
					//	selenium.waitForPageToLoad(PageLoadTime);
					//}

					// check if user want to update cart with modification or
					// cancel updates
					if (Operate.contains("INCREASE")
							|| Operate.contains("DECREASE")
							|| Operate.contains("DELETE")
							|| Operate.contains("UPDATE")
							|| Operate.contains("EMPTYCART")) {

						// Item names and values for Operation would be comma
						// separated so split those values and call modify_cart
						// function
						String Items[] = ItemName.split(",");
						String Qty[] = Quantity.split(",");
						String oper[] = Operation.split(",");
						for (int i = 0; i < oper.length; i++) {
							uiDriver.FD_modifyCart(Items[i], oper[i], Qty[i],
									EmptyCartAcceptDecline, FlexibilityFlag);
						}
					}

					if (Operate.contains("CANCEL")) {
						if (uiDriver.isDisplayed("btncancelChanges")) {
							uiDriver.getwebDriverLocator(
									objMap.getLocator("btncancelChanges"))
									.click();
							RESULT
							.passed(
									"Cancel modification",
									"Cart updation should get cancelled successfully.No modifications should be applied",
							"Cart updation cancelled successfully. No modifications applied");
						} else {
							RESULT
							.failed(
									"Cancel modification",
									"Cancel changes button should be displayed",
							"Cancel changes button is not displayed");
						}
					} else if (!Operate.contains("INCREASE")
							&& !Operate.contains("DECREASE")
							&& !Operate.contains("DELETE")
							&& !Operate.contains("UPDATE")
							&& !Operate.contains("EMPTYCART")
							&& !Operate.contains("CANCEL")) {
						RESULT
						.failed(
								"Invalid argument for 'Operation'",
								"Given argument should be INCREASE, DECREASE, DELETE, UPDATE, CANCEL or EMPTYCART",
						"Given argument is other than Operation Arguments. Give correct argument");
					}
				} else {
					RESULT.failed("Modify Order",
							"Order should modified as per specified by user",
							"Order can not be modified in status: " + orderStatus
							+ " which is other than submitted");
				}
			} else {
				RESULT.failed("Your orders page",
						"Page should be navigated to Your orders page",
				"Unsuccessful page navigation to Your orders page");
			}
		} catch (Exception e) {
			RESULT.failed("Error encountered", "order should be modified",
					"Error occured" + e);
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
	*Function Name: FD_Deliverypass_Add_Verify
     * Purpose: Function to Add,Verify and Renew Deliverypass for customer
     * Created By: Tejas Patel 
      * Created Date: 
      * Modified By:
     * Modified Date:
     * @param Process
	 *            :Define process(i.e Add/Verify/Renew) for Deliverypass
	        
     **/
	public void FD_Deliverypass_Add_Verify(String Process)
	throws InterruptedException {
		String process1 = Process.toUpperCase();
		switch (AddVerify.valueOf(process1)) {
		case ADD: {
			int count;
			String DeliverypassDetail = uiDriver.getwebDriverLocator(objMap.getLocator("strdeliverypassDetail")).getText();
			System.out.println(DeliverypassDetail);
			if (DeliverypassDetail.contains("Six-Month DeliveryPass")) {
				System.out.println("Sorry! you have already Delivery Pass");
				RESULT.failed("Add Deliverypass",
						"User should be able to add Deliverypass",
				"User unable to add Deliverypass");
			}

			else {
				// signUP for DeliveryPass
				uiDriver.click("lnkdeliverypass_signup");
				selenium.waitForPageToLoad(PageLoadTime);
				if (webDriver.findElements(By.className(objMap.getLocator("strcart_info"))).size() > 0) {
					String cart_info = uiDriver.getwebDriverLocator(objMap.getLocator("strcart_info")).getText();
					System.out.println(cart_info);
					if (cart_info.contains("1 in cart")) {
						RESULT.passed("Add Deliverypass",
								"User should be able to add Deliverypass",
						" Deliverypass addedd already ");

					}

					else {
						// adding DeliveryPass To cart
						uiDriver.click("btnadd_cart");
						RESULT.passed("Add Deliverypass",
								"User should be able to add Deliverypass",
						" Deliverypass addedd Sucessfully ");
						selenium.waitForPageToLoad(PageLoadTime);
						count = 0;
						do {
							if (webDriver.findElements(By.xpath(objMap.getLocator("popaddToCart11"))).size() > 0
									&& webDriver.findElement(By.xpath(objMap.getLocator("popaddToCart11"))).isDisplayed() ) {
								uiDriver.getwebDriverLocator(objMap.getLocator("btnClose")).click();
								break;
							}
							count++;
						} while (count < 5);
					}
				}

			}

		}
		break;
		case VERIFY: {

			uiDriver.getwebDriverLocator(objMap.getLocator("strdeliverypassDetail"));
			String Verification = uiDriver.getwebDriverLocator(objMap.getLocator("strdeliverypassDetail")).getText();
			System.out.println(Verification);
			if (Verification.contains("Six-Month DeliveryPass")) {
				System.out.println("Verification Done");
				RESULT.passed("Verify Deliverypass",
						"User should be able to verify membership of Deliverypass",
				"Verification Done");
			} else {
				RESULT.failed("Verify Deliverypass",
						"User should be able to verify membership of Deliverypass",
				"User unable to verify membership of Deliverypass");
			} 

		}
		break;
		case RENEW: {
			try {
				webDriver.findElement(By.linkText(objMap.getLocator("lnkrenewaloff")));

				uiDriver.click("lnkrenewaloff");
				selenium.waitForPageToLoad(PageLoadTime);
				if (uiDriver.isDisplayed("btnrenewalon")) {
					uiDriver.click("btnrenewalon");
					selenium.waitForPageToLoad(PageLoadTime);
					if (webDriver.findElements(By.linkText(objMap.getLocator("lnkrenewaloff"))).size()>0)
					{
						RESULT.passed("Verify renewal off/on link of Deliverypass",
								"User should be able to verify renewal off/on link of deliverypass",
						"Verification Done");
					}
					else{
						RESULT.failed("Verify renewal off/on link of Deliverypass",
								"User should be able to verify renewal off/on link of deliverypass",
						"Verification Fail");
					}
				} else {
					RESULT.failed("Wait for pageToLoad",
							"Page should be Loaded",
					"Maximum time out reached for pageToload");
				}
			} catch (NoSuchElementException e1) {
				uiDriver.click("btnrenewalon");
				selenium.waitForPageToLoad(PageLoadTime);
				if (uiDriver.isDisplayed("lnkrenewaloff")) {
					uiDriver.click("lnkrenewaloff");
					selenium.waitForPageToLoad(PageLoadTime);
					if (uiDriver.isDisplayed("btnrenewalon"))
					{
						RESULT.passed("Verify renewal off/on link of Deliverypass",
								"User should be able to verify renewal off/on link of deliverypass",
						"Verification Done");
					}else{
						RESULT.failed("Verify renewal off/on link of Deliverypass",
								"User should be able to verify renewal off/on link of deliverypass",
						"Verification Fail");
					}
				} else {
					RESULT.failed("Wait for pageToLoad",
							"Page should be Loaded",
					"Maximum time out reached for pageToload");
				}
			}
		}
		break;
		}

	}

	// Enum options for the searchOptions
	private enum searchOptions {
		CLICK, HOVER, QUICKSEARCH
	};

	/**
	*Function Name: FD_searchFunction
     * Purpose: Function to search a product in Storefront
     * Created By: Bhavik Tikudiya 
      * Created Date: 
      * Modified By:
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
	 *            : category refers to the search item i.e Apples
	 * return void
	 *
	 **/
	public void FD_searchFunction(String method, String dept, String sub_dept,
			String category) {
				
		// Global variable which can be used in Fd_addToCart if specified item
		// is not available
		prdct_category = category;
		prdct_department=dept;
		String breadcrumbs_text=null;
		switch (searchOptions.valueOf(method.toUpperCase())) {
		case QUICKSEARCH:// Quick Search
			if (webDriver.findElements(
					By.xpath(objMap.getLocator("tabyourTopItems"))).size() > 0) {
				// enter product to search
				uiDriver.setValue("txtreorderSearch", category);
				// click on the go
				actions.sendKeys(Keys.ENTER).build().perform();
				SleepUtils.getInstance().sleep(TimeSlab.HIGH);
				wait.until(ExpectedConditions
						.visibilityOfElementLocated(By.xpath(objMap
								.getLocator("btnreorderAtoZ"))));
				// result based on the list appeared or not
				if (webDriver.findElements(
						By.xpath(objMap.getLocator("stremptyReorderSearchResult"))).size() > 0) {

					RESULT.warning("Searching product using search field",
							"Product search should be successfully",
							"Product search : Showing 0 product found for "
							+ category);
				} else {
					if (webDriver.findElements(
							By.xpath(objMap.getLocator("lnkfirstItemReorder"))).size() > 0) {

						RESULT.passed("Searching product using search field",
								"Product search should be done successfully",
						"Product search is successful");
					} else {
						RESULT.log("Searching product using search field",
								ResultType.FAILED,
								"Product search should be done successfully",
								"Product search failed", true);

					}
				}
			} else {
				// enter product to search
				try
				{
					uiDriver.setValue("txtsearchField", category);
					// click on the go
					if (((String) GCONFIG.get("Browser")).equalsIgnoreCase("IE")||((String) GCONFIG.get("Browser")).equalsIgnoreCase("CHROME")){
						uiDriver.getwebDriverLocator(objMap.getLocator("txtsearchField")).sendKeys(Keys.ENTER);
					}else{
						uiDriver.click("btnsearchGo");
					}
					
				}
				catch(Exception e )
				{
					e.printStackTrace();
				}
				
				wait.until(ExpectedConditions
						.visibilityOfElementLocated(By.xpath(objMap
								.getLocator("strverifyNoSearchResult"))));
				// result based on the title of the page and search result
				if (uiDriver.getwebDriverLocator(objMap.getLocator("strverifyNoSearchResult")).getText().contains("(0)")) {
					RESULT.failed("Searching product using search field",
							"Product search should be successfully",
							"Product search failed : Showing 0 product found for "+ category);
				} else {
					if (webDriver.getTitle().toLowerCase().contains(category.toLowerCase())) {
						System.out.println(RESULT);
						RESULT.passed("Searching product using search field",
								"Product search should be successfully",
						"Product search is successfully");
					} else {
						RESULT.failed("Searching product using search field",
								"Product search should be successfully",
						"Product search failed");
					}
				}
			}
			break;

		case CLICK:// click method
			try {
				String depth = "2";
				// click on the Fruit tab directly

				webDriver.findElement(By.xpath(objMap.getLocator(dept))).click();
				selenium.waitForPageToLoad(PageLoadTime);
				robot.moveToElement(webDriver.findElement(By.name(objMap.getLocator("imgfd_Logo"))));
				// click on sub-departement if sub-departement is available
				if (!sub_dept.endsWith(dept)&& sub_dept.replace(" ", "").length() > 0) {
					// For the sub-departement correct the sub-departement if we
					// have item with space whichis without space in excel data
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
					String sub_dept_xpath = "//span[text() = '" + sub_dept+ "']";
					if (((String) GCONFIG.get("Browser")).equalsIgnoreCase("SAFARI"))
						webDriver.findElement(By.xpath(sub_dept_xpath)).click();
					else
						actions.moveToElement(webDriver.findElement(By.xpath(sub_dept_xpath))).click().build()
								.perform();
					//builder.moveToElement(webDriver.findElement(By.xpath(sub_dept_xpath))).click().build().perform();
					depth = "3";
					selenium.waitForPageToLoad(PageLoadTime);
				}
				if (category.replace(" ", "").length() > 0) {
					// click on category from side
					// wait for the page to load with element visible condition
					String category_xapth;
					if (category.equals("Cheese")) {
						category_xapth = "//li[@data-component='menuitem']/label/span/span[text()='"+ category.split("&")[0] + "']";
					} else {
						category_xapth = "//span[contains(text(),'"+ category.split("&")[0] + "')]";
					}
					wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(category_xapth)));
					// click on category
					webDriver.findElement(By.xpath(category_xapth)).click();
					String verify_locator = objMap.getLocator("strverifyCategoryClick").replace("3", depth);
					wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(verify_locator)));
				}
				breadcrumbs_text=null;
				SleepUtils.getInstance().sleep(TimeSlab.YIELD);
				do {
					breadcrumbs_text = webDriver.findElement(By.xpath(objMap.getLocator("strverifySearchResults"))).getText();
					SleepUtils.getInstance().sleep(TimeSlab.YIELD);					
				} while (breadcrumbs_text == null || breadcrumbs_text.replace(" ", "").equals(""));

				// result based on the title of the page
				// if (webDriver.getTitle().toLowerCase().contains(
				// category.toLowerCase().split("&")[0])
				// || webDriver.getTitle().toLowerCase().contains(
				// dept.toLowerCase().split("&")[0])) {
				if (breadcrumbs_text.toLowerCase().contains(category.toLowerCase())) {
					RESULT.passed("Searching product using click",
							"Product search should be successfully",
					"Product search is successfully");
				} else {
					RESULT.failed("Searching product using click",
							"Product search should be successfully : "+ category + " departement -" + dept,
							"Product search failed : " + webDriver.getTitle());
				}
				break;
			} catch (Exception e) {
				e.printStackTrace();
			}

		case HOVER:// hover method
			try {
				String category_hover_xapth;
				// Action to mouse over the header tab to make menu appear
				// exception
				// for flowers
				if (dept.equals("Flowers")) {
					webDriver.findElement(By.xpath(objMap.getLocator(dept))).click();
					selenium.waitForPageToLoad(PageLoadTime);
				} else {
					//builder.moveToElement(webDriver.findElement(By.xpath(objMap.getLocator(dept)))).build().perform();
					robot.moveToElement(webDriver.findElement(By.xpath(objMap.getLocator(dept))));
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
					if(dept.contains("Deli") || dept.contains("Kitchen") || dept.contains("Grocery"))
					{					
					for(int i=1; i<5; i++)
					{						
						if(dept.contains("Deli"))
						{
							sub_dept_xpath = "//li[@data-id='deliandcheese']/li/div/ul/li[@class='submenuitem']["+i+"]/span/a";	
						}
						else if(dept.equals("Kitchen"))
						{
							sub_dept_xpath = "//li[@data-id='kitchen']/li/div/ul/li[@class='submenuitem']["+i+"]/span/a";	
						}
						else if(dept.equals("Grocery"))
						{
							sub_dept_xpath = "//li[@data-id='supergro']/li/div/ul/li[@class='submenuitem']["+i+"]/span/a";	
						}
						else
							break;
						if(webDriver.findElement(By.xpath(sub_dept_xpath)).getText().equalsIgnoreCase(sub_dept))
						{
							robot.moveToElement(webDriver.findElement(By.xpath(sub_dept_xpath)));
							break;
						}else
							System.out.println("going for next category");
					}
					}
					else
					{
					sub_dept_xpath = "//*[text() = '" + sub_dept + "']";
					wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(sub_dept_xpath)));
					robot.moveToElement(webDriver.findElement(By.xpath(sub_dept_xpath)));
					}
					category_hover_xapth += "li/div/ul/li/" +objMap.getLocator(temp_sub_dept) + "div/div[2]/";
				}else{
					category_hover_xapth += "li/div/div[2]/";
				}
				if (category.replace(" ", "").length() > 0) {
					// wait for the page to load with element visible condition

					if (category.equals("Cheese")) {
						category_hover_xapth += "li/a[text()='" + category+ "']";
					} 
					else if(category.equals("Local")){
						category_hover_xapth += "li/a[text()='" + category+ "']";
					}else {
						category_hover_xapth += "div[@class='dropdown-column']/ul/li/a[contains(text(),'"+ category + "')]";
					}
					WebElement category_click;
					System.out.println(webDriver.findElements(By.xpath(category_hover_xapth)).size());
					if (category.equals("Cheese")) {
						category_click = webDriver.findElements(By.xpath(category_hover_xapth)).get(0);
					} else {
						category_click = webDriver.findElement(By.xpath(category_hover_xapth));
					}
					wait.until(ExpectedConditions.visibilityOf(category_click));
					// click on Apples from menu just visible after action
					category_click.click();					
				}
				wait.until(ExpectedConditions
						.visibilityOfElementLocated(By.xpath(objMap.getLocator("strverifySearchResults"))));
				breadcrumbs_text=null;	
				SleepUtils.getInstance().sleep(TimeSlab.YIELD);
				do {
					breadcrumbs_text = webDriver.findElement(By.xpath(objMap.getLocator("strverifySearchResults"))).getText();
					SleepUtils.getInstance().sleep(TimeSlab.YIELD);					
				} while (breadcrumbs_text==null || breadcrumbs_text.replace(" ", "").equals(""));

				// result based on the title of the page
				// if (webDriver.getTitle().toLowerCase().contains(
				// category.toLowerCase().split("&")[0])
				// || webDriver.getTitle().toLowerCase().contains(
				// dept.toLowerCase().split("&")[0])) {
				if (breadcrumbs_text.toLowerCase().contains(category.toLowerCase())) {
					RESULT.passed("Searching product using hover",
							"Product search should be successfully",
					"Product search is successfully");
				} else {
					RESULT.failed("Searching product using hover",
							"Product search should be successfully : "+ category + " departement -" + dept,
							"Product search failed : " + webDriver.getTitle());
				}
				break;
			} catch (Exception e) {
				e.printStackTrace();
			}
		default:// function does not received any method
			RESULT.failed("Searching product",
					"Product search should be successfully : " + category+ " departement -" + dept,
			"Product search failed");
			break;
		}
	}

	public void FD_addList(WebElement product, String listflag_new_exist,
			String listname,
			String FlexibilityFlag) throws FindFailed, InterruptedException {

		String ddl_listname[];
//		WebDriverWait wait = new WebDriverWait(webDriver, 10);
		String success_msg;
		String error_msg;
		int i;

		try {
			Actions ac_key = new Actions(webDriver);
			System.out.println("Before clicking on add to list");
			try
			{
				JavascriptExecutor jse = (JavascriptExecutor) webDriver;
				jse.executeScript("arguments[0].scrollIntoView()", product);
				
				if (((String) GCONFIG.get("Browser")).equalsIgnoreCase("FIREFOX") || ((String) GCONFIG.get("Browser")).equalsIgnoreCase("CHROME")
						||((String) GCONFIG.get("Browser")).equalsIgnoreCase("IE")) 
				{
					jse.executeScript("arguments[0].click()", product);
				}
				else if(((String) GCONFIG.get("Browser")).equalsIgnoreCase("SAFARI"))
				{
					product.click();
				}
			}
			catch(Exception e)
			{
				if(FlexibilityFlag.equalsIgnoreCase("Yes"))
				{
					try{
						product=uiDriver.getwebDriverLocator(objMap.getLocator("lnkfirstItemReorder"));
						JavascriptExecutor jse = (JavascriptExecutor) webDriver;
						jse.executeScript("arguments[0].scrollIntoView()", product);
						
						if (((String) GCONFIG.get("Browser")).equalsIgnoreCase("FIREFOX") || ((String) GCONFIG.get("Browser")).equalsIgnoreCase("CHROME")
								||((String) GCONFIG.get("Browser")).equalsIgnoreCase("IE")) 
						{
							jse.executeScript("arguments[0].click()", product);
						}
						else if(((String) GCONFIG.get("Browser")).equalsIgnoreCase("SAFARI"))
						{
							product.click();
						}
					}catch (Exception e1) {
//						e.printStackTrace();
						product=uiDriver.getwebDriverLocator(objMap.getLocator("lnkfirstItem"));
						JavascriptExecutor jse = (JavascriptExecutor) webDriver;
						jse.executeScript("arguments[0].scrollIntoView()", product);
						
						if (((String) GCONFIG.get("Browser")).equalsIgnoreCase("FIREFOX") || ((String) GCONFIG.get("Browser")).equalsIgnoreCase("CHROME")
								||((String) GCONFIG.get("Browser")).equalsIgnoreCase("IE")) 
						{
							jse.executeScript("arguments[0].click()", product);
						}
						else if(((String) GCONFIG.get("Browser")).equalsIgnoreCase("SAFARI"))
						{
							product.click();
						}
					}
					
				}
				else
				{	RESULT.failed("Save to list operation failed", "Item should not be displayed", "Item is not displayed");
				return ;
				}

			}
//			uiDriver.selenium.waitForPageToLoad(PageLoadTime);
			if (webDriver.findElements(By.xpath("diaavailability")).size() > 0) {
				RESULT.warning("Unavailable product", "Product is unavailable",
						"Product name is unavailable :"+uiDriver.getwebDriverLocator(objMap
								.getLocator("strprodUnavailability")).getText());
				return;

			} else {
				SleepUtils.getInstance().sleep(TimeSlab.YIELD);	
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(objMap.getLocator("btnsaveList"))));
				uiDriver.getwebDriverLocator(
						objMap.getLocator("btnsaveList")).click();
			}
			/* Create new list */
			if (listflag_new_exist.equals("New")) {
				SleepUtils.getInstance().sleep(TimeSlab.YIELD);
				// System.out.println("Test Data well");
				List<WebElement> ddl = uiDriver.getwebDriverLocator(objMap.getLocator("drplist")).findElements(
								By.tagName("option"));
				uiDriver.DrawHighlight("drplist", webDriver);
				// webDriver.findElement(By.xpath("//select[@id='addtolist-selectList']/option[1]"));

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
					if (ddl_listname[i].equalsIgnoreCase(listname)) 
					{
						System.out.println("List already exist");
						uiDriver.setValue("txtnewList", listname);
						ac_key.sendKeys(Keys.TAB).build().perform();
						ac_key.sendKeys(Keys.ENTER).build().perform();
						// wait.until(ExpectedConditions.visibilityOf(webDriver.findElement(By.className("error"))));
						wait.until(ExpectedConditions
								.visibilityOf(uiDriver
										.getwebDriverLocator(objMap
												.getLocator("strlistNameDuplicateError"))));
						error_msg = uiDriver.getwebDriverLocator(objMap.getLocator("strlistNameDuplicateError"))
						.getText();
						if (error_msg.contains("Oops! That name is taken!")) {
							uiDriver.click("btncloseList");
							RESULT.passed("Duplicate List name","Error should be displayed", error_msg);
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
				ac_key.sendKeys(Keys.TAB).build().perform();
				ac_key.sendKeys(Keys.ENTER).build().perform();
				SleepUtils.getInstance().sleep(TimeSlab.YIELD);
				if (uiDriver.getwebDriverLocator(
						objMap.getLocator("btnlistAddOk")).isEnabled()) 
				{
					RESULT.passed("New list creation ",
							"New list should be created successfully",
							"New list "+listname+ " created successfully");
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
				try
				{
					list_ddl.selectByVisibleText(listname);
				}
				catch(Exception e)
				{
					if(FlexibilityFlag.equalsIgnoreCase("Yes"))
					{
						list_ddl.selectByIndex(0);
					}
					else
					{
						RESULT.warning("Item addition to existing list","Item should be added to the list "+listname,"Item could not be added to list as list :"+listname+ " was unavailable");
						return;
					}
				}
				//ac_key.sendKeys(Keys.ENTER).build().perform();
				//ac_key.sendKeys(Keys.TAB).build().perform();
				//ac_key.sendKeys(Keys.ENTER).build().perform();
				uiDriver.click("btnaddAddToList");
				SleepUtils.getInstance().sleep(TimeSlab.YIELD);

				if (uiDriver.isDisplayed("btnlistAddOk")) {
					RESULT.passed("Item addition to existing list",
							"Item should be added to the list :"+listname,
							"Item is added to the list :"+listname);
				}
				else
				{
					RESULT.failed("Item addition to existing list",
							"Item should be added to the list :"+listname,
							"Item is not added to the list :"+listname);
					return;
				}
				uiDriver.getwebDriverLocator(objMap.getLocator("btnlistAddOk"))
				.click();

			} else {
				return ;
			}

		} catch (NoSuchElementException e) {
			e.printStackTrace();


		} catch (Exception e) {
			e.printStackTrace();


		}
	}


	

	public void FD_loginEmailNotification(String UserID, String Password)
	throws InterruptedException, FindFailed {
		uiDriver.setValue("txtuserNameEmail", UserID);
		uiDriver.setValue("txtpasswordEmail", Password);
		SleepUtils.getInstance().sleep(TimeSlab.YIELD);
		uiDriver.click("btnloginEmail");
		selenium.waitForPageToLoad(PageLoadTime);
	}

	
	public void popup_ClickcancelOnAlert() {
		if (uiDriver.popup_isAlertPresent(5)) {
			Alert alt = webDriver.switchTo().alert();
			alt.dismiss();
		}
	}

	

	
	
	/**
	*Function Name: FD_SignUP
     * Purpose: Function to create New user in Freshdirect Application
     * Created By: Tejas Patel 
      * Created Date: 
      * Modified By:
     * Modified Date:
     * @param Firstname:
	 *        First name of user     
     * @param Lastname:
	 *        Last name of user  
	 * @param ZipCode:
	 *        Zipcode of user
	 * @param ServiceType:
	 *        ServiceType of user
	 * @param Email:
	 *        Email of user
	 * @param ConfirmEmail:
	 *        Email of user
	 * @param PassWord1:
	 *        Password of user 
	 * @param ConfirmPassWord1:
	 *        Password of user
	 * @param SecurityQuestion:
	 *        Sequrity question for user      
     * @param CompanyName:
	 *        companyname of user
	 * @param CompanyContactNum:
	 *        phone number of user
	 * @param CompanyFloor:
	 *        Apartment name of user
	 * @param CompanyAddress:
	 *        Address of company
	 * @param CompanyCity:
	 *        City of company
	 * @param CompanyState:
	 *         State of company
	 * @param CompanyZipcode:
	 *         Zipcode of company
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
			SleepUtils.getInstance().sleep(TimeSlab.MEDIUM);
			webDriver.switchTo().frame(uiDriver.getwebDriverLocator(objMap.getLocator("frameiframeSignup")));
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
			uiDriver.setValue("txtemail", Email);
			uiDriver.setValue("txtconfirmEmail", ConfirmEmail);
			uiDriver.setValue("txtpassWord1", PassWord1);
			uiDriver.setValue("txtconfirmPassWord1", ConfirmPassWord1);
			uiDriver.setValue("txtsecurityQuestion", SecurityQuestion);
			uiDriver.click("btnSIGNup");
			SleepUtils.getInstance().sleep(TimeSlab.MEDIUM);
				if (webDriver.findElements(By.id(objMap.getLocator("strerrorMsgSignup"))).size() > 0) {
				System.out.println(" Please Enter valid data ");
				List<WebElement> Errors = webDriver.findElements(By.className(objMap.getLocator("strerrorMsgSignup")));
				for (WebElement error1 : Errors) {
					String error = error1.getText();
					System.out.println(error);
					RESULT.failed("SignUP in to FreshDirect",
							"User should be able to SignUP successfully",
							error);
				}
			}
			else {
				if (webDriver.findElements(By.xpath(objMap.getLocator("btnchkAddress"))).size() > 0) {
					uiDriver.setValue("txtcompanyName", CompanyName);
					uiDriver.setValue("txtcompanyPhone", CompanyContactNum);
					uiDriver.setValue("txtcompanyFloor", CompanyFloor);
					uiDriver.setValue("txtcompanyStreetAddress", CompanyAddress);
					uiDriver.setValue("txtcompanyCity", CompanyCity);
					uiDriver.setValue("txtcompanyState", CompanyState);
					uiDriver.setValue("txtcompanyZipCode", CompanyZipcode);
					uiDriver.click("btnchkAddress");
					if (webDriver.findElements(By.className(objMap.getLocator("strcompanyErrorMsg"))).size() > 0) {
						System.out.println(" Please Enter valid data ");
						List<WebElement> Errors = webDriver.findElements(By.className(objMap.getLocator("strcompanyErrorMsg")));
						for (WebElement error2 : Errors) {
							String error = error2.getText();
							System.out.println(error);
							RESULT.failed("SignUP in to FreshDirect",
									"User should be able to SignUP successfully",
									error);
							return;
						}
					} else if (webDriver.findElements(By.id(objMap.getLocator("strnoOfficeDelivery"))).size() > 0) {
						RESULT.failed("SignUP in to FreshDirect",
								"User should be able to SignUP successfully",
						"Please enter valid Corporate Address");
						return;
					}

					else {
						RESULT.passed("SignUP in to FreshDirect",
								"User should be able to SignUP successfully",
						"User has signUP successfully");
					}
				} else {
					RESULT.passed("SignUP in to FreshDirect",
							"User should be able to SignUP successfully",
					"User has signUP successfully");
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
	*Function Name: FD_filters
     * Purpose: Function to Filter products with different filter value
     * Created By: Tejas Patel 
      * Created Date: 
      * Modified By:
     * Modified Date:
     * @param filtername:
	 *        Define filte name for filter the products  
	 **/
	public void FD_filters(String filtername) throws InterruptedException,
	FindFailed {
		try {
			Actions builder = new Actions(webDriver);
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
			int c;
			String atoz;
			String rowlist = null;

			WebDriverWait wait = new WebDriverWait(webDriver, 30);

			if ((webDriver.findElements(By.xpath(objMap.getLocator("tabyourTopItems"))).size() > 0)) 
			{
				atoz=objMap.getLocator("btnreorderAtoZ");
				rowlist=objMap.getLocator("lstreorderRowlist");

			}
			else
			{
				atoz=objMap.getLocator("btnAtoZ");
				rowlist=objMap.getLocator("lstcoupon_rowlist");
			}
			String filter_type = filtername.toUpperCase();
			switch (Sort_type.valueOf(filter_type)) {
			case ATOZ: {

				Direction = webDriver.findElement(By.xpath(atoz)).getAttribute("data-direction");
				if(!(webDriver.findElements(By.xpath(objMap.getLocator("tabyourTopItems"))).size() > 0))
				{
					System.out.println("Direction " + Direction);
					if (Direction.equals("true")) {
						uiDriver.getwebDriverLocator(atoz).sendKeys("\n");
						SleepUtils.getInstance().sleep(TimeSlab.MEDIUM);
						wait.until(ExpectedConditions.stalenessOf(webDriver.findElement(By.xpath(atoz))));
						Direction = webDriver.findElement(By.xpath(atoz)).getAttribute("data-direction");
						System.out.println("Direction " + Direction);
					}
				}else{
					if (((String) GCONFIG.get("Browser")).equalsIgnoreCase("SAFARI"))
						uiDriver.getwebDriverLocator(atoz).click();
					else
						uiDriver.getwebDriverLocator(atoz).sendKeys("\n");
					SleepUtils.getInstance().sleep(TimeSlab.MEDIUM);
				}
				if (Direction.equals("false")||webDriver.findElements(By.xpath(objMap.getLocator("tabyourTopItems"))).size() > 0) {
					if (webDriver.findElements(By.xpath(objMap.getLocator("btnshowAll"))).size() > 0) {
						// click on the show all button
						uiDriver.getwebDriverLocator(objMap.getLocator("btnshowAll")).click();
						// wait for the products to load
						wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//button[@data-component='pager-prev']")));
					}

					List<WebElement> row_list = webDriver.findElements(By.xpath(rowlist));
					wait.until(ExpectedConditions.visibilityOfAllElements(row_list));

					System.out.println("Number of rows " + row_list.size()); 

					for (int i = 0; i < row_list.size(); i++) {
						List<WebElement> col_list = row_list.get(i).findElements(By.tagName("li"));
						System.out.println("Number of columns in row is "+ col_list.size());

						for (int j = 0; j < col_list.size(); j++) {
							if (i > 0 && j == 0) {
								item_name1 = col_list.get(j).findElement(By.tagName("a")).getText();
								c = (last_item).compareTo(item_name1);
								if (c > 0) {
									System.out.println("items are not in A-Z order ");
									RESULT.failed("Filter products in A-Z order",
											"Products  should dispaly in A-Z order ",
									"Products are not dispaly in A-Z order");
									check_flag = true;
									break;
								}
							}
							if (j == col_list.size() - 1) {
								last_item = col_list.get(j).findElement(By.tagName("a")).getText();
								System.out.println("last item " + last_item);
							} else {
								item_name1 = col_list.get(j).findElement(By.tagName("a")).getText();
								item_name2 = col_list.get(j + 1).findElement(By.tagName("a")).getText();

								System.out.println("Item name1  "+ item_name1);
								System.out.println("Item name2  "+ item_name2);
								c = (item_name1).compareTo(item_name2);
								if (c > 0) {
									System.out.println("items are not in A-Z order ");
									RESULT.failed("Filter products in A-Z order",
											"Products  should dispaly in A-Z order ",
									"Products are not dispaly in A-Z order");
									check_flag = true;
									break;

								}
							}

						}
						if (check_flag == true) {
							break;
						}

					}
					if (check_flag == false) {
						System.out.println("Products over ");
						RESULT.passed("Filter products in A-Z order",
								"Products  should dispaly in A-Z order ",
						"Products are  dispaly in A-Z order");
					}
				}
			}
			break;

			case ZTOA: {
				uiDriver.click("btnAtoZ");
				wait.until(ExpectedConditions.stalenessOf(webDriver.findElement(By.xpath(objMap.getLocator("btnAtoZ")))));
				Direction = webDriver.findElement(By.xpath(objMap.getLocator("btnAtoZ"))).getAttribute("data-direction");
				System.out.println("Direction " + Direction);
				if (Direction.equals("false")) {
					uiDriver.click("btnAtoZ");
					wait.until(ExpectedConditions.stalenessOf(webDriver.findElement(By.xpath(objMap.getLocator("btnAtoZ")))));
					Direction = webDriver.findElement(By.xpath(objMap.getLocator("btnAtoZ"))).getAttribute("data-direction");
					System.out.println("Direction " + Direction);
				}

				if (Direction.equals("true")) {
					if (webDriver.findElements(By.xpath(objMap.getLocator("btnshowAll"))).size() > 0) {
						// click on the show all button
						uiDriver.getwebDriverLocator(objMap.getLocator("btnshowAll")).click();
						// wait for the products to load
						wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//button[@data-component='pager-prev']")));
					}

					List<WebElement> row_list = webDriver.findElements(By.xpath(objMap.getLocator("lstcoupon_rowlist")));
					wait.until(ExpectedConditions.visibilityOfAllElements(row_list));

					System.out.println("Number of rows " + row_list.size());

					for (int i = 0; i < row_list.size(); i++) {
						List<WebElement> col_list = row_list.get(i).findElements(By.tagName("li"));
						System.out.println("Number of columns in row is "+ col_list.size());

						for (int j = 0; j < col_list.size(); j++) {
							if (i > 0 && j == 0) {
								item_name1 = col_list.get(j).findElement(By.tagName("a")).getText();
								c = (last_item).compareTo(item_name1);
								if (c < 0) {
									System.out.println("items are not in Z-A order ");
									RESULT.failed("Filter products in Z-A order",
											"Products  should dispaly in Z-A order ",
									"Products are not dispaly in Z-A order");
									check_flag = true;
									break;
								}
							}
							if (j == col_list.size() - 1) {
								last_item = col_list.get(j).findElement(By.tagName("a")).getText();
								System.out.println("last item " + last_item);
							} else {
								item_name1 = col_list.get(j).findElement(By.tagName("a")).getText();
								item_name2 = col_list.get(j + 1).findElement(By.tagName("a")).getText();

								System.out.println("Item name1  "+ item_name1);
								System.out.println("Item name2  "+ item_name2);
								c = (item_name1).compareTo(item_name2);
								if (c < 0) {
									System.out.println("items are not in Z-A order");
									RESULT.failed("Filter products in Z-A order",
											"Products  should dispaly in Z-A order ",
									"Products are not dispaly in Z-A order");
									check_flag = true;
									break;
								}
							}
						}
						if (check_flag == true) {
							break;
						}
					}
					if (check_flag == false) {
						System.out.println("Products over ");
						RESULT.passed("Filter products in Z-A order",
								"Products  should dispaly in Z-A order ",
						"Products are  dispaly in Z-A order");
					}
				}
			}
			break;
			case PRICEUP: {
				uiDriver.click("btnprice");
				wait.until(ExpectedConditions.stalenessOf(webDriver.findElement(By.xpath(objMap.getLocator("btnprice")))));
				Direction = webDriver.findElement(By.xpath(objMap.getLocator("btnprice"))).getAttribute("data-direction");
				System.out.println("Direction " + Direction);
				if (Direction.equals("true")) {
					uiDriver.click("btnprice");
					wait.until(ExpectedConditions.stalenessOf(webDriver.findElement(By.xpath(objMap.getLocator("btnprice")))));
					Direction = webDriver.findElement(By.xpath(objMap.getLocator("btnprice"))).getAttribute("data-direction");
					System.out.println("Direction " + Direction);
				}
				if (Direction.equals("false")) {
					if (webDriver.findElements(By.xpath(objMap.getLocator("btnshowAll"))).size() > 0) {
						// click on the show all button
						uiDriver.getwebDriverLocator(objMap.getLocator("btnshowAll")).click();
						// wait for the products to load
						wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//button[@data-component='pager-prev']")));
					}

					List<WebElement> row_list = webDriver.findElements(By.xpath(objMap.getLocator("lstcoupon_rowlist")));
					wait.until(ExpectedConditions.visibilityOfAllElements(row_list));
					System.out.println("Number of rows " + row_list.size());
					for (int i = 0; i < row_list.size(); i++) {
						List<WebElement> col_list = row_list.get(i).findElements(By.tagName("li"));
						System.out.println("Number of columns in row is "+ col_list.size());

						for (int j = 0; j < col_list.size(); j++) {
							if (i > 0 && j == 0) {
								item_name1 = col_list.get(j).findElement(By.xpath(objMap.getLocator("strprice"))).getText();
								item_name1 = item_name1.split("\\/")[0];
								result1 = item_name1.replaceAll("[$]", "");
								exactPrice1 = Double.parseDouble(result1);
								System.out.println("price1 " + exactPrice1);
								if (exactPrice3 > exactPrice1) {
									System.out.println("items are not in PriceUP order");
									RESULT.failed("Filter products in PriceUP order",
											"Products  should dispaly in PriceUP order ",
									"Products are not dispaly in PriceUP order");
									check_flag = true;
									break;
								}
							}
							if (j == col_list.size() - 1) {
								last_item = col_list.get(j).findElement(By.xpath(objMap.getLocator("strprice"))).getText();
								last_item = last_item.split("\\/")[0];
								result3 = last_item.replaceAll("[$]", "");
								exactPrice3 = Double.parseDouble(result3);
								System.out.println("price3 " + exactPrice3);
							} else {
								item_name1 = col_list.get(j).findElement(By.xpath(objMap.getLocator("strprice"))).getText();
								item_name1 = item_name1.split("\\/")[0];
								result1 = item_name1.replaceAll("[$]", "");
								exactPrice1 = Double.parseDouble(result1);
								item_name2 = col_list.get(j + 1).findElement(By.xpath(objMap.getLocator("strprice"))).getText();
								item_name2 = item_name2.split("\\/")[0];
								result2 = item_name2.replaceAll("[$]", "");
								exactPrice2 = Double.parseDouble(result2);
								System.out.println("price1 " + exactPrice1);
								System.out.println("price2  " + exactPrice2);
								if (exactPrice2 < exactPrice1) {
									System.out.println("items are not in PriceUP order");
									RESULT.failed("Filter products in PriceUP order",
											"Products  should dispaly in PriceUP order ",
									"Products are not dispaly in PriceUP order");
									check_flag = true;
									break;
								}
							}

						}
						if (check_flag == true) {
							break;
						}

					}
					if (check_flag == false) {
						System.out.println("Products over ");
						RESULT.passed("Filter products in PriceUP order",
								"Products  should dispaly in PriceUP order ",
						"Products are  dispaly in PriceUP order");
					}
				}
			}
			break;
			case PRICEDOWN: {

				uiDriver.click("btnprice");
				wait.until(ExpectedConditions.stalenessOf(webDriver.findElement(By.xpath(objMap.getLocator("btnprice")))));
				Direction = webDriver.findElement(By.xpath(objMap.getLocator("btnprice"))).getAttribute("data-direction");
				System.out.println("Direction " + Direction);
				if (Direction.equals("false")) {
					uiDriver.click("btnprice");
					wait.until(ExpectedConditions.stalenessOf(webDriver.findElement(By.xpath(objMap.getLocator("btnprice")))));
					Direction = webDriver.findElement(By.xpath(objMap.getLocator("btnprice"))).getAttribute("data-direction");
					System.out.println("Direction " + Direction);
				}

				if (Direction.equals("true")) {
					if (webDriver.findElements(By.xpath(objMap.getLocator("btnshowAll"))).size() > 0) {
						// click on the show all button
						uiDriver.getwebDriverLocator(objMap.getLocator("btnshowAll")).click();
						// wait for the products to load
						wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//button[@data-component='pager-prev']")));
					}

					List<WebElement> row_list = webDriver.findElements(By.xpath(objMap.getLocator("lstcoupon_rowlist")));
					wait.until(ExpectedConditions.visibilityOfAllElements(row_list));
					System.out.println("Number of rows " + row_list.size());
					for (int i = 0; i < row_list.size(); i++) {
						List<WebElement> col_list = row_list.get(i).findElements(By.tagName("li"));
						System.out.println("Number of columns in row is "+ col_list.size());

						for (int j = 0; j < col_list.size(); j++) {
							if (i > 0 && j == 0) {
								item_name1 = col_list.get(j).findElement(By.xpath(objMap.getLocator("strprice"))).getText();
								item_name1 = item_name1.split("\\/")[0];
								result1 = item_name1.replaceAll("[$]", "");
								exactPrice1 = Double.parseDouble(result1);
								System.out.println("price1 " + exactPrice1);
								if (exactPrice3 < exactPrice1) {
									System.out.println("items are not in PRICEDOWN order");
									RESULT.failed("Filter products in PRICEDOWN order",
											"Products  should dispaly in PRICEDOWN order ",
									"Products are not dispaly in PRICEDOWN order");
									check_flag = true;
									break;
								}
							}
							if (j == col_list.size() - 1) {
								last_item = col_list.get(j).findElement(By.xpath(objMap.getLocator("strprice"))).getText();
								last_item = last_item.split("\\/")[0];
								result3 = last_item.replaceAll("[$]", "");
								exactPrice3 = Double.parseDouble(result3);
								System.out.println("price3 " + exactPrice3);
							} else {
								item_name1 = col_list.get(j).findElement(By.xpath(objMap.getLocator("strprice"))).getText();
								item_name1 = item_name1.split("\\/")[0];
								result1 = item_name1.replaceAll("[$]", "");
								exactPrice1 = Double.parseDouble(result1);
								item_name2 = col_list.get(j + 1).findElement(By.xpath(objMap.getLocator("strprice"))).getText();
								item_name2 = item_name2.split("\\/")[0];
								result2 = item_name2.replaceAll("[$]", "");
								exactPrice2 = Double.parseDouble(result2);
								System.out.println("price1 " + exactPrice1);
								System.out.println("price2  " + exactPrice2);
								if (exactPrice2 > exactPrice1) {
									System.out.println("items are not in PRICEDOWN order");
									RESULT.failed("Filter products in PRICEDOWN order",
											"Products  should dispaly in PRICEDOWN order ",
									"Products are not dispaly in PRICEDOWN order");
									check_flag = true;
									break;

								}
							}

						}
						if (check_flag == true) {
							break;
						}

					}
					if (check_flag == false) {
						System.out.println("Products over ");
						RESULT.passed("Filter products in PRICEDOWN  order",
								"Products  should dispaly in PRICEDOWNorder ",
						"Products are  dispaly in PRICEDOWN order");
					}
				}

			}
			break;

			case DISCOUNT_DOLLARUP: {
				uiDriver.click("btndiscount_dolar");
				wait.until(ExpectedConditions.stalenessOf(webDriver.findElement(By.xpath(objMap.getLocator("btndiscount_dolar")))));
				Direction = webDriver.findElement(By.xpath(objMap.getLocator("btndiscount_dolar"))).getAttribute("data-direction");
				System.out.println("Direction " + Direction);

				if (Direction.equals("false")) {
					uiDriver.click("btndiscount_dolar");
					wait.until(ExpectedConditions.stalenessOf(webDriver.findElement(By.xpath(objMap.getLocator("btndiscount_dolar")))));
					Direction = webDriver.findElement(By.xpath(objMap.getLocator("btndiscount_dolar"))).getAttribute("data-direction");
					System.out.println("Direction " + Direction);
				}
				if (Direction.equals("true")) {
					if (webDriver.findElements(By.xpath(objMap.getLocator("btnshowAll"))).size() > 0) {
						// click on the show all button
						uiDriver.getwebDriverLocator(objMap.getLocator("btnshowAll")).click();
						// wait for the products to load
						wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//button[@data-component='pager-prev']")));
					}

					List<WebElement> row_list = webDriver.findElements(By.xpath(objMap.getLocator("lstcoupon_rowlist")));
					wait.until(ExpectedConditions.visibilityOfAllElements(row_list));

					System.out.println("Number of rows " + row_list.size());

					for (int i = 0; i < row_list.size(); i++) {
						List<WebElement> col_list = row_list.get(i).findElements(By.tagName("li"));
						System.out.println("Number of columns in row is "+ col_list.size());

						for (int j = 0; j < col_list.size(); j++) {
							if (i > 0 && j == 0) {
								item_name1 = col_list.get(j).findElement(By.xpath(objMap.getLocator("strdolar_discount"))).getText();
								item_name1 = item_name1.split(" ")[1];
								result1 = item_name1.replaceAll("[$]", "");
								exactdiscount1 = Double.parseDouble(result1);
								System.out.println("exactdiscount1 "+ exactdiscount1);
								if (exactdiscount3 > exactdiscount1) {
									System.out.println("items are not in DISCOUNT_DOLARUP order");
									RESULT.failed("Filter products in DISCOUNT_DOLARUP order",
											"Products  should dispaly in DISCOUNT_DOLARUP order ",
									"Products are not dispaly in DISCOUNT_DOLARUP order");
									check_flag = true;
									break;
								}
							}
							if (j == col_list.size() - 1) {
								last_item = col_list.get(j).findElement(By.xpath(objMap.getLocator("strdolar_discount"))).getText();
								last_item = last_item.split(" ")[1];
								result3 = last_item.replaceAll("[$]", "");
								exactdiscount3 = Double.parseDouble(result3);
								System.out.println("exactdiscount3 "+ exactdiscount3);
							} else {
								item_name1 = col_list.get(j).findElement(By.xpath(objMap.getLocator("strdolar_discount"))).getText();
								item_name1 = item_name1.split(" ")[1];
								result1 = item_name1.replaceAll("[$]", "");
								exactdiscount1 = Double.parseDouble(result1);
								item_name2 = col_list.get(j + 1).findElement(By.xpath(objMap.getLocator("strdolar_discount"))).getText();

								item_name2 = item_name2.split(" ")[1];
								result2 = item_name2.replaceAll("[$]", "");
								exactdiscount2 = Double.parseDouble(result2);
								System.out.println("exactdiscount1 "+ exactdiscount1);
								System.out.println("exactdiscount2  "+ exactdiscount2);

								if (exactdiscount1 > exactdiscount2) {
									System.out.println("items are not in DISCOUNT_DOLARUP order");
									RESULT.failed("Filter products in DISCOUNT_DOLARUP order",
											"Products  should dispaly in DISCOUNT_DOLARUP order ",
									"Products are not dispaly in DISCOUNT_DOLARUP order");
									check_flag = true;
									break;

								}
							}

						}
						if (check_flag == true) {
							break;
						}

					}
					if (check_flag == false) {
						System.out.println("Products over ");
						RESULT.passed(
								"Filter products in DISCOUNT_DOLARUP order",
								"Products  should dispaly in DISCOUNT_DOLARUP order ",
						"Products are  dispaly in DISCOUNT_DOLARUP order");
					}
				}
			}
			break;

			case DISCOUNT_DOLLARDOWN: {
				uiDriver.click("btndiscount_dolar");

				wait.until(ExpectedConditions.stalenessOf(webDriver.findElement(By.xpath(objMap.getLocator("btndiscount_dolar")))));
				Direction = webDriver.findElement(By.xpath(objMap.getLocator("btndiscount_dolar"))).getAttribute("data-direction");
				System.out.println("Direction " + Direction);
				if (Direction.equals("true")) {
					uiDriver.click("btndiscount_dolar");
					wait.until(ExpectedConditions.stalenessOf(webDriver.findElement(By.xpath(objMap.getLocator("btndiscount_dolar")))));
					Direction = webDriver.findElement(By.xpath(objMap.getLocator("btndiscount_dolar"))).getAttribute("data-direction");
					System.out.println("Direction " + Direction);
				}
				if (Direction.equals("false")) {
					if (webDriver.findElements(By.xpath(objMap.getLocator("btnshowAll"))).size() > 0) {
						// click on the show all button
						uiDriver.getwebDriverLocator(objMap.getLocator("btnshowAll")).click();
						// wait for the products to load
						wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//button[@data-component='pager-prev']")));
					}

					List<WebElement> row_list = webDriver.findElements(By.xpath(objMap.getLocator("lstcoupon_rowlist")));
					wait.until(ExpectedConditions.visibilityOfAllElements(row_list));
					System.out.println("Number of rows " + row_list.size());
					for (int i = 0; i < row_list.size(); i++) {
						List<WebElement> col_list = row_list.get(i).findElements(By.tagName("li"));
						System.out.println("Number of columns in row is "+ col_list.size());
						for (int j = 0; j < col_list.size(); j++) {
							if (i > 0 && j == 0) {
								item_name1 = col_list.get(j).findElement(By.xpath(objMap.getLocator("strdolar_discount"))).getText();
								item_name1 = item_name1.split(" ")[1];
								result1 = item_name1.replaceAll("[$]", "");
								exactdiscount1 = Double.parseDouble(result1);
								System.out.println("exactdiscount1 "+ exactdiscount1);
								if (exactdiscount3 < exactdiscount1) {
									System.out.println("items are not in DISCOUNT_DOLARDOWN order");
									RESULT.failed("Filter products in DISCOUNT_DOLARDOWN order",
											"Products  should dispaly in DISCOUNT_DOLARDOWN order ",
									"Products are not dispaly in DISCOUNT_DOLARDOWN order");
									check_flag = true;
									break;
								}
							}
							if (j == col_list.size() - 1) {
								last_item = col_list.get(j).findElement(By.xpath(objMap.getLocator("strdolar_discount"))).getText();
								last_item = last_item.split(" ")[1];
								result3 = last_item.replaceAll("[$]", "");
								exactdiscount3 = Double.parseDouble(result3);
								System.out.println("exactdiscount3 "
										+ exactdiscount3);
							} else {
								item_name1 = col_list.get(j).findElement(By.xpath(objMap.getLocator("strdolar_discount"))).getText();
								item_name1 = item_name1.split(" ")[1];
								result1 = item_name1.replaceAll("[$]", "");
								exactdiscount1 = Double.parseDouble(result1);
								item_name2 = col_list.get(j + 1).findElement(By.xpath(objMap.getLocator("strdolar_discount"))).getText();
								item_name2 = item_name2.split(" ")[1];
								result2 = item_name2.replaceAll("[$]", "");
								exactdiscount2 = Double.parseDouble(result2);
								System.out.println("exactdiscount1 "+ exactdiscount1);
								System.out.println("exactdiscount2  "+ exactdiscount2);

								if (exactdiscount1 < exactdiscount2) {
									System.out.println("items are not in DISCOUNT_DOLARDOWN order");
									RESULT.failed("Filter products in DISCOUNT_DOLARDOWN order",
											"Products  should dispaly in DISCOUNT_DOLARDOWN order ",
									"Products are not dispaly in DISCOUNT_DOLARDOWN order");
									check_flag = true;
									break;

								}
							}

						}
						if (check_flag == true) {
							break;
						}

					}
					if (check_flag == false) {
						System.out.println("Products over ");
						RESULT.passed("Filter products in DISCOUNT_DOLARDOWN order",
								"Products  should dispaly in DISCOUNT_DOLARDOWN order ",
						"Products are  dispaly in DISCOUNT_DOLARDOWN order");
					}
				}

			}
			break;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	 /**
     * Function Name: FD_viewSourceLang
     * Purpose: FD_ viewSourceLang function verifies the presence of lang attribute in Source Page
     * Created By: Shraddha Shah 
     * Created Date: 
     * Modified By:
     * Modified Date: 
     **/ 

	public void FD_viewSourceLang() {
		String PageSource = webDriver.getPageSource();
		SleepUtils.getInstance().sleep(TimeSlab.LOW);
		WebElement Meta = webDriver.findElement(By.xpath("//meta[@http-equiv='Content-type']"));
		String lang = Meta.getAttribute("lang");
		System.out.println(lang);
		// Verifying lang="es-ES" for spanish language
		if(webDriver.findElements(By.cssSelector(objMap.getLocator("btnENGLISH"))).size() >0)
		{
			if (lang.equals("es-ES")) {
				RESULT.passed("viewSourceLang",
						"The lang='es-ES' in the tag should be available",
				"The lang='es-ES' in the tag is available");
			} else {
				RESULT.failed("viewSourceLang",
						"The lang='es-ES' in the tag should be available",
				"The lang='es-ES' in the tag is not available");
			}
		}
		// Verifying lang="en-US" for english language
		else
		{
			if (lang.equals("en-US")) {
				RESULT.passed("viewSourceLang",
						"The lang='en-US' in the tag should be available",
				"The lang='en-US' in the tag is available");
			} else {
				RESULT.failed("viewSourceLang",
						"The lang='en-US' in the tag should be available",
				"The lang='en-US' in the tag is not available");
			}
		}	
	}

	public void FD_passwordValidation(String password)
	throws InterruptedException {
		String password_Pattern = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*\\S+$).{0,}";
		String password_Symbol = "~`!@#$%^&*() _+=-][}{';:/><";

		final int MIN_PASSWORD_LENGTH = 6;
		final int MAX_PASSWORD_LENGTH = 20;
		int Upper = 0, Lower = 0, Digit = 0, Symbol = 0;
		int score = 0, length = 0;
		boolean upperCase = false, lowerCase = false, digits = false, nonAlpha = false;

		length = password.toString().length();
		length = password.trim().length();

		try {
			if (length < 6) {
				score = 0;
			}
			if (length > 6 && length < 12) {
				score += 10;
			} else if (length > 11) {
				score += 15;
			}

			lowerCase = password.matches(".*[a-z]+.*");
			if (lowerCase) {
				score += 5;
			}

			upperCase = password.matches(".*[A-Z]+.*");
			if (upperCase) {
				score += 5;
			}

			if (upperCase && lowerCase) {
				score += 5;
			}

			digits = password.matches("(.)*(\\d)(.)*");
			if (digits) {
				score += 5;
			}

			boolean whiteSpace = password.matches("(.)*(\\s)(.)*");

			if (!whiteSpace) {
				nonAlpha = password.matches("(.)*(\\W)(.)*");
				if (nonAlpha) {
					score += (nonAlpha) ? 15 : 10;
				}
			}

			if (upperCase && lowerCase && digits && nonAlpha) {
				score += 35;
			}

			int whiteSpacelength = password.trim().length();
			if (whiteSpacelength == 0) {
				score = 0;
			}

			if (score < 15) {
				if (uiDriver.isElementpresentweb(getwebDriverLocator(objMap.getLocator("txtPwdWeak")))) {
					RESULT.passed("Validating Strength of a Password",
							"Password Strength should be WEAK",
					"Password Strength validatation is successful");
				} else if (!uiDriver.isElementpresentweb(getwebDriverLocator(objMap.getLocator("txtPwdWeak")))) {
					RESULT.failed("Validating Strength of a Password",
							"Password Strength should be WEAK",
					"Password Strength validatation is Failed");
				}
			} else if (score < 20) {
				if (uiDriver.isElementpresentweb(getwebDriverLocator(objMap.getLocator("txtPwdAvg")))) {
					RESULT.passed("Validating Strength of a Password",
							"Password Strength should be Average",
					"Password Strength validatation is successful");
				} else if (!uiDriver.isElementpresentweb(getwebDriverLocator(objMap.getLocator("txtPwdAvg")))) {
					RESULT.failed("Validating Strength of a Password",
							"Password Strength should be Average",
					"Password Strength validatation is Failed");
				}
			} // System.out.println("Average");
			else if (score < 35) {
				if (uiDriver.isElementpresentweb(getwebDriverLocator(objMap.getLocator("txtPwdStrong")))) {
					RESULT.passed("Validating Strength of a Password",
							"Password Strength should be Strong",
					"Password Strength validatation is successful");
				} else if (!uiDriver.isElementpresentweb(getwebDriverLocator(objMap.getLocator("txtPwdStrong")))) {
					RESULT.failed("Validating Strength of a Password",
							"Password Strength should be Strong",
					"Password Strength validatation is Failed");
				}// System.out.println("strong");
			} else if (score > 34) {
				if (uiDriver.isElementpresentweb(getwebDriverLocator(objMap.getLocator("txtPwdVryStrong")))) {
					RESULT.passed("Validating Strength of a Password",
							"Password Strength should be Very Strong",
					"Password Strength validatation is successful");
				} else if (uiDriver.isElementpresentweb(getwebDriverLocator(objMap.getLocator("txtPwdVryStrong")))) {
					RESULT.failed("Validating Strength of a Password",
							"Password Strength should be Very Strong",
					"Password Strength validatation is Failed");
				}// System.out.println("Very strong");
			}

			password = password.trim();
			if ((password != null) && (password.length() > 0)) {

				char[] aC = password.toCharArray();
				for (char i : aC) {
					if (Character.isUpperCase(i)) {
						Upper++;
						if (uiDriver.isElementpresentweb(getwebDriverLocator(objMap.getLocator("txtCapitalValidation")))&& Upper == 1) {
							RESULT.passed("Validating Password hint ScratchOut",
									"1 or more capital letters hint line should Scratchout",
							"1 or more Capital letters is scratchedOut successfully");
						} else if (!uiDriver.isElementpresentweb(getwebDriverLocator(objMap.getLocator("txtCapitalValidation")))) {
							RESULT.failed("Validating Password hint ScratchOut",
									"1 or more capital letters hint line should Scratchout",
							"Password hint validatation is Failed: 1 or more capital letters line is not Scratchedout");
						}
					} else if (Character.isLowerCase(i)) {
						Lower++;
						if (uiDriver.isElementpresentweb(getwebDriverLocator(objMap.getLocator("txtLetterValidation")))&& Lower == 1) {
							RESULT.passed("Validating Password hint ScratchOut",
									"1 or more letters hint line should Scratchout",
							"1 or more letters is scratchedOut successfully");
						} else if (!uiDriver.isElementpresentweb(getwebDriverLocator(objMap.getLocator("txtLetterValidation")))) {
							RESULT.failed("Validating Password hint ScratchOut",
									"1 or more letters hint line should Scratchout",
							"Password hint validatation is Failed: 1 or more letters line is not Scratchedout");
						}
					} else if (Character.isDigit(i)) {
						Digit++;
						if (uiDriver.isElementpresentweb(getwebDriverLocator(objMap.getLocator("txtNumValidation")))&& Digit == 1) {
							RESULT.passed("Validating Password hint ScratchOut",
									"1 or more numbers hint line should Scratchout",
							"1 or more numbers is scratchedOut successfully");
						} else if (!uiDriver.isElementpresentweb(getwebDriverLocator(objMap.getLocator("txtNumValidation")))) {
							RESULT.failed("Validating Password hint ScratchOut",
									"1 or more numbers hint line should Scratchout",
							"Password hint validatation is Failed: 1 or more numbers line is not Scratchedout");
						}
					} else if (password_Symbol.indexOf(String.valueOf(i)) >= 0) {
						Symbol++;
						if (uiDriver.isElementpresentweb(getwebDriverLocator(objMap.getLocator("txtSplCharValidation")))&& Symbol == 1) {
							RESULT.passed("Validating Password hint ScratchOut",
									"1  or more special characters hint line should Scratchout",
							"1 or more special characters is scratchedOut successfully");
						} else if (!uiDriver.isElementpresentweb(getwebDriverLocator(objMap.getLocator("txtSplCharValidation")))) {
							RESULT.failed("Validating Password hint ScratchOut",
									"1 or more special characters hint line should Scratchout",
							"Password hint validatation is Failed: 1 or more special characters line is not Scratchedout");
						}
					} /*
					 * else { System.out.println(i +
					 * " is an invalid character in the password."); }
					 */
				}

				if ((length >= MIN_PASSWORD_LENGTH)
						&& (length <= MAX_PASSWORD_LENGTH)) {
					if (uiDriver.isElementpresentweb(getwebDriverLocator(objMap.getLocator("txtLengthValidation")))) {
						RESULT.passed("Validating Password hint ScratchOut",
								"6  or more characters hint line should Scratchout",
						"6 or more characters is scratchedOut successfully");
					} else if (!uiDriver.isElementpresentweb(getwebDriverLocator(objMap.getLocator("txtLengthValidation")))) {
						RESULT.failed("Validating Password hint ScratchOut",
								"6 or more characters hint line should Scratchout",
						"Password hint validatation is Failed: 6 or more characters line is not Scratchedout");
					}

					Pattern pattern = Pattern.compile(password_Pattern);
					Matcher matcher = pattern.matcher(password);
					boolean matches = matcher.matches();

					if (matches == true) {
						RESULT.passed("Validating Password",
								"password validatation should be done successfully",
						"password validatation is successful");
					} else {
						RESULT.log("Validating Password",
								ResultType.FAILED,
								"password validatation should be done successfully",
								"password validatation failed", true);
					}
				} else {
					RESULT.failed("Validating Password",
							"password validatation should be done successfully",
					"Password validation failed: Should be 6 to 20 characters");
				}
			} else {
				RESULT.failed("Validating Password",
						"password validatation should be done successfully",
				"Password validation failed: field is blank or only Whitespace");
			}
		} catch (Exception e) {
			e.printStackTrace();
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
			String AltPhone, String AltExtn, String None, String SelectAddress, String FlexibilityFlag)
	throws InterruptedException, FindFailed

	{
		try {

			String ChoosedOpt = AddSelectSelectPickupDeleteEdit;
			switch (ChooseOpt.valueOf(ChoosedOpt)) {
			/* Add a new Delivery Address to the list */
			case Add:
				uiDriver.click("AddNewAddressBtn");
				uiDriver.setValue("FirstNameTb", FirstNameTb);
				uiDriver.setValue("LastNameTb", LastNameTb);
				String Servicetype = ServiceType;
				if (Servicetype.equals("CORPORATE")) {
					uiDriver.click("Commercial");
					uiDriver.setValue("CompName", CompName);
				} else if (Servicetype.equals("HOME")) {
					uiDriver.click("Residential");
				} else {
					System.out.println("Please select your service type");
				}
				uiDriver.setValue("StreetAdd", StreetAdd);
				uiDriver.setValue("AptName", AptName);
				uiDriver.setValue("AddLine2", AddLine2);
				uiDriver.setValue("City", City);
				uiDriver.setValue("State", State);
				uiDriver.setValue("ZipCode", ZipCode);
				uiDriver.setValue("Contact", Contact);
				uiDriver.setValue("Ext1", Ext1);
				uiDriver.setValue("AltContact", AltContact);
				uiDriver.setValue("Ext2", Ext2);
				uiDriver.setValue("SpclDelInstructions", SpclDelInstructions);

				if (DoormanDelivery.equalsIgnoreCase("yes")) {
					uiDriver.click("DoormanDelivery");
				}

				if (NeighbourDelivery.equalsIgnoreCase("yes")) {
					uiDriver.click("NeighbourDelivery");
					uiDriver.setValue("AltFirstName", AltFirstName);
					uiDriver.setValue("AltLastName", AltLastName);
					uiDriver.setValue("AltApt", AltApt);
					uiDriver.setValue("AltPhone", AltPhone);
					uiDriver.setValue("AltExtn", AltExtn);
				}

				uiDriver.click("EditBtn");
				SleepUtils.getInstance().sleep(TimeSlab.YIELD);

				if (webDriver.getTitle().equals(
				"FreshDirect - Checkout - Choose Delivery Address")) {
					SleepUtils.getInstance().sleep(TimeSlab.YIELD);
					System.out.println("Address added Sucessfully!!!!");
					RESULT.passed("ChooseAction",
							"User should be able add new delivery address",
					"Address added Sucessfully!!!!");
				} else {
					String Error_Msg1 = webDriver.findElement(
							By.xpath(objMap.getLocator("ErrorMsgAdd")))
							.getText();
					System.out.println(Error_Msg1);
					RESULT.failed("ChooseAction",
							"User should be able add new delivery address",
							Error_Msg1);
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
						System.out.println(gotadd);
						precedingSiblings.get(j).getAttribute("value");
						precedingSiblings.get(j).click();
						System.out.println(precedingSiblings.get(j).getAttribute("value"));
						System.out.println("Delivery Address selected");
						RESULT.passed("Select Delivery Address",
								"User should be able select Delivery Address",
						"Address selected Sucessfully!!!!");
						break;
					}
				}
				if ((a == 0)&&(FlexibilityFlag.equalsIgnoreCase("YES"))) {
					precedingSiblings.get(0).click();
					RESULT.passed("Select Delivery Address",
							"User should be able to select delivery address",
							myadd +" Address do not exist in the list.Default First Address selected Sucessfully!!!!");

				} else if((a == 0)&&(FlexibilityFlag.equalsIgnoreCase("NO"))) {
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
						.xpath(objMap.getLocator("AddressPathPickup")));
				int iSize1 = addLst3.size();
				List<WebElement> precedingSiblings1 = webDriver.findElements(By
						.xpath(objMap.getLocator("RadioPathPickup")));
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
						RESULT.passed("ChooseAction",
								"User should be able add new delivery address",
						"Address selected Sucessfully!!!!");
						break;
					}
				}
				if (b == 0) {
					String Error_Msg2 = "Address did not match";
					System.out.println(Error_Msg2);
					RESULT.failed("ChooseAction",
							"User should be able to select Pickup Address",
					"Address did not match");
				}

				break;

				/* Delete Delivery Address from the existing list */
			case Delete:

				List<WebElement> addLst1 = webDriver.findElements(By
						.xpath(objMap.getLocator("AddressPath")));
				int dSize = addLst1.size();

				String myadd1 = SelectAddress;
				// System.out.println("given:"+myadd1);
				System.out.println(dSize);

				String spltAddress1[] = myadd1.split("\\n");
				String myaddedt1 = "";
				for (int i = 0; i < spltAddress1.length; i++) {
					myaddedt1 = myaddedt1 + spltAddress1[i] + "\n";
				}
				System.out.println(myaddedt1);

				int c = 0;
				for (int i = 0; i < dSize; i++) {

					String gotadd1 = addLst1.get(i).getText();
					// System.out.println("Address:"+gotadd1);
					if (gotadd1.startsWith(myaddedt1)) {
						c++;
						System.out.println(gotadd1);
						addLst1.get(i).findElement(By.xpath("input")).click();
						System.out.println("Address deleted successfully!!!!");
						RESULT
						.passed(
								"ChooseAction",
								"User should be able delete new delivery address",
						"Address deleted Sucessfully!!!!");
						break;
					}
				}
				if (c == 0) {
					RESULT.failed("ChooseAction",
							"User should be able to delete Delivery Address",
					"Address did not match");

				}

				break;

				/* Edit existing Delivery Address from the list */
			case Edit:

				List<WebElement> addLst2 = webDriver.findElements(By
						.xpath(objMap.getLocator("AddressPath")));
				int eSize = addLst2.size();

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
					if (gotadd2.startsWith(myaddedt2)) {
						d++;
						System.out.println(gotadd2);
						addLst2.get(i).findElement(By.xpath("a")).click();

						uiDriver.setValue("FirstNameTb", FirstNameTb);
						uiDriver.setValue("LastNameTb", LastNameTb);
						String Servicetype1 = ServiceType;
						if (Servicetype1.equals("CORPORATE")) {
							uiDriver.click("CommercialEdit");
							uiDriver.setValue("CompName", CompName);
						}
						if (Servicetype1.equals("HOME")) {
							uiDriver.click("ResidentialEdit");
						}
						// else
						// {
						// System.out.println("Please select your service type");
						// }
						uiDriver.setValue("StreetAdd", StreetAdd);
						uiDriver.setValue("AptName", AptName);
						uiDriver.setValue("AddLine2", AddLine2);
						uiDriver.setValue("City", City);
						uiDriver.setValue("State", State);
						uiDriver.setValue("ZipCode", ZipCode);
						uiDriver.setValue("Contact", Contact);
						uiDriver.setValue("Ext1", Ext1);
						uiDriver.setValue("AltContact", AltContact);
						uiDriver.setValue("Ext2", Ext2);
						uiDriver.setValue("SpclDelInstructions",
								SpclDelInstructions);
						if (None.equalsIgnoreCase("yes")) {
							uiDriver.click("None");
						}

						if (DoormanDelivery.equalsIgnoreCase("yes")) {
							uiDriver.click("DoormanDelivery");
						}

						if (NeighbourDelivery.equalsIgnoreCase("yes")) {
							uiDriver.click("NeighbourDelivery");
							uiDriver.setValue("AltFirstName", AltFirstName);
							uiDriver.setValue("AltLastName", AltLastName);
							uiDriver.setValue("AltApt", AltApt);
							uiDriver.setValue("AltPhone", AltPhone);
							uiDriver.setValue("AltExtn", AltExtn);
						}

						uiDriver.click("EditBtn");
						uiDriver.selenium.waitForPageToLoad("9000");
						System.out.println("Address edited successfully!!!");
						if (webDriver
								.getTitle()
								.equals(
								"FreshDirect - Checkout - Choose Delivery Address")) {
							SleepUtils.getInstance().sleep(TimeSlab.YIELD);
							RESULT
							.passed(
									"ChooseAction",
									"User should be able edit delivery address",
							"Address edited Sucessfully!!!!");
						} else {
							String Error_Msg3 = webDriver
							.findElement(
									By
									.xpath(objMap
											.getLocator("ErrorMsgEdit")))
											.getText();
							System.out.println(Error_Msg3);
							RESULT
							.failed(
									"ChooseAction",
									"User should be able edit delivery address",
									Error_Msg3);
						}
						break;
					}
				}
				if (d == 0) {
					RESULT.failed("ChooseAction",
							"User should be able edit delivery address",
					"Address did not match");
				}
				break;
			}
		} catch (Exception e) {
			System.out.println("Please enter valid option");
			RESULT.failed("Error Found", "Delivery Address should be selected",
					"Error occured" + e);
		}
	}
	public void FD_paymentOption_old(String Chooseoptn, String cardDetail,
			String NameOnCard, String routing, String Bank,
			String CardOrAcctype, String CardNum, String ExpiryMnth,
			String ExpiryYr, String CardStreetAdd, String CardAppNum,
			String CardAddLine2, String CardCountry, String CardCity,
			String CardState, String CardZip,String FlexibilityFlag) {

		try {
			// handle exception if an order is already placed
			if (webDriver.getTitle().endsWith("An order is already placed")) {
				uiDriver.getwebDriverLocator(objMap.getLocator("btnalreadyPlaced")).click();
			}
			// select chosen option for operation on details of card/account via switch case
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
				// To tackle  problem due  to \n while dealing with excel  handle the problem caused due to excel appending extra "/n"
				String mycard = cardDetail;
//				String spltSel[] = mycard1.split("\\n");
				mycard=mycard.replaceAll("\\n", "");
				mycard=mycard.replaceAll(" ", "");
//				String mycard = "";
//				for (int i = 0; i < spltSel.length-1; i++) {
//					mycard = mycard + spltSel[i] + "\n";
//				}
//				mycard=mycard+spltSel[spltSel.length];
				// check if no cards added in system
				int a = 0;
				if (CardList.size() == 0) {
					// System.out.println("No card in list");
					RESULT.failed(
							"Selection of Payment option",
							mycard+":Payment option should get selected",
					"No Payment option available in list to select");
				} else {
					// Retrieve one by one all card details to compare with  given card detail
					for (int i = 0, j = 0; i < CardList.size(); i++, j++) {
						String details1 = CardList.get(i).findElement(By.tagName(objMap.getLocator("lstgetCardDetails"))).getText();
						System.out.println("Details:" + details1);
						String details=details1.replaceAll("\\n", "");
						details=details.replaceAll(" ", "");
						if (details.startsWith(mycard)) {
							a++;
							CardRadio.get(j).click();
							RESULT.passed(
									"Selection of Payment option",
									mycard+":Payment option should get selected",
									details1+":Payment option selected");
							break;
						}
					}
					if (a == 0 && FlexibilityFlag.equalsIgnoreCase("Yes")) {
						RESULT.warning("Selection of Payment option",
								cardDetail+":Payment option should get selected",
								"No Payment option matched with given details:"+cardDetail);
						CardRadio.get(0).click();
						String card=CardList.get(0).findElement(By.tagName(objMap.getLocator("lstgetCardDetails"))).getText();
						RESULT.passed("Selection of Payment option",
								cardDetail+":Payment option should get selected",
								cardDetail+":card is not available in system so first available card:"+card+" is selected");

					} else if (a == 0 && FlexibilityFlag.equalsIgnoreCase("No"))
					{
						RESULT.failed("Selection of Payment option",
								cardDetail+":Payment option should get selected",
								"No Payment option from list matched with given details:"+mycard);
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
						if (webDriver.getTitle().contains("Edit Credit Card")) {
							// System.out.println("Error");
							RESULT
							.failed(
									"Edit existing Credit or EBT card",
									"Credit or EBT card should get added with given details",
							"one or more given detail is incorrect");
						} else {
							// System.out.println("caRD EDITED");
							RESULT
							.passed(
									"Edit existing Credit or EBT card",
									"Credit or EBT card should get updated with given details",
							"Credit or EBT card updated with given details");
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
						// System.out.println("No card FOUND");
						RESULT
						.done(
								"Credit or EBT card did not matched with current card List",
								"Given details could not match with any existing card details",
						"Given details did not matched with any existing card details");
					}
				}
				break;
			}

		} catch (Exception e) {
			// System.out.println("\n Exception"+e);
			RESULT
			.failed(
					"Error occured some where",
					"Desired Credit or EBT card should get deleted or edited or selected or added",
					"\n Exception" + e);
			e.printStackTrace();
		}
	}

	private enum AddYMALOrCarousal {
		YMAL, CAROUSAL
	};

	public void FD_addYMALOrCarousal( String Item, String ItemType, String OrderFlagNewModify,
			String Quantity, String ClickOrHover, String FlexibilityFlag,
			String SalesUnit, String Packaging) throws InterruptedException {
		String type = ItemType.toUpperCase();
		switch (AddYMALOrCarousal.valueOf(type)) {
		case YMAL: {
			if (uiDriver.isDisplayed("strymalOrCarosProd")) {
				try {
					uiDriver.FD_addToCart(Item, OrderFlagNewModify,
							Quantity, ClickOrHover, FlexibilityFlag,
							SalesUnit, Packaging);
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
		}break;
		case CAROUSAL: {
			//webDriver.findElement(By.name("FD_LOGO")).click();
			//WebUIDriver.selenium.waitForPageToLoad(PageLoadTime);
			SleepUtils.getInstance().sleep(TimeSlab.YIELD);
			if (uiDriver.isDisplayed("strymalOrCarosProd")) {
				try {
					uiDriver.FD_addToCart(Item, OrderFlagNewModify,
							Quantity, ClickOrHover, FlexibilityFlag,
							SalesUnit, Packaging);
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
		}break;
		default: {
			RESULT.warning("Operation check", "Operation should be either YMAL or CAROUSAL", "Correct operation not found");						
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
						String details = CardList.get(i).findElement(By.tagName(objMap
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

	public File takescreenshot() {
		System.out.println("ENTERED WEUIDRIVER TakeSS method******");
		File scrFile = ((TakesScreenshot)webDriver).getScreenshotAs(OutputType.FILE);
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
