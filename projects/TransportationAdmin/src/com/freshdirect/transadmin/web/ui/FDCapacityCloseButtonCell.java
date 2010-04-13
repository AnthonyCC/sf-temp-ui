package com.freshdirect.transadmin.web.ui;

import org.extremecomponents.table.bean.Column;
import org.extremecomponents.table.core.TableModel;
import org.extremecomponents.table.view.html.ColumnBuilder;

import weblogic.servlet.jsp.PageContextImpl;

import com.freshdirect.transadmin.web.model.EarlyWarningCommand;


public class FDCapacityCloseButtonCell extends FDBaseCell  {
	
	public String getExportDisplay(TableModel model, Column column) {
        return null;
    }

    public String getHtmlDisplay(TableModel model, Column column) {    	
        ColumnBuilder columnBuilder = new ColumnBuilder(column);
        columnBuilder.tdStart();
        EarlyWarningCommand command = null;
        String rowClass = "timeslot_open";        
        try {                       
            if(model.getCurrentRowBean() instanceof EarlyWarningCommand) {
            	
            	command = (EarlyWarningCommand)model.getCurrentRowBean();
            	if(command.getCode() != null && command.getCode().trim().length() > 0) {
            		if(command.getOpenCount() > 0 && command.getClosedCount() > 0) {
            			rowClass = "timeslot_closedpartial";
            		} else if(command.getOpenCount() == 0 && command.getClosedCount() > 0) {
            			rowClass = "timeslot_closed";
            		}
	            	columnBuilder.getHtmlBuilder().append("<input type=\"button\" class=\"")
	            											.append(rowClass)
	            											.append("\" value=\"")
	            											.append(command.getClosedCount() > 0 ? "C" : "O")
															.append("\" onclick=\"updateTimeslot(this,'")
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
