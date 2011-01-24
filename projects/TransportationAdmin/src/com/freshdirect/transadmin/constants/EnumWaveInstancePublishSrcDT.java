package com.freshdirect.transadmin.constants;

import com.freshdirect.routing.constants.EnumWaveInstancePublishSrc;
import com.freshdirect.transadmin.datamanager.util.GenericEnumUserType;

public class EnumWaveInstancePublishSrcDT extends GenericEnumUserType {

    public Object getEnum(String value) {
          // TODO Auto-generated method stub
          return EnumWaveInstancePublishSrc.getEnum(value);
    }

}

