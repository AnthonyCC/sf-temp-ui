package com.freshdirect.cms.ui.client.wysiwig.toolbar.item;

import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.freshdirect.cms.ui.client.wysiwig.editor.EditorControler;
import com.freshdirect.cms.ui.client.wysiwig.i18n.EditorMessagesAccess;
import com.freshdirect.cms.ui.client.wysiwig.imagebundle.EditorImageBundleAccess;
import com.freshdirect.cms.ui.client.wysiwig.widget.ImageBundleButton;
import com.freshdirect.cms.ui.client.wysiwig.widget.toolbar.EditorCommand;
import com.freshdirect.cms.ui.client.wysiwig.widget.toolbar.SimpleComandPromptBox;

public class InsertImageButton extends ImageBundleButton implements Listener<BaseEvent> {
  
    private EditorControler controler;

    public InsertImageButton(EditorControler controler) {
    	super(EditorImageBundleAccess.getEditorImageBundle().image_add());
        this.controler = controler;
        setImagePrototype(EditorImageBundleAccess.getEditorImageBundle().image_add());
        addListener(Events.Select, this);
    }

    public void handleEvent(BaseEvent event) {
        new SimpleComandPromptBox(controler, new EditorCommand() {
        public void exec(String[] params) {
          controler.saveSelection();
          controler.doInsertImage(params[0]);
        }
      }, EditorMessagesAccess.getEditorMessages().insertImageTitle(), EditorMessagesAccess.getEditorMessages().insertImageFieldDescription(), EditorMessagesAccess.getEditorMessages().insertImageButtonTitle()).show();
    }
}
