package com.freshdirect.webapp.ajax.product;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.ejb.CreateException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspFactory;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.freshdirect.ErpServicesProperties;
import com.freshdirect.ecommerce.data.product.ProductRequestData;
import com.freshdirect.fdstore.FDEcommProperties;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.customer.ejb.FDCustomerManagerHome;
import com.freshdirect.fdstore.customer.ejb.FDCustomerManagerSB;
import com.freshdirect.fdstore.ecomm.gateway.CustomerInfoService;
import com.freshdirect.fdstore.request.FDProductRequest;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionError;
import com.freshdirect.webapp.ajax.standingorder.ManageStandingOrderServlet.HttpErrorResponse;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;
import com.freshdirect.webapp.taglib.fdstore.SessionName;

/*
 * Based on: RequestAProductNew.java, request_product.jsp, i_request_product_dept_cat_map.jspf
 * 
 * */

public class ProductRequestServlet extends HttpServlet {

	private static final Logger LOGGER = LoggerFactory.getInstance(ProductRequestServlet.class);
	private static final long serialVersionUID = -2721193868485382061L;
	public static final String STOREDPRODUCTREQUESTS = "storedProductRequests";
	
	/* errors */
	public static final String ERR_NOCUSTOMERID = "ERR_NOCUSTOMERID";
	public static final String ERR_NOCUSTOMERID_MSG = "Customer Id is required.";
	public static final String ERR_NOREQUEST = "ERR_NOREQUEST";
	public static final String ERR_NOREQUEST_MSG = "Please enter at least one product request.";
	public static final String ERR_NOSAVE = "ERR_NOSAVE";
	public static final String ERR_NOSAVE_MSG = "Error saving product request(s).";
	

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		Map<String, HashMap<String, String>> returnData = getCatDeptMap();
		try {
			writeResponseData(response, returnData);
		} catch (HttpErrorResponse e) {
			LOGGER.error("Unable to fetch Product Request Data");
			throw new ServletException(e);
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		Map<String, Object> returnData = new HashMap<String, Object>();
		String action = request.getParameter("action");
		FDSessionUser user = (FDSessionUser) request.getSession().getAttribute(SessionName.USER);
		String customerId = null;

		JspFactory factory = JspFactory.getDefaultFactory();
		PageContext pageContext = factory.getPageContext(this, request, response, null, true, JspWriter.DEFAULT_BUFFER, true);

		returnData.put("SUCCESS", false); //default to failure
		
		if (user != null && user.getIdentity() != null) {
			customerId = user.getIdentity().getErpCustomerPK();
		}

 		List<FDProductRequest> productRequests = getProductRequests(request, customerId);
 		
		if (customerId != null) { //custId is required for prodReqs
			if ("requestProducts".equalsIgnoreCase(action)){
		 		if (productRequests.size() > 0) {
					try {
						FDCustomerManager.storeProductRequest(productRequests);
					} catch (FDResourceException e) {
			 			ProductRequestServlet.addError(returnData, new ActionError(ProductRequestServlet.ERR_NOSAVE, ProductRequestServlet.ERR_NOSAVE_MSG));
						LOGGER.error("Error saving Product Request(s)...");
						LOGGER.error("Action: " + action + " productRequests: " + productRequests);
						e.printStackTrace();
					}
					
					returnData.put("SUCCESS", true);
					
					//clear any previously saved items as well
					pageContext.getSession().removeAttribute(STOREDPRODUCTREQUESTS);
					
		 		} else {
		 			ProductRequestServlet.addError(returnData, new ActionError(ProductRequestServlet.ERR_NOREQUEST, ProductRequestServlet.ERR_NOREQUEST_MSG));
		 		}
		 		
		 	}
		} else {
			ProductRequestServlet.addError(returnData, new ActionError(ProductRequestServlet.ERR_NOCUSTOMERID, ProductRequestServlet.ERR_NOCUSTOMERID_MSG));
			
			pageContext.getSession().setAttribute(STOREDPRODUCTREQUESTS, productRequests);
		}
		
		try {
			writeResponseData(response, returnData);
		} catch (HttpErrorResponse e) {
			LOGGER.error("Unable to save Product Request Data");
			throw new ServletException(e);
		}
	}
	
	private List<FDProductRequest> getProductRequests(HttpServletRequest request, String customerId) {		
 		String[][] productReq=new String[3][4];
 		List<FDProductRequest> productRequests=new ArrayList<FDProductRequest>(3);
 		
 		for(int row=0;row<3;row++) {
	 		productReq[row][0]=request.getParameter("cat_prod"+(row+1));
	 		//productReq[row][1]=request.getParameter("subCategory"+(row+1));
	 		productReq[row][1]=request.getParameter("brandParams_prod"+(row+1));
	 		productReq[row][2]=request.getParameter("descrip_prod"+(row+1));
	 		productReq[row][3]=request.getParameter("dept_prod"+(row+1));
	 		
	 		//check for defaults and clear them to make save fail
	 		if ("Brand".equals(productReq[row][1])) { productReq[row][1] = ""; }
	 		if ("Description".equals(productReq[row][2])) { productReq[row][2] = ""; }
	 		
	 		if(isValued(productReq[row][1])||isValued(productReq[row][2])) {
	 			productRequests.add(getProductRequest(productReq[row][0], productReq[row][1], productReq[row][2], productReq[row][3], customerId)) ;
	 		}
 		}
		return productRequests;
	}
	
