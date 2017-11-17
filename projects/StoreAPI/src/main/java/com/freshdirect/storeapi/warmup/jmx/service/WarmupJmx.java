package com.freshdirect.storeapi.warmup.jmx.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.freshdirect.storeapi.warmup.WarmupInitiatorService;

@Service
public class WarmupJmx implements WarmupJmxMBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(WarmupJmx.class);

    @Override
    public void repeatWarmup() {
        LOGGER.info("Received REPEAT-WARMUP notification via JMX. Repeating warmup...");
        try {
            WarmupInitiatorService.getInstance().startWarmupViaReflection();
        } catch (Exception e) {
            LOGGER.error("Exception while trying to start warmup", e);
        }

    }
}
