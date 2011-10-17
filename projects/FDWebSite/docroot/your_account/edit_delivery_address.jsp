<%@ page import='com.freshdirect.webapp.util.*' %>
<%@ page import='java.util.List.*' %>
<%@ page import="com.freshdirect.framework.util.NVL"%>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='bean' prefix='bean' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<% //expanded page dimensions
final int W_YA_EDIT_DELIVERY_ADDR = 970;
%>
<fd:CheckLoginStatus guestAllowed="false" recognizedAllowed="false" />
<%
	//stick the current id being edited into the session so we can check it back on delivery info
	session.setAttribute("lastEditedAddressId", (String)NVL.apply(request.getParameter("addressId"), ""));
%>
<tmpl:insert template='/common/template/dnav.jsp'>
<tmpl:put name='title' direct='true'>FreshDirect - Your Account - Edit Delivery Address</tmpl:put>
<tmpl:put name='content' direct='true'>
<fd:RegistrationController actionName="editDeliveryAddress" result="result" successPage="/your_account/delivery_information.jsp">

	<fd:ErrorHandler result='<%=result%>' field='<%=checkErrorType%>' id='errorMsg'>
		<%@ include file="/includes/i_error_messages.jspf" %>
	</fd:ErrorHandler>

	<fd:ErrorHandler result='<%=result%>' field='<%=checkAddressForm%>'>
		<% String errorMsg = SystemMessageList.MSG_MISSING_INFO; %>
		<%@ include file="/includes/i_error_messages.jspf" %>	
	</fd:ErrorHandler>

	<table width="<%= W_YA_EDIT_DELIVERY_ADDR %>" border="0" cellpadding="0" cellspacing="0">
		<tr><td width="<%= W_YA_EDIT_DELIVERY_ADDR %>" class="text11">
			<font class="title18">Edit Delivery Address</font><br />
		</td></tr>
	</table>
	<img src="/media_stat/images/layout/clear.gif" width="1" height="8" border="0"><br />
	<img src="/media_stat/images/layout/ff9933.gif" width="<%= W_YA_EDIT_DELIVERY_ADDR %>" height="1" border="0"><br />
	<img src="/media_stat/images/layout/clear.gif" width="1" height="8" border="0"><br /><br />

	<form name="address" method="post">
		<input type="hidden" name="updateShipToAddressId" value="<%=request.getParameter("addressId")%>">

		<table border="0" cellspacing="0" cellpadding="2" width="<%= W_YA_EDIT_DELIVERY_ADDR %>">
			<tr valign="top">
				<td width="<%= W_YA_EDIT_DELIVERY_ADDR %>"><img src="/media_stat/images/navigation/delivery_address.gif" width="133" height="15" border="0" alt="DELIVERY ADDRESS">
					&nbsp;&nbsp;&nbsp;<font class="text9">* Required information</font><br />
					<img src="/media_stat/images/layout/999966.gif" width="<%= W_YA_EDIT_DELIVERY_ADDR %>" height="1" border="0" vspace="3"><br /></td>
			</tr>
		</table>

		<font class="space4pix"><br /></font><%@ include file="/includes/ckt_acct/i_delivery_address_field.jspf" %><br /><br />
		<br /><br />

		<table cellpadding="0" cellspacing="0" border="0" width="<%= W_YA_EDIT_DELIVERY_ADDR %>">
			<tr valign="top" bgcolor="#ff9933">
				<td width="<%= W_YA_EDIT_DELIVERY_ADDR %>"><img src="/media_stat/images/layout/ff9933.gif" hspace="0" width="1" height="1" border="0"></td>
			</tr>
		<tr>
			<td width="<%= W_YA_EDIT_DELIVERY_ADDR %>" align="right" valign="top"><font class="space2pix"><br /></font><a href="<%=response.encodeURL("/your_account/delivery_information.jsp")%>"><image src="/media_stat/images/buttons/cancel.gif" height="16" width="54" alt="Cancel" border="0" hspace="4" vspace="4" name="cancel_delivery"></a><input type="image" src="/media_stat/images/buttons/save_changes.gif" height="16" width="84" alt="Save New Address" border="0" hspace="4" vspace="4" name="edit_delivery_address"></td>
		</tr>
		</table>
		<br />
	</form>
</fd:RegistrationController>
</tmpl:put>
</tmpl:insert>
