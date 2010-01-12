/**
 * @author ekracoff
 * Created on Feb 3, 2005*/

package com.freshdirect.cms.publish;

import java.util.List;

import com.freshdirect.cms.ConcurrentPublishException;

/**
 * Service interface to initiate publishing and obtain information about
 * historical publications.
 * <p>
 * Publishing consist of a series of {@link com.freshdirect.cms.publish.PublishTask}s.
 * The publishing process is performed asynchronously and can be initiated
 * using {@link #doPublish(Publish)}. Status can be polled via
 * {@link #getPublish(PrimaryKey)}.
 *
 * @see com.freshdirect.cms.publish.PublishTask
 */
public interface PublishServiceI {

	/**
	 * Get all publishes.
	 * 
	 * @return List of {@link Publish}
	 */
	public List<Publish> getPublishHistory();

	/**
	 * Get information about a specific publish.
	 * 
	 * @param id the id of the publish.
	 * @return Publish, or null if not found
	 */
	public Publish getPublish(String id);

	/**
	 * Get information about the last publish, which is finished.
	 * 
	 * @return Publish, or null if there was no publish yet
	 */
	public Publish getMostRecentPublish();
	
	
    /**
     *  Return the most recent Publish object, probably this is work in progress.
     * 
     *  @return the most recent Publish object.
     */
    public Publish getMostRecentNotCompletedPublish();
	

	/**
	 * Get a publish made before a specified publish.
	 * 
	 * @param publish get the publish that was made before this one.
	 * @return the publish made before the supplied publish object,
	 *         or null if there is no previous publish.
	 */
	public Publish getPreviousPublish(Publish publish);
	
	/**
	 * Initiate the publish process (asynchronous).
	 * 
	 * @param publish contextual info about the publish
	 * @return ID assigned to the current publish
	 * @throws ConcurrentPublishException if there's a publish already in progress
	 */
	public String doPublish(Publish publish) throws ConcurrentPublishException;

}