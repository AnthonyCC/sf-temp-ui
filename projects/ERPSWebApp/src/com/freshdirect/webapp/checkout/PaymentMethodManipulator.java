package com.freshdirect.webapp.checkout;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.PageContext;

import org.apache.log4j.Category;

import com.freshdirect.common.customer.EnumCardType;
import com.freshdirect.customer.EnumAccountActivityType;
import com.freshdirect.customer.EnumAlertType;
import com.freshdirect.customer.EnumPaymentMethodDefaultType;
import com.freshdirect.customer.EnumPaymentType;
import com.freshdirect.customer.ErpAuthorizationException;
import com.freshdirect.customer.ErpAuthorizationModel;
import com.freshdirect.customer.ErpPaymentMethodI;
import com.freshdirect.customer.ErpPaymentMethodModel;
import com.freshdirect.customer.ErpTransactionException;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.customer.FDActionInfo;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.fdstore.customer.FDCustomerCreditUtil;
import com.freshdirect.fdstore.customer.FDCustomerFactory;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.ewallet.EnumEwalletType;
import com.freshdirect.fdstore.rollout.EnumRolloutFeature;
import com.freshdirect.fdstore.rollout.FeatureRolloutArbiter;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.util.NVL;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionError;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.payment.EnumPaymentMethodType;
import com.freshdirect.webapp.ajax.BaseJsonServlet;
import com.freshdirect.webapp.ajax.BaseJsonServlet.HttpErrorResponse;
import com.freshdirect.webapp.ajax.expresscheckout.data.FormDataRequest;
import com.freshdirect.webapp.ajax.expresscheckout.service.FormDataService;
import com.freshdirect.webapp.features.service.FeaturesService;
import com.freshdirect.webapp.taglib.fdstore.AccountActivityUtil;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;
import com.freshdirect.webapp.taglib.fdstore.PaymentMethodName;
import com.freshdirect.webapp.taglib.fdstore.PaymentMethodUtil;
import com.freshdirect.webapp.taglib.fdstore.SessionName;
import com.freshdirect.webapp.taglib.fdstore.SystemMessageList;
import com.freshdirect.webapp.taglib.fdstore.UserUtil;
import com.freshdirect.webapp.util.StandingOrderHelper;

public class PaymentMethodManipulator extends CheckoutManipulator {
	private static Category		LOGGER	= LoggerFactory.getInstance( PaymentMethodManipulator.class );

	private static final String EWALLET_SESSION_ATTRIBUTE_NAME="EWALLET_CARD_TYPE";
	private static final String MP_EWALLET_CARD="MP_CARD";
	
	public PaymentMethodManipulator(PageContext context, ActionResult result, String actionName) {
		super(context, result, actionName);
	}

	public void setPaymentMethod() throws FDResourceException {
		FDUserI user = getUser();
		String paymentId = request.getParameter( "paymentMethodList" );
		String billingRef = request.getParameter( "billingRef" );
		String isAccountLevel = request.getParameter( "isAccountLevel" );
		setPaymentMethod(paymentId, billingRef, request, session, result, actionName, isAccountLevel);
	}
	
	public static void setPaymentMethod(String paymentId, String billingRef, HttpServletRequest request, HttpSession session, ActionResult result, String actionName, String isAccountLevel) throws FDResourceException {
		FDUserI user = (FDUserI) session.getAttribute( SessionName.USER );	
		boolean makeGoodOrder = false;
		boolean addOnOrder = false;
		String referencedOrder = "";
		String app = (String) session.getAttribute( SessionName.APPLICATION );
		if ( "CALLCENTER".equalsIgnoreCase( app ) ) {
			makeGoodOrder = request.getParameter( "makeGoodOrder" ) != null;
			referencedOrder = NVL.apply( request.getParameter( "referencedOrder" ), "" ).trim();
			if ( makeGoodOrder && "".equals( referencedOrder ) ) {
				result.addError( true, "referencedOrder", "Reference Order number is required for a make good order" );
				return;
			}
		} else if (user.getMasqueradeContext()!=null && user.getMasqueradeContext().getMakeGoodFromOrderId()!=null) {
			referencedOrder = user.getMasqueradeContext().getMakeGoodFromOrderId();
			makeGoodOrder = true;
		} else if (user.getMasqueradeContext()!=null && user.getMasqueradeContext().getParentOrderId()!=null) {
			referencedOrder = user.getMasqueradeContext().getParentOrderId();
			addOnOrder = true;
		}
		setPaymentMethod( request, session, (FDUserI) session.getAttribute( SessionName.USER ), result, actionName, paymentId, billingRef, makeGoodOrder, addOnOrder, referencedOrder, isAccountLevel );
	}

