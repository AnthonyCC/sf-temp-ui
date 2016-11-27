package com.freshdirect.transadmin.constants;

import com.freshdirect.routing.constants.EnumWaveInstanceStatus;
import com.freshdirect.transadmin.datamanager.util.GenericEnumUserType;

public class EnumWaveInstanceStatusDataType extends GenericEnumUserType {

    public Object getEnum(String value) {
          // TODO Auto-generated method stub
          return EnumWaveInstanceStatus.getEnum(value);
    }

}

