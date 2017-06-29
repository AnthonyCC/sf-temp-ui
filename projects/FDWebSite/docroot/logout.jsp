<%@page import="com.freshdirect.fdstore.FDStoreProperties"%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.webapp.util.*' %>
<%@ page import="java.util.Enumeration"%>
<%@ page import="java.net.URLEncoder"%>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='bean' prefix='bean' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<%

FDSessionUser user = (FDSessionUser) session.getAttribute(SessionName.USER);
if (user!=null) {
    user.touch();
    if(user.getSessionEvent()!=null)
    	user.getSessionEvent().setIsTimeout("N");
}

String lockout="";
if (session.getAttribute("verifyFail")!=null) {
	lockout=(String)session.getAttribute("verifyFail");
}	

// clear session
Enumeration<String> e = session.getAttributeNames();
while (e.hasMoreElements()) {
    String name = (String)e.nextElement();
    session.removeAttribute(name);
}
// end session
session.invalidate();
// remove cookie
CookieMonster.clearCookie(response);

String logoutPage ="";
boolean toSiteAccess = false;

if(request.getParameter("logoutPage")!= null){
	logoutPage = request.getParameter("logoutPage");
	//APPDEV-2448 only redirect to site_access page if IPLocator is disabled for the user
	toSiteAccess = !FDStoreProperties.isIpLocatorEnabled() || !new RequestClassifier(request).isInHashRange(FDStoreProperties.getIpLocatorRolloutPercent());
}


if (toSiteAccess) {
	//response.sendRedirect(response.encodeRedirectURL("/site_access/site_access.jsp?successPage=/index.jsp"));
	response.sendRedirect(response.encodeRedirectURL("/about/index.jsp?siteAccessPage=aboutus&successPage=/index.jsp"));
}

//else just go on to welcome, nothing here down is used.
//copied blog iframe to welcome.jsp so it gets called still
//response.sendRedirect("/welcome.jsp");
/* APPBUG-4674 */
response.sendRedirect("/index.jsp");
%>


<%-- <tmpl:insert template='/common/template/no_site_nav.jsp'>
    <tmpl:put name="seoMetaTag" direct='true'>
        <fd:SEOMetaTag title="FreshDirect - Logged Out"/>
    </tmpl:put>
	<tmpl:put name='title' direct='true'>FreshDirect - Logged Out</tmpl:put>
		<tmpl:put name='content' direct='true'>

			<TABLE BORDER="0" CELLSPACING="0" CELLPADDING="0" WIDTH="400">
	        <TR VALIGN="TOP">
	            <TD WIDTH="400" COLSPAN="3">
					<img src="/media_stat/images/template/site_access/thank_you_for_visting.gif" alt="" border="0">
					<BR>
	           		<IMG src="/media_stat/images/layout/999966.gif" ALT="" VSPACE="3" HSPACE="0" WIDTH="400" HEIGHT="1" BORDER="0"><BR>
				</TD>
			</TR>
	        <TR VALIGN="TOP">
	            
		    <% if (!"".equals(lockout)) {   %>
		    <td class="text11rbold" width="100%" bgcolor="#FFFFFF">
			<div style="padding: 3px 1px 3px 1px"><%= lockout %></div>
			
		    
		    <% } else { %>
		    <TD WIDTH="400" COLSPAN="3" class="text13">
					You are now logged out.  You will need to log in again to access Your Account or to Checkout.
		    <%}%>			
				</TD>
			</TR>	
	        <TR VALIGN="TOP">
	            <TD WIDTH="400" COLSPAN="3"><img src="/media_stat/images/layout/clear.gif" width="1" height="1" alt="" border="0"></TD>
			</TR>	
	        <TR VALIGN="TOP">
	            <TD WIDTH="400" COLSPAN="3" class="text13">
				<br>
				</TD>
			</TR>	
			</TABLE>
<TABLE BORDER="0" CELLPADDING="0" CELLSPACING="0" WIDTH="390">
<TR VALIGN="TOP">
	<td WIDTH="390" class="text13">
		<a href="/login/login_main.jsp">Click here</a> to log in.  <br>
		<br>
		<a href="/index.jsp">Click here</a> to return to FreshDirect without logging in.
	</TD>
</TR>
</TABLE><BR><BR><BR>
</tmpl:put>
</tmpl:insert> --%>