	private void setNoPaymentMethod( HttpServletRequest request, ActionResult result ) throws FDResourceException {
		FDCartModel cart = getCart();
		String billingRef = request.getParameter( "billingRef" );
		boolean makeGoodOrder = false;
		String referencedOrder = "";
		String app = (String) session.getAttribute( SessionName.APPLICATION );
		FDUserI user = getUser();
		
		if ( "CALLCENTER".equalsIgnoreCase( app ) ) {
			makeGoodOrder = request.getParameter( "makeGoodOrder" ) != null;
			referencedOrder = NVL.apply( request.getParameter( "referencedOrder" ), "" ).trim();
			if ( makeGoodOrder && "".equals( referencedOrder ) ) {
				result.addError( true, "referencedOrder", "Reference Order number is required for a make good order" );
				return;
			}
		} else if (user.getMasqueradeContext()!=null && user.getMasqueradeContext().getMakeGoodFromOrderId()!=null) {
			makeGoodOrder = true;
			referencedOrder = user.getMasqueradeContext().getMakeGoodFromOrderId();
		}else if (cart.getSelectedGiftCards() == null || cart.getSelectedGiftCards().size() == 0){
			result.addError( new ActionError( "paymentMethodList", "You must select a payment method." ) );
			return;
		}

		setNoPaymentMethod(session, getUser(), cart, getActionName(), billingRef, referencedOrder, makeGoodOrder, result);
	}

	public static void setNoPaymentMethod(HttpSession session, FDUserI user, FDCartModel cart, String actionName, String billingRef, String referencedOrder, boolean makeGoodOrderEnabled,
			ActionResult result) throws FDResourceException {
		ErpPaymentMethodI paymentMethod = PaymentMethodUtil.createGiftCardPaymentMethod(user);
		paymentMethod.setBillingRef( billingRef );
		paymentMethod.setPaymentType(makeGoodOrderEnabled ? EnumPaymentType.MAKE_GOOD : EnumPaymentType.REGULAR);
		paymentMethod.setReferencedOrder( referencedOrder );
		cart.setPaymentMethod( paymentMethod );
		setCart(cart, user, actionName, session);
		user.setPostPromoConflictEnabled(true);
		user.updateUserState();
	}
	
