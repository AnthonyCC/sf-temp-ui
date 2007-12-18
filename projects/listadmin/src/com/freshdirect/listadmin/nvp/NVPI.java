package com.freshdirect.listadmin.nvp;

/*
 * An abstract name/value pair
 */
public interface NVPI {
	public String getName();
	public String getValue();
	
	// For tapestry
	public boolean getSelected();
}
