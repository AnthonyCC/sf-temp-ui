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
<fd:CheckLoginStatus id="user" />

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
            <br><br>
            <fd:IncludeMedia name="/media/editorial/site_pages/delivery_time_text.html"/>            
          <br><br>
				<fd:IncludeMedia name="/media/editorial/site_pages/delivery_info.html"/><br>
				You are under no obligation to tip but have the option of providing a nominal tip if you feel that you've received exceptional service.
		  <br><br>
          <b>Handled With Extra Care</b><br>Fresh foods need extra care in handling, and we do all the right things to make sure your food gets to you in top shape. After we prepare everything to fill your order, our trained packers carefully assemble it in sturdy boxes. We put the boxes straight into our own FreshDirect fridge/freeze trucks, keeping everything cool and dry until it reaches you.
          <br><img src="/media_stat/images/layout/clear.gif" width="1" height="18">
     <div align="center"><a href="javascript:popup('/help/faq_index.jsp?show=delivery','large')"><b>Learn More - Click here for our Home Delivery FAQs</b></a></div>
	 <br><br><br></td>
          <td bgcolor="#CCCCCC"><img src="/media_stat/images/layout/clear.gif" width="1" height="1"></td>

          <td align="center"><%@ include file="/shared/includes/delivery/i_pickup_promo.jspf"%></td></tr>
<tr><td colspan="5"><img src="/media_stat/images/layout/clear.gif" width="10" height="10"></td></tr>
</table>
</tmpl:put>
</tmpl:insert>

