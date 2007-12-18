package com.freshdirect.cms.application;

import java.io.Serializable;

import com.freshdirect.framework.core.PrimaryKey;

/**
 * Delegate to record information during processing the
 * {@link com.freshdirect.cms.application.CmsRequestI}.
 * Currently carries the change set ID that was assigned to the request.
 */
public interface CmsResponseI extends Serializable {

	/**
	 * Get change set ID that was assigned to the request.
	 * 
	 * @return PrimaryKey of the change set
	 */
	public PrimaryKey getChangeSetId();

	/**
	 * Set the change set ID that was assigned to the request.
	 * 
	 * @param pk PrimaryKey of the change set
	 */
	public void setChangeSetId(PrimaryKey pk);

}