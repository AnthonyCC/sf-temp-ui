/**
 * @author ekracoff
 * Created on Apr 15, 2005*/

package com.freshdirect.listadmin.core;

import org.hibernate.SessionFactory;

import com.freshdirect.framework.hibernate.HibernateDaoFactory;
import com.freshdirect.listadmin.db.ListadminDao;

public class ListadminDaoFactory extends HibernateDaoFactory {

	/**
	 *  Configuration URL for the hibernate config file.
	 */
	private final static String HIBERNATE_CONFIG_URL = "classpath:/com/freshdirect/listadmin/hibernate.cfg.xml";

	/**
	 *  The singleton instance.
	 */
	private static ListadminDaoFactory instance = null;	
	
	/**
	 *  The hibernate session factory.
	 */
	private SessionFactory sessionFactory;
	
	/**
	 *  The list admin data access object.
	 */
	private ListadminDao		listadminDao;

	/**
	 *  Default constructor.
	 */
	private ListadminDaoFactory() {
		sessionFactory = HibernateDaoFactory.getSessionFactory(HIBERNATE_CONFIG_URL);
		listadminDao   = new ListadminDao(sessionFactory);
	}

	/**
	 *  Constructor with a hibernate session factory.
	 * 
	 *  @param sessionFactory the hibernate session factory to use.
	 */
	private ListadminDaoFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
		this.listadminDao   = new ListadminDao(sessionFactory);
	}

	/**
	 *  Set the hibernate session factory for the manager.
	 *  Use only for testing purposes.
	 * 
	 *  @param sessionFactory the new hibernate session factory.
	 */
	static synchronized void setSessionFactory(SessionFactory sessionFactory) {
		instance = new ListadminDaoFactory(sessionFactory);
	}
	
	/**
	 *  Get the singleton instance.
	 * 
	 *  @return the singleton instance of the listadmin manager.
	 */
	public static synchronized ListadminDaoFactory getInstance() {
		if (instance == null) {
			instance = new ListadminDaoFactory();
		}
		
		return instance;
	}

	/**
	 *  Get the hibernate session factory.
	 * 
	 *  @return the listadmin data access object.
	 */
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	
	/**
	 *  Get the listadmin data access object.
	 * 
	 *  @return the listadmin data access object.
	 */
	public ListadminDao getListadminDao() {
		return listadminDao;
	}
}
