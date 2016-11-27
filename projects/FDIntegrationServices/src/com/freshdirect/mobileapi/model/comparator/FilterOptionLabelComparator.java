package com.freshdirect.mobileapi.model.comparator;

import java.util.Comparator;

import com.freshdirect.mobileapi.controller.data.response.FilterOption;

public class FilterOptionLabelComparator implements Comparator<FilterOption> {

    @Override
    public int compare(FilterOption o1, FilterOption o2) {
        return o1.getLabel().compareTo(o2.getLabel());
    }

}
