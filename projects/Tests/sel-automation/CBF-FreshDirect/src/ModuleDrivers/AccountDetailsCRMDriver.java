package ModuleDrivers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
//import org.sikuli.script.FindFailed;

import ui.WebUIDriver;

import cbf.engine.BaseModuleDriver;
import cbf.engine.TestResultLogger;
import cbf.engine.TestResult.ResultType;
import cbf.utils.DataRow;
import cbf.utils.SleepUtils;
import cbf.utils.SleepUtils.TimeSlab;

public class AccountDetailsCRMDriver extends BaseModuleDriver {
	// This will wait for the page to load for 300 seconds (i.e. 300000 Millie seconds) 
	
	public AccountDetailsCRMDriver(TestResultLogger resultLogger) {
		super(resultLogger);
		// TODO Auto-generated constructor stub
	}
	public void ClickAccountDetail(DataRow input, DataRow output)
	throws InterruptedException {
		// click on account details
		try
		{
			uiDriver.click("lnkAccountDetails");
			uiDriver.waitForPageLoad();
			//SleepUtils.getInstance().sleep(TimeSlab.MEDIUM);
			if (webDriver.getTitle().contains("/ FreshDirect CRM : Account Details /")) {
				RESULT.passed("Account Details Page Navigation", "page should be navigated to Account Details",
				"Page is navigated to Account Details");
			} else {
				RESULT.failed("Account Details Page Navigation", "page should be navigated to Account Details",
				"Page is not navigated to Account Details");
				return;
			}
		}
		catch(Exception e)
		{
			RESULT.failed("Click Account Details", "Account details link should be clicked successfully",
					"Account details link is not clicked");
		}
	}

	/*
	 * Component function for the update detail for a particular customer
	 */
	public void UpdateBasicInfo(DataRow input, DataRow output)
	throws InterruptedException {
		try{
			// Click on Edit button
			List<WebElement> EditLst = webDriver.findElements(By
					.xpath(objMap.getLocator("CRM_EditAddressPath")));
			int e1Size = EditLst.size();
			
				for (int k = 0; k < e1Size; k++) {
					String editbtn = EditLst.get(k).getAttribute("href");
					if (editbtn.contains("name_contact_info")) {
						EditLst.get(k).click();
						break;
					}
				}
	
		
			uiDriver.wait.until(ExpectedConditions.visibilityOfElementLocated(By.name(objMap.getLocator("drptitle"))));
			uiDriver.setValue("drptitle", input.get("Title"));
			uiDriver.setValue("txtfirstNameCRM", input.get("FirstName"));
			uiDriver.setValue("txtmiddleNameCRM",input.get("MiddleName"));
			uiDriver.setValue("txtlastNameCRM", input.get("LastName"));
			uiDriver.setValue("txthomePhoneCRM", input.get("HomePhone"));
			uiDriver.setValue("txthomeExtCRM",input.get("HomeExt"));
			uiDriver.setValue("txtworkPhoneCRM",input.get("WorkPhone"));
			uiDriver.setValue("txtworkExtCRM", input.get("WorkExt"));
			uiDriver.setValue("txtcellPhoneCRM", input.get("CellPhone"));
			uiDriver.setValue("txtcellExtCRM", input.get("CellExt"));
			uiDriver.setValue("txtaltEmailCRM",input.get("AlterEmail"));
			uiDriver.setValue("txtdepartment",input.get("Department"));
			uiDriver.setValue("txtempID", input.get("EMPID"));
			uiDriver.setValue("txtindustry", input.get("Industry"));
			uiDriver.setValue("txtemployees", input.get("Employees"));
			uiDriver.setValue("txtemail2", input.get("EmailCRM2"));
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Date date = new Date();
			String Date1=date.toString();
			Date1=Date1.replaceAll(" ", "");
			Date1=Date1.replaceAll(":", "");
			Date1=Date1.split("IST")[0];
			System.out.println(Date1);
			uiDriver.setValue("txtdisplayName", Date1);
			// click on Save Button
			uiDriver.click("lnkSave");
			uiDriver.wait.until(ExpectedConditions.invisibilityOfElementLocated(By
					.linkText(objMap.getLocator("lnkSave"))));
			uiDriver.waitForPageLoad();
			
			SleepUtils.getInstance().sleep(TimeSlab.MEDIUM);
			
			// verifying updated details of customer
			if (webDriver.findElements(By.xpath(objMap.getLocator("strbasicInfo"))).size() > 0){
				String BasicInfo = uiDriver.getwebDriverLocator(
						objMap.getLocator("strbasicInfo")).getText();
				System.out.println(BasicInfo);
				if (BasicInfo.toLowerCase().contains(
						input.get("FirstName").toLowerCase())
						&& BasicInfo.toLowerCase().contains(
								input.get("MiddleName").toLowerCase())
								&& BasicInfo.toLowerCase().contains(
										input.get("LastName").toLowerCase())
										&& BasicInfo.toLowerCase().contains(
												input.get("AlterEmail").toLowerCase())
												&& BasicInfo.toLowerCase().contains(
														input.get("Department").toLowerCase())
														&& BasicInfo.toLowerCase().contains(
																input.get("EMPID").toLowerCase())
																&& BasicInfo.toLowerCase().contains(
																		input.get("Industry").toLowerCase())
																		&& BasicInfo.toLowerCase().contains(
																				input.get("Employees").toLowerCase())
																				&& BasicInfo.toLowerCase().contains(
																						input.get("EmailCRM2").toLowerCase())
																						&& BasicInfo.toLowerCase().contains(
																								Date1.toLowerCase())) {
					System.out.println("Edited Details  :"+BasicInfo);
					RESULT.passed("Update customer basic information",
							"Should be able to update customer details successfully",
							"Customer details updation successful..!!");
				} else {
					RESULT.failed("Update customer detail",
							"Should be able to update customer details successfully",
					"Customer details updation is unsuccessful...!!");
					return;
				}
			}else{RESULT.failed("Update customer detail",
					"Should be able to update customer details successfully",
					"Error Due to invalid data");
			return;

			}
		}catch(Exception e)
		{
			RESULT.failed("Update Basic Info", "Basic Detail should be updated",
							"Basic detail is not updated and the exception caught is  : " + e.getMessage());
		}
	}

