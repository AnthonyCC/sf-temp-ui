package com.freshdirect.cms.ui.editor.reports;


public enum ReportColumnType {
    SUFFIX_ATTR("_ATTRIBUTE$"),
    SUFFIX_KEY("_KEY$"),
    CLASS_COL("CLASS$"),
    GROUP_COL("GROUP$");

    public String fragment;

    ReportColumnType(String fragment) {
        this.fragment = fragment;
    }
}
