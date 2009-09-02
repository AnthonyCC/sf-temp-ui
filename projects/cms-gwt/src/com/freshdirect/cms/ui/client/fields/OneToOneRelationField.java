package com.freshdirect.cms.ui.client.fields;

import java.util.HashSet;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.button.IconButton;
import com.extjs.gxt.ui.client.widget.form.AdapterField;
import com.extjs.gxt.ui.client.widget.form.LabelField;
import com.extjs.gxt.ui.client.widget.form.MultiField;
import com.extjs.gxt.ui.client.widget.tips.ToolTipConfig;
import com.freshdirect.cms.ui.client.nodetree.ContentNodeModel;
import com.freshdirect.cms.ui.client.nodetree.ContentTreePopUp;

public class OneToOneRelationField extends MultiField<ContentNodeModel> {

    private IconButton relationButton;
    private IconButton deleteButton;
    
    private LabelField valueField;

    private ContentNodeModel value;
    private HashSet<String> allowedTypes;

    public OneToOneRelationField(HashSet<String> aTypes) {
        super();
        allowedTypes = aTypes;
        initialize();
    }

    @Override
    public ContentNodeModel getValue() {
        return value;
    }
	
    @Override
    public void setValue(ContentNodeModel model) {
        this.value = model;

        if (value != null) {
            valueField.setValue(model.renderLink(true));
            valueField.setToolTip(model.getPreviewToolTip());
        } else {
            valueField.setValue("");
        }
    }
	
    private void initialize() {
        relationButton = new IconButton("rel-button");
        relationButton.setToolTip("Change relationship");
        relationButton.addListener(Events.OnClick, new Listener<BaseEvent>() {
            public void handleEvent(BaseEvent be) {
                final ContentTreePopUp popup = ContentTreePopUp.getInstance(allowedTypes);
                popup.setHeading(getFieldLabel());
                popup.addListener(Events.Select, new Listener<BaseEvent>() {
                    public void handleEvent(BaseEvent be) {
                        setValue(popup.getSelected());
                        fireEvent(AttributeChangeEvent.TYPE, new AttributeChangeEvent(OneToOneRelationField.this));
                    }
                });
                popup.show();
            }
        });
        add(new AdapterField(relationButton));

        valueField = new LabelField();
        add(valueField);

        deleteButton = new IconButton("delete-button");
        deleteButton.setToolTip(new ToolTipConfig("DELETE", "Delete relation."));
        deleteButton.addListener(Events.OnClick, new Listener<BaseEvent>() {
            @Override
            public void handleEvent(BaseEvent be) {
                OneToOneRelationField.this.setValue(null);
            }
        });
        add(new AdapterField(deleteButton));
    }
	
    @Override
    public void disable() {
        super.disable();
        if (relationButton != null) {
            relationButton.disable();
        }
        if (deleteButton != null) {
        	deleteButton.disable();
        }
    }

    @Override
    public void enable() {
        super.enable();
        if (relationButton != null) {
            relationButton.enable();
        }
        if (deleteButton != null) {
        	deleteButton.enable();
        }
    }
        
    @Override
    public void setReadOnly(boolean readOnly) {
        super.setReadOnly(readOnly);
        setEnabled(!readOnly);
    }	
}
