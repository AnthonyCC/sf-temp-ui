package ui;

import java.awt.AWTException;
import java.awt.Robot;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.internal.Locatable;

import ModuleDrivers.CompositeAppDriver;

import cbf.harness.Harness;
import cbf.utils.Configuration;
import cbf.utils.SleepUtils;
import cbf.utils.SleepUtils.TimeSlab;

public class RobotPowered {

	private final Robot mouseObject;
	private final WebDriver driver;
	private final JavascriptExecutor executor;
	Actions actions;
	//Configuration GCONFIG;

	public RobotPowered(WebDriver driver) throws AWTException {
		this.mouseObject = new Robot();
		this.driver = driver;
		this.executor = (JavascriptExecutor) driver;
		actions = new Actions(driver);
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
			mouseObject.mouseMove(xPosition-1, yPosition);	        
			SleepUtils.getInstance().sleep(TimeSlab.YIELD);
			mouseObject.mouseMove(xPosition, yPosition);	
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
