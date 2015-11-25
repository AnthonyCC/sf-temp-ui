/******************************************************************************
$Id : IConnectDriver.java 9/8/2014 1:21:28 PM
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

package ModuleDrivers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;

import org.sikuli.script.FindFailed;

import com.gargoylesoftware.htmlunit.ElementNotFoundException;

import ui.RobotPowered;
import cbf.engine.BaseModuleDriver;
import cbf.engine.TestResultLogger;
import cbf.utils.DBUtils;
import cbf.utils.DataRow;
import cbf.utils.SleepUtils;

import cbf.utils.SleepUtils.TimeSlab;



/**
 * This Function allows FreshDirect user to select Delivery Address in 4 different ways:
 * 1)Adding New Delivery Address
 * 2)Selecting Delivery Address from Corporate,Residential or Pickup list of Addresses
 * 3)Editing Delivery Address from Corporate,Residential or Pickup list of Addresses
 * 4)Deleting Delivery Address from Corporate or Residential list of Addresses
 */

/**
 * 
 * Extends BaseModuleDriver class and performs application specific operations
 * 
 */
public class HeaderDriver extends BaseModuleDriver 
{	// This will wait for the page to load for 300 seconds (i.e. 300000 Millie seconds)
	
	/**
	 * Constructor to initialize logger
	 * 
	 * @param resultLogger
	 *            TestResultLogger object with methods like passed, failed,
	 *            error etc available for reporting runtime results
	 */
	public HeaderDriver(TestResultLogger resultLogger)
	{
		
		super(resultLogger);
		  
		RESULT = resultLogger;
	}

	/* Verify zip code selection field displayed on page header.*/	

	public void ZipCode(DataRow input, DataRow output) throws InterruptedException, FindFailed
	{	
		try{	
			
			// wait and check if the zipcode is available
			try{
				uiDriver.wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(objMap.getLocator("strzipcode"))));
			}catch(Exception e){
				RESULT.failed("Zipcode validation", "Zip code should be available on page", "Zip code is not avaialble on page after waiting for 3 minutes");
				return;
			}

			String zip_pattern="^[0-9]{5}$";
			Pattern zipcode_p=Pattern.compile(zip_pattern);
			Matcher m;
			
			try
			{
				m=zipcode_p.matcher(webDriver.findElement(By.xpath(objMap.getLocator("strzipcode"))).getText());
				if(m.find())
				{
					RESULT.passed("Zipcode validation", "Zip code should contain 5 digits", "Zip code contains 5 digits "+
							webDriver.findElement(By.xpath(objMap.getLocator("strzipcode"))).getText());
				}
				else
				{
					RESULT.failed("Zipcode validation", "Zip code should contain 5 digits", "Zip code is invalid"+
							webDriver.findElement(By.xpath(objMap.getLocator("strzipcode"))).getText());
				}
			}
			catch(Exception e)
			{
				RESULT.failed("Zip code ", "Zip code should be available and 5 digit validation should be done",
						"Zip code 5 digit validation failed due to " + e.getMessage());
				
			}
			
			//go button validation
			try
			{
				if(uiDriver.getwebDriverLocator(objMap.getLocator("btngoBtn")).isDisplayed() &&
						uiDriver.getwebDriverLocator(objMap.getLocator("btngoBtn")).isEnabled())
				{
					RESULT.passed("Zip code Go button validation", "Zip code go button should be visible and enabled", 
					"Zip code go button is visible and enabled");
				}
				else
				{
					RESULT.failed("Zip code Go button validation", "Zip code go button should be visible and enabled", 
					"Zip code go button is not visible or enabled");
				}
			}
			catch(Exception e)
			{
				RESULT.failed("Zip code ","Go button should be displayed and enabled",
						"Go button is either not displayed or enabled");
			}
			
			List<Map> list;
			String zipcode=null;
			DBUtils connect=new DBUtils("devint");
			try
			{				 
				//Verify Zipcode for No Coverage
				list =connect.runQueryOr("devint","select ZIPCODE from DLV.ZIPCODE where COS_COVERAGE<=0.1 and HOME_COVERAGE<=0.1");
				zipcode=list.get((int) Math.round(Math.ceil(Math.random()*(list.size()-1)))).values().toString();
				webDriver.findElement(By.id(objMap.getLocator("strnewZipCode"))).sendKeys(zipcode);
			}
			catch(Exception e)
			{
				RESULT.failed("Zip code : Database exception", "Database query should be successful", "Database query unsuccessful and zipcode value is "+zipcode);
			}
			
