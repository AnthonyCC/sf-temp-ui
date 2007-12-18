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
        
        if(hasAdd == null || TransStringUtil.isEmpty(hasAdd)
        	|| hasAdd.equalsIgnoreCase("TRUE")) {

	        toolbarBuilder.addItemAsImage();
	        
	        html.nbsp();
        }

        toolbarBuilder.deleteItemAsImage();

        html.tdEnd();
    }
}
