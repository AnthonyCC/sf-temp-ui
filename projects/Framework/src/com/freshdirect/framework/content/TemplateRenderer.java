/**
 * 
 */
package com.freshdirect.framework.content;

import java.io.IOException;
import java.net.URL;

import com.freshdirect.framework.template.FreemarkerTemplateRenderer;
import com.freshdirect.framework.template.ITemplateRenderer;

/**
 * Singleton template renderer.
 * 
 * @TODO make this obtain a service configured via IoC
 */
public class TemplateRenderer {

	private final static ITemplateRenderer INSTANCE = new FreemarkerTemplateRenderer(
			TemplateRenderer.class, "/com/freshdirect/webapp/template/");

	private TemplateRenderer() {
	}

	public static ITemplateRenderer getInstance() {
		return INSTANCE;
	}

	
	public static URL resolve(String rootPath, String childPath)
	throws IOException {
		URL url = new URL(rootPath);
		if (childPath.startsWith("/")) {
			childPath = childPath.substring(1, childPath.length());
		}
		url = new URL(url, childPath);
		
		if (!url.toString().startsWith(rootPath)) {
			throw new IOException("Child path not under root");
		}

	return url;
	}
}
