<%@ taglib uri='template' prefix='tmpl' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*'%>
<%@ taglib uri='freshdirect' prefix='fd' %>

<fd:CheckLoginStatus />
<% 
String deptName = "meat";
int maxNum = 12;
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
<td><img src="/media_stat/images/template/about/plant_tour/meat/meat_header.gif" width="500" height="46" alt="A photographic tour of our Meat Department"><br><img src="/media_stat/images/layout/clear.gif" width="1" height="4"></td>
</tr>

<tr bgcolor="#CCCCCC"><td><img src="/media_stat/images/layout/clear.gif" width="1" height="1"></td></tr>

<tr>
<td class="text12">
<div align="right"><a href="/category.jsp?catId=about_tour_seafood"><img src="/media_stat/images/template/about/plant_tour/meat/meat_next.gif" width="105" height="16" alt="Next - Seafood" vspace="6" border="0"></a></div><span class="text15"><font color="#666666"><b>Click on the images below to enlarge.</b></font></span><br>
<a href="<%=doLink1%>1<%=doLink2%>"><img src="/media_stat/images/template/about/plant_tour/meat/meat_p_1.jpg" width="126" height="158" alt="" vspace="10" border="0"></a>
<img src="/media_stat/images/layout/clear.gif" width="4" height="1">
<a href="<%=doLink1%>2<%=doLink2%>"><img src="/media_stat/images/template/about/plant_tour/meat/meat_p_2.jpg" width="126" height="158" alt="" vspace="10" border="0"></a>
<img src="/media_stat/images/layout/clear.gif" width="4" height="1">
<a href="<%=doLink1%>3<%=doLink2%>"><img src="/media_stat/images/template/about/plant_tour/meat/meat_p_3.jpg" width="126" height="158" alt="" vspace="10" border="0"></a>
<img src="/media_stat/images/layout/clear.gif" width="4" height="1">
<a href="<%=doLink1%>4<%=doLink2%>"><img src="/media_stat/images/template/about/plant_tour/meat/meat_p_4.jpg" width="126" height="158" alt="" vspace="10" border="0"></a>
<br>
<b>Large cuts of meat</b> are delivered in refrigerated trucks <b>directly from the 
slaughterhouse</b> &#151; ensuring your meat has been in continuous refrigeration from start to 
finish. When your order is received, skilled butchers break down and trim the large cuts into 
smaller, wholesale cuts.
<br><br>
<a href="<%=doLink1%>5<%=doLink2%>"><img src="/media_stat/images/template/about/plant_tour/meat/meat_p_5.jpg" width="126" height="158" alt="" vspace="10" border="0"></a>
<img src="/media_stat/images/layout/clear.gif" width="4" height="1">
<a href="<%=doLink1%>6<%=doLink2%>"><img src="/media_stat/images/template/about/plant_tour/meat/meat_p_6.jpg" width="126" height="158" alt="" vspace="10" border="0"></a>
<img src="/media_stat/images/layout/clear.gif" width="4" height="1">
<a href="<%=doLink1%>7<%=doLink2%>"><img src="/media_stat/images/template/about/plant_tour/meat/meat_p_7.jpg" width="126" height="158" alt="" vspace="10" border="0"></a>
<img src="/media_stat/images/layout/clear.gif" width="4" height="1">
<a href="<%=doLink1%>8<%=doLink2%>"><img src="/media_stat/images/template/about/plant_tour/meat/meat_p_8.jpg" width="126" height="158" alt="" vspace="10" border="0"></a>
<br>
These wholesale cuts are conveyed to our retail cutting stations. At the retail cutting 
stations, <b>butchers customize your meat to order</b> (boneless or bone-in, &frac12;' to 3' thick, 
vacuum- or standard-packed, with or without marinade, etc.).
<br><br>
<a href="<%=doLink1%>9<%=doLink2%>"><img src="/media_stat/images/template/about/plant_tour/meat/meat_p_9.jpg" width="126" height="158" alt="" vspace="10" border="0"></a>
<img src="/media_stat/images/layout/clear.gif" width="4" height="1">
<a href="<%=doLink1%>10<%=doLink2%>"><img src="/media_stat/images/template/about/plant_tour/meat/meat_p_10.jpg" width="126" height="158" alt="" vspace="10" border="0"></a>
<img src="/media_stat/images/layout/clear.gif" width="4" height="1">
<a href="<%=doLink1%>11<%=doLink2%>"><img src="/media_stat/images/template/about/plant_tour/meat/meat_p_11.jpg" width="126" height="158" alt="" vspace="10" border="0"></a>
<img src="/media_stat/images/layout/clear.gif" width="4" height="1">
<a href="<%=doLink1%>12<%=doLink2%>"><img src="/media_stat/images/template/about/plant_tour/meat/meat_p_12.jpg" width="126" height="158" alt="" vspace="10" border="0"></a>
<br>
Your <b>customized meat</b> is conveyed to the packaging area where it is <b>packaged, 
weighed and priced</b>. Finally, it is put on a conveyer belt to sortation where it will join the 
rest of your order.
<br><br><br>
</td>
</tr>

<tr>
<td bgcolor="#CCCCCC"><img src="/media_stat/images/layout/clear.gif" width="1" height="1"></td>
</tr>

<tr>
<td><div align="right"><a href="/category.jsp?catId=about_tour_seafood"><img src="/media_stat/images/template/about/plant_tour/meat/meat_next.gif" width="105" height="16" alt="Next - Seafood" vspace="6" border="0"></a></div></td>
</tr>
</table>
</tmpl:put>
</tmpl:insert>