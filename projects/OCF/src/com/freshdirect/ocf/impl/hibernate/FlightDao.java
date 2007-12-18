/**
 * @author ekracoff
 * Created on Apr 27, 2005*/

package com.freshdirect.ocf.impl.hibernate;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.freshdirect.framework.conf.ResourceUtil;
import com.freshdirect.framework.hibernate.HibernateDaoFactory;
import com.freshdirect.framework.hibernate.HibernateDaoSupport;
import com.freshdirect.listadmin.db.StoredQuery;
import com.freshdirect.ocf.core.Flight;
import com.freshdirect.ocf.core.RunCust;
import com.freshdirect.ocf.core.RunLog;
import com.freshdirect.ocf.core.RunStatus;


public class FlightDao extends HibernateDaoSupport{
	
	/**
	 *  The hibernate dao factory used to create this DAO object.
	 */
	private HibernateDaoFactory daoFactory;
		
	/**
	 *  Constructor.
	 * 
	 *  @param daoFactory the hibernate dao factory used to create this dao object.
	 */
	public FlightDao(HibernateDaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}
		
	/**
	 *  Return a hibernate session factory to use for accessing persistent storage.
	 * 
	 *  @return a hibernate session factory
	 *  @see ResourceUtil#getResource(String)
	 */
	protected SessionFactory getSessionFactory()
	{
		return daoFactory.getSessionFactory();
	}

	/**
	 * Return a HibernateDaoFactory object that is specific to the package.
	 * The main reason for this object is to provide a thread-local, package-specific
	 * hibernate session and transaction.
	 */
	protected HibernateDaoFactory getDaoFactory() {
		return daoFactory;
	}	

	public Flight loadFlight(String flightId){
		return (Flight) this.load(Flight.class, flightId);
	}
	
	public List loadFlightsBySchedule(String scheduleName){
		return query("from Flight f where f.campaign.startDate <= current_timestamp() and f.campaign.endDate >= current_timestamp() and f.scheduleName = '" + scheduleName.toUpperCase() + "'" );
	}
	
	public void logFlightRun(RunLog run){
		this.saveOrUpdate(run);
	}
	
	/** @return Map customerId to RunCust */ 
	public Map getAffectedCustomers(String flightId){
		Session sess = currentSession();
		Query q = sess.createQuery("select distinct elements(r.customers) from RunLog r where r.flightId=:flightId and r.status<>:status");
		q.setParameter("flightId", flightId);
		q.setParameter("status", RunStatus.RETRY);
		
		Map map = new HashMap();
		for(Iterator i = q.list().iterator(); i.hasNext();){
			RunCust runCust = (RunCust)i.next();
			map.put(runCust.getCustomerId(), runCust);
		}
		
		return map;
	}
	
	public void saveOrUpdateFlight(final Flight flight){
		Session sess = currentSession();
		if(flight.getListGenerator().getStoredQuery() != null) {
			StoredQuery query = flight.getListGenerator().getStoredQuery();
			if(query.getStoredQueryId() != null) {
				sess.merge(query);
			} else {
				sess.saveOrUpdate(query);
			}
		}
		if(flight.getListGenerator().getId() != null) {
			sess.merge(flight.getListGenerator());
		} else {
			sess.saveOrUpdate(flight.getListGenerator());
		}
		sess.merge(flight);
	}

}
