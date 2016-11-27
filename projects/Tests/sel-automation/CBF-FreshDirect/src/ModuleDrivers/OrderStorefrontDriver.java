package ModuleDrivers;

import java.util.List;
import java.util.concurrent.TimeUnit;

import cbf.utils.SleepUtils.TimeSlab;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import cbf.engine.BaseModuleDriver;
import cbf.engine.TestResultLogger;
import cbf.utils.DataRow;
import cbf.utils.SleepUtils;

public class OrderStorefrontDriver extends BaseModuleDriver {

	static boolean flag = false;

	public OrderStorefrontDriver(TestResultLogger resultLogger) {
		super(resultLogger);
		// TODO Auto-generated constructor stub
	}
	/*
	 * Component for checkout and Your Cart page
	 */

	public void CartVerify(DataRow input, DataRow output) throws  
	InterruptedException{
		try 
		{
			uiDriver.waitForPageLoad();
			if (!webDriver.getTitle().contains("View Cart")) 
			{
				uiDriver.FD_viewCart();
			}
		}
		catch (Exception e) {
			RESULT.failed("Verify Products in cart",
					"User should be able to Verify Products in cart successfully",
					"Unable to Verify cart Products and exception caught is "+e.getMessage());

		}
	}

	public void Checkout(DataRow input, DataRow output) throws  
	InterruptedException{
	
		try 
		{
			//uiDriver.waitForPageLoad();
			if (!webDriver.getTitle().contains("View Cart")) 
			{
				//				WebElement btnYourcart = uiDriver.getwebDriverLocator(objMap.getLocator("btnyourCart"));					
				if (CompositeAppDriver.startUp.equalsIgnoreCase("IE"))
				{
					uiDriver.getwebDriverLocator(objMap.getLocator("btnyourCart")).sendKeys("\n");
				}
				else
				{
					uiDriver.click("btnyourCart");
				}
				//removed wait for testing purposes
				//uiDriver.waitForPageLoad();
				uiDriver.wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(objMap.getLocator("tXtSuBtoTAL"))));
				//uiDriver.FD_viewCart();
			}

