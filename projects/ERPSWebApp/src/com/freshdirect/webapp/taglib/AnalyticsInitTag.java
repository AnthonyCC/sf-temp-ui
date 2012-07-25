package com.freshdirect.webapp.taglib;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.apache.log4j.Logger;

import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.framework.util.log.LoggerFactory;

public class AnalyticsInitTag extends SimpleTagSupport {

	private static final Logger LOGGER = LoggerFactory.getInstance(AnalyticsInitTag.class);

	private final String ANALYTICS_INCLUDE = "(function() { var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true; ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js'; var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s); })();";
	private static final String START_SCRIPT_TAG = "<script type=\"text/javascript\">";
	private static final String END_SCRIPT_TAG = "</script>";
	
	@Override
	public void doTag() throws JspException, IOException {

		String account = FDStoreProperties.getGoogleAnalyticsKey();
		String domain = FDStoreProperties.getGoogleAnlayticsDomain();
		
		getJspContext().getOut().println(START_SCRIPT_TAG);
		
		getJspContext().getOut().println("var _gaq = _gaq || [];");
		getJspContext().getOut().println("_gaq.push(['_setAccount', '"+account+"']);");
		getJspContext().getOut().println("_gaq.push(['_setDomainName', '"+domain+"']);");
		getJspContext().getOut().println("_gaq.push(['_setCustomVar',1,'Video','Watcher',1]);");
		getJspContext().getOut().println("_gaq.push(['_trackPageview']);");
		
		getJspContext().getOut().println(ANALYTICS_INCLUDE);
		
		getJspContext().getOut().println(END_SCRIPT_TAG);
	}

}
