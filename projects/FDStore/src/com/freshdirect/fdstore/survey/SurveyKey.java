/**
 * 
 */
package com.freshdirect.fdstore.survey;

import java.io.Serializable;

import com.freshdirect.common.customer.EnumServiceType;

public class SurveyKey implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    EnumSurveyType surveyType;
    EnumServiceType userType;

    public SurveyKey(EnumSurveyType surveyType, EnumServiceType userType) {
        super();
        this.surveyType = surveyType;
        this.userType = userType;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof SurveyKey) {
            SurveyKey sk = (SurveyKey) obj;
            return surveyType.equals(sk.surveyType) && userType.equals(sk.userType);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return surveyType.hashCode() << 4 ^ userType.hashCode();
    }

    public EnumSurveyType getSurveyType() {
        return surveyType;
    }

    public EnumServiceType getUserType() {
        return userType;
    }

    @Override
    public String toString() {
        return "SurveyKey[" + surveyType + ',' + userType + ']';
    }

}