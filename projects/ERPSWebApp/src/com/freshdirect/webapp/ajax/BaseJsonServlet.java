package com.freshdirect.webapp.ajax;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URLDecoder;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDUser;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;
import com.freshdirect.webapp.taglib.fdstore.SessionName;


public abstract class BaseJsonServlet extends HttpServlet {

	private static final long	serialVersionUID	= -425214649996529130L;

	private static final Logger LOG = LoggerFactory.getInstance( BaseJsonServlet.class );
	
	@Override
	protected final void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
		try {
			FDUserI user = authenticate( request );
			if ( synchronizeOnUser() ) {
				synchronized ( user ) {
					doGet( request, response, user );
				}
			} else {
				doGet( request, response, user );
			}
		} catch ( HttpErrorResponse e ) {
			response.sendError( e.getErrorCode() );
		}
	}	
	protected void doGet( HttpServletRequest request, HttpServletResponse response, FDUserI user ) throws HttpErrorResponse {
		returnHttpError( 405 );	// 405 Method Not Allowed
	}
	
	@Override
	protected final void doPut( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
		try {
			FDUserI user = authenticate( request );
			if ( synchronizeOnUser() ) {
				synchronized ( user ) {
					parsePutData(request);
					doPut( request, response, user );
				}
			} else {
				doPut( request, response, user );
			}
		} catch ( HttpErrorResponse e ) {
			response.sendError( e.getErrorCode() );
		}
	}
	protected void doPut( HttpServletRequest request, HttpServletResponse response, FDUserI user ) throws HttpErrorResponse {
		returnHttpError( 405 );	// 405 Method Not Allowed
	}
	
	@Override
	protected final void doDelete( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
		try {
			FDUserI user = authenticate( request );
			if ( synchronizeOnUser() ) {
				synchronized ( user ) {
					parsePutData(request);
					doDelete( request, response, user );
				}
			} else {
				doDelete( request, response, user );
			}
		} catch ( HttpErrorResponse e ) {
			response.sendError( e.getErrorCode() );
		}
	}
	protected void doDelete( HttpServletRequest request, HttpServletResponse response, FDUserI user ) throws HttpErrorResponse {
		returnHttpError( 405 );	// 405 Method Not Allowed
	}
	
	private void parsePutData(HttpServletRequest request) throws HttpErrorResponse {
		
		BufferedReader br;
		String data = null;
		try {
			br = new BufferedReader(new InputStreamReader(request.getInputStream()));
			data = br.readLine();
			if(data.contains("data=")){
				request.setAttribute("data", data.substring(data.indexOf("=")+1, data.length()));
			}
		} catch (IOException e1) {
			returnHttpError( 400, "Cannot read request data" );	// 400 Bad Request
		}
	}
	
	protected final void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {		
		try {
			FDUserI user = authenticate( request );
			if ( synchronizeOnUser() ) {
				synchronized ( user ) {
					doPost( request, response, user );
				}
			} else {
				doPost( request, response, user );
			}
		} catch ( HttpErrorResponse e ) {
			response.sendError( e.getErrorCode() );
		}
	}
	protected void doPost( HttpServletRequest request, HttpServletResponse response, FDUserI user ) throws HttpErrorResponse {
		returnHttpError( 405 );	// 405 Method Not Allowed
	}
	
	
	protected final <T> T parseRequestData( HttpServletRequest request, Class<T> typeClass ) throws HttpErrorResponse {
		return parseRequestData( request, typeClass, false );
	}
	
	protected final <T> T parseRequestData( HttpServletRequest request, Class<T> typeClass, boolean allowEmpty ) throws HttpErrorResponse {
	
		String reqJson = (String)request.getParameter( "data" );
		if(reqJson == null){
			reqJson = (String)request.getAttribute( "data" );
			if(reqJson != null){
				try {
					reqJson = URLDecoder.decode(reqJson, "UTF-8");
				} catch (UnsupportedEncodingException e) {
					returnHttpError( 400, "Cannot decode request string", e );	// 400 Bad Request
				}				
			}
		}
		if ( reqJson == null ) {
			if ( allowEmpty ) {
				return null;
			} else {
				returnHttpError( 400, "Empty request. Aborting" );	// 400 Bad Request
			}
		}
		
		LOG.debug( "Parsing request data: " + reqJson );
		
		T reqData = null;
		try {
			reqData = new ObjectMapper().readValue(reqJson, typeClass);
		} catch (IOException e) {
			returnHttpError( 400, "Cannot read JSON", e );	// 400 Bad Request
		}
		if ( reqData == null && !allowEmpty ) {
			returnHttpError( 400, "Cannot read JSON" );	// 400 Bad Request
		}
		
		return reqData;
	}
	
	protected final <T> void writeResponseData( HttpServletResponse response, T responseData ) throws HttpErrorResponse {
		
		// Set response parameters
		configureJsonResponse( response );
		
		// Serialize data to JSON and write out the result 
		try {
			
			Writer writer = new StringWriter();
			new ObjectMapper().writeValue( writer, responseData );
			
			ServletOutputStream out = response.getOutputStream();
			String responseStr = writer.toString();
			
			LOG.debug( "Generated response data: " + responseStr );
			
			out.print( responseStr );
			
			out.flush();
			
		} catch ( JsonGenerationException e ) {
        	returnHttpError( 500, "Error writing JSON response", e );	// 500 Internal Server Error
		} catch ( JsonMappingException e ) {
        	returnHttpError( 500, "Error writing JSON response", e );	// 500 Internal Server Error
		} catch ( IOException e ) {
        	returnHttpError( 500, "Error writing JSON response", e );	// 500 Internal Server Error
		} catch ( Exception e ) {
        	returnHttpError( 500, "Error writing JSON response", e );	// 500 Internal Server Error
		}
	}
	
	public final static void configureJsonResponse(HttpServletResponse response) {
		// Set common response properties for JSON response
		response.setHeader( "Cache-Control", "no-cache" );
		response.setHeader( "Pragma", "no-cache" );
		response.setContentType( "application/json" );
		response.setLocale( Locale.US );
		response.setCharacterEncoding( "ISO-8859-1" );
	}

	/**
	 * Does the authentication, checks user auth level, returns the user object. 
	 * 
	 * Sends HTTP 401 Unauthorized if user is not found in session, or auth level is insufficient.
	 * 
	 * @param request
	 * @return the user object from the session
	 * @throws HttpErrorResponse
	 */
	protected final FDUserI authenticate( HttpServletRequest request ) throws HttpErrorResponse {
        HttpSession session = request.getSession();
    	
        FDUserI user = ((FDUserI)session.getAttribute(SessionName.USER));
        if ( user == null ) {
        	// There is no user data in the session. Serious problem, should not happen.
        	returnHttpError( 401, "No user in session!" ); // 401 Unauthorized
        }
        
		if ( user.getLevel() < getRequiredUserLevel() ) {
        	// User level not sufficient. 
        	returnHttpError( 401, "User auth level not sufficient!" ); // 401 Unauthorized
		}
        
		return user;		
	}
	
	/**
	 * Override to set a required user auth level.
	 * 
	 * @return the required user level (use GUEST,RECOGNIZED,SIGNED_IN constants from FDUserI)
	 */
	protected int getRequiredUserLevel() {		
		return FDUserI.RECOGNIZED;
	}
	
	/**
	 * Subclasses can override and return false to turn off automatic synchronization on the user object and do their own synchronization manually. 
	 * @return
	 */
	protected boolean synchronizeOnUser() {
		return true;
	}
	
	/**
	 * Utility method to persist the user object (and invalidate any related caches)
	 * 
	 * @param user
	 */
	public final static void saveUser( FDUserI user ) {
		synchronized ( user ) {
			
			// Invalidate cached information
			user.invalidateCache();

			// Update user
			user.updateUserState();
			
			// Save user
			try {
				if ( user instanceof FDUser ) {
					FDCustomerManager.storeUser( (FDUser)user );
				} else if ( user instanceof FDSessionUser ) {
					FDCustomerManager.storeUser( ((FDSessionUser)user).getUser() );
				}
			} catch (FDResourceException e) {
				LOG.error( "Store user failed", e );
			}
		}
	}

	protected final void returnHttpError( int errorCode ) throws HttpErrorResponse {
    	LOG.error( "Aborting with HTTP"+errorCode );
    	throw new HttpErrorResponse( errorCode );
	}
	protected final void returnHttpError( int errorCode, String errorMessage ) throws HttpErrorResponse {
    	LOG.error( errorMessage );
    	throw new HttpErrorResponse( errorCode );
	}
	protected final void returnHttpError( int errorCode, String errorMessage, Throwable e ) throws HttpErrorResponse {
    	LOG.error( errorMessage, e );
    	throw new HttpErrorResponse( errorCode );
	}
	
	protected final static class HttpErrorResponse extends Exception {
				
		private static final long	serialVersionUID	= -4320607318778165536L;

		public HttpErrorResponse( int errorCode ) {
			this.errorCode = errorCode;
		}
		
		private int errorCode;

		public int getErrorCode() {
			return errorCode;
		}
	}
}
