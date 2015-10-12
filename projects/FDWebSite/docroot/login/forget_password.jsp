<%@ page import='java.util.*' %>
<%@ page import='com.freshdirect.webapp.util.*' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='bean' prefix='bean' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<% //expanded page dimensions
final int W_FORGET_PASSWORD_TOTAL = 700;
%>

<fd:CheckLoginStatus guestAllowed='true' recognizedAllowed='true' />

<fd:ForgotPasswordController results="result" successPage='/login/forget_password_confirmation.jsp' password="password">	
<tmpl:insert template='/common/template/no_nav.jsp'>
	<tmpl:put name='title' direct='true'>FreshDirect - Forgot Your Password? - Enter E-mail Address</tmpl:put>
		<tmpl:put name='content' direct='true'>

<% String[] checkReminderForm = {"email_not_expired", "invalid_email", "email"}; %>

<fd:ErrorHandler result='<%=result%>' field='<%=checkReminderForm%>' id='errorMsg'>
	<%@ include file="/includes/i_error_messages.jspf" %>	
</fd:ErrorHandler>

			<TABLE BORDER="0" CELLSPACING="0" CELLPADDING="0" WIDTH="<%=W_FORGET_PASSWORD_TOTAL%>">
	        <TR VALIGN="TOP">
	            <TD WIDTH="<%=W_FORGET_PASSWORD_TOTAL%>" COLSPAN="3">
					<img src="/media_stat/images/template/site_access/forgot_your_pswd.gif" width="165" height="9" alt="Forgot Your Password?" border="0">
					<BR>
	           		<IMG src="/media_stat/images/layout/999966.gif" VSPACE="3" HSPACE="0" WIDTH="<%=W_FORGET_PASSWORD_TOTAL%>" HEIGHT="1" BORDER="0"><BR>
				</TD>
			</TR>
	        <TR VALIGN="TOP">
	            <TD WIDTH="<%=W_FORGET_PASSWORD_TOTAL%>" COLSPAN="3" class="text13">
					Please enter your user name/e-mail address. We'll send you an e-mail with a link you can use to retrieve your password.
				</TD>
			</TR>	
	        <TR VALIGN="TOP">
	            <TD WIDTH="<%=W_FORGET_PASSWORD_TOTAL%>" COLSPAN="3"><img src="/media_stat/images/layout/clear.gif" width="1" height="1" alt="" border="0"></TD>
			</TR>	
	        <TR VALIGN="TOP">
	            <TD WIDTH="<%=W_FORGET_PASSWORD_TOTAL%>" COLSPAN="3" class="text13">
				<%@ include file="/login/includes/i_forget_password.jspf" %>
				</TD>
			</TR>	
			</TABLE>
<TABLE BORDER="0" CELLPADDING="0" CELLSPACING="0" WIDTH="<%=W_FORGET_PASSWORD_TOTAL-10%>">
<TR VALIGN="TOP">
	<td WIDTH="<%=W_FORGET_PASSWORD_TOTAL-10%>" class="text13">
		<font class="text13bold">New Customer?</font><BR>
		<a href="javascript:popup('/help/delivery_zones.jsp','large')" class="text13">See if we deliver to your area</a>.<br><br>
	</TD>
</TR>
</TABLE><BR><BR><BR>
</tmpl:put>
</tmpl:insert>


</fd:ForgotPasswordController>
