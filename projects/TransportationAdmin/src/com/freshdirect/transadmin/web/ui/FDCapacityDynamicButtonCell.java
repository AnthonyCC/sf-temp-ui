package com.freshdirect.transadmin.web.ui;

import org.extremecomponents.table.bean.Column;
import org.extremecomponents.table.core.TableModel;
import org.extremecomponents.table.view.html.ColumnBuilder;

import com.freshdirect.transadmin.web.model.EarlyWarningCommand;


public class FDCapacityDynamicButtonCell extends FDBaseCell  {
	
	public String getExportDisplay(TableModel model, Column column) {
        return null;
    }

    public String getHtmlDisplay(TableModel model, Column column) {    	
        ColumnBuilder columnBuilder = new ColumnBuilder(column);
        columnBuilder.tdStart();
        EarlyWarningCommand command = null;
        
        try {                       
            if(model.getCurrentRowBean() instanceof EarlyWarningCommand) {
            	
            	command = (EarlyWarningCommand)model.getCurrentRowBean();
            	if(command.getCode() != null && command.getCode().trim().length() > 0) {
	            	columnBuilder.getHtmlBuilder().append("<input type=\"button\" class=\"")
	            											.append(command.isDynamicActive() ? "dynamic_enabled" : "dynamic_disabled")
	            											.append("\" value=\"")
	            											.append(command.isDynamicActive() ? "D" : "S")
															.append("\" onclick=\"updateDynamicTimeslot(this,'")
															.append(command.getCode())
															.append("', '")
															.append(command.isRegion() ? "2" : "1")
															.append("')\"")
															.append(" ")
															.append("true".equalsIgnoreCase((String)model.getContext().getPageAttribute("IS_USERADMIN")) ? "" : "disabled=\"disabled\"")
	            											.append(" />");
            	}
            	//columnBuilder.getHtmlBuilder().input("checkbox").name(column.getPropertyValueAsString()).styleClass("datalistchx");            
                //columnBuilder.getHtmlBuilder().xclose();
            }                            
            
        } catch (Exception e) {
        	e.printStackTrace();
        }
        
        columnBuilder.tdEnd();
        
        return columnBuilder.toString();
    }
}
