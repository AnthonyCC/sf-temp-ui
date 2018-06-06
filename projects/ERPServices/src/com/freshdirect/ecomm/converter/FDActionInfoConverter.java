package com.freshdirect.ecomm.converter;

import java.util.ArrayList;
import java.util.List;

import com.freshdirect.crm.CrmCaseQueue;
import com.freshdirect.ecommerce.data.ecoupon.CrmAgentModelData;
import com.freshdirect.ecommerce.data.list.FDActionInfoData;
import com.freshdirect.fdstore.customer.FDActionInfo;

public class FDActionInfoConverter {

	
	public static FDActionInfoData buildActionInfoData(FDActionInfo info) {
		FDActionInfoData actionInfoData = new FDActionInfoData();
		if(info.getType() != null)
		actionInfoData.setAccountActivityType(info.getType().getName());
		CrmAgentModelData agentModelData = new CrmAgentModelData();
		if(info.getAgent() != null){
			agentModelData.setActive(info.getAgent().isActive());
			List<CrmCaseQueue> casequeue = info.getAgent().getAgentQueues();
			List<String> casequeues= new ArrayList<String>();
			for (CrmCaseQueue crmCaseQueue : casequeue) {
				casequeues.add(crmCaseQueue.getName());
			}
			agentModelData.setAgentCaseQueues(casequeues);
			agentModelData.setCreateDate(info.getAgent().getCreateDate());
			agentModelData.setCrmAgentId(info.getAgent().getId());
			agentModelData.setCurFacilityContext(info.getAgent().getCurFacilityContext());
			agentModelData.setFirstName(info.getAgent().getFirstName());
			agentModelData.setLastName(info.getAgent().getLastName());
			agentModelData.setLdapId(info.getAgent().getLdapId());
			agentModelData.setMasqueradeAllowed(info.getAgent().isMasqueradeAllowed());
			agentModelData.setPassword(info.getAgent().getPassword());
			if(info.getAgent().getRole()!= null)
			agentModelData.setRoleCode(info.getAgent().getRole().getCode());
			}
		actionInfoData.setAgent(agentModelData);
		actionInfoData.setErpCustomerId(info.getIdentity().getErpCustomerPK());
		if(info.geteStore() != null)
		actionInfoData.seteStore(info.geteStore().getContentId());
		actionInfoData.setFdCustomerId(info.getIdentity().getFDCustomerPK());
		actionInfoData.setFdUserId(info.getFdUserId());
		actionInfoData.setInitiator(info.getInitiator());
		actionInfoData.setMasqueradeAgent(info.getMasqueradeAgentTL());
		actionInfoData.setNote(info.getNote());
		actionInfoData.setPR1(info.isPR1());
		if(info.getSource() != null)
		actionInfoData.setSource(info.getSource().getCode());
		if(info.getTaxationType() != null)
		actionInfoData.setTaxationType(info.getTaxationType().getName());
		return actionInfoData;
	}

}
