package com.freshdirect.webapp.util;

import com.freshdirect.framework.util.StringUtil;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.mail.EmailUtil;
import com.freshdirect.webapp.taglib.fdstore.SystemMessageList;

public class ValidationUtils {

    private final static int MIN_PASSWORD_LENGTH = 6;

    public static void validateEmailAddress(ActionResult result, String type, String emailAddress) {
        result.addError(StringUtil.isEmpty(emailAddress), type, SystemMessageList.MSG_REQUIRED);
        result.addError(!EmailUtil.isValidEmailAddress(emailAddress), type, SystemMessageList.MSG_EMAIL_FORMAT);
    }

    public static void validateZipCode(ActionResult result, String type, String zipCode) {
        result.addError(zipCode != null && zipCode.length() < 5, type, SystemMessageList.MSG_REQUIRED);
    }

    public static void validatePassword(ActionResult result, String type, String password) {
        result.addError(StringUtil.isEmpty(password), type, SystemMessageList.MSG_REQUIRED);
        result.addError(!StringUtil.isEmpty(password) && password.length() < MIN_PASSWORD_LENGTH, type, SystemMessageList.MSG_PASSWORD_LENGTH);
    }

    public static void validateCompany(ActionResult result, String type, String companyName) {
        result.addError(StringUtil.isEmpty(companyName), type, SystemMessageList.MSG_REQUIRED);
    }

    public static void validateWorkPhone(ActionResult result, String type, String phone) {
        result.addError(StringUtil.isEmpty(phone), type, SystemMessageList.MSG_REQUIRED);
        validateOptionalWorkPhone(result, type, phone);
    }
    
    public static void validateOptionalWorkPhone(ActionResult result, String type, String phone) {
        result.addError(!StringUtil.isEmpty(phone) && (phone.length() > 0 && phone.length() < 10), type, SystemMessageList.MSG_NUM_REQ);
    }

    public static void validateEmailPreferencesLevel(ActionResult result, String type, String emailPreferenceLevel) {
        result.addError(StringUtil.isEmpty(emailPreferenceLevel), type, SystemMessageList.MSG_REQUIRED);
        result.addError(!StringUtil.isEmpty(emailPreferenceLevel) && emailPreferenceLevel.length() != 1, type, SystemMessageList.MSG_EMAIL_PREFERENCE_LEVEL_LENGTH);
        result.addError(!StringUtil.isEmpty(emailPreferenceLevel) && !StringUtil.isNumeric(emailPreferenceLevel), type, SystemMessageList.MSG_EMAIL_PREFERENCE_LEVEL_NUMBER);
    }

    public static void validateFirstName(ActionResult result, String type, String firstName) {
        result.addError(StringUtil.isEmpty(firstName), type, SystemMessageList.MSG_REQUIRED);
    }

    public static void validateLastName(ActionResult result, String type, String lastName) {
        result.addError(StringUtil.isEmpty(lastName), type, SystemMessageList.MSG_REQUIRED);
    }
}
