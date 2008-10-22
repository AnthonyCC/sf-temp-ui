package com.freshdirect.routing.manager;

import java.util.List;

import com.freshdirect.routing.service.proxy.UtilityProxy;

public class UtilityManager {
	
	public boolean isValidExpression(String expression, List variableList) {
		UtilityProxy proxy = new UtilityProxy();
		return proxy.isValidExpression(expression, variableList);
	}
}
