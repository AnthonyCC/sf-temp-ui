package com.freshdirect.webapp.checkout;

import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.PageContext;

import org.apache.log4j.Category;

import com.freshdirect.customer.EnumPaymentType;
import com.freshdirect.customer.ErpPaymentMethodI;
import com.freshdirect.customer.ErpPaymentMethodModel;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDActionInfo;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.fdstore.customer.FDCustomerCreditUtil;
import com.freshdirect.fdstore.customer.FDCustomerFactory;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.util.NVL;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionError;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.payment.EnumPaymentMethodType;
import com.freshdirect.webapp.taglib.fdstore.AccountActivityUtil;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;
import com.freshdirect.webapp.taglib.fdstore.PaymentMethodName;
import com.freshdirect.webapp.taglib.fdstore.PaymentMethodUtil;
import com.freshdirect.webapp.taglib.fdstore.SessionName;
import com.freshdirect.webapp.taglib.fdstore.SystemMessageList;

public class PaymentMethodManipulator extends CheckoutManipulator {
	private static Category		LOGGER	= LoggerFactory.getInstance( PaymentMethodManipulator.class );

	public PaymentMethodManipulator(PageContext context, ActionResult result, String actionName) {
		super(context, result, actionName);
	}

	public void setPaymentMethod() throws FDResourceException {
		String paymentId = request.getParameter( "paymentMethodList" );
		String billingRef = request.getParameter( "billingRef" );
		boolean makeGoodOrder = false;
		String referencedOrder = "";
		String app = (String) session.getAttribute( SessionName.APPLICATION );
		if ( "CALLCENTER".equalsIgnoreCase( app ) ) {
			makeGoodOrder = request.getParameter( "makeGoodOrder" ) != null;
			referencedOrder = NVL.apply( request.getParameter( "referencedOrder" ), "" ).trim();
			if ( makeGoodOrder && "".equals( referencedOrder ) ) {
				result.addError( true, "referencedOrder", "Reference Order number is required for a make good order" );
				return;
			}
		}
		this.setPaymentMethod( request, result, paymentId, billingRef, makeGoodOrder, referencedOrder );
	}

