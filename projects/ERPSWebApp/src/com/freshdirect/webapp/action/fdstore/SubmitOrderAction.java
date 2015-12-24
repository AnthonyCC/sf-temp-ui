package com.freshdirect.webapp.action.fdstore;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Category;

import com.freshdirect.common.customer.EnumWebServiceType;
import com.freshdirect.customer.EnumFraudReason;
import com.freshdirect.customer.EnumTransactionSource;
import com.freshdirect.customer.ErpAddressModel;
import com.freshdirect.customer.ErpAddressVerificationException;
import com.freshdirect.customer.ErpAuthorizationException;
import com.freshdirect.customer.ErpDepotAddressModel;
import com.freshdirect.customer.ErpFraudException;
import com.freshdirect.customer.ErpPaymentMethodI;
import com.freshdirect.customer.ErpPaymentMethodModel;
import com.freshdirect.customer.ErpTransactionException;
import com.freshdirect.delivery.ReservationException;
import com.freshdirect.deliverypass.DeliveryPassException;
import com.freshdirect.deliverypass.DlvPassConstants;
import com.freshdirect.deliverypass.EnumDlvPassStatus;
import com.freshdirect.fdlogistics.model.FDReservation;
import com.freshdirect.fdstore.EnumCheckoutMode;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.customer.FDActionInfo;
import com.freshdirect.fdstore.customer.FDAuthenticationException;
import com.freshdirect.fdstore.customer.FDBulkRecipientList;
import com.freshdirect.fdstore.customer.FDCartLineI;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.fdstore.customer.FDCustomerCreditUtil;
import com.freshdirect.fdstore.customer.FDCustomerFactory;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDModifyCartModel;
import com.freshdirect.fdstore.customer.FDPaymentInadequateException;
import com.freshdirect.fdstore.customer.FDProductSelectionI;
import com.freshdirect.fdstore.customer.FDUser;
import com.freshdirect.fdstore.customer.FDUserUtil;
import com.freshdirect.fdstore.customer.adapter.CustomerRatingAdaptor;
import com.freshdirect.fdstore.customer.adapter.FDOrderAdapter;
import com.freshdirect.fdstore.customer.ejb.EnumCustomerListType;
import com.freshdirect.fdstore.lists.FDCustomerRecipeList;
import com.freshdirect.fdstore.lists.FDCustomerShoppingList;
import com.freshdirect.fdstore.lists.FDListManager;
import com.freshdirect.fdstore.promotion.PromotionFactory;
import com.freshdirect.fdstore.referral.FDReferralManager;
import com.freshdirect.fdstore.rollout.EnumRolloutFeature;
import com.freshdirect.fdstore.rules.FDRulesContextImpl;
import com.freshdirect.fdstore.standingorders.FDStandingOrder;
import com.freshdirect.fdstore.standingorders.FDStandingOrdersManager;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.giftcard.EnumGiftCardType;
import com.freshdirect.giftcard.ErpRecipentModel;
import com.freshdirect.giftcard.RecipientModel;
import com.freshdirect.giftcard.ServiceUnavailableException;
import com.freshdirect.webapp.action.WebActionSupport;
import com.freshdirect.webapp.features.service.FeaturesService;
import com.freshdirect.webapp.taglib.coremetrics.CmShop9Tag;
import com.freshdirect.webapp.taglib.crm.CrmSession;
import com.freshdirect.webapp.taglib.fdstore.AccountActivityUtil;
import com.freshdirect.webapp.taglib.fdstore.FDCustomerCouponUtil;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;
import com.freshdirect.webapp.taglib.fdstore.PaymentMethodUtil;
import com.freshdirect.webapp.taglib.fdstore.SessionName;
import com.freshdirect.webapp.taglib.fdstore.SystemMessageList;
import com.freshdirect.webapp.taglib.fdstore.UserUtil;
import com.freshdirect.webapp.taglib.fdstore.UserValidationUtil;
import com.freshdirect.webapp.util.ShoppingCartUtil;
import com.freshdirect.webapp.util.StandingOrderUtil;

public class SubmitOrderAction extends WebActionSupport {

	private static final long	serialVersionUID	= 799322647556961986L;

	private static Category LOGGER = LoggerFactory.getInstance( SubmitOrderAction.class );

	private String ccdProblemPage;
	private String authCutoffPage;
	private String ccdAddCardPage;
	private String gcFraudPage;
	private FDStandingOrder standingOrder;
	private String addGcPage = "/gift_card/purchase/add_giftcard.jsp";
	private String addGcDonPage = "/gift_card/purchase/landing.jsp";
	private String crmAddBulkGcPage = "/gift_card/purchase/add_bulk_giftcard.jsp";
	
	public void setCcdProblemPage(String ccdProblemPage){
		this.ccdProblemPage = ccdProblemPage;
	}
	
	public void setAuthCutoffPage(String authCutoffPage){
		this.authCutoffPage = authCutoffPage;
	}

	public void setGCFraudPage(String gcFraudPage){
		this.gcFraudPage = gcFraudPage;
	}
	
	public void setStandingOrder(FDStandingOrder standingOrder) {
		this.standingOrder = standingOrder;
	}


	@Override
    public String execute() throws Exception {
		HttpSession session = getWebActionContext().getSession();
		if (session.getAttribute("ProcessingOrder")!=null) {	

			// another thread is already processing this request
			boolean done = false;
			for (int i=0; i<30; i++) {
				// wait for a little
				try {
					Thread.sleep(100);
				} catch (InterruptedException ex) {
					break;
				}

				// check again to see if it has finished
				if (session.getAttribute("ProcessingOrder")==null) {
					done = true;
					break;
				}
			}

			if (!done) {
				// first request still processing after 1.5 seconds, give up
				this.addError("processing_order", formatPhoneMsg(SystemMessageList.MSG_CHECKOUT_DUP_ORDER)); 
				
				return ERROR;
			}
			return SUCCESS;
		}
		
		String soLockId =  UUID.randomUUID().toString();
		try {
			session.setAttribute("ProcessingOrder", Boolean.TRUE); //attempt to prevent double submission
			
			if (standingOrder!=null && standingOrder.getId()!=null){
				FDStandingOrdersManager.getInstance().lockSync(standingOrder, soLockId);
			}
			return doExecute();

		} finally {
			session.removeAttribute("ProcessingOrder");
			if (standingOrder!=null && standingOrder.getId()!=null){
				try{
					FDStandingOrdersManager.getInstance().unlock(standingOrder, soLockId);
				} catch (Exception e) {
					LOGGER.error("Unlocking standing order with lockid " + soLockId + " failed", e);
				}
			}
		}

	}
	
