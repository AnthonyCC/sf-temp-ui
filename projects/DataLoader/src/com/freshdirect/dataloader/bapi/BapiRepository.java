package com.freshdirect.dataloader.bapi;

import java.util.HashMap;
import java.util.Map;

import com.sap.mw.jco.JCO;
import com.sap.mw.jco.JCO.MetaData;

public class BapiRepository extends JCO.BasicRepository {
	private Map<String, BapiFunctionI> functions;
	
	public BapiRepository(String name) {
		super(name);
		this.functions = new HashMap<String, BapiFunctionI>();
	}
	
	public void addFunction(BapiFunctionI function) {

		JCO.MetaData[] structMeta = function.getStructureMetaData();
		if (structMeta!=null) {
			for (MetaData element : structMeta) {
				this.addStructureDefinitionToCache(element);
			}
		}
		
		JCO.MetaData meta = function.getFunctionMetaData();
		this.functions.put(meta.getName(), function);
		this.addFunctionInterfaceToCache(meta);
	}
	
	public BapiFunctionI getFunction(String name) {
		return this.functions.get(name);
	}
}

