package com.freshdirect.fdstore.survey;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.enums.Enum;

public class EnumSurveyType extends Enum {

	public static final EnumSurveyType CUSTOMER_PROFILE_SURVEY = new EnumSurveyType("Customer Profile Survey");
	public static final EnumSurveyType REGISTRATION_SURVEY = new EnumSurveyType("Registration_survey");
	public static final EnumSurveyType SECOND_ORDER_SURVEY = new EnumSurveyType("Second Order Survey");
	public static final EnumSurveyType POST_ORDER_SURVEY = new EnumSurveyType("Post Order Survey");

	
	private EnumSurveyType(String name) {
		super(name);
	}


	public static EnumSurveyType getEnum(String name) {
		return (EnumSurveyType) getEnum(EnumSurveyType.class, name);
	}

	public static Map getEnumMap() {
		return getEnumMap(EnumSurveyType.class);
	}

	public static List getEnumList() {
		return getEnumList(EnumSurveyType.class);
	}
}

