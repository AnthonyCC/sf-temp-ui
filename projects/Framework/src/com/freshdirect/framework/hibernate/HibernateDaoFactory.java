package com.freshdirect.framework.hibernate;

import java.util.HashMap;
import java.util.Map;

import org.apache.hivemind.Resource;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import com.freshdirect.framework.conf.ResourceUtil;

/**
 * Configures and provides access to Hibernate sessions, tied to the current
 * thread of execution. Follows the Thread Local Session pattern, see:
 * 
 * <blockquote><pre>
 *     <a href="http://hibernate.org/42.html">http://hibernate.org/42.html</a>
 * </pre></blockquote> 
 */
public abstract class HibernateDaoFactory {

	private static Map sessionFactoryMap = new HashMap();
		
	/**
	 *  Holds a thread-single instance of Hibernate Session
	 */
	private ThreadLocal threadSession = new ThreadLocal();

	/**
	 *  Holds a thread-single instance of Hibernate Transaction
	 */
	private ThreadLocal threadTransaction = new ThreadLocal();
	
	/**
	 *  Get a session factory, based on a session configuration resource URL.
	 * 
	 *  @param sesssionFactoryUrl the resource URL pointing to the session
	 *         factory configuration
	 *  @return a session factory configured by the supplied configuration
	 *  @see ResourceUtil#getResource(String) 
	 */
	public static synchronized SessionFactory getSessionFactory(String sessionFactoryUrl) {
		SessionFactory sf;
		
		if (!sessionFactoryMap.containsKey(sessionFactoryUrl)) {
			Resource		   resource = ResourceUtil.getResource(sessionFactoryUrl);		
			Configuration  cfg      = new Configuration().configure(resource.getResourceURL());
			
			sf = cfg.buildSessionFactory();
			sessionFactoryMap.put(sessionFactoryUrl, sf);
		} else {
			sf = (SessionFactory) sessionFactoryMap.get(sessionFactoryUrl);
		}
		
		return sf;
	}

	/**
	 *  Set a session factory for on a session configuration resource URL.
	 *  This overwrides any existing session factories for that configuration.
	 *  For when you really, really need to manually construct a 
	 *  configuration (ie, for testing, see ListAdminTests)
	 * 
	 *  @param sesssionFactoryUrl the resource URL pointing to the session
	 *         factory configuration
	 *  @param factory the session factory to associate with the supplied
	 *         config URL
	 */
	public static void setSessionFactory(String sessionFactoryUrl, SessionFactory factory) {
		sessionFactoryMap.put(sessionFactoryUrl, factory);
	}

	/**
	 *  Get the hibernate session factory.
	 * 
	 *  @return the hibernate session factory
	 */
	public abstract SessionFactory getSessionFactory();
	
	/**
	 * Return the thread-local object that stores a hibernate session,
	 * that is shared among transactions.
	 * 
	 * @return a thread local object holding a hibernate Session object.
	 */
	public Session getThreadSession() {
		return (Session) threadSession.get();
	}
	
	/**
	 * Set the thread-local object that stores a hibernate session,
	 * that is shared among transactions.
	 * 
	 * @param session the thread local hibernate Session object.
	 */
	public void setThreadSession(Session session) {
		threadSession.set(session);
	}

	/**
	 * Return the thread-local object that stores a hibernate transaction.
	 * 
	 * @return a thread local object holding a hibernate Transaction.
	 */
	public Transaction getThreadTransaction() {
		return (Transaction) threadTransaction.get();
	}
	
	/**
	 * Set the the thread-local object that stores a hibernate transaction.
	 * 
	 * @param transaction a thread local object holding a hibernate Transaction.
	 */
	public void setThreadTransaction(Transaction transaction) {
		threadTransaction.set(transaction);
	}

	/**
	 *  Returns the ThreadLocal Session instance. Lazy initialize the
	 *  <code>SessionFactory</code> if needed.
	 * 
	 *  @throws HibernateException
	 */
	public Session currentSession() {
		Session session = getThreadSession();

		if (session == null) {
			session = getSessionFactory().openSession();
			setThreadSession(session);
		}

		return session;
	}

	/**
	 * Close the single hibernate session instance.
	 * 
	 * @throws HibernateException
	 */
	public void closeSession() {

		Session session = getThreadSession();
		setThreadSession(null);
		rollbackTransaction();
		if (session != null && session.isOpen()) {
			session.close();
		}
	}

	/**
	 *  Begin a hiberante transaction.
	 * 
	 *  @throws HibernateException
	 *  @see #currentSession(String)
	 */
	public void beginTransaction() {

		Transaction tx = getThreadTransaction();
		if (tx == null) {
			tx = currentSession().beginTransaction();
			setThreadTransaction(tx);
		}
	}

	/**
	 * Commit a hibernate transaction
	 * 
	 * @throws HibernateException
	 */
	public void commitTransaction() {
		Transaction tx = getThreadTransaction();

		if (tx != null && !tx.wasCommitted() && !tx.wasRolledBack()) {
			tx.commit();
		}
		setThreadTransaction(null);
	}

	/**
	 * Rollback a hibernate transaction
	 * 
	 * @throws HibernateException
	 */
	public void rollbackTransaction() {
		Transaction tx = getThreadTransaction();
		setThreadTransaction(null);
		if (tx != null && !tx.wasCommitted() && !tx.wasRolledBack()) {
			tx.rollback();
		}
		//closeSession();
	}

	/**
	 *  Attach an object to the current hibernate session.
	 * 
	 *  @param sesssionFactoryUrl the resource URL pointing to the session
	 *         factory configuration that creates the session, if needed.
	 *  @param obj
	 */
	public void reattach(String sessionFactoryUrl, Object obj) {
		currentSession().lock(obj, LockMode.NONE);
	}
	
}
