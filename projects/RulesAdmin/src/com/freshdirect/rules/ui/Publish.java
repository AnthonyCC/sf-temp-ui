package com.freshdirect.rules.ui;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.IPropertySelectionModel;

import com.freshdirect.storeapi.StoreServiceLocator;
import com.freshdirect.storeapi.rules.RulesEngineI;
import com.freshdirect.storeapi.rules.RulesPublisher;

/**
 * @author knadeem Date Apr 13, 2005
 */
public abstract class Publish extends AppPage {

	public IPropertySelectionModel getSubSystemSelectionModel() {
		Map<String, String> m = new HashMap<String, String>();
		for (Iterator<String> i = StoreServiceLocator.rulesRegistry().getSubsystems().iterator(); i.hasNext();) {
			String s = i.next();
			m.put(s, s);
		}
		return new LabelPropertySelectionModel(m, true);
	}

	public IPropertySelectionModel getPublishEnvModel(){
		// FIXME make environment list configurable
		Map<String, String> m = new LinkedHashMap<String, String>();
		m.put("Integration", "int");
		m.put("Stage", "stage");
		m.put("PRD Cluster A", "clustera");
		m.put("PRD Cluster B", "clusterb");

		return new LabelPropertySelectionModel(m, true);
	}

	public void publish(IRequestCycle cycle) {

		RulesEngineI engine = StoreServiceLocator.rulesRegistry().getRulesEngine(this.getSubsystem());
        RulesPublisher publisher = StoreServiceLocator.rulesPublisher();

		publisher.publish(engine.getRules(), this.getSubsystem(), this.getPublishEnv());

	}

	public abstract String getSubsystem();
	public abstract void setSubsystem(String selectedSubsystem);

	public abstract String getPublishEnv();
	public abstract void setPublishEnv(String publishEnv);
}
