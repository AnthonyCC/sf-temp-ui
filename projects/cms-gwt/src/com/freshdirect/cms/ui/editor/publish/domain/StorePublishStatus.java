package com.freshdirect.cms.ui.editor.publish.domain;

public enum StorePublishStatus {

    /** Publish is running. */
    PROGRESS("Publish in process"),

    /** Publish was completed successfully. */
    COMPLETE("Publish completed"),

    /** Publish was terminated with an error. */
    FAILED("Publish failed");

    final String description;

    StorePublishStatus(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return this.name() + ": " + description;
    }
}
