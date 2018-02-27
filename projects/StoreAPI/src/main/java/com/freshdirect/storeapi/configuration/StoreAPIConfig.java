package com.freshdirect.storeapi.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan({ "com.freshdirect.storeapi", "com.freshdirect.fdstore.content.browse.grabber", "com.freshdirect.smartstore.impl" })
public class StoreAPIConfig {
}
