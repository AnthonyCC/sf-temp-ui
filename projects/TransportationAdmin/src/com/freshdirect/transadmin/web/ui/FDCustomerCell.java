package com.freshdirect.transadmin.web.ui;

import org.apache.commons.beanutils.PropertyUtils;
import org.extremecomponents.table.bean.Column;
import org.extremecomponents.table.core.TableModel;
import org.extremecomponents.table.view.html.ColumnBuilder;

import com.freshdirect.transadmin.model.DlvLocation;

public class FDCustomerCell extends FDBaseCell  {
	
	public String getExportDisplay(TableModel model, Column column) {
        return null;
    }

    public String getHtmlDisplay(TableModel model, Column column) {    	
        ColumnBuilder columnBuilder = new ColumnBuilder(column);
        
        columnBuilder.tdStart();
        Object objBean = model.getCurrentRowBean();
                        
        int width = 400;
        int height = 400;
        int buffer = 20;
        try {  
        	String id = null;
        	if(objBean instanceof DlvLocation){
        		id = ""+PropertyUtils.getProperty(objBean, "locationId");
        	}else{
        		id = ""+PropertyUtils.getProperty(objBean, "buildingId");
        	}
            
            StringBuffer strBuf = new StringBuffer();
            if(objBean instanceof DlvLocation){
            	strBuf.append("javascript:pop('customerinfo.do?context=location&id=");
            }else{
            	strBuf.append("javascript:pop('customerinfo.do?context=building&id=");
            }
            //strBuf.append("javascript:pop('http://maps.google.com/?saddr=");
            strBuf.append(id);            
            strBuf.append("&width=");
            strBuf.append(width);
            strBuf.append("&height=");
            strBuf.append(height);  
            strBuf.append("', ").append(height+buffer).append(",").append(width+buffer).append(")");
            
            columnBuilder.getHtmlBuilder().a(strBuf.toString()).close().img("images/customer.png").aEnd();            
            //columnBuilder.getHtmlBuilder().xclose();
        } catch (Exception e) {e.printStackTrace();}
        
        columnBuilder.tdEnd();
        
        return columnBuilder.toString();
    }

}
