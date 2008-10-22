package com.freshdirect.transadmin.web.ui.interceptor;


import org.extremecomponents.table.bean.Row;
import org.extremecomponents.table.core.TableModel;
import org.extremecomponents.table.interceptor.RowInterceptor;
import org.extremecomponents.table.view.html.BuilderConstants;

import com.freshdirect.transadmin.model.TrnBaseEntityI;

public class ObsoleteRowInterceptor implements RowInterceptor {
	
	public void addRowAttributes(TableModel tableModel, Row row) {
    } 

    public void modifyRowAttributes(TableModel model, Row row) {
    	
    	TrnBaseEntityI rowEntity = (TrnBaseEntityI) model.getCurrentRowBean();        
        if (rowEntity != null && rowEntity.isObsoleteEntity()) {
            row.setStyleClass("obsoleteRow");
        } else {
        	if(model.getRowHandler().isRowEven()) {
        		row.setStyleClass(BuilderConstants.ROW_EVEN_CSS);
        	} else {
        		row.setStyleClass(BuilderConstants.ROW_ODD_CSS);
        	}
        }
    }

}
