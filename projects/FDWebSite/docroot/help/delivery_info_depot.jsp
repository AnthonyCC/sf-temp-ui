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
<% if (user.isDepotUser()) { %>
<table width="693" CELLPADDING="0" CELLSPACING="0" border="0">
		  	<tr valign="top">
		  		<td><IMG src="/media_stat/images/layout/clear.gif" width="693" HEIGHT="15" border="0"><BR><img src="/media_stat/images/template/depot/how_depot_delivery_works.gif" width="525" height="21" alt="" border="0"><br><FONT CLASS="space2pix"><BR></FONT></td>
		 	</tr>
			<tr bgcolor="#CCCCCC">
			<td><IMG src="/media_stat/images/layout/clear.gif" width="1" HEIGHT="1"></td>
			</tr>
			<tr>
			<td><IMG src="/media_stat/images/layout/clear.gif" width="1" HEIGHT="16" border="0"></td>
			</tr>
		  <tr>
		  	<td class="text12" valign="top"><img src="/media_stat/images/template/checkout/truck_about.gif" width="100" height="72" border="0" alt="FreshDirect Delivery truck" align="right" hspace="30" vspace="10"><img src="/media_stat/images/template/depot/depot_basics.gif" width="85" height="9" alt="" border="0"><br><img src="/media_stat/images/layout/clear.gif" width="1" height="5" alt="" border="0"><br>
				Now you can get our great food, huge selection, and low prices delivered to your work. Place your order one day to one week before your company's designated delivery day. On delivery day, just pull up your car to the FreshDirect truck, present your ID, and drive home with your goods.

<br><img src="/media_stat/images/layout/clear.gif" width="1" height="16"><br>

				<img src="/media_stat/images/template/depot/depot_delivery_charge.gif" width="155" height="9" alt="" border="0"><br>
				<img src="/media_stat/images/layout/clear.gif" width="1" height="5" alt="" border="0"><br>
				The minimum order is $40. There is a fee of $3.95 for depot delivery. You are under no obligation to tip but have the option of providing a nominal tip if you feel that you've received exceptional service.

<br><img src="/media_stat/images/layout/clear.gif" width="1" height="16"><br>

				<img src="/media_stat/images/template/checkout/handled_with_extra_care.gif" width="165" height="9" border="0" alt="HANDLED WITH EXtrA CARE" VSPACE="1"><BR>
				<img src="/media_stat/images/layout/clear.gif" width="1" height="5" alt="" border="0"><br>
				Fresh foods need extra care in handling, and we do all the right things to make sure your 
				food gets to you in top shape. After we prepare everything to fill your order, our trained 
				packers carefully assemble it in sturdy boxes. We put the boxes straight into our own 
				FreshDirect fridge/freeze trucks, keeping everything cool and dry until you pick it up.<br><br><br>
			</td>
	</tr>
</table>
<% } else { %>
<table width="693" border="0" cellpadding="0" cellspacing="0">
     <tr>
          <td><img src="/media_stat/images/layout/clear.gif" width="512" height="18"></td>
          <td rowspan="2"><img src="/media_stat/images/layout/clear.gif" width="10" height="1"></td>
          <td><img src="/media_stat/images/layout/clear.gif" width="1" height="1"></td>
          <td rowspan="2"><img src="/media_stat/images/layout/clear.gif" width="10" height="1"></td>
          <td><img src="/media_stat/images/layout/clear.gif" width="160" height="1"></td>
    </tr>
          
    <tr valign="top">
          <td class="text12"><img src="/media_stat/images/template/help/about_depot.gif" width="310" height="13"><br><img src="/media_stat/images/layout/clear.gif" width="1" height="14"><br><b>Corporate Depot Program</b><br>
          Available to corporations in the Tri-State area with 1000+ employees at a single location, this program is a great way for your company to provide employees with a convenient new way to shop for food.
          <br><br> 
          Here's how it works: Your employees sign up through the FreshDirect Web site and place orders anytime (we'll put your company's logo on the Web site too!). After work on the day of delivery, at a designated spot in the parking lot, employees will pull their car up to one of our refrigerated trucks and FreshDirect staff will put their order in the trunk. That's all there is to it -- you've saved your employees precious time and the food is fresher than the supermarket and better priced to boot.
          <br><br>
If you are interested in our corporate depot program, please e-mail us at <a href="mailto:service@freshdirect.com">service@freshdirect.com</a>
          <br><img src="/media_stat/images/layout/clear.gif" width="1" height="18"><br>
          <b>Special Event Depot Program</b><br>
From football games to concerts throughout the Tri-State area, we'll bring our fresh food wherever the action is. Through one of our special event depots, event attendees can use the FreshDirect Web site to place orders for all of their tailgate or picnic needs. On game day, their food will be waiting for them in the parking lot, in one of our refrigerated trucks. (Unfortunately, the truck's tailgate is not available.) 
          <br><br>
          If you are interested in arranging a special event depot, please e-mail us at <a href="mailto:service@freshdirect.com">service@freshdirect.com</a>
          <br><br><br></td>
          <td bgcolor="#CCCCCC"><img src="/media_stat/images/layout/clear.gif" width="1" height="1"></td>
          <td align="center"><%@ include file="/shared/includes/delivery/i_pickup_promo.jspf" %></td></tr>
<tr><td colspan="5"><img src="/media_stat/images/layout/clear.gif" width="10" height="10"></td></tr>
</table>
<% } %>
</tmpl:put>
</tmpl:insert>