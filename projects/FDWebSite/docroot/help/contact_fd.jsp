<%@ page import='com.freshdirect.webapp.util.*' %>
<%@ page import='com.freshdirect.fdstore.customer.*'%>
<%@ page import='java.text.*' %>
<%@ page import='com.freshdirect.fdstore.*' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.customer.*'%>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='bean' prefix='bean' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<fd:CheckLoginStatus />
<script language='javascript'>
<!--
function limitText(textArea, length) {
    if (textArea.value.length > length) {
        textArea.value = textArea.value.substr(0,length);
    }
}
-->
</script>
<%
	DateFormat dateFormatter = new SimpleDateFormat("MM/dd/yy");
	
	FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);
	FDIdentity identity  = user.getIdentity();
        ErpCustomerInfoModel cm = null;
        if(identity != null){
            cm = FDCustomerFactory.getErpCustomerInfo(identity);
	}

    
%>		

<%
int subjectIndex = -1;
String email = "";
String firstName = "";
String lastName = "";
String homePhone = "";
String homePhoneExt = "";
String workPhone = "";
String workPhoneExt = "";
String altPhone = "";
String altPhoneExt = "";
String body = "";

if (cm != null) {
	email = cm.getEmail();
	lastName = cm.getLastName();
	firstName = cm.getFirstName();
	homePhone = cm.getHomePhone()==null?"":cm.getHomePhone().getPhone();
	homePhoneExt = cm.getHomePhone()==null?"":cm.getHomePhone().getExtension();
	altPhone = cm.getOtherPhone()==null?"":cm.getOtherPhone().getPhone();
	altPhoneExt = cm.getOtherPhone()==null?"":cm.getOtherPhone().getExtension();
}

if(request.getParameter("subject")!=null){
	try {
		subjectIndex = Integer.parseInt(request.getParameter("subject"));
	} catch (NumberFormatException Ex){
		subjectIndex = 0;
	}
}
if(request.getParameter("email")!=null){
	email=request.getParameter("email");
}
if(request.getParameter("first_name")!=null){
	firstName=request.getParameter("first_name");
}
if(request.getParameter("last_name")!=null){
	lastName=request.getParameter("last_name");
}
if(request.getParameter("home_phone")!=null){
	homePhone=request.getParameter("home_phone");
}
if(request.getParameter("home_phone_ext")!=null){
	homePhoneExt=request.getParameter("home_phone_ext");
}
if(request.getParameter("work_phone")!=null){
	workPhone=request.getParameter("work_phone");
}
if(request.getParameter("work_phone_ext")!=null){
	workPhoneExt=request.getParameter("work_phone_ext");
}
if(request.getParameter("alt_phone")!=null){
	altPhone=request.getParameter("alt_phone");
}
if(request.getParameter("alt_phone_ext")!=null){
	altPhoneExt=request.getParameter("alt_phone_ext");
}
if(request.getParameter("message")!=null){
	body=request.getParameter("message");
}
%>

<fd:ContactFdController result="result" successPage='/help/contact_fd_thank_you.jsp'>

<tmpl:insert template='/common/template/dnav.jsp'>
    <tmpl:put name='title' direct='true'>FreshDirect - Help - Contact Us</tmpl:put>
    <tmpl:put name='content' direct='true'>
	
<fd:ErrorHandler result='<%=result%>' name='kana' id='errorMsg'>
	<%@ include file="/includes/i_error_messages.jspf" %>
</fd:ErrorHandler>

<% String[] checkContactForm = {"subject", "message", "email", "first_name", "last_name", "home_phone"}; %>

<fd:ErrorHandler result='<%=result%>' field='<%=checkContactForm%>'>
	<% String errorMsg = SystemMessageList.MSG_MISSING_INFO; %>
	<%@ include file="/includes/i_error_messages.jspf" %>	
</fd:ErrorHandler>

<fd:ErrorHandler result='<%=result%>' name='technical_difficulty' id='errorMsg'>
	<%@ include file="/includes/i_error_messages.jspf" %>	
</fd:ErrorHandler>

<table width="670" cellpadding="0" cellspacing="0" border="0">
<form method="post" name="contact_fd">
<input type="hidden" name="customerPK" value="<%= user.getIdentity() != null ? user.getIdentity().getErpCustomerPK():"" %>">		
<tr valign="TOP">
	<td width="670" class="text12">
		<font class="title18">Contact FreshDirect</font><br>
		FreshDirect Customer Service is standing by to answer your questions, seven days a week.  <b>We generally respond within 1 to 3 hours, during our business day.</b> Please select an order number and include as much specific information as possible to ensure a prompt
        response to your inquiry.
        
          <% if ("CA".equalsIgnoreCase(user.getDepotCode())) { %>
                <br>If you require further assistance other than what FreshDirect has provided, please contact the PeopleCall Center, <a href="http://peoplecallcenter.ca.com/ntlm.html" onMouseOver="javascript:window.status=''; return true;">click here</a>.         
          <% } %>
		<br><br>
		
		<%
		boolean loyaltyHelpContact = true;
		%>
		<%@ include file="/shared/help/i_loyalty_banner.jspf"%>

		<font class="space8pix"><br></font>
		<font class="space4pix"><br></font>
