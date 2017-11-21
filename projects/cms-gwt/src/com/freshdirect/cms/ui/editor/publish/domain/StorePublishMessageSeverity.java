package com.freshdirect.cms.ui.editor.publish.domain;

public enum StorePublishMessageSeverity {

    FAILURE(0),
    ERROR(1),
    WARNING(2),
    INFO(3),
    DEBUG(4),
    UNKNOWN(-1);

    public final int code;

    StorePublishMessageSeverity(int code) {
        this.code = code;
    }

    public static StorePublishMessageSeverity valueOf(int messageSeverity) {
        for (StorePublishMessageSeverity severity : values()) {
            if (severity.code == messageSeverity) {
                return severity;
            }
        }

        throw new IllegalArgumentException("Illegal severity number " + messageSeverity);
    }

}