	public String gcExecute(boolean oneStep, boolean isBulkOrder) throws Exception {
		HttpSession session = this.getWebActionContext().getSession();

		if (session.getAttribute("ProcessingOrder")!=null) {				
			// another thread is already processing this request
			boolean done = false;
			for (int i=0; i<30; i++) {
				// wait for a little
				try {
					Thread.sleep(100);
				} catch (InterruptedException ex) {
					break;
				}

				// check again to see if it has finished
				if (session.getAttribute("ProcessingOrder")==null) {
					done = true;
					break;
				}
			}

			if (!done) {
				// first request still processing after 1.5 seconds, give up
				this.addError("processing_order", formatPhoneMsg(SystemMessageList.MSG_CHECKOUT_DUP_ORDER)); 
				
				return ERROR;
			}
			return SUCCESS;
		}
				
		try {
			session.setAttribute("ProcessingOrder", Boolean.TRUE); //attempt to prevent double submmission			
			return this.doGCOrderExecute(oneStep, isBulkOrder);

		} finally {
			session.removeAttribute("ProcessingOrder");
		}

	}
		
	private  String doGCOrderExecute(boolean oneStep, boolean isBulkOrder) throws FDResourceException {
		HttpSession session = this.getWebActionContext().getSession();
		HttpServletRequest request = this.getWebActionContext().getRequest();
						

		FDSessionUser user = (FDSessionUser) session.getAttribute(SessionName.USER);
		FDCartModel cart = user.getGiftCart();
		//FDReservation reservation = cart.getDeliveryReservation();
		
				
		// potential double-submission, how else would the user end up here...
		if (cart.getPaymentMethod()==null ) {
			return SUCCESS;
		}
		
		
		if (!isBulkOrder && UserValidationUtil.validateRecipientListEmpty(request, this.getResult())) {
			HttpServletResponse response = this.getWebActionContext().getResponse();
			try {
				response.sendRedirect(EnumGiftCardType.DONATION_GIFTCARD.equals(user.getGiftCardType()) ? this.addGcDonPage : this.addGcPage);
			} catch (IOException ioe) {
				throw new FDResourceException(ioe.getMessage());
			}
			return ERROR;
		}

		if (isBulkOrder && UserValidationUtil.validateBulkRecipientListEmpty(request, this.getResult())) {
			HttpServletResponse response = this.getWebActionContext().getResponse();
			try {
				response.sendRedirect(this.crmAddBulkGcPage);
			} catch (IOException ioe) {
				throw new FDResourceException(ioe.getMessage());
			}
			return ERROR;
		}
		EnumTransactionSource transactionSource = session.getAttribute(SessionName.CUSTOMER_SERVICE_REP)!=null || CrmSession.getCurrentAgent(session)!=null ? EnumTransactionSource.CUSTOMER_REP : EnumTransactionSource.WEBSITE;
        if(user.getRecipientList().size() > FDStoreProperties.getGiftCardRecipientLimit() && !EnumTransactionSource.CUSTOMER_REP.equals(transactionSource)) {
    		this.addError("limitReached", formatGCRecipientCountMsg(SystemMessageList.MSG_CHECKOUT_GC_RECIPIENT_COUNT));
    		return ERROR;
    	}

		ErpAddressModel addModel = cart.getDeliveryAddress();
		if("professional".equals(request.getParameter("serviceType"))) {
        	addModel.setWebServiceType(EnumWebServiceType.GIFT_CARD_CORPORATE);
        } else {
        	addModel.setWebServiceType(EnumWebServiceType.GIFT_CARD_PERSONAL);
        }
        cart.setDeliveryAddress(addModel);
        cart.setDeliveryPlantInfo(FDUserUtil.getDeliveryPlantInfo(user));
        cart.setEStoreId(user.getUserContext().getStoreContext().getEStoreId());
        
		// set the default credit card to the one that is in the cart
		FDCustomerManager.setDefaultPaymentMethod(
			AccountActivityUtil.getActionInfo(session),
			((ErpPaymentMethodModel) cart.getPaymentMethod()).getPK());
										
		
		
		//
		// Marketing message
		//
		cart.setMarketingMessage("Thanks for your order! Did you know you can shop your top items and past orders to reorder in minutes? We automatically store all your orders, saving all the details. When you come back, you can reorder in minutes. Log onto www.FreshDirect.com");
		
		// !!! HACK: For the first order, "Welcome!" gets prepended to the dlv. instructions in SapOrderAdapter.getDeliveryInstructions()
		if (user.getAdjustedValidOrderCount()==0) {
			cart.setMarketingMessage("Welcome! "+cart.getMarketingMessage());
		}
				
		
		boolean sendEmail = true;
		FDCustomerCreditUtil.applyCustomerCredit(cart,user.getIdentity());
		
		FDUser fdUser = user.getUser();
		if(!fdUser.isPaymentechEnabled()) {
			ErpPaymentMethodI pm=cart.getPaymentMethod();
//			pm.setProfileID("");//Explicitly clear the profile ID;
		} else if (cart instanceof FDModifyCartModel){
			 
				FDModifyCartModel modifyCart = (FDModifyCartModel) cart;
				FDOrderAdapter order = modifyCart.getOriginalOrder();
				ErpPaymentMethodI pymtMethod=order.getPaymentMethod();
		}
		try {
			
			String orderNumber = null;
			CustomerRatingAdaptor cra = new CustomerRatingAdaptor(user.getFDCustomer().getProfile(),user.isCorporateUser(),user.getAdjustedValidOrderCount());
			
			boolean modifying = false;
			EnumDlvPassStatus status = user.getDeliveryPassStatus();
			String note = "GiftCard Order Created";
			if ( isBulkOrder ) {
				List<ErpRecipentModel> repList = convertSavedToErpRecipientModel( user.getBulkRecipentList().getFDRecipentsList().getRecipients() );
				// new order -> place it
				orderNumber = FDCustomerManager.placeGiftCardOrder( AccountActivityUtil.getActionInfo( session, note ), cart, Collections.<String>emptySet(), sendEmail, cra, status, repList, isBulkOrder );
			} else {
				List<ErpRecipentModel> repList = convertSavedToErpRecipientModel( user.getRecipientList().getRecipients(user.getGiftCardType()) );
				// new order -> place it
				orderNumber = FDCustomerManager.placeGiftCardOrder( AccountActivityUtil.getActionInfo( session, note ), cart, Collections.<String>emptySet(), sendEmail, cra, status, repList, isBulkOrder );
			}
			
			//update or create everyItemEverOrdered Customer List
			try{
				updateEIEO(user, cart, modifying);
			} catch(Exception e){
				LOGGER.error("Error handling every item ever ordered list during checkout", e);
			}
			
						
			//if customer utilized a prereserved slot then remove it from the user.
			FDReservation rsv = user.getReservation();
			if(rsv != null && rsv.getPK().equals(cart.getDeliveryReservation().getPK())){
			    user.setReservation(null);
			}
			
			//Clear Recipient List of gift card type
			if(user.getRecipientList() != null){
				user.getRecipientList().removeRecipients(user.getGiftCardType());
			}
			
			user.setBulkRecipientList(new FDBulkRecipientList());
			user.setRedeemedPromotion(null);
			//CLear the fake delivery address.
			user.getGiftCart().setDeliveryAddress(null);
			
			user.invalidateCache();
	        // make sure we're not using stale order history data.
			user.invalidateOrderHistoryCache();
			//Now store the user to update the service_Type		
			/*if(user.getTotalRegularOrderCount()<=0){
				user.setSelectedServiceType(EnumServiceType.PICKUP);
			}*/
			FDCustomerManager.storeUser(fdUser);
			session.setAttribute(SessionName.USER, user);
			
			//clean session
			session.removeAttribute(SessionName.AUTHORIZED_PEOPLE);
			session.removeAttribute(SessionName.PICKUP_AGREEMENT);
			
			// Set the order on the session			
			session.setAttribute(SessionName.RECENT_ORDER_NUMBER, orderNumber);
			
			//prepare and store model for Coremetrics report
			CmShop9Tag.buildPendingModels(session, cart);
			
			//Remove the Delivery Pass Session ID If any.
			session.removeAttribute(DlvPassConstants.DLV_PASS_SESSION_ID);
		} catch (ErpFraudException ex) {
			
			LOGGER.info("Potential fraud detected, saving shopping cart");
			try {
				FDCustomerManager.storeUser(fdUser);
			} catch (FDResourceException fex) {
				LOGGER.warn("Unable to save shopping cart", fex);
			}
				
			LOGGER.warn("FRAUD CHECK FAILED", ex);
			String fraudReason = "";
			if (EnumFraudReason.MAX_GC_ORDER_TOTAL.equals(ex.getFraudReason())) {
				fraudReason = formatGCOrderMaxMsg(SystemMessageList.MSG_CHECKOUT_GC_ORDER_TOO_LARGE);
				this.addError("gc_order_amount_fraud", fraudReason);
			}else if (EnumFraudReason.MAX_ORDER_COUNT_LIMIT.equals(ex.getFraudReason())) {
				fraudReason = formatGCOrderCountMsg(SystemMessageList.MSG_CHECKOUT_GC_ORDER_COUNT);
				this.addError("gc_order_count_fraud", fraudReason);
			}else {
			
				boolean callcenter = "CALLCENTER".equalsIgnoreCase((String) session.getAttribute(SessionName.APPLICATION));
				if (callcenter) {
					fraudReason = ex.getFraudReason().getDescription();
					this.addError("fraud_check_failed", fraudReason);
				} else {
					fraudReason = formatPhoneMsg(SystemMessageList.MSG_CHECKOUT_GENERIC_FRAUD);
					this.addError("fraud_check_failed", fraudReason);
				}
			}
			if(oneStep) {
				user.setGCSignupError(true);
				user.setGcFraudReason(fraudReason);
				HttpServletResponse response = this.getWebActionContext().getResponse();
				try {
					response.sendRedirect(this.gcFraudPage);
				} catch (IOException ioe) {
					throw new FDResourceException(ioe.getMessage());
				}
			}
		} catch (ErpAuthorizationException ae) {
			user.incrementFailedAuthorizations();
			try {
				HttpServletResponse response = this.getWebActionContext().getResponse();
				if(user.getFailedAuthorizations() >= 8){
					response.sendRedirect(this.authCutoffPage);
				}else{
					
					this.addError("gc_payment_auth_failed", ae.getMessage());
					//response.sendRedirect(this.ccdProblemPage);
					
				}				
			} catch(IOException ie) {
				throw new FDResourceException(ie.getMessage());
			}
				
		}catch(ServiceUnavailableException se){
			this.addError("service_unavailable", SystemMessageList.MSG_GC_SERVICE_UNAVAILABLE);
		}catch (ErpAddressVerificationException ae) {
						
			user.setAddressVerificationError(true);
			
			String message =ae.getMessage();
			message=message.replace("9999",user.getCustomerServiceContact());
			LOGGER.error( message, ae );
			this.addError("address_verification_failed", message);
			user.setAddressVerficationMsg(message);
			//response.sendRedirect(this.ccdProblemPage);
		}		
		return this.getResult().isSuccess() ? "SUCCESS" : "ERROR";	
	}
	
