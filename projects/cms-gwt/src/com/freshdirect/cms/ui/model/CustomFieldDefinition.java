package com.freshdirect.cms.ui.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CustomFieldDefinition implements Serializable {

    private static final long serialVersionUID = 1L;

    public enum Type {
        ProductConfigEditor, PrimaryHomeSelection, VariationMatrix, Grid, GmapsLocation, MonthDay
    }

    Type                        type;

    List<String> gridColumns;

    /** just for the serialization */
    public CustomFieldDefinition() {
    }

    public CustomFieldDefinition(Type t) {
        this.type = t;
    }

    public CustomFieldDefinition(String type) {
        this.type = Type.valueOf(type);
    }

    public List<String> getGridColumns() {
        return gridColumns;
    }

    public void addColumn(String columnName) {
        if (gridColumns == null) {
            gridColumns = new ArrayList<String>();
        }
        gridColumns.add(columnName);
    }
    
    
    public Type getType() {
        return type;
    }
}
