package ModuleDrivers;

import java.util.ArrayList;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.python.core.exceptions;
import org.sikuli.script.FindFailed;

import com.sun.java.swing.plaf.windows.resources.windows;

import cbf.engine.BaseModuleDriver;
import cbf.engine.TestResultLogger;
import cbf.engine.TestResult.ResultType;
import cbf.utils.DataRow;
import cbf.utils.SleepUtils;
import cbf.utils.SleepUtils.TimeSlab;

public class OrderCRMDriver extends BaseModuleDriver {

	public OrderCRMDriver(TestResultLogger resultLogger) {
		super(resultLogger);
		// TODO Auto-generated constructor stub
	}
	/*
	public void DeliveryAddress(DataRow input, DataRow output)
	throws FindFailed, InterruptedException {

		try{
			if (webDriver.getTitle().equals("/ FreshDirect CRM : Checkout > Select Delivery Address /")) {
				uiDriver.FD_chooseDeliveryAddress_CRM(input
						.get("CRM_AddSelectSelectPickupDeleteEdit"), input
						.get("CRM_UseCustDefault"), input.get("CRM_ServiceType"),
						input.get("CRM_CompName"), input.get("CRM_FirstNameTb"),
						input.get("CRM_LastNameTb"), input.get("CRM_StreetAdd"),
						input.get("CRM_AddLine2"), input.get("CRM_AptName"), input
						.get("CRM_City"), input.get("CRM_State"), input
						.get("CRM_ZipCode"), input.get("CRM_Contact"),
						input.get("CRM_AltContact"), input
						.get("CRM_SpecialDelivery"), input
						.get("CRM_DoormanRadioBtn"), input
						.get("CRM_NeighborRadioBtn"), input
						.get("CRM_AltFirstName"), input
						.get("CRM_AltLastName"), input.get("CRM_AltApt"),
						input.get("CRM_AltPhn"), input.get("CRM_SaveCancelBtn"),
						input.get("CRM_SelectAddress"));
			}
			else if (webDriver.getTitle().equals("/ FreshDirect CRM : Account Details /")) {
				uiDriver.FD_AccDetailsDelAdd_CRM(input
						.get("CRM_AddSelectSelectPickupDeleteEdit"), input
						.get("CRM_UseCustDefault"), input.get("CRM_ServiceType"),
						input.get("CRM_CompName"), input.get("CRM_FirstNameTb"),
						input.get("CRM_LastNameTb"), input.get("CRM_StreetAdd"),
						input.get("CRM_AddLine2"), input.get("CRM_AptName"), input
						.get("CRM_City"), input.get("CRM_State"), input
						.get("CRM_ZipCode"), input.get("CRM_Contact"),
						input.get("CRM_AltContact"), input
						.get("CRM_SpecialDelivery"), input
						.get("CRM_DoormanRadioBtn"), input
						.get("CRM_NeighborRadioBtn"), input
						.get("CRM_AltFirstName"), input
						.get("CRM_AltLastName"), input.get("CRM_AltApt"),
						input.get("CRM_AltPhn"), input.get("CRM_SaveCancelBtn"),
						input.get("CRM_SelectAddress"));
			}else{
				RESULT.failed("CRM Delivery Address Page","CRM Delivery Address page title should be from allowed values ['/ FreshDirect CRM : Checkout > Select Delivery Address /','/ FreshDirect CRM : Account Details /']",
				"CRM Delivery Address page title is [" + webDriver.getTitle() + "]");
			}
		}catch(Exception e){
			RESULT.failed("CRM Delivery Address Componennt","CRM Delivery Address operation should be successful",
			"CRM Delivery Address operation failed");
		}
	}

	public void TimeSlot(DataRow input, DataRow output) throws FindFailed,
	InterruptedException {
		try {
			if (webDriver.getTitle().contains(
					"/ FreshDirect CRM : Checkout > Age Verification for orders containing alcohol /"))
			{
				uiDriver.click("chkageVerification");
				uiDriver.click("btnageVerification");
			}
			if (webDriver.getTitle().equals(
			"/ FreshDirect CRM : Checkout > No Alcohol Address /")) {
				uiDriver.click("btnnoAlcohol");
				if (webDriver.getTitle().equals(
						"/ FreshDirect CRM : Checkout > Select Delivery Address /")) {
					uiDriver.FD_chooseDeliveryAddress_CRM(input
							.get("CRM_AddSelectSelectPickupDeleteEdit"), input
							.get("CRM_UseCustDefault"), input
							.get("CRM_ServiceType"), input.get("CRM_CompName"),
							input.get("CRM_FirstNameTb"), input
							.get("CRM_LastNameTb"), input
							.get("CRM_StreetAdd"), input
							.get("CRM_AddLine2"), input
							.get("CRM_AptName"), input.get("CRM_City"),
							input.get("CRM_State"), input.get("CRM_ZipCode"),
							input.get("CRM_Contact"), input
							.get("CRM_AltContact"), input
							.get("CRM_SpecialDelivery"), input
							.get("CRM_DoormanRadioBtn"), input
							.get("CRM_NeighborRadioBtn"), input
							.get("CRM_AltFirstName"), input
							.get("CRM_AltLastName"), input
							.get("CRM_AltApt"),
							input.get("CRM_AltPhn"), input
							.get("CRM_SaveCancelBtn"), input
							.get("CRM_SelectAddress"));
				}
			}
			uiDriver.click("btnContinueChkout");
			if (webDriver.getTitle().contains("/ FreshDirect CRM : Checkout > Age Verification for orders containing alcohol /")) 
				//WebElement Error = webDriver.findElement(By.name(objMap.getLocator("btnageVerification")));
			{
				System.out.println(objMap.getLocator("chkageVerification"));
				uiDriver.click("chkageVerification");				
				uiDriver.click("btnageVerification");
			}
			if (webDriver.getTitle().equals(
			"/ FreshDirect CRM : Checkout > No Alcohol Address /")) {
				uiDriver.click("btnnoAlcohol");
			}	

		} catch (Exception e) {
			e.printStackTrace();
		}
		if (webDriver.getTitle().contains(
		"/ FreshDirect CRM : Checkout > Select Delivery Time /")) {
			uiDriver.FD_chooseTimeslot(input.get("Delivery_Day"), input
					.get("Time_Slot"), input.get("FlexibilityFlag"));
			uiDriver.click("btnContinueChkout");
			SleepUtils.getInstance().sleep(TimeSlab.YIELD);
			uiDriver.selenium.waitForPageToLoad("2000");
			if (webDriver.getTitle().contains(
			"/ FreshDirect CRM : Checkout > Items unavailable /")) {		
				uiDriver.click("imgContChkout");
			}			
		}
	}

	public void PaymentOptions(DataRow input, DataRow output)
	throws FindFailed, InterruptedException {
		try {
			if (webDriver.getTitle().equals(
			"/ FreshDirect CRM : Checkout > Select Payment Method /")) {
				uiDriver.FD_payment_CRM(input.get("AddEditDeleteSelect"), input
						.get("cardDetail"), input.get("NameOnCard"), input
						.get("routing"), input.get("Bank"), input
						.get("CardOrAcctype"), input.get("CardNum"), input
						.get("ExpiryMnth"), input.get("ExpiryYr"), input
						.get("CardStreetAdd"), input.get("CardAppNum"), input
						.get("CardAddLine2"), input.get("CardCountry"), input
						.get("CardCity"), input.get("CardState"), input
						.get("CardZip"));
				uiDriver.click("btnContinueChkout");
				uiDriver.selenium.waitForPageToLoad("2000");
				if (webDriver.getTitle().equals(
				"/ FreshDirect CRM : Checkout > Items unavailable /")) {
					uiDriver.click("imgContChkout");
				}
				if(webDriver.findElements(By.xpath(objMap.getLocator("strcartEmptyError"))).size()>0)
				{
					String ErrorMsg=webDriver.findElement(By.xpath(objMap.getLocator("strcartEmptyError"))).getText();
					RESULT.failed("ChoosePaymentoption", "Payment Option should be selected", ErrorMsg);
					return;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void SubmitOrder(DataRow input, DataRow output) throws FindFailed,
	InterruptedException {

		//		if(webDriver.findElements(By.xpath(objMap.getLocator("lnkcontinueChkout"))).size()>0)
		//		{
		//			uiDriver.click("lnkcontinueChkout");
		//		}
		uiDriver.click("inkPlaceOrderLink");
		SleepUtils.getInstance().sleep(TimeSlab.YIELD);
		if (webDriver.getTitle().equals("Checkout > Order Confirmation")) {
			RESULT.passed("ChoosePaymentoption",
					"Order should be placed successfully!!!",
			"Order placed sucessfully!");
		} else {
			RESULT.failed("ChoosePaymentoption",
					"Order should be placed successfully!!!",
			"Error occured somewhere");
			return;
		}
	}

	public void SearchProduct(DataRow input, DataRow output) throws FindFailed,
	InterruptedException {
		uiDriver.FD_selectProdBrowse_CRM(input.get("Method"),
				input.get("Path"), input.get("Product"), input
				.get("Product_Method"), input.get("Count"));
		if (!(uiDriver.isDisplayed("btnCheckOut"))) {
			RESULT.warning("Case creation required",
					"Case must be created first",
			"User is required to create case once");
			uiDriver.click("lnkcase");
			uiDriver.FD_case_CRM(input.get("Assigned"), input.get("Queue"),
					input.get("Priority"), input.get("Subject"), input
					.get("Reported"), input.get("Actual"), input
					.get("Summary"), input.get("Notes"));
			// click on New order
			webDriver.findElement(
					By.linkText(objMap
							.getLocator("btnsearch_product_new_order")))
							.click();
		}
		uiDriver.click("btnCheckOut");
		uiDriver.selenium.waitForPageToLoad("1000");
		uiDriver.click("btnContinueChkoutAddress");
	}
	 */
	public void Checkout(DataRow input, DataRow output) throws FindFailed,
	InterruptedException {
		try{
			if(uiDriver.isDisplayed("lnksummary"))
			{
				uiDriver.click("lnksummary");	
				uiDriver.waitForPageLoad();
				uiDriver.setValue("drpstore", input.get("Store"));
				if(webDriver.findElements(By.linkText(objMap.getLocator("lnksummaryCheckout"))).size()>0 &&
						webDriver.findElement(By.linkText(objMap.getLocator("lnksummaryCheckout"))).isDisplayed())
				{
					String winHandleBefore = webDriver.getWindowHandle();
					String winHandleafter ;
					RESULT.passed("Checkout", "Checkout link should be available", "Checkout link is availbale");
					if (CompositeAppDriver.startUp.equalsIgnoreCase("IE"))
					{
						uiDriver.getwebDriverLocator(objMap.getLocator("lnksummaryCheckout")).sendKeys("\n");
					}
					else
					{
						uiDriver.click("lnksummaryCheckout");
					}
					winHandleafter=webDriver.getWindowHandle();
					try{
						webDriver.switchTo().window(winHandleBefore).close();
//						webDriver.switchTo().window(winHandleafter);
					}catch (Exception e) {
						RESULT.failed("CRM tab close","CRM tab should be closed ","CRM tab is not closed");
					}
					
					//uiDriver.waitForPageLoad();
					SleepUtils.getInstance().sleep(TimeSlab.YIELD);
					List<String> browserTabs = new ArrayList<String> (webDriver.getWindowHandles());
					System.out.println("tabs size : "+browserTabs.size());	  
					//SleepUtils.getInstance().sleep(TimeSlab.MEDIUM);
					//switch to new window
					webDriver.switchTo().window(browserTabs.get(0));
					//uiDriver.selenium.waitForPageToLoad("3000");
					SleepUtils.getInstance().sleep(TimeSlab.LOW);
					if (!CompositeAppDriver.startUp.equalsIgnoreCase("SAFARI")){
					webDriver.manage().window().maximize();	
					uiDriver.waitForPageLoad();
					}
					try{
						uiDriver.wait.until(ExpectedConditions.presenceOfElementLocated(By.name(objMap.getLocator("imgfd_Logo"))));
					}catch (Exception e) {
						RESULT.failed("Checkout", "User should be on storefront", "Nevigation to storefront failed");
					}
					if(webDriver.findElements(By.className(objMap.getLocator("btnlogout"))).size()>0)
					{
						RESULT.passed("Checkout",
								"User should be navigated to Freshdirect storefront application",
						"User has navigated to Freshdirect storefront application sucessfully!");						
					}
					else
					{
						RESULT.failed("Checkout",
								"User should be navigated to Freshdirect storefront application",
						"Unable to navigate to Freshdirect storefront application");
						return;
					}	
				}
				else
				{
					RESULT.failed("Checkout", "Checkout link should be available", "Checkout link is not availbale");
					return;
				}
			}else
			{
				RESULT.failed("Checkout", "Summary link should be available", "Summary link is not available");
				return;
			}
		}catch (Exception e) {
			RESULT.failed("Checkout", "Checkout should be successful", "Exception in checkout due to: "+e.getMessage());
			return;
		}
	}
}