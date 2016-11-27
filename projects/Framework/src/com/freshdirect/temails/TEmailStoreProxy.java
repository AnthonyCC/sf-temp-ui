package com.freshdirect.temails;

import java.util.Map;

/**
 * @author 
 */
public class TEmailStoreProxy implements TEmailStoreI {

	private final TEmailStoreI store;

	public TEmailStoreProxy(TEmailStoreI store) {
		this.store = store;
	}

	public String getSubsystem() {
		return this.store.getSubsystem();
	}

	public Map getTemplates() {
		return store.getTemplates();
	}

	public Template getTemplate(String templateId) {
		return store.getTemplate(templateId);
	}

	public void storeTemplate(Template template) {
		this.store.storeTemplate(template);
	}

	public void deleteTemplate(String templateId) {
		this.store.deleteTemplate(templateId);
	}
}
