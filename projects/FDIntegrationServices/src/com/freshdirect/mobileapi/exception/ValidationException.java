package com.freshdirect.mobileapi.exception;

import com.freshdirect.framework.webapp.ActionResult;

/**
 * Recoverable validation error that should be actively checked
 * and process as part of the normal process.
 * 
 * @author Rob
 *
 */
public class ValidationException extends Throwable {

    private static final long serialVersionUID = 2393365488335116327L;

    private ActionResult result;

    public ValidationException(ActionResult result) {
        this.result = result;
    }

    public ActionResult getResult() {
        return result;
    }

    public void addError(String type, String message) {
        this.result.addError(true, type, message);
    }

}
