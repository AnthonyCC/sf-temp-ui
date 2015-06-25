package com.freshdirect.cms.ui.client.wysiwig.toolbar.item;

import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.freshdirect.cms.ui.client.wysiwig.editor.EditorControler;
import com.freshdirect.cms.ui.client.wysiwig.i18n.EditorMessagesAccess;
import com.freshdirect.cms.ui.client.wysiwig.imagebundle.EditorImageBundleAccess;
import com.freshdirect.cms.ui.client.wysiwig.widget.ImageBundleButton;

public class BackgroundColorButton extends ImageBundleButton implements Listener<BaseEvent> {

    private EditorControler controler;
    private ColourPickerDialog bgPicker;

    public BackgroundColorButton(EditorControler c) {
    	super(EditorImageBundleAccess.getEditorImageBundle().script_palette());
    	this.controler = c;
        setImagePrototype(EditorImageBundleAccess.getEditorImageBundle().script_palette());

        this.bgPicker = new ColourPickerDialog();
        this.bgPicker.cancelText = EditorMessagesAccess.getEditorMessages().cancel();
        this.bgPicker.okText = EditorMessagesAccess.getEditorMessages().ok();
        this.bgPicker.setHeading(EditorMessagesAccess.getEditorMessages().backgroundTitle());
        this.bgPicker.addColourSelectedListener(new Listener<BaseEvent> (){
            public void handleEvent(BaseEvent event) {
                String rgb = bgPicker.getColour();
                controler.restoreSelection();
                controler.doBackgroundColor(rgb);
                controler.doFocus();
            }
        });

        this.addListener(Events.Select, this);
    }

    public void handleEvent(BaseEvent event) {
        controler.saveSelection();
        bgPicker.show();

    }
}