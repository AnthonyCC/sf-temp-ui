/*
 * Class.java
 *
 * Created on October 29, 2001, 5:46 PM
 */

package com.freshdirect.webapp.taglib.fdstore;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.http.*;
import javax.servlet.jsp.*;

import com.freshdirect.framework.webapp.*;
import com.freshdirect.fdstore.*;
import com.freshdirect.fdstore.customer.*;
import com.freshdirect.customer.*;
import com.freshdirect.enums.CaptchaType;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.payment.EnumPaymentMethodType;
import com.freshdirect.webapp.ajax.expresscheckout.validation.data.ValidationError;
import com.freshdirect.webapp.util.CaptchaUtil;
import com.freshdirect.webapp.util.RequestUtil;

import org.apache.log4j.*;

public class PaymentMethodControllerTag extends com.freshdirect.framework.webapp.BodyTagSupport {

    private static Category LOGGER = LoggerFactory.getInstance( PaymentMethodControllerTag.class );
    private FDUserI user = null;

    private String actionName 		= "addPaymentMethod";
    private String successPage;
    private String result;

    public void setSuccessPage(String sp) {
        if (sp.length()>0 ){
            this.successPage = sp;
        }else {
            this.successPage = null;
        }
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }


    public void setResult(String resultName) {
        this.result = resultName;
    }

