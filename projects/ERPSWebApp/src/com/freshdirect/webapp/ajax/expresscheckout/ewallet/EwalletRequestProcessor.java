package com.freshdirect.webapp.ajax.expresscheckout.ewallet;

import com.freshdirect.fdstore.ewallet.EnumEwalletType;
import com.freshdirect.fdstore.ewallet.EwalletConstants;
import com.freshdirect.fdstore.ewallet.EwalletRequestData;
import com.freshdirect.fdstore.ewallet.EwalletResponseData;
import com.freshdirect.fdstore.ewallet.EwalletServiceFactory;
import com.freshdirect.fdstore.ewallet.IEwallet;


/**
 * @author Aniwesh Vatsal
 *
 */
public class EwalletRequestProcessor {

	/**
	 * @param ewalletRequestData
	 * @return
	 */
	public EwalletResponseData processRequest(EwalletRequestData ewalletRequestData) throws Exception{
		EwalletResponseData ewalletResponseData = null;
		
		IEwallet ewallet = null;
		if(ewalletRequestData != null){
			EwalletServiceFactory ewalletServiceFactory = new EwalletServiceFactory();
			ewallet = ewalletServiceFactory.getEwalletService(ewalletRequestData);
		}
		
		// For Masterpass 
		if(ewallet != null && EnumEwalletType.MP.getName().equals(ewalletRequestData.geteWalletType())){
			/*if(ewalletRequestData.geteWalletAction().equalsIgnoreCase(EWALLET_MP_PAIRING_CHECKOUT)){
				ewalletResponseData  = processMasterpassStartPairing(ewallet,ewalletRequestData);
			}
			if(ewalletRequestData.geteWalletAction().equalsIgnoreCase(EWALLET_MP_PAIRING_END)){
				ewalletResponseData  = checkoutProcess(ewallet,ewalletRequestData);
			}
			if(ewalletRequestData.geteWalletAction().equalsIgnoreCase(EWALLET_MP_EXPRESS_CHECKOUT)){
				ewalletResponseData  = expressCheckout(ewallet,ewalletRequestData);
			}
			if (ewalletRequestData.geteWalletAction().equalsIgnoreCase(
					EWALLET_MP_GET_ALL_PAYMETRHOD_IN_EWALLET)) {
				ewalletResponseData = getAllPayMethodInEwallet(ewallet,
						ewalletRequestData);
			}
			if(ewalletRequestData.geteWalletAction().equalsIgnoreCase(EWALLET_MP_CONNECT_START)){
				ewalletResponseData  = connect(ewallet,ewalletRequestData);
			}
			if(ewalletRequestData.geteWalletAction().equalsIgnoreCase(EWALLET_MP_CONNECT_END)){
				ewalletResponseData  = connectComplete(ewallet,ewalletRequestData);
			}
			if(ewalletRequestData.geteWalletAction().equalsIgnoreCase(EWALLET_MP_DISCONNECT)){
				ewalletResponseData  = disconnect(ewallet,ewalletRequestData);
			}*/
			if(EwalletConstants.EWALLET_MP_STANDARD_CHECKOUT.equals(ewalletRequestData.geteWalletAction())){
				ewalletResponseData  = standardCheckout(ewallet,ewalletRequestData);
			}
			if(EwalletConstants.EWALLET_MP_STANDARD_CHECKOUT_DATA.equals(ewalletRequestData.geteWalletAction())){
				ewalletResponseData  = standardCheckoutData(ewallet,ewalletRequestData);
			}
		}
		
		if(ewallet != null && EnumEwalletType.PP.getName().equals(ewalletRequestData.geteWalletType())){
			if(EwalletConstants.EWALLET_PP_START_PAIRING.equals(ewalletRequestData.geteWalletAction()) || 
					EwalletConstants.EWALLET_PP_START_CONNECTING.equalsIgnoreCase(ewalletRequestData.geteWalletAction()) ||
					EwalletConstants.GET_PP_DEVICE_DATA.equalsIgnoreCase(ewalletRequestData.geteWalletAction())){
				ewalletResponseData  = pairingEwallet(ewallet,ewalletRequestData);
			}
			if(EwalletConstants.EWALLET_PP_END_PAIRING.equals(ewalletRequestData.geteWalletAction()) || 
					EwalletConstants.EWALLET_PP_END_CONNECTING.equalsIgnoreCase(ewalletRequestData.geteWalletAction())){
				ewalletResponseData  = obtainVaultToken(ewallet,ewalletRequestData);
			}
			if(EwalletConstants.EWALLET_PP_WALLET_DISCONNECT.equals(ewalletRequestData.geteWalletAction())){
				ewalletResponseData  = disconnectPayPalAccount(ewallet,ewalletRequestData);
			}
		}
		
		return ewalletResponseData;
	}

