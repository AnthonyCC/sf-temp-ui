package com.freshdirect.routing.service.impl;

import java.util.List;

import com.freshdirect.routing.service.IUtilityService;
import com.freshdirect.routing.util.ServiceTimeUtil;

public class UtilityService implements IUtilityService {

	public boolean isValidExpression(String expression, List variableList) {
		// TODO Auto-generated method stub
		return ServiceTimeUtil.isValidExpression(expression, variableList);
	}
	
	
}
