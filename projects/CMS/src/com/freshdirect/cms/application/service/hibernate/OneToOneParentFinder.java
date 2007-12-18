package com.freshdirect.cms.application.service.hibernate;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Expression;
import org.hibernate.type.EntityType;
import org.hibernate.type.ManyToOneType;
import org.hibernate.type.OneToOneType;

import com.freshdirect.cms.ContentType;

/**
 * Parent lookup strategy for one-to-one relationships.
 * 
 * @author lpekowsky
 */
class OneToOneParentFinder implements ParentFinderI {
	
	private final EntityType type;
	private final Class parentClass;
	private final String propertyName;
	private final ContentType parentType;
	
	public OneToOneParentFinder(Class parentClass, 	ContentType parentType, String propertyName, EntityType et) {
		this.parentClass  = parentClass;
		this.propertyName = propertyName;
		this.parentType   = parentType;
		this.type = et;
	}

	// Each Foo (possibly) has a Bar.  So either foo has a barId or bar has a fooId
	public List findParents(HibernateContentNode node, SessionFactory sessionFactory) {
		if(type instanceof ManyToOneType) {
			return manyToOne(node, sessionFactory);
		} else if(type instanceof OneToOneType) {
			return oneToOne(node, sessionFactory);
		}
		
		return new ArrayList();
	}
	
	// each foo has a barId
	private List manyToOne(HibernateContentNode node, SessionFactory sessionFactory) {
		Session sess = sessionFactory.openSession();
		
		List l = sess.createCriteria(parentClass).add(Expression.eq(propertyName, node.getObject())).list();
		
		sess.close();
		
		return l;
	}
	
	// each bar has a fooId
	private List oneToOne(HibernateContentNode node, SessionFactory sessionFactory) {
		//OneToOneType oneToOne = (OneToOneType) type;
		
		return new ArrayList();
	}
}
