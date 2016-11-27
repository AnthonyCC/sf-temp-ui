/*
 * Created on Mar 28, 2005
 */
package com.freshdirect.cms.publish;


/**
 * A step in the publishing process.
 * 
 * @see com.freshdirect.cms.publish.PublishServiceI
 */
public interface PublishTask {

	/**
	 * Perform this task in the context of a given publish.
	 * 
	 * @param publish contextual info about the publish
	 */
	public void execute(Publish publish);

	/**
	 * Get a human readable description of this task.
	 * 
	 * @return display message about this task (never null)
	 */
	public String getComment();

}