	/*FD_AccDetailsDelAdd_CRM
	 * Component function for the update delivery address for a particular
	 * customer
	 */
	public void UpdateAddressInfo(DataRow input, DataRow output)
	throws InterruptedException {
		try{
			// call to the CRM_DeliveryAddress reusable function
			uiDriver.FD_AccDetailsDelAdd_CRM(input
					.get("CRMAddSelectSelectPickupDeleteEdit"), input
					.get("CRMUseCustDefault"), input.get("CRMServiceType"), input
					.get("CRMCompName"), input.get("CRMFirstNameTb"), input
					.get("CRMLastNameTb"), input.get("CRMStreetAdd"), input
					.get("CRMAddLine2"), input.get("CRMAptName"), input
					.get("CRMCity"), input.get("CRMState"), input
					.get("CRMZipCode"), input.get("CRMContact"), input
					.get("CRMAltContact"), input.get("CRMSpecialDelivery"), input
					.get("CRMDoormanRadioBtn"), input.get("CRMNeighborRadioBtn"),
					input.get("CRMAltFirstName"), input.get("CRMAltLastName"),
					input.get("CRMAltApt"), input.get("CRMAltPhn"), input
					.get("CRMSaveCancelBtn"), input
					.get("CRMSelectAddress"));

			// RESULT based on the check for the address
			SleepUtils.getInstance().sleep(TimeSlab.MEDIUM);
			List<WebElement> addLst2 = webDriver.findElements(By.xpath(objMap.getLocator("CRM_AddressPath")));
			int eSize = addLst2.size();
			System.out.println(eSize);
			boolean check = false;

			for (int i = 0; i < eSize; i++) {

				String gotadd2 = addLst2.get(i).getText();
				System.out.println("Address:" + gotadd2);
				if (gotadd2.toLowerCase().contains(
						input.get("CRMFirstNameTb").toLowerCase())
						&& gotadd2.toLowerCase().contains(
								input.get("CRMLastNameTb").toLowerCase())
								&& gotadd2.toLowerCase().contains(
										input.get("CRMCompName").toLowerCase())
										&& gotadd2.toLowerCase().contains(
												input.get("CRMStreetAdd").toLowerCase())
												&& gotadd2.toLowerCase().contains(
														input.get("CRMAddLine2").toLowerCase())
														&& gotadd2.toLowerCase().contains(
																input.get("CRMAptName").toLowerCase())
																&& gotadd2.toLowerCase().contains(
																		input.get("CRMCity").toLowerCase())
																		&& gotadd2.toLowerCase().contains(
																				input.get("CRMState").toLowerCase())
																				&& gotadd2.toLowerCase().contains(
																						input.get("CRMZipCode").toLowerCase())) {
					check = true;
					System.out.println("edited address:" + gotadd2);
					uiDriver.robot.scrollToElement(addLst2.get(i),webDriver);
					RESULT.passed(
							"Update customer delivery address",
							"Should be able to search edited detail of delivery address",
					"Delivery address updation successful..!!");
					break;
				}
			}
			if (check == false)
			{RESULT.failed(
					"Update customer detail",
					"Should be able to search edited detail of delivery address",
					"Unable to update delivery address");
			return;
			}
		}catch(Exception e)
		{
			RESULT.failed("Update Address Info Exception", "Address Detail should be updated/selected ",
					"Address details could not be updated/selected and the exception caught is  " + e.getMessage());
		}
	}

