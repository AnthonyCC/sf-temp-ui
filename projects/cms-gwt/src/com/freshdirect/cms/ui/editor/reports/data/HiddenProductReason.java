package com.freshdirect.cms.ui.editor.reports.data;


public enum HiddenProductReason {
    VISIBLE("Visible"),
    ORPHAN("Orphan Product"),
    HIDDEN_BY_HIDE_URL("Hidden by HIDE_URL"),
    HIDDEN_BY_REDIRECT_URL("Hidden by REDIRECT_URL"),
    ARCHIVED("Archived Product");

    private final String description;

    HiddenProductReason(String description) {
        this.description = description;
    }

    public boolean isHidden() {
        return VISIBLE != this;
    }

    public String getDescription() {
        return description;
    }
}
