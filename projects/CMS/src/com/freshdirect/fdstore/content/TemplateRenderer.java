/**
 * 
 */
package com.freshdirect.fdstore.content;

import com.freshdirect.cms.template.FreemarkerTemplateRenderer;
import com.freshdirect.cms.template.ITemplateRenderer;

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

}
