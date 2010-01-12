package com.freshdirect.cms.ui.client.contenteditor;

import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.widget.Container;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.freshdirect.cms.ui.model.GwtContextualizedNodeI;

/**
 * 
 * @author segabor
 *
 */
public class ContentEditorFactory {
	/**
	 * Returns content editor
	 * 
	 * @param nodeData
	 * @param contextPath (optional)
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Container getEditor(GwtContextualizedNodeI cn ) {
        if ( !cn.getNodeData().hasTabs() ) {
        	// Tab-less form
        	LayoutContainer cont = new LayoutContainer();
        	cont.setScrollMode(Scroll.AUTO);
        	ContentForm fp = new ContentForm(cn);
            fp.addStyleName("fixed");
            cont.add(fp);            
            return cont;
        } else {
        	// Tabbed form
        	ContentEditor ce = new ContentEditor(cn);        	 
            ce.addStyleName("fixed");
        	
            return ce;
        }
	}
}
