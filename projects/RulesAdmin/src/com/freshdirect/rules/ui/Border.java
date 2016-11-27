package com.freshdirect.rules.ui;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.tapestry.BaseComponent;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.IPropertySelectionModel;

import com.freshdirect.rules.Rule;
import com.freshdirect.rules.RulesRegistry;

public abstract class Border extends BaseComponent {
	
	public IPropertySelectionModel getSubsystemSelectionModel() {
		Map m = new HashMap();
		for (Iterator i = RulesRegistry.getSubsystems().iterator(); i.hasNext();) {
			String s = (String) i.next();
			m.put(s, s);
		}
		return new LabelPropertySelectionModel(m, true);
	}
	
	public void createRule(IRequestCycle cycle) {
		Rule r = new Rule();
		RulesAdminVisit visit = (RulesAdminVisit) cycle.getPage().getVisit();
		r.setSubsystem(visit.getSubsystem());
		
		cycle.getPage().setProperty("selectedRule", r);
	}
}
