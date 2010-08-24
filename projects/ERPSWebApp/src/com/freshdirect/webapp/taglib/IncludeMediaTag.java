package com.freshdirect.webapp.taglib;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

import javax.servlet.jsp.JspException;

import org.apache.log4j.Category;

import com.freshdirect.common.pricing.PricingContext;
import com.freshdirect.fdstore.content.Html;
import com.freshdirect.fdstore.content.Image;
import com.freshdirect.fdstore.content.MediaI;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.template.TemplateException;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.BodyTagSupport;
import com.freshdirect.webapp.taglib.fdstore.SessionName;
import com.freshdirect.webapp.util.MediaUtils;

public class IncludeMediaTag extends BodyTagSupport {

	private static final long	serialVersionUID	= 2286819781084137526L;

	private final static Category LOGGER = LoggerFactory.getInstance(IncludeMediaTag.class);

	private String name;

	private Map parameters;
	
	private Boolean withErrorReport;
	
	private MediaI media;
	
	public void setName(String file) {
		this.name = file;
	}
	
	public void setParameters(Map parameters) {
		this.parameters = parameters;
	}

	public void setWithErrorReport(Boolean withErrorReport) {
		this.withErrorReport = withErrorReport;
	}
	
	public void setMedia( MediaI media ) {
		this.media = media;
	}

	public int doStartTag() throws JspException {
		try {
		    if (this.name == null && this.media == null) {		    	
		        throw new NullPointerException("Media path not specified!");
		    }
		    
		    if ( media instanceof Image ) {
		    	Image img = (Image)media;
		    	pageContext.getOut().append( "<img src=\"" + img.getPath() + "\" width=\"" + img.getWidth() + "\" height=\"" + img.getHeight() + "\" alt=\"" + name + "\">" );		    	
				return SKIP_BODY;
		    } 
		    		    
		    if ( media instanceof Html ) {
		    	this.name = media.getPath();		    	
		    } 
			FDUserI user = (FDUserI) pageContext.getSession().getAttribute(SessionName.USER);
			//Pass the pricing context to the template context
			MediaUtils.render(this.name, this.pageContext.getOut(), this.parameters, this.withErrorReport, 
					user != null && user.getPricingContext() != null ? user.getPricingContext() : PricingContext.DEFAULT);
				
			return SKIP_BODY;
			
		} catch (FileNotFoundException e) {
			LOGGER.warn("Media file not found " + name);
			return EVAL_BODY_INCLUDE;
		} catch (IOException e) {
			LOGGER.warn("Failed to load resource", e);
			return SKIP_BODY;
		} catch (TemplateException e) {
			LOGGER.warn("Failed to process template", e);
			throw new JspException(e);
		} 
	}

}
