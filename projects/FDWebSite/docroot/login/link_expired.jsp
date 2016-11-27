<%@ page import='com.freshdirect.webapp.util.*' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='bean' prefix='bean' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<tmpl:insert template='/common/template/no_site_nav.jsp'>
	<tmpl:put name='title' direct='true'>Expired Link</tmpl:put>
		<tmpl:put name='content' direct='true'>
		
		

	<TABLE BORDER="0" CELLSPACING="0" CELLPADDING="0" WIDTH="500">
	        <TR VALIGN="TOP">
	            <TD WIDTH="400" COLSPAN="3">
					<!-- <img src="/media_stat/images/template/site_access/identity_confirmed.gif" width="130" height="9" alt="" border="0"><img src="/media_stat/images/layout/clear.gif" width="120" height="1" alt="" border="0"><FONT CLASS="text9">* Required Information</FONT>
 -->					<BR>
	           		<IMG src="/media_stat/images/layout/999966.gif" VSPACE="3" HSPACE="0" WIDTH="400" HEIGHT="1" BORDER="0"><BR>
				</TD>
			</TR>
	        <TR VALIGN="TOP">
	            <TD WIDTH="400" COLSPAN="3">
					Sorry, the link you are using has expired. <a href="/index.jsp">Click here</a> to browse the FreshDirect Web site. 
					
				</TD>
			</TR>	

	        <TR VALIGN="TOP">
	            <TD WIDTH="400" COLSPAN="3">
				


				</TD>
			</TR>	
	</TABLE>

</tmpl:put>
</tmpl:insert>

