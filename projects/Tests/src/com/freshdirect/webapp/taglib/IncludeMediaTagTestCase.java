package com.freshdirect.webapp.taglib;

import java.io.IOException;
import java.net.URL;

import com.freshdirect.webapp.util.MediaUtils;

import junit.framework.TestCase;

public class IncludeMediaTagTestCase extends TestCase {

	public IncludeMediaTagTestCase(String arg0) {
		super(arg0);
	}

	public void testResolve() throws IOException {

		String baseUrl = "http://www.freshdirect.com/test/";

		URL url = MediaUtils.resolve(baseUrl, "baz.txt");
		assertEquals(baseUrl + "baz.txt", url.toExternalForm());

		url = MediaUtils.resolve(baseUrl, "/baz.txt");
		assertEquals(baseUrl + "baz.txt", url.toExternalForm());

		try {
			url = MediaUtils.resolve(baseUrl, "../baz.txt");
			fail("IOException should be thrown");
		} catch (IOException ex) {
		}
	}

}