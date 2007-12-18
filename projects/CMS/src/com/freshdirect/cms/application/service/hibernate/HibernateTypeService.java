/*
 * Created on Jul 5, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.freshdirect.cms.application.service.hibernate;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.hibernate.EntityMode;
import org.hibernate.MappingException;
import org.hibernate.SessionFactory;
import org.hibernate.engine.SessionFactoryImplementor;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.persister.entity.BasicEntityPersister;
import org.hibernate.type.AssociationType;
import org.hibernate.type.Type;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.ContentTypeDefI;
import com.freshdirect.cms.EnumAttributeType;
import com.freshdirect.cms.EnumCardinality;
import com.freshdirect.cms.application.ContentTypeServiceI;
import com.freshdirect.cms.meta.AttributeDef;
import com.freshdirect.cms.meta.ContentTypeDef;
import com.freshdirect.cms.meta.RelationshipDef;
import com.freshdirect.cms.util.CollectionUtil;

/**
 * {@link com.freshdirect.cms.application.ContentTypeServiceI} implementation
 * based on Hibernate mapping metadata.
 * 
 * @author lpekowsky
 */
public class HibernateTypeService implements ContentTypeServiceI {

	private final Map defsByType = new HashMap();
	private final Map inheritance = new HashMap();

	/**
	 * Create the type service by exposing types in the given session factory.
	 * 
	 * @param sessionFactory Hibernate session factory, never null
	 */
	public HibernateTypeService(SessionFactory sessionFactory) {
		Map allClassMetadata = sessionFactory.getAllClassMetadata();

		for (Iterator i = allClassMetadata.values().iterator(); i.hasNext();) {
			ClassMetadata classMeta = (ClassMetadata) i.next();
			String superClass = ((BasicEntityPersister)classMeta).getMappedSuperclass();

			if(superClass != null){
				CollectionUtil.addToMapOfSets(inheritance, superClass, classMeta);
			}			
		}
		
		for(Iterator iter = allClassMetadata.values().iterator();iter.hasNext();){
			ClassMetadata classMeta = (ClassMetadata) iter.next();
			ContentTypeDefI def = introspect(sessionFactory, classMeta);
			defsByType.put(def.getType(), def);
		}

		System.out.println("HibernateTypeService mapped " + defsByType);
	}

	static ContentType getContentType(String className) {
		int pos1 = className.lastIndexOf('.');
		int pos2 = className.lastIndexOf('$');
		className = className.substring((pos1 > pos2 ? pos1 : pos2) + 1);

		return ContentType.get(className);
	}

	static ContentType getContentType(Class mappedClass) {
		return getContentType(mappedClass.getName());
	}

	private ContentTypeDefI introspect(SessionFactory sessionFactory, ClassMetadata metaData) {
		Class mappedClass = metaData.getMappedClass(EntityMode.POJO);

		ContentType type = getContentType(mappedClass);

		ContentTypeDef contDef = new ContentTypeDef(this, type, type.getName());

		Type[] propertyTypes = metaData.getPropertyTypes();
		String[] propertyNames = metaData.getPropertyNames();

		for (int i = 0; i < propertyNames.length; i++) {
			String attrName = propertyNames[i];
			Type attrType = propertyTypes[i];

			if (attrType.isEntityType()) {

				RelationshipDef def = new RelationshipDef(
					attrName,
					attrName,
					false,
					false,
					false,
					attrType.isMutable(),
					EnumCardinality.ONE);
				Class relClass = attrType.getReturnedClass();
				def.addContentType(getContentType(relClass));

				contDef.addAttributeDef(def);

			} else if (attrType.isCollectionType()) {

				try {

					//				if (!attrType.isAssociationType()) {
					//					System.err.println("HibernateTypeService.introspect() unsupported collection: " + attrType);
					//					continue;
					//				}

					RelationshipDef def = new RelationshipDef(
						attrName,
						attrName,
						false,
						false,
						true,
						attrType.isMutable(),
						EnumCardinality.MANY);

					System.out.println("HibernateTypeService.introspect() " + attrType.getClass().getName());
					AssociationType t = ((AssociationType) attrType);
					String assocClass = t.getAssociatedEntityName((SessionFactoryImplementor) sessionFactory);
					
					if(inheritance.containsKey(assocClass)){
						Set subclasses = (Set)inheritance.get(assocClass);
						for(Iterator subIter = subclasses.iterator(); subIter.hasNext();){
							ClassMetadata subclass = (ClassMetadata) subIter.next();
							def.addContentType(getContentType(subclass.getEntityName()));
							contDef.addAttributeDef(def);
						}
					}else {
						def.addContentType(getContentType(assocClass));
						contDef.addAttributeDef(def);
					}
				} catch (MappingException e) {
					System.err.println("HibernateTypeService.introspect() unsupported collection: " + attrType);
				}

			} else {

				// A primative relationship	
				Class attrClass = attrType.getReturnedClass();
				EnumAttributeType eType = null;
				if (String.class.equals(attrClass)) {
					eType = EnumAttributeType.STRING;
				} else if (Integer.class.equals(attrClass)) {
					eType = EnumAttributeType.INTEGER;
				} else if (Double.class.equals(attrClass)) {
					eType = EnumAttributeType.DOUBLE;
				} else if (Boolean.class.equals(attrClass)) {
					eType = EnumAttributeType.BOOLEAN;
				} else {
					System.err.println("HibernateTypeService.introspect() unsupported primitive type: " + attrClass);
					continue;
				}

				AttributeDef def = new AttributeDef(eType, attrName, attrName);
				contDef.addAttributeDef(def);
			}

		}

		return contDef;
	}

	public Set getContentTypes() {
		return Collections.unmodifiableSet(defsByType.keySet());
	}

	public Set getContentTypeDefinitions() {
		return Collections.unmodifiableSet(defsByType.entrySet());
	}

	public ContentTypeDefI getContentTypeDefinition(ContentType type) {
		return (ContentTypeDefI) defsByType.get(type);
	}
	
	public String generateUniqueId(ContentType type) {
		// TODO implement
		return null;
	}

	public ContentKey generateUniqueContentKey(ContentType type) throws UnsupportedOperationException {
		// TODO implement
		return null;
	}

}
