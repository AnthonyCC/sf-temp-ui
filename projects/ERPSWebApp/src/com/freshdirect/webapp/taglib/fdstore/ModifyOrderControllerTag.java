/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */

package com.freshdirect.webapp.taglib.fdstore;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

import org.apache.log4j.Category;

import com.freshdirect.customer.EnumChargeType;
import com.freshdirect.customer.EnumPaymentResponse;
import com.freshdirect.customer.EnumPaymentType;
import com.freshdirect.customer.EnumSaleStatus;
import com.freshdirect.customer.EnumSaleType;
import com.freshdirect.customer.EnumTransactionSource;
import com.freshdirect.customer.ErpAddressVerificationException;
import com.freshdirect.customer.ErpAuthorizationException;
import com.freshdirect.customer.ErpChargeLineModel;
import com.freshdirect.customer.ErpCustomerModel;
import com.freshdirect.customer.ErpFraudException;
import com.freshdirect.customer.ErpPaymentMethodI;
import com.freshdirect.customer.ErpPaymentMethodModel;
import com.freshdirect.customer.ErpTransactionException;
import com.freshdirect.dataloader.subscriptions.DeliveryPassRenewalCron;
import com.freshdirect.delivery.DlvZoneInfoModel;
import com.freshdirect.delivery.EnumReservationType;
import com.freshdirect.deliverypass.DeliveryPassException;
import com.freshdirect.deliverypass.DlvPassConstants;
import com.freshdirect.fdstore.CallCenterServices;
import com.freshdirect.fdstore.FDDeliveryManager;
import com.freshdirect.fdstore.FDException;
import com.freshdirect.fdstore.FDReservation;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDRuntimeException;
import com.freshdirect.fdstore.FDTimeslot;
import com.freshdirect.fdstore.customer.FDPaymentInadequateException;
import com.freshdirect.fdstore.customer.FDActionInfo;
import com.freshdirect.fdstore.customer.FDAuthenticationException;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.fdstore.customer.FDCustomerFactory;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDInvalidConfigurationException;
import com.freshdirect.fdstore.customer.FDModifyCartModel;
import com.freshdirect.fdstore.customer.FDOrderI;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.customer.adapter.CustomerRatingAdaptor;
import com.freshdirect.fdstore.customer.adapter.FDOrderAdapter;
import com.freshdirect.fdstore.giftcard.FDGiftCardI;
import com.freshdirect.fdstore.giftcard.FDGiftCardInfoList;
import com.freshdirect.fdstore.giftcard.FDGiftCardModel;
import com.freshdirect.fdstore.promotion.EnumPromotionType;
import com.freshdirect.fdstore.promotion.Promotion;
import com.freshdirect.fdstore.promotion.PromotionFactory;
import com.freshdirect.fdstore.promotion.RedemptionCodeStrategy;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.util.NVL;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionError;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.giftcard.ErpAppliedGiftCardModel;
import com.freshdirect.giftcard.ErpGiftCardModel;
import com.freshdirect.webapp.taglib.crm.CrmSession;
import com.freshdirect.webapp.util.OrderPermissionsI;
import com.freshdirect.webapp.util.OrderPermissionsImpl;
import com.freshdirect.customer.ErpSaleNotFoundException;
import com.freshdirect.customer.ErpAddressModel;
/**
 *
 * @version $Revision$
 * @author $Author$
 */
public class ModifyOrderControllerTag extends com.freshdirect.framework.webapp.BodyTagSupport {

	private static Category LOGGER 	= LoggerFactory.getInstance( ModifyOrderControllerTag.class );

	private final String CANCEL_ACTION 			= "cancel";
	private final String MODIFY_ACTION 			= "modify";
	private final String RETURN_ACTION 			= "return";
	private final String RESUBMIT_ACTION 		= "resubmit";
	private final String CANCEL_MODIFY_ACTION 	= "cancelModify";
	private final String NEW_AUTHORIZATION		= "new_authorization";
	private final String NEW_AUTHORIZATION_FAILED_SETTLEMENT = "new_payment_for_failed_settlement";
	private final String REDELIVERY_ACTION		= "redelivery";
	private final String CHARGE_ORDER_ACTION	= "charge_order";
	private final String AUTO_RENEW_AUTH	= "auto_renew_auth";
	private final String PLACE_AUTO_RENEW_ORDER	= "place_auto_renew_order";
	
	private String action;
	private String orderId;
	private String result;
	private String successPage;

	public void setAction(String s) {
		this.action = s;
	}
	public void setOrderId(String s) {
		this.orderId = s;
	}

    public void setResult(String s) {
        this.result = s;
    }

    public void setSuccessPage(String sp) {
        this.successPage = sp;
    }

