package ModuleDrivers;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.sikuli.script.FindFailed;

import ui.WebUIDriver;

import cbf.engine.BaseModuleDriver;
import cbf.engine.TestResultLogger;
import cbf.utils.DataRow;
import cbf.utils.SleepUtils;
import cbf.utils.SleepUtils.TimeSlab;

public class CartDriver extends BaseModuleDriver {

	Actions ac_key = new Actions(webDriver);
	WebDriverWait wait = new WebDriverWait(webDriver, 20);

	public CartDriver(TestResultLogger resultLogger) {
		super(resultLogger);
		// TODO Auto-generated constructor stub
	}

	public void AddItemtoCart(DataRow input, DataRow output) throws FindFailed,
			InterruptedException {

		try {
			uiDriver.FD_addToCart(input.get("Method"), input
					.get("Item"), input.get("Quantity"), input
					.get("OrderFlagNewModify"), 
					input.get("GridOrList"), input.get("FlexibilityFlag"));
		} catch (Exception e) {
			RESULT.failed("Add To Cart Component",
					"Add To Cart Component should work successfully",
					"Add To Cart Component Exception: " + e.getMessage());
			return;
		}

	}

	public void AddAllitemstoCart(DataRow input, DataRow output)
	throws InterruptedException, FindFailed {
		// quantity variables for the result
		int initialqty;
		int added_qty = 0;
		int finalqty;
		try {
		
			//get the initial quantity of items in the cart
			initialqty = Integer.parseInt(webDriver.findElement(
					By.xpath(objMap.getLocator("stryourCart_initialqty")))
					.getText().split(" ")[0]);

			//get the list check available items and later utilize it for checking the added total quantity			
			List<WebElement> item_quantities = webDriver.findElements(By.xpath(objMap.getLocator("straddAllListQuantity")));
			//if list has only one item 
			if(item_quantities.size() == 1){
				if(!(webDriver.findElement(By
							.xpath(objMap.getLocator("btnreorderAddAllToCart"))).isDisplayed())){
					RESULT.passed("Add All Item On Page",
							"Add All Item On Page button should not be available as only one item on page", "Add All Item On Page button is not available as only one item on page");						
				}else{
					RESULT.failed("Add All Item On Page",
							"Add All Item On Page button should not be available as only one item on page", "Add All Item On Page button is available. Although only one item on page");				
				}
			}else{
				//click on the Add All items on Page 
				try{
					wait.until(ExpectedConditions.visibilityOfElementLocated(By
							.xpath(objMap.getLocator("btnreorderAddAllToCart"))));
					uiDriver.getwebDriverLocator(
							objMap.getLocator("btnreorderAddAllToCart")).click();				
				}catch(TimeoutException e){
					RESULT.failed("Add all items to cart",
							"Add All Items on Page button should be displayed",
					"Add All Items on Page button is not displayed");
				}

				//click on the "Add Items button"
				try{
					wait.until(ExpectedConditions.visibilityOfElementLocated(By
							.xpath(objMap.getLocator("popconfirm"))));
					uiDriver.getwebDriverLocator(
							objMap.getLocator("btnreorderAddAllToCartOk")).click();
					SleepUtils.getInstance().sleep(TimeSlab.YIELD);
				}catch(TimeoutException e){
					RESULT.failed("Add all items to cart",
							"Add All Items on Page confirm box should be displayed",
					"Add All Items on Page confirm box is not displayed");
				}
				catch(Exception e)
				{
					RESULT.failed("Add all items on page : Confirm ok click", "Confirm OK button should be clicked",
							"Confirm ok button is not clicked");
				}
				// handle the alcohol alert
				if (webDriver.findElements(
						By.className(objMap.getLocator("alertAlcohol"))).size() > 0) {
					uiDriver.getwebDriverLocator(
							objMap.getLocator("btnalcoholAlertAccept")).click();
				}

				// This popup appears when the user is logging in and adding any product to cart
				uiDriver.orderCreationPopup(input.get("OrderFlagNewModify"));

				//Find the total of the quantity added
				for(WebElement item : item_quantities){
					added_qty += Integer.parseInt(item.getText());
				}

				//get the final quntity in your cart
				uiDriver.waitForPageLoad();
				SleepUtils.getInstance().sleep(TimeSlab.YIELD);
				finalqty = Integer.parseInt(webDriver.findElement(
						By.xpath(objMap.getLocator("stryourCart_initialqty")))
						.getText().split(" ")[0]);

				// Verify whether the products are added to the cart or not
				if (finalqty == initialqty + added_qty) {
					RESULT.passed("Add All Item to Cart",
							"All Items should be added and total items in your cart should be " + finalqty, "All Items are added and total items in your cart is " + initialqty + added_qty);				
				}else{
					RESULT.failed("Add All Item to Cart",
							"All Items should be added and total items in your cart should be " + finalqty, "All Items are not added and total items in your cart is " + (initialqty + added_qty));
				}
			}
		} catch (Exception e) {
			RESULT.failed("Add all items to cart",
					"All items should be added to the cart",
					"Add all items to cart failed due to " + e.getMessage());
		}
	}

	public void AddYMALOrCarousal(DataRow input, DataRow output)
			throws FindFailed, InterruptedException {

		String Item = webDriver.findElement(
				By.xpath("//div[@class='transactional']/ul/li[1]/div[2]/a"))
				.getText();
		uiDriver.FD_addYMALOrCarousal(Item, input.get("ItemType"), input
				.get("OrderFlagNewModify"), input.get("Quantity"), input
				.get("ClickOrHover"), input.get("FlexibilityFlag"), input.get("GridOrList"));

	}

}