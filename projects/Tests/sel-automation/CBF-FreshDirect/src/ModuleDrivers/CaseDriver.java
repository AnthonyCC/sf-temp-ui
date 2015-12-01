package ModuleDrivers;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
//import org.sikuli.script.FindFailed;

import cbf.engine.BaseModuleDriver;
import cbf.engine.TestResultLogger;
import cbf.utils.DataRow;

public class CaseDriver extends BaseModuleDriver {
	// This will wait for the page to load for 300 seconds (i.e. 300000 Millie
	// seconds)

	public CaseDriver(TestResultLogger resultLogger) {
		super(resultLogger);
		// TODO Auto-generated constructor stub
	}

	public void SearchCustomer(DataRow input, DataRow output)
			throws  InterruptedException {
		try
		{
			if (webDriver.getTitle().contains("/ FreshDirect CRM : Home /")) {
				RESULT.passed("SearchCustomer",
						"Should be navigated to SearchCustomer page",
						"Navigated to SearchCustomer page successfully");
			} else {
				uiDriver.click("lnkhome");
				uiDriver.wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText(objMap.getLocator("lnknewCustomer"))));
				if (webDriver.getTitle().contains("/ FreshDirect CRM : Home /")) {
					RESULT.passed("SearchCustomer",
							"Should be navigated to SearchCustomer page",
							"Navigated to SearchCustomer page successfully");
				}else{
				RESULT.failed("SearchCustomer",
						"Should be navigated to SearchCustomer page",
						"Failed to Navigate to SearchCustomer page ");
				return;
				}
			}
			uiDriver.setValue("txtFirstname_customerserch", input.get("FirstName"));
			uiDriver.setValue("txtLastname_customerserch", input.get("LastName"));
			uiDriver.setValue("txtemail_CRM", input.get("EmailCRM"));
			uiDriver.setValue("txtPhone_customerserch", input.get("Phone"));
			uiDriver.setValue("txtCustomerID_customerserch", input.get("CustomerID"));
			uiDriver.click("btnsearch_Customer");
			uiDriver.waitForPageLoad();
//			SleepUtils.getInstance().sleep(TimeSlab.HIGH);
		//	SleepUtils.getInstance().sleep(TimeSlab.HIGH);
			uiDriver.wait.until(ExpectedConditions.invisibilityOfElementLocated(By.name(objMap.getLocator("txtemail_CRM"))));
			uiDriver.wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText(objMap.getLocator("lnksummary"))));
			if (webDriver.getTitle().contains("/ FreshDirect CRM : Account Details /")) {
				RESULT.passed("CustomerSearch", "Should open Account Details Page",
						"Customer searched successfully!!");
			} else {
				RESULT.failed("CustomerSearch", "Should open Account Details Page",
						"Unable to search the customer");
				return;
			}
		}
		catch(Exception e)
		{
			RESULT.failed("Search Customer Exception","Customer should be searched successfully",
					"Customer search is not successful");
			return;
		}
		

	}

	public void SearchOrder(DataRow input, DataRow output) throws 
			InterruptedException {
		try
		{
			if (webDriver.getTitle().contains("/ FreshDirect CRM : Home /")) {
				RESULT.passed("Search Order",
						"Should be navigated to Search Order page",
						"Navigated to Search Order page successfully");
			}else {
				uiDriver.click("lnkhome");
				uiDriver.wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText(objMap.getLocator("lnknewCustomer"))));
				if (webDriver.getTitle().contains("/ FreshDirect CRM : Home /")) {
					RESULT.passed("Search Order",
							"Should be navigated to Search Order page",
							"Navigated to Search Order page successfully");
				}else{
				RESULT.failed("Search Order",
						"Should be navigated to Search Order page",
						"Failed to Navigate to Search Order page ");
				return;
				}
			}
			uiDriver.setValue("txtFirstname_orderserch", input.get("First_Name"));
			uiDriver.setValue("txtLastname_orderserch", input.get("Last_Name"));
			uiDriver.setValue("txtOrderNumber_orderserch", input.get("OrderNumber"));
			uiDriver.setValue("txtPhone_orderserch", input.get("Phone"));
			uiDriver.setValue("txtGiftCard_orderserch", input.get("GiftCard"));
			uiDriver.click("btnsearch_Order");
			uiDriver.waitForPageLoad();
			String OrderNumber = uiDriver.getwebDriverLocator(
					objMap.getLocator("strorderDetails")).getText();
			if (OrderNumber.equals(input.get("OrderNumber"))) {
				RESULT.passed("OrderSearch", "Order " + OrderNumber
						+ "Details page should get open", "Order " + OrderNumber
						+ "Details page opened successfully!");
			} else {
				RESULT.failed("OrderSearch", "Order " + OrderNumber
						+ "Details page should get open",
						"Unable to search the Order" + OrderNumber);
			}
			
		}
		catch(Exception e)
		{
			RESULT.failed("Search Order in CRM","Order should be searched successfully","Order is not searched and the exception caught is "+e.getMessage());
		}
		
	}

	public void CreateCase(DataRow input, DataRow output)
			throws InterruptedException {
	
		try
		{
			if (uiDriver.isDisplayed("lnkcase")) {
				uiDriver.click("lnkcase");
				uiDriver.waitForPageLoad();
			} else {
				
				RESULT.failed("Create Case ", "Case should be visible", "Case tab is not visible");
				return;
			} 
		}
		catch(Exception e)
		{
			RESULT.failed("Create Case ", "Case should be visible", "Case tab is not visible");
			return;
		}
		

		// call to the create case reusable function
		uiDriver.FD_case_CRM(input.get("Assigned"), input.get("Queue"), input
				.get("Priority"), input.get("Subject"), input.get("Reported"),
				input.get("Actual"), input.get("Summary"), input.get("Notes"));

	}
}
