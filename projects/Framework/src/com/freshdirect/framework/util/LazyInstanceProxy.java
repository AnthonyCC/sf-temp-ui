package com.freshdirect.framework.util;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.apache.commons.collections.Factory;

public class LazyInstanceProxy implements InvocationHandler, Serializable {

	private final Factory factory;

	private Object actualObject = null;

	private LazyInstanceProxy(Factory factory) {
		this.factory = factory;
	}

	public static Object newInstance(Class targetInterface, Factory factory) {
		return Proxy.newProxyInstance(targetInterface.getClassLoader(), new Class[] {targetInterface}, new LazyInstanceProxy(factory));
	}

	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		if (actualObject == null) {
			actualObject = factory.create();
		}

		return method.invoke(actualObject, args);
	}

}