/**
 * @author ekracoff
 * Created on Jan 18, 2005*/

package com.freshdirect.cms.slide;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class RevisionLine {
	private Map properties = new HashMap();
	
	public List getPropertyNames(){
		List names = new ArrayList();
		for(Iterator i = properties.keySet().iterator(); i.hasNext();){
			String name = (String) i.next();
			names.add(name);
		}
		return names;
	}
	
	public Map getProperties(){
		return this.properties;
	}
	
	public String getValue(String name){
		return (String)properties.get(name);
	}
	
	public void addProperty(String name, String value){
		properties.put(name, value);
	}
}