	private EwalletResponseData pairingEwallet(IEwallet ewallet,
			EwalletRequestData ewalletRequestData) throws Exception {
		EwalletResponseData ewalletResponseData = ewallet.getToken(ewalletRequestData);
		return ewalletResponseData;
	}
	
	private EwalletResponseData obtainVaultToken(IEwallet ewallet,
			EwalletRequestData ewalletRequestData) throws Exception {
		EwalletResponseData ewalletResponseData = ewallet.addPayPalWallet(ewalletRequestData);
		return ewalletResponseData;
	}

	private EwalletResponseData disconnectPayPalAccount(IEwallet ewallet,EwalletRequestData ewalletRequestData) throws Exception{
		// get token
		EwalletResponseData ewalletResponseData = ewallet.disconnect(ewalletRequestData);
		return ewalletResponseData;
	}


	/**
	 * @param ewallet
	 * @param ewalletRequestData
	 * @return
	 * @throws Exception
	 */
	private EwalletResponseData standardCheckout(IEwallet ewallet,EwalletRequestData ewalletRequestData)throws Exception{
		// get Standard Checkout token
		EwalletResponseData ewalletResponseData = ewallet.preStandardCheckout(ewalletRequestData);
		return ewalletResponseData;
	}
	
	/**
	 * @param ewallet
	 * @param ewalletRequestData
	 * @return
	 * @throws Exception
	 */
	private EwalletResponseData standardCheckoutData(final IEwallet ewallet,final EwalletRequestData ewalletRequestData)throws Exception{
		// get Standard Checkout Data
		EwalletResponseData ewalletResponseData = ewallet.standardCheckout(ewalletRequestData);
		
		return ewalletResponseData;
	}
	/**
	 * @param ewallet
	 * @param ewalletRequestData
	 * @return
	 */
	/*private EwalletResponseData processMasterpassStartPairing(IEwallet ewallet,EwalletRequestData ewalletRequestData)throws Exception{
		// get token
		EwalletResponseData ewalletResponseData = ewallet.getToken(ewalletRequestData);
		
		if(ewalletResponseData!=null){
			// Check for errors, if any error exists then re-direct to Checkout screen
			if(ewalletResponseData.getValidationResult()!= null && ewalletResponseData.getValidationResult().getErrors()!=null && !ewalletResponseData.getValidationResult().getErrors().isEmpty()){
				ewalletRequestData.getResponse().sendRedirect("/expressco/checkout.jsp");
			}
		}
		return ewalletResponseData;
	}*/
	
	/**
	 * @param ewallet
	 * @param ewalletRequestData
	 * @return
	 */
	/*private EwalletResponseData checkoutProcess(IEwallet ewallet,EwalletRequestData ewalletRequestData)throws Exception{
		// get token
		EwalletResponseData ewalletResponseData = ewallet.checkout(ewalletRequestData);
		if(ewalletResponseData.getValidationResult()!=null){
			if(ewalletResponseData.getValidationResult().getErrors()!=null && !ewalletResponseData.getValidationResult().getErrors().isEmpty()){
				System.out.println("There is error..");
			}
		}else{
			ewalletRequestData.getRequest().getSession().setAttribute(MASTERPASS_TRANSACTIONID, ewalletResponseData.getTransactionId());
			ewalletRequestData.getRequest().setAttribute(MASTERPASS_REQ_ATTR_ACTION_COMPLETED, MASTERPASS_REQ_ATTR_ACTION_COMPLETED_VALUE);
			ewalletRequestData.getUser().getShoppingCart().setPaymentMethod(ewalletResponseData.getPaymentMethod());
			ewalletRequestData.getRequest().getRequestDispatcher(ewalletResponseData.getRedirectUrl()).forward(ewalletRequestData.getRequest(), ewalletRequestData.getResponse());
		}
		return ewalletResponseData;
	}*/
	
