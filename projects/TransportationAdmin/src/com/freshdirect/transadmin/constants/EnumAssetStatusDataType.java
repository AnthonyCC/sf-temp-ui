package com.freshdirect.transadmin.constants;

import com.freshdirect.transadmin.datamanager.util.GenericEnumUserType;

public class EnumAssetStatusDataType extends GenericEnumUserType {

    public Object getEnum(String value) {
          // TODO Auto-generated method stub
          return EnumAssetStatus.getEnum(value);
    }

}

