package com.freshdirect.cms.cache;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import com.freshdirect.cms.core.domain.ContentKey;

@Service
public class CacheEvictors {

    @Caching(evict = { @CacheEvict(value = "attributeCache", key = "#contentKey") })
    public void evictAttributeCacheWithContentKey(ContentKey contentKey) {
    }

    @Caching(evict = { @CacheEvict(value = "nodesByIdCache", key = "#contentKey.id"), @CacheEvict(value = "nodesByKeyCache", key = "#contentKey") })
    public void evictContentFactoryCaches(ContentKey contentKey) {
    }
}
