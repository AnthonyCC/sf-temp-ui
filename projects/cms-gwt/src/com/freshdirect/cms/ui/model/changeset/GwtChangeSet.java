package com.freshdirect.cms.ui.model.changeset;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class GwtChangeSet implements Serializable {

    private static final long serialVersionUID = -19795265222449154L;

    private String changeSetId;
    private String userId;
    private Date modifiedDate;
    private String note;
    private List<GwtNodeChange> nodeChanges;

    public GwtChangeSet() {
        nodeChanges = new ArrayList<GwtNodeChange>();
    }

    public GwtChangeSet(String id, String userId, Date date, String note) {
        this();
        this.changeSetId = id;
        this.userId = userId;
        this.modifiedDate = date;
        this.note = note;
    }

    public String getChangeSetId() {
        return changeSetId;
    }

    public void setChangeSetId(String changeSetId) {
        this.changeSetId = changeSetId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modified) {
        this.modifiedDate = modified;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public List<GwtNodeChange> getNodeChanges() {
        return nodeChanges;
    }

    public void addChange(GwtNodeChange nodeChange) {
        nodeChanges.add(nodeChange);
    }

    public void addChanges(Collection<GwtNodeChange> nodeChanges) {
        this.nodeChanges.addAll(nodeChanges);
    }

    /**
     * Get a filtered list of changes for a given content object.
     * 
     * @param key
     *            content node key (never null)
     * @return List of {@link GwtNodeChange} (never null)
     */
    public List<GwtNodeChange> getNodeChangesById(String key) {
        List<GwtNodeChange> changes = new ArrayList<GwtNodeChange>();
        for (GwtNodeChange cnc : nodeChanges) {
            if (key.equals(cnc.getKey())) {
                changes.add(cnc);
            }
        }
        return changes;
    }

    public int length() {
        int result = 0;
        for (GwtNodeChange c : nodeChanges) {
            result += c.length();
        }
        return result;
    }

    @Override
    public String toString() {
        return "GwtChangeSet[" + changeSetId + ',' + userId + "," + modifiedDate + ',' + note + ",length:" + length() + ']';
    }

}
