package com.freshdirect.routing.model;

public interface IRegionModel {
	
	String getRegionCode();

	void setRegionCode(String regionCode);
	
	boolean isDepot();
	void setDepot(boolean isDepot);

	String getName();
	void setName(String name);

	String getDescription();
	void setDescription(String description);
	
	
}
