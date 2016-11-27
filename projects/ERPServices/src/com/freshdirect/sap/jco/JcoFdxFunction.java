package com.freshdirect.sap.jco;

import org.apache.log4j.Category;

import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.sap.bapi.BapiException;
import com.sap.conn.jco.JCoException;

/**
 *
 *
 * @version $Revision$
 * @author $Author$
 */
abstract class JcoFdxFunction extends JcoBapiFunction {

	private static Category LOGGER = LoggerFactory.getInstance(JcoFdxFunction.class);

	public JcoFdxFunction(String functionName) throws JCoException {
		super(functionName);
	}
	
	@Override
	protected void processResponse() throws BapiException {
		
		// response structure for FDX SAP Interfaces are different. They don't follow the BAPI convention. commenting this as it is no longer applicable.
		
		/*
		List retList = new ArrayList();

		if (function.getExportParameterList() != null && function.getExportParameterList().getMetaData().hasField("RETURN")
				&& function.getExportParameterList().getMetaData().isStructure("RETURN") ) {
			// get "return" as a struct
			JCoStructure ret = function.getExportParameterList().getStructure("RETURN");
			retList.add(this.convertReturn(ret));

		} else if (function.getTableParameterList() != null && function.getTableParameterList().getMetaData().hasField("RETURN")
				&& function.getTableParameterList().getMetaData().isTable("RETURN")) {
			// get "return" as a table
			JCoTable ret = function.getTableParameterList().getTable("RETURN");
			ret.firstRow();
			do {
				retList.add(this.convertReturn(ret));
			} while (ret.nextRow());
		} 

		this.bapiInfos = (BapiInfo[]) retList.toArray(new BapiInfo[0]);
	*/}
}