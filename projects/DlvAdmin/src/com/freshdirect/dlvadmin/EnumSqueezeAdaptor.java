package com.freshdirect.dlvadmin;

import java.io.IOException;
import java.lang.reflect.Method;

import org.apache.commons.lang.enums.Enum;
import org.apache.tapestry.services.DataSqueezer;


//XXX: Does anything use this class?
public class EnumSqueezeAdaptor 
//implements ISqueezeAdaptor 
{

	private String PREFIX = "E"; 
	private char SEPARATOR = '|';

	public String squeeze(DataSqueezer squeezer, Object o) throws IOException {
		Enum e = (Enum)o;
		return PREFIX + e.getClass().getName() + SEPARATOR + e.getName();
	}

	public Object unsqueeze(DataSqueezer squeezer, String str) throws IOException {
		int pos = str.indexOf(SEPARATOR);

		String className = str.substring(1, pos);
		String name = str.substring(pos+1, str.length());
		
		System.out.println(className + " - " + name);

		try {
			Class klazz = Class.forName(className);
			Method m = klazz.getMethod("getEnum", new Class[] { String.class });
			return (Enum)m.invoke(null, new Object[] { name }); 
		} catch (Exception e) {
			throw new IOException(e.toString());
		}
	}

	public void register(DataSqueezer squeezer) {
//		squeezer.register(PREFIX, Enum.class, this);
	}

}