	/*
	 * Component function for the update user name and password for a particular
	 * customer
	 */
	public void UpdateLoginInfo(DataRow input, DataRow output)
	throws InterruptedException {
		try{
			//uiDriver.click("lnkEdit_Password");
			List<WebElement> EditLst = webDriver.findElements(By
					.xpath(objMap.getLocator("CRM_EditAddressPath")));
			int e1Size = EditLst.size();
			for (int k = 0; k < e1Size; k++) {
				String editbtn = EditLst.get(k)
				.getAttribute("href");
				if (editbtn.contains("username_password_option")) {
					EditLst.get(k).click();
					break;
				}
			}
			uiDriver.wait.until(ExpectedConditions.visibilityOfElementLocated(By.name(objMap.getLocator("txtNew_Userid"))));
			// after clearing username and password field enter new username and password for customer
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Date date = new Date();
			String Date1=date.toString();
			Date1=Date1.replaceAll(" ", "");
			Date1=Date1.replaceAll(":", "");
			Date1=Date1.split("IST")[0];
			System.out.println(Date1);
			String EmailCRM="Test"+Date1+"@gmail.com";
			uiDriver.getwebDriverLocator(objMap.getLocator("txtNew_Userid")).clear();
			uiDriver.setValue("txtNew_Userid", EmailCRM);
			uiDriver.getwebDriverLocator(objMap.getLocator("txtPass_CRM")).clear();
			uiDriver.setValue("txtPass_CRM", input.get("Password"));
			uiDriver.getwebDriverLocator(objMap.getLocator("txtVerify_pass")).clear();
			uiDriver.setValue("txtVerify_pass", input.get("Password"));
			// click on save button
			uiDriver.click("lnkSave");
			uiDriver.waitForPageLoad();
			SleepUtils.getInstance().sleep(TimeSlab.MEDIUM);
			if (webDriver.findElements(By.xpath(objMap.getLocator("strVerify_Userid"))).size() > 0){
				// verifying updated username for customer
				String Verification = uiDriver.getwebDriverLocator(
						objMap.getLocator("strVerify_Userid")).getText();
				System.out.println(Verification);
				if (Verification.equalsIgnoreCase(EmailCRM)) {
					RESULT.passed("Update customer Login detail",
							"Should be able to update customer Login details successfully",
					"Customer Login details updation successful..!!");
					output.put("userID", EmailCRM);
					output.put("password",  input.get("Password"));
				} else {
					RESULT.failed("Update customer Login detail",
							"Should be able to update customer Login details successfully",
					"Customer Login details updation unsuccessful..!!");
					return;
				}
			}else{
				RESULT.failed("Update customer Login detail",
						"Should be able to update customer Login details successfully",
				"Error Due to invalid data");
				return;
			}
		}catch(Exception e)
		{
			RESULT.failed("Update Login Info", "Login Detail should be updated",
					"Login detail is not updated and the exception caught is " + e.getMessage());
		}
	}

