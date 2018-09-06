package com.freshdirect.fdstore.ewallet.impl.ejb;

import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.log4j.Category;

import com.braintreegateway.PaymentMethod;
import com.braintreegateway.Result;
import com.braintreegateway.exceptions.AuthenticationException;
import com.freshdirect.common.customer.EnumCardType;
import com.freshdirect.customer.EnumPaymentMethodDefaultType;
import com.freshdirect.customer.ErpCustEWalletModel;
import com.freshdirect.customer.ErpPaymentMethodI;
import com.freshdirect.customer.ErpPaymentMethodModel;
import com.freshdirect.fdstore.FDPayPalServiceException;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.customer.FDCustomerFactory;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.ewallet.EnumEwalletType;
import com.freshdirect.fdstore.ewallet.EwalletConstants;
import com.freshdirect.fdstore.ewallet.EwalletRequestData;
import com.freshdirect.fdstore.ewallet.EwalletResponseData;
import com.freshdirect.fdstore.ewallet.ValidationError;
import com.freshdirect.fdstore.ewallet.ValidationResult;
import com.freshdirect.fdstore.ewallet.impl.EWalletRuntimeException;
import com.freshdirect.fdstore.payments.util.PaymentMethodUtil;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.core.SessionBeanSupport;
import com.freshdirect.framework.util.StringUtil;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.payment.EnumPaymentMethodType;
import com.freshdirect.payment.PayPalResponse;
import com.freshdirect.payment.PaymentManager;
import com.freshdirect.payment.ewallet.gateway.ejb.EwalletActivityLogModel;
import com.freshdirect.payment.gateway.ewallet.impl.EWalletLogActivity;
import com.freshdirect.payment.service.FDPayPalService;
import com.freshdirect.payment.service.IPayPalService;

/**
 * @author Aniwesh Vatsal
 *
 */
public class PayPalServiceSessionBean extends SessionBeanSupport{
	
	
	private static final long serialVersionUID = -4137899364083573169L;
	private final static Category LOGGER = LoggerFactory.getInstance(PayPalServiceSessionBean.class);
	
	/**
	 * @param ewalletRequestData
	 * @return
	 * @throws RemoteException
	 */
	public EwalletResponseData getToken(EwalletRequestData ewalletRequestData) throws RemoteException {
		EwalletResponseData ewalletResponseData = new EwalletResponseData();
		List<ValidationError> eWalletValidationErrors = new ArrayList<ValidationError>();
		try{
			String clientToken = clientToken();
			ewalletResponseData.setToken(clientToken);	
			String tnx = EwalletConstants.PAYPAL_TXN_FAIL;
			if(ewalletResponseData.getToken() != null  && ewalletResponseData.getToken().trim().length() > 0){
				tnx = EwalletConstants.PAYPAL_TXN_SUCCESS;
			}
			logPPEwalletRequestResponse(ewalletRequestData, ewalletResponseData, EwalletConstants.PAYPAL_CLIENT_TOKEN_TXN, tnx);
		}catch(EWalletRuntimeException walletException){
			eWalletValidationErrors.add(new ValidationError("Cannot Connect", "Error while getting Request Token From PayPal"));
		}
		catch(Exception ex){
			eWalletValidationErrors.add(new ValidationError("Cannot Connect", "Error while getting Request Token From PayPal"));
		}
		
		// Check for any error
		if(eWalletValidationErrors!=null && !eWalletValidationErrors.isEmpty()){
			ValidationResult result = new ValidationResult();
			result.setErrors(eWalletValidationErrors);
			ewalletResponseData.setValidationResult(result);
			
		}
		return ewalletResponseData;
	}
	
