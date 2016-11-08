package com.freshdirect.mobileapi.model.data;

import java.util.ArrayList;
import java.util.List;


public class HelpTopics {
	
	private List<HelpTopic> helpTopics = new ArrayList<HelpTopic>();

	public List<HelpTopic> getHelpTopics() {
		return helpTopics;
	}

	public void addHelpTopics(HelpTopic helpTopic) {
		helpTopics.add(helpTopic);
	}

}