	private List<ErpRecipentModel> convertSavedToErpRecipientModel( List<RecipientModel> savedList ) {
		List<ErpRecipentModel> recList = new ArrayList<ErpRecipentModel>();
		for ( RecipientModel srm : savedList ) {
			ErpRecipentModel rm = new ErpRecipentModel();
			rm.toModel( srm );
			recList.add( rm );
		}
		return recList;
	}
	
	protected String doExecute() throws FDResourceException {

		final HttpSession session = this.getWebActionContext().getSession();
		final HttpServletRequest request = this.getWebActionContext().getRequest();

		final FDSessionUser user = (FDSessionUser) session.getAttribute(SessionName.USER);
		final FDCartModel cart = user.getShoppingCart();
		final FDReservation reservation = cart.getDeliveryReservation();

		final EnumCheckoutMode mode = user.getCheckoutMode();
		
		final String userApplicaionSource = (String)session.getAttribute(SessionName.APPLICATION);
		final EnumTransactionSource transactionSource = session.getAttribute(SessionName.CUSTOMER_SERVICE_REP)!=null || CrmSession.getCurrentAgent(session)!=null||user.getMasqueradeContext()!=null ? EnumTransactionSource.CUSTOMER_REP : userApplicaionSource!=null?EnumTransactionSource.getTransactionSource(userApplicaionSource):EnumTransactionSource.WEBSITE;
	       
		 	// potential double-submission, how else would the user end up here...
		if (cart.getDeliveryAddress()==null || reservation==null || reservation.getStartTime()==null || reservation.getEndTime()==null || cart.getPaymentMethod()==null ) {
			return SUCCESS;
		}

		if (!UserValidationUtil.validateOrderMinimum(session, this.getResult())) {
			return ERROR;
		}
		
		if(cart.containsAlcohol() && !cart.isAgeVerified()){
			return ERROR;
		}
		
		if(!cart.getZoneInfo().getZoneId().equals(reservation.getZoneId())) {
			LOGGER.warn( "Invalid reservation : zone id-s do not match!" );
			this.addError("invalid_reservation", SystemMessageList.MSG_CHECKOUT_MISMATCHED_RESERVATION);
			return ERROR;
		}
		Date cutoffTime = reservation.getCutoffTime();
		
		//update cutoff time based on context. fdx order will have different cutoff time from reservation cutoff
		cutoffTime = ShoppingCartUtil.getCutoffByContext(cutoffTime, user);
		
		boolean pastCutoff = new Date().after(cutoffTime);
		
		if (pastCutoff) {
			this.addError( "invalid_reservation", MessageFormat.format(
				SystemMessageList.MSG_CHECKOUT_PAST_CUTOFF, new Object[] { cutoffTime }
			));
			return ERROR;
		}
		if(!cart.getPaymentMethod().isGiftCard()) {	
			// set the default credit card to the one that is in the cart if Card does not belong to EWallet
			ErpPaymentMethodModel paymentMethodModel = (ErpPaymentMethodModel) cart.getPaymentMethod();
			if(paymentMethodModel.geteWalletID() == null){ 
				FDCustomerManager.setDefaultPaymentMethod(
					AccountActivityUtil.getActionInfo(session),paymentMethodModel.getPK());
			}
		}
        if (!(user.getMasqueradeContext() != null && user.getMasqueradeContext().isAddOnOrderEnabled())) {
            ErpAddressModel address = cart.getDeliveryAddress();
            if (address instanceof ErpDepotAddressModel) {
                FDCustomerManager.setDefaultDepotLocationPK(user.getIdentity(), ((ErpDepotAddressModel) address).getLocationId());
            } else {
                // get the address pk and set the default address
				if(!(user.isVoucherHolder() && user.getMasqueradeContext() == null)){
					FDCustomerManager.setDefaultShipToAddressPK(user.getIdentity(), address.getPK().getId());
				}
            }
        }
		
		boolean sendEmail = true;

		if (EnumTransactionSource.CUSTOMER_REP.equals(transactionSource)) {
			// override silent mode when CRM agent submits order
			sendEmail = !((user.getMasqueradeContext() != null) ? user.getMasqueradeContext().isSilentMode() : false);
		} else {
			//
			// Marketing message
			//
			cart.setMarketingMessage("Thanks for your order! Did you know you can shop your top items and past orders to reorder in minutes? We automatically store all your orders, saving all the details. When you come back, you can reorder in minutes. Log onto www.FreshDirect.com");
			
			// !!! HACK: For the first order, "Welcome!" gets prepended to the dlv. instructions in SapOrderAdapter.getDeliveryInstructions()
			if (user.getAdjustedValidOrderCount()==0) {
				cart.setMarketingMessage("Welcome! "+cart.getMarketingMessage());
			}
		}

		/*
		 * Get all applied promotions before invalidating the cache.
		 */
		//Get applied promos
		Set<String> appliedPromos = user.getPromotionEligibility().getAppliedPromotionCodes();
		//Clear all applied promotions
		user.clearAllAppliedPromos();
		
		user.setPostPromoConflictEnabled(true);
		// make sure we're not using stale data
		user.invalidateCache();
        // make sure we're not using stale order history data
		user.invalidateOrderHistoryCache();

		// recalculate promotion
		user.updateUserState();		

		// recalculate credit since promotion is reapplied order amonuts might have changed
		
		FDCustomerCreditUtil.applyCustomerCredit(cart,user.getIdentity());
		
		FDUser fdUser = user.getUser();


		/**
		 * Prepare a standing order object
		 * Note that this operation must precede order creation!
		 */
		if (EnumCheckoutMode.CREATE_SO == mode) {
			// this condition is equal to that SO exists but it does not have PK yet
			standingOrder.setCustomerId(user.getIdentity().getErpCustomerPK());
			
			standingOrder.setPaymentMethodId( FDCustomerManager.getDefaultPaymentMethodPK(user.getIdentity()) );

			standingOrder.setAddressId(cart.getDeliveryAddress().getPK().getId());

			standingOrder.setAlcoholAgreement( cart.isAgeVerified() );
			
			standingOrder.setupDelivery(cart.getDeliveryReservation());
		}

      

		try {
			String orderNumber = null;
			CustomerRatingAdaptor cra = new CustomerRatingAdaptor(user.getFDCustomer().getProfile(),user.isCorporateUser(),user.getAdjustedValidOrderCount());
		   
			boolean isFriendReferred=false;
			if(FDStoreProperties.isExtoleRafEnabled() && user.getRafClickId()!=null  && user.getOrderHistory().getSettledOrderCount()<1 
					&&user.getRafPromoCode()!=null && !user.isReferralPromotionFraud()){
				isFriendReferred=true;
			}
			
			boolean modifying = false;
			EnumDlvPassStatus status = user.getDeliveryPassStatus();
			
			if(user.isEligibleForSignupPromotion()){
				//If applied promo is signup then store the eligibilty list.
				appliedPromos = user.getPromotionEligibility().getEligiblePromotionCodes();
			}else{
				//Otherwise store the applied list.
				appliedPromos = user.getPromotionEligibility().getAppliedPromotionCodes();; 
			}
			//List selectedGiftCards = user.getGiftCardList().getSelectedGiftcards();
			//cart.setSelectedGiftCards(selectedGiftCards);
			if (cart instanceof FDModifyCartModel) {
				// modify order
				FDModifyCartModel modCart = (FDModifyCartModel) cart;
				orderNumber = modCart.getOriginalOrder().getErpSalesId();
				
				Date origCutoff = modCart.getOriginalOrder().getDeliveryReservation().getCutoffTime();
				
				origCutoff = ShoppingCartUtil.getCutoffByContext(origCutoff, user);
				
				if (!EnumTransactionSource.CUSTOMER_REP.equals(transactionSource) && new Date().after(origCutoff)) {
					this.addError("invalid_reservation", MessageFormat.format(
						SystemMessageList.MSG_CHECKOUT_PAST_CUTOFF_MODIFY,
						new Object[] {origCutoff}));
					return ERROR;
				}
				FDActionInfo info=AccountActivityUtil.getActionInfo(session, "Order Modified");
				info.setSource(transactionSource);
				FDCustomerManager.modifyOrder(
					info,
					modCart,
					appliedPromos,
					sendEmail, cra, status,false
				);
				modifying = true;
	            //The previous recommendations of the current user need to be removed.
	            session.removeAttribute(SessionName.SMART_STORE_PREV_RECOMMENDATIONS);
				
			} else {
				// new order -> place it
				FDActionInfo info=AccountActivityUtil.getActionInfo(session, "Order Created");
				info.setSource(transactionSource);
				boolean isFirstOrder = false;
				if(user.getOrderHistory().getTotalOrderCount() <=0){
					isFirstOrder= true;
				}
				orderNumber = FDCustomerManager.placeOrder(info, cart, appliedPromos, sendEmail,cra,status ,isFriendReferred);
			    //[APPDEV-4574]-Auto optin for emails, if its customer's first order.
				if(isFirstOrder){
					FDCustomerManager.storeEmailPreferenceFlag(user.getIdentity().getFDCustomerPK(), "X",user.getUserContext().getStoreContext().getEStoreId());
				}
			}


			/**
			 * Standing Order related post actions
			 */
			if (orderNumber != null && EnumCheckoutMode.NORMAL != mode) {
				if (mode.isModifyStandingOrder()) {
					// modify SO
					StandingOrderUtil.updateStandingOrder(session, mode, cart, standingOrder, orderNumber);
				} else {
					// create SO
					StandingOrderUtil.createStandingOrder(session, cart, standingOrder, orderNumber);
				}
			}

			
			//update or create everyItemEverOrdered Customer List
			try{
				updateEIEO(user, cart, modifying);
			} catch(Exception e){
				LOGGER.error("Error handling every item ever ordered list during checkout", e);
			}
			
			updateCustomerRecipeList(user, cart, modifying);
			
			// SmartStore
			//  record customer and variant for the particular order
			FDCustomerManager.logCustomerVariants(user, orderNumber);
			
			/*APPDEV-1888 - record if the referral promo is not applied due to unique FN+LN+Zipcode rule.*/
			if(user.isReferralPromotionFraud()) {
				FDReferralManager.storeFailedAttempt(user.getUserId(),"", user.getShoppingCart().getDeliveryAddress().getZipCode(),user.getFirstName(),user.getLastName(), "","Checkout FNLNZipCode Fraud");
			}
			
			
			//if customer utilized a prereserved slot then remove it from the user.
			FDReservation rsv = user.getReservation();
			if(rsv != null && rsv.getPK().equals(cart.getDeliveryReservation().getPK())){
			    user.setReservation(null);
			}
			
			if ( cart instanceof FDModifyCartModel || mode.isCartSaved() ) {
				// load cart user had before modifying order.
				ShoppingCartUtil.restoreCart(session);
				
			} else {
				// Clear the cart from the session by replacing it with a new cart
				user.setShoppingCart( new FDCartModel() );
				user.getShoppingCart().setDeliveryAddress(cart.getDeliveryAddress());
				user.getShoppingCart().setDeliveryPlantInfo(cart.getDeliveryPlantInfo());
				user.getShoppingCart().setZoneInfo(cart.getZoneInfo());
				user.getShoppingCart().setEStoreId(cart.getEStoreId());
				// user.updateSurcharges();
				user.getShoppingCart().updateSurcharges(new FDRulesContextImpl(user));

			}
			if(user.getRedeemedPromotion() != null){
				// This forceRefresh is for redemption count on the promo to be reloaded once 
				// user successfully places an order.
				PromotionFactory.getInstance().forceRefreshRedemptionCnt(user.getRedeemedPromotion().getPromotionCode());
			}
			user.setRedeemedPromotion(null);
			
			//Siva: Modified to track user last order zipcode and not a pick up order.
			if(cart != null && cart.getDeliveryAddress() != null && !(cart.getDeliveryAddress() instanceof ErpDepotAddressModel)) {
				user.setZipCode(cart.getDeliveryAddress().getZipCode());
				user.setSelectedServiceType(cart.getDeliveryAddress().getServiceType());
				//Added the following line for zone pricing to keep user service type up-to-date.
				user.setZPServiceType(cart.getDeliveryAddress().getServiceType());
			}
			
			user.invalidateCache();
	        // make sure we're not using stale order history data.
			user.invalidateOrderHistoryCache();
			//Now store the user to update the service_Type
			//invalidate gift cards.
			user.invalidateGiftCards();
			//Clear All Applied promotions
			user.clearAllAppliedPromos();
			//Refresh customer's coupon wallet.
			FDCustomerCouponUtil.getCustomerCoupons(session);
			FDCustomerManager.storeUser(fdUser);
			session.setAttribute(SessionName.USER, user);
			
			//clean session
			session.removeAttribute(SessionName.AUTHORIZED_PEOPLE);
			session.removeAttribute(SessionName.PICKUP_AGREEMENT);
			
			if(orderNumber!=null && user.getSessionEvent()!=null)
			{
				user.getSessionEvent().setOrderId(orderNumber);
			}
			// Set the order on the session
			session.setAttribute(SessionName.RECENT_ORDER_NUMBER, orderNumber);
			/*
			 * Reload delivery pass status to allow user to buy another delivery pass with in the
			 * same session if he modified an order by removing a delivery pass and he has no 
			 * delivery passes active.
			 */
			user.updateDlvPassInfo();
			//Remove the Delivery Pass Session ID If any.
			session.removeAttribute(DlvPassConstants.DLV_PASS_SESSION_ID);
			
		} catch (FDAuthenticationException fdae) {
			LOGGER.warn("Error recalling original cart", fdae);
			throw new FDResourceException("Error recalling original cart " + fdae.getMessage());
				
		} catch (ErpTransactionException ex) {
			LOGGER.warn("Business rules violated", ex);
			throw new FDResourceException("Bussiness rules violated " + ex.getMessage());
				
		} catch (ErpAuthorizationException ae) {
			user.incrementFailedAuthorizations();
			try {
				HttpServletResponse response = this.getWebActionContext().getResponse();
				cart.setDeliveryReservation(null);
				if(user.getFailedAuthorizations() >= 8){
                    if (FeaturesService.defaultService().isFeatureActive(EnumRolloutFeature.checkout2_0, request.getCookies(), fdUser)) {
                        getWebActionContext().getSession().setAttribute(SessionName.ORDER_AUTHORIZATION_CUTOFF_FAILURE_REDIRECT_URL, authCutoffPage);
                    } else {
                        response.sendRedirect(this.authCutoffPage);
                    }
				}else{
                    getWebActionContext().getSession().setAttribute(SessionName.ORDER_AUTHORIZATION_FAILURE_MESSAGE, PaymentMethodUtil.getAuthFailErrorMessage(ae.getMessage()));
                    if (!FeaturesService.defaultService().isFeatureActive(EnumRolloutFeature.checkout2_0, request.getCookies(), fdUser)) {
                        response.sendRedirect(this.ccdProblemPage + "?duplicateCheck=skip");
                    }
				}
				
				return NONE;
			} catch(IOException ie) {
				throw new FDResourceException(ie.getMessage());
				
			}
				
		}
		catch (ErpAddressVerificationException ae) {
			//user.incrementFailedAuthorizations();			
			try{
				HttpServletResponse response = this.getWebActionContext().getResponse();				
					//response.sendRedirect(this.ccdProblemPage);				
				user.setAddressVerificationError(true);		
				cart.setDeliveryReservation(null);
				String message =ae.getMessage();
				message=message.replace("9999",user.getCustomerServiceContact());
				LOGGER.error( message, ae );
				this.addError("address_verification_failed", message);
				user.setAddressVerficationMsg(message);
				response.sendRedirect(this.ccdProblemPage);
			} catch(IOException ie) {
			   throw new FDResourceException(ie.getMessage());
		    }			
		}
		catch (ErpFraudException ex) {
			
			LOGGER.info("Potential fraud detected, saving shopping cart");
			try {
				FDCustomerManager.storeUser(fdUser);
			} catch (FDResourceException fex) {
				LOGGER.warn("Unable to save shopping cart", fex);
			}
				
			LOGGER.warn("FRAUD CHECK FAILED", ex);
			if (EnumFraudReason.MAX_ORDER_TOTAL.equals(ex.getFraudReason())) {
				
				int order_amount = 750;
				String msg = null;
				
				if(cart instanceof FDModifyCartModel) {
					order_amount = Integer.parseInt(FDStoreProperties.getModifyOrderMaxTotal()); //1500;
				}
				
		        if(cart.getEStoreId()!=null && cart.getEStoreId().getContentId()!=null && "FDX".equalsIgnoreCase(cart.getEStoreId().getContentId())){
		        	msg = MessageFormat.format(
							SystemMessageList.MSG_CHECKOUT_AMOUNT_TOO_LARGE_FDX,
							new Object[] {order_amount, UserUtil.getCustomerServiceContact(this.getWebActionContext().getRequest())});
		        } else {
				msg = MessageFormat.format(
						SystemMessageList.MSG_CHECKOUT_AMOUNT_TOO_LARGE,
						new Object[] {order_amount, UserUtil.getCustomerServiceContact(this.getWebActionContext().getRequest())});
		        }
				this.addError("order_amount_fraud", msg);
				
			} else if (EnumFraudReason.MAX_MAKEGOOD.equals(ex.getFraudReason())) {

				this.addError("order_amount_fraud", formatPhoneMsg(SystemMessageList.MSG_CHECKOUT_MAKEGOOD_TOO_LARGE));

			} else {
			
				boolean callcenter = "CALLCENTER".equalsIgnoreCase((String) session.getAttribute(SessionName.APPLICATION));
				if (callcenter) {
					this.addError("fraud_check_failed", ex.getFraudReason().getDescription());
				} else {
					this.addError("fraud_check_failed", formatPhoneMsg(SystemMessageList.MSG_CHECKOUT_GENERIC_FRAUD));
				}
			}
			
		}catch(FDPaymentInadequateException pe){
			this.addError("payment_indequate", SystemMessageList.MSG_PAYMENT_INADEQUATE);
			//clear the dummy Payment method.
			cart.setPaymentMethod(null);
			List<ErpPaymentMethodI> payMethods = FDCustomerFactory.getErpCustomer(user.getIdentity()).getPaymentMethods();
			try {
				HttpServletResponse response = this.getWebActionContext().getResponse();
				 if (payMethods==null || payMethods.size()==0) {
					response.sendRedirect(this.ccdAddCardPage+"?duplicateCheck=skip");
				}else{
					response.sendRedirect(this.ccdProblemPage+"?duplicateCheck=skip");
				}
			}catch(IOException ie) {
				throw new FDResourceException(ie.getMessage());
			}
		}
		catch(ReservationException ex) {
			//Remove the timeslot id from the session
			cart.setDeliveryReservation(null);
			
			this.addError("invalid_reservation", SystemMessageList.MSG_CHECKOUT_EXPIRED_RESERVATION);
		}  catch(DeliveryPassException ex) {
			//There was delivery pass validation failure.
			String errorMsg =  ex.getMessage();
			this.addError("invalid_deliverypass", errorMsg);
			
		} 

		return this.getResult().isSuccess() ? "SUCCESS" : "ERROR";		

	}

