package com.freshdirect.webapp.soy;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;

import com.freshdirect.framework.util.log.LoggerFactory;
import com.google.template.soy.data.SoyMapData;


public class SoyRendererTag extends SimpleTagSupport {

	private static final Logger LOGGER = LoggerFactory.getInstance( SoyRendererTag.class );
		
	private String template;
	private Map<String,?> data;
	
	public String getTemplate() {
		return template;
	}	
	public void setTemplate( String template ) {
		this.template = template;
	}
	public Map<String,?> getData() {
		return data;
	}	
	public void setData( Map<String,?> data ) {
		this.data = data;
	}

	
	@Override
	public void doTag() throws JspException {
		
		JspWriter out = getJspContext().getOut();
		ServletContext servletCtx = ((PageContext)getJspContext()).getServletContext();		
		
		//LOGGER.debug( "Rendering " + template);
		
	    SoyMapData soyData;
	    
	    if ( data == null ) {
	    	soyData = new SoyMapData();
	    } else if ( data instanceof SoyMapData ) {
	    	soyData = (SoyMapData)data;
	    } else {
	    	soyData = new SoyMapData( data );
	    }
	    
	    try {
	    	String result = StringEscapeUtils.unescapeHtml(SoyTemplateEngine.getInstance().render( servletCtx, template, soyData ));
	    	out.write( result );
	    	
//			LOGGER.debug( "Rendered " + template + " successfully.");
//			LOGGER.debug( "data = " + data );
//			LOGGER.debug( "result = " + result );

	    } catch ( IOException e ) {
			throw new JspException( "Failed to render template: "+template, e );
		} catch (RuntimeException e) {
			throw new JspException( "Failed to render template: "+ template + " => "+e.getMessage(), e );
		}
		
	}
	
}