    public int doStartTag() throws JspException {

		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		ActionResult results = new ActionResult();

		boolean actionPerformed = false;

		LOGGER.debug("Action in doStartTag: "+this.action);

		if ( "POST".equalsIgnoreCase( request.getMethod() ) ) {

			if (this.action==null) {
				this.action = request.getParameter("action");
			}
			//
			// Check action attribute
			//
			if ( CANCEL_ACTION.equalsIgnoreCase(this.action) ) {
				this.cancelOrder(request, results);
				actionPerformed = true;
			} else if ( MODIFY_ACTION.equalsIgnoreCase(this.action) ) {
				this.modifyOrder(request, results);
				actionPerformed = true;
			} else if ( CANCEL_MODIFY_ACTION.equalsIgnoreCase(this.action) ) {
				this.cancelModifyOrder(request, results);
				actionPerformed = true;
			} else if ( NEW_AUTHORIZATION.equalsIgnoreCase(this.action) ) {
				this.doNewAuthorization(request, results);
				actionPerformed = true;
			} else if ( NEW_AUTHORIZATION_FAILED_SETTLEMENT.equalsIgnoreCase(this.action) ) {
				this.doResubmitPayment(request, results);
				actionPerformed = true;
			} else if ( RETURN_ACTION.equalsIgnoreCase(this.action) ) {
				this.returnOrder(request, results);
				actionPerformed = true;
			} else if ( RESUBMIT_ACTION.equalsIgnoreCase(this.action) ) {
				this.resubmitOrder(request, results);
				actionPerformed = true;
				StringBuffer successPage = new StringBuffer();
				successPage.append(this.successPage);
				if (this.successPage.indexOf("?") < 0) { 
					successPage.append("?");
				} else {
					successPage.append("&");
				}
				successPage.append("status=resubmitted");
				setSuccessPage(successPage.toString());
			}else if ( REDELIVERY_ACTION.equals(this.action)){
				this.redeliverOrder(request, results);
				actionPerformed = true;
			} else if ( CHARGE_ORDER_ACTION.equalsIgnoreCase(this.action) ) {
				this.chargeOrder(request, results);
				actionPerformed = true;
			} else if ( AUTO_RENEW_AUTH.equalsIgnoreCase(this.action) ) {
				this.auto_renew_auth(request, results);
				actionPerformed = true;
			}else if ( PLACE_AUTO_RENEW_ORDER.equalsIgnoreCase(this.action) ) {
				this.place_auto_renew_order(request, results);
				actionPerformed = true;
				StringBuffer successPage = new StringBuffer();
				successPage.append(this.successPage);
				successPage.append(this.orderId);
				setSuccessPage(successPage.toString());
			}



		} else if (this.action!=null && this.action.equalsIgnoreCase(CANCEL_MODIFY_ACTION)) {

			LOGGER.debug("GET + cancelModify");
			// we got a GET, not a POST, but that's fine.. :)
			this.cancelModifyOrder(request, results);
			actionPerformed = true;

		}

		//
		// redirect to success page if an action was successfully performed
		// and a success page was defined
		//
		if (actionPerformed && results.isSuccess() && successPage!=null) {
			LOGGER.debug("Success, redirecting to: "+successPage);
			HttpServletResponse response = (HttpServletResponse) pageContext.getResponse();
			try {
				response.sendRedirect(response.encodeRedirectURL(successPage));
				JspWriter writer = pageContext.getOut();
				writer.close();
			} catch (IOException ioe) {
				throw new JspException(ioe.getMessage());
			}
		}
		if ( !PLACE_AUTO_RENEW_ORDER.equalsIgnoreCase(this.action) && request.getRequestURI().indexOf("place_auto_renew_order")<0 )
			this.setOrderActivityPermissions(results);

		pageContext.setAttribute(result, results);
		return EVAL_BODY_BUFFERED;

	} // method doStartTag

