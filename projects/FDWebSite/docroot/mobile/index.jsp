<%@ page import='com.freshdirect.fdstore.FDStoreProperties' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%
	String UA = request.getHeader("User-Agent");
	String landingPage = FDStoreProperties.getIphoneLandingPage();

	Map params = new HashMap();

	//put in other params passed from page
		Enumeration keys = request.getParameterNames();  
		while (keys.hasMoreElements() ) {  
			String key = (String)keys.nextElement(); 

			//To retrieve a single value  
			String value = request.getParameter(key); 

			params.put(key, value);
		
			// If the same key has multiple values (check boxes)  
			String[] valueArray = request.getParameterValues(key);  
			 
			for(int i = 0; i > valueArray.length; i++){
				params.put(key+valueArray[i], valueArray[i]);
			}  
		}

	//put in baseUrl
		String baseUrl = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort();
		params.put("baseUrl", baseUrl);
		params.put("context", request.getRequestURI());

	//put in device type
		params.put("deviceTypeName", (UA.toLowerCase().indexOf("iphone;")>=0) ? "iPhone" : "iPod Touch");
		params.put("isIphone", (UA.toLowerCase().indexOf("iphone;")>=0));
		params.put("isIpod", (UA.toLowerCase().indexOf("ipod;")>=0));

	//capture query string so we can use it in ftl
		if (request.getQueryString() != null) {
			params.put("qString", request.getQueryString()+"&noMobile=TRUE");
		}else{
			params.put("qString", "noMobile=TRUE");
		}
%>
<fd:IncludeMedia name="<%=landingPage%>" parameters="<%=params%>" withErrorReport="false" />

