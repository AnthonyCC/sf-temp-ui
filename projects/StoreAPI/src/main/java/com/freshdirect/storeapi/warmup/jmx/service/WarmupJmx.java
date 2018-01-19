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

	@Override
	public int isWarmupFinished() {
		// TODO Auto-generated method stub
		LOGGER.info("Received WARMUP=STATE Get Request via JMX. Fetching warmup status...");
        try {
            Object result = WarmupInitiatorService.getInstance().isWarmupFinished();
            LOGGER.info("Fetching warmup status..." + result);
            return ((Boolean)result ? 1 : 0);
        } catch (Exception e) {
            LOGGER.error("Exception while trying to get warmup state", e);
        }
        
        LOGGER.info("Returned warmup status...false!");
        return 0;
	}
}
