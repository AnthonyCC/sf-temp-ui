<%@ page contentType="text/html"%>
<%@ page import="java.util.*"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="com.freshdirect.customer.ErpTruckInfo"%>
<%@ taglib uri="logic" prefix="logic" %>
<%@ taglib uri="delivery" prefix="dlv" %>

<html>
<head><title>Confirmation By Truck</title>
<link rel="stylesheet" href="css/pc_ie.css">
</head>
<body bgcolor="#ffffff" topmargin="0" leftmargin="0">

<table width="100%" cellpadding="0" cellspacing="0" border="0">
		<tr bgcolor="#FF9933">
			<td width="100%"><font class="space2pix"><br></font>&nbsp;<a href="index.jsp" class="headerLink">HOME</a>&nbsp;&nbsp;&nbsp;<a href="confirm_by_order.jsp" class="headerLink">Confirm By Order</a>&nbsp;&nbsp;&nbsp;<font class="text12wbold">Confirm By Truck</font><br><font class="space2pix"><br></font></td>
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
<dlv:truckTag id="trucks">
<table width="300" cellpadding="1" cellspacing="1" border="0">
	<tr bgcolor="#c00cc3d">
		<td valign="center" align="center"  class="title12" colspan="3">Confirming Orders By Truck</td>
	</tr>
	<% 
		List confirmedTrucks = (List)session.getAttribute("confirmed_trucks");
		if(confirmedTrucks == null){
			confirmedTrucks = new ArrayList();
			session.setAttribute("confirmed_trucks", confirmedTrucks);
		}
		Calendar begCal = new GregorianCalendar();
		begCal.add(Calendar.DATE, -6);
		Calendar endCal = new GregorianCalendar();
		String selectedDate = request.getParameter("date_to_confirm");
		String selectedTruck = request.getParameter("truck_number");
		if(selectedDate == null){
			selectedDate = "";
		}
		endCal.add(Calendar.DATE, 1);
		 
		SimpleDateFormat sf = new SimpleDateFormat("MM-dd-yyyy");
	%>
	<tr>
		<td align="right"><b>Choose Day to Confirm:</b></td>
		<td align="center" colspan="2">
			<select name="date_to_confirm" onChange="javascript:window.location='confirm_by_truck.jsp?date_to_confirm='+this.value">
				<option value=""></option>
			<%while(begCal.before(endCal)){
				String dateStr = sf.format(begCal.getTime());
				%>
				<option value="<%=dateStr%>" <%=selectedDate.equals(dateStr) ? "selected" : "" %>><%=dateStr%></option>
				
				<%begCal.add(Calendar.DATE, 1);
			}%>
				
			</select>
		</td>
	</tr>
	<tr>
		<td align="center" width="150">&nbsp;</td>
		<td align="center" width="75">&nbsp;</td>
		<td align="center" width="75">&nbsp;</td>
	</tr>
	<tr align="center">
		<td align="center"><b>Truck Number</b></td>
		<td align="center" colspan="2"><b>Confirmed</b></td>
	</tr>
	<tr>
		<td align="center">&nbsp;</td>
		<td align="center">YES</td>
		<td align="center">NO</td>
	</tr>
	<% 
	for(Iterator i = trucks.iterator(); i.hasNext(); ){
		ErpTruckInfo truck = (ErpTruckInfo)i.next();
		if(truck.isConfirmed() || confirmedTrucks.contains(truck.getTruckNumber())){
	%>
			<tr>
				<td align="center"><a href="confirm_truck.jsp?truck_number=<%=truck.getTruckNumber()%>&date_to_confirm=<%=selectedDate%>"><%=truck.getTruckNumber()%></a></td>
				<td align="center" style="color:green;">X</td>
				<td align="center" style="color:red;">&nbsp;</td>
			</tr>
			<tr>
				<td align="center" colspan="3" bgcolor="#c00cc3d"></td>
			</tr>
	<%}else{%>	
		
		<tr>
			<td align="center"><a href="confirm_truck.jsp?truck_number=<%=truck.getTruckNumber()%>&date_to_confirm=<%=selectedDate%>"><%=truck.getTruckNumber()%></a></td>
			<td align="center" style="color:green;">&nbsp;</td>
			<td align="center" style="color:red;">X</td>
		</tr>
		<tr>
			<td align="center" colspan="3" bgcolor="#c00cc3d"></td>
		</tr>
	<%}
	}%>
</table>
</dlv:truckTag>
</body>
</html>