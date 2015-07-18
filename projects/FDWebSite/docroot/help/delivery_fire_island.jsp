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
final int W_DELIVERY_FIRE_ISLAND_TOTAL = 970;
final int W_DELIVERY_FIRE_ISLAND_LEFT = 724;
final int W_DELIVERY_FIRE_ISLAND_CENTER = 55;
final int W_DELIVERY_FIRE_ISLAND_RIGHT = 191;
%>

<fd:CheckLoginStatus id="user" />

<tmpl:insert template='/common/template/delivery_info_nav.jsp'>
	<tmpl:put name="seoMetaTag" direct="true">
		<fd:SEOMetaTag pageId="delivery_fire_island"></fd:SEOMetaTag>
	</tmpl:put>
	<tmpl:put name='title' direct='true'>Delivery Information</tmpl:put>
		<tmpl:put name='content' direct='true'>
<table width="<%=W_DELIVERY_FIRE_ISLAND_TOTAL%>" border="0" cellpadding="0" cellspacing="0">
     <tr>
          <td><img src="/media_stat/images/layout/clear.gif" width="<%=W_DELIVERY_FIRE_ISLAND_LEFT%>" height="18"></td>
          <td rowspan="2"><img src="/media_stat/images/layout/clear.gif" width="<%=(W_DELIVERY_FIRE_ISLAND_CENTER-1)/2%>" height="1"></td>
          <td><img src="/media_stat/images/layout/clear.gif" width="1" height="1"></td>
          <td rowspan="2"><img src="/media_stat/images/layout/clear.gif" width="<%=(W_DELIVERY_FIRE_ISLAND_CENTER-1)/2%>" height="1"></td>
          <td><img src="/media_stat/images/layout/clear.gif" width="<%=W_DELIVERY_FIRE_ISLAND_RIGHT%>" height="1"></td>
    </tr>
          
    <tr valign="top">
    <% boolean isPopup = false; %>
          <td class="text12"><%@ include file="/shared/includes/delivery/i_fire_island.jspf"%><br><br><br></td>
          <td bgcolor="#CCCCCC"><img src="/media_stat/images/layout/clear.gif" width="1" height="1"></td>
          <td align="center"><a href="http://www.pagelinx.com/fififerry/index.shtml" target="fi_sched"><img src="/media_stat/images/template/pickup/fi_learn_more.gif" width="146" height="31" border="0" alt="Learn more about Fire Island ferries!"><br><img src="/media_stat/images/template/pickup/fi_ferries.jpg" width="159" height="59" border="0" alt="Fire Island ferry" vspace="6"><br>Click here</a> to visit Fire Island Ferries online.<br><br>
<b>Passenger ferry schedules</b><br>
<a href="http://www.fireislandferries.com/beach.shtml" target="fi_sched">Ocean Beach</a><br>
<a href="http://www.fireislandferries.com/baypark.shtml" target="fi_sched">Ocean Bay Park</a><br>
<a href="http://www.fireislandferries.com/fairharbor.shtml" target="fi_sched">Fair Harbor</a><br>
<a href="http://www.fireislandferries.com/kismet.shtml" target="fi_sched">Kismet</a><br>
<a href="http://www.fireislandferries.com/seaview.shtml" target="fi_sched">Seaview</a><br>
<a href="http://www.fireislandferries.com/saltaire.shtml" target="fi_sched">Saltaire</a><br>
<a href="http://www.fireislandferries.com/dunewood.shtml" target="fi_sched">Dunewood</a><br>
<a href="http://www.fireislandferries.com/atlantique.shtml" target="fi_sched">Atlantique</a>
</td></tr>
	
</table>
</tmpl:put>
</tmpl:insert>