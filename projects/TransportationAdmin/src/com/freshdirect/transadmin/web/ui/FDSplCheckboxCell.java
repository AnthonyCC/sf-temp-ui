package com.freshdirect.transadmin.web.ui;

import org.extremecomponents.table.bean.Column;
import org.extremecomponents.table.core.TableModel;
import org.extremecomponents.table.view.html.ColumnBuilder;

import com.freshdirect.transadmin.util.DispatchPlanUtil;
import com.freshdirect.transadmin.util.EnumStatus;
import com.freshdirect.transadmin.web.model.DispatchCommand;

public class FDSplCheckboxCell extends FDBaseCell 
{

	public String getExportDisplay(TableModel model, Column column) {
        return null;
    }

    public String getHtmlDisplay(TableModel model, Column column) 
    {    	
        ColumnBuilder columnBuilder = new ColumnBuilder(column);
        columnBuilder.tdStart();
       
        try {    
        	Object obj=model.getCurrentRowBean();
        	
        	if(obj instanceof DispatchCommand)
			{
        		
        		DispatchCommand command=(DispatchCommand)obj;
        		String id=command.getDispatchId();
        		boolean value=Boolean.valueOf(column.getPropertyValue().toString()).booleanValue();
        		boolean enabled=false;
        		
        		if(command.isToday())
        		{
	        		if("phoneAssigned".equalsIgnoreCase(column.getProperty()))
	    			{
	        			if(("false".equalsIgnoreCase(command.getIsBullpen()) || "N".equalsIgnoreCase(command.getIsBullpen()))&&(command.getDispatchStatus()==EnumStatus.ActualTruck))
	        			{
	        				enabled=true;
	        			}
	    			}
	    			if("keysReady".equalsIgnoreCase(column.getProperty()))
	    			{
	    				if(("false".equalsIgnoreCase(command.getIsBullpen()) ||"N".equalsIgnoreCase(command.getIsBullpen()))&&(command.getDispatchStatus()==EnumStatus.ActualTruck))
	        			{
	        				enabled=true;
	        			}
	    			}
	    			if("dispatched".equalsIgnoreCase(column.getProperty()))
	    			{
	    				if((command.getDispatchStatus()==EnumStatus.Ready)||(command.getDispatchStatus()==EnumStatus.EmpReady)||DispatchPlanUtil.isBullpen(command.getIsBullpen()))
	        			{
	        				enabled=true;
	        			}
	    				if(DispatchPlanUtil.isBullpen(command.getIsBullpen())&&value)
	    				{
	    					enabled=false;
	    				}
	    			}
        		}
        		//if(command.isTodayTomorrow())
        		{
	    			if("checkedIn".equalsIgnoreCase(column.getProperty()))
	    			{
	    				if((command.getDispatchStatus()==EnumStatus.Dispatched)||DispatchPlanUtil.isBullpen(command.getIsBullpen()))
	        			{
	        				enabled=true;
	        			}
	    				if(DispatchPlanUtil.isBullpen(command.getIsBullpen())&&value)
	    				{
	    					enabled=false;
	    				}
	    				if(DispatchPlanUtil.isBullpen(command.getIsBullpen())&&!command.isDispatched())
	    				{
	    					enabled=false;
	    				}
	    			}
        		}
        		
        		
        		 columnBuilder.getHtmlBuilder().input("checkbox").name(id+"_"+column.getProperty()).styleClass("datalistchx"); 
        		 if(!enabled)columnBuilder.getHtmlBuilder().disabled();
        		 if(value)columnBuilder.getHtmlBuilder().checked();
                 columnBuilder.getHtmlBuilder().xclose();
			}        	
        	
           
            
            
        } catch (Exception e) {e.printStackTrace();}
        
        columnBuilder.tdEnd();
        
        return columnBuilder.toString();
    }
}
