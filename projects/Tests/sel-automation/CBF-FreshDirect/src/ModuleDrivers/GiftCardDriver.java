package ModuleDrivers;

import java.rmi.server.UID;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.sun.org.apache.bcel.internal.generic.Select;

import cbf.engine.BaseModuleDriver;
import cbf.engine.TestResultLogger;
import cbf.engine.TestResult.ResultType;
import cbf.utils.DataRow;
import cbf.utils.SleepUtils;
import cbf.utils.SleepUtils.TimeSlab;

public class GiftCardDriver extends BaseModuleDriver{
	public GiftCardDriver(TestResultLogger resultLogger) {
		super(resultLogger);
		RESULT = resultLogger;
	}
	//customize or donate a gift card
	public void PurchaseDonateGiftcard(DataRow input,DataRow output)throws InterruptedException 
	{
		try{
			uiDriver.waitForPageLoad();
			//verify that you are on gift cards page	
			if(webDriver.getTitle().contains("Gift Cards"))
			{
				if (CompositeAppDriver.startUp.equalsIgnoreCase("SAFARI")) 
					webDriver.findElement(By.xpath(objMap.getLocator("btnpurchase"))).click();
				else
					webDriver.findElement(By.xpath(objMap.getLocator("btnpurchase"))).sendKeys("\n");
				uiDriver.waitForPageLoad();
				
			}else
			{
				RESULT.failed("Gift Card Page", "User should be on Gift Cards page", "Unfortunately user is not on Gift Cards page");
				return;
			}
			
			//verify that you are on purchase gift cards page
			try{
				uiDriver.wait.until(ExpectedConditions.presenceOfElementLocated(By.name(objMap.getLocator("txtgiftCardForm"))));
			}catch(Exception e){
				RESULT.failed("Navigation to Gift Card Page", "Navigation to Gift Card Page should be successful", "User is not on Gift Cards page");
				return;
			}
			
			if(uiDriver.isDisplayed("txtgiftCardForm"))
			{
				//check you have selected to customize a gift card
				if (input.get("DonateCust").contains("Customise"))
				{
					webDriver.findElement(By.linkText(input.get("Event"))).click();
					uiDriver.click("btncustomize");
					//verify that you are on add gift cards page
					try{
						uiDriver.wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(objMap.getLocator("strGiftSubMsgCust"))));
					}catch(Exception e){
						RESULT.failed("Customize Gift Card Page", "User should be on Customize Gift Card Page", "User is not on Customize Gift Card page");
						return;
					}
					if (uiDriver.isDisplayed("strGiftSubMsgCust")) 
					{
						//add all details on the page
						uiDriver.getwebDriverLocator(objMap.getLocator("txtsenderName")).sendKeys(input.get("SenderName")); //buyer's name
						uiDriver.getwebDriverLocator(objMap.getLocator("txtsenderEmail")).sendKeys(input.get("SenderEmail")); //buyer's email
						uiDriver.getwebDriverLocator(objMap.getLocator("txtreceiverName")).sendKeys(input.get("ReceiverName")); //receiver's name
						uiDriver.getwebDriverLocator(objMap.getLocator("txtreceiverEmail")).sendKeys(input.get("ReceiverEmail")); //receiver's email
						uiDriver.setValue("drpamount", input.get("Amount")); //amount of gift card
						uiDriver.getwebDriverLocator(objMap.getLocator("txtprintMsg")).clear(); 
						uiDriver.getwebDriverLocator(objMap.getLocator("txtprintMsg")).sendKeys(input.get("Message")); //print message on gift card
						
						//method of sending gift card:by Email,By sending in PDF format
						if (input.get("DeliveryMethod").contains("Email"))
						{
							uiDriver.click("radmethodEmail");
						}
						else if(input.get("DeliveryMethod").contains("PDF"))
						{
							uiDriver.click("radmethodPdf");
						}
						
						uiDriver.setValue("drpoccasion", input.get("Event")); //gift card design
						uiDriver.click("btnpreview"); //preview the gift card
						uiDriver.isDisplayed("strpreviewAmount");
						//verify all details displayed on preview card is same as added or not
						String amnt=uiDriver.getwebDriverLocator(objMap.getLocator("strpreviewAmount")).getText();
						//verify amount
						if(amnt.contains(input.get("Amount")))
						{
							RESULT.passed("Preview amount", "Preview amount should be"+input.get("Amount"), 
									"preview amount matched with amlount selected"+amnt);
						}
						else
						{
							RESULT.failed("Preview amount", "Preview amount should be"+input.get("Amount"), 
									"preview amount could not matched with amlount selected"+amnt);
						}
						
						//verify receiver name
						WebElement receiver=uiDriver.getwebDriverLocator(objMap.getLocator("strpreviewSender"));
						if(receiver.getText().contains(input.get("ReceiverName")))
						{
							RESULT.passed("Receiver name", "Receiver name should be matched with given:"+input.get("ReceiverName"),
									"Receiver matched"+receiver.getText());
						}
						else
						{
							RESULT.failed("Receiver name", "Receiver name should be: "+input.get("ReceiverName"), "Receiver could not match:"+receiver.getText());
						}
						
						//verify sender name
						WebElement sender=uiDriver.getwebDriverLocator(objMap.getLocator("strpreviewReceiver"));
						if(sender.getText().contains(input.get("SenderName")))
						{
							RESULT.passed("Sender name", "Sender name should be: "+input.get("SenderName"), "Sender matched:"+sender.getText());
						}
						else
						{
							RESULT.failed("Sender name", "Sender name should be: "+input.get("SenderName"), "sender could not match:"+sender.getText());
						}
						
						//verify message
						WebElement message=uiDriver.getwebDriverLocator(objMap.getLocator("strPrvwmsg"));
						if(message.getText().contains(input.get("Message")))
						{
							RESULT.passed("Message", "Message should be: "+input.get("Message"), "message matched:"+message.getText());
						}
						else{
							RESULT.failed("Message", "Message should be: "+input.get("Message"), "message didnot matched:"+message.getText());
						}
						
						//close preview page 
						try{
							uiDriver.click("imgclosePrevw");
							if (CompositeAppDriver.startUp.equalsIgnoreCase("CHROME")) 
								uiDriver.wait.until(ExpectedConditions.visibilityOfElementLocated(By.name(objMap.getLocator("btnAddtoList"))));
							else	
								uiDriver.wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className(objMap.getLocator("imgclosePrevw"))));
							uiDriver.click("btnAddtoList"); //add to list
							uiDriver.waitForPageLoad();
							uiDriver.wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(objMap.getLocator("strsuccessOrError"))));
							
