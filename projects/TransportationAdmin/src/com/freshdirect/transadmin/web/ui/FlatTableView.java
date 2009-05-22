package com.freshdirect.transadmin.web.ui;

import org.extremecomponents.table.core.TableModel;
import org.extremecomponents.table.view.HtmlView;
import org.extremecomponents.table.view.html.CalcBuilder;
import org.extremecomponents.table.view.html.RowBuilder;
import org.extremecomponents.util.HtmlBuilder;


public class FlatTableView extends HtmlView {
	
	protected void statusBar(HtmlBuilder html, TableModel model) {       
    }
	
	protected void toolbar(HtmlBuilder html, TableModel model) {
       
    }
	
	protected void init(HtmlBuilder html, TableModel model) {
        setTableBuilder(new FlatTableBuilder(html, model));
        setRowBuilder(new RowBuilder(html, model));
        setCalcBuilder(new CalcBuilder(html, model));
    }

}
