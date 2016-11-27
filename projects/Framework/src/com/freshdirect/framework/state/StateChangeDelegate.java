package com.freshdirect.framework.state;

public interface StateChangeDelegate<T> {
	public boolean changeState(T s1, T s2);
}
