package com.freshdirect.common;

import com.freshdirect.customer.ejb.ErpCustomerHome;
import com.freshdirect.customer.ejb.ErpSaleHome;
import com.freshdirect.framework.core.SessionBeanSupport;
import com.freshdirect.mail.ejb.MailerGatewayHome;

/**
 * It's needed, because there is a split between ERPServices and FDStore, and here we
 * cant refer to FDSessionBeanSupport.
 * 
 * @author zsombor
 *
 */
public class ERPSessionBeanSupport extends SessionBeanSupport {
    
	private static final long serialVersionUID = 757585523554698974L;
	

    /**
     * @return
     * @see com.freshdirect.common.ERPServiceLocator#getErpCustomerHome()
     */
    protected ErpCustomerHome getErpCustomerHome() {
        return ERPServiceLocator.getInstance().getErpCustomerHome();
    }

    /**
     * @return
     * @see com.freshdirect.common.ERPServiceLocator#getErpSaleHome()
     */
    protected ErpSaleHome getErpSaleHome() {
        return ERPServiceLocator.getInstance().getErpSaleHome();
    }

    /**
     * @return
     * @see com.freshdirect.common.ERPServiceLocator#getMailerHome()
     */
    protected MailerGatewayHome getMailerHome() {
        return ERPServiceLocator.getInstance().getMailerHome();
    }

}
