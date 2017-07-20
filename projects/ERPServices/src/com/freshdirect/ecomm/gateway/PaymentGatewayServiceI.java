package com.freshdirect.ecomm.gateway;

import java.rmi.RemoteException;

import com.freshdirect.payment.command.PaymentCommandI;

public interface PaymentGatewayServiceI {
	
	public void updateSaleDlvStatus(PaymentCommandI command) throws RemoteException;

}
