package com.freshdirect.mobileapi.util;

import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.mobileapi.controller.data.request.DeliveryAddressRequest;
import com.freshdirect.webapp.ajax.expresscheckout.validation.service.DeliveryAddressValidationDataService;

public class DeliveryAddressValidatorUtil {
    
    private static final Logger LOGGER = LoggerFactory.getInstance(DeliveryAddressValidatorUtil.class);
    
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
    public static ActionResult validateDeliveryAddress(DeliveryAddressRequest request) {
        ActionResult result = new ActionResult();
        
        {
            final Pattern phonePattern = Pattern.compile("(\\d){10}"); // 10 digits is required
            final String value = request.getDlvhomephone();

            result.addError( !phonePattern.matcher(value).matches(), "address", "Invalid Mobile Number");
        }
        return result;
    }
}
