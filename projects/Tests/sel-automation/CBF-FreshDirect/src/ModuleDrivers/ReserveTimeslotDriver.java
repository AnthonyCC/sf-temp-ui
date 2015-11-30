package ModuleDrivers;

import java.util.NoSuchElementException;

import org.openqa.selenium.By;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.sikuli.script.FindFailed;

import cbf.engine.BaseModuleDriver;
import cbf.engine.TestResultLogger;
import cbf.utils.DataRow;
import cbf.utils.SleepUtils;
import cbf.utils.SleepUtils.TimeSlab;

public class ReserveTimeslotDriver extends BaseModuleDriver {

	Actions ac_key = new Actions(webDriver);
	//	WebDriverWait wait = new WebDriverWait(webDriver, 20);
//	public static boolean reserve_flag = false;

	public ReserveTimeslotDriver(TestResultLogger resultLogger) {
		super(resultLogger);
		// TODO Auto-generated constructor stub
	}

	public void ReserveDeliveryTime(DataRow input, DataRow output)
	throws InterruptedException, FindFailed {
		try{	
			uiDriver.FD_chooseReserveTimeSlot(input.get("Address"), input
					.get("ReserveType"), input.get("Day"), input.get("Time"), input
					.get("FlexibilityFlag"));
		}catch (Exception e) {
			RESULT.failed("Reserve delivery time component", "Reserve delivery time should be successful"
					, "Reserve delivery time failed due to "+e.getMessage());
		}
	}

