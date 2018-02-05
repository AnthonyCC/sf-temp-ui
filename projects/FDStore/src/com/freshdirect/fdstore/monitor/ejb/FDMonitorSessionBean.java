/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.fdstore.monitor.ejb;

import java.rmi.RemoteException;

import javax.ejb.CreateException;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.ejb.FDSessionBeanSupport;
import com.freshdirect.monitor.ejb.ErpMonitorSB;

/**
 *
 *
 * @version $Revision$
 * @author $Author$
 */

/**
 *@deprecated Please use the DbMonitorController and DbMonitorServiceI in Storefront2.0 project.
 * SVN location :: https://appdevsvn.nj01/appdev/ecommerce
 *
 *
 */
public class FDMonitorSessionBean extends FDSessionBeanSupport {
    
    public void healthCheck() throws FDResourceException {
        try {
            ErpMonitorSB sb = this.getErpMonitor();
            sb.healthCheck();
        } catch(CreateException ce) {
            throw new FDResourceException(ce);
        } catch(RemoteException re) {
            throw new FDResourceException(re);
        }
    }
    
}