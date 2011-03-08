package com.freshdirect.webapp.taglib;

import java.io.IOException;
import java.util.Collection;
import javax.servlet.jsp.JspException;

import org.apache.log4j.Category;

import com.freshdirect.common.pricing.PricingContext;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.sempixel.FDSemPixelCache;
import com.freshdirect.fdstore.sempixel.SemPixelModel;
import com.freshdirect.framework.template.TemplateException;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.BodyTagSupport;
import com.freshdirect.webapp.taglib.fdstore.SessionName;
import com.freshdirect.webapp.util.MediaUtils;

/*
 * Include media based on SemPixels
 * 
 * pixelNames is a comma separated string of names defined in the fdstore.sem.pixels property
 * */
public class SemPixelIncludeMediaTag extends BodyTagSupport {

	private static final long serialVersionUID = 1184706804296429744L;

	private final static Category LOGGER = LoggerFactory.getInstance( SemPixelIncludeMediaTag.class );
	
	private String id;
	private boolean withErrorReport = false;
	private boolean fileNotFound = false;
	private String pixelNames = "";
	
	public void setId(String id) {
		this.id = id;
	}

	public void setWithErrorReport(Boolean withErrorReport) {
		this.withErrorReport = withErrorReport;
	}
	
	public void setFileNotFound(boolean fileNotFound) {
		this.fileNotFound = fileNotFound;
	}

	public boolean isFileNotFound() {
		return fileNotFound;
	}

	public void setPixelNames(String pixelNames) {
		this.pixelNames = pixelNames;
	}

	public String getPixelNames() {
		return pixelNames;
	}

	@SuppressWarnings("unchecked")
	public int doStartTag() throws JspException {
		int renderTemplate = 0; //control if template will eventually render after logic
			    
		String[] sem_parsePixels = new String[0];
		FDSemPixelCache spc = FDSemPixelCache.getInstance();
		
		if ( "*".equals(this.getPixelNames()) ) {
			//display all pixel media

		    Collection keys = spc.getCachedSemPixels().keySet();
		    	
		    for(Object key: keys){
				renderTemplate += includePixelMedia(spc.getSemPixel((String) key));
		    }
		}else{
			//only display specific pixels
			sem_parsePixels = this.getPixelNames().split(",");

			for (int n = 0; n < sem_parsePixels.length; n++) {
				renderTemplate += includePixelMedia(spc.getSemPixel(sem_parsePixels[n]));
			}
		}
			
		if (renderTemplate > 0) {
			return EVAL_BODY_INCLUDE;
		}
		return SKIP_BODY;
	}
	
	public int includePixelMedia(SemPixelModel curSemPixel) throws JspException {
		if ( curSemPixel.isEnabled() && !"".equals(curSemPixel.getMediaPath()) ) {
			try {
				FDUserI user = (FDUserI) pageContext.getSession().getAttribute(SessionName.USER);
				
				//pixel enabled and has media, display
				//Pass the pricing context to the template context
				MediaUtils.render(curSemPixel.getMediaPath(), this.pageContext.getOut(), curSemPixel.getParams(), this.withErrorReport, 
						user != null && user.getPricingContext() != null ? user.getPricingContext() : PricingContext.DEFAULT);
				return EVAL_BODY_INCLUDE;
			} catch (IOException e) {
				LOGGER.warn("Failed to load resource", e);
				this.setFileNotFound(true);
				return SKIP_BODY;
			} catch (TemplateException e) {
				LOGGER.warn("Failed to process template", e);
				this.setFileNotFound(true);
				throw new JspException(e);
			}
		}
		return SKIP_BODY;
	}

}
