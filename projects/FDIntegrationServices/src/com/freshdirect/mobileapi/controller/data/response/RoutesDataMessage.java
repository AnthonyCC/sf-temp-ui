package com.freshdirect.mobileapi.controller.data.response;

import com.freshdirect.logistics.controller.data.response.RoutesData;
import com.freshdirect.mobileapi.controller.data.Message;

public class RoutesDataMessage extends Message {

    private RoutesData routedata;
    
    public RoutesData getRouteData() {
        return routedata;
    }

    public void setRouteData(RoutesData routedata) {
        this.routedata = routedata;
    }
}
