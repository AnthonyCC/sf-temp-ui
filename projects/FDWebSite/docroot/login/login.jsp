<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ page import='com.freshdirect.fdstore.FDStoreProperties' %>
<% 
String template = "/common/template/no_nav.jsp";
	//diff nav for popup login
	//if ("popup".equals( request.getParameter("type") ))
boolean isPopup = false;
String sPage = request.getParameter("successPage").toLowerCase();
	if (sPage != null) {
		if (sPage.indexOf("type=popup") != -1){
			template = "/common/template/large_pop.jsp";
			isPopup = true;
		}else if ( sPage.indexOf("gift_card") > 0 && FDStoreProperties.isGiftCardEnabled() ) {
			template = "/common/template/giftcard.jsp";
		}else if ( sPage.indexOf("robin_hood") > 0 && FDStoreProperties.isRobinHoodEnabled() ) {
			template = "/common/template/robinhood.jsp";
		}
	}
%>
<fd:CheckLoginStatus/>
<tmpl:insert template='<%=template%>'>
<tmpl:put name='title' direct='true'>FreshDirect - Log In</tmpl:put>
<tmpl:put name='content' direct='true'>
<table border="0" cellspacing="0" cellpadding="0" width="500" align="center">
		<tr><td colspan="2"><img src="/media_stat/images/layout/clear.gif" width="1" height="20"></td></tr>
        <tr>
            <td width="300">
				<img src="/media_stat/images/navigation/current_cust_log_in_now.gif" width="222" height="13" border="0" alt="CURRENT CUSTOMERS LOG IN NOW"></td>
        	<td width="200" align="right"><font class="text9">* Required Information</font></td>
		</tr>
		<tr><td colspan="2"><img src="/media_stat/images/layout/clear.gif" width="1" height="4"></td></tr>
		<tr><td colspan="2" bgcolor="#999966"><img src="/media_stat/images/layout/clear.gif" width="1" height="1"></td></tr>
		<tr><td colspan="2"><img src="/media_stat/images/layout/clear.gif" width="1" height="14"></td></tr>
		<tr><td colspan="2">
		<table border="0" cellspacing="0" cellpadding="0" align="center">
		<tr>
			<td width="110"><img src="/media_stat/images/layout/clear.gif" width="110" height="1" border="0"><BR></td>
			<td class="text13">
		<%@ include file="/includes/i_login_field.jspf" %>
		<br>
		<% if (!isPopup) { %>
			<%if(uri.indexOf("quickshop")== -1){%>

				<font class="text13bold">Forgot your password?</FONT><BR>
				<font class="text13">
				<%if(uri.indexOf("main")> -1){%>
						<A HREF="/login/forget_password_main.jsp">Click here for help</a>
				<%}else{%>	
						<A HREF="/login/forget_password.jsp">Click here for help</a>
				<%}%>
				</FONT>
			<%}%>
		
		<br><br>
			<% if( null != request.getParameter("successPage")) { %>
				<% if(request.getParameter("successPage").toLowerCase().indexOf("gift_card") > 0 ) { %>
					<font class="text13bold">NEW TO FRESHDIRECT?</FONT><BR>
					<A HREF='<%= response.encodeURL("/gift_card/purchase/register_and_purchase.jsp") %>'><font class="text13"><b>Click here to continue</b></font></a><br><br></td>
				<% } else if(request.getParameter("successPage").toLowerCase().indexOf("robin_hood") > 0 ){ %>        
					<font class="text13bold">NEW TO FRESHDIRECT?</FONT><BR>
					<A HREF='<%= response.encodeURL("/robin_hood/register_purchase.jsp") %>'><font class="text13"><b>Click here to continue</b></font></a><br><br></td>
					
				<% }else{ %>
					<font class="text13bold">New Customer?</FONT><BR>
						<A HREF='<%= response.encodeURL("/about/index.jsp?siteAccessPage=aboutus&successPage=/index.jsp") %>'><font class="text13">See if we deliver to your area</font></a>.<% } %>
			<%} else { %>        
				<font class="text13bold">New Customer?</FONT><BR>
				<A HREF='<%= response.encodeURL("/about/index.jsp?siteAccessPage=aboutus&successPage=/index.jsp") %>'><font class="text13">See if we deliver to your area</font></a>.<% } %>
		<% } %><br><br>
			</td>
		</tr>
		</table>
		</td>
		</tr>
</table>
</tmpl:put>
</tmpl:insert>
