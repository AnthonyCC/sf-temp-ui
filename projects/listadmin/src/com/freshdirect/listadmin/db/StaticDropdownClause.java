package com.freshdirect.listadmin.db;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import com.freshdirect.listadmin.nvp.NVP;
import com.freshdirect.listadmin.nvp.NVPSourceI;
import com.freshdirect.listadmin.query.QueryContextI;

public class StaticDropdownClause extends NVPClause implements NVPSourceI, Serializable {
	private String column;
	private int operatorId;
	private String options;
	private String param;
	
	private List nvps = new ArrayList();
	
	public StaticDropdownClause() {}
	
	public StaticDropdownClause(String column, int operatorId, String options, String param) {
		this.column     = column;
		this.operatorId = operatorId;
		this.param      = param;
		
		setOptions(options);
	}

	public boolean getRunnable(QueryContextI context) {
		return context.get(getClauseId()) != null;
	}

	public String getOptions() {
		return options;
	}

	public void setOptions(String options) {
		this.options = options;
		
		StringTokenizer st = new StringTokenizer(options,"|");

		int count = 0;
		
		while(st.hasMoreTokens()) {
			String token = st.nextToken();
			int pos      = token.indexOf('=');
			if(pos == -1) {
				nvps.add(new NVP(token,token));
			} else {
				nvps.add(new NVP(token.substring(0,pos),token.substring(pos+1)));
			}
			
			count++;
		}
	}
	
	public String getColumn() {
		return column;
	}
	public void setColumn(String column) {
		this.column = column;
	}

	public int getOperatorId() {
		return operatorId;
	}
	public void setOperatorId(int operatorId) {
		this.operatorId = operatorId;
	}

	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}
	
	public Iterator iterator() {return nvps.iterator();}
	
	public String toString() {
		return super.toString() + "values from among: " + getOptions();
	}
	
	public String getXml() {
		return "<staticDropdown field='" + getColumn() + "' " +
		       "operatorId='" + getOperatorId()  + "' " +
		       "options='" + getOptions() + 
		       "'/>";
	}
}
