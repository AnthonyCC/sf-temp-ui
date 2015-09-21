/******************************************************************************
$Id : CompositeAppDriver.java 9/8/2014 1:21:28 PM
Copyright 2014-2016 IGATE GROUP OF COMPANIES. All rights reserved
(Subject to Limited Distribution and Restricted Disclosure Only.)
THIS SOURCE FILE MAY CONTAIN INFORMATION WHICH IS THE PROPRIETARY
INFORMATION OF IGATE GROUP OF COMPANIES AND IS INTENDED FOR USE
ONLY BY THE ENTITY WHO IS ENTITLED TO AND MAY CONTAIN
INFORMATION THAT IS PRIVILEGED, CONFIDENTIAL, OR EXEMPT FROM
DISCLOSURE UNDER APPLICABLE LAW.
YOUR ACCESS TO THIS SOURCE FILE IS GOVERNED BY THE TERMS AND
CONDITIONS OF AN AGREEMENT BETWEEN YOU AND IGATE GROUP OF COMPANIES.
The USE, DISCLOSURE REPRODUCTION OR TRANSFER OF THIS PROGRAM IS
RESTRICTED AS SET FORTH THEREIN.
******************************************************************************/

package ModuleDrivers;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.Platform;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.safari.SafariDriver;
import org.sikuli.script.Screen;

import ui.SikuliUIDriver;
import ui.WebUIDriver;
import cbf.engine.BaseAppDriver;
import cbf.engine.ModuleDriver;
import cbf.engine.TestResultLogger;
import cbf.harness.Harness;
import cbf.plugin.PluginManager;
import cbf.utils.Configuration;

/**
 * 
 * Extends BaseAppDriver class and starts execution
 * 
 */
public class CompositeAppDriver extends BaseAppDriver {

	/**
	 * Main method to start the execution
	 * 
	 * @param args
	 *            command line arguments
	 */
	public static void main(String args[]) {
		
		String testSet= args[0];
		startUp=args[1];
		String nodeLabel= args[2];
		String workingDirectory = System.getProperty("user.dir");
		String filename = "Test/Plan/config.xml";
		String absoluteFilePath = workingDirectory + File.separator + filename;
		new Harness().runTestSet(absoluteFilePath,testSet,startUp,nodeLabel);
		new WebUIDriver().recover();
	}

	private enum BrowserType {
		IE, CHROME, FIREFOX,SAFARI;
	}

	private static String startUp;
	private String browserDriver, property;
	private Configuration GCONFIG=Harness.GCONFIG;
	
	private void initializeParameters() {
		//startUp = (String) GCONFIG.get("Browser");
		browserDriver = (String) GCONFIG.get("BrowserDriverFolder");
		property = "webdriver." + startUp.toLowerCase() + ".driver";
	}
	
