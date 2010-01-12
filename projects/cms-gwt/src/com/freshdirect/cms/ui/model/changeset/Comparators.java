package com.freshdirect.cms.ui.model.changeset;

import java.util.Comparator;

public class Comparators {
	
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
            return o1.getUserId().compareTo(o2.getUserId());
        }
    }

    private static final class DateComparator implements Comparator<GwtChangeSet> {
        @Override
        public int compare(GwtChangeSet o1, GwtChangeSet o2) {
            return o1.getModifiedDate().compareTo(o2.getModifiedDate());
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

}
