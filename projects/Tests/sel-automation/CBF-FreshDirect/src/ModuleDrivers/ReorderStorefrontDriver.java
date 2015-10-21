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

public class ReorderStorefrontDriver extends BaseModuleDriver {

	Actions ac_key = new Actions(webDriver);
	WebDriverWait wait = new WebDriverWait(webDriver, 30);

	public ReorderStorefrontDriver(TestResultLogger resultLogger) {
		super(resultLogger);
		// TODO Auto-generated constructor stub
	}

	public void PastorderFilter(DataRow input, DataRow output) {
		try {
			uiDriver.FD_reorderPastOrder(input.get("ReorderFilter"), input
					.get("Timeframe"),input.get("Department"),input.get("SpecialPreference"),input.get("Product"),input.get("FlexibilityFlag"));
		} catch (FindFailed e) {

			System.out.println("Exception FindFailed");
			e.printStackTrace();

		} catch (InterruptedException e) {

			System.out.println("Exception unexpected Interrupt");
			e.printStackTrace();

		} catch (TimeoutException e) {
			System.out.println("Exception Timeout");
			e.printStackTrace();
		}
	}

	public void YourTopItemFilter(DataRow input, DataRow output)
			throws InterruptedException, FindFailed, TimeoutException {
		uiDriver.FD_reorderYourTopItems(input.get("Filter"), input.get("DepartmentOrItem"), input.get("SpecialPref"), input.get("FlexibilityFlag"));
	}

	public void YourListFilter(DataRow input, DataRow output)
			throws InterruptedException, FindFailed, TimeoutException {
		try {
			uiDriver.FD_reorderYourList(input.get("YourList"), input.get("Department"),input.get("SpecialPref"), 
					input.get("FlexibilityFlag"));
		} catch (Exception e) {
			e.printStackTrace();			
			RESULT.failed("Add to list fails",
					"Product add to list should not be successful",
					"Product add to list is not successful");
		}

	}

	public void YourlistManageList(DataRow input, DataRow output)
			throws InterruptedException, FindFailed, TimeoutException {
		try {

			if (!(webDriver.findElements(By.xpath(objMap.getLocator("tabyourList"))).size() > 0))  {
				// click reorder tab
				uiDriver.getwebDriverLocator(objMap.getLocator("lnkreorder"))
						.click();
				wait.until(ExpectedConditions.visibilityOfElementLocated(By
						.xpath(objMap.getLocator("tabyourList"))));
				uiDriver.getwebDriverLocator(objMap.getLocator("tabyourList"))
						.click();
				// Need to check this wait

				try {
					wait.until(ExpectedConditions
							.visibilityOfAllElementsLocatedBy(By.xpath(objMap
									.getLocator("lstreorderDept"))));

				} catch (Exception e) {
					System.out.println("Unable to find element");
					e.printStackTrace();
				}
			}

			wait.until(ExpectedConditions.visibilityOfElementLocated(By
					.xpath(objMap.getLocator("tabyourList"))));
			wait.until(ExpectedConditions
					.visibilityOfAllElementsLocatedBy(By.xpath(objMap
							.getLocator("lstreorderDept"))));
			wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By
					.xpath(objMap.getLocator("popmanageList"))));
			uiDriver.getwebDriverLocator(
					objMap.getLocator("popmanageList")).click();

//			ac_key.moveToElement(webDriver.findElement(By
//					.id("manageShoppingListsPopup")));
			
			//wait for the manage list pop-up 
			SleepUtils.getInstance().sleep(TimeSlab.MEDIUM);
			wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(objMap.getLocator("lstreorderManageList"))));

			List<WebElement> shoppinglist_manage = webDriver.findElements(By
					.xpath(objMap.getLocator("lstreorderManageList")));
			int count=0;

			for (WebElement slist_ite : shoppinglist_manage) {
//				wait.until(ExpectedConditions.stalenessOf(webDriver.findElement(By
//						.className(objMap.getLocator("popManageList")))));
				
				if (slist_ite.findElement(
						By.className(objMap.getLocator("strlistName")))
						.getAttribute("data-listname").equals(
								input.get("SettingValue"))) {
					count++;
					if (input.get("Settings").equalsIgnoreCase("Default")) {

						slist_ite.findElement(
								By.xpath(objMap
										.getLocator("radmanageListDefault")))
								.click();
						// slist_ite.findElement(By.xpath(objMap.getLocator("radmanageListDefault"))).click();
						uiDriver.click("btnmanageListSaveChanges");
						SleepUtils.getInstance().sleep(TimeSlab.YIELD);
						if (webDriver.findElement(
								By.className(objMap
										.getLocator("strsettingsDefaultList")))
								.getText().equals(input.get("SettingValue")))
						{
							RESULT.passed("List default ", input.get("SettingValue")+" list should be changed to default", input.get("SettingValue")+" is made default");
						}
						else
						{
							RESULT.failed("List default ", input.get("SettingValue")+" list should be changed to default", input.get("SettingValue")+" is made default");

						}
						uiDriver.click("btnmanageListSaveChanges");
						break;
					} else if (input.get("Settings").equalsIgnoreCase("Delete")) {
						slist_ite.findElement(
								By.xpath(objMap
										.getLocator("radmanageListDelete")))
								.click();
						uiDriver.click("btnmanageListSaveChanges");
						SleepUtils.getInstance().sleep(TimeSlab.YIELD);
						if (webDriver.findElement(
								By.className(objMap
										.getLocator("strsettingsDeleteList")))
								.getText().equals(input.get("SettingValue")))
						{
							RESULT.passed("List delete ", input.get("SettingValue")+" list should be deleted", input.get("SettingValue")+" is deleted");
						}
						else
						{
							RESULT.failed("List delete ", input.get("SettingValue")+" list should be deleted", input.get("SettingValue")+" is deleted");

						}
								uiDriver.click("btnmanageListSaveChanges");
						break;
					} else if (input.get("Settings").equalsIgnoreCase("Rename")) {
						
//						wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
//								By.className(objMap.getLocator("txtmanageListRename"))));
						//wait.until(ExpectedConditions.stalenessOf(slist_ite.findElement(By.className(objMap.getLocator("txtmanageListRename")))));
						slist_ite.findElement(
								By.xpath(objMap.getLocator("txtmanageListRename")))
								.clear();
						slist_ite.findElement(
								By.xpath(objMap.getLocator("txtmanageListRename")))
								.sendKeys(input.get("RenameValue"));
						uiDriver.click("btnmanageListSaveChanges");
						if (webDriver.findElement(
								By.className(objMap
										.getLocator("strrenameOldName")))
								.getText().equals(input.get("SettingValue"))
								&& webDriver
										.findElement(
												By.className(objMap
																.getLocator("strrenameNewName")))
										.getText().equals(
												input.get("RenameValue"))) {
							RESULT.passed("Shopping list renaming",
									"List should be reanmed to "
											+ input.get("RenameValue"), "List "
											+ input.get("SettingValue")
											+ "is renamed to "
											+ input.get("RenameValue"));
						} else {
							RESULT.failed("Shopping list renaming",
									"List should be reanmed to "
											+ input.get("RenameValue"),
									"List renaming unsuccessful");
						}

						uiDriver.click("btnmanageListSaveChanges");
						break;
					}
				}
			}
			if(count==0)
			{
				RESULT.warning("Manage List - "+input.get("Settings"), "Shopping list named "+input.get("SettingValue")+" should be available", "Shopping list named "+input.get("SettingValue")+" not available");
				uiDriver.click("btnmanageListPopupClose");
			}
		} catch (Exception e) {
			e.printStackTrace();

		}
	}

}
