package com.freshdirect.webapp.taglib.fdstore;

import java.text.MessageFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;

import org.apache.log4j.Category;

import com.freshdirect.analytics.TimeslotEventModel;
import com.freshdirect.crm.CrmAgentModel;
import com.freshdirect.crm.CrmAgentRole;
import com.freshdirect.customer.EnumTransactionSource;
import com.freshdirect.customer.EnumUnattendedDeliveryFlag;
import com.freshdirect.customer.ErpComplaintException;
import com.freshdirect.customer.ErpComplaintLineModel;
import com.freshdirect.customer.ErpComplaintModel;
import com.freshdirect.customer.ErpPaymentMethodI;
import com.freshdirect.fdstore.EnumCheckoutMode;
import com.freshdirect.fdstore.FDDeliveryManager;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDTimeslot;
import com.freshdirect.fdstore.Util;
import com.freshdirect.fdstore.coremetrics.builder.ConversionEventTagModelBuilder;
import com.freshdirect.fdstore.customer.FDCartLineI;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDOrderHistory;
import com.freshdirect.fdstore.customer.FDOrderInfoI;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.standingorders.FDStandingOrder;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionError;
import com.freshdirect.framework.webapp.ActionResult;
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
import com.freshdirect.webapp.crm.security.CrmSecurityManager;
import com.freshdirect.webapp.taglib.AbstractControllerTag;
import com.freshdirect.webapp.taglib.coremetrics.CmConversionEventTag;
import com.freshdirect.webapp.taglib.coremetrics.CmShop9Tag;
import com.freshdirect.webapp.taglib.crm.CrmSession;
import com.freshdirect.webapp.util.StandingOrderHelper;
import com.freshdirect.webapp.util.StandingOrderUtil;



public class CheckoutControllerTag extends AbstractControllerTag {

	private static final long	serialVersionUID	= 8993233587790486175L;

	private static Category		LOGGER				= LoggerFactory.getInstance( CheckoutControllerTag.class );

	private String				ccdProblemPage		= "/checkout/step_3_choose.jsp";
	private String				authCutoffPage		= "/checkout/account_problem.jsp";
	private final String		ageVerificationPage	= "/checkout/step_2_verify_age.jsp";
	private final String		backToViewCart		= "/checkout/view_cart.jsp";
	private final String		ccdAddCardPage		= "/checkout/step_3_card_add.jsp";
	private String				gcFraudPage			= "/gift_card/purchase/purchase_giftcard.jsp";
	private final String		gcAVSExceptionPage	= "/gift_card/purchase/purchase_giftcard.jsp";
	private String 				noContactPhonePage	= "/checkout/step_1_edit.jsp";

	private final String ebtUnavailableItemsPage	= "/checkout/step_3_unavail.jsp";
	private final String ebtCRMUnavailableItemsPage	= "/checkout/checkout_EBT_unavail.jsp";
	
	
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
		HttpSession session = request.getSession();
		boolean saveCart = false;

		final String app = (String)pageContext.getSession().getAttribute( SessionName.APPLICATION );
		final boolean isNotCallCenter = !"CALLCENTER".equals( app );
		final FDSessionUser currentUser = (FDSessionUser) getUser();
		FDCartModel cart = currentUser.getShoppingCart();
		String zoneId = null;
		if(cart!=null && cart.getZoneInfo()!=null)
			zoneId = cart.getZoneInfo().getZoneId();
		
