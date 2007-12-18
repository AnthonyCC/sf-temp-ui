<%@ page import="com.freshdirect.delivery.depot.*"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="com.freshdirect.delivery.*"%>

<%@ taglib uri="logic" prefix="logic" %>
<%@ taglib uri="delivery" prefix="dlv" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%@page contentType="text/html"%>
<html>
<head>
	<script type="text/javascript" src="../javascript/calendar.js"></script>
	<script type="text/javascript" src="../javascript/calendar-en.js"></script>
	<script type="text/javascript" src="../javascript/calendar-setup.js"></script>
	<title>Edit Location</title>
</head>

<link rel="stylesheet" href="../css/pc_ie.css">
<link rel="stylesheet" href="../css/calendar-blue.css">
<%
	String depotId = request.getParameter("depot_id");
	String locationId = request.getParameter("location_id");
%>
<body BGCOLOR="#FFFFFF">
<dlv:depotTag id="depot" depotId="<%=depotId%>">
<dlv:locationTag id="location" depotId="<%=depotId%>" locationId="<%=locationId%>">
<dlv:locationControllerTag depotId="<%=depotId%>" location="<%=location%>" actionName="edit_location" result="result">
<form name="location" method="POST" action="edit_location.jsp?depot_id=<%=depotId%>&location_id=<%=locationId%>">
	<%@ include file="/include/i_location_fields.jspf" %>
</form>
</dlv:locationControllerTag>
</dlv:locationTag>
</dlv:depotTag>
</body>
</html>