	protected void cancelOrder(HttpServletRequest request, ActionResult results) throws JspException {
		HttpSession session = request.getSession();

		FDSessionUser user = (FDSessionUser) session.getAttribute(SessionName.USER);
		if (user.getLevel() < FDUserI.SIGNED_IN) {
			throw new JspException("No customer was found for the requested action.");
		}
        
        EnumTransactionSource transactionSource = session.getAttribute(SessionName.CUSTOMER_SERVICE_REP)!=null || CrmSession.getCurrentAgent(session)!=null ? EnumTransactionSource.CUSTOMER_REP : EnumTransactionSource.WEBSITE;

        // !!! determine this
		boolean sendEmail = true;
        
        if (EnumTransactionSource.CUSTOMER_REP.equals(transactionSource)) {  
            //
            // check for silent order modification (CALLCENTER)
            //
            sendEmail = request.getParameter("silent_mode") == null;
            
        }

       
		String reason = NVL.apply( request.getParameter("cancel_reason"), "").trim();
		String notes = NVL.apply( request.getParameter("cancel_notes"), "").trim();
        
		StringBuffer sb = new StringBuffer("Order #");
		sb.append(this.orderId);
		sb.append(" cancelled: ");
		if (!"".equals(reason)) {
			sb.append(reason+". ");
		}
		if (!"".equals(notes) ) {
			sb.append(notes);
		}
		
		FDActionInfo info = AccountActivityUtil.getActionInfo(session, sb.toString());

		//
		// Cancel the order
		//
		try {
			FDOrderI origOrder = FDCustomerManager.getOrder(user.getIdentity(), orderId);
			if (!EnumTransactionSource.CUSTOMER_REP.equals(transactionSource)) {
				// check original cutoff
				

				Date origCutoff = origOrder.getDeliveryReservation().getCutoffTime();
				if (new Date().after(origCutoff)) {
					results.addError(new ActionError("invalid_reservation", MessageFormat.format(
						SystemMessageList.MSG_CHECKOUT_PAST_CUTOFF_MODIFY,
						new Object[] {origCutoff})));
					return;
				}
			}
			
			FDReservation restoredRsv = FDCustomerManager.cancelOrder(info, this.orderId, sendEmail);
			if(restoredRsv != null){
				user.setReservation(restoredRsv);
			}
			/*
			 * Reload delivery pass status to allow user to buy another delivery pass with in the
			 * same session if he cancelled an order that contained a delivery pass and he has no 
			 * delivery passes active.
			 */
			user.updateDlvPassInfo();
			//Remove the Delivery Pass Session ID If any.
			session.removeAttribute(DlvPassConstants.DLV_PASS_SESSION_ID);

			//if it's make good order, reject complaints 
			if(EnumPaymentType.MAKE_GOOD.equals(origOrder.getPaymentMethod().getPaymentType())){
				CallCenterServices.rejectMakegoodComplaint(orderId);
			}

		} catch(DeliveryPassException ex) {
			LOGGER.error("Error performing a Delivery pass operation. ", ex);
			//There was delivery pass validation failure.
			results.addError(new ActionError(ex.getMessage()));
		} catch (FDResourceException ex) {
			LOGGER.warn("Error accessing resources", ex);
			throw new JspException(ex.getMessage());
		} catch (ErpTransactionException ex) {
			results.addError(new ActionError("order_status", "This order is not in the proper state to be cancelled."));
			LOGGER.error("Order " + this.orderId + " was not in the proper state to be cancelled.", ex);
		}

		user.invalidateCache();
        // make sure we're not using stale order history data.
		user.invalidateOrderHistoryCache();
		//invalidate gift cards.
		user.invalidateGiftCards();
		session.setAttribute( SessionName.USER, user );

	}

	protected void modifyOrder(HttpServletRequest request, ActionResult results) throws JspException {
		HttpSession session = request.getSession();

		FDSessionUser currentUser = (FDSessionUser) session.getAttribute(SessionName.USER);
		if (currentUser.getLevel() < FDUserI.SIGNED_IN) {
			throw new JspException("No customer was found for the requested action.");
		}
		//
		// Modify order: load the shopping cart with the old items
		//
		try {
			FDOrderAdapter order = (FDOrderAdapter) FDCustomerManager.getOrder( currentUser.getIdentity(), this.orderId );

			FDCustomerManager.storeUser(currentUser.getUser());

			FDCartModel cart = new FDModifyCartModel(order);
			cart.refreshAll();
			currentUser.setShoppingCart( cart );
			//Reload gift card balance.
			loadGiftCardsIntoCart(currentUser, order);
			// resolve timeslot id based on delivery reservation id
			FDReservation reservation = FDDeliveryManager.getInstance().getReservation( order.getDeliveryReservationId() );
			cart.setDeliveryReservation(reservation);
			
			// resolve the redemption promotions
			for (Iterator i = order.getUsedPromotionCodes().iterator(); i.hasNext();) {
				String promoCode = (String) i.next();
				Promotion promo = (Promotion) PromotionFactory.getInstance().getPromotion(promoCode);
				RedemptionCodeStrategy redemption = (RedemptionCodeStrategy) promo.getStrategy(RedemptionCodeStrategy.class);
				if (redemption != null) {
					currentUser.setRedeemedPromotion(PromotionFactory.getInstance().getPromotion(promoCode));
				}
			}
			
			// recalculate promotion
			currentUser.invalidateCache();
			currentUser.updateUserState( );
			session.setAttribute( SessionName.USER, currentUser );
            //The previous recommendations of the current user need to be removed.
            session.removeAttribute(SessionName.SMART_STORE_PREV_RECOMMENDATIONS);

		} catch (FDException ex) {
			LOGGER.warn("Unable to create modify cart", ex);
			throw new JspException(ex.getMessage());
		}
	}

	private void loadGiftCardsIntoCart(FDUserI user, FDOrderI originalOrder) {
		FDGiftCardInfoList gcList = user.getGiftCardList();
		//Clear any hold amounts.
		gcList.clearAllHoldAmount();
    	List appliedGiftCards = originalOrder.getAppliedGiftCards();
    	if(appliedGiftCards != null && appliedGiftCards.size() > 0) {
	    	for(Iterator it = appliedGiftCards.iterator(); it.hasNext();) {
	    		ErpAppliedGiftCardModel agcmodel = (ErpAppliedGiftCardModel) it.next();
	    		String certNum = agcmodel.getCertificateNum();
	    		FDGiftCardI fg = gcList.getGiftCard(certNum);
	    		if(fg != null) {
	    			//Found. Gift card already validated. set hold amount = amount applied on this order.
	    			fg.setHoldAmount(originalOrder.getAppliedAmount(certNum));
	    		} 
	    	}
    	}
	}
	protected void returnOrder(HttpServletRequest request, ActionResult results) throws JspException {
		HttpSession session = request.getSession();

		FDSessionUser currentUser = (FDSessionUser) session.getAttribute(SessionName.USER);
		if (currentUser.getLevel() < FDUserI.SIGNED_IN) {
			throw new JspException("No customer was found for the requested action.");
		}
		//
		// Return order
		//
		/*try {
			CallCenterServices.returnOrder(currentUser.getIdentity(), this.orderId);

		} catch (ErpTransactionException ex) {
			LOGGER.error("Order cannot be returned because it is not in proper status", ex);
			results.addError(new ActionError("order_status", "This order is not in the proper state to be returned."));
		} catch (FDResourceException ex) {
			LOGGER.error("Error accessing resources", ex);
			throw new JspException(ex.getMessage());
		}*/
	}


