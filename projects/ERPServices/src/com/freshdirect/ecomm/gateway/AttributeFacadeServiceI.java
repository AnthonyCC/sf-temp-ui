package com.freshdirect.ecomm.gateway;

import java.util.Date;
import java.util.Map;

import com.freshdirect.content.attributes.AttributeException;

public interface AttributeFacadeServiceI {

	public Map loadAttributes(Date since) throws AttributeException;

}
