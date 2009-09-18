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
String deptName = "food_safety";
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
<td><img src="/media_stat/images/template/about/plant_tour/food_safety/food_safety_header.gif" width="459" height="46" alt="A photographic tour of our Food Safety Procedures"><br><img src="/media_stat/images/layout/clear.gif" width="1" height="4"></td>
</tr>

<tr bgcolor="#CCCCCC"><td><img src="/media_stat/images/layout/clear.gif" width="1" height="1"></td></tr>

<tr>
<td class="text12">
<div align="right"><a href=<%=(siteAccessPage==null || !siteAccessPage.equalsIgnoreCase("tour")) ? "/category.jsp?catId=about_tour_coffee" : "/about/plant_tour/coffee/index.jsp?siteAccessPage=tour&catId=about_tour_coffee"%>><img src="/media_stat/images/template/about/plant_tour/food_safety/food_safety_next.gif" width="93" height="16" alt="Next - Coffee" vspace="6" border="0"></a></div><span class="text15"><font color="#666666"><b>Click on the images below to enlarge.</b></font></span><br>
<a href="<%=doLink1%>1<%=doLink2%>"><img src="/media_stat/images/template/about/plant_tour/food_safety/food_safety_p_1.jpg" width="126" height="158" alt="" vspace="10" border="0"></a>
<img src="/media_stat/images/layout/clear.gif" width="4" height="1">
<a href="<%=doLink1%>2<%=doLink2%>"><img src="/media_stat/images/template/about/plant_tour/food_safety/food_safety_p_2.jpg" width="126" height="158" alt="" vspace="10" border="0"></a>
<img src="/media_stat/images/layout/clear.gif" width="4" height="1">
<a href="<%=doLink1%>3<%=doLink2%>"><img src="/media_stat/images/template/about/plant_tour/food_safety/food_safety_p_3.jpg" width="126" height="158" alt="" vspace="10" border="0"></a>
<img src="/media_stat/images/layout/clear.gif" width="4" height="1">
<a href="<%=doLink1%>4<%=doLink2%>"><img src="/media_stat/images/template/about/plant_tour/food_safety/food_safety_p_4.jpg" width="126" height="158" alt="" vspace="10" border="0"></a>
<br>
Temperature-controlled and entirely washable, our facility is kept <b>colder and cleaner than 
any retail environment</b>. Unlike retail food stores, we follow USDA guidelines and the 
HACCP food safety system in our fresh food storage and production areas, <b>exceeding the 
national standards</b> for food excellence.
<br><br>
<a href="<%=doLink1%>5<%=doLink2%>"><img src="/media_stat/images/template/about/plant_tour/food_safety/food_safety_p_5.jpg" width="126" height="158" alt="" vspace="10" border="0"></a>
<img src="/media_stat/images/layout/clear.gif" width="4" height="1">
<a href="<%=doLink1%>6<%=doLink2%>"><img src="/media_stat/images/template/about/plant_tour/food_safety/food_safety_p_6.jpg" width="126" height="158" alt="" vspace="10" border="0"></a>
<img src="/media_stat/images/layout/clear.gif" width="4" height="1">
<a href="<%=doLink1%>7<%=doLink2%>"><img src="/media_stat/images/template/about/plant_tour/food_safety/food_safety_p_7.jpg" width="126" height="158" alt="" vspace="10" border="0"></a>
<img src="/media_stat/images/layout/clear.gif" width="4" height="1">
<a href="<%=doLink1%>8<%=doLink2%>"><img src="/media_stat/images/template/about/plant_tour/food_safety/food_safety_p_8.jpg" width="126" height="158" alt="" vspace="10" border="0"></a>
<br>
In addition to our staff of seven food safety professionals, a full-time microbiologist runs our 
<b>onsite laboratory</b>. Every day the lab conducts tests to assess freshness and ensure the environment 
is free from pathogens, determines the spoilage factors of prepared foods and analyzes swabs to ensure the 
work surfaces are properly cleaned. All  fresh food areas are washed and <b>disinfected each day</b>.
<br><br><br>
</td>
</tr>

<tr>
<td bgcolor="#CCCCCC"><img src="/media_stat/images/layout/clear.gif" width="1" height="1"></td>
</tr>

<tr>
<td><div align="right"><a href="/category.jsp?catId=about_tour_coffee"><img src="/media_stat/images/template/about/plant_tour/food_safety/food_safety_next.gif" width="93" height="16" alt="Next - Coffee" vspace="6" border="0"></a></div></td>
</tr>
</table>
</tmpl:put>
</tmpl:insert>