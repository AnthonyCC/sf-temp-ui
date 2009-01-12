package com.freshdirect.transadmin.web.ui;

import java.util.Iterator;
import java.util.Set;

import org.extremecomponents.table.bean.Column;
import org.extremecomponents.table.core.TableModel;
import org.extremecomponents.table.view.html.ColumnBuilder;
import org.extremecomponents.util.HtmlBuilder;

import com.freshdirect.transadmin.model.EmployeeInfo;
import com.freshdirect.transadmin.model.ZonetypeResource;
import com.freshdirect.transadmin.util.TransportationAdminProperties;
import com.freshdirect.transadmin.web.model.ResourceList;

public class FDResourceCell extends FDBaseCell  {
	
	public String getExportDisplay(TableModel model, Column column) {
		 Set resources = (Set)column.getPropertyValue();
	        if(resources != null) {
	        	Iterator iterator = resources.iterator();
	        	ZonetypeResource resource = null;
	        	StringBuffer response= new StringBuffer(10);
	        	while(iterator.hasNext()) {
	        		resource = (ZonetypeResource)iterator.next();
	        		if(resource.getId().getRole().equals(column.getAlias())) {
	        			response.append(resource.getMaximumNo()).append("/").append(resource.getRequiredNo()).toString();
	        		}
	        	}
	        	
	        	return response.toString();
	        }             
		return "";       
    }

    public String getHtmlDisplay(TableModel model, Column column) {    	
        ColumnBuilder columnBuilder = new ColumnBuilder(column);
        HtmlBuilder html = columnBuilder.getHtmlBuilder();
       
        columnBuilder.tdStart();
        
        
        
        Set resources = (Set)column.getPropertyValue();
        if(resources != null) {
        	Iterator iterator = resources.iterator();
        	ZonetypeResource resource = null;
        	while(iterator.hasNext()) {
        		resource = (ZonetypeResource)iterator.next();
        		if(resource.getId().getRole().equals(column.getAlias())) {
	        		html.table(0).close();
	        		html.tr(0).close();	        		
	        		html.td(0).close().append(resource.getMaximumNo()).tdEnd();
	        		html.td(0).close().append(resource.getRequiredNo()).tdEnd();
	        		html.trEnd(0);
	        		html.tableEnd(0);
	        		break;
        		}
        	}
        }               
        columnBuilder.tdEnd();
        return columnBuilder.toString();
    }
}