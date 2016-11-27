package ModuleDrivers;


import ui.WebUIDriver;

import cbf.engine.BaseModuleDriver;
import cbf.engine.TestResultLogger;
import cbf.utils.DataRow;
import cbf.utils.SleepUtils;
import cbf.utils.SleepUtils.TimeSlab;

public class ModifyOrderCRMDriver extends BaseModuleDriver {

	public ModifyOrderCRMDriver(TestResultLogger resultLogger) {
		super(resultLogger);
	}

	public void ModifyCancelOrder(DataRow input, DataRow output)
	throws InterruptedException  {

		try{
		String Modify_Cancel=input.get("ModifyCancel").toUpperCase();
		if(Modify_Cancel.equals("CANCEL")){
			// call to cancel order reusable function
			uiDriver.FD_cancelOrderCRM(input.get("OrderNo"),input.get("EmailNotification"),input.get("Reason"),input.get("Notes"));
		}else{
			//call to modify order reusable function
			uiDriver.FD_modifyOrderCRM(input.get("OrderNo"), input.get("ItemName"),
					input.get("Quantity"), input.get("Operation"),
					input.get("DeleteAllAcceptDecline"), input.get("FlexibilityFlag"));
		}
		
		}catch(Exception e){
			RESULT.failed("Modify or Cancel Order CRM", "Order should get modified or canceled successfully",
					"Order is not modified or canceled due to " + e.getMessage());	
		}
		
	}

	public void MoodifyOrderWindowClose(DataRow input, DataRow output) {
		try {
			webDriver.close();
			//webDriver.quit();
			webDriver.switchTo().window(WebUIDriver.CRMWindow);
			SleepUtils.getInstance().sleep(TimeSlab.YIELD);
			RESULT.passed("Bring back control to CRM", "Bring back control to CRMshould be successful", 
					"Bring back control to CRM is successful...!!!");
		} catch (Exception e) {
			e.printStackTrace();
			RESULT.failed("Window transition from Storefront to CRM", "Window transition from Storefront to CRM should be successful",
					"Window transition from Storefront to CRM is failed");
			
		}
		
	}
	
}