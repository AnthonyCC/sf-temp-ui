package com.freshdirect.event.ejb;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;

/**
 * Home interface of impression logger.
 * @author istvan
 *
 */
public interface RecommendationEventLoggerHome extends EJBHome {

	/**
	 * Get impression logger session bean.
	 * @return impression logger session bean.
	 * @throws CreateException
	 * @throws RemoteException
	 */
	public RecommendationEventLoggerSB create() throws CreateException, RemoteException;
}
