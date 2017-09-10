package com.freshdirect.ecomm.gateway;

import java.rmi.RemoteException;

import com.freshdirect.customer.EnumSaleType;
import com.freshdirect.customer.ErpAbstractOrderModel;
import com.freshdirect.customer.ErpInvoiceModel;
import com.freshdirect.sap.SapCustomerI;
import com.freshdirect.sap.SapOrderI;
import com.freshdirect.sap.command.SapPostReturnCommand;

public interface SapGatewayServiceI {
	
	
	public SapOrderI checkAvailability(SapOrderI order, long timeout) throws RemoteException;

	public void sendCreateSalesOrder(SapOrderI order, EnumSaleType saleType) throws RemoteException;
	
	public void sendCreateCustomer(String erpCustomerNumber, SapCustomerI customer) throws RemoteException;
	
	public void sendCancelSalesOrder(String webOrderNumber, String sapOrderNumber) throws RemoteException;

	public void sendChangeSalesOrder(String webOrderNumber, String sapOrderNumber, SapOrderI order, boolean isPlantChanged) throws RemoteException;
	
	public void sendReturnInvoice(ErpAbstractOrderModel order, ErpInvoiceModel invoice, boolean cancelCoupons) throws RemoteException;

}
