package com.freshdirect.cms.ui.client.fields;

import java.util.Set;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FieldEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.button.IconButton;
import com.extjs.gxt.ui.client.widget.form.AdapterField;
import com.extjs.gxt.ui.client.widget.form.LabelField;
import com.extjs.gxt.ui.client.widget.form.MultiField;
import com.extjs.gxt.ui.client.widget.layout.ColumnData;
import com.extjs.gxt.ui.client.widget.layout.ColumnLayout;
import com.extjs.gxt.ui.client.widget.tips.ToolTipConfig;
import com.freshdirect.cms.ui.client.nodetree.ContentTreePopUp;
import com.freshdirect.cms.ui.model.ContentNodeModel;
import com.freshdirect.cms.ui.model.GwtNodePermission;
import com.freshdirect.cms.ui.model.GwtNodePermission.EvalResult;
import com.google.gwt.user.client.Element;

public class OneToOneRelationField extends MultiField<ContentNodeModel> {

    private IconButton relationButton;
    private IconButton deleteButton;

    private LabelField valueField;

    private Set<String> allowedTypes;

    private AdapterField af;
    private int buttonWidth = 0;

    private GwtNodePermission permissions;

    protected boolean readonly;

    final static int MAIN_LABEL_WIDTH = 415;

    public OneToOneRelationField(Set<String> aTypes, GwtNodePermission permission) {
        super();
        allowedTypes = aTypes;
        this.permissions = permission;
        initialize();

        setReadOnly(permission.isReadonly());
    }

    @Override
    public ContentNodeModel getValue() {
        return value;
    }

    @Override
    public void setValue(ContentNodeModel model) {
        if (model != null) {
            model = new ContentNodeModel(model);
            valueField.setValue(model.renderLink(true));
            valueField.setToolTip(model.getPreviewToolTip());
        } else {
            valueField.setValue("");
        }
        super.setValue(model);
    }

    private void initialize() {

        LayoutContainer cp = new LayoutContainer();
        cp.setLayout(new ColumnLayout());
        cp.setBorders(true);

        EvalResult result = permissions.evaluate(readonly);
        boolean isEnabled = result != EvalResult.READONLY;

        if (isEnabled) {
            relationButton = new IconButton("rel-button");
            relationButton.setToolTip("Change relationship");

            relationButton.addListener(Events.OnClick, new Listener<BaseEvent>() {

                public void handleEvent(BaseEvent be) {
                    final ContentTreePopUp popup = ContentTreePopUp.getInstance(allowedTypes, false);
                    popup.setHeading(getFieldLabel());
                    popup.addListener(Events.Select, new Listener<BaseEvent>() {

                        public void handleEvent(BaseEvent be) {
                            ContentNodeModel model = popup.getSelectedItem();
                            setValue(model);
                            fireEvent(Events.Change, new FieldEvent(OneToOneRelationField.this));
                        }
                    });
                    popup.show();
                }
            });
            cp.add(relationButton, new ColumnData(20.0));
            buttonWidth += 20;
        }

        valueField = new LabelField();
        cp.add(valueField);

        if (isEnabled) {
            deleteButton = new IconButton("delete-button");
            deleteButton.setToolTip(new ToolTipConfig("DELETE", "Delete relation."));

            deleteButton.addListener(Events.OnClick, new Listener<BaseEvent>() {

                @Override
                public void handleEvent(BaseEvent be) {
                    if (getValue() != null) {
                        setValue(null);
                        fireEvent(Events.Change, new FieldEvent(OneToOneRelationField.this));
                    }
                }
            });

            cp.add(deleteButton, new ColumnData(20.0));
            buttonWidth += 20;
        }

        af = new AdapterField(cp);
        af.setResizeWidget(true);

        if (!isEnabled) {
            af.setReadOnly(true);
        }

        add(af);

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
        this.readonly = readOnly;

        super.setReadOnly(readOnly);

        if (relationButton != null) {
            relationButton.setEnabled(!readOnly);
        }
        if (deleteButton != null) {
            deleteButton.setEnabled(!readOnly);
        }
    }

    @Override
    public void setWidth(int width) {
        af.setWidth(width);
        valueField.setWidth(width - buttonWidth - 2);
        super.setWidth(width);
    }

    @Override
    protected void onRender(Element target, int index) {
        originalValue = getValue();
        super.onRender(target, index);
    }

    @Override
    public void reset() {
        setValue(originalValue);
    }
}
