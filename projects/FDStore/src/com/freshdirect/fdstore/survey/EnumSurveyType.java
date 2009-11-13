package com.freshdirect.fdstore.survey;

import java.util.HashMap;
import java.util.Map;


public enum EnumSurveyType {

    CUSTOMER_PROFILE_SURVEY("Customer Profile Survey"), 
    REGISTRATION_SURVEY("Registration_survey"), 
    SECOND_ORDER_SURVEY("Second Order Survey"), 
    POST_ORDER_SURVEY("Post Order Survey")

    ,DIET_NUTRITION("DietNutrition")
    ,COS_FEEDBACK_SURVEY("COS_Feedback_Survey")
    ,HAMPTONS("Hamptons05")
    ,POST_ORDER("PostOrder")
    ,POST_ORDER_DETAIL("PostOrderDetail")
    ,MORNING_DELIVERY("MorningDelivery")
    ,ORGANIC_2("Organic2")
    ,USABILITY("Usability")
    
    ;
    
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
