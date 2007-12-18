/*
 * Created on Jul 5, 2005
 *
 */
package com.freshdirect.cms.application.service.hibernate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.EntityMode;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.engine.SessionFactoryImplementor;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.type.CollectionType;
import org.hibernate.type.EntityType;
import org.hibernate.type.Type;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.application.CmsRequestI;
import com.freshdirect.cms.application.CmsResponse;
import com.freshdirect.cms.application.CmsResponseI;
import com.freshdirect.cms.application.ContentTypeServiceI;
import com.freshdirect.cms.application.service.AbstractContentService;

/**
 * {@link com.freshdirect.cms.application.ContentServiceI} implementation
 * that exposes entities mapped by Hibernate.
 * 
 * @FIXME parent relationship handling
 * 
 * @author lpekowsky
 */
public class HibernateContentService extends AbstractContentService implements HibernateContentServiceI {

	private final SessionFactory sessionFactory;
	private final HibernateTypeService typeService;
	private final Map parentRelationships = new HashMap();
	private final Map classNameCache = new HashMap();

	public HibernateContentService(SessionFactory sessionFactory) {
		typeService = new HibernateTypeService(sessionFactory);
		this.sessionFactory = sessionFactory;
		cacheClassNames(sessionFactory);
		// FIXME restore this
		// buildParentRelationships();
	}

	private void buildParentRelationships() {
		Map allClassMetadata = sessionFactory.getAllClassMetadata();

		for (Iterator it = allClassMetadata.values().iterator(); it.hasNext();) {
			ClassMetadata metaData = (ClassMetadata) it.next();
			Class theClass         = metaData.getMappedClass(EntityMode.POJO); 
			String names[]         = metaData.getPropertyNames();
			Type propertyTypes[]   = metaData.getPropertyTypes();
			
			for(int i=0; i<propertyTypes.length; i++) {
				Type type = propertyTypes[i];
			
				if (type.isCollectionType()) {
					// A one->many relationship
					CollectionType ct           = (CollectionType) type;
					String associatedEntityName = ct.getAssociatedEntityName((SessionFactoryImplementor) sessionFactory);
					int pos = associatedEntityName.lastIndexOf('.');
					
					ContentType ctype = ContentType.get(associatedEntityName.substring(pos+1));
					
					List l = (List) parentRelationships.get(ctype);
					if(l == null) {
						l = new ArrayList();
						parentRelationships.put(ctype,l);
					}
					
					l.add(new OneToManyParentFinder(theClass, HibernateTypeService.getContentType(theClass),names[i],ct));
				} else if (type.isEntityType()) {
					// A one->one relationship
					EntityType et     = (EntityType) type;
					Class childClass  = et.getReturnedClass();
					ContentType ctype = HibernateTypeService.getContentType(childClass);
					
					List l = (List) parentRelationships.get(ctype);
					if(l == null) {
						l = new ArrayList();
						parentRelationships.put(ctype,l);
					}
					
					l.add(new OneToOneParentFinder(theClass,HibernateTypeService.getContentType(theClass),names[i],et));
				} else {
					// A primitive type
					// Nothing we need to do here.
				}
			}
		}
	}

	public ContentNodeI createPrototypeContentNode(ContentKey key) {
		ClassMetadata classMeta = (ClassMetadata) classNameCache.get(key.getType());

		if (classMeta == null) {
			return null;
		}

		Object newObj = classMeta.instantiate(key.getId(), EntityMode.POJO);
		ContentNodeI newNode = new HibernateContentNode(this, key, newObj);

		return newNode;
	}

	public CmsResponseI handle(CmsRequestI request) {
		Session sess = sessionFactory.openSession();
		Transaction tx = sess.beginTransaction();

		for (Iterator i = request.getNodes().iterator(); i.hasNext();) {
			ContentNodeI node = (ContentNodeI) i.next();

			HibernateContentNode hcn = (HibernateContentNode) node;
			sess.merge(hcn.getObject());
		}

		tx.commit();
		sess.close();

		return new CmsResponse();
	}

