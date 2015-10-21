package ModuleDrivers;

import java.util.List;
import java.util.concurrent.TimeoutException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.sikuli.script.FindFailed;

import ui.ObjectMap;

import cbf.engine.BaseModuleDriver;
import cbf.engine.TestResultLogger;
import cbf.engine.TestResult.ResultType;
import cbf.utils.DataRow;
import cbf.utils.SleepUtils;
import cbf.utils.SleepUtils.TimeSlab;

public class OrderStorefront_oldDriver extends BaseModuleDriver{

	Actions ac_key=new Actions(webDriver);
	WebDriverWait wait = new WebDriverWait(webDriver, 20);

	public OrderStorefront_oldDriver(TestResultLogger resultLogger) {
		super(resultLogger);
		// TODO Auto-generated constructor stub
	}

	public void DeliveryAddress(DataRow input, DataRow output) throws FindFailed, InterruptedException
	{  
		try{ 
		if(!(webDriver.findElements(By.linkText("Delivery Addresses")).size()>0))
		{
			if(!webDriver.getTitle().contains("view_cart"))
			{
			uiDriver.getwebDriverLocator(objMap.getLocator("btnyourCart")).click();
			//uiDriver.wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(objMap.getLocator("btnchooseDelAddress"))));
			}
			if(webDriver.findElements(By.xpath(objMap.getLocator("strstandingOrder"))).size()>0){
				String Order =webDriver.findElement(By.xpath(objMap.getLocator("strstandingOrder"))).getText();
				System.out.println(Order);
				if(Order.contains("standing order")){
					RESULT.passed("Standing Order Verification", "Order Should be standing Order", "Order is standing Order");
				}else{
					RESULT.passed("Standing Order Verification", "Order Should be standing Order", "Order is not standing Order");
				}
			}
	
			uiDriver.getwebDriverLocator(objMap.getLocator("btnchooseDelAddress")).click();
			uiDriver.wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(objMap.getLocator("lstaddressPathOld"))));                                                                                                                                                                                                                                                                           
		}
		//call FD_chooseDeliveryAddress function
		uiDriver.FD_chooseDeliveryAddress_old(input.get("AddSelectSelectPickupDeleteEdit"), input.get("FirstNameTb"), input.get("LastNameTb"), input.get("ServiceType"), 
				input.get("CompName"), input.get("StreetAdd"), input.get("AptName"), input.get("AddLine2"), input.get("City"), input.get("State"), 
				input.get("ZipCode"), input.get("Contact"), input.get("Ext1"), input.get("AltContact"), input.get("Ext2"),input.get("SpclDelInstructions"), 
				input.get("DoormanDelivery"), input.get("NeighbourDelivery"), input.get("AltFirstName"), input.get("AltLastName"),
				input.get("AltApt"), input.get("AltPhone"), input.get("AltExtn"),input.get("None"), input.get("SelectAddress"),input.get("FlexibilityFlag"));                                                                      
	}
		catch(Exception e){
		RESULT.failed("Error encountered", "Delivery Address shoud be selected",
				"Error occured" + e);
	}	
	}


	public void TimeSlot(DataRow input,DataRow output) throws FindFailed, InterruptedException{
		try{
		//user will be on delivery address page. so go to select time slot page
		if(webDriver.findElements(By.xpath(objMap.getLocator("btnchooseTimeSlot"))).size()>0)
		{
			uiDriver.getwebDriverLocator(objMap.getLocator("btnchooseTimeSlot")).click();
			SleepUtils.getInstance().sleep(TimeSlab.HIGH);
			if(webDriver.findElements(By.xpath(objMap.getLocator("strnoDeliveryError"))).size()>0)
			{
				String Error=webDriver.findElement(By.xpath(objMap.getLocator("strnoDeliveryError"))).getText();
				System.out.println(Error);
				RESULT.failed("Order Total", "Order Total should be more than minimum required Total", "Order Total is less than minimum required Total  "+Error);
				return;
			}
		}            
		//check if it asks for age verification for products containing alcohol
		if(webDriver.getTitle().endsWith("Age Verification for orders containing alcohol"))
		{
			WebElement accept=uiDriver.getwebDriverLocator(objMap.getLocator("strAgeVerification"));
			accept.click();
			uiDriver.getwebDriverLocator(objMap.getLocator("btnAgeVerified")).click();                                    
			//webDriver.findElement(By.className("imgButtonOrange")).click();
		}

		//Area restriction for alcoholic products
		if(webDriver.findElements(By.xpath(objMap.getLocator("stralcohol"))).size()>0) 
		{
			String Alcohol_restriction= uiDriver.getwebDriverLocator(objMap.getLocator("stralcohol")).getText(); //check the message displayed
			RESULT.done("alcoholic product restriction in particular area", "Alcoholic product should not be delivered to some area", Alcohol_restriction);
			uiDriver.getwebDriverLocator(objMap.getLocator("btncontinueChkoutOld")).click();
		}           
		//check if user has reached to selection of time slot page
		if(webDriver.findElements(By.xpath(objMap.getLocator("btnpaymentBtnNew"))).size()>0)
		{
			uiDriver.FD_chooseTimeslot(input.get("Delivery_Day"), input.get("Time_Slot"),input.get("FlexibilityFlag"));
		}
		//if user is on view cart page check the error message
		else if(webDriver.getTitle().endsWith("View Cart")&&uiDriver.isDisplayed("strcheckOrderTotal"))
		{
			String error=uiDriver.getwebDriverLocator(objMap.getLocator("strcheckOrderTotal")).getText();
			RESULT.failed("Order total less than required", "Order total should be more than 30$ for home delivery and 50$ for corporate delivery",error );
			return;
		}  
		}catch(Exception e)
		{
			RESULT.failed("Error encountered", "Time slot shoud be selected",
					"Error occured" + e);
		}
	}

	public void PaymentOptions(DataRow input, DataRow output) throws FindFailed, InterruptedException
	{
		try{
		if(webDriver.findElements(By.xpath(objMap.getLocator("btnpaymentBtnNew"))).size()>0)
		{
			uiDriver.click("btnpaymentBtnNew");
			SleepUtils.getInstance().sleep(TimeSlab.MEDIUM);
		}
		//age verification for alcoholic products
		if(webDriver.getTitle().endsWith("Age Verification for orders containing alcohol"))
		{
			WebElement accept=uiDriver.getwebDriverLocator(objMap.getLocator("strAgeVerification"));
			accept.click();
			uiDriver.getwebDriverLocator(objMap.getLocator("btnageVerified")).click();                                    
			//webDriver.findElement(By.className("imgButtonOrange")).click();
		}
		
		//Product unavailable at particular time
		if(webDriver.findElements(By.xpath(objMap.getLocator("btncontinueChkoutOld"))).size()>0)
		{
			String ProdUnavailable=uiDriver.getwebDriverLocator(objMap.getLocator("strunavailabilityTime")).getText();
			RESULT.warning("Product unavailable at particular time", "Product should be available at particular time",
					ProdUnavailable);
			uiDriver.click("btncontinueChkoutOld");
			SleepUtils.getInstance().sleep(TimeSlab.MEDIUM);
			if(webDriver.findElements(By.xpath(objMap.getLocator("btnchooseDelAddress"))).size()>0)
			{
				String Error=webDriver.findElement(By.xpath(objMap.getLocator("strerrorOrderTotal"))).getText();
				System.out.println(Error);
				RESULT.failed("Order Total", "Order Total should be more than minimum required Total", "Order Total is less than minimum required Total  "+Error);
				return;
			}
			
			
		}
		
		if (webDriver.findElements(By.xpath(objMap.getLocator("btnorderExistChoosePayment"))).size()>0) 
		{
			uiDriver.click("btnorderExistChoosePayment");
			SleepUtils.getInstance().sleep(TimeSlab.MEDIUM);
		}
		//Order Total insufficient
		if (webDriver.getTitle().equals("FreshDirect - View Cart")) 
		{
			SleepUtils.getInstance().sleep(TimeSlab.YIELD);
			String Error=uiDriver.getwebDriverLocator(objMap.getLocator("strReplacementError")).getText();
			RESULT.failed("ChoosePaymentoption", "Payment page should be displayed", Error); 
			return;
		}
		//check if user is on payment options page
		if(webDriver.findElements(By.xpath(objMap.getLocator("btnreviewOrder"))).size()>0)
		{
			uiDriver.FD_paymentOption_old(input.get("AddEditDeleteSelect"),input.get("cardDetail"),
					input.get("NameOnCard"),input.get("routing"),input.get("Bank"), input.get("CardOrAcctype"), 
					input.get("CardNum"), input.get("ExpiryMnth"), input.get("ExpiryYr"), input.get("CardStreetAdd"), 
					input.get("CardAppNum"), input.get("CardAddLine2"), input.get("CardCountry"), input.get("CardCity"), 
					input.get("CardState"), input.get("CardZip"),input.get("FlexibilityFlag"));
		}
		}catch(Exception e)
		{
			RESULT.failed("Error encountered", "Payment Option shoud be selected",
					"Error occured" + e);
		}
	}
	public void ReviewSubmitOrder(DataRow input, DataRow output) throws FindFailed, InterruptedException
	{
		try{
		SleepUtils.getInstance().sleep(TimeSlab.YIELD);
		uiDriver.click("btnreviewOrder");
		uiDriver.wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(objMap.getLocator("btnsubmitOrder"))));
		uiDriver.FD_reviewOrder();
		uiDriver.click("btnsubmitOrder");
		uiDriver.wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(objMap.getLocator("strorderNumberOld"))));
		SleepUtils.getInstance().sleep(TimeSlab.YIELD);
		uiDriver.FD_submitOrder(uiDriver.verify_day.substring(0, 3),uiDriver.verify_time.split("\n")[0]);
		if (webDriver.findElements(By.xpath(objMap.getLocator("strorderNumberOld"))).size() > 0) {
			System.out.println(webDriver.findElement(By.xpath(objMap.getLocator("strorderNumberOld"))).getText());
			RESULT.passed("Submit Order",
					"Order should be submitted",
					"Order is submitted: "+ webDriver.findElement(By.xpath(objMap.getLocator("strorderNumberOld"))).getText());
		}
		else
		{
			RESULT.failed("Submit Order", "Order should be submitted", "Order is not submitted.");
			return;
		}
		}catch(Exception e)
		{
			RESULT.failed("Error encountered", "Order should be submitted",
					"Error occured" + e);
		}
	}
}