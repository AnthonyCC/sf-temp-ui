package com.freshdirect.dataloader.bapi;

import com.sap.mw.jco.JCO;

public interface BapiFunctionI {

	public JCO.MetaData getFunctionMetaData();

	public JCO.MetaData[] getStructureMetaData();
	
	public void execute(JCO.ParameterList input, JCO.ParameterList output, JCO.ParameterList tables);

}
