package com.freshdirect.athena.config;

import java.io.File;
import java.io.Serializable;
import java.util.List;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

@Root
public class DatasourceConfig implements Serializable{ //fdcart-datasources.xml, fdsapprod-datasources.xml
	
	@ElementList
	private List<Datasource> datasources;
	
	@Attribute
	private String name;

	public List<Datasource> getDatasources() {
		return datasources;
	}

	public void setDatasources(List<Datasource> datasources) {
		this.datasources = datasources;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "DatasourceConfig [datasources=" + datasources + ", name="
				+ name + "]";
	}
	
	public static void main(String args[]) throws Exception {
		Serializer serializer = new Persister();
		File source = new File("C:\\trunk\\projects\\Athena\\config\\fdsapprod-datasources.xml");

		DatasourceConfig dbConfig = serializer.read(DatasourceConfig.class, source);
		
	}
	
}
