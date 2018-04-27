package com.freshdirect.fdstore.deliverypass;


import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Category;

import com.freshdirect.common.context.UserContext;
import com.freshdirect.common.customer.EnumCardType;
import com.freshdirect.common.customer.EnumServiceType;
import com.freshdirect.customer.EnumNotificationType;
import com.freshdirect.customer.EnumPaymentMethodDefaultType;
import com.freshdirect.customer.EnumSaleStatus;
import com.freshdirect.customer.EnumSaleType;
import com.freshdirect.customer.EnumTransactionSource;
import com.freshdirect.customer.ErpAddressModel;
import com.freshdirect.customer.ErpDeliveryPlantInfoModel;
import com.freshdirect.customer.ErpFraudException;
import com.freshdirect.customer.ErpPaymentMethodI;
import com.freshdirect.customer.ErpSaleNotFoundException;
import com.freshdirect.deliverypass.DeliveryPassException;
import com.freshdirect.fdlogistics.model.FDDeliveryZoneInfo;
import com.freshdirect.fdlogistics.model.FDInvalidAddressException;
import com.freshdirect.fdlogistics.model.FDReservation;
import com.freshdirect.fdlogistics.model.FDTimeslot;
import com.freshdirect.fdstore.FDCachedFactory;
import com.freshdirect.fdstore.FDConfiguration;
import com.freshdirect.fdstore.FDProduct;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDRuntimeException;
import com.freshdirect.fdstore.FDSalesUnit;
import com.freshdirect.fdstore.FDSku;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.customer.FDActionInfo;
import com.freshdirect.fdstore.customer.FDAuthenticationException;
import com.freshdirect.fdstore.customer.FDCartLineI;
import com.freshdirect.fdstore.customer.FDCartLineModel;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.fdstore.customer.FDCustomerInfo;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDInvalidConfigurationException;
import com.freshdirect.fdstore.customer.FDOrderI;
import com.freshdirect.fdstore.customer.FDPaymentInadequateException;
import com.freshdirect.fdstore.customer.FDUser;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.customer.FDUserUtil;
import com.freshdirect.fdstore.customer.adapter.CustomerRatingAdaptor;
import com.freshdirect.fdstore.mail.FDEmailFactory;
import com.freshdirect.fdstore.payments.util.PaymentMethodUtil;
import com.freshdirect.fdstore.rollout.EnumRolloutFeature;
import com.freshdirect.fdstore.rollout.FeatureRolloutArbiter;
import com.freshdirect.fdstore.services.tax.AvalaraContext;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.mail.XMLEmailI;
import com.freshdirect.framework.util.StringUtil;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.logistics.delivery.model.EnumReservationType;
import com.freshdirect.logistics.delivery.model.EnumZipCheckResponses;
import com.freshdirect.storeapi.content.ContentFactory;
import com.freshdirect.storeapi.content.ProductModel;



public class DeliveryPassSubscriptionUtil {
	
//	private static Category LOGGER = LoggerFactory.getInstance(DeliveryPassSubscriptionUtil.class);
//	private static final DateFormat DATE_FORMATTER = new SimpleDateFormat("MM/dd/yyyy");
//	private final static String CLASS_NAME=DeliveryPassSubscriptionUtil.class.getSimpleName();
	
