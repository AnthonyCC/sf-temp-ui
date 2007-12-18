package com.freshdirect.listadmin.nvp;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.enum.Enum;
import org.apache.commons.lang.enum.EnumUtils;

public class EnumNVPSource implements NVPSourceI {
	private List enums = new ArrayList();
	
	public EnumNVPSource(String EnumClassName) throws ClassNotFoundException {
		Class c = Class.forName(EnumClassName);
		
		for(Iterator it=EnumUtils.iterator(c);it.hasNext();) {
			Enum e = (Enum) it.next();
			
			enums.add(new NVP(e.getName(),e.getName()));
		}
	}

	public Iterator iterator() {
		return enums.iterator();
	}
}
