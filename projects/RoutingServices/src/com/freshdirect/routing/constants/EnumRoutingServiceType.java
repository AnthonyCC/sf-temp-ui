package com.freshdirect.routing.constants;

public enum EnumRoutingServiceType {

    HOME, CORPORATE, COSENABLED;

    public String getName() {
        return name();
    }
    
    public static EnumRoutingServiceType getEnum(String service) {
        try {
            return service != null ? EnumRoutingServiceType.valueOf(service) : null;
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

}
