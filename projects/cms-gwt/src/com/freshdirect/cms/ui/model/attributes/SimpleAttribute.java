package com.freshdirect.cms.ui.model.attributes;

import java.io.Serializable;

public class SimpleAttribute<T extends Serializable> extends BaseAttribute implements ModifiableAttributeI, Serializable {

    private static final long serialVersionUID = 6404463953818866485L;

    private String            type;
    private T                 value;

    public SimpleAttribute() {
        type = "string";
    }

    public SimpleAttribute(String t, T v, String l) {
        type = t;
        value = v;
        label = l;
    }

    public String getType() {
        return type;
    }

    public T getValue() {
        return value;
    }

    public void setType(String t) {
        type = t;
    }


    @Override
    public String toString() {
        return "SimpleAttribute[" + type + ',' + label + ',' + value + ']';
    }

    
}
