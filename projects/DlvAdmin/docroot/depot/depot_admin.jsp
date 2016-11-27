<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%@page contentType="text/html"%>
<html>
<head>
	<title>Depot Admin</title>
</head>
<%@ page import="com.freshdirect.delivery.depot.*"%>
<%@ taglib uri="template" prefix="tmpl" %>
<%@ taglib uri="logic" prefix="logic" %>
<%@ taglib uri="delivery" prefix="dlv" %>
<link rel="stylesheet" href="../css/pc_ie.css">

<body BGCOLOR="#FFFFFF">
<dlv:allDepotTag id="depots">
<table width="800" cellpadding="0" cellspacing="1" border="0">
		<tr valign="bottom">
			<td width="800" colspan="13"><img src="../images/urban-highway2.jpg" width="129" height="82" border="0" alt="Urban"><img src="../images/logo_sm.gif" width="130" height="32" border="0" alt="FreshDirect" hspace="10" vspace="2"></td>
		</tr>
		<tr>
			<td width="800" bgcolor="#FF9933" class="text12wbold"  colspan="13"><font class="space2pix"><BR></font>&nbsp;&nbsp;&nbsp;&nbsp;Depots<br><font class="space2pix"><BR></font></td>
		</tr>
		<tr>
			<td width="800" colspan="13"><font class="space8pix"><br></font></td>
		</tr>
		
		<logic:iterate id="depot" collection="<%= depots %>" type="com.freshdirect.delivery.depot.DlvDepotModel">
			<tr>
				<td>
					<%=depot.getName()%>
				</td>
				<td>
					<a href="edit_depot.jsp?depot_id=<%=depot.getPK().getId()%>"><b>EDIT</b></a>&nbsp;&nbsp;<a href="delete_depot.jsp?depot_id=<%=depot.getPK().getId()%>"><b>DELETE</b></a>
				</td>
				<td>
					<a href="list_locations.jsp?depot_id=<%=depot.getPK().getId()%>"><b>LOCATIONS</b></a>
				</td>
			</tr>
			<tr>
				<td width="800" colspan="13"><font class="space8pix"><br></font></td>
			</tr>
		</logic:iterate>
		
		<tr>
			<td bgcolor="c00cc3d" colspan="3"></td>
		</tr>
		<tr>
			<table cellpadding="0" cellspacing="0" border="0"> 
				<tr valign="bottom">
					<td><input type="button" value="Go Back" onclick="javascript:window.location='../app'" class="text12wbold" style="background-color:669933;color:ffffff"></td>
					<td><font class="space8pix">&nbsp;&nbsp;</font></td>
					<td><input type="button" value="Add Depot" onclick="javascript:window.location='add_depot.jsp'" class="text12wbold" style="background-color:669933;color:ffffff"></td>
					<td><font class="space8pix">&nbsp;&nbsp;</font></td>
				</tr>
			</table>		
</table>
</dlv:allDepotTag>
</body>
</html>
