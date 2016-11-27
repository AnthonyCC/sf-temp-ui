package com.freshdirect.transadmin.web.ui;

import org.extremecomponents.table.bean.Column;
import org.extremecomponents.table.core.TableModel;
import org.extremecomponents.table.view.html.ColumnBuilder;

import com.freshdirect.transadmin.web.model.IToolTip;

public class FDToolTipCell extends FDBaseCell  {
	
	public String getExportDisplay(TableModel model, Column column) {
        return getExportCellValue(model, column);
    }

    public String getHtmlDisplay(TableModel model, Column column) {
    	CustomColumnBuilder columnBuilder = new CustomColumnBuilder(column);
    	if(column.getValue() instanceof IToolTip) {
    		IToolTip toolTipVO = ((IToolTip)column.getValue());
    		if(toolTipVO.getToolTip() != null) {
    			columnBuilder.tdStart(toolTipVO.getToolTip().toString());
    		} else {
    			columnBuilder.tdStart("");
    		}
    	} else {
    		columnBuilder.tdStart();
    	}
        
        columnBuilder.tdBody(getCellValue(model, column));
        columnBuilder.tdEnd();
        return columnBuilder.toString();
    }
    
    protected String getCellValue(TableModel model, Column column) {
    	if(column.getValue() instanceof IToolTip) {
    		IToolTip toolTipVO = ((IToolTip)column.getValue());
    		if(toolTipVO.getValue() != null) {
    			if("Bullpen".equalsIgnoreCase(toolTipVO.getValue().toString()))
    				return "<b>"+toolTipVO.getValue().toString()+"</b>";
    			else 
    				return toolTipVO.getValue().toString();
    		}
    	} 
    	 return column.getValueAsString();       
    }
    
    protected String getExportCellValue(TableModel model, Column column) {
    	if(column.getValue() instanceof IToolTip) {
    		IToolTip toolTipVO = ((IToolTip)column.getValue());
    		if(toolTipVO.getValue() != null) {
    			return toolTipVO.getValue().toString();
    		}
    	} 
    	 return column.getValueAsString();       
    }
    class CustomColumnBuilder extends ColumnBuilder {

		public CustomColumnBuilder(Column arg0) {
			super(arg0);
			// TODO Auto-generated constructor stub
		}
		
		public void tdStart(String val) {
			getHtmlBuilder().td(2);
	        styleClass();
	        style();
	        width();
	        title(val);
	        getHtmlBuilder().close();
	    }
		public void title(String val) {	        
	        getHtmlBuilder().title(val);
	    }
    	
    }
}