	@SuppressWarnings("deprecation")
	public void CancelReserveDeliveryTime(DataRow input, DataRow output)
	throws InterruptedException, FindFailed {
		//wait for drop down of address
		try{
			uiDriver.wait.until(ExpectedConditions.presenceOfElementLocated(By
					.name(objMap.getLocator("drpreserveAddress"))));
			Select add_ddl = new Select(uiDriver.getwebDriverLocator(objMap
					.getLocator("drpreserveAddress")));
			
			//select address from drop down
			try{
				add_ddl.selectByVisibleText(input.get("AddressDropDown"));
			}catch (Exception e) {
				RESULT.warning("Select address from drop down", "Select given address from drop down",
						"Given address does not exist in list.First address from dropdown has been selected");
				add_ddl.selectByIndex(0);
			}
			uiDriver.waitForPageLoad();
			uiDriver.wait.until(ExpectedConditions.visibilityOfElementLocated(By
					.name(objMap.getLocator("drpreserveAddress"))));

			//page refresh to handle stale element reference error
			if (CompositeAppDriver.startUp.equalsIgnoreCase("IE")) {
				webDriver.navigate().refresh();
				if(uiDriver.popup_isAlertPresent(10))
					uiDriver.popup_ClickOkOnAlert();
			}
			uiDriver.waitForPageLoad();
			//check if Cancel reservation button available or not
			if (webDriver.findElements(
					By.xpath(objMap.getLocator("btnreserveCancel"))).size() > 0) {
				//choose any time slot to cancel for particular address
				uiDriver.FD_chooseTimeslot(input.get("Day"),input.get("Time"), input.get("FlexibilityFlag") );
				try{
					uiDriver.waitForPageLoad();
					//click on cancel button
					uiDriver.click("btnreserveCancel");
					uiDriver.wait.until(ExpectedConditions.visibilityOfElementLocated(By
							.xpath(objMap.getLocator("strreserveSuccessMsg"))));					
				}catch (Exception e) {
					//check for error on cancellation of reservation 
					if(webDriver.findElements(By.xpath(objMap.getLocator("strerrorReservation"))).size()>0)
					{
						RESULT.failed("Reservation Time slot updation unsuccessful",
								"Reservation time slot should be modified",
								webDriver.findElement(By.xpath(objMap.getLocator("strerrorReservation"))).getText());
					}
				}
				//verify success message of reservation cancellation
				if (uiDriver.getwebDriverLocator(
							objMap.getLocator("strreserveSuccessMsg")).getText()
							.contains("Your delivery timeslot reservation has been cancelled")) {
					RESULT.passed("Reservation Time slot cancellation successful",
							"Reservation time slot should be cancelled",
						"Reservation time slot cancelled");
				} else {
					RESULT.failed(
							"Reservation Time slot cancellation unsuccessful",
							"Reservation time slot cancellation error",
							"Reservation time slot not cancelled");
				}

			}else
			{
				//Time slot is not reserved to cancel
				RESULT.passed("Cancel reservation", "Time slot should not be reserved to cancel", "Time slot is not reserved to cancel");
			}
		}catch (Exception e) {
			RESULT.failed("Cancel reserved delivery time component", "Reserve delivery time should be canceled successful"
					, "Reserve delivery is not canceled due to "+e.getMessage());
		}

	}
	public void ModifyReserveDeliveryTime(DataRow input, DataRow output)
	throws InterruptedException, FindFailed {
		try{
			Select add_ddl = new Select(uiDriver.getwebDriverLocator(objMap
					.getLocator("drpreserveAddress")));
			
			//select address from drop down 
			add_ddl.selectByVisibleText(input.get("AddressDropDown"));
			uiDriver.waitForPageLoad();
			
			//page refresh to handle stale element reference error
			if (CompositeAppDriver.startUp.equalsIgnoreCase("IE")) {
				webDriver.navigate().refresh();
				if(uiDriver.popup_isAlertPresent(10))
					uiDriver.popup_ClickOkOnAlert();
			}
			
			uiDriver.wait.until(ExpectedConditions.visibilityOfElementLocated(By
					.name(objMap.getLocator("drpreserveAddress"))));
            // use only for stand alone component
			//check if time slot is already reserved to modify
			/*if (!(webDriver.findElements(
					By.xpath(objMap.getLocator("btnreserveCancel"))).size() > 0)) {
				RESULT.warning("Modify reserved time slot", "Reservation time slot should be modified", "No time slot has been reserved to modify");
				// if time slot is not reserved make a fresh reservation
				uiDriver.FD_chooseReserveTimeSlot(input.get("Address"), input
						.get("ReserveType"), input.get("Day"), input.get("Time"), input
						.get("FlexibilityFlag"));

			}*/
			//check again for cancel button to confirm that time slot is reserved and can be modified
			if((webDriver.findElements(
					By.xpath(objMap.getLocator("btnreserveCancel"))).size() > 0))
			{
				//page refresh to handle stale element reference error
				if (CompositeAppDriver.startUp.equalsIgnoreCase("IE")) {
					webDriver.navigate().refresh();
					if(uiDriver.popup_isAlertPresent(10))
						uiDriver.popup_ClickOkOnAlert();
				}
				//Modify the time slot
				uiDriver.FD_chooseTimeslot(input.get("Day1"),input.get("Time1"), input.get("FlexibilityFlag") );
				
				//select type of reservation
				if (input.get("ReserveType").equalsIgnoreCase("This week")) {				
					uiDriver.click("radreserveThisWeek");
				} else if (input.get("ReserveType").equalsIgnoreCase("Every week")) {					
					uiDriver.click("radreserveEveryWeek");
				}else{
					RESULT.warning("Modify Reserved delivery time Reserve Type", "Reserve type should have value from allowed values ['This week','Every week']"
							, "Reserve type has value  "+ input.get("ReserveType"));
				}
				SleepUtils.getInstance().sleep(TimeSlab.YIELD);
				//			uiDriver.click("btnsaveReserveTime");
				//click on save reserved time slot button
				webDriver.findElement(By.xpath(objMap.getLocator("btnsaveReserveTime"))).click();
				uiDriver.waitForPageLoad();
				try{
					uiDriver.wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(objMap.getLocator("strreserveSuccessMsg"))));
					uiDriver.robot.scrollToElement(webDriver.findElement(By.xpath(objMap.getLocator("strreserveSuccessMsg"))), webDriver);
			}catch (Exception e) {
					//Fetch error if reservation modification unsuccessful
					if(webDriver.findElements(By.xpath(objMap.getLocator("strerrorReservation"))).size()>0)
					{
						RESULT.failed("Reservation Time slot updation unsuccessful",
								"Reservation time slot should be modified",
								webDriver.findElement(By.xpath(objMap.getLocator("strerrorReservation"))).getText());
						
					}
					else{
						RESULT.failed("Reservation Time slot updation unsuccessful",
								"Reservation time slot should be modified","Error in modification of reservation");
					}
					return;
				}
				//Fetch success message for modification of reservation
				if (uiDriver.getwebDriverLocator(objMap.getLocator("strreserveSuccessMsg")).getText().contains("has been changed")) {
					RESULT.passed("Reservation Time slot modification successful",
							"Reservation time slot should be modified",
							"Reservation time slot modified to " + uiDriver.verify_day+ " " + uiDriver.verify_time);
					uiDriver.reserve_flag = true;
				} else {
					RESULT.failed("Reservation Time slot updation unsuccessful",
							"Reservation time slot should be modified",
							"Reservation time slot cannot be modofied to"+uiDriver.verify_day+ " " + uiDriver.verify_time);
					return;
				}
			}
		}catch(Exception e)
		{
			RESULT.failed("Modify Reserved delivery time component", "Reserve delivery time should be modified successful"
					, "Reserve delivery time is not modified due to "+e.getMessage());
			return;
		}
	}

}
