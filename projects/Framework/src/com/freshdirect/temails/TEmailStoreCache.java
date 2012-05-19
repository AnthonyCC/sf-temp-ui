package com.freshdirect.temails;

import java.util.Map;

/**
 * @author 
 */
public class TEmailStoreCache extends TEmailStoreProxy {

	private Map templates;
	
	private long REFRESH_PERIOD = 0;
	private long lastRefresh = 0;

	public TEmailStoreCache(TEmailStoreI store) {
		super(store);
	}
	
	public TEmailStoreCache(TEmailStoreI store, int refresh) {
		super(store);
		this.REFRESH_PERIOD = 1000 * 60 * refresh;
	}

	public Map getTemplates() {
		this.refreshTemplates();
		return templates;
	}

	public Template getTemplate(String templateId) {
		return (Template) getTemplates().get(templateId);
	}

	public void storeTemplate(Template template) {
		super.storeTemplate(template);
		this.templates = null;
	}

	public void deleteTemplate(String templateId) {
		super.deleteTemplate(templateId);
		this.templates.remove(templateId);
	}
	
	private void refreshTemplates () {
		if (templates == null || System.currentTimeMillis() - lastRefresh > REFRESH_PERIOD) {
			this.templates = super.getTemplates();
			
			lastRefresh = System.currentTimeMillis();
		}
	}

}
