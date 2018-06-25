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
import com.freshdirect.customer.EnumTransactionSource;
import com.freshdirect.customer.ErpAddressModel;
import com.freshdirect.customer.ErpAddressVerificationException;
import com.freshdirect.customer.ErpAuthorizationException;
import com.freshdirect.customer.ErpChargeLineModel;
import com.freshdirect.customer.ErpCustomerModel;
import com.freshdirect.customer.ErpFraudException;
import com.freshdirect.customer.ErpPaymentMethodI;
import com.freshdirect.customer.ErpPaymentMethodModel;
import com.freshdirect.customer.ErpSaleModel;
import com.freshdirect.customer.ErpTransactionException;
import com.freshdirect.dataloader.subscriptions.DeliveryPassRenewalCron;
import com.freshdirect.deliverypass.DeliveryPassException;
import com.freshdirect.deliverypass.DlvPassConstants;
import com.freshdirect.fdlogistics.model.FDDeliveryZoneInfo;
import com.freshdirect.fdlogistics.model.FDReservation;
import com.freshdirect.fdlogistics.model.FDTimeslot;
import com.freshdirect.fdstore.CallCenterServices;
import com.freshdirect.fdstore.EnumCheckoutMode;
import com.freshdirect.fdstore.EnumEStoreId;
import com.freshdirect.fdstore.FDException;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.customer.FDActionInfo;
import com.freshdirect.fdstore.customer.FDAuthenticationException;
import com.freshdirect.fdstore.customer.FDCartLineI;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.fdstore.customer.FDCustomerFactory;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDInvalidConfigurationException;
import com.freshdirect.fdstore.customer.FDModifyCartModel;
import com.freshdirect.fdstore.customer.FDOrderI;
import com.freshdirect.fdstore.customer.FDPaymentInadequateException;
import com.freshdirect.fdstore.customer.FDUser;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.customer.FDUserUtil;
import com.freshdirect.fdstore.customer.adapter.CustomerRatingAdaptor;
import com.freshdirect.fdstore.customer.adapter.FDOrderAdapter;
import com.freshdirect.fdstore.giftcard.FDGiftCardInfoList;
import com.freshdirect.fdstore.promotion.ExtendDeliveryPassApplicator;
import com.freshdirect.fdstore.promotion.Promotion;
import com.freshdirect.fdstore.promotion.PromotionApplicatorI;
import com.freshdirect.fdstore.promotion.PromotionFactory;
import com.freshdirect.fdstore.promotion.PromotionI;
import com.freshdirect.fdstore.standingorders.FDStandingOrder;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.event.EnumEventSource;
import com.freshdirect.framework.util.NVL;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionError;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.logistics.delivery.model.EnumReservationType;
import com.freshdirect.storeapi.content.ProductModel;
import com.freshdirect.webapp.ajax.cart.ModifyOrderHelper;
import com.freshdirect.webapp.taglib.crm.CrmSession;
import com.freshdirect.webapp.util.FDEventUtil;
import com.freshdirect.webapp.util.OrderPermissionsI;
import com.freshdirect.webapp.util.OrderPermissionsImpl;
import com.freshdirect.webapp.util.ShoppingCartUtil;
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
	private final String AUTO_RENEW_AUTH	= "auto_renew_auth";
	private final String PLACE_AUTO_RENEW_ORDER	= "place_auto_renew_order";
	private static final String EWALLET_SESSION_ATTRIBUTE_NAME="EWALLET_CARD_TYPE";
	private static final String WALLET_SESSION_CARD_ID="WALLET_CARD_ID";

	
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

    @Override
    public int doStartTag() throws JspException {

		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		ActionResult results = new ActionResult();
		
		HttpSession session = request.getSession();
		FDSessionUser currentUser = (FDSessionUser) session.getAttribute(SessionName.USER);
		
		boolean actionPerformed = false;

		if (this.action==null) {
			this.action = request.getParameter("action");
		}

		LOGGER.debug("Action in doStartTag: "+this.action);

		if ( "POST".equalsIgnoreCase( request.getMethod() ) ) {

			//
			// Check action attribute
			//
			if ( CANCEL_ACTION.equalsIgnoreCase(this.action) ) {
				//if user is in modify order mode, cancel that first
				if (currentUser != null && currentUser.getShoppingCart() instanceof FDModifyCartModel) {
					String modOrderId = ((FDModifyCartModel)currentUser.getShoppingCart()).getOriginalOrder().getSale().getId();
					
					//but only if the order id being cancelled is the one being modified
					if (this.orderId.equalsIgnoreCase(modOrderId)) {
						this.cancelModifyOrder(request, results);
					}
				}
				
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



		} else { 
			if ( CANCEL_MODIFY_ACTION.equalsIgnoreCase(this.action) ) {
				LOGGER.debug("GET + cancelModify");
				// we got a GET, not a POST, but that's fine.. :)
				this.cancelModifyOrder(request, results);
				actionPerformed = true;
			} else if ( MODIFY_ACTION.equalsIgnoreCase(this.action) ) {
				this.modifyOrder(request, results);
				actionPerformed = true;
				
				// Remove the EWallet Payment MEthod ID from session
				if(session.getAttribute(EWALLET_SESSION_ATTRIBUTE_NAME) != null){
					session.removeAttribute(EWALLET_SESSION_ATTRIBUTE_NAME);
				}if(session.getAttribute(WALLET_SESSION_CARD_ID) != null){
					session.removeAttribute(WALLET_SESSION_CARD_ID);
				}
			}
		}

		//
		// redirect to success page if an action was successfully performed
		// and a success page was defined
		//
		if (actionPerformed && results.isSuccess() && successPage!=null && !"noSuccess".equals(successPage)) {
			LOGGER.debug("Success, redirecting to: "+successPage);
			HttpServletResponse response = (HttpServletResponse) pageContext.getResponse();
			try {
				response.sendRedirect(response.encodeRedirectURL(successPage));
				//JspWriter writer = pageContext.getOut();
				//writer.close();
			} catch (IOException ioe) {
				throw new JspException(ioe.getMessage());
			}
		}
		if (actionPerformed && results.isSuccess() && "noSuccess".equals(successPage)) {
			try {
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
        
        EnumTransactionSource transactionSource = session.getAttribute(SessionName.CUSTOMER_SERVICE_REP)!=null || CrmSession.getCurrentAgent(session)!=null || user.getMasqueradeContext()!=null ? EnumTransactionSource.CUSTOMER_REP : EnumTransactionSource.WEBSITE;

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
		
		cancelOrder(session, user, this.orderId, transactionSource, sendEmail, reason, notes, results);		

	}
	
	public static void cancelOrder(HttpSession session, FDSessionUser user, String orderId, EnumTransactionSource transactionSource, boolean sendEmail, String reason, String notes, ActionResult results) throws JspException{
        
		StringBuffer sb = new StringBuffer("Order Cancelled (");
		if (!"".equals(reason)) {
			sb.append(reason);
			sb.append(". ");
		}
		if (!"".equals(notes) ) {
			sb.append(notes);
		}
		sb.append(")");
		
		FDActionInfo info = AccountActivityUtil.getActionInfo(session, sb.toString());

		//
		// Cancel the order
		//
		try {
			FDOrderAdapter origOrder = (FDOrderAdapter) FDCustomerManager.getOrder(user.getIdentity(), orderId);
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
			
			// Check if this order has a extend delivery pass promotion. If so get the no. of extended days.
			int currentDPExtendDays = 0;
			Set<String> usedPromoCodes = origOrder.getSale().getUsedPromotionCodes();
			for(Iterator<String> it = usedPromoCodes.iterator(); it.hasNext();){
				PromotionI promo  = PromotionFactory.getInstance().getPromotion(it.next());
				//APPDEV-2850 - combinable offers
				if(promo != null) {
					for (Iterator<PromotionApplicatorI> i = ((Promotion)promo).getApplicatorList().iterator(); i.hasNext();) {
						PromotionApplicatorI _applicator = i.next();
						if (_applicator instanceof ExtendDeliveryPassApplicator) {
							ExtendDeliveryPassApplicator app = (ExtendDeliveryPassApplicator) _applicator;
							currentDPExtendDays = app.getExtendDays();
						}
					}
				}
				
				/*
				if (promo != null && promo.isExtendDeliveryPass()){
					ExtendDeliveryPassApplicator app = (ExtendDeliveryPassApplicator)((Promotion)promo).getApplicator();
					currentDPExtendDays = app.getExtendDays();
				}
				*/
			}

			FDReservation restoredRsv = FDCustomerManager.cancelOrder(info, orderId, sendEmail, currentDPExtendDays, true);
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
			//Clear All Applied promotions
			user.clearAllAppliedPromos();
			//Refresh customer's coupon wallet.
			FDCustomerCouponUtil.getCustomerCoupons(session);

			//if it's make good order, reject complaints 
			if(/*EnumPaymentType.MAKE_GOOD.equals(origOrder.getPaymentMethod().getPaymentType())*/ origOrder.isMakeGood()){
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
			LOGGER.error("Order " + orderId + " was not in the proper state to be cancelled.", ex);
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
		
		String mergePendingStr = request.getParameter("mergePending");
		boolean mergePernding = mergePendingStr != null && mergePendingStr.equals("1");

		FDSessionUser currentUser = (FDSessionUser) session.getAttribute(SessionName.USER);
		if (currentUser.getLevel() < FDUserI.SIGNED_IN) {
			throw new JspException("No customer was found for the requested action.");
		}
		//
		// Modify order: load the shopping cart with the old items
		//
		try {
			modifyOrder(request, currentUser, orderId, session, null, EnumCheckoutMode.NORMAL, mergePernding, results);

			//set user as having seen the overlay and used it (in case of login step)
			currentUser.setSuspendShowPendingOrderOverlay(true);
			//set inform ordermodify flag
			currentUser.setShowingInformOrderModify(true);
		}catch (FDException ex) {
			LOGGER.warn("Unable to create modify cart", ex);
			throw new JspException(ex.getMessage());
		}
	}

	
	protected static void doCancelModifyOrder(HttpSession session, FDSessionUser currentUser) throws FDAuthenticationException, FDResourceException {
		
		String currentOrderId = null;
		try{
			
			currentOrderId = ((FDModifyCartModel)currentUser.getShoppingCart()).getOriginalOrder().getSale().getId();
		
			FDCustomerManager.releaseModificationLock(currentOrderId);
		}catch(Exception e){
			LOGGER.info("Unable to release the in_modify lock for orderId"+currentOrderId);
		}
		
		/* save FDCustomerEStore model before restore cart, which invalidates and reloads */
        try {
        	if (currentUser != null && currentUser.getIdentity() != null && currentUser.getIdentity().getErpCustomerPK() != null) {
            	LOGGER.debug("Updating FDCustomerEStore (custId:"+currentUser.getIdentity().getErpCustomerPK()+")");
				FDCustomerManager.updateFDCustomerEStoreInfo(currentUser.getFDCustomer().getCustomerEStoreModel(), currentUser.getFDCustomer().getId());	
        	}
		} catch (FDResourceException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		FDCustomerManager.clearModifyCartlines(currentOrderId);
		ShoppingCartUtil.restoreCart(session);
			
		FDGiftCardInfoList gcList = currentUser.getGiftCardList();
		//Clear any hold amounts.
		gcList.clearAllHoldAmount();
        
		//reset user to see pendingOrder overlay again since they didn't check out
		currentUser.setSuspendShowPendingOrderOverlay(false);
		//clear inform ordermodify flag
		currentUser.setShowingInformOrderModify(false);
	}


	public static FDModifyCartModel modifyOrder(HttpServletRequest request, FDSessionUser currentUser, String orderId, HttpSession session,
			FDStandingOrder currentStandingOrder, EnumCheckoutMode checkOutMode, boolean mergePending, ActionResult results)
					throws FDResourceException, FDInvalidConfigurationException{

		if (currentUser != null && currentUser.getShoppingCart() instanceof FDModifyCartModel ) {
			// there's a modify order already going on ...
			LOGGER.warn("An order is already opened for modification, cancel it first");
			
			try {
				doCancelModifyOrder(session, currentUser);
			} catch (FDAuthenticationException e) {
				// it is unlikely to happen but report it anyway
				LOGGER.error(e);
			}

			session.removeAttribute(SessionName.MODIFY_CART_PRESELECTION_COMPLETED);
			session.removeAttribute(SessionName.PAYMENT_BILLING_REFERENCE);
		}


		FDOrderAdapter order = (FDOrderAdapter) FDCustomerManager.getOrder( currentUser.getIdentity(), orderId );
		
		ModifyOrderHelper.handleModificationCutoff(order, currentUser, session, results);
				
		FDCustomerManager.storeUser(currentUser.getUser());
		FDModifyCartModel cart = new FDModifyCartModel(order);
		
		//this is added because the delivery pass is false when you modify the order though original order has delivery pass applied. This will fix any rules that use dlvpassapplied flag for applying charge
		FDUser fdUser =  currentUser.getUser();
		ModifyOrderHelper.handleDlvPass(cart, fdUser);
				
		if (mergePending) {
			FDCartModel tempMergePendCart = currentUser.getMergePendCart();
			
			if (tempMergePendCart.getOrderLines().size() > 0) {
				tempMergePendCart = removeOverflowingLines(cart, tempMergePendCart, currentUser); // check maximum quantity limits
//Reasons/intentions remain to be unknown. Commenting out, checking out the results. 				
//				List<FDCartLineI> tmpOrderLines = tempMergePendCart.getOrderLines();
//				for (FDCartLineI tmpOrderLine : tmpOrderLines)
//					tmpOrderLine.setRecipeSourceId(null);

				// merge into order's cart
				cart.mergeCart(tempMergePendCart);

				cart.refreshAll(true);
				
				currentUser.updateUserState();

				cart.sortOrderLines();
				
				// TODO we may want to distinguish between this kind of
				// add and regular one
				int n = tempMergePendCart.getOrderLines().size();
				for (int i = 0; i < n; i++) {
					FDCartLineI orderLine = tempMergePendCart.getOrderLine(i);
					orderLine.setSource(EnumEventSource.BROWSE);
					FDEventUtil.logAddToCartEvent(orderLine, request);
				}
				
				// TODO set latest SKUs 
				// I'm not sure if this needs to be added here
				// session.setAttribute("SkusAdded", frmSkuIds);
				
						
				//remove temp cart from session
				currentUser.setMergePendCart(null);
				
				// save previous cart before moving on
				currentUser.saveCart();
			} else {
				throw new FDResourceException("session timed out");
			}
		}
		
		// Check if this order has a extend delivery pass promotion. If so get the no. of extended days.
		ModifyOrderHelper.handleDeliveryPassPromotion(currentUser, currentStandingOrder, checkOutMode, order, cart);
		
		List<FDCartLineI> modifiedCartlines = FDCustomerManager.getModifiedCartlines(cart.getOriginalOrder().getSale().getId(), fdUser.getUserContext());
		
		if(fdUser.getMasqueradeContext() == null && (null != modifiedCartlines && modifiedCartlines.size() > 0)){
			cart.addOrderLines(modifiedCartlines);
		}
		
		// Check if this order has a extend delivery pass promotion. If so get the no. of extended days.
		ModifyOrderHelper.handleDeliveryPassPromotion(currentUser, currentStandingOrder, checkOutMode, order, cart);
				
		//Reload gift card balance.
		ModifyOrderHelper.loadGiftCardsIntoCart(currentUser, order);
		
		if(EnumEStoreId.FDX.name().equalsIgnoreCase(order.getEStoreId().name())){
			cart.setDeliveryReservation(order.getDeliveryReservation());
		}else{
			// resolve timeslot id based on delivery reservation id
			ModifyOrderHelper.handleReservation(order, cart);
		}
		
		
		// resolve the redemption promotions
		ModifyOrderHelper.handleRedemptionPromotions(currentUser, order);
		//Refresh customer's coupon wallet.
		ModifyOrderHelper.handleCoupons(session);

		ModifyOrderHelper.updateSession(currentUser, session);
		
		FDCustomerManager.updateOrderInModifyState(order);
        
		currentUser.resetUserContext();
		currentUser.getShoppingCart().setDeliveryPlantInfo(FDUserUtil.getDeliveryPlantInfo(currentUser.getUserContext(false)));

        return cart;
	}
	
	
	private static FDCartModel removeOverflowingLines(FDCartModel currentCart, FDCartModel tempCart, FDUserI user) {
		FDCartModel newCart = new FDCartModel();
		int n = tempCart.getOrderLines().size();
		for (int i = 0; i < n; i++) {
			FDCartLineI orderLine = tempCart.getOrderLine(i);
			ProductModel product = orderLine.getProductRef().lookupProductModel();
			if (newCart.getTotalQuantity(product) + currentCart.getTotalQuantity(product) + orderLine.getQuantity() <=
				user.getQuantityMaximum(product))
				newCart.addOrderLine(orderLine);
			else
				LOGGER.warn("SKIPPING: pending merge order line with product '" + product.getContentKey().getId() + "' due to quantity limit");
		}
		return newCart;
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
			doCancelModifyOrder(session, currentUser);
		} catch (FDResourceException ex) {
			LOGGER.warn("Error accessing resources", ex);
			throw new JspException(ex.getMessage());
		} catch (FDAuthenticationException fdae) {
            LOGGER.warn("Error authenticating user to access original shopping cart", fdae);
            throw new JspException(fdae.getMessage());
        }
		session.removeAttribute(SessionName.MODIFY_CART_PRESELECTION_COMPLETED);
        session.removeAttribute(SessionName.PAYMENT_BILLING_REFERENCE);
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
				Collection<ErpPaymentMethodI> payments = erpCustomer.getPaymentMethods();
				for (Iterator<ErpPaymentMethodI> it = payments.iterator(); it.hasNext(); ) {
					ErpPaymentMethodI p = it.next();
					tmpPM = p;
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
				FDDeliveryZoneInfo zoneInfo = AddressUtil.getZoneInfo(request, modCart.getDeliveryAddress(), results, date.getTime(), null, 
						modCart.getDeliveryReservation().getRegionSvcType());
				if ( results.isSuccess() ) {
					String zoneId = zoneInfo.getZoneCode();
					if(zoneId==null || zoneId.length()==0) {
						results.addError(new ActionError("technical_difficulty", "Unable to get ZoneId"));
						return;						
					}

					modCart.setZoneInfo(zoneInfo);
					modCart.refreshAll(true);
					currentUser.updateUserState();
					ErpSaleModel originalSale =modCart.getOriginalOrder().getSale();
					boolean hasSomeCaptures= (null !=originalSale.getCaptures() && originalSale.getCaptures().size() >0) 
							|| (null!=originalSale.getValidGCPostAuthorizations() && originalSale.getValidGCPostAuthorizations().size() >0);
					if(!hasSomeCaptures){
						FDCustomerCouponUtil.evaluateCartAndCoupons(session);
					}
					//FDCustomerManager.doNewAuthorization(currentUser.getIdentity(), this.orderId, modCart, paymentMethod);

		        	modCart.setPaymentMethod(paymentMethod);
					FDSessionUser user = (FDSessionUser) session.getAttribute(SessionName.USER);
		        	CustomerRatingAdaptor cra = new CustomerRatingAdaptor(user.getFDCustomer().getProfile(),user.isCorporateUser(),user.getAdjustedValidOrderCount());
					Set<String> appliedPromos = null;
					if(user.isEligibleForSignupPromotion()){
						//If applied promo is signup then store the eligibilty list.
						appliedPromos = user.getPromotionEligibility().getEligiblePromotionCodes();
					}else{
						//Otherwise store the applied list.
						appliedPromos = user.getPromotionEligibility().getAppliedPromotionCodes();; 
					}
					FDCustomerManager.modifyOrder(AccountActivityUtil.getActionInfo(session), modCart, appliedPromos, false,cra, user.getDeliveryPassStatus(),hasSomeCaptures);

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
				String message =e.getMessage();
				if(message!=null)
				    message=message.replace("9999",currentUser.getCustomerServiceContact());
				LOGGER.debug("ex.getMessage() :"+message);
				results.addError(new ActionError("technical_difficulty", message));							
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
				Collection<ErpPaymentMethodI> payments = erpCustomer.getPaymentMethods();
				for (Iterator<ErpPaymentMethodI> it = payments.iterator(); it.hasNext(); ) {
					ErpPaymentMethodI p = it.next();
					tmpPM = p;
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
					Set<String> appliedPromos = null;
					if(user.isEligibleForSignupPromotion()){
						//If applied promo is signup then store the eligibilty list.
						appliedPromos = user.getPromotionEligibility().getEligiblePromotionCodes();
					}else{
						//Otherwise store the applied list.
						appliedPromos = user.getPromotionEligibility().getAppliedPromotionCodes();; 
					}
					modCart.refreshAll(true);
					

					int fdcOrderCount = (FDStoreProperties.isFdcFirstOrderEmailMsgEnabled()) ? user.getOrderHistory().getValidOrderCount("1400") : -1;
					FDCustomerManager.modifyOrder(AccountActivityUtil.getActionInfo(session), modCart, appliedPromos, false,cra, user.getDeliveryPassStatus(),false);

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
				String message =e.getMessage();
				if(message!=null)
				    message=message.replace("9999",currentUser.getCustomerServiceContact());
				LOGGER.debug("ex.getMessage() :"+message);
				
				results.addError(new ActionError(message));				
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
				/// String erpCustomerID = currentUser.getIdentity().getErpCustomerPK();
				//
				// Get payment method
				//
				ErpPaymentMethodI paymentMethod = null;
				ErpPaymentMethodI tmpPM = null;
				Collection<ErpPaymentMethodI> payments = erpCustomer.getPaymentMethods();
				for (Iterator<ErpPaymentMethodI> it = payments.iterator(); it.hasNext(); ) {
					ErpPaymentMethodI p = it.next();
					tmpPM = p;
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
						this.orderId = DeliveryPassRenewalCron.placeOrder(AccountActivityUtil.getActionInfo(session),cra, arSKU, paymentMethod, address,user.getUserContext(),false);
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
				Collection<ErpPaymentMethodI> payments = erpCustomer.getPaymentMethods();
				for (Iterator<ErpPaymentMethodI> it = payments.iterator(); it.hasNext(); ) {
					ErpPaymentMethodI p = it.next();
					tmpPM = p;
					if (((ErpPaymentMethodModel)tmpPM).getPK().getId().equals(paymentId)) {
						paymentMethod = tmpPM;
						break;
					}
				}
				//
				// Get list of charges
				//
				Collection<ErpChargeLineModel> charges = new ArrayList<ErpChargeLineModel>();
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
			pageContext.setAttribute("allowModifyOrder", Boolean.valueOf(orderPermissions.allowModifyOrder()));
			
			// WHEN CAN I RETURN AN ORDER?
			pageContext.setAttribute("allowReturnOrder", Boolean.valueOf(orderPermissions.allowReturnOrder()));
			
			// 	REFUSED ORDER STATE
			pageContext.setAttribute("isRefusedOrder", Boolean.valueOf(orderPermissions.isRefusedOrder()));
			
			// WHEN CAN I CANCEL AN ORDER?
			pageContext.setAttribute("allowCancelOrder", Boolean.valueOf(orderPermissions.allowCancelOrder()));

			// WHEN CAN I ISSUE A CREDIT REQUEST (COMPLAINT)?
			pageContext.setAttribute("allowComplaint", Boolean.valueOf(orderPermissions.allowComplaint()));
			
			// WHEN CAN I LEVY FEES ON AN ORDER?
			pageContext.setAttribute("allowNewCharges", Boolean.valueOf(orderPermissions.allowNewCharges()));
			
			// WHEN DOES AN ORDER HAVE PAYMENT EXCEPTIONS?
			pageContext.setAttribute("hasPaymentException", Boolean.valueOf(orderPermissions.hasPaymentException()));
			
			// WHEN CAN I RESUBMIT AN ORDER?
			pageContext.setAttribute("allowResubmitOrder", Boolean.valueOf(orderPermissions.allowResubmitOrder()));

		} catch (FDResourceException ex) {
			LOGGER.error("FDResourceException trying to get order information.", ex);
			results.addError(new ActionError("technical_difficulty", "We're currently experiencing technical difficulties. Please try again later."));
		}

	}
	private static FDReservation getFDReservation(String customerID, String addressID) {
		Date expirationDT = new Date(System.currentTimeMillis() + 1000);
		FDTimeslot timeSlot=null;
		FDReservation reservation=new FDReservation(new PrimaryKey("1"), timeSlot, expirationDT, 
				EnumReservationType.STANDARD_RESERVATION, customerID, addressID,false, null,20,null,false,null,null);
		return reservation;
		
	}
	
} // ModifyOrderControllerTag