package com.freshdirect.cms.ui.client;

import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.freshdirect.cms.ui.client.fields.OneToManyRelationField;
import com.freshdirect.cms.ui.model.GwtContentNode;
import com.freshdirect.cms.ui.model.GwtNodeData;

/**
 * Popup window for creating new content nodes.
 * 
 */
public class NewContentNodePopup extends Window {

    GwtNodeData nodeData;
    OneToManyRelationField field;
    boolean add;

    /**
     * If field is null, we wont add the new node to the workingset, and to the relation field.
     * @param result
     * @param field
     */
    public NewContentNodePopup(GwtNodeData result, OneToManyRelationField field) {
        super();
        this.nodeData = result;
        this.field = field;

        setHeading("New " + result.getNode().getType() + " - " + result.getNode().getKey());
        setLayout(new BorderLayout());
        setSize(840, 600);
        setModal(true);
        setMaximizable(true);

        ContentPanel panel = new ContentPanel();
        panel.setHeaderVisible(false);
        panel.setScrollMode(Scroll.AUTO);
        panel.setBorders(false);
        panel.setBodyBorder(false);

        result.setupUI(panel, null);
        add(panel, new BorderLayoutData(LayoutRegion.CENTER));

        Button cancelButton = new Button("Cancel");
        cancelButton.addListener(Events.OnClick, new Listener<BaseEvent>() {

            @Override
            public void handleEvent(BaseEvent be) {
                hide();
            }
        });
        addButton(cancelButton);

        Button okButton = new Button("Ok");
        okButton.addListener(Events.OnClick, new Listener<BaseEvent>() {

            @Override
            public void handleEvent(BaseEvent be) {
                GwtNodeData nodeData = NewContentNodePopup.this.nodeData;
                nodeData.collectValuesFromFields();
                
                OneToManyRelationField f = NewContentNodePopup.this.field;
                if (f != null) {
                    GwtContentNode node = nodeData.getNode();
                    WorkingSet.add(node);
    
                    f.addOneToManyModel(node.getType(), node.getKey(), node.getType() + " : " + node.getLabel(), nodeData);
                }
                hide();
            }
        });
        addButton(okButton);
    }

}
