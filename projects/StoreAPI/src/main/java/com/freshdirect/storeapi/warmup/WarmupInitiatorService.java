package com.freshdirect.storeapi.warmup;

/**
 * This service is to initiate the Warmup via reflection.
 *
 */
public class WarmupInitiatorService {

    private static final String REPEAT_WARMUP_METHOD_NAME = "repeatWarmup";
    private static final String DEFAULT_SERVICE_METHOD_NAME = "defaultService";
    private static final String WARMUP_SERVICE_CLASS_NAME = "com.freshdirect.webapp.warmup.WarmupService";
    private static final WarmupInitiatorService INSTANCE = new WarmupInitiatorService();

    public static WarmupInitiatorService getInstance() {
        return INSTANCE;
    }

    private WarmupInitiatorService() {

    }

    /**
     * This method starts the warmup or repeatWarmup via reflection. This throws Exception as non of the current users of this method are interested in the exact exception. Refine
     * this if and when it is needed.
     * 
     * @throws Exception
     *             if something goes wrong
     */
    public void startWarmupViaReflection() throws Exception {
        Class<?> warmupServiceClass;
        String className = WARMUP_SERVICE_CLASS_NAME;
        warmupServiceClass = Class.forName(className);
        Object warmupService = warmupServiceClass.getMethod(DEFAULT_SERVICE_METHOD_NAME, null).invoke(warmupServiceClass, null);
        warmupService.getClass().getMethod(REPEAT_WARMUP_METHOD_NAME, null).invoke(warmupService, null);
    }

}
