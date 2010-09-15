package com.freshdirect.smartstore.scoring;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.framework.util.BalkingExpiringReference;
import com.freshdirect.framework.util.LruCache;
import com.freshdirect.smartstore.SessionInput;

public class CachingDataGenerator extends DataGenerator {

    private static final int HOUR_IN_MILLIS = 60 * 60 * 1000;

    protected static LruCache<String, BalkingExpiringReference> cache          = new LruCache<String, BalkingExpiringReference>(FDStoreProperties.getSmartStoreDataSourceCacheSize());

    private static Executor        threadPool     = new ThreadPoolExecutor(1, 1, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(),
                                                          new ThreadPoolExecutor.DiscardPolicy());

    boolean cacheEnabled;

    
    public CachingDataGenerator() {
        super();
        cacheEnabled = FDStoreProperties.isSmartstoreDataSourcesCached();
    }

    public String getKey(SessionInput input) {
        return null;
    }

    public final List<ContentNodeModel> generate(SessionInput sessionInput, final DataAccess input) {
        if (cacheEnabled) {
            String key = getKey(sessionInput);
            final SessionInput inp = new SessionInput(sessionInput.getCustomerId(), sessionInput.getCustomerServiceType(), sessionInput.getPricingContext());
            inp.setCurrentNode(sessionInput.getCurrentNode());
            inp.setExplicitList(sessionInput.getExplicitList());
            if (cache.get(key) == null) {
                cache.put(key, new BalkingExpiringReference<List<ContentNodeModel>>(HOUR_IN_MILLIS, threadPool, generateImpl(inp, input)) {
                    protected List<ContentNodeModel> load() {
                        List<ContentNodeModel> result = generateImpl(inp, input);
                        return result;
                    }
                });
            }
            List<ContentNodeModel> cached = (List<ContentNodeModel>) cache.get(key).get();
            if (cached != null) {
                return cached;
            }
            return Collections.<ContentNodeModel>emptyList();
        } else {
            return generateImpl(sessionInput, input);
        }
    }

    public List<ContentNodeModel> generateImpl(SessionInput sessionInput, DataAccess input) {
        return Collections.<ContentNodeModel>emptyList();
    }

    public static List<ContentNodeModel> peekIntoCache(String key) {
        if (cache.get(key) == null) {
            return Collections.<ContentNodeModel>emptyList();
        }

        List<ContentNodeModel> cached = (List<ContentNodeModel>) cache.get(key).get();
        if (cached != null) {
            return cached;
        }
        return Collections.<ContentNodeModel>emptyList();
    }
    
    public boolean isCacheEnabled() {
        return cacheEnabled;
    }
    
    public void setCacheEnabled(boolean cacheEnabled) {
        this.cacheEnabled = cacheEnabled;
    }

}
