<%@ page import='com.freshdirect.webapp.util.*' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='bean' prefix='bean' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<%
String emailAddress = request.getParameter("emailAddress");
%>
<fd:CheckLoginStatus id="user" /> 
<fd:ForgotPasswordController results="result" successPage='/login/forget_password_confirmation.jsp' password="password">	
<tmpl:insert template='/common/template/no_site_nav.jsp'>
	<tmpl:put name='title' direct='true'>FreshDirect - Forgot Your Password? - Enter Security Word</tmpl:put>
		<tmpl:put name='content' direct='true'>
		
		
<% if(password==null || password.equals("")){%>		
<TABLE BORDER="0" CELLSPACING="0" CELLPADDING="0" WIDTH="400">
        <TR VALIGN="TOP">
            <TD WIDTH="400" COLSPAN="3">
				<img src="/media_stat/images/template/site_access/forgot_your_pswd.gif" alt="" border="0">
				<BR>
           		<IMG src="/media_stat/images/layout/999966.gif" VSPACE="3" HSPACE="0" WIDTH="400" HEIGHT="1" BORDER="0"><BR>
			</TD>
		</TR>
        <TR VALIGN="TOP">
            <TD WIDTH="400" COLSPAN="3"  class="bodyCopy">
				To confirm your identity, please first enter your security word.  If you do not remember this word or get stuck, please contact us at <b><%=user.getCustomerServiceContact()%></b>.
			</TD>
		</TR>	
        <TR VALIGN="TOP">
            <TD WIDTH="400" COLSPAN="3">
				<img src="/media_stat/images/layout/clear.gif" width="1" height="20" alt="" border="0">
			</TD>
		</TR>	
        <TR VALIGN="TOP">
            <TD WIDTH="400" COLSPAN="3">

					<fd:ErrorHandler result='<%=result%>' name='hint' id='errorMsg'>
						<%@ include file="/includes/i_error_messages.jspf" %>	
					</fd:ErrorHandler>					
					
					<fd:ErrorHandler result='<%=result%>' name='invalid_hint' id='errorMsg'>
						<%@ include file="/includes/i_error_messages.jspf" %>	
					</fd:ErrorHandler>
										
					<fd:ErrorHandler result='<%=result%>' name='numberOfAttempts' id='errorMsg'>
						<%@ include file="/includes/i_error_messages.jspf" %>	
					</fd:ErrorHandler>
				<%if(!result.hasError("numberOfAttempts")){%>
				<form name="lost_password" method="post">
					
					<table border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td class="bodyCopy">
							Town of birth or <br>
							Mother's Maiden Name
						</td>
						<td><img src="/media_stat/images/layout/clear.gif" width="10" height="1" alt="" border="0"></td>
						<td>
							<input type="text" class="text10" name="hint" size="20" maxlength="50">&nbsp;<input type="image" src="/media_stat/images/template/homepages/go_round.gif"" width="18" height="18" name="check_access" border="0" alt="GO" HSPACE="4">
						</td>
					</tr>
					</table>

				<input type="hidden" name="passStep" value="checkHint">
				<input type="hidden" name="email" value="<%=emailAddress%>">
				</form>				
				<%}%>		
			</TD>
		</TR>	
</TABLE>
<%}
else{
%>

<TABLE BORDER="0" CELLSPACING="0" CELLPADDING="0" WIDTH="400">
	        <TR VALIGN="TOP">
	            <TD WIDTH="400" COLSPAN="3">
						
					
					<form name="update_change_password" method="post" >
					<input type="hidden" name="passStep" value="changePassword">
					<input type="hidden" name="email" value="rfalck@freshdirect.com">
					
					<TABLE BORDER="0" CELLSPACING="0" CELLPADDING="2" WIDTH="400">
					<TR VALIGN="TOP">
						<TD WIDTH="400">
							<img src="/media_stat/images/navigation/change_your_password.gif" WIDTH="162" HEIGHT="9" BORDER="0" alt="CHANGE YOUR PASSWORD" ALIGN="absbottom">&nbsp;&nbsp;&nbsp;&nbsp; <FONT CLASS="text9">* Required Information</FONT><BR>
							<IMG src="/media_stat/images/layout/cccccc.gif" WIDTH="400" HEIGHT="1" BORDER="0" VSPACE="3"><BR>
						</TD>
					</TR>
					</TABLE>
					<fd:ErrorHandler result='<%=result%>' name='password' id='errorMsg'><span class="text11rbold"><%=errorMsg%></span></fd:ErrorHandler>
					<fd:ErrorHandler result='<%=result%>' name='newPassword' id='errorMsg'><span class="text11rbold"><%=errorMsg%></span></fd:ErrorHandler>
					<fd:ErrorHandler result='<%=result%>' name='passwordMissmatch' id='errorMsg'><span class="text11rbold"><%=errorMsg%></span></fd:ErrorHandler><br>
					<TABLE BORDER="0" CELLSPACING="01" CELLPADDING="0" WIDTH="400">
						<TR VALIGN="top">
							<TD WIDTH="180" ALIGN="RIGHT" class="bodyCopy">*New Password&nbsp;&nbsp;</TD>
							<TD>
								<input CLASS="text9" SIZE="21" type="password" tabindex="4"  name="newPassword">
								<BR><font class="space4pix"><BR></font>
							</TD>
						</tr>
						<TR VALIGN="TOP">
							<TD WIDTH="180" ALIGN="RIGHT" class="bodyCopy">*Repeat New Password&nbsp;&nbsp;</TD>
							<TD colspan="2"><input type="password" tabindex="5" CLASS="text9" size="21" name="confirmNewPassword">
								<BR>
								Must be at least four characters.<br>
								Passwords are case-sensitive.<BR>
								<font class="space4pix"><BR></font>
								<input type="image" tabindex="7" name="update_password" src="/media_stat/images/buttons/save_changes.gif" WIDTH="84" HEIGHT="16"  alt="Save Changes" vspace="3" hspace="3" BORDER="0">
							</TD>
						</tr>
						
					</TABLE>
					</form>					
										
				</TD>
			</TR>	
	</TABLE>

<%}%>
</tmpl:put>
</tmpl:insert>
</fd:ForgotPasswordController>



