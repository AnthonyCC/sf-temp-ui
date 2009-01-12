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
			if(resList.getResourceReq()!=null) {
				
				for(int i=0;i<resList.size();i++) {
					String name=((EmployeeInfo)resList.get(i)).getName();
					response.append(name);
					if(i<(resList.size()-1)) {
						TransportationAdminProperties t;
						response.append(TransportationAdminProperties.getCellDataSeperator());
					}
				}
				if(TransportationAdminProperties.getCellDataSeperator().equals(response.toString())) {
					return "";
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
        ResourceList resources=(ResourceList)column.getPropertyValue();
        ResourceReq resourceReq=resources.getResourceReq();
        
        if(resources!=null) {
        	html.table(0).close();
        	Integer req=new Integer(0);
        	Integer max=new Integer(0);
        	Iterator it=resources.iterator();
        	boolean renderReq=true;
        	ResourceInfoI resourceInfo=null;
        	while(it.hasNext()) {
        		resourceInfo=(ResourceInfoI)it.next();
        		if(renderReq) {
        			
        			req=resourceReq.getReq();
        			max=resourceReq.getMax();
            		html.tr(0).close();
    				html.td(0).close().append(req+"/"+max).tdEnd();
    				html.trEnd(0);
        			renderReq=false;
        		}
        		if(resourceInfo.getLastName()!=null || resourceInfo.getFirstName()!=null) {
        			html.tr(0).close();
        			html.td(0).close().append(resourceInfo.getLastName()+" "+resourceInfo.getFirstName()).tdEnd();
        			html.trEnd(0);
        		}
        	}
    		html.tableEnd(0);
        }
        columnBuilder.tdEnd();
        /*HashMap resourceMap = (HashMap)column.getPropertyValue();
        if(resourceMap != null) {
        	html.table(0).close();
        	
        	Integer req=new Integer(0);
        	Integer max=new Integer(0);
        	if(resourceMap.get(column.getAlias()+PlanInfo.REQUIRED_SUFFIX)!=null) {
        		req=(Integer)resourceMap.get(column.getAlias()+PlanInfo.REQUIRED_SUFFIX);
        	}
        	if(resourceMap.get(column.getAlias()+PlanInfo.MAX_SUFFIX)!=null) {
        		max=(Integer)resourceMap.get(column.getAlias()+PlanInfo.MAX_SUFFIX);
        	}
        	if(0!=req.intValue() || 0!=max.intValue()) {
        		
        		html.tr(0).close();
				html.td(0).close().append(resourceMap.get(column.getAlias()+PlanInfo.REQUIRED_SUFFIX)+"/"+resourceMap.get(column.getAlias()+PlanInfo.MAX_SUFFIX)).tdEnd();
				html.trEnd(0);
        	}
			
        	Set resources=(Set)resourceMap.get(column.getAlias());
        	Iterator iterator = resources.iterator();
        	WebEmployeeInfo resource = null;
        	while(iterator.hasNext()) {
        		resource = (WebEmployeeInfo)iterator.next();
        		if(resource.getEmpInfo()!=null) {
        			html.tr(0).close();
        			html.td(0).close().append(resource.getLastName()+" "+resource.getFirstName()).tdEnd();
        			html.trEnd(0);
        		}
        	}
        	
    		html.tableEnd(0);
        }      
        
        if(resourceMap != null) {
        	html.table(0).close();
        	Integer req=new Integer(0);
        	Integer max=new Integer(0);
        	ResourceInfo resourceInfo=(ResourceInfo)resourceMap.get(column.getAlias());
        	if(resourceInfo!=null) {
        		req=resourceInfo.getReq();
        		max=resourceInfo.getMax();
	        	if(0!=req.intValue() || 0!=max.intValue()) {
	        		html.tr(0).close();
					html.td(0).close().append(req+"/"+max).tdEnd();
					html.trEnd(0);
	        	}
	        	List employees=resourceInfo.getEmployees();
	        	if(employees!=null && employees.size()>0) {
	        		Iterator iterator = employees.iterator();
	        		EmployeeInfo employee = null;
	        		while(iterator.hasNext()) {
	        			employee = (EmployeeInfo)iterator.next();
	        			html.tr(0).close();
	        			html.td(0).close().append(employee.getLastName()+" "+employee.getFirstName()).tdEnd();
	        			html.trEnd(0);
	        		}
	        	}
        	}
    		html.tableEnd(0);
    		columnBuilder.tdEnd();
        }*/
        
        return columnBuilder.toString();
    	
    }
    
    
}