   /*public static String placeOrder(FDActionInfo actionInfo, CustomerRatingAdaptor cra, String arSKU, ErpPaymentMethodI pymtMethod, ErpAddressModel dlvAddress, UserContext userCtx) {
		String orderID = null;
		FDCartModel cart=null;
		try {
			cart = getCart(arSKU,pymtMethod,dlvAddress,actionInfo.getIdentity().getErpCustomerPK(),userCtx);
			if(FDStoreProperties.getAvalaraTaxEnabled()){
			AvalaraContext context = new AvalaraContext(cart);
			context.setCommit(false);
			cart.getAvalaraTaxValue(context);
			actionInfo.setTaxationType(EnumNotificationType.AVALARA);
			}
			orderID = FDCustomerManager.placeSubscriptionOrder(actionInfo, cart, null, false, cra, null);
		}  catch (FDResourceException e) {
			LOGGER.warn(e);
		//	email(actionInfo.getIdentity().getErpCustomerPK(),e.toString());
		} catch (ErpFraudException e) {
			LOGGER.warn(e);
		//	email(actionInfo.getIdentity().getErpCustomerPK(),e.toString());
		} catch (DeliveryPassException e) {
			LOGGER.warn(e);
		//	email(actionInfo.getIdentity().getErpCustomerPK(),e.toString());
		} catch (FDPaymentInadequateException e) {
			LOGGER.warn(e);
		//	email(actionInfo.getIdentity().getErpCustomerPK(),e.toString());

		}
		return orderID;
	}
   
*/  /* public static FDCartModel getCart(String skuCode,ErpPaymentMethodI paymentMethod,ErpAddressModel deliveryAddress, String erpCustomerID,UserContext userCtx) {

		FDCartModel cart=null;
		try {
			cart=new FDCartModel();
			cart.addOrderLine(getCartLine(skuCode));
			cart.setPaymentMethod(paymentMethod);
			cart.setDeliveryAddress(deliveryAddress);
			cart.recalculateTaxAndBottleDeposit(cart.getDeliveryAddress().getZipCode());
			cart.handleDeliveryPass();
			FDReservation reservation=getFDReservation(erpCustomerID,deliveryAddress.getId());
			cart.setDeliveryReservation(reservation);
			cart.setZoneInfo(getZoneInfo(cart.getDeliveryAddress()));
	        cart.setDeliveryPlantInfo(FDUserUtil.getDeliveryPlantInfo(userCtx));
	        cart.setEStoreId(userCtx.getStoreContext().getEStoreId());

		} catch (FDSkuNotFoundException e) {
			LOGGER.warn("Unable to create shopping cart for customer :"+erpCustomerID);
			LOGGER.warn(e);
			cart=null;
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));	
		//	email(erpCustomerID,sw.toString());
		} catch (FDResourceException e) {
			LOGGER.warn("Unable to create shopping cart for customer :"+erpCustomerID);
			LOGGER.warn(e);
			cart=null;
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));	
		//	email(erpCustomerID,sw.toString());
		} catch (FDInvalidAddressException e) {
			LOGGER.warn("Unable to create shopping cart for customer :"+erpCustomerID);
			LOGGER.warn(e);
			cart=null;
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));	
		//	email(erpCustomerID,sw.toString());
		}
		return cart;
	}
*/
	/*private static FDCartLineI getCartLine(String skuCode) throws FDSkuNotFoundException, FDResourceException  {

		ProductModel prodNode = null;
		FDProduct product=null;
		FDSalesUnit salesUnit = null;
		HashMap<String, String> varMap = new HashMap<String, String>();
		double quantity = 1.0D;

		prodNode = ContentFactory.getInstance().getProduct(skuCode);
		product=FDCachedFactory.getProduct(FDCachedFactory.getProductInfo(skuCode));
		salesUnit=product.getSalesUnits()[0];
		//TODO Need to pre-select pricing zone based on last order delivery type and zipcode.
		FDCartLineModel cartLine = new FDCartLineModel(new FDSku(product), prodNode
				, new FDConfiguration(quantity, salesUnit
				.getName(), varMap), null, ContentFactory.getInstance().getCurrentUserContext());

		try {
			cartLine.refreshConfiguration();
		} catch (FDInvalidConfigurationException e) {
			throw new FDResourceException(e);
		}
		return cartLine;
	}

	private static FDActionInfo getFDActionInfo(FDIdentity identity) {
		return new FDActionInfo(EnumTransactionSource.SYSTEM,identity,CLASS_NAME,"",null ,null);
	}

	private static FDIdentity getFDIdentity(String erpCustomerID) {
		return new FDIdentity(erpCustomerID,null);
	}
*/

	
/*	private static ErpPaymentMethodI getPaymentMethod(String paymentMethodPk, Collection<ErpPaymentMethodI> paymentMethods){
		for(Iterator<ErpPaymentMethodI> i=paymentMethods.iterator(); i.hasNext();){
			ErpPaymentMethodI pmethod = i.next();
			if(paymentMethodPk.equals(pmethod.getPK().getId())){
				return pmethod;
			}
		}
		return null;
	}

	private static boolean isExpiredCC(ErpPaymentMethodI paymentMethod) {
		if(paymentMethod.getExpirationDate().before(java.util.Calendar.getInstance().getTime()))
			return true;
		return false;
		
	}
	
	private static FDOrderI getLastNonCOSOrder(String erpCustomerID) throws FDResourceException {

		try {
			return FDCustomerManager.getLastNonCOSOrder(erpCustomerID, EnumSaleType.REGULAR);
		} catch (FDResourceException e) {
			LOGGER.warn("Unable to find payment method for deliveryPass order for customer :"+erpCustomerID);
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
		//	email(erpCustomerID,sw.toString());
			return null;
		}
		catch (ErpSaleNotFoundException e) {
			LOGGER.info("Unable to find a home/pickup order using credit card for customer: "+erpCustomerID);
//			createCase(erpCustomerID,CrmCaseSubject.CODE_AUTO_BILL_PAYMENT_MISSING,DlvPassConstants.AUTORENEW_PYMT_METHOD_UNKNOWN);
			return null;
		}
	}*/

/*	private static Collection<ErpPaymentMethodI> getPaymentMethods(FDIdentity identity) throws FDResourceException {

		try {
			return FDCustomerManager.getPaymentMethods(identity);
		} catch (FDResourceException e) {
			String customerID=identity.getErpCustomerPK();
			LOGGER.warn("Unable to find payment method for autoRenewal order for customer :"+customerID);
//			createCase(customerID,CrmCaseSubject.CODE_AUTO_BILL_PAYMENT_MISSING,DlvPassConstants.AUTORENEW_PYMT_METHOD_UNKNOWN);
			return null;
		}
	}
	

	private static ErpPaymentMethodI getMatchedPaymentMethod(ErpPaymentMethodI pymtMethod, Collection<ErpPaymentMethodI> pymtMethods) {
		if(pymtMethods==null ||pymtMethods.isEmpty())
			return null;
		Iterator<ErpPaymentMethodI> it=pymtMethods.iterator();
		ErpPaymentMethodI _pymtMethod=null;
		boolean exists=false;
		List<ErpPaymentMethodI> matchedPymtMethods=new ArrayList<ErpPaymentMethodI>(pymtMethods.size());
		while (it.hasNext()&& !exists) {
			_pymtMethod=(ErpPaymentMethodI)it.next();
			if( !StringUtil.isEmpty(pymtMethod.getProfileID()) && pymtMethod.getProfileID().equals(_pymtMethod.getProfileID()))	
			    {
				matchedPymtMethods.add(_pymtMethod);
				exists = true;
			} else if(pymtMethod.getCardType().equals(_pymtMethod.getCardType()) ) {
				
				if(!StringUtils.isEmpty(pymtMethod.getMaskedAccountNumber()) && !StringUtils.isEmpty(_pymtMethod.getMaskedAccountNumber())&& _pymtMethod.getMaskedAccountNumber().length()>=4) {
					if(pymtMethod.getMaskedAccountNumber().endsWith(_pymtMethod.getMaskedAccountNumber().substring(_pymtMethod.getMaskedAccountNumber().length()-4))) {
						matchedPymtMethods.add(_pymtMethod);
						exists = true;
					}
				}
			}
		}
		if(matchedPymtMethods.size()==0)
			return exists?_pymtMethod:null;	
		else {
			for (ErpPaymentMethodI temp : matchedPymtMethods) {
				if(temp.getCardType().equals(EnumCardType.PAYPAL) || temp.getCardType().equals(EnumCardType.ECP) || !isExpiredCC(temp)) {
					return temp;
				}
			}
			return null;
		}
	}*/
/*	
	public static String placeOrder(String erpCustomerID, String arSKU, boolean isFirstOrder) throws FDResourceException {

		FDIdentity identity=null;
		FDActionInfo actionInfo=null;
		ErpPaymentMethodI pymtMethod=null;
		FDOrderI lastOrder=null;
		FDUser user=null;
		CustomerRatingAdaptor cra=null;
		lastOrder=getLastNonCOSOrder(erpCustomerID);
		String orderID="";
		ErpAddressModel address = null;		
		try {
			identity=getFDIdentity(erpCustomerID);
			user=FDCustomerManager.getFDUser(identity);
			actionInfo=getFDActionInfo(identity);
			actionInfo.setIdentity(user.getIdentity());
		} catch (FDAuthenticationException e) {
			LOGGER.warn("Unable to place free-trial Delivery Pass order for customer :"+erpCustomerID);
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
		//	email(erpCustomerID,sw.getBuffer().toString());
		}
		if(lastOrder!=null) {
			address = lastOrder.getDeliveryAddress();
			pymtMethod=getMatchedPaymentMethod(lastOrder.getPaymentMethod(),getPaymentMethods(user.getIdentity()));
		} else {
			address=getDeliveryPassDeliveryAddress(user.getSelectedServiceType());
			Collection<ErpPaymentMethodI> ccards = FDCustomerManager.getPaymentMethods(user.getIdentity());
			if(!ccards.isEmpty()){
				pymtMethod=(ErpPaymentMethodI) ((ccards.toArray())[0]);
			}
		} 
		
		if(null == pymtMethod){
			identity = new FDIdentity(erpCustomerID);
			Collection<ErpPaymentMethodI> paymentMethods = FDCustomerManager.getPaymentMethods(identity);
			List<ErpPaymentMethodI> paymentMethodList = new ArrayList<ErpPaymentMethodI>(paymentMethods);
			if(!paymentMethodList.isEmpty()){
				for (ErpPaymentMethodI erpPaymentMethodI : paymentMethodList) {
					
					if(!erpPaymentMethodI.getCardType().equals(EnumCardType.PAYPAL) && !erpPaymentMethodI.getCardType().equals(EnumCardType.ECP) && isExpiredCC(erpPaymentMethodI)) {
						continue;
					} else{
						pymtMethod = erpPaymentMethodI;
						break;
					}
				}
				LOGGER.info("First payment method from the list is chosen for customer :"+erpCustomerID);
			}
		}
		if(null == address) {
			Collection<ErpAddressModel> addresses = FDCustomerManager.getShipToAddresses(identity);
			List<ErpAddressModel> addressesList = new ArrayList<ErpAddressModel>(addresses);
			if(null != addressesList && !addressesList.isEmpty()){
				address = addressesList.get(0);
				LOGGER.info("First address from the list is chosen for customer :"+erpCustomerID);
			}
		}
		
		if(pymtMethod!=null && address != null) {
			if(!pymtMethod.getCardType().equals(EnumCardType.PAYPAL) && !pymtMethod.getCardType().equals(EnumCardType.ECP) && isExpiredCC(pymtMethod)) {
				LOGGER.warn("Free-trial deliveryPass order payment method is expired for customer :"+erpCustomerID);
				//		email(erpCustomerID,"Free-trial deliveryPass order payment method is expired for customer :"+erpCustomerID);
			} else {
				try {

					cra=new CustomerRatingAdaptor(user.getFDCustomer().getProfile(),user.isCorporateUser(),user.getAdjustedValidOrderCount());
					orderID=placeOrder(actionInfo,cra,arSKU,pymtMethod,address,user.getUserContext());

				}
				catch(FDResourceException fe) {
					LOGGER.warn("Unable to place free-trial deliveryPass order for customer :"+erpCustomerID);
					StringWriter sw = new StringWriter();
					fe.printStackTrace(new PrintWriter(sw));
	//				email(erpCustomerID,sw.getBuffer().toString());
				}
			}

		} else {
			LOGGER.warn("Unable to find payment method for free-trial deliveryPass order for customer :"+erpCustomerID);
//			email(erpCustomerID,"Unable to find payment method for free-trial deliveryPass order for customer :"+erpCustomerID);
		}

		return orderID;

	}


*/	
	private static FDReservation getFDReservation(String customerID, String addressID) {
		Date expirationDT = new Date(System.currentTimeMillis() + 1000);
		FDTimeslot timeSlot=getFDTimeSlot();
		FDReservation reservation=new FDReservation(new PrimaryKey("1"), timeSlot, expirationDT, EnumReservationType.STANDARD_RESERVATION, 
				customerID, addressID,false, null,20,null,false,null,null);
		return reservation;

	}

	private static FDTimeslot getFDTimeSlot() {
		// TODO Auto-generated method stub
		return null;
	}
	public static FDDeliveryZoneInfo getZoneInfo(ErpAddressModel address) throws FDResourceException, FDInvalidAddressException {

		FDDeliveryZoneInfo zInfo =new FDDeliveryZoneInfo("1","1","1",EnumZipCheckResponses.DELIVER);
		return zInfo;
	}

	
	private static ErpAddressModel getDeliveryPassDeliveryAddress(EnumServiceType sType){
		ErpAddressModel address=new ErpAddressModel();
		address.setAddress1("23-30 borden ave");
		address.setCity("Long Island City");
		address.setState("NY");
		address.setCountry("US");
		address.setZipCode("11101");
		address.setServiceType(sType);
		return address;

	}
	
	public static ErpAddressModel setDeliveryPassDeliveryAddress(EnumServiceType sType){
		return DeliveryPassSubscriptionUtil.getDeliveryPassDeliveryAddress(sType);
	}
	
	public static FDReservation setFDReservation(String customerID, String addressID) {
		return DeliveryPassSubscriptionUtil.getFDReservation(customerID, addressID);

	}


}
