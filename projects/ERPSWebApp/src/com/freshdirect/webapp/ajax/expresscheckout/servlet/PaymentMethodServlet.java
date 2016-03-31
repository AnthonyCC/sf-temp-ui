package com.freshdirect.webapp.ajax.expresscheckout.servlet;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.freshdirect.customer.ErpPaymentMethodI;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDCustomerFactory;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.ewallet.EnumEwalletType;
import com.freshdirect.fdstore.ewallet.EwalletConstants;
import com.freshdirect.webapp.ajax.BaseJsonServlet;
import com.freshdirect.webapp.ajax.data.PageAction;
import com.freshdirect.webapp.ajax.expresscheckout.data.FormDataRequest;
import com.freshdirect.webapp.ajax.expresscheckout.data.FormDataResponse;
import com.freshdirect.webapp.ajax.expresscheckout.payment.data.FormPaymentData;
import com.freshdirect.webapp.ajax.expresscheckout.payment.data.PaymentData;
import com.freshdirect.webapp.ajax.expresscheckout.payment.data.PaymentEditData;
import com.freshdirect.webapp.ajax.expresscheckout.payment.service.PaymentService;
import com.freshdirect.webapp.ajax.expresscheckout.service.FormDataService;
import com.freshdirect.webapp.ajax.expresscheckout.service.SinglePageCheckoutFacade;
import com.freshdirect.webapp.ajax.expresscheckout.validation.data.ValidationError;
import com.freshdirect.webapp.ajax.expresscheckout.validation.data.ValidationResult;
import com.freshdirect.webapp.util.StandingOrderHelper;
import com.freshdirect.webapp.util.StandingOrderUtil;

public class PaymentMethodServlet extends BaseJsonServlet {

