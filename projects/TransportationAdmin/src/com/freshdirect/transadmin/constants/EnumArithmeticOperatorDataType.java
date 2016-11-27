package com.freshdirect.transadmin.constants;

import com.freshdirect.routing.constants.EnumArithmeticOperator;
import com.freshdirect.transadmin.datamanager.util.GenericEnumUserType;

public class EnumArithmeticOperatorDataType extends GenericEnumUserType {

    public Object getEnum(String value) {
          // TODO Auto-generated method stub
          return EnumArithmeticOperator.getEnum(value);
    }

}

