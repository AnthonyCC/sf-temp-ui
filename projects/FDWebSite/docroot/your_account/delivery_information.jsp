<%@ page import="com.freshdirect.framework.util.NVL"%>
<%@ page import='com.freshdirect.fdstore.*' %>
<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.customer.*' %>
<%@ page import="com.freshdirect.delivery.restriction.GeographyRestrictionMessage"%>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<fd:CheckLoginStatus guestAllowed="false" recognizedAllowed="false" />
<%
	//get un.address string

	String allAddresses = (String)NVL.apply(session.getAttribute("allAddresses"), "");
	Boolean newSession = ((String)NVL.apply(session.getAttribute("newSession"), "true")).equals("true"); 
	Integer oldAddressCount = (Integer)NVL.apply(session.getAttribute("addressCount"), 0);

	String newAddressId = "";
	
	FDSessionUser dlvInfoUser = (FDSessionUser) session.getAttribute(SessionName.USER);
	Collection shippingAddresses = FDCustomerManager.getShipToAddresses(dlvInfoUser.getIdentity());
	Integer addressCount = shippingAddresses.size();
	ErpAddressModel newAddress = null;

	
	String redirectURL = "";

	if (addressCount > 0 && !newSession) {

		//loop and remove old addresses
		for(Iterator i = shippingAddresses.iterator(); i.hasNext();) {
			ErpAddressModel thisAddress = (ErpAddressModel)i.next();
			if ( allAddresses.indexOf((String)thisAddress.getPK().getId()) == -1 ) {
				//this address is new
				newAddressId = (String)thisAddress.getPK().getId();
				newAddress = thisAddress;
				allAddresses += (String)thisAddress.getPK().getId()+",";
			}
		}
	}else{
		allAddresses = "";
		for(Iterator i = shippingAddresses.iterator(); i.hasNext();) {
			ErpAddressModel thisAddress = (ErpAddressModel)i.next();
			allAddresses += (String)thisAddress.getPK().getId()+",";
		}
	}
	
	if ( oldAddressCount != addressCount) {
		//regenerate list
		allAddresses = "";
		for(Iterator i = shippingAddresses.iterator(); i.hasNext();) {
			ErpAddressModel thisAddress = (ErpAddressModel)i.next();
			allAddresses += (String)thisAddress.getPK().getId()+",";
		}
		oldAddressCount = addressCount;
	}
		//set in session
		session.setAttribute("allAddresses", allAddresses);
		session.setAttribute("newSession", "false");
		session.setAttribute("oldAddressCount", oldAddressCount);
		
	if ( newAddress != null ) {
		//check for unattended
		%>
		<fd:UnattendedDelivery id="zone" address="<%= newAddress %>" checkUserOptions="true">
			<%
				if (zone.isUnattended() && !EnumUnattendedDeliveryFlag.OPT_OUT.equals(newAddress.getUnattendedDeliveryFlag())) {
					//redirect
					response.sendRedirect(response.encodeRedirectURL("/your_account/edit_delivery_address_unattended.jsp?page=udConfirm&addressId="+newAddressId));
				}
			%>
		</fd:UnattendedDelivery>
		<%
	}
%>

<tmpl:insert template='/common/template/dnav.jsp'>
    <tmpl:put name='title' direct='true'>FreshDirect - Your Account - Delivery Addresses</tmpl:put>
    <tmpl:put name='content' direct='true'>
<fd:RegistrationController actionName='<%=request.getParameter("dlvActionName")%>' result="result">
<!-- error message handling here -->
<% Boolean removedReservation = (Boolean)NVL.apply(session.getAttribute(SessionName.REMOVED_RESERVATION), Boolean.FALSE);%>
<%if(removedReservation.booleanValue()){
	session.removeAttribute(SessionName.REMOVED_RESERVATION);
%>
	<br>
	<table width="600" align="center" border="1" cellpadding="0" cellspacing="0">
		<tr align="center">
			<td>
				<br><b>YOUR DELIVERY RESERVATION HAS BEEN CLEARED</b><br>
					You will need to renew your delivery timeslot reservation for your new address.<br>
				<a href="/your_account/reserve_timeslot.jsp">Click here to reserve your delivery timeslot.</a><br><br>
			</td>
		</tr>
	</table>
	<br>
<%}%>
<form method="post">
<TABLE WIDTH="675" ALIGN="CENTER" BORDER="0" CELLPADDING="0" CELLSPACING="0">
<TR><TD class="text11">
<font class="title18">Delivery Addresses</font><br>
Update your delivery address information.<br></td>
</tr></table>
<IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="8" BORDER="0"><BR>
<IMG src="/media_stat/images/layout/ff9933.gif" WIDTH="675" HEIGHT="1" BORDER="0"><BR>
<IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="8" BORDER="0"><br>

<%@ include file="/includes/your_account/i_edit_delivery_address.jspf" %><BR>

<IMG src="/media_stat/images/layout/ff9933.gif" WIDTH="675" HEIGHT="1" BORDER="0"><BR>
<FONT CLASS="space4pix"><BR><BR></FONT>
<TABLE BORDER="0" CELLSPACING="0" CELLPADDING="0" WIDTH="675">
<TR VALIGN="TOP">
<TD WIDTH="35"><a href="/index.jsp"><img src="/media_stat/images/buttons/arrow_green_left.gif" border="0" alt="CONTINUE SHOPPING" ALIGN="LEFT"></a></TD>
<TD WIDTH="640"><a href="/index.jsp"><img src="/media_stat/images/buttons/continue_shopping_text.gif"  border="0" alt="CONTINUE SHOPPING"></a>
<BR>from <FONT CLASS="text11bold"><A HREF="/index.jsp">Home Page</A></FONT><BR><IMG src="/media_stat/images/layout/clear.gif" WIDTH="340" HEIGHT="1" BORDER="0"></TD>
</TR>
</TABLE>
</form>
<BR>
<%--<TABLE CELLPADDING="2" CELLSPACING="0" BORDER="0" WIDTH="675">
<TR VALIGN="TOP"><TD><%@ include file="/includes/i_footer_account.jspf" %></TD></TR>
</TABLE>--%>
</fd:RegistrationController>
</tmpl:put>
</tmpl:insert>