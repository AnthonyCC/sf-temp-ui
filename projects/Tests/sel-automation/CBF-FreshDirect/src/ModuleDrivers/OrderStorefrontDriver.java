package ModuleDrivers;

import java.awt.Point;
import java.awt.Robot;
import java.util.List;
import java.util.concurrent.TimeoutException;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.python.antlr.PythonParser.return_stmt_return;
import org.sikuli.script.FindFailed;
import org.testng.reporters.ExitCodeListener;

import com.thoughtworks.selenium.Selenium;

import ui.WebUIDriver;

import cbf.engine.BaseModuleDriver;
import cbf.engine.TestResultLogger;
import cbf.engine.TestResult;
import cbf.utils.DataRow;
import cbf.utils.SleepUtils;
import cbf.utils.SleepUtils.TimeSlab;

public class OrderStorefrontDriver extends BaseModuleDriver {

	static boolean flag = false;

	public OrderStorefrontDriver(TestResultLogger resultLogger) {
		super(resultLogger);
		// TODO Auto-generated constructor stub
	}
	/*
	 * Component for checkout and Your Cart page
	 */
	@SuppressWarnings("deprecation")
	public void Checkout(DataRow input, DataRow output) throws FindFailed,
	InterruptedException{
		try 
		{
			uiDriver.selenium.waitForPageToLoad(uiDriver.PageLoadTime);
			if (!webDriver.getTitle().contains("View Cart")) 
			{
				uiDriver.FD_viewCart();
			}

			if(webDriver.findElements(By.xpath(objMap.getLocator("btncheckOut"))).size()>0)
			{
				uiDriver.click("btncheckOut");
				//	uiDriver.selenium.waitForPageToLoad(uiDriver.PageLoadTime);
				//				SleepUtils.getInstance().sleep(TimeSlab.HIGH);
				uiDriver.wait.until(ExpectedConditions.stalenessOf(uiDriver.getwebDriverLocator(objMap.getLocator("btncheckOut"))));
				SleepUtils.getInstance().sleep(TimeSlab.YIELD);
				if (webDriver.getTitle().contains(input.get("PageName"))) 
				{
					RESULT.passed("Checkout Page","Page should be navigated to checkout","Successful navigation to checkout page");
					uiDriver.wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(objMap.getLocator("btnchangeDeliveryAddress"))));
				}
				else
				{
					if(webDriver.findElements(By.xpath(objMap.getLocator("strverifyWarningOnPlaced"))).size()>0)
					{
						String orderTotal=uiDriver.getwebDriverLocator(objMap.getLocator("strverifyWarningOnPlaced")).getText();
						RESULT.failed("Checkout Page","Page should be navigated to checkout",orderTotal);
						return;
					}
					else
						RESULT.failed("Checkout Page","Page should be navigated to checkout","Unsuccessful navigation to checkout page");
					return;
				}
			}
			else
				RESULT.failed("Check out button visibility","Check out button should be visible","Check out button is not visible");
			SleepUtils.getInstance().sleep(TimeSlab.YIELD);
			if(webDriver.findElements(By.xpath(objMap.getLocator("chkageVerificationAlcohol"))).size() > 0)
			{
				uiDriver.click("chkageVerificationAlcohol");
				uiDriver.click("btnsaveAlcohol");				
				uiDriver.wait.until(ExpectedConditions.stalenessOf(webDriver.findElement(By.xpath(objMap.getLocator("btnchangeDeliveryAddress")))));		
				((JavascriptExecutor) webDriver).executeScript("scroll(0, -250);");
				SleepUtils.getInstance().sleep(TimeSlab.YIELD);
			}
			// Alcohol Restriction Pop up for Time Slot			
			if (webDriver.findElements(By.xpath(objMap.getLocator("btnchangeTimeSlotAlcohol"))).size() > 0 && 
					webDriver.findElement(By.xpath(objMap.getLocator("btnchangeTimeSlotAlcohol"))).isDisplayed())
			{
				if (input.get("RestrictionHandle").equalsIgnoreCase("Remove")) {
					uiDriver.click("btnremoveProceed");
					RESULT.passed("Alcohol Restriction",
							"Alcohol Restriciton popup should be displayed",
					"Alcohol Restriciton popup is displayed and Alcohol item removed and proceeded");
				} 
				if (input.get("RestrictionHandle").equalsIgnoreCase("Change")) {
					uiDriver.click("btnchangeTimeSlotAlcohol");
					RESULT.passed("Alcohol Restriction",
							"Alcohol Restriciton popup should be displayed",
					"Alcohol Restriciton popup is displayed and Address changed");
					uiDriver.FD_chooseTimeslot(input.get("DeliveryDay1"), input.get("TimeSlot1"), input.get("FlexibilityFlag"));
					uiDriver.click("btnDoneTimeSlot");
					SleepUtils.getInstance().sleep(TimeSlab.MEDIUM);
					if(webDriver.findElements(By.xpath(objMap.getLocator("chkageVerificationAlcohol"))).size() > 0)
					{
						uiDriver.click("chkageVerificationAlcohol");
						uiDriver.click("btnsaveAlcohol");
					}
					if (webDriver.findElements(By.xpath(objMap.getLocator("btnchangeTimeSlotAlcohol"))).size() > 0 && 
							webDriver.findElement(By.xpath(objMap.getLocator("btnchangeTimeSlotAlcohol"))).isDisplayed())
					{
						uiDriver.click("btnremoveProceed");
						RESULT.passed("Alcohol Restriction",
								"Alcohol Restriciton popup should be displayed",
						"Alcohol Restriciton popup is displayed and Alcohol item removed and proceeded");
					}										
					uiDriver.wait.until(ExpectedConditions.stalenessOf(webDriver.findElement(By.xpath(objMap.getLocator("btnChangeTimeSlot")))));
					SleepUtils.getInstance().sleep(TimeSlab.YIELD);
				}
			} 
		}
		catch (Exception e) {
			RESULT.failed("Checkout",
					"User should be able to Checkout successfully",
					"Unable to Checkout!");
		}
	}

	/*
	 * Component to handle Delivery Address selection
	 */
	public void DeliveryAddress(DataRow input, DataRow output)
	throws FindFailed, InterruptedException {
		boolean change_address = true;
		boolean alcohol_popup = false;
		try {	
			if (webDriver.findElements(
					By.xpath(objMap.getLocator("btnchangeAddress"))).size() > 0 && webDriver.findElement(By.xpath(objMap.getLocator("btnchangeAddress"))).isDisplayed()) 
			{
				alcohol_popup = true;			
				change_address = uiDriver.FD_alcoholPopUp(input.get("AlcoholRestriction"));			
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		try{
			if (webDriver.getTitle().equals(input.get("PageName"))) {

				if (change_address) {
					uiDriver.click("btnchangeDeliveryAddress");
				}
				RESULT.passed("DeliveryAddress",
						"User should be able to navigate on Delivery Address page",
				"User is navigating to Delivery Address Page");
				uiDriver.FD_chooseDeliveryAddress(input.get("AddSelectDeleteEdit"),
						input.get("ServiceType"), input.get("AddressNickName"),
						input.get("FirstName"), input.get("LastName"), input
						.get("StreetAddress"), input.get("Apartment"),
						input.get("AddLine2"), input.get("City"), input
						.get("State"), input.get("ZipCode"), input
						.get("Contact1"), input.get("Ext1"), input
						.get("ContactType"), input.get("AddAltContact"),
						input.get("Contact2"), input.get("Ext2"), input
						.get("AltContactType"), input
						.get("SpclDelInstructions"), input
						.get("DoormanDelivery"), input
						.get("NeighbourDelivery"), input
						.get("AltFirstName"), input.get("AltLastName"),
						input.get("AltApt"), input.get("AltPhone"), input
						.get("AltExtn"), input.get("UnattendedDelivery"),
						input.get("UnattendedDelInstructions"), input
						.get("CompanyName"), input.get("SaveCancelBtn"),
						input.get("SelectAddress"), input.get("FlexibilityFlag"));	
				uiDriver.click("btndoneDelAddress");
				uiDriver.wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(objMap.getLocator("btndoneDelAddress"))));
			}
		}
		catch(Exception e)
		{
			RESULT.failed("Delivery Address",
					"User should be able to "+ input.get("AddSelectDeleteEdit")+ " delivery address",
			"Unable to redirect on appropriate page of delivery address");
			return;
		}
		try {	
			if (webDriver.findElements(
					By.xpath(objMap.getLocator("btnchangeAddress"))).size() > 0 && webDriver.findElement(By.xpath(objMap.getLocator("btnchangeAddress"))).isDisplayed()) 
			{
				alcohol_popup = true;				
				change_address = uiDriver.FD_alcoholPopUp(input.get("AlcoholRestriction"));	

				if(change_address == false){
					uiDriver.FD_chooseDeliveryAddress(input.get("AddSelectDeleteEdit"),
							input.get("ServiceType"), input.get("AddressNickName"),
							input.get("FirstName"), input.get("LastName"), input
							.get("StreetAddress"), input.get("Apartment"),
							input.get("AddLine2"), input.get("City"), input
							.get("State"), input.get("ZipCode"), input
							.get("Contact1"), input.get("Ext1"), input
							.get("ContactType"), input.get("AddAltContact"),
							input.get("Contact2"), input.get("Ext2"), input
							.get("AltContactType"), input
							.get("SpclDelInstructions"), input
							.get("DoormanDelivery"), input
							.get("NeighbourDelivery"), input
							.get("AltFirstName"), input.get("AltLastName"),
							input.get("AltApt"), input.get("AltPhone"), input
							.get("AltExtn"), input.get("UnattendedDelivery"),
							input.get("UnattendedDelInstructions"), input
							.get("CompanyName"), input.get("SaveCancelBtn"),
							input.get("SelectAddress1"), input.get("FlexibilityFlag"));					
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		uiDriver.robot.scrollToElement(uiDriver.getwebDriverLocator(objMap.getLocator("btndoneDelAddress")),webDriver);
		uiDriver.click("btndoneDelAddress");
		SleepUtils.getInstance().sleep(TimeSlab.LOW);
		try{
			List<WebElement> selectLst = webDriver.findElements(By.xpath(objMap.getLocator("lstradioPath")));
			int i=1;
			do{
				if(webDriver.findElement(By.xpath(objMap.getLocator("straddressError"))).isDisplayed());
				{
					String error=webDriver.findElement(By.xpath("//div[@data-drawer-content='address']/form/div[@fdform-error-container='']/span")).getText();
					RESULT.warning("warning for selected address", "Address should get selected", error);
				}
				i++;
				selectLst.get(i).click();
				uiDriver.robot.scrollToElement(uiDriver.getwebDriverLocator(objMap.getLocator("btndoneDelAddress")),webDriver);
				uiDriver.click("btndoneDelAddress");
				SleepUtils.getInstance().sleep(TimeSlab.LOW);

			}while(i<selectLst.size());

		}catch (Exception e) {
			uiDriver.wait.until(ExpectedConditions.visibilityOf(webDriver.findElement(By.xpath(objMap.getLocator("btnchangeDeliveryAddress")))));
		}
		//		uiDriver.wait.until(ExpectedConditions.stalenessOf(webDriver.findElement(By.xpath(objMap.getLocator("btnchangeDeliveryAddress")))));				
		uiDriver.wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(objMap.getLocator("btndoneDelAddress"))));
	}

	/*
	 * Component to handle Time slot selection
	 */
	@SuppressWarnings("null")
	public void TimeSlot(DataRow input, DataRow output) throws FindFailed,
	InterruptedException {
		try {
			String DeliveryDay=null;
			String TimeSlot=null;
			String TimeSlot1=null;
			String TimeSlot2=null;
			webDriver.navigate().refresh();
			//			WebUIDriver.selenium.waitForPageToLoad(uiDriver.PageLoadTime);
			if (webDriver.findElements(By.xpath(objMap.getLocator("btnChangeTimeSlot"))).size() > 0) {
				if (WebUIDriver.reserve_flag == true)
				{
					if(webDriver.findElements(By.xpath(objMap.getLocator("btnselectTime"))).size() > 0) {
						RESULT.failed("Select Timeslot button", "Select time slot button should not be available for already reserved time slot", 
								"Select Time slot button available for already Reserved time slot");
					}
					else
					{
						uiDriver.wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(objMap.getLocator("strdeliveryDay"))));
						DeliveryDay = webDriver.findElement(By.xpath(objMap.getLocator("strdeliveryDay"))).getText();
						System.out.println("Text:" + DeliveryDay);
						TimeSlot = webDriver.findElement(By.xpath(objMap.getLocator("strdeliveryTime"))).getText();
						System.out.println("Text:" + TimeSlot);
						TimeSlot = TimeSlot.replaceAll(" ", "");
						TimeSlot1 = TimeSlot.split("-")[0];
						TimeSlot2 = TimeSlot.split("-")[1];
						TimeSlot1 = TimeSlot1.replaceAll(("AM"), "");
						TimeSlot1 = TimeSlot1.replaceAll(("PM"), "");
						TimeSlot = TimeSlot1 + "-" + TimeSlot2;
						System.out.println("Selected time:" + TimeSlot);
						System.out.println("Selected day:"+DeliveryDay);
						if (TimeSlot.toLowerCase().contains(uiDriver.verify_time.toLowerCase())&& 
								DeliveryDay.toLowerCase().contains(uiDriver.verify_day.toLowerCase())) {
							RESULT.passed("Select Time Slot","Reserved Time Slot Should be selected",
									"Already Selected time slot is same as reserved time");
							return;
						}
						else
							RESULT.failed("Select Time Slot","Reserved Time Slot Shoud be selected",
							"Already Selected time slot is not same as reserved time");
					}
				}else if (webDriver.findElements(By.xpath(objMap.getLocator("btnselectTime"))).size() > 0) {  
					uiDriver.click("btnselectTime");
					uiDriver.FD_chooseTimeslot(input.get("DeliveryDay"), input.get("TimeSlot"), input.get("FlexibilityFlag"));
					uiDriver.click("btnDoneTimeSlot");
					try
					{
						uiDriver.wait.until(ExpectedConditions.visibilityOf(webDriver.findElement(By.xpath(objMap.getLocator("btnChangeTimeSlot")))));
					}
					catch(Exception e)
					{
						//						return;
						e.printStackTrace();
					}
					//					uiDriver.wait.until(ExpectedConditions.stalenessOf(webDriver.findElement(By.xpath(objMap.getLocator("btnChangeTimeSlot")))));
					SleepUtils.getInstance().sleep(TimeSlab.MEDIUM);
					if(webDriver.findElements(By.xpath(objMap.getLocator("chkageVerificationAlcohol"))).size() > 0)
					{
						uiDriver.click("chkageVerificationAlcohol");
						uiDriver.click("btnsaveAlcohol");
					}
					((JavascriptExecutor) webDriver).executeScript("scroll(0, -250);");
					SleepUtils.getInstance().sleep(TimeSlab.YIELD);    
					if(uiDriver.alcoholRestriction)
					{
						if (webDriver.findElements(By.xpath(objMap.getLocator("btnchangeTimeSlotAlcohol"))).size() > 0 && 
								webDriver.findElement(By.xpath(objMap.getLocator("btnchangeTimeSlotAlcohol"))).isDisplayed())
						{
							if (input.get("RestrictionHandle").equalsIgnoreCase("Remove")) {
								uiDriver.click("btnremoveProceed");
								RESULT.passed("Alcohol Restriction",
										"Alcohol Restriciton popup should be displayed",
								"Alcohol Restriciton popup is displayed and Alcohol item removed and proceeded");
							} 
							if (input.get("RestrictionHandle").equalsIgnoreCase("Change")) {
								uiDriver.click("btnchangeTimeSlotAlcohol");
								RESULT.passed("Alcohol Restriction",
										"Alcohol Restriciton popup should be displayed",
								"Alcohol Restriciton popup is displayed and Address changed");
								uiDriver.FD_chooseTimeslot(input.get("DeliveryDay1"), input.get("TimeSlot1"), input.get("FlexibilityFlag"));
								uiDriver.click("btnDoneTimeSlot");
								//uiDriver.wait.until(ExpectedConditions.stalenessOf(webDriver.findElement(By.xpath(objMap.getLocator("btnChangeTimeSlot")))));
								SleepUtils.getInstance().sleep(TimeSlab.YIELD);
								if(webDriver.findElements(By.xpath(objMap.getLocator("chkageVerificationAlcohol"))).size() > 0)
								{
									uiDriver.click("chkageVerificationAlcohol");
									uiDriver.click("btnsaveAlcohol");
								}			
								((JavascriptExecutor) webDriver).executeScript("scroll(0, -250);");
								if (webDriver.findElements(By.xpath(objMap.getLocator("btnchangeTimeSlotAlcohol"))).size() > 0 && 
										webDriver.findElement(By.xpath(objMap.getLocator("btnchangeTimeSlotAlcohol"))).isDisplayed())
								{
									uiDriver.click("btnremoveProceed");
									RESULT.passed("Alcohol Restriction",
											"Alcohol Restriciton popup should be displayed",
									"Alcohol Restriciton popup is displayed and Alcohol item removed and proceeded");
								}	
								uiDriver.wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(objMap.getLocator("btnChangeTimeSlot"))));
								((JavascriptExecutor) webDriver).executeScript("scroll(0, -250);");
							}
						}
						else
						{
							RESULT.failed("Alcohol Restriction",
									"Alcohol Restriciton popup should be displayed",
							"Alcohol Restriciton popup is not visible");
						} 
						return;
					}
					else
					{
						if (webDriver.findElements(By.xpath(objMap.getLocator("btnchangeTimeSlotAlcohol"))).size() > 0 && 
								webDriver.findElement(By.xpath(objMap.getLocator("btnchangeTimeSlotAlcohol"))).isDisplayed())
						{
							RESULT.failed("Alcohol Restriction",
									"Alcohol Restriciton symbol should be displayed",
							"Alcohol Restriciton symbol is not visible");	
							return;
						}
					}

				} 
				else {
					uiDriver.wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(objMap.getLocator("strdeliveryDay"))));
					System.out.println("Selected time:" + TimeSlot);
					System.out.println("Selected day:"+DeliveryDay);
					if (TimeSlot.toLowerCase().contains(input.get("TimeSlot").toLowerCase())&& 
							DeliveryDay.toLowerCase().contains(input.get("DeliveryDay").toLowerCase())) {
						uiDriver.verify_day = input.get("DeliveryDay");
						uiDriver.verify_time = input.get("TimeSlot");
						RESULT.passed("Select Time Slot",
								"Given Time Slot Shoud be selected",
						"Given  Time Slot already Selected");
					} else {
						uiDriver.click("btnChangeTimeSlot");
						uiDriver.FD_chooseTimeslot(input.get("DeliveryDay"),input.get("TimeSlot"), input.get("FlexibilityFlag"));
						uiDriver.click("btnDoneTimeSlot");
						uiDriver.wait.until(ExpectedConditions.visibilityOf(webDriver.findElement(By.xpath(objMap.getLocator("btnChangeTimeSlot")))));
						SleepUtils.getInstance().sleep(TimeSlab.YIELD);
						if(webDriver.findElements(By.xpath(objMap.getLocator("chkageVerificationAlcohol"))).size() > 0)
						{
							uiDriver.click("chkageVerificationAlcohol");
							uiDriver.click("btnsaveAlcohol");
						}		
						((JavascriptExecutor) webDriver).executeScript("scroll(0, -250);");
						SleepUtils.getInstance().sleep(TimeSlab.YIELD); 
						// Alcohol Restriction Pop up
						if(uiDriver.alcoholRestriction)
						{
							if (webDriver.findElements(By.xpath(objMap.getLocator("btnchangeTimeSlotAlcohol"))).size() > 0 && 
									webDriver.findElement(By.xpath(objMap.getLocator("btnchangeTimeSlotAlcohol"))).isDisplayed())
							{
								if (input.get("RestrictionHandle").equalsIgnoreCase("Remove")) {
									uiDriver.click("btnremoveProceed");
									RESULT.passed("Alcohol Restriction",
											"Alcohol Restriciton popup should be displayed",
									"Alcohol Restriciton popup is displayed and Alcohol item removed and proceeded");
								} 
								if (input.get("RestrictionHandle").equalsIgnoreCase("Change")) {
									uiDriver.click("btnchangeTimeSlotAlcohol");
									RESULT.passed("Alcohol Restriction",
											"Alcohol Restriciton popup should be displayed",
									"Alcohol Restriciton popup is displayed and Address changed");
									uiDriver.FD_chooseTimeslot(input.get("DeliveryDay1"), input.get("TimeSlot1"), input.get("FlexibilityFlag"));
									uiDriver.click("btnDoneTimeSlot");
									//uiDriver.wait.until(ExpectedConditions.stalenessOf(webDriver.findElement(By.xpath(objMap.getLocator("btnChangeTimeSlot")))));
									SleepUtils.getInstance().sleep(TimeSlab.YIELD);
									if(webDriver.findElements(By.xpath(objMap.getLocator("chkageVerificationAlcohol"))).size() > 0)
									{
										uiDriver.click("chkageVerificationAlcohol");
										uiDriver.click("btnsaveAlcohol");
									}
									((JavascriptExecutor) webDriver).executeScript("scroll(0, -250);");
									if (webDriver.findElements(By.xpath(objMap.getLocator("btnchangeTimeSlotAlcohol"))).size() > 0 && 
											webDriver.findElement(By.xpath(objMap.getLocator("btnchangeTimeSlotAlcohol"))).isDisplayed())
									{
										uiDriver.click("btnremoveProceed");
										RESULT.passed("Alcohol Restriction",
												"Alcohol Restriciton popup should be displayed",
										"Alcohol Restriciton popup is displayed and Alcohol item removed and proceeded");
									}
									uiDriver.wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(objMap.getLocator("btnChangeTimeSlot"))));
									((JavascriptExecutor) webDriver).executeScript("scroll(0, -250);");
								}
							} 
							else
							{
								RESULT.failed("Alcohol Restriction",
										"Alcohol Restriciton popup should be displayed",
								"Alcohol Restriciton popup is not visible");

							} 
							return;
						}
						else
						{
							if (webDriver.findElements(By.xpath(objMap.getLocator("btnchangeTimeSlotAlcohol"))).size() > 0 && 
									webDriver.findElement(By.xpath(objMap.getLocator("btnchangeTimeSlotAlcohol"))).isDisplayed())
							{
								RESULT.failed("Alcohol Restriction",
										"Alcohol Restriciton symbol should be displayed",
								"Alcohol Restriciton symbol is not visible");	
								return;
							}
						}

					}
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}		
	}

	/*
	 * Component to handle payment methods
	 */
	public void PaymentOptions(DataRow input, DataRow output)
	throws FindFailed, InterruptedException {
		try {
			if (webDriver.findElements(By.xpath(objMap.getLocator("btnchangePaymnt"))).size()>0)
			{
				uiDriver.click("btnchangePaymnt");
				uiDriver.wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//li[@data-drawer-id='payment']/button[contains(text(),'Cancel')]")));
//				if(webDriver.findElement(By.xpath("//li[@data-drawer-id='payment']/button[contains(text(),'Cancel')]")).isEnabled()){
					RESULT.done("Change Payment","Change button should be clicked successful",
							"Click on change button successful");
					uiDriver.FD_paymentOption(input.get("AddEditDeleteSelect"), input
							.get("CardDetail"), input.get("NameOnCard"), input
							.get("Routing"), input.get("Bank"), input
							.get("CardOrAcctype"), input.get("CardNum"), input
							.get("ExpiryMnth"), input.get("ExpiryYr"), input
							.get("CardStreetAdd"), input.get("CardAppNum"), input
							.get("CardAddLine2"), input.get("CardCountry"), input
							.get("CardCity"), input.get("CardState"), input
							.get("CardZip"), input.get("PhoneNum"), input
							.get("FlexibilityFlag"));
					try{
						uiDriver.click("btndonePaymnt");
						uiDriver.wait.until(ExpectedConditions.visibilityOf(webDriver.findElement(By.xpath(objMap.getLocator("btnchangePaymnt")))));
						//					if(uiDriver.getwebDriverLocator(objMap.getLocator("btndonePaymnt")).isDisplayed())
						//					{
						//						RESULT.failed("Change Payment button",
						//								"Payment option should get selected",
						//						"Specified Payment option:"+input.get("AddEditDeleteSelect")+" is unsucessful");
						//						return;
						//					}
						//					else 
						if(uiDriver.getwebDriverLocator(objMap.getLocator("btnchangePaymnt")).isDisplayed())
							RESULT.passed("Change Payment","Payment option should get selected",
									"Specified Payment option:"+input.get("AddEditDeleteSelect")+" is sucessful");
						else{
								RESULT.failed("Change Payment",
									"Payment option should get selected",
									"Specified Payment option:"+input.get("AddEditDeleteSelect")+" is unsucessful");
							return;
							}

					}
					catch(Exception e)
					{
						RESULT.failed("Change Payment button",
								"Payment option should get selected",
								"Specified Payment option:"+input.get("AddEditDeleteSelect")+" is unsucessful");
						return;
					}
				}
				else
				{	
					RESULT.failed("Change Payment button","It should click on change button", "Change button is not displayed");
					return;
				}
//			}
//			else
//			{
//				RESULT.failed("Change Payment button","Click on change button should be successful",
//						"Clicked on change button");
//			}
		} catch (Exception e) {
			RESULT.failed("Change Payment button",
					"Payment option should get selected",
					"Specified Payment option:"+input.get("AddEditDeleteSelect")+" is unsucessful");
			return;
		}
	}

	/*
	 * Component to review and submit order
	 */
	public void PlaceOrder(DataRow input, DataRow output)
	throws FindFailed, InterruptedException {
		try{
			SleepUtils.getInstance().sleep(TimeSlab.YIELD);
			uiDriver.FD_returnProducts();
			SleepUtils.getInstance().sleep(TimeSlab.YIELD);
			uiDriver.FD_placeOrder(uiDriver.verify_day.substring(0, 3),uiDriver.verify_time.split("\n")[0]);	
		}catch (Exception e) {
			RESULT.warning("Place order", "Place order aborted", "Place order aborted due to " + e.getMessage());
			return;
		}
	}

	/*
	 * Component to search a product 
	 */
	public void SearchProduct(DataRow input, DataRow output) throws FindFailed,
	InterruptedException {
		//uiDriver.click("imgfd_Logo");		
		for(int i=0;i<15;i++)
		{
			uiDriver.robot.moveToElement(webDriver.findElement(By.name(objMap.getLocator("imgfd_Logo"))));
			if(i>=5 &&!(CompositeAppDriver.startUp.equalsIgnoreCase("CHROME")))
			{
				break;
			}
		}
		uiDriver.FD_searchFunction(input.get("Method"), input
				.get("Departement"), input.get("SubDepartement"), input
				.get("Category"));
		if (!((input.get("SubCategory") == null)|| input.get("SubCategory").replaceAll(" ", "").equals(""))) {
			String sub_cat_xpath = "//div[text() = '"+ input.get("SubCategory") + "']";
			uiDriver.wait.until(ExpectedConditions.visibilityOfElementLocated(By
					.xpath(sub_cat_xpath)));
			// click on sub-category
			webDriver.findElement(By.xpath(sub_cat_xpath)).click();
			//wait for page
			uiDriver.wait.until(ExpectedConditions
					.invisibilityOfElementLocated(By.xpath(sub_cat_xpath)));
			String breadcrumbs_text = null;
			SleepUtils.getInstance().sleep(TimeSlab.YIELD);
			do{
				breadcrumbs_text = webDriver.findElement(By.xpath(objMap.getLocator("strverifySearchResults"))).getText();
				SleepUtils.getInstance().sleep(TimeSlab.YIELD);
			}while(breadcrumbs_text == null || breadcrumbs_text.replace(" ", "").equals(""));
			if (breadcrumbs_text.toLowerCase().contains(input.get("SubCategory").toLowerCase())) {
				RESULT.passed("Searching product having customization",
						"Product search should be successfully",
				"Product search is successfully");
			} else {
				RESULT.failed("Searching product having customization",
						"Product search should be successfully : "
						+ input.get("SubCategory"),
						"Product search failed : " + breadcrumbs_text);
			}
		}
	}

	/*
	 * Component for Available Delivery timeslot in Delivery Info
	 */
	public void DeliveryInfo(DataRow input, DataRow output) throws FindFailed,
	InterruptedException {
		//		if(!(webDriver.findElements(By.linkText(objMap.getLocator("lnkdeliveryInfoLink"))).size()>0))
		//		{
		//			uiDriver.click("imgfd_Logo");		
		if(webDriver.findElements(By.linkText(objMap.getLocator("lnkdeliveryInfoLink"))).size()>0 && webDriver.findElements(By.className(objMap.getLocator("btnlogout"))).size()>0)
		{
			uiDriver.click("lnkdeliveryInfoLink");
			uiDriver.wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(objMap.getLocator("strdeliveryPickupInfo"))));			
			SleepUtils.getInstance().sleep(TimeSlab.YIELD);
			String url = webDriver.getCurrentUrl();
			if(!(url.contains("avail_slots")))
			{
				String Newurl = url.split("/delivery_info.jsp")[0]+ "/delivery_info_avail_slots.jsp";
				webDriver.get(Newurl);
			}
			uiDriver.wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.id(objMap.getLocator("tabletimeSlot"))));
			//uiDriver.click("lnkdeliverySlots");
			if(uiDriver.isDisplayed("tabletimeSlot"))
			{
				WebElement element1 = uiDriver.getwebDriverLocator(objMap.getLocator("tabletimeSlot"));
				((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView(true);", element1);					
				SleepUtils.getInstance().sleep(TimeSlab.YIELD);
				RESULT.passed("DeliveryInfo",
						"Available Delivery Time Slot table should be displayed",
				"Available Delivery Time Slots table gets displayed");
				// Verifying Alcohol Delivery Restriction indication
				if (webDriver.findElements(By.xpath(objMap.getLocator("imgalcoholRestrictionDeliveryInfo"))).size()>0)
				{
					RESULT.passed("DeliveryInfo",
							"Alcohol Delivery Restriction indication should be displayed for alcoholic product in cart",
					"Alcohol Delivery Restriction symbol gets displayed for alcoholic product in cart");
				}
				else
				{
					RESULT.warning("DeliveryInfo",
							"Alcohol Delivery Restriction indication should be displayed for alcoholic product in cart",
					"Alcohol Delivery Restriction indication is not displayed for alcoholic product in cart");
				}
				// Verifying Reserved text
				if (webDriver.findElements(By.className(objMap.getLocator("strreserved"))).size()>0)
				{
					RESULT.passed("DeliveryInfo",
							"Reserved text should be displayed for reserved time slot",
					"Reserved text should gets displayed for reserved time slot");
				}
				else
				{
					RESULT.warning("DeliveryInfo",
							"Reserved text should be displayed for reserved time slot",
					"Reserved text should is not displayed for reserved time slot");
				}
				// Verifying Eco-Friendly indication
				if (webDriver.findElements(By.linkText(objMap.getLocator("strecoFriendly"))).size()>0)
				{
					RESULT.passed("DeliveryInfo",
							"Eco-Friendly indication should be displayed for recommendation of delivery slot",
					"Eco-Friendly indication is displayed for recommendation of delivery slot");
				}
				else
				{
					RESULT.warning("DeliveryInfo",
							"Eco-Friendly indication should be displayed for recommendation of delivery slot",
					"Eco-Friendly indication is not displayed for recommendation of delivery slot");
				}
			}
			JavascriptExecutor jse = (JavascriptExecutor)webDriver;
			jse.executeScript("scroll(0, -250);");
			uiDriver.click("imgfd_Logo");
			uiDriver.wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(objMap.getLocator("imgmediaSection"))));			
		}

		else if(webDriver.findElements(By.linkText(objMap.getLocator("lnkdeliveryInfoLink"))).size()>0 && webDriver.findElements(By.className(objMap.getLocator("btnlogin"))).size()>0)
		{
			uiDriver.click("lnkdeliveryInfoLink"); 		
			uiDriver.wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(objMap.getLocator("strdeliveryPickupInfo"))));
			SleepUtils.getInstance().sleep(TimeSlab.YIELD);
			try{	
				String url = webDriver.getCurrentUrl();
				String Newurl = url.split("/delivery_info.jsp")[0]+ "/delivery_info_check_slots.jsp";
				webDriver.get(Newurl);
				List<WebElement> RadioGroup1 = webDriver.findElements(By.name("dlvservicetype"));
				if(uiDriver.isDisplayed("btncheckMyAddress"))
				{
					String Servicetype=input.get("ServiceType");
					if(Servicetype.equalsIgnoreCase("Office"))
					{
						RadioGroup1.get(1).click();
					}
					else
					{
						RadioGroup1.get(0).click();
					}
					uiDriver.setValue("txtstreetAddressDeliveryInfo", input.get("StreetAddress"));
					uiDriver.setValue("txtapartmentDeliveryInfo", input.get("Apartment"));
					uiDriver.setValue("txtcityDeliveryInfo", input.get("City"));
					uiDriver.setValue("txtstateDeliveryInfo", input.get("State"));
					uiDriver.setValue("txtzipcodeDeliveryInfo", input.get("Zipcode"));
					uiDriver.click("btncheckMyAddress");
					uiDriver.wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.id(objMap.getLocator("tabletimeSlot"))));
					if(uiDriver.isDisplayed("tabletimeSlot"))
					{
						WebElement element = uiDriver.getwebDriverLocator(objMap.getLocator("tabletimeSlot"));
						((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView(true);", element);					
						SleepUtils.getInstance().sleep(TimeSlab.YIELD);
						RESULT.passed("DeliveryInfo",
								"Available Delivery Time Slot table should be displayed",
						"Available Delivery Time Slots table gets displayed");
					}
					else
					{
						RESULT.failed("DeliveryInfo",
								"Available Delivery Time Slot table should be displayed",
						"Available Delivery Time Slots table is not displayed");
						return;
					}				
				}
				else
				{
					RESULT.failed("DeliveryInfo",
							"Delivery Info Link should be available",
					"Delivery Info Link is not available");
					return;
				}
				uiDriver.click("imgfd_Logo");
				uiDriver.wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(objMap.getLocator("imgmediaSection"))));
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void TDSValidation(DataRow input, DataRow output) throws FindFailed, InterruptedException{
		try { 
			uiDriver.click("btnyourCart");
			uiDriver.wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(objMap.getLocator("btncheckOut"))));
			uiDriver.FD_displayValidation("btncheckOut");
			uiDriver.FD_tdsValidation();
			uiDriver.click("imgfd_Logo");
			uiDriver.wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(objMap.getLocator("imgmediaSection"))));
			uiDriver.FD_displayValidation("imgmediaSection");
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
	}
	
}