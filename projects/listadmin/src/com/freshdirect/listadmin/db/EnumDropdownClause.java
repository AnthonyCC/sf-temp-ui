package com.freshdirect.listadmin.db;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.enum.Enum;
import org.apache.commons.lang.enum.EnumUtils;

import com.freshdirect.listadmin.nvp.NVP;
import com.freshdirect.listadmin.nvp.NVPSourceI;

public class EnumDropdownClause extends NVPClause implements NVPSourceI {
	private static final long serialVersionUID = 2604860205177151177L;
	
	private String column;
	private int operatorId;
	private String className;
	
	
	private List nvps = new ArrayList();
	
	public EnumDropdownClause() {}
	
	public EnumDropdownClause(String column, int operatorId, String className) {
		this.column     = column;
		this.operatorId = operatorId;
		
		setClassName(className);
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
	
	public Iterator iterator() {
		if(nvps == null) {
			return new HashSet().iterator();
		}
		return nvps.iterator();
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		// Something inside hibernate calls this at startup.
		// I don't know how or why, but it seems className is
		// some sort of reserved name in hibernate.
		
		if(className == null) {
			return;
		}
		
		this.className = className;
		Class c        = null;
		nvps           = new ArrayList();
		
		try {
			c = Class.forName(className);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		
		for(Iterator it=EnumUtils.iterator(c);it.hasNext();) {
			Enum e = (Enum) it.next();
			
			nvps.add(new NVP(e.getName(),e.getName()));
		}
	}
	
	public String toString() {
		return super.toString() + "values from the enum type: " + getClassName();
	}
	
	public String getXml() {
		return "<enumDropdown field='" + getColumn() + "' " +
		       "operatorId='" + getOperatorId()  + "' " +
		       "constant='" + getClassName() + 
		       "'/>";
	}
}
