package com.freshdirect.backoffice.selfcredit.data;

import java.io.Serializable;

public class RequestContext implements Serializable {

    private static final long serialVersionUID = -1887311811253266321L;

    private String initiator;
	
	private String masqueradeAgent; 
	
	private String source;
	
	private String estoreId;
	
	private String facility;
	
	private String level;
	
	private String application;
	
	private String orderId;

    public String getInitiator() {
        return initiator;
    }

    public void setInitiator(String initiator) {
        this.initiator = initiator;
    }

    public String getMasqueradeAgent() {
        return masqueradeAgent;
    }

    public void setMasqueradeAgent(String masqueradeAgent) {
        this.masqueradeAgent = masqueradeAgent;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getEstoreId() {
        return estoreId;
    }

    public void setEstoreId(String estoreId) {
        this.estoreId = estoreId;
    }

    public String getFacility() {
        return facility;
    }

    public void setFacility(String facility) {
        this.facility = facility;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
	
}
