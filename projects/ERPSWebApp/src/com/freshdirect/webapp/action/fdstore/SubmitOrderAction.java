package com.freshdirect.webapp.action.fdstore;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;

import org.apache.log4j.Category;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.common.customer.EnumServiceType;
import com.freshdirect.common.customer.EnumWebServiceType;
import com.freshdirect.crm.CrmCaseSubject;
import com.freshdirect.customer.EnumChargeType;
import com.freshdirect.customer.EnumFraudReason;
import com.freshdirect.customer.EnumTransactionSource;
import com.freshdirect.customer.ErpAddressModel;
import com.freshdirect.customer.ErpAuthorizationException;
import com.freshdirect.customer.ErpDepotAddressModel;
import com.freshdirect.customer.ErpFraudException;
import com.freshdirect.customer.ErpPaymentMethodModel;
import com.freshdirect.giftcard.ErpRecipentModel;
import com.freshdirect.customer.ErpTransactionException;
import com.freshdirect.giftcard.RecipientModel;
import com.freshdirect.delivery.DlvZoneInfoModel;
import com.freshdirect.delivery.EnumReservationType;
import com.freshdirect.delivery.EnumZipCheckResponses;
import com.freshdirect.delivery.ReservationException;
import com.freshdirect.deliverypass.DeliveryPassException;
import com.freshdirect.deliverypass.DlvPassConstants;
import com.freshdirect.deliverypass.EnumDlvPassStatus;
import com.freshdirect.fdstore.FDCachedFactory;
import com.freshdirect.fdstore.FDConfiguration;
import com.freshdirect.fdstore.FDInvalidAddressException;
import com.freshdirect.fdstore.FDProduct;
import com.freshdirect.fdstore.FDProductInfo;
import com.freshdirect.fdstore.FDReservation;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDActionInfo;
import com.freshdirect.fdstore.FDSalesUnit;
import com.freshdirect.fdstore.FDSku;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.FDTimeslot;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.customer.FDPaymentInadequateException;
import com.freshdirect.fdstore.customer.FDActionInfo;
import com.freshdirect.fdstore.customer.FDAuthenticationException;
import com.freshdirect.fdstore.customer.FDBulkRecipientList;
import com.freshdirect.fdstore.customer.FDCartLineModel;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.fdstore.customer.FDCustomerCreditUtil;
import com.freshdirect.fdstore.customer.FDCustomerFactory;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDModifyCartModel;
import com.freshdirect.fdstore.customer.FDOrderI;
import com.freshdirect.fdstore.customer.FDProductSelectionI;
import com.freshdirect.fdstore.customer.FDRecipientList;
import com.freshdirect.fdstore.customer.FDUser;
import com.freshdirect.fdstore.customer.SavedRecipientModel;
import com.freshdirect.fdstore.customer.adapter.CustomerRatingAdaptor;
import com.freshdirect.fdstore.customer.ejb.EnumCustomerListType;
import com.freshdirect.fdstore.lists.FDCustomerRecipeList;
import com.freshdirect.fdstore.lists.FDCustomerShoppingList;
import com.freshdirect.fdstore.lists.FDListManager;
import com.freshdirect.fdstore.util.CTDeliveryCapacityLogic;
import com.freshdirect.fdstore.util.EnumSiteFeature;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionError;
import com.freshdirect.webapp.action.WebActionSupport;
import com.freshdirect.webapp.taglib.crm.CrmSession;
import com.freshdirect.webapp.taglib.fdstore.AccountActivityUtil;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;
import com.freshdirect.webapp.taglib.fdstore.SessionName;
import com.freshdirect.webapp.taglib.fdstore.SystemMessageList;
import com.freshdirect.webapp.taglib.fdstore.UserUtil;
import com.freshdirect.webapp.taglib.fdstore.UserValidationUtil;

public class SubmitOrderAction extends WebActionSupport {

	private static Category LOGGER = LoggerFactory.getInstance( SubmitOrderAction.class );

	private String ccdProblemPage;
	private String authCutoffPage;
	private String ccdAddCardPage;
	private String gcFraudPage;
	private String addGcPage = "/gift_card/purchase/add_giftcard.jsp";
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
	
