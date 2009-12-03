package com.freshdirect.mobileapi.util;

import java.util.ArrayList;
import java.util.List;

/**
 * This utility class will use a comparator to make a simple filter on the list. 
 * For multiple filters, several instance of FilterCondition should be used, being added using the addFilterCondition method.
 * The FilterCondition class will determine if an item on the list should be kept. 
 * @author fgarcia
 *
 * @param <T>
 */
public class FilterUtil<T> {
    private List<T> targetList;

    private List<Filter<T>> conditions = new ArrayList<Filter<T>>();

    public FilterUtil(List<T> targetList) {
        this.targetList = targetList;
    }

    public void addFilter(Filter<T> condition) {
        conditions.add(condition);
    }

    public void addAllFilters(List<Filter<T>> conditions){
        this.conditions.addAll(conditions);
    }
    /**
     * For every condition set in the filter, all objects are checked against the filter. If the object is "filtered" then is removed from the result;
     * @return
     */
    public List<T> filter() {
        List<T> result = new ArrayList<T>(targetList);
        for (Filter<T> condition : conditions) {
            for (T item : targetList) {
                if (!condition.isFiltered(item)) {
                    result.remove(item);
                }
            }
        }
        return result;
    }

    public void removeAllConditions() {
        conditions.clear();
    }
}
