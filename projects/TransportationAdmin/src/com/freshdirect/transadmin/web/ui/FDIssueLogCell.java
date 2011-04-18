package com.freshdirect.transadmin.web.ui;

import java.util.Iterator;
import java.util.Set;

import org.extremecomponents.table.bean.Column;
import org.extremecomponents.table.core.TableModel;
import org.extremecomponents.table.view.html.ColumnBuilder;
import org.extremecomponents.util.HtmlBuilder;

import com.freshdirect.transadmin.constants.EnumIssueStatus;
import com.freshdirect.transadmin.constants.EnumServiceStatus;
import com.freshdirect.transadmin.model.EmployeeInfo;
import com.freshdirect.transadmin.model.IssueLog;
import com.freshdirect.transadmin.model.ZonetypeResource;
import com.freshdirect.transadmin.util.TransportationAdminProperties;
import com.freshdirect.transadmin.web.model.ResourceList;

public class FDIssueLogCell extends FDBaseCell  {
	
	public String getExportDisplay(TableModel model, Column column) {
		 Set issueLogs = (Set)column.getPropertyValue();
	        if(issueLogs != null) {
	        	Iterator iterator = issueLogs.iterator();
	        	IssueLog issueLog = null;
	        	StringBuffer response = new StringBuffer(10);
	        	while(iterator.hasNext()) {
	        		issueLog = (IssueLog)iterator.next();
	        		response.append(issueLog.getIssueType()).append("/").append(issueLog.getIssueSubType())
	        								.append("/").append(issueLog.getVirRecord().getId()).append("/").append(issueLog.getComments());
	        		if(issueLog.getMaintenanceIssue() !=null)
	        			response.append("/").append(issueLog.getMaintenanceIssue().getId());
	        	}
	        	
	        	return response.toString();
	        }
		return "";
    }

    public String getHtmlDisplay(TableModel model, Column column) {    	
        ColumnBuilder columnBuilder = new ColumnBuilder(column);
        HtmlBuilder html = columnBuilder.getHtmlBuilder();
        columnBuilder.tdStart();        
        Set issueLogs = (Set)column.getPropertyValue();
        if(issueLogs != null) {
        	Iterator iterator = issueLogs.iterator();
        	IssueLog issueLog = null;
        	while(iterator.hasNext()) {
        			issueLog = (IssueLog)iterator.next();
        			html.table(0).close();
        			
        			
	        		html.tr(0).close();
	        		html.td(0).close().append(issueLog.getIssueType()+"/ "+issueLog.getIssueSubType()).tdEnd();
	        		HtmlBuilder td = html.td(0);       			
        			if(issueLog.getMaintenanceIssue() !=null && EnumIssueStatus.OPEN.getName().equals(issueLog.getMaintenanceIssue().getIssueStatus()))
        				td.styleClass("employee_off");
        			else 
        				td.styleClass("employee_on");
        			td.close();
        			if(issueLog.getMaintenanceIssue() !=null && issueLog.getMaintenanceIssue().getId()!=null)
        				td.append(" ["+issueLog.getMaintenanceIssue().getId()+"]");
        			else
        				td.append(" 		");
        			html.td(0).close().append("["+issueLog.getDamageLocation()+"]  ["+issueLog.getIssueSide()+"]");
        			if(issueLog.getComments()!= null)
	        			html.td(0).close().append(issueLog.getComments());
        			else
        				html.td(0).close().append(" 					");
        			html.tdEnd();
        			html.trEnd(0);
	        	html.tableEnd(0);	        		     		
        	}
        }               
        columnBuilder.tdEnd();
        return columnBuilder.toString();
    }
}