package com.freshdirect.dataloader.autoorder.create.util;

import com.freshdirect.crm.CrmAgentModel;
import com.freshdirect.crm.CrmAgentRole;

public interface IConstants {
	
	static final String CREATE_CUSTOMER = "CREATECUSTOMER";
    static final String CREATE_ORDERS = "CREATEORDERS";
    
    static CrmAgentModel AGENT = new CrmAgentModel("admin", "admin", "Admin", "User", true, CrmAgentRole.getEnum("ADM"));

}
