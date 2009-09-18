package com.freshdirect.webapp.taglib.fdstore;


import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.webapp.taglib.AbstractGetterTag;

/**
 * This tag class contains logic for APPDEV-484 Targeted HomePage Messaging
 * @author asexton
 *
 */
public class GetSegmentMessageTag extends AbstractGetterTag {

	private FDUserI user;

	public void setUser(FDUserI user) {
		this.user = user;
	}

	protected Object getResult() throws Exception {
		
		SegmentMessage sm = getSegment(user.getMarketingPromo());

		return sm;
	}
	
	private boolean displayMessage() throws FDResourceException {
		Calendar aWeekAgoCal = Calendar.getInstance();
		aWeekAgoCal.add(Calendar.DAY_OF_MONTH, -6);
		
		Date aWeekAgo = aWeekAgoCal.getTime();
		Date lastOrderDelivered = user.getOrderHistory().getLastOrderDlvDate();
		return lastOrderDelivered.before(aWeekAgo);
	}
	
	private SegmentMessage getSegment(String marketingPromoValue) throws FDResourceException {
		
		// MarketingPromo value is in the form of "29_lapsedloy_notdporctc"
		if( null == marketingPromoValue || marketingPromoValue.length() < 2 )
			return null;
		
		// The first two chars of the String represent an int that uniquely identifies the MarketingPromo value (possible values:  3 through 37)
		String parsedValue = marketingPromoValue.substring(0, 2);
		
		if( !StringUtils.isNumeric(parsedValue)) {
			return null;
		}
		SegmentMessage sm = new SegmentMessage();
		
		sm.setMarketingPromoValue(marketingPromoValue);
		
		switch(Integer.parseInt(parsedValue)) {
		
			case 3:   // same as case 35  do not insert break statement here.
			
			case 35:  sm.setLocation1(true);
					  sm.setLocation2(false);
					  sm.setCenterMessage(true);
					  sm.setGreeting("Welcome back, ");
					  if(user.getPromotionHistory().getPromotionUsageCount("5FREEDEL", "") < 5) {
						  sm.setMessage("<b>Enjoy five deliveries fee-free!</b>  Enter code <b>5FREEDEL</b> at checkout.");
					  }
					  return sm;
					 
					 
					 
			case 15:  sm.setLocation1(true);
					  sm.setLocation2(false);
					  sm.setGreeting("Welcome back, ");
					  sm.setMessage("You're close to joining our Chef\'s Table rewards program!");
					  sm.setMessageLink("<a href=\"/your_account/manage_account.jsp\">Click here to learn more.</A>");
					  return sm;
					  
					  
					  
			case 13:  // same as case 30  do not insert break statement here.	  
					  
			case 30:  sm.setLocation1(true);
					  sm.setLocation2(false);
					  sm.setGreeting("Welcome back, ");
					  sm.setMessage("Thanks for being one of our best customers!");
					  sm.setMessageLink("<a href=\"/your_account/manage_account.jsp\">Click here for Chef\'s Table offers.</A>");
					  return sm;
		  
				      
				      
			case 11:  sm.setLocation1(true);
			  		  sm.setLocation2(false);
			  		  sm.setGreeting("Welcome back, ");
			  		  sm.setMessage("Enjoy your free Unlimited DeliveryPass trial!");
			  		  sm.setMessageLink("<a href=\"/your_account/delivery_pass.jsp\">Click here for details.</a>");
			  		  return sm;
			  		  
			  		  
			  		  
			case 32:  // same as case 33  do not insert break statement here.
				
			case 33:  sm.setLocation1(true);
	  		  	      sm.setLocation2(true);
	  		  	      if(displayMessage()) {
	  		  	    	  sm.setGreeting("We\'ve missed you, ");
	  		  	    	  sm.setMessage("Don\'t forget about your Unlimited Delivery Pass.");
	  		  	    	  sm.setMessageLink("<a href=\"/your_account/delivery_pass.jsp\">Click here for details.</a>");
	  		  	      } else {
	  	      			  sm.setGreeting("Welcome back, ");
	  	      			  if(user.isChefsTable()) {
    		  				sm.setMessage("Thanks for being one of our best customers!");
    		  				sm.setMessageLink("<a href=\"/your_account/manage_account.jsp\">Click here for Chef\'s Table offers.</A>");
    		  			  }
    		  			  else if(user.isDlvPassActive()) {
	  		  		  	  	sm.setMessage("Enjoy your Unlimited DeliveryPass membership!");
	  		  		  	  	sm.setMessageLink("<a href=\"/your_account/delivery_pass.jsp\">Click here for details.</a>");
    		  			  } 
	  	      		  }
	  		  	      return sm;
	  		  		  
	  		  		  
	  		
	  		/*  These cases have the same logic as case 29.  Do not insert unrelated cases in between this segment. */
			case 26:  // same as case 29  do not insert break statement here.
				
			case 27:  // same as case 29  do not insert break statement here.
				
			case 28:  // same as case 29  do not insert break statement here.
				
			case 29:  sm.setLocation1(true);
	  	      		  sm.setLocation2(true);
	  	      		  sm.setCenterMessage(true);	  	      		  
	  	      		  if(displayMessage()) {
	  	      			  sm.setGreeting("We\'ve missed you, ");
	  	      			  sm.setMessage("Thanks for being a great customer.");
	  	      		  } else {
	  	      			  sm.setGreeting("Welcome back, ");
	  	      			  if(user.isChefsTable()) {
    		  				sm.setMessage("Thanks for being one of our best customers!");
    		  				sm.setMessageLink("<a href=\"/your_account/manage_account.jsp\">Click here for Chef\'s Table offers.</A>");
    		  			  }
    		  			  else if(user.isDlvPassActive()) {
	  		  		  	  	sm.setMessage("Enjoy your Unlimited DeliveryPass membership!");
	  		  		  	  	sm.setMessageLink("<a href=\"/your_account/delivery_pass.jsp\">Click here for details.</a>");
    		  			  } 
	  	      		  }
	  	      	      return sm;
		  		      
	  	      	      
		    
			case 12:  // same as case 14  do not insert break statement here.
				
			case 14:  sm.setLocation1(true);
	  		  		  sm.setLocation2(false);
	  		  		  sm.setGreeting("Welcome back, ");
	  		  		  sm.setMessage("Enjoy your Unlimited DeliveryPass membership!");
	  		  		  sm.setMessageLink("<a href=\"/your_account/delivery_pass.jsp\">Click here for details.</a>");
	  		  		  return sm;
	  		  		  
	  		  		  
	  		  		  
			case 22:  // same as case 23  do not insert break statement here.
				
			case 23:  sm.setLocation1(true);
      		  		  sm.setLocation2(true);
      		  		  sm.setCenterMessage(true);
      		  		  if(displayMessage()) {
      		  			  sm.setGreeting("We\'ve missed you, ");
      		  			  sm.setMessage("Welcome back to FreshDirect.");
      		  		  } else {
      		  			sm.setGreeting("Welcome back, ");
      		  		  }
      		  		  return sm;
  		      
      		  		  
      		  
			case 18:  // same as case 19  do not insert break statement here.
				
			case 19:  sm.setLocation1(true);
			 		  sm.setLocation2(true);
			 		  sm.setCenterMessage(true);
			 		  sm.setGreeting("Welcome back, ");
			 		  if(displayMessage()) {
			 			  sm.setMessage("It\'s nice to see you again.");
			 		  }
			 		  return sm;
			 		  
			 		  
			 		  
			case 20:  // same as case 21  do not insert break statement here.
				
			case 21:  sm.setLocation1(true);
      		  		  sm.setLocation2(true);
      		  		  if(displayMessage()) {
      		  			  sm.setGreeting("We\'ve missed you, ");  
      		  			  sm.setMessage("Thanks for being one of our best customers.");
      		  		  } else {
      		  			  sm.setGreeting("Welcome back, ");
      		  			  if(user.isChefsTable()) {
      		  				sm.setMessage("Thanks for being one of our best customers!");
      		  				sm.setMessageLink("<a href=\"/your_account/manage_account.jsp\">Click here for Chef\'s Table offers.</A>");
      		  			  }
      		  			  else if(user.isDlvPassActive()) {
  	  		  		  	  	sm.setMessage("Enjoy your Unlimited DeliveryPass membership!");
  	  		  		  	  	sm.setMessageLink("<a href=\"/your_account/delivery_pass.jsp\">Click here for details.</a>");
      		  			  } 
      		  		  }
      		  		  return sm;
		}
		return null;
	}
	

	public static class TagEI extends AbstractGetterTag.TagEI {
		protected String getResultType() {
			return "com.freshdirect.webapp.taglib.fdstore.SegmentMessage";
		}
	}
}
