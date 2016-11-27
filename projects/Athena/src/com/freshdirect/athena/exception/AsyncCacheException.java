package com.freshdirect.athena.exception;

public class AsyncCacheException extends Exception {
	
	private AsyncCacheExceptionType type;
	

	public AsyncCacheException(AsyncCacheExceptionType type) {
		super();
		this.type = type;
	}
	
	public AsyncCacheException(Throwable e, AsyncCacheExceptionType type) {
		super(e);
		this.type = type;
	}

	public AsyncCacheExceptionType getType() {
		return type;
	}
	
	
}
