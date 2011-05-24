<%@ page import='com.freshdirect.fdstore.FDStoreProperties' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%
	String UA = request.getHeader("User-Agent");
	if (UA != null) {
		UA = UA.toLowerCase();
	}

	//default to iphone page, this should really be a 'mobile' page
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
		//iphone
		if (UA.indexOf("iphone;") >= 0) {
			params.put("deviceTypeName", "iPhone");
			params.put("isIphone", "true");
			landingPage = FDStoreProperties.getIphoneLandingPage();
		}
		//ipod
		if (UA.indexOf("ipod;") >= 0) {
			params.put("deviceTypeName", "iPod Touch");
			params.put("isIpod", "true");
			landingPage = FDStoreProperties.getIphoneLandingPage();
		}
		//android
		if (UA.indexOf("android") >= 0) {
			params.put("deviceTypeName", "Android");
			params.put("isAndroid", "true");
			landingPage = FDStoreProperties.getAndroidLandingPage();
		}

	//capture query string so we can use it in ftl
		if (request.getQueryString() != null) {
			params.put("qString", request.getQueryString()+"&noMobile=TRUE");
		}else{
			params.put("qString", "noMobile=TRUE");
		}
%>
<fd:IncludeMedia name="<%=landingPage%>" parameters="<%=params%>" withErrorReport="false" />

