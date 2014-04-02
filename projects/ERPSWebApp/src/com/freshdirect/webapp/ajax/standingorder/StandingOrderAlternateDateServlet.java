package com.freshdirect.webapp.ajax.standingorder;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.freshdirect.crm.CrmAgentModel;
import com.freshdirect.customer.EnumAccountActivityType;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.standingorders.EnumStandingOrderAlternateDeliveryType;
import com.freshdirect.fdstore.standingorders.FDStandingOrderAltDeliveryDate;
import com.freshdirect.fdstore.standingorders.FDStandingOrdersManager;
import com.freshdirect.fdstore.standingorders.StandingOrderAlternateDatesParser;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.webapp.taglib.crm.CrmSession;
import com.ibm.icu.util.Calendar;

public class StandingOrderAlternateDateServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3194743558815296602L;
	private static final Logger LOGGER = LoggerFactory.getInstance( StandingOrderAlternateDateServlet.class );
	private static DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
	private static DateFormat tf = new SimpleDateFormat("hh:mm a");
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String altId = request.getParameter("altId");
		FDStandingOrderAltDeliveryDate altDate = null;
		List<FDStandingOrderAltDeliveryDate> altDeliverDates = null;
		if(null != altId && !"".equals(altId.trim())){
			try {
				altDeliverDates = new ArrayList<FDStandingOrderAltDeliveryDate>();
				altDate =FDStandingOrdersManager.getInstance().getStandingOrderAltDeliveryDateById(altId);
				altDeliverDates.add(altDate);
			} catch (FDResourceException e) {
				LOGGER.error("Failed to get the standing order alternate delivery date with id:"+altId,e);
			}
		}
		else{
			try {
				altDeliverDates= FDStandingOrdersManager.getInstance().getStandingOrderAltDeliveryDates();
			} catch (FDResourceException e) {
				LOGGER.error("Failed to get the standing order alternate delivery dates: ",e);
			}
		}
		
		//Return JSON response.
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.writeValue(response.getOutputStream(), altDeliverDates);
		} catch (Exception e) {
			LOGGER.error("Failed to get the standing order alternate delivery dates: ",e);
		}
						
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException{
	
		FDStandingOrderAltDeliveryDate altDeliveryDate =parseRequestData( request, response, FDStandingOrderAltDeliveryDate.class,true );
		Date currentDate = Calendar.getInstance().getTime();
		HttpSession session = request.getSession();
		CrmAgentModel agent = CrmSession.getCurrentAgent(session);
		if(null != altDeliveryDate){
			try {
				if(null == altDeliveryDate.getId()){
					altDeliveryDate.setCreatedTime(currentDate);
					altDeliveryDate.setCreatedBy(agent.getUserId());
					altDeliveryDate.setModifiedTime(currentDate);
					altDeliveryDate.setModifiedBy(agent.getUserId());
					FDStandingOrdersManager.getInstance().addStandingOrderAltDeliveryDate(altDeliveryDate);
				}else{
					altDeliveryDate.setModifiedTime(currentDate);
					altDeliveryDate.setModifiedBy(agent.getUserId());
					FDStandingOrdersManager.getInstance().updateStandingOrderAltDeliveryDate(altDeliveryDate);
				}
			} catch (FDResourceException e) {
				LOGGER.error("Failed to save the standing order alternate delivery date");
				//TODO: Handle it.
			}
		}
	}
	
	private void validate(FDStandingOrderAltDeliveryDate altDate){
		List<String> errors = new ArrayList<String>();
		if(null !=altDate){
			Date currentDate = Calendar.getInstance().getTime();
			if(null ==altDate.getOrigDate()){
				errors.add("Original delivery date can't be empty");
			}
			if(EnumStandingOrderAlternateDeliveryType.ALTERNATE_DELIVERY.getName().equals(altDate.getActionType()) && null == altDate.getAltDate() ){
				errors.add("Alternate delivery date can't be empty to change the standing order delivery");				
			}
			/*if(null == altDate.getStartDate()){
				errors.add("Start Date can't be empty");
			} else if(altDate.getStartDate().before(currentDate)){
				errors.add("Start Date shouldn't be earlier than today");
			}
			if(null == altDate.getEndDate()){
				errors.add("End Date can't be empty");
			} else if(null != altDate.getStartDate() && altDate.getEndDate().before(altDate.getStartDate())){
				errors.add("End Date shouldn't be earlier than Start Date");
			}*/
			if(null != altDate.getSoId() && !StringUtils.isNumeric(altDate.getSoId())){
				errors.add("SO Id should be an integer");
			}
			
		}
	}
	protected final static <T> T parseRequestData( HttpServletRequest request, HttpServletResponse response, Class<T> typeClass, boolean allowEmpty ) {
		T reqData = null;
		boolean  isMultipart = ServletFileUpload.isMultipartContent(request);
		if(isMultipart){
			File file;
			String filePath="";
			DiskFileItemFactory factory = new DiskFileItemFactory();
			ServletFileUpload uploadServlet = new ServletFileUpload(factory);
			try{
			List fileItems = uploadServlet.parseRequest(request);
			
			// Process the uploaded file items
			Iterator i = fileItems.iterator();  
			while ( i.hasNext () ) 
			{
				FileItem fi = (FileItem)i.next();
				if ( !fi.isFormField () )	
				{
		            // Get the uploaded file parameters
		            String fileName = fi.getName();
		            String contentType = fi.getContentType();
		            boolean isInMemory = fi.isInMemory();
		            long sizeInBytes = fi.getSize();
		            // Write the file
		            if( fileName.lastIndexOf("\\") >= 0 ){
		               file = new File( filePath + 
		               fileName.substring( fileName.lastIndexOf("\\"))) ;
		            }else{
		               file = new File( filePath + 
		               fileName.substring(fileName.lastIndexOf("\\")+1)) ;
		            }
		            fi.write( file ) ;
		            StandingOrderAlternateDatesParser parser = new StandingOrderAlternateDatesParser();
		            List<FDStandingOrderAltDeliveryDate> listAltDates =parser.parseFile(file);
		            if(parser.isParseSuccessful()){
		            	FDStandingOrdersManager.getInstance().addStandingOrderAltDeliveryDates(listAltDates);
		            }else{
		            	response.addHeader ("Content-Disposition","attachment;filename="+fileName);
		    			response.setContentType("application/Text"); 
		    			PrintWriter pw = response.getWriter();
		    			pw.write(parser.buildResponse());
		    			pw.flush();
		            }
				}
			}
		    }catch(Exception ex) {
		       LOGGER.error("Error while uploading/parsing the standing order alternate delivery dates:", ex);
		       //TODO: Handle it
			    }
		}
		else{
			String reqJson = request.getParameter( "data" );
			if(reqJson == null){
				reqJson = (String)request.getAttribute( "data" );
				if(reqJson != null){
					try {
						reqJson = URLDecoder.decode(reqJson, "UTF-8");
					} catch (UnsupportedEncodingException e) {
						sendError( response, 400, "Cannot decode request string");	// 400 Bad Request
					}				
				}
			}
			if ( reqJson == null ) {
				if ( allowEmpty ) {
					return null;
				}
				sendError( response, 400, "Empty request. Aborting" );	// 400 Bad Request
			}
			try {
				ObjectMapper mapper = new ObjectMapper();
				mapper.setDateFormat(df);
				reqData = mapper.readValue(reqJson, typeClass);
			} catch (IOException e) {
				sendError( response, 400, "Cannot read JSON" );	// 400 Bad Request
			}
			if ( reqData == null && !allowEmpty ) {
				sendError( response, 400, "Cannot read JSON" );	// 400 Bad Request
			}
		}
		return reqData;
	}

	protected static void sendError(HttpServletResponse response, int status, String message) {
		//
		try {
			response.getWriter().append(message);
			response.setStatus(status);
		} catch (IOException e) {
			LOGGER.error("Failed to append message to response", e);
		}
	}

	@Override
	protected void doDelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String id = request.getParameter( "id" );
		try {
			FDStandingOrdersManager.getInstance().deleteStandingOrderAltDeliveryDateById(id);
		} catch (FDResourceException e) {
			//TODO: Handle it.
		}
	}
}
