package com.freshdirect.transadmin.web.ui;

import java.util.Iterator;
import java.util.List;

import org.extremecomponents.table.bean.Column;
import org.extremecomponents.table.core.TableModel;
import org.extremecomponents.table.view.html.ColumnBuilder;
import org.extremecomponents.util.HtmlBuilder;

import com.freshdirect.transadmin.model.EmployeeInfo;
import com.freshdirect.transadmin.model.ResourceInfoI;
import com.freshdirect.transadmin.util.TransStringUtil;
import com.freshdirect.transadmin.util.TransportationAdminProperties;
import com.freshdirect.transadmin.web.model.ResourceList;
import com.freshdirect.transadmin.web.model.ResourceReq;

public class FDPlanResourceCell extends FDBaseCell  {
	
	public String getExportDisplay(TableModel model, Column column) {
		
		Object obj=column.getPropertyValue();
		if(obj instanceof ResourceList) {
			ResourceList resList=(ResourceList)obj;
			StringBuffer response=new StringBuffer(200);
			if(resList!=null) {
				
				for(int i=0;i<resList.size();i++) {
					String name=((EmployeeInfo)resList.get(i)).getName();
					response.append(name);
					if(i<(resList.size()-1)&& !TransStringUtil.isEmpty(name)) {
						response.append(TransportationAdminProperties.getCellDataSeperator());
					}
				}
				if(TransportationAdminProperties.getCellDataSeperator().equals(response.toString())) {
					return "";
				}
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
        ResourceList resources=(ResourceList)column.getPropertyValue();
        if(resources!=null) {
        	html.table(0).close();
        	Iterator it=resources.iterator();
        	ResourceInfoI resourceInfo=null;
        	while(it.hasNext()) {
        		resourceInfo=(ResourceInfoI)it.next();
        		if(resourceInfo.getLastName()!=null || resourceInfo.getFirstName()!=null) 
        		{
        			String star="";
        			if(resourceInfo.getAdjustmentTime()!=null) star="#";
        			html.tr(0).close();
        			html.td(0).close().append(star+resourceInfo.getLastName()+" "+resourceInfo.getFirstName()).tdEnd();
        			html.trEnd(0);
        		}
        	}
    		html.tableEnd(0);
        }
        columnBuilder.tdEnd();
        
        return columnBuilder.toString();
    	
    }
    
    
}