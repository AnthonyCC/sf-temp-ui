<%@page contentType="text/html"%>
<%@ page import="com.freshdirect.delivery.model.*"%>
<%@ page import="com.freshdirect.customer.*"%>
<%@ page import="com.freshdirect.framework.util.QuickDateFormat"%>
<%@ page import="com.freshdirect.delivery.*"%>
<%@ page import="java.util.*" %>

<%@ page import="java.text.SimpleDateFormat" %>
<%@ taglib uri="template" prefix="tmpl" %>
<%@ taglib uri="logic" prefix="logic" %>
<%@ taglib uri="delivery" prefix="dlv" %>

<% //String orderNumber = (String)pageContext.getAttribute("ORDER_NUMBER"); %>
<dlv:deliveryConfirmControllerTag id="dlvInfo" result="errors">

<html>
<head><title>Confirmation By Order</title>
<link rel="stylesheet" href="css/pc_ie.css">
</head>
<BODY bgcolor="#ffffff" topmargin="0" leftmargin="0">
<table width="100%" cellpadding="0" cellspacing="0" border="0">
		<tr bgcolor="#FF9933">
			<td width="1000" colspan="13"><font class="space2pix"><br></font>&nbsp;<a href="index.jsp" class="headerLink">HOME</a>&nbsp;&nbsp;&nbsp;<font class="text12wbold">Confirm By Order</font>&nbsp;&nbsp;&nbsp;<a href="confirm_by_truck.jsp" class="headerLink">Confirm By Truck</a><br><font class="space2pix"><br></font></td>
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
 
<table width="100%" cellpading="0" cellspacing="0" border="0">
		<form name="confirm_delivery" method="post">
		<tr>
			<td>
				<b>Enter Order Number: </b>&nbsp;&nbsp;
				<input type="text" size="20" class="text10" name="order_number" value='<%=(ORDER_NUMBER != null && !"".equals(ORDER_NUMBER)) ? ORDER_NUMBER: ""%>'>&nbsp;&nbsp;
				<input type="submit" value="Get Details" class="text12wbold" style="background-color:669933;color:ffffff" onclick="javascript:document.confirm_delivery.action.value='get_details'">
				<input type="hidden" name="action" value="">
				<input type="hidden" name="order_number" value="<%=ORDER_NUMBER%>">
		</tr>
		<% if ((dlvInfo != null) && (status != null)) {%>
			<tr>
				<td>
					<br>
					<table>
						<th>Order Delivery Details</th>
						<tr>
							<td bgcolor="c00cc3d" colspan="2"></td>
						</tr>
						<tr>
							<td><b>Delivery Start Time:</b></td>
							<td><%=dlvInfo.getDeliveryStartTime()%></td>
						</tr>
						<tr>
							<td><b>Delivery End Time:</b></td>
							<td><%=dlvInfo.getDeliveryEndTime()%></td>
						</tr>
						<tr>
							<td><b>Delivery Cutoff Time</b></td>
							<td><%=dlvInfo.getDeliveryCutoffTime()%></td>
						</tr>
						<tr>
							<td><b>Delivery Reservation Id</b></td>
							<td><%=dlvInfo.getDeliveryReservationId()%></td>
						</tr>
						<% ErpAddressModel address = dlvInfo.getDeliveryAddress(); %>
						<tr>
							<td><b>First Name</b></td>
							<td><%=address.getFirstName()%></td>
						</tr>
						<tr>
							<td><b>Last Name</b></td>
							<td><%=address.getLastName()%></td>
						</tr>
							<td><b>Delivery Address</b></td>
							<td>
								<%=address.getAddress1()%><br>
								<%String address2 = address.getAddress2();
								  if(address2 != null && !"".equals(address2)){%>
									<%=address2%><br>
								<%}
								  String apartment = address.getApartment();
								  if(apartment != null && !"".equals(apartment)){
								%>
									<%=address.getApartment()%><br>
								<%}%>
								<%=address.getCity()%>&nbsp;&nbsp;
								<%=address.getState()%>&nbsp;&nbsp;
								<%=address.getZipCode()%><br>
								
							</td>
						</tr>
						<tr>
							<td><b>Phone Number</b></td>
							<td><%=address.getPhone()%></td>
						</tr>
						<tr>
							<td><b>DeliveryInstructions</b></td>
							<td><%=address.getInstructions()%></td>
                        </tr>
                        <tr>
                            <td><b>Status</b></td>
                            <td><font color="red"><b><%=status.getName()%></b></font></td>
                        </tr>
						<tr>
							<td><input type="checkbox" name="full_return">&nbsp;<b>Full</b></td>
							<td><input type="checkbox" name="alcohol_only">&nbsp;<b>Alcohol</b></td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td bgcolor="c00cc3d" colspan="2"></td>
			</tr>
			<tr>
				<td>
                <% if (EnumSaleStatus.ENROUTE.equals(status) || EnumSaleStatus.REDELIVERY.equals(status) || EnumSaleStatus.PENDING.equals(status)) { %>
                <input type="submit" value="Confirm Delivery" class="text12wbold" style="background-color:669933;color:ffffff" onclick="javascript:document.confirm_delivery.action.value='confirm_order'">&nbsp;
				<input type="submit" value="Refused Delivery" class="text12wbold" style="background-color:669933;color:ffffff" onclick="javascript:document.confirm_delivery.action.value='refused_order'">&nbsp;
                <% } %>
                </td>
			</tr>
      <% } %>
		</form>
		
		<%if (errors.hasError("resource_error")){ %>
			<%@ include file="/include/i_error_messages.jspf" %>
		<% } %>
				
		<tr>
			<td bgcolor="c00cc3d" colspan="2"></td>
		</tr>		
</TABLE>
</dlv:deliveryConfirmControllerTag>

</body>
</html>