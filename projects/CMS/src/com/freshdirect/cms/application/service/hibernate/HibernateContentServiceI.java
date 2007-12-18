package com.freshdirect.cms.application.service.hibernate;

import org.hibernate.SessionFactory;

import com.freshdirect.cms.application.ContentServiceI;

/**
 * Service interface for {@link com.freshdirect.cms.application.service.hibernate.HibernateContentService}.
 * 
 * @author lpekowsky
 */
public interface HibernateContentServiceI extends ContentServiceI {

	/**
	 * Get the session factory used by this service.
	 * 
	 * @return Hibernate session factory, never null
	 */
	public SessionFactory getSessionFactory();

}
