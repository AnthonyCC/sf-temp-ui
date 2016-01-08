package com.freshdirect.webapp.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Category;

import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.logistics.delivery.dto.ScrubbedAddress;

/**
 * @author Aniwesh Vatsal
 *
 */
public class AddressScrubbingServlet extends HttpServlet {
	
	private static Category LOGGER = LoggerFactory.getInstance(AddressScrubbingServlet.class);
//	private final String [] INPUT_FILE_HEADER_MAPPING = {"CUSTOMER_ID","SCRUBBED_ADDRESS","ADDRESS","APARTMENT","CITY","STATE","ZIP","DELIVERY_TYPE","Street/HighRise","Residential/Commercial"};
	
	private final String [] OUTPUT_FILE_HEADER_MAPPING = {"CUSTOMER_ID","SCRUBBED_ADDRESS","ADDRESS","APARTMENT","CITY","STATE","ZIP","DELIVERY_TYPE","Street/HighRise","Residential/Commercial","SS_SCRUBBED_ADDRESS","RDI","Address Type","Status"};
				
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		Future futureTemp = null;
		if(request.getAttribute("future") !=null ){
			futureTemp =  (Future)request.getAttribute("future");	
		}else if(request.getSession().getAttribute("future") != null){
			futureTemp = (Future)request.getSession().getAttribute("future");
		}else{
			response.getWriter().print("Exception");
		}
		if(futureTemp != null && futureTemp.isDone()){
			response.getWriter().print("Completed");
			request.setAttribute("future", (Future)request.getAttribute("future"));
		}else{
				response.getWriter().print("NotCompleted");
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		LOGGER.debug("Inside doPost of AddressScrubbingServlet");
		String action="";
		if(request.getParameter("addressScrubbingAction") != null){
			action = (String)request.getParameter("addressScrubbingAction");
		}else if(request.getAttribute("addressScrubbingAction") != null ){
			action = (String)request.getAttribute("addressScrubbingAction");
		}
		
		if(action != null && action.equals("cancelscrubbing")){
			
			Future futureTemp = null;
			if(request.getAttribute("future") !=null ){
				futureTemp =  (Future)request.getAttribute("future");	
			}else if(request.getSession().getAttribute("future") != null){
				futureTemp = (Future)request.getSession().getAttribute("future");
			}
			
			if(futureTemp != null){
				if(!futureTemp.isDone()){
					futureTemp.cancel(true);
				}
				getServletContext().getRequestDispatcher("/scrubbingtool/address_scrubbing_tool.jsp").forward(request, response);
			}		
		}else{
			if(action != null && action.equals("scrubbingCompleted")){
				if(request.getAttribute("future") != null){
					request.setAttribute("future", (Future)request.getAttribute("future"));
				}
				getServletContext().getRequestDispatcher("/scrubbingtool/address_scrubbing_success.jsp").forward(request, response);
			}else{
				processCSV(request,response);
				getServletContext().getRequestDispatcher("/scrubbingtool/address_scrubbing_inprogress.jsp").forward(request, response);
			}
		}
		LOGGER.debug("Exit doPost of AddressScrubbingServlet");
	}
	
	/**
	 * @param request
	 * @param response
	 */
	private void processCSV(HttpServletRequest request,HttpServletResponse response) {
		LOGGER.debug("Inside processCSV");
		Map<String, ScrubbedAddress> addressInputMap = new HashMap<String, ScrubbedAddress>();
		List<ScrubbedAddress> csvAddrList = new ArrayList<ScrubbedAddress>();

		try {
			if (ServletFileUpload.isMultipartContent(request)) {
				DiskFileItemFactory factory = new DiskFileItemFactory();
				// files smaller than 5MB will be held in memory
				factory.setSizeThreshold(5000000);
				ServletFileUpload upload = new ServletFileUpload(factory);

				List<FileItem> list;
				try {
					list = upload.parseRequest(request);
					Iterator<FileItem> iter = list.iterator();
					String csv = null;
					String line = "";
					ScrubbedAddress address = null;
					int addressId = 1;
					while (iter.hasNext()) {
						FileItem param = (FileItem) iter.next();
						if (!param.isFormField()) {
							InputStream inputStream = param.getInputStream();
							BufferedReader reader = new BufferedReader(
									new InputStreamReader(inputStream));
							int counter = 0;
							while ((line = reader.readLine()) != null) {
								if (counter == 0) {
									counter++;
									continue;
								}
								address = buildScrubbedAddressReq(line,addressId);
								csvAddrList.add(address);
								addressInputMap.put("" + addressId, address);
								addressId++;
							}
							break;
						}
					}
					if (csvAddrList != null && !csvAddrList.isEmpty() && addressInputMap != null && addressInputMap.size() > 0) {
						AddressScrubbingProcessor scrubbingProcessor = new AddressScrubbingProcessor();
						Future future = scrubbingProcessor.processCSV(addressInputMap, csvAddrList);
						request.setAttribute("future", future);
						request.getSession().setAttribute("future", future);
					}
				} catch (FileUploadException e) {
					e.printStackTrace();
					LOGGER.error("Exception AddressScrubbingServlet 2 "+e.getMessage());
				} catch (IOException e) {
					e.printStackTrace();
					LOGGER.error("Exception AddressScrubbingServlet 1 "+e.getMessage());
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Exception AddressScrubbingServlet"+e.getMessage());
		}
		LOGGER.debug("exit processCSV");
	}
    /**
     * @param rec
     * @return
     */
    private ScrubbedAddress buildScrubbedAddressReq(String rec , int addressId){
    	ScrubbedAddress address = new ScrubbedAddress();
    	if(rec != null && rec.length() > 0 ){
    		String[] row = rec.split(",");
    		address.setId(""+addressId);
    		address.setCustomerId(row[0]);
    		address.setScrubbedAddress(row[1]);
    		address.setAddress1(row[2]);
    		address.setApartment(row[3]);
    		address.setCity(row[4]);
    		address.setState(row[5]);
    		address.setZipCode(row[6]);
    		address.setServiceType(row[7]);
    		address.setAddressType(row[8]);
    		address.setRdi(row[9]);
    	}
    	return address;
    }
    
}
