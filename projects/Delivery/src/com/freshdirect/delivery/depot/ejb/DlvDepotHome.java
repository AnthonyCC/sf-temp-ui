/*
 * 
 * DlvDepotHome.java
 * Date: Jul 23, 2002 Time: 8:19:03 PM
 */
package com.freshdirect.delivery.depot.ejb;

/**
 * 
 * @author knadeem
 */
import javax.ejb.*;
import java.util.Collection;
import java.rmi.RemoteException;

import com.freshdirect.framework.core.*;

public interface DlvDepotHome extends EJBHome {
	
	/**
     * Create from model.
     *
     * @param model DlvDepotModel object
     */
     public DlvDepotEB create(ModelI model) throws CreateException, RemoteException;
    
     public DlvDepotEB findByPrimaryKey(PrimaryKey pk) throws FinderException, RemoteException;
     
     public Collection findAll() throws FinderException, RemoteException;
     
     public DlvDepotEB findByDepotCode(String depotCode) throws FinderException, RemoteException;

}
