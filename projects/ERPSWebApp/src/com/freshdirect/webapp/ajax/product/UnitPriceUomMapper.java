package com.freshdirect.webapp.ajax.product;

import java.util.HashMap;
import java.util.Map;

public class UnitPriceUomMapper {

    private static final Map<String, String> uomMappings = new HashMap<String, String>();

    static {
        uomMappings.put("SS1", "Per Serving");
    }
    
    /**
     * Get the mapping for the UnitPriceUOM
     * @param key
     * @return the mapped string for the UnitPriceUOM string if any, otherwise returns the key
     */
    public static String getMapping(String key){
        String mapping = key;
        if(key != null && uomMappings.containsKey(key)){
            mapping = uomMappings.get(key);
        }
        return mapping;
    }
}
