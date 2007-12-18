package com.freshdirect.cms.application.service.hibernate;

import org.hibernate.EntityMode;
import org.hibernate.Hibernate;
import org.hibernate.proxy.HibernateProxy;

import com.freshdirect.cms.AttributeDefI;
import com.freshdirect.cms.AttributeI;
import com.freshdirect.cms.ContentNodeI;

/**
 * {@link com.freshdirect.cms.AttributeI} implementation backed by a property
 * of a Hibernate entity.
 * 
 * @author lpekowsky
 */
class HibernateAttribute implements AttributeI {

	protected final HibernateContentNode node;
	
	protected final AttributeDefI attrDef;

	/**
	 * @param node hibernated node, never null
	 * @param attrDef attribute definition, never null
	 */
	public HibernateAttribute(HibernateContentNode node, AttributeDefI attrDef) {
		this.node = node;
		this.attrDef = attrDef;
		Object o = getValue();		
		if (o instanceof HibernateProxy) {
			Hibernate.initialize(o);
		}
	}

	public String getName() {
		return this.attrDef.getName();
	}

	public Object getValue() {
		return node.getClassMetadata().getPropertyValue(node.getObject(), getName(), EntityMode.POJO);
	}

	public void setValue(Object o) {
		node.getClassMetadata().setPropertyValue(node.getObject(), getName(), o, EntityMode.POJO);
	}

	public AttributeDefI getDefinition() {
		return this.attrDef;
	}

	public ContentNodeI getContentNode() {
		return this.node;
	}

	public String toString() {
		return "[" + attrDef.getName() + ":" + getValue() + "]";
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;

		if (obj == null)
			return false;

		if (obj instanceof HibernateAttribute) {
			HibernateAttribute attr = (HibernateAttribute) obj;
			if (!attr.getName().equals(this.getName())) {
				return false;
			}
			Object val = attr.getValue();
			Object mine = this.getValue();
			if (val == null) {
				return mine == null;
			}
			return val.equals(mine);
		}
		return false;
	}
}