	private String formatPhoneMsg(String pattern) {
		return MessageFormat.format(
			pattern,
			new Object[] {UserUtil.getCustomerServiceContact(this.getWebActionContext().getRequest())});
	}
	
	private String formatGCOrderMaxMsg(String pattern) {
		return MessageFormat.format(
			pattern,
			new Object[] {UserUtil.getCustomerServiceContact(this.getWebActionContext().getRequest())});
	}
	
	private String formatGCOrderCountMsg(String pattern) {
		return MessageFormat.format(
			pattern,
			new Object[] {UserUtil.getCustomerServiceContact(this.getWebActionContext().getRequest())});
	}

	private String formatGCRecipientCountMsg(String pattern) {
		return MessageFormat.format(
			pattern,
			new Object[] {new Integer(FDStoreProperties.getGiftCardRecipientLimit()),  UserUtil.getCustomerServiceContact(this.getWebActionContext().getRequest())});
	}
	

	/**
	 * Update or create everyItemEverOrdered Customer List
	 * 
	 * @param user
	 * @param cart
	 * @param modifying
	 * @throws FDResourceException
	 */
	private void updateEIEO(FDSessionUser user, FDCartModel cart, boolean modifying) throws FDResourceException {
		FDCustomerShoppingList everyItemList = (FDCustomerShoppingList) FDListManager.getCustomerList(user.getIdentity(), EnumCustomerListType.SHOPPING_LIST, FDCustomerShoppingList.EVERY_ITEM_LIST);
		
		if(everyItemList == null){
			LOGGER.debug("Customer has no Reorder List -- creating one");
			everyItemList = FDListManager.generateEveryItemEverOrdered(user.getIdentity());
		} else {
		
			for( FDProductSelectionI selection : cart.getOrderLines() ) { 
				everyItemList.mergeSelection(selection, modifying, false);
			}
		}
		
		FDListManager.storeCustomerList(everyItemList);
	}
	
