package com.freshdirect.transadmin.web.ui;

import org.extremecomponents.table.bean.Column;
import org.extremecomponents.table.core.TableModel;
import org.extremecomponents.table.view.html.ColumnBuilder;

public class FDCheckboxCell extends FDBaseCell  {
	
	public String getExportDisplay(TableModel model, Column column) {
        return null;
    }

    public String getHtmlDisplay(TableModel model, Column column) {    	
        ColumnBuilder columnBuilder = new ColumnBuilder(column);
        columnBuilder.tdStart();
        
        try {                       
            //columnBuilder.getHtmlBuilder().input("hidden").name("chkbx_" + (String)column.getPropertyValue()).value(UNSELECTED).xclose();
            columnBuilder.getHtmlBuilder().input("checkbox").name(column.getPropertyValueAsString()).styleClass("datalistchx");            
            columnBuilder.getHtmlBuilder().xclose();
        } catch (Exception e) {e.printStackTrace();}
        
        columnBuilder.tdEnd();
        
        return columnBuilder.toString();
    }
}
