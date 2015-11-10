package ModuleDrivers;
import org.sikuli.script.FindFailed;

import cbf.engine.BaseModuleDriver;
import cbf.engine.TestResultLogger;
import cbf.utils.DataRow;

public class ModifyOrderCRMDriver extends BaseModuleDriver {

	public ModifyOrderCRMDriver(TestResultLogger resultLogger) {
		super(resultLogger);
	}
	public void ModifyCancelOrder(DataRow input, DataRow output)
	throws InterruptedException, FindFailed {

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


}