    private static final long serialVersionUID = -7582639712245761241L;
	private static final String EWALLET_SESSION_ATTRIBUTE_NAME="EWALLET_CARD_TYPE";
	private static final String MP_EWALLET_CARD="MP_CARD";
//	private final static String EWALLET_REQ_ATTR_WALLET_TYPE_NAME = "ewalletType";
//	private final static String EWALLET_REQ_ATTR_ACTION = "action";
//	private static final String EWALLET_MP_STANDARD_CHECKOUT = "MP_Standard_Checkout";
//	private static final String MASTERPASS_WALLET_TYPE_NAME = "MP";
	private static final String WALLET_SESSION_CARD_ID = "WALLET_CARD_ID"; 
	private final String EWALLET_ERROR_CODE = "WALLET_ERROR";
    
//    private static final String MASTERPASS_REQ_ATTR_MPPREFERREDCARD="mpEwalletPreferredCard";
//    private boolean preCheckoutCallRequired = true;
//    private List<PaymentData> walletCards ; 

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response, FDUserI user) throws HttpErrorResponse {
        try {
            final FormDataRequest paymentRequestData = BaseJsonServlet.parseRequestData(request, FormDataRequest.class);
            PageAction pageAction = FormDataService.defaultService().getPageAction(paymentRequestData);
            ValidationResult validationResult = new ValidationResult();
            boolean changed = false;
            final FormDataResponse paymentSubmitResponse = FormDataService.defaultService().prepareFormDataResponse(paymentRequestData, validationResult);
            if (pageAction != null) {
                switch (pageAction) {
                    case ADD_PAYMENT_METHOD: {
                        List<ValidationError> validationErrors = PaymentService.defaultService().addPaymentMethod(paymentRequestData, request, user);
                        validationResult.getErrors().addAll(validationErrors);
                        if (validationErrors.isEmpty()) {
                            List<ErpPaymentMethodI> paymentMethods = FDCustomerFactory.getErpCustomer(user.getIdentity()).getPaymentMethods();
                            if (!paymentMethods.isEmpty()) {
                                PaymentService.defaultService().sortPaymentMethodsByIdReserved(paymentMethods);
                                String paymentId = paymentMethods.get(0).getPK().getId();
                                validationErrors = PaymentService.defaultService().selectPaymentMethod(paymentId, pageAction.actionName, request);
                                validationResult.getErrors().addAll(validationErrors);
                            }
                        }
                        changed = true;
                        break;
                    }
                    case EDIT_PAYMENT_METHOD: {
                        List<ValidationError> validationErrors = PaymentService.defaultService().editPaymentMethod(paymentRequestData, request, user);
                        validationResult.getErrors().addAll(validationErrors);
                        changed = true;
                        break;
                    }
                    case DELETE_PAYMENT_METHOD: {
                        PaymentService.defaultService().deletePaymentMethod(paymentRequestData, request);
                        changed = true;
                        if(StandingOrderHelper.isSO3StandingOrder(user)){
                        	user.getCurrentStandingOrder().setPaymentMethodId(null);	
                        }
                        break;
                    }
                    case SELECT_PAYMENT_METHOD: {
                        String paymentId = FormDataService.defaultService().get(paymentRequestData, "id");
    					if(StandingOrderHelper.isSO3StandingOrder(user)){
    						user.getCurrentStandingOrder().setPaymentMethodId(paymentId);
    					}
                        
                        // EWallet Express Checkout
                        /*String eWalletID = FormDataService.defaultService().get(paymentRequestData, "eWalletID_"+paymentId);
                        if(eWalletID != null && eWalletID.equals("1")){
                            ErpCustEWalletModel erpCustEWalletModel = FDCustomerManager.findLongAccessTokenByCustID(user.getFDCustomer().getErpCustomerPK(),"MP");
                            
                    		FDIdentity identity = user.getIdentity();

                    		//
                    		// search for the payment method with the matching ID
                    		//
                    		Collection<ErpPaymentMethodI> paymentMethods = FDCustomerManager.getPaymentMethods( identity );
                    		
                    		ErpPaymentMethodI paymentMethod = null;
                    		String mpPairedPaymentMethod="";
                    		for ( ErpPaymentMethodI item : paymentMethods ) {
                    			if ( item.getPK().getId().equals( paymentId ) ) {
                    				paymentMethod = item;
                    			}
                    			if ( item.geteWalletID() !=null && item.geteWalletID().equals("1")) {
                    				mpPairedPaymentMethod = item.getPK().getId();
                    			}
                    		}
                    		
						try {
							if (erpCustEWalletModel != null
									&& erpCustEWalletModel.getLongAccessToken() != null
									&& !erpCustEWalletModel
											.getLongAccessToken().isEmpty()) {

								if (paymentMethod == null) {
									request.setAttribute("precheckout_CardId",
											paymentId);
									request.setAttribute("mpPairedPaymentMethod", mpPairedPaymentMethod);
								} else {
									PaymentData data = createPaymentData(paymentMethod);
									request.setAttribute("paymentData",data);
								}

								request.setAttribute("action","MP_Express_Checkout");
								request.setAttribute("ewalletType", "MP");
								request.getRequestDispatcher("/api/expresscheckout/addpayment/ewalletPayment").include(request, response);
							}
						} catch (Exception e) {
								e.printStackTrace();
							}
                        	
                        }else{
                        */
                        
                        // If selected Card is not Wallet Card
                        String selectedWalletCardId="";
            			if(request.getSession().getAttribute(WALLET_SESSION_CARD_ID) != null ){
            				selectedWalletCardId = request.getSession().getAttribute(WALLET_SESSION_CARD_ID).toString();
            				if(selectedWalletCardId != null && selectedWalletCardId.length() > 0 && !selectedWalletCardId.equals(paymentId)){
            					request.getSession().removeAttribute(EWALLET_SESSION_ATTRIBUTE_NAME);
            				}else{
            					request.getSession().setAttribute(EWALLET_SESSION_ATTRIBUTE_NAME, MP_EWALLET_CARD);
            				}
            			}
                        List<ValidationError> validationErrors = PaymentService.defaultService().selectPaymentMethod(paymentId, pageAction.actionName, request);
                        validationResult.getErrors().addAll(validationErrors);
//                        }
                        changed = true;
                        break;
                    }
                    case LOAD_PAYMENT_METHOD: {
                        String paymentId = FormDataService.defaultService().get(paymentRequestData, "id");
                        PaymentEditData userPaymentMethod = PaymentService.defaultService().loadUserPaymentMethod(user, paymentId);
                        paymentSubmitResponse.getSubmitForm().getResult().put("paymentEditValue", userPaymentMethod);
                        break;
                    }
/*                    case MASTERPASS_PICK_MP_PAYMENTMETHOD: {
                    	String selectedWalletCardId ="";
                    	if(request.getSession().getAttribute(WALLET_SESSION_CARD_ID) != null ){
            				selectedWalletCardId = request.getSession().getAttribute(WALLET_SESSION_CARD_ID).toString();
            			}
                    	if(selectedWalletCardId != null && selectedWalletCardId.length() > 0 ){
	                    	 List<ValidationError> validationErrors = PaymentService.defaultService().selectPaymentMethod(selectedWalletCardId, pageAction.actionName, request);
	                         validationResult.getErrors().addAll(validationErrors);
	                         changed = true;
	                     }
                         break;
                    }*/
                    // EWallet Express Checkout
                    /*case MASTERPASS_SELECT_PAYMENTMETHOD: {
                    	
                    	ErpCustEWalletModel erpCustEWalletModel = FDCustomerManager.findLongAccessTokenByCustID(user.getFDCustomer().getErpCustomerPK(),"MP");
                    	FDIdentity identity = user.getIdentity();
                    	// Get List of all PaymentMethods for the user
                    	Collection<ErpPaymentMethodI> paymentMethods = FDCustomerManager.getPaymentMethods( identity );
                        
                    	ErpPaymentMethodI paymentMethodId = getEwalletPaymentMethodId(paymentMethods,"MP");
                    	if(paymentMethodId != null){		// Masterpass Payment Method Available so need Express Checkout Call
                    		PaymentData paymentData = createPaymentData(paymentMethodId);
                    		request.setAttribute("paymentData",paymentData);
                    		request.setAttribute("action","MP_Express_Checkout");
							request.setAttribute("ewalletType", "MP");
							request.getRequestDispatcher("/api/expresscheckout/addpayment/ewalletPayment").include(request, response);
							
							user.getShoppingCart().setPaymentMethod(paymentMethodId);
                    	}else{
                    		// Check If user is already paired with Masterpass then make precheckout call and select preferred card and make express checkout call
                    		if(erpCustEWalletModel != null && erpCustEWalletModel.getLongAccessToken() != null && !erpCustEWalletModel.getLongAccessToken().isEmpty()){
                    			List<PaymentData> walletCards = new ArrayList<PaymentData>();
                    			request.setAttribute("action", "MP_All_PayMethod_In_Ewallet");
        						request.setAttribute("ewalletType", "MP");
        						request.getRequestDispatcher("/api/expresscheckout/addpayment/ewalletPayment").include(request,response);
        						if (request.getSession().getAttribute("mpEwalletPaymentData") != null){
        							walletCards = (List<PaymentData>) request.getSession().getAttribute("mpEwalletPaymentData");
//        							preCheckoutCallRequired = false;
        							// Get the Preferred Card ID from the EWallet
        							String mpPreferredCard="";
        							if(request.getSession().getAttribute(MASTERPASS_REQ_ATTR_MPPREFERREDCARD) != null){
        								mpPreferredCard = request.getSession().getAttribute(MASTERPASS_REQ_ATTR_MPPREFERREDCARD).toString();
        							}
        							PaymentData mpPaymentData = getPreferredCardFromEWallet(walletCards,mpPreferredCard);
        							if(mpPaymentData != null && mpPaymentData.getId() !=null && mpPaymentData.getId().length() > 0){
        								request.setAttribute("precheckout_CardId",mpPaymentData.getId());
    									request.setAttribute("action","MP_Express_Checkout");
    									request.setAttribute("ewalletType", "MP");
    									request.getRequestDispatcher("/api/expresscheckout/addpayment/ewalletPayment").include(request, response);
        							}
        						}
                    		}
                    	}
                        changed = true;
                        break;
                    }*/
                    default:
                        break;
                }

                paymentSubmitResponse.getSubmitForm().setSuccess(paymentSubmitResponse.getValidationResult().getErrors().isEmpty());
                if (changed && paymentSubmitResponse.getSubmitForm().isSuccess()) {
                    Map<String, Object> singlePageCheckoutData = SinglePageCheckoutFacade.defaultFacade().loadByPageAction(user, request, pageAction, validationResult);
                    
                    FormPaymentData formPayment = (FormPaymentData) singlePageCheckoutData.get("payment");
                    
                    checkEWalletCard(formPayment,request);
                    removeOlderEwalletPaymentMethod(formPayment, request);
                    
                    // EWallet Express Checkout
                    /*FormPaymentData formPayment = (FormPaymentData) singlePageCheckoutData.get("payment");
                    List<PaymentData> payments = formPayment.getPayments();
                    PaymentData mpPaymentMethod = null;
                    
                    for(PaymentData data : payments){
                        if(data.geteWalletID()!=null && data.geteWalletID().equals("1")){
                        	mpPaymentMethod = new PaymentData();
    						mpPaymentMethod = data;
    					}
                    }
                    
                    ErpCustEWalletModel erpCustEWalletModel = FDCustomerManager.findLongAccessTokenByCustID(user.getFDCustomer().getErpCustomerPK(),"MP");	
        			List<PaymentData> walletCards = new ArrayList<PaymentData>();
        			if (erpCustEWalletModel != null	&& erpCustEWalletModel.getLongAccessToken() != null
        					&& !erpCustEWalletModel.getLongAccessToken().isEmpty()) {
        				try {
//        					if(preCheckoutCallRequired){
        						request.setAttribute("action", "MP_All_PayMethod_In_Ewallet");
        						request.setAttribute("ewalletType", "MP");
        						request.getRequestDispatcher("/api/expresscheckout/addpayment/ewalletPayment").include(request,response);
        						if (request.getSession().getAttribute("mpEwalletPaymentData") != null){
        							walletCards = (List<PaymentData>) request.getSession().getAttribute("mpEwalletPaymentData");
        						}
//        					}
        				} catch (Exception e) {
        					e.printStackTrace();
        				}
        				
        			}
        			if (erpCustEWalletModel != null	&& erpCustEWalletModel.getLongAccessToken() != null
    						&& !erpCustEWalletModel.getLongAccessToken().isEmpty()) {
        				
        				if(mpPaymentMethod !=null)
        					addPaymentMethods(walletCards, mpPaymentMethod, payments);
        				else
        					payments.addAll(walletCards);
    					
    				}*/
        			
                    paymentSubmitResponse.getSubmitForm().setResult(singlePageCheckoutData);
                }

   			if(StandingOrderHelper.isSO3StandingOrder(user)
   					&& validationResult.getErrors().isEmpty() ){
   				try {
   					StandingOrderHelper.clearSO3ErrorDetails(user.getCurrentStandingOrder(), new String[] {"PAYMENT","PAYMENT_ADDRESS"});
 					StandingOrderHelper.populateStandingOrderDetails(user.getCurrentStandingOrder(),paymentSubmitResponse.getSubmitForm().getResult());
                    user.setRefreshValidSO3(true);
 					StandingOrderUtil.createStandingOrder(request.getSession(), user.getSoTemplateCart(), user.getCurrentStandingOrder(), null);
   				} catch (FDResourceException e) {
   					BaseJsonServlet.returnHttpError(500, "Error while submit payment for user " + user.getUserId(), e);  				}
   			}
            }
            writeResponseData(response, paymentSubmitResponse);
        } catch (final Exception e) {
            BaseJsonServlet.returnHttpError(500, "Error while submit payment for user " + user.getUserId(), e);
        }
    }

    
   /**
 * @param formpaymentData
 * @param request
 */
    private void checkEWalletCard(FormPaymentData formpaymentData,HttpServletRequest request){
		if (formpaymentData != null) {
			if(request.getSession().getAttribute(EWALLET_ERROR_CODE) != null ){
				request.getSession().removeAttribute(EWALLET_ERROR_CODE);
			}
			List<PaymentData> payments = formpaymentData.getPayments();
			String session_card = "";
			if(request.getSession().getAttribute(EWALLET_SESSION_ATTRIBUTE_NAME) != null){
				session_card = request.getSession().getAttribute(EWALLET_SESSION_ATTRIBUTE_NAME).toString();
			}
			String selectedWalletCardId="";
			if(request.getSession().getAttribute(WALLET_SESSION_CARD_ID) != null ){
				selectedWalletCardId = request.getSession().getAttribute(WALLET_SESSION_CARD_ID).toString();
			}
			if(formpaymentData.getSelected() != null && formpaymentData.getSelected().equals(selectedWalletCardId)){
				for (PaymentData data : payments) {
					data.setMpLogoURL(FDStoreProperties.getMasterpassLogoURL());
					if( (session_card != null && session_card.equals(MP_EWALLET_CARD)) ){
						int ewalletId = EnumEwalletType.getEnum("MP").getValue();
						if(data.geteWalletID()!=null && data.geteWalletID().equals(""+ewalletId) && selectedWalletCardId.equals(data.getId())){
							data.setSelected(true);
							formpaymentData.setSelected(data.getId());
						}else{
							data.setSelected(false);
						}
					}
				}
			}
		}
	}
    
    /**
	 * @param formpaymentData
	 * @param request
	 */
	private void removeOlderEwalletPaymentMethod(FormPaymentData formpaymentData,HttpServletRequest request){
		if (formpaymentData != null) {
			List<PaymentData> payments = formpaymentData.getPayments();
			List<PaymentData> paymentsNew = new ArrayList<PaymentData>();
			String selectedWalletCardId="";
			if(request.getSession().getAttribute(WALLET_SESSION_CARD_ID) != null ){
				selectedWalletCardId = request.getSession().getAttribute(WALLET_SESSION_CARD_ID).toString();
			}
			for (PaymentData data : payments) {
				if(data.geteWalletID() == null){
					paymentsNew.add(data);
				}else{
					int ewalletId = EnumEwalletType.MP.getValue();
					if(data.geteWalletID()!=null && data.geteWalletID().equals(""+ewalletId) && selectedWalletCardId.equals(data.getId())){
						paymentsNew.add(data);
					}
					
					//PayPal Changes
					ewalletId = EnumEwalletType.PP.getValue();
					if(data.geteWalletID()!=null && data.geteWalletID().equals(""+ewalletId)){
						paymentsNew.add(data);
					}
				}
			}
			formpaymentData.setPayments(paymentsNew);
		}
	}
    /**
     * @param walletCards
     * @return
     */
    /*private PaymentData getPreferredCardFromEWallet(final List<PaymentData> walletCards, String mpPreferredCard){
    	if(mpPreferredCard != null){
	    	for(PaymentData data : walletCards){
	    		if(data.getId().equalsIgnoreCase(mpPreferredCard)){
	    			return data;
	    		}
	    	}
    	}
    	return null;
    }*/
    /**
	 * Method is used to remove the duplicate card display on UI
	 * @param walletCards
	 * @param mpPaymentMethod
	 * @param fdPayments
	 */
    /*private void addPaymentMethods(List<PaymentData> walletCards,PaymentData mpPaymentMethod,List<PaymentData> fdPayments ){
    	
		for(PaymentData mpdata : walletCards){
			if( !( mpdata.getAccountNumber() != null && mpdata.getAccountNumber().equalsIgnoreCase(mpPaymentMethod.getAccountNumber()) && 
				mpdata.getExpiration() != null && mpdata.getExpiration().equalsIgnoreCase(mpPaymentMethod.getExpiration()) &&
				mpdata.getBankAccountType() != null &&  mpdata.getBankAccountType().equalsIgnoreCase(mpPaymentMethod.getType()) &&
				mpdata.getNameOnCard() != null && mpdata.getNameOnCard().equalsIgnoreCase(mpPaymentMethod.getNameOnCard()) )
					) {
				fdPayments.add(mpdata);
			}
		}
	}
    */
    /**
     * @param paymentMethods
     * @param eWalletID
     * @return
     */
   /* private ErpPaymentMethodI getEwalletPaymentMethodId(final Collection<ErpPaymentMethodI> paymentMethods, final String eWalletID){
    	for(ErpPaymentMethodI paymentMethod:paymentMethods){
    		if(paymentMethod.geteWalletID()!=null && paymentMethod.geteWalletID().equalsIgnoreCase("1") && eWalletID.equalsIgnoreCase("MP")){
    			return paymentMethod;
    		}
    	}
    	return null;
    }*/
    
    /*private PaymentData createPaymentData(final ErpPaymentMethodI payment) {
        PaymentData paymentData = new PaymentData();
        paymentData.setId(payment.getPK().getId());
        paymentData.setTitle(payment.getName());
        if (payment.getCardType() != null) {
            paymentData.setType(payment.getCardType().getDisplayName());
        }
        paymentData.setNameOnCard(payment.getName());
        String maskedAccountNumber = payment.getMaskedAccountNumber();
        if (maskedAccountNumber != null) {
            if (8 < maskedAccountNumber.length()) {
                maskedAccountNumber = maskedAccountNumber.substring(maskedAccountNumber.length() - 8);
            }
            paymentData.setAccountNumber(maskedAccountNumber);
        }
        paymentData.setBestNumber(payment.getBestNumberForBillingInquiries());
        if (payment.getExpirationDate() != null) {
            paymentData.setExpiration(DateUtil.getCreditCardExpiryDate(payment.getExpirationDate()));
        }
        paymentData.setAddress1(payment.getAddress1());
        paymentData.setAddress2(payment.getAddress2());
        paymentData.setApartment(payment.getApartment());
        paymentData.setCity(payment.getCity());
        paymentData.setZip(payment.getZipCode());
        paymentData.setState(payment.getState());
        paymentData.setAbaRouteNumber(payment.getAbaRouteNumber());
        if (payment.getBankAccountType() != null) {
            paymentData.setBankAccountType(payment.getBankAccountType().getDescription());
        }
        paymentData.setBankName(payment.getBankName());
        
        paymentData.seteWalletID(payment.geteWalletID());
        paymentData.setVendorEWalletID(payment.getVendorEWalletID());
        return paymentData;
    }*/
    
    @Override
    protected int getRequiredUserLevel() {
        return FDUserI.SIGNED_IN;
    }

    @Override
    protected boolean synchronizeOnUser() {
        return false;
    }
}
