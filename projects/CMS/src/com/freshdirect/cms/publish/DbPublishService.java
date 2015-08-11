/**
 * @author ekracoff
 * Created on Feb 3, 2005*/

package com.freshdirect.cms.publish;

import java.util.List;

import com.freshdirect.cms.ConcurrentPublishException;
import com.freshdirect.cms.application.service.DbService;
import com.freshdirect.cms.core.CmsDaoFactory;
import com.freshdirect.cms.search.IBackgroundProcessor;

/**
 * Implementation of {@link com.freshdirect.cms.publish.PublishServiceI} that
 * records publications in the database.
 */
public class DbPublishService extends DbService implements PublishServiceI {

	/**
	 * The Publish data access object
	 */
	private PublishDao publishDao;

	private String basePath;

	private IBackgroundProcessor processor;

	/**
	 * Constructor.
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

	public void setProcessor(IBackgroundProcessor processor) {
		this.processor = processor;
	}

	public List<Publish> getPublishHistory() {
		// return publishDao.getAllPublishesOrdered("timestamp desc");
		// use lightweight fetch instead of hibernate
		return publishDao.fetchPublishes(null, "timestamp desc");
	}
	
	public List<Publish> getPublishHistoryByType(String type) {
		if(type == null){
			return getPublishHistory();
		} else {
			return publishDao.fetchPublishesX(null, "cro_mod_date desc");
		}
	}

	private void storePublish(final Publish publish) {
		publishDao.savePublish(publish);
	}

	public Publish getPublish(String id, Class clazz) {
		return publishDao.getPublish(id, clazz);
	}
	
	public void updatePublish(final Publish publish) {
		publishDao.savePublish(publish);
	}

	/**
	 * Return the most recent Publish object, with state COMPLETE.
	 * 
	 * @return the most recent Publish object.
	 */
	public Publish getMostRecentPublish() {
		return publishDao.getMostRecentPublish();
	}
	
	@Override
	public PublishX getMostRecentPublishX() {
		return publishDao.getMostRecentPublishX();
	}

	/**
	 * Return the most recent Publish object.
	 * 
	 * @return the most recent Publish object.
	 */
	public Publish getMostRecentNotCompletedPublish() {
		return publishDao.getMostRecentNotCompletedPublish();
	}

	public Publish getPreviousPublish(Publish publish) {
		return publishDao.getPreviousPublish(publish);
	}

	public String doPublish(Publish publish) throws ConcurrentPublishException {

		// TODO: put the below store inside a unit of work
		publishDao.beginTransaction();

		storePublish(publish);

		// one has to commit the transation for the storePublish() call above,
		// as transactions are thread-specific
		// and the Publisher will run in a different thread
		publishDao.commitTransaction();
		publish.setBasePath(basePath + "/" + publish.getId() );

		processor.executePublish(publish);
		return publish.getId();
	}
	
	public PublishX getMostRecentNotCompletedPublishX() {
		return publishDao.getMostRecentNotCompletedPublishX();
	}
}
