package ModuleDrivers;

import java.util.List;
import java.util.concurrent.TimeoutException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import ui.ObjectMap;
import cbf.engine.BaseModuleDriver;
import cbf.engine.TestResultLogger;
import cbf.engine.TestResult.ResultType;
import cbf.utils.DataRow;
import cbf.utils.SleepUtils;
import cbf.utils.SleepUtils.TimeSlab;

public class OrderStorefront_oldDriver extends BaseModuleDriver{

	public OrderStorefront_oldDriver(TestResultLogger resultLogger) {
		super(resultLogger);
		// TODO Auto-generated constructor stub
	}

	public void DeliveryAddress(DataRow input, DataRow output) throws   InterruptedException
	{  

		try{ 
			//if page is not checkout then first navigate to checkout
			if(!(webDriver.findElements(By.linkText("Delivery Addresses")).size()>0))
			{
				if(!webDriver.getTitle().contains("view_cart"))
				{
					if (CompositeAppDriver.startUp.equalsIgnoreCase("IE"))
					{
						uiDriver.getwebDriverLocator(objMap.getLocator("btnyourCart")).sendKeys("\n");
					}
					else
					{
					uiDriver.getwebDriverLocator(objMap.getLocator("btnyourCart")).click();
					}
					uiDriver.waitForPageLoad();
					//uiDriver.wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(objMap.getLocator("btnchooseDelAddress"))));
				}
				if(webDriver.findElements(By.xpath(objMap.getLocator("strstandingOrder"))).size()>0){
					String Order =webDriver.findElement(By.xpath(objMap.getLocator("strstandingOrder"))).getText();
					System.out.println(Order);
					if(Order.toLowerCase().contains("standing order")){
						RESULT.passed("Standing Order Verification", "Order Should be standing Order", "Order is standing Order");
					}
				}

				uiDriver.getwebDriverLocator(objMap.getLocator("btnchooseDelAddress")).click();
				uiDriver.waitForPageLoad();
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
					"Delivery Address selection failed due to :" + e.getMessage());
		}	
	}

	public void TimeSlot(DataRow input,DataRow output) throws   InterruptedException{
		try{
			//user will be on delivery address page. so go to select time slot page
			if(webDriver.findElements(By.xpath(objMap.getLocator("btnchooseTimeSlot"))).size()>0)
			{
				uiDriver.getwebDriverLocator(objMap.getLocator("btnchooseTimeSlot")).click();
				uiDriver.waitForPageLoad();
				//			SleepUtils.getInstance().sleep(TimeSlab.HIGH);
				if(webDriver.findElements(By.xpath(objMap.getLocator("strnoDeliveryError"))).size()>0)
				{
					String Error=webDriver.findElement(By.xpath(objMap.getLocator("strnoDeliveryError"))).getText();
					System.out.println(Error);
					RESULT.failed("Order Total", "Order Total should be more than minimum required Total", "Order Total is less than minimum required Total  "+Error);
					return;
				}
			} 

			//check if it asks for age verification for products containing alcohol
			if(webDriver.findElements(By.xpath(objMap.getLocator("strageverify"))).size()>0)
			{
				if(webDriver.findElement(By.xpath(objMap.getLocator("strageverify"))).getText()
						.contains("Age Verification"))
				{
					WebElement accept=uiDriver.getwebDriverLocator(objMap.getLocator("strageVerification"));
					accept.click();
					uiDriver.getwebDriverLocator(objMap.getLocator("btnageVerified")).click();                                    
					//webDriver.findElement(By.className("imgButtonOrange")).click();
				}
			}
			//Area restriction for alcoholic products
			if(webDriver.findElements(By.xpath(objMap.getLocator("stralcohol"))).size()>0) 
			{
				//check the message displayed
				String Alcohol_restriction= uiDriver.getwebDriverLocator(objMap.getLocator("stralcohol")).getText();
				if(!(Alcohol_restriction==null ||Alcohol_restriction==""))
					RESULT.done("Alcoholic product restriction in particular area", "Message for Alcoholic product cannot not be delivered to some area", Alcohol_restriction);
				uiDriver.getwebDriverLocator(objMap.getLocator("btncontinueChkoutOld")).click();
				uiDriver.waitForPageLoad();
			}           
			//check if user has reached to selection of time slot page
			if(webDriver.findElements(By.cssSelector(objMap.getLocator("btnpaymentBtnNew"))).size()>0)
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
			RESULT.failed("Time slot Checkout Exception", "Time slot shoud be selected",
					"Time slot is not selected and the exception caught is " + e.getMessage());
		}
	}

