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
// setting up template parameters
Map params = new HashMap();
params.put("baseUrl", "");
params.put("helper", new MediaHelper());
if (user != null) {
    params.put("minCorpOrderAmount", new Integer((int)user.getMinCorpOrderAmount()) );
    params.put("corpDeliveryFee", new Double(user.getCorpDeliveryFee()) );
} else {
    params.put("minCorpOrderAmount", new Integer(0) );
    params.put("corpDeliveryFee", new Integer(0) );
}
%>
<tmpl:insert template='/common/template/delivery_info_nav.jsp'>
	<tmpl:put name='title' direct='true'>Delivery Information</tmpl:put>
	<tmpl:put name='content' direct='true'>
		<table width="693" border="0" cellpadding="0" cellspacing="0">
		     <tr>
		          <td><img src="/media_stat/images/layout/clear.gif" width="467" height="18"></td>
		          <td rowspan="3"><img src="/media_stat/images/layout/clear.gif" width="16" height="1"></td>
		          <td><img src="/media_stat/images/layout/clear.gif" width="1" height="1"></td>
		          <td rowspan="3"><img src="/media_stat/images/layout/clear.gif" width="16" height="1"></td>
		          <td><img src="/media_stat/images/layout/clear.gif" width="193" height="1"></td>
		    </tr>
		
		    <tr valign="top">
		        <td class="text12">
					<fd:IncludeMedia name="/media/editorial/site_pages/delivery_info/corp/main.ftl" parameters="<%=params%>" withErrorReport="true"/>
					<%@ include file="/survey/includes/cos.jsp" %>
					<br><br>
		        </td>
		        <td rowspan="2" bgcolor="#CCCCCC">&nbsp;</td>
				<td rowspan="2" align="center">
		            <fd:IncludeMedia name="/media/editorial/site_pages/delivery_info/corp/right.ftl" parameters="<%=params%>" withErrorReport="true"/>
		        </td>
		    </tr>
		</table>
    </tmpl:put>
</tmpl:insert>
