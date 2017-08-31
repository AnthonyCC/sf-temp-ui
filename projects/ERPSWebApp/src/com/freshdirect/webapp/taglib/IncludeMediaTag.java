package com.freshdirect.webapp.taglib;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

import org.apache.log4j.Category;

import com.freshdirect.common.pricing.PricingContext;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.cache.EhCacheUtil;
import com.freshdirect.fdstore.content.Html;
import com.freshdirect.fdstore.content.Image;
import com.freshdirect.fdstore.content.MediaI;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.template.TemplateException;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.BodyTagSupport;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;
import com.freshdirect.webapp.taglib.fdstore.SessionName;
import com.freshdirect.webapp.util.MediaUtils;


public class IncludeMediaTag extends BodyTagSupport {

	private static final long	serialVersionUID	= 2286819781084137526L;

	private final static Category LOGGER = LoggerFactory.getInstance(IncludeMediaTag.class);

	private String name;

	private Map parameters;
	
	private Boolean withErrorReport;
	
	private MediaI media;
	
	private int mediaContentCacheSize;
	
	public IncludeMediaTag() {
		mediaContentCacheSize = FDStoreProperties.getRefreshSecsProduct();
	}
	
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
		    	pageContext.getOut().append( img.toHtml(name, true) );		    	
			return SKIP_BODY;
		    } 
		    		    
		    if ( media instanceof Html ) {
		    	this.name = media.getPath();		    	
		    } 
		    
		    if (mediaContentCacheSize != 0 && EhCacheUtil.isKeyInCache(EhCacheUtil.MEDIA_CONTENT_CACHE_NAME, this.name)) {
		    	String cachedMediaContent = EhCacheUtil.getObjectFromCache(EhCacheUtil.MEDIA_CONTENT_CACHE_NAME, this.name);
		    	if (cachedMediaContent != null) {
					this.pageContext.getOut().write(cachedMediaContent);
					
					return SKIP_BODY;
		    	} else {
		    		return EVAL_BODY_INCLUDE;
		    	}
		    }
		    
			FDUserI user = (FDUserI) pageContext.getSession().getAttribute(SessionName.USER);
			//Pass the pricing context to the template context
			if (this.parameters == null) {
				this.parameters = new HashMap();
			}
			/* pass user/sessionUser by default, so it doesn't need to be added every place this tag is used. */
			this.parameters.put("user", (FDUserI)user);
			this.parameters.put("sessionUser", (FDSessionUser)user);
			
			/* add current uri */
			HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
			this.parameters.put("uri", request.getRequestURI());
			
			StringWriter writer = new StringWriter();
			
			MediaUtils.render(this.name, writer, this.parameters, this.withErrorReport, 
					user != null && user.getPricingContext() != null ? user.getPricingContext() : PricingContext.DEFAULT);
			
			String out = writer.toString();
			
			//fix media if needed
			out = MediaUtils.fixMedia(out);
			cacheMediaContent(this.name, out);
			this.pageContext.getOut().write(out);
			
			return SKIP_BODY;
			
		} catch (FileNotFoundException e) {
			LOGGER.warn("Media file not found " + name);
			cacheMediaContent(this.name, null);
			return EVAL_BODY_INCLUDE;
		} catch (IOException e) {
			LOGGER.warn("Failed to load resource", e);
			return SKIP_BODY;
		} catch (TemplateException e) {
			LOGGER.warn("Failed to process template", e);
			throw new JspException(e);
		} 
	}
	
	private void cacheMediaContent(String key, String content) {
		boolean isCacheEnabled = mediaContentCacheSize != 0;
		if (key == null || key.isEmpty() || !isCacheEnabled){
			return;
		}
		if (content == null || content.length() < mediaContentCacheSize) {
			EhCacheUtil.putObjectToCache(EhCacheUtil.MEDIA_CONTENT_CACHE_NAME, key, content);
		}
		
	}

}
