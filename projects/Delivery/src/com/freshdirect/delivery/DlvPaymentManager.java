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
import com.freshdirect.framework.core.ServiceLocator;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.payment.PaymentManager;
import com.freshdirect.payment.command.DeliveryConfirmation;
import com.freshdirect.payment.command.PaymentCommandI;
import com.freshdirect.payment.command.Redelivery;
import com.freshdirect.payment.command.RefusedOrder;
import com.freshdirect.payment.ejb.PaymentGatewayHome;
import com.freshdirect.payment.ejb.PaymentGatewaySB;

public class DlvPaymentManager {
	
	private static Category LOGGER = LoggerFactory.getInstance( DlvPaymentManager.class );
	private final ServiceLocator serviceLocator;
	
	private static DlvPaymentManager instance = null;
	
	/** Creates new DlvPaymentManager */
    private DlvPaymentManager() throws NamingException {
		//private constructor so that we have a single instance of this class in jvm
		this.serviceLocator = new ServiceLocator(DlvProperties.getInitialContext());
		
	}
	
	public static DlvPaymentManager getInstance() throws DlvResourceException {
		if(instance == null){
			try{
				instance = new DlvPaymentManager();
			}catch(NamingException e){
				throw new DlvResourceException (e);
			}
		}
		return instance;
	}
	
	public synchronized void updateSaleDlvStatus(List delivered, List redelivery, List refused) throws DlvResourceException {		
		try{
			PaymentGatewaySB sb = this.getPaymentGatewaySB();
			String saleId = null;
			PaymentCommandI command = null;
			
			for(int i = 0, size = delivered.size(); i < size; i++){
				saleId = (String)delivered.get(i);
				command = new DeliveryConfirmation(saleId);
				sb.updateSaleDlvStatus(command);
			}
			
			for(int i = 0, size = redelivery.size(); i < size; i++){
				saleId = (String)redelivery.get(i);
				command = new Redelivery(saleId);
				sb.updateSaleDlvStatus(command);
			}
			
			for(int i = 0, size = refused.size(); i < size; i++){
				DlvReturnRecord returnRec = (DlvReturnRecord)refused.get(i);
				command = new RefusedOrder(returnRec.getSaleId(), returnRec.isFullReturn(), returnRec.isAlocholReturn());
				sb.updateSaleDlvStatus(command);
			}
					
		}catch(RemoteException re){
			LOGGER.warn("RemoteException: ", re);
			throw new DlvResourceException(re, "Cannot talk to SB");
		}
		
	}
	
	public synchronized List getOrdersByTruckNumber(String truckNumber, Date deliveryDate) throws DlvResourceException {
		try{
			ErpCustomerManagerSB sb = this.getErpCustomerManagerSB();
			List ret = sb.getOrdersByTruckNumber(truckNumber, deliveryDate);
			
			return ret;
		}catch(RemoteException re){
			LOGGER.warn("RemoteException: ", re);
			throw new DlvResourceException(re, "Cannot talk to SB");
		}	
	}

	public synchronized void deliveryConfirm(String orderNumber) throws DlvResourceException {
		try{
			PaymentManager paymentManager = new PaymentManager();
			paymentManager.deliveryConfirm(orderNumber);
		}catch(ErpTransactionException te){
			throw new DlvResourceException(te, "Sale is not in the right state to be confirmed");
		}
	} 
	
	public synchronized void captureAuthorization(String orderNumber) throws DlvResourceException {
		try{
			PaymentManager paymentManager = new PaymentManager();
			paymentManager.captureAuthorization(orderNumber);
		}catch(ErpTransactionException te){
			throw new DlvResourceException(te, "Sale is not in the right state to be confirmed");
		}
	} 
	
	public synchronized ErpDeliveryInfoModel getDeliveryInfo(String orderNumber) throws DlvResourceException, ErpSaleNotFoundException {
		try{
			ErpCustomerManagerSB sb = this.getErpCustomerManagerSB();
			return sb.getDeliveryInfo(orderNumber);
		}catch(RemoteException re){
			throw new DlvResourceException(re, "Cannot talk to SB");
		}
	}
	
