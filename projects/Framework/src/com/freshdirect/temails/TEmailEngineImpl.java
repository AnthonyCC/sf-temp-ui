package com.freshdirect.temails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class TEmailEngineImpl extends TEmailStoreProxy implements TEmailEngineI {

	public TEmailEngineImpl(TEmailStoreI store) {
		super(store);
	}

	public Object formatTemplates(Object target,String templateId) {
		
		String formattedStr="";
		Map templates = getTemplates();
		
		System.out.println("templates :"+templates);
		
		for (Iterator i = templates.values().iterator(); i.hasNext();) {
			Template r = (Template) i.next();
			if(r.getEid().equalsIgnoreCase(templateId)){
				TEmailRuntime rt = new TEmailRuntime(templates);
				formattedStr=rt.formatTemplate(target, r);
			}			
		}
		return formattedStr; 
		
	}

}
