package com.freshdirect.cms.persistence.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Profile("database")
@Service
@EnableScheduling
public class RegularCacheUpdaterTask {

    /**
     * Update period of ERPS caches
     */
    private static final long DELAY_MILLISECS = 60 * 60 * 1000; // 1h

    @Autowired
    private ERPSDataService erpsDataService;

    @Scheduled(initialDelay = DELAY_MILLISECS, fixedDelay = DELAY_MILLISECS)
    public void updateERPSDataCaches() {
        erpsDataService.populateCaches();
    }

}
