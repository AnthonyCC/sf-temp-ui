package com.freshdirect.fdstore;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.ejb.EJBException;

import org.apache.log4j.Logger;

import com.freshdirect.common.ERPServiceLocator;
import com.freshdirect.fdstore.cache.ExternalSharedCache;
import com.freshdirect.fdstore.ejb.FDFactorySB;
import com.freshdirect.framework.cache.MemcacheConfiguration;
import com.freshdirect.framework.util.DateUtil;
import com.freshdirect.framework.util.ProgressReporter;
import com.freshdirect.framework.util.log.LoggerFactory;

public class FDProductCache extends ExternalSharedCache<FDSku, Integer, FDProduct> {
	private final static Logger LOGGER = LoggerFactory.getInstance(FDProductCache.class);

	private static FDProductCache instance;
        private Map<String, Integer> lastVersions = new ConcurrentHashMap<String, Integer>();


        public synchronized static FDProductCache getInstance() {
            if (instance == null) {
                FDCachedFactory.setupMemcached();
                instance = new FDProductCache(FDStoreProperties.getMemcachedProductTTL());
                instance.startRefresher();
            }
            return instance;
        }

        public synchronized static void mockInstance() {
            if (instance == null) {
                instance = new FDProductCache(true);
            }
        }

        private static FDProduct getProductInternal(FDSku sku) throws FDSkuNotFoundException, FDResourceException {
            try {
                FDFactorySB sb = ERPServiceLocator.getInstance().getFDFactorySessionBean();
                return sb.getProduct(sku.getSkuCode(), sku.getVersion());
            } catch (EJBException re) {
                throw new FDResourceException (re, "Error talking to session bean");
            } catch (RemoteException re) {
                throw new FDResourceException (re, "Error talking to session bean");
            }
        }
        

    public FDProductCache(int ttl) {
        super(5 * DateUtil.MINUTE, "p/", ttl);
    }
    
    public FDProductCache(boolean mock) {
        super(mock);
    }

    public FDProduct getBySkuCode(String skuCode) throws FDSkuNotFoundException, FDResourceException {
        Integer vs = lastVersions.get(skuCode);
        if (vs != null) {
            return getFDProduct(new FDSku(skuCode, vs));
        }
        throw new FDSkuNotFoundException("SKU not found in lastVersion cache:" + skuCode);
    }
    
    public FDProduct getFDProduct(FDSku key) throws FDSkuNotFoundException, FDResourceException {
        FDProduct p = get(key);
        if (p == null) {
            p = getProductInternal(key);
            if (p == null) {
                throw new FDSkuNotFoundException("SKU not found in the database:" + key);
            } else {
                put(key, p);
            }
        }
        return p;
    }
    
    
        @Override
        protected Map<FDSku, FDProduct> loadData(Integer lastVersion) {
            try {
                LOGGER.info("loading FDProduct since :" + lastVersion);
                List<FDSku> changedSkus = FDFactory.getLatestOrChangedSkus(lastVersion);
                final int size = changedSkus.size();
                LOGGER.info("skus changed :" + size + " since " + (lastVersion != null ? lastVersion : "epoch"));
                Map<FDSku, FDProduct> data = new HashMap<FDSku, FDProduct>(size * 3 / 2);
                if (size > 0) {
                    int offset = (int) (Math.random() * size);
                    ProgressReporter p = new ProgressReporter();
                    p.setShowAtEvery(1000);
                    p.setElapsedMillis(10000);
                    
                    for (int i = 0; i < size; i++) {
                        FDSku sku = changedSkus.get((offset + i) % size);
                        if (!lastVersions.containsKey(sku.getSkuCode()) || lastVersions.get(sku.getSkuCode()) < sku.getVersion()) {
                            lastVersions.put(sku.getSkuCode(), sku.getVersion());
                        }
                        // check the external cache
                        FDProduct externalItem = getFromExternalCache(sku);
                        try {
                            if (externalItem == null) {
                                externalItem = getProductInternal(sku);
                                putToExternalCache(sku, externalItem);
                            }
                            data.put(sku, externalItem);
                        } catch (FDSkuNotFoundException e) {
                            LOGGER.error("changed sku which is not found (contradictory and critical):"+sku, e);
                        } catch (FDResourceException e) {
                            LOGGER.error("Error loading sku:"+sku, e);
                        }
                        if (p.shouldLogMessage(i)) {
                            LOGGER.info("already loaded " + i + " from " + size);
                        }
                    }
                }
                LOGGER.info("FDProduct loaded [" + data.size() + ']');
                return data;
            } catch (FDResourceException e) {
                throw new FDRuntimeException(e);
            }
        }
        
        @Override
        protected String getStringKey(FDSku key) {
            return key.getSkuCode() + ':' + key.getVersion();
        }
        
        
}
