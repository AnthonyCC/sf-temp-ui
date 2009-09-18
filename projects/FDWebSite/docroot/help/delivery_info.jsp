<%@ page import='com.freshdirect.framework.webapp.*' %>
<%@ page import='com.freshdirect.fdstore.*' %>
<%@ page import='com.freshdirect.customer.ErpAddressModel'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ page import='com.freshdirect.customer.*' %>
<%@ page import='java.util.*' %>
<%@ page import='java.net.URLEncoder'%>
<%@ page import='java.text.DateFormat' %>
<%@ page import='java.text.SimpleDateFormat' %>
<%@ page import="com.freshdirect.webapp.util.MediaHelper" %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<fd:CheckLoginStatus id="user" />

<% String siteAccessPage = request.getParameter("siteAccessPage"); 
   String jspTemplate = null;
   if(siteAccessPage!=null && siteAccessPage.equalsIgnoreCase("delivery"))
	   jspTemplate = "/site_access/site_access.jsp";
   else
	   jspTemplate = "/common/template/delivery_info_nav.jsp";
   %>

<%
Map params = new HashMap();
params.put("baseUrl", "");
params.put("helper", new MediaHelper());
//--------OAS Page Variables-----------------------
request.setAttribute("sitePage", "www.freshdirect.com/help/delivery_info.jsp");
request.setAttribute("listPos", "SystemMessage,ZDeliveryRight");
%>
<tmpl:insert template='<%= jspTemplate %>'>
    <tmpl:put name='title' direct='true'>Delivery Information</tmpl:put>
    <tmpl:put name='content' direct='true'>
		<table border="0" cellpadding="0" cellspacing="0" width=<%=(siteAccessPage==null || !siteAccessPage.equalsIgnoreCase("delivery")) ? "693" : "550"%> >
		    <tr>
				<td><img src="/media_stat/images/layout/clear.gif" width=<%=(siteAccessPage==null || !siteAccessPage.equalsIgnoreCase("delivery")) ? "512" : "519"%> height="18"></td>
				<td rowspan="2"><img src="/media_stat/images/layout/clear.gif" width="10" height="1"></td>
				<td><img src="/media_stat/images/layout/clear.gif" width="1" height="1"></td>
				<td rowspan="2"><img src="/media_stat/images/layout/clear.gif" width="10" height="1"></td>
				<td><img src="/media_stat/images/layout/clear.gif" width=<%=(siteAccessPage!=null && !siteAccessPage.equalsIgnoreCase("delivery")) ? "160" : "10"%> height="1"></td>
		    </tr>
		    <tr valign="top">
		        <td class="text12"><img src="/media_stat/images/template/help/about_home.gif" width="306" height="13">
                    <fd:IncludeMedia name="/media/editorial/site_pages/delivery_info/home/main.ftl" parameters="<%=params%>" withErrorReport="true"/>		        
                </td>
		        <%if(siteAccessPage==null || !siteAccessPage.equalsIgnoreCase("delivery")){ %>
			        <td bgcolor="#CCCCCC"><img src="/media_stat/images/layout/clear.gif" width="1" height="1"></td>
			        <td align="center">
	                    <% if (FDStoreProperties.isAdServerEnabled()) { %>
			                <SCRIPT LANGUAGE=JavaScript>
	                        <!--
	                            OAS_AD('ZDeliveryRight');
	                        //-->
	      	                </SCRIPT><br><br>
	                	 <% } %>
	
			            <fd:IncludeMedia name="/media/editorial/site_pages/delivery_info/home/right.ftl" parameters="<%=params%>" withErrorReport="true"/>
			        </td>
		        <% } %>
		    </tr>
		    <tr>
		        <td colspan="5"><img src="/media_stat/images/layout/clear.gif" width="10" height="10"></td>
		    </tr>
		</table>
    </tmpl:put>
</tmpl:insert>
