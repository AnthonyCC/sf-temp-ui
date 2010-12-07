<%@ page import='com.freshdirect.webapp.util.*' %>
<%@ page import='java.util.List.*' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='bean' prefix='bean' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<fd:CheckLoginStatus guestAllowed="false" recognizedAllowed="true" />
<tmpl:insert template='/common/template/dnav.jsp'>
<tmpl:put name='title' direct='true'>FreshDirect - Your Account - Unattended Delivery Confirmation</tmpl:put>
<tmpl:put name='content' direct='true'>
<%
	String successPage = "/your_account/delivery_information.jsp";

	if (session.getAttribute("redirectToIndex") != null) {
		successPage = "/index.jsp";
		//un-set attribute but check for null, because submitting passes here a second time before going to successPage
		session.setAttribute("redirectToIndex", "false");
	} else {
		successPage = "/your_account/delivery_information.jsp";
	}

%>
<fd:RegistrationController actionName="editDeliveryAddress" result="result" successPage="<%=successPage%>">

<fd:ErrorHandler result='<%=result%>' field='<%=checkErrorType%>' id='errorMsg'>
	<%@ include file="/includes/i_error_messages.jspf" %>
</fd:ErrorHandler>

<fd:ErrorHandler result='<%=result%>' field='<%=checkAddressForm%>'>
<% String errorMsg = SystemMessageList.MSG_MISSING_INFO; %>
	<%@ include file="/includes/i_error_messages.jspf" %>	
</fd:ErrorHandler>

	<table width="675" border="0" cellpadding="0" cellspacing="0">
		<tr><td width="675" class="text11">
			<font class="title18">Your neighborhood has Unattended Delivery Service, <br />for no extra charge!</font><br />
		</td></tr>
	</table>
	<img src="/media_stat/images/layout/clear.gif" width="1" height="8" border="0"><br />
	<img src="/media_stat/images/layout/ff9933.gif" width="675" height="1" border="0"><br />
	<img src="/media_stat/images/layout/clear.gif" width="1" height="8" border="0"><br /><br />

	<form name="address" method="post">
		<input type="hidden" name="updateShipToAddressId" value="<%=request.getParameter("addressId")%>">

		<font class="space4pix"><br /></font><%@ include file="/includes/ckt_acct/i_delivery_address_field.jspf" %><br /><br />
			<%//@ include file="/includes/ckt_acct/i_delivery_special_field.jspf" %><br /><br />
		
		<table cellpadding="0" cellspacing="0" border="0" width="675">
			<tr valign="top" bgcolor="#ff9933">
				<td width="675" colspan="2"><img src="/media_stat/images/layout/ff9933.gif" hspace="0" width="1" height="1" border="0"></td>
			</tr>
		<tr>
			<td width="375"><font class="space2pix"><br /></font><input type="image" src="/media_stat/images/buttons/submit_orange.gif" height="22" width="103" alt="Save Unattended Delivery Choice" border="0" hspace="4" vspace="4" name="edit_delivery_address"></td>
			<td width="300" align="right" valign="top"><!--  --></td>
		</tr>
		</table>
		<br />
	</form>
</fd:RegistrationController>
</tmpl:put>
</tmpl:insert>
