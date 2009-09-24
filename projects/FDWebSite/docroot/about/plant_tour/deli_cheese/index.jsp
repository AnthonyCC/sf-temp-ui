<%@ taglib uri='template' prefix='tmpl' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*'%>
<%@ taglib uri='freshdirect' prefix='fd' %>

<fd:CheckLoginStatus/>

<% String siteAccessPage = request.getParameter("siteAccessPage"); 
   String jspTemplate = null;
   if(siteAccessPage!=null && siteAccessPage.equalsIgnoreCase("tour"))
	   jspTemplate = "/site_access/site_access.jsp";
   else
	   jspTemplate = "/common/template/left_dnav.jsp";
   %>
<% 
String deptName = "deli_cheese";
int maxNum = 8;
String link = "/about/plant_tour/tour_popup.jsp?deptName="+ deptName + "&maxNum=" + maxNum + "&imgNum=";
String doLink1 = "javascript:pop('"+ link ;
String doLink2 = "',445,375);";
%>
<tmpl:insert template='<%= jspTemplate %>'>
<tmpl:put name='title' direct='true'>FreshDirect - About FreshDirect</tmpl:put>
<tmpl:put name='content' direct='true'>
<table cellpadding="0" cellspacing="0" border="0" width="568">
<tr>
<td><img src="/media_stat/images/layout/clear.gif" width="1" height="4"></td>
</tr>

<tr>
<td><img src="/media_stat/images/template/about/plant_tour/deli_cheese/deli_cheese_header.gif" width="472" height="46" alt="A photographic tour of our Deli & Cheese Department"><br><img src="/media_stat/images/layout/clear.gif" width="1" height="4"></td>
</tr>

<tr bgcolor="#CCCCCC"><td><img src="/media_stat/images/layout/clear.gif" width="1" height="1"></td></tr>

<tr>
<td class="text12">
<div align="right"><a href=<%=(siteAccessPage==null || !siteAccessPage.equalsIgnoreCase("tour")) ? "/category.jsp?catId=about_tour_kitchen" : "/about/plant_tour/kitchen/index.jsp?siteAccessPage=tour&catId=about_tour_kitchen"%>><img src="/media_stat/images/template/about/plant_tour/deli_cheese/deli_cheese_next.gif" width="97" height="16" alt="Next - Kitchen" vspace="6" border="0"></a></div><span class="text15"><font color="#666666"><b>Click on the images below to enlarge.</b></font></span><br>
<a href="<%=doLink1%>1<%=doLink2%>"><img src="/media_stat/images/template/about/plant_tour/deli_cheese/deli_cheese_p_1.jpg" width="126" height="158" alt="" vspace="10" border="0"></a>
<img src="/media_stat/images/layout/clear.gif" width="4" height="1">
<a href="<%=doLink1%>2<%=doLink2%>"><img src="/media_stat/images/template/about/plant_tour/deli_cheese/deli_cheese_p_2.jpg" width="126" height="158" alt="" vspace="10" border="0"></a>
<img src="/media_stat/images/layout/clear.gif" width="4" height="1">
<a href="<%=doLink1%>3<%=doLink2%>"><img src="/media_stat/images/template/about/plant_tour/deli_cheese/deli_cheese_p_3.jpg" width="126" height="158" alt="" vspace="10" border="0"></a>
<img src="/media_stat/images/layout/clear.gif" width="4" height="1">
<a href="<%=doLink1%>4<%=doLink2%>"><img src="/media_stat/images/template/about/plant_tour/deli_cheese/deli_cheese_p_4.jpg" width="126" height="158" alt="" vspace="10" border="0"></a>
<br>
We store fine cheeses and deli goods at <b>38&ordm; in optimal humidity levels to maintain 
quality</b> and minimize waste. When your order is received, product is pulled from storage 
and carted to the slicing stations.
<br><br>
<a href="<%=doLink1%>5<%=doLink2%>"><img src="/media_stat/images/template/about/plant_tour/deli_cheese/deli_cheese_p_5.jpg" width="126" height="158" alt="" vspace="10" border="0"></a>
<img src="/media_stat/images/layout/clear.gif" width="4" height="1">
<a href="<%=doLink1%>6<%=doLink2%>"><img src="/media_stat/images/template/about/plant_tour/deli_cheese/deli_cheese_p_6.jpg" width="126" height="158" alt="" vspace="10" border="0"></a>
<img src="/media_stat/images/layout/clear.gif" width="4" height="1">
<a href="<%=doLink1%>7<%=doLink2%>"><img src="/media_stat/images/template/about/plant_tour/deli_cheese/deli_cheese_p_7.jpg" width="126" height="158" alt="" vspace="10" border="0"></a>
<img src="/media_stat/images/layout/clear.gif" width="4" height="1">
<a href="<%=doLink1%>8<%=doLink2%>"><img src="/media_stat/images/template/about/plant_tour/deli_cheese/deli_cheese_p_8.jpg" width="126" height="158" alt="" vspace="10" border="0"></a>
<br>
<b>We slice meats thick, thin, or in-between</b>, cut smoked fish by hand into translucent 
slices, and slice or cube cheeses to order. Finally, each item is packaged, weighed, and priced 
before being sent to sortation.
<br><br><br>
</td>
</tr>

<tr>
<td bgcolor="#CCCCCC"><img src="/media_stat/images/layout/clear.gif" width="1" height="1"></td>
</tr>

<tr>
<td><div align="right"><a href=<%=(siteAccessPage==null || !siteAccessPage.equalsIgnoreCase("tour")) ? "/category.jsp?catId=about_tour_kitchen" : "/about/plant_tour/kitchen/index.jsp?siteAccessPage=tour&catId=about_tour_kitchen"%>><img src="/media_stat/images/template/about/plant_tour/deli_cheese/deli_cheese_next.gif" width="97" height="16" alt="Next - Kitchen" vspace="6" border="0"></a></div></td>
</tr>
</table>
</tmpl:put>
</tmpl:insert>