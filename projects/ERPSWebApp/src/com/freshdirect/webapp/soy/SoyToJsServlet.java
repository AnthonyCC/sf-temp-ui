package com.freshdirect.webapp.soy;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.framework.util.log.LoggerFactory;


public class SoyToJsServlet extends HttpServlet {

	private static final long	serialVersionUID	= -6264155451529928792L;	
	
	private static final Logger LOGGER = LoggerFactory.getInstance( SoyToJsServlet.class );
	
	@SuppressWarnings( "deprecation" )
	@Override
	protected void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
		
	    String packageName = SoyTemplateEngine.cleanPackageName( request.getPathInfo() );
	    		
		//LOGGER.info( "Serving soy package: " + packageName );
				
		try {
			List<String> jsSrcs = SoyTemplateEngine.getInstance().getJsSrc( getServletContext(), packageName ); 

			
			Writer out = response.getWriter();
			response.setContentType( "application/javascript" );

			if ( FDStoreProperties.isSoyDebugMode() ) {				
				// debug mode
				response.addHeader( "Cache-control", "public, max-age=0, no-cache" );
			} else {
				// production mode - set cache headers to very long expiration
				response.addHeader( "Cache-control", "public, max-age=14400" );
			}				
			
			for ( String js : jsSrcs ) {
				out.write( js );
			}			

			//LOGGER.info( "Served soy package: " + packageName );
			
	    } catch ( IOException e ) {
	    	LOGGER.error( "Failed to serve soy package: "+packageName, e );
	    	response.sendError( 500, "Failed to serve soy package: "+packageName );
		} catch (RuntimeException e) {
	    	LOGGER.error( "Failed to serve soy package: "+packageName, e );
	    	LOGGER.error( "Soy compiler error: " + e.getMessage() );
	    	
	    	// On compiler errors we try to send back a meaningful error message
	    	// We cannot use HTTP 500 here, because that redirects to error.jsp
	    	// TODO: find a suitable error code, we use "HTTP 418 I'm a teapot" for now...
	    	
			response.setContentType( "text/plain" );
			response.setStatus( 418, "Soy Compile Error" );
			
			Writer outStream = response.getWriter();
			outStream.write( "Soy compiler error in package: " + packageName );
			outStream.write( "\n" );			
			outStream.write( e.getMessage() );
			outStream.write( "\n" );
			outStream.flush();
		}
	    
	}
	
}
