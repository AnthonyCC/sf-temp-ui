package com.freshdirect.cms.ui.editor.reports.data;


public enum HiddenProductReason {
    NOT_HIDDEN(false, "Visible"),
    ORPHAN(true, "Orphan Product"),
    HIDDEN_BY_HIDE_URL(true, "Hidden by HIDE_URL"),
    HIDDEN_BY_REDIRECT_URL(true, "Hidden by REDIRECT_URL"),
    ARCHIVED(true, "Archived Product");

    private final boolean hidden;

    private final String description;

    HiddenProductReason(boolean hidden, String description) {
        this.hidden = hidden;
        this.description = description;
    }

    public boolean isHidden() {
        return hidden;
    }

    public String getDescription() {
        return description;
    }
}
