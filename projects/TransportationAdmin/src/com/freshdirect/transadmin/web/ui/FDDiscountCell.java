package com.freshdirect.transadmin.web.ui;

import org.extremecomponents.table.bean.Column;
import org.extremecomponents.table.core.TableModel;
import org.extremecomponents.table.view.html.ColumnBuilder;

import com.freshdirect.transadmin.web.model.EarlyWarningCommand;

public class FDDiscountCell extends FDBaseCell  {
	
	public String getExportDisplay(TableModel model, Column column) {
		
        try {        	
        	Boolean cellValue = (Boolean)column.getPropertyValue();
        	if(cellValue != null && cellValue) {
        		return "Discounted";
        	} else {
        		return "No Discount";
        	}
        } catch (Exception e) {e.printStackTrace();}
        return "No Discount";
    }

    public String getHtmlDisplay(TableModel model, Column column) {    	
        ColumnBuilder columnBuilder = new ColumnBuilder(column);
        columnBuilder.tdStart();
        try {        	
        	EarlyWarningCommand cell = (EarlyWarningCommand)model.getCurrentRowBean();
        	if(cell != null && cell.isDiscounted()) {
        		columnBuilder.getHtmlBuilder().img("images/dollar.gif");
        	}
            
        } catch (Exception e) {e.printStackTrace();}
        columnBuilder.tdEnd();
        return columnBuilder.toString();
    }

}
