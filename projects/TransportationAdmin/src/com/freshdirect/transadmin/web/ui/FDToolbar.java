package com.freshdirect.transadmin.web.ui;

import java.util.Iterator;
import java.util.List;

import org.extremecomponents.table.bean.Export;
import org.extremecomponents.table.core.TableModel;
import org.extremecomponents.table.view.DefaultToolbar;
import org.extremecomponents.table.view.html.BuilderConstants;
import org.extremecomponents.table.view.html.BuilderUtils;
import org.extremecomponents.table.view.html.TableBuilder;
import org.extremecomponents.table.view.html.ToolbarBuilder;
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
	 
	    protected void columnRight(HtmlBuilder html, TableModel model) {
	        boolean showPagination = BuilderUtils.showPagination(model);
	        boolean showExports = BuilderUtils.showExports(model);

	        FDStatusBarBuilder toolbarBuilder = new FDStatusBarBuilder(html, model);

	        html.td(2).align("right").close();

	        html.table(2).border("0").cellPadding("0").cellSpacing("1").styleClass(BuilderConstants.TOOLBAR_CSS).close();

	        html.tr(3).close();

	        if (showPagination) {

	            html.td(4).close();
	            toolbarBuilder.firstPageItemAsImage();
	            html.tdEnd();

	            html.td(4).close();
	            toolbarBuilder.prevPageItemAsImage();
	            html.tdEnd();

	            html.td(4).close();
	            toolbarBuilder.nextPageItemAsImage();
	            html.tdEnd();

	            html.td(4).close();
	            toolbarBuilder.lastPageItemAsImage();
	            html.tdEnd();

	            html.td(4).close();
	            toolbarBuilder.separator();
	            html.tdEnd();

	            html.td(4).style("width:20px").close();
	            html.newline();
	            html.tabs(4);
	            toolbarBuilder.rowsDisplayedDroplist();
	            html.img();
	            html.src(BuilderUtils.getImage(model, BuilderConstants.TOOLBAR_ROWS_DISPLAYED_IMAGE));
	            html.style("border:0");
	            html.alt("Rows Displayed");
	            html.xclose();
	            html.tdEnd();

	            if (showExports) {
	                html.td(4).close();
	                toolbarBuilder.separator();
	                html.tdEnd();
	            }
	        }

	        if (showExports) {
	            Iterator iterator = model.getExportHandler().getExports().iterator();
	            for (Iterator iter = iterator; iter.hasNext();) {
	                html.td(4).close();
	                Export export = (Export) iter.next();
	                toolbarBuilder.exportItemAsImage(export);
	                html.tdEnd();
	            }
	        }

	        html.trEnd(3);

	        html.tableEnd(2);
	        html.newline();
	        html.tabs(2);

	        html.tdEnd();
	    }



}