<%-- message --%>
		<table border="0" cellspacing="0" cellpadding="2" width="675">
		    <tr valign="TOP">
			    <td width="675"><img src="/media_stat/images/template/help/enter_message.gif" width="152" height="9" border="0" alt="ENTER YOUR MESSAGE">&nbsp;&nbsp;&nbsp;<font class="text9">* Required information</font><br>
				<img src="/media_stat/images/layout/999966.gif" width="675" height="1" border="0" VSPACE="3"><br>
				<font class="space4pix"><br></font></td>
			</tr>
		</table>

		<table border="0" cellspacing="0" cellpadding="0" width="675">
		    <tr valign="middle">
			    <td width="200" align="right" class="lineItems">* Subject:&nbsp;</td>
				<td>
				<select class="text12" name="subject">
				<option value="">Select Subject:</option>
				<logic:iterate id="subject" indexId="idx" collection="<%= ContactFdControllerTag.selections %>" type="com.freshdirect.webapp.taglib.fdstore.ContactFdControllerTag.Selection">
					<option value="<%= idx %>" <%= idx.intValue() == subjectIndex ? "selected" : "" %>><%= subject.getDescription() %></option>
				</logic:iterate>
				</select>
				&nbsp;(this will help us to address your inquiry faster)
				&nbsp;<fd:ErrorHandler result='<%=result%>' name='subject' id='errorMsg'><br><span class="text11rbold"><%=errorMsg%></span></fd:ErrorHandler>
				</td>
				
			</tr>
	
<%if (identity != null) {%>
<fd:OrderHistoryInfo id='orderHistoryInfo'>
	<%if(orderHistoryInfo.size() > 0){%>
			<tr><td><img src="/media_stat/images/layout/clear.gif" width="1" height="8" alt="" border="0"></td></tr>		
		    <tr>
			    <td width="200" align="right" class="lineItems">Order #:&nbsp;</td>
				<td>
				<select class="text12" name="salePK">
					<option value="">Select Order:</option>
					<logic:iterate id="orderInfo" indexId="idx" collection="<%= orderHistoryInfo %>" type="com.freshdirect.fdstore.customer.FDOrderInfoI">
					<% if (idx.intValue() == 5) break; %>
					<option value="<%= orderInfo.getErpSalesId() %>">#<%= orderInfo.getErpSalesId() %> - <%=orderInfo.getOrderStatus().getDisplayName()%> - <%= dateFormatter.format( orderInfo.getRequestedDate() ) %>
					</logic:iterate>
				</select>&nbsp;(optional)
				</td>
			</tr>			
	<%}%>		
</fd:OrderHistoryInfo>	
<%}%>		
		
		    <tr valign="middle">
			    <td></td>
			    <td class="lineItems">
				<br>
				* Please enter your message here:
				<textarea cols="60" rows="5" name="message" onKeyPress="limitText(this, 2048)" onChange="limitText(this, 2048)"><%=body%></textarea><br>
				<fd:ErrorHandler result='<%=result%>' name='message' id='errorMsg'><span class="text11rbold"><%=errorMsg%></span></fd:ErrorHandler>
				</td>
			</tr>
            <tr>
                <td></td>
                <td align="right"><br><A href="javascript:pop('/request_product.jsp',400,585);">Click here to request a product</A></td>
            </tr>
		</table>
		<br><br>

