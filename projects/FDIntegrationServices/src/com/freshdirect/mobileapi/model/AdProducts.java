package com.freshdirect.mobileapi.model;


public class AdProducts {
    private String id;

    private String contentbeacon;

    private String impbeacon;
    
    public AdProducts(String id, String contentbeacon, String impbeacon){
    	this.id = id;
    	this.contentbeacon = contentbeacon;
    	this.impbeacon = impbeacon;
    }
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContentBeacon() {
        return this.contentbeacon;
    }

    public void setContentBeacon(String contentbeacon) {
        this.contentbeacon = contentbeacon;
    }

    public String getImpBeacon() {
        return this.impbeacon;
    }

    public void setImpBeacon(String impbeacon) {
        this.impbeacon = impbeacon;
    }

    
}
