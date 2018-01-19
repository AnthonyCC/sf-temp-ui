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
  <tmpl:put name="seoMetaTag" direct='true'>
    <fd:SEOMetaTag title="FreshDirect - Your Account - Edit Delivery Address"/>
  </tmpl:put>
<%--   <tmpl:put name='title' direct='true'>FreshDirect - Your Account - Edit Delivery Address</tmpl:put> --%>
<tmpl:put name='content' direct='true'>
<fd:RegistrationController actionName="editDeliveryAddress" result="result" successPage="/your_account/delivery_information.jsp">
	<script>var doubleSubmitAddrAdd = false;</script>
	
	<fd:ErrorHandler result='<%=result%>' field='<%=checkErrorType%>' id='errorMsg'>
		<%@ include file="/includes/i_error_messages.jspf" %>
		<script>doubleSubmitAddrAdd = false;</script>
	</fd:ErrorHandler>

	<fd:ErrorHandler result='<%=result%>' field='<%=checkAddressForm%>'>
		<% String errorMsg = SystemMessageList.MSG_MISSING_INFO; %>
		<%@ include file="/includes/i_error_messages.jspf" %>
		<script>doubleSubmitAddrAdd = false;</script>
	</fd:ErrorHandler>

	<table width="<%= W_YA_EDIT_DELIVERY_ADDR %>" border="0" cellpadding="0" cellspacing="0">
		<tr><td width="<%= W_YA_EDIT_DELIVERY_ADDR %>" class="text11">
			<font class="title18">Edit Delivery Address</font><br />
		</td></tr>
	</table>
	<img src="/media_stat/images/layout/clear.gif" alt="" width="1" height="8" border="0"><br />
	<img src="/media_stat/images/layout/ff9933.gif" alt="" width="<%= W_YA_EDIT_DELIVERY_ADDR %>" height="1" border="0"><br />
	<img src="/media_stat/images/layout/clear.gif" alt="" width="1" height="8" border="0"><br /><br />

	<form fdform name="address" method="post" onSubmit="doubleSubmitAddrAdd=true;" fdform-displayerrorafter>
		<input type="hidden" name="updateShipToAddressId" value="<%=request.getParameter("addressId")%>">

		<table border="0" cellspacing="0" cellpadding="2" width="<%= W_YA_EDIT_DELIVERY_ADDR %>">
			<tr valign="top">
				<td width="<%= W_YA_EDIT_DELIVERY_ADDR %>"><img src="/media_stat/images/navigation/delivery_address.gif" width="133" height="15" border="0" alt="DELIVERY ADDRESS">
					&nbsp;&nbsp;&nbsp;<font class="text9">* Required information</font><br />
					<img src="/media_stat/images/layout/999966.gif" alt="" width="<%= W_YA_EDIT_DELIVERY_ADDR %>" height="1" border="0" vspace="3"><br /></td>
			</tr>
		</table>

		<font class="space4pix"><br /></font><%@ include file="/includes/ckt_acct/i_delivery_address_field.jspf" %><br /><br />
		<br /><br />

		<table cellpadding="0" cellspacing="0" border="0" width="<%= W_YA_EDIT_DELIVERY_ADDR %>">
			<tr valign="top" bgcolor="#ff9933">
				<td width="<%= W_YA_EDIT_DELIVERY_ADDR %>"><img src="/media_stat/images/layout/ff9933.gif" alt="" hspace="0" width="1" height="1" border="0"></td>
			</tr>
		<tr>
			<td width="<%= W_YA_EDIT_DELIVERY_ADDR %>" align="right" valign="middle"><br>
				<a class="cssbutton green transparent small" href="<%=response.encodeURL("/your_account/delivery_information.jsp")%>">CANCEL</a>
				<button class="cssbutton green small" name="edit_delivery_address">SAVE CHANGES</button>
			</td>
		</tr>
		</table>
		<br />
	</form>
	<script>
		$jq('input.edit_delivery_address').each( function(index, elem){
			$jq(elem).on('click', function(event) {
				if (doubleSubmitAddrAdd) { event.preventDefault(); }
			});
		});
	</script>
</fd:RegistrationController>
</tmpl:put>
</tmpl:insert>
