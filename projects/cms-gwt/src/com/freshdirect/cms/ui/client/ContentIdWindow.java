package com.freshdirect.cms.ui.client;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FillLayout;

public class ContentIdWindow extends Window {

    TextField<String> textField;
    
    public ContentIdWindow(String id, String title) {
        
        setLayout(new FillLayout());
        setModal(true);
        
        setHeading(title);

        textField = new TextField<String>();
        textField.setValue(id);
        add(textField);
        
        Button cancelButton = new Button("Cancel");
        cancelButton.addListener(Events.OnClick, new Listener<BaseEvent>() {
            public void handleEvent(BaseEvent be) {
                hide();
            }
        });
        
        addButton(cancelButton);
        Button okButton = new Button("Ok");
        okButton.addListener(Events.OnClick, new Listener<BaseEvent>() {
            public void handleEvent(BaseEvent be) {
                String value = textField.getValue();
                if (value!=null && value.trim().length()>0) {
                    BaseEvent e = new BaseEvent(Events.Select);
                    e.setSource(ContentIdWindow.this);
                    fireEvent(e.getType(), e);
                    hide();
                }
            }
        });
        addButton(okButton);
    }
    
    public String getContentId() {
        return textField.getValue();
    }

}
