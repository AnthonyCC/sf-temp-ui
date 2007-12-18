package com.freshdirect.cms.application.service.hibernate;

import java.util.List;

import org.hibernate.SessionFactory;

/**
 * Strategy interface for performing parent relationship lookups.
 *
 * @author lpekowsky
 */
interface ParentFinderI {

	/**
	 * Find all parents of a given node.
	 * 
	 * @param node
	 * @param sessionFactory Hibernate session factory
	 * @return List of {@link com.freshdirect.cms.ContentKey}, never null
	 */
	public List findParents(HibernateContentNode node, SessionFactory sessionFactory);

}
