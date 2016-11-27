package com.freshdirect.transadmin.web.ui;

import org.extremecomponents.table.bean.Column;
import org.extremecomponents.table.core.TableModel;
import org.extremecomponents.table.view.html.ColumnBuilder;

public class FDZoneExpansionCheckboxCell extends FDBaseCell {

	@Override
	public String getExportDisplay(TableModel model, Column column) {
		return null;
	}

	@Override
	public String getHtmlDisplay(TableModel arg0, Column column) {
		ColumnBuilder columnBuilder = new ColumnBuilder(column);
        columnBuilder.tdStart();
        
        try {                       
            //columnBuilder.getHtmlBuilder().input("hidden").name("chkbx_" + (String)column.getPropertyValue()).value(UNSELECTED).xclose();
            columnBuilder.getHtmlBuilder().input("checkbox").name(column.getPropertyValueAsString()).styleClass("datalistchx");            
            columnBuilder.getHtmlBuilder().onclick("showExpansion(this)");
            columnBuilder.getHtmlBuilder().xclose();
        } catch (Exception e) {e.printStackTrace();}
        
        columnBuilder.tdEnd();
        
        return columnBuilder.toString();
	}

}
