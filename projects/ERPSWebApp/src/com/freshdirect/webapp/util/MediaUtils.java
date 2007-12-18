package com.freshdirect.webapp.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.net.URL;

import javax.servlet.jsp.JspException;

import com.freshdirect.cms.template.ITemplateRenderer;
import com.freshdirect.cms.template.TemplateException;
import com.freshdirect.fdstore.content.TemplateRenderer;
import com.freshdirect.webapp.template.TemplateContext;

public class MediaUtils {

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
	
}
