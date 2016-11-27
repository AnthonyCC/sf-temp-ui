package com.freshdirect.transadmin.web.ui;

import org.apache.commons.beanutils.PropertyUtils;
import org.extremecomponents.table.bean.Column;
import org.extremecomponents.table.core.TableModel;
import org.extremecomponents.table.view.html.ColumnBuilder;

import com.freshdirect.transadmin.model.DlvLocation;
import com.freshdirect.transadmin.util.TransportationAdminProperties;

public class FDGeocodeCell extends FDBaseCell  {
	
	public String getExportDisplay(TableModel model, Column column) {
        return null;
    }

    public String getHtmlDisplay(TableModel model, Column column) { 
    	
        ColumnBuilder columnBuilder = new ColumnBuilder(column);
        
        columnBuilder.tdStart();
        Object objBean = model.getCurrentRowBean();
                        
        int width = 800;
        int height = 400;
        int buffer = 20;
        try {        	
            String address = null;
            String latitude = null;
            String longitude = null;
            
            if(objBean instanceof DlvLocation) {
            	address = PropertyUtils.getProperty(objBean, "building.srubbedStreet")+" "+PropertyUtils.getProperty(objBean, "building.zip");
                latitude = ""+PropertyUtils.getProperty(objBean, "building.latitude");
                longitude =""+PropertyUtils.getProperty(objBean, "building.longitude");
            } else {
            	address = PropertyUtils.getProperty(objBean, "srubbedStreet")+" "+PropertyUtils.getProperty(objBean, "zip");
                latitude = ""+PropertyUtils.getProperty(objBean, "latitude");
                longitude =""+PropertyUtils.getProperty(objBean, "longitude");
            }
            
            StringBuffer strBuf = new StringBuffer();
            strBuf.append("javascript:pop('common/geography.jsp?address=");
            //strBuf.append("javascript:pop('http://maps.google.com/?saddr=");
            strBuf.append(address);
            strBuf.append("&latitude=");
            //strBuf.append("&daddr=");
            strBuf.append(latitude);
            //strBuf.append(",");
            strBuf.append("&longitude=");
            strBuf.append(longitude);
            strBuf.append("&width=");
            strBuf.append(width);
            strBuf.append("&height=");
            strBuf.append(height);
            strBuf.append("&mapkey=");
            strBuf.append(TransportationAdminProperties.getDefaultMapKey());
            
            strBuf.append("', ").append(height+buffer).append(",").append(width+buffer).append(")");
            
            columnBuilder.getHtmlBuilder().a(strBuf.toString()).close().img("images/bg_ball.gif").aEnd();            
            //columnBuilder.getHtmlBuilder().xclose();
        } catch (Exception e) {e.printStackTrace();}
        
        columnBuilder.tdEnd();
        
        return columnBuilder.toString();
    }

}
