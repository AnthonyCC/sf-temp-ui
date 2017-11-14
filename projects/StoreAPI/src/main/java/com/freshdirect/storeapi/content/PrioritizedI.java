/*
 * PrioritizedI.java
 *
 * Created on October 31, 2001, 9:34 PM
 */

package com.freshdirect.storeapi.content;

import java.util.Comparator;


/**
 * 
 * @author mrose
 * @version
 */
public interface PrioritizedI {
    
    public int getPriority();
    
    public static Comparator<PrioritizedI> PRIORITY_COMPARATOR = new Comparator<PrioritizedI>() {
        public int compare(PrioritizedI o1, PrioritizedI o2) {
            int p1 = o1.getPriority();
            int p2 = o2.getPriority();
            return p1==p2 ? 0 : (p1<p2 ? -1 : 1); 
        }
    };

}
