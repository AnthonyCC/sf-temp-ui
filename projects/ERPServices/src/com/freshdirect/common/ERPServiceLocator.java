package com.freshdirect.common;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.naming.Context;
import javax.naming.NamingException;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.content.nutrition.ejb.ErpNutritionHome;
import com.freshdirect.content.nutrition.ejb.ErpNutritionSB;
import com.freshdirect.customer.ejb.ErpCustomerHome;
import com.freshdirect.customer.ejb.ErpSaleHome;
import com.freshdirect.erp.ejb.ErpCOOLManagerHome;
import com.freshdirect.erp.ejb.ErpCOOLManagerSB;
import com.freshdirect.erp.ejb.ErpInfoHome;
import com.freshdirect.erp.ejb.ErpInfoSB;
import com.freshdirect.fdstore.FDRuntimeException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.ejb.FDFactoryHome;
import com.freshdirect.fdstore.ejb.FDFactorySB;
import com.freshdirect.framework.core.ServiceLocator;
import com.freshdirect.mail.ejb.MailerGatewayHome;
import com.freshdirect.security.ticket.TicketServiceHome;
import com.freshdirect.security.ticket.TicketServiceSB;

public class ERPServiceLocator extends ServiceLocator {

    static ERPServiceLocator INSTANCE = null;

    public static ERPServiceLocator getInstance() {
        try {
            if (INSTANCE == null) {
                INSTANCE = new ERPServiceLocator(FDStoreProperties.getInitialContext());
            }
            return INSTANCE;
        } catch (NamingException e) {
            throw new FDRuntimeException(e);
        }
    }

    /**
     * For testing purposes only.
     * @param instance
     */
    public static void setInstance(ERPServiceLocator instance) {
        INSTANCE = instance;
    }

    @Deprecated
    public ERPServiceLocator() {
    }

    public ERPServiceLocator(Context ctx) {
        super(ctx);
    }

    public MailerGatewayHome getMailerHome() {
        try {
            return (MailerGatewayHome) getRemoteHome("freshdirect.mail.MailerGateway");
        } catch (NamingException e) {
            throw new EJBException(e);
        }
    }

    public ErpSaleHome getErpSaleHome() {
        try {
            return (ErpSaleHome) getRemoteHome("freshdirect.erp.Sale");
        } catch (NamingException e) {
            throw new EJBException(e);
        }
    }

    public ErpCustomerHome getErpCustomerHome() {
        try {
            return (ErpCustomerHome) getRemoteHome("freshdirect.erp.Customer");
        } catch (NamingException e) {
            throw new EJBException(e);
        }
    }

    private ErpCOOLManagerHome getCOOLInfoHome() {
        try {
            return (ErpCOOLManagerHome) getRemoteHome(ErpServicesProperties.getCOOLManagerHome());
        } catch (NamingException ne) {
            throw new EJBException(ne);
        }
    }

    public ErpCOOLManagerSB getErpCOOLManagerSessionBean() {
        try {
            return getCOOLInfoHome().create();
        } catch (RemoteException e) {
            throw new EJBException(e);
        } catch (CreateException e) {
            throw new EJBException(e);
        }
    }

    private ErpInfoHome getErpInfoHome() {
        try {
            return (ErpInfoHome) getRemoteHome("freshdirect.erp.Info");
        } catch (NamingException ne) {
            throw new EJBException(ne);
        }
    }

    public ErpInfoSB getErpInfoSessionBean() {
        try {
            return getErpInfoHome().create();
        } catch (RemoteException e) {
            throw new EJBException(e);
        } catch (CreateException e) {
            throw new EJBException(e);
        }
    }

    private ErpNutritionHome getErpNutritionHome() {
        try {
            return (ErpNutritionHome) getRemoteHome("freshdirect.content.Nutrition");
        } catch (NamingException ne) {
            throw new EJBException(ne);
        }
    }

    public ErpNutritionSB getErpNutritionSessionBean() {
        try {
            return getErpNutritionHome().create();
        } catch (RemoteException e) {
            throw new EJBException(e);
        } catch (CreateException e) {
            throw new EJBException(e);
        }
    }

    public FDFactoryHome getFactoryHome() {
        try {
            return (FDFactoryHome) getRemoteHome(FDStoreProperties.getFDFactoryHome());
        } catch (NamingException e) {
            throw new EJBException(e);
        }
    }

    public FDFactorySB getFDFactorySessionBean() {
        try {
            return getFactoryHome().create();
        } catch (RemoteException e) {
            throw new EJBException(e);
        } catch (CreateException e) {
            throw new EJBException(e);
        }
    }

    TicketServiceHome getTicketServiceHome() {
        try {
            return (TicketServiceHome) getRemoteHome(TicketServiceHome.JNDI_HOME);
        } catch (NamingException e) {
            throw new EJBException(e);
        }
    }
    
    public TicketServiceSB getTicketServiceSessionBean() {
        try {
            return getTicketServiceHome().create();
        } catch (RemoteException e) {
            throw new EJBException(e);
        } catch (CreateException e) {
            throw new EJBException(e);
        }
    }
    

}
