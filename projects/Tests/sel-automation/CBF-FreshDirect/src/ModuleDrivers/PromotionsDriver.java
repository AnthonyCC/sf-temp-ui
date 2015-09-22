package ModuleDrivers;

import java.awt.AWTException;
import java.util.List;
import java.util.NoSuchElementException;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
//import org.openqa.selenium.interactions.Actions;
import org.sikuli.script.FindFailed;

import ui.RobotPowered;
import ui.WebUIDriver;

import cbf.engine.BaseModuleDriver;
import cbf.engine.TestResultLogger;
import cbf.engine.TestResult.ResultType;
import cbf.harness.Harness;
import cbf.utils.Configuration;
import cbf.utils.DataRow;
import cbf.utils.SleepUtils;
import cbf.utils.SleepUtils.TimeSlab;

public class PromotionsDriver extends BaseModuleDriver{
	public WebDriverWait wait = new WebDriverWait(webDriver, 3000);
	Actions actions = new Actions(webDriver);
	public PromotionsDriver(TestResultLogger resultLogger) {
		super(resultLogger);
	}
	//verify user is able to place order using promotional offers on home page[TestCase#1,2,3,4,5]
	public void PromotionalOffers(DataRow input,DataRow output)throws InterruptedException, FindFailed {
		//verify if promotional offers section is there or not if yes it should contain links[TestCase#1]
		if(uiDriver.getwebDriverLocator(objMap.getLocator("lstpromoOffers")).isDisplayed())
		{
			RESULT.passed("Promotional offers links on home page", "Promotional offers links section should be available on home page", 
			"Promotional offers links section available on home page");
		}
		else
			RESULT.failed("Promotional offers links on home page", "Promotional offers links section should be available on home page", 
			"Promotional offers links section not available on home page");

		//verify promotional link redirects to correct page[TestCase#2]
		try{
			//check if input has been given by user to open the link
			if(input.get("offers")!=null||input.get("offers")!="")
			{
				List<WebElement> Links=webDriver.findElements(By.xpath(objMap.getLocator("lstoffersLinks")));
				int a=0;
				for(WebElement link:Links)
				{ 
					//check if the name of link taken from link matches with the user input
					if(link.findElement(By.tagName("a")).getAttribute("href").contains(input.get("offers")))
					{
						link.findElement(By.tagName("a")).click(); //open the link
						a++;
						if(webDriver.getTitle().contains(input.get("offers")) || webDriver.getTitle().contains("Reorder")) //check if correct page has been opened
						{
							RESULT.passed("Promotional offers link page", "Promotional offers link should redirect to correct page", 
							"Promotional offers link redirected to correct page");
						}
						else
							RESULT.failed("Promotional offers link page", "Promotional offers link should redirect to correct page", 
							"Promotional offers link could not redirect to correct page");
						break;
					}
				}
				//check if user input has matched with any of the link available on home page
				if(a==0)
					RESULT.failed("Promotional offers link page", "Promotional offers link should redirect to correct page", 
					"Promotional offers link name specified is not correct");
			}
			else
				RESULT.failed("Promotional offers link page", "Promotional offers link should redirect to correct page", 
				"Promotional offers link name not specified by user");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		//place an order from promotional offers page products[TestCase#4]
		try{
			if(uiDriver.isDisplayed("lnkproductElement"))
			{
				String prodName=uiDriver.getwebDriverLocator(objMap.getLocator("lnkproductElement")).getText();
				//				RESULT.passed("", expected, actual)
				uiDriver.FD_addToCart(input.get("Item"),
						input.get("orderflag_new_modify"), input.get("quantity"), input.get("click_hover"),input.get("FlexibilityFlag"),"","");
			}
			else
			{
				RESULT.failed("Product Add to cart", "Desired product should get added to cart", "There are no products available as of now on this page");
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			RESULT.failed("Error", "Element not found", "Element not found");
		}

	}

	/*Verify promotional media images are available on home page media ad section and place order using those products[TestCase#6,7,8,9,10]*/
	public void MediaAddSection(DataRow input,DataRow output)throws InterruptedException, FindFailed {
		try{
			//Verify promotional media images are available on home page media ad image section.[TC#6]
			if(uiDriver.isElementPresent(By.xpath(objMap.getLocator("mediaAd"))))
			{
				RESULT.passed("Promotional offers links on home page", "Promotional offers links section should be available on home page", 
				"Promotional offers links section available on home page");

				//Verify promotional offer pages are displayed after clicking on promotional offers' media ads image[TC#7].
				uiDriver.click("mediaAd");
				uiDriver.selenium.waitForPageToLoad("3000");
				if(webDriver.getTitle().equals("Welcome to FreshDirect"))
				{
					RESULT.failed("Promotional offers media ad images", "Promotional offers media ad images section should redirect to relavent page", 
					"Promotional offers media ad images section could not redirect to relavent page");
				}
				else
				{
					RESULT.passed("Promotional offers media ad images", "Promotional offers media ad images section should redirect to relavent page",
					"Promotional offers media ad images section redirected to its page");
				}
				try{
					//Verify if product exists in page then add product to cart 

					if(uiDriver.isElementPresent(By.className("portrait-item   carouselTransactionalItem")))
					{
						uiDriver.FD_addToCart(input.get("Item"),
								input.get("orderflag_new_modify"), input.get("quantity"), input.get("click_hover"),input.get("FlexibilityFlag"),"","");
					}
					else
						RESULT.failed("Promotional offers media ad products", "Promotional offers media ad product should get added to cart",
						"Promotional offers media ad product page does not exist");
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
			else
				RESULT.failed("Promotional offers media ad images on home page", "Promotional offers media ad images section should be available on home page", 
				"Promotional offers media ad images section not available on home page");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

	}

	//Go to deals and add product from any of available sub category[TestCase#11,12,13]
	public void PromotionsDeals(DataRow input,DataRow output)throws InterruptedException, FindFailed {

		//check if all options of promotions are available or not.[Testcase#11] 
		String promoSubCat=uiDriver.getwebDriverLocator(objMap.getLocator("lstpromoSubCat")).getText();
		System.out.println("Names:"+promoSubCat);
		if(promoSubCat.contains("DEALS")&&promoSubCat.contains("PRESIDENT'S PICKS")&&promoSubCat.contains("TOP-RATED")&& promoSubCat.contains("IDEAS")&& promoSubCat.contains("COUPONS"))
		{
			System.out.println("desired elements found");
		}
		else
		{
			System.out.println("desired elements not found");
		}

		//check if all options under Deals are available or not.[Testcase#12]
		uiDriver.DrawHighlight("lnkdeals", webDriver);
		uiDriver.click("lnkdeals");
		//click on Deals promotions link
		String DealsLst=uiDriver.getwebDriverLocator(objMap.getLocator("lnksubDeals")).getText();
		System.out.println(DealsLst);
		if(DealsLst.contains("Grocery") || DealsLst.contains("Frozen") || DealsLst.contains("Coupons")|| DealsLst.contains("Top-Rated")|| DealsLst.contains("President's Picks")|| DealsLst.contains("Dairy"))
		{
			System.out.println("desired elements found");
		}
		else
		{
			System.out.println("desired elements not found");
		}
		//select sub category from Deals page
		//		uiDriver.selenium.waitForPageToLoad("5000");
		webDriver.findElement(By.linkText(input.get("SelCat"))).click();
		boolean present=true;
		try {
			do{
				SleepUtils.getInstance().sleep(TimeSlab.YIELD);
			}
			while(!(webDriver.getTitle().contains(input.get("SelCat"))));
			present = true;
		}
		catch (NoSuchElementException e1) {
			present = false;
		}
		if(present=false)
		{
			RESULT.failed("could not reach product page", "Should go to product page", "Page could not reach to product page");
		}
		else{
			uiDriver.isDisplayed("strsubCatPage");
			uiDriver.isDisplayed("lstproduct1");
			uiDriver.DrawHighlight("lstproduct1", webDriver);
			uiDriver.FD_addToCart(input.get("Item"),
					input.get("orderflag_new_modify"), input.get("quantity"), input.get("click_hover"),input.get("FlexibilityFlag"),"","");
		}
	}

	//Verify Promotional offers relevance in the category defined in Sub global navigation header[TestCase#14]
	public void PromotionalCategories(DataRow input,DataRow output)throws InterruptedException, FindFailed {
		//President's pick
		uiDriver.DrawHighlight("lnkpresidentsPicks", webDriver);
		uiDriver.click("lnkpresidentsPicks");
		//verify if user is on correct page
		if(webDriver.getTitle().contains("President's Picks"))
		{
			RESULT.passed("President's Picks page", "User should be on President's Picks page", "User is on President's Picks page");
		}
		else
			RESULT.failed("President's Picks page", "User should be on President's Picks page", "User is not on President's Picks page");

		//Top Rated
		uiDriver.DrawHighlight("lnktopRated", webDriver);
		uiDriver.click("lnktopRated");
		boolean result=true;
		//verify if user is on correct page
		if(webDriver.getTitle().contains("Top-Rated"))
		{
			//For Fruit section in top rated page
			try{
				List<WebElement> ratings=webDriver.findElements(By.xpath(objMap.getLocator("lstratingFruit"))); //List of all products of fruit section
				System.out.println("size:"+ratings.size());
				for(int i=0;i<ratings.size()-1;i++)
				{
					System.out.println("Rate:"+ratings.get(i).findElement(By.className(objMap.getLocator("strtopRatedRate"))).getText());
					int rate = Integer.parseInt(ratings.get(i).findElement(By.className(objMap.getLocator("strtopRatedRate"))).getText());
					int rate1=Integer.parseInt(ratings.get(i+1).findElement(By.className(objMap.getLocator("strtopRatedRate"))).getText());

					System.out.println("Name:"+ratings.get(i).findElement(By.className(objMap.getLocator("lnktopRatedName"))).getText());
					System.out.println(ratings.get(i+1).findElement(By.className(objMap.getLocator("lnktopRatedName"))).getText());
					//verify if displayed products are sorted by its rating
					if(rate < rate1)
						result=false;
				}
				if(!(result))
					RESULT.failed("Top Rated page Fruit section", "User should be on Top Rated page and products should be sorted by ratings", 
					"User is not on Top Rated page or products are not sorted by ratings");

				else
					RESULT.passed("Top Rated page Fruit section", "User should be on Top Rated page and products should be sorted by ratings", 
					"User is on Top Rated page and products are sorted by ratings");
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}		
			//For vegetable section in top rated page

			try{
				result=true;
				List<WebElement> ratings=webDriver.findElements(By.xpath(objMap.getLocator("lstratingVegetbl"))); //List of all products of vegetable section
				System.out.println("size:"+ratings.size());
				for(int i=0;i<ratings.size()-1;i++)
				{
					System.out.println("Rate:"+ratings.get(i).findElement(By.className(objMap.getLocator("strtopRatedRate"))).getText()); //rating of Item
					int rate = Integer.parseInt(ratings.get(i).findElement(By.className(objMap.getLocator("strtopRatedRate"))).getText()); 
					int rate1=Integer.parseInt(ratings.get(i+1).findElement(By.className(objMap.getLocator("strtopRatedRate"))).getText()); 

					System.out.println("Name:"+ratings.get(i).findElement(By.className(objMap.getLocator("lnktopRatedName"))).getText()); //name of Item(i)
					System.out.println(ratings.get(i+1).findElement(By.className(objMap.getLocator("lnktopRatedName"))).getText()); //name of Item(i+1)
					//verify if displayed products are sorted by its rating
					if(rate < rate1)
						result=false;
				}
				if(!(result))
					RESULT.failed("Top Rated page Vegetable section", "User should be on Top Rated page and products should be sorted by ratings", 
					"User is not on Top Rated page or products are not sorted by ratings");

				else
					RESULT.passed("Top Rated page Vegetable section", "User should be on Top Rated page and products should be sorted by ratings", 
					"User is on Top Rated page and products are sorted by ratings");
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}		
		}
		else
			RESULT.failed("Top Rated page", "User should be on Top Rated page", "User is not on Top Rated page");

		//New Product
		uiDriver.click("imghomePage");
		uiDriver.DrawHighlight("lnknew", webDriver);
		uiDriver.click("lnknew");
		//verify if user is on correct page
		if(webDriver.getTitle().contains("New"))
		{
			RESULT.passed("New Products page", "User should be on New Products page", "User is on New Products page");
		}
		else
			RESULT.failed("New Products page", "User should be on New Products page", "User is not on New Products page");

		//Local
		uiDriver.click("imghomePage");
		uiDriver.DrawHighlight("lnklocal", webDriver);
		uiDriver.click("lnklocal");
		//verify if user is on correct page
		if(webDriver.getTitle().contains("Local"))
		{
			RESULT.passed("Local page", "User should be on Local page", "User is on Local page");
		}
		else
			RESULT.failed("Local page", "User should be on Local page", "User is not on Local page");
	}

	//verify more shop and save option available or not[TestCase#15]
	public void MoreWaysToShopAndSave(DataRow input,DataRow output)throws InterruptedException, FindFailed {
		try{
			
			if(uiDriver.getwebDriverLocator(objMap.getLocator("imgshopAndSave")).isDisplayed())
			{
				
				WebElement learnMore=uiDriver.getwebDriverLocator(objMap.getLocator("lnkshopAndSaveElements"));

				if(learnMore.isDisplayed())
				{
					RESULT.passed("More shop and save", "More shop and save options should be available", "More shop and save options available");
					String checkOptn=learnMore.getAttribute("href");
					if(checkOptn.contains("ebt"))
					{
						learnMore.click();
						if(uiDriver.isElementPresent(By.xpath(objMap.getLocator("strlearnMoreEBTPilot"))))
						{
							if(uiDriver.getwebDriverLocator(objMap.getLocator("strlearnMoreEBTPilot")).getText().
									contains("Details About the FreshDirect EBT Pilot"))
								RESULT.passed("EBT card Learn more", "User should be on EBT pilot program options learn more page",
								"User is on EBT pilot program options learn more page");
							else
								RESULT.failed("EBT card Learn more", "User should be on EBT pilot program options learn more page",
								"User is not on EBT pilot program options learn more page");
						}
					}
					else if(checkOptn.contains("your_account"))
					{
						learnMore.click();
						if(webDriver.getCurrentUrl().contains("your_account"))
							RESULT.passed("Refer a friend option", "User should be on share and earn brownie points page",
							"User is on share and earn brownie points page");
						else
							RESULT.passed("Refer a friend option", "User should be on share and earn brownie points page",
							"User is not on share and earn brownie points page");
					}
					else
					{
						RESULT.failed("Learn more link", "Learn more link should be available for EBT or refer a friend option", "Learn more link available for another option");
					}

				}
				else
					RESULT.failed("More shop and save Learn more", "More shop and save options should be available with learn more link", "More shop and save options with learn more link not available");
			}
			else
				RESULT.failed("More shop and save", "More shop and save options should be available", "More shop and save link not available");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	
	//Go to coupons and add product from any of available sub category[TestCase#16,17]
	@SuppressWarnings("deprecation")
	public void PromotionsCoupons(DataRow input,DataRow output)throws InterruptedException, FindFailed, AWTException{
		Configuration GCONFIG = Harness.GCONFIG;
		RobotPowered robot=new RobotPowered(webDriver);
		try{
			uiDriver.click("lnkcoupons");
			try{
				wait.until(ExpectedConditions.presenceOfElementLocated(By.className(objMap.getLocator("lnkfirstItem"))));
			}
			catch(Exception e)
			{
				RESULT.failed("No product in coupon section", "There should be atleast one product available in coupon section",
				"No product available in coupon section");
				return;
			}
			SleepUtils.getInstance().sleep(TimeSlab.YIELD);
			String verifCoupnPage=uiDriver.getwebDriverLocator(objMap.getLocator("imgcouponPage")).getAttribute("alt");
			if(verifCoupnPage.contains("FDCoupons"))
			{
				RESULT.passed("Coupons page", "User should be redirected to coupons page", "User is on coupons page");
			}
			else
			{
				RESULT.failed("Coupons page", "User should be redirected to coupons page", "User is not on coupons page");
				return;
			}
		}	
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	public void VerifyCouponApplied(DataRow input,DataRow output)throws InterruptedException, FindFailed{
		//go to view cart page and check if coupon has been applied or not for added item
		//check if check box for application of coupon is checked or not
		//		try{
		if(webDriver.findElements(By.className(objMap.getLocator("chkcouponApply"))).size()>0)
		{
			//for product added to cart by hover
			if(webDriver.findElements(By.className(objMap.getLocator("chkcouponApply"))).size()>1)
			{
				try{
					actions.moveToElement(webDriver.findElement(By.partialLinkText(uiDriver.productFullName)));
					if(webDriver.findElement(By.className(objMap.getLocator("popupProduct"))).findElement(By.className(objMap.getLocator("chkcouponApply"))).getAttribute("checked").equalsIgnoreCase("true"))
					{
						RESULT.passed("Coupon apply check box", "Checkbox should be checked to apply coupon", 
						"Checkbox is checked to apply coupon");
					}				
					else
					{	RESULT.failed("Coupon apply check box", "Checkbox should be checked to apply coupon", 
					"Coupon Webelement is not available");
					return;
					}
				}catch (Exception e) {
					try{
						//To check coupons check box
						webDriver.findElement(By.className(objMap.getLocator("popupProduct"))).findElement(By.className(objMap.getLocator("chkcouponApply"))).sendKeys(" ");
						SleepUtils.getInstance().sleep(TimeSlab.YIELD);
						if(webDriver.findElement(By.className(objMap.getLocator("popupProduct"))).findElement(By.className(objMap.getLocator("chkcouponApply"))).getAttribute("checked").equalsIgnoreCase("true"))
						{
							RESULT.passed("Coupon apply check box", "Checkbox should be checked to apply coupon", 
							"Checkbox is checked to apply coupon");
						}
						else
						{
							RESULT.failed("Coupon apply check box", "Checkbox should be checked to apply coupon", 
							"Checkbox is NOT checked to apply coupon");
							return;
						}
					}catch (Exception e1) {
						RESULT.failed("Coupon apply check box", "Checkbox should be checked to apply coupon", 
						"Checkbox is not checked to apply coupon");
						e1.printStackTrace();
						return;
					}
				}
			}//for product added to cart by click
			else{
				try{	
					if(uiDriver.getwebDriverLocator(objMap.getLocator("chkcouponApply")).getAttribute("checked").equalsIgnoreCase("true"))
					{
						RESULT.passed("Coupon apply check box", "Checkbox should be checked to apply coupon", 
						"Checkbox is checked to apply coupon");
						if(uiDriver.getwebDriverLocator(objMap.getLocator("chkcouponApply")).isEnabled())
						{
							RESULT.failed("Coupon apply check box Uncheck", "Checkbox should be disabled to uncheck", 
									"Coupon check box is disabled to uncheck");
									return;
						}
						else
						{
							RESULT.passed("Coupon apply check box Uncheck", "Checkbox should be disabled to uncheck", 
							"Coupon check box is enabled to uncheck");
						}
//						
					}				
					else
					{	RESULT.failed("Coupon apply check box", "Checkbox should be checked to apply coupon", 
					"Coupon Webelement is not available");
					return;
					}
				}catch(Exception e)
				{
					if(webDriver.findElements(By.className(objMap.getLocator("chkcouponApply"))).size()>0)
					{
						//uiDriver.robot.moveToElement(uiDriver.getwebDriverLocator(objMap.getLocator("lnkfirstItem")));
						SleepUtils.getInstance().sleep(TimeSlab.YIELD);
						try{
							//To check coupons check box
							webDriver.findElement(By.className(objMap.getLocator("chkcouponApply"))).sendKeys(" ");
							SleepUtils.getInstance().sleep(TimeSlab.YIELD);
							if(uiDriver.getwebDriverLocator(objMap.getLocator("chkcouponApply")).getAttribute("checked").equalsIgnoreCase("true"))
							{
								RESULT.passed("Coupon apply check box", "Checkbox should be checked to apply coupon", 
								"Checkbox is checked to apply coupon");
								if(uiDriver.getwebDriverLocator(objMap.getLocator("chkcouponApply")).isEnabled())
								{
									RESULT.failed("Coupon apply check box Uncheck", "Checkbox should be disabled to uncheck", 
											"Coupon check box is enabled to uncheck");
											return;
								}
								else
								{
									RESULT.passed("Coupon apply check box Uncheck", "Checkbox should be disabled to uncheck", 
									"Coupon check box is disabled to uncheck");
								}
//								
							}
							else
							{
								RESULT.failed("Coupon apply check box", "Checkbox should be checked to apply coupon", 
								"Checkbox is NOT checked to apply coupon");
								return;
							}
						}catch (Exception e1) {
							RESULT.failed("Coupon apply check box", "Checkbox should be checked to apply coupon", 
							"Checkbox is not checked to apply coupon");
							e1.printStackTrace();
							return;
						}
					}
					e.printStackTrace();
				}
			}
		}
		else
		{	RESULT.failed("Coupon apply check box", "Checkbox should be checked to apply coupon", 
		"Check box for coupon is not available");
		return;
		}
		System.out.println(uiDriver.productFullName);

		//check for "coupon applied" text on your card description page
		uiDriver.click("btnyourCart");
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(objMap.getLocator("btncheckOut"))));
		SleepUtils.getInstance().sleep(TimeSlab.YIELD);

		try{
			List<WebElement> li = webDriver.findElements(By.xpath(objMap.getLocator("lstverifyProducts")));
			// Iterating through all the list elements
			for (WebElement we : li)
			{
				if (we.findElement(By.className(objMap.getLocator("lnkitemName"))).getText().contains(uiDriver.productFullName))
				{
					if(we.findElements(By.className(objMap.getLocator("strcouponState"))).size()>0){
						if(we.findElement(By.className(objMap.getLocator("strcouponState"))).getText().contains("coupon applied"))
						{
							RESULT.passed("Coupon applied in cart page", "Coupon applied text should be present for particular Item",
									"Coupon applied text is present for Item:"+uiDriver.productFullName);
							//calculate price from quantity and unit price and match with displayed one
							String priceEach=we.findElement(By.className(objMap.getLocator("strunitPrice"))).getText();
							String quantity=we.findElement(By.className(objMap.getLocator("lstqntyNew"))).getAttribute("value");
							String price = we.findElement(By.className(objMap.getLocator("lstpriceNew"))).getText();
							if(priceEach.contains("ea"))
							{
								String PriceEach[]=priceEach.substring(1).split("/");
								double pricematch=Double.parseDouble(PriceEach[0]) * Double.parseDouble(quantity);
								if(we.findElement(By.className(objMap.getLocator("strsaveDollar"))).getText().contains("for"))
								{
									RESULT.warning("Price Match for coupon product", "Discount should be for single product", "This product is having group discount");
								}
								else{
									String discount[]=we.findElement(By.className(objMap.getLocator("strsaveDollar"))).getText().split("\\$");
									if((Double.parseDouble(String.format("%.2f", pricematch))- Double.parseDouble(discount[1]))==Double.parseDouble(price.substring(1)))
									{
										RESULT.passed("Price Match for coupon product", "For coupon product,Price: "+String.format("%.2f", pricematch)+" should be discounted to:"+price.substring(1), 
												"Price for Coupon product:"+price.substring(1) +" is discounted");
									}
									else {
										RESULT.failed("Price Match for coupon product", "For coupon product,Price: "+String.format("%.2f", pricematch)+" should be discounted to:"+price.substring(1), 
												"Price for Coupon product:"+price.substring(1) +" is not discounted");
									}
								}
							}
							else
							{
								RESULT.warning("Price Match for coupon product", "Unit price should be in each unit per item", "Unit price is in diff unit: "+priceEach.split("/")[1]);
							}
							break;
						}
						else if(we.findElement(By.className(objMap.getLocator("strcouponState"))).getText().contains("min qty not met"))
						{
							RESULT.failed("Coupon applied in cart page", "Coupon applied text should be present for particular Item",
									"Min quantity not met text is present for Item:"+uiDriver.productFullName);
							return;
						}
						else
						{
							RESULT.failed("Coupon applied in cart page", "Coupon applied text should be present for particular Item",
									"Coupon applied text is not present for Item:"+uiDriver.productFullName);
							return;
						}
					}
					else
						RESULT.warning("Coupon applied in cart page", "Coupon applied text should be present for particular Item",
						"coupon product found but coupon status not available");
				}
			}
		}
		catch(Exception e)
		{
			RESULT.failed("Coupon applied in cart page", "Coupon applied text should be present for particular Item",
			"Coupon not available for product");
			e.printStackTrace();
			return;
		}
	}
	//		}
	public void VerifyCart(DataRow input,DataRow output)throws InterruptedException, FindFailed{
	
		System.out.println(uiDriver.productFullName);
		uiDriver.click("btnyourCart");
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(objMap.getLocator("btncheckOut"))));
		SleepUtils.getInstance().sleep(TimeSlab.YIELD);
		try {
			// paste the product name as it is with formatting from the web page
			String product = webDriver.findElement(By.partialLinkText(uiDriver.productFullName)).getText();
			RESULT.passed("Coupon product after logging in again", "Coupon product should be available in cart after logging in again", 
					"Coupon product:"+ product+" is available in cart after logging in again");
		} 
		catch(Exception e)
		{
			RESULT.failed("Coupon product after logging in again", "Coupon product:"+uiDriver.productFullName +"should be available in cart after logging in again", 
					"Coupon product:"+uiDriver.productFullName +"is not available in cart after logging in again");
			e.printStackTrace();
			return;
		}
	}
}