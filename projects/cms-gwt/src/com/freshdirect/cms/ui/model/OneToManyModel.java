package com.freshdirect.cms.ui.model;

import com.freshdirect.cms.ui.client.NewContentNodePopup;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Widget;

public class OneToManyModel extends ContentNodeModel {

    private static final long serialVersionUID = 1L;

    public final static OneToManyModel NULL_MODEL = new OneToManyModel(GwtContentNode.NULL_TYPE, GwtContentNode.NULL_ID, "Empty list", 0); 

    GwtNodeData newNodeData;
    
    public OneToManyModel() {
        super();
    }

    public OneToManyModel(String type, String key, String label, int index) {
        super(type, label, key);
        setIndex(index);
    }
    
    
    public boolean isNewlyCreated() {
        return newNodeData != null;
    }
    
    public void setNewNodeData(GwtNodeData newNodeData) {
        this.newNodeData = newNodeData;
    }
    
    public GwtNodeData getNewNodeData() {
        return newNodeData;
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

    
    @Override
    public Widget renderLinkComponent() {
        if (isNewlyCreated()) {
            Anchor a = new Anchor(getLabel());
            a.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    NewContentNodePopup ncn = new NewContentNodePopup(OneToManyModel.this.newNodeData, null);
                    ncn.show();
                }
            });
            return a;
        }
        return super.renderLinkComponent();
    }

}