		TimeslotEventModel event = new TimeslotEventModel((currentUser.getApplication()!=null)?currentUser.getApplication().getCode():"",
				(cart!=null)?cart.isDlvPassApplied():false, (cart!=null)?cart.getDeliverySurcharge():0.00,
						(cart!=null)?cart.isDeliveryChargeWaived():false, Util.isZoneCtActive(zoneId), currentUser.getPrimaryKey());
		try {
			if ( "setDeliveryAddress".equalsIgnoreCase( action ) ) {
				try {
					LOGGER.debug("setDeliveryAddress[START] :");
					DeliveryAddressManipulator m = new DeliveryAddressManipulator(this.pageContext, result, action);
					m.performSetDeliveryAddress(noContactPhonePage);
				} catch (RedirectToPage e) {
					LOGGER.debug("setDeliveryAddress[RedirectToPage] :"+ e);
					this.redirectTo(e.getPage());
				}
				if ( result.isSuccess() ) {
					UserValidationUtil.validateContainsDlvPassOnly( request, result );
					UserValidationUtil.validateOrderMinimum( request, result );
					boolean makeGoodOrder = request.getParameter( "makeGoodOrder" ) != null;
					if ( !makeGoodOrder ) {
						// Set the selected gift carts for processing.
						currentUser.getShoppingCart().setSelectedGiftCards( currentUser.getGiftCardList().getSelectedGiftcards() );
					}
					
					if(EnumTransactionSource.IPHONE_WEBSITE.getCode().equals(app)) {
						com.freshdirect.customer.ErpAddressModel dlvAddress = currentUser.getShoppingCart().getDeliveryAddress();
						if (dlvAddress != null) {
					        if (EnumUnattendedDeliveryFlag.NOT_SEEN.equals(dlvAddress.getUnattendedDeliveryFlag())) {
					        	dlvAddress.setUnattendedDeliveryFlag(EnumUnattendedDeliveryFlag.OPT_IN);					        	
					        }
						}
					}
					
				}
				
				
				
				LOGGER.debug("setDeliveryAddress[END] :");
			}  else if ( "addAndSetDeliveryAddress".equalsIgnoreCase( action ) ) { //Added IPhone functionality APPDEV-1565
				DeliveryAddressManipulator m = new DeliveryAddressManipulator(this.pageContext, result, "addDeliveryAddressEx");
				m.performAddAndSetDeliveryAddress();
			}  else if ( "editAndSetDeliveryAddress".equalsIgnoreCase( action ) ) {
				DeliveryAddressManipulator m = new DeliveryAddressManipulator(this.pageContext, result, getActionName());
				m.performEditAndSetDeliveryAddress();
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
				String outcome = performSubmitOrder( request, result );
				// add logic to process make good order complaint.
				if ( "true".equals( session.getAttribute( "makeGoodOrder" ) ) ) {
					ErpComplaintModel complaintModel = (ErpComplaintModel)session.getAttribute( SessionName.MAKEGOOD_COMPLAINT );
					String makeGoodOrderId = (String)session.getAttribute( SessionName.RECENT_ORDER_NUMBER );
					complaintModel.setMakegood_sale_id( makeGoodOrderId );
					try {
						addMakeGoodComplaint( result, complaintModel );
					} catch ( Exception e ) {
						LOGGER.error( "Add 0 credit complaint failed for make good order:" + makeGoodOrderId, e );
						// create case here
					}
					session.removeAttribute( "makeGoodOrder" );
					session.removeAttribute( "referencedOrder" );
				}
				currentUser.setSuspendShowPendingOrderOverlay(false);
				currentUser.setShowPendingOrderOverlay(true);
				
				//prepare and store model for Coremetrics report
				CmConversionEventTag.buildPendingOrderModifiedModels(session, cart);
				CmShop9Tag.buildPendingModels(session, cart);

				
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
					String outcome = performSubmitOrder( request, result );
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
					UserValidationUtil.validateOrderMinimum( request, result );
					checkEBTRestrictedLineItems(cart, isNotCallCenter);
					if ( currentUser.isPromotionAddressMismatch() && isNotCallCenter ) {
						this.setSuccessPage( "/checkout/step_3_waive.jsp" );
					}
				}

			} else if ( "setNoPaymentMethod".equalsIgnoreCase( action ) ) {
				PaymentMethodManipulator m = new PaymentMethodManipulator(pageContext, result, action);
				m.performSetNoPaymentMethod();

				if ( result.isSuccess() ) {
					UserValidationUtil.validateOrderMinimum( request, result );
					if ( currentUser.isPromotionAddressMismatch() && isNotCallCenter ) {
						this.setSuccessPage( "/checkout/step_3_waive.jsp" );
					}
				}

			} else if ( "addAndSetPaymentMethod".equalsIgnoreCase( action ) ) {
				PaymentMethodManipulator m = new PaymentMethodManipulator(pageContext, result, action);
				m.performAddAndSetPaymentMethod();
				if ( result.isSuccess() ) {
					UserValidationUtil.validateOrderMinimum( request, result );
					checkEBTRestrictedLineItems(cart, isNotCallCenter);
				}

			} else if ( "addPaymentMethod".equalsIgnoreCase( action ) || "gc_addPaymentMethod".equalsIgnoreCase( action ) ) {
				PaymentMethodManipulator m = new PaymentMethodManipulator(pageContext, result, action);
				m.performAddPaymentMethod();

			}else if ( "editPaymentMethod".equalsIgnoreCase( action )) {//Added IPhone functionality APPDEV-1565
				PaymentMethodManipulator m = new PaymentMethodManipulator(pageContext, result, action);
				m.performEditPaymentMethod();

			} else if ( "setPaymentAndSubmit".equalsIgnoreCase( action ) ) {
				if ( UserValidationUtil.validateOrderMinimum( request, result ) ) {
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
					UserValidationUtil.validateOrderMinimum( request, result );
				}

			}

