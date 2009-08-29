package com.freshdirect.cms.ui.model.changeset;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class GwtChangeSet implements Serializable {

    public final static Comparator<GwtChangeSet> DATE_COMPARATOR = new Comparator<GwtChangeSet>() {
        @Override
        public int compare(GwtChangeSet o1, GwtChangeSet o2) {
            return -o1.getModifiedDate().compareTo(o2.getModifiedDate());
        }
    };
    
    public final static Comparator<GwtChangeSet> USER_COMPARATOR = new Comparator<GwtChangeSet>() {
        @Override
        public int compare(GwtChangeSet o1, GwtChangeSet o2) {
            return -o1.getUserId().compareTo(o2.getUserId());
        }
    };

    public final static Comparator<GwtChangeSet> NOTE_COMPARATOR = new Comparator<GwtChangeSet>() {
        @Override
        public int compare(GwtChangeSet o1, GwtChangeSet o2) {
            return o1.getNote().compareTo(o2.getNote());
        }
    };
    

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String id;

    private String userId;

    private Date modifiedDate;

    /** List of {@link GwtContentNodeChange} */
    private List<GwtContentNodeChange> nodeChanges = new ArrayList<GwtContentNodeChange>();

    private String note;

    /**
     * @return List of {@link GwtContentNodeChange}
     */
    public List<GwtContentNodeChange> getNodeChanges() {
        return nodeChanges;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public String getUserId() {
        return userId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setNodeChanges(List<GwtContentNodeChange> nodeChanges) {
        this.nodeChanges = nodeChanges;
    }

    public void setModifiedDate(Date modified) {
        this.modifiedDate = modified;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void addChange(GwtContentNodeChange nodeChange) {
        this.nodeChanges.add(nodeChange);
        nodeChange.setChangeSet(this);
    }

    /**
     * Get a filtered list of changes for a given content object.
     * 
     * @param key
     *            content node key (never null)
     * @return List of {@link GwtContentNodeChange} (never null)
     */
    public List<GwtContentNodeChange> getNodeChangesById(String key) {
        List<GwtContentNodeChange> nodeChanges = new ArrayList<GwtContentNodeChange>();
        for (Iterator<GwtContentNodeChange> i = this.nodeChanges.iterator(); i.hasNext();) {
            GwtContentNodeChange cnc = i.next();
            if (key.equals(cnc.getKey())) {
                nodeChanges.add(cnc);
            }
        }
        return nodeChanges;
    }

    public int length() {
        int result = 0;
        for (GwtContentNodeChange c : nodeChanges) {
            result += c.length();
        }
        return result;
    }

    @Override
    public String toString() {
        return "GwtChangeSet[" + id + ',' + userId + "," + modifiedDate + ',' + note + ",length:" + length() + ']';
    }

}
