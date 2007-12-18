/**
 * @author ekracoff
 * Created on May 24, 2005*/

package com.freshdirect.ocf.quartz;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.quartz.JobExecutionContext;

import com.freshdirect.framework.hibernate.HibernateDaoSupport;
import com.freshdirect.ocf.core.Flight;
import com.freshdirect.ocf.core.OcfDaoFactory;
import com.freshdirect.ocf.core.OcfManager;
import com.freshdirect.ocf.impl.hibernate.FlightDao;


public class ScheduledJob extends FDJob{

	/**
	 *  Return a flight DAO support object.
	 * 
	 *  @return a flight DAO support object used by the job.
	 */
	public FlightDao getFlightDao() {
		return OcfDaoFactory.getInstance().getFlightDao();
	}

	/**
	 *  Return a hibernate DAO support object.
	 * 
	 *  @return a hibernate DAO support object used by the job.
	 */
	public HibernateDaoSupport getDaoSupport() {
		return getFlightDao();
	}

	public void run(JobExecutionContext context) {
		List exceptions = new ArrayList();
		
		String name = context.getJobDetail().getName();
		
		List flights = OcfManager.getInstance().getFlightsBySchedule(name);
		
		for(Iterator i = flights.iterator(); i.hasNext();){
			Flight flight = (Flight) i.next();
			LOGGER.info("Running flight " + flight.getName() + "...");
			
			try {
				OcfManager.getInstance().runFlight(flight.getId());
			} catch (Exception e) {
				exceptions.add(new FlightError(flight, e));
				e.printStackTrace();
				continue;
			}
		}
		
		if(exceptions.size() > 0){
			sendMail(createMessage(exceptions, flights.size(), name));
		}
		
	}
	
	private String createMessage(List exceptions, int totalFlights, String jobName){
		StringBuffer buff = new StringBuffer();
		buff.append("Jobs failed during run of ").append(jobName).append("\n");
		buff.append("Of a total of ").append(totalFlights).append(" flights run, ").append(exceptions.size()).append(" had errors").append("\n\n");
		
		for(Iterator i = exceptions.iterator();i.hasNext();){
			FlightError error = (FlightError)i.next();
			
			buff.append(error.flight.getId()).append("\t").append(error.flight.getName()).append("\n");
			buff.append(stack2string(error.exception)).append("\n\n");
		}
		
		return buff.toString();
	}
	
	private class FlightError{
		public Flight flight;
		public Exception exception;
		
		public FlightError(Flight flight, Exception exception){
			this.flight = flight;
			this.exception = exception;
		}
	}


}
