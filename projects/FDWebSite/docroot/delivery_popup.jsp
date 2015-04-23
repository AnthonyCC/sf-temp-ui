<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ page import='com.freshdirect.fdstore.FDDepotManager' %>
<%@ page import='com.freshdirect.delivery.depot.DlvDepotModel' %>
<%@ page import='com.freshdirect.delivery.depot.DlvLocationModel' %>
<%@ page import='java.util.*' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='logic' prefix='logic' %>

<fd:CheckLoginStatus id="user" />

<tmpl:insert template='/shared/template/large_pop_no_ads.jsp'>
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
	params.put("uri", request.getRequestURI());

	boolean fromZipCheck = "yes".equalsIgnoreCase(request.getParameter("zipCheck"));
	params.put("fromZipCheck", fromZipCheck);
	
	boolean isPopup = true;
	params.put("isPopup", isPopup);
	
	boolean isHamptons = "hamptons".equalsIgnoreCase(request.getParameter("location"));
	boolean isLbi = "lbi".equalsIgnoreCase(request.getParameter("location"));
	boolean isJerseyShore = "jersey_shore".equalsIgnoreCase(request.getParameter("location"));
	
	params.put("location", request.getParameter("location"));
	
	DlvDepotModel depotModel = FDDepotManager.getInstance().getDepot(request.getParameter("depotCode"));
	if (depotModel != null) {
		params.put("depotModel", depotModel);
		/* get location */
		%>
		<logic:iterate id="location" collection="<%= depotModel.getLocations() %>" type="com.freshdirect.delivery.depot.DlvLocationModel">
		<%
			if ((request.getParameter("locaId")).equals(location.getPK().getId())) {
				params.put("locationModel", location);
			}
		%>
		</logic:iterate>
	<% } %>
	
	<tmpl:put name='title' direct='true'>FreshDirect - Pickup</tmpl:put>
	<tmpl:put name='content' direct='true'>
		<fd:IncludeMedia name="/media/editorial/site_pages/delivery_info/pickup/main.ftl" parameters="<%=params%>" withErrorReport="true"/>
	</tmpl:put>
</tmpl:insert>
