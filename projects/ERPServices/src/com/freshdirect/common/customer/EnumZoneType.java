package com.freshdirect.common.customer;


public enum EnumZoneType {

    HYBRID, REGULAR;

    public String getName() {
        return name();
    }
    
    public static EnumZoneType getEnum(String service) {
        try {
            return service != null ? EnumZoneType.valueOf(service) : null;
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

}
