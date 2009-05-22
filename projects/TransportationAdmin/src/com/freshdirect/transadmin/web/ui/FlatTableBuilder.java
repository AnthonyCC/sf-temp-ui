package com.freshdirect.transadmin.web.ui;

import org.extremecomponents.table.core.TableModel;
import org.extremecomponents.table.view.html.TableBuilder;
import org.extremecomponents.util.HtmlBuilder;

public class FlatTableBuilder extends TableBuilder {
	
	public FlatTableBuilder(TableModel model) {
        super(new HtmlBuilder(), model);
    }
    
    public FlatTableBuilder(HtmlBuilder html, TableModel model) {
    	super(html, model);
    }
    
    public void filterRow() {       
    }
}
