package com.freshdirect.common.customer;


public enum EnumServiceType {

    HOME, CORPORATE, DEPOT, PICKUP, WEB, IPHONE;

    public String getName() {
        return name();
    }
    
    public static EnumServiceType getEnum(String service) {
        try {
            return service != null ? EnumServiceType.valueOf(service) : null;
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

}
