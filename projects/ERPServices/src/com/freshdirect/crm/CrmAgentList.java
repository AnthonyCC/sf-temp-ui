package com.freshdirect.crm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CrmAgentList implements Serializable {
	
	private static final long serialVersionUID = -2444582023399331852L;
	
	private final List<CrmAgentModel> agents;
	
	public CrmAgentList(List<CrmAgentModel> agents){
		this.agents = agents;
	}
	
	public List<CrmAgentModel> getAgents(CrmAgentRole role) {
		List<CrmAgentModel> lst = new ArrayList<CrmAgentModel>();
		for( CrmAgentModel agent : this.agents ) {
			if(agent.getRole().equals(role)){
				lst.add(agent);
			}
		}
		return lst;
	}
}