<%-- info --%>
<%if(identity == null){%>
                <table border="0" cellspacing="0" cellpadding="2" width="675">
                    <tr valign="TOP">
                        <td width="675"><img src="/media_stat/images/template/help/enter_contact_info.gif"" width="246" height="9" border="0" alt="ENTER YOUR CONTACT INFORMATION">&nbsp;&nbsp;&nbsp;<font class="text9">* Required information</font><br>
				<img src="/media_stat/images/layout/999966.gif" width="675" height="1" border="0" VSPACE="3"><br><font class="space4pix"><br></font>
			</td>
                    </tr>
                </table>

		<table border="0" cellspacing="0" cellpadding="0">
			<tr>
			    <td width="200" align="right" class="lineItems">* E-mail Address/User Name&nbsp;</td>
				<td><input type="text" class="text11" name="email" size="30" value="<%=email%>">&nbsp;<fd:ErrorHandler result='<%=result%>' name='email' id='errorMsg'><span class="text11rbold"><%=errorMsg%></span></fd:ErrorHandler></td>
			</tr>
                        <tr>
			    <td align="right" class="lineItems">* First Name&nbsp;</td>
				<td colspan="2"><input type="text" class="text11" name="first_name" size="21" value="<%=firstName%>">&nbsp;<fd:ErrorHandler result='<%=result%>' name='first_name' id='errorMsg'><span class="text11rbold"><%=errorMsg%></span></fd:ErrorHandler></td>
			</tr>
			<tr>
			    <td align="right" class="lineItems">* Last Name&nbsp;</td>
				<td colspan="2"><input type="text" class="text11" name="last_name" size="21" value="<%=lastName%>">&nbsp;<fd:ErrorHandler result='<%=result%>' name='last_name' id='errorMsg'><span class="text11rbold"><%=errorMsg%></span></fd:ErrorHandler> </td>
			</tr>
			<tr>
			    <td align="right" class="lineItems">Home Phone #&nbsp;</td>
				<td colspan="2"><input type="text" class="text11" name="home_phone" size="21" value="<%=homePhone%>" maxlength="15">&nbsp;
				    <font class="text9">Ext.</font> <input type="text" class="text9" name="home_phone_ext" size="4" value="<%=homePhoneExt%>" maxlength="6"></td>
			</tr>
			<tr>
			    <td align="right" class="lineItems">Work Phone #&nbsp;</td>
				<td colspan="2"><input type="text" class="text11" name="work_phone" size="21" value="<%=workPhone%>" maxlength="15">&nbsp;
				    <font class="text9">Ext.</font> <input type="text" class="text9" name="home_phone_ext" size="4" value="<%=workPhoneExt%>" maxlength="6"></td>
			</tr>
			<tr>
			    <td align="right" class="lineItems">Other Phone #&nbsp;</td>
				<td colspan="2"><input type="text" class="text11" name="alt_phone" size="21" value="<%=altPhone%>" maxlength="15">&nbsp;
				    <font class="text9">Ext.</font> <input type="text" class="text9" name="alt_phone_ext" size="4" value="<%=altPhoneExt%>" maxlength="6"></td>
			</tr>
		</table>
<%}else{%>
		<table border="0" cellspacing="0" cellpadding="2" width="675">
                    <tr valign="TOP">
                        <td width="675"><img src="/media_stat/images/template/help/review_contact_info.gif"" width="246" height="9" border="0" alt="REVIEW YOUR CONTACT INFORMATION">&nbsp;&nbsp;&nbsp;<br>
				<img src="/media_stat/images/layout/999966.gif" width="675" height="1" border="0" VSPACE="3"><br><font class="space4pix"><br></font>
			</td>
                    </tr>
		</table>
                
                <table border="0" cellspacing="0" cellpadding="2">
                    <tr>
                        <td width="200" align="right" class="lineItems" valign='bottom'><b>E-mail Address/User Name:&nbsp;</b></td>
                        <td valign='bottom'><%=email%></td>
                    </tr>
                    <tr>
                        <td align="right" class="lineItems" valign='bottom'><b>Name:&nbsp;</b></td>
                        <td valign='bottom'><%=firstName%>&nbsp;<%=lastName%></td>
                    </tr>
                    <tr>
                        <td colspan='2' align='center'><font class='text10'>(If this information is incorrect, <a href='/your_account/signin_information.jsp'>click here</a>!)</font></td>
                    </tr>
                </table>
<%}%>
		<br>
		<img src="/media_stat/images/layout/FF9933.gif" width="675" height="1" border="0" vspace="4">
		<table border="0" cellspacing="0" cellpadding="2" width="675">
			<tr valign="right">
				<td width="25%"></td>
				<td width="50%" align="center">
				<a href="/help/contact_fd.jsp?home_phone=&home_phone_ext=&alt_phone=&alt_phone_ext=&body="><img src="/media_stat/images/template/help/clear.gif" width="46" height="16" border="0" alt="CLEAR"></A>&nbsp;&nbsp;
				<input type="image" src="/media_stat/images/template/help/send_message.gif" width="90" height="16"></td>
				<td width="25%"></td>
			</tr>
		</table>
		<img src="/media_stat/images/layout/FF9933.gif" width="675" height="1" border="0" vspace="4">
	</td>
</tr>
</form>
</table>			
</tmpl:put>
</tmpl:insert>

</fd:ContactFdController>