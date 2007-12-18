<%@ page contentType="text/html"%>
<%@ page import="com.freshdirect.customer.DlvSaleInfo"%>
<%@ page import="com.freshdirect.customer.EnumSaleStatus"%>
<%@ taglib uri="logic" prefix="logic" %>
<%@ taglib uri="delivery" prefix="dlv" %>

<html>
<head><title>Confirmation By Truck</title>
<link rel="stylesheet" href="css/pc_ie.css">
<script language="javascript">
	function confirmConfirmations(){
		var ConfirmedCount = 0;
		var RedeliveryCount = 0;
		var RefusedCount = 0;
		for (i=0;i < document.confirm_orders.elements.length;i++) {
		 var elementObj = document.confirm_orders.elements[i];
		 var objValue = elementObj.value;
		 var isChecked = elementObj.checked;
		 if (elementObj.name.indexOf("deliverystatus_")==-1) continue;
		 
		 if(objValue=="delivered" && isChecked ) ConfirmedCount++;
		 if(objValue=="redelivery" && isChecked ) RedeliveryCount++;
		 if(objValue=="refused" && isChecked ) RefusedCount++;
	
		}
		
		return confirm("Summary\n=======\n\n"+"Delivered: "+ConfirmedCount+"\n"+"Refused:"+RefusedCount+"\nRedelivery:"+RedeliveryCount);
	}
		
</script>
</head>
<% 
   boolean unconfirmedSale = false;
   String truckNumber = request.getParameter("truck_number");
   String dateToConfirm = request.getParameter("date_to_confirm");
   String successPage = "confirm_by_truck.jsp?date_to_confirm="+dateToConfirm;
%>
<dlv:orderStatusControllerTag successPage="<%=successPage%>" />
<body bgcolor="#ffffff" topmargin="0" leftmargin="0">

<table width="100%" cellpadding="0" cellspacing="0" border="0">
		<tr bgcolor="#FF9933">
			<td width="100%"><font class="space2pix"><br></font>&nbsp;<a href="confirm_by_truck.jsp?date_to_confirm=<%=dateToConfirm%>" class="headerLink">HOME</a><br></font></td>
		</tr>
		<tr bgcolor="#c00cc3d" class="text12wbold">
			<td width="100%"><font class="space2pix">&nbsp;</font></td>
		</tr>
		<tr valign="bottom">
			<td width="100%"><img src="images/urban-highway2.jpg" width="129" height="82" border="0" alt="Urban"><img src="images/logo_sm.gif" width="130" height="32" border="0" alt="FreshDirect" HSPACE="10" VSPACE="2"></TD>
		</tr>
		<tr>
			<td bgcolor="c00cc3d" width="100%"><font class="space2pix">&nbsp;</font></td>
		</tr>	
</table><br><br>
<dlv:orderTag id="orders">
<form name="confirm_orders" method="POST" onSubmit="return confirmConfirmations();">
<table width="800" cellpadding="0" cellspacing="0" border="0">
	<tr bgcolor="#c00cc3d">
		<td valign="center" align="center"  class="title12" colspan="7">Confirming Order For Truck Number <%=truckNumber%></td>
	</tr>
	<tr>
		<td align="center" width="100">&nbsp;</td>
		<td align="center" width="100">&nbsp;</td>
		<td align="center" width="150">&nbsp;</td>
		<td align="center" width="150">&nbsp;</td>
		<td align="center" width="100">&nbsp;</td>
		<td align="center" width="100">&nbsp;</td>
	</tr>
	<tr>
		<td align="center"><b>Stop Sequence</b></td>
		<td align="center"><b>Order Number</b></td>
		<td align="left"><b>First Name</b></td>
		<td align="left"><b>Last Name</b></td>
		<td align="center"><b>Confirmed</b></td>
		<td align="center"><b>Returned</b></td>
	</tr>
	<%for(int i = 0, size = orders.size(); i < size; i++){
		DlvSaleInfo saleInfo = (DlvSaleInfo)orders.get(i);	
		if(i%2 == 0){
	%>
			<tr bgcolor="#dfdfdf">
		<%}else{%>
			<tr>
		<%}%>
		<td align="center"><%=saleInfo.getStopSequence()%></td>
		<td align="center"><%=saleInfo.getSaleId()%></td>
		<td align="left"><%=saleInfo.getFirstName()%></td>
		<td align="left"><%=saleInfo.getLastName()%></td>
		<%if(EnumSaleStatus.ENROUTE.equals(saleInfo.getStatus())){
			if(!unconfirmedSale){
				unconfirmedSale = true;
			}%>
			<td align="center"><input type="radio" name="deliverystatus_<%=saleInfo.getSaleId()%>" value="delivered" checked></td>
			<td align="center" nowrap><input type="radio" name="deliverystatus_<%=saleInfo.getSaleId()%>" value="refused"><input type="checkbox" name="full_<%=saleInfo.getSaleId()%>"><b>Full</b>&nbsp<input type="checkbox" name="alcohol_<%=saleInfo.getSaleId()%>"><b>Alcohol</b></td>
		<%}else{ 
			if(EnumSaleStatus.CAPTURE_PENDING.equals(saleInfo.getStatus())||EnumSaleStatus.PAYMENT_PENDING.equals(saleInfo.getStatus()) || EnumSaleStatus.SETTLED.equals(saleInfo.getStatus())){%>
				<td align="center" style="color:green;font-weight:bold">X</td>
				<td align="center"><img src="images/images/clear.gif" height="18" width="1"></td>
			<%}else if(EnumSaleStatus.PENDING.equals(saleInfo.getStatus())){%>
				<td align="center">&nbsp;</td>
				<td align="center"><img src="images/images/clear.gif" height="18" width="1"></td>
			<%} else if(EnumSaleStatus.RETURNED.equals(saleInfo.getStatus()) || EnumSaleStatus.REFUSED_ORDER.equals(saleInfo.getStatus())){%>
				<td align="center">&nbsp;</td>
				<td align="center" style="color:red;font-weight:bold">X</td>
			<%}else{%>
				<td align="center">&nbsp;</td>
				<td align="center">&nbsp;</td>
			<%}%>
		<%}%>
	</tr>
	<tr>
		<td align="center" colspan="7" bgcolor="#c00cc3d"></td>
	</tr>
	<%}%>
	<%if(unconfirmedSale){%>
	<tr>
		<td align="right" colspan="7"><input type="submit" value="Confirm" class="text12wbold" style="background-color:669933;color:ffffff"></td>
	</tr>
	<%}%>
</table>
</form>
</dlv:orderTag>
</body>
</html>