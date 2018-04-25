package com.freshdirect.mobileapi.util;

import java.text.MessageFormat;
import java.util.regex.Pattern;

import com.freshdirect.framework.webapp.ActionError;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.mobileapi.controller.data.request.DeliveryAddressRequest;
import com.freshdirect.webapp.ajax.expresscheckout.validation.service.DeliveryAddressValidationDataService;

public class DeliveryAddressValidatorUtil {

    private static final Pattern PHONE_CONSTRAINT_REGEX_PATTERN = Pattern.compile("(\\d){10}");
    private static final Pattern ZIP_CODE_CONSTRAINT_REGEX_PATTERN = Pattern.compile("^(\\d){5}$");
    private static final Pattern TEXT_CONSTRAINT_REGEX_PATTERN = Pattern.compile("^(?!.*[&|<|>|\\\"|/|#|%|=])\\w.*$");
    private static final Pattern STREET_NAME_CONSTRAINT_REGEX_PATTERN = Pattern.compile("^(?!.*[&|<|>|\\\"|#|%|=])\\w.*$");
    private static final Pattern STATE_CONSTRAINT_REGEX_PATTERN = Pattern.compile(
            "^(AL|AK|AS|AZ|AR|CA|CO|CT|DE|DC|FM|FL|GA|HI|ID|IL|IN|IA|KS|KY|LA|ME|MH|MD|MA|MI|MN|MS|MO|MT|NE|NV|NH|NJ|NM|NY|NC|ND|MP|OH|OK|OR|PW|PA|PR|RI|SC|SD|TN|TX|UT|VT|VI|VA|WA|WV|WI|WY)$");

    private DeliveryAddressValidatorUtil() {}

    /**
     * A very simple delivery address form validator method
     * 
     * DEV NOTE: rather incomplete implementation, just focusing to the apparent issue
     * It should be integrated into a common validation framework or service later.
     * 
     * @see DeliveryAddressValidationDataService
     * 
     * @param request
     * @return
     */
    public static ActionResult validateDeliveryAddress(DeliveryAddressRequest address) {
        ActionResult result = new ActionResult();
        addStrictError(result, PHONE_CONSTRAINT_REGEX_PATTERN, "dlvhomephone", address.getDlvhomephone());
        return result;
    }

    public static ActionResult validateUnattendeDeliveryAddress(DeliveryAddressRequest address) {
        ActionResult result = new ActionResult();
        addStrictError(result, STREET_NAME_CONSTRAINT_REGEX_PATTERN, "address1", address.getAddress1());
        addOptionalError(result, STREET_NAME_CONSTRAINT_REGEX_PATTERN, "address2", address.getAddress2());
        addOptionalError(result, TEXT_CONSTRAINT_REGEX_PATTERN, "apartment", address.getApartment());
        addStrictError(result, TEXT_CONSTRAINT_REGEX_PATTERN, "city", address.getCity());
        addStrictError(result, STATE_CONSTRAINT_REGEX_PATTERN, "state", address.getState());
        addStrictError(result, ZIP_CODE_CONSTRAINT_REGEX_PATTERN, "zipcode", address.getZipcode());
        return result;
    }

    private static void addStrictError(ActionResult result, Pattern pattern, String key, String value) {
        if (!isRegexMatched(pattern, value)) {
            result.addError(new ActionError(key, MessageFormat.format("Invalid value - {0}: {1}", key, value)));
        }
    }

    private static void addOptionalError(ActionResult result, Pattern pattern, String key, String value) {
        if (value != null && !"".equals(value)) {
            addStrictError(result, pattern, key, value);
        }
    }

    private static boolean isRegexMatched(Pattern pattern, String input) {
        return (input != null) ? pattern.matcher(input).matches() : false;
    }

}
