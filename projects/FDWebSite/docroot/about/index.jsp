<%@ taglib uri='template' prefix='tmpl' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*'%>
<%@ taglib uri='freshdirect' prefix='fd' %>

<fd:CheckLoginStatus />

<tmpl:insert template='/common/template/no_space_border.jsp'>
<tmpl:put name='title' direct='true'>FreshDirect - About Us</tmpl:put>
<tmpl:put name='content' direct='true'>
<%
if("dvlprs".equalsIgnoreCase(request.getParameter("catID"))){
%>
	<%@ include file="/includes/i_about_preamble.jspf"%>
<%
}
else{
%>
<table cellpadding="0" cellspacing="0" border="0" width="695">
<tr><td colspan="5"><img src="/media_stat/images/layout/clear.gif" width="1" height="10"></td></tr>
<%-- main content row --%>
<tr>
<td class="text12"><img src="/media_stat/images/template/about/tart.jpg" width="200" height="100" alt="">
<br><img src="/media_stat/images/layout/clear.gif" width="1" height="4"><br>
<img src="/media_stat/images/template/about/new_way_to_shop.gif" width="338" height="14" alt="FreshDirect is the new way to shop for food." vspace="4"><br>
We've hired New York's food experts, built the perfect environment for food and found the shortest distance from farms, dairies and fisheries to your table.
<br><img src="/media_stat/images/layout/clear.gif" width="1" height="8"><br>
We have all the irresistibly fresh food you could want, plus popular grocery brands for up to 25% less than supermarket prices and we bring it right to your door.
<br><br>
<img src="/media_stat/images/template/about/high_quality_low_price.gif" width="348" height="15" alt="Our Promise: higher quality at lower prices." vspace="4"><br>
<img src="/media_stat/images/template/about/lettuce.jpg" width="132" height="160" alt="" align="right">
Our food comes directly from farms, dairies and fisheries (not middlemen), so it's several days fresher and a lot less expensive when it gets to your table. Our fully refrigerated, state-of-the-art facility (minutes from Manhattan in Long Island City) lets us meet standards no retail store in the country can match. We follow USDA guidelines and the HACCP food safety system in all our fresh storage and production rooms. Since customers don't shop in our facility, we can maintain different environments for each type of food we sell. For example, we have seven different climates for handling produce, ensuring that the bananas are as happy as the potatoes.
<br><img src="/media_stat/images/layout/clear.gif" width="1" height="8"><br>
In addition, we prepare a lot of our fresh food to order right on the premises. We cut your meat to order. All our seafood is prepared in a chilly, sparklingly clean prep room. We have a bakery which produces nice crusty bread, rolls, and more exotic pastries to your exact order. We even prepare meals in separate hot and cold kitchens. As we say in our ads: "Made to order. Again and again."
<br><br>
<img src="/media_stat/images/template/about/satisfaction_guarantee.gif" width="430" height="15" alt="We back all this up with a 100% satisfaction guarantee." vspace="4"><br>
<a href="/category.jsp?catId=about_tour">Take a tour of our facility</a> if you'd like to get a closer view, and then come and shop with us. We have a lot of happy customers already, as you can see from some of the <a href="/about/testimonial.jsp?catId=about_test">testimonials</a> on the site, and we'd like you to become one. We're working hard to provide a unique service to all New Yorkers.
<br><br>
</td>
<td>&nbsp;</td>
<td bgcolor="#CCCCCC"><img src="/media_stat/images/layout/clear.gif" width="1" height="1"></td>
<td>&nbsp;</td>
<td valign="top" class="text12">
	<table cellpadding="0" cellspacing="0" border="0">
	<tr><td class="text12"><a href="/category.jsp?catId=about_tour"><img src="/media_stat/images/template/about/visit_our_facility.gif" width="160" height="11" alt="Visit our Facility" border="0" vspace="2"><br><img src="/media_stat/images/template/about/aboutus_visit_img_01.jpg" width="52" height="65" alt="" vspace="6" border="0"><img src="/media_stat/images/layout/clear.gif" width="4" height="1" border="0"><img src="/media_stat/images/template/about/aboutus_visit_img_02.jpg" width="52" height="65" alt="" vspace="6" border="0"><img src="/media_stat/images/layout/clear.gif" width="4" height="1" border="0"><img src="/media_stat/images/template/about/aboutus_visit_img_03.jpg" width="52" height="65" alt="" vspace="6" border="0"></a><br>
	See a photographic tour of our state-of-the-art food facility.
	<br><img src="/media_stat/images/layout/clear.gif" width="1" height="4"><br>
	<a href="/category.jsp?catId=about_tour"><b>Take the tour!</b></a><br><img src="/media_stat/images/layout/clear.gif" width="1" height="8">
	<br><img src="/media_stat/images/layout/cccccc.gif" width="200" height="1" vspace="10"><br>
	<a href="testimonial.jsp?catId=about_test"><img src="/media_stat/images/template/about/read_what.gif" width="200" height="26" alt="In the Press" border="0" vspace="8"><br><img src="/media_stat/images/template/about/thought_heart.gif" width="54" height="53" alt="In the Press" border="0" align="right"></a>
	Read what New Yorkers like yourself are saying about FreshDirect.<br><img src="/media_stat/images/layout/clear.gif" width="1" height="14"><br>
	<div align="center"><a href="/category.jsp?catId=about_test"><img src="/media_stat/images/template/about/crab_s.jpg" width="145" height="82" border="0"></a></div>
	<img src="/media_stat/images/layout/cccccc.gif" width="200" height="1" vspace="10"><br>
	<a href="/category.jsp?catId=about_press_recent"><img src="/media_stat/images/template/about/in_the_press.gif" width="109" height="11" alt="In the Press" border="0" vspace="8"></a><br>
	Read what they're saying about FreshDirect in local and national press.<br><img src="/media_stat/images/layout/clear.gif" width="1" height="12"><br>
	<img src="/media_stat/images/layout/cccccc.gif" width="200" height="1" vspace="10"><br>
	<fd:IncludeMedia name="/media/editorial/about/careers/main_intro.html" /></td></tr>
	</table>
</td>
</tr>
<%-- spacers --%>
<tr>
<td><img src="/media_stat/images/layout/clear.gif" width="470" height="14"></td>
<td><img src="/media_stat/images/layout/clear.gif" width="10" height="14"></td>
<td><img src="/media_stat/images/layout/clear.gif" width="1" height="14"></td>
<td><img src="/media_stat/images/layout/clear.gif" width="10" height="14"></td>
<td><img src="/media_stat/images/layout/clear.gif" width="203" height="14"></td>
</tr>
<%-- footer --%>
<tr>
<td colspan="5" bgcolor="#CCCCCC"><img src="/media_stat/images/layout/clear.gif" width="1" height="1"></td>
</tr>

<tr>
<td colspan="5" align="center" class="text12"><img src="/media_stat/images/layout/clear.gif" width="1" height="8"><br>Have more questions? Find the answers in our <a href="javascript:popup('/help/faq_index.jsp','large');">FAQ's</a>.<br><img src="/media_stat/images/layout/clear.gif" width="1" height="4"></td>
</tr>
</table>
<div align="center"><img src="/media_stat/images/template/about/abouthome_bottomstrip.jpg" width="738" height="46" vspace="6"></div>
<%}%>
</tmpl:put>
</tmpl:insert>