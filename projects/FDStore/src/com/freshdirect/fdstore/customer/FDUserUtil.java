package com.freshdirect.fdstore.customer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Category;

import com.freshdirect.common.context.FulfillmentContext;
import com.freshdirect.common.context.UserContext;
import com.freshdirect.common.pricing.CatalogKey;
import com.freshdirect.common.pricing.PricingContext;
import com.freshdirect.customer.EnumSaleType;
import com.freshdirect.customer.ErpDeliveryPlantInfoModel;
import com.freshdirect.customer.OrderHistoryI;
import com.freshdirect.fdstore.EnumEStoreId;
import com.freshdirect.fdstore.FDDeliveryManager;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.customer.adapter.FDOrderAdapter;
import com.freshdirect.framework.util.log.LoggerFactory;

public class FDUserUtil {
	private static Category		LOGGER	= LoggerFactory.getInstance( FDUserUtil.class );	
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
		if(null == userContext){
			LOGGER.warn(" UserContext is null ");
		}else { 
			if(null == userContext.getFulfillmentContext()){
				LOGGER.warn(" FulfillmentContext is null ");
			}
			if(null == userContext.getPricingContext()){
				LOGGER.warn(" PricingContext is null ");
			}
		}
		if(null !=userContext && null !=userContext.getFulfillmentContext() && null !=userContext.getPricingContext()){ //TODO: Populate it with default values.
			delPlantInfo.setPlantId(userContext.getFulfillmentContext().getPlantId());
			delPlantInfo.setSalesOrg(userContext.getPricingContext().getZoneInfo().getSalesOrg());
			delPlantInfo.setDistChannel(userContext.getPricingContext().getZoneInfo().getDistributionChanel());
			CatalogKey catalogKey = new CatalogKey(userContext.getStoreContext().getEStoreId().name(),Long.parseLong(userContext.getFulfillmentContext().getPlantId()),userContext.getPricingContext().getZoneInfo());
			delPlantInfo.setCatalogKey(catalogKey);
		}
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
	
	public static FDOrderAdapter getModifyingOrder(FDUserI user) {
		if (user.getShoppingCart() instanceof FDModifyCartModel) {
			FDModifyCartModel moCart = (FDModifyCartModel) user.getShoppingCart();
			return moCart.getOriginalOrder();
		}
		return null;
	}
	
	public static String getModifyingOrderId(FDUserI user) {
		if (user.getShoppingCart() instanceof FDModifyCartModel) {
			FDModifyCartModel moCart = (FDModifyCartModel) user.getShoppingCart();
			FDOrderAdapter order =  moCart.getOriginalOrder();
			return order == null? null : order.getErpSalesId();
		}
		return null;
	}
	
	public static List<FDOrderInfoI> getModifiableOrders(FDUserI user) {
		List<FDOrderInfoI> modifiableOrders = new ArrayList<FDOrderInfoI>();
		FDOrderHistory history;
		try {
			history = (FDOrderHistory) user.getOrderHistory();
			List<FDOrderInfoI> orderHistoryInfo = new ArrayList<FDOrderInfoI>(history.getFDOrderInfos(EnumSaleType.REGULAR, user.getUserContext().getStoreContext().getEStoreId()));

			for (Iterator<FDOrderInfoI> i = orderHistoryInfo.iterator(); i.hasNext();) {
				FDOrderInfoI o = i.next();
				if(o.isModifiable()) {
					modifiableOrders.add(o);
				}
			}
		} catch (FDResourceException e) {
			LOGGER.debug("Error getting modifiable orders for user: "+user.getUserId());
			e.printStackTrace();
		}
		
		return modifiableOrders;
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