	protected void cancelModifyOrder(HttpServletRequest request, ActionResult results) throws JspException {
		HttpSession session = request.getSession();

		FDSessionUser currentUser = (FDSessionUser) session.getAttribute(SessionName.USER);
		if (currentUser.getLevel() < FDUserI.SIGNED_IN) {
			throw new JspException("No customer was found for the requested action.");
		}
		//
		// Cancel Modify order: load the shopping cart with the old items
		//
		try {
			FDCartModel originalCart = FDCustomerManager.recognize(currentUser.getIdentity()).getShoppingCart();
			currentUser.setShoppingCart( originalCart );
			currentUser.invalidateCache();

			session.setAttribute( SessionName.USER, currentUser );
            //The previous recommendations of the current user need to be removed.
            session.removeAttribute(SessionName.SMART_STORE_PREV_RECOMMENDATIONS);
    		FDGiftCardInfoList gcList = currentUser.getGiftCardList();
    		//Clear any hold amounts.
    		gcList.clearAllHoldAmount();
            
		} catch (FDResourceException ex) {
			LOGGER.warn("Error accessing resources", ex);
			throw new JspException(ex.getMessage());
		} catch (FDAuthenticationException fdae) {
            LOGGER.warn("Error authenticating user to access original shopping cart", fdae);
            throw new JspException(fdae.getMessage());
        }
	}


	protected void resubmitOrder(HttpServletRequest request, ActionResult results) throws JspException {
		HttpSession session = request.getSession();

		FDSessionUser currentUser = (FDSessionUser) session.getAttribute(SessionName.USER);
		if (currentUser.getLevel() < FDUserI.SIGNED_IN) {
			throw new JspException("No customer was found for the requested action.");
		}
		//
		// Cancel Modify order: load the shopping cart with the old items
		//
		try {
			FDSessionUser user = (FDSessionUser) session.getAttribute(SessionName.USER);
			FDOrderI order = FDCustomerManager.getOrder(this.orderId);
			CustomerRatingAdaptor cra = new CustomerRatingAdaptor(user.getFDCustomer().getProfile(),user.isCorporateUser(),user.getAdjustedValidOrderCount());
			CallCenterServices.resubmitOrder(this.orderId,cra,order.getOrderType());

		} catch (FDResourceException ex) {
			LOGGER.warn("Error accessing resources", ex);
			throw new JspException(ex.getMessage());
		} catch (ErpTransactionException ex) {
            LOGGER.warn("Order not in the proper state to be resubmitted.", ex);
            results.addError(new ActionError("order_status", "This order is not in the proper state to be resubmitted."));
        }
	}
	
	private void redeliverOrder(HttpServletRequest request, ActionResult results) throws JspException{
		HttpSession session = request.getSession();
		FDSessionUser currentUser = (FDSessionUser) session.getAttribute(SessionName.USER);
		if(currentUser.getLevel() < FDUserI.SIGNED_IN){
			throw new JspException("No customer was found for the requested action.");
		}
	}


