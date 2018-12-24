package com.freshdirect.delivery;

import com.freshdirect.common.pricing.Discount;
import com.freshdirect.customer.EnumChargeType;
import com.freshdirect.customer.ErpAbstractOrderModel;
import com.freshdirect.customer.ErpChargeLineModel;

public class Util {
		
	public static boolean isDlvChargeWaived(ErpAbstractOrderModel order)
	{
		ErpChargeLineModel dlvCharge;
		dlvCharge = order.getCharge(EnumChargeType.DELIVERY);
		if(dlvCharge != null)
		{
			Discount discount = dlvCharge.getDiscount();
			if(discount!=null && discount.getAmount()>0)
				return true;
		}
		
		return false;
			
	}
	
}
