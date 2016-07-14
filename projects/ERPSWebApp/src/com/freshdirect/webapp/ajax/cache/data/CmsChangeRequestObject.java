package com.freshdirect.webapp.ajax.cache.data;

import java.util.HashSet;
import java.util.Set;

import com.freshdirect.cms.application.DraftContext;

public class CmsChangeRequestObject {

	private Set<String> contentKeys = new HashSet<String>();
	private DraftContext draftContext;
	
	/**
	 * @return the contentKeys
	 */
	public Set<String> getContentKeys() {
		return contentKeys;
	}

	/**
	 * @param contentKeys the contentKeys to set
	 */
	public void setContentKeys(Set<String> contentKeys) {
		this.contentKeys = contentKeys;
	}

    
    public DraftContext getDraftContext() {
        return draftContext;
    }

    
    public void setDraftContext(DraftContext draftContext) {
        this.draftContext = draftContext;
    }
	
}
