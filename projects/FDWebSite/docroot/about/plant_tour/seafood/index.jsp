<%@ taglib uri='template' prefix='tmpl' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*'%>
<%@ taglib uri='freshdirect' prefix='fd' %>

<fd:CheckLoginStatus />
<% 
String deptName = "seafood";
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
<td><img src="/media_stat/images/template/about/plant_tour/seafood/seafood_header.gif" width="500" height="46" alt="A photographic tour of our Seafood Department"><br><img src="/media_stat/images/layout/clear.gif" width="1" height="4"></td>
</tr>

<tr bgcolor="#CCCCCC"><td><img src="/media_stat/images/layout/clear.gif" width="1" height="1"></td></tr>

<tr>
<td class="text12">
<div align="right"><a href="/category.jsp?catId=about_tour_produce"><img src="/media_stat/images/template/about/plant_tour/seafood/seafood_next.gif" width="103" height="16" alt="Next - Produce" vspace="6" border="0"></a></div><span class="text15"><font color="#666666"><b>Click on the images below to enlarge.</b></font></span><br>
<a href="<%=doLink1%>1<%=doLink2%>"><img src="/media_stat/images/template/about/plant_tour/seafood/seafood_p_1.jpg" width="126" height="158" alt="" vspace="10" border="0"></a>
<img src="/media_stat/images/layout/clear.gif" width="4" height="1">
<a href="<%=doLink1%>2<%=doLink2%>"><img src="/media_stat/images/template/about/plant_tour/seafood/seafood_p_2.jpg" width="126" height="158" alt="" vspace="10" border="0"></a>
<img src="/media_stat/images/layout/clear.gif" width="4" height="1">
<a href="<%=doLink1%>3<%=doLink2%>"><img src="/media_stat/images/template/about/plant_tour/seafood/seafood_p_3.jpg" width="126" height="158" alt="" vspace="10" border="0"></a>
<img src="/media_stat/images/layout/clear.gif" width="4" height="1">
<a href="<%=doLink1%>4<%=doLink2%>"><img src="/media_stat/images/template/about/plant_tour/seafood/seafood_p_4.jpg" width="126" height="158" alt="" vspace="10" border="0"></a>
<br>
From the moment your seafood is pulled live from the water until it is delivered to you, it is kept at 
35&ordm; or below. <b>Every day, fresh fish and shellfish arrives at FreshDirect</b> between 
2:00 and 4:00 a.m. It is inspected and immediately put on ice in a storage space kept at the 
optimal temperature for <b>preserving flavor and texture</b>.
<br><br>
<a href="<%=doLink1%>5<%=doLink2%>"><img src="/media_stat/images/template/about/plant_tour/seafood/seafood_p_5.jpg" width="126" height="158" alt="" vspace="10" border="0"></a>
<img src="/media_stat/images/layout/clear.gif" width="4" height="1">
<a href="<%=doLink1%>6<%=doLink2%>"><img src="/media_stat/images/template/about/plant_tour/seafood/seafood_p_6.jpg" width="126" height="158" alt="" vspace="10" border="0"></a>
<img src="/media_stat/images/layout/clear.gif" width="4" height="1">
<a href="<%=doLink1%>7<%=doLink2%>"><img src="/media_stat/images/template/about/plant_tour/seafood/seafood_p_7.jpg" width="126" height="158" alt="" vspace="10" border="0"></a>
<img src="/media_stat/images/layout/clear.gif" width="4" height="1">
<a href="<%=doLink1%>8<%=doLink2%>"><img src="/media_stat/images/template/about/plant_tour/seafood/seafood_p_8.jpg" width="126" height="158" alt="" vspace="10" border="0"></a>
<br>
When your order is received, a skilled fish cutter retrieves the fish and takes it to be scaled and 
cleaned. Next, our automated system specifies the quantity of steaks or filets and the portion size 
to be cut. <b>Your custom prepared fish</b> is then sent to be  packaged, weighed, and 
priced before being sent to join the rest of your order.
<br><br><br>
</td>
</tr>

<tr>
<td bgcolor="#CCCCCC"><img src="/media_stat/images/layout/clear.gif" width="1" height="1"></td>
</tr>

<tr>
<td><div align="right"><a href="/category.jsp?catId=about_tour_produce"><img src="/media_stat/images/template/about/plant_tour/seafood/seafood_next.gif" width="103" height="16" alt="Next - Produce" vspace="6" border="0"></a></div></td>
</tr>
</table>
</tmpl:put>
</tmpl:insert>