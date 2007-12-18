package com.freshdirect;

import org.apache.log4j.xml.DOMConfigurator;
import org.hibernate.SessionFactory;

import com.freshdirect.framework.hibernate.HibernateDaoFactory;

public abstract class HibernateDbTestCaseSupport extends DbTestCaseSupport {

	/**
	 *  Configuration URL for the hibernate config file.
	 */
	private final static String HIBERNATE_CONFIG_URL = "classpath:/com/freshdirect/hibernate-testing.cfg.xml";

	static {
		// Configure log4j.
		DOMConfigurator.configure(Thread.currentThread().getContextClassLoader().getResource("log4j.xml"));
	}

	private SessionFactory sessionFactory;
	
	public HibernateDbTestCaseSupport(String name) {
		super(name);
	}

	protected SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	
	protected void setUp() throws Exception {
		super.setUp();
		sessionFactory = HibernateDaoFactory.getSessionFactory(HIBERNATE_CONFIG_URL);
		
	}

	protected void tearDown() throws Exception {
		sessionFactory.close();
		sessionFactory = null;
		super.tearDown();
	}

}
