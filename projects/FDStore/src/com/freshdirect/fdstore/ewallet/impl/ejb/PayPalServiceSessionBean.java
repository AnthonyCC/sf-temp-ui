package com.freshdirect.fdstore.ewallet.impl.ejb;

import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.log4j.Category;

import com.braintreegateway.BraintreeGateway;
import com.braintreegateway.Customer;
import com.braintreegateway.CustomerRequest;
import com.braintreegateway.Environment;
import com.braintreegateway.PaymentMethod;
import com.braintreegateway.PaymentMethodRequest;
import com.braintreegateway.Result;
import com.freshdirect.common.customer.EnumCardType;
import com.freshdirect.customer.ErpCreditCardModel;
import com.freshdirect.customer.ErpCustEWalletModel;
import com.freshdirect.customer.ErpPaymentMethodI;
import com.freshdirect.customer.ErpPaymentMethodModel;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.PayPalData;
import com.freshdirect.fdstore.customer.FDCustomerFactory;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.ewallet.EnumEwalletType;
import com.freshdirect.fdstore.ewallet.EwalletRequestData;
import com.freshdirect.fdstore.ewallet.EwalletResponseData;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.core.SessionBeanSupport;
import com.freshdirect.framework.util.StringUtil;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.payment.ewallet.gateway.ejb.EwalletActivityLogModel;
import com.freshdirect.payment.gateway.ewallet.impl.EWalletLogActivity;

/**
 * @author Aniwesh Vatsal
 *
 */
public class PayPalServiceSessionBean extends SessionBeanSupport{
	
	
	private static final long serialVersionUID = -4137899364083573169L;
	private final static Category LOGGER = LoggerFactory.getInstance(PayPalServiceSessionBean.class);
	
	private static final String PAYPAL_CLIENT_TOKEN_TXN="ClientTokenTxn";
	private static final String PAYPAL_CREATE_CUSTOMER_TXN="CreateCustomerTxn";
	private static final String PAYPAL_GET_VAULT_TOKEN_TXN="GetVaultTokenTxn";
	
	private static final String PAYPAL_TXN_SUCCESS="SUCCESS";
	private static final String PAYPAL_TXN_FAIL="FAIL";
	private static final String PARAM_PP_PAYMENMETHOD_NONCE ="paymentMethodNonce";
	private static final String PARAM_FIRST_NAME ="firstName";
	private static final String PARAM_LAST_NAME ="lastName";
	private static final String PARAM_EMAILID ="email";
	private static final String PARAM_ORIGIN ="origin";


	/**
	 * @param ewalletRequestData
	 * @return
	 * @throws RemoteException
	 */
	public EwalletResponseData getToken(EwalletRequestData ewalletRequestData) throws RemoteException {
		EwalletResponseData ewalletResponseData = new EwalletResponseData();
		try{
			String clientToken = clientToken();
			ewalletResponseData.setToken(clientToken);	
			
			String tnx = PAYPAL_TXN_FAIL;
			if(ewalletResponseData.getToken() != null  && ewalletResponseData.getToken().trim().length() > 0){
				tnx = PAYPAL_TXN_SUCCESS;
			}
			logPPEwalletRequestResponse(ewalletRequestData, ewalletResponseData, PAYPAL_CLIENT_TOKEN_TXN, tnx);
		}catch(Exception ex){
			logPPEwalletRequestResponse(ewalletRequestData, ewalletResponseData, PAYPAL_CLIENT_TOKEN_TXN, PAYPAL_TXN_FAIL);
		}
		return ewalletResponseData;
	}
	
