package com.freshdirect.athena.data;

import java.io.Serializable;
import java.util.List;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;


@Root
public class Variable implements Serializable {
	
	@Attribute
	private String name; 
	
	@ElementList(entry="row",inline=true,required=false)
	private List<Row> row;

	public Variable() {
		super();
	}
	
	public Variable(String name) {
		super();
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Row> getRow() {
		return row;
	}

	public void setRow(List<Row> row) {
		this.row = row;
	}

	@Override
	public String toString() {
		return "Variable [name=" + name + ", row=" + row + "]";
	}
	
	
}
