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
          <td><img src="/media_stat/images/layout/clear.gif" width="467" height="18"></td>
          <td rowspan="3"><img src="/media_stat/images/layout/clear.gif" width="16" height="1"></td>
          <td><img src="/media_stat/images/layout/clear.gif" width="1" height="1"></td>
          <td rowspan="3"><img src="/media_stat/images/layout/clear.gif" width="16" height="1"></td>
          <td><img src="/media_stat/images/layout/clear.gif" width="193" height="1"></td>
    </tr>
          
    <tr valign="top">
          <td class="text12"><img src="/media_stat/images/template/help/about_cos.gif" width="291" height="13"><img src="/media_stat/images/template/help/cos_sandwich.jpg" width="108" height="70" align="right"><br><br>
           
			FreshDirect At The Office brings area businesses the same great, fresh food that's made FreshDirect indispensable for tens of thousands of New York households, with expanded product and service offerings especially for corporate clients, including: 
					<div class="text12">
					<ul>
					<li style="padding-bottom:5px; margin-left:-10px; margin-right:0px">Chef-prepared breakfast and luncheon catering platters perfect for business meetings;</li>
					<li style="padding-bottom:5px; margin-left:-10px; margin-right:0px">Popular brands of snacks, beverages, and pantry-stocking items;</li>
					<li style="padding-bottom:5px; margin-left:-10px; margin-right:0px">Delicious restaurant-quality individual meals;</li>
					<li style="padding-bottom:5px; margin-left:-10px; margin-right:0px">Catering services for upscale events;</li>
					<li style="padding-bottom:5px; margin-left:-10px; margin-right:0px">Convenient morning and afternoon delivery windows;</li>
					<li style="padding-bottom:5px; margin-left:-10px; margin-right:0px">Dedicated corporate account manager and customer service representatives.</li>
					</ul>
					</div>
		  We deliver as soon as the next day, every weekday from 7:00 a.m. to 3:00 p.m. When you check out, just select the time slot that's most convenient for you.
			<br><br>
			<b>Office Delivery Areas</b><img src="/media_stat/images/template/help/cos_crudites.jpg" width="130" height="93" align="right" hspace="3"><br>
			FreshDirect at the office is available in Manhattan. To see our current corporate delivery zones, <a href="javascript:popup('/help/delivery_zones.jsp','large')">click here</a>. <b>If you're interested but not yet in a delivery zone, <a href="#survey">let us know</a>.</b> 
			<br><br>
			<b>Office Delivery Fees</b><br>
			The minimum order is $<%=(int)user.getMinCorpOrderAmount()%>. Delivery costs just $<%=(double)user.getCorpDeliveryFee()%>. You are under no obligation to tip but have the option of providing a nominal tip if you feel that you've received exceptional service. 
			<br><br>
			<b>Handled With Extra Care</b><br>
			Fresh foods need extra care in handling, and we do all the right things to make sure your food gets to you in top shape. After we prepare everything to fill your order, our trained packers carefully assemble it in sturdy boxes. We put the boxes straight into our own FreshDirect fridge/freeze trucks, keeping everything cool and dry until it reaches you.
			<br><br>
			<div align="center"><a href="javascript:popup('/help/faq_index.jsp?show=cos','large')"><b>Learn More - Click here for our Office Delivery FAQs</b></a></div>
			<br><br>
			<a name="survey"></a><b>Interested? Let Us Know!</b><br>
			If you (or maybe someone you know) are interested in FreshDirect At The Office, please let us know by giving us the following information:
			<br><br>
			<%@ include file="/survey/includes/cos.jsp" %>
			<br><br>
          </td>
		  <td rowspan="2" bgcolor="#CCCCCC"></td>
		  <td rowspan="2" align="center"><img src="/media_stat/images/template/help/cos_delivery_zone.gif" width="168" height="25"><br><img src="/media_stat/images/template/help/cos_map.gif" width="193" height="729" vspace="8"></td>
    </tr>
</table>
</tmpl:put>
</tmpl:insert>
