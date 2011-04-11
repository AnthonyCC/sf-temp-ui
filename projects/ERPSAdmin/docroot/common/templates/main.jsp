<%@ taglib uri='template' prefix='tmpl' %>
<%@ include file="/common/css/popcalendar.css" %>
<html>
<head>
    <title><tmpl:get name='title'/></title>

<link rel="stylesheet" href="/ERPSAdmin/common/css/erpsadmin.css" type="text/css">
    
    <script language="Javascript" type="text/javascript">
		<%@ include file='/common/includes/popcalendar.js'%>
    </script>

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
