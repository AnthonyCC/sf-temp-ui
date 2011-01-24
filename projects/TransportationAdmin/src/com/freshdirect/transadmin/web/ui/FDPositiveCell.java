package com.freshdirect.transadmin.web.ui;

import org.extremecomponents.table.bean.Column;
import org.extremecomponents.table.core.TableModel;
import org.extremecomponents.table.view.html.ColumnBuilder;

public class FDPositiveCell extends FDBaseCell  {
	
	public String getExportDisplay(TableModel model, Column column) {
		
        try {        	
        	Boolean cellValue = (Boolean)column.getPropertyValue();
        	
        	if(cellValue != null && cellValue) {
        		return "Match";
        	} else {
        		return " ";
        	}
        } catch (Exception e) {e.printStackTrace();}
        return "None";
    }

    public String getHtmlDisplay(TableModel model, Column column) {    	
        ColumnBuilder columnBuilder = new ColumnBuilder(column);
        columnBuilder.tdStart();
        try {        	
        	Boolean cellValue = (Boolean)column.getPropertyValue();
        	if(cellValue != null && cellValue) {
        		columnBuilder.getHtmlBuilder().img("images/icons/confirmed.gif");
        	} else {
        		//columnBuilder.getHtmlBuilder().img("images/icons/delete.gif");
        	}
            //columnBuilder.getHtmlBuilder().xclose();
        } catch (Exception e) {e.printStackTrace();}
        columnBuilder.tdEnd();
        return columnBuilder.toString();
    }

}
