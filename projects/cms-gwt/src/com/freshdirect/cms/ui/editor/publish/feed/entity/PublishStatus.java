package com.freshdirect.cms.ui.editor.publish.feed.entity;

public enum PublishStatus {

    PROGRESS("Publish in process"),
    COMPLETE("Publish completed"),
    FAILED("Publish failed");

    private String description;

    private PublishStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
