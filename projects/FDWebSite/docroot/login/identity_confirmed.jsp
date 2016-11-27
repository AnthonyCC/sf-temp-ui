<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='template' prefix='tmpl' %>
<tmpl:insert template='/common/template/no_site_nav.jsp'>
	<tmpl:put name='title' direct='true'>Login</tmpl:put>
		<tmpl:put name='content' direct='true'>
				
			
				<TABLE BORDER="0" CELLSPACING="0" CELLPADDING="0" WIDTH="400">
				        <TR VALIGN="TOP">
				            <TD WIDTH="400" COLSPAN="3">
								<img src="/media_stat/images/template/site_access/identity_confirmed.gif" width="130" height="9" alt="" border="0"><img src="/media_stat/images/layout/clear.gif" width="120" height="1" alt="" border="0">* required Information
								<BR>
				           		<IMG src="/media_stat/images/layout/999966.gif" VSPACE="3" HSPACE="0" WIDTH="400" HEIGHT="1" BORDER="0"><BR>
							</TD>
						</TR>
				        <TR VALIGN="TOP">
				            <TD WIDTH="400" COLSPAN="3">
								<font class="text9nb ">Your password is: <b>hfh3z343f</b>
								<font class="space4pix"><br><br></font>
								Please sign in to start shopping now
								</font>
							</TD>
						</TR>	
	
				        <TR VALIGN="TOP">
				            <TD WIDTH="400" COLSPAN="3">
								<%@ include file="/includes/i_login_field.jspf" %>
							</TD>
						</TR>	
				</TABLE>

		

</tmpl:put>
</tmpl:insert>

