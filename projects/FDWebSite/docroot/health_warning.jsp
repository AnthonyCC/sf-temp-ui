<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<fd:CheckLoginStatus/>
<%
	Map params = new HashMap();

	Enumeration keys = request.getParameterNames();
	while (keys.hasMoreElements()) {
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
<tmpl:insert template='/common/template/blank.jsp'>
	<tmpl:put name='title' direct='true'>FreshDirect - Alcohol Information</tmpl:put>
	<tmpl:put name='content' direct='true'>
		<fd:IncludeMedia name="/media/editorial/site_pages/health_warning.ftl" parameters="<%=params%>" withErrorReport="false" />
	</tmpl:put>
</tmpl:insert>