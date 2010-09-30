package com.freshdirect.fdstore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.freshdirect.fdstore.cache.ExternalSharedCache;
import com.freshdirect.framework.cache.ActiveCacheModul;
import com.freshdirect.framework.cache.CacheI;
import com.freshdirect.framework.cache.ObjectAccessor;
import com.freshdirect.framework.cache.SimpleCache;
import com.freshdirect.framework.util.DateUtil;
import com.freshdirect.framework.util.ProgressReporter;
import com.freshdirect.framework.util.log.LoggerFactory;

public class FDProductInfoCache extends ExternalSharedCache<String, Integer, FDProductInfo> {
    
    private final static Logger LOGGER = LoggerFactory.getInstance(FDProductInfoCache.class);

    private static CacheI<String, FDProductInfo> instance;
    
    static class FDProductInfoBuilder implements ObjectAccessor<String, FDProductInfo> {
        @Override
        public FDProductInfo get(String sku) {
            try {
                return FDFactory.getProductInfo(sku);
            } catch (FDResourceException e) {
                throw new FDRuntimeException(e);
            } catch (FDSkuNotFoundException e) {
                return FDFactory.SKU_NOT_FOUND;
            }
        }
        
    }
    

    public synchronized static CacheI<String, FDProductInfo> getInstance() {
        if (instance == null) {
            FDCachedFactory.setupMemcached();
            FDProductInfoCache i = new FDProductInfoCache(FDStoreProperties.getMemcachedProductInfoTTL());
            i.startRefresher();
            instance = i;
        }

        return instance;
    }

    public synchronized static void mockInstance() {
        if (instance == null) {
            instance = new ActiveCacheModul<String, FDProductInfo>(new FDProductInfoBuilder(), new SimpleCache<String, FDProductInfo>());
        }
    }


    public FDProductInfoCache(int ttl) {
        super(5 * DateUtil.MINUTE, "i/", ttl);
    }

    public FDProductInfoCache(boolean mock) {
        super(mock);
    }

    @Override
    protected Map<String, FDProductInfo> loadData(Integer lastVersion) {
        try {
            List<FDSku> changedSkus = FDFactory.getLatestOrChangedSkus(lastVersion);
            final int size = changedSkus.size();
            LOGGER.info("skus changed :" + changedSkus.size() + " since " + (lastVersion != null ? lastVersion : "epoch"));
            Map<String, FDProductInfo> data = new HashMap<String, FDProductInfo>(changedSkus.size() * 3 / 2);
            if (size > 0) {
                int offset = (int) (Math.random() * size);
                ProgressReporter p = new ProgressReporter();
                p.setShowAtEvery(1000);
                p.setElapsedMillis(10000);
                
                for (int i = 0; i < size; i++) {
                    FDSku sku = changedSkus.get((offset + i) % size);
                
                    try {
						FDProductInfo externalItem = this.getFromExternalCache(sku.getSkuCode());
						if (externalItem == null || externalItem.getVersion() < sku.getVersion()) {
						    externalItem = FDFactory.getProductInfo(sku.getSkuCode());
						    if (externalItem != null) {
						        this.putToExternalCache(sku.getSkuCode(), externalItem);
						    }
						}
						data.put(sku.getSkuCode(), externalItem);
						if (p.shouldLogMessage(i)) {
						    LOGGER.info("loaded so far " + i + " of " + size);
						}
					} catch (FDSkuNotFoundException e) {
			            LOGGER.error("changed sku which is not found (contradictory and critical)", e);
					}
                }
            }
            LOGGER.info("FDProductInfo loaded [" + data.size() + ']');
            return data;
        } catch (FDResourceException e) {
            throw new FDRuntimeException(e);
        }
    }

}
