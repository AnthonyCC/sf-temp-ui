package com.freshdirect.athena.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root
public class Row  implements Serializable {
	
	@ElementList(data=true,empty =true,entry="column",inline=true,required=false)
	private List<String> column;

	public List<String> getColumn() {
		return column;
	}

	public void setColumn(List<String> column) {
		this.column = column;
	}
	
	public void addColumn(Object columnV) {
		if(column == null) {
			column = new ArrayList<String>();
		}
		this.column.add(columnV != null ? columnV.toString() : null);
	}

	public Row(String[] data) {
		super();		
		this.column = Arrays.asList(data);
	}
	
	public Row() {
		super();
	}
		
}
