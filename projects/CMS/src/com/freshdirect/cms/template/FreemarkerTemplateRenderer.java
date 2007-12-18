package com.freshdirect.cms.template;

import java.io.IOException;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Category;

import com.freshdirect.fdstore.content.TemplateRenderer;

import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.cache.URLTemplateLoader;
import freemarker.core.Environment;
import freemarker.template.Configuration;
import freemarker.template.TemplateExceptionHandler;

/**
 * Freemarker-specific implementation of {@link ITemplateRenderer}.
 */
public class FreemarkerTemplateRenderer implements ITemplateRenderer {

	private final static Category LOGGER = Category.getInstance(TemplateRenderer.class);

	private final Configuration cfg;
	
	/** The list of parsing exceptions, for error reporting */
	private List exceptions;
	
	public FreemarkerTemplateRenderer(Class loaderClass, String baseClassPath) {
		this.cfg = new Configuration();
		cfg.setLocalizedLookup(false);
		TemplateLoader[] loaders = {
				new MyClassTemplateLoader(loaderClass, baseClassPath),
				new RepositoryTemplateLoader() };
		cfg.setTemplateLoader(new MultiTemplateLoader(loaders));
		cfg.setTemplateExceptionHandler(new LoggingExceptionHandler());
	}
	
	/* (non-Javadoc)
	 * @see com.freshdirect.cms.template.ITemplateRenderer#render(java.net.URL, java.io.Writer, java.lang.Object)
	 */
	public void render(URL url, Writer out, Object context, boolean withErrorReport) throws IOException,
			TemplateException {
		freemarker.template.Template template = cfg.getTemplate(url.toString());
		exceptions = new ArrayList();
		try {
			template.process(context, out);
			
			// render an HTML error report to the end, if requested
			if (withErrorReport && exceptions.size() > 0) {
				Exception exception = (Exception) exceptions.get(0);
				
				out.write("<hr/><div class=\"error\">"
					 	+ "total number of errors: " + exceptions.size() + "<br/>"
					    + "the topmost error:<br/>" + exception.getMessage() + "<br/>"
					    + "</class>");
			}
		} catch (freemarker.template.TemplateException e) {
			throw new TemplateException(e);
		}

	}
	
	/* (non-Javadoc)
	 * @see com.freshdirect.fdstore.content.ITemplateRenderer#isTemplate(java.net.URL)
	 */
	public boolean isTemplate(URL url) {
		return url.toString().endsWith(".ftl");
	}
	
	/**
	 * {@link ClassTemplateLoader} implementation that works around resource names
	 * prefixed with a protocol (submitting as an issue to FM devels).
	 */
	private static class MyClassTemplateLoader extends ClassTemplateLoader {

		public MyClassTemplateLoader(Class loaderClass, String path) {
			super(loaderClass, path);
		}

		protected URL getURL(String name) {
			int protoPos = name.indexOf("://");
			if (protoPos > 0) {
				name = name.substring(protoPos + 3);
			}
			return super.getURL(name);
		}
	}

	private static class RepositoryTemplateLoader extends URLTemplateLoader {

		protected URL getURL(String atr) {
			try {
				return new URL(atr);
			} catch (MalformedURLException e) {
				throw new RuntimeException(e);
			}
		}

	}

	private class LoggingExceptionHandler implements
			TemplateExceptionHandler {

		/*
		 * (non-Javadoc)
		 * 
		 * @see freemarker.template.TemplateExceptionHandler#handleTemplateException(freemarker.template.TemplateException,
		 *      freemarker.core.Environment, java.io.Writer)
		 */
		public void handleTemplateException(
				freemarker.template.TemplateException exception,
				Environment environment, Writer writer)
				throws freemarker.template.TemplateException {
			LOGGER.warn("Template handling exception", exception);
			exceptions.add(exception);
		}

	}

}
