<%@ page import="com.freshdirect.delivery.depot.*"%>
<%@ page import="com.freshdirect.delivery.*"%>
<%@ page import="java.text.SimpleDateFormat"%>

<%@ taglib uri="logic" prefix="logic" %>
<%@ taglib uri="delivery" prefix="dlv" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%@page contentType="text/html"%>
<html>
<head>
	<script type="text/javascript" src="../javascript/calendar.js"></script>
	<script type="text/javascript" src="../javascript/calendar-en.js"></script>
	<script type="text/javascript" src="../javascript/calendar-setup.js"></script>
	<title>Add Location</title>
</head>
<link rel="stylesheet" href="../css/pc_ie.css">
<link rel="stylesheet" href="../css/calendar-blue.css">
<%
	String depotId = request.getParameter("depot_id");
%>
<body BGCOLOR="#FFFFFF">
<%DlvLocationModel location = new DlvLocationModel();%>
<dlv:locationControllerTag depotId="<%=depotId%>" location="<%=location%>" actionName="add_location" result="result">
<dlv:depotTag id="depot" depotId="<%=depotId%>">
<form name="location" method="POST" action="add_location.jsp?depot_id=<%=depotId%>">
	<%@ include file="/include/i_location_fields.jspf" %>
</form>		
</dlv:depotTag>
</dlv:locationControllerTag>
</body>
</html>
