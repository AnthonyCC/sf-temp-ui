package com.freshdirect.mobileapi.model;

import com.freshdirect.fdstore.content.DepartmentModel;

public class Department {
    String name;

    String id;

    public static Department wrap(DepartmentModel model) {
        Department result = new Department();
        result.name = model.getFullName();
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
