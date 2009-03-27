package com.freshdirect.smartstore.scoring;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.framework.util.BalkingExpiringReference;
import com.freshdirect.smartstore.SessionInput;

import edu.emory.mathcs.backport.java.util.concurrent.Executor;
import edu.emory.mathcs.backport.java.util.concurrent.LinkedBlockingQueue;
import edu.emory.mathcs.backport.java.util.concurrent.ThreadPoolExecutor;
import edu.emory.mathcs.backport.java.util.concurrent.TimeUnit;

public class CachingDataGenerator extends DataGenerator {

    private static final int HOUR_IN_MILLIS = 60*60*1000;
    
    private static final int MAX_CACHE_SIZE = 50;

	protected static Map cache = new HashMap();
    
	private static Executor threadPool = new ThreadPoolExecutor(1, 1, 60,
			TimeUnit.SECONDS, new LinkedBlockingQueue(), new ThreadPoolExecutor.DiscardPolicy());
    
    public String getKey(SessionInput input) {
        return null;
    }
    
    
    public final List generate(SessionInput sessionInput, final DataAccess input) {
    	if (FDStoreProperties.isSmartstoreDataSourcesCached()) {
	        String key = getKey(sessionInput);
	        if (cache.get(key) == null) {
	        	if (cache.size() < MAX_CACHE_SIZE) {
			        final SessionInput inp = new SessionInput(sessionInput.getCustomerId());
			        inp.setCurrentNode(sessionInput.getCurrentNode());
			        inp.setExplicitList(sessionInput.getExplicitList());
			        
		            cache.put(key, new BalkingExpiringReference(HOUR_IN_MILLIS, threadPool, generateImpl(inp, input)) {
						protected Object load() {
					        List result = generateImpl(inp, input);
							return result;
						}
		            	
		            });
	        	} else
	        		return generateImpl(sessionInput, input);
	        }
	        List cached = (List) ((BalkingExpiringReference) cache.get(key)).get();
	        if (cached != null)
	        	return cached;
	        return Collections.EMPTY_LIST;
    	} else
    		return generateImpl(sessionInput, input);
    }
    
    
    public List generateImpl(SessionInput sessionInput, DataAccess input) {
        return Collections.EMPTY_LIST;
    }


    public static List peekIntoCache(String key) {
    	if (cache.get(key) == null)
    		return Collections.EMPTY_LIST;

    	List cached = (List) ((BalkingExpiringReference) cache.get(key)).get();
        if (cached != null)
        	return cached;
        return Collections.EMPTY_LIST;
    }
    
}