	/**
	 * @param ewallet
	 * @param ewalletRequestData
	 * @return
	 */
	/*private EwalletResponseData expressCheckout(IEwallet ewallet,EwalletRequestData ewalletRequestData)throws Exception{

		if(ewalletRequestData.getRequest().getAttribute(MASTERPASS_REQ_ATTR_PAYMENT) !=null ){
			PaymentData paymentData =(PaymentData)ewalletRequestData.getRequest().getAttribute(MASTERPASS_REQ_ATTR_PAYMENT);
			mapPaymentData(paymentData, ewalletRequestData);
		}
		
		// Do express checkout
		EwalletResponseData ewalletResponseData = ewallet.expressCheckout(ewalletRequestData);
		if(ewalletResponseData.getTransactionId()!=null){
			ewalletRequestData.getRequest().getSession().setAttribute(MASTERPASS_TRANSACTIONID, ewalletResponseData.getTransactionId());
			if(ewalletResponseData.getPaymentMethod()!=null){
				ewalletRequestData.getUser().getShoppingCart().setPaymentMethod(ewalletResponseData.getPaymentMethod());
			}
		}

		if(ewalletRequestData.getPaymentData() ==null){
			ewalletRequestData.getRequest().setAttribute(MASTERPASS_REQ_ATTR_INVALID_PAYMENT, MASTERPASS_BOOLEAN_TRUE);
		}
		
		return ewalletResponseData;
	}*/
	
	/**
	 * @param ewallet
	 * @param ewalletRequestData
	 * @return
	 * @throws Exception 
	 */
	/*private EwalletResponseData getAllPayMethodInEwallet(IEwallet ewallet,
			EwalletRequestData ewalletRequestData) throws Exception {
		EwalletResponseData ewalletResponseData = ewallet
				.getAllPayMethodInEwallet(ewalletRequestData);
		
		if(ewalletResponseData.getPaymentDatas()!=null && ewalletResponseData.getPaymentDatas().size() > 0){
			ewalletRequestData.getRequest().getSession().setAttribute(MASTERPASS_REQ_ATTR_MPPAYMENTS, mapPaymentDataList(ewalletResponseData));
			ewalletRequestData.getRequest().getSession().setAttribute(MASTERPASS_REQ_ATTR_MPPREFERREDCARD,ewalletResponseData.getPreferredMPCard() );
		}
		return ewalletResponseData;
	}*/
	/**
	 * 
	 * @param ewallet
	 * @param ewalletRequestData
	 * @return
	 * @throws Exception
	 */
	/*private EwalletResponseData connect(IEwallet ewallet,EwalletRequestData ewalletRequestData) throws Exception{
		// get token
		EwalletResponseData ewalletResponseData = ewallet.connect(ewalletRequestData);
		if(ewalletResponseData!=null){
			// Check for errors, if any error exists then re-direct to payment_information screen
			if(ewalletResponseData.getValidationResult()!= null && ewalletResponseData.getValidationResult().getErrors()!=null && !ewalletResponseData.getValidationResult().getErrors().isEmpty()){
				ewalletRequestData.getResponse().sendRedirect("/your_account/payment_information.jsp");
			}
		}
		return ewalletResponseData;
	}*/
	
