<%@ page import='java.util.*' %>
<%@ page import="com.freshdirect.fdstore.customer.FDCSContactHoursUtil" %>
<%@ page import="com.freshdirect.fdstore.customer.FDCSContactHours" %>
<%@ page import='com.freshdirect.fdstore.customer.*'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.fdstore.rollout.EnumRolloutFeature'%>
<%@ page import='com.freshdirect.fdstore.rollout.FeatureRolloutArbiter'%>
<%@ page import="com.freshdirect.webapp.util.JspMethods" %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri="http://jawr.net/tags" prefix="jwr" %>
<fd:CheckLoginStatus />
<%
FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);
List<FDCSContactHours> csHours = FDCSContactHoursUtil.getFDCSHours();

String overlay = "false";
if(request.getParameter("overlay")!=null){
	overlay=request.getParameter("overlay");
}

boolean mobWeb = FeatureRolloutArbiter.isFeatureRolledOut(EnumRolloutFeature.mobweb, user) && JspMethods.isMobile(request.getHeader("User-Agent"));
String pageTemplate = "/common/template/dnav.jsp";
if (mobWeb) {
	pageTemplate = "/common/template/mobileWeb.jsp"; //mobWeb template
	String oasSitePage = (request.getAttribute("sitePage") != null) ? request.getAttribute("sitePage").toString() : "www.freshdirect.com/help/contact_fd_thank_you.jsp";
	if (oasSitePage.startsWith("www.freshdirect.com/") && !oasSitePage.startsWith("www.freshdirect.com/mobileweb/")) {
		request.setAttribute("sitePage", oasSitePage.replace("www.freshdirect.com/", "www.freshdirect.com/mobileweb/")); //change for OAS	
	}
}

if ("true".equalsIgnoreCase(overlay)) {
	pageTemplate = "/common/template/no_nav_html5.jsp"; //instead of mobWeb template
}

%>

<tmpl:insert template='<%= pageTemplate %>'>
    <tmpl:put name="seoMetaTag" direct='true'>
        <fd:SEOMetaTag title="FreshDirect - THANK YOU"/>
    </tmpl:put>
<%--     <tmpl:put name='title' direct='true'>FreshDirect - Help</tmpl:put> --%>

    <tmpl:put name='extraCss' direct='true'>
    	<% if (mobWeb) { %>
    		<jwr:style src="/mobileweb.css" media="all" /><%-- mobileweb should be last, for overriding --%>
    	<% } %>
   	</tmpl:put>
   	
    <tmpl:put name='content' direct='true'>
	<br>
	<table role="presentation" border="0" cellspacing="0" cellpadding="2" style="width: <%= (mobWeb) ? "100%" : "675px" %>">
	    <tr valign="TOP">
			<td style="width: <%= (mobWeb) ? "100%" : "675px" %>" align="center" class="text13">
			<div style="font-size: 28px; font-face:Arial,Verdana,Helvetica; color: #FF9933; margin-bottom: 6px;"><b>THANK YOU!</b></div>
			<span class="text15"><b>We've received your message.</b></span>
			<br><font class="space8pix"><br></font>
			  <b> We generally respond within 1 to 3 hours, </b> during our business day.<br/><br/>
			 		 As a reminder, we are here:<br><br>
			 
			  <div align="center">
                            <table role="presentation">							
							<% for (int i=0; i<csHours.size(); i++ ) {
									FDCSContactHours csHour = (FDCSContactHours) csHours.get(i);
							%>
								<tr><td>
								<%=csHour.getDaysDisplay()%> : <%=csHour.getHoursDisplay()%>
								</td></tr>
							<% } %>							
                            </table>
                        </div>
			</td>
		</tr>
		<tr>
			<td>
			</td>
		</tr>
		<tr class="NOMOBWEB">
		    <td style="width: <%= (mobWeb) ? "100%" : "675px" %>" align="center">
				<br><a href="/index.jsp"<%= (("true".equalsIgnoreCase(overlay)) ? " onclick=\"FreshDirect.components.ifrPopup.close()\"" : "") %> onmouseover="swapImage('home_img','/media_stat/images/template/help/help_home_r.gif')" onmouseout="swapImage('home_img','/media_stat/images/template/help/help_home.gif')"><img src="/media_stat/images/template/help/help_home.gif" name="home_img" width="71" height="26" alt="return to home page" border="0"></a>
			</td>		
		</tr>
	</table><br><br>
</tmpl:put>
</tmpl:insert>
