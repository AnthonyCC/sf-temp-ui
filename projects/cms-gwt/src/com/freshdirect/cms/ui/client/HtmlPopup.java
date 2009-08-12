package com.freshdirect.cms.ui.client;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.HtmlContainer;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;

public class HtmlPopup extends Window {

    public HtmlPopup(String title, String content) {
        setHeading(title);
        setWidth(600);
        add(new HtmlContainer(content));
        addButton(new Button("Close", new SelectionListener<ButtonEvent> () {
            @Override
            public void componentSelected(ButtonEvent ce) {
                HtmlPopup.this.hide();
            }
        }));
    }
}
