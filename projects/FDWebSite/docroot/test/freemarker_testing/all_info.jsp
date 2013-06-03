<%@ taglib uri='freshdirect' prefix='fd' %>
<%-- <fd:CheckLoginStatus guestAllowed='true' /> --%>
<%
	Map params = new HashMap();

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

	String baseUrl = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort();
	params.put("baseUrl", baseUrl);
%>
<fd:IncludeMedia name="/media/testing/all_info.ftl" parameters="<%=params%>" withErrorReport="false" />