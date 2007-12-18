/*
 * ErpOrderLineUtil.java
 *
 * Created on December 27, 2002, 7:10 PM
 */

package com.freshdirect.customer.ejb;

import java.util.*;

/**
 *
 * @author  mrose
 * @version 
 */
public class ErpOrderLineUtil {

    /** Creates new ErpOrderLineUtil */
    public ErpOrderLineUtil() {
        super();
    }
    
    public static String convertHashMapToString(Map map){
		StringBuffer ret = new StringBuffer();
		for(Iterator i = map.keySet().iterator(); i.hasNext(); ){
			String key = (String)i.next();
			ret.append(key);
			ret.append("=");
			ret.append((String)map.get(key));
			if(i.hasNext()){
				ret.append(",");
			}
		}
		return ret.toString();
	}
	
	public static HashMap convertStringToHashMap(String configuration){
		
		HashMap ret = new HashMap();
		if (configuration != null) {
			StringTokenizer st = new StringTokenizer(configuration, ",");
			while(st.hasMoreTokens()){
				String token = st.nextToken().trim();
				int idx = token.indexOf("=");
				String key = token.substring(0, idx++);
				String value = token.substring(idx, token.length());
				ret.put(key, value);
			}
		}
		return ret;
   
	}

}
