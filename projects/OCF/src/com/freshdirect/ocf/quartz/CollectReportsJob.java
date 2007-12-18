/**
 * @author ekracoff
 * Created on May 19, 2005*/

package com.freshdirect.ocf.quartz;

import java.io.File;
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
import com.freshdirect.ocf.core.OcfDaoFactory;
import com.freshdirect.ocf.core.Unzip;
import com.freshdirect.ocf.impl.hibernate.ActionDao;
import com.freshdirect.ocf.impl.hibernate.EmailStatLineDao;
import com.freshdirect.ocf.silverpop.SilverpopStatistics;
import com.freshdirect.ocf.silverpop.SilverpopStatsParser;

public class CollectReportsJob extends FDJob {

	private final static Category LOGGER = LoggerFactory.getInstance(CollectReportsJob.class);

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

		List emailers = dao.findEmailersToCollect();
		LOGGER.debug("Found emailers " + emailers);

		try {
			for (Iterator i = emailers.iterator(); i.hasNext();) {
				Emailer emailer = (Emailer) i.next();

				boolean jobOk = SilverpopStatistics.getJobStatus(emailer.getSilverpopJobId());
				LOGGER.info("Silverpop Job " + emailer.getSilverpopJobId() + " status " + jobOk);

				if (jobOk) {
					doProcessing(emailer);
				}
				
				dao.currentSession().clear();
			}
		} catch (IOException e) {
			throw new FDRuntimeException(e);
		}

	}

	private void doProcessing(Emailer emailer) throws IOException {
		File zipFile = null;
		File temp = null;
		try {
			//download the file
			LOGGER.info("Downloading file " + emailer.getSilverpopFilePath());
			zipFile = SilverpopStatistics.downloadFile(emailer.getSilverpopFilePath());
			LOGGER.info("Downloaded file " + emailer.getSilverpopFilePath());

			//unzip the file
			LOGGER.debug("Unzipping file");
			temp = File.createTempFile("real", ".csv");
			Unzip.unzip(zipFile, temp);

			//parse the file
			LOGGER.debug("Parsing file");
			SilverpopStatsParser parser = new SilverpopStatsParser();
			List stats = parser.parseFile(temp);
			storeStats(stats);

			//prep to be run again for next time
			emailer.scheduleNextEnqueue();
			store(emailer);

			LOGGER.info("Processed " + emailer.getId() + ": " + emailer.getSilverpopFilePath());
		} finally {
			if (temp != null) {
				temp.delete();
			}
			if (zipFile != null) {
				zipFile.delete();
			}
		}
	}

	private void store(final Emailer spammer) {
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

	private void storeStats(final List stats) {
		new UnitOfWork() {
			protected void perform() {
				getEmailStatLineDao().saveStats(stats);
			}
			
			private EmailStatLineDao getEmailStatLineDao() {
				return OcfDaoFactory.getInstance().getEmailStatLineDao();
			}
			
			protected HibernateDaoSupport getDaoSupport() {
				return getEmailStatLineDao();
			}
		}.execute();
	}

}