	protected void doNewAuthorization(HttpServletRequest request, ActionResult results) throws JspException {
		HttpSession session = request.getSession();

		FDSessionUser currentUser = (FDSessionUser) session.getAttribute(SessionName.USER);
		if (currentUser.getLevel() < FDUserI.SIGNED_IN) {
			throw new JspException("No customer was found for the requested action.");
		}
		//
		// Select new payment and resubmit order
		//
		String paymentId = request.getParameter("payment_id");
		if (paymentId != null && !"".equals(paymentId.trim())) {
			try {
				ErpCustomerModel erpCustomer = FDCustomerFactory.getErpCustomer(currentUser.getIdentity());
				//
				// Get payment method
				//
				ErpPaymentMethodI paymentMethod = null;
				ErpPaymentMethodI tmpPM = null;
				Collection payments = erpCustomer.getPaymentMethods();
				for (Iterator it = payments.iterator(); it.hasNext(); ) {
					ErpPaymentMethodI p = (ErpPaymentMethodI) it.next();
					tmpPM = (ErpPaymentMethodModel) p;
					if (((ErpPaymentMethodModel)tmpPM).getPK().getId().equals(paymentId)) {
						paymentMethod = tmpPM;
						break;
					}
				}
				//
				// Get order to modify
				//
				FDOrderAdapter order = (FDOrderAdapter) FDCustomerManager.getOrder( currentUser.getIdentity(), this.orderId );

				//
				// Get ModifyCart model
				//
				FDCustomerManager.storeUser(currentUser.getUser());
				FDModifyCartModel modCart = new FDModifyCartModel(order);
				//
				// Get current zone id for cart
				//
				//String regionName = (session.getAttribute(SessionName.DELIVERY_REGION) == null) ? "URBAN" : (String) session.getAttribute(SessionName.DELIVERY_REGION);
				Calendar date = new GregorianCalendar();
				date.add(Calendar.DATE, 7);
				DlvZoneInfoModel zoneInfo = AddressUtil.getZoneInfo(request, modCart.getDeliveryAddress(), results, date.getTime());
				if ( results.isSuccess() ) {
					String zoneId = zoneInfo.getZoneCode();
					if(zoneId==null || zoneId.length()==0) {
						results.addError(new ActionError("technical_difficulty", "Unable to get ZoneId"));
						return;						
					}

					modCart.setZoneInfo(zoneInfo);
					modCart.refreshAll();
					currentUser.updateUserState();

					//FDCustomerManager.doNewAuthorization(currentUser.getIdentity(), this.orderId, modCart, paymentMethod);

		        	modCart.setPaymentMethod(paymentMethod);
					FDSessionUser user = (FDSessionUser) session.getAttribute(SessionName.USER);
		        	CustomerRatingAdaptor cra = new CustomerRatingAdaptor(user.getFDCustomer().getProfile(),user.isCorporateUser(),user.getAdjustedValidOrderCount());
					Set appliedPromos = null;
					if(user.isEligibleForSignupPromotion()){
						//If applied promo is signup then store the eligibilty list.
						appliedPromos = user.getPromotionEligibility().getEligiblePromotionCodes();
					}else{
						//Otherwise store the applied list.
						appliedPromos = user.getPromotionEligibility().getAppliedPromotionCodes();; 
					}
					FDCustomerManager.modifyOrder(AccountActivityUtil.getActionInfo(session), modCart, appliedPromos, false,cra, user.getDeliveryPassStatus());

				}
			} catch (ErpFraudException ex) {
				LOGGER.warn("Possible fraud occured", ex);
				results.addError(new ActionError("order_status", "Possible fraud occured. "+ex.getFraudReason().getDescription()));

			} catch (ErpAuthorizationException ex) {
				LOGGER.warn("Authorization failed", ex);
				results.addError(new ActionError("order_status", "Authorization failed."));
		
			} catch (FDPaymentInadequateException ex) {
				LOGGER.error("Payment Inadequate to process the ReAuthorization", ex);
				results.addError(new ActionError("payment_inadequate", SystemMessageList.MSG_PAYMENT_INADEQUATE));

			} catch (ErpTransactionException ex) {
				LOGGER.error("Current sale status incompatible with requested action", ex);
				results.addError(new ActionError("order_status", "This current order status does not permit the requested action."));

			} catch (FDInvalidConfigurationException ex) {
				LOGGER.error("FDInvalidConfigurationException occured in doNewAuthorization.", ex);
				results.addError(new ActionError("technical_difficulty", "We're currently experiencing technical difficulties. Please try again later."));

			} catch (FDResourceException ex) {
				LOGGER.error("FDResourceException while attempting to perform reauthorization.", ex);
				results.addError(new ActionError("technical_difficulty", "We're currently experiencing technical difficulties. Please try again later."));
			} catch(DeliveryPassException ex) {
				LOGGER.error("Error performing a Delivery pass operation. ", ex);
				//There was delivery pass validation failure.
				results.addError(new ActionError(ex.getMessage()));
			}
			catch(ErpAddressVerificationException e){
				LOGGER.error("Error performing a modify order operation. ", e);
				//There was delivery pass validation failure.
				results.addError(new ActionError("technical_difficulty", e.getMessage()));							
			}
			
		} else {
			LOGGER.warn("No payment id selected by user");
			results.addError(new ActionError("no_payment_selected", "Please select a payment option below."));
		}
	} // method doNewAuthorization

