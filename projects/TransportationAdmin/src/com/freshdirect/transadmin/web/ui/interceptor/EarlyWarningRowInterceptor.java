package com.freshdirect.transadmin.web.ui.interceptor;

import org.extremecomponents.table.bean.Row;
import org.extremecomponents.table.core.TableModel;

import com.freshdirect.transadmin.util.TransStringUtil;
import com.freshdirect.transadmin.web.model.EarlyWarningCommand;

public class EarlyWarningRowInterceptor extends FDRowInterceptor {
	
	public void addRowAttributes(TableModel tableModel, Row row) {
    } 

    public void modifyRowAttributes(TableModel model, Row row) {
    	
    	EarlyWarningCommand rowEntity = (EarlyWarningCommand) model.getCurrentRowBean(); 
    	
    	if(getDouble(rowEntity.getPercentageAllocated()) > 90.0) {
    		row.setStyleClass("earlyWarningRow");
    		
    	} else {
    		if(TransStringUtil.isEmpty(rowEntity.getName())) {
    			row.setStyleClass("earlyWarningTotalRow");
    		} else {
    			super.modifyRowAttributes(model, row);
    		}
    	}
    	/*if(rowEntity.isManuallyClosed()) {
			row.setStyle("text-decoration:line-through;");
		} else {
			row.setStyle("text-decoration:none;");
		}*/
    }
    
    private double getDouble(String strVal) {
    	try {
    		return Double.parseDouble(strVal.substring(0, strVal.length()-1));
    	} catch(NumberFormatException ex) {
    		return 0;
    	}
    }

}
