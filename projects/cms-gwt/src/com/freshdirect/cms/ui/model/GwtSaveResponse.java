package com.freshdirect.cms.ui.model;

import java.io.Serializable;
import java.util.List;

import com.freshdirect.cms.ui.model.changeset.GwtChangeSet;
import com.freshdirect.cms.ui.model.publish.GwtValidationError;

public class GwtSaveResponse implements Serializable {

    private static final long serialVersionUID = 1L;

	String						changesetId;
	GwtChangeSet				changeSet;
	List<GwtValidationError>	validationMessages;

    public GwtSaveResponse() {
    }

    public GwtSaveResponse(String changesetId, GwtChangeSet changeSet) {
        this.changesetId = changesetId;
        this.changeSet = changeSet;
    }

    public GwtSaveResponse(List<GwtValidationError> validationMessages) {
        this.validationMessages = validationMessages;
    }

    public String getChangesetId() {
        return changesetId;
    }

    public List<GwtValidationError> getValidationMessages() {
        return validationMessages;
    }
    
    public boolean isOk() {
        return validationMessages==null;
    }
    
    public GwtChangeSet getChangeSet() {
        return changeSet;
    }

}
