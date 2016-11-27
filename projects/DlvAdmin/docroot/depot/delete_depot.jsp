<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%@page contentType="text/html"%>
<html>
<head>
	<title>Update Depot</title>
</head>
<%@ page import="com.freshdirect.delivery.depot.*"%>
<%@ taglib uri="template" prefix="tmpl" %>
<%@ taglib uri="logic" prefix="logic" %>
<%@ taglib uri="delivery" prefix="dlv" %>
<link rel="stylesheet" href="../css/pc_ie.css">

<%
	String depotId = request.getParameter("depot_id");
%>

<body BGCOLOR="#FFFFFF">
<dlv:depotControllerTag depotId="<%=depotId%>" redirectPage="depot_admin.jsp" />
<dlv:depotRegionTag>
<dlv:depotTag id="depot" depotId="<%=depotId%>">
<table width="800" cellpadding="0" cellspacing="1" border="0">
	<form name="edit_depot" method="POST" action="delete_depot.jsp?depot_id=<%=depotId%>">
		<tr valign="bottom">
			<td width="800" colspan="13"><img src="../images/urban-highway2.jpg" width="129" height="82" border="0" alt="Urban"><img src="../images/logo_sm.gif" width="130" height="32" border="0" alt="FreshDirect" hspace="10" vspace="2"></td>
		</tr>
		<tr>
			<td width="800" bgcolor="#FF9933" class="text12wbold"  colspan="13"><font class="space2pix"><BR></font>&nbsp;&nbsp;&nbsp;&nbsp;Add Depot<br><font class="space2pix"><BR></font></td>
		</tr>
		<tr>
			<td width="800" colspan="13"><font class="space8pix"><br></font></td>
		</tr>
		
		<tr>
			<td>
				<table>
					<tr>
						<td align="right"><b>Name: </b></td>
						<td><%=depot.getName()%></td>
					</tr>
					<tr>
						<td align="right"><b>Registration Code: </b></td>
						<td><%=depot.getRegistrationCode()%></td>
					</tr>
					<tr>
						<td align="right"><b>Depot Code: </b></td>
						<td><%=depot.getDepotCode()%></td>
					</tr>
                    <tr>
                        <td align="right"><b>Require Employee ID for Registration</b></td>
                        <td><%= depot.getRequireEmployeeId()?"YES":"NO" %></td>
                    </tr>
                    <tr>
						<td align="right"><b>Customer Service Email: </b></td>
						<td><%=depot.getCustomerServiceEmail()%></td>
					</tr>
					<tr>
						<td align="right"><b>Region: </b></td>
						<td>
							<logic:iterate id="region" collection="<%= regions %>" type="com.freshdirect.delivery.model.DlvRegionModel">
								<%if(depot.getRegionId().equals(region.getPK().getId())){%>
									<%=region.getName()%>
								<%}%>
							</logic:iterate>
						</td>
					</tr>
				</table>
			</td>	
		</tr>
		
		<tr>
			<td bgcolor="c00cc3d" colspan="2"></td>
		</tr>
		<tr>
			<table cellpadding="0" cellspacing="0" border="0">
				<tr valign="bottom">
					<td><input type="button" value="Go Back" onclick="javascript:window.location='depot_admin.jsp'" class="text12wbold" style="background-color:669933;color:ffffff"></td>
					<td><font class="space8pix">&nbsp;&nbsp;</font></td>
					<td><input type="submit" value="Delete Depot" class="text12wbold" style="background-color:669933;color:ffffff"></td>
					<td><font class="space8pix">&nbsp;&nbsp;</font></td>
				</tr>
			</table>
		</tr>
		<input type="hidden" name="action" value="delete_depot">
	</form>			
</table>
</dlv:depotTag>
</dlv:depotRegionTag>
</body>
</html>
