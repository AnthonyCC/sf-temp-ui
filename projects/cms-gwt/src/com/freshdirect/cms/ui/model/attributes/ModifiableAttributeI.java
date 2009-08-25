package com.freshdirect.cms.ui.model.attributes;


public interface ModifiableAttributeI extends ContentNodeAttributeI {

    public void setLabel(String label);

    public void setReadonly(boolean flag);
    
    public void setInheritable(boolean flag);

}