							// wait for safari
							if (CompositeAppDriver.startUp.equalsIgnoreCase("SAFARI")) 
								SleepUtils.getInstance().sleep(TimeSlab.YIELD);
							
							if(uiDriver.isElementPresent(By.className(objMap.getLocator("strverifycardAdded"))))
							{
								//verify success message and list get updated with created gift card and sub total
								String verif=uiDriver.getwebDriverLocator(objMap.getLocator("strverifycardAdded")).getText();
								if(verif.contains("Your recipient list has been updated. You may enter the details for another gift or place your order by clicking the"))
								{
									RESULT.passed("Message verify", "Expected message should be on the page", "Desired message found on the page:"+verif);
								}
								else
								{
									RESULT.failed("Message verify", "Expected message should be on the page", "Desired message not found on the page:"+verif);
								}
							}
							else if(uiDriver.isElementPresent(By.className(objMap.getLocator("strfailmsg"))))
							{
								//check which details gone wrong or incorrect 
								String verif=uiDriver.getwebDriverLocator(objMap.getLocator("strfailmsg")).getText();
								if(verif.contains("We're sorry; some of the information below is missing or was filled out incorrectly."))
								{
									String detailsIncorct=uiDriver.getwebDriverLocator(objMap.getLocator("strincorrectDetl")).getText();
									RESULT.failed("Message verify","Some information provided is incorrect", "Missing or incorrect information for fields:"+detailsIncorct);
									return;
								}
							}
							else
							{
								RESULT.failed("Message verify", "Expected message should be on the page", "Desired message not found on the page");
							}
						}
						catch(Exception e)
						{
							RESULT.failed("Close Gift Card Preview","Gift Card Preview should be closed successfully", "Gift Card Preview is not closed");
							return;
						}
						
						//verify list is updated or not
						if(uiDriver.isElementPresent(By.className(objMap.getLocator("strsubtotalgift"))))
						{
							String subtotal=uiDriver.getwebDriverLocator(objMap.getLocator("strsubtotalgift")).getText();
							if(subtotal.isEmpty())
							{
								RESULT.failed("List updation with subtotal", "Recepient list should get updated with subtotal", "Recepient list could not get updated and Subtotal was not found");
							}
							else
							{
								RESULT.passed("List updation with subtotal", "Recepient list should get updated with subtotal", "Recepient list got updated and Subtotal found:"+subtotal);
							}
						}
						
					}
					else
					{
						RESULT.failed("you are on wrong page", "User should be on Add gift card page", "Unfortunately you are currently on wrong page");
					}
				}
				
				//Go for Donate gift card option
				else if (input.get("DonateCust").contains("Donate"))
				{
					uiDriver.click("radorgName"); //select organization radio button
					uiDriver.click("btndonate");//click on donate a gift card
					uiDriver.waitForPageLoad();
					uiDriver.setValue("txtsenderName", input.get("SenderName"));//buyer's name
					uiDriver.setValue("txtorganizationNm", input.get("OrganizationName"));//organization name
					uiDriver.setValue("txtsenderEmail", input.get("SenderEmail"));//buyer's email
					uiDriver.setValue("drpamount", input.get("Amount")); //amount of gift card
				}
				else
				{
					RESULT.failed("Wrong argument for DonateCust", "Argument for DonateCust should be either Customise or Donate", "Argument for DonateCust is other than Customise or Donate");
				}
				//click on  continue
				try{
					uiDriver.click("btncontinue");
					uiDriver.wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(objMap.getLocator("imgcreditcardpage"))));
				}catch(Exception e){
					RESULT.failed("Navigation to Credit Card page", "User should be on choose credit card page", 
							"User is not on choose credit card");					
				}
				if(webDriver.getTitle().endsWith("Purchase Gift Card") || uiDriver.isDisplayed("imgcreditcardpage"))	
					RESULT.passed("Choose credit card page", "User should be on choose credit card page", "User is on choose credit card page.");
				else
					RESULT.failed("Choose credit card page", "User should be on choose credit card page", "User is on other page than choose credit card");
			}
			else
			{
				RESULT.failed("you are on wrong page", "User should be on purchase gift card page", "Unfortunately you are currently on wrong page");
			}
		} catch (Exception e) {
			RESULT.failed("Purchase or Donate gift card Component",
					"Purchase or Donate gift card should be launched successfully",
			"Purchase or Donate gift card failed");		
		}
	}

	public void VerifyGIftcardPayment(DataRow input,DataRow output)throws InterruptedException 
	{
		try{
			// click on submit button
			uiDriver.getwebDriverLocator(objMap.getLocator("btnGiftCsubmit")).click();
			uiDriver.waitForPageLoad();
			uiDriver.wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id(objMap.getLocator("btnGiftCsubmit"))));
			if(CompositeAppDriver.startUp.equalsIgnoreCase("SAFARI"))
				SleepUtils.getInstance().sleep(TimeSlab.MEDIUM);
			//fetch success message for purchase or donate gift card
			if(uiDriver.isElementPresent(By.xpath(objMap.getLocator("strGiftSubMsgDonat"))))
			{
				String submitMsg=uiDriver.getwebDriverLocator(objMap.getLocator("strGiftSubMsgDonat")).getText();
				String msg1="Thank you for supporting New York Common Pantry!";
				if(submitMsg.contains(msg1))
				{
					RESULT.passed("Gift card Donated ", "Success message:"+msg1+"should be displayed", "Success message matched with expected message: "+submitMsg);
				}
				else
					RESULT.failed("Gift card Donated ", "Success message:"+msg1+"should be displayed", "Success message did not match with expected message: "+submitMsg);
			} 
			else if(uiDriver.isElementPresent(By.xpath(objMap.getLocator("strGiftSubMsgCust"))))
			{
				String submitMsg1=uiDriver.getwebDriverLocator(objMap.getLocator("strGiftSubMsgCust")).getText();
				String msg2="Thank you for buying Gift Cards";
				if(submitMsg1.contains(msg2))
					RESULT.passed("Gift card Purchased ", "Success message:"+msg2+"should be displayed", "Success message matched with expected message: "+submitMsg1);			
				else
					RESULT.failed("Gift card Donated", "Success message:"+msg2+"should be displayed", "Success message did not match with expected message: "+submitMsg1);		
			}else{
				RESULT.failed("Gift card Donated/Purchase", "Gift card Donated/Purchase success message should be displayed", "Gift card Donated/Purchase success message not available");	
			}
		}catch (Exception e) {
			RESULT.failed("Gift card Payment Component",
					"Gift card Payment should be successfully",
			"Gift card Payment failed");	
		}
	}

}