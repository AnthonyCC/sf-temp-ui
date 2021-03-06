package com.freshdirect.webapp.taglib.fdstore;

import javax.servlet.http.HttpSession;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.webapp.taglib.AbstractGetterTag;

public class FDCartGetterTag extends AbstractGetterTag<FDCartModel> implements SessionName {

	private static final long serialVersionUID = 3449571121178723442L;


	protected FDCartModel getResult() throws FDResourceException {
        HttpSession session = pageContext.getSession();
        FDCartModel shoppingCart = ((FDUserI)session.getAttribute(USER)).getShoppingCart();
        if (shoppingCart == null) {
            // user doesn't have a cart, this is a bug, as login or site_access should put it there
            throw new FDResourceException("No shopping cart found");
        }
		return shoppingCart;
    }


	public static class TagEI extends AbstractGetterTag.TagEI {

		protected String getResultType() {
			return "com.freshdirect.fdstore.customer.FDCartModel";
		}

	}

}
