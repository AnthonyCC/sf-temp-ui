package com.freshdirect.webapp.taglib.standingorder;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpSession;

import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.standingorders.FDStandingOrder;
import com.freshdirect.fdstore.standingorders.FDStandingOrdersManager;
import com.freshdirect.webapp.taglib.AbstractGetterTag;
import com.freshdirect.webapp.taglib.fdstore.SessionName;

public class GetStandingOrderDependencyIdsTag extends AbstractGetterTag<String> {
	
	private static final long serialVersionUID = 5338401818642931479L;
	protected String type;
	
	@Override
	protected String getResult() throws Exception {
		final FDStandingOrdersManager m = FDStandingOrdersManager.getInstance();
        HttpSession session = pageContext.getSession();
	
        FDUserI user = (FDUserI) session.getAttribute(SessionName.USER);
        
        Collection<FDStandingOrder> sos = m.loadCustomerStandingOrders( user.getIdentity() );
		
        Set<String> depedencyIdsUsedBySos = new HashSet<String>(); 
        StringBuilder result = new StringBuilder("[");
        boolean first = true;
        
        for (FDStandingOrder so : sos) {
    		String dependencyId = getDependencyId(so); 
        	if (depedencyIdsUsedBySos.add(dependencyId)){
    			if (first){
    				first = false;
    			} else {
    				result.append(", ");
    			}
        		result.append("'").append(dependencyId).append("'");
    		}
		}
        		 
        return result.append("]").toString();
	}

	protected String getDependencyId(FDStandingOrder so){
		if ("paymentMethod".equalsIgnoreCase(type)){
			return so.getPaymentMethodId();
		
		} else if ("deliveryAddress".equalsIgnoreCase(type)){
			return so.getAddressId();
		
		} else {
			return null;
		}
	}
	
    public static class TagEI extends AbstractGetterTag.TagEI {
        @Override
		protected String getResultType() {
            return "java.lang.String";
        }
    }
    
    public void setType(String type){
    	this.type = type;
    }
}
