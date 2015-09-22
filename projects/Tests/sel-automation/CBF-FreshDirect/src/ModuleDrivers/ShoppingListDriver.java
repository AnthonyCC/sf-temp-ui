package ModuleDrivers;

import java.util.List;
import java.util.concurrent.TimeoutException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.sikuli.script.FindFailed;

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
	
	 public void AddItemtoList(DataRow input, DataRow output) throws InterruptedException,FindFailed
     {
   	try{
		uiDriver.FD_addList(webDriver.findElement(By.partialLinkText(input.get("Item"))),input.get("ListFlagNewExist"), input.get("Listname"),input.get("FlexibilityFlag"));
   	}catch (Exception e) {
		e.printStackTrace();
		RESULT.failed("Add to List", "Item should get added to given list", "Item could not get added to given list");
		return;
	}
   	
     } 
     
     public void AddAllitemstoList(DataRow input, DataRow output) throws InterruptedException,FindFailed
     {
   	  if(uiDriver.getwebDriverLocator(objMap.getLocator("btnreorderAddAllToList")).isDisplayed())
   	  {
   		 uiDriver.FD_addList(uiDriver.getwebDriverLocator(objMap.getLocator("btnreorderAddAllToList")),input.get("ListFlagNewExist"), input.get("Listname"),input.get("FlexibilityFlag"));
   		 return;
   	  }
   	  
     }
     
}