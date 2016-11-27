<%@ page import='com.freshdirect.webapp.util.*' %>
<%@ page import='java.util.List.*' %> 
<%@ page import='com.freshdirect.framework.webapp.*' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='bean' prefix='bean' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<% //expanded page dimensions
final int W_YA_ADD_DELIVERY_ADDR = 970;
%>
<fd:CheckLoginStatus guestAllowed="false" recognizedAllowed="false" />

<tmpl:insert template='/common/template/dnav.jsp'>
<tmpl:put name='title' direct='true'>FreshDirect - Your Account - Add New Delivery Address</tmpl:put>
<tmpl:put name='content' direct='true'>

<fd:RegistrationController actionName="addDeliveryAddress" result="result" successPage="/your_account/delivery_information.jsp">

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
	
	
	<form name="address" method="post" onSubmit="doubleSubmitAddrAdd=true;">
		<table width="<%= W_YA_ADD_DELIVERY_ADDR %>" border="0" cellpadding="0" cellspacing="0">
			<tr>
				<td width="<%= W_YA_ADD_DELIVERY_ADDR %>" class="title18">Add New Delivery Address</td>
			</tr>
		</table>
		
		<img src="/media_stat/images/layout/clear.gif" width="1" height="8" border="0" alt="" /><br />
		<img src="/media_stat/images/layout/ff9933.gif" width="<%= W_YA_ADD_DELIVERY_ADDR %>" height="1" border="0" alt="" /><br />
		<img src="/media_stat/images/layout/clear.gif" width="1" height="8" border="0" alt="" /><br /><br />
		
		<table border="0" cellspacing="0" cellpadding="2" width="<%= W_YA_ADD_DELIVERY_ADDR %>">
			<tr valign="top">
				<td width="<%= W_YA_ADD_DELIVERY_ADDR %>">
					<img src="/media_stat/images/navigation/delivery_address.gif" width="133" height="15" border="0" alt="DELIVERY ADDRESS" />
					&nbsp;&nbsp;&nbsp;<span class="text9">* Required information</span><br />
					<img src="/media_stat/images/layout/999966.gif" width="<%= W_YA_ADD_DELIVERY_ADDR %>" height="1" border="0" vspace="3" alt="" /><br />
				</td>
			</tr>
		</table>
		
		<%@ include file="/includes/ckt_acct/i_delivery_address_field.jspf" %><br /><br /><br /><br />
		
		<table cellpadding="0" cellspacing="0" border="0" width="<%= W_YA_ADD_DELIVERY_ADDR %>">
			<tr valign="top">
				<td width="<%= W_YA_ADD_DELIVERY_ADDR %>" colspan="2"><img src="/media_stat/images/layout/ff9933.gif" hspace="0" width="1" height="1" border="0"></TD>
			</tr>
			<tr>
				<td width="<%= W_YA_ADD_DELIVERY_ADDR %>" align="right" valign="top">
					<a class="cssbutton green transparent small" href="<%=response.encodeURL("/your_account/delivery_information.jsp")%>">CANCEL</a>
					<button class="cssbutton green small" name="edit_delivery_address">SAVE ADDRESS</button>
				</td>
			</tr>
			<tr>
				<td><img src="/media_stat/images/layout/clear.gif" width="1" height="10" alt="" border="0" /></td>
			</tr>
		</table>
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


