package com.freshdirect.cms.ui.model.changeset;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import com.freshdirect.cms.ui.client.nodetree.ContentNodeModel;

public class GwtChangeSet extends ContentNodeModel {

    public final static Comparator<GwtChangeSet> DATE_COMPARATOR = new DateComparator();
    
    public final static Comparator<GwtChangeSet> USER_COMPARATOR = new UserComparator();

    public final static Comparator<GwtChangeSet> NOTE_COMPARATOR = new NoteComparator();

    public final static Comparator<GwtChangeSet> DATE_COMPARATOR_INV = new InverseComparator<GwtChangeSet>(new DateComparator());
    
    public final static Comparator<GwtChangeSet> USER_COMPARATOR_INV = new InverseComparator<GwtChangeSet>(new UserComparator());

    public final static Comparator<GwtChangeSet> NOTE_COMPARATOR_INV = new InverseComparator<GwtChangeSet>(new NoteComparator());
    
     
    private static final class NoteComparator implements Comparator<GwtChangeSet> {
        @Override
        public int compare(GwtChangeSet o1, GwtChangeSet o2) {
            return o1.getNote().compareTo(o2.getNote());
        }
    }

    private static final class UserComparator implements Comparator<GwtChangeSet> {
        @Override
        public int compare(GwtChangeSet o1, GwtChangeSet o2) {
            return -o1.getUserId().compareTo(o2.getUserId());
        }
    }

    private static final class DateComparator implements Comparator<GwtChangeSet> {
        @Override
        public int compare(GwtChangeSet o1, GwtChangeSet o2) {
            return -o1.getModifiedDate().compareTo(o2.getModifiedDate());
        }
    }

    static class InverseComparator<X> implements Comparator<X> {
        final Comparator<X> inner;
        
        public InverseComparator(Comparator<X> inner) {
            this.inner = inner;
        }
        
        public int compare(X o1, X o2) {
            return -inner.compare(o1, o2);
        }
    }

    
    private String changeSetId;
    private String userId;
    private Date modifiedDate;
    private String note;
    private List<GwtContentNodeChange> nodeChanges = new ArrayList<GwtContentNodeChange>();
    
    	
	public GwtChangeSet() {
		super();
	}
    
    public GwtChangeSet( String type, String key ) {
    	super( type, "", key );
    }
    
    
	public String getChangeSetId() {
		return changeSetId;
	}
	
	public void setChangeSetId( String changeSetId ) {
		this.changeSetId = changeSetId;
	}

    public String getUserId() {
        return userId;
    }
    
	public void setUserId( String userId ) {
		this.userId = userId;
	}
    
    public Date getModifiedDate() {
        return modifiedDate;
    }
   
	public void setModifiedDate( Date modified ) {
		this.modifiedDate = modified;
	}    
    
    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
        
	public List<GwtContentNodeChange> getNodeChanges() {
		return nodeChanges;
	}

	public void setNodeChanges( List<GwtContentNodeChange> nodeChanges ) {
		this.nodeChanges = nodeChanges;
	}


	public void addChange( GwtContentNodeChange nodeChange ) {
		this.nodeChanges.add( nodeChange );
		nodeChange.setChangeSet( this );
	}
	
    /**
     * Get a filtered list of changes for a given content object.
     * 
     * @param key	content node key (never null)
     * @return List of {@link GwtContentNodeChange} (never null)
     */
	public List<GwtContentNodeChange> getNodeChangesById( String key ) {
		List<GwtContentNodeChange> nodeChanges = new ArrayList<GwtContentNodeChange>();
		for ( GwtContentNodeChange cnc : this.nodeChanges ) {
			if ( key.equals( cnc.getContentKey() ) ) {
				nodeChanges.add( cnc );
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
        return "GwtChangeSet[" + changeSetId + ',' + userId + "," + modifiedDate + ',' + note + ",length:" + length() + ']';
    }

}
