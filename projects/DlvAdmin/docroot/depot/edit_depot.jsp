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
<dlv:depotControllerTag depotId="<%=depotId%>" />
<dlv:depotTag id="depot" depotId="<%=depotId%>">
<dlv:depotRegionTag>
<table width="800" cellpadding="0" cellspacing="1" border="0">
	<form name="edit_depot" method="POST" action="edit_depot.jsp?depot_id=<%=depotId%>">
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
						<td><input type="text" size="20" name="depot_name" class="text10" value="<%=depot.getName()%>"></td>
					</tr>
					<tr>
						<td align="right"><b>Registration Code: </b></td>
						<td><input type="text" size="20" name="registration_code" class="text10" value="<%=depot.getRegistrationCode()%>"></td>
					</tr>
					<tr>
						<td align="right"><b>Depot Code: </b></td>
						<td><input type="text" size="20" name="depot_code" class="text10" value="<%=depot.getDepotCode()%>"></td>
					</tr>
                    <tr>
                        <td align="right"><b>Require Employee ID for Registration</b></td>
                        <td><input type="checkbox" name="requireEmployeeId" <%= depot.getRequireEmployeeId()?"checked":"" %>></td>
                    </tr>
					<tr>
                        <td align="right"><b>Pickup Location</b></td>
                        <td><input type="checkbox" name="pickup" <%= depot.isPickup()?"checked":"" %>></td>
                    </tr>
					<tr>
                        <td align="right"><b>Corporate Depot</b></td>
                        <td><input type="checkbox" name="corporateDepot" <%= depot.isCorporateDepot()?"checked":"" %>></td>
                    </tr>
					<tr>
                        <td align="right" <%=depot.isDeactivated() ? "style='color:red;'" : ""%>><b>Deactivated</b></td>
                        <td><input type="checkbox" name="deactivated" <%= depot.isDeactivated()?"checked":"" %>></td>
                    </tr>
                    <tr>
						<td align="right"><b>Customer Service Email: </b></td>
						<td><input type="text" size="30" name="cust_serv_email" class="text10" value="<%=depot.getCustomerServiceEmail()%>"></td>
					</tr>
					<tr>
						<td align="right"><b>Region: </b></td>
						<td>
							<select name="region_id">
								<option value="">Please Select</option>
								<logic:iterate id="region" collection="<%= regions %>" type="com.freshdirect.delivery.model.DlvRegionModel">
									<option value="<%=region.getPK().getId()%>" <%=depot.getRegionId().equals(region.getPK().getId()) ? "selected" : ""%>><%=region.getName()%></option>
								</logic:iterate>
							</select>
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
					<td><input type="submit" value="Update Depot" class="text12wbold" style="background-color:669933;color:ffffff"></td>
					<td><font class="space8pix">&nbsp;&nbsp;</font></td>
				</tr>
			</table>
		</tr>
		<input type="hidden" name="action" value="edit_depot">
	</form>			
</table>
</dlv:depotRegionTag>
</dlv:depotTag>
</body>
</html>
