package com.freshdirect.webapp.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.net.URL;
import java.util.Map;

import com.freshdirect.common.pricing.PricingContext;

import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.framework.content.TemplateRenderer;
import com.freshdirect.framework.template.ITemplateRenderer;
import com.freshdirect.framework.template.TemplateException;
import com.freshdirect.webapp.taglib.IncludeMediaTag;
import com.freshdirect.webapp.template.TemplateContext;

public class MediaUtils {

	public static URL resolve(String rootPath, String childPath)
			throws IOException {
		// remove absolute path mark
		if (childPath.startsWith("/")) {
			childPath = childPath.substring(1, childPath.length());
		}
		
		
		final URL url = new URL( new URL(rootPath) , childPath.replace(" ", "%20"));
	
		if (!url.toString().startsWith(rootPath)) {
			throw new IOException("Child path not under root");
		}
	
		return url;
	}

	public static void renderMedia(URL url, Writer out, TemplateContext context, boolean withErrorReport) throws TemplateException, IOException {
		ITemplateRenderer renderer = TemplateRenderer.getInstance();

		InputStream in = null;
		try {
			in = url.openStream();

			if (in == null) {
				throw new FileNotFoundException();
			}

			if (renderer.isTemplate(url)) {
				if (context == null) {
					context = new TemplateContext();
				}
				renderer.render(url, out, context, withErrorReport);

			} else {
				byte[] buf = new byte[4096];
				int i;
				while ((i = in.read(buf)) != -1) {
					out.write(new String(buf, 0, i));
				}
			}		
		} finally {
			if (in != null) {
				in.close();
			}
		}
	}
	


	/**
	 * Helper method to support {@link IncludeMediaTag#doStartTag()}
	 * 
	 * @param name
	 * @param out Writer
	 * @param parameters Template parameters
	 * @param withErrorReport
	 * @return
	 * @throws IOException
	 * @throws TemplateException
	 */
	public static void render(String name, Writer out, Map parameters, Boolean withErrorReport) throws IOException, TemplateException {
		URL url = MediaUtils.resolve(FDStoreProperties.getMediaPath(), name);

		// JspWriter out = this.pageContext.getOut();
		
		TemplateContext context = parameters != null ? new TemplateContext(parameters) : null;
		boolean errorReport = withErrorReport == null ? false : withErrorReport.booleanValue();

		MediaUtils.renderMedia(url, out, context, errorReport);
	}
	
	public static void render(String name, Writer out, Map parameters, Boolean withErrorReport, PricingContext pricingContext) throws IOException, TemplateException {
		URL url = MediaUtils.resolve(FDStoreProperties.getMediaPath(), name);

		// JspWriter out = this.pageContext.getOut();
		
		TemplateContext context = parameters != null ? new TemplateContext(parameters, pricingContext) : null;
		boolean errorReport = withErrorReport == null ? false : withErrorReport.booleanValue();

		MediaUtils.renderMedia(url, out, context, errorReport);
	}
}
