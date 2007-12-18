<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%@page contentType="text/html"%>
<html>
<head>
	<script language="javascript" src="../include/popcalendar.js"></script>
	<title>Delete Location</title>
</head>
<%@ page import="com.freshdirect.delivery.depot.*"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="com.freshdirect.delivery.*"%>
<%@ taglib uri="template" prefix="tmpl" %>
<%@ taglib uri="logic" prefix="logic" %>
<%@ taglib uri="delivery" prefix="dlv" %>
<link rel="stylesheet" href="../css/pc_ie.css">
<link rel="stylesheet" href="../css/popcalendar.css">
<%
	String depotId = request.getParameter("depot_id");
	String locationId = request.getParameter("location_id");
	SimpleDateFormat sf = new SimpleDateFormat("MM-dd-yyyy"); 
	String method = request.getMethod();
	String redirect = "list_locations.jsp?depot_id="+depotId;
%>
<body BGCOLOR="#FFFFFF">
<dlv:depotTag id="depot" depotId="<%=depotId%>">
<dlv:locationTag id="location" depotId="<%=depotId%>" locationId="<%=locationId%>">
<dlv:locationControllerTag depotId="<%=depotId%>" location="<%=location%>" actionName="delete_location" redirectPage="<%=redirect%>" result="result">
<table width="800" cellpadding="0" cellspacing="1" border="0">
		<form name="location" method="POST" action="delete_location.jsp?depot_id=<%=depotId%>&location_id=<%=locationId%>">
		<tr valign="bottom">
			<td width="800" colspan="13"><img src="../images/urban-highway2.jpg" width="129" height="82" border="0" alt="Urban"><img src="../images/logo_sm.gif" width="130" height="32" border="0" alt="FreshDirect" hspace="10" vspace="2"></td>
		</tr>
		
		<tr>
			<td width="800" bgcolor="#FF9933" class="text12wbold"  colspan="13"><font class="space2pix"><BR></font>&nbsp;&nbsp;&nbsp;&nbsp;Edit Location from&nbsp;<%=depot.getName()%>&nbsp;Depot<br><font class="space2pix"><BR></font></td>
		</tr>
		<tr>
			<td width="800"><font class="space8pix"><br></font></td>
		</tr>
		<tr>
			<td width="800"><font style="color:red;font-size:12">Are you sure you want to delete following location</font></td>
		</tr>
		<tr>
			<td>
				<table>
					<tr>
						<td align="right"><b>Facility<b></td>
						<td><%=location.getFacility()%></td>
					</tr>
					<tr>
						<td align="right"><b>Start Date<b></td>
						<td><%=sf.format(location.getStartDate())%></td>
					</tr>
					<tr>
						<td align="right"><b>End Date<b></td>
						<td><%=sf.format(location.getEndDate())%></td>
					</tr>
					<%AddressModel address = location.getAddress();%>
					<tr>
						<td align="right"><b>Address1<b></td>
						<td><%=address.getAddress1()%></td>
					</tr>
					<tr>
						<td align="right"><b>Address2<b></td>
						<td><%=address.getAddress2()%></td>
					</tr>
					<tr>
						<td align="right"><b>City<b></td>
						<td><%=address.getCity()%></td>
						<td align="right"><b>State<b></td>
						<td><%=address.getState()%></td>
						<td align="right"><b>ZipCode<b></td>
						<td><%=address.getZipCode()%></td>
					</tr>
					<tr>
						<td align="right"><b>Zone Code</b></td>
						<td><%=location.getZoneCode()%></td>
					</tr>
					<tr>
						<td align="right"><b>Instructions</b></td>
						<td colspan="4"><%=location.getInstructions()%></td>
					</tr>
                    <tr>
                        <td align="right"><b>Delivery Charge Waived</b></td>
                        <td colspan="4"><%= location.getDeliveryChargeWaived()?"YES":"NO" %></td>
                    </tr>
				</table>
			</td>
		</tr>
		<tr>
			<td bgcolor="c00cc3d">
		</tr>
		<tr>
			<table cellpadding="0" cellspacing="0" border="0">
				<tr valign="bottom">
					<td><input type="button" value="Go Back" onclick="javascript:window.location='list_locations.jsp?depot_id=<%=depotId%>'" class="text12wbold" style="background-color:669933;color:ffffff"></td>
					<td><font class="space8pix">&nbsp;&nbsp;</font></td>
					<td><input type="SUBMIT" value="Delete Location" class="text12wbold" style="background-color:669933;color:ffffff"></td>
					<td><font class="space8pix">&nbsp;&nbsp;</font></td>
				</tr>
			</table>
			<input type="hidden" value="delete_location" name="action">
		</form>		
</table>
</dlv:locationControllerTag>
</dlv:locationTag>
</dlv:depotTag>
</body>
</html>