	private void updateCustomerRecipeList(FDSessionUser user, FDCartModel cart, boolean modifying) throws FDResourceException {
		FDCustomerRecipeList everyRecipeList = (FDCustomerRecipeList) FDListManager.getCustomerList(user.getIdentity(), EnumCustomerListType.RECIPE_LIST, FDCustomerRecipeList.EVERY_RECIPE_LIST);
		
		if (everyRecipeList == null) {
			PrimaryKey custPk = new PrimaryKey(user.getIdentity().getErpCustomerPK());
			everyRecipeList = new FDCustomerRecipeList(custPk, FDCustomerRecipeList.EVERY_RECIPE_LIST);
		}
		Set<String> recipeIds = new HashSet<String>();
		for ( FDProductSelectionI selection : cart.getOrderLines() ) {
			if (selection.getRecipeSourceId() != null) {
				recipeIds.add(selection.getRecipeSourceId());
			}
		}
		for ( String id : recipeIds ) {
			everyRecipeList.mergeRecipe(id, modifying);
		}
		
		FDListManager.storeCustomerList(everyRecipeList);
		
	}

	public String getCcdAddCardPage() {
		return ccdAddCardPage;
	}

	public void setCcdAddCardPage(String ccdAddCardPage) {
		this.ccdAddCardPage = ccdAddCardPage;
	}

	
	public String donationOrderExecute() throws Exception {
		HttpSession session = this.getWebActionContext().getSession();
		if (session.getAttribute("ProcessingOrder")!=null) {	

			// another thread is already processing this request
			boolean done = false;
			for (int i=0; i<30; i++) {
				// wait for a little
				try {
					Thread.sleep(100);
				} catch (InterruptedException ex) {
					break;
				}

				// check again to see if it has finished
				if (session.getAttribute("ProcessingOrder")==null) {
					done = true;
					break;
				}
			}

			if (!done) {
				// first request still processing after 1.5 seconds, give up
				this.addError("processing_order", formatPhoneMsg(SystemMessageList.MSG_CHECKOUT_DUP_ORDER)); 
				
				return ERROR;
			}
			return SUCCESS;
		}
				
		try {
			session.setAttribute("ProcessingOrder", Boolean.TRUE); //attempt to prevent double submmission
			return this.doDonationOrderExecute();

		} finally {
			session.removeAttribute("ProcessingOrder");
		}

	}
	
