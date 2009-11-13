package com.freshdirect.fdstore.survey;

import java.util.HashMap;
import java.util.Map;


public enum EnumSurveyType {

    CUSTOMER_PROFILE_SURVEY("Customer Profile Survey"), 
    REGISTRATION_SURVEY("Registration_survey"), 
    SECOND_ORDER_SURVEY("Second Order Survey"), 
    POST_ORDER_SURVEY("Post Order Survey");

    String label;

    final static Map<String, EnumSurveyType> types = new HashMap<String, EnumSurveyType>();
    
    static {
        for (EnumSurveyType s : values()) {
            types.put(s.getLabel(), s);
        }
    }
    
    private EnumSurveyType(String name) {
        label = name;
    }

    public static EnumSurveyType getEnum(String name) {
        try {
            return types.get(name);
        } catch (NullPointerException e) {
            return null;
        }
    }

    public String getLabel() {
        return label;
    }
    
}