			// list of the ultimate checkout actions
			final boolean terminalAction = action != null && (
				action.toLowerCase().indexOf( "submit" ) > -1		// submit actions
				||
				"modifyStandingOrderTemplate".equalsIgnoreCase(action) // SO-MSOT (change SO without creating an order)
			);

			// if there is alcohol in the cart and the age verification has not been set then send to age verification page
			if ( action != null && !terminalAction && isAgeVerificationNeeded( app, request ) ) {
				this.setSuccessPage( this.ageVerificationPage );
			}
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
			if ( "true".equals( session.getAttribute( "makeGoodOrder" ) ) ) {
				currentUser.setShowPendingOrderOverlay(true);
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
			final FDTimeslot ts = FDDeliveryManager.getInstance().getTimeslotsById(tsId, false);
			if (ts != null) {

				
				
				int offsetStart = 1;
				try {
					FDOrderHistory history = (FDOrderHistory) FDCustomerManager.getOrderHistoryInfo(currentUser.getIdentity());
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
			return performSubmitOrder( request, result );
		}
		return Action.ERROR;
	}




	protected void configureAction( Action action, ActionResult result ) {
		if ( action instanceof HttpContextAware ) {
			HttpContext ctx = new HttpContext( this.pageContext.getSession(), (HttpServletRequest)this.pageContext.getRequest(), (HttpServletResponse)this.pageContext.getResponse() );

			( (HttpContextAware)action ).setHttpContext( ctx );
		}

		if ( action instanceof ResultAware ) {
			( (ResultAware)action ).setResult( result );
		}
	}

	protected String performSubmitOrder( HttpServletRequest request, ActionResult result ) throws Exception {

		SubmitOrderAction soa = new SubmitOrderAction();
		this.configureAction( soa, result );
		soa.setAuthCutoffPage( authCutoffPage );
		soa.setCcdProblemPage( ccdProblemPage );
		soa.setCcdAddCardPage( ccdAddCardPage );
		soa.setGCFraudPage( gcFraudPage );
		soa.setStandingOrder( this.getUser().getCurrentStandingOrder() );

		if ( this.getActionName().equals( "gc_submitGiftCardOrder" ) || ( this.getActionName().equals( "gc_submitGiftCardBulkOrder" ) ) ) {
			if ( this.getActionName().equals( "gc_submitGiftCardBulkOrder" ) ) {
				return soa.gcExecute( false, true );
			} else {
				return soa.gcExecute( false, false );
			}
		} else if ( this.getActionName().equals( "gc_onestep_submitGiftCardOrder" ) ) {
			return soa.gcExecute( true, false );
		} else if ( this.getActionName().equals( "rh_submitDonationOrder" ) || this.getActionName().equals( "rh_onestep_submitDonationOrder" ) ) {
			return soa.donationOrderExecute();
		}
		return soa.execute();
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

	private void addMakeGoodComplaint( ActionResult result, ErpComplaintModel complaintModel ) throws FDResourceException, ErpComplaintException {

		final NumberFormat currencyFormatter = java.text.NumberFormat.getCurrencyInstance( Locale.US );

		LOGGER.debug( "Creating credits for the following departments:" );
		LOGGER.debug( "  Method\t\tDepartment\t\t\tAmount\t\t\tReason\t\t\tCarton Number" );
		LOGGER.debug( "  ------\t\t----------\t\t\t------\t\t\t------\t\t\t------" );
		
		for ( ErpComplaintLineModel line : complaintModel.getComplaintLines() ) {
			LOGGER.debug( line.getMethod().getStatusCode() + "\t\t" + line.getDepartmentCode() + "\t\t\t" + currencyFormatter.format( line.getAmount() ) + "\t\t\t" + line.getReason().getReason() + "\t\t\t" + line.getCartonNumber() );			
		}
		
		LOGGER.debug( "  Credit Notes: " + complaintModel.getDescription() );
		HttpSession session = pageContext.getSession();
		FDIdentity identity = getUser().getIdentity();
		String orderId = (String)session.getAttribute( "referencedOrder" );
		CrmAgentModel agentModel =CrmSession.getCurrentAgent(session);
		CrmAgentRole agentRole = agentModel.getRole();
		boolean autoApproveAuthorized = CrmSecurityManager.isAutoApproveAuthorized(agentRole.getLdapRoleName());
		Double limit =CrmSecurityManager.getAutoApprovalLimit(agentRole.getLdapRoleName());
		FDCustomerManager.addComplaint( complaintModel, orderId, identity,autoApproveAuthorized,limit );
	}

	public static class TagEI extends AbstractControllerTag.TagEI {
		// default impl
	}

}