	/**
	 * This method returns the Client Token
	 * @return
	 */
	private String clientToken() {
		return PayPalData.getBraintreeGateway().clientToken().generate();
	}
	
	
	/**
	 * @param ewalletRequestData
	 * @return
	 * @throws RemoteException
	 */
	public EwalletResponseData addPayPalWallet(EwalletRequestData ewalletRequestData) throws RemoteException {
		
		EwalletResponseData ewalletResponseData = new EwalletResponseData();
		String paymentMethodNonce = "";
		String firstName = "";
		String lastName = "";
		String email = "";
		String origin = "";
		if (ewalletRequestData.getReqParams().containsKey(PARAM_PP_PAYMENMETHOD_NONCE)) {
			paymentMethodNonce = ewalletRequestData.getReqParams().get(
					PARAM_PP_PAYMENMETHOD_NONCE);
		}
		if (ewalletRequestData.getReqParams().containsKey(PARAM_FIRST_NAME)) {
			firstName = ewalletRequestData.getReqParams().get(PARAM_FIRST_NAME);
		}
		if (ewalletRequestData.getReqParams().containsKey(PARAM_LAST_NAME)) {
			lastName = ewalletRequestData.getReqParams().get(PARAM_LAST_NAME);
		}
		if (ewalletRequestData.getReqParams().containsKey(PARAM_EMAILID)) {
			email = ewalletRequestData.getReqParams().get(PARAM_EMAILID);
		}
		if (ewalletRequestData.getReqParams().containsKey(PARAM_ORIGIN)) {
			origin = ewalletRequestData.getReqParams().get(PARAM_ORIGIN);
		}
		
		String vaultToken = obtainVaultToken(paymentMethodNonce,ewalletRequestData, firstName, lastName);
		
		if(vaultToken != null && !StringUtil.isEmpty(vaultToken)){
			//Store VaultToken and then Store PayPal account in Paymentmethod table.
			
			ErpCustEWalletModel custEWalletModel = new ErpCustEWalletModel();
			// Insert the Long Access Token to FD database
			custEWalletModel.seteWalletId(""+EnumEwalletType.PP.getValue());
			custEWalletModel.setCustomerId(ewalletRequestData.getCustomerId());
			custEWalletModel.setLongAccessToken(vaultToken);
			custEWalletModel.setEmailId(email);
			// Delete the Long Access Token for the Customer
			deleteVaultToken(ewalletRequestData.getCustomerId(),""+EnumEwalletType.PP.getValue());
			// Insert new Long access token
			insertVaultToken(custEWalletModel);
			
			//Add paymentmethod
			ErpPaymentMethodI paymentMethod = new ErpCreditCardModel();
			paymentMethod.setName(firstName+" "+lastName);
			paymentMethod.setEmailID(email);
			paymentMethod.setAccountNumber("1111111111111111");
			paymentMethod.setCardType(EnumCardType.PAYPAL);
			paymentMethod.setAddress1("20-30 BORDEN AVE");
			paymentMethod.setAddress2("");
			paymentMethod.setApartment("");
			paymentMethod.setCity("Long Island City");
			paymentMethod.setState("NY");
			paymentMethod.setZipCode("11101");
			paymentMethod.setCountry("US");
			paymentMethod.setProfileID(vaultToken);
			paymentMethod.seteWalletID(""+EnumEwalletType.PP.getValue());
			paymentMethod.setVendorEWalletID("");
			paymentMethod.seteWalletTrxnId("");
		    paymentMethod.setCustomerId(ewalletRequestData.getCustomerId());
		     
		    ErpPaymentMethodI searchedPM = searchPPWalletCards(ewalletRequestData, paymentMethod);
		     
		 	// Add the card detail to Database
			try {
				if (searchedPM == null) {	// No Paypal account exists for the customer
					ewalletRequestData.setPaymentechEnabled(false);
					FDCustomerManager.addPaymentMethod(ewalletRequestData.getFdActionInfo(), paymentMethod,ewalletRequestData.isPaymentechEnabled());
			
					List<ErpPaymentMethodI> paymentMethods = FDCustomerFactory.getErpCustomer(ewalletRequestData.getCustomerId()).getPaymentMethods();
					
			        if (!paymentMethods.isEmpty() && !origin.equals("your_account")) {
			        	sortPaymentMethodsByIdReserved(paymentMethods);
			    		final PrimaryKey pmPK = ( (ErpPaymentMethodModel)paymentMethods.get(0)).getPK();
			    		FDCustomerManager.setDefaultPaymentMethod( ewalletRequestData.getFdActionInfo(), pmPK );
			    		
			        }
					if (!paymentMethods.isEmpty() && paymentMethods.size() > 1) {
						sortPaymentMethodsByIdReserved(paymentMethods);
					}
					ErpPaymentMethodI lastAddedPM = paymentMethods.get(0);
					ewalletResponseData.setPaymentMethod(lastAddedPM);
				} else {	// Paypal account exists for the customer, remove and add new record
					FDCustomerManager.removePaymentMethod(ewalletRequestData.getFdActionInfo(), searchedPM);
					ewalletRequestData.setPaymentechEnabled(false);
					FDCustomerManager.addPaymentMethod(ewalletRequestData.getFdActionInfo(), paymentMethod,	ewalletRequestData.isPaymentechEnabled());
			
					List<ErpPaymentMethodI> paymentMethods = FDCustomerFactory.getErpCustomer(ewalletRequestData.getCustomerId()).getPaymentMethods();
					
			        if (!paymentMethods.isEmpty() && origin!= null && !origin.equals("your_account")) {
			        	sortPaymentMethodsByIdReserved(paymentMethods);
			    		final PrimaryKey pmPK = ( (ErpPaymentMethodModel)paymentMethods.get(0)).getPK();
			    		FDCustomerManager.setDefaultPaymentMethod( ewalletRequestData.getFdActionInfo(), pmPK );
			    		
			        }
					if (!paymentMethods.isEmpty() && paymentMethods.size() > 1) {
						sortPaymentMethodsByIdReserved(paymentMethods);
					}
					ErpPaymentMethodI lastAddedPM = paymentMethods.get(0);
					ewalletResponseData.setPaymentMethod(lastAddedPM);
				}
			} catch (Exception exception) {
			
			}	
		}
		ewalletResponseData.setToken(vaultToken);
		return ewalletResponseData;
	}
	
	
	/**
	 * @param ewalletRequestData
	 * @param mapPaymentMethod
	 * @return
	 */
	private ErpPaymentMethodI searchPPWalletCards(EwalletRequestData ewalletRequestData, ErpPaymentMethodI mapPaymentMethod){
		ErpPaymentMethodI paymentMethodModel = null;
		try {
			List<ErpPaymentMethodI> paymentMethods = FDCustomerFactory.getErpCustomer(ewalletRequestData.getFdActionInfo().getIdentity()).getPaymentMethods();
			for(ErpPaymentMethodI paymentMethod:paymentMethods){
				 if(paymentMethod.geteWalletID() != null && paymentMethod.geteWalletID().equals(""+EnumEwalletType.PP.getValue())){
					paymentMethodModel = paymentMethod;
					break;
				 }
			}
		} catch (FDResourceException fdException) {
			LOGGER.error("Exception: while calling removing MP paired cards from database.", fdException);
			fdException.printStackTrace();
		}
		return paymentMethodModel;
	}
	
