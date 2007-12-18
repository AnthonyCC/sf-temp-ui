/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.fdstore.monitor.ejb;

import java.rmi.*;
import javax.ejb.*;
import javax.naming.*;

import org.apache.log4j.*;
import com.freshdirect.framework.util.log.LoggerFactory;

import com.freshdirect.framework.core.*;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.monitor.ejb.*;

/**
 *
 *
 * @version $Revision$
 * @author $Author$
 */
public class FDMonitorSessionBean extends SessionBeanSupport {
    
    private final static Category LOGGER = LoggerFactory.getInstance( FDMonitorSessionBean.class );
    
    private transient ErpMonitorHome erpMonitorHome = null;
    
    public void healthCheck() throws FDResourceException {
        
        if(this.erpMonitorHome == null){
            this.lookupErpMonitorHome();
        }
        try {
            ErpMonitorSB sb = (ErpMonitorSB) this.erpMonitorHome.create();
            sb.healthCheck();
        } catch(CreateException ce) {
            throw new FDResourceException(ce);
        } catch(RemoteException re) {
            throw new FDResourceException(re);
        }
    }
    
    private void lookupErpMonitorHome() throws FDResourceException {
        Context ctx = null;
        try {
            ctx = FDStoreProperties.getInitialContext();
            erpMonitorHome = (ErpMonitorHome) ctx.lookup("freshdirect.monitor.Monitor");
        } catch (NamingException ne) {
            throw new FDResourceException(ne);
        } finally {
            try {
                if(ctx != null) {
                    ctx.close();
                    ctx = null;
                }
            } catch(NamingException ne) {
                LOGGER.warn("Cannot close Context while trying to cleanup");
            }
        }
    }
    
    
}