	public void PaymentOptions(DataRow input, DataRow output) throws   InterruptedException
	{
		try{
			boolean gift=false;
			uiDriver.waitForPageLoad();
			if(webDriver.findElements(By.cssSelector(objMap.getLocator("btnpaymentBtnNew"))).size()>0 && 
					webDriver.findElement(By.cssSelector(objMap.getLocator("btnpaymentBtnNew"))).isDisplayed())
			{
				uiDriver.click("btnpaymentBtnNew");
				if (CompositeAppDriver.startUp.equalsIgnoreCase("SAFARI")) {
					SleepUtils.getInstance().sleep(TimeSlab.YIELD);
					uiDriver.wait.until(ExpectedConditions.invisibilityOfElementLocated(By.linkText("CHOOSE TIME")));
				}			
//				uiDriver.wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(objMap.getLocator("btnpaymentBtnNew"))));
				uiDriver.waitForPageLoad();
				if (CompositeAppDriver.startUp.equalsIgnoreCase("SAFARI")) 
					SleepUtils.getInstance().sleep(TimeSlab.MEDIUM);
			}else if(webDriver.findElements(By.id(objMap.getLocator("btnGiftCsubmit"))).size()>0 && 
					webDriver.findElement(By.id(objMap.getLocator("btnGiftCsubmit"))).isDisplayed())
			{
				RESULT.done("Payment Page for Gift card", "User Should be on Paymnet page of Gift card",
				"User is on Gift card Payment page");
				gift=true;
			}else
			{
				RESULT.failed("Payment Page for Gift card", "User Should be on Paymnet page of Gift card",
				"User is neither on Normal checkout page nor Gift card Payment page");
				return;
			}
			//age verification for alcoholic products
			/*		if(webDriver.getTitle().endsWith("Age Verification for orders containing alcohol"))
		{
			WebElement accept=uiDriver.getwebDriverLocator(objMap.getLocator("strAgeVerification"));
			accept.click();
			uiDriver.getwebDriverLocator(objMap.getLocator("btnageVerified")).click();                                    
			//webDriver.findElement(By.className("imgButtonOrange")).click();
		}
			 */
			//Product unavailable at particular time
			if(webDriver.findElements(By.xpath(objMap.getLocator("btncontinueChkoutOld"))).size()>0 &&
					webDriver.findElement(By.xpath(objMap.getLocator("btncontinueChkoutOld"))).isDisplayed())
			{
				String ProdUnavailable=uiDriver.getwebDriverLocator(objMap.getLocator("strunavailabilityTime")).getText();
				RESULT.warning("Product unavailable at particular time", "Product should be available at particular time",
						ProdUnavailable);
				uiDriver.click("btncontinueChkoutOld");
				uiDriver.waitForPageLoad();
				uiDriver.wait.until(ExpectedConditions.visibilityOfElementLocated(By.name(objMap.getLocator("imgfd_Logo"))));
				//			SleepUtils.getInstance().sleep(TimeSlab.MEDIUM);
				if(webDriver.findElements(By.xpath(objMap.getLocator("btnchooseDelAddress"))).size()>0
						&& webDriver.findElement(By.xpath(objMap.getLocator("btnchooseDelAddress"))).isDisplayed())
				{
					try{
						String Error=webDriver.findElement(By.xpath(objMap.getLocator("strerrorOrderTotal"))).getText();
						System.out.println(Error);
						if(!(Error==null||Error==""))
						{
							RESULT.warning("Order Total", "Order Total should be more than minimum required Total", Error);
							return;
						}
					}catch (Exception e) {
						RESULT.failed("Choose Payment", "Error should be generated on Delivery address page",
						"Exception in Error generation on Delivery address page");
						return;
					}
				}
				else if(webDriver.findElements(By.xpath(objMap.getLocator("btnorderExistChoosePayment"))).size()>0
						&& webDriver.findElement(By.xpath(objMap.getLocator("btnorderExistChoosePayment"))).isDisplayed()) 
				{
					uiDriver.click("btnorderExistChoosePayment");
					uiDriver.waitForPageLoad();
				}
			}else if (webDriver.findElements(By.xpath(objMap.getLocator("btnorderExistChoosePayment"))).size()>0
					&& webDriver.findElement(By.xpath(objMap.getLocator("btnorderExistChoosePayment"))).isDisplayed()) 
			{
				uiDriver.click("btnorderExistChoosePayment");
				uiDriver.waitForPageLoad();
			}
//			else
//			{
//				if(!(webDriver.findElements(By.className(objMap.getLocator("btnreviewOrder"))).size()>0)|| 
//						!(webDriver.findElements(By.id(objMap.getLocator("btnGiftCsubmit"))).size()>0))
//				{
//					RESULT.failed("Choose Payment", "Submit order button should be available",
//					"User is on unexpected page");
//					return;
//				}
//			}
			/*//Order Total insufficient
			if (webDriver.getTitle().equals("FreshDirect - View Cart")) 
			{
//				SleepUtils.getInstance().sleep(TimeSlab.YIELD);
				String Error=uiDriver.getwebDriverLocator(objMap.getLocator("strReplacementError")).getText();
				RESULT.failed("ChoosePaymentoption", "Payment page should be displayed", Error); 
				return;
			}*/
			//check if user is on payment options page
			try{
				if(!gift){
				uiDriver.wait.until(ExpectedConditions.visibilityOfElementLocated(By.className(objMap.getLocator("btnreviewOrder"))));
				}
			}catch (Exception e) {
				RESULT.failed("Choose Payment", "Review order button should be available",
				"Review order button is not available");
				return;
			}
			if((webDriver.findElements(By.className(objMap.getLocator("btnreviewOrder"))).size()>0 && 
					webDriver.findElement(By.className(objMap.getLocator("btnreviewOrder"))).isDisplayed())|| 
					(webDriver.findElements(By.id(objMap.getLocator("btnGiftCsubmit"))).size()>0 &&
							webDriver.findElement(By.id(objMap.getLocator("btnGiftCsubmit"))).isDisplayed()))
			{
				uiDriver.FD_paymentOption_old(input.get("AddEditDeleteSelect"),input.get("cardDetail"),
						input.get("NameOnCard"),input.get("routing"),input.get("Bank"), input.get("CardOrAcctype"), 
						input.get("CardNum"), input.get("ExpiryMnth"), input.get("ExpiryYr"), input.get("CardStreetAdd"), 
						input.get("CardAppNum"), input.get("CardAddLine2"), input.get("CardCountry"), input.get("CardCity"), 
						input.get("CardState"), input.get("CardZip"),input.get("FlexibilityFlag"));
			}else
			{
				RESULT.failed("Choose Payment", "Submit order button should be available",
				"Submit order button is not available");
				return;
			}
		}catch(Exception e)
		{
			RESULT.failed("ChoosePaymentoption", input.get("AddEditDeleteSelect")+" Payment Option should be successfull",
					"Choose payment option unsuccessful due to exception: " + e.getMessage());
		}
	}

	public void ReviewSubmitOrder(DataRow input, DataRow output) throws   InterruptedException
	{
		try{
			SleepUtils.getInstance().sleep(TimeSlab.YIELD);
			uiDriver.click("btnreviewOrder");
			uiDriver.waitForPageLoad();
			try{
				uiDriver.wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(objMap.getLocator("btnsubmitOrder"))));
			}catch (Exception e) {
				RESULT.failed("Review order", "User should be on review order page",
				"Redirection to review order page is unsuccessful");
				return;
			}
			uiDriver.FD_reviewOrder();
			uiDriver.click("btnsubmitOrder");
			uiDriver.waitForPageLoad();
			uiDriver.wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(objMap.getLocator("strorderNumberOld"))));
			//SleepUtils.getInstance().sleep(TimeSlab.YIELD);
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
			RESULT.failed("Review Submit Order", "Review Submit order should be sucessful",
					"Review submit order is not successful and the exception caught is " + e.getMessage());
		}
	}

}