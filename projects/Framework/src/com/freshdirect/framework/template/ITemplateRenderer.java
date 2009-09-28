package com.freshdirect.framework.template;


import java.io.IOException;
import java.io.Writer;
import java.net.URL;


/**
 * Interface for template renderer implementations.
 */
public interface ITemplateRenderer {

	/**
	 * Determine if a resource is a template or not.
	 * 
	 * @param url
	 *            the url of the resource (not null)
	 * @return true if the URL is a supported template format
	 */
	public boolean isTemplate(URL url);

	/**
	 * @param url
	 * @param out
	 * @param context
	 * @param withErrorReport if true, an error report will be rendered after
	 *        the content of the rendered template.
	 * @throws IOException
	 * @throws TemplateException
	 */
	public void render(URL url, Writer out, Object context, boolean withErrorReport) throws IOException,
			TemplateException;

}