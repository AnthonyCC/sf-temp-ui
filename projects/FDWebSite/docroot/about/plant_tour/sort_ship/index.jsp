<%@ taglib uri='template' prefix='tmpl' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*'%>
<%@ taglib uri='freshdirect' prefix='fd' %>

<fd:CheckLoginStatus />
<% 
String deptName = "sort_ship";
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
<td><img src="/media_stat/images/template/about/plant_tour/sort_ship/sort_ship_header.gif" width="568" height="46" alt="A photographic tour of our Sorting & Shipping Department"><br><img src="/media_stat/images/layout/clear.gif" width="1" height="4"></td>
</tr>

<tr bgcolor="#CCCCCC"><td><img src="/media_stat/images/layout/clear.gif" width="1" height="1"></td></tr>

<tr>
<td class="text12">
<div align="right"><a href="/category.jsp?catId=about_tour_safety"><img src="/media_stat/images/template/about/plant_tour/sort_ship/sort_ship_next.gif" width="126" height="16" alt="Next - Food Safety" vspace="6" border="0"></a></div><span class="text15"><font color="#666666"><b>Click on the images below to enlarge.</b></font></span><br>
<a href="<%=doLink1%>1<%=doLink2%>"><img src="/media_stat/images/template/about/plant_tour/sort_ship/sort_ship_p_1.jpg" width="126" height="158" alt="" vspace="10" border="0"></a>
<img src="/media_stat/images/layout/clear.gif" width="4" height="1">
<a href="<%=doLink1%>2<%=doLink2%>"><img src="/media_stat/images/template/about/plant_tour/sort_ship/sort_ship_p_2.jpg" width="126" height="158" alt="" vspace="10" border="0"></a>
<img src="/media_stat/images/layout/clear.gif" width="4" height="1">
<a href="<%=doLink1%>3<%=doLink2%>"><img src="/media_stat/images/template/about/plant_tour/sort_ship/sort_ship_p_3.jpg" width="126" height="158" alt="" vspace="10" border="0"></a>
<img src="/media_stat/images/layout/clear.gif" width="4" height="1">
<a href="<%=doLink1%>4<%=doLink2%>"><img src="/media_stat/images/template/about/plant_tour/sort_ship/sort_ship_p_4.jpg" width="126" height="158" alt="" vspace="10" border="0"></a>
<br>
<b>Customized products</b> travel along miles of conveyors to the sortation area.  A 
computerized sorting system (with a little human help) sorts items and <b>assembles them 
into complete orders</b>.
<br><br>
<a href="<%=doLink1%>5<%=doLink2%>"><img src="/media_stat/images/template/about/plant_tour/sort_ship/sort_ship_p_5.jpg" width="126" height="158" alt="" vspace="10" border="0"></a>
<img src="/media_stat/images/layout/clear.gif" width="4" height="1">
<a href="<%=doLink1%>6<%=doLink2%>"><img src="/media_stat/images/template/about/plant_tour/sort_ship/sort_ship_p_6.jpg" width="126" height="158" alt="" vspace="10" border="0"></a>
<img src="/media_stat/images/layout/clear.gif" width="4" height="1">
<a href="<%=doLink1%>7<%=doLink2%>"><img src="/media_stat/images/template/about/plant_tour/sort_ship/sort_ship_p_7.jpg" width="126" height="158" alt="" vspace="10" border="0"></a>
<img src="/media_stat/images/layout/clear.gif" width="4" height="1">
<a href="<%=doLink1%>8<%=doLink2%>"><img src="/media_stat/images/template/about/plant_tour/sort_ship/sort_ship_p_8.jpg" width="126" height="158" alt="" vspace="10" border="0"></a>
<br>
When a box is full, we arrange all the items inside, so they won't get damaged in transit. The 
boxes are sealed and conveyed to the shipping area. We scan each box to make sure it is in the 
right place before <b>packing it into a refrigerated truck</b>. Next stop: your door.
<br><br><br>
</td>
</tr>

<tr>
<td bgcolor="#CCCCCC"><img src="/media_stat/images/layout/clear.gif" width="1" height="1"></td>
</tr>

<tr>
<td><div align="right"><a href="/category.jsp?catId=about_tour_safety"><img src="/media_stat/images/template/about/plant_tour/sort_ship/sort_ship_next.gif" width="126" height="16" alt="Next - Food Safety" vspace="6" border="0"></a></div></td>
</tr>
</table>
</tmpl:put>
</tmpl:insert>