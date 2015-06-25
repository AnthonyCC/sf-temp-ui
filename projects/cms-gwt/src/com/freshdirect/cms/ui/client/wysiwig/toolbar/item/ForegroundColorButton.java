package com.freshdirect.cms.ui.client.wysiwig.toolbar.item;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.Events;
import com.freshdirect.cms.ui.client.wysiwig.editor.EditorControler;
import com.freshdirect.cms.ui.client.wysiwig.i18n.EditorMessagesAccess;
import com.freshdirect.cms.ui.client.wysiwig.imagebundle.EditorImageBundleAccess;
import com.freshdirect.cms.ui.client.wysiwig.widget.ImageBundleButton;

public class ForegroundColorButton extends ImageBundleButton implements Listener<BaseEvent> {

    private EditorControler controler;
    private ColourPickerDialog fgPicker;

    public ForegroundColorButton(EditorControler c) {
    	super(EditorImageBundleAccess.getEditorImageBundle().palette());
    	this.controler = c;
        setImagePrototype(EditorImageBundleAccess.getEditorImageBundle().palette());
        this.fgPicker = new ColourPickerDialog();
        this.fgPicker.cancelText = EditorMessagesAccess.getEditorMessages().cancel();
        this.fgPicker.okText = EditorMessagesAccess.getEditorMessages().ok();
        this.fgPicker.setHeading(EditorMessagesAccess.getEditorMessages().foregroundTitle());
        this.fgPicker.addColourSelectedListener( new Listener<BaseEvent> (){

            public void handleEvent(BaseEvent event) {
                String rgb = fgPicker.getColour();
                controler.restoreSelection();
                controler.doForeColor(rgb);
                controler.doFocus();
            }
        });
        addListener(Events.Select,  this);
    }

    public void handleEvent(BaseEvent event) {
        controler.saveSelection();
        fgPicker.show();
    }
}
