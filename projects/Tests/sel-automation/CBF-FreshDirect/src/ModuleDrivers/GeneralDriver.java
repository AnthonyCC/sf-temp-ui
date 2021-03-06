package ModuleDrivers;

import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


import cbf.engine.BaseModuleDriver;
import cbf.engine.TestResultLogger;
import cbf.engine.TestResult.ResultType;
import cbf.utils.DBUtils;
import cbf.utils.DataRow;
import cbf.utils.SleepUtils;
import cbf.utils.SleepUtils.TimeSlab;

public class GeneralDriver extends BaseModuleDriver {

	WebDriverWait wait = new WebDriverWait(webDriver, 60);
	public GeneralDriver(TestResultLogger resultLogger) {
		super(resultLogger);
		// TODO Auto-generated constructor stub
	}

	// Launch Store front
	public void LaunchApp(DataRow input, DataRow output) {
		uiDriver.launchApplication(input.get("url"));
		//if (webDriver.getTitle().equals("FreshDirect")) {
		if (webDriver.getTitle().contains("FreshDirect") && webDriver.findElement(By.linkText(objMap.getLocator("lnkprivacyPolicy"))).isDisplayed()) {
			if(webDriver.findElements(By.className(objMap.getLocator("btnlogout"))).size()>0){
				if (uiDriver.isDisplayed("btnlogout")) {
					Logout(input, output);	
				}
			}else if(webDriver.findElements(By.className(objMap.getLocator("btnlogout"))).size()<0 && 
					webDriver.findElements(By.className(objMap.getLocator("btnlogin"))).size()<0){
				uiDriver.click("imgfd_Logo");
				Logout(input, output);
			}
			RESULT.passed("Launching the Application",
					"Should open the Application",
			"Application opened successfully!");
			try{
				if(webDriver.findElements(By.xpath(objMap.getLocator("lnkclickHere"))).size() > 0)
				{
					uiDriver.click("lnkclickHere");	
					uiDriver.wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(objMap.getLocator("lnkclickHere"))));	
					uiDriver.waitForPageLoad();
				}	
			}
			catch (Exception e) {
				RESULT.warning("New user login", "Media section should be visible", "Media section is not visible");

			}
		} else {
			RESULT.log("Launching the Application", ResultType.FAILED,
					"Should open the Application",
					"Error in opening the Application", true);
		}
	}

	// Launch CRM
	public void LaunchCRM(DataRow input, DataRow output) {
		try {
			uiDriver.launchApplication(input.get("url"));
			try{
				uiDriver.waitForPageLoad();
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.className(objMap.getLocator("btnCRMLogin"))));
			}catch(Exception e){
				try{
					wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(objMap.getLocator("lnklogout_CRM"))));
				}catch (Exception e1) {
					RESULT.failed("Launching the CRM Application",
							"CRM should be launched successfully and Login button should be available",
					"CRM launching failed as login button is not available");		
				}	
			}
			if (webDriver.findElements(By.xpath(objMap.getLocator("lnklogout_CRM"))).size()>0 || 
					webDriver.findElements(By.className(objMap.getLocator("btnCRMLogin"))).size()>0) {
				RESULT.passed("Launching the CRM Application",
						"Should open the Application",
				"Application opened sucessfully!");
			} else {
				RESULT.failed("Launching the CRM Application",
						"Should open the Application",
				"Error in opening the Application");
			}
		} catch (Exception e) {
			RESULT.failed("Launching the CRM Application",
					"CRM should be launched successfully",
			"CRM launching failed");
		}
	}

	// Log in to the Storefront
	@SuppressWarnings("deprecation")
	public void Login(DataRow input, DataRow output) throws InterruptedException
	{
		if(webDriver.findElements(By.xpath(objMap.getLocator("DownArwHdr"))).size()>0)
		{
			if (uiDriver.isDisplayed("DownArwHdr")) 
			{
				Logout(input, output);	
			}
		//}
		//else if(webDriver.findElements(By.xpath(objMap.getLocator("DownArwHdr"))).size()<0 && webDriver.findElements(By.className(objMap.getLocator("btnlogin1"))).size()<0)
		//{
		//	uiDriver.click("imgfd_Logo");
		//	Logout(input, output);
		//}
		//webDriver.findElement(By.className(objMap.getLocator("btnlogin1"))).click();
		uiDriver.wait.until(ExpectedConditions.visibilityOfElementLocated(By.className(objMap.getLocator("btnlogin1"))));
		uiDriver.FD_login(input.get("userID"), input.get("password"));
		// wait for logout button
		try
		{
			uiDriver.wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(objMap.getLocator("DownArwHdr"))));
			if (uiDriver.isDisplayed("DownArwHdr")) {
				RESULT.passed("Login", 
						"Should be Logged in Successfully ",
						"Logged in successfully with  username  " +input.get("userID") +" and  Password " +input.get("password") );
			} else {
				RESULT.failed("User login", 
						uiDriver.fd_username +" should be logged in successfully",
						uiDriver.fd_username+" failed to log in");
			}
		}
		catch(Exception e)
		{
			RESULT.failed("Login ","Header downward arrow should be available","Header downward arrow is not available");
		}

		// set the alcohol alert flag to false as user logged in and its new session 
		uiDriver.item_alcohol_accepted = false;

		//Merge Cart block
		try {
			if (webDriver.findElements(By.xpath(objMap.getLocator("strmergeCart1"))).size()>0) {
				String str=webDriver.findElement(By.xpath(objMap.getLocator("strmergeCart1"))).getText();
				if (str.contains("You already have items")) {
					List<WebElement> li = webDriver.findElements(By.xpath(objMap.getLocator("radgrpMergeCart")));
					for (WebElement we : li) {
						String str1=we.getAttribute("value");
						if (str1.equalsIgnoreCase("merge")) {
							we.click();
							if (we.isSelected()) 
							{
								RESULT.passed("Merge radio button should be selected", 
										"Should select Merge radio button", "Merge radio button selected");
								uiDriver.DrawHighlight("btncontinueMerge", webDriver);
								webDriver.findElement(By.name(objMap.getLocator("btnmergeCartContinue"))).click();
								uiDriver.waitForPageLoad();
								uiDriver.wait.until(ExpectedConditions.invisibilityOfElementLocated(By.name(objMap.getLocator("btnmergeCartContinue"))));
								uiDriver.waitForPageLoad();
								if(webDriver.findElement(By.xpath(objMap.getLocator("btnyourCart"))).isEnabled()){
									RESULT.passed("Select merge & continue", 
											"Should select Merge & continue", "Merge selected & Clicked on continue");
									break;
								}
								else
								{
									RESULT.failed("Click on Continue", 
											"Should click on Continue", "Failed to click on continue");
								}
							} 
							else 
							{
								RESULT.failed("Select merge & continue", 
										"Should select Merge & continue", "Merge is not selected");
							}
						} else {
							RESULT.failed("Merge Old Or New", 
									"Input should be Merge Old Or New", "Input other than Merge Old Or New");
						}
					}
				}

			} /*else {
				RESULT.passed("Merging window handling", "Shouldnt show merging window", "merging window is not displayed");
				System.out.println("No previous products in cart");
			}*/

		} catch (Exception e) {
			RESULT.failed("Merge cart", "Cart should be successfully merged", "Cart is unable to merge successfully");
		}

		}
	}

	// Logout from the Storefront
	public void Logout(DataRow input, DataRow output) {
		try
		{
			//if (!(webDriver.findElements(By.className(objMap.getLocator("btnlogout"))).size()>0)) {
				//if (webDriver.findElements(By.name(objMap.getLocator("imgfd_Logo"))).size()>0){
				//	uiDriver.click("imgfd_Logo");
				//	uiDriver.wait.until(ExpectedConditions.invisibilityOfElementLocated(By.name(objMap.getLocator("imgfd_Logo"))));
				//}else if(webDriver.findElements(By.className(objMap.getLocator("imgfdLogoCheckout"))).size()>0){
				//	uiDriver.click("imgfdLogoCheckout");
				//	uiDriver.wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className(objMap.getLocator("imgfdLogoCheckout"))));
				//}
			//}			
			// verify logout button is there or not
			if (uiDriver.isDisplayed("DownArwHdr")) {
				// click on logout button
				if (CompositeAppDriver.startUp.equalsIgnoreCase("IE"))
				{
					uiDriver.getwebDriverLocator(objMap.getLocator("DownArwHdr")).sendKeys("\n");
					uiDriver.getwebDriverLocator(objMap.getLocator("btnlogout1")).sendKeys("\n");
					uiDriver.waitForPageLoad();
				}				
				else
				{
				//uiDriver.robot.moveToElement(webDriver.findElement(By.className(objMap.getLocator("DownArwHdr"))));
			    uiDriver.click("DownArwHdr");
				uiDriver.click("btnlogout1");
				}
				// verify login button appears or not
				try{
					uiDriver.wait.until(ExpectedConditions.visibilityOfElementLocated(By.className(objMap.getLocator("btnlogin1"))));
					RESULT.passed("Logout", 
							"Should be Logged out Successfully",
					"Logged out successfully");
					SleepUtils.getInstance().sleep(TimeSlab.YIELD);
				}catch(Exception e){
					RESULT.failed("Logout", 
							"Should be Logged out Successfully",
					"Could not logout. Log out is not visible");
				}

			} else {
				RESULT.failed("Logout", 
						"Should be Logged out Successfully",
				"Failed to Log out as no logout button available");
			}
		}
		catch(Exception e)
		{
			RESULT.failed("Logout component", "Log out component should be successful", "Log out is not successful");
		}

	}

	// Log in to the CRM Application
	public void LoginCRM(DataRow input, DataRow output)
	throws InterruptedException {
		try{
			try{
				if (webDriver.findElements(By.xpath(objMap.getLocator("lnklogout_CRM"))).size()>0 && 
						uiDriver.isDisplayed("lnklogout_CRM"))
				{
					// click on logout link
					uiDriver.click("lnklogout_CRM");
					uiDriver.waitForPageLoad();
					// verify logout button disappears or not
					uiDriver.wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(objMap.getLocator("lnklogout_CRM"))));

				}
			}catch (Exception e) {
				RESULT.failed("CRM Log out for Fresh Log in", "User should be logged out successfully", 
				"Log out unsuccessful");
				return;
			}
			if(webDriver.findElements(By.className(objMap.getLocator("btnCRMLogin"))).size()>0
					&& uiDriver.isDisplayed("btnCRMLogin"))
			{
				try{
					uiDriver.FD_loginCRM(input.get("UserID"), input.get("Password"));
				}catch (Exception e) {
					RESULT.failed("CRM Log in", "User should be logged in successfully", 
					"Log in unsuccessful");
					return;
				}

			}else
			{
				RESULT.failed("CRM Log in", "Log in button should be available", "Log in button is not available on page");
				return;
			}
			// click on home if the landing page is not home
			if (!webDriver.getTitle().contains(input.get("pageName"))) {
				// click on Home link
				uiDriver.click("lnkhome");
				uiDriver.waitForPageLoad();
				try{
					uiDriver.wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText(objMap.getLocator("lnknewCustomer"))));
				}catch(Exception e)
				{
					RESULT.failed("CRM Homepage","Homepage Should be displayed","Homepage is not displayed");
				}
				//SleepUtils.getInstance().sleep(TimeSlab.MEDIUM);
				if (webDriver.getTitle().contains(input.get("pageName"))) {
					RESULT.passed("LoginCRM", 
							"Should be Logged in Successfully",
					"Logged in successfully");
				} else {
					RESULT.failed("LoginCRM", 
							"Should be Logged in Successfully",
					"Failed to Log in");
				}
			}else{
				RESULT.passed("LoginCRM", 
						"Should be Logged in Successfully",
				"Logged in successfully");
			}

		}catch(Exception e){
			RESULT.failed("Login CRM component", "Login CRM component should be successful", "Login CRM is failed");
		}
	}

	// Logout from the CRM
	public void LogoutCRM(DataRow input, DataRow output) {

		try{
			if (uiDriver.isDisplayed("lnklogout_CRM")) {
				// click on logout link
				uiDriver.click("lnklogout_CRM");
				// verify login button appears or not
				if (uiDriver.isDisplayed("btnCRMLogin")) {
					RESULT.passed("LogoutCRM", 
							"Should be Logged out Successfully",
					"Logged out successfully");
				} else {
					RESULT.failed("LogoutCRM", 
							"Should be Logged out Successfully",
					"Could not logout. Log in is not visible");
				}
			} else {
				RESULT.failed("Logout", 
						"Should be Logged out Successfully",
				"Failed to Log out as no logout link available");
			}

		}catch(Exception e){
			RESULT.failed("Logout CRM component", "Logout CRM component should be successful", "Logout CRM is failed");
		}
	}


	public void YourAccount(DataRow input, DataRow output)
	throws InterruptedException {

		try
		{
			try
			{
				uiDriver.click("lnkyouraccountlink");
				uiDriver.waitForPageLoad();
				//wait for Your Account page
				uiDriver.wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(objMap.getLocator("lnkpresidentAnonymous1"))));
			}
			catch(Exception e)
			{
				RESULT.failed("Your Account ", "Your account link should be successfully clicked", "Your Account link is not clicked successfully");
			}
			//click on Your Account link

			//Verifying user is logged in or not
			if(webDriver.findElements(By.xpath(objMap.getLocator("btngoAnonymous"))).size()>0)
			{
				uiDriver.FD_login(input.get("userID"), input.get("password"));	
			}
			try
			{
				uiDriver.wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(objMap.getLocator("lnkaccountCredits"))));
			}
			catch(TimeoutException e)
			{
				RESULT.warning("Your Account : Link credit wait",
						"Account credit link should be available", "Account credit link is not available");
			}

			//			SleepUtils.getInstance().sleep(TimeSlab.HIGH);
			//verifying Your Account page
			if(webDriver.findElements(By.xpath(objMap.getLocator("lnkyourOrders"))).size() > 0)
			{
				RESULT.passed("Your Account link navigation","Page should be redirected to Your Account","Page is redirected to Your Account");
			}
			else
			{
				RESULT.failed("Your Account link navigation","Page should be redirected to Your Account","Page is not redirected to Your Account");
			}
			//check if customer is of VIP type
			/*DBUtils connect=new DBUtils("devint"); 
			List<Map> list1=connect.runQueryOr("devint","select p.profile_value from cust.profile p,cust.fdcustomer fc,CUST.CUSTOMER c where c.id=fc.ERP_CUSTOMER_ID and p.customer_id = fc.id and p.profile_name='VIPCustomer' and c.user_id="+uiDriver.user);
			String profile=list1.get(0).values().toString();
			connect.disconnect();
			System.out.println("ust type:"+profile);
			if(profile.equalsIgnoreCase("true"))
			{
				if(input.get("select_link").equalsIgnoreCase("reserveDeliveryTime")){
				if(webDriver.findElements(By.xpath(objMap.getLocator("lnkreserveDeliveryTime"))).size()>0)
				{
					RESULT.passed("Reserve a time slot for VIP customer", "VIP Customer should be able to reserve a time slot",
							"Reserve a delivery time link is available for VIP customer");
				}else
					RESULT.failed("Reserve a time slot for VIP customer", "VIP Customer should be able to reserve a time slot",
					"Reserve a delivery time link is not available for VIP customer");
				}
			}
			else
			{
				if(input.get("select_link").equalsIgnoreCase("reserveDeliveryTime")){
				if(webDriver.findElements(By.xpath(objMap.getLocator("lnkreserveDeliveryTime"))).size()>0)
				{
					RESULT.failed("Reserve a time slot for VIP customer", "VIP Customer should not be able to reserve a time slot",
							"Reserve a delivery time link is available for VIP customer");
				}else
					RESULT.passed("Reserve a time slot for VIP customer", "VIP Customer should not be able to reserve a time slot",
					"Reserve a delivery time link is not available for VIP customer");
				}

			}*/
			//Go to particular desired link
			uiDriver.clickFromData("lnk"+input.get("select_link"));
			uiDriver.waitForPageLoad();
			//SleepUtils.getInstance().sleep(TimeSlab.YIELD);
			try
			{
				uiDriver.wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(objMap.getLocator("lnkreorderPastOrder"))));
			}
			catch(Exception e)
			{
				RESULT.failed("Navigation to link lnk"+input.get("select_link"), " page should not be navigated to lnk"+input.get("select_link")+"page", "lnk"+input.get("select_link")+" page is not available");
				return;
			}

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
		catch(Exception e)
		{
			RESULT.failed("Your Account Exception","Your account check should be successful ",
			"Your Account check is not successful");
		}
	}

	public void CreateNewCustomer(DataRow input, DataRow output)
	throws InterruptedException {

		try{
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
			output.put("userID", uiDriver.UserID);
			output.put("password", uiDriver.Password);
		}catch(Exception e){
			RESULT.failed("Create new customer CRM component", "Create new customer CRM component should be successful", "Create new customer CRM is failed");
		}
	}

	public void SignUp(DataRow input, DataRow output)
	throws InterruptedException, TimeoutException  {
		try {
			if(webDriver.findElements(By.className(objMap.getLocator("btnlogin1"))).size()>0)
			{
				if (uiDriver.isDisplayed("btnlogout1")) {
					Logout(input, output);
				}
			}
			//else if(webDriver.findElements(By.className(objMap.getLocator("btnlogout"))).size()<0 && webDriver.findElements(By.className(objMap.getLocator("btnlogin"))).size()<0)
			//{
				//uiDriver.click("imgfd_Logo");
				//Logout(input, output);
			//}
			uiDriver.FD_SignUP(input.get("Firstname"), input.get("Lastname"),
					input.get("ZipCode"), input.get("ServiceType"), input
					.get("Email"), input.get("ConfirmEmail"), input
					.get("PassWord1"), input.get("ConfirmPassWord1"),
					input.get("SecurityQuestion"), input.get("CompanyName"),
					input.get("CompanyContactNum"), input.get("CompanyFloor"),
					input.get("CompanyAddress"), input.get("CompanyCity"),
					input.get("CompanyState"), input.get("CompanyZipcode"));
			//uiDriver.waitForPageLoad();
			//uiDriver.wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className(objMap.getLocator("SgnUp"))));
			//uiDriver.wait.until(ExpectedConditions.visibilityOfElementLocated(By.className(objMap.getLocator("WelcomeMsg"))));
			if (webDriver.findElements(By.xpath(objMap.getLocator("DownArwHdr"))).size()>0) { 
				uiDriver.isElementPresent(By.xpath(objMap.getLocator("DownArwHdr")));
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
			RESULT.failed("SignUp",
					"User should be successfully logged in to the Application and navigated to Home Page",
			"Unable to SignUp successfully");
		}
	}

	public void LanguageDetection(DataRow input, DataRow output)
	throws InterruptedException, TimeoutException  {
		try {
			if(uiDriver.isDisplayed("lnkprivacyPolicy")){
				/*//Click on Help link
				uiDriver.click("lnkhelpLink");
				uiDriver.waitForPageLoad();*/
				//Click on Pricacy Policy link at bottom of page
				uiDriver.click("lnkprivacyPolicy");
				uiDriver.waitForPageLoad();
				//Verify language
				uiDriver.FD_viewSourceLang();
				//Click on ESPANOL to change the language
				uiDriver.click("btnENESPANOL");
				uiDriver.waitForPageLoad();
				//SleepUtils.getInstance().sleep(TimeSlab.MEDIUM);
				//Verify language
				uiDriver.FD_viewSourceLang();
			}	
			else
			{
				RESULT.failed("Help Link","Help link should be available","Help link is not available");
				return;
			}
		} catch (Exception e) {
			RESULT.failed("Language detection exception", "Language detection should be successful",
			"Language detection is not successful");
			return;
		}
	}

	public void ProductFilters(DataRow input, DataRow output)
	{
		try {
			uiDriver.FD_filters(input.get("FilterName"));
		}catch (InterruptedException e) {
			// TODO Auto-generated catch block
			RESULT.failed("Product Filters Find failed exception: ", "Product filters should be successful", "Product filters is not successful");
		}
	}

	public void PasswordValidation(DataRow input, DataRow output){
		try {
			if(webDriver.findElements(By.className(objMap.getLocator("btnsignup"))).size()>0){
				uiDriver.click("btnsignup");
				uiDriver.waitForPageLoad();

				try{
					uiDriver.wait.until(ExpectedConditions.visibilityOf(uiDriver.getwebDriverLocator(objMap.getLocator("frameiframeSignup"))));
					webDriver.switchTo().frame(uiDriver.getwebDriverLocator(objMap.getLocator("frameiframeSignup")));
					SleepUtils.getInstance().sleep(TimeSlab.YIELD);
				}catch (Exception e) {
					RESULT.failed("SignUp Page", "SignUp Page should be available", "SignUp Page is not available");
					return;
				}

				try{
					uiDriver.wait.until(ExpectedConditions.visibilityOfElementLocated(By.name(objMap.getLocator("txtpassWord1"))));
				}catch (Exception e) {
					RESULT.failed("SignUp Page password field", "SignUp Page password field should be available", "SignUp Page password field is not available");
					return;
				}				


				String PWD[] = input.get("password").split("\n");
				for (int i = 0; i < PWD.length; i++) {
					uiDriver.setValue("txtpassWord1",PWD[i]);
					String Pwd=PWD[i];
					uiDriver.FD_passwordValidation(Pwd);
				}

				webDriver.switchTo().defaultContent();
				webDriver.findElement(By.xpath(objMap.getLocator("btnsignupClose"))).click();
				//SleepUtils.getInstance().sleep(TimeSlab.YIELD);		
				return;
			}
		} catch (Exception e) {
			RESULT.failed("SignUp Page", "SignUp Page should be available", "SignUp Page is not available");
			return;
		}
		try {
			if(webDriver.findElements(By.xpath(objMap.getLocator("txtaccountPref"))).size()>0){
				String PWD[] = input.get("password").split("\n");
				for (int i = 0; i < PWD.length; i++) {
					uiDriver.setValue("txtAccPrefPwd",PWD[i]);
					String Pwd=PWD[i];
					uiDriver.FD_passwordValidation(Pwd);
				}
				return;
			}
		} catch (Exception e) {
			RESULT.warning("Account pref Page", "Account pref Page should be available", "Account pref Page is not available");
			return;
		}
		RESULT.failed("Page not found error","Signup/Your acc pref page should be present","Signup/Your acc pref page is present");
	}

}