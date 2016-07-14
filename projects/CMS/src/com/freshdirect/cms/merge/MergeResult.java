package com.freshdirect.cms.merge;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.freshdirect.cms.application.CmsRequestI;
import com.freshdirect.cmsadmin.domain.DraftChange;

/**
 * Result of a draft content merge event.
 */
public class MergeResult implements Serializable {

    private static final long serialVersionUID = 1976170267920245072L;
    
    private final CmsRequestI cmsRequest;
    private final Map<DraftChange, String> validationResults;

    public MergeResult(CmsRequestI cmsRequest) {
        this.cmsRequest = cmsRequest;
        this.validationResults = new HashMap<DraftChange, String>();
    }

    public CmsRequestI getCmsRequest() {
        return cmsRequest;
    }

    public void clearResult() {
        validationResults.clear();
    }

    public void addResult(DraftChange draftChange, String errorMessage) {
        validationResults.put(draftChange, errorMessage);
    }

    public void addResult(List<DraftChange> draftChanges, String errorMessage) {
        for (DraftChange draftChange : draftChanges) {
            addResult(draftChange, errorMessage);
        }
    }

    public String getErrorMessage(DraftChange draftChange) {
        return validationResults.get(draftChange);
    }

    public Set<DraftChange> getInvalidChanges() {
        return Collections.unmodifiableSet(validationResults.keySet());
    }

    public Collection<String> getInvalidMessage() {
        return Collections.unmodifiableCollection(validationResults.values());
    }

    public boolean isSuccess() {
        return validationResults.isEmpty();
    }
}
