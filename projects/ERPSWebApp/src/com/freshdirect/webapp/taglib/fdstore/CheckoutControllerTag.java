package com.freshdirect.webapp.taglib.fdstore;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;

import org.apache.log4j.Category;

import com.freshdirect.common.context.MasqueradeContext;
import com.freshdirect.common.customer.EnumServiceType;
import com.freshdirect.customer.EnumChargeType;
import com.freshdirect.customer.EnumTransactionSource;
import com.freshdirect.customer.EnumUnattendedDeliveryFlag;
import com.freshdirect.customer.ErpAddressModel;
import com.freshdirect.customer.ErpAuthorizationException;
import com.freshdirect.customer.ErpPaymentMethodI;
import com.freshdirect.delivery.ReservationException;
import com.freshdirect.fdlogistics.model.FDTimeslot;
import com.freshdirect.fdstore.EnumCheckoutMode;
import com.freshdirect.fdstore.EnumEStoreId;
import com.freshdirect.fdstore.FDDeliveryManager;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.customer.FDCartLineI;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.fdstore.customer.FDCustomerInfo;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDOrderHistory;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.mail.FDEmailFactory;
import com.freshdirect.fdstore.services.tax.AvalaraContext;
import com.freshdirect.fdstore.standingorders.FDStandingOrder;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionError;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.logistics.analytics.model.TimeslotEvent;
import com.freshdirect.logistics.delivery.model.EnumCompanyCode;
import com.freshdirect.payment.EnumPaymentMethodType;
import com.freshdirect.webapp.action.Action;
import com.freshdirect.webapp.action.HttpContext;
import com.freshdirect.webapp.action.HttpContextAware;
import com.freshdirect.webapp.action.ResultAware;
import com.freshdirect.webapp.action.fdstore.SubmitOrderAction;
import com.freshdirect.webapp.checkout.DeliveryAddressManipulator;
import com.freshdirect.webapp.checkout.PaymentMethodManipulator;
import com.freshdirect.webapp.checkout.RedirectToPage;
import com.freshdirect.webapp.checkout.TimeslotManipulator;
import com.freshdirect.webapp.crm.util.MakeGoodOrderUtility;
import com.freshdirect.webapp.crm.util.MakeGoodOrderUtility.PostAction;
import com.freshdirect.webapp.crm.util.MakeGoodOrderUtility.SessionParamGetter;
import com.freshdirect.webapp.taglib.AbstractControllerTag;
import com.freshdirect.webapp.taglib.coremetrics.CmConversionEventTag;
import com.freshdirect.webapp.taglib.coremetrics.CmShop9Tag;
import com.freshdirect.webapp.util.StandingOrderHelper;
import com.freshdirect.webapp.util.StandingOrderUtil;



public class CheckoutControllerTag extends AbstractControllerTag {

    public static final String AUTHORIZATION_CUTOFF_PAGE = "/checkout/account_problem.jsp";

    private static final long serialVersionUID = 8993233587790486175L;

	private static Category		LOGGER				= LoggerFactory.getInstance( CheckoutControllerTag.class );

	private String				ccdProblemPage		= "/checkout/step_3_choose.jsp";
    private String authCutoffPage = AUTHORIZATION_CUTOFF_PAGE;
	private final String		ageVerificationPage	= "/checkout/step_2_verify_age.jsp";
	private final String		backToViewCart		= "/checkout/view_cart.jsp";
	private final String		ccdAddCardPage		= "/checkout/step_3_card_add.jsp";
	private String				gcFraudPage			= "/gift_card/purchase/purchase_giftcard.jsp";
	private final String		gcAVSExceptionPage	= "/gift_card/purchase/purchase_giftcard.jsp";
	private String 				noContactPhonePage	= "/checkout/step_1_edit.jsp";
	private String blockedAddressPage = "/checkout/no_alcohol_address.jsp";

	private final String ebtUnavailableItemsPage	= "/checkout/step_3_unavail.jsp";
	private final String ebtCRMUnavailableItemsPage	= "/checkout/checkout_EBT_unavail.jsp";
	
