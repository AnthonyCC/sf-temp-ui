package com.freshdirect.webapp.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.freshdirect.common.pricing.PricingContext;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.content.Html;
import com.freshdirect.framework.content.TemplateRenderer;
import com.freshdirect.framework.template.ITemplateRenderer;
import com.freshdirect.framework.template.TemplateException;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.webapp.taglib.IncludeMediaTag;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;
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
				String encoding = FDStoreProperties.getMediaUtilsSourceEncoding();
				
				byte[] buf = new byte[4096];
				int i;
				while ((i = in.read(buf)) != -1) {
					out.write(new String(buf, 0, i, encoding));
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
	
	//based on IncludeMediaTag
	public static String renderHtmlToString(Html media, FDSessionUser user){
		String htmlString = "";
		
		if(media!=null) {
			Map<String,Object> parameters = new HashMap<String, Object>();
			/* pass user/sessionUser by default, so it doesn't need to be added every place this tag is used. */
			parameters.put("user", user);
			parameters.put("sessionUser", user);
			String name = media.getPath();

			if (name!=null){
				StringWriter writer = new StringWriter();
				PricingContext pc = user != null && user.getPricingContext() != null ? user.getPricingContext() : PricingContext.DEFAULT;
				
				try {
					MediaUtils.render(name, writer, parameters, false, pc);
					htmlString = writer.toString();
				} catch (IOException e) {
					LOGGER.error("IOException, returning empty string",e);
				} catch (TemplateException e) {
					LOGGER.error("TemplateException, returning empty string",e);
				} finally{
					try {
						writer.close();
					} catch (IOException e) {
						LOGGER.error(e);
					}
				}
			}
		}
	
		return htmlString;
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
	
	public static String fixMedia(String media){
		if(FDStoreProperties.isMediaUtilsReallyClose()){
			media = media.replaceAll("window.close", "window.reallyClose");
		}
		
		return media;
	}
}
