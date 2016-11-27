package com.freshdirect.webapp.util.prodconf;

public class DefaultProductConfigurationStrategy extends FallbackConfigurationStrategy {
	public DefaultProductConfigurationStrategy() {
		super(new ConfiguredProductConfigurationStrategy(new AutoConfigurationStrategy()));
	}
}
