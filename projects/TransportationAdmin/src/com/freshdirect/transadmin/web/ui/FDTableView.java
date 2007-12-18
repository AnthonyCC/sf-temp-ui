package com.freshdirect.transadmin.web.ui;

import org.extremecomponents.table.core.TableModel;
import org.extremecomponents.table.view.HtmlView;
import org.extremecomponents.util.HtmlBuilder;

public class FDTableView extends HtmlView {
	
	protected void statusBar(HtmlBuilder html, TableModel model) {
        new FDStatusBar(html, model).layout();
    }
	
	protected void toolbar(HtmlBuilder html, TableModel model) {
        new FDToolbar(html, model).layout();
    }

}
