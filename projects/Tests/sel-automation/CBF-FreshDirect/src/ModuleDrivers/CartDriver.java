
package ModuleDrivers;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.sikuli.script.FindFailed;

import cbf.engine.BaseModuleDriver;
import cbf.engine.TestResultLogger;
import cbf.harness.Harness;
import cbf.utils.Configuration;
import cbf.utils.DataRow;

public class CartDriver extends BaseModuleDriver {

	Actions ac_key = new Actions(webDriver);
	WebDriverWait wait = new WebDriverWait(webDriver, 20);

	public CartDriver(TestResultLogger resultLogger) {
		super(resultLogger);
		// TODO Auto-generated constructor stub
	}

	public void AddItemtoCart(DataRow input, DataRow output) throws FindFailed,
			InterruptedException {
		
		uiDriver.FD_addToCart(input.get("Item"), input
				.get("OrderFlagNewModify"), input.get("Quantity"), input
				.get("Method"), input.get("FlexibilityFlag"), input
				.get("SalesUnit"), input.get("Packaging"));		
		
	}

	public void AddAllitemstoCart(DataRow input, DataRow output)throws InterruptedException, FindFailed 
	{
	double initialqty;
	double added_qty;
	double finalqty;
	int flag = 0;
	initialqty = Double.parseDouble(uiDriver.getwebDriverLocator(objMap.getLocator("stryourCart_initialqty")).getText());
	Actions actions = new Actions(webDriver);
	WebDriverWait wait = new WebDriverWait(webDriver, 10);
	
		if (uiDriver.getwebDriverLocator(objMap.getLocator("btnreorderAddAllToCart")).isDisplayed()) 
		{
			uiDriver.getwebDriverLocator(objMap.getLocator("btnreorderAddAllToCart")).click();
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(objMap.getLocator("popconfirm"))));
			uiDriver.getwebDriverLocator(objMap.getLocator("btnreorderAddAllToCartOk")).click();
		}
		if (webDriver.findElements(By.className(objMap.getLocator("alertAlcohol"))).size() > 0) 
		{
			uiDriver.getwebDriverLocator(objMap.getLocator("btnalcoholAlertAccept")).click();
		}
		
		 //This popup appears when the user is logging in and adding any product to cart
		if (webDriver.findElements(By.xpath(objMap.getLocator("popreorder_atc_1_1"))).size() > 0)
		{
			//add new order
			if (input.get("OrderFlagNewModify").equalsIgnoreCase("New")) 
			{
				uiDriver.getwebDriverLocator(objMap.getLocator("popreorder_atc_1_1")).click();
			}
			 /*
            * If user wants to modify an existing order
            * Pre-condtion : Default 0 index provided to select dropdownlist
            */
			else if (input.get("OrderFlagNewModify").equalsIgnoreCase("Modify")) {
				uiDriver.getwebDriverLocator(objMap.getLocator("popreorder_atc_1_2")).click();
				Select select = new Select(uiDriver.getwebDriverLocator(objMap.getLocator("popreorder_atc_2_1")));
				select.selectByIndex(0);
				uiDriver.getwebDriverLocator(objMap.getLocator("popreorder_atc_2_2")).click();
			}
			else 
			{
				uiDriver.click("btnClose");
			}
		}
		added_qty = Double.parseDouble(input.get("Quantity"));
		// verify "checkout" and "view cart" button is present or not in mouse hover on "your cart" button
		actions.moveToElement(uiDriver.getwebDriverLocator(objMap.getLocator("btnYourCart"))).build().perform();
		WebElement btnview = uiDriver.getwebDriverLocator(objMap.getLocator("btnviewcart"));
		WebElement btnchkout = uiDriver.getwebDriverLocator(objMap.getLocator("btncheckout"));
		finalqty = Double.parseDouble(uiDriver.getwebDriverLocator(objMap.getLocator("stryourCart_initialqty")).getText());
            if (btnview.isEnabled() && btnchkout.isEnabled()) 
			{
                  RESULT.passed("Checkout and view cart button",
				  "View cart and checkout button should exist and should be enabled","View cart and checkout button exists and are enabled");
            }
            // Verify whether the product is added to the cart or not
            if (finalqty == initialqty + added_qty)
            {
                RESULT.passed("Item added successfully", "Item should be added ","Item added ");
                  flag = 1;
            }
            if (flag == 0) 
			{
                  RESULT.failed("Item not added", "Item should be added ","Item adding unsuccessful ");
            }
		

	}
	
	public void AddYMALOrCarousal(DataRow input, DataRow output) throws FindFailed,
	InterruptedException {

		String Item=webDriver.findElement(By.xpath("//div[@class='transactional']/ul/li[1]/div[2]/a")).getText();
		uiDriver.FD_addYMALOrCarousal(Item, input.get("ItemType"), input.get("OrderFlagNewModify"), 
									input.get("Quantity"), input.get("ClickOrHover"), input.get("FlexibilityFlag"), 
									input.get("SalesUnit"), input.get("Packaging"));		

	}
	
}