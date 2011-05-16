package com.freshdirect.erp;


public enum EnumStateCodes {

    ENUM_STATE_NY("NY", "New York, NY"), 
    ENUM_STATE_NJ("NJ", "New Jersey, NJ"), 
    ENUM_STATE_CT("CT", "Connecticut, CT");

    private final String desc;
    private final String id;

    private EnumStateCodes(String id, String desc) {
        this.id = id;
        this.desc = desc;
    }

    public static EnumStateCodes getEnumById(String id) {
        for (EnumStateCodes e : values()) {
            if (e.id.equals(id)) {
                return e;
            }
        }
        return null;
    }
    
    public String getId() {
        return id;
    }

    public String getDesc() {
        return this.desc;
    }

}
