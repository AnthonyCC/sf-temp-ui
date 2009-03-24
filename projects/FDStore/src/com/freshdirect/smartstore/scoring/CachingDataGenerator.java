package com.freshdirect.smartstore.scoring;

import java.util.Collections;
import java.util.List;

import com.freshdirect.framework.util.TimedLruCache;
import com.freshdirect.smartstore.SessionInput;

public class CachingDataGenerator extends DataGenerator {

    protected static TimedLruCache cache = new TimedLruCache(1000,60*60*1000);
    
    
    public String getKey(SessionInput input) {
        return null;
    }
    
    
    public final List generate(SessionInput sessionInput, DataAccess input) {
        String key = getKey(sessionInput);
        if (key != null) {
            List result = (List) cache.get(key);
            if (result != null) {
                return result;
            }
        }
        List result = generateImpl(sessionInput, input);
        if (key != null) {
            cache.put(key, result);
        }
        return result;
    }
    
    
    public List generateImpl(SessionInput sessionInput, DataAccess input) {
        return Collections.EMPTY_LIST;
    }


    public static List peekIntoCache(String key) {
        return (List) cache.get(key);
    }
    
}
