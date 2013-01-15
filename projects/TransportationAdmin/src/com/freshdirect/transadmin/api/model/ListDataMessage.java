package com.freshdirect.transadmin.api.model;

import java.util.Collection;

public class ListDataMessage extends Message {
	
	private Collection rows;
    
    @SuppressWarnings("rawtypes")
	public Collection getRows() {
		return rows;
	}

	@SuppressWarnings("rawtypes")
	public void setRows(Collection rows) {
		this.rows = rows;
	}
}