			uiDriver.click("btngoBtn");
			uiDriver.waitForPageLoad();
			try{
				(new WebDriverWait(webDriver,20)).until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(objMap.getLocator("btngoBtn"))));
			}catch(Exception e){
				uiDriver.wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector(objMap.getLocator("btngoBtn"))));
			}
			
			//SleepUtils.getInstance().sleep(TimeSlab.YIELD);
			
			if(webDriver.findElements(By.linkText(objMap.getLocator("lnkofficeDeliveryLink"))).size()>0 &&
					webDriver.findElement(By.linkText(objMap.getLocator("lnkofficeDeliveryLink"))).getText().equals("Office delivery?"))
			{
				try
				{
					uiDriver.click("lnkofficeDeliveryLink");
					uiDriver.waitForPageLoad();
					//uiDriver.wait.until(ExpectedConditions.stalenessOf(webDriver.findElement(By.linkText(objMap.getLocator("lnkofficeDeliveryLink")))));
					if(webDriver.findElements(By.className(objMap.getLocator("strnoCoverage"))).size()>0 && 
							uiDriver.isElementPresent(By.className(objMap.getLocator("strnoCoverage"))))
					{
						RESULT.passed("Zip code validation for No Coverage Office Delivery", "'FRESH DIRECT IS NOT AVAILABLE AT YOUR AREA THIS TIME' Attention message should be displayed for zipcode having No Coverage in Office Delivery", 
						"'FRESH DIRECT IS NOT AVAILABLE AT YOUR AREA THIS TIME' Attention message is displayed for zipcode having No Coverage in Office Delivery");
					}
					else
					{
						RESULT.failed("Zip code validation for No Coverage Office Delivery", "'FRESH DIRECT IS NOT AVAILABLE AT YOUR AREA THIS TIME' Attention message should be displayed for zipcode having No Coverage area in Office Delivery", 
						"'FRESH DIRECT IS NOT AVAILABLE AT YOUR AREA THIS TIME' Attention message is not displayed for zipcode having No Coverage area in Office Delivery");
					}
					
					try
					{
						if(webDriver.findElements(By.xpath(objMap.getLocator("imgatTheOffice"))).size()>0 && 
								uiDriver.getwebDriverLocator(objMap.getLocator("lnkhomeDelivery")).getText().equals("Home delivery?"))
						{
							RESULT.passed("Verify Office/Home Delivery Link", "Home Delivery Link and Web page with 'AT THE OFFICE' header should be displayed.",
							"Home Delivery Link and Web page with 'AT THE OFFICE' header is displayed.");
						}
						else	
						{
							String ImageFile1 = webDriver.findElement(By.className(objMap.getLocator("strerror"))).getText();									
							RESULT.failed("Verify Office/Home Delivery Link", "Home Delivery Link and Web page with 'AT THE OFFICE' header should be displayed.", ImageFile1);
						}
					}
					catch(Exception e)
					{
						RESULT.warning("Office/Home Delivery", "Home delivery link should be available and should contain text Home delivery? text",
								"Home delivery link is either not available or text is not Home delivery?");
					}
				}
				catch(Exception e)
				{
					RESULT.failed("Zip Code : Office Delivery link","Office delivery link should be clicked successfully",
							"Office Delivery link is not clicked successfully and the exception caught is "+e.getMessage());
				}
				
			}
			else
			{
				RESULT.warning("Zip code validation for Coverage check for Office Delivery",
						"Office Delivery link should be available", "Office Delivery link is not available");
			}
			
			if(webDriver.findElements(By.linkText(objMap.getLocator("lnkhomeDelivery"))).size()>0 && 
					uiDriver.getwebDriverLocator(objMap.getLocator("lnkhomeDelivery")).getText().equals("Home delivery?"))
			{
				try
				{
					uiDriver.click("lnkhomeDelivery");		
					uiDriver.waitForPageLoad();
					//uiDriver.wait.until(ExpectedConditions.stalenessOf(webDriver.findElement(By.linkText(objMap.getLocator("lnkhomeDelivery")))));
					if(webDriver.findElements(By.className(objMap.getLocator("strnoCoverage"))).size()>0
							&& uiDriver.isElementPresent(By.className(objMap.getLocator("strnoCoverage"))))
					{
						RESULT.passed("Zip code validation for No Coverage Home Delivery", "'FRESH DIRECT IS NOT AVAILABLE AT YOUR AREA THIS TIME' Attention message should be displayed for zipcode having No Coverage in Home Delivery", 
						"'FRESH DIRECT IS NOT AVAILABLE AT YOUR AREA THIS TIME' Attention message is displayed for zipcode having No Coverage in Home Delivery");
					}
					else
					{
						RESULT.failed("Zip code validation for No Coverage Home Delivery", "'FRESH DIRECT IS NOT AVAILABLE AT YOUR AREA THIS TIME' Attention message should be displayed for zipcode having No Coverage area in Home Delivery", 
						"'FRESH DIRECT IS NOT AVAILABLE AT YOUR AREA THIS TIME' Attention message is not displayed for zipcode having No Coverage area in Home Delivery");
					}
				}
				catch(Exception e)
				{
					RESULT.failed("Zip code validation for coverage check for Home Delivery", "Home Delivery link should be clicked successfully",
							"Home Delivery link is not clicked and the exception caught is "+e.getMessage());
				}
				
			}
			else
			{
				RESULT.warning("Zip code validation for No Coverage Home Delivery",
						"Home Delivery link should be available", "Home Delivery link is not available");
			}
			
			// Verify Zipcode for Partial Coverage
			zipcode = null;
			try{			
			List<Map> list1 = connect.runQueryOr("devint","select ZIPCODE from DLV.ZIPCODE where COS_COVERAGE >0.1 and COS_COVERAGE< 0.9 and HOME_COVERAGE> 0.1 and HOME_COVERAGE<0.9");
			zipcode=list1.get((int) Math.round(Math.ceil(Math.random()*(list1.size()-1)))).values().toString();
			webDriver.findElement(By.id(objMap.getLocator("strnewZipCode"))).sendKeys(zipcode);
			}
			catch(Exception e)
			{
				RESULT.failed("Zip code : Database exception", "Database query should be successful", "Database query unsuccessful and zipcode value is "+zipcode);
			}
			uiDriver.click("btngoBtn");
			uiDriver.waitForPageLoad();
			
			try{
				(new WebDriverWait(webDriver,20)).until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(objMap.getLocator("btngoBtn"))));
			}catch(Exception e){
				uiDriver.wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector(objMap.getLocator("btngoBtn"))));
			}
		
			if(webDriver.findElements(By.xpath(objMap.getLocator("btnclosePopupNeedInfo"))).size()>0 && 
					webDriver.findElement(By.xpath(objMap.getLocator("btnclosePopupNeedInfo"))).isDisplayed())
			{
				uiDriver.click("btnclosePopupNeedInfo");
			}			
			if(webDriver.findElements(By.linkText(objMap.getLocator("lnkofficeDeliveryLink"))).size()>0 && 
					webDriver.findElement(By.linkText(objMap.getLocator("lnkofficeDeliveryLink"))).getText().equals("Office delivery?"))
			{
				uiDriver.click("lnkofficeDeliveryLink");
				uiDriver.waitForPageLoad();
				//uiDriver.wait.until(ExpectedConditions.stalenessOf(webDriver.findElement(By.linkText(objMap.getLocator("lnkofficeDeliveryLink")))));			
				if(webDriver.findElements(By.xpath(objMap.getLocator("strapartialCoverage"))).size() > 0 &&  webDriver.findElement(By.xpath(objMap.getLocator("strapartialCoverage"))).getText().contains("We don't currently deliver to all parts of your neighborhood."))
				{
					RESULT.passed("Zip code validation for Partial Coverage Office Delivery", "'We don't currently deliver to all parts of your neighborhood.' Attention message should be displayed for zipcode having Partial Coverage Office Delivery", 
					"'We don't currently deliver to all parts of your neighborhood.' Attention message is displayed for zipcode having Partial Coverage Office Delivery");
				}
				else
				{
					RESULT.failed("Zip code validation for Partial Coverage Office Delivery", "'We don't currently deliver to all parts of your neighborhood.' Attention message should be displayed for zipcode having Partial Coverage Office Delivery", 
					"'We don't currently deliver to all parts of your neighborhood.' Attention message is not displayed for zipcode having Partial Coverage Office Delivery");
				}
			}
			else
			{
				RESULT.warning("Zip code validation for Partial Coverage Office Delivery",
						"Office Delivery link should be available", "Office Delivery link is not available");
			} 
			
			if(webDriver.findElements(By.linkText(objMap.getLocator("lnkhomeDelivery"))).size()>0 &&
					uiDriver.getwebDriverLocator(objMap.getLocator("lnkhomeDelivery")).getText().equals("Home delivery?"))
			{
				uiDriver.click("lnkhomeDelivery");	
				uiDriver.waitForPageLoad();
				//wait.until(ExpectedConditions.stalenessOf(webDriver.findElement(By.linkText(objMap.getLocator("lnkhomeDelivery")))));
				if(webDriver.findElements(By.xpath(objMap.getLocator("strapartialCoverage"))).size()>0 && webDriver.findElement(By.xpath(objMap.getLocator("strapartialCoverage"))).getText().contains("We don't currently deliver to all parts of your neighborhood."))
				{
					RESULT.passed("Zip code validation for Partial Coverage Home Delivery", "'We don't currently deliver to all parts of your neighborhood.' Attention message should be displayed for zipcode having Partial Coverage Home Delivery", 
					"'We don't currently deliver to all parts of your neighborhood.' Attention message is displayed for zipcode having Partial Coverage Home Delivery");
				}
				else
				{
					RESULT.failed("Zip code validation for Partial Coverage Home Delivery", "'We don't currently deliver to all parts of your neighborhood.' Attention message should be displayed for zipcode having Partial Coverage Home Delivery", 
					"'We don't currently deliver to all parts of your neighborhood.' Attention message is not displayed for zipcode having Partial Coverage Home Delivery");
				}
			}
			else
			{
				RESULT.warning("Zip code validation for No Coverage Home Delivery",
						"Home Delivery link should be available", "Home Delivery link is not available");
			}
			
			// Verify Zipcode for Full Coverage
			try{
			List<Map> list2 = connect.runQueryOr("devint","select ZIPCODE from DLV.ZIPCODE where COS_COVERAGE>=0.9 and HOME_COVERAGE>=0.9");
			String zipcode2=list2.get((int) Math.round(Math.ceil(Math.random()*(list2.size()-1)))).values().toString();
			webDriver.findElement(By.id(objMap.getLocator("strnewZipCode"))).sendKeys(zipcode2);
			}
			catch(Exception e)
			{
				RESULT.failed("Zip code : Database exception", "Database query should be successful", "Database query unsuccessful and zipcode value is "+zipcode);
			}
			uiDriver.click("btngoBtn");
			uiDriver.waitForPageLoad();

			try{
				(new WebDriverWait(webDriver,20)).until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(objMap.getLocator("btngoBtn"))));
			}catch(Exception e){
				uiDriver.wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector(objMap.getLocator("btngoBtn"))));
			}
			
			if(webDriver.findElements(By.linkText(objMap.getLocator("lnkofficeDeliveryLink"))).size()>0 && 
					webDriver.findElement(By.linkText(objMap.getLocator("lnkofficeDeliveryLink"))).getText().equals("Office delivery?"))
			{
				uiDriver.click("lnkofficeDeliveryLink");
				uiDriver.waitForPageLoad();
				//wait.until(ExpectedConditions.stalenessOf(webDriver.findElement(By.linkText(objMap.getLocator("lnkofficeDeliveryLink")))));						
				if(webDriver.findElements(By.xpath(objMap.getLocator("strfullCoverage"))).size() > 0)
				{
					RESULT.failed("Zip code validation for Full Coverage Office Delivery", "No warning message should be displayed for zipcode having Full Coverage Office Delivery", 
					"Warning message should not be displayed for zipcode having Full Coverage Office Delivery");				
				}
				else
				{
					RESULT.passed("Zip code validation for Full Coverage Office Delivery", "No warning message should be displayed for zipcode having Full Coverage Office Delivery", 
					"Zipcode is valid for Full Coverage Office Delivery");
				}
			}			
			else
			{
				RESULT.warning("Zip code validation for Full Coverage Office Delivery",
						"Office Delivery link should be available", "Office Delivery link is not available");
			}
			
			if(webDriver.findElements(By.linkText(objMap.getLocator("lnkhomeDelivery"))).size()>0 &&
					uiDriver.getwebDriverLocator(objMap.getLocator("lnkhomeDelivery")).getText().equals("Home delivery?"))
			{
				uiDriver.click("lnkhomeDelivery");	
				uiDriver.waitForPageLoad();
				//wait.until(ExpectedConditions.stalenessOf(webDriver.findElement(By.linkText(objMap.getLocator("lnkhomeDelivery")))));
				if(webDriver.findElements(By.xpath(objMap.getLocator("strfullCoverage"))).size() > 0)
				{
					RESULT.failed("Zip code validation for Full Coverage Home Delivery", "No warning message should be displayed for zipcode having Full Coverage Home Delivery", 
					"Warning message should not be displayed for zipcode having Full Coverage Home Delivery");				
				}
				else
				{
					RESULT.passed("Zip code validation for Full Coverage Home Delivery", "No warning message should be displayed for zipcode having Full Coverage Home Delivery", 
					"Zipcode is valid for Full Coverage Home Delivery");
				}
			}
			else
			{
				RESULT.warning("Zip code validation for No Coverage Home Delivery",
						"Home Delivery link should be available", "Home Delivery link is not available");
			}
			connect.disconnect();			
		}
		catch (Exception e) {
			RESULT.failed("Verify Zip Code", "Zip Code checks should be successful", "Zip code checks failed");
		}
	}	
	

	/* Verify 'SIGN UP' button displayed on page header.*/		

	public void SignUpButton(DataRow input, DataRow output) throws InterruptedException, FindFailed
	{
		try
		{
			if(webDriver.findElement(By.className(objMap.getLocator("btnsignupBtn"))).isDisplayed())
			{
				RESULT.passed("Verify SIGN UP button on the page header", "SIGN UP button should be displayed",
				"SIGN UP button is displayed" );
				
				uiDriver.click("btnsignupBtn");
				
				SleepUtils.getInstance().sleep(TimeSlab.MEDIUM);
				try
				{
					webDriver.switchTo().frame(uiDriver.getwebDriverLocator(objMap.getLocator("frameiframe")));
				}
				catch(Exception e)
				{
					RESULT.failed("Sign up Button ", "Sign up frame should be available", "Sign up frame is not available");
					return;
				}
				SleepUtils.getInstance().sleep(TimeSlab.MEDIUM);
				try
				{
					String SignUpClick = webDriver.findElement(By.className(objMap.getLocator("strchkFrame"))).getText();
					if(SignUpClick.equals("Sign up now to receive promotional materials or place your first order."))
					{
						RESULT.passed("Verify SIGN UP button on the page header", "SIGN UP, IT'S EASY pop up should be displayed.",
						"SIGN UP, IT'S EASY pop up is displayed." );
					}
					else
					{
						RESULT.failed("Verify SIGN UP button on the page header", "SIGN UP, IT'S EASY pop up should be displayed.",
						"SIGN UP, IT'S EASY pop up is not displayed." );
						return;
					}
				}
				catch(Exception e)
				{
					RESULT.failed("Sign Up Button", "Sign up now to receive promotional materials or place your first order text verification should be successful",
							"Text verification unsuccessful");
				}
				
				webDriver.switchTo().defaultContent();
				SleepUtils.getInstance().sleep(TimeSlab.YIELD);
				//uiDriver.click("btnsignupClose");
				webDriver.findElement(By.xpath(objMap.getLocator("btnsignupClose"))).click();
				SleepUtils.getInstance().sleep(TimeSlab.YIELD);
			}
			else
			{
				RESULT.failed("Verify SIGN UP button on the page header", "SIGN UP button should be displayed",
				"SIGN UP button is not displayed" );
			}	
		}
		catch(Exception e)
		{
			
			RESULT.failed("SIGN UP button Verification", "SIGN UP button should be displayed.",
			"SIGN UP button verification failed due to "+e.getMessage() );
		}		
	}	


	/* Verify 'LOG IN' button displayed on page header.*/		

	public void LoginButton(DataRow input, DataRow output) throws InterruptedException, FindFailed
	{
		try
		{			
			uiDriver.wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.className(objMap.getLocator("btnlogin"))));
			if(uiDriver.getwebDriverLocator(objMap.getLocator("btnlogin")).isDisplayed())
			{
				RESULT.passed("Verify Login button and its functionality",
						"LOG IN button should be displayed",
						"LOG IN button is displayed" );
				uiDriver.FD_login(input.get("userID"), input.get("password"));
				
				try
				{
					uiDriver.wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.className(objMap.getLocator("btnlogout"))));
				}
				catch(TimeoutException e)
				{
					RESULT.failed("Header Login ", "Logout button should be available", "Log out button is not available after waiting for 3 minutes");
					return;
				}
				
				if(uiDriver.isDisplayed("btnlogout"))
				{
					RESULT.passed("Verify Login button and its functionality", "Welcome message with <Username> should be displayed on FD Home page.",
					"Welcome message with <Username> is displayed on FD Home page." );
				}
				else
				{
					RESULT.failed("Verify Login button and its functionality", "Welcome message with <Username> should be displayed on FD Home page.",
					"Welcome message with <Username> is not displayed on FD Home page." );
				}
				
			}
			else
			{
				RESULT.failed("Verify Login button and its functionality", "LOG IN button should be displayed on right side of page header.",
				"LOG IN button is not displayed on right side of page header." );
			}				

		}
		catch(Exception e)
		{
			RESULT.failed("Login button verification", "Login button should be displayed",
			"Login button verification failed due to "+e.getMessage());
		}	
	}

	/* Verify 'Fresh Direct' logo displayed on web page header.*/

	public void FreshDirectLogo(DataRow input, DataRow output) throws InterruptedException, FindFailed
	{
		
		try
		{
			if(webDriver.findElements(By.name(objMap.getLocator("imgfd_Logo"))).size()>0 && 
					webDriver.findElement(By.name(objMap.getLocator("imgfd_Logo"))).isDisplayed())
			{
				RESULT.passed("Verify 'Fresh Direct' logo displayed on web page header.", "Fresh Direct' logo should be displayed on web page header.",
				"Fresh Direct' logo is displayed on web page header.");										
			}
			else
			{
				RESULT.failed("Verify 'Fresh Direct' logo displayed on web page header.", "Fresh Direct' logo should be displayed on web page header.",
				"Fresh Direct' logo is not displayed on web page header.");
			}	
			if(webDriver.findElement(By.id(objMap.getLocator("txtsearchBox"))).isDisplayed())
				if(webDriver.findElements(By.id(objMap.getLocator("btngo"))).size()>0 && 
						webDriver.findElement(By.id(objMap.getLocator("btngo"))).isEnabled())					
				{
					uiDriver.setValue("txtsearchBox", "Chocolates");
					uiDriver.click("btngo");
					uiDriver.waitForPageLoad();
					try
					{
						uiDriver.wait.until(ExpectedConditions
								.presenceOfElementLocated(By.xpath(objMap
										.getLocator("strverifyNoSearchResult"))));
					}
					catch(TimeoutException e)
					{
						RESULT.failed("Search Filed validation", "Search string should be available", 
								"Search string is not available");
					}
					
					RESULT.passed("Verify search field on web page header", "Search' field with 'Go' button should be displayed on web page header.",
					"Search' field with 'Go' button is displayed on web page header.");							
				}
				else
				{
					RESULT.failed("Verify search field on web page header", "Search' field with 'Go' button should be displayed on web page header.",
					"Search' field with 'Go' button is not displayed on web page header.");
				}
			//SleepUtils.getInstance().sleep(TimeSlab.HIGH);
			try
			{
				uiDriver.click("imgfd_Logo");
				uiDriver.waitForPageLoad();
				//uiDriver.wait.until(ExpectedConditions.presenceOfElementLocated(By.className(objMap.getLocator("btnlogin"))));
			}
			catch(Exception e)
			{
				RESULT.failed("FreshDirectLogo", "Logo click should be successful",
						"Logo click is unsuccessful");
			}
			if(webDriver.getCurrentUrl().contains("index.jsp")){
			//if (uiDriver.isDisplayed("btnlogin")) {
				RESULT.passed("Verify 'Fresh Direct' logo displayed on web page header.",
						"Fresh Direct homepage should be displayed.", 
				"Fresh Direct homepage is displayed.");
			} else {
				RESULT.failed("Verify 'Fresh Direct' logo displayed on web page header.",
						"Fresh Direct homepage should be displayed.", 
				"FreshDirect homepage is not displayed");
			}
		}
		catch(Exception e)
		{
			
			RESULT.failed("'Fresh Direct' logo verification",
					"Fresh Direct logo should be displayed.", 
					"'Fresh Direct' logo verification failed and the exception caught is "+e.getMessage());
		}
	}


	/* Verify search field on web page header.*/		

	public void SearchField(DataRow input, DataRow output) throws InterruptedException, FindFailed
	{
		try
		{
			try
			{
				uiDriver.wait.until(ExpectedConditions.presenceOfElementLocated(By.id(objMap.getLocator("txtsearchBox"))));
			}
			catch(TimeoutException e)
			{
				RESULT.failed("Search box ", "Search box should be available", "Search box is not available");
				return;
			}
			if(webDriver.findElement(By.id(objMap.getLocator("txtsearchBox"))).isDisplayed())
				if(webDriver.findElements(By.id(objMap.getLocator("btngo"))).size()>0 && 
						webDriver.findElement(By.id(objMap.getLocator("btngo"))).isEnabled())					
				{
					uiDriver.setValue("txtsearchBox", input.get("SearchItem"));
					uiDriver.click("btngo");
					try
					{
						uiDriver.wait.until(ExpectedConditions
								.presenceOfElementLocated(By.xpath(objMap
										.getLocator("strverifyNoSearchResult"))));
					}
					catch(TimeoutException e)
					{
						RESULT.failed("Searcg Filed validation", "Search string should be available", 
								"Search string is not available");
					}
					
					RESULT.passed("Verify search field on web page header", "Search' field with 'Go' button should be displayed on web page header.",
					"Search' field with 'Go' button is displayed on web page header.");							
					//uiDriver.click("imgfd_Logo");
				}
				else
				{
					RESULT.failed("Verify search field on web page header", "Search' field with 'Go' button should be displayed on web page header.",
					"Search' field with 'Go' button is not displayed on web page header.");
				}
			else
			{
				RESULT.failed("Verify search field on web page header", "Search' field should be displayed on web page header.",
				"Search' field is not displayed on web page header.");
			}
		}
		catch(Exception e)
		{
			
			RESULT.failed("Search field Exception", "Search' field with 'Go' button should be displayed.",
			"Search field validation failed and exception caught is "+e.getMessage());
		}
	}		


	/* Verify 'Your Account' link in web page header when logged-in customer clicks on the link */

	public void YourAcc_Loggedin(DataRow input, DataRow output) throws InterruptedException, FindFailed
	{
		try
		{
			//uiDriver.FD_login(input.get("userID"), input.get("password"));
			try
			{
				uiDriver.wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.linkText(objMap.getLocator("lnkyourAccountLink"))));
			}
			catch(TimeoutException e)
			{
				RESULT.failed("YourAcc_Loggedin", "Your Account link should be available",
						"Your account link is not available");
				return;
			}
			//String YourAcctLink = webDriver.findElement(By.id(objMap.getLocator("YourAccountLink"))).getText();
			if(webDriver.findElement(By.linkText(objMap.getLocator("lnkyourAccountLink"))).isDisplayed())
			{
				RESULT.passed("Verify 'Your Account' link in web page header when logged-in customer clicks on the link", 
						"Your Account' link should be displayed on web page header.",
						"Your Account' link gets displayed on web page header.");		
				//WebUIDriver.selenium.waitForPageToLoad(PageLoadTime);
				SleepUtils.getInstance().sleep(TimeSlab.YIELD);
				uiDriver.click("lnkyourAccountLink");
				uiDriver.waitForPageLoad();
				uiDriver.wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(objMap.getLocator("stryourAcctPage"))));
				//if(webDriver.findElement(By.id(objMap.getLocator("YourAcctPage"))).getText().contains("Welcome to Your Account"))
				if(uiDriver.verifyMsg("stryourAcctPage", "Welcome to Your Account"))
				{
					RESULT.passed("Verify 'Your Account' link in web page header when logged-in customer clicks on the link", 
							"Welcome to Your account' page should be displayed.",
							"Welcome to Your account' page gets displayed.");	
				}
				else
				{
					RESULT.failed("Verify 'Your Account' link in web page header when logged-in customer clicks on the link", 
							"Welcome to Your account' page should be displayed.",
							"Welcome to Your account' page failed to open.");
				}
				uiDriver.click("imgfd_Logo");
				uiDriver.waitForPageLoad();
			}					
			else
			{
				RESULT.failed("Verify 'Your Account' link in web page header when logged-in customer clicks on the link", 
						"Your Account' link should be displayed on web page header.",
				"Unable to find 'Your Account' link on web page header.");		
			}								
		}
		catch(Exception e)
		{
			RESULT.failed("YourAcc_Loggedin Exception", 
					"Your Account' link should be displayed as user is logged in",
			"Link 'Your Account' verification failed and exception caught is "+e.getMessage());
		}
	}	


	/* Verify 'Your Account' link in web page header when anonymous customer clicks on the link */

	public void YourAcc_Anonymous(DataRow input, DataRow output) throws InterruptedException, FindFailed
	{
		try
		{
			try
			{
				uiDriver.wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.linkText(objMap.getLocator("lnkyourAccountLink"))));
			}
			catch(TimeoutException e)
			{
				RESULT.failed("YourAcc_Anonymous","Your Account link should be available",
						"Your Account link is not available");
				return;
			}
			
			if(webDriver.findElement(By.linkText(objMap.getLocator("lnkyourAccountLink"))).isDisplayed())
			{
				RESULT.passed("Verify 'Your Account' link in web page header when anonymous customer clicks on the link", 
						"Your Account' link should be displayed on web page header.",
				"Your Account' link gets displayed on web page header.");					
				uiDriver.click("lnkyourAccountLink");	
				uiDriver.waitForPageLoad();
				try
				{
					uiDriver.waitForPageLoad();
					uiDriver.wait.until(ExpectedConditions.presenceOfElementLocated(By.name(objMap.getLocator("btnloginGoBtn"))));
				}
				catch(TimeoutException e)
				{
					RESULT.failed("YourAcc_Anonymous", "Anonymous page should be available", "Anonymous page is not available");
				}
				
				if(webDriver.findElements(By.xpath(objMap.getLocator("btngoAnonymous"))).size() > 0)
				{
					RESULT.passed("Verify 'Your Account' link in web page header when anonymous customer clicks on the link", 
							"User should be redirected to Log In page",
							"User is redirected to Log In page");
					try
					{
						uiDriver.setValue("txtemailAddress", input.get("userID"));
						uiDriver.setValue("txtpass", input.get("password"));
						uiDriver.click("btnloginGoBtn");
						uiDriver.waitForPageLoad();
						
					}
					catch(Exception e)
					{
						RESULT.failed("YourAcc_Anonymous", "Anonymous login should be logged in",
								"Anonymous login is not successful");
						return;
					}
					uiDriver.waitForPageLoad();
					//SleepUtils.getInstance().sleep(TimeSlab.YIELD);
					try
					{
						uiDriver.wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(objMap.getLocator("stryourAcctPage"))));
						if(webDriver.findElement(By.xpath(objMap.getLocator("stryourAcctPage"))).getText().contains("Welcome to Your Account"))
						{
							RESULT.passed("Verify 'Your Account' link in web page header when anonymous customer clicks on the link", 
									"User should be redirected to Your Account page",
									"User is redirected to Your Account page");	
						}
						else
						{
							RESULT.failed("Verify 'Your Account' link in web page header when anonymous customer clicks on the link", 
									"User should be redirected to Your Account page",
									"Unable to navigate to Your Account page");	
						}
					}
					catch(Exception e)
					{
						RESULT.failed("YourAcc_Anonymous", "Welcome to Your Account text should be available", "Welcome to Your Account text is not available");
					}
									
				}
				else
				{
					RESULT.failed("Verify 'Your Account' link in web page header when anonymous customer clicks on the link", 
							"User should be redirected to Log In page",
							"Unable to redirect user to Log In page");
				}	
				try
				{
					uiDriver.click("btnlogout");
					uiDriver.wait.until(ExpectedConditions.presenceOfElementLocated(By.className(objMap.getLocator("btnlogin"))));
				}
				catch(Exception e)
				{
					RESULT.failed("YourAcc_Anonymous ", "Log out should be successful", "Log out is unsuccessful");
				}
			}					
			else
			{
				RESULT.failed("Verify 'Your Account' link in web page header when anonymous customer clicks on the link", 
						"Your Account' link should be displayed on web page header.",
						"Unable to find 'Your Account' link on web page header.");		
			}										
		}
		catch(Exception e)
		{
			
			RESULT.failed("Verify 'Your Account' link for anonymous customer", 
					"Your Account' link should be displayed",
					"Verification for 'Your Account' link for anonymous customer failed and exception caught is "+e.getMessage());
		}
	}	


	/* Verify 'LOGOUT' button displayed on page header after user login. */

	public void LogoutButton(DataRow input, DataRow output) throws InterruptedException, FindFailed
	{
		try
		{			
			try
			{
				uiDriver.wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.className(objMap.getLocator("btnlogout"))));
			}
			catch(TimeoutException me)
			{
				RESULT.failed("LogoutButton", "Log out button should be available", "Log out button is not available");
			}
			
			//SleepUtils.getInstance().sleep(TimeSlab.YIELD);
			if(webDriver.findElement(By.className(objMap.getLocator("btnlogout"))).isDisplayed())
			{
				RESULT.passed("Verify 'LOGOUT' button displayed on page header after user login.", 
						"LOGOUT' button should be displayed on page header.",
						"LOGOUT' button gets displayed on page header.");		
				
				uiDriver.click("btnlogout");
				uiDriver.waitForPageLoad();
				try
				{
					uiDriver.wait.until(ExpectedConditions.visibilityOfElementLocated(By.className(objMap.getLocator("btnlogin"))));
				}
				catch(TimeoutException e)
				{
					RESULT.failed("LogoutButton Component","Log in  button should be available","Log in button is not available");
					return;
				}
				if(webDriver.findElement(By.className(objMap.getLocator("btnlogin"))).isDisplayed())
				{
					RESULT.passed("Verify 'LOGOUT' button displayed on page header after user login.", 
							"FD Storefront home page with 'LOG IN' button on page header should be displayed.",
					"FD Storefront home page with 'LOG IN' button on page header gets displayed.");		
				}

				else
				{
					RESULT.failed("Verify 'LOGOUT' button displayed on page header after user login.", 
							"FD Storefront home page with 'LOG IN' button on page header should be displayed.",
					"Error occured somewhere.");
				}
			}
			else
			{
				RESULT.failed("Verify 'LOGOUT' button displayed on page header after user login.", 
						"LOGOUT' button should be displayed on page header.",
				"LOGOUT' button is not displayed on page header.");	
			}								
		}
		catch(Exception e)
		{
			
			RESULT.failed("'LOGOUT' button verification", 
					"LOGOUT' button should be displayed",
			"'LOGOUT' button verification failed and the exception caught is "+e.getMessage());	
		}
	}	


	/* Verify 'Reorder' link displayed on page header and adding product to cart for an anonymous customer. */

	public void Reorder_Anonymous(DataRow input, DataRow output) throws InterruptedException, FindFailed
	{
		try
		{
			try
			{
				uiDriver.wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.id(objMap.getLocator("lnkreorderLink"))));
			}
			catch(TimeoutException e)
			{
				RESULT.failed("Reorder_Anonymous","Reorder link should be available",
						"Reorder link is not available");
				return;
			}
			if(webDriver.findElement(By.id(objMap.getLocator("lnkreorderLink"))).isDisplayed())
			{
				RESULT.passed("Verify 'Reorder' link displayed on page header and adding product to cart for an anonymous customer", 
						"'Reorder' link should be displayed on page header.",
				"Reorder' link gets displayed on page header.");		
				uiDriver.click("lnkreorderLink");
				uiDriver.waitForPageLoad();
				try
				{
					uiDriver.wait.until(ExpectedConditions.presenceOfElementLocated(By.name(objMap.getLocator("btnloginGoBtn"))));
				}
				catch(TimeoutException e)
				{
					RESULT.failed("Reorder_Anonymous","Anonymous page should be available",
					"Anonymous page is not available");
					return;
				}
				try
				{
					uiDriver.setValue("txtemailAddress", input.get("userID"));
					uiDriver.setValue("txtpass", input.get("password"));
					uiDriver.click("btnloginGoBtn");
					uiDriver.waitForPageLoad();
				}
				catch(Exception e)
				{
					RESULT.failed("Reorder_Anonymous","Anonymous page login should be successful",
					"Anonymous page login is not successful");
				}
				try
				{
					uiDriver.wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(objMap.getLocator("strreorder"))));
				}
				catch(TimeoutException e)
				{
					RESULT.failed("Reorder_Anonymous","Reorder page should be available",
					"Reorder page is not available");
					return;
				}
				
