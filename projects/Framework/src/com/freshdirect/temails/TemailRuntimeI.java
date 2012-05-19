package com.freshdirect.temails;

import java.io.Serializable;

import com.freshdirect.rules.Rule;

public interface TemailRuntimeI extends Serializable{

	public String formatTemplate(Object target, Template t);
	
}
