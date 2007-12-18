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
</body>	
</html>
