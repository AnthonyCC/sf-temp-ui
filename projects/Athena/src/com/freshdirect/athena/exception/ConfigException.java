package com.freshdirect.athena.exception;

public class ConfigException extends Exception {
	
	private ConfigExceptionType type;
	

	public ConfigException(ConfigExceptionType type) {
		super();
		this.type = type;
	}
	
	public ConfigException(Throwable e, ConfigExceptionType type) {
		super(e);
		this.type = type;
	}

	public ConfigExceptionType getType() {
		return type;
	}
	
	
}
