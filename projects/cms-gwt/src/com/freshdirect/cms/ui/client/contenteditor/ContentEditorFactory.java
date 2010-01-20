package com.freshdirect.cms.ui.client.contenteditor;

import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.widget.Container;
import com.freshdirect.cms.ui.model.GwtNodeData;

/**
 * 
 * @author segabor
 *
 */
public class ContentEditorFactory {
	/**
	 * Returns content editor
	 * 
	 * @param contextualizedNode
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Container getEditor(GwtNodeData cn ) {
        if ( !cn.hasTabs() ) {
        	ContentForm fp = new ContentForm(cn);
        	fp.setScrollMode(Scroll.AUTO);
            return fp;
        } else {
        	// Tabbed form
        	ContentEditor ce = new ContentEditor(cn);        	 
            ce.addStyleName("fixed");
        	
            return ce;
        }
	}
}