	protected void auto_renew_auth(HttpServletRequest request, ActionResult results) throws JspException {
		HttpSession session = request.getSession();

		FDSessionUser currentUser = (FDSessionUser) session.getAttribute(SessionName.USER);
		if (currentUser.getLevel() < FDUserI.SIGNED_IN) {
			throw new JspException("No customer was found for the requested action.");
		}
		//
		// Select new payment and resubmit order
		//
		String paymentId = request.getParameter("payment_id");
		if (paymentId != null && !"".equals(paymentId.trim())) {
			try {
				ErpCustomerModel erpCustomer = FDCustomerFactory.getErpCustomer(currentUser.getIdentity());
				String erpCustomerID = currentUser.getIdentity().getErpCustomerPK();
				//
				// Get payment method
				//
				ErpPaymentMethodI paymentMethod = null;
				ErpPaymentMethodI tmpPM = null;
				Collection payments = erpCustomer.getPaymentMethods();
				for (Iterator it = payments.iterator(); it.hasNext(); ) {
					ErpPaymentMethodI p = (ErpPaymentMethodI) it.next();
					tmpPM = (ErpPaymentMethodModel) p;
					if (((ErpPaymentMethodModel)tmpPM).getPK().getId().equals(paymentId)) {
						paymentMethod = tmpPM;
						break;
					}
				}
				Date checkDate = new Date();
		        results.addError(
		    	        paymentMethod.getExpirationDate() == null || checkDate.after(paymentMethod.getExpirationDate()),
		    	        "expiration", SystemMessageList.MSG_CARD_EXPIRATION_DATE
		    	        );

				//
				// Get order to modify
				//
				FDOrderAdapter order = (FDOrderAdapter) FDCustomerManager.getOrder( currentUser.getIdentity(), this.orderId );
				//
				// Get ModifyCart model
				//
				FDCustomerManager.storeUser(currentUser.getUser());
				FDModifyCartModel modCart = new FDModifyCartModel(order);
				String addressID = modCart.getDeliveryAddress().getId();
				if ( results.isSuccess() ) {

		        	modCart.setPaymentMethod(paymentMethod);
		        	modCart.setDeliveryReservation(getFDReservation(erpCustomerID,addressID));
		        	modCart.recalculateTaxAndBottleDeposit(modCart.getDeliveryAddress().getZipCode());
		        	modCart.handleDeliveryPass();

					FDSessionUser user = (FDSessionUser) session.getAttribute(SessionName.USER);
		        	CustomerRatingAdaptor cra = new CustomerRatingAdaptor(user.getFDCustomer().getProfile(),user.isCorporateUser(),user.getAdjustedValidOrderCount());
					Set appliedPromos = null;
					if(user.isEligibleForSignupPromotion()){
						//If applied promo is signup then store the eligibilty list.
						appliedPromos = user.getPromotionEligibility().getEligiblePromotionCodes();
					}else{
						//Otherwise store the applied list.
						appliedPromos = user.getPromotionEligibility().getAppliedPromotionCodes();; 
					}
					modCart.refreshAll();
					FDCustomerManager.modifyOrder(AccountActivityUtil.getActionInfo(session), modCart, appliedPromos, false,cra, user.getDeliveryPassStatus());

				}
			} catch (ErpFraudException ex) {
				LOGGER.warn("Possible fraud occured", ex);
				results.addError(new ActionError("order_status", "Possible fraud occured. "+ex.getFraudReason().getDescription()));

			}catch (FDResourceException ex) {
				LOGGER.error("FDResourceException while attempting to perform reauthorization.", ex);
				results.addError(new ActionError("technical_difficulty", "We're currently experiencing technical difficulties. Please try again later."));
			} catch(DeliveryPassException ex) {
				LOGGER.error("Error performing a Delivery pass operation. ", ex);
				//There was delivery pass validation failure.
				results.addError(new ActionError(ex.getMessage()));
			} catch (FDPaymentInadequateException ex) {
				LOGGER.error("Payment Inadequate to process the ReAuthorization", ex);
				results.addError(new ActionError("payment_inadequate", SystemMessageList.MSG_PAYMENT_INADEQUATE));

			} catch (ErpTransactionException ex) {
				LOGGER.error("Current sale status incompatible with requested action", ex);
				results.addError(new ActionError("order_status", "This current order status does not permit the requested action."));
			}catch (ErpAuthorizationException ex) {
				LOGGER.warn("Authorization failed", ex);
				results.addError(new ActionError("order_status", "Authorization failed."));
			} catch (FDInvalidConfigurationException ex) {
				LOGGER.warn("invalid config", ex);
				results.addError(new ActionError("order_status", "Invalid configuration."));
			}
			catch(ErpAddressVerificationException e){
				LOGGER.error("Error performing a modify order operation. ", e);
				//There was delivery pass validation failure.
				results.addError(new ActionError(e.getMessage()));				
			}
		} else {
			LOGGER.warn("No payment id selected by user");
			results.addError(new ActionError("no_payment_selected", "Please select a payment option below."));
		}
	} 
	protected void place_auto_renew_order(HttpServletRequest request, ActionResult results) throws JspException {
		HttpSession session = request.getSession();

		FDSessionUser currentUser = (FDSessionUser) session.getAttribute(SessionName.USER);
		if (currentUser.getLevel() < FDUserI.SIGNED_IN) {
			throw new JspException("No customer was found for the requested action.");
		}
		//
		// Select new payment and resubmit order
		//
		String paymentId = request.getParameter("payment_id");
		if (paymentId != null && !"".equals(paymentId.trim())) {
			try {
				ErpCustomerModel erpCustomer = FDCustomerFactory.getErpCustomer(currentUser.getIdentity());
				String erpCustomerID = currentUser.getIdentity().getErpCustomerPK();
				//
				// Get payment method
				//
				ErpPaymentMethodI paymentMethod = null;
				ErpPaymentMethodI tmpPM = null;
				Collection payments = erpCustomer.getPaymentMethods();
				for (Iterator it = payments.iterator(); it.hasNext(); ) {
					ErpPaymentMethodI p = (ErpPaymentMethodI) it.next();
					tmpPM = (ErpPaymentMethodModel) p;
					if (((ErpPaymentMethodModel)tmpPM).getPK().getId().equals(paymentId)) {
						paymentMethod = tmpPM;
						break;
					}
				}
				Date checkDate = new Date();
		        results.addError(
		    	        paymentMethod.getExpirationDate() == null || checkDate.after(paymentMethod.getExpirationDate()),
		    	        "expiration", SystemMessageList.MSG_CARD_EXPIRATION_DATE
		    	        );
				if ( results.isSuccess() ) {
					FDSessionUser user = (FDSessionUser) session.getAttribute(SessionName.USER);
		        	CustomerRatingAdaptor cra = new CustomerRatingAdaptor(user.getFDCustomer().getProfile(),user.isCorporateUser(),user.getAdjustedValidOrderCount());

		        	ErpAddressModel address= FDCustomerManager.getLastOrderAddress(user.getIdentity());
					String arSKU = FDCustomerManager.getAutoRenewSKU(currentUser.getIdentity().getErpCustomerPK());
					if (arSKU != null){
						this.orderId = DeliveryPassRenewalCron.placeOrder(AccountActivityUtil.getActionInfo(session),cra, arSKU, paymentMethod, address);
					}else{
						throw new DeliveryPassException("Customer has no valid auto_renew DP. ",currentUser.getIdentity().getErpCustomerPK());
					
					}
				}
			} catch (FDResourceException ex) {
				LOGGER.error("FDResourceException while attempting to perform reauthorization.", ex);
				results.addError(new ActionError("technical_difficulty", "We're currently experiencing technical difficulties. Please try again later."));
			} catch(DeliveryPassException ex) {
				LOGGER.error("Error performing a Delivery pass operation. ", ex);
				//There was delivery pass validation failure.
				results.addError(new ActionError("delivery_pass_error","Error performing a Delivery pass operation. "+ex.getMessage()));
			}
		} else {
			LOGGER.warn("No payment id selected by user");
			results.addError(new ActionError("no_payment_selected", "Please select a payment option below."));
		}
	} 

