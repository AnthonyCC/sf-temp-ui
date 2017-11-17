package com.freshdirect.temails;

import java.io.Serializable;


public interface ParserI extends Serializable {

	public String parse(Object target, TemailRuntimeI ctx);

	public boolean validate();
	
	public void setOrder(String order);
	
	public String getOrder();

	public String getId();
	
	public String getParentId();

	public void setChildParser(ParserI p);
	
	public ParserI getChildParser();
	
	public int getLevel();
	
}