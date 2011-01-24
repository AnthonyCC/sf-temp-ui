package com.freshdirect.crm.ejb;


import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.core.EntityDAOI;

import com.freshdirect.crm.*;

public class CrmCaseEntityBean extends CrmEntityBeanSupport {

	protected String getResourceCacheKey() {
		return "com.freshdirect.crm.ejb.CrmCaseHome";
	}

	protected EntityDAOI getDAO() {
		return new CrmCaseDAO();
	}

	protected String getSchema() {
		return "CUST";
	}
    
    public void addCaseAction(CrmCaseAction caseAction) {
        ((CrmCaseModel) this.model).addAction(caseAction);
        setModified();
    }
    
    public void updateCaseInfo(CrmCaseInfo caseInfo) {
        ((CrmCaseModel) this.model).setCaseInfo(caseInfo);
        setModified();
    }
    
    public void lock(PrimaryKey agentPK) {
        ((CrmCaseModel) this.model).setLockedAgentPK(agentPK);
        setModified();
    }
    
    public void lock(String agentUserId) {
        ((CrmCaseModel) this.model).setLockedAgentUserId(agentUserId);
        setModified();
    }
    
    public void unlock() {
        ((CrmCaseModel) this.model).setLockedAgentPK(null);
        setModified();
    }
    
    public void setState(CrmCaseState state) {
        ((CrmCaseModel) this.model).setState(state);
        setModified();
    }

}
