<%@ page import='java.util.*' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri="fd-data-potatoes" prefix="potato" %>
<fd:CheckLoginStatus guestAllowed='true' /><%
	String pageTemplate = "/common/template/no_nav_html5.jsp";
	
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
<tmpl:insert template="<%=pageTemplate %>">
	<tmpl:put name='title' direct='true'>Product Request HTTP/S Tests</tmpl:put>
	
	<tmpl:put name='extraJs' direct='true'>
	</tmpl:put>
	
	<tmpl:put name='content' direct='true'>
		<fd:IncludeMedia name="/media/testing/product_request_https.ftl" parameters="<%=params%>" withErrorReport="false" />
	</tmpl:put>
	
	<tmpl:put name="jsmodules">
		<%@ include file="/common/template/includes/i_jsmodules.jspf" %>
		<jwr:script src="/fdmodules.js"  useRandomParam="false" />
		<jwr:script src="/fdcomponents.js"  useRandomParam="false" />
		
		<jwr:script src="/pdp.js"  useRandomParam="false" />
	</tmpl:put>
	
	<tmpl:put name="soytemplates" direct="true">
		<soy:import packageName="pdp" />
		<soy:import packageName="browse" />
	</tmpl:put>
</tmpl:insert>