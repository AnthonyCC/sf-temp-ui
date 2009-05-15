package com.freshdirect.transadmin.web.ui;

import java.util.Iterator;

import org.extremecomponents.table.bean.Column;
import org.extremecomponents.table.core.TableModel;
import org.extremecomponents.table.view.html.ColumnBuilder;
import org.extremecomponents.util.HtmlBuilder;

import com.freshdirect.transadmin.util.TransStringUtil;
import com.freshdirect.transadmin.util.TransportationAdminProperties;
import com.freshdirect.transadmin.web.model.DispatchResourceInfo;
import com.freshdirect.transadmin.web.model.ResourceList;
import com.freshdirect.transadmin.web.model.ResourceReq;

public class FDDispatchResourceCell extends FDBaseCell  {
	
	public String getExportDisplay(TableModel model, Column column) {
		
		Object obj=column.getPropertyValue();
		if(obj instanceof ResourceList) {
			ResourceList resList=(ResourceList)obj;
			StringBuffer response=new StringBuffer(200);
			if(resList.getResourceReq()!=null) {
				
				for(int i=0;i<resList.size();i++) {
					String name= getResourceName(((DispatchResourceInfo)resList.get(i)));
					response.append(name);
					if(!TransStringUtil.isEmpty(((DispatchResourceInfo)resList.get(i)).getNextelNo())) {
						response.append(" [").append(((DispatchResourceInfo)resList.get(i)).getNextelNo()).append(" ]");
					}
					
					if(i<(resList.size()-1) && !TransStringUtil.isEmpty(name)) {
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
        	DispatchResourceInfo resourceInfo=null;
        	while(it.hasNext()) {
        		resourceInfo=(DispatchResourceInfo)it.next();
        		if(getResourceName(resourceInfo).length() > 0) {
        			html.tr(0).close();
        			if(resourceInfo.getEmployeeId()==null)
        			{
        				html.td(0).styleClass("employee_no");
        			}
        			else if(resourceInfo.getPunchInfo()==null)
        			{
        				//do nothing white bg
        			}
        			else if(resourceInfo.getPunchInfo().isPunchedIn())
        			{
        				html.td(0).styleClass("employee_on");
        			}
        			else if(resourceInfo.getPunchInfo().isLate())
        			{
        				html.td(0).styleClass("employee_off");
        			}	
        			else if(resourceInfo.getPunchInfo().getOutPunchDTM()!=null)
        			{
        				html.td(0).styleClass("employee_out");
        			}
        			html.td(0).close().append(getResourceName(resourceInfo));
        			if(!TransStringUtil.isEmpty(resourceInfo.getNextelNo())) {
        				html.append(" ["+resourceInfo.getNextelNo()+"]");
        			}
        			html.tdEnd();
        			html.trEnd(0);
        		}
        	}
    		html.tableEnd(0);
        }
        columnBuilder.tdEnd();
               
        return columnBuilder.toString();
    	
    }
    
    public String getResourceName(DispatchResourceInfo resource) {
    	if(resource != null) {
    		return resource.getName();
    	} else {
    		return "";
    	}
    }
    
    
}