/*
 * CrmCaseComparator.java
 *
 * Created on October 11, 2003, 8:49 PM
 */

package com.freshdirect.crm;

import java.util.*;

import com.freshdirect.enums.EnumModel;

/**
 *
 * @author  mrose
 */
public abstract class CrmCaseComparator implements Comparator {
    
    /** Creates a new instance of CrmCaseComparator */
    protected CrmCaseComparator() {
        super();
    }
    
    public int compare(Object o1, Object o2) {
        return this.compare((CrmCaseI) o1, (CrmCaseI) o2);
    }
    
    protected abstract int compare(CrmCaseI c1, CrmCaseI c2);
    
    protected int compare(List enumList, EnumModel e1, EnumModel e2) {
        return compare(enumList.indexOf(e1), enumList.indexOf(e2));
    }
    
    protected int compare(int p1, int p2) {
        return p1 == p2 ? 0 : (p1 < p2 ? -1 : 1);
    }
    
    /*
    public final static Comparator COMP_PK = new CrmCaseComparator() {
		protected int compare(CrmCaseI c1, CrmCaseI c2) {
			return c1.getPK().getId().compareTo(c2.getPK().getId());
		}
	};
    */
    
    public final static Comparator COMP_PRIORITY = new CrmCaseComparator() {
        protected int compare(CrmCaseI c1, CrmCaseI c2) {
            return this.compare(c1.getPriority().getPriority(), c2.getPriority().getPriority());
        }
    };
    
    public final static Comparator COMP_CUSTOMER = new CrmCaseComparator() {
        protected int compare(CrmCaseI c1, CrmCaseI c2) {
        	if ( c1.getCustomerLastName().compareToIgnoreCase(c2.getCustomerLastName()) == 0 ) {
				return c1.getCustomerFirstName().compareToIgnoreCase(c2.getCustomerFirstName());
        	} else {
            	return c1.getCustomerLastName().compareToIgnoreCase(c2.getCustomerLastName());
        	}
        }
    };
    
    public final static Comparator COMP_STATE = new CrmCaseComparator() {
        protected int compare(CrmCaseI c1, CrmCaseI c2) {
            return compare(CrmCaseState.getEnumList(), c1.getState(), c2.getState());
        }
    };
    
    public final static Comparator COMP_CREATE_DATE = new CrmCaseComparator() {
        protected int compare(CrmCaseI c1, CrmCaseI c2) {
            return c1.getCreateDate().compareTo(c2.getCreateDate());
        }
    };
    
    public final static Comparator COMP_ORIGIN = new CrmCaseComparator() {
        protected int compare(CrmCaseI c1, CrmCaseI c2) {
            return compare(CrmCaseOrigin.getEnumList(), c1.getOrigin(), c2.getOrigin());
        }
    };
    
    public final static Comparator COMP_QUEUE = new CrmCaseComparator() {
        protected int compare(CrmCaseI c1, CrmCaseI c2) {
            return compare(CrmCaseQueue.getEnumList(), c1.getSubject().getQueue(), c2.getSubject().getQueue());
        }
    };
    
    public final static Comparator COMP_SUBJECT = new CrmCaseComparator() {
        protected int compare(CrmCaseI c1, CrmCaseI c2) {
            return compare(CrmCaseSubject.getEnumList(), c1.getSubject(), c2.getSubject());
        }
    };
    
    public final static Comparator COMP_SUMMARY = new CrmCaseComparator() {
        protected int compare(CrmCaseI c1, CrmCaseI c2) {
            return c1.getSummary().compareTo(c2.getSummary());
        }
    };
    
    public final static Comparator COMP_ASSIGNED = new CrmCaseComparator() {
        protected int compare(CrmCaseI c1, CrmCaseI c2) {
            return c1.getAssignedAgentPK().getId().compareTo(c2.getAssignedAgentPK().getId());
        }
    };
    
    public final static Comparator COMP_IDLE_TIME = new CrmCaseComparator() {
        protected int compare(CrmCaseI c1, CrmCaseI c2) {
            long t1 = c1.getIdleTime();
            long t2 = c2.getIdleTime();
            return t1 == t2 ? 0 : (t1 < t2 ? -1 : 1);
        }
    };
    
}
