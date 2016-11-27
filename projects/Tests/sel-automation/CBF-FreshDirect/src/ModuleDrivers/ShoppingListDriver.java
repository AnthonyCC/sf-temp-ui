package ModuleDrivers;

import java.util.List;
import java.util.concurrent.TimeoutException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import cbf.engine.BaseModuleDriver;
import cbf.engine.TestResultLogger;
import cbf.engine.TestResult.ResultType;
import cbf.utils.DataRow;
import cbf.utils.SleepUtils;
import cbf.utils.SleepUtils.TimeSlab;

public class ShoppingListDriver extends BaseModuleDriver{

	Actions ac_key=new Actions(webDriver);
	WebDriverWait wait = new WebDriverWait(webDriver, 20);
	
	public ShoppingListDriver(TestResultLogger resultLogger) {
		super(resultLogger);
		// TODO Auto-generated constructor stub
	}
	
	public void AddItemtoList(DataRow input, DataRow output) throws InterruptedException 
	{
		try{
			uiDriver.FD_addList(webDriver.findElement(By.partialLinkText(input.get("Item"))),input.get("ListFlagNewExist"), input.get("Listname"),input.get("FlexibilityFlag"));
		}catch (Exception e) {
			RESULT.failed("Add item to List component", "Item should get added to given list", "Item could not get added to given list");
			return;
		}
	} 
     
	public void AddAllitemstoList(DataRow input, DataRow output) throws InterruptedException 
	{
		try{
			if(webDriver.findElements(By.xpath(objMap.getLocator("btnreorderAddAllToList"))).size()>0 &&
					uiDriver.getwebDriverLocator(objMap.getLocator("btnreorderAddAllToList")).isDisplayed())
			{
				uiDriver.FD_addList(uiDriver.getwebDriverLocator(objMap.getLocator("btnreorderAddAllToList")),input.get("ListFlagNewExist"), input.get("Listname"),input.get("FlexibilityFlag"));
				return;
			}else{
				RESULT.failed("Add All item to List", "Add All item to List button should get displayed"
						, "Add All item to List button is not displayed");
				
			}
		}catch (Exception e) {
			RESULT.failed("Add All item to List component", "Items should get added to given list", "Items could not get added to given list");
			return;
		}
	}
     
}