<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%@page contentType="text/html"%>
<html>
<head>
	<title>Depot Detail</title>
</head>
<%@ page import="com.freshdirect.delivery.depot.*"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ taglib uri="template" prefix="tmpl" %>
<%@ taglib uri="logic" prefix="logic" %>
<%@ taglib uri="delivery" prefix="dlv" %>
<%@ include file="/css/pc_ie.css" %>
<%
	String depotId = request.getParameter("depot_id");
	SimpleDateFormat sf = new SimpleDateFormat("MM/dd/yyyy");

%>
<body BGCOLOR="#FFFFFF">
<dlv:depotTag id="depot" depotId="<%=depotId%>">
<table width="800" cellpadding="0" cellspacing="1" border="0">
		<tr valign="bottom">
			<td width="800" colspan="13"><img src="../images/urban-highway2.jpg" width="129" height="82" border="0" alt="Urban"><img src="../images/logo_sm.gif" width="130" height="32" border="0" alt="FreshDirect" hspace="10" vspace="2"></td>
		</tr>
		
		<tr>
			<td width="800" bgcolor="#FF9933" class="text12wbold"  colspan="13"><font class="space2pix"><BR></font>&nbsp;&nbsp;&nbsp;&nbsp;<%=depot.getName()%>&nbsp;Depot<br><font class="space2pix"><BR></font></td>
		</tr>
		<tr>
			<td width="800" colspan="13"><font class="space8pix"><br></font></td>
		</tr>
		<tr bgcolor="c00cc3d">
			<td class="text12wbold" align="center">Facility</td>
			<td class="text12wbold" align="center">Address1</td>
			<td class="text12wbold" align="center">Start Date</td>
			<td class="text12wbold" align="center">End Date</td>
			<td class="text12wbold" align="center">Action</td>
		</tr>
		<logic:iterate id="location" collection="<%= depot.getLocations() %>" type="com.freshdirect.delivery.depot.DlvLocationModel">
			<tr>
				<td><b><%=location.getFacility()%></b></td>
				<td><b><%=location.getAddress().getAddress1()%></b></td>
				<td align="center"><b><%=sf.format(location.getStartDate())%></b></td>
				<td align="center"><b><%=sf.format(location.getEndDate())%></b></td>
				<td align="center"><b><a href="edit_location.jsp?depot_id=<%=depotId%>&location_id=<%=location.getPK().getId()%>">EDIT</a> <a href="delete_location.jsp?depot_id=<%=depotId%>&location_id=<%=location.getPK().getId()%>">DELETE</a></b></td>
			</tr>
			<tr>
				<td><font class="space8pix"><br></font></td>
			</tr>
		</logic:iterate>
		<tr>
			<td bgcolor="c00cc3d" colspan="13"></td>
		</tr>
		<tr>
			<table cellpadding="0" cellspacing="0" border="0">
				<tr valign="bottom">
					<td><input type="button" value="Go Back" onclick="javascript:window.location='depot_admin.jsp'" class="text12wbold" style="background-color:669933;color:ffffff"></td>
					<td><font class="space8pix">&nbsp;&nbsp;</font></td>
					<td><input type="button" value="Add Location" onclick="javascript:window.location='add_location.jsp?depot_id=<%=depotId%>'" class="text12wbold" style="background-color:669933;color:ffffff"></td>
					<td><font class="space8pix">&nbsp;&nbsp;</font></td>
				</tr>
			</table>		
</table>
</dlv:depotTag>
</body>
</html>
