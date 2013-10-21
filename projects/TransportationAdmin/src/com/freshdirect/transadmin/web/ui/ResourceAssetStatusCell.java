package com.freshdirect.transadmin.web.ui;

import java.util.Iterator;
import java.util.List;

import org.extremecomponents.table.bean.Column;
import org.extremecomponents.table.core.TableModel;
import org.extremecomponents.table.view.html.ColumnBuilder;
import org.extremecomponents.util.HtmlBuilder;

import com.freshdirect.transadmin.constants.EnumAssetScanStatus;
import com.freshdirect.transadmin.util.DispatchPlanUtil;
import com.freshdirect.transadmin.util.TransStringUtil;
import com.freshdirect.transadmin.util.TransportationAdminProperties;
import com.freshdirect.transadmin.web.model.AssetScanInfo;
import com.freshdirect.transadmin.web.model.DispatchCommand;
import com.freshdirect.transadmin.web.model.DispatchResourceInfo;
import com.freshdirect.transadmin.web.model.ResourceList;

public class ResourceAssetStatusCell extends FDBaseCell  {
	
	public String getExportDisplay(TableModel model, Column column) {
		
		DispatchCommand dispatch = (DispatchCommand) model.getCurrentRowBean();
		List resources = dispatch.getDispatchResources();
		if (resources != null) {
			StringBuffer response = new StringBuffer(200);

			for (int i = 0; i < resources.size(); i++) {
				DispatchResourceInfo resourceInfo = (DispatchResourceInfo) resources
						.get(i);
				if(resourceInfo.getScannedAssets() != null) {
					for (int j = 0; j < resourceInfo.getScannedAssets().size(); j++) {
						String name = ((AssetScanInfo) resourceInfo.getScannedAssets().get(j)).getAssetNo();;
						response.append(name);
	
						if (i < (resourceInfo.getScannedAssets().size() - 1) && !TransStringUtil.isEmpty(name)) {
							response.append(TransportationAdminProperties.getCellDataSeperator());
						}
					}
				}
			}
			if (TransportationAdminProperties.getCellDataSeperator().equals(response.toString())) {
				return "";
			}

			if (response.toString().endsWith(TransportationAdminProperties.getCellDataSeperator()))
				return response.toString().substring(0,	response.toString().lastIndexOf(TransportationAdminProperties.getCellDataSeperator()));
			return response.toString();
		}
		return "";    
	}

    public String getHtmlDisplay(TableModel model, Column column) {   
    	
           
        ColumnBuilder columnBuilder = new ColumnBuilder(column);
        HtmlBuilder html = columnBuilder.getHtmlBuilder();
        columnBuilder.tdStart();
        DispatchCommand dispatch = (DispatchCommand) model.getCurrentRowBean();
        
        html.table(0).close();        
        
        List resources = dispatch.getDispatchResources();
        if (resources != null) {
			for (int i = 0; i < resources.size(); i++) {
				DispatchResourceInfo resourceInfo = (DispatchResourceInfo) resources.get(i);
				if(resourceInfo.getScannedAssets() != null) {
					for (int j = 0; j < resourceInfo.getScannedAssets().size(); j++) {
						String assetNo = ((AssetScanInfo)resourceInfo.getScannedAssets().get(j)).getAssetNo();
						String status =  ((AssetScanInfo)resourceInfo.getScannedAssets().get(j)).getStatus();
						
						html.tr(j).close();
						HtmlBuilder td = html.td(0);
						if(EnumAssetScanStatus.CHECK_IN.getName().equals(status)) {
							td.styleClass("asset_checkin");
						} else {
							td.styleClass("asset_checkout");
						}					
						td.close();
						td.append(assetNo);
						html.tdEnd();
						html.trEnd(j);
					}
				}
			}
        }
        html.tableEnd(0);
        columnBuilder.tdEnd();
               
        return columnBuilder.toString();
    	
    }

}