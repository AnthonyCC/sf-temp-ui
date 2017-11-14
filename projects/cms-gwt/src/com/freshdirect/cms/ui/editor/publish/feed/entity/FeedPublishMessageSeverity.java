package com.freshdirect.cms.ui.editor.publish.feed.entity;

public enum FeedPublishMessageSeverity {

    DEBUG(4),
    INFO(3),
    WARNING(2),
    ERROR(1),
    FAILURE(0);

    private int level;

    private FeedPublishMessageSeverity(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }

    public static FeedPublishMessageSeverity parse(int level) {
        FeedPublishMessageSeverity parsed = null;
        for (FeedPublishMessageSeverity value : FeedPublishMessageSeverity.values()) {
            if (value.getLevel() == level) {
                parsed = value;
                break;
            }
        }
        return parsed;
    }

}
