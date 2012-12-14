<!DOCTYPE html>
<%@ taglib uri='template' prefix='tmpl' %>
<html>
<head>
 	<meta charset="UTF-8" />
 	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
    <title><tmpl:get name='title'/></title>
	<link rel="stylesheet" href="/ERPSAdmin/common/css/erpsadmin.css" type="text/css" />
   <tmpl:get name='extracss'/>
	<%@ include file='/common/jquery.jspf' %>
</head>
<body>
<%
if(!request.isUserInRole("ErpsyAdminGrp")) {
%>
	<%@ include file='/common/templates/main1_readonly.jsp' %>
<%	
} else {
%>
	<%@ include file='/common/templates/main1.jsp' %>
<%}
%>

<div id="main">
   <tmpl:get name='leftnav'/>
	
	<div id="content">
		<tmpl:get name='content'/>
	</div>
</div>

</body>
</html>
