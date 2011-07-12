package com.freshdirect.fdstore;

import com.freshdirect.common.pricing.Discount;
import com.freshdirect.customer.EnumChargeType;
import com.freshdirect.customer.ErpAbstractOrderModel;
import com.freshdirect.customer.ErpChargeLineModel;
import com.freshdirect.delivery.model.DlvZoneModel;

public class Util {

	public static boolean isZoneCtActive(String zoneId) 
	{
		DlvZoneModel dlvZoneModel = null;
		try 
		{
			if(zoneId!=null)
				dlvZoneModel = FDDeliveryManager.getInstance().findZoneById(zoneId);
		} 
		catch (FDResourceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (FDZoneNotFoundException e) {
			e.printStackTrace();
		}
		return (dlvZoneModel == null)?false:dlvZoneModel.isCtActive();
		
	}
	
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
