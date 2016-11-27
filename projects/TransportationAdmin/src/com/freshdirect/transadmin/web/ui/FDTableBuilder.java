package com.freshdirect.transadmin.web.ui;

import java.util.Iterator;
import java.util.List;

import org.extremecomponents.table.bean.Column;
import org.extremecomponents.table.core.TableModel;
import org.extremecomponents.table.view.html.BuilderConstants;
import org.extremecomponents.table.view.html.TableBuilder;
import org.extremecomponents.util.HtmlBuilder;

public class FDTableBuilder extends TableBuilder {
	
	public FDTableBuilder(TableModel model) {
        super(new HtmlBuilder(), model);
    }
    
    public FDTableBuilder(HtmlBuilder html, TableModel model) {
    	super(html, model);
    }
    
    public void filterRow() {
        if (!getTableModel().getTableHandler().getTable().isFilterable()) {
            return;
        }

        getHtmlBuilder().tr(1).styleClass(BuilderConstants.FILTER_CSS).close();

        List columns = getTableModel().getColumnHandler().getFilterColumns();
        for (Iterator iter = columns.iterator(); iter.hasNext();) {
            Column column = (Column) iter.next();            
            getHtmlBuilder().append(column.getCellDisplay());
        }

        getHtmlBuilder().trEnd(1);
    }
}
