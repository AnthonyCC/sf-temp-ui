package com.freshdirect.framework.util;

import java.beans.Introspector;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class BeanUtil {

	private final static Class[] NO_PARAMETERS = new Class[0];

	private BeanUtil() {}
	
	/**
	 * @throws IllegalArgumentException
	 */
	public static Object getProperty(Object bean, String propertyName) {
		Class clazz = bean.getClass();
		
		Method method;
		try {
			method = clazz.getMethod("get" + capitalize(propertyName), NO_PARAMETERS);
		} catch (NoSuchMethodException e) {
			throw new IllegalArgumentException("Getter for property "+propertyName+" not found in "+clazz);
		}
		
		try {
			return method.invoke(bean, NO_PARAMETERS);
		} catch (IllegalAccessException e) {
			throw new IllegalArgumentException("Getter for property "+propertyName+" not accessible in "+clazz);

		} catch (InvocationTargetException e) {
			throw new IllegalArgumentException("Error invoking getter for property "+propertyName+" on "+clazz +" "+e);
		}
	}


	/**
	 * @throws IllegalArgumentException
	 */
	public static void setProperty(Object bean, String propertyName, Object property, Class type) {
		Class clazz = bean.getClass();
		
		Method method;
		try {
			method = clazz.getMethod("set" + capitalize(propertyName), new Class[] { type });
		} catch (NoSuchMethodException e) {
			throw new IllegalArgumentException("Setter for property "+propertyName+" not found in "+clazz);
		}
		
		try {
			method.invoke(bean, new Object[] { property });
		} catch (IllegalAccessException e) {
			throw new IllegalArgumentException("Setter for property "+propertyName+" not accessible in "+clazz);

		} catch (InvocationTargetException e) {
			throw new IllegalArgumentException("Error invoking setter for property "+propertyName+" on "+clazz +" "+e);
		}
	}


	/**
	 * @return list of property names that have a corresponding setter method
	 *
	public static String[] getPropertyNames(Object bean) {
		Class clazz = bean.getClass();

		Method[] methods = clazz.getMethods();
		List properties = new ArrayList( methods.length );

		for (int i=0; i<methods.length; i++) {
			Method m = methods[i];
			if (m.getName().startsWith("set") && m.getReturnType()
		}
	}
	*/
	
	/**
	 * @return javabean capitalized version of name, eg fooBar -> FooBar
	 */
	public static String capitalize(String name) {
		StringBuffer buf = new StringBuffer(name);
		buf.setCharAt(0, Character.toUpperCase(buf.charAt(0)));
		return buf.toString();
	}
	
	public static String decapitalize(String name) {
		return Introspector.decapitalize(name);
	}
	
}
