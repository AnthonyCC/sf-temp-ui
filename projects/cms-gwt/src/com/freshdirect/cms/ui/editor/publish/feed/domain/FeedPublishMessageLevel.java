package com.freshdirect.cms.ui.editor.publish.feed.domain;

public enum FeedPublishMessageLevel {

    DEBUG(4),
    INFO(3),
    WARNING(2),
    ERROR(1),
    FAILURE(0);

    public static FeedPublishMessageLevel valueOf(int messageSeverity) {
        FeedPublishMessageLevel requestedMessageLevel = FeedPublishMessageLevel.DEBUG;
        for (FeedPublishMessageLevel currentLevel : FeedPublishMessageLevel.values()) {
            if (currentLevel.numericMessageLevel == messageSeverity) {
                requestedMessageLevel = currentLevel;
                break;
            }
        }
        return requestedMessageLevel;
    }

    private int numericMessageLevel;

    private FeedPublishMessageLevel(int numericMessageLevel) {
        this.numericMessageLevel = numericMessageLevel;
    }

    public int getNumericMessageLevel() {
        return numericMessageLevel;
    }
}
