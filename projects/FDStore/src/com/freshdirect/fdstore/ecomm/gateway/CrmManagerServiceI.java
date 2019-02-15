package com.freshdirect.fdstore.ecomm.gateway;

import com.freshdirect.crm.CrmAgentModel;
import com.freshdirect.crm.CrmAuthenticationException;
import com.freshdirect.fdstore.FDResourceException;

public interface CrmManagerServiceI {

	public CrmAgentModel loginAgent(String username, String password) throws CrmAuthenticationException, FDResourceException;
}