			String cartSubtotal = uiDriver.getwebDriverLocator(objMap.getLocator("tXtSuBtoTAL")).getText();
			cartSubtotal = cartSubtotal.replaceAll(",", "");
			if(input.get("DeliverypassFlag").equalsIgnoreCase("YES")){
				try{
				String Deliveryfee = uiDriver.getwebDriverLocator(objMap.getLocator("strDeliveryFee")).getText();
				if(Deliveryfee.equalsIgnoreCase("FREE with DeliveryPass")){
					RESULT.passed("DeliveryPass Verification",
							"Delivery Fee should contain 'Free with Deliverypass' Text",
							"Delivery Fee contains 'Free with Deliverypass' Text");	
				}else{
					RESULT.failed("DeliveryPass Verification",
							"Delivery Fee should contain 'Free with Deliverypass' Text",
							"Delivery Fee does not contain 'Free with Deliverypass' Text");
				}
				}catch(Exception e){
					RESULT.failed("DeliveryPass string",
							"Deliverypass string should be available",
							"Deliverypass string is not available and exception caught is " +e.getMessage());
					return;
				}
				
			}

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
			WebElement cart2 = webDriver.findElement(By.xpath(objMap.getLocator("btnyourCart")));
			uiDriver.robot.moveToElement(cart2);
			uiDriver.wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(objMap.getLocator("btncheckOut"))));

			if(webDriver.findElements(By.xpath(objMap.getLocator("btncheckOut"))).size()>0
					&& webDriver.findElement(By.xpath(objMap.getLocator("btncheckOut"))).isDisplayed())
			{
				RESULT.passed("Checkout", "Checkout button should be available", "Checkout button is available");
				if (CompositeAppDriver.startUp.equalsIgnoreCase("IE"))
				{
					String checkout = uiDriver.getwebDriverLocator(objMap.getLocator("btncheckOut")).getText();
					System.out.println(checkout);
					uiDriver.getwebDriverLocator(objMap.getLocator("btncheckOut")).sendKeys("\n");
				}
				else
				{
				uiDriver.click("btncheckOut");
				uiDriver.robot.moveToElement(webDriver.findElement(By.xpath("html/body/div[6]/div/a/img")));
				uiDriver.wait.withTimeout(10, TimeUnit.SECONDS);
				uiDriver.wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(objMap.getLocator("ChkOut1"))));
				webDriver.findElement(By.xpath(objMap.getLocator("ChkOut1"))).click();
				uiDriver.waitForPageLoad();
				
				}
				//				SleepUtils.getInstance().sleep(TimeSlab.YIELD);
				uiDriver.waitForPageLoad();
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
				try
				{
					//uiDriver.wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(objMap.getLocator("ChkOut1"))));
				}
				catch(TimeoutException e)
				{
					RESULT.warning("Check out button invisibility", "Check out button should be invisible", "Check out button is visible");
				}
			}
			//				SleepUtils.getInstance().sleep(TimeSlab.YIELD);

			else
			{
				RESULT.failed("Check out button visibility","Check out button should be invisible","Check out button is visible");
				return;
			}
			//			SleepUtils.getInstance().sleep(TimeSlab.YIELD);

			if(webDriver.findElements(By.xpath(objMap.getLocator("chkageVerificationAlcohol"))).size() > 0 
					&& webDriver.findElement(By.xpath(objMap.getLocator("chkageVerificationAlcohol"))).isDisplayed())
			{
				try
				{
					uiDriver.click("chkageVerificationAlcohol");
					uiDriver.click("btnsaveAlcohol");				
					//uiDriver.wait.until(ExpectedConditions.stalenessOf(webDriver.findElement(By.xpath(objMap.getLocator("btnchangeDeliveryAddress")))));		
					SleepUtils.getInstance().sleep(TimeSlab.YIELD);
					((JavascriptExecutor) webDriver).executeScript("scroll(0, -250);");
					SleepUtils.getInstance().sleep(TimeSlab.YIELD);
				}
				catch(Exception e)
				{
					RESULT.warning("Check out", "Check age verification should be successful", "Check age verification  is not successful");
				}				
			}

			// Alcohol Restriction Pop up for Time Slot		
			try
			{
				if (webDriver.findElements(By.xpath(objMap.getLocator("btnchangeTimeSlotAlcohol"))).size() > 0 && 
						webDriver.findElement(By.xpath(objMap.getLocator("btnchangeTimeSlotAlcohol"))).isDisplayed())
				{
					if (input.get("RestrictionHandle").equalsIgnoreCase("Remove") || input.get("RestrictionHandle").isEmpty()) {
						uiDriver.click("btnremoveProceed");
						RESULT.passed("Alcohol Restriction",
								"Alcohol Restriciton popup should be displayed",
						"Alcohol Restriciton popup is displayed and Alcohol item removed and proceeded");
					} 
					else if (input.get("RestrictionHandle").equalsIgnoreCase("Change")) {
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
					else{
						RESULT.warning("Alcohol Restriction",
								"Alcohol Restriciton popup handle value Change or Remove should be provided",
								"Alcohol Restriciton popup handle value is " + input.get("RestrictionHandle") + " so taken Remove by default");
						uiDriver.click("btnremoveProceed");
						RESULT.passed("Alcohol Restriction",
								"Alcohol Restriciton popup should be displayed",
						"Alcohol Restriciton popup is displayed and Alcohol item removed and proceeded");
					}
				}			
			}
			catch(Exception e)
			{
				RESULT.warning("Check out : Age verification pop up ", "Age verification pop up should be validated", "Age verification pop up is not validated ");
			}
			// handle unavailability pop-up 
			uiDriver.FD_unavailablePopUp();
			if (webDriver.getTitle().contains(input.get("PageName"))) 
			{
				RESULT.passed("Checkout Page","Page should be navigated to checkout","Successful navigation to checkout page");
				uiDriver.wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(objMap.getLocator("btnchangeDeliveryAddress"))));
			}
			else
			{			
				RESULT.failed("Checkout Page","Page should be navigated to checkout. Page title shoudl be - " + input.get("PageName"),
						"Unsuccessful navigation to checkout page. Page title is - "  + webDriver.getTitle());
				return;
			}
		}
		catch (Exception e) {
			RESULT.failed("Checkout",
					"User should be able to Checkout successfully",
					"Unable to Checkout and exception caught is "+e.getMessage());

		}
	}

	/* Component to handle Delivery Address selection
	 */
	public void DeliveryAddress(DataRow input, DataRow output)
	throws   InterruptedException {

		try{
			// flag to decide for changing the address
			boolean change_address = true;
			// flag to decide alcohol pop-up handling
			boolean alcohol_popup = false;
			//Check for the alcohol pop-up and set the flag
			try {	
				if (webDriver.findElements(
						By.xpath(objMap.getLocator("btnchangeAddress"))).size() > 0 && webDriver.findElement(By.xpath(objMap.getLocator("btnchangeAddress"))).isDisplayed()) 
				{
					alcohol_popup = true;			
					change_address = uiDriver.FD_alcoholPopUp(input.get("AlcoholRestriction"));			
				}

			} catch (Exception e) {
				RESULT.failed("Handling alcohol popup in Delievery Address",
						"Alcohol pop should get handled with flag " + input.get("AlcoholRestriction"),
						"Alcohol pop is not handled with flag " + input.get("AlcoholRestriction"));
				return;
			}

			// Address operation
			try{
				if (webDriver.getTitle().equals(input.get("PageName"))) {

					// click on change based on the pop up change address flag
					if (change_address) {
						if(webDriver.findElements(
								By.xpath(objMap.getLocator("btnchangeDeliveryAddress"))).size() > 0 && 
								webDriver.findElement(By.xpath(objMap.getLocator("btnchangeDeliveryAddress"))).isDisplayed())
						{											
							uiDriver.click("btnchangeDeliveryAddress");
						}
						else
						{
							RESULT.failed("Delivery Address ", "Change Delivery address link should be available",
							"Change Delivery address link is not available or visible");
							return;
						}
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

					// move to done button and click
					//uiDriver.robot.scrollToElement(uiDriver.getwebDriverLocator(objMap.getLocator("btndoneDelAddress")),webDriver);
					webDriver.findElement(By.xpath(objMap.getLocator("btndoneDelAddress"))).click();
					// wait for the invisibility of the done button

					try{	
						uiDriver.wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(objMap.getLocator("btndoneDelAddress"))));
						uiDriver.wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(objMap.getLocator("btnChangeTimeSlot"))));
						String selectedAddress = webDriver.findElement(By.xpath(objMap.getLocator("strselectedAddress"))).getText();
						if(selectedAddress.contains(uiDriver.address) && input.get("AddSelectDeleteEdit").equalsIgnoreCase("select"))
						{
							RESULT.passed(
									"Address selection",
									uiDriver.address+ " Address should get selected",
									selectedAddress +" Address selected successfully!!!");
						}
						else if(input.get("AddSelectDeleteEdit").equalsIgnoreCase("select")){
							RESULT.failed("Address selection",
									uiDriver.address+ " Address should get selected",
							"Specified address selection is unsucessful");
							return;
						}
					}catch(TimeoutException e){
						RESULT.warning("Delivery address done click",
								"Delivery address done button should get clicked and become invisible",
						"Delivery address done button is clicked and does not become invisible after waiting for 3 minutes");
						return;
					}
				}
				else
				{
					RESULT.warning("Delivery Address", "Application should be on Express Checkout page",
					"Application is not on Express Checkout page");
					return;
				}
			}
			catch(Exception e)
			{
				RESULT.failed("Delivery address operation",
						"User should be able to "+ input.get("AddSelectDeleteEdit")+ " delivery address",
						"User is not able to "+ input.get("AddSelectDeleteEdit")+ " delivery address");
				return;
			}

			// check if the alcohol pop is displayed again
			try {	

				SleepUtils.getInstance().sleep(TimeSlab.YIELD);
				// handle the pop up with the options provided again
				if (webDriver.findElements(By.xpath(objMap.getLocator("btnchangeAddress"))).size() > 0 
						&& webDriver.findElement(By.xpath(objMap.getLocator("btnchangeAddress"))).isDisplayed()) 
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

						//uiDriver.robot.scrollToElement(uiDriver.getwebDriverLocator(objMap.getLocator("btndoneDelAddress")),webDriver);
						uiDriver.click("btndoneDelAddress");
						uiDriver.wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(objMap.getLocator("btndoneDelAddress"))));
						uiDriver.wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(objMap.getLocator("btnChangeTimeSlot"))));
						String selectedAddress = webDriver.findElement(By.xpath(objMap.getLocator("strselectedAddress"))).getText();
						if(selectedAddress.contains(uiDriver.address) && input.get("AddSelectDeleteEdit").equalsIgnoreCase("select"))
						{
							RESULT.passed(
									"Address selection",
									uiDriver.address+ " Address should get selected",
									selectedAddress +" Address selected successfully!!!");
						}
						else if(input.get("AddSelectDeleteEdit").equalsIgnoreCase("select")){
							RESULT.failed("Address selection",
									uiDriver.address+ " Address should get selected",
							"Specified address selection is unsucessful");
							return;
						}
						SleepUtils.getInstance().sleep(TimeSlab.LOW);
					}
				}
			} catch (Exception e) {
				RESULT.failed("Handling alcohol pop in Delievery Address after address selction",
						"Alcohol pop should get handled with flag " + input.get("AlcoholRestriction"),
						"Alcohol pop is not handled with flag " + input.get("AlcoholRestriction"));
				return;
			}

			// If pop up comes third or second time after first time address selection remove and proceed
			if (webDriver.findElements(By.xpath(objMap.getLocator("btnchangeAddress"))).size() > 0 
					&& webDriver.findElement(By.xpath(objMap.getLocator("btnchangeAddress"))).isDisplayed()) 
			{
				uiDriver.click("btnremoveProceed");
				RESULT.passed(
						"Alcohol Restriction",
						"Alcohol Restriciton popup should be displayed",
				"Alcohol Restriciton popup is displayed and Alcohol item removed and proceeded");		
			}

			//handle the selection error on done click 
			try{
				if(webDriver.findElements(By.xpath(objMap.getLocator("straddressError"))).size()>0){
					List<WebElement> selectLst = webDriver.findElements(By.xpath(objMap.getLocator("lstradioPath")));
					int i=1;
					do{
						if(webDriver.findElement(By.xpath(objMap.getLocator("straddressError"))).isDisplayed());
						{
							String error=webDriver.findElement(By.xpath(objMap.getLocator("straddressError"))).getText();
							RESULT.warning("warning for selected address", "Address should get selected", error);
						}
						i++;
						selectLst.get(i).click();
						//uiDriver.robot.scrollToElement(uiDriver.getwebDriverLocator(objMap.getLocator("btndoneDelAddress")),webDriver);
						uiDriver.click("btndoneDelAddress");
						SleepUtils.getInstance().sleep(TimeSlab.LOW);
					}while(i<selectLst.size());
				}
			}catch (Exception e) {
				uiDriver.wait.until(ExpectedConditions.visibilityOf(webDriver.findElement(By.xpath(objMap.getLocator("btnchangeDeliveryAddress")))));
			}
					//uiDriver.wait.until(ExpectedConditions.stalenessOf(webDriver.findElement(By.xpath(objMap.getLocator("btnchangeDeliveryAddress")))));				
			uiDriver.wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(objMap.getLocator("btndoneDelAddress"))));
		}catch(Exception e){
			RESULT.failed("Delivery Address Component",
					"User should be able to "+ input.get("AddSelectDeleteEdit")+ " delivery address",
					"User is not able to "+ input.get("AddSelectDeleteEdit")+ " delivery address");
			return;
		}//end of main try/catch

	}

	/*
	 * Component to handle Time slot selection
	 */
	@SuppressWarnings("null")
	public void TimeSlot(DataRow input, DataRow output) throws  
	InterruptedException {
		String DeliveryDay=null;
		String TimeSlot=null;
		String TimeSlot1=null;
		String TimeSlot2=null;
		try {

			if (!webDriver.getTitle().equals(input.get("PageName"))) {
				RESULT.warning("Time Slot", "Application should be on Express Checkout page",
				"Application is not on Express Checkout page");
				return;
			}

			webDriver.navigate().refresh();
			//			WebUIDriver.selenium.waitForPageToLoad(uiDriver.PageLoadTime);
			if (webDriver.findElements(By.xpath(objMap.getLocator("btnChangeTimeSlot"))).size() > 0) 
			{
				if(!(webDriver.findElements(By.xpath(objMap.getLocator("btnselectTime"))).size() >0))
				{
					try
					{
						uiDriver.wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(objMap.getLocator("strdeliveryDay"))));
					}
					catch(TimeoutException e)
					{
						RESULT.warning("Time slot : Already selected", "Already selected time slot should be available ", "Already selected times slot is not available");
					}
					try
					{
						DeliveryDay = webDriver.findElement(By.xpath(objMap.getLocator("strdeliveryDay"))).getText();
						System.out.println("Text:" + DeliveryDay);
						TimeSlot = webDriver.findElement(By.xpath(objMap.getLocator("strdeliveryTime"))).getText();
					}
					catch(Exception e)
					{
						RESULT.warning("Time slot : Already selected", "Already selected day or time should be available ", "Already selected day or time is not available");
					}

					System.out.println("Text:" + TimeSlot);
					TimeSlot = TimeSlot.replaceAll(" ", "");
					TimeSlot1 = TimeSlot.split("-")[0];
					TimeSlot2 = TimeSlot.split("-")[1];
					TimeSlot1 = TimeSlot1.replaceAll(("AM"), "");
					TimeSlot1 = TimeSlot1.replaceAll(("PM"), "");
					TimeSlot = TimeSlot1 + "-" + TimeSlot2;
					System.out.println("Selected time:" + TimeSlot);
					System.out.println("Selected day:"+DeliveryDay);
					if (uiDriver.reserve_flag == true)
					{
						if (TimeSlot.toLowerCase().contains(uiDriver.verify_time.toLowerCase())&& 
								DeliveryDay.toLowerCase().contains(uiDriver.verify_day.toLowerCase())) {
							RESULT.passed("Select Time Slot","Reserved Time Slot "+uiDriver.verify_time+uiDriver.verify_day+ "Should be selected",
									"Already Selected time slot "+TimeSlot+" is same as reserved time");
							uiDriver.reserve_flag=false;
							return;
						}
						else
							RESULT.failed("Select Time Slot","Reserved Time Slot "+uiDriver.verify_time+uiDriver.verify_day+ "Shoud be selected",
									"Already Selected time slot "+TimeSlot+" is not same as reserved time");
						uiDriver.reserve_flag=false;
						return;
					}
					else
					{
						try
						{
							uiDriver.wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(objMap.getLocator("strdeliveryDay"))));
						}
						catch(TimeoutException e)
						{
							RESULT.warning("Time slot : Already selected", "Already selected time slot should be available ", "Already selected times slot is not available");
						}

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
							try
							{
								uiDriver.click("btnChangeTimeSlot");
							}
							catch(Exception e)
							{
								RESULT.warning("Time slot : Change button", "Change button time slot should be available ", 
								"Change button times slot is not available");
							}

							uiDriver.FD_chooseTimeslot(input.get("DeliveryDay"),input.get("TimeSlot"), input.get("FlexibilityFlag"));

							uiDriver.click("btnDoneTimeSlot");
							try
							{
								uiDriver.wait.until(ExpectedConditions.visibilityOf(webDriver.findElement(By.xpath(objMap.getLocator("btnChangeTimeSlot")))));
							}
							catch(Exception e)
							{
								RESULT.warning("Time slot : Change button visibility", "Change button should be visible", "Change button is not visible");
							}

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
									if (input.get("RestrictionHandle").equalsIgnoreCase("Remove") || input.get("RestrictionHandle").isEmpty()) {
										uiDriver.click("btnremoveProceed");
										uiDriver.wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(objMap.getLocator("btnremoveProceed"))));
										RESULT.passed("Alcohol Restriction",
												"Alcohol Restriciton popup should be displayed",
										"Alcohol Restriciton popup is displayed and Alcohol item removed and proceeded");
									} 
									if (input.get("RestrictionHandle").equalsIgnoreCase("Change")) 
									{
										uiDriver.click("btnchangeTimeSlotAlcohol");
										uiDriver.wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(objMap.getLocator("btnchangeTimeSlotAlcohol"))));
										RESULT.passed("Alcohol Restriction",
												"Alcohol Restriciton popup should be displayed",
										"Alcohol Restriciton popup is displayed and Address changed");
										uiDriver.FD_chooseTimeslot(input.get("DeliveryDay1"), input.get("TimeSlot1"), input.get("FlexibilityFlag"));
										uiDriver.click("btnDoneTimeSlot");
										//uiDriver.wait.until(ExpectedConditions.stalenessOf(webDriver.findElement(By.xpath(objMap.getLocator("btnChangeTimeSlot")))));
										SleepUtils.getInstance().sleep(TimeSlab.YIELD);
										if(webDriver.findElements(By.xpath(objMap.getLocator("chkageVerificationAlcohol"))).size() > 0 &&
												webDriver.findElement(By.xpath(objMap.getLocator("chkageVerificationAlcohol"))).isDisplayed())
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
									RESULT.failed("Alcohol Restriction pop up",
											"Alcohol Restriction pop up should not be displayed",
									"Alcohol Restriction pop up is displayed");	
									return;
								}
							}
						}						
					}
				}else {  
					if (uiDriver.reserve_flag == true)
					{
						RESULT.failed("Select Timeslot button", "Select time slot button should not be available for already reserved time slot", 
						"Select Time slot button available for already Reserved time slot");
						uiDriver.reserve_flag=false;
						return;
					}
					try
					{
						uiDriver.click("btnselectTime");
					}
					catch(Exception e)
					{
						RESULT.warning("Time slot :  Select time button", "Select time button should be clicked",
						"Select time button is not clicked");
					}

					uiDriver.FD_chooseTimeslot(input.get("DeliveryDay"), input.get("TimeSlot"), input.get("FlexibilityFlag"));
					uiDriver.click("btnDoneTimeSlot");
					try
					{
						uiDriver.wait.until(ExpectedConditions.visibilityOf(webDriver.findElement(By.xpath(objMap.getLocator("btnChangeTimeSlot")))));
					}
					catch(Exception e)
					{
						RESULT.warning("Time Slot done click",
								"Time Slot done button should get clicked and become invisible",
						"Time Slot done button is clicked and does not become invisible after waiting for 3 minutes");
					}
					//					uiDriver.wait.until(ExpectedConditions.stalenessOf(webDriver.findElement(By.xpath(objMap.getLocator("btnChangeTimeSlot")))));
					SleepUtils.getInstance().sleep(TimeSlab.MEDIUM);
					if(webDriver.findElements(By.xpath(objMap.getLocator("chkageVerificationAlcohol"))).size() > 0 &&
							webDriver.findElement(By.xpath(objMap.getLocator("chkageVerificationAlcohol"))).isDisplayed())
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
							if (input.get("RestrictionHandle").equalsIgnoreCase("Remove") || input.get("RestrictionHandle").isEmpty()) {
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
							RESULT.failed("Alcohol Restriction pop up",
									"Alcohol Restriction pop up should not be displayed",
							"Alcohol Restriction pop up is displayed");		
							return;
						}
					}

				} 


				// handle unavailablity pop-up 
				uiDriver.FD_unavailablePopUp();

			}else {
				RESULT.failed("Change Time slot button", "Change time slot button should be available",
				"Change time slot button is not available");
			}
		}
		catch (Exception e) {
			RESULT.failed("Time slot", "Time slot selection should be successful",
			"Time slot selection is unsuccessful");
		}		
	}

	/*
	 * Component to handle payment methods
	 */
	public void PaymentOptions(DataRow input, DataRow output)
	throws   InterruptedException {
		try {

			if (!webDriver.getTitle().equals(input.get("PageName"))) {
				RESULT.warning("Payment Option", "Application should be on Express Checkout page",
				"Application is not on Express Checkout page");
				return;
			}
			try{
				uiDriver.wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(objMap.getLocator("btnchangePaymnt"))));
			}
			catch(Exception e){
				RESULT.warning("Change Payment button","User should be able to click on change button", "Change button is not displayed");
				return;
			}

			String selectedCard;
			if (webDriver.findElements(By.xpath(objMap.getLocator("btnchangePaymnt"))).size()>0  &&
					webDriver.findElement(By.xpath(objMap.getLocator("btnchangePaymnt"))).isDisplayed())
			{
				selectedCard=webDriver.findElement(By.xpath(objMap.getLocator("strselectedPayment"))).getText().replaceAll("\\n", "");
				//				System.out.println(selectedCard);
				uiDriver.match=input.get("CardDetail").split("Billing address")[0].replaceAll("\n", "");
				if(selectedCard.contains(uiDriver.match)&&input.get("AddEditDeleteSelect").equalsIgnoreCase("select"))
				{
					RESULT.passed(
							"Selection of Credit/EBT card/checking account",
							uiDriver.match+ ":Credit/EBT card/checking account should get selected",
							input.get("CardDetail")+"is already selected");
				}else{
					uiDriver.click("btnchangePaymnt");
					try{
						uiDriver.wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(objMap.getLocator("lstpayment"))));
					}catch(Exception e){
						RESULT.failed("Payment selection frame",
								"Payment selection frame should get available",
						"Payment selection frame is not available");
						return;
					}
					//				if(webDriver.findElement(By.xpath("//li[@data-drawer-id='payment']/button[contains(text(),'Cancel')]")).isEnabled()){
					//					RESULT.done("Change Payment","Change button should be clicked successfully",
					//							"Change button is clicked successfully");
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
						//						"Specified Payment option:"+input.get("AddEditDeleteSelect")+" is unsuccessful");
						//						return;
						//					}
						//					else 
						if(uiDriver.getwebDriverLocator(objMap.getLocator("btnchangePaymnt")).isDisplayed())
							RESULT.passed("Done Payment","Done button should get clicked successfully",
							"Done button clicked successfully");
						else{
							RESULT.failed("Done Payment","Done button should get clicked successfully",
							"Done button click is unsuccessfully");
							return;
						}

						SleepUtils.getInstance().sleep(TimeSlab.YIELD);
						selectedCard=webDriver.findElement(By.xpath(objMap.getLocator("strselectedPayment"))).getText().replaceAll("\\n", "");
						System.out.println(selectedCard);

						//						String match1=input.get("CardDetail").split("Billing address")[0].replaceAll("\\n", "");
						if(selectedCard.replace("Name on card", "Name").contains(uiDriver.match.replace("Name on card", "Name"))&&input.get("AddEditDeleteSelect").equalsIgnoreCase("select"))
						{
							RESULT.passed(
									"Selection of Credit/EBT card/checking account",
									uiDriver.match+ ":Credit/EBT card/checking account should get selected",
									"Selected card/account is:"+ selectedCard);
						}
						else if(input.get("AddEditDeleteSelect").equalsIgnoreCase("select")){
							RESULT.failed("Selection of Credit/EBT card/checking account",
									"Paymetn opation should get selected",
							"Specified Payment option: Select is unsucessful");
							return;
						}


					}
					catch(Exception e)
					{
						RESULT.failed("Payment option ",
								"Valid user operation on payment section should be done",
								"Specified Payment option: "+input.get("AddEditDeleteSelect")+ " is unsucessful");
						return;
					}
				}
			}
			else
			{	
				RESULT.warning("Change Payment button","User should be able to click on change button", "Change button is not displayed");
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
					"Specified Payment option:"+input.get("AddEditDeleteSelect")+" is unsucessful due to: "+e.getMessage());
			return;
		}
	}

	/*
	 * Component to review and submit order
	 */
	public void PlaceOrder(DataRow input, DataRow output)
	throws   InterruptedException {
		try{

			if (!webDriver.getTitle().equalsIgnoreCase("Checkout")) {
				RESULT.warning("Payment Option", "Application should be on Express Checkout page",
				"Application is not on Express Checkout page");
				return;
			}

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
	public void SearchProduct(DataRow input, DataRow output) throws  
	InterruptedException, TimeoutException {
		//uiDriver.click("imgfd_Logo");		
		try
		{
			for(int i=0;i<15;i++)
			{
				uiDriver.robot.moveToElement(webDriver.findElement(By.xpath(objMap.getLocator("imgfd_Logo"))));
				if(i>=5 &&!(CompositeAppDriver.startUp.equalsIgnoreCase("CHROME")))
				{
					break;
				}
			}
			webDriver.findElement(By.xpath(objMap.getLocator("lnkreorder"))).click();
			uiDriver.waitForPageLoad();
			webDriver.findElement(By.xpath(objMap.getLocator("tabyourTopItems"))).click();
			//Added ability to engage "reorder" search field
			uiDriver.FD_searchFunction(input.get("Method"), input
					.get("Departement"), input.get("SubDepartement"), input
					.get("Category"));
			//This is for customized product

			if (!((input.get("SubCategory") == null)|| input.get("SubCategory").replaceAll(" ", "").equals(""))) {
				String sub_cat_xpath = "//div[text() = '"+ input.get("SubCategory") + "']";

				try
				{
					uiDriver.wait.until(ExpectedConditions.visibilityOfElementLocated(By
							.xpath(sub_cat_xpath)));
					// click on sub-category
					webDriver.findElement(By.xpath(sub_cat_xpath)).click();
					uiDriver.waitForPageLoad();
				}
				catch(Exception e)
				{
					RESULT.failed("Search Product for customizable product",
							input.get("SubCategory")+" subcategory should be available",
							input.get("SubCategory")+"subcategory is not available");
					return;
				}

				uiDriver.wait.until(ExpectedConditions
						.invisibilityOfElementLocated(By.xpath(sub_cat_xpath)));

				String breadcrumbs_text = null;
				int count = 0;
				SleepUtils.getInstance().sleep(TimeSlab.YIELD);
				do{
					breadcrumbs_text = webDriver.findElement(By.xpath(objMap.getLocator("strverifySearchResults"))).getText();
					SleepUtils.getInstance().sleep(TimeSlab.YIELD);
					count++;
				}while(breadcrumbs_text == null || breadcrumbs_text.replace(" ", "").equals("") && count<90);
				if (breadcrumbs_text.toLowerCase().contains(input.get("SubCategory").toLowerCase())) {
					RESULT.passed("Searching product for customizable product",
							"Product search should be successful",
					"Product search is successful");
				} else {
					RESULT.failed("Searching product for customizable product",
							"Product search should be successful : "
							+ input.get("SubCategory"),
							"Product search failed : " + breadcrumbs_text);
				}
			}
		}
		catch(Exception e)
		{
			RESULT.failed("Search product Exception", "Search product should be successful", "Search product is unsuccessful and exception caught is "+e.getMessage());
		}

	}


	/*
	 * Component for Available Delivery timeslot in Delivery Info
	 */
	public void DeliveryInfo(DataRow input, DataRow output) throws  
	InterruptedException {
		//		if(!(webDriver.findElements(By.linkText(objMap.getLocator("lnkdeliveryInfoLink"))).size()>0))
		//		{
		//			uiDriver.click("imgfd_Logo");		
		if(webDriver.findElements(By.linkText(objMap.getLocator("lnkdeliveryInfoLink"))).size()>0 
				&& webDriver.findElements(By.className(objMap.getLocator("btnlogout"))).size()>0)
		{
			try
			{
				uiDriver.click("lnkdeliveryInfoLink");
				uiDriver.wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(objMap.getLocator("strdeliveryPickupInfo"))));			
				SleepUtils.getInstance().sleep(TimeSlab.YIELD);
				try
				{
					String url = webDriver.getCurrentUrl();
					if(!(url.contains("avail_slots")))
					{
						String Newurl = url.split("/delivery_info.jsp")[0]+ "/delivery_info_avail_slots.jsp";
						webDriver.get(Newurl);
					}
				}
				catch(Exception e)
				{
					RESULT.failed("Delivery info ", "Available delivery timeslot url should be launched", "Available delivery timeslot url is not launched");
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
			catch(Exception e)
			{
				RESULT.failed("Delivery info : User logged in","Delivery info check should be successfully validated",
						"Delivery info check is not validated successfully "+e.getMessage());
			}

		}

		else if(webDriver.findElements(By.linkText(objMap.getLocator("lnkdeliveryInfoLink"))).size()>0 && webDriver.findElements(By.className(objMap.getLocator("btnlogin"))).size()>0)
		{
			uiDriver.click("lnkdeliveryInfoLink"); 		
			uiDriver.wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(objMap.getLocator("strdeliveryPickupInfo"))));
			SleepUtils.getInstance().sleep(TimeSlab.YIELD);
			try{	
				try
				{
					String url = webDriver.getCurrentUrl();
					String Newurl = url.split("/delivery_info.jsp")[0]+ "/delivery_info_check_slots.jsp";
					webDriver.get(Newurl);
				}
				catch(Exception e)
				{
					RESULT.failed("Delivery info ", "Available delivery timeslot URL should be launched", "Available delivery timeslot is not launched");
					return;
				}

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
				try
				{
					uiDriver.click("imgfd_Logo");
					uiDriver.wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(objMap.getLocator("imgmediaSection"))));
				}
				catch(Exception e)
				{
					RESULT.failed("Delivery info ", "Application should be navigated to homepage", "Application is not navigated to homepage");
				}

			}
			catch (Exception e) {
				RESULT.failed("Delivery info Exception : User is not logged in", "Delivery info check should be successfully validated",
						"Delivery info check is not validated successfully "+e.getMessage());
			}
		}
	}

	public void TDSValidation(DataRow input, DataRow output) throws   InterruptedException{
		try { 
			uiDriver.click("btnyourCart");
			try{
				uiDriver.wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(objMap.getLocator("btncheckOut"))));
				uiDriver.FD_displayValidation("btncheckOut");
			}catch(Exception e){
				RESULT.failed("TDS validation navigation to Checkout","Navigation to Checkout should be successful",
				"Navigation to Checkout unsuccessful");
				return;
			}


			uiDriver.FD_tdsValidation();

			uiDriver.click("imgfd_Logo");
			try{
				uiDriver.wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(objMap.getLocator("imgmediaSection"))));
				uiDriver.FD_displayValidation("imgmediaSection");
			}catch(Exception e){
				RESULT.failed("TDS validation navigation to Home","Navigation to Home should be successful",
				"Navigation to Home unsuccessful");
				return;
			}
		} catch (Exception e) {
			RESULT.failed("TDS validation exception","Different combination product validation should be successful",
					"Different combination product validation failed due to " + e.getMessage());
		}
	}

}