<%@ taglib uri='template' prefix='tmpl' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*'%>
<%@ taglib uri='freshdirect' prefix='fd' %>

<fd:CheckLoginStatus />

<tmpl:insert template='/common/template/left_dnav.jsp'>
<tmpl:put name='title' direct='true'>FreshDirect - About FreshDirect</tmpl:put>
<tmpl:put name='content' direct='true'>
<table cellpadding="0" cellspacing="0" border="0" width="568">
<tr>
<td colspan="7"><img src="/media_stat/images/layout/clear.gif" width="1" height="4"></td>
</tr>
<tr>
<td colspan="7" align="center"><img src="/media_stat/images/template/about/plant_tour/photo_tour_hdr.gif" width="564" height="50" alt="A photographic tour of our State-of-the-art Food Facility"><br><img src="/media_stat/images/layout/clear.gif" width="1" height="10"></td>
</tr>
<tr>
<td colspan="7" class="bodyCopy">When you place an order with FreshDirect, it is custom prepared by a team of experts working in 
one of the <b>cleanest, most technologically advanced food processing facilities in the 
country</b>. That box of food delivered to your door is fresh off a truck that's come direct from 
our 300,000-square foot building in Long Island City. A temperature-controlled environment, 
rigorous safety standards, and automated systems ensure that you get <b>the freshest food, 
exactly the way you want it</b>.
<br><img src="/media_stat/images/layout/clear.gif" width="1" height="12"><br>
<img src="/media_stat/images/template/about/plant_tour/photo_tour_choose_dept.gif" width="568" height="17" alt="Choose a department below to start the tour!"><br><img src="/media_stat/images/layout/clear.gif" width="1" height="6"></td>
</tr>
<tr valign="top">
<td class="bodyCopy" align="center"><a href="/category.jsp?catId=about_tour_safety"><img src="/media_stat/images/template/about/plant_tour/tourhome_safety.jpg" width="120" height="120" alt="Food Safety Procedures" border="0" vspace="6"></a><br><a href="/category.jsp?catId=about_tour_safety"><b>Food Safety Procedures</b></a></td>
<td>&nbsp;</td>
<td class="bodyCopy" align="center">
<a href="/category.jsp?catId=about_tour_coffee"><img src="/media_stat/images/template/about/plant_tour/tourhome_coffee.jpg" width="120" height="120" alt="Coffee Department" border="0" vspace="6"></a><br><a href="/category.jsp?catId=about_tour_coffee"><b>Coffee Department</b></a></td>
<td>&nbsp;</td>
<td class="bodyCopy" align="center"><a href="/category.jsp?catId=about_tour_meat"><img src="/media_stat/images/template/about/plant_tour/tourhome_meat.jpg" width="120" height="120" alt="Meat Department" border="0" vspace="6"></a><br><a href="/category.jsp?catId=about_tour_meat"><b>Meat<br>Department</b></a></td>
<td>&nbsp;</td>
<td class="bodyCopy" align="center"><a href="/category.jsp?catId=about_tour_seafood"><img src="/media_stat/images/template/about/plant_tour/tourhome_seafood.jpg" width="120" height="120" alt="Seafood Department" border="0" vspace="6"></a><br><a href="/category.jsp?catId=about_tour_seafood"><b>Seafood Department</b></a></td>
</tr>
<tr>
<td colspan="7"><img src="/media_stat/images/layout/clear.gif" width="1" height="12"></td>
</tr>
<tr>
<td class="bodyCopy" align="center"><a href="/category.jsp?catId=about_tour_produce"><img src="/media_stat/images/template/about/plant_tour/tourhome_produce.jpg" width="120" height="120" alt="Produce Department" border="0" vspace="6"></a><br><a href="/category.jsp?catId=about_tour_produce"><b>Produce Department</b></a></td>
<td>&nbsp;</td>
<td class="bodyCopy" align="center"><a href="/category.jsp?catId=about_tour_delicheese"><img src="/media_stat/images/template/about/plant_tour/tourhome_deli.jpg" width="120" height="120" alt="Deli & Cheese Departments" border="0" vspace="6"></a><br><a href="/category.jsp?catId=about_tour_delicheese"><b>Deli & Cheese Departments</b></a></td>
<td>&nbsp;</td>
<td class="bodyCopy" align="center"><a href="/category.jsp?catId=about_tour_kitchen"><img src="/media_stat/images/template/about/plant_tour/tourhome_bakery_kitchen.jpg" width="120" height="120" alt="Kitchen & Bakery Departments" border="0" vspace="6"></a><br><a href="/category.jsp?catId=about_tour_kitchen"><b>Kitchen & Bakery Departments</b></a></td>
<td>&nbsp;</td>
<td class="bodyCopy" align="center"><a href="/category.jsp?catId=about_tour_sorting"><img src="/media_stat/images/template/about/plant_tour/tourhome_shipping.jpg" width="120" height="120" alt="Sorting & Shipping Process" border="0" vspace="6"></a><br><a href="/category.jsp?catId=about_tour_sorting"><b>Sorting & Shipping Process</b></a></td>
</tr>
<%-- spacers --%>
<tr>
<td><img src="/media_stat/images/layout/clear.gif" width="120" height="8"></td>
<td><img src="/media_stat/images/layout/clear.gif" width="29" height="8"></td>
<td><img src="/media_stat/images/layout/clear.gif" width="120" height="8"></td>
<td><img src="/media_stat/images/layout/clear.gif" width="30" height="8"></td>
<td><img src="/media_stat/images/layout/clear.gif" width="120" height="8"></td>
<td><img src="/media_stat/images/layout/clear.gif" width="29" height="8"></td>
<td><img src="/media_stat/images/layout/clear.gif" width="120" height="8"></td>
</tr>
</table>
</tmpl:put>
</tmpl:insert>