    public int doStartTag() throws JspException {
        //
        // perform any actions requested by the user if the request was a POST
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        HttpSession session = pageContext.getSession();
        ActionResult actionResult = new ActionResult();
        boolean isAddOrEditCard = false; 
        
        /* set UUID */
        if ( "GET".equalsIgnoreCase(request.getMethod()) && 
        	(
        		request.getRequestURI().indexOf("/your_account/add_creditcard.jsp") != -1 || 
        		request.getRequestURI().indexOf("/your_account/edit_creditcard.jsp") != -1
        	)
        ) {
        	FDSessionUser user = (FDSessionUser) request.getSession().getAttribute(SessionName.USER);
        	user.setAddCcUuid(UUID.randomUUID().toString());
        }
        
        if (("POST".equalsIgnoreCase(request.getMethod()))) {
            user = (FDUserI) session.getAttribute(SessionName.USER);
            

			try {
				/* verify UUID */
	            if(actionName.equalsIgnoreCase("addPaymentMethod") || actionName.equalsIgnoreCase("editPaymentMethod")) {
	            	FDSessionUser sessionUser = (FDSessionUser) user;
	            	String hash = RequestUtil.getRequestParameter(request, "hash");
	            	ErpPaymentMethodI paymentMethod = null;
	            	
	            	if(actionName.equalsIgnoreCase("addPaymentMethod")) {
	            		paymentMethod = PaymentMethodUtil.processForm(request, actionResult, user.getIdentity());
	            	}
	            	if(actionName.equalsIgnoreCase("editPaymentMethod")) {
	            		paymentMethod = PaymentMethodUtil.processEditForm(request, actionResult, user.getIdentity());
	            	}
	            	
	            	
	            	if (paymentMethod != null &&
	            		(
	            			paymentMethod.getPaymentMethodType() == EnumPaymentMethodType.CREDITCARD ||
	            			paymentMethod.getPaymentMethodType() == EnumPaymentMethodType.DEBITCARD
	            		)
	            	) {
						isAddOrEditCard = true;
					}
					/* limit to cc/debit */
	            	if (isAddOrEditCard && hash == null || "".equals(hash) || sessionUser == null || !hash.equals(sessionUser.getAddCcUuid())) {
	            		//"fail" silently
	                    pageContext.setAttribute(this.result, actionResult);
	            		return EVAL_BODY_BUFFERED;
	            	}
	            }
	            
	            if(actionName.equalsIgnoreCase("addPaymentMethod")) {
	            	if (!checkCaptcha(request, actionResult)) {
	            		return EVAL_BODY_BUFFERED;
	            	}
	                ErpPaymentMethodI paymentMethod = PaymentMethodUtil.processForm(request, actionResult, user.getIdentity());
					if (paymentMethod.getPaymentMethodType() == EnumPaymentMethodType.CREDITCARD
							|| paymentMethod.getPaymentMethodType() == EnumPaymentMethodType.DEBITCARD) {
						isAddOrEditCard = true;
					}
	                if(actionResult.isSuccess()){
	                    PaymentMethodUtil.validatePaymentMethod(request, paymentMethod, actionResult, user,true,EnumAccountActivityType.ADD_PAYMENT_METHOD);
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
	                    if(actionResult.isSuccess()) {
	                        PaymentMethodUtil.addPaymentMethod(request, actionResult, paymentMethod);
	                    }
	                }
	            } else if(actionName.equalsIgnoreCase("editPaymentMethod")) {
	            	if (!checkCaptcha(request, actionResult)) {
	            		return EVAL_BODY_BUFFERED;
	            	}
	            	ErpPaymentMethodI paymentMethod = PaymentMethodUtil.processEditForm(request, actionResult, user.getIdentity());
					if (paymentMethod.getPaymentMethodType() == EnumPaymentMethodType.CREDITCARD
							|| paymentMethod.getPaymentMethodType() == EnumPaymentMethodType.DEBITCARD) {
						isAddOrEditCard = true;
					}
	                if(actionResult.isSuccess()){
	                    PaymentMethodUtil.validatePaymentMethod(request, paymentMethod, actionResult, user, true, EnumAccountActivityType.UPDATE_PAYMENT_METHOD);
	                    if(actionResult.isSuccess()){
	                    	paymentMethod.setAvsCkeckFailed(false);
	                        PaymentMethodUtil.editPaymentMethod(request, actionResult, paymentMethod);
	                    }
	                }
	
	            } else if(actionName.equalsIgnoreCase("deletePaymentMethod")) {
	                String paymentId = request.getParameter("deletePaymentId");
	                if(paymentId == null || paymentId.length() <= 0){
	                    actionResult.addError(new ActionError("technical_difficulty", SystemMessageList.MSG_TECHNICAL_ERROR));
	                }
	                if(actionResult.isSuccess()){
	                    PaymentMethodUtil.deletePaymentMethod(request, actionResult, paymentId);
	                }
	            }
	            else if(actionName.equalsIgnoreCase("defaultPaymentMethod")){
	            	String paymentId = request.getParameter("defaultPaymentMethod");
	            	if(paymentId == null || paymentId.length() <= 0){
	                    actionResult.addError(new ActionError("technical_difficulty", SystemMessageList.MSG_TECHNICAL_ERROR));
	                }
	            	 if(actionResult.isSuccess()){
	            		 FDActionInfo info = AccountActivityUtil.getActionInfo(request.getSession());
	            		 com.freshdirect.fdstore.payments.util.PaymentMethodUtil.updateDefaultPaymentMethod(info, user.getPaymentMethods(), paymentId, EnumPaymentMethodDefaultType.DEFAULT_CUST, true);
	            	 }
	            }

            } catch (FDResourceException ex) {
				LOGGER.error("Error performing action "+actionName, ex);
				actionResult.addError(new ActionError("technical_difficulty", SystemMessageList.MSG_TECHNICAL_ERROR));
            }
			
			// if action is add or edit credit/debit card, reset or increment the attempt number depends on the result
			if (isAddOrEditCard) {
		        if (actionResult.isSuccess()) {
		        	session.setAttribute(SessionName.PAYMENT_ATTEMPT, 0);
		        } else {
		        	int currentAttempt = session.getAttribute(SessionName.PAYMENT_ATTEMPT) != null ? (Integer) session.getAttribute(SessionName.PAYMENT_ATTEMPT) : Integer.valueOf(0);
		    		session.setAttribute(SessionName.PAYMENT_ATTEMPT, ++currentAttempt);
		        }
	        }
			
            // redirect to success page if an action was successfully performed
            // and a success page was defined
            //
            if (actionResult.isSuccess() && (successPage != null)) {
                HttpServletResponse response = (HttpServletResponse) pageContext.getResponse();
                try {
                    response.sendRedirect(response.encodeRedirectURL(successPage));
                    JspWriter writer = pageContext.getOut();
                    writer.close();
                    return SKIP_BODY;
                } catch (IOException ioe) {
                    // if there was a problem redirecting, well.. it can't get any worse :)
                    throw new JspException("Error redirecting "+ioe.getMessage());
                }
            }
            else if(request.getSession().getAttribute("verifyFail")!=null) {
            	HttpServletResponse response = (HttpServletResponse) pageContext.getResponse();
                try {
                    response.sendRedirect(response.encodeRedirectURL("/logout.jsp"));
                    JspWriter writer = pageContext.getOut();
                    writer.close();
                    return SKIP_BODY;
                } catch (IOException ioe) {
                    // if there was a problem redirecting, well.. it can't get any worse :)
                    throw new JspException("Error redirecting "+ioe.getMessage());
                }
            }
            if("deactivated_Account".equals( actionResult.getError("deactivated_Account").getType()))  {
            	HttpServletResponse response = (HttpServletResponse) pageContext.getResponse();
                try {
                    response.sendRedirect(response.encodeRedirectURL("/logout.jsp"));
                    JspWriter writer = pageContext.getOut();
                    writer.close();
                    return SKIP_BODY;
                } catch (IOException ioe) {
                    // if there was a problem redirecting, well.. it can't get any worse :)
                    throw new JspException("Error redirecting "+ioe.getMessage());
                }
            }
        }

        // place the result as a scripting variable in the page
        //
        pageContext.setAttribute(this.result, actionResult);
        return EVAL_BODY_BUFFERED;
    }
    private boolean checkCaptcha(HttpServletRequest request, ActionResult result) {
    	HttpSession session = request.getSession();
    	String ip = request.getRemoteAddr();
    	String captchaToken = request.getParameter("g-recaptcha-response") != null
				? request.getParameter("g-recaptcha-response").toString()
				: null;
				
		boolean isCaptchaSuccess = CaptchaUtil.validateCaptcha(captchaToken,
				ip, CaptchaType.PAYMENT, session, SessionName.PAYMENT_ATTEMPT, FDStoreProperties.getMaxInvalidPaymentAttempt());
		if (!isCaptchaSuccess) {
			result.addError(new ActionError("captcha", SystemMessageList.MSG_INVALID_CAPTCHA));
			pageContext.setAttribute(this.result, result);
			return false;
		}
		
		return true;
	}
}