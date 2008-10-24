/*
 * $Workfile$
 *
 * $Date$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.sap.jco;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.sap.bapi.BapiCartonInfo;
import com.sap.mw.jco.JCO;

/**
 *
 *
 * @version $Revision$
 * @author $Author$
 */
class JcoBapiCartonInfo extends JcoBapiFunction implements BapiCartonInfo {

	private JCO.Table lstOrderID;
	
	private JCO.Table lstCartonInfo;

	public JcoBapiCartonInfo() {
		super(ErpServicesProperties.getCartonInfoFunctionName());

	}

	public void setOrderIds(List orderIdList) {
		lstOrderID = this.function.getTableParameterList().getTable("SALESORDER");
		if(orderIdList != null) {
			Iterator tmpIterator = orderIdList.iterator();
			String strOrderNo = null;
			while(tmpIterator.hasNext()) {
				strOrderNo = (String)tmpIterator.next();
				lstOrderID.insertRow(1);
				lstOrderID.setValue(strOrderNo, "VBELN");				
				lstOrderID.nextRow();				
			}
		}		
	}
	
	public Map getCartonInfos() {
		Map cartonInfoMap = new HashMap();
		lstCartonInfo = this.function.getTableParameterList().getTable("ZCARTON_COUNT");
		Map rowMap = null;
		String strOrderId = null;
		for (int loop = 0; loop < lstCartonInfo.getNumRows(); loop++) {
			strOrderId = lstCartonInfo.getString("VBELN");			
			rowMap = (Map)cartonInfoMap.get(strOrderId);
			if(rowMap == null) {
				rowMap = new HashMap();
				cartonInfoMap.put(strOrderId, rowMap);				
			}			
			rowMap.put(lstCartonInfo.getValue("CARTONTYPE"), lstCartonInfo.getValue("QUANTITY"));					
			lstCartonInfo.nextRow();
		}
		return cartonInfoMap;
	}

}