	public String getPaymentId() {			
		FDCartModel cart = getCart();
		String pId = null;
		ErpPaymentMethodI payMethods=null;
		if(cart != null){
			payMethods = cart.getPaymentMethod();				
			pId = payMethods.getPK().getId();	
		}
		return pId==null ? "":pId;	
	}
	
	/** Used by FDIntegrationService / CheckoutControllerTagWrapper */
	public String getEbtUnavailableItemsPage() {
		return ebtUnavailableItemsPage;
	}
	
	/** Used by FDIntegrationService / CheckoutControllerTagWrapper */
	public String getEbtCRMUnavailableItemsPage() {
		return ebtCRMUnavailableItemsPage;
	}

	/** Used by FDIntegrationService / CheckoutControllerTagWrapper */
	public String getAgeVerificationPage() {
		return ageVerificationPage;
	}

	public String getBlockedAddressPage() {
		return blockedAddressPage;
	}

	public void setBlockedAddressPage(String blockedAddressPage) {
		this.blockedAddressPage = blockedAddressPage;
	}

	/** Used by FDIntegrationService / CheckoutControllerTagWrapper */
	public String getAuthCutoffPage() {
		return authCutoffPage;
	}
	
	/** Used by FDIntegrationService / CheckoutControllerTagWrapper */
	public String getCcdProblemPage() {
		return ccdProblemPage;
	}
	
	/** Used by FDIntegrationService / CheckoutControllerTagWrapper */
	public String getGcAVSExceptionPage() {
		return gcAVSExceptionPage;
	}

	public void setCcdProblemPage( String ccdProblemPage ) {
		this.ccdProblemPage = ccdProblemPage;
	}

	public void setAuthCutoffPage( String authCutoffPage ) {
		this.authCutoffPage = authCutoffPage;
	}

	public void setGCFraudPage( String gcFraudPage ) {
		this.gcFraudPage = gcFraudPage;
	}

	public void setNoContactPhonePage(String noContactPhonePage) {
		this.noContactPhonePage = noContactPhonePage;
	}
	
