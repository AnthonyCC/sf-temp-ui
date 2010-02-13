<%@ taglib uri='freshdirect' prefix='fd' %>
<%
	Map params = new HashMap();

	String baseUrl = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort();
	params.put("baseUrl", baseUrl);/*
	if we're on the email.jsp, set the product base urls to PROD
	set true in email.jsp, false in newsletter.jsp
	*/
	boolean emailpage = true;
	params.put("emailpage", emailpage);

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

%>
<fd:IncludeMedia name="/media/testing/graphics_testing/email.ftl" parameters="<%=params%>" withErrorReport="true" />
<% //no dynamic images in the footer, so we'll just include the normal one... %>
<fd:IncludeMedia name="/media/editorial/picks/pres_picks/pres_picks_footer.ftl" parameters="<%=params%>" withErrorReport="true" />