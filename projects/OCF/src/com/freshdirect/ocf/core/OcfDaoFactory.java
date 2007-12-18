/**
 * @author ekracoff
 * Created on Apr 15, 2005*/

package com.freshdirect.ocf.core;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.freshdirect.framework.hibernate.HibernateDaoFactory;
import com.freshdirect.ocf.impl.hibernate.ActionDao;
import com.freshdirect.ocf.impl.hibernate.CampaignDao;
import com.freshdirect.ocf.impl.hibernate.EmailStatLineDao;
import com.freshdirect.ocf.impl.hibernate.FlightDao;

public class OcfDaoFactory extends HibernateDaoFactory {

	private final static String HIBERNATE_CONFIG_URL = "classpath:/com/freshdirect/ocf/hibernate.cfg.xml";

	private static OcfDaoFactory instance = null;
	
	/**
	 *  The hibernate session factory.
	 */
	private SessionFactory sessionFactory;
	
	/**
	 *  Holds a thread-single instance of Hibernate Session
	 */
	private ThreadLocal threadSession = new ThreadLocal();

	/**
	 *  Holds a thread-single instance of Hibernate Transaction
	 */
	private ThreadLocal threadTransaction = new ThreadLocal();
	
	private FlightDao 			flightDao;
	private CampaignDao 			campaignDao;
	private ActionDao 			actionDao;
	private EmailStatLineDao    emailStatLineDao;

	private OcfDaoFactory() {
		sessionFactory = HibernateDaoFactory.getSessionFactory(HIBERNATE_CONFIG_URL);
		
		flightDao        = new FlightDao(this);
		campaignDao      = new CampaignDao(this);
		actionDao        = new ActionDao(this);
		emailStatLineDao = new EmailStatLineDao(this);
	}

	public static synchronized OcfDaoFactory getInstance() {
		if (instance == null) {
			instance = new OcfDaoFactory();
		}
		
		return instance;
	}
	
	/**
	 *  Get the hibernate session factory.
	 * 
	 *  @return the listadmin data access object.
	 */
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	
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
	 * @return Returns the actionDao.
	 */
	public ActionDao getActionDao() {
		return actionDao;
	}
	
	/**
	 * @return Returns the campaignDao.
	 */
	public CampaignDao getCampaignDao() {
		return campaignDao;
	}
	
	/**
	 * @return Returns the flightDao.
	 */
	public FlightDao getFlightDao() {
		return flightDao;
	}
	
	/**
	 * @return Returns the emailStatLineDao.
	 */
	public EmailStatLineDao getEmailStatLineDao() {
		return emailStatLineDao;
	}
}