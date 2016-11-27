package com.freshdirect.transadmin.web.ui;

import java.util.Map;

import org.extremecomponents.table.bean.Column;
import org.extremecomponents.table.core.TableModel;
import org.extremecomponents.table.view.html.ColumnBuilder;

public class FDCompositeCell extends FDBaseCell  {
	
	public String getExportDisplay(TableModel model, Column column) {
        return null;
    }

    public String getHtmlDisplay(TableModel model, Column column) {    	
        ColumnBuilder columnBuilder = new ColumnBuilder(column);
        columnBuilder.tdStart();
        String key = column.getPropertyValueAsString();
        String value = null; 
        Map referenceMap = (Map)model.getContext().getRequestAttribute("referencemapping");               
        if(referenceMap != null) {
        	value = (String)referenceMap.get(key);        	
        }
        if(value == null) {
    		value = key;
    	}
        try {                       
            
            columnBuilder.getHtmlBuilder().append("<a ").name(key).style("text-decoration: none").close().append(value);            
            columnBuilder.getHtmlBuilder().aEnd();
        } catch (Exception e) {e.printStackTrace();}
        
        columnBuilder.tdEnd();
        
        return columnBuilder.toString();
    }
}
