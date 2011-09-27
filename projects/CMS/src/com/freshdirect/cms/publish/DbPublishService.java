/**
 * @author ekracoff
 * Created on Feb 3, 2005*/

package com.freshdirect.cms.publish;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Category;

import com.freshdirect.cms.ConcurrentPublishException;
import com.freshdirect.cms.application.service.DbService;
import com.freshdirect.cms.core.CmsDaoFactory;
import com.freshdirect.framework.util.log.LoggerFactory;

/**
 * Implementation of {@link com.freshdirect.cms.publish.PublishServiceI}
 * that records publications in the database.
 */
public class DbPublishService extends DbService implements PublishServiceI {

	private final static Category LOGGER = LoggerFactory.getInstance(DbPublishService.class);
	
	/**
	 *  The Publish data access object
	 */
	private PublishDao	publishDao;
	
	private List<PublishTask> publishTasks;

	private String basePath;

	/**
	 *  Constructor.
	 */
	public DbPublishService() {
		publishDao = CmsDaoFactory.getInstance().getPublishDao();
	}
	
	public String getBasePath() {
		return basePath;
	}

	public void setBasePath(String basePath) {
		this.basePath = basePath;
	}

	public void setPublishTasks(List<PublishTask> publishTasks) {
		this.publishTasks = publishTasks;
	}

	public List<PublishTask> getPublishTasks() {
		return publishTasks;
	}

	public List<Publish> getPublishHistory() {
		// return publishDao.getAllPublishesOrdered("timestamp desc");
		// use lightweight fetch instead of hibernate
		return publishDao.fetchPublishes(null, "timestamp desc");
	}

	private void storePublish(final Publish publish) {
		publishDao.savePublish(publish);
	}

	public Publish getPublish(String id) {
		return publishDao.getPublish(id);
	}

	public void updatePublish(final Publish publish) {
		publishDao.savePublish(publish);
	}

        /**
         *  Return the most recent Publish object, with state COMPLETE.
         * 
         *  @return the most recent Publish object.
         */
        public Publish getMostRecentPublish() {
            return publishDao.getMostRecentPublish();
        }
        
        /**
         *  Return the most recent Publish object.
         * 
         *  @return the most recent Publish object.
         */
        public Publish getMostRecentNotCompletedPublish() {
            return publishDao.getMostRecentNotCompletedPublish();
        }
        
	
	public Publish getPreviousPublish(Publish publish) {
		return publishDao.getPreviousPublish(publish);
	}

	public String doPublish(Publish newPublish) throws ConcurrentPublishException {

		// TODO: put the below store inside a unit of work
		publishDao.beginTransaction();
		
		storePublish(newPublish);
		
		// one has to commit the transation for the storePublish() call above,
		// as transactions are thread-specific
		// and the Publisher will run in a different thread
		publishDao.commitTransaction();

		Publisher publisher = new Publisher();
		publisher.publish = newPublish;

		Thread pubThread = new Thread(publisher, "Publisher");
		pubThread.start();

		return newPublish.getId();
	}

	private class Publisher implements Runnable {
		private Publish publish;

		public void run() {
			try {
				publishDao.beginTransaction();
				
				publish.setPath(basePath + "/" + publish.getId());

				for ( PublishTask task : publishTasks ) {

					publish.getMessages().add(new PublishMessage(PublishMessage.INFO, task.getComment()));
					publish.setLastModified(new Date());
					updatePublish(publish);

					task.execute(publish);
				}

				publish.setStatus(EnumPublishStatus.COMPLETE);
				publish.setLastModified(new Date());
				updatePublish(publish);

			} catch (Throwable e) {
				LOGGER.error("Exception occured during publish", e);
				publish.setStatus(EnumPublishStatus.FAILED);
				publish.setLastModified(new Date());
				publish.getMessages().add(new PublishMessage(PublishMessage.ERROR, e.toString()));
				updatePublish(publish);
			} finally {
				// one has to commit the transation, as transaction are thread-specific
				// and the publish status page is polling in a different thread
				publishDao.commitTransaction();
				publishDao.closeSession();
			}
		
		}

	}
}