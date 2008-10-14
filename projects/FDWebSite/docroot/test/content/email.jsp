<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*'%>
<%@ page import='com.freshdirect.fdstore.attributes.*' %>
<%@ page import='com.freshdirect.customer.*'%>
<%@ page import='com.freshdirect.*'%>
<%@ page import='com.freshdirect.fdstore.*'%>
<%@ page import='com.freshdirect.fdstore.FDReservation'%>
<%@ page import='com.freshdirect.fdstore.content.*'%>
<%@ page import='com.freshdirect.fdstore.promotion.*'%>
<%@ page import='com.freshdirect.webapp.util.JspMethods' %>
<%@ page import='com.freshdirect.webapp.util.*' %>
<%@ page import='com.freshdirect.fdstore.FDStoreProperties' %>
<%@ page import='java.text.*' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='oscache' prefix='oscache' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<html>
<head>
<title>FreshDirect - President's Picks</title>
<link rel="stylesheet" href="http://www.freshdirect.com/assets/css/pc_ie.css" type="text/css">
</head>
<%
Map params = new HashMap();
params.put("baseUrl", "http://www.freshdirect.com");
%>
<body>
	<fd:IncludeMedia name="/media/editorial/picks/pres_picks/pres_picks.ftl" parameters="<%=params%>"/>
	<%-- //Featured Products moved into Include --%>
	<%
	//setup to allow include in PP
        Image groDeptImage = null;
		boolean isDepartment = false;
		String deptId = "gro";



		ContentNodeModel currentFolder = null;
	%>

				<%@ include file="/includes/layouts/i_featured_products_picks.jspf" %>
				<fd:IncludeMedia name="/media/editorial/picks/pres_picks/pres_picks_footer.ftl" parameters="<%=params%>"/>
			
		

<%-- //END Featured Products --%>
</body>	
</html>
