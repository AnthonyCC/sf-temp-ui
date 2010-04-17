package com.freshdirect.webapp.util;

import javax.servlet.http.HttpSession;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDAuthenticationException;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;
import com.freshdirect.webapp.taglib.fdstore.SessionName;


/**
 * 
 * @author segabor
 *
 */
public class ShoppingCartUtil {
	/**
	 * Restore customer's original cart
	 * 
	 * @param session
	 * @param user
	 * @throws FDAuthenticationException
	 * @throws FDResourceException
	 */
	public static void restoreCart(HttpSession session) throws FDAuthenticationException, FDResourceException {
		FDSessionUser user = (FDSessionUser) session.getAttribute(SessionName.USER);
		
		FDCartModel originalCart = FDCustomerManager.recognize(user.getIdentity()).getShoppingCart();

		user.setShoppingCart( originalCart );
		user.invalidateCache();


		session.setAttribute( SessionName.USER, user );

		//The previous recommendations of the current user need to be removed.
        session.removeAttribute(SessionName.SMART_STORE_PREV_RECOMMENDATIONS);
	}
}
