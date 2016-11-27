<%@page contentType="text/html"%>

<%@ page import="java.util.List" %>

<%@ taglib uri="logic" prefix="logic" %>
<%@ taglib uri="delivery" prefix="dlv" %>

<html>
<head>
	<title>Confirmation By Order</title>
	<link rel="stylesheet" href="css/pc_ie.css">
</head>
<body bgcolor="#ffffff" topmargin="0" leftmargin="0">
<table width="100%" cellpadding="0" cellspacing="0" border="0">
		<tr bgcolor="#FF9933">
			<td colspan="2"><font class="space2pix"><br></font>&nbsp;<a href="index.jsp" class="headerLink">HOME</a>&nbsp;&nbsp;&nbsp;<br><font class="space2pix"><br></font></td>
		</tr>
		<tr bgcolor="#c00cc3d" class="text12wbold">
			<td colspan="2"><FONT CLASS="space2pix">&nbsp;</FONT></td>
		</tr>
		<tr valign="bottom">
			<td><img src="images/urban-highway2.jpg" width="129" height="82" border="0" alt="Urban"><img src="images/logo_sm.gif" width="130" height="32" border="0" alt="FreshDirect" HSPACE="10" VSPACE="2">&nbsp;&nbsp;</TD>
			<td align="right"><h4>.:&nbsp;Order Lookup&nbsp;:. </h4></td>
		</tr>
		<tr>
			<td bgcolor="c00cc3d" colspan="2"><font class="space2pix">&nbsp;</font></td>
		</tr>	
</table><br><br>
<dlv:dlvSearchOrderController actionName="search" result="result">
<% List searchResults = (List) session.getAttribute("SEARCH_RESULTS"); 
   session.removeAttribute("SEARCH_RESULTS"); 
%>
<form method="POST" action="lookup_orders_by_address.jsp">
<table width="40%" cellpadding="0" cellspacing="0" border="0">
	<tr>
		<td align="right"><b>Date: &nbsp;&nbsp;</b></td>
		<td align="left"><input type="text" name="date" size="15" class="text10" value="<%=request.getParameter("date")%>"></td>
		<td><%if (result.hasError("dateProblem")){ %><font class="text11rbold"><%= result.getError("dateProblem").getDescription() %><%}%></td>
	</tr>
	<tr>
		<td align="right"><b>Address: &nbsp;&nbsp;</b></td>
		<td align="left"><input type="text" name="address" size="25" class="text10" value="<%=request.getParameter("address")%>"></td>
		<td><%if (result.hasError("missingAddress")){ %><font class="text11rbold"><%= result.getError("missingAddress").getDescription() %><%}%></td>
	</tr>
	<tr>
		<td align="right"><b>ZipCode: &nbsp;&nbsp;</b></td>
		<td align="left"><input type="text" name="zipcode" size="10" class="text10" value="<%=request.getParameter("zipcode")%>"></td>
		<td>
			<%if (result.hasError("missingZipcode")){ %><font class="text11rbold"><%= result.getError("missingZipcode").getDescription() %><%}%>
			<%if (result.hasError("incorrectZipcode")){ %><font class="text11rbold"><%= result.getError("incorrectZipcode").getDescription() %><%}%>
		</td>
	</tr>
	<tr>
		<td colspan="3"><br></td>
	</tr>
	<tr>
		<td align="right"><input type="submit" value="SEARCH" class="text12wbold" style="background-color:669933;color:ffffff"></td>
		<td>&nbsp;</td>
	</tr>
</table>
</form>
<%if(searchResults != null){%>
	<table>
		<tr bgcolor="#ff9933">
			<td align="center"><b>Order Number<b></td>
			<td align="center"><b>Stop Sequence</b></td>
			<td align="center"><b>Address</b></td>
			<td align="center"><b>Apartment</b></td>
			<td align="center"><b>Zipcode</b></td>
		</tr>
		<logic:iterate collection="<%=searchResults%>" id="info" type="com.freshdirect.customer.DlvSaleInfo" indexId="idx">
			<tr bgcolor="<%= idx.intValue() % 2 == 0 ? "#c00cc3d" : "#ffffff"%>">
				<td><%=info.getSaleId()%></td>
				<td><%=info.getStopSequence()%></td>
				<td><%=info.getAddress()%></td>
				<td><%=info.getApartment()%></td>
				<td><%=info.getZipcode()%></td>
			</tr>
		</logic:iterate>
	</table>
<%}%>
</dlv:dlvSearchOrderController>
</body>
</html>