	private  String doDonationOrderExecute() throws FDResourceException, IOException {
		HttpSession session = this.getWebActionContext().getSession();
		HttpServletRequest request = this.getWebActionContext().getRequest();
						

		FDSessionUser user = (FDSessionUser) session.getAttribute(SessionName.USER);
		FDCartModel cart = user.getDonationCart();
		if(null !=cart.getOrderLines() && cart.getOrderLines().size()>0){
			String strQty = cart.getOrderLine(0).getOrderedQuantity();
			if("0".equals(strQty)){
				HttpServletResponse response = this.getWebActionContext().getResponse();				
				response.sendRedirect("/robin_hood/landing.jsp");				
				return NONE;
			}
		}
		String optinInd = request.getParameter("optinInd");
		boolean optIn = false;
		if(null != optinInd && !"".equals(optinInd)){
			if(optinInd.equalsIgnoreCase("optin")){				
				optIn = true;
			}
		}else{
			this.addError("Opt_in_required", SystemMessageList.MSG_RH_OPTIN_REQUIRED);
			return SUCCESS;
		}
		//FDReservation reservation = cart.getDeliveryReservation();
		
				
		// potential double-submission, how else would the user end up here...
		if (cart.getPaymentMethod()==null ) {
			return SUCCESS;
		}
		
//		EnumTransactionSource transactionSource = session.getAttribute(SessionName.CUSTOMER_SERVICE_REP)!=null || CrmSession.getCurrentAgent(session)!=null ? EnumTransactionSource.CUSTOMER_REP : EnumTransactionSource.WEBSITE;
        
		ErpAddressModel addModel = cart.getDeliveryAddress();
		if("professional".equals(request.getParameter("serviceType"))) {
        	addModel.setWebServiceType(EnumWebServiceType.DONATION_BUSINESS);
        } else {
        	addModel.setWebServiceType(EnumWebServiceType.DONATION_INDIVIDUAL);
        }
        cart.setDeliveryAddress(addModel);
        cart.setDeliveryPlantInfo(FDUserUtil.getDeliveryPlantInfo(user));
        cart.setEStoreId(user.getUserContext().getStoreContext().getEStoreId());
        
		// set the default credit card to the one that is in the cart
		FDCustomerManager.setDefaultPaymentMethod(
			AccountActivityUtil.getActionInfo(session),
			((ErpPaymentMethodModel) cart.getPaymentMethod()).getPK());
										
		
		
		//
		// Marketing message
		//
		cart.setMarketingMessage("Thanks for your order! Did you know you can shop your top items and past orders to reorder in minutes? We automatically store all your orders, saving all the details. When you come back, you can reorder in minutes. Log onto www.FreshDirect.com");
		
		// !!! HACK: For the first order, "Welcome!" gets prepended to the dlv. instructions in SapOrderAdapter.getDeliveryInstructions()
		if (user.getAdjustedValidOrderCount()==0) {
			cart.setMarketingMessage("Welcome! "+cart.getMarketingMessage());
		}
				
		
		boolean sendEmail = true;
//		FDCustomerCreditUtil.applyCustomerCredit(cart,user.getIdentity());
		
		FDUser fdUser = user.getUser();
		try {
			
			String orderNumber = null;
			CustomerRatingAdaptor cra = new CustomerRatingAdaptor(user.getFDCustomer().getProfile(),user.isCorporateUser(),user.getAdjustedValidOrderCount());
			
			boolean modifying = false;
			EnumDlvPassStatus status = user.getDeliveryPassStatus();		

			String note = "Donation Order Created";

			orderNumber = FDCustomerManager.placeDonationOrder(AccountActivityUtil.getActionInfo(session, note), cart, Collections.<String>emptySet(), sendEmail,cra,status,optIn );
			
			//update or create everyItemEverOrdered Customer List
			try{
				updateEIEO(user, cart, modifying);
			} catch(Exception e){
				LOGGER.error("Error handling every item ever ordered list during checkout", e);
			}
			
						
			//if customer utilized a prereserved slot then remove it from the user.
			FDReservation rsv = user.getReservation();
			if(rsv != null && rsv.getPK().equals(cart.getDeliveryReservation().getPK())){
			    user.setReservation(null);
			}
			
			user.setRedeemedPromotion(null);
			//CLear the fake delivery address.
			user.getDonationCart().setDeliveryAddress(null);
			user.getDonationCart().setOrderLines(Collections.<FDCartLineI>emptyList());
			
			user.invalidateCache();
	        // make sure we're not using stale order history data.
			user.invalidateOrderHistoryCache();
			//Now store the user to update the service_Type	
			/*if(user.getTotalRegularOrderCount()<=0){
				user.setSelectedServiceType(EnumServiceType.PICKUP);
			}*/
			FDCustomerManager.storeUser(fdUser);
			session.setAttribute(SessionName.USER, user);
			
			//clean session
			session.removeAttribute(SessionName.AUTHORIZED_PEOPLE);
			session.removeAttribute(SessionName.PICKUP_AGREEMENT);
			
			// Set the order on the session			
			session.setAttribute(SessionName.RECENT_ORDER_NUMBER, orderNumber);
			//Remove the Delivery Pass Session ID If any.
			session.removeAttribute(DlvPassConstants.DLV_PASS_SESSION_ID);
		} catch (ErpFraudException ex) {
			
			LOGGER.info("Potential fraud detected, saving shopping cart");
			try {
				FDCustomerManager.storeUser(fdUser);
			} catch (FDResourceException fex) {
				LOGGER.warn("Unable to save shopping cart", fex);
			}
				
			LOGGER.warn("FRAUD CHECK FAILED", ex);
			String fraudReason = "";
			fraudReason = formatPhoneMsg(SystemMessageList.MSG_CHECKOUT_GENERIC_FRAUD);
			this.addError("fraud_check_failed", fraudReason);
		} catch (ErpAuthorizationException ae) {
			user.incrementFailedAuthorizations();
			try {
				HttpServletResponse response = this.getWebActionContext().getResponse();
				if(user.getFailedAuthorizations() >= 8){
					response.sendRedirect(this.authCutoffPage);
				}else{
					//response.sendRedirect(this.ccdProblemPage);
					this.addError("payment_auth_failed", ae.getMessage());
					
				}
				//return NONE;
			} catch(IOException ie) {
				throw new FDResourceException(ie.getMessage());
			}
				
		} 	
		return this.getResult().isSuccess() ? "SUCCESS" : "ERROR";		
	
	}
}