    private static final Comparator<ErpPaymentMethodI> PAYMENT_COMPARATOR_BY_ID = new Comparator<ErpPaymentMethodI>() {
        @Override
        public int compare(ErpPaymentMethodI o1, ErpPaymentMethodI o2) {
            Long id1 = Long.parseLong(o1.getPK().getId());
            Long id2 = Long.parseLong(o2.getPK().getId());
            return id1.compareTo(id2);
        }

    };
	  
	private static final Comparator<ErpPaymentMethodI> PAYMENT_COMPARATOR_BY_ID_REVERSED = Collections.reverseOrder(PAYMENT_COMPARATOR_BY_ID);//ComparatorChain
	
	/**
	 * @param paymentMethods
	 */
	private void sortPaymentMethodsByIdReserved(List<ErpPaymentMethodI> paymentMethods) {
		Collections.sort(paymentMethods, PAYMENT_COMPARATOR_BY_ID_REVERSED);
	}
	
	/**
	 * This method interact with PayPal to get the Vault Token 
	 * @param paymentMethodNonce
	 * @return
	 */
	private String obtainVaultToken(String paymentMethodNonce, EwalletRequestData ewalletRequestData, String fName, String lName) {
		// Create customer object
		CustomerRequest request = new CustomerRequest();
		request.customerId(ewalletRequestData.getCustomerId());
		request.firstName(fName);
		request.lastName(lName);
		
		Result<? extends PaymentMethod> vaultResult = null;
		
	    Result<Customer> customerResult = PayPalData.getBraintreeGateway().customer().create(request);
	    // Check customer record created successfully or not
	    if(customerResult.isSuccess()){
	    	logPPEwalletRequestResponse(ewalletRequestData, null, PAYPAL_CREATE_CUSTOMER_TXN, PAYPAL_TXN_SUCCESS);
			PaymentMethodRequest paymentMethodRequest = new PaymentMethodRequest().customerId(customerResult.getTarget().getId())
				    .paymentMethodNonce(paymentMethodNonce);
			vaultResult = PayPalData.getBraintreeGateway().paymentMethod().create(paymentMethodRequest);
//			PAYPAL_GET_VAULT_TOKEN_TXN
			if(vaultResult != null){
				logPPEwalletRequestResponse(ewalletRequestData, null, PAYPAL_GET_VAULT_TOKEN_TXN, PAYPAL_TXN_SUCCESS);
				return vaultResult.getTarget().getToken();
			}else{
				logPPEwalletRequestResponse(ewalletRequestData, null, PAYPAL_GET_VAULT_TOKEN_TXN, PAYPAL_TXN_FAIL);
				return null;
			}
	    }else{
	    	logPPEwalletRequestResponse(ewalletRequestData, null, PAYPAL_CREATE_CUSTOMER_TXN, PAYPAL_TXN_FAIL);
	    	return null;
	    }
	}
	
	
	