	/*
	 * Component function for the add new payment option for a particular
	 * customer
	 */
	public void AddPaymentInfo(DataRow input, DataRow output)
	throws InterruptedException {
		try {
			// call to the CRM_PaymentOptions reusable function
			uiDriver.FD_payment_CRM(input.get("Chooseoptn"), input
					.get("cardDetail"), input.get("NameOnCard"), input
					.get("routing"), input.get("Bank"), input
					.get("CardOrAcctype"), input.get("CardNum"), input
					.get("ExpiryMnth"), input.get("ExpiryYr"), input
					.get("CardStreetAdd"), input.get("CardAppNum"), input
					.get("CardAddLine2"), input.get("CardCountry"), input
					.get("CardCity"), input.get("CardState"), input
					.get("CardZip"));
			SleepUtils.getInstance().sleep(TimeSlab.MEDIUM);
			//uiDriver.robot.scrollToElement(uiDriver.getwebDriverLocator(objMap.getLocator("strpaymentSection")),webDriver);
			// search the list of credit cards
			List<WebElement> CardList = webDriver.findElements(By.className(objMap.getLocator("lstpaymentlist")));
			int eSize = CardList.size();
			System.out.println(eSize);
			boolean check = false;
			for (int i = 0; i < eSize; i++) {
				String card_check = CardList.get(i).getText();

				// System.out.println("Details:" + i);
				// System.out.println("Details:" + card_check);
				if (input.get("cardDetail").contains("ebt")) {
					if (card_check.toLowerCase().contains(
							input.get("NameOnCard").toLowerCase())
							&& card_check.toLowerCase().contains(
									input.get("CardStreetAdd").toLowerCase())
									&& card_check.toLowerCase().contains(
											input.get("CardAddLine2").toLowerCase())
											&& card_check.toLowerCase().contains(
													input.get("CardAppNum").toLowerCase())
													// && card_check.toLowerCase().contains(
													// input.get("CardCity").toLowerCase())
													// && card_check.toLowerCase().contains(
													// input.get("CardState").toLowerCase())
													&& card_check.toLowerCase().contains(
															input.get("CardZip").toLowerCase())
															&& card_check.toLowerCase().contains(
																	input.get("CardCountry").toLowerCase())) {
						check = true;
						System.out.println("added Payment Option:" + card_check);
						uiDriver.robot.scrollToElement(CardList.get(i),webDriver);
						RESULT.passed(
								"Add Payment Options",
								"Should be able to search added details of Payment Option",
						"Search customer details successfully");
						break;
					}
				} else if (input.get("cardDetail").contains("credit")) {
					if (card_check.toLowerCase().contains(
							input.get("NameOnCard").toLowerCase())
							// && card_check.toLowerCase().contains(
							// input.get("CardNum").toLowerCase())
							&& card_check.toLowerCase().contains(
									input.get("CardOrAcctype").toLowerCase())
									// && card_check.toLowerCase().contains(
									// input.get("ExpiryMnth").toLowerCase())
									&& card_check.toLowerCase().contains(
											input.get("ExpiryYr").toLowerCase())
											&& card_check.toLowerCase().contains(
													input.get("CardStreetAdd").toLowerCase())
													&& card_check.toLowerCase().contains(
															input.get("CardAddLine2").toLowerCase())
															&& card_check.toLowerCase().contains(
																	input.get("CardAppNum").toLowerCase())
																	// && card_check.toLowerCase().contains(
																	// input.get("CardCity").toLowerCase())
																	// && card_check.toLowerCase().contains(
																	// input.get("CardState").toLowerCase())
																	&& card_check.toLowerCase().contains(
																			input.get("CardZip").toLowerCase())) {
						check = true;
						System.out.println("added Payment Option:" + card_check);
						uiDriver.robot.scrollToElement(CardList.get(i),webDriver);
						RESULT.passed(
								"Add Payment Options",
								"Should be able to add detail of Payment Options",
						"Payment details added successfully");
						break;
					}
				} else if (input.get("cardDetail").contains("checking")) {
					if (card_check.toLowerCase().contains(
							input.get("NameOnCard").toLowerCase())
							&& card_check.toLowerCase().contains(
									input.get("CardOrAcctype").toLowerCase())
									&& card_check.toLowerCase().contains(
											input.get("CardStreetAdd").toLowerCase())
											&& card_check.toLowerCase().contains(
													input.get("CardAddLine2").toLowerCase())
													&& card_check.toLowerCase().contains(
															input.get("CardAppNum").toLowerCase())
															&& card_check.toLowerCase().contains(
																	input.get("CardCity").toLowerCase())
																	&& card_check.toLowerCase().contains(
																			input.get("CardState").toLowerCase())
																			&& card_check.toLowerCase().contains(
																					input.get("CardZip").toLowerCase())
																					&& card_check.toLowerCase().contains(
																							input.get("routing").toLowerCase())) {
						check = true;
						System.out.println("added Payment Option:" + card_check);
						uiDriver.robot.scrollToElement(CardList.get(i),webDriver);
						RESULT.passed(
								"Add Payment Options",
								"Should be able to search added detail of Payment Options",
						"Payment details added succcessfully..!!");
						break;
					}
				}

			}

			if (check == false) {
				RESULT.failed(
						"Add Payment Options",
						"Should be able to search added detail of payment Option",
						"Payment details not found");
				return;

			}
		} catch (Exception e) {
			RESULT.failed("Add Payment Info Exception", "Payment detail should be added",
					"Payment details addition unsuccessful and the exception caught is " + e.getMessage());
		}
	}

