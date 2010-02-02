package com.freshdirect.transadmin.web.ui.interceptor;

import org.extremecomponents.table.bean.Row;
import org.extremecomponents.table.core.TableModel;
import org.extremecomponents.table.view.html.BuilderConstants;

import com.freshdirect.transadmin.web.model.UnassignedCommand;

public class UnassignedRowInterceptor extends FDRowInterceptor {
	
	UnassignedCommand currRowEntity = null;
	
	String LAST_STYLE = BuilderConstants.ROW_ODD_CSS;
	
	public void addRowAttributes(TableModel tableModel, Row row) {
    } 

    public void modifyRowAttributes(TableModel model, Row row) {
    	
    	UnassignedCommand rowEntity = (UnassignedCommand) model.getCurrentRowBean(); 
    	if(currRowEntity == null) {
    		currRowEntity = rowEntity;
    	}
    	if(rowEntity.isManuallyClosed()) {
    		row.setStyleClass("earlyWarningRow");
    		
    	} else {
    		if(currRowEntity != null && !currRowEntity.getTimeWindow().equalsIgnoreCase(rowEntity.getTimeWindow())) {
    			currRowEntity = rowEntity;
    			if(LAST_STYLE.equalsIgnoreCase(BuilderConstants.ROW_ODD_CSS)) {
    				LAST_STYLE = BuilderConstants.ROW_EVEN_CSS;
    			} else {
    				LAST_STYLE = BuilderConstants.ROW_ODD_CSS;
    			}
    		} 
    		row.setStyleClass(LAST_STYLE);
    	}
    }
}
