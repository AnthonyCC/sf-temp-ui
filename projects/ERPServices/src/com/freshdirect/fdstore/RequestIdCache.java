package com.freshdirect.fdstore;

import java.util.UUID;

public class RequestIdCache {

    private static ThreadLocal<String> requestIdCache = new ThreadLocal<String>();

    public static void init() {
        requestIdCache.set(UUID.randomUUID().toString());
    }

    public static void clear() {
        requestIdCache.remove();
    }

    public static String getRequestId() {
        return requestIdCache.get();
    }
}
