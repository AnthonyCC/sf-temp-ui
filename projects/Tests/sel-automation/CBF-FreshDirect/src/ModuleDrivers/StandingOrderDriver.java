package ModuleDrivers;

import java.util.List;
import java.util.concurrent.TimeoutException;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import ui.WebUIDriver;

import cbf.engine.BaseModuleDriver;
import cbf.engine.TestResultLogger;
import cbf.engine.TestResult.ResultType;
import cbf.utils.DataRow;
import cbf.utils.SleepUtils;
import cbf.utils.SleepUtils.TimeSlab;

public class StandingOrderDriver extends BaseModuleDriver{
	
	public StandingOrderDriver(TestResultLogger resultLogger) {
		super(resultLogger);
		// TODO Auto-generated constructor stub
	}

	/* Create a Standing Order for Corporate delivery at particular interval of time */	
	public void CreateStandingOrder(DataRow input, DataRow output) throws   InterruptedException
	{
		try{
			uiDriver.setValue("txtsoName", input.get("SOName"));                                  
			uiDriver.setValue("drpsoDeliverOrder", input.get("SODeliverOrder"));	
			uiDriver.click("btnstartShopping");		
//			uiDriver.waitForPageLoad();
//			uiDriver.wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(objMap.getLocator("btnstartShopping"))));
			SleepUtils.getInstance().sleep(TimeSlab.MEDIUM);
			if(!(webDriver.findElements(By.xpath(objMap.getLocator("btnstartShopping"))).size()>0))
			{
				RESULT.passed("StandingOrderCreaton", "Standing order should be created and should start shopping",
				"Standing Order created successfully");	
			} 
			else
			{
				WebElement element = uiDriver.getwebDriverLocator(objMap.getLocator("strsoError"));
				((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView(true);", element);					
				SleepUtils.getInstance().sleep(TimeSlab.YIELD);
				RESULT.failed("StandingOrderCreaton", "Standing order should be created and should start shopping",
				"The name is already in use. Please specify another name.");	
				return;		
			}
		}catch(Exception e)
		{
			RESULT.failed("Create Standing Order Component", "Standing order should get created successfully",
					"Standing order is not created due to " + e.getMessage());	
		}
	}

/*
	 Modify existing Standing Order 	
	public void ModifyStandingOrder(DataRow input, DataRow output) throws   InterruptedException
	{
		SleepUtils.getInstance().sleep(TimeSlab.YIELD);
		List<WebElement> SOlist = webDriver.findElements(By.xpath(objMap.getLocator("lstsoList")));       //List of Standing Orders                  
		int iSize =  SOlist.size();
		int k=0;
		for (int i=0 ; i< iSize ; i++)
		{
			String SO=SOlist.get(i).getText();													   
			if(SO.startsWith(input.get("SOName")))										
			{
				k++;
				SOlist.get(i).click(); 
				RESULT.passed("ModifyOrder", "Standing Order should be selected", "Standing Order selected!!!");
				break;					
			}				
		}
		if(k==0)
		{
			RESULT.failed("ModifyOrder", "Standing Order should be selected", "Standing Order not found.");
		}
		uiDriver.click("btnmodifyOrderBtn");													    
		uiDriver.click("btnmodifyOrderBtn1");
		if (webDriver.getTitle().equals("FreshDirect - View Cart")) 
		{
			SleepUtils.getInstance().sleep(TimeSlab.YIELD);
			RESULT.passed("ModifyOrder", "Standing Order should be selected", "Standing Order not found!!!");			
		}		
		
	}	



	 Cancel existing Standing Order 
	public void CancelStandingOrder(DataRow input, DataRow output) throws   InterruptedException
	{
		SleepUtils.getInstance().sleep(TimeSlab.YIELD);
		List<WebElement> SOlist = webDriver.findElements(By.xpath(objMap.getLocator("lstsoList")));           //List of Standing Orders         
		int iSize = SOlist.size();
		int m=0;
		for (int i=0 ; i< iSize ; i++)
		{
			String SO=SOlist.get(i).getText();
			if(SO.startsWith(input.get("SOName")))
			{
				m++;
				SOlist.get(i).click(); 
				RESULT.passed("CancelOrder", "Standing Order should be selected", "Standing Order selected!!!");
				break;					
			}				
		}
		if(m==0)
		{
			RESULT.failed("CancelOrder", "Standing Order should be selected", "Standing Order not found.");
		}
		uiDriver.click("btncancelOrderBtnNew");
		uiDriver.click("btncancelOrderBtnNew");
		if (webDriver.getTitle().equals("FreshDirect - Your Account - Order Cancelled")) 
		{
			SleepUtils.getInstance().sleep(TimeSlab.YIELD);
			RESULT.passed("CancelOrder", "Standing order should get cancelled.", "Order cancelled successfully!!!!");				
		}
		else
		{
			RESULT.failed("CancelOrder", "Standing order should get cancelled.", "Error occured somewhere");
		}
	}*/
}