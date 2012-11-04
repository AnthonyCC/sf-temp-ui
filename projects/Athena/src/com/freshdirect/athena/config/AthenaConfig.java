package com.freshdirect.athena.config;

import java.io.File;
import java.io.Serializable;
import java.util.List;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

@Root
public class AthenaConfig implements Serializable { // athena-config.xml
		
	@ElementList
	private List<String> datasources;
	
	@ElementList
	private List<String> services;
	
	@ElementList
	private List<Entry> properties;
	
	public List<String> getDatasources() {
		return datasources;
	}


	public void setDatasources(List<String> datasources) {
		this.datasources = datasources;
	}


	public List<String> getServices() {
		return services;
	}


	public void setServices(List<String> services) {
		this.services = services;
	}
	
	
	public List<Entry> getProperties() {
		return properties;
	}


	public void setProperties(List<Entry> properties) {
		this.properties = properties;
	}


	@Override
	public String toString() {
		return "AthenaConfig [datasources=" + datasources + ", services="
				+ services + ", properties=" + properties + "]";
	}


	public static void main(String args[]) throws Exception {
		Serializer serializer = new Persister();
		File source = new File("C:\\trunk\\projects\\Athena\\config\\athena-config.xml");

		AthenaConfig athenaConfig = serializer.read(AthenaConfig.class, source);
	}
}
