package com.freshdirect.fdstore.promotion;

import com.freshdirect.common.customer.EnumServiceType;
import com.freshdirect.customer.EnumChargeType;
import com.freshdirect.customer.ErpAddressModel;
import com.freshdirect.customer.ErpDepotAddressModel;
import com.freshdirect.deliverypass.DlvPassConstants;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.fdstore.customer.FDUserI;

/**
 * Ensures delivery charge is waived if the CSR requests it,
 * or a particular depot has the "waive dlv charge" flag set.  
 */
public class WaiveDeliveryCharge {

	private final static String PROMO_CODE = "DELIVERY";

	public static boolean apply(FDUserI user) {
		FDCartModel cart = user.getShoppingCart();
		if (cart.isCsrWaivedDeliveryCharge()) {
			waiveCharge(user, PROMO_CODE);
			//If a delivery pass is applied, revoke it back.
			cart.setDlvPassApplied(false);
			return true;
		}
		//Make sure the dlv pass is not applied if the service type is not HOME.
		if(cart.isDlvPassApplied()) {
				if(user.getSelectedServiceType() == EnumServiceType.HOME) {
					waiveCharge(user, DlvPassConstants.PROMO_CODE);
				}else{
					//Else if coporate delivery revoke the delivery pass.
					cart.setDlvPassApplied(false);
					revokeWaiveCharge(user, DlvPassConstants.PROMO_CODE);
				}
		}
		
		ErpAddressModel address = user.getShoppingCart().getDeliveryAddress();
		if (address != null && address instanceof ErpDepotAddressModel) {
			ErpDepotAddressModel depot = (ErpDepotAddressModel) address;
			if (depot.isDeliveryChargeWaived()){
				waiveCharge(user,PROMO_CODE);
				return true;
			}
		}
		
		return false;
	}

	private static void waiveCharge(FDUserI user, String promoCode) {
		user.getShoppingCart().setChargeWaived(EnumChargeType.DELIVERY, true, promoCode);
	}

	private static void revokeWaiveCharge(FDUserI user, String promoCode) {
		user.getShoppingCart().setChargeWaived(EnumChargeType.DELIVERY, false, promoCode);
	}
}
