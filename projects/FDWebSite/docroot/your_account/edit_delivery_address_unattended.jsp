<%@ page import='com.freshdirect.webapp.util.*' %>
<%@ page import='java.util.List.*' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='bean' prefix='bean' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<% //expanded page dimensions
final int W_YA_EDIT_DELIVERY_ADDR = 970;
%>
<fd:CheckLoginStatus guestAllowed="false" recognizedAllowed="true" />
<tmpl:insert template='/common/template/dnav.jsp'>
<tmpl:put name='title' direct='true'>FreshDirect - Your Account - Unattended Delivery Confirmation</tmpl:put>
<tmpl:put name='content' direct='true'>
<%
	String successPage = (request.getParameter("successPage") == null || "".equals(request.getParameter("successPage"))) ? "/your_account/delivery_information.jsp" : request.getParameter("successPage") ;
%>
<fd:RegistrationController actionName="editDeliveryAddressForUnattendZone" result="result" successPage="<%=successPage%>">

<fd:ErrorHandler result='<%=result%>' field='<%=checkErrorType%>' id='errorMsg'>
	<%@ include file="/includes/i_error_messages.jspf" %>
</fd:ErrorHandler>

<fd:ErrorHandler result='<%=result%>' field='<%=checkAddressForm%>'>
<% String errorMsg = SystemMessageList.MSG_MISSING_INFO; %>
	<%@ include file="/includes/i_error_messages.jspf" %>	
</fd:ErrorHandler>

	<table width="<%= W_YA_EDIT_DELIVERY_ADDR %>" border="0" cellpadding="0" cellspacing="0">
		<tr><td width="<%= W_YA_EDIT_DELIVERY_ADDR %>" class="text11">
			<font class="title18">Your neighborhood has Unattended Delivery Service, <br />for no extra charge!</font><br />
		</td></tr>
	</table>
	<img src="/media_stat/images/layout/clear.gif" width="1" height="8" border="0"><br />
	<img src="/media_stat/images/layout/ff9933.gif" width="<%= W_YA_EDIT_DELIVERY_ADDR %>" height="1" border="0"><br />
	<img src="/media_stat/images/layout/clear.gif" width="1" height="8" border="0"><br /><br />

	<form name="address" method="post">
		<input type="hidden" name="updateShipToAddressId" value="<%=request.getParameter("addressId")%>">

		<font class="space4pix"><br /></font><%@ include file="/includes/ckt_acct/i_delivery_address_field.jspf" %><br /><br />
			<br /><br />
		
		<table cellpadding="0" cellspacing="0" border="0" width="<%= W_YA_EDIT_DELIVERY_ADDR %>">
			<tr valign="top" bgcolor="#ff9933">
				<td width="<%= W_YA_EDIT_DELIVERY_ADDR %>"><img src="/media_stat/images/layout/ff9933.gif" hspace="0" width="1" height="1" border="0"></td>
			</tr>
		<tr>
			<td width="<%= W_YA_EDIT_DELIVERY_ADDR %>"><br />
				<button class="cssbutton green small" name="edit_delivery_address">SUBMIT</button>
			</td>
		</tr>
		</table>
		<br />
	</form>
</fd:RegistrationController>
</tmpl:put>
</tmpl:insert>
