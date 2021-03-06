package com.freshdirect.webapp.ajax.expresscheckout.validation.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ValidationResult {

    private String fdform;
    private List<ValidationError> errors = new ArrayList<ValidationError>();
    private Map<String, Object> result = new HashMap<String, Object>();

    public String getFdform() {
        return fdform;
    }

    public void setFdform(String fdform) {
        this.fdform = fdform;
    }

    public List<ValidationError> getErrors() {
        return errors;
    }

    public void setErrors(List<ValidationError> errors) {
        this.errors = errors;
    }

    public void addError(ValidationError error) {
        this.errors.add(error);
    }

    public void addErrors(Collection<ValidationError> errors) {
        this.errors.addAll(errors);
    }

    public Map<String, Object> getResult() {
        return result;
    }

    public void setResult(Map<String, Object> result) {
        this.result = result;
    }

    public void putResult(String key, Object value) {
        this.result.put(key, value);
    }

}
