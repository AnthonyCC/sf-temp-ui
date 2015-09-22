package ModuleDrivers;

import java.util.NoSuchElementException;

import org.openqa.selenium.By;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.sikuli.script.FindFailed;

import ui.WebUIDriver;

import cbf.engine.BaseModuleDriver;
import cbf.engine.TestResultLogger;
import cbf.engine.TestResult.ResultType;
import cbf.utils.DataRow;
import cbf.utils.SleepUtils;
import cbf.utils.SleepUtils.TimeSlab;

public class ModifyOrderStorefrontDriver extends BaseModuleDriver {
	WebDriverWait wait;
	// This will wait for the page to load for 300 seconds (i.e. 300000 Millie seconds) 
	public static String PageLoadTime="300000"; 
	


	public ModifyOrderStorefrontDriver(TestResultLogger resultLogger) {
		super(resultLogger);
		// Initialize the wait object
		wait = new WebDriverWait(webDriver, 20);
	}

	/*
	 * Component function to modify the already created order from the Your
	 * Account> Your Order
	 */
	public void ModifyCancelOrder(DataRow input, DataRow output)
	throws InterruptedException, FindFailed {

		String Modify_Cancel=input.get("ModifyCancel").toUpperCase();
		if(Modify_Cancel.equals("CANCEL")){
			// call to cancel order reusable function
			uiDriver.FD_cancelOrder(input.get("OrderNo"));
		}else{
			//call to modify order reusable function
			uiDriver.FD_modifyOrder(input.get("OrderNo"), input.get("ItemName"),
					input.get("Quantity"), input.get("Operation"),
					input.get("EmptyCartAcceptDecline"), input.get("FlexibilityFlag"));
		}
	}


	public void SubmitOrderModify(DataRow input, DataRow output)
			throws InterruptedException, FindFailed {
		// try {
		// wait for the modify order element to be visible condition
		wait.until(ExpectedConditions.visibilityOfElementLocated(By
				.linkText(objMap.getLocator("btnmodifyOrd"))));
		// click on modify order
		uiDriver.getwebDriverLocator(objMap.getLocator("btnmodifyOrd")).click();
		WebUIDriver.selenium.waitForPageToLoad(PageLoadTime);
		if (webDriver.getTitle().endsWith("View Cart")) {
			RESULT.passed("click on modifyorder button",
					"User should redirected to view cart page",
					"User successfully redirected to view cart page");
		} else
			RESULT.failed("click on modifyorder button",
					"User should redirected to view cart page",
					"User could not redirect to view cart page");
	}

	public void ModifyCart(DataRow input, DataRow output)
			throws InterruptedException, FindFailed {

		// call to the modify cart reusable function
		WebUIDriver.selenium.waitForPageToLoad(PageLoadTime);
		if(webDriver.findElements(By.className("cartheader__title")).size()==0){
			uiDriver.getwebDriverLocator(objMap.getLocator("btnyourCart")).click();
			WebUIDriver.selenium.waitForPageToLoad(PageLoadTime);	
		}
		uiDriver.FD_modifyCart(input.get("ItemName"), input.get("Operation"),input.get("Quantity"),input.get("DeleteAllAcceptDecline"),input.get("FlexibilityFlag"));
	}

	
	public void ClickViewCart(DataRow input, DataRow output) throws FindFailed,
			InterruptedException {
		try {

			uiDriver.click("lnkviewCartCorosal");
			if (webDriver.getTitle().endsWith("View Cart")) {
				RESULT.passed("click on view cart link from carousal",
						"User should redirected to view cart page",
						"User successfully redirected to view cart page");
			} else
				RESULT.failed("click on view cart link from carousal",
						"User should redirected to view cart page",
						"User could not redirect to view cart page");
		} catch (NoSuchElementException e) {
			e.printStackTrace();
			RESULT.failed("click on view cart link from carousal",
					"User should redirected to view cart page",
					"Link for view cart not found");
		}
	}

}
