package com.freshdirect.webapp.taglib;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.BodyTagSupport;

/**
 *	JSP tag to convert a suitable java object to JSON serialized form.
 *	
 * @author treer
 */
public class ToJSONTag extends BodyTagSupport {

	private static final long	serialVersionUID	= 5497142610713758686L;

	private static final Logger LOGGER = LoggerFactory.getInstance( ToJSONTag.class );

	public static final String JSONP_URL_PARAM = "jsonp";
	
	private Object object;
	private String jsonpWrapper;
	private boolean noHeaders = false;
	
	public Object getObject() {
		return object;
	}	
	public void setObject( Object object ) {
		this.object = object;
	}
	public String getJsonpWrapper() {
		return jsonpWrapper;
	}	
	public void setJsonpWrapper( String jsonpWrapper ) {
		this.jsonpWrapper = jsonpWrapper;
	}
	public boolean isNoHeaders() {
		return noHeaders;
	}	
	public void setNoHeaders( boolean noHeaders ) {
		this.noHeaders = noHeaders;
	}
	
	

	@Override
	public int doStartTag() throws JspException {		
    	HttpServletRequest request = ((HttpServletRequest)pageContext.getRequest());
        HttpServletResponse response = (HttpServletResponse)pageContext.getResponse();
        
		try {			
			// Set jsonp wrapper from url param
	    	String jsonpParam = request.getParameter( JSONP_URL_PARAM );
	    	if ( jsonpParam != null ) {
	    		setJsonpWrapper( jsonpParam );
	    	}

	    	if ( !noHeaders ) {
		        // Set common response properties for JSON response
		    	response.setHeader("Cache-Control", "no-cache");
		    	response.setHeader("Pragma", "no-cache");
				if ( jsonpWrapper != null ) {
					// JSONP
			    	response.setContentType( "application/javascript" );
				} else {
					// JSON
			    	response.setContentType( "application/json" );
				}
		    	response.setLocale( Locale.US );
		    	response.setCharacterEncoding( "ISO-8859-1" );
	    	}

			// Serialize data to JSON and write out the result 
			Writer writer = new StringWriter();
			new ObjectMapper().writeValue(writer, object);
			JspWriter out = pageContext.getOut();
			
			if ( jsonpWrapper != null ) {
				out.print( jsonpWrapper + "(" );
			}
			

			//out.print( writer.toString() );
			/* make json script-tag compatible */
			/* double escape the escape */
			out.print( writer.toString().replaceAll("</script>", "<\\\\/script>") );
			/* alternate fix, split the tag to avoid parser */
			//out.print( writer.toString().replaceAll("</script>", "</scr+\"+\"ipt>") );
			
			
			if ( jsonpWrapper != null ) {
				out.print( ");" );
			}			
			
			return EVAL_BODY_BUFFERED;
			
		} catch (JsonGenerationException e) {
			LOGGER.error("Cannot convert to JSON", e);
		} catch (JsonMappingException e) {
			LOGGER.error("Cannot convert to JSON", e);
		} catch (IOException e) {
			LOGGER.error("Cannot convert to JSON", e);
		}
		sendError( response, 500 );
		return SKIP_BODY;
	}
	
    private static final void sendError( HttpServletResponse response, int errorCode ) {
    	try {
			response.sendError( errorCode );
		} catch ( IOException e ) {
			LOGGER.error( "Failed to send error response", e );
		}
    }

}