    private FDProductRequest getProductRequest(String cat, String subCat, String prodName, String dept, String customerId) {
    	
    	FDProductRequest prodReq=new FDProductRequest();
    	prodReq.setCategory(cat);
    	prodReq.setSubCategory(subCat);
    	prodReq.setProductName(prodName);
    	prodReq.setDept(dept);
    	prodReq.setCustomerId(customerId);
    	
    	return prodReq;
    	
    }
    
    private boolean isValued(String input) {
    	if(input==null || "".equals(input.trim())) {
    		return false;
    	} else {
    		return true;
    	}
    }
	
	public final static void addError(Map<String, Object> data, ActionError errMsg) {
		List<ActionError> errors = (data.containsKey("ERRORS")) ? (List<ActionError>)data.get("ERRORS") : new ArrayList<ActionError>();
		errors.add(errMsg);
		data.put("ERRORS", errors);
	}

	//fetch all data and check for validity
	public final static Map<String, HashMap<String, String>> getCatDeptMap() {
		/* 
		 * ID: [dID|cID || dID || cID] {	- ID, not DEPTID||CATID (since MAP doesn't have those in DB)
		 * 	TYPE: [MAP || DEPT || CAT],
		 * 	NAME: [NAME],					- null for MAP
		 * 	ID: [DEPTID || CATID]			- null for MAP
		 * }
		 * 
		 * */
		HashMap<String, HashMap<String, String>> finalMap = new HashMap<String, HashMap<String, String>>();
		
		try {			


			List<HashMap<String, String>> deptsList;
			List<HashMap<String, String>> catsList;
			List<HashMap<String, String>> mappingList;
			//sb returns all, including obsolete, so clean it up
			if (FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.FDCustomerInfo)) {
				ProductRequestData productRequestData = CustomerInfoService.getInstance().productRequestFetchAll();
				deptsList = cleanObsolete(productRequestData.getDepartments());
				catsList = cleanObsolete(productRequestData.getCategories());
				mappingList = cleanObsolete(productRequestData.getMapping());
			} else {
				Context ctx = getInitialContext();
				FDCustomerManagerHome home = (FDCustomerManagerHome) ctx.lookup("freshdirect.fdstore.CustomerManager");
				FDCustomerManagerSB sb = home.create();
				deptsList = cleanObsolete(sb.productRequestFetchAllDepts());
				catsList = cleanObsolete(sb.productRequestFetchAllCats());
				mappingList = cleanObsolete(sb.productRequestFetchAllMappings());
			}
			//create a data structure we can use easily for lookups (i.e. ID={DATA})
			HashMap<String, HashMap<String, String>> deptsMap = listToMap(deptsList, "ID");
			HashMap<String, HashMap<String, String>> catsMap = listToMap(catsList, "ID");
			
			//iterate over mappingList and add results to finalMap
			Iterator<HashMap<String, String>> mappingListI = mappingList.iterator();
			while (mappingListI.hasNext()) {
				HashMap<String, String> curMap = (HashMap<String, String>)mappingListI.next();

				Set<Entry<String, String>> curMapSet = curMap.entrySet();
				Iterator<Entry<String, String>> curMapSetI = curMapSet.iterator();

				String curDeptId = null;
				String curCatId = null;
				
				while(curMapSetI.hasNext()){
					Map.Entry<String, String> me = (Map.Entry<String, String>)curMapSetI.next();
					
					if ("DEPTID".equals(me.getKey())) {
						curDeptId = me.getValue();
					}
					
					if ("CATID".equals(me.getKey())) {
						curCatId = me.getValue();
					}
				}
				
				if (curDeptId != null && deptsMap.containsKey(curDeptId) && curCatId != null && catsMap.containsKey(curCatId)) {
					HashMap<String, String> tempDataMap = new HashMap<String, String>();
					
					/* because the ids are NOT unique between dept and cat, they need prefixes.
					 * "d"+ID for dept ids, and "c"+ID for cats
					 * this could be DEPTID and CATID, but then it'd be difficult to debug the mapping since it only has IDs
					 * */
					
					//mapping data
					tempDataMap.put("type", "MAP");
					tempDataMap.put("name", null);
					tempDataMap.put("id", null);
					finalMap.put("d"+curDeptId+"|"+"c"+curCatId, tempDataMap);
					
					//reset
					tempDataMap = new HashMap<String, String>();

					//dept data
					tempDataMap.put("type", "DEPT");
					tempDataMap.put("name", deptsMap.get(curDeptId).get("NAME"));
					tempDataMap.put("id", deptsMap.get(curDeptId).get("DEPTID"));
					finalMap.put("d"+curDeptId, tempDataMap);
					
					//reset
					tempDataMap = new HashMap<String, String>();

					//dept data
					tempDataMap.put("type", "CAT");
					tempDataMap.put("name", catsMap.get(curCatId).get("NAME"));
					tempDataMap.put("id", catsMap.get(curCatId).get("CATID"));
					finalMap.put("c"+curCatId, tempDataMap);
				} else {
					LOGGER.debug("Missing Dept or Cat info: "+curMap);
				}
			}
		} catch (NamingException e) {
			LOGGER.error("Unable to fetch Product Request Data (Cat/Dept Map)");
			e.printStackTrace();
		} catch (RemoteException e) {
			LOGGER.error("Unable to fetch Product Request Data (Cat/Dept Map)");
			e.printStackTrace();
		} catch (CreateException e) {
			LOGGER.error("Unable to fetch Product Request Data (Cat/Dept Map)");
			e.printStackTrace();
		} catch (FDResourceException e) {
			LOGGER.error("Unable to fetch Product Request Data (Cat/Dept Map)");
			e.printStackTrace();
		}

