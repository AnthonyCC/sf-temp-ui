package com.freshdirect.webapp.taglib.standingorder;

import java.util.Collection;

import javax.servlet.http.HttpSession;

import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.standingorders.FDStandingOrder;
import com.freshdirect.fdstore.standingorders.FDStandingOrdersManager;
import com.freshdirect.webapp.taglib.AbstractGetterTag;
import com.freshdirect.webapp.taglib.fdstore.SessionName;

public class ListStandingOrdersTag extends AbstractGetterTag {
	
	private static final long serialVersionUID = 5338401818642931479L;

	/**
	 * Returns list of (not deleted) standing orders for the active user
	 */
	@Override
	protected Object getResult() throws Exception {
		final FDStandingOrdersManager m = FDStandingOrdersManager.getInstance();
        HttpSession session = pageContext.getSession();
	
        FDUserI user = (FDUserI) session.getAttribute(SessionName.USER);
        
        Collection<FDStandingOrder> so = m.loadCustomerStandingOrders( user.getIdentity() );
		
		return so;
	}

    public static class TagEI extends AbstractGetterTag.TagEI {
        @Override
		protected String getResultType() {
            return "java.util.Collection<com.freshdirect.fdstore.standingorders.FDStandingOrder>";
        }
    }
}