	/**
	 * This method returns the Client Token
	 * @return
	 */
	private String clientToken() {
		String requestToken="";
		try{
			
			
			requestToken=FDPayPalService.getInstance().generateToken();
		
		}catch(AuthenticationException authenticationException){
			throw new EWalletRuntimeException("Can not connect to PayPal.");
		}catch(FDPayPalServiceException fDPayPalServiceException){
			throw new EWalletRuntimeException("Can not connect to PayPal.");
		}
		return requestToken;
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
		String deviceId="";
		List<ValidationError> eWalletValidationErrors = new ArrayList<ValidationError>();
		if(ewalletRequestData != null && ewalletRequestData.getReqParams() != null){
			if (ewalletRequestData.getReqParams().containsKey(EwalletConstants.PARAM_PP_PAYMENMETHOD_NONCE)) {
				paymentMethodNonce = ewalletRequestData.getReqParams().get(
						EwalletConstants.PARAM_PP_PAYMENMETHOD_NONCE);
			}
			if (ewalletRequestData.getReqParams().containsKey(EwalletConstants.PARAM_FIRST_NAME)) {
				firstName = ewalletRequestData.getReqParams().get(EwalletConstants.PARAM_FIRST_NAME);
			}
			if (ewalletRequestData.getReqParams().containsKey(EwalletConstants.PARAM_LAST_NAME)) {
				lastName = ewalletRequestData.getReqParams().get(EwalletConstants.PARAM_LAST_NAME);
			}
			if (ewalletRequestData.getReqParams().containsKey(EwalletConstants.PARAM_EMAILID)) {
				email = ewalletRequestData.getReqParams().get(EwalletConstants.PARAM_EMAILID);
			}
			if (ewalletRequestData.getReqParams().containsKey(EwalletConstants.PARAM_ORIGIN)) {
				origin = ewalletRequestData.getReqParams().get(EwalletConstants.PARAM_ORIGIN);
			}
			if (ewalletRequestData.getReqParams().containsKey(EwalletConstants.PARAM_DEVICEID)) {
				deviceId = ewalletRequestData.getReqParams().get(EwalletConstants.PARAM_DEVICEID);
			}
		}
		String vaultToken ="";
		try{
			vaultToken = obtainVaultToken(paymentMethodNonce,ewalletRequestData, firstName, lastName);
		}catch(EWalletRuntimeException walletException){
			eWalletValidationErrors.add(new ValidationError("Cannot Connect", "Error while obtaining Vault Token from PayPal"));
		}
		catch(Exception ex){
			eWalletValidationErrors.add(new ValidationError("Cannot Connect", "Error while obtaining Vault Token from PayPal"));
		}
		
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
			ErpPaymentMethodI paymentMethod = PaymentManager.createInstance(EnumPaymentMethodType.PAYPAL);
			paymentMethod.setName(firstName+" "+lastName);
			paymentMethod.setEmailID(email);
			paymentMethod.setAccountNumber("1111111111111111");
			paymentMethod.setCardType(EnumCardType.PAYPAL);
			paymentMethod.setAddress1("23-30 BORDEN AVE");
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
		    paymentMethod.setDeviceId(deviceId);
		     
		    ErpPaymentMethodI searchedPM = searchPPWalletCards(ewalletRequestData, paymentMethod);
		     
		 	// Add the PayPal account details to Database
			try {
				if(searchedPM != null){ // PayPal card is already paired
					FDCustomerManager.removePaymentMethod(ewalletRequestData.getFdActionInfo(), searchedPM, ewalletRequestData.isDebitCardSwitch());
				}
				
				ewalletRequestData.setPaymentechEnabled(false);
				FDCustomerManager.addPaymentMethod(ewalletRequestData.getFdActionInfo(), paymentMethod,ewalletRequestData.isPaymentechEnabled(), ewalletRequestData.isDebitCardSwitch());
		
				List<ErpPaymentMethodI> paymentMethods = FDCustomerFactory.getErpCustomer(ewalletRequestData.getCustomerId()).getPaymentMethods();
				
				
				if(paymentMethods != null && !paymentMethods.isEmpty()){
					
					if (paymentMethods.size() > 1) {
						sortPaymentMethodsByIdReserved(paymentMethods);
					}
			        if (!origin.equals(EwalletConstants.PAYPAL_ORIGIN_YOUR_ACCOUNT)) {
			    		final PrimaryKey pmPK = ( (ErpPaymentMethodModel)paymentMethods.get(0)).getPK();
			    		if (!ewalletRequestData.isDebitCardSwitch()) {
			    		FDCustomerManager.setDefaultPaymentMethod( ewalletRequestData.getFdActionInfo(), pmPK, null, false);
			    		}
			        }
					ewalletResponseData.setPaymentMethod(paymentMethods.get(0));
				}
				
			} catch (Exception exception) {
				LOGGER.error("Error While adding PayPal account details : "+exception.getMessage());
			}	
		}
		// Check for any error
		if(eWalletValidationErrors!=null && !eWalletValidationErrors.isEmpty()){
			ValidationResult result = new ValidationResult();
			result.setErrors(eWalletValidationErrors);
			ewalletResponseData.setValidationResult(result);
			
		}
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
		String deviceId = "";
		if (ewalletRequestData.getReqParams().containsKey(EwalletConstants.PARAM_DEVICEID)) {
			deviceId = ewalletRequestData.getReqParams().get(EwalletConstants.PARAM_DEVICEID);
		}

