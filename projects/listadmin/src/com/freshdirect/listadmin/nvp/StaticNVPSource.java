package com.freshdirect.listadmin.nvp;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

public class StaticNVPSource implements NVPSourceI {
	private List NVPs = new ArrayList();
	
	public StaticNVPSource(List names, List values) {
		if(names.size() != values.size()) {
			throw new IllegalArgumentException("name and value lists must be the same size");
		}
		
		for(int i=0;i<names.size();i++) {
			NVPs.add(new NVP(names.get(i).toString(),values.get(i).toString()));
		}
	}
	

	/*
	 * Assumes the string is of the form opt1|opt2|opt3...
	 * where each opt is either name=value or name.  In  the latter case
	 * the name is used as both name and value.
	 */
	public StaticNVPSource(String namesAndValues) {
		StringTokenizer st = new StringTokenizer(namesAndValues,"|");
		
		while(st.hasMoreTokens()) {
			String token = st.nextToken();
			int pos      = token.indexOf("=");
			
			if(pos == -1) {
				NVPs.add(new NVP(token,token));
			} else {
				NVPs.add(new NVP(token.substring(0,pos), token.substring(pos+1)));
			}
		}
	}
	
	public Iterator iterator() {
		return NVPs.iterator();
	}
}
