package ui;

import java.awt.AWTException;
import java.awt.Robot;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.internal.Locatable;

import ModuleDrivers.CompositeAppDriver;
import cbf.utils.SleepUtils;
import cbf.utils.SleepUtils.TimeSlab;
import  ui.ObjectMap;
public class RobotPowered {

	private final Robot mouseObject;
	private final WebDriver driver;
	private final JavascriptExecutor executor;
	private final ObjectMap objMap;
	Actions actions;
	//Configuration GCONFIG;

	public RobotPowered(WebDriver driver) throws AWTException {
		this.mouseObject = new Robot();
		this.driver = driver;
		this.executor = (JavascriptExecutor) driver;
		actions = new Actions(driver);
		objMap = new ObjectMap();
		//GCONFIG = Harness.GCONFIG;
	}


	public void moveToElement(WebElement element) {

		if (CompositeAppDriver.startUp.equalsIgnoreCase("IE") || CompositeAppDriver.startUp.equalsIgnoreCase("SAFARI")) {
			//Scroll to Element
			scrollToElement(element, driver);
			// Get Browser dimensions
			int browserWidth = driver.manage().window().getSize().width;
			int browserHeight = driver.manage().window().getSize().height;

			// Get dimensions of the window displaying the web page
			int pageWidth = Integer.parseInt(executor.executeScript(
			"return document.documentElement.clientWidth").toString());
			int pageHeight = Integer.parseInt(executor.executeScript(
			"return document.documentElement.clientHeight").toString());
			// To handle hover after masquered, will chnage based on screen size
			if(pageHeight<655)
				pageHeight=655;
			
			// Calculate the space the browser is using for toolbars
			int browserFurnitureOffsetX = browserWidth - pageWidth;
			int browserFurnitureOffsetY = browserHeight - pageHeight;

			// Get the coordinates of the WebElement on the page and calculate
			// the centre point
			int webElementX = ((Locatable) element).getCoordinates()
			.inViewPort().x
			+ Math.round(element.getSize().width / 2);
			int webElementY = ((Locatable) element).getCoordinates()
			.inViewPort().y
			+ Math.round(element.getSize().height / 2);

			// Calculate the correct X/Y coordinates based upon the browser
			// furniture offset and the position of the browser on the desktop
			int xPosition = (int) (driver.manage().window().getPosition().x
					+ browserFurnitureOffsetX + webElementX);
			int yPosition = (int) (driver.manage().window().getPosition().y
					+ browserFurnitureOffsetY + webElementY);

			// Move the mouse to the calculated X/Y coordinates
			if(driver.findElements(By.id(objMap.getLocator("strstorefrontMsg"))).size()>0 && driver.findElement(By.id("topwarningbar")).isDisplayed())
			{
				if (CompositeAppDriver.startUp.equalsIgnoreCase("IE")) 
				{
					mouseObject.mouseMove(xPosition-1, yPosition-23);
					SleepUtils.getInstance().sleep(TimeSlab.YIELD);
					mouseObject.mouseMove(xPosition, yPosition-23);	
				}
				else
				{
					int sub_webElementY = Math.round(driver.findElement(By.id("topwarningbar")).getSize().height / 2);				
					mouseObject.mouseMove(xPosition-1, yPosition-sub_webElementY);
					SleepUtils.getInstance().sleep(TimeSlab.YIELD);
					mouseObject.mouseMove(xPosition, yPosition-sub_webElementY);	
				}

			}else{
				mouseObject.mouseMove(xPosition-1, yPosition);	
				SleepUtils.getInstance().sleep(TimeSlab.YIELD);
				mouseObject.mouseMove(xPosition, yPosition);	
			}
			mouseObject.waitForIdle();
		} else {
			actions.moveToElement(element).build().perform();
		}
	}

	public void moveMouseToCoordinates(int xCoordinates, int yCoordinates) {
		//Get Browser dimensions
		int browserWidth = driver.manage().window().getSize().width;
		int browserHeight = driver.manage().window().getSize().height;

		//Get dimensions of the window displaying the web page
		int pageWidth = Integer.parseInt(executor.executeScript("return document.documentElement.clientWidth").toString());
		int pageHeight = Integer.parseInt(executor.executeScript("return document.documentElement.clientHeight").toString());

		//Calculate the space the browser is using for toolbars
		int browserFurnitureOffsetX = browserWidth - pageWidth;
		int browserFurnitureOffsetY = browserHeight - pageHeight;

		//Calculate the correct X/Y coordinates based upon the browser furniture offset and the position of the browser on the desktop
		int xPosition = driver.manage().window().getPosition().x + browserFurnitureOffsetX + xCoordinates;
		int yPosition = driver.manage().window().getPosition().y + browserFurnitureOffsetY + yCoordinates;

		//Move the mouse to the calculated X/Y coordinates
		mouseObject.mouseMove(xPosition, yPosition);
		mouseObject.waitForIdle();
	}
	public void scrollToElement(WebElement elem, WebDriver driver) {
		((JavascriptExecutor) driver).executeScript(
				"arguments[0].scrollIntoView(true);", elem);
	}
}
