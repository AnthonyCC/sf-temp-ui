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
			if(resList!=null) {
				
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
        			HtmlBuilder td=html.td(0);       			
        			
        			if(resourceInfo.getEmployeeId()==null)
        			{
        				td.styleClass("employee_no");
        			}
        			else if(resourceInfo.getPunchInfo()==null)
        			{
        				//do nothing white bg
        			}
        			else if(resourceInfo.getPunchInfo().isPunchedIn())
        			{
        				td.styleClass("employee_on");
        			}
        			else if(resourceInfo.getPunchInfo().isLate())
        			{
        				td.styleClass("employee_off");
        			}	
        			else if(resourceInfo.getPunchInfo().getOutPunchDTM()!=null)
        			{
        				td.styleClass("employee_out");
        			}
        			td.close();
        			td.append(getResourceName(resourceInfo));
        			if(!TransStringUtil.isEmpty(formatNextelNo(resourceInfo))) {
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
    
    protected String formatNextelNo(DispatchResourceInfo resource) {
    	return resource.getNextelNo();
    }
    
    
}