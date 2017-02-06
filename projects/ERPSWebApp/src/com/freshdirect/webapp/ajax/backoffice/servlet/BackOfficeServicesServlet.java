package com.freshdirect.webapp.ajax.backoffice.servlet;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.freshdirect.cms.ContentType;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.customer.FDInvalidConfigurationException;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.template.TemplateException;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.webapp.ajax.BaseJsonServlet;
import com.freshdirect.webapp.ajax.JsonHelper;
import com.freshdirect.webapp.ajax.BaseJsonServlet.HttpErrorResponse;
import com.freshdirect.webapp.ajax.backoffice.data.ActionDataRequest;
import com.freshdirect.webapp.ajax.backoffice.data.BackOfficeResponse;
import com.freshdirect.webapp.ajax.backoffice.data.SkuModelResponse;
import com.freshdirect.webapp.ajax.backoffice.service.BackOfficeService;
import com.freshdirect.webapp.ajax.expresscheckout.checkout.service.CheckoutService;
import com.freshdirect.webapp.ajax.expresscheckout.data.FormDataRequest;
import com.freshdirect.webapp.ajax.expresscheckout.data.FormDataResponse;
import com.freshdirect.webapp.ajax.expresscheckout.data.SinglePageCheckoutData;
import com.freshdirect.webapp.ajax.expresscheckout.service.SinglePageCheckoutFacade;
import com.freshdirect.webapp.checkout.RedirectToPage;

public class BackOfficeServicesServlet extends HttpServlet /* BaseJsonServlet*/ {
	
	private static final Logger LOG = LoggerFactory.getInstance( BackOfficeServicesServlet.class );

	@Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
     try{
		processRequest(request, response);
     } catch ( HttpErrorResponse e ) {
			response.sendError( e.getErrorCode() );
		}
    }
    

	@Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
	     try{
	 		processRequest(request, response);
	      } catch ( HttpErrorResponse e ) {
	 			response.sendError( e.getErrorCode() );
	 		}
    }



	protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws HttpErrorResponse {
		try {
			FDUserI user =null;

			LOG.debug("Inside BackOfficeServicesServlet - processRequest() Start");
			
			final ActionDataRequest actionDataRequest = BaseJsonServlet.parseRequestData(request, ActionDataRequest.class);
			BackOfficeResponse responseData = BackOfficeService.defaultService().submitPostRequest(user, actionDataRequest,   request,   response);
			
			writeResponseData(response, responseData);
			LOG.debug("Inside BackOfficeServicesServlet - processRequest() End"+actionDataRequest);
        } catch (FDResourceException exception) {
        	returnHttpError(500, exception.getMessage(), exception);
		}  catch (JspException e) {
			LOG.error("Failed to in JspException ", e);
			returnHttpError(500, "Failed to get data for the request  .");
		} catch (Exception e) {
			LOG.error("Failed to in Exception ", e);
			returnHttpError(500,"Failed to get data for the request  .");
		}
	}
	public final static <T> T parseRequestData( HttpServletRequest request, Class<T> typeClass ) throws HttpErrorResponse {
		return parseRequestData( request, typeClass, false );
	}
	
	protected final static <T> T parseRequestData( HttpServletRequest request, Class<T> typeClass, boolean allowEmpty ) throws HttpErrorResponse {
	
		T reqData = null;
		try {
			
			reqData = JsonHelper.parseRequestData(request, typeClass);
			if ( reqData == null ) {
				if ( allowEmpty ) {
					return null;
				}
				returnHttpError( 400, "Empty request. Aborting" );	// 400 Bad Request
			}
		
		} catch (IOException e) {
			returnHttpError( 400, "Cannot read JSON", e );	// 400 Bad Request
		}
		if ( reqData == null && !allowEmpty ) {
			returnHttpError( 400, "Cannot read JSON" );	// 400 Bad Request
		}
		
		return reqData;
	}
	
	protected final static <T> void writeResponseData( HttpServletResponse response, T responseData ) throws HttpErrorResponse {
		
		// Set response parameters
		configureJsonResponse( response );
		
		// Serialize data to JSON and write out the result 
		try {
			
			Writer writer = new StringWriter();
			new ObjectMapper().writeValue( writer, responseData );
			
			ServletOutputStream out = response.getOutputStream();
			String responseStr = writer.toString();
			
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
	public final static void returnHttpError( int errorCode, String errorMessage ) throws HttpErrorResponse {
    	LOG.error( errorMessage );
    	throw new HttpErrorResponse( errorCode );
	}
	public final static void returnHttpError( int errorCode, String errorMessage, Throwable e ) throws HttpErrorResponse {
    	LOG.error( errorMessage, e );
        throw new HttpErrorResponse(errorCode);
	}

}
