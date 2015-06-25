package com.freshdirect.cms.ui.client.wysiwig.imagebundle;

import com.google.gwt.core.client.GWT;

public class EditorImageBundleAccess {

    private static EditorImageBundle editorImageBundle;

    /**
     *
     * @return
     */
    public static EditorImageBundle getEditorImageBundle(){
        if(editorImageBundle ==null){
            editorImageBundle = GWT.create(EditorImageBundle.class);
        }
        return editorImageBundle;
    }

    /**
     * Protectes this class from being initiated 
     */
    private EditorImageBundleAccess(){}
}
