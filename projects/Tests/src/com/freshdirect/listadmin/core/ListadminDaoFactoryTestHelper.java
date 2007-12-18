package com.freshdirect.listadmin.core;

import org.hibernate.SessionFactory;

/**
 * A helper package for changing behaviour of the ListadminDaoFactory class,
 * enabling testing the listadmin package.
 */
public class ListadminDaoFactoryTestHelper {
	/**
	 *  Override the hibernate session factory for the ListadminDaoFactory
	 * 
	 *  @param sessionFactory the new hibernate session factory to use
	 */
	public static void setSessionFactory(SessionFactory sessionFactory) {
		ListadminDaoFactory.setSessionFactory(sessionFactory);
	}
}
