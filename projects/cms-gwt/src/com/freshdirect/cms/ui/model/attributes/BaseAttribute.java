package com.freshdirect.cms.ui.model.attributes;

import java.io.Serializable;

import com.extjs.gxt.ui.client.widget.form.Field;

public abstract class BaseAttribute implements Serializable, ContentNodeAttributeI {

    private static final long serialVersionUID = 1L;
    
    
    protected String               label;
    protected boolean              readonly;
    protected boolean              inheritable;

    protected transient Field<? extends Serializable> fieldObject;
    
    public BaseAttribute() {    	
    }
    
    public BaseAttribute( BaseAttribute attr ) {
    	this.label = attr.label;
    	this.readonly = attr.readonly;
    	this.inheritable = attr.inheritable;
    	this.fieldObject = attr.fieldObject;
    }
    
    public Field<? extends Serializable> getFieldObject() {
        return fieldObject;
    }
    
    public void setFieldObject(Field<? extends Serializable> obj) {
        this.fieldObject = obj;
    }

    public final boolean isReadonly() {
        return readonly;
    }

    public final void setReadonly(boolean readonly) {
        this.readonly = readonly;
    }

    public final boolean isInheritable() {
        return inheritable;
    }
    
    public final void setInheritable(boolean inheritable) {
        this.inheritable = inheritable;
    }

    public final String getLabel() {
        return label;
    }

    
    public final void setLabel(String label) {
        this.label = label;
    }
    
}
