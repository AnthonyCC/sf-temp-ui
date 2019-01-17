package com.freshdirect.webapp.taglib.crm;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;

import org.apache.log4j.Category;

import com.freshdirect.common.customer.EnumCardType;
import com.freshdirect.customer.EnumAccountActivityType;
import com.freshdirect.customer.EnumNotificationType;
import com.freshdirect.customer.ErpAddressModel;
import com.freshdirect.customer.ErpCustomerModel;
import com.freshdirect.customer.ErpFraudException;
import com.freshdirect.customer.ErpPaymentMethodI;
import com.freshdirect.customer.ErpPaymentMethodModel;
import com.freshdirect.customer.ErpTransactionException;
import com.freshdirect.deliverypass.DeliveryPassException;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.customer.FDActionInfo;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.fdstore.customer.FDCustomerFactory;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDPaymentInadequateException;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.customer.adapter.CustomerRatingAdaptor;
import com.freshdirect.fdstore.deliverypass.DeliveryPassUtil;
import com.freshdirect.fdstore.services.tax.AvalaraContext;
import com.freshdirect.framework.util.NVL;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionError;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.giftcard.InvalidCardException;
import com.freshdirect.payment.EnumBankAccountType;
import com.freshdirect.payment.EnumPaymentMethodType;
import com.freshdirect.payment.PaymentManager;
import com.freshdirect.webapp.taglib.AbstractControllerTag;
import com.freshdirect.webapp.taglib.fdstore.AccountActivityUtil;
import com.freshdirect.webapp.taglib.fdstore.EnumUserInfoName;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;
import com.freshdirect.webapp.taglib.fdstore.PaymentMethodName;
import com.freshdirect.webapp.taglib.fdstore.PaymentMethodUtil;
import com.freshdirect.webapp.taglib.fdstore.SessionName;
import com.freshdirect.webapp.taglib.fdstore.SystemMessageList;

public class CrmSubscriptionControllerTag extends AbstractControllerTag {
	
	private static Category LOGGER 	= LoggerFactory.getInstance( CrmSubscriptionControllerTag.class );
	
	private final String PLACE_SUBSCRIPTION_ORDER	= "place_subscription_order";
	
	private final String REVIEW_SUBSCRIPTION_ORDER	= "review_subscription_order";
	
	private ErpPaymentMethodI paymentMethod;
	

	public void setPaymentMethod(ErpPaymentMethodI paymentMethod) {
		this.paymentMethod = paymentMethod;
	}
	
	
	protected boolean performAction(HttpServletRequest request, ActionResult actionResult) throws JspException {
		
    	boolean actionPerformed = false;
		try {
			    String actionName= getActionName();
				if (PLACE_SUBSCRIPTION_ORDER.equalsIgnoreCase(actionName)) {
					String orderID=this.placeSubscriptionOrder(request, actionResult);
					actionPerformed = true;
					if(!"".equals(orderID)) {
						
						StringBuffer successPage = new StringBuffer();
						successPage.append(getSuccessPage());
						successPage.append(orderID);
						setSuccessPage(successPage.toString());
					}
				}
				else if (REVIEW_SUBSCRIPTION_ORDER.equalsIgnoreCase(actionName)) {
					this.reviewSubscriptionOrder(request, actionResult);
					actionPerformed = true;
				}
				else if ("deletePaymentMethod".equalsIgnoreCase(actionName)) {
					deletePaymentMethod(request, actionResult);
					actionPerformed = true;
					
				}
				else if ("addPaymentMethod".equalsIgnoreCase(actionName)) {
					this.addPaymentMethod(request, actionResult);
					actionPerformed = true;
				}
				else if ("editPaymentMethod".equalsIgnoreCase(actionName)) {
					this.editPaymentMethod(request, actionResult);
					actionPerformed = true;
				}

				
			
		} catch (FDResourceException e) {
			throw new JspException(e);
		}
		/*if (actionPerformed && actionResult.isSuccess() && successPage!=null) {
			LOGGER.debug("Success, redirecting to: "+successPage);
			HttpServletResponse response = (HttpServletResponse) pageContext.getResponse();
			try {
				response.sendRedirect(response.encodeRedirectURL(successPage));
				JspWriter writer = pageContext.getOut();
				writer.close();
			} catch (IOException ioe) {
				throw new JspException(ioe.getMessage());
			}
		}*/

		return actionPerformed;

	}
	
