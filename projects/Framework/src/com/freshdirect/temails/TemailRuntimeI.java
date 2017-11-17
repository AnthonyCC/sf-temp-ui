package com.freshdirect.temails;

import java.io.Serializable;

public interface TemailRuntimeI extends Serializable{

	public String formatTemplate(Object target, Template t);
	
}
