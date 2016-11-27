package com.freshdirect.fdstore.customer;

import com.freshdirect.common.context.UserContext;
import com.freshdirect.common.pricing.CatalogKey;
import com.freshdirect.customer.ErpDeliveryPlantInfoModel;
import com.freshdirect.fdstore.EnumEStoreId;
import com.freshdirect.fdstore.FDDeliveryManager;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;

public class FDUserUtil {
	
	public static boolean isAlcoholRestricted(String zipCode) throws FDResourceException {
		String county = FDDeliveryManager.getInstance().lookupCountyByZip(zipCode);
		 String state = FDDeliveryManager.getInstance().lookupStateByZip(zipCode);
		return  FDDeliveryManager.getInstance().checkForAlcoholDelivery(state, county, zipCode);
	}
	
	public static ErpDeliveryPlantInfoModel getDeliveryPlantInfo(FDUserI user) {
		
		UserContext ctx=user.getUserContext();
		ErpDeliveryPlantInfoModel delPlantInfo=getDeliveryPlantInfo(ctx);
		return delPlantInfo;
	}
	
	public static ErpDeliveryPlantInfoModel getDeliveryPlantInfo(UserContext userContext) {
		ErpDeliveryPlantInfoModel delPlantInfo=new ErpDeliveryPlantInfoModel();
		delPlantInfo.setPlantId(userContext.getFulfillmentContext().getPlantId());
		delPlantInfo.setSalesOrg(userContext.getPricingContext().getZoneInfo().getSalesOrg());
		delPlantInfo.setDistChannel(userContext.getPricingContext().getZoneInfo().getDistributionChanel());
		CatalogKey catalogKey = new CatalogKey(userContext.getStoreContext().getEStoreId().name(),Long.parseLong(userContext.getFulfillmentContext().getPlantId()),userContext.getPricingContext().getZoneInfo());
		delPlantInfo.setCatalogKey(catalogKey);
		return delPlantInfo;
	}

	public static ErpDeliveryPlantInfoModel getDefaultDeliveryPlantInfo(EnumEStoreId eStore) {
		ErpDeliveryPlantInfoModel delPlantInfo=new ErpDeliveryPlantInfoModel();
		if(EnumEStoreId.FDX.equals(eStore)) {
			delPlantInfo.setPlantId(FDStoreProperties.getDefaultFdxPlantID());
			delPlantInfo.setSalesOrg(FDStoreProperties.getDefaultFdxSalesOrg());
			
		}
		else {
			delPlantInfo.setPlantId(FDStoreProperties.getDefaultFdPlantID());
			delPlantInfo.setSalesOrg(FDStoreProperties.getDefaultFdSalesOrg());
		}
		delPlantInfo.setDistChannel("01");
		delPlantInfo.setDivision("01");
		return delPlantInfo;
	}
	
	public static void main(String[] a) {
		EnumEStoreId eStore=null;
		ErpDeliveryPlantInfoModel delPlantInfo=FDUserUtil.getDefaultDeliveryPlantInfo(eStore);
		System.out.println(delPlantInfo);
		
		eStore=EnumEStoreId.FDX;
		delPlantInfo=FDUserUtil.getDefaultDeliveryPlantInfo(eStore);
		System.out.println(delPlantInfo);
	}
	
}
