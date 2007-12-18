package com.freshdirect.cms.application.service.hibernate;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.SerializationUtils;
import org.hibernate.Hibernate;
import org.hibernate.metadata.ClassMetadata;

import com.freshdirect.cms.AttributeDefI;
import com.freshdirect.cms.AttributeI;
import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.ContentTypeDefI;
import com.freshdirect.cms.RelationshipDefI;
import com.freshdirect.cms.application.ContentTypeServiceI;
import com.freshdirect.cms.node.ContentNodeUtil;

/**
 * {@link com.freshdirect.cms.ContentNodeI} implementation backed by Hibernate entity.
 * 
 * @author lpekowsky
 */
class HibernateContentNode implements ContentNodeI {

	private final HibernateContentServiceI contentService;

	private final ContentKey key;

	private final Map attributes = new HashMap();
	
	private final Object hibernatedObject;

	/**
	 * @param contentService
	 * @param key
	 * @param hibernatedObject
	 */
	public HibernateContentNode(HibernateContentServiceI contentService, ContentKey key, Object hibernatedObject) {
		if (key == null) {
			throw new IllegalArgumentException("ContentKey cannot be null");
		}
		this.key = key;
		this.contentService = contentService;
		this.hibernatedObject = hibernatedObject;
		this.initializeAttributes(contentService.getTypeService());
	}

	private void initializeAttributes(ContentTypeServiceI typeService) {
		ContentTypeDefI def = typeService.getContentTypeDefinition(key.getType());
		if (def == null) {
			throw new IllegalArgumentException("No type definition for " + key + " in " + typeService);
		}
		for (Iterator i = def.getSelfAttributeDefs().iterator(); i.hasNext();) {
			AttributeDefI atrDef = (AttributeDefI) i.next();
			AttributeI atr;
			if (atrDef instanceof RelationshipDefI) {
				atr = new HibernateRelationship(this, (RelationshipDefI) atrDef);
			} else {
				atr = new HibernateAttribute(this, atrDef);
			}
			attributes.put(atrDef.getName(), atr);
		}
	}

	Object getObject() {
		return hibernatedObject;
	}

	HibernateContentServiceI getContentService() {
		return contentService;
	}

	ClassMetadata getClassMetadata() {
		Class klass = Hibernate.getClass(getObject());
		return getContentService().getSessionFactory().getClassMetadata(klass);
	}

	//
	// core
	//
	
	public ContentKey getKey() {
		return key;
	}

	public ContentTypeDefI getDefinition() {
		return contentService.getTypeService().getContentTypeDefinition(getKey().getType());
	}

	//
	// convenience
	//

	public Set getChildKeys() {
		return ContentNodeUtil.getChildKeys(this);
	}

	public String getLabel() {
		return ContentNodeUtil.getLabel(this);
	}

	//
	// attributes
	//

	public AttributeI getAttribute(String name) {
		return (AttributeI) attributes.get(name);
	}

	public Map getAttributes() {
		return this.attributes;
	}

	//
	// infrastructure
	//

	private boolean delete = true;

	public void setDelete(boolean b) {
		delete = b;
	}

	public boolean isDelete() {
		return delete;
	}

	public ContentNodeI copy() {
		return (ContentNodeI) SerializationUtils.clone(this);
	}

	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj instanceof ContentNodeI) {
			ContentNodeI node = (ContentNodeI) obj;
			return key.equals(node.getKey());
		}
		return false;
	}

	public int hashCode() {
		return this.key.hashCode();
	}

	public String toString() {
		return "ContentNode[" + this.key + "]";
	}

}