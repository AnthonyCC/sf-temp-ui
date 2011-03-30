package com.freshdirect.webapp.taglib.fdstore;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;

import org.apache.log4j.Category;

import com.freshdirect.crm.CrmAgentModel;
import com.freshdirect.crm.CrmAgentRole;
import com.freshdirect.customer.ErpComplaintException;
import com.freshdirect.customer.ErpComplaintLineModel;
import com.freshdirect.customer.ErpComplaintModel;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDActionInfo;
import com.freshdirect.fdstore.customer.FDAuthenticationException;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.customer.QuickCart;
import com.freshdirect.fdstore.standingorders.FDStandingOrder;
import com.freshdirect.fdstore.standingorders.FDStandingOrdersManager;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionError;
import com.freshdirect.framework.webapp.ActionResult;
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
import com.freshdirect.webapp.taglib.crm.CrmSession;
import com.freshdirect.webapp.util.FDURLUtil;
import com.freshdirect.webapp.util.QuickCartCache;
import com.freshdirect.webapp.util.ShoppingCartUtil;



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
		
		try {
			if ( "setDeliveryAddress".equalsIgnoreCase( action ) ) {
				try {
					DeliveryAddressManipulator m = new DeliveryAddressManipulator(this.pageContext, result, action);
					m.performSetDeliveryAddress(noContactPhonePage);
				} catch (RedirectToPage e) {
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
				}
			} else if ( "editAndSetDeliveryAddress".equalsIgnoreCase( action ) ) {
				DeliveryAddressManipulator m = new DeliveryAddressManipulator(this.pageContext, result, getActionName());
				m.performEditAndSetDeliveryAddress();
			} else if ( "deleteDeliveryAddress".equalsIgnoreCase( action ) ) {
				DeliveryAddressManipulator m = new DeliveryAddressManipulator(this.pageContext, result, getActionName());
				m.performDeleteDeliveryAddress();

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

				saveCart = true;
				if ( outcome.equals( Action.NONE ) ) {
					return false; // SKIP_BODY
				}
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
				}

			} else if ( "addPaymentMethod".equalsIgnoreCase( action ) || "gc_addPaymentMethod".equalsIgnoreCase( action ) ) {
				PaymentMethodManipulator m = new PaymentMethodManipulator(pageContext, result, action);
				m.performAddPaymentMethod();

			}else if ( "editPaymentMethod".equalsIgnoreCase( action )) {
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

			// if there is alcohol in the cart and the age verification has not been set then send to age verification page
			if ( action != null && action.toLowerCase().indexOf( "submit" ) == -1 && isAgeVerificationNeeded( app, request ) ) {
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
		}

		return true;
	}




	private boolean performUpdateStandingOrder(HttpSession session, ActionResult result) {	
		return result.isSuccess();
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
		LOGGER.debug( "  Method\t\tDepartment\t\t\tAmount\t\t\tReason" );
		LOGGER.debug( "  ------\t\t----------\t\t\t------\t\t\t------" );
		
		for ( ErpComplaintLineModel line : complaintModel.getComplaintLines() ) {
			LOGGER.debug( line.getMethod().getStatusCode() + "\t\t" + line.getDepartmentCode() + "\t\t\t" + currencyFormatter.format( line.getAmount() ) + "\t\t\t" + line.getReason().getReason() );			
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
