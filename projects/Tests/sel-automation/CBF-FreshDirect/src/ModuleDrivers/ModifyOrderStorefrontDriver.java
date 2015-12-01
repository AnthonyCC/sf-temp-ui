package ModuleDrivers;

import java.util.NoSuchElementException;

import org.openqa.selenium.By;
import org.openqa.selenium.interactions.Actions;
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

public class ModifyOrderStorefrontDriver extends BaseModuleDriver {

	// This will wait for the page to load for 300 seconds (i.e. 300000 Millie seconds) 
	public static String PageLoadTime="300000"; 
	


	public ModifyOrderStorefrontDriver(TestResultLogger resultLogger) {
		super(resultLogger);
		// Initialize the wait object
	
	}

	/*
	 * Component function to modify the already created order from the Your
	 * Account> Your Order
	 */
	public void ModifyCancelOrder(DataRow input, DataRow output)
	throws InterruptedException {
		try{
			String Modify_Cancel=input.get("ModifyCancel").toUpperCase();
		if(Modify_Cancel.equals("CANCEL")){
			// call to cancel order reusable function
			uiDriver.FD_cancelOrder(input.get("OrderNo"));
		}else{
			//call to modify order reusable function
//			uiDriver.FD_modifyOrder(input.get("OrderNo"), input.get("ItemName"),
//					input.get("Quantity"), input.get("Operation"),
//					input.get("EmptyCartAcceptDecline"), input.get("FlexibilityFlag"));
			System.out.println("ordernum in modify: "+WebUIDriver.orderNum);
			if(WebUIDriver.orderNum==null ){
				RESULT.warning("Modify recently placed order", "Recently placed order should be available", 
						"No recently placed order available so modifying order given by user");
				uiDriver.FD_modifyOrder(input.get("OrderNo"), input.get("ItemName"),
						input.get("Quantity"), input.get("Operation"),
						input.get("EmptyCartAcceptDecline"), input.get("FlexibilityFlag"));
			}else{
			uiDriver.FD_modifyOrder(WebUIDriver.orderNum, input.get("ItemName"),
					input.get("Quantity"), input.get("Operation"),
					input.get("EmptyCartAcceptDecline"), input.get("FlexibilityFlag"));
			}
		}
		}catch (Exception e) {
				RESULT.failed("Modify Cancel Order Exception", "Order should be successfully modified or cancelled",
						"Order is not modified/cancelled and exception caught is "+e.getMessage());
		}
	}


	public void SubmitOrderModify(DataRow input, DataRow output)
			throws InterruptedException {
		// try {
		// wait for the modify order element to be visible condition
		try
		{
			uiDriver.wait.until(ExpectedConditions.visibilityOfElementLocated(By
					.linkText(objMap.getLocator("btnmodifyOrd"))));
			uiDriver.getwebDriverLocator(objMap.getLocator("btnmodifyOrd")).click();
		}
		catch(Exception e)
		{
			RESULT.failed("Submit Order : Modify", "Modify order button should be visible and clicked successfully",
					"Modify order button is not visible or clicked");
		}
		// click on modify order
		
		uiDriver.waitForPageLoad();
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
			throws InterruptedException {

		// call to the modify cart reusable function
		uiDriver.waitForPageLoad();
		try
		{
			if(webDriver.findElements(By.className("cartheader__title")).size()==0){
				uiDriver.getwebDriverLocator(objMap.getLocator("btnyourCart")).click();
				uiDriver.waitForPageLoad();	
			}
		}
		catch(Exception e)
		{
			RESULT.failed("Modify Cart Exception : Component ", "Your cart should be clicked",
					"Your cart is not clicked and exception caught is "+e.getMessage());
		}
		
		uiDriver.FD_modifyCart(input.get("ItemName"), input.get("Operation"),input.get("Quantity"),input.get("DeleteAllAcceptDecline"),input.get("FlexibilityFlag"));
	}

	
	public void ClickViewCart(DataRow input, DataRow output) throws 
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
			
			RESULT.failed("Click on view cart link from carousal : NoSuchElementException exception",
					"User should redirected to view cart page",
					"Link for view cart not found and exception caught is "+e.getMessage());
		}
		catch(Exception e)
		{
			RESULT.failed("click on view cart link from carousal : Exception",
					"User should redirected to view cart page",
					"Link for view cart not found and exception caught is "+e.getMessage());
		}
	}

}
