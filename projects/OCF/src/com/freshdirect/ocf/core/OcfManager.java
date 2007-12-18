/**
 * @author ekracoff
 * Created on Apr 15, 2005*/

package com.freshdirect.ocf.core;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;

import com.freshdirect.framework.conf.FDRegistry;
import com.freshdirect.framework.hibernate.HibernateDaoSupport;
import com.freshdirect.framework.hibernate.UnitOfWork;
import com.freshdirect.ocf.core.OcfTableI.Row;
import com.freshdirect.ocf.silverpop.SilverpopServiceI;

public class OcfManager {

	private SilverpopServiceI SERVICE = (SilverpopServiceI) FDRegistry.getInstance().getService(SilverpopServiceI.class);

	private static OcfManager instance = null;
	
	private OcfDaoFactory daoFactory;

	/**
	 *  Constructor.
	 */
	private OcfManager() {
		daoFactory = OcfDaoFactory.getInstance();
	}
	
	public static synchronized OcfManager getInstance() {
		if (instance == null) {
			instance = new OcfManager();
		}
		
		return instance;
	}

	public Campaign getCampaign(String campaignId) {
		return daoFactory.getCampaignDao().getCampaign(campaignId);
	}

	public void saveOrUpdateCampaign(final Campaign campaign) {
		new UnitOfWork() {
			protected void perform() {
				daoFactory.getCampaignDao().saveCampaign(campaign);
			}
			protected HibernateDaoSupport getDaoSupport() {
				return daoFactory.getCampaignDao();
			}
		}.execute();
	}

	public void saveOrUpdateAction(final ActionI action) {
		new UnitOfWork() {
			protected void perform() {
				daoFactory.getActionDao().saveOrUpdateAction(action);
			}
			protected HibernateDaoSupport getDaoSupport() {
				return daoFactory.getActionDao();
			}
		}.execute();
	}

	public Flight getFlight(String flightId) {
		return daoFactory.getFlightDao().loadFlight(flightId);
	}

	public void runFlight(String flightId) throws OcfRecoverableException {
		Flight flight = getFlight(flightId);
		OcfTableI table;

		table = flight.getListGenerator().getList();

		if (!flight.isMultipleContact()) {
			enforceMultipleContactRule(table, daoFactory.getFlightDao().getAffectedCustomers(flightId));
		}

		Set customers = createRunCustSet(table);
		
		RunLog run = new RunLog();
		run.setFlightId(flightId);
		run.setCustomers(customers);
		run.setTimestamp(new Date());

		logRun(run, RunStatus.RUNNING);

		boolean updatedStatus = false;
		try {
			for (Iterator i = flight.getActions().iterator(); i.hasNext();) {
				ActionI command = (ActionI) i.next();
				command.execute(table);
			}
		} catch (OcfRecoverableException e) {
			logRun(run, RunStatus.RETRY);
			updatedStatus = true;
			throw e;
		} finally {
			if (!updatedStatus) {
				logRun(run, RunStatus.FINISHED);
			}
		}
	}

	public List getFlightsBySchedule(String scheduleName) {
		return daoFactory.getFlightDao().loadFlightsBySchedule(scheduleName);
	}

	public String[] getSchedules() throws SchedulerException {
		SchedulerFactory schedFact = new StdSchedulerFactory();
		Scheduler sched = schedFact.getScheduler();
		return sched.getJobNames("OCF_SCHED");
	}

	//	remove previously run customers from table
	private void enforceMultipleContactRule(OcfTableI table, Map affectedCustomers) {
		for (Iterator i = table.getRows().iterator(); i.hasNext();) {
			OcfTableI.Row row = (OcfTableI.Row) i.next();
			String customerId = row.getValues()[0].toString();

			RunCust runCust = (RunCust) affectedCustomers.get(customerId);
			if (runCust != null) {
				i.remove();
			}

			if (!SERVICE.isAllowExternal()) {
				int emailCol = table.getColumnPosByName("EMAIL");
				String email = emailCol > 0 ? (String) row.getValues()[emailCol] : null;
				if (email != null
					&& email.indexOf("@freshdirect.com") < 0
					&& !email.equals("evankracoff@yahoo.com")
					&& !email.equals("hctai@yahoo.com")) {

					i.remove();
				}
			}

		}
	}

	private void logRun(final RunLog run, String status) {
		run.setStatus(status);
		new UnitOfWork() {
			protected void perform() {
				daoFactory.getFlightDao().logFlightRun(run);
			}
			protected HibernateDaoSupport getDaoSupport() {
				return daoFactory.getFlightDao();
			}
		}.execute();
	}

	private Set createRunCustSet(OcfTableI table) {
		Set set = new HashSet();
		for (Iterator i = table.getRows().iterator(); i.hasNext();) {
			Row row = (Row) i.next();
			set.add(new RunCust(row.getValues()[0].toString(), row.getValues().length > 1 ? row.getValues()[1].toString() : null));
		}
		return set;
	}

}