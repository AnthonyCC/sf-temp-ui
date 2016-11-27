package com.freshdirect.cms._import;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Enumeration;
import java.util.Properties;

import org.apache.log4j.Logger;

public class PlaceholderResolver {

	public static final Logger LOGGER = Logger.getLogger(PlaceholderResolver.class);
	public static final String PLACEHOLDER_PATTERN = "${%s}";
	private StringBuilder sb = new StringBuilder();

	public PlaceholderResolver(Reader reader, Properties properties) throws IOException {

		BufferedReader in = new BufferedReader(reader);

		String line;
		while ((line = in.readLine()) != null) {

			Enumeration<?> e = properties.propertyNames();
			while (e.hasMoreElements()) {
				String key = (String) e.nextElement();
				String placeHolder = String.format(PLACEHOLDER_PATTERN, key);
				String replacement = properties.getProperty(key);
				line = line.replace(placeHolder, replacement);
			}
			sb.append(line + "\n");
		}

	}

	public Reader getReader() {
		return new StringReader(sb.toString());
	}

	public String getString() {
		return sb.toString();
	}

}