		try{
			Result<? extends PaymentMethod> vaultResult = null;
			IPayPalService payPalService = FDPayPalService.getInstance();
			PayPalResponse payPalResponse = null;

			 payPalResponse = payPalService.createCustomer(ewalletRequestData.getCustomerId(),fName,lName);
		    // Check customer record created successfully or not
		    if(payPalResponse.getStatus().equalsIgnoreCase(com.freshdirect.payment.Result.STATUS_SUCCESS)){
		    	logPPEwalletRequestResponse(ewalletRequestData, null, EwalletConstants.PAYPAL_CREATE_CUSTOMER_TXN, EwalletConstants.PAYPAL_TXN_SUCCESS);

				payPalResponse = payPalService.createPaymentMethod(payPalResponse.getCustomerId(),paymentMethodNonce,deviceId);
				
	//			PAYPAL_GET_VAULT_TOKEN_TXN
				if(payPalResponse != null){
					if(payPalResponse.getStatus().equalsIgnoreCase(com.freshdirect.payment.Result.STATUS_SUCCESS)){
						logPPEwalletRequestResponse(ewalletRequestData, null, EwalletConstants.PAYPAL_GET_VAULT_TOKEN_TXN, EwalletConstants.PAYPAL_TXN_SUCCESS);
						return payPalResponse.getToken();
					}else{
						//fail
						return null;
					}
				}else{
					logPPEwalletRequestResponse(ewalletRequestData, null, EwalletConstants.PAYPAL_GET_VAULT_TOKEN_TXN, EwalletConstants.PAYPAL_TXN_FAIL);
					return null;
				}
		    }else{
		    	logPPEwalletRequestResponse(ewalletRequestData, null, EwalletConstants.PAYPAL_CREATE_CUSTOMER_TXN, EwalletConstants.PAYPAL_TXN_FAIL);
		    	return null;
		    }
		}catch(AuthenticationException exception){
			throw new EWalletRuntimeException("Cannot connect to PayPal.");
		}catch(FDPayPalServiceException ex){
			throw new EWalletRuntimeException("Cannot connect to PayPal.");
		}catch(Exception ex){
			throw new EWalletRuntimeException("Cannot connect to PayPal.");
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
							FDCustomerManager.removePaymentMethod(ewalletRequestData.getFdActionInfo(),payment, ewalletRequestData.isDebitCardSwitch());
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
	        
	        if(trxType.equalsIgnoreCase(EwalletConstants.PAYPAL_CLIENT_TOKEN_TXN)){
	        	eWalletTxnRequest.append("Generate Client Token ");
	        	// Create Response String
	        	eWalletTxnResponse= new StringBuffer();
	        	if(resPonseData != null && resPonseData.getToken() != null){
	        		eWalletTxnResponse.append("ClientToken="+resPonseData.getToken());
	        	}
	        }else if(trxType.equalsIgnoreCase(EwalletConstants.PAYPAL_CREATE_CUSTOMER_TXN)){
	        	eWalletTxnRequest.append("Create customer ");
	        	// Create Response String
	        	eWalletTxnResponse= new StringBuffer();
	        	if(txnStatus.equals(EwalletConstants.PAYPAL_TXN_SUCCESS)){
	        		eWalletTxnResponse.append("Successful");
	        	}else{
	        		eWalletTxnResponse.append("Failed");
	        	}
	        }else if(trxType.equalsIgnoreCase(EwalletConstants.PAYPAL_GET_VAULT_TOKEN_TXN)){
	        	eWalletTxnRequest.append("Get Vault Token ");
	        	// Create Response String
	        	eWalletTxnResponse= new StringBuffer();
	        	if(txnStatus.equals(EwalletConstants.PAYPAL_TXN_SUCCESS)){
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
