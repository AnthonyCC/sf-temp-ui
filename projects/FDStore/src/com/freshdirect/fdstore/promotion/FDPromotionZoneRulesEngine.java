package com.freshdirect.fdstore.promotion;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Category;

import com.freshdirect.customer.EnumTransactionSource;
import com.freshdirect.fdstore.FDReservation;
import com.freshdirect.fdstore.FDTimeslot;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.customer.adapter.PromotionContextAdapter;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.rules.Rule;
import com.freshdirect.rules.RulesEngineI;
import com.freshdirect.rules.RulesRegistry;
import com.freshdirect.rules.ZoneCondition;

/**
 * @author knadeem Date Jun 1, 2005
 */
public class FDPromotionZoneRulesEngine implements Serializable {
	
	private static Category LOGGER = LoggerFactory.getInstance(FDPromotionZoneRulesEngine.class);
	private static SimpleDateFormat format=new SimpleDateFormat("EEE");
	private static DecimalFormat dFormat=new DecimalFormat("#.00");
	
	public static boolean isEligible(PromotionContextI ctx)
	{
		//deny windows steering to iPhone users
		boolean result=true;
		FDUserI user=ctx.getUser();
		System.out.println("user.getApplication()"+user.getApplication());
		if(EnumTransactionSource.IPHONE_WEBSITE.equals(user.getApplication()))
		{
			result=false;
		}
		return result;
	}
	
	public static List getEligiblePromotions(PromotionContextI ctx) {
		RulesEngineI ruleEngine=getRulesEngine();
		if(ruleEngine==null)return Collections.EMPTY_LIST;
		
		Map firedRules = ruleEngine.evaluateRules(ctx);
		Map rules=ruleEngine.getRules();
		if(firedRules.isEmpty()) {
			return Collections.EMPTY_LIST;
		}
		
		List promoCodes = new ArrayList();
		for(Iterator i = firedRules.values().iterator(); i.hasNext(); ){
			Rule r = (Rule) i.next();
			if(r.validate()) {
				promoCodes.add(r);
			}
		}
		
		return promoCodes;
	}	
	
	private static RulesEngineI getRulesEngine() {
		return RulesRegistry.getRulesEngine("ZONE-PROMOTION");
	}
	
	
   public static String getPromoCode(PromotionContextI ctx)
   {	   
	    String promo=null;
	    try
	    {
	    List rules= getEligiblePromotions(ctx);
	    FDUserI user=ctx.getUser();
   		FDCartModel cart = user.getShoppingCart();
		FDReservation reservation = cart.getDeliveryReservation();
		if(reservation!=null)
		{	
			FDTimeslot timeSlot=reservation.getTimeslot();
		    for(int i=0,n=rules.size();i<n;i++)
		    {
		    	Rule r=(Rule)rules.get(i);
		        List c=r.getConditions();
		        if(c!=null&&c.size()>0)
		        {
		        	ZoneCondition con=(ZoneCondition)c.get(0);
		       
		        	if(con.getZones()!=null&&con.getZones().contains(timeSlot.getZoneCode())
			    	   &&con.getStartTimeDay()!=null&&con.getStartTimeDay().equals(timeSlot.getDlvTimeslot().getStartTime())
			    	   &&con.getEndTimeDay()!=null&&con.getEndTimeDay().equals(timeSlot.getDlvTimeslot().getEndTime())
			    	   &&con.getDay()!=null&&con.getDay().equalsIgnoreCase(format.format(timeSlot.getBaseDate()))
			    	  )
			    	{
			    		promo=(String)r.getOutcome();
			    		break;
			    	}
		        }
		    }
		}
	   }catch(Exception e)
	   {
		   e.printStackTrace();
	   }
		return promo;
   }
   
   public static double getDiscount(FDUserI user,FDTimeslot timeSlot)
   {
	   double result=0;
	   try
	   {
	    PromotionContextI ctx=new PromotionContextAdapter(user);
	    List rules= getEligiblePromotions(ctx);	
		if(timeSlot!=null)
		{				
		    for(int i=0,n=rules.size();i<n;i++)
		    {
		    	Rule r=(Rule)rules.get(i);
		        List c=r.getConditions();
		        if(c!=null&&c.size()>0)
		        {
		        	ZoneCondition con=(ZoneCondition)c.get(0);
		       
			    	if(con.getZones()!=null&&con.getZones().contains(timeSlot.getZoneCode())
			    	   &&con.getStartTimeDay()!=null&&con.getStartTimeDay().equals(timeSlot.getDlvTimeslot().getStartTime())
			    	   &&con.getEndTimeDay()!=null&&con.getEndTimeDay().equals(timeSlot.getDlvTimeslot().getEndTime())
			    	   &&con.getDay()!=null&&con.getDay().equalsIgnoreCase(format.format(timeSlot.getBaseDate()))
			    	  )
			    	{
			    		String promoCode=(String)r.getOutcome();
			    		PromotionI promo = PromotionFactory.getInstance().getPromotion(promoCode);
			    		result=promo.getHeaderDiscountTotal();
			    		break;
			    	}
		        }
		    }
		}
	   }catch(Exception e)
	   {
		   e.printStackTrace();
	   }
		return result;
   }
   
   public static double getDiscount(FDUserI user,String zoneCode)
   {
	   double result=0;
	   try
	   {
	    PromotionContextI ctx=new PromotionContextAdapter(user);
	    List rules= getEligiblePromotions(ctx);	
		if(zoneCode!=null)
		{				
		    for(int i=0,n=rules.size();i<n;i++)
		    {
		    	Rule r=(Rule)rules.get(i);
		        List c=r.getConditions();
		        if(c!=null&&c.size()>0)
		        {
		        	ZoneCondition con=(ZoneCondition)c.get(0);
		       
			    	if(con.getZones()!=null&&con.getZones().contains(zoneCode)			    	   
			    	  )
			    	{
			    		String promoCode=(String)r.getOutcome();
			    		PromotionI promo = PromotionFactory.getInstance().getPromotion(promoCode);
			    		result=promo.getHeaderDiscountTotal();
			    		break;
			    	}
		        }
		    }
		}
	   }catch(Exception e)
	   {
		   e.printStackTrace();
	   }
		return result;
   }
   
   public static String getDiscountFormatted(double amount)
   {
	   String result=null;
	   try
	   {
		   
		   result=dFormat.format(amount);
	   }catch(Exception e)
	   {
		   
	   }
	   return result;
   }
}
