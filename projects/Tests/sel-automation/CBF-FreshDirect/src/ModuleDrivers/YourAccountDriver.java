package ModuleDrivers;

import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
//import org.sikuli.script.FindFailed;

import ui.WebUIDriver;

import cbf.engine.BaseModuleDriver;
import cbf.engine.TestResultLogger;
import cbf.utils.DataRow;

public class YourAccountDriver extends BaseModuleDriver {
	
	public Date sysdate;
	public static String date_text;
	static String new_id;
	Date date=new Date();
	SimpleDateFormat dformat=new SimpleDateFormat("E',' MMMM d',' yyyy hh:mm a");
	Calendar cal=Calendar.getInstance();

	public YourAccountDriver(TestResultLogger resultLogger) {
		super(resultLogger);
		// TODO Auto-generated constructor stub
	}
	public void EmailChange(DataRow input,DataRow output) throws ParseException 
	{
		try{
			new_id=input.get("NewEmail");

			uiDriver.getwebDriverLocator(objMap.getLocator("txtnewEmail")).clear();
			uiDriver.getwebDriverLocator(objMap.getLocator("txtnewEmail")).sendKeys(input.get("NewEmail"));
			uiDriver.getwebDriverLocator(objMap.getLocator("txtconfirmNewEmail")).sendKeys(input.get("NewEmail"));
			webDriver.findElement(By.name("update_user")).click();
			date_text=dformat.format(cal.getTime());
			sysdate=dformat.parse(dformat.format(cal.getTime()));

			try{
				uiDriver.wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(objMap.getLocator("stremailChangeSuccess"))));
			}catch(Exception e){
				RESULT.failed("Email address change success message","Success message should be displayed", "Success message is not displayed");
				return;
			}
			if(webDriver.findElement(By.xpath(objMap.getLocator("stremailChangeSuccess"))).getText().contains("Your changes have been saved")){
				RESULT.passed("Email address updation","Email address should be updated", "Email updated successfully");
			}
			else{
				RESULT.failed("Email address updation","Email address should be updated", "Email Not updated successfully");
			}
		}catch(Exception e){
			RESULT.failed("Email address updation component","Email address should be updated", "Email address is not updated successfully");
		}
	}
	
	public void AddressAdd(DataRow input,DataRow output) throws ParseException
	{
		try
		{
			// add/edit/delete/select delivery address
			uiDriver.FD_chooseDeliveryAddress_old(input.get("AddSelectSelectPickupDeleteEdit"), input.get("FirstNameTb"), input.get("LastNameTb"), input.get("ServiceType"), 
					input.get("CompName"), input.get("StreetAdd"), input.get("AptName"), input.get("AddLine2"), input.get("City"), input.get("State"), 
					input.get("ZipCode"), input.get("Contact"), input.get("Ext1"), input.get("AltContact"), input.get("Ext2"),input.get("SpclDelInstructions"), 
					input.get("DoormanDelivery"), input.get("NeighbourDelivery"), input.get("AltFirstName"), input.get("AltLastName"),
					input.get("AltApt"), input.get("AltPhone"), input.get("AltExtn"),input.get("None"), input.get("SelectAddress"),input.get("FlexibilityFlag"));
			//save first name to check with body content while checking email notification 
			WebUIDriver.fistname=input.get("FirstNameTb");
			//save current date and time to compare in future
			date_text=dformat.format(cal.getTime());
			sysdate=dformat.parse(dformat.format(cal.getTime()));
		}
		catch(Exception e)
		{
			RESULT.failed(input.get("AddSelectSelectPickupDeleteEdit")+" Delivery address",
					"User should be able to"+ input.get("AddSelectSelectPickupDeleteEdit")+"delivery address",
					"address could not get "+ input.get("AddSelectSelectPickupDeleteEdit")+"ed due to: "+e.getMessage());
		}
	}
}
