package com.freshdirect.mobileapi.controller.data;

public class VariationOption {
    /** SAP characteristic value name */
    private String name;

    private String description;

    /** Chararcteristic value description */
    private String cvp;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCvp() {
        return cvp;
    }

    public void setCvp(String cvp) {
        this.cvp = cvp;
    }

}
