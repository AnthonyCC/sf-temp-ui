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

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<% //expanded page dimensions
final int W_DELIVERY_HAMPTONS_TOTAL = 970;
final int W_DELIVERY_HAMPTONS_LEFT = 724;
final int W_DELIVERY_HAMPTONS_CENTER = 55;
final int W_DELIVERY_HAMPTONS_RIGHT = 191;
%>

<fd:CheckLoginStatus id="user" />

<tmpl:insert template='/common/template/delivery_info_nav.jsp'>
	<tmpl:put name='title' direct='true'>Delivery Information</tmpl:put>
		<tmpl:put name='content' direct='true'>
<table width="<%=W_DELIVERY_HAMPTONS_TOTAL%>" border="0" cellpadding="0" cellspacing="0">
     <tr>
          <td><img src="/media_stat/images/layout/clear.gif" width="<%=W_DELIVERY_HAMPTONS_LEFT%>" height="18"></td>
          <td rowspan="2"><img src="/media_stat/images/layout/clear.gif" width="<%=(W_DELIVERY_HAMPTONS_CENTER-1)/2%>" height="1"></td>
          <td><img src="/media_stat/images/layout/clear.gif" width="1" height="1"></td>
          <td rowspan="2"><img src="/media_stat/images/layout/clear.gif" width="<%=(W_DELIVERY_HAMPTONS_CENTER-1)/2%>" height="1"></td>
          <td><img src="/media_stat/images/layout/clear.gif" width="<%=W_DELIVERY_HAMPTONS_RIGHT%>" height="1"></td>
    </tr>
          
    <tr valign="top">
    <%
    boolean fromZipCheck = false;
    boolean isPopup = false;
    %>
          <td class="text12"><fd:IncludeMedia name="/media/editorial/hamptons/hamptons_service.html" /><br><br><br></td>
          <td bgcolor="#CCCCCC"><img src="/media_stat/images/layout/clear.gif" width="1" height="1"></td>
          <td align="center">
		  <fd:IncludeMedia name="/media/editorial/hamptons/hamptons_location.html" />
</td></tr>
</table>
</tmpl:put>
</tmpl:insert>