package com.freshdirect.mobileapi.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Utility class to sort lists, using a comparator.
 * @author fgarcia
 *
 * @param <T>
 */
public class SortUtil<T> {
    private List<T> targetList;

    private Comparator<T> comparator;

    public SortUtil(List<T> targetList) {
        this.targetList = targetList;
    }

    public SortUtil(List<T> targetList, Comparator<T> comparator) {
        this.targetList = targetList;
        this.comparator = comparator;
    }

    public List<T> sort() {
        List<T> result = new ArrayList<T>(targetList);
        if (comparator != null) {
            Collections.sort(result, comparator);
        }
        return result;
    }

    public void setComparator(Comparator<T> comparator) {
        this.comparator = comparator;
    }
    
    
}
