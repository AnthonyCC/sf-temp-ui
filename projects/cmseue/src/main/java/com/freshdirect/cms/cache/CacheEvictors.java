package com.freshdirect.cms.cache;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.draft.domain.DraftContext;

@Service
public class CacheEvictors {

    @Caching(evict = { @CacheEvict(value = "attributeCache", key = "#contentKey") })
    public void evictAttributeCacheWithContentKey(ContentKey contentKey) {
    }

    @Caching(evict = { @CacheEvict(value = "nodesByIdCache", key = "#contentKey.id"), @CacheEvict(value = "nodesByKeyCache", key = "#contentKey.toString()") })
    public void evictContentFactoryCaches(ContentKey contentKey) {
    }
    
    @Caching(evict = { 
        @CacheEvict(value = "nodesByIdCache", key = "#draftContext.getDraftId() + '|' + #contentKey.id"),
        @CacheEvict(value = "nodesByKeyCache", key = "#draftContext.getDraftId() + '|' + #contentKey")
    })
    public void evictContentFactoryDraftCaches(ContentKey contentKey, DraftContext draftContext) {
    }
}