		return finalMap;
	}
	
	/* take List<HashMap<String, String>> and create HashMap<String[KEY], HashMap<String, String>[ORIG DATA]> */
	public final static HashMap<String, HashMap<String, String>> listToMap(List<HashMap<String, String>> inList, String mapKey) {
		String mapKeyToUse = "ID"; //default
		if (!"".equals(mapKey) && mapKey != null) {
			mapKeyToUse = mapKey;
		}
		HashMap<String, HashMap<String, String>> dataMap = new HashMap<String, HashMap<String, String>>();
		
		Iterator<HashMap<String, String>> inListI = inList.iterator();
		while (inListI.hasNext()) {
			HashMap<String, String> curMap = (HashMap<String, String>)inListI.next();

			Set<Entry<String, String>> mapSet = curMap.entrySet();
			Iterator<Entry<String, String>> mapSetI = mapSet.iterator();

			while(mapSetI.hasNext()){
				Map.Entry<String, String> me = (Map.Entry<String, String>)mapSetI.next();
				if ( (mapKeyToUse).equals(me.getKey()) ) {
					dataMap.put(me.getValue(), curMap);
				}
			}
		}
		
		return dataMap;
	}
	
	/* feed in a List with obsolete(s), returns a clean (minus obsolete(s)) List */
	public final static List<HashMap<String, String>> cleanObsolete(List<HashMap<String, String>> uncleanList) {
		List<HashMap<String, String>> cleanList = new ArrayList<HashMap<String, String>>();

		Iterator<HashMap<String, String>> uncleanListI = uncleanList.iterator();
		

		while (uncleanListI.hasNext()) {
			HashMap<String, String> curMap = (HashMap<String, String>)uncleanListI.next();

			Set<Entry<String, String>> mapSet = curMap.entrySet();
			Iterator<Entry<String, String>> mapSetI = mapSet.iterator();
			
			while(mapSetI.hasNext()){
				Map.Entry<String, String> me = (Map.Entry<String, String>)mapSetI.next();
				//if ("OBSOLETE".equals(me.getKey()) && !"X".equals(me.getValue())) {
					cleanList.add(curMap);
				//}
			}
		}
		
		return cleanList;
	}

	static public Context getInitialContext() throws NamingException {
		Hashtable<String, String> h = new Hashtable<String, String>();
		h.put(Context.INITIAL_CONTEXT_FACTORY, "weblogic.jndi.WLInitialContextFactory");
		h.put(Context.PROVIDER_URL, ErpServicesProperties.getProviderURL());
		return new InitialContext(h);
	}
	
	public final static void configureJsonResponse(HttpServletResponse response) {
		// Set common response properties for JSON response
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Pragma", "no-cache");
		response.setContentType("application/json");
		response.setLocale(Locale.US);
		response.setCharacterEncoding("ISO-8859-1");
	}

	protected final static <T> void writeResponseData(HttpServletResponse response, T responseData) throws HttpErrorResponse {

		// Set response parameters
		configureJsonResponse(response);

		// Serialize data to JSON and write out the result
		try {

			Writer writer = new StringWriter();
			new ObjectMapper().writeValue(writer, responseData);

			ServletOutputStream out = response.getOutputStream();
			String responseStr = writer.toString();

			// LOG.debug( "Generated response data: " + responseStr );

			out.print(responseStr);

			out.flush();

		} catch (JsonGenerationException e) {
			returnHttpError(500, "Error writing JSON response", e); // 500 Internal Server Error
		} catch (JsonMappingException e) {
			returnHttpError(500, "Error writing JSON response", e); // 500 Internal Server Error
		} catch (IOException e) {
			returnHttpError(500, "Error writing JSON response", e); // 500 Internal Server Error
		} catch (Exception e) {
			returnHttpError(500, "Error writing JSON response", e); // 500 Internal Server Error
		}
	}
	
	public final static void returnHttpError(int errorCode, String errorMessage, Throwable e) throws HttpErrorResponse {
		LOGGER.error(errorMessage, e);
		throw new HttpErrorResponse(errorCode);
	}

}