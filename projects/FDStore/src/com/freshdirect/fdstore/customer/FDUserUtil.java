package com.freshdirect.fdstore.customer;

import com.freshdirect.common.context.UserContext;
import com.freshdirect.customer.ErpDeliveryPlantInfoModel;
import com.freshdirect.fdstore.FDDeliveryManager;
import com.freshdirect.fdstore.FDResourceException;

public class FDUserUtil {
	
	public static boolean isAlcoholRestricted(String zipCode) throws FDResourceException {
		String county = FDDeliveryManager.getInstance().lookupCountyByZip(zipCode);
		 String state = FDDeliveryManager.getInstance().lookupStateByZip(zipCode);
		return  FDDeliveryManager.getInstance().checkForAlcoholDelivery(state, county, zipCode);
	}
	
	public static ErpDeliveryPlantInfoModel getDeliveryPlantInfo(FDUserI user) {
		
		UserContext ctx=user.getUserContext();
		ErpDeliveryPlantInfoModel delPlantInfo=new ErpDeliveryPlantInfoModel();
		delPlantInfo.setPlantId(ctx.getFulfillmentContext().getPlantId());
		delPlantInfo.setSalesOrg(ctx.getPricingContext().getZoneInfo().getSalesOrg());
		delPlantInfo.setDistChannel(ctx.getPricingContext().getZoneInfo().getDistributionChanel());
		return delPlantInfo;
	}

}
