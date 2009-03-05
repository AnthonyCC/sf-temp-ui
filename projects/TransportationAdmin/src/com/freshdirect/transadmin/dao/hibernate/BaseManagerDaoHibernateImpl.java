package com.freshdirect.transadmin.dao.hibernate;

import java.util.Collection;
import java.util.Iterator;

import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.freshdirect.transadmin.dao.BaseManagerDaoI;

public class BaseManagerDaoHibernateImpl extends HibernateDaoSupport implements BaseManagerDaoI {

	public void removeEntity(Collection employees) throws DataAccessException  {
		getHibernateTemplate().deleteAll(employees);

	}

	public void saveEntity(Object entity) throws DataAccessException {
		getHibernateTemplate().merge(entity);
		getHibernateTemplate().flush();
	}
	public void saveEntityEx(Object entity)throws DataAccessException {
		getHibernateTemplate().save(entity);
	
	}

	public void saveEntityList(Collection entity) throws DataAccessException {
		getHibernateTemplate().saveOrUpdateAll(entity);
	}
	
	public Collection getDataList(String dataTable) throws DataAccessException {

		return (Collection) getHibernateTemplate().find("from " + dataTable);
	}

	public Object getEntityById(String table, String keyCol, String id) throws DataAccessException {

		StringBuffer strBuf = new StringBuffer();
		strBuf.append("from ").append(table).append(" tb where tb.")
						.append(keyCol).append("='").append(id).append("'");
		Collection collection = this.getHibernateTemplate().find(strBuf.toString());
		if(collection==null || collection.size()==0) return null;
		Iterator iterator = collection.iterator();
		return iterator.next();
	}
}
