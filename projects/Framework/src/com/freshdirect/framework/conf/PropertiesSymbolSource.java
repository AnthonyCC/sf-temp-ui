package com.freshdirect.framework.conf;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.hivemind.SymbolSource;

/**
 * Hivemind SymbolSource that can read a properties file
 */
public class PropertiesSymbolSource implements SymbolSource {

	private final Properties props = new Properties();

	public PropertiesSymbolSource(String url) throws IOException {
		InputStream in = ResourceUtil.openResource(url);
		props.load(in);
		in.close();
	}

	public String valueForSymbol(String symbol) {
		return props.getProperty(symbol);
	}

}