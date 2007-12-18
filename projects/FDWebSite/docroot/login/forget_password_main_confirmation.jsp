<%@ taglib uri='template' prefix='tmpl' %>
<%@ page import='com.freshdirect.fdstore.customer.*'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>	
<%@ taglib uri='freshdirect' prefix='fd' %>
<fd:CheckLoginStatus id="user" /> 
<tmpl:insert template='/common/template/no_site_nav.jsp'>
	<tmpl:put name='title' direct='true'>FreshDirect - ID Confirmed - Security Word Confirmed</tmpl:put>
		<tmpl:put name='content' direct='true'>
		
<TABLE BORDER="0" CELLSPACING="0" CELLPADDING="0" WIDTH="400">
        <TR VALIGN="TOP">
            <TD WIDTH="400" COLSPAN="3">
				<img src="/media_stat/images/template/site_access/thank_you_pswd.gif" width="72" height="9" alt="" border="0">
				<BR>
           		<IMG src="/media_stat/images/layout/999966.gif" VSPACE="3" HSPACE="0" WIDTH="400" HEIGHT="1" BORDER="0"><BR>
			</TD>
		</TR>
        <TR VALIGN="TOP">
            <TD WIDTH="400" COLSPAN="3" class="text13">
				
			Within a few minutes you will receive an e-mail from FreshDirect containing a link you can use to create 
			a new password. This link expires after one hour, so be sure to check your e-mail soon. If you do not 
			receive the message within one hour, please contact us at <b><%=user.getCustomerServiceContact()%></b>.
			<br><br>
			In the meantime, <a href="/index.jsp">click here</a> to browse the site. Just remember that you'll need to log in to
			view account information or to checkout.
			
			</TD>
		</TR>	
        <TR VALIGN="TOP">
            <TD WIDTH="400" COLSPAN="3">
				<img src="/media_stat/images/layout/clear.gif" width="1" height="20" alt="" border="0">
			</TD>
		</TR>	

</TABLE>

</tmpl:put>
</tmpl:insert>