	public synchronized DlvSaleInfo getSaleInfo(String orderNumber) throws DlvResourceException, ErpSaleNotFoundException {
		try{
			ErpCustomerManagerSB sb = this.getErpCustomerManagerSB();
			return sb.getDlvSaleInfo(orderNumber);
		}catch(RemoteException re){
			throw new DlvResourceException (re, "Cannot talk to SB");
		}
	}
	
	public synchronized void createCaseForSale(String orderNumber, String reason) throws DlvResourceException, ErpSaleNotFoundException {
		try{
			ErpCustomerManagerSB sb = this.getErpCustomerManagerSB();
			sb.createCaseForSale(orderNumber, reason);
		}catch(RemoteException re){
			throw new DlvResourceException(re, "Cannot talk to SB");
		}
	}
	
	public synchronized void addReturn(String orderNumber, boolean fullReturn, boolean alcoholOnly) throws DlvResourceException, ErpSaleNotFoundException {
		try{
			ErpCustomerManagerSB sb = this.getErpCustomerManagerSB();
			sb.markAsReturn(orderNumber, fullReturn, alcoholOnly);
		}catch(RemoteException re){
			throw new DlvResourceException(re, "Cannot talk to SB");
		}catch(ErpTransactionException te){
			throw new DlvResourceException(te, "Order is not in right status to add RETURN");
		}
	}
	
	public synchronized void addRedelivery(String orderNumber) throws DlvResourceException, ErpSaleNotFoundException{
		try{
			ErpCustomerManagerSB sb = this.getErpCustomerManagerSB();
			sb.markAsRedelivery(orderNumber);
		}catch(RemoteException re){
			throw new DlvResourceException(re, "Cannot talk to SB");
		}catch(ErpTransactionException te){
			throw new DlvResourceException(te, "Order is not in right status to add RETURN");
		}
	}
	
	public synchronized List getRedeliveries(Date date) throws DlvResourceException {
		try{
			ErpCustomerManagerSB sb = this.getErpCustomerManagerSB();
			return sb.getRedeliveries(date);
		}catch(RemoteException re){
			throw new DlvResourceException(re, "Cannot talk to SB");
		}
	}
	
	public synchronized List getOrdersForDateAndAddress(Date date, String address, String zipcode) throws DlvResourceException {
		try{
			ErpCustomerManagerSB sb = this.getErpCustomerManagerSB();
			return sb.getOrdersForDateAndAddress(date, address, zipcode);
		}catch(RemoteException e){
			throw new DlvResourceException(e, "Cannot talk to SB");
		}
	}
	
	public synchronized void unconfirmOrder(String saleId) throws DlvResourceException {
		try{
			PaymentManager paymentManager = new PaymentManager();
			paymentManager.unconfirm(saleId);
		}catch(ErpTransactionException te){
			throw new DlvResourceException(te, te.getMessage());
		}
	}
			
	
	private PaymentGatewaySB getPaymentGatewaySB() throws DlvResourceException {
		try {
			PaymentGatewayHome home = (PaymentGatewayHome)serviceLocator.getRemoteHome(DlvProperties.getPaymentGatewayHome());
			return home.create(); 
		} catch (NamingException e) {
			throw new DlvResourceException(e);
		}catch (CreateException e){
			throw new DlvResourceException(e);
		}catch(RemoteException e){
			throw new DlvResourceException(e);
		}
	}
	
	private ErpCustomerManagerSB getErpCustomerManagerSB() throws DlvResourceException {
		try {
			ErpCustomerManagerHome home = (ErpCustomerManagerHome)serviceLocator.getRemoteHome(DlvProperties.getCustomerManagerHome());
			return home.create();
		} catch (NamingException ne) {
			throw new DlvResourceException(ne);
		}catch (CreateException e){
			throw new DlvResourceException(e);
		}catch(RemoteException e){
			throw new DlvResourceException(e);
		}
	}	
}