//				SleepUtils.getInstance().sleep(TimeSlab.YIELD);
				if(webDriver.findElement(By.xpath(objMap.getLocator("strreorder"))).getText().equals("Reorder"))
				{

					RESULT.passed("Verify 'Reorder' link displayed on page header and adding product to cart for an anonymous customer", 
							"User should be redirected to Reorder Page",
					"User is redirected to Reorder Page");		
				}
				else
				{
					RESULT.failed("Verify 'Reorder' link displayed on page header and adding product to cart for an anonymous customer", 
							"User should be redirected to Reorder Page",
					"User is not redirected to Reorder page or the text 'Reorder' has changed ");
				}				
			}
			else
			{
				RESULT.failed("Verify 'Reorder' link displayed on page header and adding product to cart for an anonymous customer", 
						"Reorder' link should be displayed on page header.",
				"Reorder' link is not displayed on page header.");	
			}	
			try
			{
				uiDriver.click("btnlogout");
				uiDriver.waitForPageLoad();
				uiDriver.wait.until(ExpectedConditions.presenceOfElementLocated(By.className(objMap.getLocator("btnlogin"))));
			}
			catch(Exception e)
			{
				RESULT.failed("Reorder_Anonymous", "User should be logged out", "User is not logged out");
			}
			
		}
		catch(Exception e)
		{
			
			RESULT.failed("Verification of 'Reorder' link for an anonymous user", 
					"'Reorder' link should be displayed",
			"Verification of 'Reorder' link for an anonymous user failed and exception caught is "+e.getMessage());
		}
	}	


	/* Verify 'Reorder' link displayed on page header and adding product to cart. */

	public void Reorder_Loggedin(DataRow input, DataRow output) throws InterruptedException, FindFailed
	{
		try
		{				
			try
			{
				uiDriver.wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.id(objMap.getLocator("lnkreorderLink"))));
			}
			catch(TimeoutException e)
			{
				RESULT.failed("Reorder_Loggedin","Reorder link should be available",
						"Reorder link is not available");
				return;
			}
			
			SleepUtils.getInstance().sleep(TimeSlab.YIELD);
			if(webDriver.findElement(By.id(objMap.getLocator("lnkreorderLink"))).isDisplayed())
			{
				RESULT.passed("Verify 'Reorder' link displayed on page header and adding product to cart for an anonymous customer", 
						"Reorder' link should be displayed on page header.",
				"Reorder' link gets displayed on page header.");		
				uiDriver.click("lnkreorderLink");     // Click on 'Reorder' link
				uiDriver.waitForPageLoad();
				//SleepUtils.getInstance().sleep(TimeSlab.YIELD);
				//SleepUtils.getInstance().sleep(TimeSlab.MEDIUM);
				try
				{
					uiDriver.wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(objMap.getLocator("strreorder"))));
					if(webDriver.findElement(By.xpath(objMap.getLocator("strreorder"))).getText().equals("Reorder"))
					{

						RESULT.passed("Verify 'Reorder' link displayed on page header and adding product to cart for logged in customer", 
								"User should be redirected to Reorder Page",
								"User is redirected to Reorder Page");		
					}
					else
					{
						RESULT.failed("Verify 'Reorder' link displayed on page header and adding product to cart for logged in customer", 
								"User should be redirected to Reorder Page",
								"User is not redirected to reorder page or the text 'Reorder' might have changed");
					}
				}
				catch(Exception e)
				{
					RESULT.failed("Reorder_Loggedin","Reorder page should be available","Reorder page is not available");
				}
				
				uiDriver.click("imgfd_Logo");
				uiDriver.waitForPageLoad();
			}	
			else
			{
				RESULT.failed("Verify 'Reorder' link displayed on page header and adding product to cart for logged in customer", 
						"Reorder' link should be displayed on page header.",
						"Reorder' link is not displayed on page header.");	
			}								
		}
		catch(Exception e)
		{
			
			RESULT.failed("Verification of 'Reorder' link for an loggedin user", 
					"'Reorder' link should be displayed",
					"Verification of 'Reorder' link for an loggedin user failed and exception caught is "+e.getMessage());
		}
	}	


	/* Verify 'Delivery Info' link displayed on page header. */

	public void DeliveryInfo(DataRow input, DataRow output) throws InterruptedException, FindFailed
	{
		try
		{
			try
			{
				uiDriver.wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.linkText(objMap.getLocator("lnkdeliveryInfoLink"))));
			}
			catch(TimeoutException e)
			{
				RESULT.failed("DeliveryInfo","DeliveryInfo link should be available",
						"DeliveryInfo link is not available");
				return;
			}
			
			if(webDriver.findElements(By.linkText(objMap.getLocator("lnkdeliveryInfoLink"))).size()>0 
					&& webDriver.findElement(By.linkText(objMap.getLocator("lnkdeliveryInfoLink"))).isDisplayed())
			{			
				uiDriver.click("lnkdeliveryInfoLink"); 
				uiDriver.waitForPageLoad();
				SleepUtils.getInstance().sleep(TimeSlab.YIELD);
				//click on Delivery Info Link
//				SleepUtils.getInstance().sleep(TimeSlab.HIGH);
				try
				{
					if(uiDriver.isDisplayed("strdeliveryPickupInfo"))
					{
						RESULT.passed("Verify 'Delivery Info' link displayed on page header.", 
								"'Delivery and Pickup info' page should be displayed.",
						"'Delivery and Pickup info' page gets displayed.");		
					}
					else
					{
						RESULT.failed("Verify 'Delivery Info' link displayed on page header.", 
								"'Delivery and Pickup info' page should be displayed.",
						"'Delivery and Pickup info' page is not displayed.");	
					}
				}
				catch(Exception e)
				{
					RESULT.failed("DeliveryInfo", "Delivery info string should be visible ",
							"Delivery info string is not visible ");
				}
							
			}				
			else
			{
				RESULT.failed("Verify 'Delivery Info' link displayed on page header.", 
						"'Delivery Info'link should be displayed.",
				"'Delivery Info'link page is not displayed.");	
			}
			uiDriver.click("imgfd_Logo");	
			uiDriver.waitForPageLoad();
		}

		catch(Exception e)
		{
			e.printStackTrace();
			RESULT.failed("Verification of 'Delivery Info' link", 
					"'Delivery Info'link should be displayed.",
			"Verification of 'Delivery Info' link failed and the exception caught is "+e.getMessage());
		}
	}


	/* Verify 'Help' link displayed on page header. */

	public void HelpLink(DataRow input, DataRow output) throws InterruptedException, FindFailed
	{
		try
		{
			try
			{
				uiDriver.wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.linkText(objMap.getLocator("lnkhelpLink"))));
			}
			
			catch(TimeoutException e)
			{
				RESULT.failed("Help Link","Help link should be available in header",
						"Help link is not available in header");
				return;
			}
			uiDriver.click("lnkhelpLink");
			uiDriver.waitForPageLoad();
			//SleepUtils.getInstance().sleep(TimeSlab.YIELD);
			try
			{
				uiDriver.wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.className(objMap.getLocator("strhelpPage"))));
			}
			catch(TimeoutException e)
			{
				RESULT.failed("Navigation to Help Page","Navigation to Help Page should be successful",
						"Navigation to Help Page unsuccessful after waiting for 3 minutes");
				return;
			}
			
			String WelcomePage;
			String imgFAQ;
			String imgContact;
			try
			{
				WelcomePage = webDriver.findElement(By.className(objMap.getLocator("strhelpPage"))).getText();
				imgFAQ = webDriver.findElement(By.xpath(objMap.getLocator("lnkFAQLink"))).findElement(By.tagName("img")).getAttribute("alt");
				imgContact = webDriver.findElement(By.xpath(objMap.getLocator("lnkcontactUsLink"))).findElement(By.tagName("img")).getAttribute("alt");
			}
			catch(Exception e)
			{
				RESULT.failed("Help Link", "Help page,FAQ link and Contact us link should be available", 
						"Help page or FAQ link or Contact us link is not available");
				return;
			}

			if(WelcomePage.contains("Welcome to Help") && imgFAQ.contains("FAQs") && imgContact.contains("CONTACT US") )
			{
				RESULT.passed("Verify 'Help' link displayed on page header.", 
						"Welcome to Help' page with FAQs and 'Contact Fresh Direct' section should be displayed.",
				"Welcome to Help' page with FAQs and 'Contact Fresh Direct' section gets displayed.");
			}
			else
			{
				RESULT.failed("Verify 'Help' link displayed on page header.", 
						"Welcome to Help' page with FAQs and 'Contact Fresh Direct' section should be displayed.",
				"Welcome to Help' page with FAQs and 'Contact Fresh Direct' section is not displayed.");
			}
			uiDriver.click("imgfd_Logo");
			uiDriver.waitForPageLoad();
		}
		catch(Exception e)
		{
			
			RESULT.failed("Verification of 'Help' link", 
					"Welcome to Help' page with FAQs and 'Contact Fresh Direct' section should be displayed.",
					"Verification of 'Help' link failed and exception caught is "+e.getMessage());
		}
	}


	/* Verify 'Your cart' button displayed on page header.*/

	public void YourCartButton(DataRow input, DataRow output) throws InterruptedException, FindFailed
	{
		try
		{
			try
			{
				uiDriver.wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(objMap.getLocator("btnyourCart"))));
			}
			catch(TimeoutException e)
			{
				RESULT.failed("Your Cart Button","Your Cart Button should be available",
						"Your Cart Button is not available");
				return;
			}
			if(uiDriver.isDisplayed("btnyourCart"))
			{
				RESULT.passed("Verify 'Your cart' button displayed on page header.", 
						"'Your cart' button should be displayed on page header.",
				"'Your cart' button gets displayed on page header.");
				uiDriver.FD_viewCart();
			}
			else
			{
				RESULT.failed("Verify 'Your cart' button displayed on page header.", 
						"'Your cart' button should be displayed on page header.",
				"'Your cart' button is not displayed on page header.");
			}
			uiDriver.click("imgfd_Logo");
			uiDriver.waitForPageLoad();
		}
		catch(Exception e)
		{

			RESULT.failed("Verification of 'Your cart' button", 
					"'Your cart' button should be displayed",
					"Verification of 'Your cart' button failed and the exception caught is "+e.getMessage());
		}
	}



	/* Verify Social buttons displayed on Product Details Page */

	public void SocialButton(DataRow input, DataRow output) throws InterruptedException, FindFailed
	{
		int flag = 0;
		WebElement product = null;
		String productFullName = null;
		try
		{		
			try{
				SleepUtils.getInstance().sleep(TimeSlab.YIELD);
				// paste the product name as it is with formatting from the webpage
				product = webDriver.findElement(By.partialLinkText(input.get("Product")));
				productFullName = product.getText();
			} catch (Exception e) {
				String first_item;
				if (webDriver.findElements(By.xpath(objMap.getLocator("tabyourTopItems"))).size() > 0) {
					first_item = objMap.getLocator("lnkfirstItemReorder");
				} else {
					first_item = objMap.getLocator("lnkfirstItem");
				}
				// to handle the pagination
				try {
					if (webDriver.findElements(By.xpath(objMap.getLocator("btnshowAll"))).size() > 0) {
						// click on the show all button
						uiDriver.getwebDriverLocator(objMap.getLocator("btnshowAll")).click();
						// wait for the products to load
						uiDriver.wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(objMap.getLocator("btnpagePrev"))));
					}
					product = webDriver.findElement(By.partialLinkText(input.get("Product")));
					productFullName = product.getText();
				} catch (Exception e1) {
					if (input.get("FlexibilityFlag").equalsIgnoreCase("Yes")) {
						product = uiDriver.getwebDriverLocator(first_item);
						productFullName = product.getText();
						RESULT.warning("Item unavalibality",
								"Item: " + input.get("Product")+ " should be available",
								"Item: "+ productFullName + " is added to cart ");
					} else if (input.get("FlexibilityFlag").equalsIgnoreCase("No")) {
						RESULT.failed("Item unavalibality",
								"Item: " + input.get("Product")+ " should be available ",
								"Item: " + input.get("Product")+ " is not be available ");
					}
				}
			}
			try {
				JavascriptExecutor jse = (JavascriptExecutor) webDriver;				
				jse.executeScript("arguments[0].scrollIntoView()", product);
				SleepUtils.getInstance().sleep(TimeSlab.YIELD);
				uiDriver.robot.moveToElement(product);
				jse.executeScript("arguments[0].click();", product);
				if (webDriver.findElements(By.className(objMap.getLocator("alertAlcoholClick"))).size() > 0) {
					uiDriver.getwebDriverLocator(objMap.getLocator("btnalcoholAlertAccept")).click();
					RESULT.passed("Alcohol alert ",
							"Alcohol alert accepted",
					"Alcohol alert passed");
				}
			}catch (Exception e) {
				RESULT.warning("Social Buttons check","Product should be clicked and alcohol alert should be accepted if it is alcohol product",
						"Produch is not clicked either or alcohol alert is not accepted");
			}
			//SleepUtils.getInstance().sleep(TimeSlab.YIELD);
			uiDriver.waitForPageLoad(); 
			try
			{
				if (webDriver.findElements(By.xpath(objMap.getLocator("diaavailability"))).size() > 0) {								
					RESULT.warning("Out of stock product",
							"Product is Out of stock",
							"Message: "+ uiDriver.getwebDriverLocator(objMap.getLocator("strprodUnavailability")).getText());
					uiDriver.click("btnback");
					uiDriver.waitForPageLoad();

					if (input.get("FlexibilityFlag").equalsIgnoreCase("Yes")) {
						product = uiDriver.getwebDriverLocator(objMap.getLocator("lnkfirstItem"));
						productFullName = product.getText();
						product.click();
						RESULT.passed("Specified item out of stock",
								"Item: "+ input.get("Product")+ " is out of stock so add first available item",
								"Item: " + product.getText()+ " is added to cart ");
					} else if (input.get("FlexibilityFlag").equalsIgnoreCase("No")) {
						RESULT.failed("Item out of stock",
								"Item: "+ input.get("Product")+ " is out of stock so add first available item",
						"User is not flexible to add first available product");
						return;
					}
				}
			}
			catch(Exception e)
			{
				RESULT.failed("Social Buttons component", "Out of stock product check should be validated", 
						"Out of stock product is not validated");
			}
	
			uiDriver.waitForPageLoad();
			try{
			//	SleepUtils.getInstance().sleep(TimeSlab.YIELD);
				try
				{
					uiDriver.wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(objMap.getLocator("framefacebook"))));
					webDriver.switchTo().frame(webDriver.findElement(By.xpath(objMap.getLocator("framefacebook"))));
				}
				catch(Exception e)
				{
					RESULT.warning("Social Buttons component", "Social button frame should be available",
							"Social button frame is not available");
				}
				
				//check the presence of Facebook button 
				try
				{
					if(uiDriver.isDisplayed("btnfacebook"))
					{
						RESULT.passed("Social button facebook on Prouct Details Page",
								"Social but ton facebook should exist on Product Details Page",
								"Social button facebook exists on Prouct Details Page of "+input.get("Product")+ " item");
					}
					else
					{
						RESULT.failed("Social button facebook on Prouct Details Page",
								"Social button facebook should exist on Product Details Page",
								"Social button facebook does not exist on Prouct Details Page of "+input.get("Product")+ " item");
					}
				}
				catch(Exception e)
				{
					RESULT.warning("Social button ", "Facebook button should be visible",
							"Facebook button is not visible");
				}
				
				webDriver.switchTo().defaultContent();
				try
				{
					webDriver.switchTo().frame(uiDriver.getwebDriverLocator(objMap.getLocator("frametwitter")));
				}
				catch(Exception e)
				{
					RESULT.warning("Social Button", "Twitter frame should be available",
							"Twitter frame is not available");
				}
				//check the presence of Twitter button
				try
				{
					if(uiDriver.isDisplayed("btntwitter"))
					{
						RESULT.passed("Social button twitter on Prouct Details Page",
								"Social button twitter should exist on Product Details Page",
								"Social button twitter exists on Prouct Details Page of "+input.get("Product")+ " item");
					}
					else
					{
						RESULT.failed("Social button twitter on Prouct Details Page",
								"Social button twitter should exist on Product Details Page",
								"Social button twitter does not exist on Prouct Details Page of "+input.get("Product")+ " item");
					}
				}
				catch(Exception e)
				{
					RESULT.warning("Social button ", "Twitter button should be visible",
					"Twitter button is not visible");
				}
			
				webDriver.switchTo().defaultContent();
				try
				{
					webDriver.switchTo().frame(uiDriver.getwebDriverLocator(objMap.getLocator("framegoogle")));
				}
				catch(Exception e)
				{
					RESULT.warning("Social button ", "Google frame should be available",
							"Google frame is not available");
				}
				//check the presence of Google button
				try
				{
					if(uiDriver.isDisplayed("btngoogle"))
					{
						RESULT.passed("Social button Google on Prouct Details Page",
								"Social button Google should exist on Product Details Page",
								"Social button Google exists on Prouct Details Page of "+input.get("Product")+ " item");
					}
					else
					{
						RESULT.failed("Social button Google on Prouct Details Page",
								"Social button Google should facebook exist on Product Details Page",
								"Social button Google does not exist on Prouct Details Page of "+input.get("Product")+ " item");
					}
				}
				catch(ElementNotVisibleException e)
				{
					RESULT.warning("Social button ", "Google button should be visible",
					"Google button is not visible");
				}
			
				webDriver.switchTo().defaultContent();
				try
				{
					webDriver.switchTo().frame(uiDriver.getwebDriverLocator(objMap.getLocator("framepin")));
				}
				catch(Exception e)
				{
					RESULT.failed("Social Button", "Pin button should be available",
							"Pin button is not available");
				}
				
				//check the presence of Pinit button
				try
				{
					if(uiDriver.isDisplayed("btnpinit"))
					{
						RESULT.passed("Social button Pinit on Prouct Details Page",
								"Social button Pinit should exist on Product Details Page",
								"Social button Pinit exists on Prouct Details Page of "+input.get("Product")+ " item");
					}
					else
					{
						RESULT.failed("Social button Pinit on Prouct Details Page",
								"Social button Pinit should exist on Product Details Page",
								"Social button Pinit does not exist on Prouct Details Page of "+input.get("Product")+ " item");
					}
				}
				catch(ElementNotVisibleException e)
				{
					RESULT.warning("Social button ", "Pin it  button should be visible",
					"Pin it button is not visible");
				}
				
			}catch (Exception e) {
				
				RESULT.failed("Verification of social buttons",
						"Social buttons should be present on Product Details Page",
						"Verification of social buttons failed");
			}
		}
		catch(Exception e)
		{
			
			RESULT.failed("Verification of social buttons",
					"Social buttons should be present on Product Details Page",
					"Verification of social buttons failed and the exception caught is "+e.getMessage());
		}
	}


	public void TopNavigation(DataRow input, DataRow output)throws InterruptedException, FindFailed
	{		
		List<WebElement> catList,wbList;
		String brdcrmb;
		WebElement we;
		try{
			try{
				uiDriver.wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//div[@class='top-nav']/ul/li")));
			}catch(Exception e){
				RESULT.failed("Top Navigation", "Top Navigation bar should be available",
				"Top Navigation bar is not available");
				return;
			}

			//Verifying top navigation categories from global navigation bar
			catList=webDriver.findElements(By.xpath("//div[@class='top-nav']/ul/li"));
			String[] categories=new String[catList.size()];
			for(int i=0;i<catList.size();i++){
				categories[i]=catList.get(i).getText();
				//System.out.println("category: "+(i+1)+":"+categories[i]);
			}		
			for(int i=0;i<categories.length;i++){
				try{        	
					//String category=we.getText();
					//System.out.println("category "+(i+1)+":"+categories[i]);
					//webDriver.findElement(By.partialLinkText(categories[i])).click();
					//Configuration GCONFIG = Harness.GCONFIG;

					if(CompositeAppDriver.startUp.equalsIgnoreCase("IE")){
						wbList=webDriver.findElements(By.xpath("//div[@class='top-nav']/ul/li/span"));
						if(wbList.get(i).getText().contains("DELI")||wbList.get(i).getText().contains("BAKERY")||wbList.get(i).getText().contains("WINES")){	    					
							//actions.moveToElement(wbList.get(i)).click().perform();
							uiDriver.robot.moveToElement(wbList.get(i));
							wbList.get(i).click();
							uiDriver.waitForPageLoad();
						}	    				
						else{
							webDriver.findElement(By.linkText(categories[i])).click();
							uiDriver.waitForPageLoad();
						}	    				    	    	
					}else{
						webDriver.findElement(By.linkText(categories[i])).click();
						uiDriver.waitForPageLoad();
						//if (CompositeAppDriver.startUp.equalsIgnoreCase("SAFARI"))
						//	uiDriver.wait.until(ExpectedConditions.refreshed(ExpectedConditions.stalenessOf(webDriver.findElement(By.linkText(categories[i])))));
					}		
					//SleepUtils.getInstance().sleep(TimeSlab.HIGH);
					//uiDriver.waitForPageLoad();
					//SleepUtils.getInstance().sleep(TimeSlab.MEDIUM);
					try{
						if (CompositeAppDriver.startUp.equalsIgnoreCase("SAFARI")) 
							uiDriver.wait.until(ExpectedConditions.refreshed(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@class='browse-breadcrumbs']/ul"))));
						else
							uiDriver.wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@class='browse-breadcrumbs']/ul")));
					}catch(Exception e){
						RESULT.failed("Top Navigation Click Breadcrumb Text", "Breadcrumb Text should be available",
						"Breadcrumb Text is not available");
					}
					//SleepUtils.getInstance().sleep(TimeSlab.MEDIUM);
					we=webDriver.findElement(By.xpath("//div[@class='browse-breadcrumbs']/ul"));
					//WebUIDriver.selenium.waitForPageToLoad(PageLoadTime);				
					//we=(new WebDriverWait(webDriver,240)).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@class='browse-breadcrumbs']/ul")));	
					//SleepUtils.getInstance().sleep(TimeSlab.YIELD);
					brdcrmb=we.getText().replaceAll("\\s", "");        	
					if(brdcrmb.toUpperCase().contains(categories[i].replaceAll("\\s", ""))){
						RESULT.passed("Navigation to "+categories[i],"Breadcrumbs for "+categories[i]+" should be displayed","Breadcrumbs for "+brdcrmb+" is displayed");
					}        	
					else{
						RESULT.failed("Navigation to "+categories[i],"Breadcrumbs for "+categories[i]+" should be displayed","Breadcrumbs for "+brdcrmb+" is NOT displayed");
					}
				}
				catch(Exception e){
					RESULT.failed("TopNavigation check "+categories[i], "Category validation should be successful",
							"Category validation is not successful and the exception caught is "+e.getMessage());				
				}
			}
		}catch(Exception e){
			RESULT.failed("TopNavigation check ", "TopNavigation validation should be successful",
					"TopNavigation validation is not successful and the exception caught is "+e.getMessage());
		}

	}
	public void BottomNavigation(DataRow input, DataRow output)throws InterruptedException, FindFailed
	{
		try{
			List<WebElement> catList;
			String brdcrmb;
			WebElement we;
			
			try{
				uiDriver.wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//div[@class='bottom-nav']/ul/li")));
			}catch(Exception e){
				RESULT.failed("Bottom Navigation", "Bottom Navigation bar should be available",
				"Bottom Navigation bar is not available");
				return;
			}
			
			//Verifying bottom navigation categories
			catList=webDriver.findElements(By.xpath("//div[@class='bottom-nav']/ul/li"));
			String[] categories=new String[catList.size()];
			categories=new String[catList.size()];
			for(int i=0;i<catList.size();i++){
				categories[i]=catList.get(i).getText();
			}
			for(int i=0;i<categories.length;i++){
				try{        	
					//String category=we.getText();
					System.out.println("category "+(i+1)+":"+categories[i]);
					webDriver.findElement(By.partialLinkText(categories[i])).click();
					uiDriver.waitForPageLoad();
					//Thread.sleep(2000);
					//WebUIDriver.selenium.waitForPageToLoad(PageLoadTime);
					if(categories[i].contains("DEALS")||categories[i].contains("TOP-RATED")||categories[i].contains("LOCAL")){
						//SleepUtils.getInstance().sleep(TimeSlab.MEDIUM);
						we=uiDriver.wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@class='browse-breadcrumbs']/ul")));
						brdcrmb=we.getText();        	
						if(brdcrmb.toUpperCase().contains(categories[i])){
							RESULT.passed("Navigation to "+categories[i],"Breadcrumbs for "+categories[i]+" should be displayed","Breadcrumbs for "+brdcrmb+" is displayed");
						}        	
						else{
							RESULT.failed("Navigation to "+categories[i],"Breadcrumbs for "+categories[i]+" should be displayed","Breadcrumbs for "+brdcrmb+" is NOT displayed");
						}
					}
					else if(categories[i].contains("COUPONS")){
						//SleepUtils.getInstance().sleep(TimeSlab.MEDIUM);
						we=uiDriver.wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//img[@alt='FDCoupons']")));
						if(we.isDisplayed()){
							RESULT.passed("Navigation to "+categories[i],"fdcoupons image should be displayed","fdcoupons image is displayed");
						}
						else{
							RESULT.failed("Navigation to "+categories[i],"fdcoupons image should be displayed","fdcoupons image is NOT displayed");
						}
					}
					else if(categories[i].contains("NEW")){
						//SleepUtils.getInstance().sleep(TimeSlab.MEDIUM);
						we=uiDriver.wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//h1[@class='orange-title']")));
						if(we.getText().contains("New Products")){
							RESULT.passed("Navigation to "+categories[i],"'New Products' text should be displayed","'New Products' text is displayed");
						}
						else{
							RESULT.failed("Navigation to "+categories[i],"'New Products' text should be displayed","'New Products' text is NOT displayed");
						}
					}
					else if(categories[i].contains("PRESIDENT'S PICKS")){
						//SleepUtils.getInstance().sleep(TimeSlab.MEDIUM);
						we=uiDriver.wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@class='sorter-element_list']")));
						if(we.getText().contains("Sort")){
							RESULT.passed("Navigation to "+categories[i],"Sort option on PRESIDENT'S PICKS should be displayed","Sort option on PRESIDENT'S PICKS is displayed");
						}
						else{
							RESULT.failed("Navigation to "+categories[i],"Sort option on PRESIDENT'S PICKS should be displayed","Sort option on PRESIDENT'S PICKS is NOT displayed");
						}
					}
					else if(categories[i].contains("IDEAS")){
						//SleepUtils.getInstance().sleep(TimeSlab.MEDIUM);
						//get window handlers as list
						List<String> browserTabs = new ArrayList<String> (webDriver.getWindowHandles());
						System.out.println("tabs size : "+browserTabs.size());	        		
						//switch to new tab
						webDriver.switchTo().window(browserTabs.get(1));
						uiDriver.waitForPageLoad();
						we=uiDriver.wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='title-area']/h1/a")));
						//WebUIDriver.selenium.waitForPageToLoad(PageLoadTime);
						if(webDriver.getCurrentUrl().contains("blog.freshdirect.com")){
							RESULT.passed("Navigation to "+categories[i],"blog.freshdirect.com should be displayed in new tab","blog.freshdirect.com is displayed in new tab");
						}
						else{
							RESULT.failed("Navigation to "+categories[i],"blog.freshdirect.com should be displayed in new tab","blog.freshdirect.com is NOT displayed in new tab");
						}
						//then close tab and get back
						webDriver.close();
						webDriver.switchTo().window(browserTabs.get(0));	        		
					}
					else{
						RESULT.warning("Category -"+categories[i],"Category NOT found","Seems like new category is introduced!!!");
					}
				}
				catch(Exception e){
					RESULT.failed("Bottom Navigation Category check: "+categories[i], "Category should be validated",
							"Category validation failed and the exception caught is "+e.getMessage());					
				}
			}
		}catch(Exception e){
			RESULT.failed("BottomNavigation ", "Bottom navigation check should be validated",
					"Bottom navigation check is not validated and the exception caught is "+e.getMessage());
		}
	}

	public void TopSubItems(DataRow input, DataRow output) throws InterruptedException, FindFailed {

		try 
		{
			try{
				SleepUtils.getInstance().sleep(TimeSlab.YIELD);
				uiDriver.wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//div[@class='top-nav']/ul/li")));
			}catch(Exception e){
				RESULT.failed("Top Navigation", "Top Navigation bar should be available",
				"Top Navigation bar is not available");
				return;
			}
			
			List<WebElement> catList=webDriver.findElements(By.xpath("//div[@class='top-nav']/ul/li"));
			//Counting number of categories which has sub-categories
			int Cat_Size=catList.size();
			//Traversing through each category having sub-category
			for (int i = 0; i < Cat_Size; i++) {

				if (webDriver.findElements(By.xpath("//ul[@class='top-nav-items']/li["+ i +"]//div[@class='subdepartments_cont']//li/span")).size() > 0) {
					//hover on each categories having sub-category

					uiDriver.robot.moveToElement(webDriver.findElement(By.xpath("//ul[@class='top-nav-items']/li["+ i +"]")));

					SleepUtils.getInstance().sleep(TimeSlab.YIELD);					
					List<WebElement> Sub_items=webDriver.findElements(By.xpath("//ul[@class='top-nav-items']/li["+ i +"]//div[@class='subdepartments_cont']//li/span"));
					//Getting sub-categories text
					String item[]=new String[Sub_items.size()];
					for (int j = 0; j < Sub_items.size(); j++) {
						item[j]=Sub_items.get(j).getText();
						System.out.println("Sub-category: "+(j+1)+":"+item[j]);
					}
					//Traversing through each sub-category
					for (int k = 0; k < item.length; k++) {
						if(k==1){
							item[1]=item[1].replace("T", "t");
						}
						String sub_dept_xpath = "//span/a[text() = '"+item[k]+"']";

						//SleepUtils.getInstance().sleep(TimeSlab.YIELD);

						uiDriver.robot.moveToElement(webDriver.findElement(By.xpath("//ul[@class='top-nav-items']/li["+ i +"]")));
						SleepUtils.getInstance().sleep(TimeSlab.YIELD);
						//uiDriver.robot.moveToElement(webDriver.findElement(By.xpath(sub_dept_xpath)));
						//SleepUtils.getInstance().sleep(TimeSlab.YIELD);
						webDriver.findElement(By.xpath(sub_dept_xpath)).click();	
						uiDriver.waitForPageLoad();
						//SleepUtils.getInstance().sleep(TimeSlab.YIELD);
						//if (CompositeAppDriver.startUp.equalsIgnoreCase("SAFARI")) 
						//	uiDriver.wait.until(ExpectedConditions.refreshed(ExpectedConditions.stalenessOf(webDriver.findElement(By.xpath(sub_dept_xpath)))));						
						uiDriver.waitForPageLoad();
						//SleepUtils.getInstance().sleep(TimeSlab.MEDIUM);
						if (CompositeAppDriver.startUp.equalsIgnoreCase("SAFARI")) 
							uiDriver.wait.until(ExpectedConditions.refreshed(ExpectedConditions.presenceOfElementLocated(By.xpath(objMap.getLocator("txtItemBreadCrumb")))));
						else
							uiDriver.wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(objMap.getLocator("txtItemBreadCrumb"))));	
						String breadcrumbs_text = webDriver.findElement(By.xpath(objMap.getLocator("txtItemBreadCrumb"))).getText();
						if(breadcrumbs_text.contains(item[k])){
							RESULT.passed("Navigation to "+item[k],
									"Breadcrumbs for "+item[k]+" should be displayed",
									"Breadcrumbs for "+item[k]+" is displayed");
						}        	
						else{
							RESULT.failed("Navigation to "+item[k],
									"Breadcrumbs for "+item[k]+" should be displayed",
									"Breadcrumbs for "+item[k]+" is NOT displayed");
						}
						//uiDriver.click("imgfd_Logo");
					}	
					
					uiDriver.click("imgfd_Logo");
					uiDriver.waitForPageLoad();
					uiDriver.wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(objMap.getLocator("imgmediaSection"))));
				}	
			}
		} catch (Exception e) {
			
			RESULT.failed("Top Sub Items Exception", "Top sub items validation should be successful",
					"Top sub items validation is not successful");
		}
	}


	/* Zipcode and Header Dropdown check */

	public void HeaderDropdown(DataRow input, DataRow output)throws InterruptedException, FindFailed
	{		
		try{
		//Verify Zipcode
		String ZipCode;
		try
		{
			ZipCode = webDriver.findElement(By.xpath(objMap.getLocator("strzipcode"))).getText();
		}
		catch(NoSuchElementException e)
		{
			RESULT.warning("Zip code", "Zip code element should be available", "Zip code is not available");
			return;
		}
		catch(NullPointerException e)
		{
			RESULT.warning("Zip code", "Zip code element should be available", "Zip code is not available");
			return;
		}		
		catch(ElementNotFoundException e)
		{
			RESULT.warning("Zip code", "Zip code element should be available", "Zip code is not available");
			return;
		}
		try{
			System.out.println("Zipcode for recently added address" + uiDriver.Zipcode);
			if(uiDriver.Zipcode == null)
			{
				RESULT.warning("Zipcode check", "Zipcode at header should reflect the previously ordered address",
				"Zipcode value of previously ordered address is null");
			}
			else if(uiDriver.Zipcode.equals(ZipCode))
			{
				RESULT.passed("Zipcode check", "Zipcode at header should reflect the previously ordered address",
				"Zipcode at header is as per the previously ordered address");
			}
			else
			{
				RESULT.warning("Zipcode check", "Zipcode at header should reflect the previously ordered address",
				"Zipcode at header is not as per the previously ordered address");
			}
		}		
		catch(Exception e){
			RESULT.failed("Zip code comparision", "Zip code compariasion should be successful", 
					"Zip code compariasion failed due to " + e.getMessage());
		}	
		}catch (Exception e) {
			RESULT.warning("Zipcode check", "Zipcode at header should reflect the previously ordered address",
			"Zipcode for previous order has not been fetched");
		}
		if(webDriver.findElements(By.id(objMap.getLocator("drpheaderDropdown"))).size()>0)
		{
		uiDriver.click("drpheaderDropdown");		
		String Label1;
		String Label2;
		String Label3;
		try
		{
			Label1 = webDriver.findElement(By.xpath(objMap.getLocator("strhomeDelivery"))).getAttribute("label");
			Label2 = webDriver.findElement(By.xpath(objMap.getLocator("strofficeDelivery"))).getAttribute("label");
			Label3 = webDriver.findElement(By.xpath(objMap.getLocator("strpickup"))).getAttribute("label");
		
			// Check for 'Home Delivery' section
			if(Label1.equals("Home Delivery"))
			{
				RESULT.passed("HeaderDropdown", "'Home Delivery' section should be available in Header dropdown",
				"'Home Delivery' section is available in Header dropdown");
			}
			else
			{
				RESULT.warning("HeaderDropdown", "'Home Delivery' section should be available in Header dropdown",
				"'Home Delivery' section is not available in Header dropdown");
			}
			
			// Check for 'Office Delivery' section		
			if(Label2.equals("Office Delivery"))
			{
				RESULT.passed("HeaderDropdown", "'Office Delivery' section should be available in Header dropdown",
				"'Office Delivery' section is available in Header dropdown");
			}
			else
			{
				RESULT.warning("HeaderDropdown", "'Office Delivery' section should be available in Header dropdown",
				"'Office DeliveryHome Delivery' section is not available in Header dropdown");
			}
			
			// Check for 'Pickup' section			
			if(Label3.equals("Pickup"))
			{
				RESULT.passed("HeaderDropdown", "'Pickup' section should be available in Header dropdown",
				"'Pickup' section is available in Header dropdown");
			}
			else
			{
				RESULT.failed("HeaderDropdown", "'Pickup' section should be available in Header dropdown",
				"'Pickup' section is not available in Header dropdown");
			}
		}
		catch(Exception e)
		{
			RESULT.warning("Address section check", "Different address section in the header dropdown should be available",
					"Different address section check  in the header dropdown failed due to " + e.getMessage());
		}
		}
		else
		{
			RESULT.warning("HeaderDropdown", "Address dropdown section should be available in Header",
			"Address dropdown section is not available in Header");
		}
	}	

	@SuppressWarnings({ "deprecation", "static-access" })
	public void FooterCheck(DataRow input,DataRow output) throws InterruptedException
	{
		//locators for links  
		String arr1[]={"lnkaboutUs","lnkfoodSafety","lnkcareers","lnkatTheOffice","lnkrecipes","lnkgiftCards",
				"lnknewProducts","imgourMobiApp","imgatTheOfc","lnkyourAccount","lnkHome","lnkhelpFAQ",
				"lnkcontactUs","lnkprivacyPolicy","lnkcustAgreemnt","lnknoteOnAOLUser"};
		//locators for verification of page
		String path1[]={"straboutUs","strfoodSafety","strcareers","stratTheOfc","strrecipes","strgiftCard",
				"strnewProducts","strourMobiApp","stratTheOfc","stryourAccount","strHome","strhelpFAQ",
				"strcontactUs","strprivacyPolicy","strcustAgreemnt","strnoteOnAOLUser"};

		String arr2[]={"lnkblog","imgfb","imgtwitter","imgpinterest","imggooglePlus","imgyouTube"};
		String path2[]={"strblog","strfB","strtwitter","imgverifyPinterest","imgverifyGooglePlus","imgverifyYouTube"};
		String winHandleBefore = webDriver.getWindowHandle();
		
		//click on links from footer and verify that it redirects to respective page
		for(int i=0;i<arr1.length;i++)
		{
			//try{
				
				try{
					uiDriver.clickFromData(arr1[i]);
					if(arr1[i].equals("lnkyourAccount"))
					{
						//uiDriver.selenium.waitForPageToLoad(PageLoadTime);
						uiDriver.wait.until(ExpectedConditions.invisibilityOfElementLocated(By.linkText(objMap.getLocator("lnkyourAccount"))));
						//Verifying user is logged in or not
						if(webDriver.findElements(By.xpath(objMap.getLocator("btngoAnonymous"))).size()>0)
						{
							uiDriver.FD_login(input.get("userID"), input.get("password"));	
						}
						uiDriver.wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(objMap.getLocator("stryourAccount"))));
						if(uiDriver.getwebDriverLocator(objMap.getLocator("stryourAccount")).getText().contains("Welcome to Your Account"))
						{
							RESULT.passed("Your account link", "Your account link should redirect to your account page",
							"Your account link redirected to your account page");
						}
						else
						{
							RESULT.failed("Your account link", "Your account link should redirect to your account page", 
							"Your account link could not redirect to your account page");											
						}
						//webDriver.navigate().refresh();
					}
					else
					{
						try{
						uiDriver.wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(objMap.getLocator(path1[i]))));
						//SleepUtils.getInstance().sleep(TimeSlab.YIELD);
						//uiDriver.DrawHighlight(path1[i], webDriver);
						}catch(Exception e){
							RESULT.passed(arr1[i].substring(3).toUpperCase(), arr1[i].substring(3).toUpperCase()+" :link should redirect to respective page",
									arr1[i].substring(3).toUpperCase()+" :link is not redirected to respective page");
						}
						RESULT.passed(arr1[i].substring(3).toUpperCase(), arr1[i].substring(3).toUpperCase()+" :link should redirect to respective page",
								arr1[i].substring(3).toUpperCase()+" :link redirected to respective page");
						if(!(arr1[i].equals("lnkHome")))
							webDriver.navigate().back();
						uiDriver.waitForPageLoad();
					}
					//					}
				}catch (Exception e) {					
					RESULT.failed(arr1[i].substring(3).toUpperCase(), arr1[i].substring(3).toUpperCase()+" :link should redirect to respective page", arr1[i].substring(3).toUpperCase()+" :link could not redirected to respective page");
				}
