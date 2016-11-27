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

public class DeliveryPassDriver extends BaseModuleDriver{

	
	public DeliveryPassDriver(TestResultLogger resultLogger) {
		super(resultLogger);
		// TODO Auto-generated constructor stub
	}
	
	/*
	 * Component function to add,verify and renew delivery pass
	 */
	public void DeliverypassOptions(DataRow input, DataRow output)
			throws InterruptedException {
		try {
			// call to the delivery pass add_verify  reusable function
			uiDriver.FD_Deliverypass_Add_Verify(input.get("Process"));
		} catch (Exception e) {
			RESULT.failed("Delivery Pass Options Component", "Delivery pass should get added/verified",
					"Delivery pass is not added/verified due to" + e.getMessage());
		}
	}

}