package com.freshdirect.cms.core;

import org.hibernate.SessionFactory;

import com.freshdirect.cms.publish.PublishDao;
import com.freshdirect.framework.hibernate.HibernateDaoFactory;


public class CmsDaoFactory extends HibernateDaoFactory {

	/**
	 *  Configuration URL for the hibernate config file.
	 */
	private final static String HIBERNATE_CONFIG_URL = "classpath:/com/freshdirect/cms/hibernate.cfg.xml";

	/**
	 *  The singleton instance.
	 */
	private static CmsDaoFactory instance = null;	
	
	/**
	 *  The hibernate session factory.
	 */
	private SessionFactory sessionFactory;
	
	/**
	 *  The publish data access object.
	 */
	private PublishDao		publishDao;

	/**
	 *  Default constructor.
	 */
	private CmsDaoFactory() {
		sessionFactory = HibernateDaoFactory.getSessionFactory(HIBERNATE_CONFIG_URL);
		publishDao     = new PublishDao(this);
	}

	/**
	 *  Set the hibernate session factory for the manager.
	 *  Use only for testing purposes.
	 * 
	 *  @param sessionFactory the new hibernate session factory.
	 */
	void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
		this.publishDao     = new PublishDao(this);
	}
	
	/**
	 *  Get the singleton instance.
	 * 
	 *  @return the singleton instance of the listadmin manager.
	 */
	public static synchronized CmsDaoFactory getInstance() {
		if (instance == null) {
			instance = new CmsDaoFactory();
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
	 *  Get the publish data access object.
	 * 
	 *  @return the publish data access object.
	 */
	public PublishDao getPublishDao() {
		return publishDao;
	}
}
