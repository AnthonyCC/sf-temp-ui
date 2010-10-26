package com.freshdirect.transadmin.web.ui;

import java.util.Map;

import org.extremecomponents.table.bean.Column;
import org.extremecomponents.table.core.TableModel;
import org.extremecomponents.table.view.html.ColumnBuilder;
import org.extremecomponents.util.HtmlBuilder;

import com.freshdirect.transadmin.model.Asset;
import com.freshdirect.transadmin.util.DispatchPlanUtil;
import com.freshdirect.transadmin.util.TransportationAdminProperties;
import com.freshdirect.transadmin.web.model.DispatchCommand;

public class FDDispatchExtraCell extends FDBaseCell  {
	
	public String getExportDisplay(TableModel model, Column column) {
		
		DispatchCommand dispatch = (DispatchCommand)model.getCurrentRowBean();
		
		StringBuffer strBuf = new StringBuffer();
		if(dispatch.getGpsNumber() != null) {
			strBuf.append(getAssetIdentifier(model,DispatchPlanUtil.ASSETTYPE_GPS, dispatch.getGpsNumber()));
		}
		if(dispatch.getEzpassNumber() != null) {
			if(strBuf.length() > 0) {
				strBuf.append(TransportationAdminProperties.getCellDataSeperator());
			}
			strBuf.append(getAssetIdentifier(model,DispatchPlanUtil.ASSETTYPE_EZPASS, dispatch.getEzpassNumber()));
		}
		
		if(dispatch.getMotKitNumber() != null) {
			if(strBuf.length() > 0) {
				strBuf.append(TransportationAdminProperties.getCellDataSeperator());
			}
			strBuf.append(getAssetIdentifier(model,DispatchPlanUtil.ASSETTYPE_MOTKIT,dispatch.getMotKitNumber()));
		}
		
		if(dispatch.getAdditionalNextels() != null) {
			if(strBuf.length() > 0) {
				strBuf.append(TransportationAdminProperties.getCellDataSeperator());
			}
			strBuf.append(dispatch.getAdditionalNextels());
		}
		
		
		return strBuf.toString();		 
	}

    public String getHtmlDisplay(TableModel model, Column column) {   
    	
        ColumnBuilder columnBuilder = new ColumnBuilder(column);
        HtmlBuilder html = columnBuilder.getHtmlBuilder();
        columnBuilder.tdStart();
        DispatchCommand dispatch = (DispatchCommand)model.getCurrentRowBean();
        
        html.table(0).close();
        
        int index = 0;
		if(dispatch.getGpsNumber() != null) {
			html.tr(index).close();
			HtmlBuilder td = html.td(0); 
			td.styleClass("asset_gps");
			td.close();
			td.append(getAssetIdentifier(model,DispatchPlanUtil.ASSETTYPE_GPS, dispatch.getGpsNumber()));
			html.tdEnd();
			html.trEnd(index);
			index++;
		}
		
		if(dispatch.getEzpassNumber() != null) {
			html.tr(index).close();
			HtmlBuilder td = html.td(0); 
			td.styleClass("asset_ezpass");
			td.close();
			td.append(getAssetIdentifier(model,DispatchPlanUtil.ASSETTYPE_EZPASS, dispatch.getEzpassNumber()));
			html.tdEnd();
			html.trEnd(index);
			index++;			
		}
		
		if(dispatch.getMotKitNumber() != null) {
			html.tr(index).close();
			HtmlBuilder td = html.td(0); 
			td.styleClass("asset_motkit");
			td.close(); 
			td.append(getAssetIdentifier(model,DispatchPlanUtil.ASSETTYPE_MOTKIT,dispatch.getMotKitNumber()));
			html.tdEnd();
			html.trEnd(index);
			index++;			
		}
		
		if(dispatch.getAdditionalNextels() != null) {
			html.tr(index).close();
			HtmlBuilder td = html.td(0); 
			td.styleClass("asset_additionalnextels");
			td.close(); 
			td.append(dispatch.getAdditionalNextels());
			html.tdEnd();
			html.trEnd(index);
			index++;			
		}
		html.tableEnd(0);
        columnBuilder.tdEnd();
               
        return columnBuilder.toString();
    	
    }
    
    private String getAssetIdentifier(TableModel model,String assetLookup, String id) {
    	Map<String, Asset> assetMapping = (Map<String, Asset>)model.getContext().getRequestAttribute(assetLookup);
    	if(assetMapping != null && id != null && id.trim().length() > 0 && assetMapping.containsKey(id)) {
    		return assetMapping.get(id).getAssetNo();
    	} else {
    		return "<del>"+id+"</del>";
    	}
    	
    }    
}