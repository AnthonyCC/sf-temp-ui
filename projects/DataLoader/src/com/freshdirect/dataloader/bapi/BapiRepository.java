package com.freshdirect.dataloader.bapi;

import java.util.*;

import com.sap.mw.jco.*;

public class BapiRepository extends JCO.BasicRepository {
	private Map functions;
	
	public BapiRepository(String name) {
		super(name);
		this.functions = new HashMap();
	}
	
	public void addFunction(BapiFunctionI function) {

		JCO.MetaData[] structMeta = function.getStructureMetaData();
		if (structMeta!=null) {
			for (int i=0; i<structMeta.length; i++) {
				this.addStructureDefinitionToCache(structMeta[i]);
			}
		}
		
		JCO.MetaData meta = function.getFunctionMetaData();
		this.functions.put(meta.getName(), function);
		this.addFunctionInterfaceToCache(meta);
	}
	
	public BapiFunctionI getFunction(String name) {
		return (BapiFunctionI) this.functions.get(name);
	}
}

