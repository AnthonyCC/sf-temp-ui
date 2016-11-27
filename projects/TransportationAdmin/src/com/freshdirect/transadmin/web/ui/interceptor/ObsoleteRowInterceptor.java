package com.freshdirect.transadmin.web.ui.interceptor;


import org.extremecomponents.table.bean.Row;
import org.extremecomponents.table.core.TableModel;

import com.freshdirect.transadmin.model.DlvScenarioDay;
import com.freshdirect.transadmin.model.TrnBaseEntityI;

public class ObsoleteRowInterceptor extends FDRowInterceptor {
	
	public void addRowAttributes(TableModel tableModel, Row row) {
    } 

    public void modifyRowAttributes(TableModel model, Row row) {
    	if(model.getCurrentRowBean() instanceof DlvScenarioDay) {
    		DlvScenarioDay rowEntity = (DlvScenarioDay) model.getCurrentRowBean();        
	        if (rowEntity != null && rowEntity.getScenario() != null
	        		&& (rowEntity.getScenario().getScenarioDays() == null || 
	        				rowEntity.getScenario().getScenarioDays().size() == 0)) {
	            row.setStyleClass("confirmedRow");
	            return;
	        }
	        if (rowEntity != null && rowEntity.getDayOfWeek() == null
	        		&& rowEntity.getNormalDate() == null) {
	            row.setStyleClass("obsoleteRow");
	            return;
	        }
    	} else if(model.getCurrentRowBean() instanceof TrnBaseEntityI) {
	    	TrnBaseEntityI rowEntity = (TrnBaseEntityI) model.getCurrentRowBean();        
	        if (rowEntity != null && rowEntity.isObsoleteEntity()) {
	            row.setStyleClass("obsoleteRow");
	            return;
	        } 	        
    	}
    	super.modifyRowAttributes(model, row);
    }

}
