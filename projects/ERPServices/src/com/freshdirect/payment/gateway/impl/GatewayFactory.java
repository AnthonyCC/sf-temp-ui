package com.freshdirect.payment.gateway.impl;

import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.framework.util.StringUtil;
import com.freshdirect.payment.ejb.PaymentGatewayContext;
import com.freshdirect.payment.gateway.Gateway;
import com.freshdirect.payment.gateway.GatewayType;
import com.freshdirect.payment.service.PaymentechService;

public class GatewayFactory {
	
	private static Gateway paymentech=new Paymentech();
	private static Gateway paymentechGateway=new PaymentechService();
	private static Gateway cybersource=new CyberSource();
	private static Gateway paypal=new PayPal();
	
	public static Gateway getGateway(String profileID) {
		GatewayType gatewayType = StringUtil.isEmpty(profileID)?GatewayType.CYBERSOURCE:GatewayType.PAYMENTECH;
		return getGateway(gatewayType);
	}
	
	public static Gateway getGateway(PaymentGatewayContext context) {
		if(context.getPreferred()!=null){
			return getGateway(context.getPreferred());
		}
		return getGateway(context.getAlternate());
	}
	public static Gateway getGateway(GatewayType gatewayType) {
		
		
		if(GatewayType.PAYMENTECH.equals(gatewayType)) {
			return getPaymentechGateway();
		} else if(GatewayType.CYBERSOURCE.equals(gatewayType)) {
			//return cybersource;
			throw new IllegalAccessError(gatewayType.getName()+" is not supported.");
		} else if(GatewayType.PAYPAL.equals(gatewayType)) {
			return paypal; 
		}else if(gatewayType==null) {
			if(FDStoreProperties.isPaymentechGatewayEnabled()) {
				return getPaymentechGateway();
			} else {
				//return cybersource;
				throw new IllegalAccessError(gatewayType.getName()+" is not supported.");
			}
		}
		else {
			throw new IllegalAccessError(gatewayType.getName()+" is not supported.");
		}
	}
	private static Gateway getPaymentechGateway(){
		if(FDStoreProperties.isTLSSHAEnabledForPaymentGateway()){
			return paymentechGateway; 
			}else{
			return paymentech; 
			}
	}
}