//			}catch (Exception e) {
//		
//				RESULT.failed(arr1[i], arr1[i]+" should pass", arr1[i]+"Failed");
//				RESULT.failed("Footer check","Footer check should be validated","Footer check is not validated");
//			}
		}

		//for links where link opens a new tab in browser 
		for(int i=0;i<arr2.length;i++)
		{

			try{
				webDriver.navigate().refresh();
				SleepUtils.getInstance().sleep(TimeSlab.YIELD);
				uiDriver.clickFromData(arr2[i]);
				SleepUtils.getInstance().sleep(TimeSlab.YIELD);

				Set<String> handles =  webDriver.getWindowHandles();
				for(String windowHandle  : handles)
				{
					if(!windowHandle.equals(winHandleBefore))
					{
						webDriver.switchTo().window(windowHandle);
					}
				}
				webDriver.manage().window().maximize();
				System.out.println(webDriver.getTitle());
				//uiDriver.selenium.waitForPageToLoad(PageLoadTime);
				try{
				uiDriver.wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(objMap.getLocator(path2[i]))));
				//uiDriver.DrawHighlight(path2[i], webDriver);
				}catch(Exception e){
					if(webDriver.findElements(By.xpath(objMap.getLocator(path2[i]))).size()>0 && 
							webDriver.findElement(By.xpath(objMap.getLocator(path2[i]))).isDisplayed())
					{
						RESULT.passed(arr2[i].substring(3).toUpperCase(), arr2[i].substring(3).toUpperCase()+" :link should redirect to respective page", arr2[i].substring(3).toUpperCase()+" :link redirected to respective page");
					}
					else
					{
						RESULT.failed(arr2[i].substring(3).toUpperCase(), arr2[i].substring(3).toUpperCase()+" :link should redirect to respective page", arr2[i].substring(3).toUpperCase()+" :link could not redirected to respective page");
					}
					//RESULT.failed(arr2[i].substring(3).toUpperCase(), arr2[i].substring(3).toUpperCase()+" :link should redirect to respective page", arr2[i].substring(3).toUpperCase()+" :link could not redirected to respective page");
	 			}
				if(webDriver.findElements(By.xpath(objMap.getLocator(path2[i]))).size()>0)
				{
					RESULT.passed(arr2[i].substring(3).toUpperCase(), arr2[i].substring(3).toUpperCase()+" :link should redirect to respective page", arr2[i].substring(3).toUpperCase()+" :link redirected to respective page");
				}
				else
				{
					RESULT.failed(arr2[i].substring(3).toUpperCase(), arr2[i].substring(3).toUpperCase()+" :link should redirect to respective page", arr2[i].substring(3).toUpperCase()+" :link could not redirected to respective page");
				}
			}catch (Exception e) {
				RESULT.failed(arr2[i].substring(3).toUpperCase() + "with new windows", arr2[i].substring(3).toUpperCase()+" :link should redirect to respective page", arr2[i].substring(3).toUpperCase()+" :link failed");
			}
			try{
				webDriver.switchTo().window(webDriver.getWindowHandle()).close();
				webDriver.switchTo().window(winHandleBefore);
			}catch (Exception e) {
				RESULT.failed(arr2[i].substring(3).toUpperCase(), arr2[i].substring(3).toUpperCase()+" opened new window should get closed", arr2[i].substring(3).toUpperCase()+" opened new window is not able to close");
			}
		}
	}
}