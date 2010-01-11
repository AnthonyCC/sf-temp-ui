package com.freshdirect.fdstore.customer.ejb;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.naming.Context;
import javax.naming.NamingException;

import com.freshdirect.customer.ejb.ActivityLogHome;
import com.freshdirect.customer.ejb.ErpCustomerHome;
import com.freshdirect.customer.ejb.ErpCustomerManagerHome;
import com.freshdirect.customer.ejb.ErpFraudPreventionHome;
import com.freshdirect.customer.ejb.ErpSaleHome;
import com.freshdirect.delivery.ejb.DlvManagerHome;
import com.freshdirect.deliverypass.ejb.DlvPassManagerHome;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.survey.ejb.FDSurveyHome;
import com.freshdirect.fdstore.survey.ejb.FDSurveySB;
import com.freshdirect.framework.core.ServiceLocator;
import com.freshdirect.giftcard.ejb.GiftCardManagerHome;
import com.freshdirect.mail.ejb.MailerGatewayHome;
import com.freshdirect.monitor.ejb.ErpMonitorHome;
import com.freshdirect.payment.ejb.PaymentManagerHome;

public class FDServiceLocator extends ServiceLocator {

    public FDServiceLocator() {
    	
    }

    public FDServiceLocator(Context ctx) {
        super(ctx);
    }

    public FDCustomerHome getFdCustomerHome() {
        try {
            return (FDCustomerHome) getRemoteHome("java:comp/env/ejb/FDCustomer", FDCustomerHome.class);
        } catch (NamingException e) {
            throw new EJBException(e);
        }
    }

    public ErpCustomerManagerHome getErpCustomerManagerHome() {
        try {
            return (ErpCustomerManagerHome) getRemoteHome("freshdirect.erp.CustomerManager", ErpCustomerManagerHome.class);
        } catch (NamingException e) {
            throw new EJBException(e);
        }
    }

    public ErpFraudPreventionHome getErpFraudHome() {
        try {
            return (ErpFraudPreventionHome) getRemoteHome("java:comp/env/ejb/FraudManager", ErpFraudPreventionHome.class);
        } catch (NamingException e) {
            throw new EJBException(e);
        }
    }

    public DlvManagerHome getDlvManagerHome() {
        try {
            return (DlvManagerHome) getRemoteHome("java:comp/env/ejb/DlvManager", DlvManagerHome.class);
        } catch (NamingException e) {
            throw new EJBException(e);
        }
    }

    public PaymentManagerHome getPaymentManagerHome() {
        try {
            return (PaymentManagerHome) getRemoteHome("java:comp/env/ejb/PaymentManager", PaymentManagerHome.class);
        } catch (NamingException e) {
            throw new EJBException(e);
        }
    }

    public DlvPassManagerHome getDlvPassManagerHome() {
        try {
            return (DlvPassManagerHome) getRemoteHome("java:comp/env/ejb/DlvPassManager", DlvPassManagerHome.class);
        } catch (NamingException e) {
            throw new EJBException(e);
        }
    }

    public GiftCardManagerHome getGiftCardGManagerHome() {
        try {
            return (GiftCardManagerHome) getRemoteHome("java:comp/env/ejb/GiftCardManager", GiftCardManagerHome.class);
        } catch (NamingException e) {
            throw new EJBException(e);
        }
    }

    public ActivityLogHome getActivityLogHome() {
        try {
            return (ActivityLogHome) getRemoteHome("freshdirect.customer.ActivityLog", ActivityLogHome.class);
        } catch (NamingException e) {
            throw new EJBException(e);
        }
    }
    
    public FDSurveyHome getSurveyHome() {
        try {
            return (FDSurveyHome) getRemoteHome(FDStoreProperties.getFDSurveyHome(), FDSurveyHome.class);
        } catch (NamingException e) {
            throw new EJBException(e);
        }
    }
    
    
    public FDCustomerManagerHome getFDCustomerManagerHome() {
        try {
            return (FDCustomerManagerHome) getRemoteHome(FDStoreProperties.getFDCustomerManagerHome(), FDCustomerManagerHome.class);
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
            return (ErpCustomerHome) getRemoteHome("java:comp/env/ejb/ErpCustomer", ErpCustomerHome.class);
        } catch (NamingException e) {
            throw new EJBException(e);
        }
    }

    public MailerGatewayHome getMailerHome() {
        try {
            return (MailerGatewayHome) getRemoteHome("freshdirect.mail.MailerGateway", MailerGatewayHome.class);
        } catch (NamingException e) {
            throw new EJBException(e);
        }
    }
    
    public ErpMonitorHome getErpMonitorHome() {
        try {
            return (ErpMonitorHome) getRemoteHome("freshdirect.monitor.Monitor", ErpMonitorHome.class);
        } catch (NamingException e) {
            throw new EJBException(e);
        }
    }

    
    public FDSurveySB getSurveySessionBean() {
        try {
            return getSurveyHome().create();
        } catch (RemoteException e) {
            throw new EJBException(e);
        } catch (CreateException e) {
            throw new EJBException(e);
        }
    }
    
    public FDCustomerManagerSB getFDCustomerManagerSessionBean() {
        try {
            return getFDCustomerManagerHome().create();
        } catch (RemoteException e) {
            throw new EJBException(e);
        } catch (CreateException e) {
            throw new EJBException(e);
        }
    }

}