	private void cacheClassNames(SessionFactory sessionFactory) {
		Map allClassMetadata = sessionFactory.getAllClassMetadata();

		for (Iterator i = allClassMetadata.values().iterator(); i.hasNext();) {
			ClassMetadata classMeta = (ClassMetadata) i.next();
			Class mappedClass = classMeta.getMappedClass(EntityMode.POJO);

			// We do this in a bunch of places, should be put in some util class
			String className = mappedClass.getName();
			int pos1 = className.lastIndexOf('.');
			int pos2 = className.lastIndexOf('$');
			className = className.substring((pos1 > pos2 ? pos1 : pos2) + 1);

			classNameCache.put(ContentType.get(className), classMeta);
		}
	}

	// You don't want to do this.  Really.
	public Set getContentKeys() {
		Set ret = new HashSet();

		for (Iterator it = classNameCache.keySet().iterator(); it.hasNext();) {
			ret.addAll(getContentKeysByType((ContentType) it.next()));
		}

		return ret;
	}

	public Set getContentKeysByType(ContentType type) {
		Set ret = new HashSet();
		ClassMetadata meta = (ClassMetadata) classNameCache.get(type);
		if (meta == null) {
			return Collections.EMPTY_SET;
		}
		Class theClass = meta.getMappedClass(EntityMode.POJO);

		Session sess = sessionFactory.openSession();
		List list = sess.createCriteria(theClass).list();

		for (Iterator it = list.iterator(); it.hasNext();) {
			Object obj = it.next();
			String id = meta.getIdentifier(obj, EntityMode.POJO).toString();
			ContentKey key = new ContentKey(type, id);
			ret.add(new HibernateContentNode(this, key, obj));
		}

		sess.close();

		return ret;
	}

	public Set getParentKeys(ContentKey key) {
		// FIXME restore
		return Collections.EMPTY_SET;
		/*
		// This is a sad waste.  We have the id in the key, but hibernate can't do:
		// sess.createCriteria(parentClass).add(Expression.eq(propertyName, the_id)).list()
		// it needs
		// sess.createCriteria(parentClass).add(Expression.eq(propertyName, the_Object)).list()
		// so we need an extra trip to the database to get the object.  I'll go sit in a 
		// corner and weep now.
		
		HibernateContentNode node = (HibernateContentNode) getContentNode(key);
		
		List parentFinders = (List) parentRelationships.get(key.getType());
		if(parentFinders == null) {
			return null;
		}
		
		Set ret = new HashSet();
		for(Iterator it=parentFinders.iterator();it.hasNext();) {
			ParentFinderI pf = (ParentFinderI) it.next();
			ret.addAll(pf.findParents(node,sessionFactory));
		}
		
		return ret;
		*/
	}

	public ContentNodeI getContentNode(ContentKey key) {
		ContentType type = key.getType();
		String id = key.getId();
		ClassMetadata meta = (ClassMetadata) classNameCache.get(type);
		if (meta == null) {
			return null;
		}
		Class theClass = meta.getMappedClass(EntityMode.POJO);
		Object obj = meta.instantiate(null, EntityMode.POJO);

		meta.setIdentifier(obj, key.getId(), EntityMode.POJO);

		Session sess = sessionFactory.openSession();

		HibernateContentNode node = null;
		try {
			Object object = sess.load(theClass, id);
			node = new HibernateContentNode(this, key, object);
		} catch (ObjectNotFoundException e) {
		}

		sess.close();
		return node;
	}

	public Map getContentNodes(Set keys) {
		Map ret = new HashMap();

		for (Iterator it = keys.iterator(); it.hasNext();) {
			ContentKey key = (ContentKey) it.next();
			ContentNodeI node = getContentNode(key);

			if (node != null) {
				ret.put(key, node);
			}
		}

		return ret;
	}

	public ContentTypeServiceI getTypeService() {
		return typeService;
	}

	public void save(HibernateContentNode node) {
		Session sess = sessionFactory.openSession();
		Transaction tx = sess.beginTransaction();

		sess.merge(node.getObject());
		tx.commit();
		sess.close();
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

}