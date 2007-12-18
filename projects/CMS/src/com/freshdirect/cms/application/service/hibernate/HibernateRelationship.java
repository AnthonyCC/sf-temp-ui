package com.freshdirect.cms.application.service.hibernate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.SessionFactory;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.EnumCardinality;
import com.freshdirect.cms.RelationshipDefI;
import com.freshdirect.cms.RelationshipI;

/**
 * {@link com.freshdirect.cms.RelationshipI} implementation backed by a property
 * of a Hibernate entity.
 * 
 * @FIXME need to add the concept of object-holding relationships (vs. key-lists) to correctly map everything
 *
 * @author lpekowsky
 */
class HibernateRelationship extends HibernateAttribute implements RelationshipI {

	/**
	 * @param node hibernated node, never null
	 * @param relDef relationship definition, never null
	 */
	public HibernateRelationship(HibernateContentNode node, RelationshipDefI relDef) {
		super(node, relDef);
		//		Object o = super.getValue();
		//		if (o instanceof HibernateProxy) {
		//			Hibernate.initialize(o);
		//		}
	}

	public Object getValue() {
		Object o = super.getValue();
		if (o == null) {
			return null;
		}

		SessionFactory sessionFactory = node.getContentService().getSessionFactory();

		if (o instanceof Collection) {
			Collection c = (Collection) o;
			List l = new ArrayList(c.size());
			for (Iterator i = c.iterator(); i.hasNext();) {
				l.add(HibernateUtil.getKey(sessionFactory, i.next()));
			}
			return l;
		}

		return HibernateUtil.getKey(sessionFactory, o);
	}

	public void setValue(Object o) {
		if (o == null) {
			super.setValue(null);
			return;
		}

		HibernateContentServiceI hibernateService = node.getContentService();

		Object objectValue;
		if (EnumCardinality.ONE.equals(getDefinition().getCardinality())) {
			ContentKey key = (ContentKey) o;
			HibernateContentNode node = (HibernateContentNode) hibernateService.getContentNode(key);
			objectValue = node.getObject();
		} else {
			List keys = (List) o;
			Map nodes = hibernateService.getContentNodes(new HashSet(keys));
			List l = new ArrayList(nodes.size());
			for (Iterator i = keys.iterator(); i.hasNext();) {
				HibernateContentNode node = (HibernateContentNode) i.next();
				l.add(node.getObject());
			}
			objectValue = l;
		}

		super.setValue(objectValue);
	}

}
