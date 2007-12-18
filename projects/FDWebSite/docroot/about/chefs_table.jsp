<%@ taglib uri='template' prefix='tmpl' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*'%>
<%@ taglib uri='freshdirect' prefix='fd' %>

<fd:CheckLoginStatus id="user" guestAllowed="false" />

<tmpl:insert template='/common/template/no_space_border.jsp'>
<tmpl:put name='title' direct='true'>FreshDirect - Chef's Table</tmpl:put>
<tmpl:put name='content' direct='true'>
<% if (user.isChefsTable()) { %>
<table cellpadding="0" cellspacing="0" border="0" width="695">
<tr><td colspan="5"><img src="/media_stat/images/layout/clear.gif" width="1" height="10"></td></tr>
<%-- main content row --%>
<tr>
<td class="text12">
<div align="center"><img src="/media_stat/images/template/about/chefs_table_welcome.gif" width="322" height="71" alt="Chef's Table - THANK YOU FOR BEING ONE OF OUR BEST CUSTOMERS!" vspace="10"></div><br>
Dear <%=user.getFirstName()%>, 
<br><br>
<b>Welcome to the FreshDirect Chef's Table.</b> Since we made our first delivery over two years ago, we have fulfilled over 1.5 million orders and are proud to recognize you as <b>one of our best customers.</b>
<br><br>
Your continued loyalty and support of FreshDirect is greatly valued, and as a token of our appreciation, we have created a program called <b>Chef's Table</b> to thank and reward our top customers.  As a Chef's Table member, you will be provided with exclusive FreshDirect services that include:
<ul>
<li><b>A Chef's Table only <a href="/your_account/reserve_timeslot.jsp">delivery timeslot reservation</a> service</b><br><br></li>

<li><b>An exclusive, dedicated Chef's Table customer support line:<br><%= user.getCustomerServiceContact() %></b><br><br></li>   

</ul>
Thank you again for your support, and we sincerely hope you enjoy these special services.
<br><br>
Best Regards,
<br><br>
Steve Michaelson<br>
President, FreshDirect<br><br><br>
</td>
<td>&nbsp;</td>
<td bgcolor="#CCCCCC"><img src="/media_stat/images/layout/clear.gif" width="1" height="1"></td>
<td>&nbsp;</td>
<td valign="top" class="text12">

<table width="200" cellpadding="0" cellspacing="0" border="0" align="center">
	<tr>
		<td colspan="2" class="text12">
		Exclusive <b>Chef's Table</b> services for
our best customers include:
<br><br>
		<a href="/your_account/reserve_timeslot.jsp"><img src="/media_stat/images/template/about/chefs_table_reserve_delivery.gif" width="167" height="12" border="0" vspace="8" alt="RESERVE DELIVERY"></a><br>Look for the new link in the<br><a href="/your_account/reserve_timeslot.jsp"><b>Your Account</b></a> section to place a standing, weekly reservation or to reserve a time <b>before</b> you place your order.<br><img src="/media_stat/images/layout/clear.gif" width="1" height="8">
		</td>
	</tr>
	<tr>
		<td class="text12">Reserve the time of your next delivery. <a href="/your_account/reserve_timeslot.jsp"><b>Click here</b></a></td>
		<td align="right"><a href="/your_account/reserve_timeslot.jsp"><img src="/media_stat/images/template/homepages/truck.gif" width="61" height="43" border="0"></a></td>
	</tr>
	<tr>
		<td colspan="2" class="text12"><br><br>
		<img src="/media_stat/images/template/about/chefs_table_customer_service.gif" width="200" height="28" border="0" vspace="8" alt="DEDICATED CHEF'S TABLE CUSTOMER SERVICE"><br>
		If you have any questions, comments, suggestions, or problems with your delivery or order, you may now call<br><b><%= user.getCustomerServiceContact() %></b> and speak to a dedicated Chef's Table customer representative.
		</td>
	</tr>
</table>
</td>
</tr>
<%-- spacers --%>
<tr>
<td><img src="/media_stat/images/layout/clear.gif" width="464" height="14"></td>
<td><img src="/media_stat/images/layout/clear.gif" width="10" height="14"></td>
<td><img src="/media_stat/images/layout/clear.gif" width="1" height="14"></td>
<td><img src="/media_stat/images/layout/clear.gif" width="10" height="14"></td>
<td><img src="/media_stat/images/layout/clear.gif" width="210" height="14"></td>
</tr>
</table>
<% } else { %>
<jsp:forward page="/index.jsp" />
<% } %>
</tmpl:put>
</tmpl:insert>