	/**
	 * 
	 * @param ewallet
	 * @param ewalletRequestData
	 * @return
	 * @throws Exception
	 */
	/*private EwalletResponseData connectComplete(IEwallet ewallet,EwalletRequestData ewalletRequestData) throws Exception{

		EwalletResponseData ewalletResponseData = ewallet.connectComplete(ewalletRequestData);
		if(ewalletResponseData.getValidationResult()!=null){
			if(ewalletResponseData.getValidationResult().getErrors()!=null && !ewalletResponseData.getValidationResult().getErrors().isEmpty()){
				System.out.println("There is error..");
			}
		}else{
			ewalletRequestData.getResponse().sendRedirect(ewalletResponseData.getRedirectUrl());
		}
		return ewalletResponseData;
	}
	
	private EwalletResponseData disconnect(IEwallet ewallet,EwalletRequestData ewalletRequestData) throws Exception{
		// get token
		EwalletResponseData ewalletResponseData = ewallet.disconnect(ewalletRequestData);
		return ewalletResponseData;
	}*/
	
	/**
	 * @param paymentData
	 * @param ewalletRequestData
	 */
	/*private void mapPaymentData(PaymentData paymentData, EwalletRequestData ewalletRequestData){
		
		if(paymentData!=null){
			com.freshdirect.fdstore.ewallet.PaymentData paymentData2 = new com.freshdirect.fdstore.ewallet.PaymentData();
			paymentData2.setAbaRouteNumber(paymentData.getAbaRouteNumber());
			paymentData2.setAccountNumber(paymentData.getAccountNumber());
			paymentData2.setAddress1(paymentData.getAddress1());
			paymentData2.setAddress2(paymentData.getAddress2());
			paymentData2.setApartment(paymentData.getApartment());
			paymentData2.setBankAccountType(paymentData.getBankAccountType());
			paymentData2.setBankName(paymentData.getBankName());
			paymentData2.setBestNumber(paymentData.getBestNumber());
			paymentData2.setCity(paymentData.getCity());
			paymentData2.seteWalletID(paymentData.geteWalletID());
			paymentData2.setExpiration(paymentData.getExpiration());
			paymentData2.setId(paymentData.getId());
			paymentData2.setNameOnCard(paymentData.getNameOnCard());
			paymentData2.setSelected(paymentData.isSelected());
			paymentData2.setState(paymentData.getState());
			paymentData2.setTitle(paymentData.getTitle());
			paymentData2.setType(paymentData.getType());
			paymentData2.setVendorEWalletID(paymentData.getVendorEWalletID());
			paymentData2.setZip(paymentData.getZip());
			
			ewalletRequestData.setPaymentData(paymentData2);
		}
	}*/
	/**
	 * @param paymentData
	 * @param ewalletRequestData
	 */
	/*private List<PaymentData> mapPaymentDataList(EwalletResponseData ewalletResponseData){
		
		List<PaymentData> paymentDataList = new ArrayList<PaymentData>();
		if(ewalletResponseData!=null && !ewalletResponseData.getPaymentDatas().isEmpty()) {
			for(com.freshdirect.fdstore.ewallet.PaymentData mpData:ewalletResponseData.getPaymentDatas()){
				PaymentData data = new PaymentData();
				data.setAbaRouteNumber(mpData.getAbaRouteNumber());
				data.setAccountNumber(mpData.getAccountNumber());
				data.setAddress1(mpData.getAddress1());
				data.setAddress2(mpData.getAddress2());
				data.setApartment(mpData.getApartment());
				data.setBankAccountType(mpData.getBankAccountType());
				data.setBankName(mpData.getBankName());
				data.setBestNumber(mpData.getBestNumber());
				data.setCity(mpData.getCity());
				data.seteWalletID(mpData.geteWalletID());
				data.setExpiration(mpData.getExpiration());
				data.setId(mpData.getId());
				data.setNameOnCard(mpData.getNameOnCard());
				data.setSelected(mpData.isSelected());
				data.setState(mpData.getState());
				data.setTitle(mpData.getTitle());
				data.setType(mpData.getType());
				data.setVendorEWalletID(mpData.getVendorEWalletID());
				data.setZip(mpData.getZip());
				paymentDataList.add(data);
			}
			
		}
		return paymentDataList;
	}*/
}
