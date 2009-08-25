package com.freshdirect.cms.ui.model;

import com.freshdirect.cms.ui.client.nodetree.ContentNodeModel;

public class OneToManyModel extends ContentNodeModel {

    private static final long serialVersionUID = 1L;

    public OneToManyModel() {
        super();
    }

    public OneToManyModel(String type, String key, String label, int index) {
        super(type, label, key);
        setIndex(index);
    }

    public int getIndex() {
        Integer indx = get("idx");
        return indx != null ? indx.intValue() : -1;
    }

    public String getValue() {
        return getKey();
    }

    public void setIndex(int index) {
        set("idx", index);
    }

    @Override
    public String toString() {
        return "OneToManyModel[" + getKey() + ',' + getType() + ',' + getLabel() + ',' + getIndex() + ',' + getValue() + ']';
    }

}
