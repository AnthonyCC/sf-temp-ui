/**
 * 
 * TellAFriendGetterTag.java
 * Created Dec 18, 2002
 */
package com.freshdirect.webapp.taglib.fdstore;

/**
 *
 *  @author knadeem
 */
import javax.servlet.http.*;

import com.freshdirect.customer.ErpCustomerInfoModel;
import com.freshdirect.fdstore.customer.*;
import com.freshdirect.fdstore.mail.*;

import com.freshdirect.webapp.taglib.AbstractGetterTag;

public class TellAFriendGetterTag extends AbstractGetterTag implements SessionName {
	
	protected Object getResult() throws Exception {
		HttpSession session = pageContext.getSession();
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		TellAFriend taf = (TellAFriend) session.getAttribute(TELL_A_FRIEND);
		
		FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);
		FDIdentity customerIdentity = user.getIdentity();
		ErpCustomerInfoModel customerInfo = FDCustomerFactory.getErpCustomerInfo(customerIdentity);	
		
		String productId  = (String) request.getParameter("productId");
		String categoryId = (String) request.getParameter("catId");
		String recipeId   = (String) request.getParameter("recipeId");
	
		boolean initTaf = false;
		
		if (taf != null) {
			if (!taf.isAbout(productId) && !taf.isAbout(recipeId)) {
				initTaf = true;
			}
		}else{
			initTaf = true;
		}
		
		if (initTaf) {
			// FIXME: use a factory pattern to build specific types of objects
			if (productId != null && categoryId != null) {
				taf = new TellAFriendProduct(categoryId, productId);
			} else if (recipeId != null) {
				taf = new TellAFriendRecipe(recipeId);
			} else {
				taf = new TellAFriend();
				taf.setCustomerIdentity(customerIdentity);				
			}
			
			decorateTellAFriend(taf, customerInfo);
		}
			
		return taf;
	}
	
	private void decorateTellAFriend(TellAFriend taf, ErpCustomerInfoModel customerInfo) {
		
		taf.setCustomerFirstName(customerInfo.getFirstName());
		taf.setCustomerLastName(customerInfo.getLastName());
		taf.setCustomerEmail(customerInfo.getEmail());
	}
	
	public static class TagEI extends AbstractGetterTag.TagEI {

		protected String getResultType() {
			return "com.freshdirect.fdstore.mail.TellAFriend";
		}

	}

}
