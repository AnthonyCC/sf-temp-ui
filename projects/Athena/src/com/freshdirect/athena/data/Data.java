package com.freshdirect.athena.data;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

@Root
public class Data implements Serializable {
	
	@Element(required=false)
	private Variable variable;

	public Variable getVariable() {
		return variable;
	}

	public void setVariable(Variable variable) {
		this.variable = variable;
	}

	@Override
	public String toString() {
		return "Data [variable=" + variable + "]";
	}
	
	public static void main(String a[]) throws Exception  {
		
		Serializer serializer = new Persister();
		Data data = new Data();
		Variable variable = new Variable("orderrate");
		data.setVariable(variable);
		
		List<Row> rows = new ArrayList<Row>();
		variable.setRow(rows);
		
		rows.add(new Row(new String[]{"A","B"}));
		rows.add(new Row(new String[]{"C","D"}));
		rows.add(new Row(new String[]{"E","F"}));
		
		File result = new File("C:\\test.xml");

		serializer.write(data, result);
	}
}
