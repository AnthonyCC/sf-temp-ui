package com.freshdirect.cms.ui.model.changeset;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GwtContentNodeChange implements Serializable  {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private GwtChangeSet changeSet;
    
    private String key;
    
    private List<GwtChangeDetail> changeDetails = new ArrayList<GwtChangeDetail>();
    
    private String changeType;


    /**
     * @return List of {@link GwtChangeDetail}
     */
    public List<GwtChangeDetail> getChangeDetails() {
        return changeDetails;
    }

    public String getKey() {
        return key;
    }

    public void setContentKey(String key) {
        this.key = key;
    }

    public void setChangeDetails(List<GwtChangeDetail> changeDetails) {
        this.changeDetails = changeDetails;
    }

    public String getChangeType() {
        return changeType;
    }

    public void setChangeType(String changeType) {
        this.changeType = changeType;
    }

    public void addDetail(GwtChangeDetail detail) {
        this.changeDetails.add(detail);
    }

    public void setChangeSet(GwtChangeSet changeSet) {
        this.changeSet = changeSet;
    }

    public GwtChangeSet getChangeSet() {
        return this.changeSet;
    }

    public int length() {
        // just for delete events ...
        return changeDetails.size() == 0 ? 1 : changeDetails.size();
    }

}
