package com.freshdirect.sap.jco;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.freshdirect.sap.SapProperties;
import com.freshdirect.sap.bapi.BapiCartonInfo;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoTable;

/**
 *
 *
 * @version $Revision$
 * @author $Author$
 */
class JcoBapiCartonInfo extends JcoBapiFunction implements BapiCartonInfo {

	private JCoTable lstOrderID;
	
	private JCoTable lstCartonInfo;

	public JcoBapiCartonInfo() throws JCoException
	{
		super(SapProperties.getCartonInfoFunctionName());
	}

	@SuppressWarnings("rawtypes")
	public void setOrderIds(List orderIdList)
	{
		lstOrderID = this.function.getTableParameterList().getTable("SALESORDER");
		if(orderIdList != null) {
			Iterator tmpIterator = orderIdList.iterator();
			String strOrderNo = null;
			while(tmpIterator.hasNext()) {
				strOrderNo = (String)tmpIterator.next();
				lstOrderID.insertRow(1);
				lstOrderID.setValue("VBELN", strOrderNo);				
				lstOrderID.nextRow();				
			}
		}		
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
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
