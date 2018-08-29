package com.freshdirect.delivery;

import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;

import javax.ejb.CreateException;
import javax.naming.NamingException;

import org.apache.log4j.Category;

import com.freshdirect.customer.DlvSaleInfo;
import com.freshdirect.customer.ErpDeliveryInfoModel;
import com.freshdirect.customer.ErpSaleNotFoundException;
import com.freshdirect.customer.ErpTransactionException;
import com.freshdirect.customer.ejb.ErpCustomerManagerHome;
import com.freshdirect.customer.ejb.ErpCustomerManagerSB;
import com.freshdirect.ecomm.gateway.OrderServiceApiClient;
import com.freshdirect.ecomm.gateway.OrderServiceApiClientI;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.framework.core.ServiceLocator;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.payment.PaymentManager;

public class DlvPaymentManager {
	
	private static Category LOGGER = LoggerFactory.getInstance( DlvPaymentManager.class );
	private final ServiceLocator serviceLocator;
	
	private static DlvPaymentManager instance = null;
	
	/** Creates new DlvPaymentManager */
    private DlvPaymentManager() throws NamingException {
		//private constructor so that we have a single instance of this class in jvm
		this.serviceLocator = new ServiceLocator(DlvProperties.getInitialContext());
		
	}
	
	public static DlvPaymentManager getInstance() throws FDResourceException {
		if(instance == null){
			try{
				instance = new DlvPaymentManager();
			}catch(NamingException e){
				throw new FDResourceException (e);
			}
		}
		return instance;
	}
	
	public synchronized List getOrdersByTruckNumber(String truckNumber, Date deliveryDate) throws FDResourceException {
		try{
			
			if(FDStoreProperties.isSF2_0_AndServiceEnabled("ordersByTruck_Api")){
	    		OrderServiceApiClientI service = OrderServiceApiClient.getInstance();
	    		return service.getOrdersByTruck(truckNumber, deliveryDate);
	    	}else{
			
			
			ErpCustomerManagerSB sb = this.getErpCustomerManagerSB();
			return sb.getOrdersByTruckNumber(truckNumber, deliveryDate);
	    	}	
		}catch(RemoteException re){
			LOGGER.warn("RemoteException: ", re);
			throw new FDResourceException(re, "Cannot talk to SB");
		}	
	}

	public synchronized void deliveryConfirm(String orderNumber) throws FDResourceException {
		try{
			PaymentManager paymentManager = new PaymentManager();
			paymentManager.deliveryConfirm(orderNumber);
		}catch(ErpTransactionException te){
			throw new FDResourceException(te, "Sale is not in the right state to be confirmed");
		}
	} 
	
	public synchronized void captureAuthorization(String orderNumber) throws FDResourceException {
		try{
			PaymentManager paymentManager = new PaymentManager();
			paymentManager.captureAuthorization(orderNumber);
		}catch(ErpTransactionException te){
			throw new FDResourceException(te, "Sale is not in the right state to be confirmed");
		}
	} 
	
	public synchronized ErpDeliveryInfoModel getDeliveryInfo(String orderNumber) throws FDResourceException, ErpSaleNotFoundException {
		try{
			if(FDStoreProperties.isSF2_0_AndServiceEnabled("getDeliveryInfo_Api")){
	    		return OrderServiceApiClient.getInstance().getDeliveryInfo(orderNumber);
	    	}else{
				ErpCustomerManagerSB sb = this.getErpCustomerManagerSB();
				return sb.getDeliveryInfo(orderNumber);
	    	}
		}catch(RemoteException re){
			throw new FDResourceException(re, "Cannot talk to SB");
		}
	}
	
	public synchronized DlvSaleInfo getSaleInfo(String orderNumber) throws FDResourceException, ErpSaleNotFoundException {
		try{
			if(FDStoreProperties.isSF2_0_AndServiceEnabled("orderHistory_Api")){
	    		OrderServiceApiClientI service = OrderServiceApiClient.getInstance();
	    		return service.getDlvSaleInfo(orderNumber);
	    	}else{
			ErpCustomerManagerSB sb = this.getErpCustomerManagerSB();
			return sb.getDlvSaleInfo(orderNumber);
	    	}
		}catch(RemoteException re){
			throw new FDResourceException (re, "Cannot talk to SB");
		}
	}
	
	public synchronized void addReturn(String orderNumber, boolean fullReturn, boolean alcoholOnly) throws FDResourceException, ErpSaleNotFoundException {
		try{
			ErpCustomerManagerSB sb = this.getErpCustomerManagerSB();
			sb.markAsReturn(orderNumber, fullReturn, alcoholOnly);
		}catch(RemoteException re){
			throw new FDResourceException(re, "Cannot talk to SB");
		}catch(ErpTransactionException te){
			throw new FDResourceException(te, "Order is not in right status to add RETURN");
		}
	}
	
	public synchronized List getOrdersForDateAndAddress(Date date, String address, String zipcode) throws FDResourceException {
		try{
			ErpCustomerManagerSB sb = this.getErpCustomerManagerSB();
			return sb.getOrdersForDateAndAddress(date, address, zipcode);
		}catch(RemoteException e){
			throw new FDResourceException(e, "Cannot talk to SB");
		}
	}
	
	public synchronized void unconfirmOrder(String saleId) throws FDResourceException {
		try{
			PaymentManager paymentManager = new PaymentManager();
			paymentManager.unconfirm(saleId);
		}catch(ErpTransactionException te){
			throw new FDResourceException(te, te.getMessage());
		}
	}
			
	private ErpCustomerManagerSB getErpCustomerManagerSB() throws FDResourceException {
		try {
			ErpCustomerManagerHome home = (ErpCustomerManagerHome)serviceLocator.getRemoteHome(DlvProperties.getCustomerManagerHome());
			return home.create();
		} catch (NamingException ne) {
			throw new FDResourceException(ne);
		}catch (CreateException e){
			throw new FDResourceException(e);
		}catch(RemoteException e){
			throw new FDResourceException(e);
		}
	}	
}