	protected void doResubmitPayment(HttpServletRequest request, ActionResult results) throws JspException {
		HttpSession session = request.getSession();

		FDSessionUser currentUser = (FDSessionUser) session.getAttribute(SessionName.USER);
		if (currentUser.getLevel() < FDUserI.SIGNED_IN) {
			throw new JspException("No customer was found for the requested action.");
		}
		//
		// Select new payment and resubmit order
		//
		String paymentId = request.getParameter("payment_id");
		if (paymentId != null && !"".equals(paymentId.trim())) {
			try {
				ErpCustomerModel erpCustomer = FDCustomerFactory.getErpCustomer(currentUser.getIdentity());
				//
				// Get specified payment method
				//
				ErpPaymentMethodI paymentMethod = null;
				ErpPaymentMethodI tmpPM = null;
				Collection payments = erpCustomer.getPaymentMethods();
				for (Iterator it = payments.iterator(); it.hasNext(); ) {
					ErpPaymentMethodI p = (ErpPaymentMethodI) it.next();
					tmpPM = p;
					if (((ErpPaymentMethodModel)tmpPM).getPK().getId().equals(paymentId)) {
						paymentMethod = tmpPM;
						break;
					}
				}
				//
				// Get list of charges
				//
				Collection charges = new ArrayList();
				if ( !"true".equalsIgnoreCase(request.getParameter("waive_"+paymentId)) ) {
					ErpChargeLineModel line = new ErpChargeLineModel();
					// !!! NEED TO READ THIS DYNAMICALLY FROM SOMEWHERE
					line.setAmount(2.99);
					line.setType(EnumChargeType.CC_DECLINED);
					line.setReasonCode("RESUBMT");
					charges.add(line);
				}

				EnumPaymentResponse response = CallCenterServices.resubmitPayment(this.orderId, paymentMethod, charges);
				//
				// Check for any "Not approved" response
				//
				if ( !EnumPaymentResponse.APPROVED.equals(response) ) {
					results.addError(new ActionError("declinedCCD", "There was a problem with the selected payment method."));
				}

			} catch (ErpTransactionException ex) {
				LOGGER.error("Current sale status incompatible with requested action", ex);
				results.addError(new ActionError("order_status", "This current order status does not permit the requested action."));
			} catch (FDResourceException ex) {
				LOGGER.error("FDResourceException while attempting to perform reauthorization.", ex);
				results.addError(new ActionError("technical_difficulty", "We're currently experiencing technical difficulties. Please try again later."));
			}
		} else {
			LOGGER.warn("No payment id selected by user");
			results.addError(new ActionError("no_payment_selected", "Please select a payment option below."));
		}
	}


