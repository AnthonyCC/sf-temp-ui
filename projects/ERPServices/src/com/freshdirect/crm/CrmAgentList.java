package com.freshdirect.crm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
		Collections.sort(lst, new Comparator<CrmAgentModel>(){
			public int compare(CrmAgentModel str1, CrmAgentModel str2) {
				if(null != str1.getUserId() && null !=str1.getUserId()){
					return str1.getUserId().toLowerCase().compareTo(str2.getUserId().toLowerCase());
				}else{
					return 0;
				}
			}
		});
		return lst;
	}
}
