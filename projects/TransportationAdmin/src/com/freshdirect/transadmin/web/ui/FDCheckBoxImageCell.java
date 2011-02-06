package com.freshdirect.transadmin.web.ui;

import org.extremecomponents.table.bean.Column;
import org.extremecomponents.table.core.TableModel;
import org.extremecomponents.table.view.html.ColumnBuilder;

public class FDCheckBoxImageCell extends FDBaseCell  {
	
	public String getExportDisplay(TableModel model, Column column) {
        return null;
    }

    public String getHtmlDisplay(TableModel model, Column column) {    	
        ColumnBuilder columnBuilder = new ColumnBuilder(column);
        columnBuilder.tdStart();
        try {        	
        	String cellValue = (String)column.getPropertyValue();
        	if(cellValue != null && new Boolean(cellValue).booleanValue())
        		columnBuilder.getHtmlBuilder().img("images/icons/tick.gif");            
            //columnBuilder.getHtmlBuilder().xclose();
        } catch (Exception e) {e.printStackTrace();}
        columnBuilder.tdEnd();
        return columnBuilder.toString();
    }

}
