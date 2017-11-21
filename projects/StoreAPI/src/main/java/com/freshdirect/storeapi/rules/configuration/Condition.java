package com.freshdirect.storeapi.rules.configuration;


public class Condition {
    
    private String clazz;
    private String label;
    private String xmlTag;
    
    public Condition(String clazz, String label, String xmlTag) {
        this.clazz = clazz;
        this.label = label;
        this.xmlTag = xmlTag;
    }

    public String getClazz() {
        return clazz;
    }

    public String getLabel() {
        return label;
    }


    public String getXmlTag() {
        return xmlTag;
    }
    
}