	/**
	 * This method will insert the new VaultToken into database (Table : CUST.CUST_EWALLET)
	* @param custEWalletModel
	*/
	private void insertVaultToken(ErpCustEWalletModel custEWalletModel){
	   	FDCustomerManager.insertLongAccessToken(custEWalletModel);
	   }
	
	/**
	* Method is used to Delete the VaultToken From the database for the given Customer and Ewallet
	* @param customerID
	* @param eWalletId
	*/
	private void deleteVaultToken(String customerID,String eWalletId){
		FDCustomerManager.deleteLongAccessToken(customerID,eWalletId);
	}
	
	
	/**
	 * @param ewalletRequestData
	 * @return
	 * @throws RemoteException
	 */
	public EwalletResponseData disconnect(EwalletRequestData ewalletRequestData)
			throws RemoteException {
		try {
			deleteVaultToken(ewalletRequestData.getCustomerId(), ""+EnumEwalletType.PP.getValue());
			List<ErpPaymentMethodI> paymentMethods = FDCustomerFactory.getErpCustomer(ewalletRequestData.getFdActionInfo().getIdentity()).getPaymentMethods();
			if (!paymentMethods.isEmpty()) {
				try {
					for (ErpPaymentMethodI payment : paymentMethods) {
						if (payment.geteWalletID() != null && payment.geteWalletID().equalsIgnoreCase(""+EnumEwalletType.PP.getValue())) {
							FDCustomerManager.removePaymentMethod(ewalletRequestData.getFdActionInfo(),payment);
							ewalletRequestData.setPaymentData(null);
							break;
						}
					}
				} catch (Exception e) {
					LOGGER.error(
							"Exception: while calling Disconnect - removePaymentMethod service call.",
							e);
					throw new RemoteException(e.getMessage());
				}
			}

		} catch (FDResourceException e) {
			LOGGER.error("Exception: while calling Disconnect service call.", e);
			throw new RemoteException(e.getMessage());
		}
		return new EwalletResponseData();
	}
	
	/**
	 * @param data
	 * @param requestData
	 * @param trxType
	 * @param txnStatus
	 */
	private void logPPEwalletRequestResponse(EwalletRequestData requestData, EwalletResponseData resPonseData, String trxType, String txnStatus) {
		try{
			EwalletActivityLogModel eWalletLogModel = new EwalletActivityLogModel();
			
	        eWalletLogModel.seteWalletID(""+EnumEwalletType.PP.getValue());
	       	eWalletLogModel.setCustomerId(requestData.getCustomerId());
	       	
	        StringBuffer eWalletTxnRequest= new StringBuffer(); 
	        StringBuffer eWalletTxnResponse= new StringBuffer();
	        
	        if(trxType.equalsIgnoreCase(PAYPAL_CLIENT_TOKEN_TXN)){
	        	eWalletTxnRequest.append("Generate Client Token ");
	        	// Create Response String
	        	eWalletTxnResponse= new StringBuffer();
	        	if(resPonseData != null && resPonseData.getToken() != null){
	        		eWalletTxnResponse.append("ClientToken="+resPonseData.getToken());
	        	}
	        }else if(trxType.equalsIgnoreCase(PAYPAL_CREATE_CUSTOMER_TXN)){
	        	eWalletTxnRequest.append("Create customer ");
	        	// Create Response String
	        	eWalletTxnResponse= new StringBuffer();
	        	if(txnStatus.equals(PAYPAL_TXN_SUCCESS)){
	        		eWalletTxnResponse.append("Successful");
	        	}else{
	        		eWalletTxnResponse.append("Failed");
	        	}
	        }else if(trxType.equalsIgnoreCase(PAYPAL_GET_VAULT_TOKEN_TXN)){
	        	eWalletTxnRequest.append("Get Vault Token ");
	        	// Create Response String
	        	eWalletTxnResponse= new StringBuffer();
	        	if(txnStatus.equals(PAYPAL_TXN_SUCCESS)){
	        		eWalletTxnResponse.append("Vault Token created Successfully");
	        	}else{
	        		eWalletTxnResponse.append("Failed to get Vault Token");
	        	}
	        }
	        eWalletLogModel.setRequest(eWalletTxnRequest.toString());
	        eWalletLogModel.setResponse(eWalletTxnResponse.toString());
	        
	        eWalletLogModel.setTransactionType(trxType);
	        Timestamp timeNow = new Timestamp(Calendar.getInstance().getTimeInMillis()); 
	        eWalletLogModel.setCreationTimeStamp(timeNow);
	        eWalletLogModel.setStatus(txnStatus);
	        eWalletLogModel.setOrderId("");
	        EWalletLogActivity.logActivity(eWalletLogModel);
	        
			}catch(Exception e){	// Any exception then ignore
				e.printStackTrace();
		}
	}
}
