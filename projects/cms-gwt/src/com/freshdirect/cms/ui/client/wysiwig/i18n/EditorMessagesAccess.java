package com.freshdirect.cms.ui.client.wysiwig.i18n;

import com.google.gwt.core.client.GWT;

public class EditorMessagesAccess {

    private static EditorMessages editorMessages;

    /**
     * Returns an implementation of @see EditorMessages 
     * @return
     */
    public static EditorMessages getEditorMessages() {
        if(editorMessages == null){
            editorMessages = GWT.create(EditorMessages.class);
        }
        return editorMessages;
    }

    /**
     * Protecting this class from being initiated
     */
    private EditorMessagesAccess(){}
}