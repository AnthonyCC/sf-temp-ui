package com.freshdirect.common.customer;

import java.util.Set;


public class ServiceTypeUtil {

    public static EnumServiceType getPreferedServiceType(Set<EnumServiceType> availableServices){
    	if (availableServices.contains(EnumServiceType.HOME)){
    		return EnumServiceType.HOME;
    	} else if (availableServices.contains(EnumServiceType.CORPORATE)){
    		return EnumServiceType.CORPORATE;
    	} else {
    		return EnumServiceType.PICKUP;
    	}
    }
}
