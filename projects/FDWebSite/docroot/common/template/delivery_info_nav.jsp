<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ page import='com.freshdirect.webapp.util.*' %>

<% //expanded page dimensions
final int W_DELIVERY_INFO_NAV_TOTAL = 970;
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
    <title><tmpl:get name='title'/></title>
    <%@ include file="/common/template/includes/metatags.jspf" %>
	<meta http-equiv="X-UA-Compatible" content="IE=8" />
    <%@ include file="/common/template/includes/i_javascripts.jspf" %>
    <%@ include file="/shared/template/includes/style_sheet_grid_compat.jspf" %>
    <%@ include file="/shared/template/includes/style_sheet_detect.jspf" %>
	<%@ include file="/shared/template/includes/ccl.jspf" %>

	<fd:javascript src="/assets/javascript/timeslots.js"/>
	<fd:css href="/assets/css/timeslots.css"/>

<%@ include file="/shared/template/includes/i_head_end.jspf" %>
</head>
<BODY bgcolor="#FFFFFF" LINK="#336600" VLINK="#336600" ALINK="#FF9900" TEXT="#333333">
<%@ include file="/shared/template/includes/i_body_start.jspf" %>
<%@ include file="/common/template/includes/globalnav.jspf" %>
<CENTER CLASS="text10">
<% boolean isCorporate = user.getSelectedServiceType().equals(EnumServiceType.CORPORATE); %>
<table width="<%=W_DELIVERY_INFO_NAV_TOTAL%>" border="0" CELLPADDING="0" CELLSPACING="0">
<tr>
	<td width="<%=W_DELIVERY_INFO_NAV_TOTAL%>" valign="top"><img src="/media_stat/images/layout/clear.gif" width="1" height="5" border="0"></td>
</tr>
<tr valign="top">
	<td width="<%=W_DELIVERY_INFO_NAV_TOTAL%>">
	<table border="0" CELLPADDING="0" CELLSPACING="0">
			    <tr valign="top">
				    <td><img src="/media_stat/images/layout/clear.gif" width="1" height="5"><br>&nbsp;&nbsp;&nbsp;<a href="/help/delivery_info.jsp"><img src="/media_stat/images/template/help/delivery/delivery_pickup_deptnav.gif" width="219" height="26" border="0" alt="DELIVERY & PICK UP INFO"></a>&nbsp;&nbsp;&nbsp;</td>
					<td bgcolor="#999966"><img src="/media_stat/images/layout/clear.gif" width="1" height="1" border="0"></td>
					<td><img src="/media_stat/images/layout/clear.gif" width="8" height="1" border="0"></td>
					<td class="text9_lh12">
                        <a href="/help/delivery_info.jsp">Home Delivery</a><br>
                        <a href="/help/delivery_info_cos.jsp">Corporate Delivery</a><br><br>
						<!--<a href="/help/delivery_info_depot.jsp">Depot Delivery</a>
					--></td>
                                        
                                        <td><img src="/media_stat/images/layout/clear.gif" width="8" height="1" border="0"></td>
                                        <td bgcolor="#999966"><img src="/media_stat/images/layout/clear.gif" width="1" height="1" border="0"></td>
					<td><img src="/media_stat/images/layout/clear.gif" width="8" height="1" border="0"></td>
                                        <td class="text9_lh12" <%=user.isEligibleForPreReservation()?"colspan=\"5\"":""%>><a href="/help/delivery_lic_pickup.jsp">Pickup at FreshDirect</a><br>
										<% if (user.getLevel() >= FDUserI.RECOGNIZED) { %>
											<a href="/your_account/delivery_info<% if (user.getLevel() >= FDUserI.RECOGNIZED) { %>_avail_slots<% } else { %>_check_slots<% } %>.jsp">Available Delivery Slots</a>
										<% } else { %>
											<a href="/help/delivery_info_check_slots.jsp">Available Delivery Slots</a>
										<% } %>
										   <br>
										   <% if(user.isEligibleForPreReservation()) { %>
												<a href="/your_account/reserve_timeslot.jsp">Reserve Delivery (in Your Account)</a>
											</td>
												<% if (FDStoreProperties.getHamptons()) {  %>
													<td><img src="/media_stat/images/layout/clear.gif" width="8" height="1" border="0"></td>
													<td bgcolor="#999966"><img src="/media_stat/images/layout/clear.gif" width="1" height="1" border="0"></td>
													<td><img src="/media_stat/images/layout/clear.gif" width="8" height="1" border="0"></td>
													<td class="text9_lh12">
								                        <a href="/help/delivery_hamptons.jsp">Hamptons Summer Service</a>
													</td>
												<% } %>
											<% } else if (FDStoreProperties.getHamptons()) { %>
											<a href="/help/delivery_hamptons.jsp">Hamptons Summer Service</a>
											</td>
											<% } %>
				</tr>
        <tr><td colspan="12"><img src="/media_stat/images/layout/clear.gif" width="1" height="6"></td></tr>
			</table></td>
</tr>
<tr bgcolor="#999966"><td><img src="/media_stat/images/layout/clear.gif" width="1" height="1"></td></tr>
<tr valign="top">
	<td align="center"><%-- content lands here --%><tmpl:get name='content'/><%-- content ends above here--%></td>
</tr>
<%-- This row is to hold the table --%>
<tr valign="top">
	<td width="<%=W_DELIVERY_INFO_NAV_TOTAL%>" align="center" ><img src="/media_stat/images/layout/clear.gif" height="1" width="<%=W_DELIVERY_INFO_NAV_TOTAL%>"></td>
</tr>

<tr valign="bottom">
	<td width="<%=W_DELIVERY_INFO_NAV_TOTAL%>"><img src="/media_stat/images/layout/clear.gif" width="1" height="5" border="0"></td>
</tr>
</table>
</CENTER>
<%@ include file="/common/template/includes/footer.jspf" %>
<%@ include file="/common/template/includes/i_jsmodules.jspf" %>
</BODY>
</HTML>
