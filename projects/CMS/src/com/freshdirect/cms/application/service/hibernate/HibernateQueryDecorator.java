package com.freshdirect.cms.application.service.hibernate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.Factory;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.application.service.ContentDecoratorI;
import com.freshdirect.cms.application.service.query.CmsQueryTypes;
import com.freshdirect.framework.util.LazyInstanceProxy;

/**
 * {@link com.freshdirect.cms.application.service.ContentDecoratorI}
 * implementation that exposes the results of queries contained in
 * content nodes of type <code>CmsQuery</code> where the <code>language</code>
 * attribute is "HQL".
 * 
 * @see com.freshdirect.cms.application.service.query.DbQueryDecorator
 */
public class HibernateQueryDecorator implements ContentDecoratorI, Serializable {

	private final HibernateContentServiceI hibernateService;

	public HibernateQueryDecorator(HibernateContentServiceI hibernateService) {
		this.hibernateService = hibernateService;
	}

	public ContentNodeI decorateNode(ContentNodeI node) {
		ContentType type = node.getKey().getType();
		if (!(type.equals(CmsQueryTypes.QUERY))) {
			return null;
		}
		String language = (String) node.getAttribute("language").getValue();
		if (!"HQL".equals(language)) {
			return null;
		}

		String query = (String) node.getAttribute("script").getValue();

		List results = (List) LazyInstanceProxy.newInstance(List.class, new QueryFactory(query));

		ContentNodeI clone = node.copy();
		clone.getAttribute("results").setValue(results);
		return clone;
	}

	private class QueryFactory implements Factory, Serializable {
		private final String query;

		private QueryFactory(String query) {
			this.query = query;
		}

		public Object create() {
			SessionFactory sessionFactory = hibernateService.getSessionFactory();
			Session session = sessionFactory.openSession();
			Query q = session.createQuery(query);
			List objects = q.list();
			List keys = new ArrayList(objects.size());
			for (Iterator i = objects.iterator(); i.hasNext();) {
				Object o = i.next();
				ContentKey key = HibernateUtil.getKey(sessionFactory, o);
				keys.add(key);
			}
			session.close();
			return keys;
		}
	}

}