<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ page import='com.freshdirect.webapp.util.*' %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
    <title><tmpl:get name='title'/></title>
    <%@ include file="/common/template/includes/metatags.jspf" %>
    <script language="javascript" src="/assets/javascript/common_javascript.js"></script>
    <%@ include file="/shared/template/includes/style_sheet_detect.jspf" %>
</head>
<BODY bgcolor="#FFFFFF" LINK="#336600" VLINK="#336600" ALINK="#FF9900" TEXT="#333333" CLASS="text10">
<CENTER>
<%@ include file="/common/template/includes/globalnav.jspf" %>
<% boolean isCorporate = user.getSelectedServiceType().equals(EnumServiceType.CORPORATE); %>
<table width="745" border="0" CELLPADDING="0" CELLSPACING="0">
<tr>
	<td width="6" COLSPAN="2" ROWSPAN="2"><img src="/media_stat/images/layout/top_left_curve.gif" width="6" height="6" border="0"></td>
	<td width="733" valign="top" bgcolor="#999966"><img src="/media_stat/images/layout/999966.gif" width="733" height="1" border="0"></td>
	<td width="6" COLSPAN="2" ROWSPAN="2"><img src="/media_stat/images/layout/top_right_curve.gif" width="6" height="6" border="0"></td>
</tr>
<tr>
	<td width="733" valign="top"><img src="/media_stat/images/layout/clear.gif" width="1" height="5" border="0"></td>
</tr>
<tr valign="top">
	<td bgcolor="#999966" valign="bottom" width="1"><img src="/media_stat/images/layout/999966.gif" width="1" height="1"></td>
	<td width="743" colspan="3">
	<table border="0" CELLPADDING="0" CELLSPACING="0">
			    <tr valign="top">
				    <td><img src="/media_stat/images/layout/clear.gif" width="1" height="5"><br>&nbsp;&nbsp;&nbsp;<a href="/help/delivery_info.jsp"><img src="/media_stat/images/template/help/delivery/delivery_pickup_deptnav.gif" width="219" height="26" border="0" alt="DELIVERY & PICK UP INFO"></a>&nbsp;&nbsp;&nbsp;</td>
					<td bgcolor="#999966"><img src="/media_stat/images/layout/clear.gif" width="1" height="1" border="0"></td>
					<td><img src="/media_stat/images/layout/clear.gif" width="8" height="1" border="0"></td>
					<td class="text9_lh12">
                        <a href="/help/delivery_info.jsp">Home Delivery</a><br>
                        <a href="/help/delivery_info_cos.jsp">Corporate Delivery</a><br>
						<a href="/help/delivery_info_depot.jsp">Depot Delivery</a>
					</td>
                                        
                                        <td><img src="/media_stat/images/layout/clear.gif" width="8" height="1" border="0"></td>
                                        <td bgcolor="#999966"><img src="/media_stat/images/layout/clear.gif" width="1" height="1" border="0"></td>
					<td><img src="/media_stat/images/layout/clear.gif" width="8" height="1" border="0"></td>
                                        <td class="text9_lh12" <%=user.isEligibleForPreReservation()?"colspan=\"5\"":""%>><a href="/help/delivery_lic_pickup.jsp">Pickup at FreshDirect</a><br>
										<% if (user.getLevel() >= FDUserI.RECOGNIZED) { %>
											<a href="/your_account/delivery_info<% if (user.getAdjustedValidOrderCount() >= 1 && (user.getLevel() >= FDUserI.RECOGNIZED)) { %>_avail_slots<% } else { %>_check_slots<% } %>.jsp">Available Delivery Slots</a>
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
				<tr><td colspan="12"><img src="/media_stat/images/layout/clear.gif" width="1" height="6" border="0"></td></tr>
			</table></td>
	<td bgcolor="#999966" valign="bottom" width="1"><img src="/media_stat/images/layout/clear.gif" width="1" height="1"></td>
</tr>
<tr bgcolor="#999966"><td colspan="5"><img src="/media_stat/images/layout/clear.gif" width="1" height="1"></td></tr>
<tr valign="top">
	<td bgcolor="#999966"><img src="/media_stat/images/layout/clear.gif" width="1" height="1"></td>
	<td align="center" colspan="3"><%-- content lands here --%><tmpl:get name='content'/><%-- content ends above here--%></td>
	<td bgcolor="#999966"><img src="/media_stat/images/layout/clear.gif" width="1" height="1"></td>
</tr>
<%-- This row is to hold the table --%>
<tr valign="top">
	<td bgcolor="#999966" valign="bottom" width="1"><img src="/media_stat/images/layout/999966.gif" width="1" height="1"></td>
	<td width="5"><img src="/media_stat/images/layout/clear.gif" height="1" width="5"></td>
	<td width="733" align="center" ><img src="/media_stat/images/layout/clear.gif" height="1" width="733"></td>
	<td width="5"><img src="/media_stat/images/layout/clear.gif" height="1" width="5"></td>
	<td bgcolor="#999966" valign="bottom" width="1"><img src="/media_stat/images/layout/999966.gif" width="1" height="1"></td>
</tr>

<tr valign="bottom">
	<td width="6" COLSPAN="2" ROWSPAN="2"><img src="/media_stat/images/layout/bottom_left_curve.gif" width="6" height="6" border="0"></td>
	<td width="733"><img src="/media_stat/images/layout/clear.gif" width="1" height="5" border="0"></td>
	<td width="6" COLSPAN="2" ROWSPAN="2"><img src="/media_stat/images/layout/bottom_right_curve.gif" width="6" height="6" border="0"></td>
</tr>
<tr>
	<td width="733" bgcolor="#999966" valign="bottom"><img src="/media_stat/images/layout/999966.gif" width="733" height="1" border="0"></td>
</tr>
</table>
<%@ include file="/common/template/includes/footer.jspf" %>
</CENTER>
</BODY>
</HTML>
