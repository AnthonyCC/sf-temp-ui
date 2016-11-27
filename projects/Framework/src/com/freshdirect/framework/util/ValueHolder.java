package com.freshdirect.framework.util;

/**
 * A very simple value holder implementation for INOUT parameters
 * @author segabor
 *
 * @param <T> arbitrary value
 */
public class ValueHolder<T> {
	private T value;

	public ValueHolder() {}

	public ValueHolder(T value) { this.value = value; }


	public T getValue() { return value; }
	
	public void setValue(T value) { this.value = value; }

	public boolean isSet() { return value != null; }
}
