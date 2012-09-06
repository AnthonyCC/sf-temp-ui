package com.freshdirect.athena.data;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

@Root
public class Data implements Serializable {
	
	@ElementList(entry="variable",inline=true,required=false)
	private List<Variable> variables;

	public List<Variable> getVariables() {
		return variables;
	}
	public void setVariables(List<Variable> variables) {
		this.variables = variables;
	}

	public Variable getVariable(String variable) {
		for(Variable var: variables)
		{
			if(variable.equals(var.getName()))
				return var;
		}
		return null;
	}
	public void addVariable(Variable variable) {
		if(variables == null) {
			variables = new ArrayList<Variable>();
		}
		variables.add(variable);
	}

	@Override
	public String toString() {
		String var = "";
		for(Variable v : variables)
			var = var + v.toString();
		return "Data [variable=" + var + "]";
	}
	
	public static void main(String a[]) throws Exception  {
		
		Serializer serializer = new Persister();
		Data data = new Data();
		Variable variable = new Variable("orderrate");
		data.addVariable(variable);
		
		List<Row> rows = new ArrayList<Row>();
		variable.setRow(rows);
		
		rows.add(new Row(new String[]{"A","B"}));
		rows.add(new Row(new String[]{"C","D"}));
		rows.add(new Row(new String[]{"E","F"}));
		
		File result = new File("C:\\test.xml");

		serializer.write(data, result);
	}
}