	private void setNoPaymentMethod( HttpServletRequest request, ActionResult result ) throws FDResourceException {
		String billingRef = request.getParameter( "billingRef" );
		boolean makeGoodOrder = false;
		String referencedOrder = "";
		String app = (String) session.getAttribute( SessionName.APPLICATION );
		if ( "CALLCENTER".equalsIgnoreCase( app ) ) {
			makeGoodOrder = request.getParameter( "makeGoodOrder" ) != null;
			referencedOrder = NVL.apply( request.getParameter( "referencedOrder" ), "" ).trim();
			if ( makeGoodOrder && "".equals( referencedOrder ) ) {
				result.addError( true, "referencedOrder", "Reference Order number is required for a make good order" );
				return;
			}
		}

		ErpPaymentMethodI paymentMethod = PaymentMethodUtil.createGiftCardPaymentMethod( this.getUser() );

		FDCartModel cart = getCart();
		paymentMethod.setBillingRef( billingRef );
		paymentMethod.setPaymentType( makeGoodOrder ? EnumPaymentType.MAKE_GOOD : EnumPaymentType.REGULAR );
		paymentMethod.setReferencedOrder( referencedOrder );
		cart.setPaymentMethod( paymentMethod );
		setCart( cart );
		this.getUser().setPostPromoConflictEnabled( true );
		this.getUser().updateUserState();
	}
	
	
	private void setPaymentMethod( HttpServletRequest request, ActionResult result, String paymentId, String billingRef, boolean makeGoodOrder, String referencedOrder ) throws FDResourceException {
		//
		// check for a valid payment ID
		//
		if ( ( paymentId == null ) || ( "".equals( paymentId ) ) ) {
			result.addError( new ActionError( "paymentMethodList", "You must select a payment method." ) );
			return;
		}

		FDIdentity identity = getIdentity();

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
		PaymentMethodUtil.validatePaymentMethod( request, paymentMethod, result, getUser() );

		//Checking for CC a/c's or at least one valid CC. If NO, restricting the customer to place order using E-check
		String app = (String) session.getAttribute( SessionName.APPLICATION);
		if (!"CALLCENTER".equalsIgnoreCase(app) && EnumPaymentMethodType.ECHECK.equals(paymentMethod.getPaymentMethodType()) && result.isSuccess()) {
			int numCreditCards=0;
			boolean isValidCreditCardAvailable = false;
			for (ErpPaymentMethodI paymentM : paymentMethods) {
        		if (EnumPaymentMethodType.CREDITCARD.equals(paymentM.getPaymentMethodType())) {
        			numCreditCards++;
	        	  	ActionResult tempResult=new ActionResult();
					PaymentMethodUtil.validatePaymentMethod(request, paymentM, tempResult, getUser());
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

		FDCartModel cart = getCart();
		paymentMethod.setBillingRef( billingRef );
		paymentMethod.setPaymentType( makeGoodOrder ? EnumPaymentType.MAKE_GOOD : EnumPaymentType.REGULAR );
		paymentMethod.setReferencedOrder( referencedOrder );
		cart.setPaymentMethod( paymentMethod );
		setCart( cart );

		//
		// set default payment method and check for unique billing address, if
		// required
		//
		FDActionInfo info = AccountActivityUtil.getActionInfo( session );
		final PrimaryKey pmPK = ( (ErpPaymentMethodModel)paymentMethod ).getPK();
		FDCustomerManager.setDefaultPaymentMethod( info, pmPK );

		/*if ( user.isDepotUser() ) {
			if ( user.isEligibleForSignupPromotion() ) {
				if ( FDCustomerManager.checkBillToAddressFraud( info, paymentMethod ) ) {

					session.setAttribute( SessionName.SIGNUP_WARNING, MessageFormat.format( SystemMessageList.MSG_NOT_UNIQUE_INFO, new Object[] { user.getCustomerServiceContact() } ) );

				}
			}
		}*/

		FDSessionUser currentUser = (FDSessionUser)getUser();
		currentUser.setPostPromoConflictEnabled( true );
		currentUser.updateUserState();
		session.setAttribute( SessionName.USER, currentUser );

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

		ErpPaymentMethodI paymentMethod = PaymentMethodUtil.processForm( request, result, identity );
		if ( result.isSuccess() ) {
			PaymentMethodUtil.validatePaymentMethod( request, paymentMethod, result, getUser() );
			if ( EnumPaymentMethodType.ECHECK.equals( paymentMethod.getPaymentMethodType() ) ) {
				String terms = request.getParameter( PaymentMethodName.TERMS );
				result.addError( terms == null || terms.length() <= 0, PaymentMethodName.TERMS, SystemMessageList.MSG_REQUIRED );
				if ( result.isSuccess() && !PaymentMethodUtil.hasECheckAccount( getUser().getIdentity() ) ) {
					paymentMethod.setIsTermsAccepted( true );
				}
			}
			if ( result.isSuccess() && identity != null ) {
				PaymentMethodUtil.addPaymentMethod( request, result, paymentMethod );
			}
		}
		if ( identity == null ) {
			result.addError( new ActionError( "unexpected_error", "User Identity cannot be Null" ) );
			return;
		}
		//
		// return the ID of the payment method (should only be one)
		//
		List<ErpPaymentMethodI> payMethods = FDCustomerFactory.getErpCustomer( identity ).getPaymentMethods();
		String paymentId = null;
		if( payMethods.size() > 0 ) {
			paymentId = ( (ErpPaymentMethodModel)payMethods.get( payMethods.size()-1 ) ).getPK().getId();
		}
		setPaymentMethod( request, result, paymentId, request.getParameter( "billingRef" ), false, "" );
		if ( result.isSuccess() ) {
			applyCustomerCredits();
		}

	}

	public void performAddPaymentMethod() throws FDResourceException {
		ErpPaymentMethodI paymentMethod = PaymentMethodUtil.processForm( request, result, getIdentity() );

		if ( result.isSuccess() ) {
			PaymentMethodUtil.validatePaymentMethod( request, paymentMethod, result, getUser() );
			if ( EnumPaymentMethodType.ECHECK.equals( paymentMethod.getPaymentMethodType() ) ) {
				String terms = request.getParameter( PaymentMethodName.TERMS );
				result.addError( terms == null || terms.length() <= 0, PaymentMethodName.TERMS, SystemMessageList.MSG_REQUIRED );
				if ( result.isSuccess() && !PaymentMethodUtil.hasECheckAccount( getUser().getIdentity() ) ) {
					paymentMethod.setIsTermsAccepted( true );
				}
			}
			if ( result.isSuccess() ) {
				PaymentMethodUtil.addPaymentMethod( request, result, paymentMethod );
			}
		}

	}

	public void performEditPaymentMethod() throws FDResourceException {
    	ErpPaymentMethodI paymentMethod = PaymentMethodUtil.processEditForm(request, result, getIdentity());	            	
        if(result.isSuccess()){
            PaymentMethodUtil.validatePaymentMethod(request, paymentMethod, result, getUser());
            if(result.isSuccess()){
            	paymentMethod.setAvsCkeckFailed(false);
                PaymentMethodUtil.editPaymentMethod(request, result, paymentMethod);
            }
        }
	}
	public void performDeletePaymentMethod() throws FDResourceException {
		String paymentId = request.getParameter( "deletePaymentId" );
		if ( paymentId == null || paymentId.length() <= 0 ) {
			throw new FDResourceException( "deletePaymentId not specified" );
		}
		PaymentMethodUtil.deletePaymentMethod( request, result, paymentId );
	}




	/**
	 * Apply customer credits
	 * One exception: store credits should not be applied for Robin Hood
	 * 
	 * @throws FDResourceException
	 */
	public void applyCustomerCredits() throws FDResourceException {
		if (	this.getActionName().equalsIgnoreCase( "rh_onestep_submitDonationOrder" ) ||
				this.getActionName().equalsIgnoreCase( "rh_submitDonationOrder" ) ) {
			// Store credits should not be applied for Robin Hood.
			return;
		}
		FDIdentity identity = getIdentity();
		FDCartModel cart = getCart();
		FDCustomerCreditUtil.applyCustomerCredit( cart, identity );
		setCart( cart );
	}
}
