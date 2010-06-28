package com.freshdirect.transadmin.datamanager.util;

import com.freshdirect.transadmin.util.EnumLogicalOperator;

public class EnumLogicalOperatorDataType extends GenericEnumUserType {

	public Object getEnum(String value) {
		// TODO Auto-generated method stub
		return EnumLogicalOperator.getEnum(value);
	}

}
