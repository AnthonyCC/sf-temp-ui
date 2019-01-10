package com.freshdirect.webapp.ajax;

import java.util.HashMap;


public class FDAjaxWarning {

    private HashMap<String, String> warnings = new HashMap<String, String>();

    public FDAjaxWarning(String primaryMessage, String secondaryMessage) {
        this.warnings.put("primaryMessage", primaryMessage);
        this.warnings.put("secondaryMessage", secondaryMessage);
    }

    public HashMap<String, String> getWarnings() {
        return warnings;
    }
}
