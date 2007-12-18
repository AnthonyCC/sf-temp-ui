/*
 * Created on Apr 30, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.freshdirect.webapp.taglib.fdstore;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

import org.apache.log4j.Category;

import com.freshdirect.customer.ErpAddressModel;
import com.freshdirect.fdstore.FDDeliveryManager;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionError;
import com.freshdirect.framework.webapp.ActionResult;

/**
 * @author knadeem
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class AgeVerificationControllerTag extends com.freshdirect.framework.webapp.BodyTagSupport {
	
	private static Category LOGGER = LoggerFactory.getInstance( AgeVerificationControllerTag.class );
	
	private String blockedAddressPage = "/checkout/no_alcohol_address.jsp";
	private String actionName;
	private String successPage;
	private String result;

	public void setSuccessPage(String sp) {
		this.successPage = sp;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	public void setResult(String resultName) {
		this.result = resultName;
	}
	
	public void setBlockedAddressPage(String blockedAddressPage) {
		this.blockedAddressPage = blockedAddressPage;
	}
	
	public int doStartTag() throws JspException {
		
		ActionResult actionResult = new ActionResult();
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		HttpSession session = pageContext.getSession();
		FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);
		FDCartModel cart = user.getShoppingCart();
		try{
			if(cart.containsAlcohol()){
				if("POST".equalsIgnoreCase(request.getMethod()) && !cart.isAgeVerified()){
					if(request.getParameter("age_verified") != null){
						cart.setAgeVerified(true);
						user.setShoppingCart(cart);
						session.setAttribute(SessionName.USER, user);
					}else{
						actionResult.addError(new ActionError("didnot_agree", "You must certify that you are over 21 in order to proceed with Checkout. If you cannot, please remove the alcohol items from your cart before continuing."));
					}
				}
				if(cart.isAgeVerified()){
					if(!this.verifyAddress(cart.getDeliveryAddress(), actionResult)){
						return SKIP_BODY;
					}
				}
			}
			if ((cart.isAgeVerified() || !cart.containsAlcohol()) && actionResult.isSuccess() && (this.successPage != null)) {

				HttpServletResponse response = (HttpServletResponse) pageContext.getResponse();
				try {
					response.sendRedirect(response.encodeRedirectURL(this.successPage));
					JspWriter writer = pageContext.getOut();
					writer.close();
				} catch (IOException ioe) {
					throw new JspException(ioe.getMessage());
				}

				return SKIP_BODY;
			}
			
		}catch(FDResourceException ex){
			LOGGER.warn("Resource error during authentication", ex);
			actionResult.addError(new ActionError("technical_difficulty", SystemMessageList.MSG_TECHNICAL_ERROR));
		}
		
		pageContext.setAttribute(this.result, actionResult);
		return EVAL_BODY_BUFFERED;
	}
	
	private boolean verifyAddress(ErpAddressModel address, ActionResult actionResult) throws JspException, FDResourceException {
		HttpServletResponse response = (HttpServletResponse) pageContext.getResponse();
		
		try{
			if(!FDDeliveryManager.getInstance().checkForAlcoholDelivery(address)){
				response.sendRedirect(response.encodeRedirectURL(blockedAddressPage));
				return false;
			}
			return true;
		}catch (IOException ioe) {
			throw new JspException(ioe.getMessage());
		}
	}
	
	public static class TagEI extends TagExtraInfo {
		public VariableInfo[] getVariableInfo(TagData data) {
			return new VariableInfo[] {
				new VariableInfo(
					data.getAttributeString("result"),
					"com.freshdirect.framework.webapp.ActionResult",
					true,
					VariableInfo.NESTED)
			};
		}
	}
}
