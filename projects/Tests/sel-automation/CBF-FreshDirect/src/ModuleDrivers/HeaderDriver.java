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
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.sikuli.script.FindFailed;

import ui.RobotPowered;
import cbf.engine.BaseModuleDriver;
import cbf.engine.TestResultLogger;
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
	public static String PageLoadTime = "300000";
	WebDriverWait wait = new WebDriverWait(webDriver, 300);
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
		//WebUIDriver.selenium.waitForPageToLoad(PageLoadTime);

		wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(objMap.getLocator("strzipcode"))));
		try{	
			
			String zip_pattern="^[0-9]{5}$";
			Pattern zipcode_p=Pattern.compile(zip_pattern);
			Matcher m=zipcode_p.matcher(webDriver.findElement(By.xpath(objMap.getLocator("strzipcode"))).getText());
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
			
			//go button validation
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
			/*String ZipCode = webDriver.findElement(By.xpath(objMap.getLocator("strzipcode"))).getText();
			String CityState = webDriver.findElement(By.xpath(objMap.getLocator("strcityState"))).getText();		
			if(ZipCode!=null && CityState!=null)
			{				
				RESULT.passed("Verify Zip Code", "Zip Code,City and State should be displayed", "Zip Code,City and State is displayed on the Page");
			}
			else
			{
				RESULT.failed("Verify Zip Code", "Zip Code,City and State should be displayed", "Zip Code and Address is not displayed on the Page");				
			}
			//WebUIDriver.selenium.waitForPageToLoad(PageLoadTime);

			uiDriver.setValue("strnewZipCode", input.get("NewZipCode"));
			SleepUtils.getInstance().sleep(TimeSlab.YIELD);
			uiDriver.click("btngoBtn");
			wait.until(ExpectedConditions.stalenessOf(webDriver.findElement(By.xpath(objMap.getLocator("strzipcode")))));
			//SleepUtils.getInstance().sleep(TimeSlab.HIGH);
			String ZipCodeNew = webDriver.findElement(By.xpath(objMap.getLocator("strzipcode"))).getText();
			String NewCityState = webDriver.findElement(By.xpath(objMap.getLocator("strcityState"))).getText();
			//if(ZipCodeNew!=ZipCode)
			if(ZipCodeNew.equals(input.get("NewZipCode")) && NewCityState!=CityState)
			{
				RESULT.passed("Verify Zip Code", "New Zip Code, City and State should be displayed","Zip Code,City and State is displayed on the Page");
			}
			else
			{
				RESULT.failed("Verify Zip Code", "Zip Code,City and State should be displayed", "Zip Code,City and State is not displayed on the Page");				
			}*/	
		}
		catch (Exception e) {
			e.printStackTrace();
			RESULT.failed("Verify Zip Code", "Zip Code,City and State should be displayed", "Zip code verification failed");
		}
	}	

	/* Verify 'office delivery'/'Home Delivery' link displayed on page header.*/

	public void OfficeHomeDeliveryLink(DataRow input, DataRow output) throws InterruptedException, FindFailed
	{
		//WebUIDriver.selenium.waitForPageToLoad(PageLoadTime);
		try
		{
			wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.linkText(objMap.getLocator("lnkofficeDeliveryLink"))));
			String OfficeDelivery = webDriver.findElement(By.linkText(objMap.getLocator("lnkofficeDeliveryLink"))).getText();
			if(OfficeDelivery.equals("Office delivery?"))
			{
				RESULT.passed("Verify Office Delivery Link", "Link with 'office delivery' text should be displayed.", "Link with 'office delivery' text is displayed.");
				uiDriver.click("lnkofficeDeliveryLink");
				SleepUtils.getInstance().sleep(TimeSlab.MEDIUM);
				//WebUIDriver.selenium.waitForPageToLoad(PageLoadTime);
				//wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(objMap.getLocator("btnclosePopupNeedInfo"))));
				if(webDriver.findElements(By.xpath(objMap.getLocator("btnclosePopupNeedInfo"))).size() > 0) 
				{ 
					uiDriver.click("btnclosePopupNeedInfo"); 
					SleepUtils.getInstance().sleep(TimeSlab.YIELD);
				}
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
				uiDriver.click("lnkhomeDelivery");
				//WebUIDriver.selenium.waitForPageToLoad(PageLoadTime);
				SleepUtils.getInstance().sleep(TimeSlab.HIGH);
			}
			else
			{
				RESULT.failed("Verify Office Delivery Link", "Link with 'office delivery' text should be displayed.", "Link with 'office delivery' text is not displayed.");				
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			RESULT.failed("Office/Home Delivery Link", "Office/Home Delivery Link should be displayed", "Office/Home Delivery Link is NOT displayed");
		}
	}	

	/* Verify 'SIGN UP' button displayed on page header.*/		

	public void SignUpButton(DataRow input, DataRow output) throws InterruptedException, FindFailed
	{
		try
		{
			if(webDriver.findElement(By.className(objMap.getLocator("btnsignupBtn"))).isDisplayed())
			{
				RESULT.passed("Verify SIGN UP button on the page header", "SIGN UP button should be displayed on right side of the page header.",
				"SIGN UP button is displayed on right side of the page header." );
				uiDriver.click("btnsignupBtn");
				//WebUIDriver.selenium.waitForPageToLoad(PageLoadTime);
				SleepUtils.getInstance().sleep(TimeSlab.MEDIUM);
				webDriver.switchTo().frame(uiDriver.getwebDriverLocator(objMap.getLocator("frameiframe")));
				SleepUtils.getInstance().sleep(TimeSlab.MEDIUM);
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
				}
				webDriver.switchTo().defaultContent();
				SleepUtils.getInstance().sleep(TimeSlab.YIELD);
				//uiDriver.click("btnsignupClose");
				webDriver.findElement(By.xpath(objMap.getLocator("btnsignupClose"))).click();
			}
			else
			{
				RESULT.failed("Verify SIGN UP button on the page header", "SIGN UP button should be displayed on right side of the page header.",
				"SIGN UP button is not displayed on right side of the page header." );
			}	
		}
		catch(Exception e)
		{
			e.printStackTrace();
			RESULT.failed("SIGN UP button Verification", "SIGN UP button should be displayed.",
			"SIGN UP button verification failed" );
		}		
	}	


	/* Verify 'LOG IN' button displayed on page header.*/		

	public void LoginButton(DataRow input, DataRow output) throws InterruptedException, FindFailed
	{
		try
		{			
			//String Login = webDriver.findElement(By.className(objMap.getLocator("LogIn"))).getText();
			wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.className(objMap.getLocator("btnlogin"))));
			if(uiDriver.getwebDriverLocator(objMap.getLocator("btnlogin")).isDisplayed())
			{
				RESULT.passed("Verify Login button and its functionality",
						"LOG IN button should be displayed on right side of page header.",
				"LOG IN button is displayed on right side of page header." );
				uiDriver.FD_login(input.get("userID"), input.get("password"));
				//WebUIDriver.selenium.waitForPageToLoad(PageLoadTime);
				wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.className(objMap.getLocator("btnlogout"))));
				//String WelcomeMsg = webDriver.findElement(By.xpath(objMap.getLocator("strwelcome"))).getText();
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
				//uiDriver.click("btnlogout");
			}
			else
			{
				RESULT.failed("Verify Login button and its functionality", "LOG IN button should be displayed on right side of page header.",
				"LOG IN button is not displayed on right side of page header." );
			}				

		}
		catch(Exception e)
		{
			e.printStackTrace();
			RESULT.failed("Login button verification", "Login button should be displayed",
			"Login button verification failed" );
		}	
	}

	/* Verify 'Fresh Direct' logo displayed on web page header.*/

	public void FreshDirectLogo(DataRow input, DataRow output) throws InterruptedException, FindFailed
	{
		WebDriverWait wait = new WebDriverWait(webDriver, 20);
		try
		{
			if(webDriver.findElement(By.name(objMap.getLocator("imgfd_Logo"))).isDisplayed())
			{
				RESULT.passed("Verify 'Fresh Direct' logo displayed on web page header.", "Fresh Direct' logo should be displayed on web page header.",
				"Fresh Direct' logo is displayed on web page header.");										
			}
			else
			{
				RESULT.failed("Verify 'Fresh Direct' logo displayed on web page header.", "Fresh Direct' logo should be displayed on web page header.",
				"Fresh Direct' logo is not displayed on web page header.");
			}	
			uiDriver.setValue("txtsearchBox", "Chocolates");
			uiDriver.click("btngo");
			SleepUtils.getInstance().sleep(TimeSlab.HIGH);
			uiDriver.click("imgfd_Logo");
			wait.until(ExpectedConditions.presenceOfElementLocated(By.className(objMap.getLocator("btnlogin"))));
			if (uiDriver.isDisplayed("btnlogin")) {
				RESULT.passed("Verify 'Fresh Direct' logo displayed on web page header.",
						"Fresh Direct homepage should be displayed.", 
				"Fresh Direct homepage is displayed.");
			} else {
				RESULT.failed("Verify 'Fresh Direct' logo displayed on web page header.",
						"Fresh Direct homepage should be displayed.", 
				"Error occured");
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			RESULT.failed("'Fresh Direct' logo verification",
					"Fresh Direct logo should be displayed.", 
			"'Fresh Direct' logo verification failed");
		}
	}


	/* Verify search field on web page header.*/		

	public void SearchField(DataRow input, DataRow output) throws InterruptedException, FindFailed
	{
		try
		{
			wait.until(ExpectedConditions.presenceOfElementLocated(By.id(objMap.getLocator("txtsearchBox"))));
			if(webDriver.findElement(By.id(objMap.getLocator("txtsearchBox"))).isDisplayed())
				if(webDriver.findElement(By.id(objMap.getLocator("btngo"))).isEnabled())					
				{
					uiDriver.setValue("txtsearchBox", input.get("SearchItem"));
					uiDriver.click("btngo");
					wait.until(ExpectedConditions
							.presenceOfElementLocated(By.xpath(objMap
									.getLocator("strverifyNoSearchResult"))));
					RESULT.passed("Verify search field on web page header", "Search' field with 'Go' button should be displayed on web page header.",
					"Search' field with 'Go' button is displayed on web page header.");							
					//uiDriver.click("imgfd_Logo");
				}
				else
				{
					RESULT.failed("Verify search field on web page header", "Search' field with 'Go' button should be displayed on web page header.",
					"Search' field with 'Go' button is not displayed on web page header.");
				}								
		}
		catch(Exception e)
		{
			e.printStackTrace();
			RESULT.failed("Search field validation", "Search' field with 'Go' button should be displayed.",
			"Search field validation failed");
		}
	}		


	/* Verify 'Your Account' link in web page header when logged-in customer clicks on the link */

	public void YourAcc_Loggedin(DataRow input, DataRow output) throws InterruptedException, FindFailed
	{
		try
		{
			//uiDriver.FD_login(input.get("userID"), input.get("password"));
			//WebUIDriver.selenium.waitForPageToLoad(PageLoadTime);
			wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.linkText(objMap.getLocator("lnkyourAccountLink"))));
			//String YourAcctLink = webDriver.findElement(By.id(objMap.getLocator("YourAccountLink"))).getText();
			if(webDriver.findElement(By.linkText(objMap.getLocator("lnkyourAccountLink"))).isDisplayed())
			{
				RESULT.passed("Verify 'Your Account' link in web page header when logged-in customer clicks on the link", 
						"Your Account' link should be displayed on web page header.",
				"Your Account' link gets displayed on web page header.");		
				//WebUIDriver.selenium.waitForPageToLoad(PageLoadTime);
				SleepUtils.getInstance().sleep(TimeSlab.YIELD);
				uiDriver.click("lnkyourAccountLink");
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
			e.printStackTrace();
			RESULT.failed("Link 'Your Account' verification", 
					"Your Account' link should be displayed",
			"Link 'Your Account' verification failed");
		}
	}	


	/* Verify 'Your Account' link in web page header when anonymous customer clicks on the link */

	public void YourAcc_Anonymous(DataRow input, DataRow output) throws InterruptedException, FindFailed
	{
		try
		{
			wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.linkText(objMap.getLocator("lnkyourAccountLink"))));
			if(webDriver.findElement(By.linkText(objMap.getLocator("lnkyourAccountLink"))).isDisplayed())
			{
				RESULT.passed("Verify 'Your Account' link in web page header when anonymous customer clicks on the link", 
						"Your Account' link should be displayed on web page header.",
				"Your Account' link gets displayed on web page header.");					
				uiDriver.click("lnkyourAccountLink");	
				wait.until(ExpectedConditions.presenceOfElementLocated(By.name(objMap.getLocator("btnloginGoBtn"))));
				if(webDriver.findElements(By.xpath(objMap.getLocator("btngoAnonymous"))).size() > 0)
				{
					RESULT.passed("Verify 'Your Account' link in web page header when anonymous customer clicks on the link", 
							"User should be redirected to Log In page",
					"User is redirected to Log In page");
					uiDriver.setValue("txtemailAddress", input.get("userID"));
					uiDriver.setValue("txtpass", input.get("password"));
					uiDriver.click("btnloginGoBtn");
					SleepUtils.getInstance().sleep(TimeSlab.MEDIUM);
					if(webDriver.findElement(By.xpath(objMap.getLocator("stryourAcctPage"))).getText().contains("Welcome to Your Account"))
					{
						RESULT.passed("Verify 'Your Account' link in web page header when anonymous customer clicks on the link", 
								"User should be redirected to Your Account page",
						"User is redirected to Your Account page");	
					}
					else
					{
						RESULT.passed("Verify 'Your Account' link in web page header when anonymous customer clicks on the link", 
								"User should be redirected to Your Account page",
						"Unable to navigate to Your Account page");	
					}					
				}
				else
				{
					RESULT.failed("Verify 'Your Account' link in web page header when anonymous customer clicks on the link", 
							"User should be redirected to Log In page",
					"Unable to redirect user to Log In page");
				}		
				uiDriver.click("btnlogout");
				wait.until(ExpectedConditions.presenceOfElementLocated(By.className(objMap.getLocator("btnlogin"))));
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
			e.printStackTrace();
			RESULT.failed("Verify 'Your Account' link for anonymous customer", 
					"Your Account' link should be displayed",
			"Verification for 'Your Account' link for anonymous customer failed");
		}
	}	


	/* Verify 'LOGOUT' button displayed on page header after user login. */

	public void LogoutButton(DataRow input, DataRow output) throws InterruptedException, FindFailed
	{
		try
		{
			//WebUIDriver.selenium.waitForPageToLoad(PageLoadTime);
			wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(objMap.getLocator("btnlogoutBtn"))));
			//SleepUtils.getInstance().sleep(TimeSlab.YIELD);
			if(webDriver.findElement(By.xpath(objMap.getLocator("btnlogoutBtn"))).isDisplayed())
			{
				RESULT.passed("Verify 'LOGOUT' button displayed on page header after user login.", 
						"LOGOUT' button should be displayed on page header.",
				"LOGOUT' button gets displayed on page header.");		
				uiDriver.click("btnlogoutBtn");
				wait.until(ExpectedConditions.presenceOfElementLocated(By.className(objMap.getLocator("btnlogin"))));
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
			e.printStackTrace();
			RESULT.failed("'LOGOUT' button verification", 
					"LOGOUT' button should be displayed",
			"'LOGOUT' button verification failed");	
		}
	}	


	/* Verify 'Reorder' link displayed on page header and adding product to cart for an anonymous customer. */

	public void Reorder_Anonymous(DataRow input, DataRow output) throws InterruptedException, FindFailed
	{
		try
		{
			wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.id(objMap.getLocator("lnkreorderLink"))));
			if(webDriver.findElement(By.id(objMap.getLocator("lnkreorderLink"))).isDisplayed())
			{
				RESULT.passed("Verify 'Reorder' link displayed on page header and adding product to cart for an anonymous customer", 
						"'Reorder' link should be displayed on page header.",
				"Reorder' link gets displayed on page header.");		
				uiDriver.click("lnkreorderLink");
				wait.until(ExpectedConditions.presenceOfElementLocated(By.name(objMap.getLocator("btnloginGoBtn"))));
				uiDriver.setValue("txtemailAddress", input.get("userID"));
				uiDriver.setValue("txtpass", input.get("password"));
				uiDriver.click("btnloginGoBtn");				
				//WebUIDriver.selenium.waitForPageToLoad(PageLoadTime);
				wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(objMap.getLocator("strreorder"))));
				SleepUtils.getInstance().sleep(TimeSlab.YIELD);
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
					"Error occured somewhere.");
				}				
			}
			else
			{
				RESULT.failed("Verify 'Reorder' link displayed on page header and adding product to cart for an anonymous customer", 
						"Reorder' link should be displayed on page header.",
				"Reorder' link is not displayed on page header.");	
			}	
			uiDriver.click("btnlogout");
			wait.until(ExpectedConditions.presenceOfElementLocated(By.className(objMap.getLocator("btnlogin"))));
		}
		catch(Exception e)
		{
			e.printStackTrace();
			RESULT.failed("Verification of 'Reorder' link for an anonymous user", 
					"'Reorder' link should be displayed",
			"Verification of 'Reorder' link for an anonymous user failed");
		}
	}	


	/* Verify 'Reorder' link displayed on page header and adding product to cart. */

	public void Reorder_Loggedin(DataRow input, DataRow output) throws InterruptedException, FindFailed
	{
		try
		{				
			//uiDriver.FD_login(input.get("userID"), input.get("password"));
			//WebUIDriver.selenium.waitForPageToLoad(PageLoadTime);
			wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.id(objMap.getLocator("lnkreorderLink"))));
			SleepUtils.getInstance().sleep(TimeSlab.YIELD);
			if(webDriver.findElement(By.id(objMap.getLocator("lnkreorderLink"))).isDisplayed())
			{
				RESULT.passed("Verify 'Reorder' link displayed on page header and adding product to cart for an anonymous customer", 
						"Reorder' link should be displayed on page header.",
				"Reorder' link gets displayed on page header.");		
				uiDriver.click("lnkreorderLink");     // Click on 'Reorder' link
				SleepUtils.getInstance().sleep(TimeSlab.MEDIUM);
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
					"Error occured somewhere.");
				}	
				uiDriver.click("imgfd_Logo");
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
			e.printStackTrace();
			RESULT.failed("Verification of 'Reorder' link for an loggedin user", 
					"'Reorder' link should be displayed",
			"Verification of 'Reorder' link for an loggedin user failed");
		}
	}	


	/* Verify 'Delivery Info' link displayed on page header. */

	public void DeliveryInfo(DataRow input, DataRow output) throws InterruptedException, FindFailed
	{
		try
		{
			//WebUIDriver.selenium.waitForPageToLoad(PageLoadTime);
			//SleepUtils.getInstance().sleep(TimeSlab.YIELD);		
			wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.linkText(objMap.getLocator("lnkdeliveryInfoLink"))));
			if(webDriver.findElement(By.linkText(objMap.getLocator("lnkdeliveryInfoLink"))).isDisplayed())
			{			
				uiDriver.click("lnkdeliveryInfoLink");       //click on Delivery Info Link
				SleepUtils.getInstance().sleep(TimeSlab.HIGH);
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
			else
			{
				RESULT.failed("Verify 'Delivery Info' link displayed on page header.", 
						"'Delivery Info'link should be displayed.",
				"'Delivery Info'link page is not displayed.");	
			}
			uiDriver.click("imgfd_Logo");		
		}

		catch(Exception e)
		{
			e.printStackTrace();
			RESULT.failed("Verification of 'Delivery Info' link", 
					"'Delivery Info'link should be displayed.",
			"Verification of 'Delivery Info' link failed");
		}
	}


	/* Verify 'Help' link displayed on page header. */

	public void HelpLink(DataRow input, DataRow output) throws InterruptedException, FindFailed
	{
		try
		{
			//uiDriver.FD_login(input.get("userID"), input.get("password"));
			//WebUIDriver.selenium.waitForPageToLoad(PageLoadTime);
			//SleepUtils.getInstance().sleep(TimeSlab.YIELD);
			wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.linkText(objMap.getLocator("lnkhelpLink"))));
			uiDriver.click("lnkhelpLink");
			//WebUIDriver.selenium.waitForPageToLoad(PageLoadTime);
			SleepUtils.getInstance().sleep(TimeSlab.YIELD);
			wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.className(objMap.getLocator("strhelpPage"))));
			String WelcomePage = webDriver.findElement(By.className(objMap.getLocator("strhelpPage"))).getText();
			String imgFAQ = webDriver.findElement(By.xpath(objMap.getLocator("lnkFAQLink"))).findElement(By.tagName("img")).getAttribute("alt");
			String imgContact = webDriver.findElement(By.xpath(objMap.getLocator("lnkcontactUsLink"))).findElement(By.tagName("img")).getAttribute("alt");

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
		}
		catch(Exception e)
		{
			e.printStackTrace();
			RESULT.failed("Verification of 'Help' link", 
					"Welcome to Help' page with FAQs and 'Contact Fresh Direct' section should be displayed.",
			"Verification of 'Help' link failed");
		}
	}


	/* Verify 'Your cart' button displayed on page header.*/

	public void YourCartButton(DataRow input, DataRow output) throws InterruptedException, FindFailed
	{
		try
		{
			wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(objMap.getLocator("btnyourCart"))));
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
		}
		catch(Exception e)
		{
			e.printStackTrace();
			RESULT.failed("Verification of 'Your cart' button", 
					"'Your cart' button should be displayed",
			"Verification of 'Your cart' button failed");
		}
	}



	/* Verify Social buttons displayed on Product Details Page */

	public void SocialButton(DataRow input, DataRow output) throws InterruptedException, FindFailed
	{
		try
		{
			Actions actions = new Actions(webDriver);
			int flag = 0;
			WebElement product = null;
			String productFullName = null;	
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
						wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(objMap.getLocator("btnpagePrev"))));
					}
					product = webDriver.findElement(By.partialLinkText(input.get("Product")));
					productFullName = product.getText();
				} catch (NoSuchElementException e1) {
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
				e.printStackTrace();
			}
			SleepUtils.getInstance().sleep(TimeSlab.YIELD);
			if (webDriver.findElements(By.xpath(objMap.getLocator("diaavailability"))).size() > 0) {								
				RESULT.warning("Out of stock product",
						"Product is Out of stock",
						"Message: "+ uiDriver.getwebDriverLocator(objMap.getLocator("strprodUnavailability")).getText());
				uiDriver.click("btnback");

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
			//uiDriver.selenium.waitForPageToLoad(PageLoadTime);

			try{
				SleepUtils.getInstance().sleep(TimeSlab.YIELD);
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(objMap.getLocator("framefacebook"))));
				webDriver.switchTo().frame(webDriver.findElement(By.xpath(objMap.getLocator("framefacebook"))));
				//check the presence of Facebook button 
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
				webDriver.switchTo().defaultContent();
				webDriver.switchTo().frame(uiDriver.getwebDriverLocator(objMap.getLocator("frametwitter")));
				//check the presence of Twitter button
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
				webDriver.switchTo().defaultContent();
				webDriver.switchTo().frame(uiDriver.getwebDriverLocator(objMap.getLocator("framegoogle")));
				//check the presence of Google button
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
				webDriver.switchTo().defaultContent();
				webDriver.switchTo().frame(uiDriver.getwebDriverLocator(objMap.getLocator("framepin")));
				//check the presence of Pinit button
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
			}catch (Exception e) {
				e.printStackTrace();
				RESULT.failed("Verification of social buttons",
						"Social buttons should be present on Product Details Page",
				"Verification of social buttons failed");
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			RESULT.failed("Verification of social buttons",
					"Social buttons should be present on Product Details Page",
			"Verification of social buttons failed");
		}
	}


	public void TopNavigation(DataRow input, DataRow output)throws InterruptedException, FindFailed
	{

		try{
			List<WebElement> catList,wbList;
			String brdcrmb;
			WebElement we;
			RobotPowered robot = new RobotPowered(webDriver);
			wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//div[@class='top-nav']/ul/li")));
			//Verifying top navigation categories from global navigation bar
			catList=webDriver.findElements(By.xpath("//div[@class='top-nav']/ul/li"));
			String[] categories=new String[catList.size()];
			for(int i=0;i<catList.size();i++){
				categories[i]=catList.get(i).getText();
				System.out.println("category: "+(i+1)+":"+categories[i]);
			}		
			for(int i=0;i<categories.length;i++){
				try{        	
					//String category=we.getText();
					System.out.println("category "+(i+1)+":"+categories[i]);
					//webDriver.findElement(By.partialLinkText(categories[i])).click();
					//Configuration GCONFIG = Harness.GCONFIG;

					if(CompositeAppDriver.startUp.equalsIgnoreCase("IE")){
						wbList=webDriver.findElements(By.xpath("//div[@class='top-nav']/ul/li/span"));
						Actions actions = new Actions(webDriver);
						if(wbList.get(i).getText().contains("DELI")||wbList.get(i).getText().contains("BAKERY")||wbList.get(i).getText().contains("WINES")){	    					
							//actions.moveToElement(wbList.get(i)).click().perform();
							robot.moveToElement(wbList.get(i));
							wbList.get(i).click();
						}	    				
						else{
							webDriver.findElement(By.linkText(categories[i])).click();
						}	    				    	    	
					}else{
						webDriver.findElement(By.linkText(categories[i])).click();
						if (CompositeAppDriver.startUp.equalsIgnoreCase("SAFARI"))
							wait.until(ExpectedConditions.refreshed(ExpectedConditions.stalenessOf(webDriver.findElement(By.linkText(categories[i])))));
					}		
					//SleepUtils.getInstance().sleep(TimeSlab.HIGH);
					SleepUtils.getInstance().sleep(TimeSlab.MEDIUM);
					if (CompositeAppDriver.startUp.equalsIgnoreCase("SAFARI")) 
						wait.until(ExpectedConditions.refreshed(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@class='browse-breadcrumbs']/ul"))));
					else
						wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@class='browse-breadcrumbs']/ul")));
					SleepUtils.getInstance().sleep(TimeSlab.MEDIUM);
					we=webDriver.findElement(By.xpath("//div[@class='browse-breadcrumbs']/ul"));
					//WebUIDriver.selenium.waitForPageToLoad(PageLoadTime);				
					//we=(new WebDriverWait(webDriver,240)).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@class='browse-breadcrumbs']/ul")));	
					SleepUtils.getInstance().sleep(TimeSlab.YIELD);
					brdcrmb=we.getText().replaceAll("\\s", "");        	
					if(brdcrmb.toUpperCase().contains(categories[i].replaceAll("\\s", ""))){
						RESULT.passed("Navigation to "+categories[i],"Breadcrumbs for "+categories[i]+" should be displayed","Breadcrumbs for "+brdcrmb+" is displayed");
					}        	
					else{
						RESULT.failed("Navigation to "+categories[i],"Breadcrumbs for "+categories[i]+" should be displayed","Breadcrumbs for "+brdcrmb+" is NOT displayed");
					}
				}
				catch(Exception e){
					RESULT.failed("Exception: Navigation to "+categories[i], "Exception caught","Exception caught");
					e.printStackTrace();

				}
			}
		}catch(Exception e){
			RESULT.failed("Exception: Navigation to " + e.getStackTrace(), "Exception caught","Exception caught");
			e.printStackTrace();

		}

	}
	public void BottomNavigation(DataRow input, DataRow output)throws InterruptedException, FindFailed
	{
		try{
			List<WebElement> catList;
			String brdcrmb;
			WebElement we;
			wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//div[@class='top-nav']/ul/li")));
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
					Thread.sleep(2000);
					//WebUIDriver.selenium.waitForPageToLoad(PageLoadTime);
					if(categories[i].contains("DEALS")||categories[i].contains("TOP-RATED")||categories[i].contains("LOCAL")){
						SleepUtils.getInstance().sleep(TimeSlab.MEDIUM);
						we=(new WebDriverWait(webDriver,240)).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@class='browse-breadcrumbs']/ul")));
						brdcrmb=we.getText();        	
						if(brdcrmb.toUpperCase().contains(categories[i])){
							RESULT.passed("Navigation to "+categories[i],"Breadcrumbs for "+categories[i]+" should be displayed","Breadcrumbs for "+brdcrmb+" is displayed");
						}        	
						else{
							RESULT.failed("Navigation to "+categories[i],"Breadcrumbs for "+categories[i]+" should be displayed","Breadcrumbs for "+brdcrmb+" is NOT displayed");
						}
					}
					else if(categories[i].contains("COUPONS")){
						SleepUtils.getInstance().sleep(TimeSlab.MEDIUM);
						we=(new WebDriverWait(webDriver,240)).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//img[@alt='FDCoupons']")));
						if(we.isDisplayed()){
							RESULT.passed("Navigation to "+categories[i],"fdcoupons image should be displayed","fdcoupons image is displayed");
						}
						else{
							RESULT.failed("Navigation to "+categories[i],"fdcoupons image should be displayed","fdcoupons image is NOT displayed");
						}
					}
					else if(categories[i].contains("NEW")){
						SleepUtils.getInstance().sleep(TimeSlab.MEDIUM);
						we=(new WebDriverWait(webDriver,240)).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//h1[@class='orange-title']")));
						if(we.getText().contains("New Products")){
							RESULT.passed("Navigation to "+categories[i],"'New Products' text should be displayed","'New Products' text is displayed");
						}
						else{
							RESULT.failed("Navigation to "+categories[i],"'New Products' text should be displayed","'New Products' text is NOT displayed");
						}
					}
					else if(categories[i].contains("PRESIDENT'S PICKS")){
						SleepUtils.getInstance().sleep(TimeSlab.MEDIUM);
						we=(new WebDriverWait(webDriver,240)).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@class='sorter-element_list']")));
						if(we.getText().contains("Sort")){
							RESULT.passed("Navigation to "+categories[i],"Sort option on PRESIDENT'S PICKS should be displayed","Sort option on PRESIDENT'S PICKS is displayed");
						}
						else{
							RESULT.failed("Navigation to "+categories[i],"Sort option on PRESIDENT'S PICKS should be displayed","Sort option on PRESIDENT'S PICKS is NOT displayed");
						}
					}
					else if(categories[i].contains("IDEAS")){
						SleepUtils.getInstance().sleep(TimeSlab.MEDIUM);
						//get window handlers as list
						List<String> browserTabs = new ArrayList<String> (webDriver.getWindowHandles());
						System.out.println("tabs size : "+browserTabs.size());	        		
						//switch to new tab
						webDriver.switchTo().window(browserTabs.get(1));
						we=(new WebDriverWait(webDriver,240)).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='title-area']/h1/a")));
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
					RESULT.failed("Navigation to "+categories[i], "Exception caught","Exception caught");
					e.printStackTrace();
				}
			}
		}catch(Exception e){
			RESULT.failed("Exception: Navigation to " + e.getStackTrace(), "Exception caught","Exception caught");
			e.printStackTrace();

		}
	}

	public void TopSubItems(DataRow input, DataRow output) throws InterruptedException, FindFailed {

		try 
		{

			RobotPowered robot = new RobotPowered(webDriver);
			Actions actions = new Actions(webDriver);
			//Configuration GCONFIG = Harness.GCONFIG;
			wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//div[@class='top-nav']/ul/li")));			
			List<WebElement> catList=webDriver.findElements(By.xpath("//div[@class='top-nav']/ul/li"));
			//Counting number of categories which has sub-categories
			int Cat_Size=catList.size();
			//Traversing through each category having sub-category
			for (int i = 0; i < Cat_Size; i++) {

				if (webDriver.findElements(By.xpath("//ul[@class='top-nav-items']/li["+ i +"]//div[@class='subdepartments_cont']//li/span")).size() > 0) {
					//hover on each categories having sub-category

					robot.moveToElement(webDriver.findElement(By.xpath("//ul[@class='top-nav-items']/li["+ i +"]")));

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
						String sub_dept_xpath = "//a[text() = '"+item[k]+"']";

						SleepUtils.getInstance().sleep(TimeSlab.YIELD);

						robot.moveToElement(webDriver.findElement(By.xpath("//ul[@class='top-nav-items']/li["+ i +"]")));
						SleepUtils.getInstance().sleep(TimeSlab.YIELD);
						robot.moveToElement(webDriver.findElement(By.xpath(sub_dept_xpath)));
						SleepUtils.getInstance().sleep(TimeSlab.YIELD);
						webDriver.findElement(By.xpath(sub_dept_xpath)).click();	
						if (CompositeAppDriver.startUp.equalsIgnoreCase("SAFARI")) 
							wait.until(ExpectedConditions.refreshed(ExpectedConditions.stalenessOf(webDriver.findElement(By.xpath(sub_dept_xpath)))));						
						SleepUtils.getInstance().sleep(TimeSlab.MEDIUM);
						if (CompositeAppDriver.startUp.equalsIgnoreCase("SAFARI")) 
							wait.until(ExpectedConditions.refreshed(ExpectedConditions.presenceOfElementLocated(By.xpath(objMap.getLocator("txtItemBreadCrumb")))));
						else
							wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(objMap.getLocator("txtItemBreadCrumb"))));	
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
					wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(objMap.getLocator("imgmediaSection"))));
				}	
			}
		} catch (Exception e) {
			e.printStackTrace();
			RESULT.failed("Exception block hit", "Exception occurred", "Exception"+e.toString());
		}
	}


	/* Zipcode and Header Dropdown check */

	public void HeaderDropdown(DataRow input, DataRow output)throws InterruptedException, FindFailed
	{
		//Verify Zipcode
		String ZipCode = webDriver.findElement(By.xpath(objMap.getLocator("strzipcode"))).getText();
		if(uiDriver.Zipcode.equals(ZipCode))
		{
			RESULT.passed("Zipcode check", "Zipcode at header should reflect the previously ordered address",
			"Zipcode at header is as per the previously ordered address");
		}
		else
		{
			RESULT.warning("Zipcode check", "Zipcode at header should reflect the previously ordered address",
			"Zipcode at header is not as per the previously ordered address");
		}
		uiDriver.click("drpheaderDropdown");
		// Check for 'Home Delivery' section
		String Label1 = webDriver.findElement(By.xpath(objMap.getLocator("strhomeDelivery"))).getAttribute("label");
		if(Label1.equals("Home Delivery"))
		{
			RESULT.passed("HeaderDropdown", "'Home Delivery' section should be available in Header dropdown",
			"'Home Delivery' section is available in Header dropdown");
		}
		else
		{
			RESULT.failed("HeaderDropdown", "'Home Delivery' section should be available in Header dropdown",
			"'Home Delivery' section is not available in Header dropdown");
		}
		// Check for 'Office Delivery' section
		String Label2 = webDriver.findElement(By.xpath(objMap.getLocator("strofficeDelivery"))).getAttribute("label");
		if(Label2.equals("Office Delivery"))
		{
			RESULT.passed("HeaderDropdown", "'Office Delivery' section should be available in Header dropdown",
			"'Office Delivery' section is available in Header dropdown");
		}
		else
		{
			RESULT.failed("HeaderDropdown", "'Office Delivery' section should be available in Header dropdown",
			"'Office DeliveryHome Delivery' section is not available in Header dropdown");
		}
		// Check for 'Pickup' section
		String Label3 = webDriver.findElement(By.xpath(objMap.getLocator("strpickup"))).getAttribute("label");
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
			try{
				uiDriver.clickFromData(arr1[i]);
				try{
					if(arr1[i].equals("lnkyourAccount"))
					{
						uiDriver.selenium.waitForPageToLoad(PageLoadTime);
						uiDriver.wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(objMap.getLocator("lnkpresidentAnonymous"))));
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
					}
					else
					{
						uiDriver.wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(objMap.getLocator(path1[i]))));
						SleepUtils.getInstance().sleep(TimeSlab.YIELD);
						uiDriver.DrawHighlight(path1[i], webDriver);
						RESULT.passed(arr1[i].substring(3).toUpperCase(), arr1[i].substring(3).toUpperCase()+" :link should redirect to respective page",
								arr1[i].substring(3).toUpperCase()+" :link redirected to respective page");
						if(!(arr1[i].equals("lnkHome")))
						webDriver.navigate().back();
						SleepUtils.getInstance().sleep(TimeSlab.YIELD);
					}

					//					}
				}catch (Exception e) {
					e.printStackTrace();
					RESULT.failed(arr1[i].substring(3).toUpperCase(), arr1[i].substring(3).toUpperCase()+" :link should redirect to respective page", arr1[i].substring(3).toUpperCase()+" :link could not redirected to respective page");
				}
			}catch (Exception e) {
				e.printStackTrace();
				RESULT.failed(arr1[i], arr1[i]+" should pass", arr1[i]+"Failed");
			}
		}

		//for links where link opens a new tab in browser 
		for(int i=0;i<arr2.length;i++)
		{
			webDriver.navigate().refresh();
			uiDriver.clickFromData(arr2[i]);
			SleepUtils.getInstance().sleep(TimeSlab.YIELD);
			try{
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
				uiDriver.selenium.waitForPageToLoad(PageLoadTime);
				uiDriver.wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(objMap.getLocator(path2[i]))));
				uiDriver.DrawHighlight(path2[i], webDriver);
				RESULT.passed(arr2[i].substring(3).toUpperCase(), arr2[i].substring(3).toUpperCase()+" :link should redirect to respective page", arr2[i].substring(3).toUpperCase()+" :link redirected to respective page");
			}catch (Exception e) {
				e.printStackTrace();
				RESULT.failed(arr2[i].substring(3).toUpperCase(), arr2[i].substring(3).toUpperCase()+" :link should redirect to respective page", arr2[i].substring(3).toUpperCase()+" :link could not redirected to respective page");
			}
			try{
				webDriver.switchTo().window(webDriver.getWindowHandle()).close();
				webDriver.switchTo().window(winHandleBefore);
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}