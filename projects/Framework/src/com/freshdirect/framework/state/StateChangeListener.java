package com.freshdirect.framework.state;

public interface StateChangeListener<T> {
	public void willLeaveState(T state);
	public void willEnterState(T state);

	public void didLeaveState(T state);
	public void didEnterState(T state);

	public void failedToEnterState(T state);
}
