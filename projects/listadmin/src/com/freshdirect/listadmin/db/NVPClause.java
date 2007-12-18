package com.freshdirect.listadmin.db;

import java.util.Iterator;

import com.freshdirect.listadmin.nvp.NVP;
import com.freshdirect.listadmin.nvp.NVPSourceI;

public abstract class NVPClause extends Clause implements NVPSourceI {

	public NVPClause() {}
	public NVPClause(String column, int operatorId) {
		super(column,operatorId);
	}
	
	public Object getRuntimeValue() {
		for(Iterator it=iterator();it.hasNext();) {
			NVP nvp = (NVP) it.next();
			
			if(nvp.getSelected()) {
				return nvp.getValue();
			}
		}
		
		return null;
	}
	
	public String toString() {
		return super.toString();
	}
}
