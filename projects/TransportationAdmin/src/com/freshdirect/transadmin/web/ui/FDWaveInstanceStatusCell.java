package com.freshdirect.transadmin.web.ui;

import org.extremecomponents.table.bean.Column;
import org.extremecomponents.table.core.TableModel;
import org.extremecomponents.table.view.html.ColumnBuilder;

import com.freshdirect.transadmin.web.model.WaveInstanceCommand;

public class FDWaveInstanceStatusCell extends FDBaseCell  {
	
	public String getExportDisplay(TableModel model, Column column) {
		
        try {        	
        	Boolean cellValue = (Boolean)column.getPropertyValue();
        	
        	if(cellValue != null && cellValue) {
        		return "Match";
        	} else {
        		return "NoMatch";
        	}
        } catch (Exception e) {e.printStackTrace();}
        return "None";
    }

    public String getHtmlDisplay(TableModel model, Column column) {    	
        ColumnBuilder columnBuilder = new ColumnBuilder(column);
        columnBuilder.tdStart();
        try {
        	Object obj = model.getCurrentRowBean();
        	
        	if(obj instanceof WaveInstanceCommand && !((WaveInstanceCommand)obj).getIsInValid()) {
        		Boolean cellValue = (Boolean)column.getPropertyValue();
            	if(cellValue != null && cellValue) {
            		columnBuilder.getHtmlBuilder().img("images/icons/tick.gif");
            	} else {
            		columnBuilder.getHtmlBuilder().img("images/icons/cross.gif");
            	}
        	} else {
        		columnBuilder.getHtmlBuilder().img("images/icons/gcross.gif");
        	}
        	
        } catch (Exception e) {e.printStackTrace();}
        columnBuilder.tdEnd();
        return columnBuilder.toString();
    }

}
