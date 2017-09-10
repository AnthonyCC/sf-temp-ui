package com.freshdirect.framework.event;

import java.util.Arrays;
import java.util.List;

public class EventSourceUtil {

    private static final List<EnumEventSource> PRODUCT_SAMPLE_CAROUSEL_EVENT_SOURCES = Arrays.asList(EnumEventSource.ps_caraousal, EnumEventSource.ps_carousel_view_cart,
            EnumEventSource.ps_carousel_checkout);

    public static boolean isSourceProductSampleCarousel(EnumEventSource source) {
        return PRODUCT_SAMPLE_CAROUSEL_EVENT_SOURCES.contains(source);
    }
}
