package com.freshdirect.fdstore.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.freshdirect.customer.ErpAddressModel;
import com.freshdirect.customer.ErpCustomerInfoModel;
import com.freshdirect.delivery.DlvZoneInfoModel;
import com.freshdirect.delivery.restriction.EnumDlvRestrictionReason;
import com.freshdirect.fdstore.FDDeliveryManager;
import com.freshdirect.fdstore.FDInvalidAddressException;
import com.freshdirect.fdstore.FDReservation;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.fdstore.customer.FDCustomerFactory;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.promotion.FDPromotionZoneRulesEngine;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.util.NVL;

public class AddressFinder {

	public static ErpAddressModel getShipToAddress(FDUserI user,String addressId, TimeslotContext timeslotCtx) throws FDResourceException{
		
		Collection shipToAddresses = new ArrayList();
		ErpAddressModel address = null;
		if(timeslotCtx.equals(TimeslotContext.CHECK_AVAILABLE_TIMESLOTS)||timeslotCtx.equals(TimeslotContext.CHECK_AVAL_SLOTS_CRM)||timeslotCtx.equals(TimeslotContext.RESERVE_TIMESLOTS)||timeslotCtx.equals(TimeslotContext.RESERVE_TIMESLOTS_CRM)){
			if(user.isHomeUser()){
				shipToAddresses=FDCustomerManager.getShipToAddresses(user.getIdentity());
				//check whether Customer having more than one address else get from shoppingCart address
				if(shipToAddresses.size() > 1){
					if(addressId==null){
						addressId = FDCustomerManager.getDefaultShipToAddressPK(user.getIdentity()); // default address
					}
					for (Iterator i=shipToAddresses.iterator(); i.hasNext(); ) {
						ErpAddressModel a = (ErpAddressModel)i.next();
						if ( a.getPK().getId().equals(addressId) ) {
							address = a;
							break;
						} 
					}
					if (address == null) {
						// no address found (no default) -> pick the first one
						address = (ErpAddressModel)shipToAddresses.iterator().next();
						addressId = address.getPK().getId();
					}
				}else{
					if(user.getShoppingCart()!=null){
						address=user.getShoppingCart().getDeliveryAddress();
					}
					
					if (address == null) {
						// no address found (no default) -> pick the first one
						address = (ErpAddressModel)shipToAddresses.iterator().next();
						addressId = address.getPK().getId();
					}
				}
			}
		}
		
		if(timeslotCtx.equals(TimeslotContext.CHECKOUT_TIMESLOTS)){
			if(user.getShoppingCart()!=null){
				address=user.getShoppingCart().getDeliveryAddress();
			}
		}
		
	  return address;
			
	}//end getDeliveryAddresses		
	
	public static Collection<ErpAddressModel> getShipToAddresses(FDUserI user) throws FDResourceException {
	
		return FDCustomerManager.getShipToAddresses(user.getIdentity());

	}

	//get amount for zone promotion
	public static double getZonePromoAmount(FDUserI user, ErpAddressModel erpAddress, TimeslotContext timeslotCtx) throws FDResourceException {
		DlvZoneInfoModel zInfo = null;
		double zonePromoAmount=0.0;
		if(erpAddress!=null){
			try {
				zInfo = FDDeliveryManager.getInstance().getZoneInfo(erpAddress, new java.util.Date());
			} catch (FDInvalidAddressException e) {
				e.printStackTrace();
			}    
			zonePromoAmount = FDPromotionZoneRulesEngine.getDiscount(user,zInfo.getZoneCode());
		}
		return zonePromoAmount;
		
	}
	
	public static void getApplicableRestrictions(HttpServletRequest request, FDCartModel cart){
		boolean thxgivingRestriction = false;
		boolean easterRestriction = false;
		boolean easterMealRestriction = false; //easter meals
		boolean valentineRestriction = false;
		boolean kosherRestriction = false;
		boolean alcoholRestriction = false;
	    boolean thxgiving_meal_Restriction=false;
	   
		for(Iterator i = cart.getApplicableRestrictions().iterator(); i.hasNext(); ){
			EnumDlvRestrictionReason reason = (EnumDlvRestrictionReason) i.next();
			if(EnumDlvRestrictionReason.THANKSGIVING.equals(reason)){
				thxgivingRestriction = true;
				continue;
			}
	        if(EnumDlvRestrictionReason.THANKSGIVING_MEALS.equals(reason)){
	           thxgiving_meal_Restriction=true;
	           continue;
	        }
			//easter
	        if(EnumDlvRestrictionReason.EASTER.equals(reason)){
	           easterRestriction=true;
	           continue;
	        }
			//easter meals
	        if(EnumDlvRestrictionReason.EASTER_MEALS.equals(reason)){
	           easterMealRestriction=true;
	           continue;
	        }
			if(EnumDlvRestrictionReason.ALCOHOL.equals(reason)){
				alcoholRestriction = true;
				continue;
			}
			if(EnumDlvRestrictionReason.KOSHER.equals(reason)){
				kosherRestriction = true;
				continue;
			}
			if(EnumDlvRestrictionReason.VALENTINES.equals(reason)){
				valentineRestriction = true;
				continue;
			}
		}
		request.setAttribute("thxgivingRestriction", thxgivingRestriction);
		request.setAttribute("thxgiving_meal_Restriction", thxgiving_meal_Restriction);
		request.setAttribute("easterRestriction", easterRestriction);
		request.setAttribute("easterMealRestriction", easterMealRestriction);
		request.setAttribute("alcoholRestriction", alcoholRestriction);
		request.setAttribute("kosherRestriction", kosherRestriction);
		request.setAttribute("valentineRestriction", valentineRestriction);		
		
	}
		
}//End class
