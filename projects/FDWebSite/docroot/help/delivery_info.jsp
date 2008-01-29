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
<%
Map params = new HashMap();
params.put("baseUrl", "");
params.put("helper", new MediaHelper());
%>
<tmpl:insert template='/common/template/delivery_info_nav.jsp'>
    <tmpl:put name='title' direct='true'>Delivery Information</tmpl:put>
    <tmpl:put name='content' direct='true'>
		<table width="693" border="0" cellpadding="0" cellspacing="0">
		    <tr>
				<td><img src="/media_stat/images/layout/clear.gif" width="512" height="18"></td>
				<td rowspan="2"><img src="/media_stat/images/layout/clear.gif" width="10" height="1"></td>
				<td><img src="/media_stat/images/layout/clear.gif" width="1" height="1"></td>
				<td rowspan="2"><img src="/media_stat/images/layout/clear.gif" width="10" height="1"></td>
				<td><img src="/media_stat/images/layout/clear.gif" width="160" height="1"></td>
		    </tr>
		          
		    <tr valign="top">
		        <td class="text12"><img src="/media_stat/images/template/help/about_home.gif" width="306" height="13">
                    <fd:IncludeMedia name="/media/editorial/site_pages/delivery_info/home/main.ftl" parameters="<%=params%>" withErrorReport="true"/>		        
                </td>
		        <td bgcolor="#CCCCCC"><img src="/media_stat/images/layout/clear.gif" width="1" height="1"></td>
		        <td align="center">
		            <fd:IncludeMedia name="/media/editorial/site_pages/delivery_info/home/right.ftl" parameters="<%=params%>" withErrorReport="true"/>
		        </td>
		    </tr>
		    <tr>
		        <td colspan="5"><img src="/media_stat/images/layout/clear.gif" width="10" height="10"></td>
		    </tr>
		</table>
    </tmpl:put>
</tmpl:insert>
