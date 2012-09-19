package com.freshdirect.fdstore.coremetrics.builder;

import org.apache.log4j.Logger;

import com.freshdirect.customer.EnumDeliveryType;
import com.freshdirect.customer.ErpAddressModel;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDOrderI;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.util.log.LoggerFactory;



public class TagModelUtil  {
	private static final Logger LOGGER = LoggerFactory.getInstance(TagModelUtil.class);
	
	public static String dropExtension(String filename){
		int dotIndex = filename.lastIndexOf(".");
		return dotIndex > -1 ? filename.substring(0, dotIndex) : filename;
	}
	
	public static int getOrderType (EnumDeliveryType type){
		
		int typeValue=-1;
		if(type.equals(EnumDeliveryType.HOME) || type.equals(EnumDeliveryType.DEPOT)){
			typeValue=1;
		}else if(type.equals(EnumDeliveryType.CORPORATE)){
			typeValue=2;
		}else if(type.equals(EnumDeliveryType.PICKUP)){
			typeValue=3;
		}else if(type.equals(EnumDeliveryType.GIFT_CARD_CORPORATE) || type.equals(EnumDeliveryType.GIFT_CARD_PERSONAL)){
			typeValue=4;
		}else if(type.equals(EnumDeliveryType.DONATION_BUSINESS) || type.equals(EnumDeliveryType.DONATION_INDIVIDUAL)){
			typeValue=5;
		}
		
		return typeValue;
	}
	
	public static String getCmOrderId(FDOrderI order){
		return getOrderType(order.getDeliveryType()) + "_" + order.getErpSalesId();
	}

	public static int getOrderCount(FDUserI user) throws SkipTagException{
		try {
			return user.getOrderHistory().getTotalOrderCount();
		} catch (FDResourceException e) {
			LOGGER.error(e);
			throw new SkipTagException("FDResourceException occured", e);
		}
	}
	
	public static ErpAddressModel getDefaultShipToErpAddressModel(FDUserI user) throws SkipTagException{
		try {
			ErpAddressModel erpAddressModel = null;
			FDIdentity identity = user.getIdentity();
			String addrPk = FDCustomerManager.getDefaultShipToAddressPK(identity);
			
			if (addrPk!=null){
				erpAddressModel = FDCustomerManager.getAddress(identity, addrPk);
			}
			
			return erpAddressModel;
		} catch (FDResourceException e) {
			LOGGER.error(e);
			throw new SkipTagException("FDResourceException occured", e);
		}
	}
}