package com.freshdirect.cms.ui.client.wysiwig.editor;

import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.freshdirect.cms.ui.client.wysiwig.widget.toolbar.EditorToolBar;

/**
 * Rich Text Editor Widget.
 *
 * @author pavel.jbanov
 * @autor peter.quiel
 */
public class RteEditor extends ContentPanel {

    private EditorWYSIWYG wysiwyg;
    private EditorControler controler;

    // internal status
    private boolean initialized = false;

    private EditorToolBar editorToolBar;

    /**
     * This temp storage is used to store html if the editor has not been initilized
     */
    private String tmpHTMLStorage = null;

    private boolean inDesignMode = true;
    
    public RteEditor(){
    	controler = new EditorControler(this);
        wysiwyg = new EditorWYSIWYG(controler);
        add(wysiwyg);
    }

    /**
     * @return HTML
     */
    public String getHTML() {
        return getEditorWYSIWYG().getHTML();
    }


    /**
     * set editor HTML.
     *
     * @param html HTML
     */
    public void setHTML(String html) {
        getEditorWYSIWYG().setHTML(html);
    }


    /**
     * Returns the current @see EditorToolBar
     * @return
     */
    public EditorToolBar getEditorToolBar() {
        return editorToolBar;
    }

    /**
     * Sets the editor tool bar
     * @param editorToolBar
     */
    public void setEditorToolBar(EditorToolBar editorToolBar) {
        this.editorToolBar = editorToolBar;
        this.setTopComponent(this.editorToolBar);
    }

    /**
     * Retruns the controller of this editor.
     *
     * @return
     */
    public EditorControler getController() {
        return controler;
    }


    /**
     * Toggles the view between source mode and wysiwig mode
     */
    protected void toggleView() {
        if (!this.inDesignMode) {
            this.switchToDesignMode();
        } else {
            this.switchToSourceMode();
        }
    }

    /**
     * Changes the mode of the edtor from source code mode to design mode / wysiwig mode
     */
    protected void switchToDesignMode() {
        if (!this.inDesignMode) {
            editorToolBar.switchToDesignMode();
            wysiwyg.switchToDesignMode();
            this.inDesignMode = true;
        }
    }


    /**
     * Changes the mode of the editor from desgin mode / wysiwig mode to source code mode.
     */
    protected void switchToSourceMode() {
        if (this.inDesignMode) {
            editorToolBar.switchToSourceMode();
            wysiwyg.switchToSourceMode();
            this.inDesignMode = false;
        }
    }

    /**
     * @return editor WYSIWYG widget
     */
    protected EditorWYSIWYG getEditorWYSIWYG() {
        return wysiwyg;
    }
    
    public Field<String> getTextArea(){
    	return getEditorWYSIWYG().getTextArea();
    }
}