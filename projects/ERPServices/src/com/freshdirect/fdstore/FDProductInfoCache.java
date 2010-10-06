package com.freshdirect.fdstore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.freshdirect.erp.EnumATPRule;
import com.freshdirect.fdstore.cache.ExternalSharedCache;
import com.freshdirect.framework.cache.ActiveCacheModul;
import com.freshdirect.framework.cache.CacheI;
import com.freshdirect.framework.cache.ObjectAccessor;
import com.freshdirect.framework.cache.SimpleCache;
import com.freshdirect.framework.util.DateUtil;
import com.freshdirect.framework.util.ProgressReporter;
import com.freshdirect.framework.util.log.LoggerFactory;

/**
 * Product Info cache
 * @author zsombor
 *
 */
public class FDProductInfoCache extends ExternalSharedCache<String, Integer, FDProductInfo> {
    
    private final static Logger LOGGER = LoggerFactory.getInstance(FDProductInfoCache.class);

    private static CacheI<String, FDProductInfo> instance;
    
    /**
     * This class is used for testing.
     * @author zsombor
     *
     */
    static class FDProductInfoBuilder implements ObjectAccessor<String, FDProductInfo> {
        @Override
        public FDProductInfo get(String sku) {
            try {
                FDProductInfo p = FDFactory.getProductInfo(sku);
                if (p != null && FDStoreProperties.getPreviewMode()) {
                    return getPreviewProductInfo(p);
                }
                return p;                
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
        super("i/", ttl);
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

    @Override
    public FDProductInfo get(String key) {
        FDProductInfo p = super.get(key);
        if (p != null && FDStoreProperties.getPreviewMode()) {
            return getPreviewProductInfo(p);
        }
        return p;
    }
    
    /**
     * Utility method: nothing is ever discontinued, out of season, or indefinitely unavailable in preview mode
     */
    private static FDProductInfo getPreviewProductInfo(FDProductInfo pinfo) {
            return new FDProductInfo(
                    pinfo.getSkuCode(),
                    pinfo.getVersion(),
                    null,
                    EnumATPRule.JIT,
                    EnumAvailabilityStatus.AVAILABLE,
                    new java.util.GregorianCalendar(3000, java.util.Calendar.JANUARY, 1).getTime(),
                    null,pinfo.getRating(),pinfo.getFreshness(), pinfo.getDefaultPriceUnit(), pinfo.getZonePriceInfoList());
    }
    
    @Override
    public long getRefreshDelay() {
        return FDStoreProperties.getProductInfoRefreshPeriond() * DateUtil.MINUTE;
    }
    
    @Override
    protected Logger getLog() {
        return LOGGER;
    }
    

}
