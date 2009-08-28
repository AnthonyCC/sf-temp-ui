package com.freshdirect.transadmin.web.ui;

import org.extremecomponents.table.bean.Column;
import org.extremecomponents.table.core.TableModel;

public class FDPercentageCell extends FDBaseCell  {
	
	public String getExportDisplay(TableModel model, Column column) {
		return column.getValueAsString() + "%";
    }

    public String getHtmlDisplay(TableModel model, Column column) {
    	return column.getValueAsString() + "%"; 
    }

}
