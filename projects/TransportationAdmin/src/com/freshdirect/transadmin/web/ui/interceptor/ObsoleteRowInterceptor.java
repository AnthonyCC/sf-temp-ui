package com.freshdirect.transadmin.web.ui.interceptor;


import org.extremecomponents.table.bean.Row;
import org.extremecomponents.table.core.TableModel;
import org.extremecomponents.table.interceptor.RowInterceptor;
import org.extremecomponents.table.view.html.BuilderConstants;

import com.freshdirect.transadmin.model.TrnBaseEntityI;

public class ObsoleteRowInterceptor extends FDRowInterceptor {
	
	public void addRowAttributes(TableModel tableModel, Row row) {
    } 

    public void modifyRowAttributes(TableModel model, Row row) {
    	
    	if(model.getCurrentRowBean() instanceof TrnBaseEntityI) {
	    	TrnBaseEntityI rowEntity = (TrnBaseEntityI) model.getCurrentRowBean();        
	        if (rowEntity != null && rowEntity.isObsoleteEntity()) {
	            row.setStyleClass("obsoleteRow");
	            return;
	        } 	        
    	}
    	super.modifyRowAttributes(model, row);
    }

}
