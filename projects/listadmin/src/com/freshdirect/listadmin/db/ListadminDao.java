/**
 * @author ekracoff
 * Created on May 19, 2005*/

package com.freshdirect.listadmin.db;

import org.hibernate.SessionFactory;

import com.freshdirect.framework.conf.ResourceUtil;
import com.freshdirect.framework.hibernate.HibernateDaoFactory;
import com.freshdirect.framework.hibernate.HibernateDaoSupport;
import com.freshdirect.listadmin.core.ListadminDaoFactory;

public class ListadminDao extends HibernateDaoSupport {

	private SessionFactory sessionFactory;
	
	/**
	 *  Constructor.
	 * 
	 *  @param sessionFactory the hibernate session factory to use for
	 *         accessing permanent storage.
	 */
	public ListadminDao(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	/**
	 *  Return a hibernate session factory to use for accessing persistent storage.
	 * 
	 *  @return a hibernate session factory
	 *  @see ResourceUtil#getResource(String)
	 */
	protected SessionFactory getSessionFactory()
	{
		return sessionFactory;
	}

	protected HibernateDaoFactory getDaoFactory() {
		return ListadminDaoFactory.getInstance();
	}
}