package com.freshdirect.webapp.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map;

import org.apache.log4j.Logger;

import com.freshdirect.common.pricing.PricingContext;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.framework.content.TemplateRenderer;
import com.freshdirect.framework.template.ITemplateRenderer;
import com.freshdirect.framework.template.TemplateException;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.webapp.taglib.IncludeMediaTag;
import com.freshdirect.webapp.template.TemplateContext;

public class MediaUtils {
	private static final Logger LOGGER = LoggerFactory.getInstance(MediaUtils.class);
	
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


	/**
	 * Convenience method to check if a media exists at the given path
	 * 
	 * @param mediaPath
	 * @return
	 */
	public static boolean checkMedia(String mediaPath) {
		try {
			URL url = MediaUtils.resolve(FDStoreProperties.getMediaPath(), mediaPath);
			if ("file".equalsIgnoreCase( url.getProtocol())) {
				File f = new File(url.toURI());
				return f.exists();
			} else if ("http".equalsIgnoreCase( url.getProtocol())) {
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setRequestMethod("HEAD"); 
				conn.connect(); 
				int resp = conn.getResponseCode();
				conn.disconnect();
				return resp == HttpURLConnection.HTTP_OK;
			} else {
				LOGGER.warn("Unknown protocol " + url.getProtocol());
				return false;
			}
		} catch (IOException e) {
			LOGGER.error("Something broke while checking media: " + mediaPath, e);
			return false;
		} catch (URISyntaxException e) {
			LOGGER.error("Bad URL for media: " + mediaPath, e);
			return false;
		}
	}
}
