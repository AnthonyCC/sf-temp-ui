package com.freshdirect.storeapi.content;

public enum EnumCatalogType {

    ALL("All"),
    RESIDENTAL("Residental"),
    CORPORATE("Corporate"),
    EMPTY("");

    private String label;

    private EnumCatalogType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public boolean isResidental() {
        return this == ALL || this == RESIDENTAL;
    }

    public boolean isCorporate() {
        return this == ALL || this == CORPORATE;
    }
}
