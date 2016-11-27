package com.freshdirect.athena.config;

import java.io.File;
import java.util.List;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

public class ApiConfig {
	
	@ElementList
	private List<Api> apis;
	
	@Attribute
	private String name;

	public List<Api> getApis() {
		return apis;
	}

	public void setApis(List<Api> apis) {
		this.apis = apis;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return "ApiConfig [apis=" + apis + ", name=" + name + "]";
	}

	public static void main(String args[]) throws Exception { //fdcart-apis.xml
		Serializer serializer = new Persister();
		File source = new File("C:\\trunk\\projects\\Athena\\config\\sap-apis.xml");

		ApiConfig config = serializer.read(ApiConfig.class, source);
	}
}