	@Override
	protected boolean performAction( HttpServletRequest request, ActionResult result ) throws JspException {
		String action = this.getActionName();
		final HttpSession session = request.getSession();
		boolean saveCart = false;

		final String app = (String)pageContext.getSession().getAttribute( SessionName.APPLICATION );
		final boolean isNotCallCenter = !"CALLCENTER".equals( app );
		final FDSessionUser currentUser = (FDSessionUser) getUser();
		FDCartModel cart = currentUser.getShoppingCart();
		
		TimeslotEvent event = new TimeslotEvent((currentUser.getApplication()!=null)?currentUser.getApplication().getCode():"",
				(cart!=null)?cart.isDlvPassApplied():false, (cart!=null)?cart.getDeliverySurcharge():0.00,
						(cart!=null)?cart.isDeliveryChargeWaived():false, (cart.getZoneInfo()!=null)?cart.getZoneInfo().isCtActive():false, currentUser.getPrimaryKey(), EnumCompanyCode.fd.name());
		try {
			if ( "setDeliveryAddress".equalsIgnoreCase( action ) ) {
				try {
					// set unattended delivery flag to opt in if the user has not seen the unattended delivery notice
					if(EnumTransactionSource.IPHONE_WEBSITE.getCode().equals(app)) {
						com.freshdirect.customer.ErpAddressModel dlvAddress = currentUser.getShoppingCart().getDeliveryAddress();
						if (dlvAddress != null) {
					        if (EnumUnattendedDeliveryFlag.NOT_SEEN.equals(dlvAddress.getUnattendedDeliveryFlag())) {
					        	dlvAddress.setUnattendedDeliveryFlag(EnumUnattendedDeliveryFlag.OPT_IN);					        	
					        }
						}
					}
					
					LOGGER.debug("setDeliveryAddress[START] :");
					DeliveryAddressManipulator m = new DeliveryAddressManipulator(this.pageContext, result, action);
					m.performSetDeliveryAddress(noContactPhonePage);
				} catch (RedirectToPage e) {
					LOGGER.debug("setDeliveryAddress[RedirectToPage] :"+ e);
					this.redirectTo(e.getPage());
				}
				if ( result.isSuccess() ) {
					UserValidationUtil.validateContainsDlvPassOnly( request, result );
					UserValidationUtil.validateOrderMinimum( session, result );
					boolean makeGoodOrder = request.getParameter( "makeGoodOrder" ) != null || currentUser.getMasqueradeContext()!=null && currentUser.getMasqueradeContext().getMakeGoodFromOrderId()!=null;
					if ( !makeGoodOrder ) {
						// Set the selected gift carts for processing.
					    if (null != currentUser.getGiftCardList()) {
						    currentUser.getShoppingCart().setSelectedGiftCards(currentUser.getGiftCardList().getSelectedGiftcards());
					    }
					}
				}
				
				
				
				LOGGER.debug("setDeliveryAddress[END] :");
			}  else if ( "addAndSetDeliveryAddress".equalsIgnoreCase( action ) ) { //Added IPhone functionality APPDEV-1565
				DeliveryAddressManipulator m = new DeliveryAddressManipulator(this.pageContext, result, "addDeliveryAddressEx");
				m.performAddAndSetDeliveryAddress();
								
			//APPDEV-4177 : Code changes to trigger email while editing the delivery address : Start
				
			}  else if ( "editAndSetDeliveryAddress".equalsIgnoreCase( action ) ) {
				DeliveryAddressManipulator m = new DeliveryAddressManipulator(this.pageContext, result, getActionName());
				m.performEditAndSetDeliveryAddress();
				FDIdentity identity = getIdentity();
				FDCustomerInfo customerInfo = FDCustomerManager.getCustomerInfo(identity);
				// Made changed for address checking
				if(result.isSuccess()){
					ActionResult resultSuccess=new ActionResult();
					ErpAddressModel erpAddress = RegistrationControllerTag.checkDeliveryAddressInForm(request, resultSuccess , session);
					LOGGER.debug("CheckoutControllerTag :: Editing the delivery adderss and sending mail during checkout flow");
					EnumEStoreId estoreId = EnumEStoreId.valueOfContentId((ContentFactory.getInstance().getStoreKey().getId()));
					FDCustomerManager.sendEmail(FDEmailFactory.getInstance().createShippingAddressChangeEmail(customerInfo,erpAddress, estoreId));
				}	
			
			//APPDEV-4177 : Code changes to trigger email while editing the delivery address : End
				
			} else if ( "deleteDeliveryAddress".equalsIgnoreCase( action ) ) {
				DeliveryAddressManipulator m = new DeliveryAddressManipulator(this.pageContext, result, getActionName());
				m.performDeleteDeliveryAddress(event); //m.performDeleteDeliveryAddress() method was refactored to check for any existing reservations.

			} else if ( "reserveDeliveryTimeSlot".equalsIgnoreCase( action ) ) {
				TimeslotManipulator m = new TimeslotManipulator(this.pageContext, result, action);
				String outcome = m.performReserveDeliveryTimeSlot();
				
				if ( outcome.equals( Action.NONE ) ) {
					return false;
				}

			} else if ( "submitOrder".equalsIgnoreCase( action ) ) {
				LOGGER.debug( "AVAILABILITY IS: " + getCart().getAvailability() );
				if ( !getCart().isAvailabilityChecked() ) {

					this.setSuccessPage( backToViewCart );
					return true;
				}
				
                if (currentUser.getMasqueradeContext() != null) {
                    currentUser.getMasqueradeContext().setCsrWaivedDeliveryCharge("true".equals(request.getParameter("waive_delivery_fee")));
                    currentUser.getMasqueradeContext().setCsrWaivedDeliveryPremium("true".equals(request.getParameter("waive_delivery_premium_fee")));
                    currentUser.getMasqueradeContext().setSilentMode("on".equals(request.getParameter("silent_mode")));
                    cart.setCustomerServiceMessage(request.getParameter("csr_message"));
                    if ("true".equals(request.getParameter("waive_phone_fee"))) {
                        cart.setChargeWaived(EnumChargeType.PHONE, true, "CSR");
                    }
                }
				request.setAttribute("TAXATION_TYPE", pageContext.getSession().getAttribute("TAXATION_TYPE"));
				pageContext.getSession().removeAttribute("TAXATION_TYPE");
				
				String outcome = performSubmitOrder(result);

				MasqueradeContext masqueradeContext = currentUser.getMasqueradeContext();
				String masqueradeMakeGoodOrderId = masqueradeContext==null ? null : masqueradeContext.getMakeGoodFromOrderId();

				// add logic to process make good order complaint.
				if ( "true".equals( session.getAttribute( "makeGoodOrder" ) ) || masqueradeMakeGoodOrderId!=null ) {
					MakeGoodOrderUtility.processComplaint(masqueradeContext,
						new SessionParamGetter(session),
						new PostAction() {
							@Override
							public void handle(MasqueradeContext masqueradeContext) {
								session.removeAttribute( "makeGoodOrder" );
								session.removeAttribute( "referencedOrder" );
								session.removeAttribute(SessionName.MAKEGOOD_COMPLAINT);
								
								if (masqueradeContext!=null){
									masqueradeContext.clearMakeGoodContext();
								}
							}
						},
						outcome);
				}
				currentUser.setSuspendShowPendingOrderOverlay(false);
				currentUser.setShowPendingOrderOverlay(true);
				
				//prepare and store model for Coremetrics report
				//   EXCEPT for make-good sessions!
				if ( masqueradeMakeGoodOrderId == null ) {
					CmConversionEventTag.buildPendingOrderModifiedModels(session, cart);
					CmShop9Tag.buildPendingModels(session, cart);
				}

				
				saveCart = true;
				if ( outcome.equals( Action.NONE ) ) {
					return false; // SKIP_BODY
				}
				
				
				
			} else if ( "modifyStandingOrderTemplate".equalsIgnoreCase( action )) {
				// Modify standing order template according to changes made during checkout
				// and return to standing order detail page
				FDStandingOrder so = currentUser.getCurrentStandingOrder();
				EnumCheckoutMode mode = currentUser.getCheckoutMode();
				
				StandingOrderUtil.updateStandingOrder(session, mode, cart, so, null);
				StandingOrderUtil.endStandingOrderCheckoutPhase(session);
			} else if ("changeSONextDeliveryDate".equalsIgnoreCase( action )) {
				changeStandingOrderDeliveryDate(request, currentUser);
			} else if ( "gc_submitGiftCardOrder".equalsIgnoreCase( action ) || 
						"gc_submitGiftCardBulkOrder".equalsIgnoreCase( action ) || 
						"gc_onestep_submitGiftCardOrder".equalsIgnoreCase( action ) || 
						"rh_submitDonationOrder".equalsIgnoreCase( action ) || 
						"rh_onestep_submitDonationOrder".equalsIgnoreCase( action ) ) {

				performGiftCardAction(request, result, action);

				if ( result.isSuccess() ) {
					String outcome = performSubmitOrder(result);
					saveCart = true;

					if ( result.getError( "address_verification_failed" ) != null ) {
						redirectTo( this.gcAVSExceptionPage );
					} else if ( result.getError( "gc_payment_auth_failed" ) != null ) {
						redirectTo( this.gcAVSExceptionPage );
					}

					if ( outcome.equals( Action.NONE ) ) {
						return false; // SKIP_BODY
					}
				}
			} else if ( "setPaymentMethod".equalsIgnoreCase( action ) ) {
				PaymentMethodManipulator m = new PaymentMethodManipulator(pageContext, result, action);
				m.performSetPaymentMethod();
				
				if ( result.isSuccess() ) {
					UserValidationUtil.validateOrderMinimum( session, result );
					checkEBTRestrictedLineItems(cart, isNotCallCenter);
					if ( currentUser.isPromotionAddressMismatch() && isNotCallCenter ) {
						this.setSuccessPage( "/checkout/step_3_waive.jsp" );
					}
				}

			} else if ( "setNoPaymentMethod".equalsIgnoreCase( action ) ) {
				PaymentMethodManipulator m = new PaymentMethodManipulator(pageContext, result, action);
				m.performSetNoPaymentMethod();

				if ( result.isSuccess() ) {
					UserValidationUtil.validateOrderMinimum( session, result );
					if ( currentUser.isPromotionAddressMismatch() && isNotCallCenter ) {
						this.setSuccessPage( "/checkout/step_3_waive.jsp" );
					}
				}

			} else if ( "addAndSetPaymentMethod".equalsIgnoreCase( action ) ) {
				PaymentMethodManipulator m = new PaymentMethodManipulator(pageContext, result, action);
				m.performAddAndSetPaymentMethod();
				if ( result.isSuccess() ) {
					UserValidationUtil.validateOrderMinimum( session, result );
					checkEBTRestrictedLineItems(cart, isNotCallCenter);					
				}

			} else if ( "addPaymentMethod".equalsIgnoreCase( action ) || "gc_addPaymentMethod".equalsIgnoreCase( action ) ) {
				PaymentMethodManipulator m = new PaymentMethodManipulator(pageContext, result, action);
				m.performAddPaymentMethod();

			}else if ( "editPaymentMethod".equalsIgnoreCase( action )) {//Added IPhone functionality APPDEV-1565
				PaymentMethodManipulator m = new PaymentMethodManipulator(pageContext, result, action);
				m.performEditPaymentMethod();

			} else if ( "setPaymentAndSubmit".equalsIgnoreCase( action ) ) {
				if ( UserValidationUtil.validateOrderMinimum( session, result ) ) {
					String outcome = performSetPaymentAndSubmit( request, result );
					saveCart = true;
					if ( outcome.equals( Action.NONE ) ) {
						return false; // SKIP_BODY
					}
				}

			} else if ( "deletePaymentMethod".equalsIgnoreCase( action ) ) {
				PaymentMethodManipulator m = new PaymentMethodManipulator(pageContext, result, getActionName());
				m.performDeletePaymentMethod();

				if ( ( request.getRequestURI().toLowerCase().indexOf( "gift_card" ) > -1 ) ) {
					this.setSuccessPage( "/gift_card/purchase/purchase_giftcard.jsp" );
				} else if ( ( request.getRequestURI().toLowerCase().indexOf( "robin_hood" ) > -1 ) ) {
					this.setSuccessPage( "/robin_hood/rh_submit_order.jsp" );
				}

			} else if ( "setDeliveryAddressAndPayment".equalsIgnoreCase( action ) ) {
				try {
					DeliveryAddressManipulator m = new DeliveryAddressManipulator(this.pageContext, result, getActionName());
					m.performSetDeliveryAddress(noContactPhonePage);
				} catch (RedirectToPage e) {
					this.redirectTo(e.getPage());
				}

				if ( result.isSuccess() ) {
					PaymentMethodManipulator m = new PaymentMethodManipulator(this.pageContext, result, getActionName());
					m.setPaymentMethod();

					if ( result.isSuccess() ) {
						m.applyCustomerCredits();
					}
				}

				if ( result.isSuccess() ) {
					UserValidationUtil.validateOrderMinimum( session, result );
				}

			}
			if ( "setOrderMobileNumber".equalsIgnoreCase( action ) ) {
				try {
					
					LOGGER.debug("setOrderMobileNumber[START] :");
					DeliveryAddressManipulator m = new DeliveryAddressManipulator(this.pageContext, result, action);
					m.performSetOrderMobileNumber();
				} catch (RedirectToPage e) {
					LOGGER.debug("setOrderMobileNumber[RedirectToPage] :"+ e);
					this.redirectTo(e.getPage());
				}
				/*if ( result.isSuccess() ) {
					UserValidationUtil.validateOrderMinimum( session, result );
				}*/
				
				LOGGER.debug("setOrderMobileNumber[END] :");
			} 

			// list of the ultimate checkout actions
			final boolean terminalAction = action != null && (
				action.toLowerCase().indexOf( "submit" ) > -1		// submit actions
				||
				"modifyStandingOrderTemplate".equalsIgnoreCase(action) // SO-MSOT (change SO without creating an order)
			);

			if ( action != null && null !=cart.getDeliveryAddress() && "setDeliveryAddress".equalsIgnoreCase( action ) && !terminalAction && EnumServiceType.PICKUP.equals(cart.getDeliveryAddress().getServiceType()) && cart.containsWineAndSpirit()){
				this.setSuccessPage( this.blockedAddressPage+"?isAlcoholForPickUp=true" );
			} else{
				// if there is alcohol in the cart and the age verification has not been set then send to age verification page
				if ( action != null && !terminalAction && isAgeVerificationNeeded( app, request ) ) {
					this.setSuccessPage( this.ageVerificationPage );
				}
			}
		} catch(ReservationException ex) {	
			LOGGER.error( "Error performing action " + action, ex );
			result.addError(new ActionError("invalid_reservation", SystemMessageList.MSG_CHECKOUT_EXPIRED_RESERVATION));
		} catch (FDResourceException ex){
			LOGGER.error( "Error performing action " + action, ex );
			result.addError(new ActionError("checkout_error", ex.getMessage()));
		} catch ( Exception ex ) {
			ex.printStackTrace();
			LOGGER.error( "Error performing action " + action, ex );
			result.addError( new ActionError( "technical_difficulty", SystemMessageList.MSG_TECHNICAL_ERROR ) );
		} finally {
			if ( saveCart ) {
				//
				// checkpoint: force a save cart
				//
				currentUser.saveCart( true );
			}

			/* reset user to see mergePending overlay again */
			if ( "true".equals( session.getAttribute( "makeGoodOrder" ) )  || currentUser.getMasqueradeContext()!=null && currentUser.getMasqueradeContext().getMakeGoodFromOrderId()!=null ) {
				currentUser.setShowPendingOrderOverlay(true);
			}
		}
		if(this.getSuccessPage()!=null && this.getSuccessPage().equals("/checkout/step_4_submit.jsp") && FDStoreProperties.getAvalaraTaxEnabled()){
			AvalaraContext avalaraContext = new AvalaraContext(cart);
			avalaraContext.setCommit(false);
			Double taxValue = cart.getAvalaraTaxValue(avalaraContext);
			avalaraContext.setReturnTaxValue(taxValue);
			if(avalaraContext.isAvalaraTaxed()){
			session.setAttribute("TAXATION_TYPE", "AVAL");
			}
		}
		return true;
	}
	//Method to check EBT Restricted item, which redirected to the restricted item screen.
	protected void checkEBTRestrictedLineItems(FDCartModel cart, boolean isNotCallCenter) {
		
		ErpPaymentMethodI paymentMethod = cart.getPaymentMethod();
		if(null != paymentMethod && EnumPaymentMethodType.EBT.equals(paymentMethod.getPaymentMethodType())){
			boolean isEBTBlocked = false;
			for (FDCartLineI cartLine : cart.getOrderLines()) {
				isEBTBlocked =cartLine.getProductRef().lookupProductModel().isExcludedForEBTPayment();
				if(isEBTBlocked){
					break;
				}
			}
			if(isEBTBlocked) {
				
				if(isNotCallCenter){
					this.setSuccessPage( ebtUnavailableItemsPage+ "?successPage="+getSuccessPage());
				}else{
					this.setSuccessPage( ebtCRMUnavailableItemsPage +"?successPage="+getSuccessPage());
				}
			}
		}
	}
	protected void changeStandingOrderDeliveryDate(HttpServletRequest request,
			final FDSessionUser currentUser) throws FDResourceException {
		final FDStandingOrder so = currentUser.getCurrentStandingOrder();
		final EnumCheckoutMode mode = currentUser.getCheckoutMode();

		if (so != null && EnumCheckoutMode.MODIFY_SO_TMPL.equals(mode)) {
			final String tsId = request.getParameter("deliveryTimeslotId");
			final FDTimeslot ts = FDDeliveryManager.getInstance().getTimeslotsById(tsId, null, false);
			if (ts != null) {

				
				
				int offsetStart = 1;
				try {
					FDOrderHistory history = FDCustomerManager.getOrderHistoryInfo(currentUser.getIdentity());
					offsetStart = StandingOrderHelper.getFirstAvailableWeekOffset( history.getStandingOrderInstances(so.getId()) );
				} catch (FDResourceException exc ) {
					LOGGER.error("Failed to retrieve scheduled orders for standing order " + so.getId(), exc);
				}
				
				
				
				
				// let's extract date and time interval
				StandingOrderHelper.DeliveryTime dt = new StandingOrderHelper.DeliveryTime(ts);
				
				if ( request.getParameter("soDeliveryWeekOffset") != null) {
					int offset = Integer.parseInt(request.getParameter("soDeliveryWeekOffset"));
					// both offset and offsetStart starts from next week so one must be decreased
					dt.setWeekOffset( offsetStart + offset  -1 );
				}

				dt.update(so);
				
			} else {
				LOGGER.error("No such timeslot with ID " + tsId);
			}
		} else {
			if (so == null)
				LOGGER.error("Missing standing order object!");
			else
				LOGGER.error("Invalid checkout mode " + mode);
		}
	}




