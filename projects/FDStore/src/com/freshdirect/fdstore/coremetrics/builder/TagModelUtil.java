package com.freshdirect.fdstore.coremetrics.builder;

import com.freshdirect.customer.EnumDeliveryType;
import com.freshdirect.fdstore.customer.FDOrderI;



public class TagModelUtil  {
	
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

}