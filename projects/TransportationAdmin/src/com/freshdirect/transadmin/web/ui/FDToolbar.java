package com.freshdirect.transadmin.web.ui;

import java.util.Iterator;
import java.util.List;

import org.extremecomponents.table.core.TableModel;
import org.extremecomponents.table.view.DefaultToolbar;
import org.extremecomponents.table.view.html.TableBuilder;
import org.extremecomponents.util.HtmlBuilder;

public class FDToolbar extends DefaultToolbar {
	
	 public FDToolbar(HtmlBuilder html, TableModel model) {
	     super(html, model);
	 }
	 
	 protected void columnLeft(HtmlBuilder html, TableModel model) {
	        html.td(2).nowrap().close();
	        new TableBuilder(html, model).title();
	        html.tdEnd();
	        html.td(2).styleClass("screenmessages").close();	        
	        html.append(getMessages(model));
	        html.tdEnd();
	        
	 }
	 
	 private String getMessages(TableModel model) {
		 
		List messageList = (List)model.getContext().getSessionAttribute("messages");
        StringBuffer strBuf = new StringBuffer();
        if(messageList != null) {
        	Iterator tmpIterator = messageList.iterator();
        	while(tmpIterator.hasNext()) {
        		strBuf.append(tmpIterator.next());
        	}
        }
        model.getContext().removeSessionAttribute("messages");
        return strBuf.toString();
	 }

}
