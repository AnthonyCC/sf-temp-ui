package com.freshdirect.transadmin.datamanager.util;

import com.freshdirect.framework.util.EnumLogicalOperator;



public class EnumLogicalOperatorUserType extends GenericEnumUserType {

	public Object getEnum(String value) {
		// TODO Auto-generated method stub
		return EnumLogicalOperator.getEnum(value);
	}

}