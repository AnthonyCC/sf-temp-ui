package com.freshdirect.mobileapi.model.data;

public class WhatsGoodCategory {

    public WhatsGoodCategory(String id, String name, String headerImage) {
        this.id = id;
        this.name = name;
        this.headerImage = headerImage;
    }

    private String name;

    private String id;

    private String headerImage;

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getHeaderImage() {
        return headerImage;
    }

    public void prependHeaderImageUrl(String hostname) {
        this.headerImage = hostname + headerImage;
    }

}
