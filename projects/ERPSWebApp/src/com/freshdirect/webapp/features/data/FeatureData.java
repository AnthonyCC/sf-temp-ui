package com.freshdirect.webapp.features.data;

public class FeatureData {

	private String name;
	private String version;
	private boolean enabledInProperty;
	private boolean enabledInCookie;
	private boolean active;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public boolean isEnabledInProperty() {
		return enabledInProperty;
	}

	public void setEnabledInProperty(boolean enabledInProperty) {
		this.enabledInProperty = enabledInProperty;
	}

	public boolean isEnabledInCookie() {
		return enabledInCookie;
	}

	public void setEnabledInCookie(boolean enabledInCookie) {
		this.enabledInCookie = enabledInCookie;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
}