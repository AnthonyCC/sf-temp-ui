<%@ page import='com.freshdirect.webapp.util.*' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='bean' prefix='bean' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<fd:ForgotPasswordController results="result" successPage='/login/forget_password_main_confirmation.jsp' password="password">	
<tmpl:insert template='/common/template/no_site_nav.jsp'>
	<tmpl:put name='title' direct='true'>FreshDirect - Forgot Your Password? - Enter E-mail Address</tmpl:put>
		<tmpl:put name='content' direct='true'>
		
<% String[] checkReminderForm = {"email_not_expired", "invalid_email", "email"}; %>
		
<fd:ErrorHandler result='<%=result%>' field='<%=checkReminderForm%>' id='errorMsg'>
	<%@ include file="/includes/i_error_messages.jspf" %>	
</fd:ErrorHandler>
		
		
			<TABLE BORDER="0" CELLSPACING="0" CELLPADDING="0" WIDTH="400">
	        <TR VALIGN="TOP">
	            <TD WIDTH="400" COLSPAN="3">
					<img src="/media_stat/images/template/site_access/forgot_your_pswd.gif" width="165" height="9" alt="" border="0">
					<BR>
	           		<IMG src="/media_stat/images/layout/999966.gif" VSPACE="3" HSPACE="0" WIDTH="400" HEIGHT="1" BORDER="0"><BR>
				</TD>
			</TR>
	        <TR VALIGN="TOP">
	            <TD WIDTH="400" COLSPAN="3" class="text13">
					Please enter your user name/e-mail address. We'll send you an e-mail with a link you can use to retrieve your password.
				</TD>
			</TR>	
	        <TR VALIGN="TOP">
	            <TD WIDTH="400" COLSPAN="3"><img src="/media_stat/images/layout/clear.gif" width="1" height="1" alt="" border="0"></TD>
			</TR>	
	        <TR VALIGN="TOP">
	            <TD WIDTH="400" COLSPAN="3">
				<%@ include file="/login/includes/i_forget_password.jspf" %>
				</TD>
			</TR>	
			</TABLE>

</tmpl:put>
</tmpl:insert>


</fd:ForgotPasswordController>