	private void reviewSubscriptionOrder(HttpServletRequest request, ActionResult actionResult) throws JspException {
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
				actionResult.addError(
		    	        paymentMethod.getExpirationDate() == null || checkDate.after(paymentMethod.getExpirationDate()),
		    	        "expiration", SystemMessageList.MSG_CARD_EXPIRATION_DATE
		    	        );
				if ( actionResult.isSuccess() ) {
					FDSessionUser user = (FDSessionUser) session.getAttribute(SessionName.USER);
		        	CustomerRatingAdaptor cra = new CustomerRatingAdaptor(user.getFDCustomer().getProfile(),user.isCorporateUser(),user.getAdjustedValidOrderCount());

		        	ErpAddressModel address= FDCustomerManager.getLastOrderAddress(user.getIdentity());
					String arSKU = FDCustomerManager.getAutoRenewSKU(currentUser.getIdentity().getErpCustomerPK());
					if (arSKU != null){
						//this.orderId = DeliveryPassRenewalCron.placeOrder(AccountActivityUtil.getActionInfo(session),cra, arSKU, paymentMethod, address);
						FDCartModel cart=DeliveryPassUtil.getCart(arSKU, paymentMethod, address, user.getIdentity().getErpCustomerPK(),user.getUserContext());
						session.setAttribute("SUBSCRIPTION_CART", cart);
						
					}else{
						throw new DeliveryPassException("Customer has no valid auto_renew DP. ",currentUser.getIdentity().getErpCustomerPK());
					
					}
				}
			} catch (FDResourceException ex) {
				LOGGER.error("FDResourceException while attempting to perform reauthorization.", ex);
				actionResult.addError(new ActionError("technical_difficulty", "We're currently experiencing technical difficulties. Please try again later."));
			} catch(DeliveryPassException ex) {
				LOGGER.error("Error performing a Delivery pass operation. ", ex);
				//There was delivery pass validation failure.
				actionResult.addError(new ActionError("delivery_pass_error","Error performing a Delivery pass operation. "+ex.getMessage()));
			}
		} else {
			LOGGER.warn("No payment id selected by user");
			actionResult.addError(new ActionError("no_payment_selected", "Please select a payment option below."));
		}
		
	}

	protected String placeSubscriptionOrder(HttpServletRequest request, ActionResult results) throws JspException {
		HttpSession session = request.getSession();

		FDSessionUser currentUser = (FDSessionUser) session.getAttribute(SessionName.USER);
		
		if (currentUser.getLevel() < FDUserI.SIGNED_IN) {
			throw new JspException("No customer was found for the requested action.");
		}
		//
		// Select new payment and resubmit order
		//
		
		
			try {
				CustomerRatingAdaptor cra = new CustomerRatingAdaptor(currentUser.getFDCustomer().getProfile(),currentUser.isCorporateUser(),currentUser.getAdjustedValidOrderCount());
				FDCartModel cart=(FDCartModel)session.getAttribute("SUBSCRIPTION_CART");
				FDActionInfo info = AccountActivityUtil.getActionInfo(session);
				if(FDStoreProperties.getAvalaraTaxEnabled()){
					AvalaraContext context = new AvalaraContext(cart);
					context.setCommit(false);
					cart.getAvalaraTaxValue(context);
					info.setTaxationType(EnumNotificationType.AVALARA);
					}
				String orderID= FDCustomerManager.placeSubscriptionOrder(info, cart, null, false, cra, null);
				session.removeAttribute("SUBSCRIPTION_CART");
				return orderID;
			} catch (FDResourceException ex) {
				LOGGER.error("FDResourceException while attempting to place subscription order.", ex);
				results.addError(new ActionError("technical_difficulty", "We're currently experiencing technical difficulties. Please try again later."));
			} catch(DeliveryPassException ex) {
				LOGGER.error("Error performing a Delivery pass operation. ", ex);
				//There was delivery pass validation failure.
				results.addError(new ActionError("delivery_pass_error","Error performing a Delivery pass operation. "+ex.getMessage()));
			} catch (ErpFraudException e) {
				LOGGER.error("FDResourceException while attempting to place subscription order.", e);
				results.addError(new ActionError("technical_difficulty", "We're currently experiencing technical difficulties. Please try again later."));
			} catch (FDPaymentInadequateException pe) {
				LOGGER.error("Exception while attempting to place subscription order.", pe);
				results.addError(new ActionError("technical_difficulty", "We're currently experiencing technical difficulties. Please try again later."));
			}catch (InvalidCardException ice) {
				LOGGER.error("InvalidCardException while attempting to place subscription order.", ice);
				results.addError(new ActionError("technical_difficulty", "We're currently experiencing technical difficulties. Please try again later."));
			}catch (ErpTransactionException ete) {
				LOGGER.error("ErpTransactionException while attempting to place subscription order.", ete);
				results.addError(new ActionError("technical_difficulty", "We're currently experiencing technical difficulties. Please try again later."));
			}
		
		return "";
	} 
	
	private void editPaymentMethod(HttpServletRequest request, ActionResult actionResult) throws FDResourceException {
		this.populatePaymentMethod(request, actionResult);
		FDUserI user = CrmSession.getUser(pageContext.getSession());
		PaymentMethodUtil.validatePaymentMethod(request, this.paymentMethod, actionResult, user,true,EnumAccountActivityType.UPDATE_PAYMENT_METHOD);
		if (actionResult.isSuccess()) {
			if (CrmSession.verifyCaseAttachment(pageContext.getSession(), actionResult)) {
				PaymentMethodUtil.editPaymentMethod(request, actionResult, this.paymentMethod);
			}
		}
	}

	private void addPaymentMethod(HttpServletRequest request, ActionResult actionResult) throws FDResourceException {
		FDUserI user = CrmSession.getUser(pageContext.getSession());
		this.populatePaymentMethod(request, actionResult);

		PaymentMethodUtil.validatePaymentMethod(request, this.paymentMethod, actionResult, user,true,EnumAccountActivityType.ADD_PAYMENT_METHOD);
		if (EnumPaymentMethodType.ECHECK.equals(paymentMethod.getPaymentMethodType())) {
	        String terms = request.getParameter(PaymentMethodName.TERMS);
	        actionResult.addError(
	    	        terms == null || terms.length() <= 0,
	    	        PaymentMethodName.TERMS,SystemMessageList.MSG_REQUIRED
	    	        );
			if (actionResult.isSuccess() && !PaymentMethodUtil.hasECheckAccount(user.getIdentity())) {
				paymentMethod.setIsTermsAccepted(true);
			}
		}
		if (actionResult.isSuccess()) {
			if (CrmSession.verifyCaseAttachment(pageContext.getSession(), actionResult)) {
				PaymentMethodUtil.addPaymentMethod(request, actionResult, this.paymentMethod);
			}
		}
	}

	private void deletePaymentMethod(HttpServletRequest request, ActionResult actionResult) throws FDResourceException {
		String paymentId = request.getParameter("deletePaymentId");
		if (paymentId == null || paymentId.length() <= 0) {
			actionResult.addError(
				new ActionError(
					"technical_difficulty",
					"Sorry, we're experiencing technical difficulties. Please try again later."));
		}
		if (actionResult.isSuccess()) {
			if (CrmSession.verifyCaseAttachment(pageContext.getSession(), actionResult)) {
				PaymentMethodUtil.deletePaymentMethod(request, actionResult, paymentId);
			}
		}
	}

	private void populatePaymentMethod(HttpServletRequest request, ActionResult actionResult) {

		EnumPaymentMethodType paymentMethodType = EnumPaymentMethodType.getEnum(request.getParameter(PaymentMethodName.PAYMENT_METHOD_TYPE));
		if (paymentMethod == null) {
			paymentMethod = PaymentManager.createInstance(paymentMethodType);
		}
		String month = NVL.apply(request.getParameter(PaymentMethodName.CARD_EXP_MONTH), "");
		String year = NVL.apply(request.getParameter(PaymentMethodName.CARD_EXP_YEAR), "");
		String cardType = NVL.apply(request.getParameter(PaymentMethodName.CARD_BRAND), "");
		String accountNumber = NVL.apply(request.getParameter(PaymentMethodName.ACCOUNT_NUMBER), "");
		
		if (!"".equals(year) && !"".equals(month)) {
			SimpleDateFormat sf = new SimpleDateFormat("MMyyyy");
			Date date = sf.parse(month.trim() + year.trim(), new ParsePosition(0));
			Calendar expCal = new GregorianCalendar();
			expCal.setTime(date);
			expCal.set(Calendar.DATE, expCal.getActualMaximum(Calendar.DATE));
			this.paymentMethod.setExpirationDate(expCal.getTime());
		} else { this.paymentMethod.setExpirationDate(null); }
		
        boolean verifyBankAccountNumber = (EnumPaymentMethodType.ECHECK.equals(paymentMethod.getPaymentMethodType()));

		if(accountNumber.startsWith("xxx")){
			accountNumber = paymentMethod.getAccountNumber();
			verifyBankAccountNumber = false;
		}

    	if (verifyBankAccountNumber) {        	
	            String accountNumberVerify = request.getParameter(PaymentMethodName.ACCOUNT_NUMBER_VERIFY);
		        // Check account number verify is entered correctly
		        actionResult.addError(
		        accountNumberVerify != null && !accountNumberVerify.equalsIgnoreCase(accountNumber),
				PaymentMethodName.ACCOUNT_NUMBER_VERIFY, SystemMessageList.MSG_INVALID_ACCOUNT_NUMBER_VERIFY
				);
		        // Check account number verify
		        actionResult.addError(
		        accountNumberVerify == null || "".equals(accountNumberVerify),
				PaymentMethodName.ACCOUNT_NUMBER_VERIFY, SystemMessageList.MSG_REQUIRED
				);

		        // Check account number has at least 5 digits
		        String scrubbedAccountNumber = PaymentMethodUtil.scrubAccountNumber(accountNumber);

		        actionResult.addError(scrubbedAccountNumber.length() < 5,
				PaymentMethodName.ACCOUNT_NUMBER, SystemMessageList.MSG_ACCOUNT_NUMBER_LENGTH
				);
			
	    }
    	
    	
	
		this.paymentMethod.setName(request.getParameter(PaymentMethodName.ACCOUNT_HOLDER));
		this.paymentMethod.setAccountNumber(PaymentMethodUtil.scrubAccountNumber(accountNumber));
		this.paymentMethod.setAbaRouteNumber(request.getParameter(PaymentMethodName.ABA_ROUTE_NUMBER));
		this.paymentMethod.setBankAccountType(EnumBankAccountType.getEnum(request.getParameter(PaymentMethodName.BANK_ACCOUNT_TYPE)));
		this.paymentMethod.setBankName(request.getParameter(PaymentMethodName.BANK_NAME));
		this.paymentMethod.setCardType(EnumCardType.getCardType(cardType));	
		
		this.paymentMethod.setAddress1(NVL.apply(request.getParameter(EnumUserInfoName.BIL_ADDRESS_1.getCode()), "").trim());
		this.paymentMethod.setAddress2(NVL.apply(request.getParameter(EnumUserInfoName.BIL_ADDRESS_2.getCode()), "").trim());
		this.paymentMethod.setApartment(NVL.apply(request.getParameter(EnumUserInfoName.BIL_APARTMENT.getCode()), "").trim());
		this.paymentMethod.setCity(NVL.apply(request.getParameter(EnumUserInfoName.BIL_CITY.getCode()), "").trim());
		this.paymentMethod.setState(NVL.apply(request.getParameter(EnumUserInfoName.BIL_STATE.getCode()), "").trim());
		this.paymentMethod.setZipCode(NVL.apply(request.getParameter(EnumUserInfoName.BIL_ZIPCODE.getCode()), "").trim());
		this.paymentMethod.setCountry("US");
		
	}
	
	public static class TagEI extends AbstractControllerTag.TagEI {
		//default implementation
	}

	
}