	protected void setOrderActivityPermissions(ActionResult results) {

		HttpSession session = pageContext.getSession();
		FDSessionUser user = (FDSessionUser) session.getAttribute(SessionName.USER);

		try {
			
			FDOrderI order = FDCustomerManager.getOrder(user.getIdentity(), this.orderId);
			String application = (String) session.getAttribute(SessionName.APPLICATION);
			boolean makeGood = EnumPaymentType.MAKE_GOOD.equals(order.getPaymentMethod().getPaymentType());
			
			OrderPermissionsI orderPermissions = new OrderPermissionsImpl(order.getOrderStatus(), application, makeGood, order.hasCreditIssued());
			
			// WHEN CAN I MODIFY AN ORDER?
			pageContext.setAttribute("allowModifyOrder", new Boolean(orderPermissions.allowModifyOrder()));
			
			// WHEN CAN I RETURN AN ORDER?
			pageContext.setAttribute("allowReturnOrder", new Boolean(orderPermissions.allowReturnOrder()));
			
			// 	REFUSED ORDER STATE
			pageContext.setAttribute("isRefusedOrder", new Boolean(orderPermissions.isRefusedOrder()));
			
			// WHEN CAN I CANCEL AN ORDER?
			pageContext.setAttribute("allowCancelOrder", new Boolean(orderPermissions.allowCancelOrder()));

			// WHEN CAN I ISSUE A CREDIT REQUEST (COMPLAINT)?
			pageContext.setAttribute("allowComplaint", new Boolean(orderPermissions.allowComplaint()));
			
			// WHEN CAN I LEVY FEES ON AN ORDER?
			pageContext.setAttribute("allowNewCharges", new Boolean(orderPermissions.allowNewCharges()));
			
			// WHEN DOES AN ORDER HAVE PAYMENT EXCEPTIONS?
			pageContext.setAttribute("hasPaymentException", new Boolean(orderPermissions.hasPaymentException()));
			
			// WHEN CAN I RESUBMIT AN ORDER?
			pageContext.setAttribute("allowResubmitOrder", new Boolean(orderPermissions.allowResubmitOrder()));

		} catch (FDResourceException ex) {
			LOGGER.error("FDResourceException trying to get order information.", ex);
			results.addError(new ActionError("technical_difficulty", "We're currently experiencing technical difficulties. Please try again later."));
		}

	}

	protected void chargeOrder(HttpServletRequest request, ActionResult results) throws JspException {
		HttpSession session = request.getSession();
		

		FDSessionUser user = (FDSessionUser) session.getAttribute(SessionName.USER);
		if(!EnumTransactionSource.CUSTOMER_REP.equals(user.getApplication())) {
			throw new JspException("This action can only be performed in CRM");
		}
		if (user.getLevel() < FDUserI.SIGNED_IN) {
			throw new JspException("No customer was found for the requested action.");
		}
		//
		// Select new payment and resubmit order
		//
		
		String paymentId = NVL.apply(request.getParameter("payment_id"), "").trim();
		boolean waiveFee = request.getParameter("waive") != null;
		String additionalCharge = NVL.apply(request.getParameter("additional_charge"), "").trim();
		
		if("".equals(paymentId)){
			results.addError(new ActionError("no_payment_selected", "Please select a payment option below."));
		}
		
		
		try {
			
			ErpPaymentMethodI paymentMethod = FDCustomerManager.getPaymentMethod(user.getIdentity(), paymentId);
			if(paymentMethod == null) {
				results.addError(new ActionError("no_payment_selected", "No payment method found for selected ID."));
			}
			
			if ( results.isSuccess() ) {
	        	CustomerRatingAdaptor cra = new CustomerRatingAdaptor(user.getFDCustomer().getProfile(),user.isCorporateUser(),user.getAdjustedValidOrderCount());
	        	double addCharge = !waiveFee && !"".equals(additionalCharge) ? Double.parseDouble(additionalCharge) : 0;
	        	FDCustomerManager.chargeOrder(AccountActivityUtil.getActionInfo(session), orderId, paymentMethod, true, cra, addCharge);
	        	CrmSession.invalidateCachedOrder(session);

			}
		} catch (ErpFraudException ex) {
			LOGGER.warn("Possible fraud occured", ex);
			results.addError(new ActionError("order_status", "Possible fraud occured. "+ex.getFraudReason().getDescription()));

		} catch (ErpAuthorizationException ex) {
			LOGGER.warn("Authorization failed", ex);
			results.addError(new ActionError("order_status", "Authorization failed."));
	
		} catch (FDPaymentInadequateException ex) {
			LOGGER.error("Payment Inadequate to process the ReAuthorization", ex);
			results.addError(new ActionError("payment_inadequate", SystemMessageList.MSG_PAYMENT_INADEQUATE));

		} catch (ErpTransactionException ex) {
			LOGGER.error("Current sale status incompatible with requested action", ex);
			results.addError(new ActionError("order_status", "This current order status does not permit the requested action."));

		} catch (FDResourceException ex) {
			LOGGER.error("FDResourceException while attempting to perform reauthorization.", ex);
			results.addError(new ActionError("technical_difficulty", "We're currently experiencing technical difficulties. Please try again later."));
		}
		 catch (ErpAddressVerificationException ex) {
				LOGGER.error("FDResourceException while attempting to perform reauthorization.", ex);
				results.addError(new ActionError("technical_difficulty", ex.getMessage()));
			}
		
	}
	private static FDReservation getFDReservation(String customerID, String addressID) {
		Date expirationDT = new Date(System.currentTimeMillis() + 1000);
		FDTimeslot timeSlot=null;
		FDReservation reservation=new FDReservation(new PrimaryKey("1"), timeSlot, expirationDT, EnumReservationType.STANDARD_RESERVATION, customerID, addressID, false,false, null,false,null,20);
		return reservation;
		
	}
	
} // ModifyOrderControllerTag