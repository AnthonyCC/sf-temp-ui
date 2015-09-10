package com.freshdirect.mobileapi.model.data;

import java.util.ArrayList;
import java.util.List;


public class HelpTopics {
	
	List helpTopics =new ArrayList();

	public List getHelpTopics() {
		return helpTopics;
	}

	public void setHelpTopics(Object helpTopics) {
		this.helpTopics.add(helpTopics);
	}

}
