package com.freshdirect.cms.ui.client.wysiwig.toolbar.item;

import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.freshdirect.cms.ui.client.wysiwig.editor.EditorControler;
import com.freshdirect.cms.ui.client.wysiwig.i18n.EditorMessagesAccess;
import com.freshdirect.cms.ui.client.wysiwig.imagebundle.EditorImageBundleAccess;
import com.freshdirect.cms.ui.client.wysiwig.widget.ImageBundleButton;
import com.freshdirect.cms.ui.client.wysiwig.widget.toolbar.EditorCommand;
import com.freshdirect.cms.ui.client.wysiwig.widget.toolbar.SimpleComandPromptBox;

public class LinkButton extends ImageBundleButton implements Listener<BaseEvent> {

    private EditorControler controler;

    public LinkButton(EditorControler controler) {
        super(EditorImageBundleAccess.getEditorImageBundle().link());
    	this.controler = controler;
        setImagePrototype(EditorImageBundleAccess.getEditorImageBundle().link());
        addListener(Events.Select, this);
    }

    public void handleEvent(BaseEvent event) {
        controler.saveSelection();
        new SimpleComandPromptBox(controler, new EditorCommand() {
            public void exec(String[] params) {
                controler.restoreSelection();
                controler.doCreateLink(params[0]);
            }
        }, EditorMessagesAccess.getEditorMessages().createLinkTitle(), EditorMessagesAccess.getEditorMessages().createLinkFieldDescription(), EditorMessagesAccess.getEditorMessages().createLinkButtonTitle()).show();
    }
}
