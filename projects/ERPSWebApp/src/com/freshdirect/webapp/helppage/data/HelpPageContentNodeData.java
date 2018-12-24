package com.freshdirect.webapp.helppage.data;

import java.io.Serializable;

public class HelpPageContentNodeData implements Serializable{

    private static final long serialVersionUID = 6338409758699630265L;
    private final String id;
    private final String name;

    public HelpPageContentNodeData(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
