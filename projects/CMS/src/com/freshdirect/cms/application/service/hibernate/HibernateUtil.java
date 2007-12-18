/*
 * Created on Jul 22, 2005
 */
package com.freshdirect.cms.application.service.hibernate;

import org.hibernate.EntityMode;
import org.hibernate.Hibernate;
import org.hibernate.SessionFactory;
import org.hibernate.metadata.ClassMetadata;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentType;

/**
 * Utility class for mapping Hibernate objects to content nodes.
 * 
 * @author vszathmary
 */
class HibernateUtil {

	private HibernateUtil() {
	}

	/**
	 * Get the {@link ContentKey} of the given persistent entity.
	 * 
	 * @param sessionFactory Hibernate session factory
	 * @param o persistent object, never null
	 * @return ContentKey of the object, or null if it cannot be determined.
	 */
	public static ContentKey getKey(SessionFactory sessionFactory, Object o) {
		Class klass = Hibernate.getClass(o);
		ContentType type = HibernateTypeService.getContentType(klass);

		ClassMetadata meta = sessionFactory.getClassMetadata(klass);
		if (meta == null) {
			System.err.println("HibernateRelationship.getKey() " + klass.getName() + " -  " + o);
			return null;
		}
		Object id = meta.getIdentifier(o, EntityMode.POJO);
		return new ContentKey(type, String.valueOf(id));
	}

}
