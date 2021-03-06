package com.freshdirect.fdstore.content;

public interface WineFilterValue {
	public String getEncoded();
	
	public String getDomainEncoded();
	
	public String getDomainName();
	
	public String getFilterRepresentation();
	
	public EnumWineFilterValueType getWineFilterValueType();
}
