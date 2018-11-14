package com.freshdirect.mobileapi.model.comparator;

import java.util.Comparator;

import com.freshdirect.mobileapi.controller.data.response.FilterGroupItem;

public class FilterGroupItemLabelComparator implements Comparator<FilterGroupItem> {

    @Override
    public int compare(FilterGroupItem o1, FilterGroupItem o2) {
        return o1.getLabel().compareTo(o2.getLabel());
    }

}
