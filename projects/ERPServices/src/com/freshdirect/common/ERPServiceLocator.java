package com.freshdirect.common;

import javax.ejb.EJBException;
import javax.naming.Context;
import javax.naming.NamingException;

import com.freshdirect.customer.ejb.ErpCustomerHome;
import com.freshdirect.customer.ejb.ErpSaleHome;
import com.freshdirect.framework.core.ServiceLocator;
import com.freshdirect.mail.ejb.MailerGatewayHome;

public class ERPServiceLocator extends ServiceLocator {

    public ERPServiceLocator() {
    }

    public ERPServiceLocator(Context ctx) {
        super(ctx);
    }

    public MailerGatewayHome getMailerHome() {
        try {
            return (MailerGatewayHome) getRemoteHome("freshdirect.mail.MailerGateway", MailerGatewayHome.class);
        } catch (NamingException e) {
            throw new EJBException(e);
        }
    }

    public ErpSaleHome getErpSaleHome() {
        try {
            return (ErpSaleHome) getRemoteHome("freshdirect.erp.Sale", ErpSaleHome.class);
        } catch (NamingException e) {
            throw new EJBException(e);
        }
    }

    public ErpCustomerHome getErpCustomerHome() {
        try {
            return (ErpCustomerHome) getRemoteHome("freshdirect.erp.Customer", ErpCustomerHome.class);
        } catch (NamingException e) {
            throw new EJBException(e);
        }
    }

}
