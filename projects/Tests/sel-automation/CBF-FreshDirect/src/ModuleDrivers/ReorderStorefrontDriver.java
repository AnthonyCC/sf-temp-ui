package ModuleDrivers;

import java.util.List;



import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.sikuli.script.FindFailed;
import org.openqa.selenium.TimeoutException;

import cbf.engine.BaseModuleDriver;
import cbf.engine.TestResultLogger;
import cbf.engine.TestResult.ResultType;
import cbf.utils.DataRow;
import cbf.utils.SleepUtils;
import cbf.utils.SleepUtils.TimeSlab;

public class ReorderStorefrontDriver extends BaseModuleDriver {

	public ReorderStorefrontDriver(TestResultLogger resultLogger) {
		super(resultLogger);
		// TODO Auto-generated constructor stub
	}

	public void PastorderFilter(DataRow input, DataRow output) 
	{
		try
		{
			uiDriver.FD_reorderPastOrder(input.get("ReorderFilter"), input
					.get("Timeframe"),input.get("Department"),input.get("SpecialPreference"),input.get("Product"),input.get("FlexibilityFlag"));
		}
		catch(Exception e)
		{
			RESULT.warning("Reorder Past Order Invocation", "Reorder Past Order invocation should be successful", "Reorder Past Order invocation fails ");
		}
	}

	public void YourTopItemFilter(DataRow input, DataRow output)
			throws InterruptedException, FindFailed, TimeoutException {
		try
		{
			uiDriver.FD_reorderYourTopItems(input.get("Filter"), input.get("DepartmentOrItem"), input.get("SpecialPref"), input.get("FlexibilityFlag"));
		}
		catch(Exception e)
		{
			RESULT.warning("Reorder Your Top Item Invocation", "Reorder Your Top Item invocation should be successful", "Reorder Your Top Item invocation fails ");
		}
		
	}

	public void YourListFilter(DataRow input, DataRow output)
			throws InterruptedException, FindFailed, TimeoutException {
		try {
			uiDriver.FD_reorderYourList(input.get("YourList"), input.get("Department"),input.get("SpecialPref"), 
					input.get("FlexibilityFlag"));
		} catch (Exception e) {
						
			RESULT.warning("Reorder Your List Invocation", "Reorder Your List invocation should be successful", "Reorder Your List invocation fails ");
		}

	}

