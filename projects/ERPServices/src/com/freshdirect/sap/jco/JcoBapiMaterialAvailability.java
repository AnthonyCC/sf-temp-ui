package com.freshdirect.sap.jco;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.freshdirect.erp.EnumATPRule;
import com.freshdirect.sap.bapi.BapiException;
import com.freshdirect.sap.bapi.BapiMaterialAvailability;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoTable;

/**
 *
 *
 * @version $Revision$
 * @author $Author$
 */
class JcoBapiMaterialAvailability extends JcoBapiFunction implements BapiMaterialAvailability {

	private Date[] commitedDates;
	private double[] commitedQtys;
	
	public final static Map<String,String> ATPRule_TO_BAPI_MAPPING;
	
	static
    {
		ATPRule_TO_BAPI_MAPPING = new HashMap<String, String>();
		ATPRule_TO_BAPI_MAPPING.put(EnumATPRule.MULTILEVEL_MATERIAL.getName(), "ZFDX_MATERIAL_AVAILABILITY");
		ATPRule_TO_BAPI_MAPPING.put(EnumATPRule.SINGLELEVEL_MATERIAL.getName(), "Z_SINGLE_LEVEL_MATERIAL_AVAIL");
		ATPRule_TO_BAPI_MAPPING.put(EnumATPRule.MATERIAL.getName(), "Z_BAPI_MATERIAL_AVAILABILITY");
    }
	
	public JcoBapiMaterialAvailability(EnumATPRule atpRule) throws JCoException {
		
		super(ATPRule_TO_BAPI_MAPPING.get(atpRule.getName()));
	}

	public void setPlant(String plant) {
		this.function.getImportParameterList().setValue("PLANT", plant);
	}

	public void setStgeLoc(String stgeLoc) {
		this.function.getImportParameterList().setValue("STGE_LOC", stgeLoc);
	}

	public void setCustomerNumber(String customerNo) {
		this.function.getImportParameterList().setValue("CUSTOMER", customerNo);
	}

	public void setMaterialNumber(String materialNo) {
		this.function.getImportParameterList().setValue("MATERIAL", materialNo);
	}

	public void setSalesUnit(String salesUnit) {
		this.function.getImportParameterList().setValue("UNIT", salesUnit);
	}

	public void addRequest(Date requestedDate, double quantity)
	{
		JCoTable wmdvsx = this.function.getTableParameterList().getTable("WMDVSX");
	
		wmdvsx.appendRow();
		wmdvsx.setValue("REQ_DATE", requestedDate);
		wmdvsx.setValue("REQ_QTY", quantity);
	}

	public void processResponse() throws BapiException {
		super.processResponse();

		JCoTable wmdvex = function.getTableParameterList().getTable("WMDVEX");

		// fill inventory entries
		this.commitedDates = new Date[wmdvex.getNumRows()];
		this.commitedQtys = new double[commitedDates.length];
		wmdvex.firstRow();
		
		for (int i = 0; i < commitedDates.length; i++)
		{
			this.commitedDates[i] = wmdvex.getDate("COM_DATE");
			this.commitedQtys[i] = wmdvex.getDouble("COM_QTY");
			wmdvex.nextRow();
		}
	}

	public int getCommitedLength() {
		return this.commitedDates.length;
	}

	public Date getCommitedDate(int index) {
		return this.commitedDates[index];
	}

	public double getCommitedQty(int index) {
		return this.commitedQtys[index];
	}

}