	@Override
	protected boolean performGetAction( HttpServletRequest request, ActionResult actionResult ) throws JspException {
		// if there is alcohol in the cart and the age verification has not been
		// set then send to age verification page
		try {
			String app = (String)pageContext.getSession().getAttribute( SessionName.APPLICATION );
			if ( isAgeVerificationNeeded( app, request ) ) {
				redirectTo( ageVerificationPage );
				return false;
			}
		} catch ( Exception ex ) {
			LOGGER.error( "Error checking for age verification condition", ex );
			actionResult.addError( new ActionError( "technical_difficulty", SystemMessageList.MSG_TECHNICAL_ERROR ) );
		}
		return true;

	}
	
	
	
	


	
	/**
	 * Performs GIFT CARD specific action
	 * 
	 * @param request
	 * @param result
	 * @param action
	 * @throws FDResourceException
	 * @throws JspException
	 */
	private void performGiftCardAction(HttpServletRequest request,
			ActionResult result, String action) throws FDResourceException,
			JspException {
		// allane: added this b/c we skip confirm order page in gc checkout. payment needs to be set at the same
		// time as checkout.
		FDSessionUser currentUser = (FDSessionUser) getUser();
		
		if ( "gc_onestep_submitGiftCardOrder".equalsIgnoreCase( action ) ) {

			if ( result.isSuccess() ) {
				
				PaymentMethodManipulator m = new PaymentMethodManipulator(pageContext, result, action);
				m.performAddAndSetPaymentMethod();

				if ( result.getError( "payment_method_fraud" ) != null ) {
					currentUser.setGCSignupError( true );
					redirectTo( this.ccdProblemPage );
				} else if ( result.getErrors() != null && result.getErrors().size() > 0 ) {
					currentUser.setGCSignupError( true );
					List<String> errList = new ArrayList<String>();
					for ( ActionError tmpResult : result.getErrors() ) {
						errList.add( tmpResult.getDescription() );
					}
					currentUser.setOneTimeGCPaymentError( errList );
					redirectTo( this.ccdProblemPage );
				}

			}
		} else if ( "rh_onestep_submitDonationOrder".equalsIgnoreCase( action ) ) {
			if ( result.isSuccess() ) {
				String optinInd = request.getParameter( "optinInd" );
				if ( optinInd == null || optinInd.equals( "" ) ) {
					result.addError( new ActionError( "Opt_in_required", SystemMessageList.MSG_RH_OPTIN_REQUIRED ) );
				}
				PaymentMethodManipulator m = new PaymentMethodManipulator(pageContext, result, action);
				m.performAddAndSetPaymentMethod();

				if ( result.getError( "payment_method_fraud" ) != null ) {
					currentUser.setGCSignupError( true );
					redirectTo( this.ccdProblemPage );
				}
			}
		} else {
			PaymentMethodManipulator m = new PaymentMethodManipulator(pageContext, result, action);
			m.performSetPaymentMethod();
		}
	}




