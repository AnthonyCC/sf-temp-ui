package com.freshdirect.transadmin.web.ui.interceptor;

import org.extremecomponents.table.bean.Row;
import org.extremecomponents.table.core.TableModel;
import org.extremecomponents.table.interceptor.RowInterceptor;
import org.extremecomponents.table.view.html.BuilderConstants;

import com.freshdirect.transadmin.constants.EnumDispatchStatusType;
import com.freshdirect.transadmin.model.Dispatch;
import com.freshdirect.transadmin.web.model.DispatchCommand;

public class DispatchObsoleteRowInterceptor implements RowInterceptor {
	
	public void addRowAttributes(TableModel tableModel, Row row) {
    } 

    public void modifyRowAttributes(TableModel model, Row row) {
    	DispatchCommand rowEntity = (DispatchCommand) model.getCurrentRowBean();        
        if (rowEntity != null && rowEntity.isObsoleteEntity()) {
            row.setStyleClass("obsoleteRow");
        } else {
        	if(rowEntity.isConfirmed()) {
        		row.setStyleClass("confirmedRow");
        	}
        	else if(model.getRowHandler().isRowEven()) {
        		row.setStyleClass(BuilderConstants.ROW_EVEN_CSS);
        	} else {
        		row.setStyleClass(BuilderConstants.ROW_ODD_CSS);
        	}
        }
    }

}
