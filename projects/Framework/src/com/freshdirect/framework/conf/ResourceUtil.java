/*
 * Created on Apr 6, 2005
 */
package com.freshdirect.framework.conf;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.net.URL;

import org.apache.hivemind.ClassResolver;
import org.apache.hivemind.Resource;
import org.apache.hivemind.impl.DefaultClassResolver;
import org.apache.hivemind.util.ClasspathResource;
import org.apache.hivemind.util.URLResource;

/**
 * @author vszathmary
 */
public class ResourceUtil {

	private final static String PREFIX_CLASSPATH = "classpath:";

	private ResourceUtil() {
	}

	/**
	 * Construct a Resource from location.
	 * 
	 * Prefix "classpath:" is processed as a ClasspathResource.
	 * Any other location is processed as URLResource.
	 * 
	 * @return Resource based on location  
	 */
	public static Resource getResource(String location) {
		if (location == null) {
			throw new IllegalArgumentException("Location cannot be null");
		}

		if (location.startsWith(PREFIX_CLASSPATH)) {
			ClassResolver resolver = new DefaultClassResolver();
			String path = location.substring(PREFIX_CLASSPATH.length() + 1);
			return new ClasspathResource(resolver, path);
		}
		System.out.println("ResourceUtil :"+location);
		return new URLResource(location);
	}

	public static InputStream openResource(String location) throws IOException {
		System.out.println("ResourceUtil.openResource() :"+location);
		Resource res = getResource(location);

		URL url = res.getResourceURL();
		if (url == null) {
			throw new IOException("Unknown resource: " + location);
		}

		InputStream stream = url.openStream();

		return stream;
	}

	/**
	 * Read a resource, and return its contents as a String.
	 *  
	 * @param location the URL of the resource.
	 * @return the contents of the resource, as a string.
	 * @throws IOException in case of I/O errors.
	 */
	public static String readResource(String location) throws IOException {
		InputStream      is     = openResource(location);
		LineNumberReader reader = new LineNumberReader(new InputStreamReader(is));
		StringBuffer		 strbuf = new StringBuffer();
		String			 line;
		
		while ((line = reader.readLine()) != null) {
			strbuf.append(line);
			strbuf.append('\n');
		}
		
		return strbuf.toString();
	}
}
