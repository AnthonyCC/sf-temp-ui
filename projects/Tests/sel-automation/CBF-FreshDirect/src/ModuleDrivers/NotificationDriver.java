package ModuleDrivers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import ui.WebUIDriver;
import cbf.engine.BaseModuleDriver;
import cbf.engine.TestResultLogger;
import cbf.utils.DataRow;
import cbf.utils.SleepUtils;
import cbf.utils.SleepUtils.TimeSlab;


public class NotificationDriver extends BaseModuleDriver{
	public static YourAccountDriver accdriver;

	public NotificationDriver(TestResultLogger resultLogger) {
		super(resultLogger);
		accdriver=new YourAccountDriver(RESULT);
		// TODO Auto-generated constructor stub
	}

	public void EmailURLLaunch(DataRow input,DataRow output)throws InterruptedException  {
		try{
			uiDriver.launchApplication(input.get("url"));
			//wait for page to load
			uiDriver.waitForPageLoad();
			try{
				uiDriver.wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(objMap.getLocator("btnloginEmail"))));
			}catch(Exception e){
				RESULT.failed("Email Login button","Email Login button should be available",
				"Email Login button is not available");
			}
			if(webDriver.getTitle().contains(input.get("PageName"))){
				RESULT.passed("Dev URL launch","Dev login page should be displayed",
				"Dev login page is displayed");
			}
			else{
				RESULT.failed("Dev URL launch","Dev login page should be displayed",
				"Dev login page is NOT displayed");
			}
		}catch(Exception e){
			RESULT.failed("Email URL launch","Email login page should be displayed",
			"Email login page failed");
		}
	}


	public void EmailLogin(DataRow input,DataRow output)throws InterruptedException  
	{
		try{
			uiDriver.FD_loginEmailNotification(input.get("Username"),input.get("Password"));
			if(webDriver.getTitle().contains(input.get("PageName")))
			{
				RESULT.passed("Login to SquirrelMail","Login should be successful",
						"Login successful with userid"+input.get("Username"));
			}
			else
			{
				RESULT.failed("Login to SquirrelMail","Login should not be successful",
						"Login unsuccessful with userid"+input.get("Username"));
			}
		}catch(Exception e){
			RESULT.failed("Email Page Login component","Email Page Login should be successful",
			"Email Page Login failed");
		}
	}

	public void EmailLogout(DataRow input,DataRow output)throws InterruptedException  
	{
		try{
			webDriver.switchTo().defaultContent();
			webDriver.switchTo().frame("right");
			webDriver.findElement(By.partialLinkText("Sign Out")).click();
			uiDriver.waitForPageLoad();
			if(webDriver.getTitle().contains(input.get("PageName")))
			{
				RESULT.passed("Logout from SquirrelMail","Logout should be successful",
				"Logout successful from SquirrelMail");
			}
			else
			{
				RESULT.failed("Logout from SquirrelMail","Logout should be successful",
				"Logout unsuccessful from SquirrelMail");
			}
		}catch(Exception e){
			RESULT.failed("Logout from SquirrelMail","Logout should be successful",
			"Logout unsuccessful from SquirrelMail");
		}
	}

	public void CheckMail(DataRow input,DataRow output) throws InterruptedException, ParseException {

		try
		{
			//To select environment
			webDriver.switchTo().frame("left").findElement(By.linkText(input.get("Environment"))).click();
			//SleepUtils.getInstance().sleep(TimeSlab.YIELD);
			uiDriver.waitForPageLoad();
			//wait to validate email notification triggered for 3-5 minutes 
			int count=0;
			//set counter for unread email
			int counter=0;

			boolean break_list=false;
			do
			{
				//click for search
				webDriver.switchTo().defaultContent();
				webDriver.switchTo().frame("right");
				uiDriver.wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//b[contains(text(),'"+input.get("Environment")+"')]")));
				webDriver.findElement(By.linkText(objMap.getLocator("lnksearch"))).click();
				uiDriver.waitForPageLoad();
				uiDriver.wait.until(ExpectedConditions.visibilityOfElementLocated(By.name(objMap.getLocator("txtsearchCriteria"))));
				webDriver.findElement(By.name(objMap.getLocator("txtsearchCriteria"))).sendKeys(input.get("Subject"));
				uiDriver.setValue("drpsearchCriteria", "Subject");
				webDriver.findElement(By.name(objMap.getLocator("btnsearch"))).click();
				uiDriver.waitForPageLoad();
				uiDriver.wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(objMap.getLocator("txtsearchResults"))));
				do{

					counter=0;
					//take list of all email with given sub name
					List<WebElement> mail=webDriver.findElements(By.xpath(objMap.getLocator("rowmail")));
					System.out.println(mail.size()+": mails with same subject");
					outer:for (int j=0;j<mail.size();j++)
					{
						if(mail.get(j).findElements(By.xpath(objMap.getLocator("boldTag"))).size()>0)
						{
							counter++;
						}
						for(WebElement e:mail)
							//for(int i = 0; i < NewMails.size(); i++) 
						{
							//Check Subject e.findElement(By.xpath("td[2]/b")).getTagName()
							//					System.out.println(e.findElements(By.xpath(objMap.getLocator("boldTag"))).size());
							//check if email is unread
							if(!(e.findElements(By.xpath(objMap.getLocator("boldTag"))).size()>0))
							{
								continue;
							}
							SimpleDateFormat dformat=new SimpleDateFormat("E',' MMMM d',' yyyy hh:mm a");
							//System.out.println(e.findElement(By.xpath("td[3]")).getText());
							//Date emaildate=dformat.parse(e.findElement(By.xpath("td[3]")).getText());

							accdriver.sysdate=dformat.parse(accdriver.date_text);
							System.out.println(accdriver.date_text);
							//				if(emaildate.compareTo(accdriver.sysdate)>=0)
							//					if(e.findElement(By.xpath(objMap.getLocator("strsubjectMail"))).getText().contains(input.get("Subject")))
							//						if(e.findElement(By.xpath("td[5]")).getText().contains(input.get("Subject")))

							//open unread email
							e.findElement(By.partialLinkText(input.get("Subject"))).click();
							uiDriver.waitForPageLoad();
							uiDriver.wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(objMap.getLocator("stremailHeader"))));
							String email_header=webDriver.findElement(By.xpath(objMap.getLocator("stremailHeader"))).getText();

							//Verify Date
							String email_date[]=email_header.split("Date:");
							email_date[1]=email_date[1].trim();
							String email_date_value[]=email_date[1].split("\\r?\\n");
							Date emaildate=dformat.parse(email_date_value[0]);

							// 					compare date in header part of email with date of addition or updation of address
							if(accdriver.sysdate.compareTo(emaildate)>=0)
							{
								String email_from[] =email_header.split("From:");
								email_from[1]=email_from[1].trim();
								String email_from_value[]=email_from[1].split("\\r?\\n");
								//Verify To
								String email_To[]=email_header.split("To:");
								email_To[1]=email_To[1].trim();
								String email_To_value[]=email_To[1].split("\\r?\\n");;

								//						check To and From for opened email
								if(!(email_from[1]==null || email_from[1].equals("") ||email_To[1]==null || 
										email_To[1].equals("") )&& email_from_value[0].replace("\"", "").equalsIgnoreCase(input.get("FromEmail")) && 
										email_To_value[0].equals(uiDriver.fd_username) )
								{
									//Email Change notification
									if(input.get("Subject").contains("Email"))		
									{
										if(webDriver.findElements(By.linkText(objMap.getLocator("lnkmore"))).size()>0)
										{
											webDriver.findElement(By.linkText(objMap.getLocator("lnkmore"))).click();
											SleepUtils.getInstance().sleep(TimeSlab.YIELD);
											email_header=webDriver.findElement(By.xpath(objMap.getLocator("stremailHeader"))).getText();
											String email_Cc[]=email_header.split("Cc:");
											String email_Cc_value[];
											email_Cc[1]=email_Cc[1].trim();
											email_Cc_value=email_Cc[1].split("\\r?\\n");
											//verify CC value is not null but it is new email id 
											if(!(email_Cc[1]==null || email_Cc[1].equals("")) && email_Cc_value[1].equals(accdriver.new_id))
											{
												//NotificationData should be created
												break_list=true;
												RESULT.passed("Email ID validation", "Email ID change should be successfully validated ", "Email ID change  successfully validated ");
												break outer;
											}
											else
											{
												//Write code for going back
												//webDriver.navigate().back();
												webDriver.findElement(By.linkText(objMap.getLocator("lnksearchResult"))).click();
												uiDriver.waitForPageLoad();
												uiDriver.wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(objMap.getLocator("rowmail"))));
												mail=webDriver.findElements(By.xpath(objMap.getLocator("rowmail")));
												break;
											}
										}
										else
										{
											RESULT.failed("CC field : ", "CC field must have null value", "CC field has null value");
										}
									}
									//Shipping address change/add notification
									else if(input.get("Subject").contains("Address"))		
									{
										//check in body for modified value(here FirstName has been taken) 
										if(webDriver.findElement(By.xpath(objMap.getLocator("strbody"))).getText().contains(WebUIDriver.fistname))
										{
											System.out.println("body"+webDriver.findElement(By.xpath(objMap.getLocator("strbody"))).getText());
											break_list=true;
											if(input.get("Subject").contains("changed")){
												RESULT.passed("Shipping address change validation", "Shipping address change should be successfully validated ", 
												"Shipping address changed successfully validated ");
												break outer;
											}else if(input.get("Subject").contains("added"))
											{
												RESULT.passed("Shipping address add validation", "Shipping address addition should be successfully validated ", 
												"Shipping address addition successfully validated ");
											}/*else{
											RESULT.failed("Shipping address validation", "Shipping address subject should be for add or change",
											"Shipping address subject incorrect");
											return;
										}*/
											break outer;
										}else
										{
											System.out.println("body/to did not matched");
											webDriver.findElement(By.linkText(objMap.getLocator("lnksearchResult"))).click();
											//										webDriver.navigate().back();
											uiDriver.waitForPageLoad();
											uiDriver.wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(objMap.getLocator("rowmail"))));
											mail=webDriver.findElements(By.xpath(objMap.getLocator("rowmail")));
											break;
										}
									}
									else
									{
										//									webDriver.navigate().back();
										webDriver.findElement(By.linkText(objMap.getLocator("lnksearchResult"))).click();
										uiDriver.waitForPageLoad();
										uiDriver.wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(objMap.getLocator("rowmail"))));
										mail=webDriver.findElements(By.xpath(objMap.getLocator("rowmail")));
										break;
									}
								}else
								{
									//									webDriver.navigate().back();
									webDriver.findElement(By.linkText(objMap.getLocator("lnksearchResult"))).click();
									uiDriver.waitForPageLoad();
									uiDriver.wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(objMap.getLocator("rowmail"))));
									mail=webDriver.findElements(By.xpath(objMap.getLocator("rowmail")));
									break;
								}
							}
							else
							{
								//							webDriver.navigate().back();
								webDriver.findElement(By.linkText(objMap.getLocator("lnksearchResult"))).click();
								uiDriver.waitForPageLoad();
								uiDriver.wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(objMap.getLocator("rowmail"))));
								mail=webDriver.findElements(By.xpath(objMap.getLocator("rowmail")));
								break;
							}
						}
					}
					counter--;
				}while(counter>=1);
				if(break_list==true)
				{
					break;
				}
				//				}		
				count++;
				//static wait of 5 seconds if email has not been triggered then check again 
				Thread.sleep(5000);
			}while(count<24 && break_list==false);

			if(break_list==false){
				RESULT.failed("Shipping address validation", "Shipping address change/add should be successfully validated ", 
				"Email not found for Shipping address change/add");
			}
		}
		catch(Exception e)
		{
			RESULT.failed("Check mail component failure","email for addition/updation of delivery address should be triggered", 
					"Exception in checking email: "+e.getMessage());			
		}
	}

}
