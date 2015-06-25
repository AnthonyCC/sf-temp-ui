package com.freshdirect.cms.ui.client.wysiwig.editor;

import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.google.gwt.user.client.Element;

public class EditorWYSIWYG extends LayoutContainer {

    private EditorIframe frame;
    private EditorControler controler;
    private boolean isDesignMode = true;
    /**
     * TextArea for html source code editing default to switch mode
     */
    private class WYSIWYGTextArea  extends TextArea {
    	public String getValue(){
    		return getValue(true);
    	}
    	
    	public String getValue(boolean switchMode){
    		if(switchMode && isDesignMode){
    			//Switching to source mode to get the value from IFRAME Automatically.
        		switchToSourceMode();
    		}
    		return super.getValue();
    	}
    }
    
    private WYSIWYGTextArea textArea;
    
    //exposing to follow the standards of FD.
    public TextArea getTextArea(){
    	return this.textArea;
    }

    protected EditorWYSIWYG(EditorControler controler) {
        this.controler = controler;
        frame = new EditorIframe(controler);
        textArea = new WYSIWYGTextArea();
        add(frame);
        setLayout(new FitLayout());
    }


    /**
     * Sets html to the textarea and the iframe
     *
     * @param html
     */
    protected void setHTML(String html) {
        if(textArea.isAttached()){
            textArea.setValue(html);
        } else {
            frame.setHTML(html);
        }
    }


    /**
     * Fetches html from the iframe and returns it
     *
     * @return
     */
    protected String getHTML() {
        if(textArea.isAttached()){
            return textArea.getValue();
        } else {
            return frame.getHTML();
        } 
    }


    /**
     * Returns the iframe
     *
     * @return
     */
    protected Element getFrameElement() {
        return frame.getElement();
    }

    protected EditorIframe getFrame() {
        return frame;
    }

    /**
     * Switches to design Mode
     * The iframe will be attached and the text area will be detached
     */
    protected void switchToDesignMode() {
        String newHtml = textArea.getValue(false);
        if(newHtml == null){
        	newHtml = "";
        }
        remove(textArea);
        add(frame);
        layout();
        setHTML(newHtml);
        isDesignMode = true;
    }


    /**
     * Switches to source code mode
     * The iframe will be detached and the text area will be attached
     */
    protected void switchToSourceMode() {
    	String htmlString = null;
    	if(!getHTML().equals("<p>&nbsp;</p>")){
    		htmlString = getHTML(); 
    	} 
    	textArea.setValue(htmlString);
        remove(frame);
        add(textArea);
        layout();
        isDesignMode = false;
    }

    /**
     * The edit mode of the iframe has to enabled every time the iframe is rendered.
     */
    @Override
    public boolean layout(){
        boolean result = super.layout();
        if(frame.isAttached()){
            frame.enableDesignMode();
        }
        return result;
    }
}