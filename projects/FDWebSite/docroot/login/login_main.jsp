<%@ page import='com.freshdirect.fdstore.FDStoreProperties' %>

<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='template' prefix='tmpl' %>
<tmpl:insert template='/common/template/no_site_nav.jsp'>
	<tmpl:put name='title' direct='true'>FreshDirect - Log In</tmpl:put>
		<tmpl:put name='content' direct='true'>
			
		<TABLE BORDER="0" CELLSPACING="0" CELLPADDING="0" WIDTH="500">
		        <TR VALIGN="TOP">
		            <TD><img src="/media_stat/images/navigation/current_cust_log_in_now.gif" WIDTH="222" HEIGHT="13" border="0" alt="CURRENT CUSTOMERS LOG IN NOW">   <FONT CLASS="text9">* Required Information</FONT><BR><IMG src="/media_stat/images/layout/999966.gif" VSPACE="3" HSPACE="0" WIDTH="400" HEIGHT="1" BORDER="0"><BR></TD>
			</TR>
                        <tr><td><img src="/media_stat/images/layout/clear.gif" width="1" height="8"><br><%@ include file="/includes/i_login_field.jspf" %></td></tr>
                        <tr>
			<td class="text13">
		<font class="text13bold">Forgot your password?</FONT><BR>
		<font class="text13">
				<A HREF="/login/forget_password_main.jsp">Click here for help</a>
		</FONT>
		<br><br>
		<% 
		
		if( null != request.getParameter("successPage") && request.getParameter("successPage").toLowerCase().indexOf("gift_card") > 0 && FDStoreProperties.isGiftCardEnabled() ) { %>
			<font class="text13bold">NEW CUSTOMER OR OUTSIDE OUR DELIVERY AREA?</FONT><BR><BR>
			<A HREF='<%= response.encodeURL("/gift_card/purchase/register_and_purchase.jsp") %>'><font class="text13"><b>Click here</b> to continue with gift card purchase</font></a>.<br><br></td>
                <% } else { %>        
			<font class="text13bold">New Customer?</FONT><BR>
			<A HREF='<%= response.encodeURL("/about/index.jsp?siteAccessPage=aboutus&successPage=/index.jsp") %>'><font class="text13">See if we deliver to your area</font></a>.<br><br></td>
                <% } %>
                </tr>
</TABLE>
</tmpl:put>
</tmpl:insert>
