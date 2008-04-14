/**
 * 
 * SiteEmailControllerTag.java
 * Created Dec 6, 2002
 */
package com.freshdirect.webapp.taglib.fdstore;

/**
 *
 *  @author knadeem
 */
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;

import org.apache.oro.text.perl.Perl5Util;
import com.freshdirect.customer.ErpCustomerInfoModel;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDCustomerFactory;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.mail.TellAFriend;
import com.freshdirect.fdstore.mail.TellAFriendProduct;
import com.freshdirect.fdstore.mail.TellAFriendRecipe;
import com.freshdirect.framework.webapp.ActionError;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.framework.webapp.WebFormI;
import com.freshdirect.webapp.taglib.AbstractControllerTag;

public class SiteEmailControllerTag extends AbstractControllerTag implements SessionName {

	 protected boolean performAction(HttpServletRequest request, ActionResult result) throws JspException {

	 	String actionName = this.getActionName();
	 	System.out.println("actionName :"+actionName);
	 	try {
		 	if ("previewEmail".equalsIgnoreCase(actionName)){
		 		if (!this.getForm(request, result)) {
		 			return true;
		 		}
		 	}else if ("sendEmail".equalsIgnoreCase(actionName)){
		 		this.sendEmail(request);	 		
		 	} else if ("sendEmailNoPreview".equalsIgnoreCase(actionName)){
		 		if (!this.getForm(request, result)) {
		 			return true;
		 		}
		 		this.sendEmail(request);

		 	}		 	
		}catch(FDResourceException fe){

			fe.printStackTrace();
			result.addError(true,"SYSTEM_ERROR", "We are experiencing technical difficulties, please try later");			
		}				
	 	return true;
	 }

	 private boolean getForm(HttpServletRequest request, ActionResult result) throws FDResourceException {
	 	
	 	HttpSession session = pageContext.getSession();

 		FDSessionUser user = (FDSessionUser)session.getAttribute(SessionName.USER);
		FDIdentity customerIdentity = user.getIdentity();
		ErpCustomerInfoModel customerInfo = FDCustomerFactory.getErpCustomerInfo(customerIdentity);	 		
	 	
	 	TellAFriendForm  form = new TellAFriendForm();

	 	form.setCustomerInfo(customerInfo);
	 	
	 	form.populateForm(request);
 		form.validateForm(result);
 		
 		TellAFriend friend= form.getTellAFriend(customerInfo, customerIdentity);
 		//friend.setUser(user.getUser());
 		//System.out.println("friend.setUser(user) :"+user); 		
		session.setAttribute(TELL_A_FRIEND, friend);
	
        if (!result.isSuccess()){
			return false; 			
 		}

		return true;
	 }
	 
	 private void sendEmail(HttpServletRequest request) throws FDResourceException {
	 	
	 	HttpSession session = pageContext.getSession();
	 	FDSessionUser user = (FDSessionUser)session.getAttribute(SessionName.USER);

	 	TellAFriend taf = (TellAFriend) session.getAttribute(TELL_A_FRIEND);
	 	System.out.println("sending the mail");	
		taf.setServer(request.getServerName()+":"+request.getServerPort());		
 		
		taf.send(pageContext.getServletContext(), user.getUser());
				
		session.removeAttribute(TELL_A_FRIEND);
	 }

	 private static class TellAFriendForm implements WebFormI {

	 	private String friendName;
	 	private String friendEmail;
	 	private String emailText;
		private String productId;
		private String categoryId;
		private String recipeId;
		
		boolean isProductEmail;

		ErpCustomerInfoModel customerInfo;
		
		public void setCustomerInfo(ErpCustomerInfoModel customerInfo) { this.customerInfo = customerInfo; }
		
	 	public void populateForm(HttpServletRequest request) {
	 		this.friendName = request.getParameter("friend_name");
	 		this.friendEmail = request.getParameter("friend_email");
	 		
	 		//going to strip html tags from email text
	 		Perl5Util perlUtil = new Perl5Util();
            String pattern = "s/<[^>]*>//gs";
            String text = request.getParameter("email_text");
	 		this.emailText = perlUtil.substitute(pattern, text.substring(0, Math.min(1000, text.length())));

	 		this.productId  = request.getParameter("productId");
	 		this.categoryId = request.getParameter("catId");
	 		this.recipeId   = request.getParameter("recipeId"); 
	 	}


	 	public void validateForm(ActionResult result) {
	 		result.addError(friendName==null || friendName.length() < 1,
	 		"friend_name", "<br>"+SystemMessageList.MSG_REQUIRED
        	);
        	
        	result.addError(
            friendEmail == null || friendEmail.length() < 1,
            "friend_email", "<br>"+SystemMessageList.MSG_REQUIRED
            );
            
            result.addError(
            !result.hasError("friend_email") && !com.freshdirect.mail.EmailUtil.isValidEmailAddress(friendEmail),
            "friend_email", "<br>"+SystemMessageList.MSG_EMAIL_FORMAT
            );
        	
//            result.addError(
//                    !result.hasError("friend_email") && (friendEmail.equalsIgnoreCase(customerInfo.getEmail()) || friendEmail.equalsIgnoreCase(customerInfo.getAlternateEmail())),
//                    "friend_email", "<br>"+SystemMessageList.MSG_EMAIL_TO_SELF
//                    );

            result.addError(
        			emailText == null || emailText.length() < 1,
                    "email_text", "<br>"+SystemMessageList.MSG_REQUIRED
                    );
	 	}
	 	

	 	public TellAFriend getTellAFriend(ErpCustomerInfoModel customerInfo, FDIdentity customerIdentity) throws FDResourceException {
	
	 		// FIXME: use a factory pattern ot create objects of proper type
	 		TellAFriend taf;
	 		
			if (productId != null) {
				taf = new TellAFriendProduct(categoryId, productId);
			} else if (recipeId != null) {
				taf = new TellAFriendRecipe(recipeId);
			} else {
				taf = new TellAFriend();
				taf.setCustomerIdentity(customerIdentity);				
			}
			
			decorateTAF(taf, customerInfo);
			
			return taf;
		}

		private void decorateTAF(TellAFriend a, ErpCustomerInfoModel customerInfo) {
			a.setFriendName(friendName);
			a.setFriendEmail(friendEmail);
			a.setEmailText(emailText);
			a.setCustomerFirstName(customerInfo.getFirstName());
			a.setCustomerLastName(customerInfo.getLastName());
			a.setCustomerEmail(customerInfo.getEmail());
		}
		
	 }

    public static class TagEI extends AbstractControllerTag.TagEI {
        // default impl
    }

}


