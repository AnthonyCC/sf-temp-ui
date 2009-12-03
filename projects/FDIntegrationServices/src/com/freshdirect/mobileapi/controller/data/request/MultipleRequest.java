package com.freshdirect.mobileapi.controller.data.request;

import java.util.ArrayList;
import java.util.List;

public class MultipleRequest {
    private List<String> ids = new ArrayList<String>();

    public List<String> getIds() {
        return ids;
    }

    public void setIds(List<String> ids) {
        this.ids = ids;
    }

}
