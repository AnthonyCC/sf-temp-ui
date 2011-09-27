/*
 * PrioritizedI.java
 *
 * Created on October 31, 2001, 9:34 PM
 */

package com.freshdirect.fdstore.content;

import java.util.Comparator;

/**
 *
 * @author  mrose
 * @version 
 */
public interface PrioritizedI {
    
    public int getPriority();
    
    public static Comparator PRIORITY_COMPARATOR = new Comparator() {
        public int compare(Object o1, Object o2) {
            int p1 = ((PrioritizedI) o1).getPriority();
            int p2 = ((PrioritizedI) o2).getPriority();
            return p1==p2 ? 0 : (p1<p2 ? -1 : 1); 
        }
    };

}

