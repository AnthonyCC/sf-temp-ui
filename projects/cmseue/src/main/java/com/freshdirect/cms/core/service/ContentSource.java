package com.freshdirect.cms.core.service;


public enum ContentSource {
    RELATIONAL_DATABASE(false, true),
    XML(true, false),
    MEMORY(false, false);

    public final boolean readOnly;
    public final boolean changeControlSupported;

    ContentSource(boolean readOnly, boolean changeControlSupported) {
        this.readOnly = readOnly;
        this.changeControlSupported = changeControlSupported;
    }
}