	protected String performSetPaymentAndSubmit( HttpServletRequest request, ActionResult result ) throws Exception {
		PaymentMethodManipulator m = new PaymentMethodManipulator(pageContext, result, getActionName());
		m.setPaymentMethod();
		
		if ( result.isSuccess() ) {
			return performSubmitOrder(result);
		}
		return Action.ERROR;
	}



	protected void configureAction( Action action, ActionResult result ) {
		configureAction(action, result, pageContext.getSession(), (HttpServletRequest) pageContext.getRequest(), (HttpServletResponse) pageContext.getResponse());
		}

	protected String performSubmitOrder(ActionResult result) throws Exception {
		return performSubmitOrder(getUser(), getActionName(), result, pageContext.getSession(), (HttpServletRequest) pageContext.getRequest(), (HttpServletResponse) pageContext.getResponse(), authCutoffPage, ccdProblemPage, ccdAddCardPage, gcFraudPage);
		}

	
	public static String performSubmitOrder(FDUserI user, String actionName, ActionResult result, HttpSession session, HttpServletRequest request, HttpServletResponse response, String authCutoffPage, String ccdProblemPage, String ccdAddCardPage, String gcFraudPage) throws Exception {
		SubmitOrderAction soa = new SubmitOrderAction();
		configureAction( soa, result, session, request, response);
		soa.setAuthCutoffPage( authCutoffPage );
		soa.setCcdProblemPage( ccdProblemPage );
		soa.setCcdAddCardPage( ccdAddCardPage );
		soa.setGCFraudPage( gcFraudPage );
		soa.setStandingOrder( user.getCurrentStandingOrder() );

		if ( actionName.equals( "gc_submitGiftCardOrder" ) || ( actionName.equals( "gc_submitGiftCardBulkOrder" ) ) ) {
			if ( actionName.equals( "gc_submitGiftCardBulkOrder" ) ) {
				return soa.gcExecute( false, true );
			} else {
				return soa.gcExecute( false, false );
			}
		} else if ( actionName.equals( "gc_onestep_submitGiftCardOrder" ) ) {
			return soa.gcExecute( true, false );
		} else if ( actionName.equals( "rh_submitDonationOrder" ) || actionName.equals( "rh_onestep_submitDonationOrder" ) ) {
			return soa.donationOrderExecute();
		}
		return soa.execute();
	}