	private static void setPaymentMethod( HttpServletRequest request, HttpSession session, FDUserI user, ActionResult result, String actionName, String paymentId, String billingRef, boolean makeGoodOrder, boolean addOnOrder, String referencedOrder, String isAccountLevel ) throws FDResourceException {
		//
		// check for a valid payment ID
		//
		if ( ( paymentId == null ) || ( "".equals( paymentId ) ) ) {
			result.addError( new ActionError( "paymentMethodList", "You must select a payment method." ) );
			return;
		}
		boolean paymentSetAsDefault = false;
		try {
			paymentSetAsDefault = Boolean.parseBoolean(FormDataService.defaultService().get(BaseJsonServlet.parseRequestData(request, FormDataRequest.class), "paymentSetAsDefault"));
		} catch (HttpErrorResponse e) {
			LOGGER.error("Error parsing request for paymentSetAsDefault");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        if (paymentSetAsDefault) {
        	//set as default call
        }

		FDIdentity identity = user.getIdentity();

		//
		// search for the payment method with the matching ID
		//
		Collection<ErpPaymentMethodI> paymentMethods = FDCustomerManager.getPaymentMethods( identity );
		
		ErpPaymentMethodI paymentMethod = null;

		for ( ErpPaymentMethodI item : paymentMethods ) {
			if ( item.getPK().getId().equals( paymentId ) ) {
				paymentMethod = item;
				break;
			}
		}

		if ( paymentMethod == null ) {
			result.addError( new ActionError( "paymentMethodList", SystemMessageList.MSG_REQUIRED ) );
			return;
		}

		//
		// set payment in cart and store cart if valid payment found
		//
        PaymentMethodUtil.validatePaymentMethod(request, paymentMethod, result, user, false, EnumAccountActivityType.PAYMENT_METHOD_VERIFICATION);

		//Checking for CC a/c's or at least one valid CC. If NO, restricting the customer to place order using E-check
		String app = (String) session.getAttribute( SessionName.APPLICATION);
		if (!"CALLCENTER".equalsIgnoreCase(app) && !makeGoodOrder && EnumPaymentMethodType.ECHECK.equals(paymentMethod.getPaymentMethodType()) && result.isSuccess()) {
			int numCreditCards=0;
			boolean isValidCreditCardAvailable = false;
			for (ErpPaymentMethodI paymentM : paymentMethods) {
        		if (EnumPaymentMethodType.CREDITCARD.equals(paymentM.getPaymentMethodType())) {
        			numCreditCards++;
	        	  	ActionResult tempResult=new ActionResult();
					PaymentMethodUtil.validatePaymentMethod(request, paymentM, tempResult, user,false,EnumAccountActivityType.UNKNOWN);
					if(null == tempResult.getError("expiration") || "".equals(tempResult.getError("expiration"))){
						isValidCreditCardAvailable = true;
						break;
					}
				}
        	}
			if (numCreditCards<1){
				LOGGER.debug("No CC Account in Customer payment methods: "+numCreditCards);
	        		result.addError(new ActionError("payment_method",SystemMessageList.MSG_NOCC_ACCOUNT_NUMBER));
			} else {
				if(!isValidCreditCardAvailable){
					result.addError(new ActionError("expiration",SystemMessageList.MSG_CC_EXPIRED_ACT_NUMBER));
				}
			}
		}

		FDCartModel cart = getCart(user, actionName);
		// EPT payment is not allowed for Standing order 
		if(!StandingOrderHelper.isSO3StandingOrder(user)){
			if(FeaturesService.defaultService().isFeatureActive(EnumRolloutFeature.checkout2_0, request.getCookies(), user)){
				// if user made a successful EBT order, isEbtAccepted flag did not populate at new cart creation correctly -> so making sure this is populated freshly
           		 if (user.getShoppingCart().getDeliveryAddress() != null) {
               	 DeliveryAddressManipulator.checkAndSetEbtAccepted(user.getShoppingCart().getDeliveryAddress().getZipCode(), user, user.getShoppingCart());
            	}	
			}
			
			if(null !=paymentMethod && EnumPaymentMethodType.EBT.equals(paymentMethod.getPaymentMethodType())&& !user.isEbtAccepted()/*!(cart.getDeliveryAddress() instanceof ErpDepotAddressModel)*/){
				/*if(null ==getUser().getShoppingCart().getPaymentMethod() || !EnumPaymentMethodType.EBT.equals(getUser().getShoppingCart().getPaymentMethod().getPaymentMethodType()) ||
						!(getUser().getShoppingCart() instanceof FDModifyCartModel)){*/	
					
					result.addError(new ActionError("ebtPaymentNotAllowed",SystemMessageList.MSG_EBT_NOT_ALLOWED));
					if(null!= cart.getDeliveryAddress() && cart.getDeliveryAddress().isEbtAccepted()){ 
						result.addError(new ActionError("ebtPaymentNotAllowed",SystemMessageList.MSG_EBT_NOT_ALLOWED_UNSETTLED_ORDERS));
					}
					if(user.hasEBTAlert()){
						result.addError(new ActionError("ebtPaymentNotAllowed",MessageFormat.format(SystemMessageList.MSG_EBT_NOT_ALLOWED_ON_ALERT, 
			            		new Object[] { UserUtil.getCustomerServiceContact(request)})));
					}
	//			}
			 }
		}
			
		if (!FeaturesService.defaultService().isFeatureActive(EnumRolloutFeature.checkout2_0, request.getCookies(), user) || result.isSuccess()) {
			paymentMethod.setBillingRef( billingRef );
			if(makeGoodOrder)
				paymentMethod.setPaymentType(EnumPaymentType.MAKE_GOOD);
			else if(addOnOrder)
				paymentMethod.setPaymentType(EnumPaymentType.ADD_ON_ORDER);
			else 
				paymentMethod.setPaymentType(EnumPaymentType.REGULAR );
			
			paymentMethod.setReferencedOrder( referencedOrder );
			cart.setPaymentMethod( paymentMethod );
				setCart(cart, user, actionName, session);
	
			//
				// set default payment method and check for unique billing address,
				// if
			// required
			//
			FDActionInfo info = AccountActivityUtil.getActionInfo( session );
			final PrimaryKey pmPK = ( (ErpPaymentMethodModel)paymentMethod ).getPK();
			// Do not set MP Ewallet card as default Payment Method
		
		if(!FeatureRolloutArbiter.isFeatureRolledOut(EnumRolloutFeature.debitCardSwitch, user) && (paymentMethod.geteWalletID() == null || paymentMethod.geteWalletID().equals(""+EnumEwalletType.PP.getValue()))){
				FDCustomerManager.setDefaultPaymentMethod( info, pmPK, null, false );
			}else{
			if(null != isAccountLevel && isAccountLevel.equalsIgnoreCase("Y")){
				ErpAuthorizationModel auth = null;
				if(paymentMethod.getPaymentMethodType().equals(EnumPaymentMethodType.ECHECK) || paymentMethod.getPaymentMethodType().equals(EnumPaymentMethodType.CREDITCARD) 
						|| paymentMethod.getPaymentMethodType().equals(EnumPaymentMethodType.DEBITCARD)){
				if(paymentMethod.getPaymentMethodType().equals(EnumPaymentMethodType.ECHECK)){
					if(!FDCustomerManager.isECheckRestricted(info.getIdentity()) && !FDCustomerManager.isOnAlert(info.getIdentity().getErpCustomerPK(), EnumAlertType.ECHECK.getName())){
						result.addError(new ActionError("verificationFailed",SystemMessageList.MSG_DEFAULT_PAYMENT_VERIVICATION_FAILURE));
					}else{
					auth = FDCustomerManager.verifyCard(info, paymentMethod, true);
					}
				}else if(paymentMethod.getPaymentMethodType().equals(EnumPaymentMethodType.CREDITCARD) || paymentMethod.getPaymentMethodType().equals(EnumPaymentMethodType.DEBITCARD)){
					try {
						if(paymentMethod.isAvsCkeckFailed() && !paymentMethod.isBypassAVSCheck()){
							result.addError(new ActionError("avsFailed",SystemMessageList.MSG_DEFAULT_PAYMENT_VERIVICATION_FAILURE));
						}else if(null != paymentMethod.getExpirationDate() && paymentMethod.getExpirationDate().before(java.util.Calendar.getInstance().getTime())){
							result.addError(new ActionError("cardExpired",SystemMessageList.MSG_CARD_EXPIRATION_DATE));
						}else{
						auth = FDCustomerManager.verify(info, paymentMethod);
						}
					} catch (ErpTransactionException e) {
						LOGGER.error(e);
					} catch (ErpAuthorizationException e) {
						LOGGER.error(e);
					}
				}
				if(null == auth || !auth.isApproved()){
					result.addError(new ActionError("verificationFailed",SystemMessageList.MSG_DEFAULT_PAYMENT_VERIVICATION_FAILURE));
				}
				}
				FDCustomerManager.setDefaultPaymentMethod( info, pmPK, EnumPaymentMethodDefaultType.DEFAULT_CUST, true );
			user.refreshFdCustomer();
			}
			
				/*
				 * if ( user.isDepotUser() ) { if (
				 * user.isEligibleForSignupPromotion() ) { if (
				 * FDCustomerManager.checkBillToAddressFraud( info, paymentMethod )
				 * ) {
				 * 
				 * session.setAttribute( SessionName.SIGNUP_WARNING,
				 * MessageFormat.format( SystemMessageList.MSG_NOT_UNIQUE_INFO, new
				 * Object[] { user.getCustomerServiceContact() } ) );
				 * 
				 * } } }
				 */
	
				FDSessionUser currentUser = (FDSessionUser) user;
			currentUser.setPostPromoConflictEnabled( true );
			currentUser.updateUserState();
			session.setAttribute( SessionName.USER, currentUser );
		}
		}
	}

	public void performSetPaymentMethod() throws FDResourceException {
		setPaymentMethod();
		if ( result.isSuccess() ) {
			applyCustomerCredits();
		}
	}

	public void performSetNoPaymentMethod( ) throws FDResourceException {
		setNoPaymentMethod( request, result );
		if ( result.isSuccess() ) {
			applyCustomerCredits();
		}
	}

	public void performAddAndSetPaymentMethod() throws FDResourceException {
		//
		// add the payment method
		//
		FDIdentity identity = getIdentity();
		if ( identity == null ) {
		    result.addError( new ActionError( "unexpected_error", "User Identity cannot be Null" ) );
		    return;
		}

		ErpPaymentMethodI paymentMethod = PaymentMethodUtil.processForm( request, result, identity );
		if ( result.isSuccess() ) {
            performAddAndSetPaymentMethod(request, session, getUser(), result, paymentMethod, actionName);
		}


	}

    public static void performAddAndSetPaymentMethod(HttpServletRequest request, HttpSession session, FDUserI user, ActionResult result, ErpPaymentMethodI paymentMethod,
            String actionName) throws FDResourceException {
        PaymentMethodUtil.validatePaymentMethod( request, paymentMethod, result, user,true,EnumAccountActivityType.ADD_PAYMENT_METHOD );
        if ( EnumPaymentMethodType.ECHECK.equals( paymentMethod.getPaymentMethodType() ) ) {
            String terms = request.getParameter( PaymentMethodName.TERMS );
            result.addError( terms == null || terms.length() <= 0, PaymentMethodName.TERMS, SystemMessageList.MSG_REQUIRED );
            if ( result.isSuccess() && !PaymentMethodUtil.hasECheckAccount( user.getIdentity() ) ) {
                paymentMethod.setIsTermsAccepted( true );
            }
        }
        if ( result.isSuccess() && user.getIdentity() != null ) {
            PaymentMethodUtil.addPaymentMethod( request, result, paymentMethod );
            //
            // return the ID of the payment method (should only be one)
            //
            List<ErpPaymentMethodI> payMethods = FDCustomerFactory.getErpCustomer( user.getIdentity() ).getPaymentMethods();
            String paymentId = null;
            if( payMethods.size() ==1 ) {
                paymentId = payMethods.get(0).getPK().getId();
            }
            if(paymentId != null) {
                setPaymentMethod(request, session, user, result, actionName, paymentId, request.getParameter("billingRef"), false, false,"", "N");
            }
            if ( result.isSuccess() ) {
                applyCustomerCredits(actionName, user, session);
            }               
        }
        
	}
	public void performAddPaymentMethod() throws FDResourceException {
		ErpPaymentMethodI paymentMethod = PaymentMethodUtil.processForm( request, result, getIdentity() );
		if ( result.isSuccess() ) {
			performAddPaymentMethod(paymentMethod, result, request, getUser());
		}
	}

	/**
	 * This method is called by the old code
	 * performing some checks before adding valid payment method
	 * 
	 * @param paymentMethod
	 * @param result
	 * @param request
	 * @param user
	 * 
	 * @throws FDResourceException
	 */
    public static void performAddPaymentMethod(ErpPaymentMethodI paymentMethod, ActionResult result, HttpServletRequest request, FDUserI user) throws FDResourceException {
        PaymentMethodUtil.validatePaymentMethod(request, paymentMethod, result, user, true, EnumAccountActivityType.ADD_PAYMENT_METHOD);
        String terms = request.getParameter(PaymentMethodName.TERMS);
        if (EnumPaymentMethodType.ECHECK.equals(paymentMethod.getPaymentMethodType())) {
            result.addError(terms == null || terms.length() <= 0, PaymentMethodName.TERMS, SystemMessageList.MSG_REQUIRED);
            if (result.isSuccess() && !PaymentMethodUtil.hasECheckAccount(user.getIdentity())) {
                paymentMethod.setIsTermsAccepted(true);
            }
        }
        if (result.isSuccess()) {
        	performAddPaymentMethodInternal(paymentMethod, result, request);
        }
    }


    /**
     * Call this with valid payment method!
     * 
     * @see {@link #performAddPaymentMethod() }
     * 
     * @param paymentMethod
     * @param result
     * @param request
     * 
     * @throws FDResourceException
     */
    public static void performAddPaymentMethodInternal(ErpPaymentMethodI paymentMethod, ActionResult result, HttpServletRequest request) throws FDResourceException {
        PaymentMethodUtil.addPaymentMethod(request, result, paymentMethod);
    }
    
    
    public void performEditPaymentMethod() throws FDResourceException {
        ErpPaymentMethodI paymentMethod = PaymentMethodUtil.processEditForm(request, result, getIdentity());
        if(result.isSuccess()){
            performEditPaymentMethod(request, paymentMethod, result, getUser());
        }
    }

    public static void performEditPaymentMethod(HttpServletRequest request, ErpPaymentMethodI paymentMethod, ActionResult result, FDUserI user) throws FDResourceException {
        PaymentMethodUtil.validatePaymentMethod(request, paymentMethod, result, user, true, EnumAccountActivityType.UPDATE_PAYMENT_METHOD);
        if (result.isSuccess()) {
            paymentMethod.setAvsCkeckFailed(false);
            PaymentMethodUtil.editPaymentMethod(request, result, paymentMethod);
        }
    }

	public void performDeletePaymentMethod() throws FDResourceException {
		String paymentId = request.getParameter( "deletePaymentId" );
		if ( paymentId == null || paymentId.length() <= 0 ) {
			throw new FDResourceException( "deletePaymentId not specified" );
		}
        performDeletePaymentMethod(request, result, paymentId);
	}

    public static void performDeletePaymentMethod(HttpServletRequest request, ActionResult result, String paymentId) throws FDResourceException {
        PaymentMethodUtil.deletePaymentMethod(request, result, paymentId);
    }



	/**
	 * Apply customer credits One exception: store credits should not be applied
	 * for Robin Hood
	 * 
	 * @throws FDResourceException
	 */
	public void applyCustomerCredits() throws FDResourceException {
		applyCustomerCredits(getActionName(), getUser(), getSession());
	}

	public static void applyCustomerCredits(String actionName, FDUserI user, HttpSession session) throws FDResourceException {
		if (actionName.equalsIgnoreCase("rh_onestep_submitDonationOrder") || actionName.equalsIgnoreCase("rh_submitDonationOrder")) {
			// Store credits should not be applied for Robin Hood.
			return;
		}
		FDCartModel cart = getCart(user, actionName);
		FDCustomerCreditUtil.applyCustomerCredit(cart, user.getIdentity());
		setCart(cart, user, actionName, session);
	}
	
	/**
	 * @param paymentMethods
	 * @param request
	 * @return
	 */
	public static List<ErpPaymentMethodI> disconnectInvalidPayPalWallet( List<ErpPaymentMethodI> paymentMethods, HttpServletRequest request){
		 ActionResult result = new ActionResult();
		if(paymentMethods != null && !paymentMethods.isEmpty()){
			List<ErpPaymentMethodI> erpPaymentMethodIs = new ArrayList<ErpPaymentMethodI>();
			for(ErpPaymentMethodI paymentMethod : paymentMethods){
				if(paymentMethod.geteWalletID() != null && paymentMethod.geteWalletID().equals(""+EnumEwalletType.PP.getValue())){ // PayPal wallet is paired
					boolean isValid  = PaymentMethodUtil.isVaultTokeValid(paymentMethod.getProfileID(),paymentMethod.getCustomerId());
					if(isValid){
						erpPaymentMethodIs.add(paymentMethod);
					}else{
						FDCustomerManager.deleteLongAccessToken(paymentMethod.getCustomerId(), ""+EnumEwalletType.PP.getValue());
						 try {
							PaymentMethodUtil.deletePaymentMethod(request, result, paymentMethod.getPK().getId());
						} catch (FDResourceException e) {
							LOGGER.error("Unable to delete the disconnect PayPal Payment Method");
						}
					}
				}else{
					erpPaymentMethodIs.add(paymentMethod);
				}
			}
			return erpPaymentMethodIs;
		}
		return paymentMethods;
	}
}
