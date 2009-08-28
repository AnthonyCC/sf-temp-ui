package com.freshdirect.transadmin.web.ui.interceptor;

import org.extremecomponents.table.bean.Row;
import org.extremecomponents.table.core.TableModel;

import com.freshdirect.transadmin.web.model.DispatchCommand;

public class DispatchObsoleteRowInterceptor extends FDRowInterceptor {
	
	public void addRowAttributes(TableModel tableModel, Row row) {
    } 

    public void modifyRowAttributes(TableModel model, Row row) {
    	DispatchCommand rowEntity = (DispatchCommand) model.getCurrentRowBean();        
        if (rowEntity != null && rowEntity.isObsoleteEntity()) {
            row.setStyleClass("obsoleteRow");
        } else {
        	if(rowEntity.isConfirmed()) {
        		row.setStyleClass("confirmedRow");
        	} else  {
        		super.modifyRowAttributes(model, row);
        	}
        }
    }

}
