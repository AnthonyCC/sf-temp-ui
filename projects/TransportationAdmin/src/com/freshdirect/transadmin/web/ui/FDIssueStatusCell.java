package com.freshdirect.transadmin.web.ui;

import org.extremecomponents.table.bean.Column;
import org.extremecomponents.table.core.TableModel;
import org.extremecomponents.table.view.html.ColumnBuilder;

import com.freshdirect.transadmin.constants.EnumIssueStatus;

public class FDIssueStatusCell extends FDBaseCell  {
	
	public String getExportDisplay(TableModel model, Column column) {
		
        try {        	
        	String cellValue = (String)column.getPropertyValue();
        	
        	if(cellValue != null && EnumIssueStatus.OPEN.getName().equals(cellValue)) {
        		return "Open";
        	} if(cellValue != null && EnumIssueStatus.VERIFIED.getName().equals(cellValue)) {
        		return "Verifed";
        	} if(cellValue != null && EnumIssueStatus.REVERIFIED.getName().equals(cellValue)) {
        		return "Re-Verified";
        	} else {        	
        		return "Resolved";
        	}
        } catch (Exception e) {
        	e.printStackTrace();
        }
        return "";
    }

    public String getHtmlDisplay(TableModel model, Column column) {    	
        ColumnBuilder columnBuilder = new ColumnBuilder(column);
        columnBuilder.tdStart();
        try {        	
        	String cellValue = (String)column.getPropertyValue();
        	if(cellValue != null && EnumIssueStatus.OPEN.getName().equals(cellValue)) {
        		columnBuilder.getHtmlBuilder().img("images/icons/o-icon.gif");
        	}else if(cellValue != null && EnumIssueStatus.VERIFIED.getName().equals(cellValue)) {
        		columnBuilder.getHtmlBuilder().img("images/icons/v-icon.gif");
        	}else if(cellValue != null && EnumIssueStatus.REVERIFIED.getName().equals(cellValue)) {
        		columnBuilder.getHtmlBuilder().img("images/icons/rv-icon.gif");
        	}else if(cellValue != null && EnumIssueStatus.RESOLVED.getName().equals(cellValue)) {
        		columnBuilder.getHtmlBuilder().img("images/icons/r-icon.gif");
        	}else {        	
        		columnBuilder.getHtmlBuilder().img("images/icons/i-icon.gif");
        	}       	
        	
        } catch (Exception e) {e.printStackTrace();}
        columnBuilder.tdEnd();
        return columnBuilder.toString();
    }

}
