package com.freshdirect.cms.application;

import com.freshdirect.framework.core.PrimaryKey;


/**
 * Simple implementation of {@link com.freshdirect.cms.application.CmsResponseI}.
 * 
 * @TODO we don't really need interface/implementation separation here
 */
public class CmsResponse implements CmsResponseI {

	private PrimaryKey changeSetId;

	public CmsResponse() {
	}

	/* (non-Javadoc)
	 * @see com.freshdirect.cms.application.CmsResponseI#setChangeSetId(com.freshdirect.framework.core.PrimaryKey)
	 */
	public void setChangeSetId(PrimaryKey pk) {
		changeSetId = pk;
	}

	/* (non-Javadoc)
	 * @see com.freshdirect.cms.application.CmsResponseI#getChangeSetId()
	 */
	public PrimaryKey getChangeSetId() {
		return changeSetId;
	}

}