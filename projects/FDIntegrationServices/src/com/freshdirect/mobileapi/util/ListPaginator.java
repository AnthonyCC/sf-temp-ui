package com.freshdirect.mobileapi.util;

import java.util.List;

public class ListPaginator<T> {
    private List<T> content;

    private int pageSize;

    public ListPaginator(List content, int pageSize) {
        this.content = content;
        this.pageSize = pageSize;
    }

    public List<T> getPage(int page) {
        int max = pageSize;
        int start = (page - 1) * max;

        int size = content.size();
        if (size < (start + max)) {
            if (size < start) {
                start = size - max;
            } else {
                max = size - start;
            }
        }

        return content.subList(start, start + max);
    }
    
    public int getTotalItems() {
        return content.size();
    }

    public int getTotalPages() {
        double pages = (double) content.size() / (double) pageSize;
        return (int) Math.ceil(pages);
    }
}
