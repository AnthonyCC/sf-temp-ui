package com.freshdirect.framework.hibernate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.freshdirect.framework.conf.ResourceUtil;

public abstract class HibernateDaoSupport {

	/**
	 *  Return a hibernate session factory to use for accessing persistent storage.
	 * 
	 *  @return a hibernate session factory
	 *  @see ResourceUtil#getResource(String)
	 */
	protected abstract SessionFactory getSessionFactory();

	/**
	 * Return a HibernateDaoFactory object that is specific to the package.
	 * The main reason for this object is to provide a thread-local, package-specific
	 * hibernate session and transaction.
	 */
	protected abstract HibernateDaoFactory getDaoFactory();
	
	/**
	 *  Returns the ThreadLocal Session instance. Lazy initialize the
	 *  <code>SessionFactory</code> if needed.
	 * 
	 *  @throws HibernateException
	 */
	public Session currentSession() {
		return getDaoFactory().currentSession();
	}

	/**
	 * Close the single hibernate session instance.
	 * 
	 * @throws HibernateException
	 */
	public void closeSession() {
		getDaoFactory().closeSession();
	}

	/**
	 *  Begin a hiberante transaction.
	 * 
	 *  @throws HibernateException
	 *  @see #currentSession(String)
	 */
	public void beginTransaction() {
		getDaoFactory().beginTransaction();
	}

	/**
	 * Commit a hibernate transaction
	 * 
	 * @throws HibernateException
	 */
	public void commitTransaction() {
		getDaoFactory().commitTransaction();
	}

	/**
	 * Rollback a hibernate transaction
	 * 
	 * @throws HibernateException
	 */
	public void rollbackTransaction() {
		getDaoFactory().rollbackTransaction();
	}

	/**
	 *  Attach an object to the current hibernate session.
	 * 
	 *  @param sesssionFactoryUrl the resource URL pointing to the session
	 *         factory configuration that creates the session, if needed.
	 *  @param obj
	 */
	public void reattach(String sessionFactoryUrl, Object obj) {
		getDaoFactory().reattach(sessionFactoryUrl, obj);
	}

	
	protected Object load(Class clazz, Serializable id) {
		Session sess = currentSession();
		return sess.load(clazz, id);
	}

	protected List query(String query) {
		Session sess = currentSession();
		return sess.createQuery(query).list();
	}
	
	protected int executeQuery(String query) {
		Session sess = currentSession();
		return sess.createQuery(query).executeUpdate();
	}

	protected List iterate(String query) {
		List l = new ArrayList();
		Session sess = currentSession();
		Iterator i = sess.createQuery(query).iterate();
		while (i.hasNext()) {
			l.add(i.next());
		}
		return l;
	}

	protected void saveOrUpdate(Object entity) {
		Session sess = currentSession();
		sess.saveOrUpdate(entity);
	}

	protected void save(Object entity) {
		Session sess = currentSession();
		sess.save(entity);
	}

	protected void delete(Object entity) {
		Session sess = currentSession();
		sess.delete(entity);
	}

}