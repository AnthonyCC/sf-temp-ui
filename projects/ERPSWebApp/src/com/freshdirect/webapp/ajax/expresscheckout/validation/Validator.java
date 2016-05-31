package com.freshdirect.webapp.ajax.expresscheckout.validation;

import java.util.List;
import java.util.Map;

import com.freshdirect.webapp.ajax.expresscheckout.validation.constraint.Constraint;
import com.freshdirect.webapp.ajax.expresscheckout.validation.data.ValidationError;

public interface Validator {

    /*
     * validateByDatas - validate all datas if constraint is available.
     */
    List<ValidationError> validateByDatas(Map<String, String> datas, Map<String, Constraint<String>> constraints);

    /*
     * validateByConstraints - validate all constraints if data is available.
     */
    List<ValidationError> validateByConstraints(Map<String, String> datas, Map<String, Constraint<String>> constraints);
}