	public static void configureAction(Action action, ActionResult result, HttpSession session, HttpServletRequest request, HttpServletResponse response) {
		if (action instanceof HttpContextAware) {
			HttpContext ctx = new HttpContext(session, request, response);

			((HttpContextAware) action).setHttpContext(ctx);
		}
		if (action instanceof ResultAware) {
			((ResultAware) action).setResult(result);
		}
	}



	private boolean isAgeVerificationNeeded( String app, HttpServletRequest request ) throws FDResourceException {
		FDCartModel cart = getCart();
		return ( cart.containsAlcohol() && !"CALLCENTER".equalsIgnoreCase( app ) && !cart.isAgeVerified() && request.getRequestURI().indexOf( "/step_1" ) == -1 );
	}



	private FDCartModel getCart() {
		if ( this.getActionName().indexOf( "gc_" ) != -1 ) {
			return this.getUser().getGiftCart();
		} else if ( this.getActionName().indexOf( "rh_" ) != -1 ) {
			return this.getUser().getDonationCart();
		}
		return this.getUser().getShoppingCart();
	}

	private FDUserI getUser() {
		HttpSession session = pageContext.getSession();
		FDUserI user = (FDUserI)session.getAttribute( SessionName.USER );
		return user;
	}
	
	//APPDEV-4177 : Code changes to trigger email while editing the delivery address : Start
	
	protected FDIdentity getIdentity() {
		HttpSession session = pageContext.getSession();
		FDSessionUser user = (FDSessionUser) session.getAttribute( SessionName.USER);
		return (user == null) ? null : user.getIdentity();
	}
	

	public static class TagEI extends AbstractControllerTag.TagEI {
		// default impl
	}

}