	public String execute() throws Exception {
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
			return this.doExecute();

		} finally {
			session.removeAttribute("ProcessingOrder");
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
				response.sendRedirect(this.addGcPage);
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
        if(user.getRecipentList().size() > FDStoreProperties.getGiftCardRecipientLimit() && !EnumTransactionSource.CUSTOMER_REP.equals(transactionSource)) {
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
        
		// set the default credit card to the one that is in the cart
		FDCustomerManager.setDefaultPaymentMethod(
			AccountActivityUtil.getActionInfo(session),
			((ErpPaymentMethodModel) cart.getPaymentMethod()).getPK());
										
		
		
		//
		// Marketing message
		//
		cart.setMarketingMessage("Thanks for your order! Don't forget to try Quickshop, the fastest (and easiest) way to shop. We automatically store all your orders there, saving all the details. When you come back, you can reorder in minutes. Log onto www.FreshDirect.com");
		
		// !!! HACK: For the first order, "Welcome!" gets prepended to the dlv. instructions in SapOrderAdapter.getDeliveryInstructions()
		if (user.getAdjustedValidOrderCount()==0) {
			cart.setMarketingMessage("Welcome! "+cart.getMarketingMessage());
		}
				
		
		boolean sendEmail = true;
		FDCustomerCreditUtil.applyCustomerCredit(cart,user.getIdentity());
		
		FDUser fdUser = user.getUser();
		try {
			
			String orderNumber = null;
			CustomerRatingAdaptor cra = new CustomerRatingAdaptor(user.getFDCustomer().getProfile(),user.isCorporateUser(),user.getAdjustedValidOrderCount());
			
			boolean modifying = false;
			EnumDlvPassStatus status = user.getDeliveryPassStatus();
			if(isBulkOrder){
				List repList = convertSavedToErpRecipienntModel(user.getBulkRecipentList().getFDRecipentsList().getRecipents());
				// new order -> place it
				orderNumber = FDCustomerManager.placeGiftCardOrder(AccountActivityUtil.getActionInfo(session), cart, Collections.EMPTY_SET, sendEmail,cra,status,repList,isBulkOrder );			
			} else {
				List repList = convertSavedToErpRecipienntModel(user.getRecipentList().getRecipents());
				// new order -> place it
				orderNumber = FDCustomerManager.placeGiftCardOrder(AccountActivityUtil.getActionInfo(session), cart, Collections.EMPTY_SET, sendEmail,cra,status,repList,isBulkOrder );			
			}

			
			//update or create everyItemEverOrdered Customer List
			try{
				updateCustomerShoppingList(user, cart, modifying);
			} catch(Exception e){
				LOGGER.error("Error handling every item ever ordered list during checkout", e);
			}
			
						
			//if customer utilized a prereserved slot then remove it from the user.
			FDReservation rsv = user.getReservation();
			if(rsv != null && rsv.getPK().equals(cart.getDeliveryReservation().getPK())){
			    user.setReservation(null);
			}
			//Clear Recipient List
			user.setRecipientList(new FDRecipientList());
			user.setBulkRecipientList(new FDBulkRecipientList());
			user.setRedeemedPromotion(null);
			//CLear the fake delivery address.
			user.getGiftCart().setDeliveryAddress(null);
			
			user.invalidateCache();
	        // make sure we're not using stale order history data.
			user.invalidateOrderHistoryCache();
			//Now store the user to update the service_Type			
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
					response.sendRedirect(this.ccdProblemPage);
				}
				return NONE;
			} catch(IOException ie) {
				throw new FDResourceException(ie.getMessage());
			}
				
		} 	
		return this.getResult().isSuccess() ? "SUCCESS" : "ERROR";		
	
	}
	
	private List convertSavedToErpRecipienntModel(List savedList)
	{
	 	ListIterator i = savedList.listIterator();
	 	List recList=new ArrayList();
    	while(i.hasNext()) {    		
    		SavedRecipientModel srm = (SavedRecipientModel)i.next();
    		ErpRecipentModel rm=new ErpRecipentModel();
    		rm.toModel(srm);
    		recList.add(rm);
         }
    	return recList;
	}
	
	protected String doExecute() throws FDResourceException {

		HttpSession session = this.getWebActionContext().getSession();
		HttpServletRequest request = this.getWebActionContext().getRequest();

		FDSessionUser user = (FDSessionUser) session.getAttribute(SessionName.USER);
		FDCartModel cart = user.getShoppingCart();
		FDReservation reservation = cart.getDeliveryReservation();


		// potential double-submission, how else would the user end up here...
		if (cart.getDeliveryAddress()==null || reservation==null || reservation.getStartTime()==null || reservation.getEndTime()==null || cart.getPaymentMethod()==null ) {
			return SUCCESS;
		}

		if (!UserValidationUtil.validateOrderMinimum(request, this.getResult())) {
			return ERROR;
		}
		
		if(cart.containsAlcohol() && !cart.isAgeVerified()){
			return ERROR;
		}
		
		if(!cart.getZoneInfo().getZoneId().equals(reservation.getZoneId())) {
			this.addError("invalid_reservation", SystemMessageList.MSG_CHECKOUT_MISMATCHED_RESERVATION);
			return ERROR;
		}

		boolean pastCutoff = new Date().after( reservation.getCutoffTime() );
		if (pastCutoff) {
			this.addError( "invalid_reservation", MessageFormat.format(
				SystemMessageList.MSG_CHECKOUT_PAST_CUTOFF, new Object[] { reservation.getCutoffTime() }
			));
			return ERROR;
		}
		if(!cart.getPaymentMethod().isGiftCard()) {	
			// set the default credit card to the one that is in the cart
			FDCustomerManager.setDefaultPaymentMethod(
				AccountActivityUtil.getActionInfo(session),
				((ErpPaymentMethodModel) cart.getPaymentMethod()).getPK());
		}
		ErpAddressModel address = cart.getDeliveryAddress();
		if(address instanceof ErpDepotAddressModel){
			FDCustomerManager.setDefaultDepotLocationPK(user.getIdentity(), ((ErpDepotAddressModel)address).getLocationId());
		}else{
			//get the address pk and set the default address
			FDCustomerManager.setDefaultShipToAddressPK(user.getIdentity(), address.getPK().getId());
			
		}
		
		//
		// Marketing message
		//
		cart.setMarketingMessage("Thanks for your order! Don't forget to try Quickshop, the fastest (and easiest) way to shop. We automatically store all your orders there, saving all the details. When you come back, you can reorder in minutes. Log onto www.FreshDirect.com");
		
		// !!! HACK: For the first order, "Welcome!" gets prepended to the dlv. instructions in SapOrderAdapter.getDeliveryInstructions()
		if (user.getAdjustedValidOrderCount()==0) {
			cart.setMarketingMessage("Welcome! "+cart.getMarketingMessage());
		}
		
		boolean sendEmail = true;
		EnumTransactionSource transactionSource = session.getAttribute(SessionName.CUSTOMER_SERVICE_REP)!=null || CrmSession.getCurrentAgent(session)!=null ? EnumTransactionSource.CUSTOMER_REP : EnumTransactionSource.WEBSITE;
		
		if (EnumTransactionSource.CUSTOMER_REP.equals(transactionSource)) {
			//
			// Check for delivery surcharge waiver (CALLCENTER)
			//
			boolean csrWaivedDeliveryCharge = "true".equalsIgnoreCase(request.getParameter("waive_delivery_fee")); 
			LOGGER.debug("Delivery charge waive requested by CSR: "+ csrWaivedDeliveryCharge);
			cart.setCsrWaivedDeliveryCharge(csrWaivedDeliveryCharge);
				

			//
			// Check for phone handling charge waiver (CALLCENTER)
			//
			cart.setChargeWaived(EnumChargeType.PHONE, "true".equalsIgnoreCase(request.getParameter("waive_phone_fee")), "CSR");
			
			//
			// Check for CSR message (CALLCENTER)
			//
			String csrMessage = request.getParameter("csr_message");
			if ( csrMessage != null && !"".equalsIgnoreCase(csrMessage.trim()) ) {
				cart.setCustomerServiceMessage( csrMessage );
			} else {
				cart.setCustomerServiceMessage("");
			}
			
			//
			// check for silent order placement (CALLCENTER)
			//
			sendEmail = request.getParameter("silent_mode") == null;
			
		}

		// SORI changes
		
		user.setPostPromoConflictEnabled(true);
		// make sure we're not using stale data
		user.invalidateCache();
        // make sure we're not using stale order history data
		user.invalidateOrderHistoryCache();
		// recalculate promotion
		user.updateUserState( );		
						
		// recalculate credit since promotion is reapplied order amonuts might have changed
		
		FDCustomerCreditUtil.applyCustomerCredit(cart,user.getIdentity());
		
		FDUser fdUser = user.getUser();
		try {
			
			String orderNumber = null;
			CustomerRatingAdaptor cra = new CustomerRatingAdaptor(user.getFDCustomer().getProfile(),user.isCorporateUser(),user.getAdjustedValidOrderCount());
			
			boolean modifying = false;
			EnumDlvPassStatus status = user.getDeliveryPassStatus();
			Set appliedPromos = null;
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
				if (!EnumTransactionSource.CUSTOMER_REP.equals(transactionSource) && new Date().after(origCutoff)) {
					this.addError("invalid_reservation", MessageFormat.format(
						SystemMessageList.MSG_CHECKOUT_PAST_CUTOFF_MODIFY,
						new Object[] {origCutoff}));
					return ERROR;
				}
				FDActionInfo info=AccountActivityUtil.getActionInfo(session);
				boolean isPR1=CTDeliveryCapacityLogic.isPR1(user,reservation.getTimeslot());
				info.setPR1(isPR1);
				FDCustomerManager.modifyOrder(
					info,
					modCart,
					appliedPromos,
					sendEmail, cra, status
				);
				modifying = true;
	            //The previous recommendations of the current user need to be removed.
	            session.removeAttribute(SessionName.SMART_STORE_PREV_RECOMMENDATIONS);
				
			} else {
				// new order -> place it
				FDActionInfo info=AccountActivityUtil.getActionInfo(session);
				boolean isPR1=CTDeliveryCapacityLogic.isPR1(user,reservation.getTimeslot());
				info.setPR1(isPR1);
				orderNumber = FDCustomerManager.placeOrder(info, cart, appliedPromos, sendEmail,cra,status );
			}
			
			//update or create everyItemEverOrdered Customer List
			try{
				updateCustomerShoppingList(user, cart, modifying);
			} catch(Exception e){
				LOGGER.error("Error handling every item ever ordered list during checkout", e);
			}
			
			updateCustomerRecipeList(user, cart, modifying);
			
			// SmartStore
			//  record customer and variant for the particular order
			//FDCustomerManager.logCustomerVariants(user, orderNumber);
			
			
			//if customer utilized a prereserved slot then remove it from the user.
			FDReservation rsv = user.getReservation();
			if(rsv != null && rsv.getPK().equals(cart.getDeliveryReservation().getPK())){
			    user.setReservation(null);
			}
			
			if (cart instanceof FDModifyCartModel) {
				// load cart user had before modifying order.
				FDCartModel originalCart = FDCustomerManager.recognize(user.getIdentity()).getShoppingCart();
				user.setShoppingCart( originalCart );
				
			} else {
				// Clear the cart from the session by replacing it with a new cart
				user.setShoppingCart( new FDCartModel() );

			}

			user.setRedeemedPromotion(null);
			
			//Siva: Modified to track user last order zipcode
			if(cart != null && cart.getDeliveryAddress() != null) {
				user.setZipCode(cart.getDeliveryAddress().getZipCode());
			}
			
			user.invalidateCache();
	        // make sure we're not using stale order history data.
			user.invalidateOrderHistoryCache();
			//Now store the user to update the service_Type
			//invalidate gift cards.
			user.invalidateGiftCards();
			FDCustomerManager.storeUser(fdUser);
			session.setAttribute(SessionName.USER, user);
			
			//clean session
			session.removeAttribute(SessionName.AUTHORIZED_PEOPLE);
			session.removeAttribute(SessionName.PICKUP_AGREEMENT);
			
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
				if(user.getFailedAuthorizations() >= 8){
					response.sendRedirect(this.authCutoffPage);
				}else{
					response.sendRedirect(this.ccdProblemPage);
				}
				return NONE;
			} catch(IOException ie) {
				throw new FDResourceException(ie.getMessage());
			}
				
		} catch (ErpFraudException ex) {
			
			LOGGER.info("Potential fraud detected, saving shopping cart");
			try {
				FDCustomerManager.storeUser(fdUser);
			} catch (FDResourceException fex) {
				LOGGER.warn("Unable to save shopping cart", fex);
			}
				
			LOGGER.warn("FRAUD CHECK FAILED", ex);
			if (EnumFraudReason.MAX_ORDER_TOTAL.equals(ex.getFraudReason())) {

				this.addError("order_amount_fraud", formatPhoneMsg(SystemMessageList.MSG_CHECKOUT_AMOUNT_TOO_LARGE));
				
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
			List payMethods = FDCustomerFactory.getErpCustomer(user.getIdentity()).getPaymentMethods();
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
	
	private void updateCustomerShoppingList(FDSessionUser user, FDCartModel cart, boolean modifying) throws FDResourceException {
		FDCustomerShoppingList everyItemList = (FDCustomerShoppingList) FDListManager.getCustomerList(user.getIdentity(), EnumCustomerListType.SHOPPING_LIST, FDCustomerShoppingList.EVERY_ITEM_LIST);
		
		if(everyItemList == null){
			LOGGER.debug("Customer has no QuickShop List -- creating one");
			everyItemList = FDListManager.generateEveryItemEverOrdered(user.getIdentity());
		} else {
		
			for(Iterator i = cart.getOrderLines().iterator(); i.hasNext();){
				FDProductSelectionI selection = (FDProductSelectionI) i.next();
				everyItemList.mergeSelection(selection, modifying);
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
		Set recipeIds = new HashSet();
		for (Iterator i = cart.getOrderLines().iterator(); i.hasNext();) {
			FDProductSelectionI selection = (FDProductSelectionI) i.next();
			if (selection.getRecipeSourceId() != null) {
				recipeIds.add(selection.getRecipeSourceId());
			}
		}
		for (Iterator i = recipeIds.iterator(); i.hasNext();) {
			everyRecipeList.mergeRecipe((String) i.next(), modifying);
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
		

		EnumTransactionSource transactionSource = session.getAttribute(SessionName.CUSTOMER_SERVICE_REP)!=null || CrmSession.getCurrentAgent(session)!=null ? EnumTransactionSource.CUSTOMER_REP : EnumTransactionSource.WEBSITE;
        

		ErpAddressModel addModel = cart.getDeliveryAddress();
		if("professional".equals(request.getParameter("serviceType"))) {
        	addModel.setWebServiceType(EnumWebServiceType.DONATION_BUSINESS);
        } else {
        	addModel.setWebServiceType(EnumWebServiceType.DONATION_INDIVIDUAL);
        }
        cart.setDeliveryAddress(addModel);
        
		// set the default credit card to the one that is in the cart
		FDCustomerManager.setDefaultPaymentMethod(
			AccountActivityUtil.getActionInfo(session),
			((ErpPaymentMethodModel) cart.getPaymentMethod()).getPK());
										
		
		
		//
		// Marketing message
		//
		cart.setMarketingMessage("Thanks for your order! Don't forget to try Quickshop, the fastest (and easiest) way to shop. We automatically store all your orders there, saving all the details. When you come back, you can reorder in minutes. Log onto www.FreshDirect.com");
		
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

			orderNumber = FDCustomerManager.placeDonationOrder(AccountActivityUtil.getActionInfo(session), cart, Collections.EMPTY_SET, sendEmail,cra,status,optIn );
			
			//update or create everyItemEverOrdered Customer List
			try{
				updateCustomerShoppingList(user, cart, modifying);
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
			user.getDonationCart().setOrderLines(Collections.EMPTY_LIST);
			
			user.invalidateCache();
	        // make sure we're not using stale order history data.
			user.invalidateOrderHistoryCache();
			//Now store the user to update the service_Type			
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
			/*if (EnumFraudReason.MAX_GC_ORDER_TOTAL.equals(ex.getFraudReason())) {
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
			}*/
			/*if(oneStep) {
				user.setGCSignupError(true);
				user.setGcFraudReason(fraudReason);
				HttpServletResponse response = this.getWebActionContext().getResponse();
				try {
					response.sendRedirect(this.gcFraudPage);
				} catch (IOException ioe) {
					throw new FDResourceException(ioe.getMessage());
				}
			}*/
		} catch (ErpAuthorizationException ae) {
			user.incrementFailedAuthorizations();
			try {
				HttpServletResponse response = this.getWebActionContext().getResponse();
				if(user.getFailedAuthorizations() >= 8){
					response.sendRedirect(this.authCutoffPage);
				}else{
					response.sendRedirect(this.ccdProblemPage);
				}
				return NONE;
			} catch(IOException ie) {
				throw new FDResourceException(ie.getMessage());
			}
				
		} 	
		return this.getResult().isSuccess() ? "SUCCESS" : "ERROR";		
	
	}
}
