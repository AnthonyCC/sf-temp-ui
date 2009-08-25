package com.freshdirect.cms.ui.client;

import java.util.Set;
import java.util.TreeSet;

import com.extjs.gxt.ui.client.data.BaseModel;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.layout.FillLayout;

/**
 * Emit Events.Select event when a type selected.
 * 
 * @author zsombor
 *
 */
public class ContentTypeSelectorPopup extends Window {

    Set<String> allowedTypes;
    ComboBox<BaseModel> selectorField;
    Button okButton;
    Button cancelButton;
    
    String selected;
    
    public ContentTypeSelectorPopup(Set<String> allowedTypes) {
        this.allowedTypes = allowedTypes;
        setLayout(new FillLayout());
        setModal(true);
        
        setHeading("Please select content type:");
        
        ListStore<BaseModel> store = new ListStore<BaseModel>();
        for (String allowedType : new TreeSet<String>(allowedTypes)) {
            BaseModel b = new BaseModel();
            b.set("value", allowedType);
            store.add(b);
        }
        
        selectorField = new ComboBox<BaseModel>();
        selectorField.setStore( store );
		selectorField.setValueField( "value" );
		selectorField.setDisplayField( "value" );
		selectorField.setValue( null );
		selectorField.setEditable( false );
        selectorField.setForceSelection( true );
        selectorField.setEmptyText( "Select content type" );        
        selectorField.setSimpleTemplate( "<img src=\"img/icons/{value}.gif\">{value}" );
        
        add(selectorField);
        
        
        cancelButton = new Button("Cancel");
        cancelButton.addListener(Events.OnClick, new Listener<BaseEvent>() {
            public void handleEvent(BaseEvent be) {
                hide();
            }
        });
        
        addButton(cancelButton);
        okButton = new Button("Ok");
        okButton.addListener(Events.OnClick, new Listener<BaseEvent>() {
            public void handleEvent(BaseEvent be) {
                BaseModel value = selectorField.getValue();
                if (value!=null) {
                    ContentTypeSelectorPopup.this.selected = value.get("value");
                    BaseEvent e = new BaseEvent(Events.Select);
                    e.setSource(ContentTypeSelectorPopup.this);
                    fireEvent(e.getType(), e);
                    hide();
                }
            }
        });

        addButton(okButton);
    }


    public String getSelectedType() {
        return selected;
    }
    
}
