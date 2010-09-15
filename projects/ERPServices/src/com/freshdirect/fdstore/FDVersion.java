package com.freshdirect.fdstore;

public interface FDVersion<T extends Comparable<T>> {
	public T getVersion();
}
