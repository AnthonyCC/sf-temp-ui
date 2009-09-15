package com.freshdirect.smartstore.scoring;

import java.util.Collections;
import java.util.List;

import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.framework.util.BalkingExpiringReference;
import com.freshdirect.framework.util.TimedLruCache;
import com.freshdirect.smartstore.SessionInput;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class CachingDataGenerator extends DataGenerator {

    private static final int       HOUR_IN_MILLIS = 60 * 60 * 1000;

    protected static TimedLruCache cache          = new TimedLruCache(FDStoreProperties.getSmartStoreDataSourceCacheSize(), HOUR_IN_MILLIS);

    private static Executor        threadPool     = new ThreadPoolExecutor(1, 1, 60, TimeUnit.SECONDS, new LinkedBlockingQueue(),
                                                          new ThreadPoolExecutor.DiscardPolicy());

    boolean                        cacheEnabled;

    public CachingDataGenerator() {
        super();
        cacheEnabled = FDStoreProperties.isSmartstoreDataSourcesCached();
    }

    public String getKey(SessionInput input) {
        return null;
    }

    public final List generate(SessionInput sessionInput, final DataAccess input) {
        if (cacheEnabled) {
            String key = getKey(sessionInput);
            final SessionInput inp = new SessionInput(sessionInput.getCustomerId(), sessionInput.getCustomerServiceType());
            inp.setCurrentNode(sessionInput.getCurrentNode());
            inp.setExplicitList(sessionInput.getExplicitList());
            if (cache.get(key) == null) {
                cache.put(key, new BalkingExpiringReference(HOUR_IN_MILLIS, threadPool, generateImpl(inp, input)) {
                    protected Object load() {
                        List result = generateImpl(inp, input);
                        return result;
                    }

                });
            }
            List cached = (List) ((BalkingExpiringReference) cache.get(key)).get();
            if (cached != null) {
                return cached;
            }
            return Collections.EMPTY_LIST;
        } else {
            return generateImpl(sessionInput, input);
        }
    }

    public List generateImpl(SessionInput sessionInput, DataAccess input) {
        return Collections.EMPTY_LIST;
    }

    public static List peekIntoCache(String key) {
        if (cache.get(key) == null) {
            return Collections.EMPTY_LIST;
        }

        List cached = (List) ((BalkingExpiringReference) cache.get(key)).get();
        if (cached != null) {
            return cached;
        }
        return Collections.EMPTY_LIST;
    }

}
