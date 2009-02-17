package com.freshdirect.smartstore.scoring;

import java.util.Iterator;
import java.util.List;

/**
 * This class contains functions which used by he generated code.
 * @author zsombor
 *
 */
public class HelperFunctions {
    private HelperFunctions() {}
    
    
    /**
     * This functions concatenates the two list, but avoid creating duplicates.
     * 
     * @param first
     * @param second
     */
    public static final void addAll(List first, List second) {
        for (Iterator iter=second.iterator();iter.hasNext();) {
            Object obj = iter.next();
            if (!first.contains(obj)) {
                first.add(obj);
            }
        }
    }
    
    /**
     * used by the generated code
     * @param value
     * @param min
     * @param max
     * @return
     */
    public static final int between(double value, double min, double max) {
        return (min<=value && value<= max ? 1 : 0);
    }

    /**
     * used by the generated code
     * @param value
     * @param min
     * @param max
     * @return
     */
    public static final int between(int value, int min, int max) {
        return (min<=value && value<= max ? 1 : 0);
    }


}
