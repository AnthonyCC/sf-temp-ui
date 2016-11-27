package com.freshdirect.transadmin.web.ui;

import java.util.Iterator;
import java.util.List;

import org.extremecomponents.table.bean.Column;
import org.extremecomponents.table.core.TableModel;
import org.extremecomponents.table.view.html.ColumnBuilder;
import org.extremecomponents.util.HtmlBuilder;

import com.freshdirect.transadmin.util.TransStringUtil;
import com.freshdirect.transadmin.util.TransportationAdminProperties;
import com.freshdirect.transadmin.web.model.WebEmployeeInfo;

public class FDEmployeeCell extends FDBaseCell  {
	
	public String getExportDisplay(TableModel model, Column column) {
		
		List<WebEmployeeInfo> resources = (List<WebEmployeeInfo>) column.getPropertyValue();
		StringBuffer response = new StringBuffer(200);
		
		if(resources != null) {
				
				for(int i=0; i<resources.size(); i++) {					
					response.append(((WebEmployeeInfo)resources.get(i)).toString());
					if(i<(resources.size()-1)&& !TransStringUtil.isEmpty(((WebEmployeeInfo)resources.get(i)).toString())) {
						response.append(TransportationAdminProperties.getCellDataSeperator());
					}
				}
				if(TransportationAdminProperties.getCellDataSeperator().equals(response.toString())) {
					return "";
				}
			if(response.toString().endsWith(TransportationAdminProperties.getCellDataSeperator()))
				return response.toString().substring(0, response.toString().lastIndexOf(TransportationAdminProperties.getCellDataSeperator()));

			return response.toString();
		}
		return "";
    }

	
    public String getHtmlDisplay(TableModel model, Column column) {   
    	
        ColumnBuilder columnBuilder = new ColumnBuilder(column);
        HtmlBuilder html = columnBuilder.getHtmlBuilder();
        columnBuilder.tdStart();
        List<WebEmployeeInfo> resources = (List<WebEmployeeInfo>) column.getPropertyValue();
        if(resources!=null) {
        	html.table(0).close();
        	Iterator it=resources.iterator();
        	WebEmployeeInfo resourceInfo = null;
        	while(it.hasNext()) {
        		resourceInfo = (WebEmployeeInfo)it.next();
        		html.tr(0).close();
    			html.td(0).close().append(((WebEmployeeInfo)resourceInfo).toString()).tdEnd();
    			html.trEnd(0);
        	}
    		html.tableEnd(0);
        }
        columnBuilder.tdEnd();
        
        return columnBuilder.toString();
    	
    }
    
}