	/**
	 * Used to initialize WebDriver
	 */
	public void initializeDrivers(){
		initializeParameters();		
		
		Object obj = GCONFIG.get("UIDrivers");
		if (!obj.toString().equals("{}")) {
			ArrayList<Map<String, Object>> list = (ArrayList<Map<String, Object>>) obj;
			
			if (webDriver == null && sikuliUIDriver == null) {
				for (Map<String, Object> map : list) {
					String plugin=(String) map.get("plugin");
					Map<String,Object> parameters =new HashMap<String, Object>();
					
					if(plugin.equals("Selenium")){
						switch (BrowserType.valueOf(startUp)) {
						case IE:
							System.setProperty(property, browserDriver);									             
				            DesiredCapabilities cap1 = DesiredCapabilities.internetExplorer();
				            cap1.setCapability("requireWindowFocus", true);
				            //cap1.setCapability("enablePersistentHover", true);
				            //cap1.setCapability("nativeEvents", false);   
				            cap1.setCapability("javascriptEnabled", true);
							//cap1.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,true);				           // cap1.setCapability("elementScrollBehavior", 0);
				           // cap1.setCapability("ignoreZoomSetting", false);
				           // cap1.setCapability("cssSelectorsEnabled", true);
				           // cap1.setCapability("javascriptEnabled", true);
				           // cap1.setCapability("unexpectedAlertBehaviour", "dismiss");
				           // cap1.setCapability("ignoreProtectedModeSettings", false);
				           // cap1.setCapability("disable-popup-blocking", true);
				            webDriver = new InternetExplorerDriver(cap1);
//							InternetExplorerOptions options = new InternetExplorerOptions();
//							options.EnablePersistentHover = false;
//							webDriver= new InternetExplorerDriver(options);
							break;

						case FIREFOX:
							webDriver = new FirefoxDriver();
							break;
		
						case CHROME:
							System.setProperty(property, browserDriver);
							ChromeOptions options = new ChromeOptions();
							options.addArguments("--test-type");
							webDriver = new ChromeDriver(options);
							break;
							
						case SAFARI:
							DesiredCapabilities cap = new DesiredCapabilities();
							cap.setPlatform(Platform.MAC);
							cap.setJavascriptEnabled(true);
							System.setProperty("webdriver.safari.noinstall", "true");
							webDriver=new SafariDriver();
							break;
						}

						String sBaseUrl = "http://www.google.com";
						//uiDriver = new WebUIDriver(webDriver, sBaseUrl);
						
						parameters.put("webDriver", webDriver);
						parameters.put("sBaseUrl", sBaseUrl);
						map.put("parameters", parameters);
					
						uiDriver = (WebUIDriver) PluginManager.getPlugin((String) map.get("plugin"),(Map<String, Object>) map.get("parameters"));
						webDriver.manage().window().maximize(); // To maximize the window
					
					}else if(plugin.equals("Sikuli")){
						sikuli=  new Screen();
						//sikuliUIDriver = new SikuliUIDriver(sikuli);
					
						parameters.put("sikuli", sikuli);
						map.put("parameters", parameters);
						sikuliUIDriver = (SikuliUIDriver) PluginManager.getPlugin((String) map.get("plugin"),(Map<String, Object>) map.get("parameters"));
					}			
				}	
			}			
		}
	}

	/**
	 * Initializes the modules specific to the application to be automated
	 * 
	 * @param resultLogger
	 *            TestResultLogger object with methods like passed, failed,
	 *            error etc available for reporting runtime results
	 * @return Map of modules
	 */
	public Map<String, ModuleDriver> initializeModules(
			TestResultLogger resultLogger) {
		HashMap<String, ModuleDriver> moduleDrivers = new HashMap<String, ModuleDriver>();
		//moduleDrivers.put("DemoInsurance", new DemoInsuranceDriver(resultLogger));
	moduleDrivers.put("General", new GeneralDriver(resultLogger));
		moduleDrivers.put("ReorderStorefront", new ReorderStorefrontDriver(resultLogger));
		moduleDrivers.put("ShoppingList", new ShoppingListDriver(resultLogger));
		moduleDrivers.put("Cart", new CartDriver(resultLogger));
		moduleDrivers.put("OrderStorefront", new OrderStorefrontDriver(resultLogger));
		moduleDrivers.put("DeliveryPass", new DeliveryPassDriver(resultLogger));
		moduleDrivers.put("StandingOrder", new StandingOrderDriver(resultLogger));
		moduleDrivers.put("ModifyOrderStorefront", new ModifyOrderStorefrontDriver(resultLogger));
		moduleDrivers.put("OrderCRM", new OrderCRMDriver(resultLogger));
		moduleDrivers.put("AccountDetailsCRM", new AccountDetailsCRMDriver(resultLogger));
		moduleDrivers.put("Case", new CaseDriver(resultLogger));
		moduleDrivers.put("Header", new HeaderDriver(resultLogger));
		moduleDrivers.put("AccountDetailsCRM", new AccountDetailsCRMDriver(resultLogger));
		moduleDrivers.put("GiftCard", new GiftCardDriver(resultLogger));
		moduleDrivers.put("ReserveTimeslot", new ReserveTimeslotDriver(resultLogger));
		moduleDrivers.put("Promotions", new PromotionsDriver(resultLogger));
		moduleDrivers.put("OrderStorefront_old", new OrderStorefront_oldDriver(resultLogger));
		return moduleDrivers;
	}

}
