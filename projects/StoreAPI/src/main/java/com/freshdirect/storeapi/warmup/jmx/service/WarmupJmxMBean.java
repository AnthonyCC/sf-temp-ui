package com.freshdirect.storeapi.warmup.jmx.service;


public interface WarmupJmxMBean {

    void repeatWarmup();
    
    int isWarmupFinished();

}
