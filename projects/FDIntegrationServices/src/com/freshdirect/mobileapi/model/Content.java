package com.freshdirect.mobileapi.model;

import com.freshdirect.fdstore.content.CategoryModel;

public class Content {
    String name;

    String id;

    public static Category wrap(CategoryModel model) {
        Category result = new Category();
        result.name = model.getContentName();
        result.id = model.getContentKey().getId();
        return result;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
