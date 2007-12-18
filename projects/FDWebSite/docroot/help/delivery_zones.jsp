<%@ page import="com.freshdirect.fdstore.FDDeliveryManager"%>
<%@ page import="com.freshdirect.common.customer.EnumServiceType" %>

<%@ taglib uri="template" prefix="tmpl" %>

<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='oscache' prefix='oscache' %>

<% boolean fromZipCheck = "yes".equalsIgnoreCase(request.getParameter("zipCheck"));%>

<tmpl:insert template='/shared/template/large_pop.jsp'>
	<tmpl:put name='title' direct='true'>FreshDirect - Our Delivery Zones</tmpl:put>
		<tmpl:put name='content' direct='true'>

<script>
<!--
function goToDepotHome(){

	var depot = document.forms['depots'].depotNames.value;
	if(depot != ""){
		var url = "http://<%=request.getServerName()%>/" + depot + "/";
		parent.window.opener.location = url ;
		parent.window.opener.focus();
	}
	else{
	alert("Please chose a Depot Delivery Partner")
	}
}

function goToPage(pagePath) {
	redirectUrl = "http://" + location.host + pagePath;
	self.opener.location = redirectUrl;
	self.opener.focus();
}
//-->
</script>
	
<oscache:cache time="28800">
<table border="0" cellpadding="0" cellspacing="0" width="100%">
	<tr valign="top">
		<td width="341">
		<%
			List zipCodes = FDDeliveryManager.getInstance().getDeliverableZipCodes(EnumServiceType.HOME);
		%>
			<%if (zipCodes.size() > 0) {%>
				<table border="0" cellpadding="0" cellspacing="0" width="100%">
					<tr>
						<td>	
							<img src="/media_stat/images/layout/clear.gif" width="1" height="5" alt="" border="0"><br>
							<img src="/media_stat/images/template/help/delivery/home_delivery_zones.gif" width="144" height="9" alt="" border="0">
							<br><img src="/media_stat/images/layout/clear.gif" width="1" height="5"><br>
							We currently deliver to the zip codes below, and we are expanding all the time.<br><img src="/media_stat/images/layout/clear.gif" width="1" height="5">
						</td>
					</tr>
				</table>
				<%@ include file="/shared/includes/popups/i_deliverable_zips.jspf" %><br>
			<%}%>
		<%
			zipCodes = FDDeliveryManager.getInstance().getDeliverableZipCodes(EnumServiceType.CORPORATE);
		%>
			<%if (zipCodes.size() > 0) {%>
				<table border="0" cellpadding="0" cellspacing="0" width="100%">
					<tr>
						<td>	
							<img src="/media_stat/images/layout/clear.gif" width="1" height="5" alt="" border="0"><br>
							<img src="/media_stat/images/template/help/delivery/cos_delivery_zones.gif" width="180" height="9" alt="" border="0">
							<br><img src="/media_stat/images/layout/clear.gif" width="1" height="5"><br>
							We currently deliver to the zip codes below, and we are expanding all the time.<br><img src="/media_stat/images/layout/clear.gif" width="1" height="5">
						</td>
					</tr>
				</table>
				<%@ include file="/shared/includes/popups/i_deliverable_zips.jspf" %><br>
			<%}%>
		</td>
		<td><img src="/media_stat/images/layout/clear.gif" width="8" height="1" alt="" border="0"></td>
		<td bgcolor="#CCCCCC"><img src="/media_stat/images/layout/clear.gif" width="1" height="1" alt="" border="0"></td>
		<td><img src="/media_stat/images/layout/clear.gif" width="6" height="1" alt="" border="0"></td>
		<td width="164" align="center">
			<% if (!fromZipCheck) {%>
				<a href="javascript:goToPage('/help/delivery_lic_pickup.jsp')">
			<%}%>
			<img src="/media_stat/images/template/pickup/lic_pickup_hdr.gif" width="144" height="77" alt="DID YOU KNOW? YOU CAN PICKUP YOUR ORDER AT OUR FACILITY!" border="0"><br>
			<img src="/media_stat/images/template/pickup/lic_pickup_img.jpg" width="115" height="75" alt="Our Facility" border="0" vspace="8">
			<% if (!fromZipCheck) {%>
				</a>
			<%}%><br>
			<b>Minutes from Manhattan!</b><br>You can also enjoy food from<br>FreshDirect by picking up<br>your order at our<br>
			<% if (!fromZipCheck) {%>
				<a href="javascript:goToPage('/help/delivery_lic_pickup.jsp')">
			<%}%>
			Long Island City facility
			<% if (!fromZipCheck) {%>
				</a>
			<%}%>.*<br><br>
		</td>
	</tr>
</table>
</oscache:cache>
</tmpl:put>
</tmpl:insert>


