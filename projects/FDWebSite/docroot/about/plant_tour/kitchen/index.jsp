<%@ taglib uri='template' prefix='tmpl' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*'%>
<%@ taglib uri='freshdirect' prefix='fd' %>

<fd:CheckLoginStatus />
<% 
String deptName = "kitchen";
int maxNum = 8;
String link = "/about/plant_tour/tour_popup.jsp?deptName="+ deptName + "&maxNum=" + maxNum + "&imgNum=";
String doLink1 = "javascript:pop('"+ link ;
String doLink2 = "',445,375);";
%>
<tmpl:insert template='/common/template/left_dnav.jsp'>
<tmpl:put name='title' direct='true'>FreshDirect - About FreshDirect</tmpl:put>
<tmpl:put name='content' direct='true'>
<table cellpadding="0" cellspacing="0" border="0" width="568">
<tr>
<td><img src="/media_stat/images/layout/clear.gif" width="1" height="4"></td>
</tr>

<tr>
<td><img src="/media_stat/images/template/about/plant_tour/kitchen/kitchen_header.gif" width="500" height="46" alt="A photographic tour of our Meat Department"><br><img src="/media_stat/images/layout/clear.gif" width="1" height="4"></td>
</tr>

<tr bgcolor="#CCCCCC"><td><img src="/media_stat/images/layout/clear.gif" width="1" height="1"></td></tr>

<tr>
<td class="text12">
<div align="right"><a href="/category.jsp?catId=about_tour_sorting"><img src="/media_stat/images/template/about/plant_tour/kitchen/kitchen_next.gif" width="97" height="16" alt="Next - Kitchen" vspace="6" border="0"></a></div><span class="text15"><font color="#666666"><b>Click on the images below to enlarge.</b></font></span><br>
<a href="<%=doLink1%>1<%=doLink2%>"><img src="/media_stat/images/template/about/plant_tour/kitchen/kitchen_p_1.jpg" width="126" height="158" alt="" vspace="10" border="0"></a>
<img src="/media_stat/images/layout/clear.gif" width="4" height="1">
<a href="<%=doLink1%>2<%=doLink2%>"><img src="/media_stat/images/template/about/plant_tour/kitchen/kitchen_p_2.jpg" width="126" height="158" alt="" vspace="10" border="0"></a>
<img src="/media_stat/images/layout/clear.gif" width="4" height="1">
<a href="<%=doLink1%>3<%=doLink2%>"><img src="/media_stat/images/template/about/plant_tour/kitchen/kitchen_p_3.jpg" width="126" height="158" alt="" vspace="10" border="0"></a>
<img src="/media_stat/images/layout/clear.gif" width="4" height="1">
<a href="<%=doLink1%>4<%=doLink2%>"><img src="/media_stat/images/template/about/plant_tour/kitchen/kitchen_p_4.jpg" width="126" height="158" alt="" vspace="10" border="0"></a>
<br>
The <b>ingredients we use</b> in our kitchen come <b>directly from our own produce, 
meat, and seafood departments</b>. We create fine breads using artisanal methods. Unlike 
retail bakeries, we parbake and freeze our breads, bringing you access to a product that 
is generally only available wholesale.
<br><br>
<a href="<%=doLink1%>5<%=doLink2%>"><img src="/media_stat/images/template/about/plant_tour/kitchen/kitchen_p_5.jpg" width="126" height="158" alt="" vspace="10" border="0"></a>
<img src="/media_stat/images/layout/clear.gif" width="4" height="1">
<a href="<%=doLink1%>6<%=doLink2%>"><img src="/media_stat/images/template/about/plant_tour/kitchen/kitchen_p_6.jpg" width="126" height="158" alt="" vspace="10" border="0"></a>
<img src="/media_stat/images/layout/clear.gif" width="4" height="1">
<a href="<%=doLink1%>7<%=doLink2%>"><img src="/media_stat/images/template/about/plant_tour/kitchen/kitchen_p_7.jpg" width="126" height="158" alt="" vspace="10" border="0"></a>
<img src="/media_stat/images/layout/clear.gif" width="4" height="1">
<a href="<%=doLink1%>8<%=doLink2%>"><img src="/media_stat/images/template/about/plant_tour/kitchen/kitchen_p_8.jpg" width="126" height="158" alt="" vspace="10" border="0"></a>
<br>
<b>Every night our New York restaurant chefs cook meals</b> according to HACCP guidelines for safe food 
handling. From the oven, food is brought directly to the <b>blast chillers</b> - this 
<b>quickly arrests the cooking process, retarding bacterial growth</b>. We also use 
Modified Atmosphere Packaging (MAP) to preserve the food (without altering its composition) and 
<b>extend its shelf-life</b>.
<br><br><br>
</td>
</tr>

<tr>
<td bgcolor="#CCCCCC"><img src="/media_stat/images/layout/clear.gif" width="1" height="1"></td>
</tr>

<tr>
<td><div align="right"><a href="/category.jsp?catId=about_tour_sorting"><img src="/media_stat/images/template/about/plant_tour/kitchen/kitchen_next.gif" width="97" height="16" alt="Next - Shipping" vspace="6" border="0"></a></div></td>
</tr>
</table>
</tmpl:put>
</tmpl:insert>