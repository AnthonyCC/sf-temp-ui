<%@ page import='com.freshdirect.webapp.util.*' %>
<%@ page import='java.util.List.*' %> 
<%@ page import='com.freshdirect.framework.webapp.*' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='bean' prefix='bean' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<fd:CheckLoginStatus guestAllowed="false" recognizedAllowed="false" />

<tmpl:insert template='/common/template/dnav.jsp'>
<tmpl:put name='title' direct='true'>FreshDirect - Your Account - Add New Delivery Address</tmpl:put>
<tmpl:put name='content' direct='true'>

<fd:RegistrationController actionName="addDeliveryAddress" result="result" successPage="/your_account/delivery_information.jsp">

<fd:ErrorHandler result='<%=result%>' field='<%=checkErrorType%>' id='errorMsg'>
	<%@ include file="/includes/i_error_messages.jspf" %>
</fd:ErrorHandler>

<fd:ErrorHandler result='<%=result%>' field='<%=checkAddressForm%>'>
<% String errorMsg = SystemMessageList.MSG_MISSING_INFO; %>
	<%@ include file="/includes/i_error_messages.jspf" %>	
</fd:ErrorHandler>

<TABLE WIDTH="675" BORDER="0" CELLPADDING="0" CELLSPACING="0">
<form name="address" method="post">
<TR>
<TD width="675" class="text11">
<font class="title18">Add New Delivery Address</font></td></tr>
</TABLE>
<IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="8" BORDER="0"><BR>
<IMG src="/media_stat/images/layout/ff9933.gif" WIDTH="675" HEIGHT="1" BORDER="0"><BR>
<IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="8" BORDER="0"><br><br>
<TABLE BORDER="0" CELLSPACING="0" CELLPADDING="2" WIDTH="675">
<TR VALIGN="TOP">
	<TD WIDTH="675">
		<img src="/media_stat/images/navigation/delivery_address.gif" WIDTH="116" HEIGHT="9" border="0" alt="DELIVERY ADDRESS">
		&nbsp;&nbsp;&nbsp;<FONT CLASS="text9">* Required information</FONT><BR>
		<IMG src="/media_stat/images/layout/999966.gif" WIDTH="675" HEIGHT="1" BORDER="0" VSPACE="3"><BR>
	</TD>
</TR>
</TABLE>
<%@ include file="/includes/ckt_acct/i_delivery_address_field.jspf" %><br><br>
<BR><BR>
<TABLE CELLPADDING="0" CELLSPACING="0" BORDER="0" WIDTH="675">
<TR VALIGN="TOP" BGCOLOR="#FF9933">
	<TD WIDTH="675" COLSPAN="2"><IMG src="/media_stat/images/layout/ff9933.gif" HSPACE="0" WIDTH="1" HEIGHT="1" BORDER="0"></TD>
</TR>
<TR>
	<TD WIDTH="375"></TD>
	<TD WIDTH="300" ALIGN="RIGHT" VALIGN="TOP"><FONT CLASS="space2pix"><BR></FONT><a href="<%=response.encodeURL("/your_account/delivery_information.jsp")%>"><image src="/media_stat/images/buttons/cancel.gif" height="16" width="54" alt="Cancel" border="0" hspace="4" vspace="4" name="cancel_delivery"></a><input type="image" src="/media_stat/images/buttons/save_address.gif"  alt="Save Address" border="0" hspace="4" vspace="4" name="edit_delivery_address"></td>
</TR>
<tr><td><img src="/media_stat/images/layout/clear.gif" width="1" height="10" alt="" border="0"></td></tr>
</form>
</table>

</fd:RegistrationController>
</tmpl:put>
</tmpl:insert>