	public void YourlistManageList(DataRow input, DataRow output)
			throws InterruptedException, FindFailed, TimeoutException {
		boolean manage_flag=false;
		try {
			//Navigate to reoder page through Reorder tab on homepage
			if (!(webDriver.findElements(By.xpath(objMap.getLocator("tabyourList"))).size() > 0))  {
				
				// click reorder tab
				try
				{
					uiDriver.getwebDriverLocator(objMap.getLocator("lnkreorder")).click();
					uiDriver.wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(objMap.getLocator("tabyourList"))));
					uiDriver.getwebDriverLocator(objMap.getLocator("tabyourList")).click();
				}
				catch(Exception e)
				{
					RESULT.failed("Reorder Your List : Reorder tab on Homepage", 
							"Application should navigate to reorder page and Reorder Your List tab should be clicked successfully",
							"Application could not click on Reorder Your List tab successfully");
				}
	
				try 
				{
					uiDriver.wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(objMap.getLocator("tabyourList"))));
					uiDriver.wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(objMap.getLocator("lstreorderDept"))));

				} 
				catch (TimeoutException e) {
					
				}
			}
			try
			{
				uiDriver.wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(objMap.getLocator("popmanageList"))));
				uiDriver.getwebDriverLocator(objMap.getLocator("popmanageList")).click();

				//wait for the manage list pop-up 
				SleepUtils.getInstance().sleep(TimeSlab.MEDIUM);
				uiDriver.wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(objMap.getLocator("lstreorderManageList"))));
			}
			catch(Exception e)
			{
				RESULT.warning("Reorder Your List : Manage list", "Manage list section should be available", "");
			}
			List<WebElement> shoppinglist_manage = webDriver.findElements(By.xpath(objMap.getLocator("lstreorderManageList")));
		

			if(shoppinglist_manage.size()>0)
			{
				for (WebElement slist_ite : shoppinglist_manage) {
//					wait.until(ExpectedConditions.stalenessOf(webDriver.findElement(By
//							.className(objMap.getLocator("popManageList")))));
					
					if (slist_ite.findElement(By.className(objMap.getLocator("strlistName"))).getAttribute("data-listname").equals(input.get("SettingValue"))) {
						manage_flag=true;
						if (input.get("Settings").equalsIgnoreCase("Default")) {
								
							try
							{
								slist_ite.findElement(By.xpath(objMap.getLocator("radmanageListDefault"))).click();
								// slist_ite.findElement(By.xpath(objMap.getLocator("radmanageListDefault"))).click();
								uiDriver.click("btnmanageListSaveChanges");
								SleepUtils.getInstance().sleep(TimeSlab.YIELD);
								if (webDriver.findElement(By.className(objMap.getLocator("strsettingsDefaultList"))).getText().equals(input.get("SettingValue")))
								{
									RESULT.passed("List default ", input.get("SettingValue")+" list should be changed to default", input.get("SettingValue")+" is made default");
									uiDriver.click("btnmanageListSaveChanges");
									break;
								}
								else
								{
									RESULT.failed("List default ", input.get("SettingValue")+" list should be changed to default", input.get("SettingValue")+" is made default");
									break;

								}
								
							}
							catch(Exception e)
							{
								RESULT.failed("Manage list : Default", input.get("SettingValue")+" list should be successfully set to default", input.get("SettingValue")+" list is not set to default");
							}
						
							break;
						} else if (input.get("Settings").equalsIgnoreCase("Delete")) {
							try
							{
								slist_ite.findElement(By.xpath(objMap.getLocator("radmanageListDelete"))).click();
								uiDriver.click("btnmanageListSaveChanges");
								SleepUtils.getInstance().sleep(TimeSlab.YIELD);
								if (webDriver.findElement(By.className(objMap.getLocator("strsettingsDeleteList"))).getText().equals(input.get("SettingValue")))
								{
									RESULT.passed("List delete ", input.get("SettingValue")+" list should be deleted", input.get("SettingValue")+" is deleted");
									uiDriver.click("btnmanageListSaveChanges");
									break;
								}
								else
								{
									RESULT.failed("List delete ", input.get("SettingValue")+" list should be deleted", input.get("SettingValue")+" is deleted");
									break;

								}
							
							}
							catch(Exception e)
							{
								RESULT.failed("Manage list : Delete", input.get("SettingValue")+" list should be successfully deleted", input.get("SettingValue")+" list is not deleted");
							}
					
							break;
						} 
						else if (input.get("Settings").equalsIgnoreCase("Rename")) {
							
//							wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
//									By.className(objMap.getLocator("txtmanageListRename"))));
							//wait.until(ExpectedConditions.stalenessOf(slist_ite.findElement(By.className(objMap.getLocator("txtmanageListRename")))));
							try
							{
								slist_ite.findElement(By.xpath(objMap.getLocator("txtmanageListRename"))).clear();
								slist_ite.findElement(By.xpath(objMap.getLocator("txtmanageListRename"))).sendKeys(input.get("RenameValue"));
								uiDriver.click("btnmanageListSaveChanges");
								if (webDriver.findElement(By.className(objMap.getLocator("strrenameOldName"))).getText().equals(input.get("SettingValue"))
										&& webDriver.findElement(By.className(objMap.getLocator("strrenameNewName"))).getText().equals(input.get("RenameValue"))) {
									RESULT.passed("Shopping list renaming",
													"List should be reanmed to "+ input.get("RenameValue")
													, "List "
													+ input.get("SettingValue")
													+ "is renamed to "
													+ input.get("RenameValue"));
									uiDriver.click("btnmanageListSaveChanges");
									
									break;
								} else {
									RESULT.failed("Shopping list renaming",
											"List should be reanmed to "+ input.get("RenameValue"),
											"List renaming unsuccessful");
								}

								
							}
							catch(Exception e)
							{
								RESULT.failed("Manage list : Rename", input.get("SettingValue")+" list should be successfully renamed to "+ input.get("RenameValue"), input.get("SettingValue")+" list is not renamed successfully");
							}
						
							break;
						}
						else
						{
							RESULT.failed("Reorder Your List  : Manage List", "Parameter for manage list should be Default, Delete or Rename", "Invalid parameter for Manage List :"+input.get("Settings"));
						}
						uiDriver.wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("manageShoppingListsPopup")));
					}
					
				}
				
				if(manage_flag==false)
				{
					uiDriver.click("btnmanageListPopupClose");
					uiDriver.wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("manageShoppingListsPopup")));
					RESULT.warning("Reorder Your List : Manage List", input.get("SettingValue")+" list name should be existing in the list",input.get("SettingValue")+ " list name is not available");
				}
			}
			else
			{
				RESULT.warning("Reorder Your List : Manage list", "One or more lists should be available to manage", "No list available");
			}
			
		} catch (Exception e) {
			RESULT.failed("Reorder Your List : Manage List", "Manage list should be successful", "Manage list is unsuccessful");

		}
	}

}
