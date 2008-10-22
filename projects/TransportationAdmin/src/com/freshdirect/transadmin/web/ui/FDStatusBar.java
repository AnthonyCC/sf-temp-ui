package com.freshdirect.transadmin.web.ui;

import org.extremecomponents.table.core.TableModel;
import org.extremecomponents.table.view.DefaultStatusBar;
import org.extremecomponents.table.view.html.BuilderConstants;
import org.extremecomponents.table.view.html.BuilderUtils;
import org.extremecomponents.util.HtmlBuilder;

import com.freshdirect.transadmin.util.TransStringUtil;

public class FDStatusBar extends DefaultStatusBar {
	
	public FDStatusBar(HtmlBuilder html, TableModel model) {
        super(html, model);
    }
	protected void columnRight(HtmlBuilder html, TableModel model) {
        boolean filterable = BuilderUtils.filterable(model);
        if (!filterable) {
            return;
        }

        html.td(4).styleClass(BuilderConstants.FILTER_BUTTONS_CSS).close();

        html.img();
        html.src(BuilderUtils.getImage(model, BuilderConstants.TOOLBAR_FILTER_ARROW_IMAGE));
        html.style("border:0");
        html.alt("Arrow");
        html.xclose();

        html.nbsp();

        FDStatusBarBuilder toolbarBuilder = new FDStatusBarBuilder(html, model);
        toolbarBuilder.filterItemAsImage();

        html.nbsp();

        toolbarBuilder.clearItemAsImage();
        
        html.nbsp();
        
        String hasAdd = (String)model.getContext().getPageAttribute("HAS_ADDBUTTON");
        
        String hasConfirm = (String)model.getContext().getPageAttribute("HAS_CONFIRMBUTTON");
        
        String hasDelete = (String)model.getContext().getPageAttribute("HAS_DELETEBUTTON");
        
        String hasCopy = (String)model.getContext().getPageAttribute("HAS_COPYBUTTON");
        
        String hasGeocode = (String)model.getContext().getPageAttribute("HAS_GEOCODEBUTTON");
        
        String hasUpdate = (String)model.getContext().getPageAttribute("HAS_UPDATEBUTTON");
        
        String hasSend = (String)model.getContext().getPageAttribute("HAS_SENDBUTTON");
        
        if(hasAdd == null || TransStringUtil.isEmpty(hasAdd)
        	|| hasAdd.equalsIgnoreCase("TRUE")) {

	        toolbarBuilder.addItemAsImage();
	        
	        html.nbsp();
        }
        
        if(hasConfirm != null && hasConfirm.equalsIgnoreCase("TRUE")) {

    	    toolbarBuilder.confirmItemAsImage();
    	     
    	    html.nbsp();
        }
        
        if(hasCopy != null && hasCopy.equalsIgnoreCase("TRUE")) {

    	    toolbarBuilder.copyItemAsImage();
    	     
    	    html.nbsp();
        }
        
        if(hasGeocode != null && hasGeocode.equalsIgnoreCase("TRUE")) {

    	    toolbarBuilder.geocodeItemAsImage();
    	     
    	    html.nbsp();
        }
        
        if(hasUpdate != null && hasUpdate.equalsIgnoreCase("TRUE")) {

    	    toolbarBuilder.updateItemAsImage();
    	     
    	    html.nbsp();
        }
        
        if(hasSend != null && hasSend.equalsIgnoreCase("TRUE")) {

    	    toolbarBuilder.sendItemAsImage();
    	     
    	    html.nbsp();
        }
        
        if(!"FALSE".equalsIgnoreCase(hasDelete)) {

        	toolbarBuilder.deleteItemAsImage();
        }

        

        html.tdEnd();
    }
}
