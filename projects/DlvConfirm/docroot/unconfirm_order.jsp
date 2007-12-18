<%@page contentType="text/html"%>
<%@ taglib uri="delivery" prefix="dlv" %>

<%@ page import="com.freshdirect.customer.EnumSaleStatus"%>
<html>
<head><title>Delivery Confirmation</title>
<link rel="stylesheet" href="css/pc_ie.css">
</head>
<body bgcolor="#ffffff" topmargin="0" leftmargin="0">
<table width="100%" cellpadding="0" cellspacing="0" border="0">
		<tr bgcolor="#FF9933">
			<td width="1000" colspan="13"><font class="space2pix"><br></font>&nbsp;<a href="index.jsp" class="headerLink">HOME</a><br></font></td>
		</tr>
		<tr bgcolor="#c00cc3d" class="text12wbold">
			<td width="1000" colspan="13"><FONT CLASS="space2pix">&nbsp;</FONT></td>
		</tr>
		<tr valign="bottom">
			<td width="100%"><img src="images/urban-highway2.jpg" width="129" height="82" border="0" alt="Urban"><img src="images/logo_sm.gif" width="130" height="32" border="0" alt="FreshDirect" HSPACE="10" VSPACE="2"></TD>
		</tr>
		<tr>
			<td bgcolor="c00cc3d" width="100%"><font class="space2pix">&nbsp;</font></td>
		</tr>	
</table><br><br>
<dlv:unconfirmControllerTag id="saleInfo" result="errors">
<%if (errors.hasError("resource_error")){ %>
	<%@ include file="/include/i_error_messages.jspf" %>
<% } %>
<table width="100%" cellpading="0" cellspacing="0" border="0">
<form name="confirm_delivery" method="post">
	<tr>
		<td>
			<b>Enter Order Number: </b>&nbsp;&nbsp;
			<input type="text" size="20" class="text10" name="order_number" value='<%=(ORDER_NUMBER != null && !"".equals(ORDER_NUMBER)) ? ORDER_NUMBER: ""%>'>&nbsp;&nbsp;
			<input type="submit" value="Get Details" class="text12wbold" style="background-color:669933;color:ffffff" onclick="javascript:document.confirm_delivery.action.value='get_details'">
			<input type="hidden" name="action" value="">
			<input type="hidden" name="order_number" value="<%=ORDER_NUMBER%>">
		</td>
	</tr>
	<tr>
		<td bgcolor="c00cc3d" colspan="2"></td>
	</tr>
	<%if(saleInfo != null){%>
		<tr>
			<td><br><br>
				<table>
					<tr>
						<td><b>Order Nmber: </b></td><td><b><%=saleInfo.getSaleId()%></b></td>
					</tr>
					<tr>
						<td><b>Order Status: </b></td><td><b><%=saleInfo.getStatus().getName()%></td>
					</tr>
					<tr>
						<td><b>Stop Sequence: </b></td><td><b><%=saleInfo.getStopSequence()%></td>
					</tr>
					<tr>
						<td><b>First Name: </b></td><td><b><%=saleInfo.getFirstName()%></td>
					</tr>
					<tr>
						<td><b>Last Name: </b></td><td><b><%=saleInfo.getLastName()%></td>
					</tr>
				</table>
				<br><br>
			</td>
		</tr>
		<%if(EnumSaleStatus.CAPTURE_PENDING.equals(saleInfo.getStatus())){%>
			<tr>
				<td><input type="submit" value="Unconfirm Order" class="text12wbold" style="background-color:669933;color:ffffff" onclick="javascript:document.confirm_delivery.action.value='unconfirm_order'"></td>
			</tr>
		<%}%>
				
	<%}%> 
	
</form>		
</table>
</dlv:unconfirmControllerTag>

</body>
</html>