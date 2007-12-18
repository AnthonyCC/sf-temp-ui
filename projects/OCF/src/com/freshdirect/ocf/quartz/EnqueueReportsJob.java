/**
 * @author ekracoff
 * Created on May 18, 2005*/

package com.freshdirect.ocf.quartz;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Category;
import org.quartz.JobExecutionContext;

import com.freshdirect.fdstore.FDRuntimeException;
import com.freshdirect.framework.hibernate.HibernateDaoSupport;
import com.freshdirect.framework.hibernate.UnitOfWork;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.ocf.core.Emailer;
import com.freshdirect.ocf.core.JobDetail;
import com.freshdirect.ocf.core.OcfDaoFactory;
import com.freshdirect.ocf.impl.hibernate.ActionDao;
import com.freshdirect.ocf.silverpop.SilverpopStatistics;

public class EnqueueReportsJob extends FDJob {

	private final static Category LOGGER = LoggerFactory.getInstance(EnqueueReportsJob.class);

	/**
	 *  Return an action DAO support object.
	 * 
	 *  @return an action DAO support object used by the job.
	 */
	public ActionDao getActionDao() {
		return OcfDaoFactory.getInstance().getActionDao();
	}

	/**
	 *  Return a hibernate DAO support object.
	 * 
	 *  @return a hibernate DAO support object used by the job.
	 */
	public HibernateDaoSupport getDaoSupport() {
		return getActionDao();
	}

	public void run(JobExecutionContext arg0) {
		ActionDao dao = getActionDao();
		List emailers = dao.findEmailersToEnqueue();

		LOGGER.info("Enqueueing " + emailers);

		try {
			for (Iterator i = emailers.iterator(); i.hasNext();) {
				final Emailer spammer = (Emailer) i.next();
				JobDetail jd = SilverpopStatistics.getStatistics(spammer);
				if (jd != null) {
					spammer.jobEnqueued(jd.getJobId(), jd.getFilePath());
				}

				new UnitOfWork() {
					protected void perform() {
						getActionDao().saveOrUpdateAction(spammer);
					}
					
					private ActionDao getActionDao() {
						return OcfDaoFactory.getInstance().getActionDao();
					}
					
					protected HibernateDaoSupport getDaoSupport() {
						return getActionDao();
					}
				}.execute();
			}
		} catch (IOException e) {
			throw new FDRuntimeException(e);
		}

	}
}