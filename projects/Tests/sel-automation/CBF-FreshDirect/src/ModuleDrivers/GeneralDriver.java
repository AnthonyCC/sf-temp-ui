package ModuleDrivers;

import java.util.List;
import java.util.concurrent.TimeoutException;

import org.apache.james.mime4j.message.Message;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.sikuli.script.FindFailed;

import com.thoughtworks.selenium.Selenium;

import ui.WebUIDriver;

import cbf.engine.BaseModuleDriver;
import cbf.engine.TestResultLogger;
import cbf.engine.TestResult.ResultType;
import cbf.utils.DataRow;
import cbf.utils.SleepUtils;
import cbf.utils.SleepUtils.TimeSlab;

public class GeneralDriver extends BaseModuleDriver {

	public GeneralDriver(TestResultLogger resultLogger) {
		super(resultLogger);
		// TODO Auto-generated constructor stub
	}
	
	// Launch Storefront
	public void LaunchApp(DataRow input, DataRow output) {
		uiDriver.launchApplication(input.get("url"));
		//if (webDriver.getTitle().equals("FreshDirect")) {
		if (webDriver.getTitle().contains("FreshDirect")) {
			RESULT.passed("Launching the Application",
					      "Should open the Application",
					      "Application opened sucessfully!");
			if(webDriver.findElements(By.className(objMap.getLocator("btnsignup"))).size() <= 0)
			{
				uiDriver.click("lnkclickHere");
				uiDriver.wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(objMap.getLocator("btnsignup"))));
				uiDriver.selenium.waitForPageToLoad("5000");
			}		
		} else {
			RESULT.log("Launching the Application", ResultType.FAILED,
					   "Should open the Application",
					   "Error in opening the Application", true);
		}
	}

	// Log in to the Storefront
	@SuppressWarnings("deprecation")
	public void Login(DataRow input, DataRow output)
	throws InterruptedException, FindFailed {
		uiDriver.FD_login(input.get("userID"), input.get("password"));
		// wait for logout button
		uiDriver.wait.until(ExpectedConditions.visibilityOfElementLocated(By.className(objMap.getLocator("btnlogout"))));
		if (uiDriver.isDisplayed("btnlogout")) {
			RESULT.passed("Login", 
					"Should be Logged in Successfully",
			"Logged in successfully");
		} else {
			RESULT.failed("Login", 
					"Should be Logged in Successfully",
			"Failed to Log in");
		}

		try {
			if (webDriver.findElements(By.xpath("//font[@class='title18']")).size()>0) {
				String str=webDriver.findElement(By.xpath("//font[@class='title18']")).getText();
				if (str.contains("You already have items")) {
					List<WebElement> li = webDriver.findElements(By.xpath("//input[@type='radio']"));
					for (WebElement we : li) {
						String str1=we.getAttribute("value");
						if (str1.equalsIgnoreCase("merge")) {
							we.click();
							if (we.isSelected()) {
								RESULT.passed("Merge radio btn should be selected", 
										"Should select Merge radio btn", "Merge radio btn selected");
								uiDriver.DrawHighlight("btncontinueMerge", webDriver);
								Actions Continue = new Actions(webDriver);
								Continue.sendKeys(Keys.ENTER).build().perform();
								WebUIDriver.selenium.waitForPageToLoad(uiDriver.PageLoadTime);
								if(webDriver.findElement(By.xpath(objMap.getLocator("btnyourCart"))).isEnabled()){
								RESULT.passed("Select merge & continue", 
										"Should select Merge & continue", "Merge selected & Clicked on continue");
								break;
								}else{
									RESULT.failed("Click on Continue", 
											"Should click on Continue", "Failed to click on continue");
								}
							} else {
								RESULT.failed("Select merge & continue", 
										"Should select Merge & continue", "Merge is not selected");
							}
						} else {
							RESULT.failed("Merge Old Or New", 
									"Input should be Merge Old Or New", "Input otherthan Merge Old Or New");
						}
					}
				}
				
			} /*else {
				RESULT.passed("Merging window handling", "Shouldnt show merging window", "merging window is not displayed");
				System.out.println("No previous products in cart");
			}*/
				
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Logout from the Storefront
	public void Logout(DataRow input, DataRow output) {
		if (!uiDriver.isDisplayed("btnlogout")) {
			uiDriver.click("imgfd_Logo");
		}			
		// verify logout button is there or not
		if (uiDriver.isDisplayed("btnlogout")) {
			// click on logout button
			uiDriver.click("btnlogout");
			// verify login button appears or not
			try{
                uiDriver.wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className("btnlogout")));        
                RESULT.passed("Logout", 
                              "Should be Logged out Successfully",
                              "Logged out successfully");
                SleepUtils.getInstance().sleep(TimeSlab.YIELD);
            }catch(Exception e){
                RESULT.failed("Logout", 
                          "Should be Logged out Successfully",
                          "Could not logout. Log in is not visible");
            }

		} else {
			RESULT.failed("Logout", 
					      "Should be Logged out Successfully",
					      "Failed to Log out as no logout button available");
		}
	}


	public void YourAccount(DataRow input, DataRow output)
	throws InterruptedException, FindFailed {
		//click on Your Account link
		uiDriver.click("lnkyouraccountlink");
		//wait for Your Account page
		uiDriver.wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(objMap.getLocator("lnkpresidentAnonymous"))));
		//Verifying user is logged in or not
		if (webDriver.findElements(By.xpath(objMap.getLocator("btngoAnonymous"))).size() > 0) {
			uiDriver.setValue("txtuserNameAnonymous", input.get("userID"));
			uiDriver.setValue("txtuserPwdAnonymous", input.get("password"));
			SleepUtils.getInstance().sleep(TimeSlab.YIELD);
			uiDriver.click("btngoAnonymous");
			SleepUtils.getInstance().sleep(TimeSlab.HIGH);
		} 
		uiDriver.wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(objMap.getLocator("lnkaccountCredits"))));
		SleepUtils.getInstance().sleep(TimeSlab.HIGH);
		//verifying Your Account page
		if(webDriver.findElements(By.xpath(objMap.getLocator("lnkyourOrders"))).size() > 0)
		{
			RESULT.passed("Your Account link navigation","Page should be redirected to Your Account","Page is redirected to Your Account");
		}
		else
		{
			RESULT.failed("Your Account link navigation","Page should be redirected to Your Account","Page is not redirected to Your Account");
		}
		//Go to particular desired link
		uiDriver.clickFromData("lnk"+input.get("select_link"));
		uiDriver.wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(objMap.getLocator("lnkreorderPastOrder"))));
		SleepUtils.getInstance().sleep(TimeSlab.YIELD);
		//verifying particular page 
		if (webDriver.findElements(By.xpath(objMap.getLocator("lnkreorderPastOrder"))).size() <1) {
			RESULT.passed(input.get("select_link")+" :link",
					"Page should be navigated to:"+ input.get("select_link")+" link",
			"Successful page navigation to:"+input.get("select_link")+" link");
		} else {
			RESULT.failed(input.get("select_link")+" :link",
					"Page should be navigated to:"+ input.get("select_link")+" link",
			"Unsuccessful page navigation to:"+input.get("select_link")+" link");
		}

	}

	public void CreateNewCustomer(DataRow input, DataRow output)
			throws InterruptedException, FindFailed {
		// call to the new customer reusable function
		uiDriver.FD_newCustomerCRM(input.get("CustomerType"),
				input.get("Zip"), input.get("Title"), input.get("FirstName"),
				input.get("MiddleName"), input.get("LastName"), input
						.get("HomePhone"), input.get("HomeExt"), input
						.get("WorkPhone"), input.get("WorkExt"), input
						.get("CellPhone"), input.get("CellExt"), input
						.get("AlterEmail"), input.get("EmailCRM"), input
						.get("PassCRM"), input
						.get("TOB"), input.get("AddressType"), input
						.get("DeliveryFirstName"), input.get("DeliveryLastName"), input
						.get("DeliveryCompanyName"), input.get("Add1"), input
						.get("Add2"), input.get("AptName"), input.get("City"),
				input.get("State"), input.get("ZipCRM"), input.get("DeliveryPhone"),
				input.get("Instruction"), input.get("AlternateDelivery"),
				input.get("CardName"), input.get("CardType"), input
						.get("CardNumber"), input.get("CardMonth"), input
						.get("CardYear"), input.get("UseDelivery"), input
						.get("BilAdd1"), input.get("BilAdd2"), input
						.get("BilAptName"), input.get("BilCity"), input
						.get("BilState"), input.get("BilZipcode"), input
						.get("BilCountry"));
		String Verification = uiDriver.getwebDriverLocator(objMap.getLocator("strverify_Customer")).getText();
		System.out.println(Verification);
		// verifying the new created customer
		if (Verification.equalsIgnoreCase(input.get("EmailCRM"))) {
			System.out.println(Verification);
			RESULT.passed("New customer search",
					      "Should be able to serch customer",
					      "serch customer successfully");
		} else {
			RESULT.failed("New customer search",
					      "Should be able to serch customer",
					      "unable to search customer");
			return;
		}
	}

	public void SignUp(DataRow input, DataRow output)
			throws InterruptedException, TimeoutException, FindFailed {
		try {			
			uiDriver.FD_SignUP(input.get("Firstname"), input.get("Lastname"),
					input.get("ZipCode"), input.get("ServiceType"), input
							.get("Email"), input.get("ConfirmEmail"), input
							.get("PassWord1"), input.get("ConfirmPassWord1"),
					input.get("SecurityQuestion"), input.get("CompanyName"),
					input.get("CompanyContactNum"), input.get("CompanyFloor"),
					input.get("CompanyAddress"), input.get("CompanyCity"),
					input.get("CompanyState"), input.get("CompanyZipcode"));
			WebUIDriver.selenium.waitForPageToLoad(uiDriver.PageLoadTime);
			uiDriver.wait.until(ExpectedConditions.visibilityOfElementLocated(By.className(objMap.getLocator("btnlogout"))));
			if (uiDriver.isDisplayed("btnlogout")) {
				RESULT.passed("SignUp",
							  "User should be successfully logged in to the Application and navigated to Home Page",
							  "Logged in successfully in to the Application and navigated to Home Page");
			} else {
				RESULT.failed("SignUp",
							  "User should be successfully logged in to the Application and navigated to Home Page",
							  "Unable to Login to the Application");
				return;
			}
		} catch (Exception e) {
			//e.printStackTrace();
			RESULT.failed("SignUp",
					      "User should be successfully logged in to the Application and navigated to Home Page",
						  "Unable to SignUp successfully");
		}
	}

	public void ProductFilters(DataRow input, DataRow output)
	{
		try {
			uiDriver.FD_filters(input.get("FilterName"));
		} catch (FindFailed e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}