	public void EditPaymentInfo(DataRow input, DataRow output)
	throws InterruptedException {
		try {

			uiDriver.FD_payment_CRM(input.get("Chooseoptn"), input
					.get("cardDetail"), input.get("NameOnCard"), input
					.get("routing"), input.get("Bank"), input
					.get("CardOrAcctype"), input.get("CardNum"), input
					.get("ExpiryMnth"), input.get("ExpiryYr"), input
					.get("CardStreetAdd"), input.get("CardAppNum"), input
					.get("CardAddLine2"), input.get("CardCountry"), input
					.get("CardCity"), input.get("CardState"), input
					.get("CardZip"));
			//uiDriver.robot.scrollToElement(uiDriver.getwebDriverLocator(objMap.getLocator("strpaymentSection")),webDriver);
			// search the list of credit cards
			List<WebElement> CardList = webDriver.findElements(By.className(objMap.getLocator("lstpaymentlist")));
			int eSize = CardList.size();
			System.out.println(eSize);
			boolean check = false;
			for (int i = 0; i < eSize; i++) {
				String card_check = CardList.get(i).getText();

				// System.out.println("Details:" + i);
				// System.out.println("Details:" + card_check);
				if (input.get("cardType").contains("ebt")) {
					if (card_check.toLowerCase().contains(
							input.get("NameOnCard").toLowerCase())
							// && card_check.toLowerCase().contains(
							// input.get("CardNum").toLowerCase())
							&& card_check.toLowerCase().contains(
									input.get("CardStreetAdd").toLowerCase())
									&& card_check.toLowerCase().contains(
											input.get("CardAddLine2").toLowerCase())
											&& card_check.toLowerCase().contains(
													input.get("CardAppNum").toLowerCase())
													// && card_check.toLowerCase().contains(
													// input.get("CardCity").toLowerCase())
													// && card_check.toLowerCase().contains(
													// input.get("CardState").toLowerCase())
													&& card_check.toLowerCase().contains(
															input.get("CardZip").toLowerCase())
															&& card_check.toLowerCase().contains(
																	input.get("CardCountry").toLowerCase())) {
						check = true;
						System.out.println("edited Payment Option:" + card_check);
						uiDriver.robot.scrollToElement(CardList.get(i),webDriver);
						RESULT.passed(
								"Edit Payment Options",
								"Should be able to search edited detail of Payment Options",
						"Edited details of Payment Option found..!!");
						break;
					}
				} else if (input.get("cardType").contains("credit")) {
					if (card_check.toLowerCase().contains(
							input.get("NameOnCard").toLowerCase())
							// && card_check.toLowerCase().contains(
							// input.get("CardNum").toLowerCase())
							&& card_check.toLowerCase().contains(
									input.get("CardOrAcctype").toLowerCase())
									// && card_check.toLowerCase().contains(
									// input.get("ExpiryMnth").toLowerCase())
									&& card_check.toLowerCase().contains(
											input.get("ExpiryYr").toLowerCase())
											&& card_check.toLowerCase().contains(
													input.get("CardStreetAdd").toLowerCase())
													&& card_check.toLowerCase().contains(
															input.get("CardAddLine2").toLowerCase())
															&& card_check.toLowerCase().contains(
																	input.get("CardAppNum").toLowerCase())
																	// && card_check.toLowerCase().contains(
																	// input.get("CardCity").toLowerCase())
																	// && card_check.toLowerCase().contains(
																	// input.get("CardState").toLowerCase())
																	&& card_check.toLowerCase().contains(
																			input.get("CardZip").toLowerCase())) {
						check = true;
						System.out.println("edited payment option:" + card_check);
						uiDriver.robot.scrollToElement(CardList.get(i),webDriver);
						RESULT.passed(
								"Edit Payment Options",
								"Should be able to search edited detail of Payment Options",
						"Edited details of Payment Option found..!!");
						break;
					}
				} else if (input.get("cardType").contains("checking")) {
					if (card_check.toLowerCase().contains(
							input.get("NameOnCard").toLowerCase())
							&& card_check.toLowerCase().contains(
									input.get("CardNum").toLowerCase())
									&& card_check.toLowerCase().contains(
											input.get("CardOrAcctype").toLowerCase())

											&& card_check.toLowerCase().contains(
													input.get("CardStreetAdd").toLowerCase())
													&& card_check.toLowerCase().contains(
															input.get("CardAddLine2").toLowerCase())
															&& card_check.toLowerCase().contains(
																	input.get("CardAppNum").toLowerCase())
																	&& card_check.toLowerCase().contains(
																			input.get("CardCity").toLowerCase())
																			&& card_check.toLowerCase().contains(
																					input.get("CardState").toLowerCase())
																					&& card_check.toLowerCase().contains(
																							input.get("CardZip").toLowerCase())) {
						check = true;
						System.out.println("edited payment option:" + card_check);
						uiDriver.robot.scrollToElement(CardList.get(i),webDriver);
						RESULT.passed(
								"Edit Payment Options",
								"Should be able to search edited detail of Payment Options",
						"Edited details of Payment Option found..!!");
						break;
					}
				}

			}

			if (check == false) {
				RESULT.failed(
						"Edit Payment Options",
						"Should be able to search edited detail of Payment Options",
				"Edited details of Payment Option NOT found..!!");
				return;

			}
		} catch (Exception e) {
			RESULT.failed("Edit Payment Options", "Payment Detail should be edited",
					"Payment Detail is not edited and the exception caught is " + e.getMessage());
		}

	}

	public void CRMPasswordValidation(DataRow input, DataRow output){
		try {
			uiDriver.FD_passwordValidationCRM(input.get("password"), input.get("Type"));
			
		} catch (Exception e) {
			RESULT.failed("CRMPasswordValidation Exception", "Password should be validated",
					"Error occured and the exception caught is " + e.getMessage());
		}
	}

}