package com.freshdirect.crm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CrmAgentList implements Serializable {
	
	private final List agents;
	
	public CrmAgentList(List agents){
		this.agents = agents;
	}
	
	public List getAgents(CrmAgentRole role) {
		List lst = new ArrayList();
		for(Iterator i = this.agents.iterator(); i.hasNext(); ){
			CrmAgentModel agent = (CrmAgentModel)i.next();
			if(agent.getRole().equals(role)){
				lst.add(agent);
			}
		}
		return lst;
	}
}
